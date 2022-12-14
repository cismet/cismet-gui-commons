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
package de.cismet.security;

import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;

import java.awt.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import java.net.URL;

import java.util.Collection;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import de.cismet.commons.security.AccessHandler;
import de.cismet.commons.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.commons.security.AccessHandler.ACCESS_METHODS;
import de.cismet.commons.security.Tunnel;
import de.cismet.commons.security.TunnelStore;
import de.cismet.commons.security.handler.ExtendedAccessHandler;
import de.cismet.commons.security.handler.ProxyCabaple;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;

import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.MissingArgumentException;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.RequestFailedException;

import de.cismet.security.handler.DefaultHTTPAccessHandler;
import de.cismet.security.handler.FTPAccessHandler;
import de.cismet.security.handler.HTTPBasedAccessHandler;
import de.cismet.security.handler.SecondaryJksSSLSocketFactory;
import de.cismet.security.handler.WSSAccessHandler;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
//ToDO default Handler (HTTP)
//ToDo Proxy
//ToDO Http Access
//ToDo Multithreading
//Problematik wenn unter der url mehrere services z.B. wms wfs wss sind
//Todo url leichen weil statisch --> wenn versucht wird eine schon vorhandene URL hinzuzufügen --> wir im Moment  überschrieben
public class WebAccessManager implements AccessHandler, TunnelStore, ExtendedAccessHandler, ProxyHandler.Listener {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(WebAccessManager.class);

    private static WebAccessManager instance = null;
    private static final ReentrantReadWriteLock reLock = new ReentrantReadWriteLock();
    private static final Lock readLock = reLock.readLock();
    private static final Lock writeLock = reLock.writeLock();

    //~ Instance fields --------------------------------------------------------

