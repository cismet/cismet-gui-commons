/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.MemoryCacheSeekableStream;
import com.sun.media.jai.codec.SeekableStream;

import org.openide.util.Exceptions;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.lang.ref.SoftReference;

import java.net.URL;

import javax.media.jai.RenderedImageAdapter;

import de.cismet.security.WebAccessManager;

import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.MissingArgumentException;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.RequestFailedException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MultiPagePictureReader {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(MultiPagePictureReader.class);
    private static final String CODEC_JPEG = "jpeg"; // NOI18N
    private static final String CODEC_TIFF = "tiff"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final ImageDecoder decoder;
    private final int pageCount;
    private final SoftReference<BufferedImage>[] cache;
    private boolean caching = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageFile  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public MultiPagePictureReader(final File imageFile) throws IOException {
        if ((imageFile != null) && imageFile.isFile()) {
            final String codec = getCodecString(imageFile);
            if (codec != null) {
                final SeekableStream ss = new FileSeekableStream(imageFile);
                decoder = ImageCodec.createImageDecoder(codec, ss, null);
                pageCount = decoder.getNumPages();
                cache = new SoftReference[pageCount];
                for (int i = 0; i < cache.length; ++i) {
                    cache[i] = new SoftReference<BufferedImage>(null);
                }
            } else {
                throw new IOException("Unsupported filetype: " + imageFile.getAbsolutePath()
                            + " is not a tiff or jpeg file!");          // NOI18N
            }
        } else {
            throw new IOException("Could not open file: " + imageFile); // NOI18N
        }
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageURL  DOCUMENT ME!
     *
     * @throws  IOException               DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public MultiPagePictureReader(final URL imageURL) throws IOException {
        if (imageURL == null) {
            throw new IllegalArgumentException("Cannot open a null URL.");
        }

        final String codec = getCodecString(imageURL.toExternalForm());

        if (codec == null) {
            throw new IOException("Unsupported filetype: '" + imageURL.toExternalForm()
                        + "' doesn't point to a tiff or jpeg file!");
        }

        final SeekableStream stream;
        try {
            stream = new MemoryCacheSeekableStream(WebAccessManager.getInstance().doRequest(imageURL));
        } catch (Exception ex) {
            throw new IOException("Could not open '" + imageURL.toExternalForm() + "'.");
        }

        decoder = ImageCodec.createImageDecoder(codec, stream, null);
        pageCount = decoder.getNumPages();
        cache = new SoftReference[pageCount];
        for (int i = 0; i < cache.length; ++i) {
            cache[i] = new SoftReference<BufferedImage>(null);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  enabled  DOCUMENT ME!
     */
    public void setCaching(final boolean enabled) {
        this.caching = enabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getCaching() {
        return this.caching;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   imageFile  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getCodecString(final File imageFile) {
        final String filename = imageFile.getName().toLowerCase();
        final String extension = filename.substring(filename.lastIndexOf(".") + 1); // NOI18N
        if (extension.matches("(tiff|tif)")) {                                      // NOI18N
            return CODEC_TIFF;
        } else if (extension.matches("(jpg|jpeg)")) {                               // NOI18N
            return CODEC_JPEG;
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   imagePath  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String getCodecString(final String imagePath) {
        final String filename = imagePath.toLowerCase();
        final String extension = filename.substring(filename.lastIndexOf(".") + 1); // NOI18N
        if (extension.matches("(tiff|tif)")) {                                      // NOI18N
            return CODEC_TIFF;
        } else if (extension.matches("(jpg|jpeg)")) {                               // NOI18N
            return CODEC_JPEG;
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   position  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private BufferedImage getFormCache(final int position) {
        final SoftReference<BufferedImage> cacheItem = cache[position];
        if (cacheItem != null) {
            return cacheItem.get();
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  position  DOCUMENT ME!
     * @param  image     DOCUMENT ME!
     */
    private void addToCache(final int position, final BufferedImage image) {
        final SoftReference<BufferedImage> newCacheItem = new SoftReference<BufferedImage>(image);
        cache[position] = newCacheItem;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public final int getNumberOfPages() throws IOException {
        return pageCount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   page  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public final BufferedImage loadPage(final int page) throws IOException {
        if ((page > -1) && (page < pageCount)) {
            BufferedImage result = getFormCache(page);
            if (result == null) {
                final RenderedImage renderImage = decoder.decodeAsRenderedImage(page);
                final RenderedImageAdapter imageAdapter = new RenderedImageAdapter(renderImage);
                result = imageAdapter.getAsBufferedImage();
                if (caching) {
                    addToCache(page, result);
                }
            }
            return result;
        } else {
            throw new IOException("Could not find page " + page + " in file. Range is [0.." + (pageCount - 1) + "]."); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     */
    public final void close() {
        try {
            decoder.getInputStream().close();
        } catch (IOException ex) {
            log.warn(ex, ex);
        }
    }
}
