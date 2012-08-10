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

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;

import java.io.File;
import java.io.IOException;

import java.lang.ref.SoftReference;

import java.net.URL;

import javax.media.jai.RenderedImageAdapter;

import de.cismet.security.WebAccessManager;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MultiPagePictureReader {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(MultiPagePictureReader.class);
    private static final int MB = 1024 * 1024;
    private static final String CODEC_JPEG = "jpeg"; // NOI18N
    private static final String CODEC_TIFF = "tiff"; // NOI18N

    //~ Instance fields --------------------------------------------------------

    private final ImageDecoder decoder;
    private final String pathOfImage;
    private final int pageCount;
    private final SoftReference<BufferedImage>[] cache;
    private final boolean caching;
    private final boolean checkHeapSize;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageFile  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public MultiPagePictureReader(final File imageFile) throws IOException {
        this(imageFile, true, false);
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageURL  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public MultiPagePictureReader(final URL imageURL) throws IOException {
        this(imageURL, true, false);
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
    public MultiPagePictureReader(final File imageFile, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        if ((imageFile == null) || !imageFile.isFile() || !imageFile.canRead()) {
            throw new IOException("Could not open file: " + imageFile); // NOI18N
        }

        final String codec = getCodecString(imageFile.getName());
        if (codec == null) {
            throw new IOException("Unsupported filetype: " + imageFile.getAbsolutePath()
                        + " is not a tiff or jpeg file!"); // NOI18N
        }

        pathOfImage = imageFile.getAbsolutePath();
        this.caching = caching;
        this.checkHeapSize = checkHeapSize;

        final SeekableStream ss = new FileSeekableStream(imageFile);
        decoder = ImageCodec.createImageDecoder(codec, ss, null);

        pageCount = decoder.getNumPages();

        if (this.caching) {
            cache = new SoftReference[pageCount];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new SoftReference<BufferedImage>(null);
            }
        } else {
            cache = null;
        }
    }

    /**
     * Creates a new MultiPagePictureReader object.
     *
     * @param   imageURL       DOCUMENT ME!
     * @param   caching        DOCUMENT ME!
     * @param   checkHeapSize  DOCUMENT ME!
     *
     * @throws  IOException               DOCUMENT ME!
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public MultiPagePictureReader(final URL imageURL, final boolean caching, final boolean checkHeapSize)
            throws IOException {
        if (imageURL == null) {
            throw new IllegalArgumentException("Cannot open a null URL.");
        }

        final String codec = getCodecString(imageURL.toExternalForm());

        if (codec == null) {
            throw new IOException("Unsupported filetype: '" + imageURL.toExternalForm()
                        + "' doesn't point to a tiff or jpeg file!");
        }

        pathOfImage = imageURL.toExternalForm();
        this.caching = caching;
        this.checkHeapSize = checkHeapSize;

        final SeekableStream stream;
        try {
            stream = new MemoryCacheSeekableStream(WebAccessManager.getInstance().doRequest(imageURL));
        } catch (final Exception ex) {
            throw new IOException("Could not open '" + imageURL.toExternalForm() + "'.", ex);
        }

        decoder = ImageCodec.createImageDecoder(codec, stream, null);

        pageCount = decoder.getNumPages();

        if (this.caching) {
            cache = new SoftReference[pageCount];
            for (int i = 0; i < cache.length; ++i) {
                cache[i] = new SoftReference<BufferedImage>(null);
            }
        } else {
            cache = null;
        }
    }

    //~ Methods ----------------------------------------------------------------

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
        } else if (extension.matches("(jpg|jpeg|jpe)")) {                           // NOI18N
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
    private BufferedImage getFromCache(final int position) {
        BufferedImage result = null;
        if (!caching || (cache == null) || (position < 0) || (position >= cache.length)) {
            return result;
        }

        final SoftReference<BufferedImage> cacheItem = cache[position];
        if (cacheItem != null) {
            result = cacheItem.get();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  position  DOCUMENT ME!
     * @param  image     DOCUMENT ME!
     */
    private void addToCache(final int position, final BufferedImage image) {
        cache[position] = new SoftReference<BufferedImage>(image);
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
        if ((page <= -1) || (page >= pageCount)) {
            throw new IOException("Could not find page " + page + " in file. Range is [0.." + (pageCount - 1) + "]."); // NOI18N
        }

        BufferedImage result = getFromCache(page);

        if (result != null) {
            return result;
        }

        int size = 0;
        final long freeMemory = Runtime.getRuntime().freeMemory() / MB;

        final RenderedImage renderImage = decoder.decodeAsRenderedImage(page);
        final RenderedImageAdapter imageAdapter = new RenderedImageAdapter(renderImage);
        final SampleModel sampleModel = renderImage.getSampleModel();

        if (checkHeapSize) {
            if (sampleModel != null) {
                final int[] sampleSize = sampleModel.getSampleSize();
                for (int i = 0; i < sampleSize.length; i++) {
                    size += sampleSize[i];
                }

                size *= sampleModel.getWidth() * sampleModel.getHeight();
            } else {
                // Assume 8 bits per pixel and 4 bands.
                size = 32;
                size *= renderImage.getWidth() * renderImage.getHeight();
            }

            // size is image size in bits, so we make MB out of it.
            size = size / (8 * MB);
        }

        if (checkHeapSize && (size > freeMemory)) {
            LOG.warn("Couldn't read page '" + page + "' from image '" + pathOfImage
                        + "', since there's no memory left.");
        } else {
            result = imageAdapter.getAsBufferedImage();
        }

        if (caching) {
            addToCache(page, result);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     */
    public final void close() {
        try {
            decoder.getInputStream().close();
        } catch (IOException ex) {
            LOG.warn(ex, ex);
        }
    }
}