    private final HashMap<URL, AccessHandler> handlerMapping = new HashMap<>();
    private final HashMap<ACCESS_HANDLER_TYPES, AccessHandler> allHandlers = new HashMap<>();
//    private final ArrayList<ACCESS_HANDLER_TYPES> supportedHandlerTypes = new ArrayList<>();
    private AccessHandler defaultHandler;
    private final Properties serverAliasProps = new Properties();
    private Component topLevelComponent = null;
    private Tunnel tunnel = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebAccessManager object.
     */
    private WebAccessManager() {
        final Proxy proxy = ProxyHandler.getInstance().getProxy();
        initHandlers(proxy);
        setProxy(proxy);
        ProxyHandler.getInstance().addListener(this);

        try(final InputStream jksInputStream = getClass().getClassLoader().getResourceAsStream(
                            "de/cismet/security/secondary.jks");
                    final InputStream pwInputStream = getClass().getClassLoader().getResourceAsStream(
                            "de/cismet/security/secondary.pw");
            ) {
            if ((jksInputStream != null) && (pwInputStream != null)) {
                final String pw = IOUtils.toString(pwInputStream, "UTF-8");
                Protocol.registerProtocol(
                    "https",
                    new Protocol(
                        "https",
                        (ProtocolSocketFactory)new SecondaryJksSSLSocketFactory(jksInputStream, pw),
                        443));
            }
        } catch (final Exception ex) {
            LOG.error(ex, ex);
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void proxyChanged(final ProxyHandler.Event event) {
        setProxy(event.getNewProxy());
    }

    /**
     * Sets the Proxy-Object of the HTTP- and the WSS-AccessHandler. Does nothing if no HTTP-AccessHandler and no
     * WSS-AccessHandler exists.
     *
     * @param  proxy  DOCUMENT ME!
     */
    private void setProxy(final Proxy proxy) {
        for (final AccessHandler accessHandler : allHandlers.values()) {
            if (accessHandler instanceof ProxyCabaple) {
                ((ProxyCabaple)accessHandler).setProxy(proxy);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void resetWSSCredentials() {
        // WSS-Handler holen
        final AccessHandler wssHandler = allHandlers.get(AccessHandler.ACCESS_HANDLER_TYPES.WSS);
        // pruefen ob vom Typ WSSAccessHandler
        if ((wssHandler != null) && (wssHandler instanceof WSSAccessHandler)) {
            // proxy setzen
            if (LOG.isDebugEnabled()) {
                LOG.debug("reset WSS credentials"); // NOI18N
            }
            ((WSSAccessHandler)wssHandler).resetCredentials();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void resetCredentials() {
        for (final AccessHandler wmsHandler : allHandlers.values()) {
            // pruefen ob vom Typ HTTPBasedAccessHandler
            if ((wmsHandler != null) && (wmsHandler instanceof HTTPBasedAccessHandler)) {
                // proxy setzen
                if (LOG.isDebugEnabled()) {
                    LOG.debug("reset credentials"); // NOI18N
                }
                ((HTTPBasedAccessHandler)wmsHandler).resetCredentials();
            }
        }
    }

    /**
     * Returns the Proxy-Object of the HTTP-AccessHandler or (if it not exists) the Proxy-Object of the
     * WSS-AccessHandler or null if no proxy exists.
     *
     * @return  HTTP-AccessHandler or null
     */
    public Proxy getHttpProxy() {
        // HTTP-Handler holen
        final AccessHandler httpHandler = allHandlers.get(AccessHandler.ACCESS_HANDLER_TYPES.HTTP);
        // pruefen ob vom Typ HTTPBasedAccessHandler
        if ((httpHandler != null) && (httpHandler instanceof HTTPBasedAccessHandler)) {
            // proxy zurueckgeben
            return ((HTTPBasedAccessHandler)httpHandler).getProxy();
        } else {
            // WSS-Handler holen
            final AccessHandler wssHandler = allHandlers.get(AccessHandler.ACCESS_HANDLER_TYPES.WSS);
            // pruefen ob vom Typ WSSAccessHandler
            if ((wssHandler != null) && (wssHandler instanceof WSSAccessHandler)) {
                return ((WSSAccessHandler)wssHandler).getProxy();
            } else {
                return null;
            }
        }
    }

    /**
     * ToDO make configurable.
     *
     * @param  proxy  DOCUMENT ME!
     */
    private void initHandlers(final Proxy proxy) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("initHandlers"); // NOI18N
        }
        allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.HTTP, new DefaultHTTPAccessHandler(proxy));
        allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.WSS, new WSSAccessHandler(proxy));
        allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.FTP, new FTPAccessHandler(proxy));

        // allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.SOAP, new SOAPAccessHandler());
        // allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.SANY, new SanyAccessHandler());
// supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.WSS);
// supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.HTTP);
// supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.SOAP);
// supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.SANY);
// supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.FTP);

        defaultHandler = allHandlers.get(AccessHandler.ACCESS_HANDLER_TYPES.HTTP);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AccessHandler getDefaultHandler() {
        return defaultHandler;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  defaultHandler  DOCUMENT ME!
     */
    public void setDefaultHandler(final AccessHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static WebAccessManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            createInstance();
            return instance;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new WebAccessManager();
        }
    }

    /**
     * overwrites at the moment.
     *
     * @param   url          DOCUMENT ME!
     * @param   handlerType  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized boolean registerAccessHandler(final URL url, final ACCESS_HANDLER_TYPES handlerType) {
        writeLock.lock();
        try {
            if ((handlerMapping.get(url) == null) && (allHandlers.get(handlerType) != null)) {
                handlerMapping.put(url, allHandlers.get(handlerType));
                return true;
            } else {
                // todo einfacher wäre überschreiben ohne zu deregistrieren --> ist synchronisiert
                if (deregisterAccessHandler(url)) {
                    if (allHandlers.get(handlerType) != null) {
                        handlerMapping.put(url, allHandlers.get(handlerType));
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized boolean deregisterAccessHandler(final URL url) {
        writeLock.lock();
        try {
            if (handlerMapping.containsKey(url)) {
                handlerMapping.remove(url);
                return true;
            } else {
                return false;
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isHandlerForURLRegistered(final URL url) {
        readLock.lock();
        try {
            return handlerMapping.get(url) != null;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public AccessHandler getHandlerForURL(final URL url) {
        readLock.lock();
        try {
            final AccessHandler handler = handlerMapping.get(url);
            if (handler == null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("no handler found  for url --> try to extract base");         // NOI18N
                }
                final String urlString = url.toString();
                URL baseURL = null;
                if (urlString.indexOf('?') != -1) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("there are parameter appended to the url try to remove"); // NOI18N
                    }
                    try {
                        baseURL = new URL(urlString.substring(0, urlString.indexOf('?')));
                        return handlerMapping.get(baseURL);
                    } catch (Exception ex) {
                    }
                }
            }
            return handler;
        } finally {
            readLock.unlock();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ACCESS_HANDLER_TYPES getTypeOfHandler(final URL url) {
        final AccessHandler accessHandler = handlerMapping.get(url);
        if (accessHandler != null) {
            return accessHandler.getHandlerType();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  MissingArgumentException             DOCUMENT ME!
     * @throws  AccessMethodIsNotSupportedException  DOCUMENT ME!
     * @throws  RequestFailedException               DOCUMENT ME!
     * @throws  NoHandlerForURLException             DOCUMENT ME!
     * @throws  Exception                            DOCUMENT ME!
     */
    @Override
    public InputStream doRequest(final URL url) throws MissingArgumentException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("URL: " + url + "... trying to retrieve parameters automatically by HTTP_GET");       // NOI18N
        }
        URL serviceURL;
        String requestParameter;
        try {
            final String urlString = url.toString();
            if (urlString.indexOf('?') != -1) {
                serviceURL = new URL(urlString.substring(0, urlString.indexOf('?')));                       // NOI18N
                if (LOG.isDebugEnabled()) {
                    LOG.debug("service URL: " + serviceURL);                                                // NOI18N
                }
                if ((urlString.indexOf('?') + 1) < urlString.length()) {                                    // NOI18N
                    requestParameter = urlString.substring(urlString.indexOf('?') + 1, urlString.length()); // NOI18N
                    if (requestParameter.toLowerCase().contains("service=wss")) {                           // NOI18N
                        // TODO muss auch wfs fähig sein
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("query default WMS");                       // NOI18N
                        }
                        requestParameter = "REQUEST=GetCapabilities&service=WMS"; // NOI18N
                    }
                } else {
                    requestParameter = "";                                        // NOI18N
                }

                if (LOG.isDebugEnabled()) {
                    LOG.debug("requestParameter: " + requestParameter);               // NOI18N
                }
            } else {
                LOG.warn("Not able to parse requestparameter (no ?) trying without"); // NOI18N
                serviceURL = url;
                requestParameter = "";                                                // NOI18N
            }
        } catch (Exception ex) {
            // final String errorMessage = "Exception während dem bestimmen der Request Parameter";
            final String errorMessage = "Request parameters coud not be parsed: " + ex.getMessage(); // NOI18N
            LOG.error(errorMessage);
            throw new RequestFailedException(errorMessage, ex);
        }
        return doRequest(serviceURL, new StringReader(requestParameter), AccessHandler.ACCESS_METHODS.GET_REQUEST);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   accessMethod      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  MissingArgumentException             DOCUMENT ME!
     * @throws  AccessMethodIsNotSupportedException  DOCUMENT ME!
     * @throws  RequestFailedException               DOCUMENT ME!
     * @throws  NoHandlerForURLException             DOCUMENT ME!
     * @throws  Exception                            DOCUMENT ME!
     */
    public InputStream doRequest(final URL url,
            final String requestParameter,
            final AccessHandler.ACCESS_METHODS accessMethod) throws MissingArgumentException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Requestparameter: " + requestParameter); // NOI18N
        }
        return doRequest(url, new StringReader(requestParameter), accessMethod);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   accessMethod      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  MissingArgumentException             DOCUMENT ME!
     * @throws  AccessMethodIsNotSupportedException  DOCUMENT ME!
     * @throws  RequestFailedException               DOCUMENT ME!
     * @throws  NoHandlerForURLException             DOCUMENT ME!
     * @throws  Exception                            DOCUMENT ME!
     */
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final AccessHandler.ACCESS_METHODS accessMethod) throws MissingArgumentException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        return doRequest(url, requestParameter, accessMethod, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   accessMethod      DOCUMENT ME!
     * @param   options           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  MissingArgumentException             DOCUMENT ME!
     * @throws  AccessMethodIsNotSupportedException  DOCUMENT ME!
     * @throws  RequestFailedException               DOCUMENT ME!
     * @throws  NoHandlerForURLException             DOCUMENT ME!
     * @throws  Exception                            DOCUMENT ME!
     */
    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final AccessHandler.ACCESS_METHODS accessMethod,
            final HashMap<String, String> options) throws MissingArgumentException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        readLock.lock();

        if (url == null) {
            throw new MissingArgumentException("URL is null.");                        // NOI18N
        } else if (accessMethod == null) {
            LOG.warn("No access method specified. Calling handler's default method."); // NOI18N
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request URL: '" + url.toString() + "'."); // NOI18N
        }

        final AccessHandler handler;
        try {
            handler = handlerMapping.get(url);

            if (handler != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Handler for URL '" + url + "' available."); // NOI18N
                }

                if (handler.isAccessMethodSupported(accessMethod)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Handler supports access method '" + accessMethod + "'."); // NOI18N
                    }

                    return handler.doRequest(url, requestParameter, accessMethod, options);
                } else {
                    throw new AccessMethodIsNotSupportedException("The access method '" + accessMethod
                                + "' is not supported by handler '" // NOI18N
                                + handler.getClass() + "'.");       // NOI18N
                }
            } else {
                // TODO Default handler
                if (LOG.isInfoEnabled()) {
                    LOG.info("No handler for URL available. Using DefaultHandler."); // NOI18N
                }

                if (defaultHandler != null) {
                    return defaultHandler.doRequest(url, requestParameter, accessMethod, options);
                } else {
                    throw new NoHandlerForURLException("No default handler available."); // NOI18N
                }
            }
        } finally {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Releasing lock.");                                            // NOI18N
            }

            readLock.unlock();
        }
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws MissingArgumentException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        readLock.lock();

        if (url == null) {
            throw new MissingArgumentException("URL is null."); // NOI18N
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Request URL: '" + url.toString() + "'."); // NOI18N
        }

        final AccessHandler handler;
        try {
            handler = handlerMapping.get(url);

            if (handler != null) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Handler for URL '" + url + "' available."); // NOI18N
                }

                if (handler.isAccessMethodSupported(ACCESS_METHODS.POST_REQUEST)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Handler supports access method + '" + ACCESS_METHODS.POST_REQUEST + "'."); // NOI18N
                    }

                    return handler.doRequest(url, requestParameter, options);
                } else {
                    throw new AccessMethodIsNotSupportedException("The access method '" + ACCESS_METHODS.POST_REQUEST
                                + "' is not supported by handler '" // NOI18N
                                + handler.getClass() + "'.");       // NOI18N
                }
            } else {
                // TODO Default handler

                if (LOG.isInfoEnabled()) {
                    LOG.info("No handler for URL available. Using default handler."); // NOI18N
                }

                if (defaultHandler != null) {
                    return defaultHandler.doRequest(url, requestParameter, options);
                } else {
                    throw new NoHandlerForURLException("No default handler available."); // NOI18N
                }
            }
        } catch (Exception ex) {
            LOG.error("Error while doRequest.", ex);                                     // NOI18N

            throw ex;
        } finally {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Releasing lock."); // NOI18N
            }

            readLock.unlock();
        }
    }

    /**
     * Checks with a HEAD request, if an URL is accessible or not.
     *
     * <p>Note: The method might return false, even if the URL exists, because of network problems or permission issues
     * etc...</p>
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  true: the URL is accessible. Otherwise false
     */
    @Override
    public boolean checkIfURLaccessible(final URL url) {
        final boolean urlAccessible = false;
        // if the URL is accessible an InputStream is returned. Otherwise an Exception is thrown. As the URL might not
        // be accessible, the exceptions are only logged in the debug mode.
        try(final InputStream inputStream = this.doRequest(url, "", AccessHandler.ACCESS_METHODS.HEAD_REQUEST)) {
            return inputStream != null;
        } catch (final MissingArgumentException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Could not read document from URL '" + url.toExternalForm() + "'.", ex);
            }
        } catch (final AccessMethodIsNotSupportedException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Can't access document URL '" + url.toExternalForm()
                            + "' with default access method.",
                    ex);
            }
        } catch (final RequestFailedException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Requesting document from URL '" + url.toExternalForm() + "' failed.",
                    ex);
            }
        } catch (final NoHandlerForURLException ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Can't handle URL '" + url.toExternalForm() + "'.", ex);
            }
        } catch (final Exception ex) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("An exception occurred while opening URL '" + url.toExternalForm()
                            + "'.",
                    ex);
            }
        }
        return false;
    }

    /**
     * TODO keine Funktionalität --> nur dummies zur kompatibilität.
     *
     * @param  key    DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    public void addServerAliasProperty(final String key, final String value) {
        serverAliasProps.put(key, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getServerAliasProperty(final String key) {
        return serverAliasProps.getProperty(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Component getTopLevelComponent() {
        return topLevelComponent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  topLevelComponent  DOCUMENT ME!
     */
    public void setTopLevelComponent(final Component topLevelComponent) {
        this.topLevelComponent = topLevelComponent;
    }

    /**
     * todo.
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    @Override
    public ACCESS_HANDLER_TYPES getHandlerType() {
        throw new UnsupportedOperationException("Not supported yet."); // NOI18N
    }

    /**
     * todo.
     *
     * @param   method  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  UnsupportedOperationException  DOCUMENT ME!
     */
    @Override
    public boolean isAccessMethodSupported(final ACCESS_METHODS method) {
        throw new UnsupportedOperationException("Not supported yet."); // NOI18N
    }

    @Override
    public Tunnel getTunnel() {
        return tunnel;
    }

    @Override
    public void setTunnel(final Tunnel tunnel) {
        this.tunnel = tunnel;
        final Collection<AccessHandler> c = allHandlers.values();
        for (final AccessHandler a : c) {
            if (a instanceof TunnelStore) {
                ((TunnelStore)a).setTunnel(tunnel);
            }
        }
        if (!c.contains(defaultHandler)) {
            if (defaultHandler instanceof TunnelStore) {
                ((TunnelStore)defaultHandler).setTunnel(tunnel);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        WebAccessManager.getInstance()
                .setProxy(new Proxy(true, "localhost", 9090, null, "102-cismet", "Irgendwas 2021!", "stadt"));
        System.out.println(IOUtils.toString(
                WebAccessManager.getInstance().doRequest(new URL("https://boxy.cismet.de")),
                "UTF-8"));
    }
}
