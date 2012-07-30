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
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.HashMap;
import java.util.Map.Entry;

import de.cismet.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.security.AccessHandler.ACCESS_METHODS;

import de.cismet.security.exceptions.BadHttpStatusCodeException;
import de.cismet.security.exceptions.CannotReadFromURLException;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public class DefaultHTTPAccessHandler extends HTTPBasedAccessHandler {

    //~ Static fields/initializers ---------------------------------------------

    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[] {
            ACCESS_METHODS.GET_REQUEST,
            ACCESS_METHODS.POST_REQUEST
        };
    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.HTTP;

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

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

        switch (method) {
            case POST_REQUEST: {
                httpMethod = new PostMethod(url.toString());
                ((PostMethod)httpMethod).setRequestEntity(new StringRequestEntity(
                        parameter.toString(),
                        "text/xml",
                        "UTF-8"));                                                          // NOI18N
                break;
            }
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
            default: {
                if (parameter.length() > 0) {
                    if (log.isDebugEnabled()) {
                        log.debug("No method specified, switching to '" + ACCESS_METHODS.GET_REQUEST + "'. URI used: '"
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
        httpMethod.setDoAuthentication(true);
        final int statuscode = client.executeMethod(httpMethod);
        switch (statuscode) {
            case (HttpStatus.SC_UNAUTHORIZED): {
                if (log.isInfoEnabled()) {
                    log.info("HTTP status code from server: SC_UNAUTHORIZED (" + HttpStatus.SC_UNAUTHORIZED + ")."); // NOI18N
                }

                throw new CannotReadFromURLException("You are not authorized to access this URL."); // NOI18N
            }
            case (HttpStatus.SC_OK): {
                if (log.isDebugEnabled()) {
                    log.debug("HTTP status code from server: OK.");                                 // NOI18N
                }

                return new BufferedInputStream(httpMethod.getResponseBodyAsStream());
            }
            default: {
                if (log.isDebugEnabled()) {
                    log.debug("Unhandled HTTP status code: " + statuscode + " (" + HttpStatus.getStatusText(statuscode)
                                + ")"); // NOI18N
                }

                throw new BadHttpStatusCodeException(httpMethod.getURI().toString(),
                    statuscode,
                    HttpStatus.getStatusText(statuscode),
                    httpMethod.getResponseBodyAsString()); // NOI18N
            }
        }
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        final HttpClient client = getSecurityEnabledHttpClient(url);
        final PostMethod postMethod = new PostMethod(url.toString());

        postMethod.setRequestEntity(new InputStreamRequestEntity(requestParameter));

        if ((options != null) && !options.isEmpty()) {
            for (final Entry<String, String> option : options.entrySet()) {
                postMethod.addRequestHeader(option.getKey(), option.getValue());
            }
        }

        postMethod.setDoAuthentication(true);

        final int statuscode = client.executeMethod(postMethod);
        switch (statuscode) {
            case (HttpStatus.SC_UNAUTHORIZED): {
                if (log.isInfoEnabled()) {
                    log.info("HTTP status code from server: SC_UNAUTHORIZED (" + HttpStatus.SC_UNAUTHORIZED + ")."); // NOI18N
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
                    log.debug("Unhandled HTTP status code: " + statuscode + " (" + HttpStatus.getStatusText(statuscode)
                                + ")."); // NOI18N
                }

                throw new BadHttpStatusCodeException(postMethod.getURI().toString(),
                    statuscode,
                    HttpStatus.getStatusText(statuscode),
                    postMethod.getResponseBodyAsString()); // NOI18N
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
}
