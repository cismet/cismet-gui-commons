/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.documents;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import de.cismet.tools.gui.Static2DTools;
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
import org.jdesktop.swingx.graphics.GraphicsUtilities;

/**
 *
 * @author hell
 */
public class DefaultDocument implements Document {

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    String documentURI = null;
    Icon icon = null;
    Icon defaultIcon = null;
    String name = null;
    Image preview = null;
    String extension = null;

    public DefaultDocument(String name, String documentURI) {
        this.name = name;
        this.documentURI = documentURI;
        init();
    }

    private void init() {
        int from = documentURI.lastIndexOf(".") + 1;  //NOI18N
        int to = documentURI.length();

        extension = documentURI.substring(from, to).toLowerCase();
        String path = "/de/cismet/tools/gui/documents/documenttypeicons/" + extension + ".png";  //NOI18N
        URL unknownURL = getClass().getResource("/de/cismet/tools/gui/documents/documenttypeicons/unknown.png"); //NOI18N
        URL webURL = getClass().getResource("/de/cismet/tools/gui/documents/documenttypeicons/html.png");  //NOI18N
        URL url = getClass().getResource(path);
        log.debug(path);
        if (url != null) {
            defaultIcon = new javax.swing.ImageIcon(url);
        } else if (documentURI.startsWith("http://")) {  //NOI18N
            defaultIcon = new javax.swing.ImageIcon(webURL);
        } else {
            defaultIcon = new javax.swing.ImageIcon(unknownURL);
        }
    }

    public void setDocumentURI(String documentURI) {
        this.documentURI = documentURI;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPreview(Image preview) {
        this.preview = preview;
    }

    public String getDocumentURI() {
        return documentURI;
    }

    public Icon getIcon() {
        if (icon == null) {
            return defaultIcon;
        } else {
            return icon;
        }

    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Image getPreview(int width, int height) {
        ImageIcon ii = null;
        if (preview != null) {
            ii = new ImageIcon(preview);
        } else {

            if (extension.matches("jpg|jpeg|gif|png|bmp")) {  //NOI18N

                try {
                    ii = new ImageIcon(GraphicsUtilities.loadCompatibleImage(new URL(documentURI)));
                } catch (Exception e) {
                    log.warn(e, e);
                    try {
                        ii = new ImageIcon(documentURI); //First test : Local Filename
                    } catch (Exception e2) {
                        log.error(e2, e2);
                    }
                }
            } else if (extension.matches("pdf|PDF")) {  //NOI18N
                try {
                    ByteBuffer buf = null;
                    try {
                        File file = new File(documentURI);
                        RandomAccessFile raf = new RandomAccessFile(file, "r");  //NOI18N
                        FileChannel channel = raf.getChannel();
                        buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
                    } catch (Exception e) {
                        try {
                            URL url = new URL(documentURI);
                            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
                            InputStream in = httpConnection.getInputStream();
                            byte[] buffer = new byte[1024];
                            int bytes_read;
                            ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();

                            while ((bytes_read = in.read(buffer)) != -1) {
                                bufferOut.write(buffer, 0, bytes_read);
                            }

                            byte[] sresponse = bufferOut.toByteArray();
                            httpConnection.disconnect();
                            buf = ByteBuffer.wrap(bufferOut.toByteArray());

                        } catch (Exception e2) {
                        }
                    }


                    PDFFile pdffile = new PDFFile(buf);

                    // draw the first page to an image
                    PDFPage page = pdffile.getPage(0);

                    //get the width and height for the doc at the default zoom 
                    Rectangle rect = new Rectangle(0, 0,
                            (int) page.getBBox().getWidth(),
                            (int) page.getBBox().getHeight());

                    //generate the image
                    Image img = page.getImage(
                            rect.width, rect.height, //width & height
                            rect, // clip rect
                            null, // null for the ImageObserver
                            true, // fill background with white
                            true // block until drawing is done
                            );
                    ii = new ImageIcon(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (extension.matches("dxf|DXF")) {  //NOI18N
            }
        }
        if (ii != null && ii.getImage() != null && ii.getImage().getWidth(null) > 0) {
            preview = ii.getImage();
            BufferedImage in;
            try {
                in = Static2DTools.toCompatibleImage(ii.getImage());
            } catch (Exception ex) {
                log.error(ex, ex);
                return null;
            }
            //BufferedImage out=Static2DTools.getFasterScaledInstance(in, width, height, RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
            Image out = null;
            int newHeight = 0;
            int newWidth = 0;
            double widthToHeightRatio = (double) ii.getIconWidth() / (double) ii.getIconHeight();
            if (widthToHeightRatio / ((double) width / (double) height) < 1) {
                //height is the critical value and must be shrinked. in german: bestimmer ;-)
                newHeight = height;
                newWidth = (int) (height * widthToHeightRatio);

            } else {
                //width is the critical value and must be shrinked. in german: bestimmer ;-)
                newWidth = width;
                newHeight = (int) ((double) width / (double) widthToHeightRatio);

            }
            out = Static2DTools.getFasterScaledInstance(in, newWidth, newHeight, RenderingHints.VALUE_INTERPOLATION_BICUBIC, true);
//            out = in.getScaledInstance(newWidth, newHeight, 0);
            return out;
        }

        return null;
    }
}
