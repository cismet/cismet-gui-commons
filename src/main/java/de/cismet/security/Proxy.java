/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security;

import org.apache.log4j.Logger;

import java.util.prefs.Preferences;

import de.cismet.tools.PasswordEncrypter;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @author   martin.scholl@cismet.de
 * @version  $Revision$, $Date$
 */
public final class Proxy {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(Proxy.class);

    public static final String PROXY_HOST = "proxy.host";         // NOI18N
    public static final String PROXY_PORT = "proxy.port";         // NOI18N
    public static final String PROXY_USERNAME = "proxy.username"; // NOI18N
    public static final String PROXY_PASSWORD = "proxy.password"; // NOI18N
    public static final String PROXY_DOMAIN = "proxy.domain";     // NOI18N
    public static final String PROXY_ENABLED = "proxy.enabled";   // NOI18N

    public static final String SYSTEM_PROXY_HOST = "http.proxyHost";         // NOI18N
    public static final String SYSTEM_PROXY_PORT = "http.proxyPort";         // NOI18N
    public static final String SYSTEM_PROXY_USERNAME = "http.proxyUsername"; // NOI18N
    public static final String SYSTEM_PROXY_PASSWORD = "http.proxyPassword"; // NOI18N
    public static final String SYSTEM_PROXY_DOMAIN = "http.proxyDomain";     // NOI18N

    public static final String SYSTEM_PROXY_SET = "proxySet"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private transient String host;
    private transient int port;
    private transient String username;
    private transient String password;
    private transient String domain;
    private transient boolean enabled;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProxyConfig object.
     */
    public Proxy() {
        this(null, -1, null, null, null, false);
    }

    /**
     * Creates a new Proxy object.
     *
     * @param  host  DOCUMENT ME!
     * @param  port  DOCUMENT ME!
     */
    public Proxy(final String host, final int port) {
        this(host, port, null, null, null, true);
    }

    /**
     * Creates a new ProxyConfig object.
     *
     * @param  host      proxyURL DOCUMENT ME!
     * @param  port      DOCUMENT ME!
     * @param  username  DOCUMENT ME!
     * @param  password  DOCUMENT ME!
     */
    public Proxy(final String host, final int port, final String username, final String password) {
        this(host, port, username, password, null, true);
    }

