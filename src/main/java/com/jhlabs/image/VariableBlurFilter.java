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

/**
 * A filter which performs a box blur with a different blur radius at each pixel. The radius can either be specified by
 * providing a blur mask image or by overriding the blurRadiusAt method.
 *
 * @version  $Revision$, $Date$
 */
public class VariableBlurFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private int hRadius = 1;
    private int vRadius = 1;
    private int iterations = 1;
    private BufferedImage blurMask;
    private boolean premultiplyAlpha = true;

    //~ Methods ----------------------------------------------------------------

    /**
     * Set whether to premultiply the alpha channel.
     *
     * @param  premultiplyAlpha  true to premultiply the alpha
     *
     * @see    #getPremultiplyAlpha
     */
    public void setPremultiplyAlpha(final boolean premultiplyAlpha) {
        this.premultiplyAlpha = premultiplyAlpha;
    }

    /**
     * Get whether to premultiply the alpha channel.
     *
     * @return  true to premultiply the alpha
     *
     * @see     #setPremultiplyAlpha
     */
    public boolean getPremultiplyAlpha() {
        return premultiplyAlpha;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        }

        final int[] inPixels = new int[width * height];
        final int[] outPixels = new int[width * height];
        getRGB(src, 0, 0, width, height, inPixels);

        if (premultiplyAlpha) {
            ImageMath.premultiply(inPixels, 0, inPixels.length);
        }
        for (int i = 0; i < iterations; i++) {
            blur(inPixels, outPixels, width, height, hRadius, 1);
            blur(outPixels, inPixels, height, width, vRadius, 2);
        }
        if (premultiplyAlpha) {
            ImageMath.unpremultiply(inPixels, 0, inPixels.length);
        }

        setRGB(dst, 0, 0, width, height, inPixels);
        return dst;
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
     * DOCUMENT ME!
     *
     * @param  in      DOCUMENT ME!
     * @param  out     DOCUMENT ME!
     * @param  width   DOCUMENT ME!
     * @param  height  DOCUMENT ME!
     * @param  radius  DOCUMENT ME!
     * @param  pass    DOCUMENT ME!
     */
    public void blur(final int[] in,
            final int[] out,
            final int width,
            final int height,
            final int radius,
            final int pass) {
        final int widthMinus1 = width - 1;
        final int[] r = new int[width];
        final int[] g = new int[width];
        final int[] b = new int[width];
        final int[] a = new int[width];
        final int[] mask = new int[width];

        int inIndex = 0;

        for (int y = 0; y < height; y++) {
            int outIndex = y;

            if (blurMask != null) {
                if (pass == 1) {
                    getRGB(blurMask, 0, y, width, 1, mask);
                } else {
                    getRGB(blurMask, y, 0, 1, width, mask);
                }
            }

            for (int x = 0; x < width; x++) {
                final int argb = in[inIndex + x];
                a[x] = (argb >> 24) & 0xff;
                r[x] = (argb >> 16) & 0xff;
                g[x] = (argb >> 8) & 0xff;
                b[x] = argb & 0xff;
                if (x != 0) {
                    a[x] += a[x - 1];
                    r[x] += r[x - 1];
                    g[x] += g[x - 1];
                    b[x] += b[x - 1];
                }
            }

            for (int x = 0; x < width; x++) {
                // Get the blur radius at x, y
                int ra;
                if (blurMask != null) {
                    if (pass == 1) {
                        ra = (int)((mask[x] & 0xff) * hRadius / 255f);
                    } else {
                        ra = (int)((mask[x] & 0xff) * vRadius / 255f);
                    }
                } else {
                    if (pass == 1) {
                        ra = (int)(blurRadiusAt(x, y, width, height) * hRadius);
                    } else {
                        ra = (int)(blurRadiusAt(y, x, height, width) * vRadius);
                    }
                }

                final int divisor = (2 * ra) + 1;
                int ta = 0;
                int tr = 0;
                int tg = 0;
                int tb = 0;
                int i1 = x + ra;
                if (i1 > widthMinus1) {
                    final int f = i1 - widthMinus1;
                    final int l = widthMinus1;
                    ta += (a[l] - a[l - 1]) * f;
                    tr += (r[l] - r[l - 1]) * f;
                    tg += (g[l] - g[l - 1]) * f;
                    tb += (b[l] - b[l - 1]) * f;
                    i1 = widthMinus1;
                }
                int i2 = x - ra - 1;
                if (i2 < 0) {
                    ta -= a[0] * i2;
                    tr -= r[0] * i2;
                    tg -= g[0] * i2;
                    tb -= b[0] * i2;
                    i2 = 0;
                }

                ta += a[i1] - a[i2];
                tr += r[i1] - r[i2];
                tg += g[i1] - g[i2];
                tb += b[i1] - b[i2];
                out[outIndex] = ((ta / divisor) << 24) | ((tr / divisor) << 16) | ((tg / divisor) << 8)
                            | (tb / divisor);

                outIndex += height;
            }
            inIndex += width;
        }
    }

    /**
     * Override this to get a different blur radius at eahc point.
     *
     * @param   x       the x coordinate
     * @param   y       the y coordinate
     * @param   width   the width of the image
     * @param   height  the height of the image
     *
     * @return  the blur radius
     */
    protected float blurRadiusAt(final int x, final int y, final int width, final int height) {
        return (float)x / width;
    }

    /**
     * Set the horizontal size of the blur.
     *
     * @param      hRadius  the radius of the blur in the horizontal direction
     *
     * @see        #getHRadius
     * @min-value  0
     */
    public void setHRadius(final int hRadius) {
        this.hRadius = hRadius;
    }

    /**
     * Get the horizontal size of the blur.
     *
     * @return  the radius of the blur in the horizontal direction
     *
     * @see     #setHRadius
     */
    public int getHRadius() {
        return hRadius;
    }

    /**
     * Set the vertical size of the blur.
     *
     * @param      vRadius  the radius of the blur in the vertical direction
     *
     * @see        #getVRadius
     * @min-value  0
     */
    public void setVRadius(final int vRadius) {
        this.vRadius = vRadius;
    }

    /**
     * Get the vertical size of the blur.
     *
     * @return  the radius of the blur in the vertical direction
     *
     * @see     #setVRadius
     */
    public int getVRadius() {
        return vRadius;
    }

    /**
     * Set the radius of the effect.
     *
     * @param      radius  the radius
     *
     * @see        #getRadius
     * @min-value  0
     */
    public void setRadius(final int radius) {
        this.hRadius = this.vRadius = radius;
    }

    /**
     * Get the radius of the effect.
     *
     * @return  the radius
     *
     * @see     #setRadius
     */
    public int getRadius() {
        return hRadius;
    }

    /**
     * Set the number of iterations the blur is performed.
     *
     * @param      iterations  the number of iterations
     *
     * @see        #getIterations
     * @min-value  0
     */
    public void setIterations(final int iterations) {
        this.iterations = iterations;
    }

    /**
     * Get the number of iterations the blur is performed.
     *
     * @return  the number of iterations
     *
     * @see     #setIterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Set the mask used to give the amount of blur at each point.
     *
     * @param  blurMask  the mask
     *
     * @see    #getBlurMask
     */
    public void setBlurMask(final BufferedImage blurMask) {
        this.blurMask = blurMask;
    }

    /**
     * Get the mask used to give the amount of blur at each point.
     *
     * @return  the mask
     *
     * @see     #setBlurMask
     */
    public BufferedImage getBlurMask() {
        return blurMask;
    }

    @Override
    public String toString() {
        return "Blur/Variable Blur..."; // NOI18N
    }
}
