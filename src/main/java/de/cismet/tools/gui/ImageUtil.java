/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 srichter
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.tools.gui;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.graphics.ShadowRenderer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import de.cismet.tools.ExifReader;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class ImageUtil {

    //~ Static fields/initializers ---------------------------------------------

    private static Logger LOG = Logger.getLogger(ImageUtil.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   bi         DOCUMENT ME!
     * @param   component  DOCUMENT ME!
     * @param   insetX     DOCUMENT ME!
     * @param   insetY     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Image adjustScale(final BufferedImage bi,
            final JComponent component,
            final int insetX,
            final int insetY) {
        final double scalex = (double)component.getWidth() / bi.getWidth();
        final double scaley = (double)component.getHeight() / bi.getHeight();
        final double scale = Math.min(scalex, scaley);
        if (scale <= 1d) {
            return bi.getScaledInstance((int)(bi.getWidth() * scale) - insetX,
                    (int)(bi.getHeight() * scale)
                            - insetY,
                    Image.SCALE_SMOOTH);
        } else {
            return bi;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   bi       DOCUMENT ME!
     * @param   targetW  DOCUMENT ME!
     * @param   targetH  DOCUMENT ME!
     * @param   insetX   DOCUMENT ME!
     * @param   insetY   DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Image adjustScale(final BufferedImage bi,
            final int targetW,
            final int targetH,
            final int insetX,
            final int insetY) {
        final double scalex = (double)targetW / bi.getWidth();
        final double scaley = (double)targetH / bi.getHeight();
        final double scale = Math.min(scalex, scaley);
        if (scale <= 1d) {
            return bi.getScaledInstance((int)(bi.getWidth() * scale) - insetX,
                    (int)(bi.getHeight() * scale)
                            - insetY,
                    Image.SCALE_SMOOTH);
        } else {
            return bi;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   origImage  DOCUMENT ME!
     * @param   maxWidth   DOCUMENT ME!
     * @param   maxHeight  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Image resizeOnScale(final Image origImage, final int maxWidth, final int maxHeight) {
        final int origWidth = origImage.getWidth(null);
        final int origHeight = origImage.getHeight(null);
        final double ratio = origWidth / (double)origHeight;
        final int resizedWidth;
        final int resizedHeight;
        if (ratio > (maxWidth / maxHeight)) {
            resizedWidth = maxWidth;
            resizedHeight = (int)Math.round(maxWidth / ratio);
        } else {
            resizedWidth = (int)Math.round(maxHeight * ratio);
            resizedHeight = maxHeight;
        }
        return origImage.getScaledInstance(resizedWidth, resizedHeight, Image.SCALE_SMOOTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   in           DOCUMENT ME!
     * @param   shadowPixel  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage generateShadow(final Image in, final int shadowPixel) {
        if (in == null) {
            return null;
        }
        final BufferedImage input;
        if (in instanceof BufferedImage) {
            input = (BufferedImage)in;
        } else {
            final BufferedImage temp = new BufferedImage(in.getWidth(null),
                    in.getHeight(null),
                    BufferedImage.TYPE_4BYTE_ABGR);
            final Graphics tg = temp.createGraphics();
            tg.drawImage(in, 0, 0, null);
            tg.dispose();
            input = temp;
        }
        if (shadowPixel < 1) {
            return input;
        }
        final ShadowRenderer renderer = new ShadowRenderer(shadowPixel, 0.5f, Color.BLACK);
        final BufferedImage shadow = renderer.createShadow(input);
        final BufferedImage result = new BufferedImage(input.getWidth() + (2 * shadowPixel),
                input.getHeight()
                        + (2 * shadowPixel),
                BufferedImage.TYPE_4BYTE_ABGR);
        final Graphics2D rg = result.createGraphics();
        rg.drawImage(shadow, 0, 0, null);
        rg.drawImage(input, 0, 0, null);
        rg.dispose();
        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   src      DOCUMENT ME!
     * @param   degrees  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static BufferedImage rotateImage(final BufferedImage src, final double degrees) {
        final float radianAngle = (float)Math.toRadians(degrees);
        final float sin = (float)Math.abs(Math.sin(radianAngle));
        final float cos = (float)Math.abs(Math.cos(radianAngle));

        final int width = src.getWidth();
        final int height = src.getHeight();
        final int newWidth = (int)Math.round((width * cos) + (height * sin));
        final int newHeight = (int)Math.round((height * cos) + (width * sin));

        final AffineTransform transform = AffineTransform.getTranslateInstance((newWidth - width) / 2,
                (newHeight - height)
                        / 2);
        transform.rotate(radianAngle, width / 2, height / 2);

        final BufferedImage result = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration()
                    .createCompatibleImage(newWidth, newHeight, Transparency.TRANSLUCENT);

        final Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.drawRenderedImage(src, transform);
        g.dispose();

        return result;
    }

    /**
     * Converts the given Image object to a BufferedImage object.
     *
     * @param   image  then image object
     *
     * @return  the BufferedImage object
     */
    public static BufferedImage toBufferedImage(final Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        final BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);

        final Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(image, 0, 0, null);
        bGr.dispose();

        return bufferedImage;
    }

    /**
     * Creates a horizontally mirrored image of the given image.
     *
     * @param   image  the image to mirror
     *
     * @return  the horizontally mirrored image
     */
    public static BufferedImage toHorizontallyMirroredImage(final BufferedImage image) {
        final BufferedImage mirroredImage = new BufferedImage(image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                mirroredImage.setRGB(image.getWidth() - x, y, image.getRGB(x, y));
            }
        }

        return mirroredImage;
    }

    /**
     * Creates a vertically mirrored image of the given image.
     *
     * @param   image  the image to mirror
     *
     * @return  the vertically mirrored image
     */
    public static BufferedImage toVerticallyMirroredImage(final BufferedImage image) {
        final BufferedImage mirroredImage = new BufferedImage(image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < image.getWidth(); ++x) {
            for (int y = 0; y < image.getHeight(); ++y) {
                mirroredImage.setRGB(x, image.getHeight() - y, image.getRGB(x, y));
            }
        }

        return mirroredImage;
    }

    /**
     * Loads the given image file and change the rotation mirroring according to the exif information.
     *
     * @param   imageFile  the image to adjust
     *
     * @return  the adjust image
     */
    public static Image loadImageAccordingToExifOrientation(final File imageFile) {
        Double rotation = null;
        ExifReader.Mirrored mirrored = ExifReader.Mirrored.NONE;

        try {
            final ExifReader reader = new ExifReader(imageFile);

            rotation = reader.getOrientationRotation();
            mirrored = reader.getKindOfMirrored();
        } catch (Exception e) {
            LOG.error("Error while reading exif information", e);
        }

        Image image = new ImageIcon(imageFile.getAbsolutePath()).getImage();

        if (image != null) {
            if ((rotation != null) && (rotation != 0.0)) {
                image = ImageUtil.rotateImage(toBufferedImage(image), rotation);
            }

            if (!mirrored.equals(ExifReader.Mirrored.NONE)) {
                if (mirrored.equals(ExifReader.Mirrored.HORIZONTAL)) {
                    image = toHorizontallyMirroredImage(toBufferedImage(image));
                } else {
                    image = toVerticallyMirroredImage(toBufferedImage(image));
                }
            }
        }

        return image;
    }
}
