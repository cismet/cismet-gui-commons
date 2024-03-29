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

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;
import org.apache.commons.httpclient.auth.CredentialsProvider;
import org.apache.commons.httpclient.auth.NTLMScheme;
import org.apache.commons.httpclient.auth.RFC2617Scheme;
import org.apache.commons.httpclient.methods.GetMethod;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.DefaultUserNameStore;
import org.jdesktop.swingx.auth.LoginService;

import java.awt.Component;

import java.io.IOException;

import java.net.URL;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   Sebastian
 * @version  $Revision$, $Date$
 */
public class GUICredentialsProvider extends LoginService implements CredentialsProvider {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(GUICredentialsProvider.class);

    //~ Instance fields --------------------------------------------------------

    private DefaultUserNameStore usernames;
    private Preferences appPrefs = null;
    private UsernamePasswordCredentials creds;
    private Component parent;
    private JFrame parentFrame;
    private boolean isAuthenticationCanceled = false;
    private URL url;
    private Object dummy = new Object();
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
        if (log.isDebugEnabled()) {
            log.debug("Creating new Credential Provider Instance for URL: " + url.toString()); // NOI18N
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
    public Credentials getCredentials(
            final AuthScheme authscheme,
            final String host,
            final int port,
            final boolean proxy) throws CredentialsNotAvailableException {
        if (log.isDebugEnabled()) {
            log.debug("Credentials requested for :" + url.toString() + " alias: " + title);                    // NOI18N
        }
        usernames = new DefaultUserNameStore();
        appPrefs = Preferences.userNodeForPackage(this.getClass());
        usernames.setPreferences(appPrefs.node("loginURLHash" + Integer.toString(url.toString().hashCode()))); // NOI18N

        if (creds != null) {
            return creds;
        }

        synchronized (dummy) {
            if (creds != null) {
                return creds;
            }

            isAuthenticationCanceled = false;

            if (authscheme == null) {
                return null;
            }

            if (authscheme instanceof NTLMScheme) {
                requestUsernamePassword();

                return creds;
            } else if (authscheme instanceof RFC2617Scheme) {
                final String[] userPassword = addUserAndPasswordToUrlIfRequired(url.toString());

                if (userPassword != null) {
                    try {
                        if (authenticate(userPassword[0], userPassword[1].toCharArray(), null)) {
                            return creds;
                        }
                    } catch (Exception e) {
                        if (log.isDebugEnabled()) {
                            log.debug("Error during athentication with url user/password", e);
                        }
                    }
                }
                requestUsernamePassword();

                return creds;
            } else {
                throw (new CredentialsNotAvailableException(
                        "Unsupported authentication scheme: " // NOI18N
                                + authscheme.getSchemeName()));
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
    private String[] addUserAndPasswordToUrlIfRequired(final String url) {
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
            if (log.isDebugEnabled()) {
                log.debug("parentFrame in GUICredentialprovider:" + parent);                      // NOI18N
            }

            final JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog((JFrame)parent, login);
//            SwingUtilities.invokeLater(new Runnable() {
//
//                    @Override
//                    public void run() {
            try {
                ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
            } catch (Exception skip) {
            }

            dialog.setAlwaysOnTop(true);
            dialog.setVisible(true);
//                    }
//                });
//            while ((JXLoginPane.Status.NOT_STARTED == dialog.getStatus())
//                        || (JXLoginPane.Status.IN_PROGRESS == dialog.getStatus())) {
//                Thread.sleep(100);
//            }
            if (JXLoginPane.Status.SUCCEEDED != dialog.getStatus()) {
                isAuthenticationCanceled = true;
                throw (new CredentialsNotAvailableException());
            }
        } catch (RuntimeException rte) {
            log.error("Problem in GUICredProv", rte); // NOI18N
        }
    }

    @Override
    public boolean authenticate(final String name, final char[] password, final String server) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("Authentication with username: " + name); // NOI18N
        }

        if (testConnection(new UsernamePasswordCredentials(name, new String(password)))) {
            if (log.isDebugEnabled()) {
                log.debug("Credentials are valid for URL: " + url.toString());     // NOI18N
            }
            usernames.removeUserName(name);
            usernames.saveUserNames();
            usernames.addUserName(name);
            usernames.saveUserNames();
            setUsernamePassword(new UsernamePasswordCredentials(name, new String(password)));
            return true;
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Credentials are not valid for URL: " + url.toString()); // NOI18N
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
        final HttpClient client = new HttpClient();
        final Proxy proxy = ProxyHandler.getInstance().getProxy();

        if ((proxy != null) && proxy.isValid() && proxy.isEnabledFor(url.getHost())) { // NOI18N
            if (log.isDebugEnabled()) {
                log.debug("proxyIs Set");                                              // NOI18N
                log.debug("ProxyHost:" + System.getProperty("http.proxyHost"));        // NOI18N
            }
            if (log.isDebugEnabled()) {
                log.debug("ProxyPort:" + System.getProperty("http.proxyPort"));        // NOI18N
            }

            try {
                client.getHostConfiguration().setProxy(proxy.getHost(), // NOI18N
                    proxy.getPort()); // NOI18N
                if ((proxy.getUsername() != null) && (proxy.getPassword() != null)) {
                    final AuthScope authscope = new AuthScope(proxy.getHost(), proxy.getPort());
                    final Credentials credentials = new NTCredentials(proxy.getUsername(),
                            proxy.getPassword(),
                            "", // NOI18N
                            (proxy.getDomain() == null) ? "" : proxy.getDomain());
                    client.getState().setProxyCredentials(
                        authscope,
                        credentials);
                }
            } catch (Exception e) {
                log.error("Problem while setting proxy", e); // NOI18N
            }
        }

        final GetMethod method = new GetMethod(url.toString());
        client.getState().setCredentials(new AuthScope(url.getHost(), AuthScope.ANY_PORT, AuthScope.ANY_REALM), creds);
        method.setDoAuthentication(true);

        int statuscode = 200;

        try {
            statuscode = client.executeMethod(method);
        } catch (IOException ex) {
        }

        if (statuscode != HttpStatus.SC_UNAUTHORIZED) {
            method.releaseConnection();

            return true;
        } else {
            method.releaseConnection();
            usernames.removeUserName(creds.getUserName());

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
