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

    protected HttpClient getConfiguredHttpClient() {
        final HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
        Proxy proxyInUse;
        if ((proxyInUse = getSystemProxy()) != null) {
            client.getHostConfiguration().setProxy(proxyInUse.getHost(), proxyInUse.getPort());
        }
        return client;
    }

    protected HttpClient getSecurityEnabledHttpClient(final URL url) {
        log.debug("getSecurityEnabledHttpClient");
        final HttpClient client = getConfiguredHttpClient();
        client.getParams().setParameter(CredentialsProvider.PROVIDER, getCredentialProvider(url));
        return client;
    }

    protected CredentialsProvider getCredentialProvider(URL url) {        
        GUICredentialsProvider cp = getHttpCredentialProviderURL(url);
        log.debug("Retrieving Credential Provider for url: " + url.toString());
        if (cp != null) {
            log.debug("Credential Provider available for ... " + url.toString());
        } else {
            cp = createSynchronizedCP(url);
        }
        return cp;
    }
    
    public GUICredentialsProvider getHttpCredentialProviderURL(URL url) {
        return httpCredentialsForURLS.get(url);
    }

    public synchronized GUICredentialsProvider createSynchronizedCP(URL url) {
        log.debug("Credential Provider should be created synchronously");
        GUICredentialsProvider cp = httpCredentialsForURLS.get(url);
        if (cp != null) {
            log.debug("Credential Provider was already available: " + url.toString());
            return cp;
        } else {
            log.debug("A new Credential Provider instance was created for: " + url.toString());
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
        String proxySet = System.getProperty("proxySet");
        if (proxySet != null && proxySet.equals("true")) {
            log.debug("proxyIs Set");
            log.debug("ProxyHost:" + System.getProperty("http.proxyHost"));
            log.debug("ProxyPort:" + System.getProperty("http.proxyPort"));
            try {
                Proxy proxy = new Proxy(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort")));
                return proxy;
            } catch (Exception e) {
                log.error("Problem while setting proxy", e);
                return null;
            }
        }
        return null;
    }
}
