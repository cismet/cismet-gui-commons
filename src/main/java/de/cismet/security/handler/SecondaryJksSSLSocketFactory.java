/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.SecureProtocolSocketFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class SecondaryJksSSLSocketFactory implements SecureProtocolSocketFactory {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(SecondaryJksSSLSocketFactory.class);

    //~ Instance fields --------------------------------------------------------

    private final SSLContext sslContext = SSLContext.getInstance("TLS");

    private X509TrustManager origX509TM;
    private X509TrustManager ownX509TM;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MySSLSocketFactory object.
     *
     * @param   inputStream  DOCUMENT ME!
     * @param   jksPassword  DOCUMENT ME!
     *
     * @throws  NoSuchAlgorithmException   DOCUMENT ME!
     * @throws  KeyManagementException     DOCUMENT ME!
     * @throws  KeyStoreException          DOCUMENT ME!
     * @throws  UnrecoverableKeyException  DOCUMENT ME!
     */
    public SecondaryJksSSLSocketFactory(final InputStream inputStream, final String jksPassword)
            throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super();
        final TrustManagerFactory origTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                        .getDefaultAlgorithm());
        origTrustManagerFactory.init((KeyStore)null);

        for (final TrustManager origTrustManager : origTrustManagerFactory.getTrustManagers()) {
            if (origTrustManager instanceof X509TrustManager) {
                origX509TM = (X509TrustManager)origTrustManager;
                break;
            }
        }

        try {
            final KeyStore ownKeystore = KeyStore.getInstance(KeyStore.getDefaultType());
            ownKeystore.load(inputStream, jksPassword.toCharArray());

            final TrustManagerFactory ownTrustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                            .getDefaultAlgorithm());
            ownTrustManagerFactory.init(ownKeystore);
            for (final TrustManager ownTrustManager : ownTrustManagerFactory.getTrustManagers()) {
                if (ownTrustManager instanceof X509TrustManager) {
                    ownX509TM = (X509TrustManager)ownTrustManager;
                    break;
                }
            }
        } catch (final Exception ex) {
            LOG.warn(ex, ex);
        }

        final TrustManager tm = new X509TrustManager() {

                @Override
                public void checkClientTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                    if (ownX509TM != null) {
                        try {
                            ownX509TM.checkClientTrusted(chain, authType);
                        } catch (final CertificateException ex) {
                            origX509TM.checkClientTrusted(chain, authType);
                        }
                    } else {
                        origX509TM.checkClientTrusted(chain, authType);
                    }
                }

                @Override
                public void checkServerTrusted(final X509Certificate[] chain, final String authType)
                        throws CertificateException {
                    if (ownX509TM != null) {
                        try {
                            ownX509TM.checkServerTrusted(chain, authType);
                        } catch (final CertificateException ex) {
                            origX509TM.checkServerTrusted(chain, authType);
                        }
                    } else {
                        origX509TM.checkServerTrusted(chain, authType);
                    }
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return origX509TM.getAcceptedIssuers();
                }
            };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Socket createSocket(final String host,
            final int port,
            final InetAddress clientHost,
            final int clientPort) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(
                host,
                port,
                clientHost,
                clientPort);
    }
    @Override
    public Socket createSocket(
            final String host,
            final int port,
            final InetAddress localAddress,
            final int localPort,
            final HttpConnectionParams params) throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        final int timeout = params.getConnectionTimeout();
        final SocketFactory socketfactory = sslContext.getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        } else {
            final Socket socket = socketfactory.createSocket();
            final SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
            final SocketAddress remoteaddr = new InetSocketAddress(host, port);
            socket.bind(localaddr);
            socket.connect(remoteaddr, timeout);
            return socket;
        }
    }

    @Override
    public Socket createSocket(final String host, final int port) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(
                host,
                port);
    }

    @Override
    public Socket createSocket(final Socket socket, final String host, final int port, final boolean autoClose)
            throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(
                socket,
                host,
                port,
                autoClose);
    }
}
