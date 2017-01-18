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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;

import java.net.BindException;
import java.net.URL;

import java.util.HashMap;
import java.util.Map.Entry;

import de.cismet.commons.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.commons.security.AccessHandler.ACCESS_METHODS;
import de.cismet.commons.security.Tunnel;
import de.cismet.commons.security.TunnelStore;
import de.cismet.commons.security.exceptions.BadHttpStatusCodeException;
import de.cismet.commons.security.exceptions.CannotReadFromURLException;

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

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Tunnel tunnel = null;

    //~ Methods ----------------------------------------------------------------

    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final ACCESS_METHODS method,
            final HashMap<String, String> options) throws Exception {
        final HttpClient client = getSecurityEnabledHttpClient(url);
        final StringBuilder parameter = new StringBuilder();
        final BufferedReader reader = new BufferedReader(requestParameter);

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }

        HttpMethod httpMethod;

        if (log.isDebugEnabled()) {
            log.debug("Access method: '" + method + "'."); // NOI18N
        }

        if ((tunnel != null)
                    && ((method == ACCESS_METHODS.GET_REQUEST) || (method == ACCESS_METHODS.POST_REQUEST)
                        || (method == ACCESS_METHODS.HEAD_REQUEST))
                    && tunnel.isResponsible(method, url.toString())) {
            return tunnel.doRequest(url, new StringReader(parameter.toString()), method, options);
        } else {
            switch (method) {
                case POST_REQUEST_NO_TUNNEL:
                case POST_REQUEST: {
                    httpMethod = new PostMethod(url.toString());
                    ((PostMethod)httpMethod).setRequestEntity(new StringRequestEntity(
                            parameter.toString(),
                            "text/xml",
                            "UTF-8"));                                                          // NOI18N
                    break;
                }
                case GET_REQUEST_NO_TUNNEL:
                case GET_REQUEST: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP GET: '" + url.toString() + "?" + parameter + "'."); // NOI18N
                        }

                        httpMethod = new GetMethod(url.toString() + "?" + parameter);                  // NOI18N
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No parameters specified. HTTP GET: '" + url.toString() + "'."); // NOI18N
                        }

                        httpMethod = new GetMethod(url.toString());
                    }
                    break;
                }
                case HEAD_REQUEST_NO_TUNNEL:
                case HEAD_REQUEST: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP HEAD: '" + url.toString() + "?" + parameter + "'."); // NOI18N
                        }

                        httpMethod = new HeadMethod(url.toString() + "?" + parameter);                  // NOI18N
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No parameters specified. HTTP HEAD: '" + url.toString() + "'."); // NOI18N
                        }

                        httpMethod = new HeadMethod(url.toString());
                    }
                    break;
                }
                default: {
                    if (parameter.length() > 0) {
                        if (log.isDebugEnabled()) {
                            log.debug("No method specified, switching to '" + ACCESS_METHODS.GET_REQUEST
                                        + "'. URI used: '"
                                        + url.toString() + "?" + parameter + "'."); // NOI18N
                        }

                        // httpMethod = new PostMethod(url.toString()); ((PostMethod) httpMethod).setRequestEntity(new
                        // StringRequestEntity(parameter.toString(), "text/xml", "UTF-8"));
                        httpMethod = new GetMethod(url.toString() + "?" + parameter);                         // NOI18N
                    } else {
                        if (log.isDebugEnabled()) {
                            log.debug("No method specified, switching to '" + ACCESS_METHODS.GET_REQUEST
                                        + "'. No parameters specified. URI used: '" + url.toString() + "'."); // NOI18N
                        }

                        httpMethod = new GetMethod(url.toString());
                    }
                }
            }

            if ((options != null) && !options.isEmpty()) {
                for (final Entry<String, String> option : options.entrySet()) {
                    httpMethod.addRequestHeader(option.getKey(), option.getValue());
                }
            }
            final boolean hasBound = false;
            while (!hasBound) {
                try {
                    httpMethod.setDoAuthentication(true);

                    final int statuscode = client.executeMethod(httpMethod);
                    switch (statuscode) {
                        case (HttpStatus.SC_UNAUTHORIZED): {
                            if (log.isInfoEnabled()) {
                                log.info("HTTP status code from server: SC_UNAUTHORIZED (" + HttpStatus.SC_UNAUTHORIZED
                                            + ")."); // NOI18N
                            }

                            throw new CannotReadFromURLException("You are not authorized to access this URL."); // NOI18N
                        }
                        case (HttpStatus.SC_OK): {
                            if (log.isDebugEnabled()) {
                                log.debug("HTTP status code from server: OK.");                                 // NOI18N
                            }
                            if ((method == ACCESS_METHODS.HEAD_REQUEST)
                                        || (method == ACCESS_METHODS.HEAD_REQUEST_NO_TUNNEL)) {
                                // returning the HTTP Header as InputStream, because some valid InputStream has to be
                                // returned. The HTTP body can not be returned because it does not exist for HEAD
                                // requests.
                                final ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                final ObjectOutputStream oos = new ObjectOutputStream(baos);

                                oos.writeObject(httpMethod.getRequestHeaders());

                                oos.flush();
                                oos.close();

                                final InputStream is = new ByteArrayInputStream(baos.toByteArray());
                                baos.close();
                                return is;
                            } else {
                                return new BufferedInputStream(httpMethod.getResponseBodyAsStream());
                            }
                        }
                        default: {
                            if (log.isDebugEnabled()) {
                                log.debug("Unhandled HTTP status code: " + statuscode + " ("
                                            + HttpStatus.getStatusText(statuscode)
                                            + ")"); // NOI18N
                            }

                            throw new BadHttpStatusCodeException(httpMethod.getURI().toString(),
                                statuscode,
                                HttpStatus.getStatusText(statuscode),
                                httpMethod.getResponseBodyAsString()); // NOI18N
                        }
                    }
                } catch (BindException e) {
                    if (log.isDebugEnabled()) {
                        log.debug("Catched Bind Exception. Will try again in 50 ms", e);
                    }
                    Thread.sleep(50);
                }
            }
        }
