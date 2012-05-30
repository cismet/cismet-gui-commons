/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security.handler;

import net.environmatics.acs.accessor.WSSAccessorDeegree;
import net.environmatics.acs.accessor.interfaces.AuthenticationMethod;
import net.environmatics.acs.accessor.interfaces.SessionInformation;
import net.environmatics.acs.accessor.methods.PasswordAuthenticationMethod;
import net.environmatics.acs.exceptions.AuthenticationFailedException;

import org.apache.commons.httpclient.UsernamePasswordCredentials;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import java.net.URL;
import java.net.URLEncoder;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;

import de.cismet.netutil.Proxy;

import de.cismet.security.PasswordDialog;
import de.cismet.security.WebAccessManager;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
//ToDO AbstractHandler schreiben Gemeinsamkeiten HTTPAccessHandler
public class WSSAccessHandler extends HTTPBasedAccessHandler {

    //~ Static fields/initializers ---------------------------------------------

    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[] {
            ACCESS_METHODS.GET_REQUEST,
            ACCESS_METHODS.POST_REQUEST
        };
    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.WSS;
    private static final HashMap<URL, WSSAccessorDeegree> wssAccessorMapping = new HashMap<URL, WSSAccessorDeegree>();
    // ToDo outsource in something CharsetUtils
    private static final String DEFAULT_CHARSET = "UTF-8"; // NOI18N

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum SECURED_SERVICE_TYPE {

        //~ Enum constants -----------------------------------------------------

        WMS, WFS
    }

    //~ Instance fields --------------------------------------------------------

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
//    private final WSSAccessorDeegree wssac;
//    private String sInfo;
//    private String subParent;
    // private UsernamePasswordCredentials credentials;
    private ReentrantLock lock = new ReentrantLock();

