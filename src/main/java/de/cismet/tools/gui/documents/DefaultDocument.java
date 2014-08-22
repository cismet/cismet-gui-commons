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
package de.cismet.tools.gui.documents;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

import org.jdesktop.swingx.graphics.GraphicsUtilities;

import java.awt.Component;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import de.cismet.commons.security.WebDavClient;
import de.cismet.commons.security.WebDavHelper;

import de.cismet.tools.gui.Static2DTools;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class DefaultDocument implements Document {

    //~ Instance fields --------------------------------------------------------

    String documentURI = null;
    Icon icon = null;
    Icon defaultIcon = null;
    String name = null;
    Image preview = null;
    String extension = null;
    WebDavClient webDavClient = null;
    Component parent = null;
    String webDavDirectory = null;

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultDocument object.
     *
     * @param  name         DOCUMENT ME!
     * @param  documentURI  DOCUMENT ME!
     */
    public DefaultDocument(final String name, final String documentURI) {
        this(name, documentURI, null, null, null);
    }

    /**
     * Creates a new DefaultDocument object.
     *
     * @param  name             DOCUMENT ME!
     * @param  documentURI      DOCUMENT ME!
     * @param  webDavClient     DOCUMENT ME!
     * @param  parent           DOCUMENT ME!
     * @param  webDavDirectory  DOCUMENT ME!
     */
    public DefaultDocument(final String name,
            final String documentURI,
            final WebDavClient webDavClient,
            final Component parent,
            final String webDavDirectory) {
        this.name = name;
        this.documentURI = documentURI;
        this.parent = parent;
        this.webDavClient = webDavClient;
        this.webDavDirectory = webDavDirectory;
        init();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void init() {
        final int from = documentURI.lastIndexOf(".") + 1; // NOI18N
        final int to = documentURI.length();

        extension = documentURI.substring(from, to).toLowerCase();
        final String path = "/de/cismet/tools/gui/documents/documenttypeicons/" + extension + ".png";                  // NOI18N
        final URL unknownURL = getClass().getResource("/de/cismet/tools/gui/documents/documenttypeicons/unknown.png"); // NOI18N
        final URL webURL = getClass().getResource("/de/cismet/tools/gui/documents/documenttypeicons/html.png");        // NOI18N
        final URL url = getClass().getResource(path);
        if (log.isDebugEnabled()) {
            log.debug(path);
        }
        if (url != null) {
            defaultIcon = new javax.swing.ImageIcon(url);
        } else if (documentURI.startsWith("http://")) {                                                                // NOI18N
            defaultIcon = new javax.swing.ImageIcon(webURL);
        } else {
            defaultIcon = new javax.swing.ImageIcon(unknownURL);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  documentURI  DOCUMENT ME!
     */
    public void setDocumentURI(final String documentURI) {
        this.documentURI = documentURI;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  icon  DOCUMENT ME!
     */
    public void setIcon(final Icon icon) {
        this.icon = icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  DOCUMENT ME!
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  preview  DOCUMENT ME!
     */
    public void setPreview(final Image preview) {
        this.preview = preview;
    }

    @Override
    public String getDocumentURI() {
        return documentURI;
    }

    @Override
    public Icon getIcon() {
        if (icon == null) {
            return defaultIcon;
        } else {
            return icon;
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Image getPreview(final int width, final int height) {
        ImageIcon ii = null;
        if (preview != null) {
            ii = new ImageIcon(preview);
        } else {
            if (extension.matches("jpg|jpeg|gif|png|bmp")) { // NOI18N

                try {
                    if (webDavClient != null) {
                        ii = new ImageIcon(WebDavHelper.downloadImageFromWebDAV(
                                    documentURI,
                                    webDavDirectory,
                                    webDavClient,
                                    parent));
                    } else {
                        ii = new ImageIcon(GraphicsUtilities.loadCompatibleImage(new URL(documentURI)));
                    }
                } catch (Exception e) {
                    log.warn(e, e);
                    try {
                        ii = new ImageIcon(documentURI); // First test : Local Filename
                    } catch (Exception e2) {
                        log.error(e2, e2);
                    }
                }
            } else if (extension.matches("pdf|PDF")) {   // NOI18N
                try {
                    ByteBuffer buf = null;
                    try {
                        final File file = new File(documentURI);
                        final RandomAccessFile raf = new RandomAccessFile(file, "r"); // NOI18N
                        final FileChannel channel = raf.getChannel();
                        buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                    } catch (Exception e) {
                        try {
                            final URL url = new URL(documentURI);
                            final HttpURLConnection httpConnection = (HttpURLConnection)url.openConnection();
                            final InputStream in = httpConnection.getInputStream();
                            final byte[] buffer = new byte[1024];
                            int bytes_read;
                            final ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();

                            while ((bytes_read = in.read(buffer)) != -1) {
                                bufferOut.write(buffer, 0, bytes_read);
                            }

                            final byte[] sresponse = bufferOut.toByteArray();
                            httpConnection.disconnect();
                            buf = ByteBuffer.wrap(bufferOut.toByteArray());
                        } catch (Exception e2) {
                        }
                    }

                    final PDFFile pdffile = new PDFFile(buf);

                    // draw the first page to an image
                    final PDFPage page = pdffile.getPage(0);

                    // get the width and height for the doc at the default zoom
                    final Rectangle rect = new Rectangle(
                            0,
                            0,
                            (int)page.getBBox().getWidth(),
                            (int)page.getBBox().getHeight());

                    // generate the image
                    final Image img = page.getImage(
                            rect.width,
                            rect.height, // width & height
                            rect, // clip rect
                            null, // null for the ImageObserver
                            true, // fill background with white
                            true  // block until drawing is done
                            );
                    ii = new ImageIcon(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (extension.matches("dxf|DXF")) { // NOI18N
            }
        }
        if ((ii != null) && (ii.getImage() != null) && (ii.getImage().getWidth(null) > 0)) {
            preview = ii.getImage();
            final BufferedImage in;
            try {
                in = Static2DTools.toCompatibleImage(ii.getImage());
            } catch (Exception ex) {
                log.error(ex, ex);
                return null;
            }
            // BufferedImage out=Static2DTools.getFasterScaledInstance(in, width, height,
            // RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            Image out = null;
            int newHeight = 0;
            int newWidth = 0;
            final double widthToHeightRatio = (double)ii.getIconWidth() / (double)ii.getIconHeight();
            if ((widthToHeightRatio / ((double)width / (double)height)) < 1) {
                // height is the critical value and must be shrinked. in german: bestimmer ;-)
                newHeight = height;
                newWidth = (int)(height * widthToHeightRatio);
            } else {
                // width is the critical value and must be shrinked. in german: bestimmer ;-)
                newWidth = width;
                newHeight = (int)((double)width / (double)widthToHeightRatio);
            }
            out = Static2DTools.getFasterScaledInstance(
                    in,
                    newWidth,
                    newHeight,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                    true);
//            out = in.getScaledInstance(newWidth, newHeight, 0);
            return out;
        }

        return null;
    }
}
