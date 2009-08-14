/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;


import de.cismet.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.security.AccessHandler.ACCESS_METHODS;
import de.cismet.security.exceptions.BadHttpStatusCodeException;
import de.cismet.security.exceptions.CannotReadFromURLException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;

/**
 *
 * @author spuhl
 */
public class DefaultHTTPAccessHandler extends HTTPBasedAccessHandler {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[]{
        ACCESS_METHODS.GET_REQUEST, ACCESS_METHODS.POST_REQUEST
    };
    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.HTTP;

    public InputStream doRequest(URL url, Reader requestParameter, ACCESS_METHODS method,HashMap<String,String> options) throws Exception {
        final HttpClient client = getSecurityEnabledHttpClient(url);
        final StringBuffer parameter = new StringBuffer();
        final BufferedReader reader = new BufferedReader(requestParameter);
        String currentLine = null;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }
        HttpMethod httpMethod;
        log.debug("Access method: " + method);

        switch (method) {
            case POST_REQUEST:
                httpMethod = new PostMethod(url.toString());
                ((PostMethod) httpMethod).setRequestEntity(new StringRequestEntity(parameter.toString(), "text/xml", "UTF-8"));
                break;
            case GET_REQUEST:
                if (parameter.length() > 0) {
                    log.debug("http getRequest: " + url.toString() + "?" + parameter);
                    httpMethod = new GetMethod(url.toString() + "?" + parameter);
                } else {
                    log.debug("keine Parameter");
                    log.debug("http getRequest: " + url.toString());
                    httpMethod = new GetMethod(url.toString());
                }
                break;
            default:
                if (parameter.length() > 0) {
                    log.debug("Keine Methode spezifiziert default: " + ACCESS_METHODS.GET_REQUEST);
                    log.debug("http getRequest: " + url.toString() + "?" + parameter);
                    //httpMethod = new PostMethod(url.toString());
                    //((PostMethod) httpMethod).setRequestEntity(new StringRequestEntity(parameter.toString(), "text/xml", "UTF-8"));
                    httpMethod = new GetMethod(url.toString() + "?" + parameter);
                } else {
                    log.debug("Keine Methode spezifiziert default: " + ACCESS_METHODS.GET_REQUEST);
                    log.debug("keine Parameter");
                    log.debug("http getRequest: " + url.toString());
                    httpMethod = new GetMethod(url.toString());
                }
        }
        httpMethod.setDoAuthentication(true);
        int statuscode = client.executeMethod(httpMethod);
        switch (statuscode) {
            case (HttpStatus.SC_UNAUTHORIZED):
                log.info("Status Code from Server SC_UNAUTHORIZED: " + HttpStatus.SC_UNAUTHORIZED);
                throw new CannotReadFromURLException("Sie sind nicht authorisiert um auf diese URL zuzugreifen.");
            case (HttpStatus.SC_OK):
                log.debug("httpstatuscode ok");
                return new BufferedInputStream(httpMethod.getResponseBodyAsStream());
            default:
                log.debug("bad httpstatuscode");
                throw new BadHttpStatusCodeException("Statuscode: " + statuscode);
        }

    }

    public boolean isAccessMethodSupported(ACCESS_METHODS method) {
        for (ACCESS_METHODS curMethod : SUPPORTED_ACCESS_METHODS) {
            if (curMethod == method) {
                return true;
            }
        }
        return false;
    }

    public ACCESS_HANDLER_TYPES getHandlerType() {
        return ACCESS_HANDLER_TYPE;
    }
}
