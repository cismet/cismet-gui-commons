/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security.handler;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.URL;

import java.util.HashMap;

import de.cismet.netutil.Proxy;

import de.cismet.security.Tunnel;

import static org.apache.commons.net.ftp.FTP.BINARY_FILE_TYPE;

/**
 * DOCUMENT ME!
 *
 * @author   Gilles Baatz
 * @version  $Revision$, $Date$
 */
public class FTPAccessHandler extends AbstractAccessHandler {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(FTPAccessHandler.class);

    public static final ACCESS_HANDLER_TYPES ACCESS_HANDLER_TYPE = ACCESS_HANDLER_TYPES.FTP;
    public static ACCESS_METHODS[] SUPPORTED_ACCESS_METHODS = new ACCESS_METHODS[] { ACCESS_METHODS.GET_REQUEST, };

    //~ Instance fields --------------------------------------------------------

    private transient Proxy proxy;
    private Tunnel tunnel = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FTPAccessHandler object.
     */
    public FTPAccessHandler() {
        this.proxy = Proxy.fromSystem();
    }

    //~ Methods ----------------------------------------------------------------

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

    @Override
    public InputStream doRequest(final URL url,
            final Reader requestParameter,
            final ACCESS_METHODS method,
            final HashMap<String, String> options) throws Exception {
        final FTPClient ftpClient = getConfiguredFTPClient();
        final StringBuilder parameter = new StringBuilder();
        final BufferedReader reader = new BufferedReader(requestParameter);

        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            parameter.append(currentLine);
        }

        if ((tunnel != null)
                    && ((method == ACCESS_METHODS.GET_REQUEST) || (method == ACCESS_METHODS.POST_REQUEST)
                        || (method == ACCESS_METHODS.HEAD_REQUEST))
                    && tunnel.isResponsible(method, url.toString())) {
            return tunnel.doRequest(url, new StringReader(parameter.toString()), method, options);
        }

        final int port = (url.getPort() != -1) ? url.getPort() : url.getDefaultPort();
        ftpClient.connect(url.getHost(), port);

        if (url.getUserInfo() != null) {
            final String[] user_password = url.getUserInfo().split(":");
            String password = "";
            if (user_password.length > 1) {
                password = user_password[1];
            }
            ftpClient.login(user_password[0], password);
        } else {
            ftpClient.login("anonymous", "");
        }

        ftpClient.enterLocalPassiveMode();
        ftpClient.setFileType(BINARY_FILE_TYPE);

        return ftpClient.retrieveFileStream(url.getPath());
    }

    @Override
    public InputStream doRequest(final URL url,
            final InputStream requestParameter,
            final HashMap<String, String> options) throws Exception {
        LOG.fatal("FTPAccessHandler.doRequest: Not supported yet.", new Exception()); // NOI18N
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected FTPClient getConfiguredFTPClient() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("getConfiguredFTPClient"); // NOI18N
        }

        final FTPClient client = new FTPClient();
        final FTPClientConfig config = new FTPClientConfig();
        if (proxy != null) {
            // proxy needs authentication
            if ((proxy.getUsername() != null) && (proxy.getPassword() != null)) {
                Authenticator.setDefault(new Authenticator() {

                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            final PasswordAuthentication p = new PasswordAuthentication(
                                    proxy.getUsername(),
                                    proxy.getPassword().toCharArray());
                            return p;
                        }
                    });
            }

            final java.net.Proxy proxyfo = new java.net.Proxy(
                    java.net.Proxy.Type.HTTP,
                    new InetSocketAddress(this.proxy.getHost(), this.proxy.getPort()));
            client.setProxy(proxyfo);
        }
        client.configure(config);

        return client;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Proxy getProxy() {
        return proxy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  proxy  DOCUMENT ME!
     */
    public void setProxy(final Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Tunnel getTunnel() {
        return tunnel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tunnel  DOCUMENT ME!
     */
    public void setTunnel(final Tunnel tunnel) {
        this.tunnel = tunnel;
    }
}
