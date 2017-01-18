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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
@Deprecated
public class MultiPagePictureReader extends WebAccessMultiPagePictureReader {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageFile  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Deprecated
    public MultiPagePictureReader(final File imageFile) throws IOException {
        super(imageFile);
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageURL  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Deprecated
    public MultiPagePictureReader(final URL imageURL) throws IOException {
        super(imageURL);
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageFile      DOCUMENT ME!
     * @param   caching        DOCUMENT ME!
     * @param   checkHeapSize  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Deprecated
    public MultiPagePictureReader(final File imageFile, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        super(imageFile, caching, checkHeapSize);
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageURL       DOCUMENT ME!
     * @param   caching        DOCUMENT ME!
     * @param   checkHeapSize  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    @Deprecated
    public MultiPagePictureReader(final URL imageURL, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        super(imageURL, caching, checkHeapSize);
    }
}