    /**
     * Creates a new ProxyConfig object.
     *
     * @param  host      proxyURL DOCUMENT ME!
     * @param  port      computerName DOCUMENT ME!
     * @param  username  DOCUMENT ME!
     * @param  password  DOCUMENT ME!
     * @param  domain    DOCUMENT ME!
     * @param  enabled   DOCUMENT ME!
     */
    public Proxy(final String host,
            final int port,
            final String username,
            final String password,
            final String domain,
            final boolean enabled) {
        setHost(host);
        this.port = port;
        this.enabled = enabled;
        setUsername(username);
        setPassword(password);
        setDomain(domain);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getHost() {
        return host;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  host  DOCUMENT ME!
     */
    public void setHost(final String host) {
        this.host = ((host == null) || host.isEmpty()) ? null : host;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPort() {
        return port;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  port  DOCUMENT ME!
     */
    public void setPort(final int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Proxy: " + host + ":" + port + " | username: " + username + " | password: " // NOI18N
                    + ((password == null) ? null : "<invisible>") + " | domain: " + domain; // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getDomain() {
        return domain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  domain  DOCUMENT ME!
     */
    public void setDomain(final String domain) {
        this.domain = ((domain == null) || domain.isEmpty()) ? null : domain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPassword() {
        return password;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  password  DOCUMENT ME!
     */
    public void setPassword(final String password) {
        this.password = ((password == null) || password.isEmpty()) ? null : password;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getUsername() {
        return username;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  username  DOCUMENT ME!
     */
    public void setUsername(final String username) {
        this.username = ((username == null) || username.isEmpty()) ? null : username;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  enabled  DOCUMENT ME!
     */
    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * DOCUMENT ME!
     */
    public void toPreferences() {
        toPreferences(this);
    }

    /**
     * Loads a <code>Proxy</code> instance from previously stored user preferences. If there are no host and port proxy
     * information <code>null</code> will be returned. If the return value is non-null at least the host and the port is
     * initialised. Username, Password and Domain may be null.
     *
     * @return  the user's proxy settings or null if no settings present
     */
    public static Proxy fromPreferences() {
        final Preferences prefs = Preferences.userNodeForPackage(Proxy.class);
        final String host = prefs.get(PROXY_HOST, null); // NOI18N
        final int port = prefs.getInt(PROXY_PORT, -1);

        final Proxy proxy;
        if ((host == null) || (port < 1)) {
            proxy = null;
        } else {
            proxy = new Proxy();
            proxy.setHost(host);
            proxy.setPort(port);
            proxy.setUsername(prefs.get(PROXY_USERNAME, null));
            proxy.setPassword(PasswordEncrypter.decryptString(prefs.get(PROXY_PASSWORD, null)));
            proxy.setDomain(prefs.get(PROXY_DOMAIN, null));
            proxy.setEnabled(prefs.getBoolean(PROXY_ENABLED, false));
        }

        return proxy;
    }

    /**
     * Stores the given proxy in the user's preferences. If the proxy or the host is <code>null</code> or empty or the
     * port is not greater than 0 all proxy entries will be removed.
     *
     * @param  proxy  the proxy to store
     */
    public static void toPreferences(final Proxy proxy) {
        final Preferences prefs = Preferences.userNodeForPackage(Proxy.class);

        if ((proxy == null) || (proxy.getHost() == null) || proxy.getHost().isEmpty() || (proxy.getPort() < 1)) {
            // won't use clear since we don't know if anybody else stored preferences for this package
            prefs.remove(PROXY_HOST);
            prefs.remove(PROXY_PORT);
            prefs.remove(PROXY_USERNAME);
            prefs.remove(PROXY_PASSWORD);
            prefs.remove(PROXY_DOMAIN);
            prefs.remove(PROXY_ENABLED);
        } else {
            prefs.put(PROXY_HOST, proxy.getHost());
            prefs.putInt(PROXY_PORT, proxy.getPort());
            if (proxy.getUsername() == null) {
                prefs.remove(PROXY_USERNAME);
            } else {
                prefs.put(PROXY_USERNAME, proxy.getUsername());
            }
            if (proxy.getPassword() == null) {
                prefs.remove(PROXY_PASSWORD);
            } else {
                prefs.put(PROXY_PASSWORD, PasswordEncrypter.encryptString(proxy.getPassword()));
            }
            if (proxy.getDomain() == null) {
                prefs.remove(PROXY_DOMAIN);
            } else {
                prefs.put(PROXY_DOMAIN, proxy.getDomain());
            }
            prefs.put(PROXY_ENABLED, Boolean.toString(proxy.isEnabled()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Proxy fromSystem() {
        if (Boolean.getBoolean(System.getProperty(SYSTEM_PROXY_SET))) {
            final String host = System.getProperty(SYSTEM_PROXY_HOST);
            final String portString = System.getProperty(SYSTEM_PROXY_PORT);
            if ((host != null) && (portString != null)) {
                try {
                    final int port = Integer.valueOf(portString);
                    final String username = System.getProperty(SYSTEM_PROXY_USERNAME);
                    final String password = PasswordEncrypter.decryptString(System.getProperty(SYSTEM_PROXY_PASSWORD));
                    final String domain = System.getProperty(SYSTEM_PROXY_DOMAIN);

                    return new Proxy(host, port, username, password, domain, true);
                } catch (final NumberFormatException e) {
                    LOG.error("error during creation of proxy from system properties", e); // NOI18N
                }
            }
        }

        return null;
    }
}
