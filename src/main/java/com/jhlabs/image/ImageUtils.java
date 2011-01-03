/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.jhlabs.image;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.util.*;

/**
 * A class containing some static utility methods for dealing with BufferedImages.
 *
 * @version  $Revision$, $Date$
 */
public abstract class ImageUtils {

    //~ Static fields/initializers ---------------------------------------------

    private static BufferedImage backgroundImage = null;

    //~ Methods ----------------------------------------------------------------

    /**
     * Cretae a BufferedImage from an ImageProducer.
     *
     * @param   producer  the ImageProducer
     *
     * @return  a new TYPE_INT_ARGB BufferedImage
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public static BufferedImage createImage(final ImageProducer producer) {
        final PixelGrabber pg = new PixelGrabber(producer, 0, 0, -1, -1, null, 0, 0);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException("Image fetch interrupted"); // NOI18N
        }
        if ((pg.status() & ImageObserver.ABORT) != 0) {
            throw new RuntimeException("Image fetch aborted");     // NOI18N
        }
        if ((pg.status() & ImageObserver.ERROR) != 0) {
            throw new RuntimeException("Image fetch error");       // NOI18N
        }
        final BufferedImage p = new BufferedImage(pg.getWidth(), pg.getHeight(), BufferedImage.TYPE_INT_ARGB);
        p.setRGB(0, 0, pg.getWidth(), pg.getHeight(), (int[])pg.getPixels(), 0, pg.getWidth());
        return p;
    }

    /**
     * Convert an Image into a TYPE_INT_ARGB BufferedImage. If the image is already of this type, the original image is
     * returned unchanged.
     *
     * @param   image  the image to convert
     *
     * @return  the converted image
     */
    public static BufferedImage convertImageToARGB(final Image image) {
        if ((image instanceof BufferedImage) && (((BufferedImage)image).getType() == BufferedImage.TYPE_INT_ARGB)) {
            return (BufferedImage)image;
        }
        final BufferedImage p = new BufferedImage(image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = p.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return p;
    }

    /**
     * Returns a *copy* of a subimage of image. This avoids the performance problems associated with
     * BufferedImage.getSubimage.
     *
     * @param   image  the image
     * @param   x      the x position
     * @param   y      the y position
     * @param   w      the width
     * @param   h      the height
     *
     * @return  the subimage
     */
    public static BufferedImage getSubimage(final BufferedImage image,
            final int x,
            final int y,
            final int w,
            final int h) {
        final BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = newImage.createGraphics();
        g.drawRenderedImage(image, AffineTransform.getTranslateInstance(-x, -y));
        g.dispose();
        return newImage;
    }

    /**
     * Clones a BufferedImage.
     *
     * @param   image  the image to clone
     *
     * @return  the cloned image
     */
    public static BufferedImage cloneImage(final BufferedImage image) {
        final BufferedImage newImage = new BufferedImage(image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_ARGB);
        final Graphics2D g = newImage.createGraphics();
        g.drawRenderedImage(image, null);
        g.dispose();
        return newImage;
    }

    /**
     * Paint a check pattern, used for a background to indicate image transparency.
     *
     * @param  c       the component to draw into
     * @param  g       the Graphics objects
     * @param  x       the x position
     * @param  y       the y position
     * @param  width   the width
     * @param  height  the height
     */
    public static void paintCheckedBackground(final Component c,
            final Graphics g,
            int x,
            int y,
            final int width,
            final int height) {
        if (backgroundImage == null) {
            backgroundImage = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
            final Graphics bg = backgroundImage.createGraphics();
            for (int by = 0; by < 64; by += 8) {
                for (int bx = 0; bx < 64; bx += 8) {
                    bg.setColor((((bx ^ by) & 8) != 0) ? Color.lightGray : Color.white);
                    bg.fillRect(bx, by, 8, 8);
                }
            }
            bg.dispose();
        }

        if (backgroundImage != null) {
            final Shape saveClip = g.getClip();
            Rectangle r = g.getClipBounds();
            if (r == null) {
                r = new Rectangle(c.getSize());
            }
            r = r.intersection(new Rectangle(x, y, width, height));
            g.setClip(r);
            final int w = backgroundImage.getWidth();
            final int h = backgroundImage.getHeight();
            if ((w != -1) && (h != -1)) {
                final int x1 = (r.x / w) * w;
                final int y1 = (r.y / h) * h;
                final int x2 = ((r.x + r.width + w - 1) / w) * w;
                final int y2 = ((r.y + r.height + h - 1) / h) * h;
                for (y = y1; y < y2; y += h) {
                    for (x = x1; x < x2; x += w) {
                        g.drawImage(backgroundImage, x, y, c);
                    }
                }
            }
            g.setClip(saveClip);
        }
    }

    /**
     * Calculates the bounds of the non-transparent parts of the given image.
     *
     * @param   p  the image
     *
     * @return  the bounds of the non-transparent area
     */
    public static Rectangle getSelectedBounds(final BufferedImage p) {
        final int width = p.getWidth();
        final int height = p.getHeight();
        int maxX = 0;
        int maxY = 0;
        int minX = width;
        int minY = height;
        boolean anySelected = false;
        int y1;
        int[] pixels = null;

        for (y1 = height - 1; y1 >= 0; y1--) {
            pixels = getRGB(p, 0, y1, width, 1, pixels);
            for (int x = 0; x < minX; x++) {
                if ((pixels[x] & 0xff000000) != 0) {
                    minX = x;
                    maxY = y1;
                    anySelected = true;
                    break;
                }
            }
            for (int x = width - 1; x >= maxX; x--) {
                if ((pixels[x] & 0xff000000) != 0) {
                    maxX = x;
                    maxY = y1;
                    anySelected = true;
                    break;
                }
            }
            if (anySelected) {
                break;
            }
        }
        pixels = null;
        for (int y = 0; y < y1; y++) {
            pixels = getRGB(p, 0, y, width, 1, pixels);
            for (int x = 0; x < minX; x++) {
                if ((pixels[x] & 0xff000000) != 0) {
                    minX = x;
                    if (y < minY) {
                        minY = y;
                    }
                    anySelected = true;
                    break;
                }
            }
            for (int x = width - 1; x >= maxX; x--) {
                if ((pixels[x] & 0xff000000) != 0) {
                    maxX = x;
                    if (y < minY) {
                        minY = y;
                    }
                    anySelected = true;
                    break;
                }
            }
        }
        if (anySelected) {
            return new Rectangle(minX, minY, maxX - minX + 1, maxY - minY + 1);
        }
        return null;
    }

    /**
     * Compose src onto dst using the alpha of sel to interpolate between the two. I can't think of a way to do this
     * using AlphaComposite.
     *
     * @param  src  the source raster
     * @param  dst  the destination raster
     * @param  sel  the mask raster
     */
    public static void composeThroughMask(final Raster src, final WritableRaster dst, final Raster sel) {
        final int x = src.getMinX();
        int y = src.getMinY();
        final int w = src.getWidth();
        final int h = src.getHeight();

        int[] srcRGB = null;
        int[] selRGB = null;
        int[] dstRGB = null;

        for (int i = 0; i < h; i++) {
            srcRGB = src.getPixels(x, y, w, 1, srcRGB);
            selRGB = sel.getPixels(x, y, w, 1, selRGB);
            dstRGB = dst.getPixels(x, y, w, 1, dstRGB);

            int k = x;
            for (int j = 0; j < w; j++) {
                final int sr = srcRGB[k];
                final int dir = dstRGB[k];
                final int sg = srcRGB[k + 1];
                final int dig = dstRGB[k + 1];
                final int sb = srcRGB[k + 2];
                final int dib = dstRGB[k + 2];
                final int sa = srcRGB[k + 3];
                final int dia = dstRGB[k + 3];

                final float a = selRGB[k + 3] / 255f;
                final float ac = 1 - a;

                dstRGB[k] = (int)((a * sr) + (ac * dir));
                dstRGB[k + 1] = (int)((a * sg) + (ac * dig));
                dstRGB[k + 2] = (int)((a * sb) + (ac * dib));
                dstRGB[k + 3] = (int)((a * sa) + (ac * dia));
                k += 4;
            }

            dst.setPixels(x, y, w, 1, dstRGB);
            y++;
        }
    }

    /**
     * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance penalty of
     * BufferedImage.getRGB unmanaging the image.
     *
     * @param   image   a BufferedImage object
     * @param   x       the left edge of the pixel block
     * @param   y       the right edge of the pixel block
     * @param   width   the width of the pixel arry
     * @param   height  the height of the pixel arry
     * @param   pixels  the array to hold the returned pixels. May be null.
     *
     * @return  the pixels
     *
     * @see     #setRGB
     */
    public static int[] getRGB(final BufferedImage image,
            final int x,
            final int y,
            final int width,
            final int height,
            final int[] pixels) {
        final int type = image.getType();
        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            return (int[])image.getRaster().getDataElements(x, y, width, height, pixels);
        }
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }

    /**
     * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance penalty of
     * BufferedImage.setRGB unmanaging the image.
     *
     * @param  image   a BufferedImage object
     * @param  x       the left edge of the pixel block
     * @param  y       the right edge of the pixel block
     * @param  width   the width of the pixel arry
     * @param  height  the height of the pixel arry
     * @param  pixels  the array of pixels to set
     *
     * @see    #getRGB
     */
    public static void setRGB(final BufferedImage image,
            final int x,
            final int y,
            final int width,
            final int height,
            final int[] pixels) {
        final int type = image.getType();
        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            image.getRaster().setDataElements(x, y, width, height, pixels);
        } else {
            image.setRGB(x, y, width, height, pixels, 0, width);
        }
    }
}
