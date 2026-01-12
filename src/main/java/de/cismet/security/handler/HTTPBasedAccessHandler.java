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

//import org.apache.commons.httpclient.Credentials;
//import org.apache.commons.httpclient.HttpClient;
//import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
//import org.apache.commons.httpclient.NTCredentials;
//import org.apache.commons.httpclient.auth.AuthScope;
//import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.routing.DefaultProxyRoutePlanner;
import org.apache.hc.client5.http.routing.HttpRoutePlanner;
import org.apache.hc.core5.http.HttpHost;
import org.apache.log4j.Logger;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.cismet.commons.security.handler.AbstractAccessHandler;
import de.cismet.commons.security.handler.ChainedCredentialsProvider;
import de.cismet.commons.security.handler.ProxyCabaple;

import de.cismet.netutil.Proxy;

import de.cismet.security.GUICredentialsProvider;
import de.cismet.security.WebAccessManager;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public abstract class HTTPBasedAccessHandler extends AbstractAccessHandler implements ProxyCabaple {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(HTTPBasedAccessHandler.class);

    //~ Instance fields --------------------------------------------------------

    // do not use the url as key, because similar urls will did not be differentiated in the HashMap implementation
    // use the string representation of the urls instead.
    private final transient Map<String, GUICredentialsProvider> httpCredentialsForURLS;
    private transient Proxy proxy;

    //~ Constructors -----------------------------------------------------------

    /**
     * Sets the SystemProxy by default.
     *
     * @param  proxy  DOCUMENT ME!
     */
    protected HTTPBasedAccessHandler(final Proxy proxy) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("HTTPBasedAccessHandler"); // NOI18N
        }
        httpCredentialsForURLS = new HashMap<>();
        this.proxy = proxy;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns a configured HttpClient with (if set) proxy settings.
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  configured HttpClient
     */
    protected HttpClientBuilder getConfiguredHttpClientBuilderForUrl(final URL url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getConfiguredHttpClient"); // NOI18N
        }

        // Replacement for MultiThreadedHttpConnectionManager
        final PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
        connManager.setMaxTotal(200);
        connManager.setDefaultMaxPerRoute(20);

        final HttpClientBuilder clientBuilder = HttpClients.custom();
        clientBuilder.setConnectionManager(connManager);

        if ((proxy != null)
                    && (proxy.getHost() != null)
                    && (proxy.getPort() > 0)
                    && proxy.isValid()
                    && proxy.isEnabledFor((url != null) ? url.getHost() : null)) {
            // Proxy setzen
            final HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
            final HttpRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxyHost);
            clientBuilder.setRoutePlanner(routePlanner);

            final BasicCredentialsProvider credentialsProvider = getProxyCredentialsProvider(url);

            if (credentialsProvider != null) {
                clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }
        }

        return clientBuilder;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private BasicCredentialsProvider getProxyCredentialsProvider(final URL url) {
        if ((proxy != null)
                    && (proxy.getHost() != null)
                    && (proxy.getPort() > 0)
                    && proxy.isValid()
                    && proxy.isEnabledFor((url != null) ? url.getHost() : null)) {
            // Proxy-Authentifizierung
            if ((proxy.getUsername() != null) && (proxy.getPassword() != null)) {
                final BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();

                Credentials credentials;

                if ((proxy.getDomain() != null) && (proxy.getDomain().length() > 0)) {
                    // NTLM
                    credentials = new NTCredentials(
                            proxy.getUsername(),
                            proxy.getPassword().toCharArray(),
                            "", // Workstation
                            proxy.getDomain());
                } else {
                    // Basic / Digest
                    credentials = new UsernamePasswordCredentials(
                            proxy.getUsername(),
                            proxy.getPassword().toCharArray());
                }

                credentialsProvider.setCredentials(
                    new AuthScope(proxy.getHost(), proxy.getPort()),
                    credentials);

                return credentialsProvider;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  url  DOCUMENT ME!
     */
    private void askForCredentials(final URL url) {
        getProxyCredentialsProvider(url);
    }

    /**
     * Proxy getter.
     *
     * @return  proxy
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * Proxy setter.
     *
     * @param  proxy  DOCUMENT ME!
     */
    @Override
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected CloseableHttpClient getSecurityEnabledHttpClient(final URL url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getSecurityEnabledHttpClient"); // NOI18N
        }

        final HttpClientBuilder builder = getConfiguredHttpClientBuilderForUrl(url);

        builder.setDefaultCredentialsProvider(getCredentialProvider(url));

        return builder.build();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected ChainedCredentialsProvider getCredentialProvider(final URL url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Retrieving Credential Provider for url: " + url); // NOI18N
        }

        CredentialsProvider cp = getHttpCredentialProviderURL(url);

        if (cp == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("no Credential Provider available for url: " + url);
            }
            cp = createSynchronizedCP(url);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credential Provider available for url: " + url);
            }
        }

        final List<CredentialsProvider> credentialProviders = new ArrayList<CredentialsProvider>();
        final BasicCredentialsProvider proxyCredentials = getProxyCredentialsProvider(url);

        if (proxyCredentials != null) {
            credentialProviders.add(proxyCredentials);
        }
        credentialProviders.add(cp);

        return new ChainedCredentialsProvider(credentialProviders);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public GUICredentialsProvider getHttpCredentialProviderURL(final URL url) {
        final GUICredentialsProvider cp = httpCredentialsForURLS.get(url.toString());

        return cp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public synchronized GUICredentialsProvider createSynchronizedCP(final URL url) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Credential Provider should be created synchronously"); // NOI18N
        }

        GUICredentialsProvider cp = httpCredentialsForURLS.get(url.toString());
        if (cp == null) {
            cp = new GUICredentialsProvider(url, WebAccessManager.getInstance().getTopLevelComponent());
            if (LOG.isDebugEnabled()) {
                LOG.debug("A new Credential Provider instance was created for: " + url.toString()); // NOI18N
            }
            httpCredentialsForURLS.put(url.toString(), cp);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credential Provider was already available: " + url.toString());          // NOI18N
            }
        }

        return cp;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetCredentials() {
        for (final GUICredentialsProvider prov : httpCredentialsForURLS.values()) {
            prov.setUsernamePassword(null);
        }
        httpCredentialsForURLS.clear();
    }
}