    //~ Methods ----------------------------------------------------------------

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * public HTTPWSSAccessHandler(Component parentComponent, String subParent) { super(parentComponent); wssac = new
     * WSSAccessorDeegree(); //ToDo check if url is valid; wssac.setWSS(url.toString()); this.subParent = subParent; }.
     *
     * @param   method  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isAccessMethodSupported(final ACCESS_METHODS method) {
        for (final ACCESS_METHODS curMethod : SUPPORTED_ACCESS_METHODS) {
            if (curMethod == method) {
                return true;
            }
        }
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetCredentials() {
        wssAccessorMapping.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public ACCESS_HANDLER_TYPES getHandlerType() {
        return ACCESS_HANDLER_TYPE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   method            DOCUMENT ME!
     * @param   options           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final ACCESS_METHODS method,
            final HashMap<String, String> options) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("doRequest: " + url);                  // NOI18N
        }
        WSSAccessorDeegree accessor;
        accessor = wssAccessorMapping.get(url);
        if (accessor == null) {
            if (log.isDebugEnabled()) {
                log.debug("no WSSAccessor for URL: " + url); // NOI18N
            }
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

        final Proxy proxy = getProxy();
        if (proxy != null) {
            accessor.setProxy(proxy.getHost(), proxy.getPort());
        } else {
            accessor.setProxy(null, -1);
        }

        String accessMethod = null;
        switch (method) {
            case POST_REQUEST: {
                if (log.isDebugEnabled()) {
                    log.debug("wss accessmethod ist post");                                          // NOI18N
                }
                accessMethod = WSSAccessorDeegree.DCP_HTTP_POST;
                break;
            }
            case GET_REQUEST: {
                if (log.isDebugEnabled()) {
                    log.debug("wss accessmethod ist get");                                           // NOI18N
                }
                accessMethod = WSSAccessorDeegree.DCP_HTTP_GET;
                break;
            }
            default: {
                if (log.isDebugEnabled()) {
                    log.debug("Keine Methode spezifiziert default: " + ACCESS_METHODS.POST_REQUEST); // NOI18N
                }
                accessMethod = WSSAccessorDeegree.DCP_HTTP_POST;
            }
        }
        final StringBuffer parameter = new StringBuffer();
        final BufferedReader reader = new BufferedReader(requestParameter);
        String currentLine = null;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }
        if (log.isDebugEnabled()) {
            log.debug("WSSRequestParameter: " + parameter.toString());                               // NOI18N
            log.debug("using facade URL: " + url.toString());                                        // NOI18N
        }
        return new ByteArrayInputStream(accessor.doService(accessMethod, parameter.toString(), url.toString())
                        .asBytes());
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private synchronized WSSAccessorDeegree createNewWSSAccessor(
            final URL url) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("createNewWSSAccessor");                     // NOI18N
        }
        final WSSAccessorDeegree testAccessor = wssAccessorMapping.get(url);
        if (testAccessor == null) {
            if (log.isDebugEnabled()) {
                log.debug("accessor für URL wird angelegt");       // NOI18N
            }
            final WSSAccessorDeegree accessor = new WSSAccessorDeegree();
            accessor.setWSS(url.toString());
            authenticate(accessor);
            wssAccessorMapping.put(url, accessor);
            accessor.setCredentialProvider(getCredentialProvider(url));
            return accessor;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("accessor für URL ist schon vorhanden"); // NOI18N
            }
            return testAccessor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public String getSecuredServiceTypeForURL(final URL url) throws Exception {
        WSSAccessorDeegree accessor = wssAccessorMapping.get(url);
        if (accessor
                    == null) {
            accessor = createNewWSSAccessor(url);
            return accessor.getSecuredServiceType();
        } else {
            return accessor.getSecuredServiceType();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   accessor  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private synchronized void authenticate(final WSSAccessorDeegree accessor) throws Exception {
        if (!accessor.isSessionAvailable()) {
            if (log.isDebugEnabled()) {
                log.debug("Keine Session Informationen für url: " + accessor.getWSS()
                            + " vorhanden --> Passwordfenster");                            // NOI18N
            }
            final WSSPasswordDialog dialog = new WSSPasswordDialog(accessor);
            dialog.getCredentials();
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Session infromationen vorhanden --> ist schon authentifiziert"); // NOI18N
            }
        }
    }
    /**
     * Modified sources from deegree WAClient and CharsetUtils.
     *
     * @param   request  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public String createGetRequest(final String request) throws Exception {
        URL baseURL = null;
        String requestString = null;
        if (request.indexOf('?') != -1) {                                  // NOI18N
            baseURL = new URL(request.substring(0, request.indexOf('?'))); // NOI18N
            requestString = request.substring(request.indexOf('?') + 1);   // NOI18N
        } else {
            baseURL = new URL(request);
        }
        if (log.isDebugEnabled()) {
            log.debug("Urlbase: " + baseURL.toString());                   // NOI18N
            log.debug("Requestparameter: " + requestString);               // NOI18N
        }
        final WSSAccessorDeegree accessor;
        accessor = wssAccessorMapping.get(baseURL);
        if (accessor == null) {
            if (log.isDebugEnabled()) {
                log.debug("there is no accessor for the given url");       // NOI18N
            }
            return null;
        }
        final SessionInformation si = accessor.getSession();
        if (log.isDebugEnabled()) {
            log.debug("session information" + si.getSessionID());          // NOI18N
        }
        requestString += "&sessionID=" + si.getSessionID();                // NOI18N
        final StringBuffer sb = new StringBuffer(2000);
        sb.append(baseURL).append(
            "?service=WSS&request=DoService&version=1.0.0&");              // NOI18N
        sb.append("AUTHMETHOD=urn:x-gdi-nrw:authnMethod:1.0:session&");    // NOI18N
        sb.append("DCP=http_get&");                                        // NOI18N
        sb.append("CREDENTIALS=").append(si.getSessionID()).append("&");   // NOI18N
        sb.append("SERVICEREQUEST=").append(                               // NOI18N
            URLEncoder.encode(requestString, getSystemCharset()));
        return sb.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getSystemCharset() {
        String charset = null;
        try {
            charset = System.getProperty("CHARSET");                    // NOI18N
        } catch (Exception exc) {
            log.error("Error retrieving system property CHARSET", exc); // NOI18N
        }
        if (charset == null) {
            charset = DEFAULT_CHARSET;
        }
        log.error("Using system charset: " + charset);                  // NOI18N
        return charset;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class WSSPasswordDialog extends PasswordDialog {

        //~ Instance fields ----------------------------------------------------

        WSSAccessorDeegree wssac;

        private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
        private String sInfo;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new WSSPasswordDialog object.
         *
         * @param  accessor  DOCUMENT ME!
         */
        public WSSPasswordDialog(final WSSAccessorDeegree accessor) {
            super(accessor.getWSSURL(),
                StaticSwingTools.getParentFrame(WebAccessManager.getInstance().getTopLevelComponent()));
            this.wssac = accessor;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   name      DOCUMENT ME!
         * @param   password  DOCUMENT ME!
         * @param   server    DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         *
         * @throws  Exception  DOCUMENT ME!
         */
        @Override
        public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
            if (log.isDebugEnabled()) {
                log.debug("Authentication with username: " + name); // NOI18N
            }

            try {
                final AuthenticationMethod authMethod = new PasswordAuthenticationMethod(name
                                + "," + new String(password)); // NOI18N
                sInfo = wssac.getSession(authMethod).getSessionID();
                // smPanel.setCredentials(sInfo);
                if (log.isDebugEnabled()) {
                    log.debug("Authentication successful for WSS " + url.toString() + " New SesionID:" + sInfo); // NOI18N
                }
                usernames.removeUserName(
                    name);
                usernames.saveUserNames();
                usernames.addUserName(name);
                usernames.saveUserNames();
                isAuthenticationDone = true;
                setUsernamePassword(new UsernamePasswordCredentials(name, new String(password)));
                return true;
            } catch (AuthenticationFailedException ex) {
                log.error("Authentication failed for WSS: " + url.toString(), ex);                               // NOI18N
                return false;
            }
        }
    }
}
