/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security;

import de.cismet.security.handler.WSSAccessHandler;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.RequestFailedException;
import de.cismet.security.exceptions.MissingArgumentException;
import de.cismet.security.AccessHandler.ACCESS_HANDLER_TYPES;
import de.cismet.security.handler.DefaultHTTPAccessHandler;
import java.awt.Component;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 * @author spuhl
 */
//ToDO default Handler (HTTP)
//ToDo Proxy
//ToDO Http Access
//ToDo Multithreading
//Problematik wenn unter der url mehrere services z.B. wms wfs wss sind
//Todo url leichen weil statisch --> wenn versucht wird eine schon vorhandene URL hinzuzufügen --> wir im Moment  überschrieben
public class WebAccessManager implements AccessHandler {

    private static WebAccessManager instance = null;
    private final HashMap<URL, AccessHandler> handlerMapping = new HashMap<URL, AccessHandler>();
    private final HashMap<ACCESS_HANDLER_TYPES, AccessHandler> allHandlers = new HashMap<ACCESS_HANDLER_TYPES, AccessHandler>();
    private final static ReentrantReadWriteLock reLock = new ReentrantReadWriteLock();
    private final static Lock readLock = reLock.readLock();
    private final static Lock writeLock = reLock.writeLock();
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private ArrayList<ACCESS_HANDLER_TYPES> supportedHandlerTypes = new ArrayList<ACCESS_HANDLER_TYPES>();
    private AccessHandler defaultHandler;
    private Properties serverAliasProps = new Properties();
    private Component topLevelComponent = null;

    private WebAccessManager() {
        initHandlers();
    }

