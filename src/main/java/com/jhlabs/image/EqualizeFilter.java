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

/**
 * A filter to perform auto-equalization on an image.
 *
 * @version  $Revision$, $Date$
 */
public class EqualizeFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    private int[][] lut;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EqualizeFilter object.
     */
    public EqualizeFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        final Histogram histogram = new Histogram(inPixels, width, height, 0, width);

        int i;
        int j;

        if (histogram.getNumSamples() > 0) {
            final float scale = 255.0f / histogram.getNumSamples();
            lut = new int[3][256];
            for (i = 0; i < 3; i++) {
                lut[i][0] = histogram.getFrequency(i, 0);
                for (j = 1; j < 256; j++) {
                    lut[i][j] = lut[i][j - 1] + histogram.getFrequency(i, j);
                }
                for (j = 0; j < 256; j++) {
                    lut[i][j] = (int)Math.round(lut[i][j] * scale);
                }
            }
        } else {
            lut = null;
        }

        i = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                inPixels[i] = filterRGB(x, y, inPixels[i]);
                i++;
            }
        }
        lut = null;

        return inPixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x    DOCUMENT ME!
     * @param   y    DOCUMENT ME!
     * @param   rgb  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int filterRGB(final int x, final int y, final int rgb) {
        if (lut != null) {
            final int a = rgb & 0xff000000;
            final int r = lut[Histogram.RED][(rgb >> 16) & 0xff];
            final int g = lut[Histogram.GREEN][(rgb >> 8) & 0xff];
            final int b = lut[Histogram.BLUE][rgb & 0xff];

            return a | (r << 16) | (g << 8) | b;
        }
        return rgb;
    }

    @Override
    public String toString() {
        return "Colors/Equalize"; // NOI18N
    }
}
