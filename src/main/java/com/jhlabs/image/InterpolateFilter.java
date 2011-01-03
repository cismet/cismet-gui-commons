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
import java.awt.image.*;

import java.util.*;

/**
 * A filter which interpolates betwen two images. You can set the interpolation factor outside the range 0 to 1 to
 * extrapolate images.
 *
 * @version  $Revision$, $Date$
 */
public class InterpolateFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private BufferedImage destination;
    private float interpolation;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new InterpolateFilter object.
     */
    public InterpolateFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the destination image.
     *
     * @param  destination  the destination image
     *
     * @see    #getDestination
     */
    public void setDestination(final BufferedImage destination) {
        this.destination = destination;
    }

    /**
     * Get the destination image.
     *
     * @return  the destination image
     *
     * @see     #setDestination
     */
    public BufferedImage getDestination() {
        return destination;
    }

    /**
     * Set the interpolation factor.
     *
     * @param  interpolation  the interpolation factor
     *
     * @see    #getInterpolation
     */
    public void setInterpolation(final float interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * Get the interpolation factor.
     *
     * @return  the interpolation factor
     *
     * @see     #setInterpolation
     */
    public float getInterpolation() {
        return interpolation;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        int width = src.getWidth();
        int height = src.getHeight();
        final int type = src.getType();
        final WritableRaster srcRaster = src.getRaster();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        final WritableRaster dstRaster = dst.getRaster();

        if (destination != null) {
            width = Math.min(width, destination.getWidth());
            height = Math.min(height, destination.getWidth());
            int[] pixels1 = null;
            int[] pixels2 = null;

            for (int y = 0; y < height; y++) {
                pixels1 = getRGB(src, 0, y, width, 1, pixels1);
                pixels2 = getRGB(destination, 0, y, width, 1, pixels2);
                for (int x = 0; x < width; x++) {
                    final int rgb1 = pixels1[x];
                    final int rgb2 = pixels2[x];
                    final int a1 = (rgb1 >> 24) & 0xff;
                    int r1 = (rgb1 >> 16) & 0xff;
                    int g1 = (rgb1 >> 8) & 0xff;
                    int b1 = rgb1 & 0xff;
                    final int a2 = (rgb2 >> 24) & 0xff;
                    final int r2 = (rgb2 >> 16) & 0xff;
                    final int g2 = (rgb2 >> 8) & 0xff;
                    final int b2 = rgb2 & 0xff;
                    r1 = PixelUtils.clamp(ImageMath.lerp(interpolation, r1, r2));
                    g1 = PixelUtils.clamp(ImageMath.lerp(interpolation, g1, g2));
                    b1 = PixelUtils.clamp(ImageMath.lerp(interpolation, b1, b2));
                    pixels1[x] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
                }
                setRGB(dst, 0, y, width, 1, pixels1);
            }
        }

        return dst;
    }

    @Override
    public String toString() {
        return "Effects/Interpolate..."; // NOI18N
    }
}
