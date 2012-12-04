/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.jackrabbit.webdav.client.methods.DavMethod;
import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;
import org.apache.jackrabbit.webdav.client.methods.PutMethod;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;

import de.cismet.netutil.Proxy;

/**
 * Communicates with a web dav server.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class WebDavClient {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger log = Logger.getLogger(WebDavClient.class);
    private static final int MAX_HOST_CONNECTIONS = 20;

    //~ Instance fields --------------------------------------------------------

    private String username;
    private String password;
    private HttpClient client = null;
    private String currentHost = null;
    private Proxy proxy = null;
    private boolean useNTAuth;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebDavClient object.
     *
     * @param  proxy     the currently used proxy or null, if no proxy is used
     * @param  username  can be null, if no authentication is required
     * @param  password  can be null, if no authentication is required
     */
    public WebDavClient(final Proxy proxy, final String username, final String password) {
        this(proxy, username, password, false);
    }

    /**
     * Creates a new WebDavClient object.
     *
     * @param  proxy      DOCUMENT ME!
     * @param  username   DOCUMENT ME!
     * @param  password   DOCUMENT ME!
     * @param  useNTAuth  DOCUMENT ME!
     */
    public WebDavClient(final Proxy proxy, final String username, final String password, final boolean useNTAuth) {
        this.username = username;
        this.password = password;
        this.proxy = proxy;
        this.useNTAuth = useNTAuth;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * initialises the http client for the given host.
     *
     * @param  host  DOCUMENT ME!
     */
    public void init(final String host) {
        if (log.isDebugEnabled()) {
            log.debug("initialise WebDavClient");
        }
        final HostConfiguration hostConfig = new HostConfiguration();
        hostConfig.setHost(host);
        final HttpConnectionManager connectionManager = new MultiThreadedHttpConnectionManager();
        final HttpConnectionManagerParams params = new HttpConnectionManagerParams();
        params.setMaxConnectionsPerHost(hostConfig, MAX_HOST_CONNECTIONS);
        connectionManager.setParams(params);
        client = new HttpClient(connectionManager);
        client.setHostConfiguration(hostConfig);

        if ((username != null) && (password != null)) {
            if (useNTAuth) {
                final Credentials credentials = new NTCredentials(username,
                        password,
                        "",
                        "");
                client.getState().setCredentials(AuthScope.ANY, credentials);
            } else {
                final Credentials creds = new UsernamePasswordCredentials(username, password);
                client.getState().setCredentials(AuthScope.ANY, creds);
            }
        }

        if ((proxy != null) && proxy.isEnabled()) {
            if (log.isDebugEnabled()) {
                log.debug("use proxy");
            }
            client.getHostConfiguration().setProxy(proxy.getHost(), proxy.getPort());

            if (proxy.getUsername() != null) {
                final AuthScope scope = new AuthScope(proxy.getHost(), proxy.getPort());
                final Credentials credentials = new NTCredentials(proxy.getUsername(),
                        proxy.getPassword(),
                        "",
                        proxy.getDomain());
                client.getState().setProxyCredentials(scope, credentials);
            }
        }
        currentHost = host;
    }

    /**
     * delete the given path.
     *
     * @param   path  DOCUMENT ME!
     *
     * @throws  MalformedURLException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     * @throws  HttpException          DOCUMENT ME!
     */
    public void delete(final String path) throws MalformedURLException, IOException, HttpException {
        lazyInitialise(path);
        if (log.isDebugEnabled()) {
            log.debug("delete: " + path);
        }
        final DavMethod put = new DeleteMethod(path);
        client.executeMethod(put);

        put.releaseConnection();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  an InputStream from the given path
     *
     * @throws  MalformedURLException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     * @throws  HttpException          DOCUMENT ME!
     */
    public InputStream getInputStream(final String path) throws MalformedURLException, IOException, HttpException {
        lazyInitialise(path);
        if (log.isDebugEnabled()) {
            log.debug("get: " + path);
        }
        final GetMethod get = new GetMethod(path);
        client.executeMethod(get);

        return get.getResponseBodyAsStream();
    }

    /**
     * copies the content of the given InputStream to the given path.
     *
     * @param   path   DOCUMENT ME!
     * @param   input  DOCUMENT ME!
     *
     * @throws  MalformedURLException  DOCUMENT ME!
     * @throws  IOException            DOCUMENT ME!
     * @throws  HttpException          DOCUMENT ME!
     */
    public void put(final String path, final InputStream input) throws MalformedURLException,
        IOException,
        HttpException {
        lazyInitialise(path);
        if (log.isDebugEnabled()) {
            log.debug("put: " + path);
        }
        final PutMethod put = new PutMethod(path);
        final RequestEntity requestEntity = new InputStreamRequestEntity(input);

        put.setRequestEntity(requestEntity);
        client.executeMethod(put);
        put.releaseConnection();
    }

    /**
     * checks if the HttpClient is initialised with the host of the given path and initialises the client if required.
     *
     * @param   path  DOCUMENT ME!
     *
     * @throws  MalformedURLException  DOCUMENT ME!
     */
    private void lazyInitialise(final String path) throws MalformedURLException {
        final int startIndex = path.indexOf("://") + "://".length();
        String host = null;

        if (startIndex != -1) {
            int endIndex = path.indexOf("/", startIndex);
            if (endIndex == -1) {
                endIndex = path.length();
            }
            host = path.substring(0, endIndex);
        } else {
            throw new MalformedURLException("Protocol not found in url " + path);
        }

        if (log.isDebugEnabled()) {
            log.debug("WebDav host: " + host);
        }

        // initialises the Httpclient if it is not initialised, yet, or the host was changed
        if ((client == null) || ((currentHost != null) && !currentHost.equals(host))) {
            init(host);
        }
    }
}
