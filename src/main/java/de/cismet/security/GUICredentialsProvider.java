/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * GuiCredentialProvider.java
 *
 * Created on 18. Oktober 2006, 11:18
 *
 * To change this template, choose Tools | Template Manager and open the template in the editor.
 */
package de.cismet.security;

import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.Credentials;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.NTCredentials;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.protocol.HttpContext;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.DefaultUserNameStore;
import org.jdesktop.swingx.auth.LoginService;

import java.awt.Component;

import java.io.IOException;

import java.net.URL;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import de.cismet.commons.security.exceptions.CredentialsNotAvailableException;
import de.cismet.commons.security.handler.InteractiveCredentialsProvider;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;

import de.cismet.tools.gui.DialogOpenedEvent;
import de.cismet.tools.gui.DialogSupport;
import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   Sebastian
 * @version  $Revision$, $Date$
 */
public class GUICredentialsProvider extends LoginService implements CredentialsProvider,
    InteractiveCredentialsProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(GUICredentialsProvider.class);

    //~ Instance fields --------------------------------------------------------

    private DefaultUserNameStore usernames;
    private Preferences appPrefs = null;
    private UsernamePasswordCredentials creds;
    private Component parent;
    private JFrame parentFrame;
    private boolean isAuthenticationCanceled = false;
    private URL url;
    private final Object dummy = new Object();
    private String username = null;
    private String title;
    private String prefTitle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new GUICredentialsProvider object.
     *
     * @param  url  DOCUMENT ME!
     */
    public GUICredentialsProvider(final URL url) {
        super();
        if (LOG.isDebugEnabled()) {
            LOG.debug("Creating new Credential Provider Instance for URL: " + url.toString()); // NOI18N
        }
        this.url = url;
    }

    /**
     * Creates a new GUICredentialsProvider object.
     *
     * @param  url              DOCUMENT ME!
     * @param  parentComponent  DOCUMENT ME!
     */
    public GUICredentialsProvider(final URL url, final Component parentComponent) {
        this(url);

        if (parentComponent != null) {
            this.parent = (StaticSwingTools.getParentFrame(parentComponent));

            if (this.parent == null) {
                this.parent = (StaticSwingTools.getFirstParentFrame(parentComponent));
            }
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUserName() {
        return creds.getUserName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public UsernamePasswordCredentials getCredentials() {
        return creds;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  creds  DOCUMENT ME!
     */
    public void setUsernamePassword(final UsernamePasswordCredentials creds) {
        this.creds = creds;
    }

    @Override
    public Credentials getCredentials(final AuthScope as, final HttpContext hc) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Credentials requested for :" + url.toString() + " alias: " + title); // NOI18N
        }

        final Proxy proxy = ProxyHandler.getInstance().getProxy();

        if ((proxy.getUsername() != null) && (proxy.getPassword() != null) && as.getHost().equals(proxy.getHost())) {
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

            return credentials;
        }

        usernames = new DefaultUserNameStore();
        appPrefs = Preferences.userNodeForPackage(this.getClass());
        usernames.setPreferences(appPrefs.node("loginURLHash" + Integer.toString(url.toString().hashCode()))); // NOI18N

        if (creds != null) {
            return creds;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   authenticationHeader  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CredentialsNotAvailableException  DOCUMENT ME!
     */
    @Override
    public CredentialsProvider askForCredentials(final String authenticationHeader)
            throws CredentialsNotAvailableException {
        synchronized (dummy) {
            if (creds != null) {
                return this;
            }

            isAuthenticationCanceled = false;

            if (authenticationHeader == null) {
                return null;
            }

            if (authenticationHeader.startsWith("NTLM")) {
                requestUsernamePassword();

                return this;
            } else {
                final String[] userPassword = getUserAndPasswordFromUrlIfExists(url.toString());

                if (userPassword != null) {
                    try {
                        if (authenticate(userPassword[0], userPassword[1].toCharArray(), null)) {
                            return this;
                        }
                    } catch (Exception e) {
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Error during athentication with url user/password", e);
                        }
                    }
                }
                requestUsernamePassword();

                return this;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   url  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String[] getUserAndPasswordFromUrlIfExists(final String url) {
        if (url.contains("@") && url.contains("://")
                    && (url.indexOf("@") > url.indexOf("://"))) {
            final String userPwd = url.substring(url.indexOf("://") + 3,
                    url.indexOf("@"));

            if (userPwd.contains(":")) {
                final String[] userPassword = new String[2];

                userPassword[0] = userPwd.substring(0, userPwd.indexOf(":"));
                userPassword[1] = userPwd.substring(userPwd.indexOf(":") + 1);

                return userPassword;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  CredentialsNotAvailableException  DOCUMENT ME!
     */
    private void requestUsernamePassword() throws CredentialsNotAvailableException {
        try {
            final JXLoginPane login = new JXLoginPane(this, null, usernames);

            final String[] names = usernames.getUserNames();

            if (names.length != 0) {
                username = names[names.length - 1];
            }

            login.setUserName(username);
            title = WebAccessManager.getInstance().getServerAliasProperty(url.toString());

            if (title != null) {
                login.setMessage(
                    org.openide.util.NbBundle.getMessage(
                                GUICredentialsProvider.class,
                                "GUICredentialsProvider.requestUsernamePassword().login.message") // NOI18N
                            + " \""
                            + title
                            + "\" ");                                                             // NOI18N
            } else {
                title = url.toString();

                if (title.startsWith("http://") && (title.length() > 21)) { // NOI18N
                    title = title.substring(7, 21) + "...";                 // NOI18N
                } else if (title.length() > 14) {
                    title = title.substring(0, 14) + "...";                 // NOI18N
                }

                login.setMessage(
                    org.openide.util.NbBundle.getMessage(
                                GUICredentialsProvider.class,
                                "GUICredentialsProvider.requestUsernamePassword().login.message") // NOI18N
                            + "\n"
                            + " \""
                            + title
                            + "\" ");                                                             // NOI18N
            }
            if (LOG.isDebugEnabled()) {
                LOG.debug("parentFrame in GUICredentialprovider:" + parent);                      // NOI18N
            }

            final JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog((JFrame)parent, login);

            try {
                ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
            } catch (Exception skip) {
            }

            dialog.setAlwaysOnTop(true);
            dialog.toFront();
            dialog.setAlwaysOnTop(false);
            DialogSupport.fireNewDialogOpened(new DialogOpenedEvent(dialog));
            dialog.setVisible(true);

            if (JXLoginPane.Status.SUCCEEDED != dialog.getStatus()) {
                isAuthenticationCanceled = true;
                throw (new CredentialsNotAvailableException());
            }
        } catch (RuntimeException rte) {
            LOG.error("Problem in GUICredProv", rte); // NOI18N
        }
    }

    @Override
    public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Authentication with username: " + name); // NOI18N
        }

        if (testConnection(new UsernamePasswordCredentials(name, new String(password).toCharArray()))) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credentials are valid for URL: " + url.toString());     // NOI18N
            }
            usernames.removeUserName(name);
            usernames.saveUserNames();
            usernames.addUserName(name);
            usernames.saveUserNames();
            setUsernamePassword(new UsernamePasswordCredentials(name, new String(password).toCharArray()));
            return true;
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Credentials are not valid for URL: " + url.toString()); // NOI18N
            }
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isAuthenticationCanceled() {
        return isAuthenticationCanceled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   creds  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean testConnection(final UsernamePasswordCredentials creds) {
        final Proxy proxy = ProxyHandler.getInstance().getProxy();

        final BasicCredentialsProvider credsProvider = new BasicCredentialsProvider();

        // Target (server) credentials
        credsProvider.setCredentials(
            new AuthScope(url.getHost(), -1),
            new org.apache.hc.client5.http.auth.UsernamePasswordCredentials(
                creds.getUserName(),
                creds.getPassword()));

        final RequestConfig.Builder requestConfig = RequestConfig.custom();

        // Proxy handling
        if ((proxy != null) && proxy.isValid() && proxy.isEnabledFor(url.getHost())) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("proxyIs Set");                  // NOI18N
                LOG.debug("ProxyHost:" + proxy.getHost()); // NOI18N
                LOG.debug("ProxyPort:" + proxy.getPort()); // NOI18N
            }

            final HttpHost proxyHost = new HttpHost(proxy.getHost(), proxy.getPort());
            requestConfig.setProxy(proxyHost);

            if ((proxy.getUsername() != null) && (proxy.getPassword() != null)) {
                credsProvider.setCredentials(
                    new AuthScope(proxy.getHost(), proxy.getPort()),
                    new NTCredentials(
                        proxy.getUsername(),
                        proxy.getPassword().toCharArray(),
                        null,
                        proxy.getDomain()));
            }
        }

        try(final CloseableHttpClient client = HttpClients.custom().setDefaultCredentialsProvider(credsProvider)
                        .setDefaultRequestConfig(requestConfig.build()).build()) {
            final HttpGet request = new HttpGet(url.toString());

            try(final CloseableHttpResponse response = client.execute(request)) {
                final int statusCode = response.getCode();

                if (statusCode != HttpStatus.SC_UNAUTHORIZED) {
                    return true;
                } else {
                    usernames.removeUserName(creds.getUserName());
                    return false;
                }
            }
        } catch (IOException e) {
            LOG.error("Connection test failed", e);
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  title  DOCUMENT ME!
     */
    public void setTitle(final String title) {
        this.title = title;
    }
}
