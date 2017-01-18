/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.downloadmanager;

import java.io.FileNotFoundException;
import java.io.InputStream;

import java.net.URISyntaxException;
import java.net.URL;

import java.util.HashMap;

import de.cismet.commons.security.AccessHandler;

import de.cismet.security.WebAccessManager;

import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.RequestFailedException;

/**
 * The objects of this class represent a HTTP or a FTP download. The objects of this class are observed by the download
 * manager.
 *
 * @author   DOCUMENT ME!
 * @version  $Revision$, $Date$
 */
public class HttpOrFtpDownload extends HttpDownload {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HttpOrFtpDownload object.
     */
    public HttpOrFtpDownload() {
    }

    /**
     * Creates a new HttpOrFtpDownload object.
     *
     * @param  url        DOCUMENT ME!
     * @param  request    DOCUMENT ME!
     * @param  directory  DOCUMENT ME!
     * @param  title      DOCUMENT ME!
     * @param  filename   DOCUMENT ME!
     * @param  extension  DOCUMENT ME!
     */
    public HttpOrFtpDownload(final URL url,
            final String request,
            final String directory,
            final String title,
            final String filename,
            final String extension) {
        super(url, request, directory, title, filename, extension);
    }

    /**
     * Creates a new HttpOrFtpDownload object.
     *
     * @param  url        DOCUMENT ME!
     * @param  request    DOCUMENT ME!
     * @param  headers    DOCUMENT ME!
     * @param  directory  DOCUMENT ME!
     * @param  title      DOCUMENT ME!
     * @param  filename   DOCUMENT ME!
     * @param  extension  DOCUMENT ME!
     */
    public HttpOrFtpDownload(final URL url,
            final String request,
            final HashMap<String, String> headers,
            final String directory,
            final String title,
            final String filename,
            final String extension) {
        super(url, request, headers, directory, title, filename, extension);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected InputStream getUrlInputStreamWithWebAcessManager(final URL url) throws URISyntaxException,
        FileNotFoundException,
        AccessMethodIsNotSupportedException,
        RequestFailedException,
        NoHandlerForURLException,
        Exception {
        InputStream inputStream = null;
        if ("ftp".equalsIgnoreCase(url.getProtocol())) {
            WebAccessManager.getInstance().registerAccessHandler(url, AccessHandler.ACCESS_HANDLER_TYPES.FTP);
        }
        try {
            inputStream = super.getUrlInputStreamWithWebAcessManager(url);
        } finally {
            if ("ftp".equalsIgnoreCase(url.getProtocol())) {
                WebAccessManager.getInstance().deregisterAccessHandler(url);
            }
        }
        return inputStream;
    }
}
