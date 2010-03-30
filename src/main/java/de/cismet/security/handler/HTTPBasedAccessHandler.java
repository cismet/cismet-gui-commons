/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;

import de.cismet.security.AccessHandler;
import de.cismet.security.GUICredentialsProvider;
import de.cismet.security.Proxy;
import de.cismet.security.WebAccessManager;
import java.awt.Component;
import java.net.URL;
import java.util.Hashtable;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.CredentialsProvider;

/**
 *
 * @author spuhl
 */
public abstract class HTTPBasedAccessHandler extends AbstractAccessHandler{

    private Hashtable<URL, GUICredentialsProvider> httpCredentialsForURLS = new Hashtable<URL, GUICredentialsProvider>();
    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private Proxy proxy = null;

    /**
     * Sets the SystemProxy by default.
     */
    protected HTTPBasedAccessHandler() {
        if(log.isDebugEnabled())
            log.debug("HTTPBasedAccessHandler"); //NOI18N
        setProxy(getSystemProxy());
    }

    /**
     * Returns a configured HttpClient with (if set) proxy settings.
     * @return configured HttpClient
     */
    protected HttpClient getConfiguredHttpClient() {
        if(log.isDebugEnabled())
            log.debug("getConfiguredHttpClient"); //NOI18N

        final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
        Proxy proxyInUse;        
        if ((proxyInUse = getProxy()) != null) { // ist ein Proxy gesetzt?
            // proxy auf HostConfiguration anwenden
            client.getHostConfiguration().setProxy(proxyInUse.getHost(), proxyInUse.getPort());
        }
        return client;
    }

    /**
     * Proxy getter
     * @return proxy
     */
    public Proxy getProxy() {
        if(log.isDebugEnabled())
            log.debug("getProxy: " + proxy); //NOI18N
        return proxy;
    }

    /**
     * Proxy setter
     * @param proxy
     */
    public void setProxy(Proxy proxy) {
        if(log.isDebugEnabled())
            log.debug("setProxy: " + proxy); //NOI18N
        this.proxy = proxy;
    }

    protected HttpClient getSecurityEnabledHttpClient(final URL url) {
        if(log.isDebugEnabled())
            log.debug("getSecurityEnabledHttpClient"); //NOI18N
        final HttpClient client = getConfiguredHttpClient();
        client.getParams().setParameter(CredentialsProvider.PROVIDER, getCredentialProvider(url));
        return client;
    }

    protected CredentialsProvider getCredentialProvider(URL url) {        
        GUICredentialsProvider cp = getHttpCredentialProviderURL(url);
        if(log.isDebugEnabled())
            log.debug("Retrieving Credential Provider for url: " + url.toString()); //NOI18N
        if (cp != null) {
            if(log.isDebugEnabled())
                log.debug("Credential Provider available for ... " + url.toString()); //NOI18N
        } else {
            cp = createSynchronizedCP(url);
        }
        return cp;
    }
    
    public GUICredentialsProvider getHttpCredentialProviderURL(URL url) {
        return httpCredentialsForURLS.get(url);
    }

    public synchronized GUICredentialsProvider createSynchronizedCP(URL url) {
        if(log.isDebugEnabled())
            log.debug("Credential Provider should be created synchronously"); //NOI18N
        GUICredentialsProvider cp = httpCredentialsForURLS.get(url);
        if (cp != null) {
            if(log.isDebugEnabled())
                log.debug("Credential Provider was already available: " + url.toString());  //NOI18N
            return cp;
        } else {
            if(log.isDebugEnabled())
                log.debug("A new Credential Provider instance was created for: " + url.toString()); //NOI18N
            cp = new GUICredentialsProvider(url,WebAccessManager.getInstance().getTopLevelComponent());
            httpCredentialsForURLS.put(url, cp);
            return cp;
        }
    }

//    public synchronized GUICredentialsProvider createSynchronizedCP(URL url, Component parent, WMSCapabilities cap) {
//        log.debug("Credential Provider should be created synchronously");
//        GUICredentialsProvider cp = httpCredentialsForCapabilities.get(cap);
//        if (cp != null) {
//            log.debug("Credential Provider was already available: " + url.toString());
//            return cp;
//        } else {
//            log.debug("A new Credential Provider instance was created for: " + url.toString());
//            cp = new GUICredentialsProvider(url, parent);
//            httpCredentialsForURLS.put(url, cp);
//            cp.setTitle(cap.getCapability().getLayer().getTitle());
//            return cp;
//        }
//    }
//
//    public synchronized GUICredentialsProvider createSynchronizedCP(URL url, WMSCapabilities cap) {
//        log.debug("Credential Provider should be created synchronously");
//        GUICredentialsProvider cp = httpCredentialsForCapabilities.get(cap);
//        if (cp != null) {
//            log.debug("Credential Provider was already available: " + url.toString());
//            return cp;
//        } else {
//            log.debug("A new Credential Provider instance was created for: " + url.toString());
//            cp = new GUICredentialsProvider(url);
//            httpCredentialsForURLS.put(url, cp);
//            cp.setTitle(cap.getCapability().getLayer().getTitle());
//            return cp;
//        }
//    }
//
//    public void addHttpCredentialProviderCapabilities(WMSCapabilities caps, GUICredentialsProvider httpCredentialsProvider) {
//        if (caps != null && httpCredentialsProvider != null) {
//            httpCredentialsForCapabilities.put(caps, httpCredentialsProvider);
//        }
//    }
//
//    public GUICredentialsProvider getHttpCredentialProviderCapabilities(WMSCapabilities caps) {
//        return httpCredentialsForCapabilities.get(caps);
//    }
//
//    public static boolean isServerSecuredByPassword(WMSCapabilities cap) {        
//        GUICredentialsProvider cp;
//        cp = broker.getHttpCredentialProviderCapabilities(cap);
//        if (cp != null) {
//            UsernamePasswordCredentials creds = cp.getCredentials();
//            if (creds != null) {
//                return true;
//            } else {
//                return false;
//            }
//        } else {
//            return false;
//        }
//    }

    protected Proxy getSystemProxy() {
        String proxySet = System.getProperty("proxySet");  //NOI18N
        if (proxySet != null && proxySet.equals("true")) {  //NOI18N
            if(log.isDebugEnabled()) {
                log.debug("proxyIs Set"); //NOI18N
                log.debug("ProxyHost:" + System.getProperty("http.proxyHost")); //NOI18N
                log.debug("ProxyPort:" + System.getProperty("http.proxyPort")); //NOI18N
            }
            try {
                Proxy proxy = new Proxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))); //NOI18N
                return proxy;
            } catch (Exception e) {
                log.error("Problem while setting proxy", e);  //NOI18N
                return null;
            }
        }
        return null;
    }
}