    //ToDO make configurable
    private void initHandlers() {
        WSSAccessHandler wssHandler = new WSSAccessHandler();
        log.debug("Creating DefaultHTTPAccessHandler");
        DefaultHTTPAccessHandler httpHandler = new DefaultHTTPAccessHandler();
        //SOAPAccessHandler soapAccessHandler = new SOAPAccessHandler();
        //SanyAccessHandler sanyAccessHandler = new SanyAccessHandler();
        defaultHandler = httpHandler;
        allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.WSS, wssHandler);
        allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.HTTP, httpHandler);
        //allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.SOAP, soapAccessHandler);
        //allHandlers.put(AccessHandler.ACCESS_HANDLER_TYPES.SANY, sanyAccessHandler);
        supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.WSS);
        supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.HTTP);
        supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.SOAP);
        supportedHandlerTypes.add(ACCESS_HANDLER_TYPES.SANY);
    }

    public AccessHandler getDefaultHandler() {
        return defaultHandler;
    }

    public void setDefaultHandler(AccessHandler defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public static WebAccessManager getInstance() {
        if (instance != null) {
            return instance;
        } else {
            createInstance();
            return instance;
        }
    }

    private static synchronized void createInstance() {
        if (instance == null) {
            instance = new WebAccessManager();
        }
    }

    //overwrites at the moment
    public synchronized boolean registerAccessHandler(URL url, ACCESS_HANDLER_TYPES handlerType) {
        writeLock.lock();
        try {
            if (handlerMapping.get(url) == null && allHandlers.get(handlerType) != null) {
                handlerMapping.put(url, allHandlers.get(handlerType));
                return true;
            } else {
                //todo einfacher wäre überschreiben ohne zu deregistrieren --> ist synchronisiert
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

    public synchronized boolean deregisterAccessHandler(URL url) {
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

    public boolean isHandlerForURLRegistered(URL url) {
        readLock.lock();
        try {
            return handlerMapping.get(url) != null;
        } finally {
            readLock.unlock();
        }
    }

    public AccessHandler getHandlerForURL(URL url) {
        readLock.lock();
        try {
            AccessHandler handler = handlerMapping.get(url);
            if (handler == null) {
                log.debug("no handler found  for url --> try to extract base");
                String urlString = url.toString();
                URL baseURL = null;
                if (urlString.indexOf('?') != -1) {
                    log.debug("there are parameter appended to the url try to remove");
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

    public ACCESS_HANDLER_TYPES getTypeOfHandler(URL url) {
        AccessHandler accessHandler = handlerMapping.get(url);
        if (accessHandler != null) {
            return accessHandler.getHandlerType();
        } else {
            return null;
        }
    }

    public InputStream doRequest(URL url) throws
            MissingArgumentException,
            AccessMethodIsNotSupportedException,
            RequestFailedException,
            NoHandlerForURLException,
            Exception {
        log.debug("URL: " + url + "... Paramter werden versucht automatisch zu bestimmen und die Methode ist HTTP_GET");
        URL serviceURL;
        String requestParameter;
        try {
            final String urlString = url.toString();
            if (urlString.indexOf('?') != -1) {
                serviceURL = new URL(urlString.substring(0, urlString.indexOf('?')));
                log.debug("service URL: " + serviceURL);
                if (urlString.indexOf('?') + 1 < urlString.length()) {
                    requestParameter = urlString.substring(urlString.indexOf('?') + 1, urlString.length());
                    if (requestParameter.toLowerCase().contains("service=wss")) {
                        //TODO muss auch wfs fähig sein
                        log.debug("default WMS abrufen");
                        requestParameter = "REQUEST=GetCapabilities&service=WMS";
                    }
                } else {
                    requestParameter = "";
                }
                log.debug("requestParameter: " + requestParameter);
            } else {
                log.warn("Es war nicht möglich die requestparameter zu parsen (kein ?) versuche es ohne");
                serviceURL = url;
                requestParameter = "";
            }
        } catch (Exception ex) {
            //final String errorMessage = "Exception während dem bestimmen der Request Parameter";
            final String errorMessage = "Request parameters coud not be parsed: " + ex.getMessage();
            log.error(errorMessage);
            throw new RequestFailedException(errorMessage, ex);
        }
        return doRequest(serviceURL, new StringReader(requestParameter), AccessHandler.ACCESS_METHODS.GET_REQUEST);
    }

    public InputStream doRequest(URL url, String requestParameter, AccessHandler.ACCESS_METHODS accessMethod) throws
            MissingArgumentException,
            AccessMethodIsNotSupportedException,
            RequestFailedException,
            NoHandlerForURLException,
            Exception {
        log.debug("Requestparameter: " + requestParameter);
        return doRequest(url, new StringReader(requestParameter), accessMethod);
    }

    public InputStream doRequest(URL url, Reader requestParameter, AccessHandler.ACCESS_METHODS accessMethod) throws
            MissingArgumentException,
            AccessMethodIsNotSupportedException,
            RequestFailedException,
            NoHandlerForURLException,
            Exception {
        return doRequest(url, requestParameter, accessMethod, null);
    }

    public InputStream doRequest(URL url, Reader requestParameter, AccessHandler.ACCESS_METHODS accessMethod, HashMap<String, String> options) throws
            MissingArgumentException,
            AccessMethodIsNotSupportedException,
            RequestFailedException,
            NoHandlerForURLException,
            Exception {
        readLock.lock();
        AccessHandler handler;
        if (url == null) {
            //throw new MissingArgumentException("Es wurde keine URL gesetzt für das Request gesetzt");
            throw new MissingArgumentException("URL parameter is empty");
        } else if (accessMethod == null) {
            log.warn("Keine Access Methode vorhanden führe Defaultmethod des Handlers durch");
        }
        log.debug("Request URL: " + url.toString());
        try {
            handler = handlerMapping.get(url);
            if (handler != null) {
                log.debug("Handler für URL " + url + " vorhanden");
                if (handler.isAccessMethodSupported(accessMethod)) {
                    log.debug("Handler unterstützt access method");
                    return handler.doRequest(url, requestParameter, accessMethod, options);
//                    try {
//                        return handler.doRequest(url, requestParameter, accessMethod, options);
//                    } catch (Exception ex) {
//                        //throw new RequestFailedException("Das Request konnte nicht ausgeführt werden", ex);
//                        throw new RequestFailedException("The request cound not be performed: " + ex.getMessage(), ex);
//                    }
                } else {
                    //throw new AccessMethodIsNotSupportedException("Die Accesss Methode: " + accessMethod + " ist vom handler: " +
                    //        handler.getClass() + " nicht unterstützt");
                    throw new AccessMethodIsNotSupportedException("The access method '" + accessMethod + "' is not supported by handler '" +
                            handler.getClass() + "'");
                }
            } else {
                //TODO Default handler
                //throw new NoHandlerForURLException("Es ist kein Handler für die URL vorhanden");
                log.info("Es ist kein Handler für die URL vorhanden --> benutze DefaultHandler");
                if (defaultHandler != null) {
                    return defaultHandler.doRequest(url, requestParameter, accessMethod, options);
//                    try {
//                        return defaultHandler.doRequest(url, requestParameter, accessMethod, options);
//                    } catch (Exception ex) {
//                        //throw new RequestFailedException("Das Request konnte nicht ausgeführt werden :", ex);
//                        throw new RequestFailedException("The request cound not be performed: " + ex.getMessage(), ex);
//                    }
                } else {
                    //throw new NoHandlerForURLException("Es ist kein Defaulthandler vorhanden");
                    throw new NoHandlerForURLException("No default handler available");
                }
            }
        } catch (Exception ex) {
            log.error("Fehler bei doRequest: ", ex);

            throw ex;
        //throw new RequestFailedException("Das Request konnte nicht ausgeführt werden", ex);
        //throw new RequestFailedException("The request cound not be performed: " + ex.getMessage(), ex);
        } finally {
            log.debug("releasing lock");
            readLock.unlock();
        }

    }

    //TODO keine Funktionalität --> nur dummies zur kompatibilität
    public void addServerAliasProperty(String key, String value) {
        serverAliasProps.put(key, value);
    }

    public String getServerAliasProperty(String key) {
        return serverAliasProps.getProperty(key);
    }

    public Component getTopLevelComponent() {
        return topLevelComponent;
    }

    public void setTopLevelComponent(Component topLevelComponent) {
        this.topLevelComponent = topLevelComponent;
    }

    //todo
    public ACCESS_HANDLER_TYPES getHandlerType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //todo
    public boolean isAccessMethodSupported(ACCESS_METHODS method) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
