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
 * A filter which produces an image simulating brushed metal.
 *
 * @version  $Revision$, $Date$
 */
public class BrushedMetalFilter implements BufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private int radius = 10;
    private float amount = 0.1f;
    private int color = 0xff888888;
    private float shine = 0.1f;
    private boolean monochrome = true;
    private Random randomNumbers;

    //~ Constructors -----------------------------------------------------------

    /**
     * Constructs a BrushedMetalFilter object.
     */
    public BrushedMetalFilter() {
    }

    /**
     * Constructs a BrushedMetalFilter object.
     *
     * @param  color       an int specifying the metal color
     * @param  radius      an int specifying the blur size
     * @param  amount      a float specifying the amount of texture
     * @param  monochrome  a boolean -- true for monochrome texture
     * @param  shine       a float specifying the shine to add
     */
    public BrushedMetalFilter(final int color,
            final int radius,
            final float amount,
            final boolean monochrome,
            final float shine) {
        this.color = color;
        this.radius = radius;
        this.amount = amount;
        this.monochrome = monochrome;
        this.shine = shine;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        final int[] inPixels = new int[width];
        final int[] outPixels = new int[width];

        randomNumbers = new Random(0);
        final int a = color & 0xff000000;
        final int r = (color >> 16) & 0xff;
        final int g = (color >> 8) & 0xff;
        final int b = color & 0xff;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int tr = r;
                int tg = g;
                int tb = b;
                if (shine != 0) {
                    final int f = (int)(255 * shine * Math.sin((double)x / width * Math.PI));
                    tr += f;
                    tg += f;
                    tb += f;
                }
                if (monochrome) {
                    final int n = (int)(255 * ((2 * randomNumbers.nextFloat()) - 1) * amount);
                    inPixels[x] = a | (clamp(tr + n) << 16) | (clamp(tg + n) << 8) | clamp(tb + n);
                } else {
                    inPixels[x] = a | (random(tr) << 16) | (random(tg) << 8) | random(tb);
                }
            }

            if (radius != 0) {
                blur(inPixels, outPixels, width, radius);
                setRGB(dst, 0, y, width, 1, outPixels);
            } else {
                setRGB(dst, 0, y, width, 1, inPixels);
            }
        }
        return dst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int random(int x) {
        x += (int)(255 * ((2 * randomNumbers.nextFloat()) - 1) * amount);
        if (x < 0) {
            x = 0;
        } else if (x > 0xff) {
            x = 0xff;
        }
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int clamp(final int c) {
        if (c < 0) {
            return 0;
        }
        if (c > 255) {
            return 255;
        }
        return c;
    }

    /**
     * Return a mod b. This differs from the % operator with respect to negative numbers.
     *
     * @param   a  the dividend
     * @param   b  the divisor
     *
     * @return  a mod b
     */
    private static int mod(int a, final int b) {
        final int n = a / b;

        a -= n * b;
        if (a < 0) {
            return a + b;
        }
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  in      DOCUMENT ME!
     * @param  out     DOCUMENT ME!
     * @param  width   DOCUMENT ME!
     * @param  radius  DOCUMENT ME!
     */
    public void blur(final int[] in, final int[] out, final int width, final int radius) {
        final int widthMinus1 = width - 1;
        final int r2 = (2 * radius) + 1;
        int tr = 0;
        int tg = 0;
        int tb = 0;

        for (int i = -radius; i <= radius; i++) {
            final int rgb = in[mod(i, width)];
            tr += (rgb >> 16) & 0xff;
            tg += (rgb >> 8) & 0xff;
            tb += rgb & 0xff;
        }

        for (int x = 0; x < width; x++) {
            out[x] = 0xff000000 | ((tr / r2) << 16) | ((tg / r2) << 8) | (tb / r2);

            int i1 = x + radius + 1;
            if (i1 > widthMinus1) {
                i1 = mod(i1, width);
            }
            int i2 = x - radius;
            if (i2 < 0) {
                i2 = mod(i2, width);
            }
            final int rgb1 = in[i1];
            final int rgb2 = in[i2];

            tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
            tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
            tb += (rgb1 & 0xff) - (rgb2 & 0xff);
        }
    }

    /**
     * Set the horizontal size of the blur.
     *
     * @param      radius  the radius of the blur in the horizontal direction
     *
     * @see        #getRadius
     * @min-value  0
     * @max-value  100+
     */
    public void setRadius(final int radius) {
        this.radius = radius;
    }

    /**
     * Get the horizontal size of the blur.
     *
     * @return  the radius of the blur in the horizontal direction
     *
     * @see     #setRadius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Set the amount of noise to add in the range 0..1.
     *
     * @param      amount  the amount of noise
     *
     * @see        #getAmount
     * @min-value  0
     * @max-value  1
     */
    public void setAmount(final float amount) {
        this.amount = amount;
    }

    /**
     * Get the amount of noise to add.
     *
     * @return  the amount of noise
     *
     * @see     #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Set the amount of shine to add to the range 0..1.
     *
     * @param      shine  the amount of shine
     *
     * @see        #getShine
     * @min-value  0
     * @max-value  1
     */
    public void setShine(final float shine) {
        this.shine = shine;
    }

    /**
     * Get the amount of shine to add in the range 0..1.
     *
     * @return  the amount of shine
     *
     * @see     #setShine
     */
    public float getShine() {
        return shine;
    }

    /**
     * Set the color of the metal.
     *
     * @param  color  the color in ARGB form
     *
     * @see    #getColor
     */
    public void setColor(final int color) {
        this.color = color;
    }

    /**
     * Get the color of the metal.
     *
     * @return  the color in ARGB form
     *
     * @see     #setColor
     */
    public int getColor() {
        return color;
    }

    /**
     * Set the type of noise to add.
     *
     * @param  monochrome  true for monochrome noise
     *
     * @see    #getMonochrome
     */
    public void setMonochrome(final boolean monochrome) {
        this.monochrome = monochrome;
    }

    /**
     * Get the type of noise to add.
     *
     * @return  true for monochrome noise
     *
     * @see     #setMonochrome
     */
    public boolean getMonochrome() {
        return monochrome;
    }

    @Override
    public BufferedImage createCompatibleDestImage(final BufferedImage src, ColorModel dstCM) {
        if (dstCM == null) {
            dstCM = src.getColorModel();
        }
        return new BufferedImage(
                dstCM,
                dstCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()),
                dstCM.isAlphaPremultiplied(),
                null);
    }

    @Override
    public Rectangle2D getBounds2D(final BufferedImage src) {
        return new Rectangle(0, 0, src.getWidth(), src.getHeight());
    }

    @Override
    public Point2D getPoint2D(final Point2D srcPt, Point2D dstPt) {
        if (dstPt == null) {
            dstPt = new Point2D.Double();
        }
        dstPt.setLocation(srcPt.getX(), srcPt.getY());
        return dstPt;
    }

    @Override
    public RenderingHints getRenderingHints() {
        return null;
    }

    /**
     * A convenience method for setting ARGB pixels in an image. This tries to avoid the performance penalty of
     * BufferedImage.setRGB unmanaging the image.
     *
     * @param  image   DOCUMENT ME!
     * @param  x       DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     * @param  width   DOCUMENT ME!
     * @param  height  DOCUMENT ME!
     * @param  pixels  DOCUMENT ME!
     */
    private void setRGB(final BufferedImage image,
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

    @Override
    public String toString() {
        return "Texture/Brushed Metal..."; // NOI18N
    }
}
