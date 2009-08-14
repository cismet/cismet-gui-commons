/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;

import de.cismet.security.*;
import de.cismet.tools.gui.StaticSwingTools;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;
import net.environmatics.acs.accessor.WSSAccessorDeegree;
import net.environmatics.acs.accessor.interfaces.AuthenticationMethod;
import net.environmatics.acs.accessor.interfaces.SessionInformation;
import net.environmatics.acs.accessor.methods.PasswordAuthenticationMethod;
import net.environmatics.acs.exceptions.AuthenticationFailedException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

/**
 *
 * @author spuhl
 */
//ToDO AbstractHandler schreiben Gemeinsamkeiten HTTPAccessHandler
public class WSSAccessHandler extends HTTPBasedAccessHandler {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private final WSSAccessorDeegree wssac;
//    private String sInfo;
//    private String subParent;
    //private UsernamePasswordCredentials credentials;
    private ReentrantLock lock = new ReentrantLock();
    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[]{
        ACCESS_METHODS.GET_REQUEST, ACCESS_METHODS.POST_REQUEST
    };

    public static enum SECURED_SERVICE_TYPE {

        WMS, WFS
    };
    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.WSS;
    private static final HashMap<URL, WSSAccessorDeegree> wssAccessorMapping = new HashMap<URL, WSSAccessorDeegree>();

//    public HTTPWSSAccessHandler(Component parentComponent, String subParent) {
//        super(parentComponent);
//        wssac = new WSSAccessorDeegree();
//        //ToDo check if url is valid;
//        wssac.setWSS(url.toString());
//        this.subParent = subParent;
//    }
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

