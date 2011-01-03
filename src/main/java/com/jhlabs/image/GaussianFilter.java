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

import java.awt.image.*;

/**
 * A filter which applies Gaussian blur to an image. This is a subclass of ConvolveFilter which simply creates a kernel
 * with a Gaussian distribution for blurring.
 *
 * @author   Jerry Huxtable
 * @version  $Revision$, $Date$
 */
public class GaussianFilter extends ConvolveFilter {

    //~ Instance fields --------------------------------------------------------

    /** The blur radius. */
    protected float radius;

    /** The convolution kernel. */
    protected Kernel kernel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a Gaussian filter.
     */
    public GaussianFilter() {
        this(2);
    }

    /**
     * Construct a Gaussian filter.
     *
     * @param  radius  blur radius in pixels
     */
    public GaussianFilter(final float radius) {
        setRadius(radius);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will
     * take.
     *
     * @param      radius  the radius of the blur in pixels.
     *
     * @see        #getRadius
     * @min-value  0
     * @max-value  100+
     */
    public void setRadius(final float radius) {
        this.radius = radius;
        kernel = makeKernel(radius);
    }

    /**
     * Get the radius of the kernel.
     *
     * @return  the radius
     *
     * @see     #setRadius
     */
    public float getRadius() {
        return radius;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        final int[] inPixels = new int[width * height];
        final int[] outPixels = new int[width * height];
        src.getRGB(0, 0, width, height, inPixels, 0, width);

        if (radius > 0) {
            convolveAndTranspose(
                kernel,
                inPixels,
                outPixels,
                width,
                height,
                alpha,
                alpha
                        && premultiplyAlpha,
                false,
                CLAMP_EDGES);
            convolveAndTranspose(
                kernel,
                outPixels,
                inPixels,
                height,
                width,
                alpha,
                false,
                alpha
                        && premultiplyAlpha,
                CLAMP_EDGES);
        }

        dst.setRGB(0, 0, width, height, inPixels, 0, width);
        return dst;
    }

    /**
     * Blur and transpose a block of ARGB pixels.
     *
     * @param  kernel         the blur kernel
     * @param  inPixels       the input pixels
     * @param  outPixels      the output pixels
     * @param  width          the width of the pixel array
     * @param  height         the height of the pixel array
     * @param  alpha          whether to blur the alpha channel
     * @param  premultiply    DOCUMENT ME!
     * @param  unpremultiply  DOCUMENT ME!
     * @param  edgeAction     what to do at the edges
     */
    public static void convolveAndTranspose(final Kernel kernel,
            final int[] inPixels,
            final int[] outPixels,
            final int width,
            final int height,
            final boolean alpha,
            final boolean premultiply,
            final boolean unpremultiply,
            final int edgeAction) {
        final float[] matrix = kernel.getKernelData(null);
        final int cols = kernel.getWidth();
        final int cols2 = cols / 2;

        for (int y = 0; y < height; y++) {
            int index = y;
            final int ioffset = y * width;
            for (int x = 0; x < width; x++) {
                float r = 0;
                float g = 0;
                float b = 0;
                float a = 0;
                final int moffset = cols2;
                for (int col = -cols2; col <= cols2; col++) {
                    final float f = matrix[moffset + col];

                    if (f != 0) {
                        int ix = x + col;
                        if (ix < 0) {
                            if (edgeAction == CLAMP_EDGES) {
                                ix = 0;
                            } else if (edgeAction == WRAP_EDGES) {
                                ix = (x + width) % width;
                            }
                        } else if (ix >= width) {
                            if (edgeAction == CLAMP_EDGES) {
                                ix = width - 1;
                            } else if (edgeAction == WRAP_EDGES) {
                                ix = (x + width) % width;
                            }
                        }
                        final int rgb = inPixels[ioffset + ix];
                        final int pa = (rgb >> 24) & 0xff;
                        int pr = (rgb >> 16) & 0xff;
                        int pg = (rgb >> 8) & 0xff;
                        int pb = rgb & 0xff;
                        if (premultiply) {
                            final float a255 = pa * (1.0f / 255.0f);
                            pr *= a255;
                            pg *= a255;
                            pb *= a255;
                        }
                        a += f * pa;
                        r += f * pr;
                        g += f * pg;
                        b += f * pb;
                    }
                }
                if (unpremultiply && (a != 0) && (a != 255)) {
                    final float f = 255.0f / a;
                    r *= f;
                    g *= f;
                    b *= f;
                }
                final int ia = alpha ? PixelUtils.clamp((int)(a + 0.5)) : 0xff;
                final int ir = PixelUtils.clamp((int)(r + 0.5));
                final int ig = PixelUtils.clamp((int)(g + 0.5));
                final int ib = PixelUtils.clamp((int)(b + 0.5));
                outPixels[index] = (ia << 24) | (ir << 16) | (ig << 8) | ib;
                index += height;
            }
        }
    }

    /**
     * Make a Gaussian blur kernel.
     *
     * @param   radius  the blur radius
     *
     * @return  the kernel
     */
    public static Kernel makeKernel(final float radius) {
        final int r = (int)Math.ceil(radius);
        final int rows = (r * 2) + 1;
        final float[] matrix = new float[rows];
        final float sigma = radius / 3;
        final float sigma22 = 2 * sigma * sigma;
        final float sigmaPi2 = 2 * ImageMath.PI * sigma;
        final float sqrtSigmaPi2 = (float)Math.sqrt(sigmaPi2);
        final float radius2 = radius * radius;
        float total = 0;
        int index = 0;
        for (int row = -r; row <= r; row++) {
            final float distance = row * row;
            if (distance > radius2) {
                matrix[index] = 0;
            } else {
                matrix[index] = (float)Math.exp(-(distance) / sigma22) / sqrtSigmaPi2;
            }
            total += matrix[index];
            index++;
        }
        for (int i = 0; i < rows; i++) {
            matrix[i] /= total;
        }

        return new Kernel(rows, 1, matrix);
    }

    @Override
    public String toString() {
        return "Blur/Gaussian Blur..."; // NOI18N
    }
}
