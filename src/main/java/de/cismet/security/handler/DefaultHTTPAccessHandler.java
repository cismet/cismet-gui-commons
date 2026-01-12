/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;

import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;

import java.net.BindException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import de.cismet.commons.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.commons.security.AccessHandler.ACCESS_METHODS;
import de.cismet.commons.security.Tunnel;
import de.cismet.commons.security.TunnelStore;
import de.cismet.commons.security.exceptions.BadHttpStatusCodeException;
import de.cismet.commons.security.exceptions.CannotReadFromURLException;
import de.cismet.commons.security.exceptions.CredentialsNotAvailableException;
import de.cismet.commons.security.handler.ChainedCredentialsProvider;
import de.cismet.commons.security.handler.InteractiveCredentialsProvider;

import de.cismet.netutil.Proxy;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl, thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultHTTPAccessHandler extends HTTPBasedAccessHandler implements TunnelStore {

    //~ Static fields/initializers ---------------------------------------------

    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[] {
            ACCESS_METHODS.GET_REQUEST,
            ACCESS_METHODS.POST_REQUEST
        };
    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.HTTP;
    private static final String USER_AGENT_HEADER_KEY = "User-Agent";

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Tunnel tunnel = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultHTTPAccessHandler object.
     *
     * @param  proxy  DOCUMENT ME!
     */
    public DefaultHTTPAccessHandler(final Proxy proxy) {
        super(proxy);

//        AuthPolicy.registerAuthScheme(AuthPolicy.NTLM, JcifsNtlmScheme.class);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final ACCESS_METHODS method,
            final HashMap<String, String> options) throws Exception {
        final CloseableHttpClient client = getSecurityEnabledHttpClient(url);
        final StringBuilder parameter = new StringBuilder();
        final BufferedReader reader = new BufferedReader(requestParameter);

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }

        if (log.isDebugEnabled()) {
            log.debug("Access method: '" + method + "'."); // NOI18N
        }

        if ((tunnel != null)
                    && ((method == ACCESS_METHODS.GET_REQUEST) || (method == ACCESS_METHODS.POST_REQUEST)
                        || (method == ACCESS_METHODS.HEAD_REQUEST))
                    && tunnel.isResponsible(method, url.toString())) {
            return tunnel.doRequest(url, new StringReader(parameter.toString()), method, options);
        } else {
            HttpUriRequestBase request;

            switch (method) {
                case POST_REQUEST_NO_TUNNEL:
                case POST_REQUEST: {
                    final HttpPost httpMethod = new HttpPost(url.toString());
                    httpMethod.setEntity(new StringEntity(
                            parameter.toString(),
                            ContentType.TEXT_XML.withCharset(StandardCharsets.UTF_8)));                       // NOI18N
                    request = httpMethod;
                    break;
                }
                case GET_REQUEST_NO_TUNNEL:
                case GET_REQUEST: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP GET: '" + url.toString() + "?" + parameter + "'.");               // NOI18N
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No parameters specified. HTTP GET: '" + url.toString() + "'.");        // NOI18N
                        }
                    }
                    request = createGetOrHead(new HttpGet(url.toString()), parameter);
                    break;
                }
                case HEAD_REQUEST_NO_TUNNEL:
                case HEAD_REQUEST: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP HEAD: '" + url.toString() + "?" + parameter + "'.");              // NOI18N
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No parameters specified. HTTP HEAD: '" + url.toString() + "'.");       // NOI18N
                        }
                    }
                    request = createGetOrHead(new HttpHead(url.toString()), parameter);
                    break;
                }
                default: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("No method specified, switching to '" + ACCESS_METHODS.GET_REQUEST
                                        + "'. URI used: '"
                                        + url.toString() + "?" + parameter + "'.");                           // NOI18N
                        }
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No method specified, switching to '" + ACCESS_METHODS.GET_REQUEST
                                        + "'. No parameters specified. URI used: '" + url.toString() + "'."); // NOI18N
                        }
                    }
                    request = createGetOrHead(new HttpGet(url.toString()), parameter);
                }
            }

            if ((options != null) && !options.isEmpty()) {
                for (final Entry<String, String> option : options.entrySet()) {
                    request.addHeader(option.getKey(), option.getValue());
                }
            }

            final boolean hasBound = false;
            int bindExceptionCounter = 0;
            boolean authenticationTried = false;

            while (!hasBound) {
                try {
                    request.addHeader(USER_AGENT_HEADER_KEY, "wunda");

                    final ClassicHttpResponse response = client.executeOpen(null, request, null);

                    final int status = response.getCode();

                    // Redirect manuell (301 / 308)
                    if ((status == HttpStatus.SC_MOVED_PERMANENTLY)
                                || (status == HttpStatus.SC_PERMANENT_REDIRECT)) {
                        final Header location = response.getFirstHeader("location");

                        if ((location != null) && (location.getValue() != null)) {
                            return doRequest(new URL(location.getValue()), requestParameter, method, options);
                        }
                    }

                    switch (status) {
                        case HttpStatus.SC_UNAUTHORIZED: {
                            if (log.isInfoEnabled()) {
                                log.info(
                                    "HTTP status code from server: SC_UNAUTHORIZED ("
                                            + HttpStatus.SC_UNAUTHORIZED
                                            + ")."); // NOI18N
                            }
                            final ChainedCredentialsProvider cp = getCredentialProvider(url);

                            if (!authenticationTried && (cp != null)) {
                                final InteractiveCredentialsProvider interactiveCp =
                                    cp.getInteractiveCredentialsProvider();

                                if (interactiveCp != null) {
                                    String authHeader = "";
                                    final Header[] headers = response.getHeaders("WWW-Authenticate");

                                    for (final Header header : headers) {
                                        authHeader = header.getValue();
                                        break;
                                    }

                                    response.close();

                                    try {
                                        interactiveCp.askForCredentials(authHeader);
                                        continue;
                                    } catch (CredentialsNotAvailableException e) {
                                        authenticationTried = true;
                                    }
                                }
                            }

                            throw new CannotReadFromURLException(
                                "You are not authorized to access this URL.");
                        }
                        case HttpStatus.SC_OK: {
                            if (log.isDebugEnabled()) {
                                log.debug("HTTP status code from server: OK."); // NOI18N
                            }
                            if ((method == ACCESS_METHODS.HEAD_REQUEST)
                                        || (method == ACCESS_METHODS.HEAD_REQUEST_NO_TUNNEL)) {
                                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                final ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(response.getHeaders());
                                oos.flush();
                                oos.close();
                                final InputStream is = new ByteArrayInputStream(baos.toByteArray());
                                baos.close();
                                response.close();
                                return is;
                            } else {
                                final HttpEntity entity = response.getEntity();

                                if (entity == null) {
                                    return new ByteArrayInputStream(new byte[0]);
                                }

                                return new BufferedInputStream(new ResponseInputStream(entity.getContent(), response));
                            }
                        }
                        default: {
                            if (log.isDebugEnabled()) {
                                log.debug("Unhandled HTTP status code: " + status); // NOI18N
                            }
                            String body = "Cannot parse response entity";

                            try {
                                body = (response.getEntity() != null) ? EntityUtils.toString(response.getEntity()) : "";
                            } catch (ParseException e) {
                                // nothing to do
                            }

                            String uri = "invalid uri syntax";

                            try {
                                uri = request.getUri().toString();
                            } catch (URISyntaxException e) {
                                // nothing to do
                            }

                            throw new BadHttpStatusCodeException(
                                uri,
                                status,
                                response.getReasonPhrase(),
                                body);
                        }
                    }
                } catch (BindException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Catched Bind Exception. Will try again in 50 ms", e);
                    }

                    ++bindExceptionCounter;

                    if (bindExceptionCounter > 10) {
                        // prevent infinite loop
                        throw e;
                    }

                    Thread.sleep(50);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   request    DOCUMENT ME!
     * @param   parameter  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  URISyntaxException  DOCUMENT ME!
     */
    private HttpUriRequestBase createGetOrHead(final HttpUriRequestBase request, final StringBuilder parameter)
            throws URISyntaxException {
        if (parameter.length() > 0) {
            request.setUri(URI.create(request.getUri().toString() + "?" + parameter));
        }

        return request;
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        final CloseableHttpClient client = getSecurityEnabledHttpClient(url);
        final StringBuilder parameter = new StringBuilder();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(requestParameter));

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }

        final HttpPost request = new HttpPost(url.toString());
        request.setEntity(new StringEntity(
                parameter.toString(),
                ContentType.TEXT_XML.withCharset(StandardCharsets.UTF_8))); // NOI18N

        if ((options != null) && !options.isEmpty()) {
            for (final Entry<String, String> option : options.entrySet()) {
                request.addHeader(option.getKey(), option.getValue());
            }
        }

        final boolean hasBound = false;
        int bindExceptionCounter = 0;
        boolean authenticationTried = false;

        while (!hasBound) {
            try {
                request.addHeader(USER_AGENT_HEADER_KEY, "wunda");

                final ClassicHttpResponse response = client.executeOpen(null, request, null);

                final int status = response.getCode();

                // Redirect manuell (301 / 308)
                if ((status == HttpStatus.SC_MOVED_PERMANENTLY)
                            || (status == HttpStatus.SC_PERMANENT_REDIRECT)) {
                    final Header location = response.getFirstHeader("location");

                    if ((location != null) && (location.getValue() != null)) {
                        return doRequest(new URL(location.getValue()), requestParameter, options);
                    }
                }

                switch (status) {
                    case HttpStatus.SC_UNAUTHORIZED: {
                        if (log.isInfoEnabled()) {
                            log.info(
                                "HTTP status code from server: SC_UNAUTHORIZED ("
                                        + HttpStatus.SC_UNAUTHORIZED
                                        + ")."); // NOI18N
                        }

                        final ChainedCredentialsProvider cp = getCredentialProvider(url);

                        if (!authenticationTried && (cp != null)) {
                            final InteractiveCredentialsProvider interactiveCp = cp.getInteractiveCredentialsProvider();

                            if (interactiveCp != null) {
                                String authHeader = "";
                                final Header[] headers = response.getHeaders("WWW-Authenticate");

                                for (final Header header : headers) {
                                    authHeader = header.getValue();
                                    break;
                                }

                                response.close();

                                try {
                                    interactiveCp.askForCredentials(authHeader);
                                    continue;
                                } catch (CredentialsNotAvailableException e) {
                                    authenticationTried = true;
                                }
                            }
                        }

                        throw new CannotReadFromURLException(
                            "You are not authorized to access this URL.");
                    }
                    case HttpStatus.SC_OK: {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP status code from server: OK."); // NOI18N
                        }

                        final HttpEntity entity = response.getEntity();

                        if (entity == null) {
                            response.close();
                            return new ByteArrayInputStream(new byte[0]);
                        }

                        return new BufferedInputStream(new ResponseInputStream(entity.getContent(), response));
                    }
                    default: {
                        if (log.isDebugEnabled()) {
                            log.debug("Unhandled HTTP status code: " + status); // NOI18N
                        }
                        String body = "Cannot parse response entity";

                        try {
                            body = (response.getEntity() != null) ? EntityUtils.toString(response.getEntity()) : "";
                        } catch (ParseException e) {
                            // nothing to do
                        }

                        String uri = "invalid uri syntax";

                        try {
                            uri = request.getUri().toString();
                        } catch (URISyntaxException e) {
                            // nothing to do
                        }

                        throw new BadHttpStatusCodeException(
                            uri,
                            status,
                            response.getReasonPhrase(),
                            body);
                    }
                }
            } catch (BindException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Catched Bind Exception. Will try again in 50 ms", e);
                }

                ++bindExceptionCounter;

                if (bindExceptionCounter > 10) {
                    // prevent infinite loop
                    throw e;
                }

                Thread.sleep(50);
            }
        }
    }

    @Override
    public boolean isAccessMethodSupported(final ACCESS_METHODS method) {
        for (final ACCESS_METHODS curMethod : SUPPORTED_ACCESS_METHODS) {
            if (curMethod == method) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ACCESS_HANDLER_TYPES getHandlerType() {
        return ACCESS_HANDLER_TYPE;
    }

    @Override
    public Tunnel getTunnel() {
        return tunnel;
    }

    @Override
    public void setTunnel(final Tunnel tunnel) {
        this.tunnel = tunnel;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static class ResponseInputStream extends FilterInputStream {

        //~ Instance fields ----------------------------------------------------

        private final ClassicHttpResponse response;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ResponseInputStream object.
         *
         * @param  in        DOCUMENT ME!
         * @param  response  DOCUMENT ME!
         */
        public ResponseInputStream(final InputStream in, final ClassicHttpResponse response) {
            super(in);
            this.response = response;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void close() throws IOException {
            try {
                super.close();
            } finally {
                response.close();
            }
        }
    }
}
