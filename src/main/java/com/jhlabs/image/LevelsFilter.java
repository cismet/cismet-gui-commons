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
 * A filter which allows levels adjustment on an image.
 *
 * @version  $Revision$, $Date$
 */
public class LevelsFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    private int[][] lut;
    private float lowLevel = 0;
    private float highLevel = 1;
    private float lowOutputLevel = 0;
    private float highOutputLevel = 1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LevelsFilter object.
     */
    public LevelsFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  lowLevel  DOCUMENT ME!
     */
    public void setLowLevel(final float lowLevel) {
        this.lowLevel = lowLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getLowLevel() {
        return lowLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  highLevel  DOCUMENT ME!
     */
    public void setHighLevel(final float highLevel) {
        this.highLevel = highLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getHighLevel() {
        return highLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  lowOutputLevel  DOCUMENT ME!
     */
    public void setLowOutputLevel(final float lowOutputLevel) {
        this.lowOutputLevel = lowOutputLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getLowOutputLevel() {
        return lowOutputLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  highOutputLevel  DOCUMENT ME!
     */
    public void setHighOutputLevel(final float highOutputLevel) {
        this.highOutputLevel = highOutputLevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getHighOutputLevel() {
        return highOutputLevel;
    }

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

            final float low = lowLevel * 255;
            float high = highLevel * 255;
            if (low == high) {
                high++;
            }
            for (i = 0; i < 3; i++) {
                for (j = 0; j < 256; j++) {
                    lut[i][j] = PixelUtils.clamp((int)(255
                                        * (lowOutputLevel
                                            + ((highOutputLevel - lowOutputLevel) * (j - low) / (high - low)))));
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
    public int filterRGB(final int x, final int y, final int rgb) {
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
        return "Colors/Levels..."; // NOI18N
    }
}
