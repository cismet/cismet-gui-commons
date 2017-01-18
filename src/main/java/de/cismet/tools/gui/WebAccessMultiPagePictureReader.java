/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.io.File;
import java.io.IOException;

import java.net.URL;

import de.cismet.security.WebAccessManager;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class WebAccessMultiPagePictureReader extends de.cismet.commons.utils.MultiPagePictureReader {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WebAccessMultiPagePictureReader object.
     *
     * @param   imageFile  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public WebAccessMultiPagePictureReader(final File imageFile) throws IOException {
        super(imageFile, WebAccessManager.getInstance());
    }

    /**
     * Creates a new WebAccessMultiPagePictureReader object.
     *
     * @param   imageURL  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public WebAccessMultiPagePictureReader(final URL imageURL) throws IOException {
        super(imageURL, WebAccessManager.getInstance());
    }

    /**
     * Creates a new WebAccessMultiPagePictureReader object.
     *
     * @param   imageFile      DOCUMENT ME!
     * @param   caching        DOCUMENT ME!
     * @param   checkHeapSize  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public WebAccessMultiPagePictureReader(final File imageFile, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        super(imageFile, caching, checkHeapSize, WebAccessManager.getInstance());
    }

    /**
     * Creates a new WebAccessMultiPagePictureReader object.
     *
     * @param   imageURL       DOCUMENT ME!
     * @param   caching        DOCUMENT ME!
     * @param   checkHeapSize  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public WebAccessMultiPagePictureReader(final URL imageURL, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        super(imageURL, caching, checkHeapSize, WebAccessManager.getInstance());
    }
}
