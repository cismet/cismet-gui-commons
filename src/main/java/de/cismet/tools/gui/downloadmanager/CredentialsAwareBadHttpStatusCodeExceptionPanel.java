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
package de.cismet.tools.gui.downloadmanager;

import java.awt.EventQueue;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JDialog;

import de.cismet.commons.security.exceptions.BadHttpStatusCodeException;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class CredentialsAwareBadHttpStatusCodeExceptionPanel extends BadHttpStatusCodeExceptionPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final String MASK = "***";

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CredentialsAwareBadHttpStatusCodeExceptionPanel object.
     *
     * @param  exception             DOCUMENT ME!
     * @param  userFilterRegexp      The key of the url parameter that represent the user
     * @param  passwordFilterRegexp  The key of the url paramter that represents the password
     */
    public CredentialsAwareBadHttpStatusCodeExceptionPanel(final BadHttpStatusCodeException exception,
            final String userFilterRegexp,
            final String passwordFilterRegexp) {
        super(exception);
        if (exception != null) {
            String requestedUri = exception.getRequestedURI();
            if (requestedUri != null) {
                final Pattern userPattern = Pattern.compile(userFilterRegexp + "=([^&]*)&");
//                Pattern.compile(userFilterRegexp+"([^&]*)&").matcher(requestedUri).start();
                Matcher m = userPattern.matcher(requestedUri);
                if (m.find()) {
                    String group = m.group();
                    group = group.replace(userFilterRegexp + "=", "");
                    group = group.replace("&", "");
                    requestedUri = requestedUri.replaceAll(group, MASK);
                }
                final Pattern pwPattern = Pattern.compile(passwordFilterRegexp + "=([^&]*)&");
                m = pwPattern.matcher(requestedUri);
                if (m.find()) {
                    String group = m.group();
                    group = group.replace(userFilterRegexp + "=", "");
                    group = group.replace("&", "");
                    requestedUri = requestedUri.replaceAll(group, MASK);
                }
                txaRequestedURI.setText(requestedUri);
            }
        }
    }
}
