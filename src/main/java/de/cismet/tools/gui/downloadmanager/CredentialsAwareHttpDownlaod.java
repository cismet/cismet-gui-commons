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

import java.net.URL;

import java.util.HashMap;

import javax.swing.JPanel;

import de.cismet.commons.security.exceptions.BadHttpStatusCodeException;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class CredentialsAwareHttpDownlaod extends HttpDownload {

    //~ Instance fields --------------------------------------------------------

    private String urlUserFilter;
    private String urlPwFilter;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CredentialsAwareHttpDownlaod object.
     *
     * @param  url            DOCUMENT ME!
     * @param  request        DOCUMENT ME!
     * @param  directory      DOCUMENT ME!
     * @param  title          DOCUMENT ME!
     * @param  filename       DOCUMENT ME!
     * @param  extension      DOCUMENT ME!
     * @param  urlUserFilter  DOCUMENT ME!
     * @param  urlPwFilter    DOCUMENT ME!
     */
    public CredentialsAwareHttpDownlaod(final URL url,
            final String request,
            final String directory,
            final String title,
            final String filename,
            final String extension,
            final String urlUserFilter,
            final String urlPwFilter) {
        super(url, request, directory, title, filename, extension);
        this.urlPwFilter = urlPwFilter;
        this.urlUserFilter = urlUserFilter;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public JPanel getExceptionPanel(final Exception exception) {
        if (exception instanceof BadHttpStatusCodeException) {
            return new CredentialsAwareBadHttpStatusCodeExceptionPanel((BadHttpStatusCodeException)exception,
                    urlUserFilter,
                    urlPwFilter);
        }

        return super.getExceptionPanel(exception);
    }
}
