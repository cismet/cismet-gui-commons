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
package de.cismet.security;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.CredentialsNotAvailableException;

import org.jdesktop.swingx.JXLoginPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.auth.DefaultUserNameStore;
import org.jdesktop.swingx.auth.LoginService;

import java.awt.Component;

import java.net.URL;

import java.util.prefs.Preferences;

import javax.swing.JFrame;

import de.cismet.tools.gui.DialogOpenedEvent;
import de.cismet.tools.gui.DialogSupport;
import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public abstract class PasswordDialog extends LoginService {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(PasswordDialog.class);

    //~ Instance fields --------------------------------------------------------

    protected DefaultUserNameStore usernames;
    protected Component parent;
    protected boolean isAuthenticationDone = false;
    protected URL url;
    private Preferences appPrefs = null;
    private UsernamePasswordCredentials creds;
    private JFrame parentFrame;
    private boolean isAuthenticationCanceled = false;
    private Object dummy = new Object();
    private String username = null;
    private String title;
    private String prefTitle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PasswordDialog object.
     *
     * @param  url  DOCUMENT ME!
     */
    public PasswordDialog(final URL url) {
        super();
        if (log.isDebugEnabled()) {
            log.debug("Creating new PaswordDialog Instance for URL: " + url.toString()); // NOI18N
        }
        this.url = url;
    }

    /**
     * Creates a new PasswordDialog object.
     *
     * @param  url              DOCUMENT ME!
     * @param  parentComponent  DOCUMENT ME!
     */
    public PasswordDialog(final URL url, final Component parentComponent) {
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
     * @param  creds  DOCUMENT ME!
     */
    public void setUsernamePassword(final UsernamePasswordCredentials creds) {
        this.creds = creds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  CredentialsNotAvailableException  DOCUMENT ME!
     */
    public UsernamePasswordCredentials getCredentials() throws CredentialsNotAvailableException {
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
            requestUsernamePassword();

            return creds;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  CredentialsNotAvailableException  DOCUMENT ME!
     */
    private void requestUsernamePassword() throws CredentialsNotAvailableException {
        if (log.isDebugEnabled()) {
            log.debug("requestUsernamePassword"); // NOI18N
        }
        final JXLoginPane login = new JXLoginPane(this, null, usernames);

        final String[] names = usernames.getUserNames();
        if (names.length != 0) {
            username = names[names.length - 1];
        }

        login.setUserName(username);
        title = WebAccessManager.getInstance().getServerAliasProperty(url.toString());
        if (title != null) {
            final String msg = org.openide.util.NbBundle.getMessage(
                    PasswordDialog.class,
                    "PasswordDialog.requestUsernamePassword().login.message");
            login.setMessage(msg + " \"" + title + "\" ");              // NOI18N
        } else {
            title = url.toString();
            if (title.startsWith("http://") && (title.length() > 21)) { // NOI18N
                title = title.substring(7, 21) + "...";                 // NOI18N
            } else if (title.length() > 14) {
                title = title.substring(0, 14) + "...";                 // NOI18N
            }

            final String msg = org.openide.util.NbBundle.getMessage(
                    PasswordDialog.class,
                    "PasswordDialog.requestUsernamePassword().login.message");
            login.setMessage(msg + "\n" + " \"" + title + "\" "); // NOI18N
        }

        if (log.isDebugEnabled()) {
            log.debug("parentFrame in GUICredentialprovider:" + parent); // NOI18N
        }
        final JXLoginPane.JXLoginDialog dialog = new JXLoginPane.JXLoginDialog((JFrame)parent, login);

        try {
            ((JXPanel)((JXPanel)login.getComponent(1)).getComponent(1)).getComponent(3).requestFocus();
        } catch (Exception skip) {
        }
        login.setVisible(true);
        dialog.setAlwaysOnTop(true);
        dialog.toFront();
        dialog.setAlwaysOnTop(false);
        DialogSupport.fireNewDialogOpened(new DialogOpenedEvent(dialog));
        dialog.setVisible(true);

        if (!isAuthenticationDone) {
            isAuthenticationCanceled = true;
            throw new CredentialsNotAvailableException();
        }
    }

    @Override
    public abstract boolean authenticate(String name, char[] password, String server) throws Exception;

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
     * @param  title  DOCUMENT ME!
     */
    public void setTitle(final String title) {
        this.title = title;
    }
}