    public InputStream doRequest(URL url, Reader requestParameter, ACCESS_METHODS method, HashMap<String, String> options) throws Exception {
        log.debug("doRequest: " + url);
        WSSAccessorDeegree accessor;
        accessor = wssAccessorMapping.get(url);
        if (accessor == null) {
            log.debug("kein WSSAccessor für URL: " + url);
//            lock.lock();
//            log.debug("sperre bekommen");
//            try {
//                if (wssAccessorMapping.get(url) == null) {
//                    log.debug("immer noch kein WSSAccessor für URL: " + url + " --> wird angelegt");
//                    accessor = createNewWSSAccessor(url);
//                } else {
//                    log.debug("WSSAccessor ist jetzt vorhanden und wird benutzt");
//                    accessor = wssAccessorMapping.get(url);
//                }
//                if (!accessor.isSessionAvailable()) {
//                    if (accessor.isSessionAvailable()) {
//                        authenticate(accessor);
//                    }
//                }
//            } finally {
//                lock.unlock();
//            }
            accessor = createNewWSSAccessor(url);
        }


        String accessMethod = null;
        switch (method) {
            case POST_REQUEST:
                log.debug("wss accessmethod ist post");
                accessMethod = WSSAccessorDeegree.DCP_HTTP_POST;
                break;
            case GET_REQUEST:
                log.debug("wss accessmethod ist get");
                accessMethod = WSSAccessorDeegree.DCP_HTTP_GET;
                break;
            default:
                log.debug("Keine Methode spezifiziert default: " + ACCESS_METHODS.POST_REQUEST);
                accessMethod = WSSAccessorDeegree.DCP_HTTP_POST;
        }
        final StringBuffer parameter = new StringBuffer();
        final BufferedReader reader = new BufferedReader(requestParameter);
        String currentLine = null;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }
        log.debug("WSSRequestParameter: " + parameter.toString());
        log.debug("using facade URL: " + url.toString());
        return new ByteArrayInputStream(accessor.doService(accessMethod, parameter.toString(), url.toString()).asBytes());

    }

    private synchronized WSSAccessorDeegree createNewWSSAccessor(
            final URL url)
            throws Exception {
        log.debug("createNewWSSAccessor");
        final WSSAccessorDeegree testAccessor = wssAccessorMapping.get(url);
        if (testAccessor == null) {
            log.debug("accessor für URL wird angelegt");
            final WSSAccessorDeegree accessor = new WSSAccessorDeegree();
            accessor.setWSS(url.toString());
            authenticate(accessor);
            wssAccessorMapping.put(url, accessor);
            accessor.setCredentialProvider(getCredentialProvider(url));
            return accessor;
        } else {
            log.debug("accessor für URL ist schon vorhanden");
            return testAccessor;
        }

    }

    public String getSecuredServiceTypeForURL(URL url) throws Exception {
        WSSAccessorDeegree accessor = wssAccessorMapping.get(url);
        if (accessor ==
                null) {
            accessor = createNewWSSAccessor(url);
            return accessor.getSecuredServiceType();
        } else {
            return accessor.getSecuredServiceType();
        }
    }

    private synchronized void authenticate(WSSAccessorDeegree accessor) throws Exception {
        if (!accessor.isSessionAvailable()) {
            log.debug("Keine Session Informationen für url: " + accessor.getWSS() + " vorhanden --> Passwordfenster");
            WSSPasswordDialog dialog = new WSSPasswordDialog(accessor);
            dialog.getCredentials();
        } else {
            log.debug("Session infromationen vorhanden --> ist schon authentifiziert");
        }
    }

    class WSSPasswordDialog extends PasswordDialog {

        private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
        private String sInfo;
        WSSAccessorDeegree wssac;

        public WSSPasswordDialog(WSSAccessorDeegree accessor) {
            super(accessor.getWSSURL(), StaticSwingTools.getParentFrame(WebAccessManager.getInstance().getTopLevelComponent()));
            this.wssac = accessor;
        }

        @Override
        public boolean authenticate(String name, char[] password, String server) throws Exception {
            log.debug("Authentication with username: " +
                    name);

            try {
                final AuthenticationMethod authMethod = new PasswordAuthenticationMethod(name +
                        "," + new String(password));
                sInfo = wssac.getSession(authMethod).getSessionID();
                //smPanel.setCredentials(sInfo);
                log.debug("Authentication successful for WSS " + url.toString() + " New SesionID:" +
                        sInfo);
                usernames.removeUserName(
                        name);
                usernames.saveUserNames();
                usernames.addUserName(name);
                usernames.saveUserNames();
                isAuthenticationDone = true;
                setUsernamePassword(new UsernamePasswordCredentials(name, new String(password)));
                return true;
            } catch (AuthenticationFailedException ex) {
                log.error("Authentication failed for WSS: " + url.toString(), ex);
                return false;

            }
        }
    }

    //Modified sources from deegree WAClient and CharsetUtils
    public String createGetRequest(String request) throws Exception {
        
        URL baseURL = null;
        String requestString = null;        
        if (request.indexOf('?') != -1) {
                    baseURL = new URL(request.substring(0, request.indexOf('?')));
                    requestString=request.substring(request.indexOf('?')+1);
                } else {
                    baseURL = new URL(request);
                }
        log.debug("Urlbase: "+baseURL.toString());
        log.debug("Requestparameter: "+requestString);
        WSSAccessorDeegree accessor;        
        accessor = wssAccessorMapping.get(baseURL);
        if(accessor == null){
            log.debug("there is no accessor for the given url");
            return null;
        }
        SessionInformation si = accessor.getSession();
        log.debug("session information" + si.getSessionID());
        requestString+="&sessionID="+si.getSessionID();
        StringBuffer sb = new StringBuffer(2000);
        sb.append(baseURL).append(
                "?service=WSS&request=DoService&version=1.0.0&");
        sb.append("AUTHMETHOD=urn:x-gdi-nrw:authnMethod:1.0:session&");
        sb.append("DCP=http_get&");
        sb.append("CREDENTIALS=").append(si.getSessionID()).append("&");
        sb.append("SERVICEREQUEST=").append(
                URLEncoder.encode(requestString, getSystemCharset()));
        return sb.toString();
    }
    //ToDo outsource in something CharsetUtils
    private static final String DEFAULT_CHARSET = "UTF-8";

    public String getSystemCharset() {
        String charset = null;
        try {
            charset = System.getProperty("CHARSET");
        } catch (Exception exc) {
            log.error("Error retrieving system property CHARSET",
                    exc);
        }
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        log.error("Using system charset: " + charset);
        return charset;
    }
}