//        throw new RuntimeException("Should never happen");
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        final HttpClient client = getSecurityEnabledHttpClient(url);
        final PostMethod postMethod = new PostMethod(url.toString());
        log.fatal("request2", new Exception());
        postMethod.setRequestEntity(new InputStreamRequestEntity(requestParameter));

        if ((options != null) && !options.isEmpty()) {
            for (final Entry<String, String> option : options.entrySet()) {
                postMethod.addRequestHeader(option.getKey(), option.getValue());
            }
        }
        boolean hasBound = false;
        while (!hasBound) {
            try {
                postMethod.setDoAuthentication(true);

                final int statuscode = client.executeMethod(postMethod);
                hasBound = true;
                switch (statuscode) {
                    case (HttpStatus.SC_UNAUTHORIZED): {
                        if (log.isInfoEnabled()) {
                            log.info("HTTP status code from server: SC_UNAUTHORIZED (" + HttpStatus.SC_UNAUTHORIZED
                                        + ")."); // NOI18N
                        }

                        throw new CannotReadFromURLException("You are not authorized to access this URL."); // NOI18N
                    }
                    case (HttpStatus.SC_OK): {
                        if (log.isDebugEnabled()) {
                            log.debug("HTTP status code from server: OK.");                                 // NOI18N
                        }

                        return new BufferedInputStream(postMethod.getResponseBodyAsStream());
                    }
                    default: {
                        if (log.isDebugEnabled()) {
                            log.debug("Unhandled HTTP status code: " + statuscode + " ("
                                        + HttpStatus.getStatusText(statuscode)
                                        + ")."); // NOI18N
                        }

                        throw new BadHttpStatusCodeException(postMethod.getURI().toString(),
                            statuscode,
                            HttpStatus.getStatusText(statuscode),
                            postMethod.getResponseBodyAsString()); // NOI18N
                    }
                }
            } catch (BindException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Catched Bind Exception. Will try again in 50 ms", e);
                }
                Thread.sleep(50);
            }
        }
        throw new RuntimeException("Should never happen");
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
}
