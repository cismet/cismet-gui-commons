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
 * A filter to posterize an image.
 *
 * @version  $Revision$, $Date$
 */
public class PosterizeFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int numLevels;
    private int[] levels;
    private boolean initialized = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PosterizeFilter object.
     */
    public PosterizeFilter() {
        setNumLevels(6);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the number of levels in the output image.
     *
     * @param  numLevels  the number of levels
     *
     * @see    #getNumLevels
     */
    public void setNumLevels(final int numLevels) {
        this.numLevels = numLevels;
        initialized = false;
    }

    /**
     * Get the number of levels in the output image.
     *
     * @return  the number of levels
     *
     * @see     #setNumLevels
     */
    public int getNumLevels() {
        return numLevels;
    }

    /**
     * Initialize the filter.
     */
    protected void initialize() {
        levels = new int[256];
        if (numLevels != 1) {
            for (int i = 0; i < 256; i++) {
                levels[i] = 255 * (numLevels * i / 256) / (numLevels - 1);
            }
        }
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        if (!initialized) {
            initialized = true;
            initialize();
        }
        final int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = levels[r];
        g = levels[g];
        b = levels[b];
        return a | (r << 16) | (g << 8) | b;
    }

    @Override
    public String toString() {
        return "Colors/Posterize..."; // NOI18N
    }
}
