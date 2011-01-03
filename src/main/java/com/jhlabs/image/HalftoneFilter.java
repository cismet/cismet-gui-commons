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

import java.util.*;

/**
 * A filter which can be used to produce wipes by transferring the luma of a mask image into the alpha channel of the
 * source.
 *
 * @version  $Revision$, $Date$
 */
public class HalftoneFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private float density = 0;
    private float softness = 0;
    private boolean invert;
    private BufferedImage mask;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HalftoneFilter object.
     */
    public HalftoneFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the density of the image in the range 0..1.*arg density The density
     *
     * @param  density  DOCUMENT ME!
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getDensity() {
        return density;
    }

    /**
     * Set the softness of the effect in the range 0..1.
     *
     * @param      softness  the softness
     *
     * @see        #getSoftness
     * @min-value  0
     * @max-value  1
     */
    public void setSoftness(final float softness) {
        this.softness = softness;
    }

    /**
     * Get the softness of the effect.
     *
     * @return  the softness
     *
     * @see     #setSoftness
     */
    public float getSoftness() {
        return softness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mask  DOCUMENT ME!
     */
    public void setMask(final BufferedImage mask) {
        this.mask = mask;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BufferedImage getMask() {
        return mask;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  invert  DOCUMENT ME!
     */
    public void setInvert(final boolean invert) {
        this.invert = invert;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getInvert() {
        return invert;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        if (mask == null) {
            return dst;
        }

        final int maskWidth = mask.getWidth();
        final int maskHeight = mask.getHeight();

        final float d = density * (1 + softness);
        final float lower = 255 * (d - softness);
        final float upper = 255 * d;
        final float s = 255 * softness;

        final int[] inPixels = new int[width];
        final int[] maskPixels = new int[maskWidth];

        for (int y = 0; y < height; y++) {
            getRGB(src, 0, y, width, 1, inPixels);
            getRGB(mask, 0, y % maskHeight, maskWidth, 1, maskPixels);

            for (int x = 0; x < width; x++) {
                final int maskRGB = maskPixels[x % maskWidth];
                final int inRGB = inPixels[x];
                final int v = PixelUtils.brightness(maskRGB);
                final int iv = PixelUtils.brightness(inRGB);
                final float f = ImageMath.smoothStep(iv - s, iv + s, v);
                int a = (int)(255 * f);

                if (invert) {
                    a = 255 - a;
                }
//                              inPixels[x] = (a << 24) | (inRGB & 0x00ffffff);
                inPixels[x] = (inRGB & 0xff000000) | (a << 16) | (a << 8) | a;
            }

            setRGB(dst, 0, y, width, 1, inPixels);
        }

        return dst;
    }

    @Override
    public String toString() {
        return "Stylize/Halftone..."; // NOI18N
    }
}
