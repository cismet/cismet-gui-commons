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
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class RGBAdjustFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    public float rFactor;
    public float gFactor;
    public float bFactor;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RGBAdjustFilter object.
     */
    public RGBAdjustFilter() {
        this(0, 0, 0);
    }

    /**
     * Creates a new RGBAdjustFilter object.
     *
     * @param  r  DOCUMENT ME!
     * @param  g  DOCUMENT ME!
     * @param  b  DOCUMENT ME!
     */
    public RGBAdjustFilter(final float r, final float g, final float b) {
        rFactor = 1 + r;
        gFactor = 1 + g;
        bFactor = 1 + b;
        canFilterIndexColorModel = true;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  rFactor  DOCUMENT ME!
     */
    public void setRFactor(final float rFactor) {
        this.rFactor = 1 + rFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRFactor() {
        return rFactor - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  gFactor  DOCUMENT ME!
     */
    public void setGFactor(final float gFactor) {
        this.gFactor = 1 + gFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getGFactor() {
        return gFactor - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bFactor  DOCUMENT ME!
     */
    public void setBFactor(final float bFactor) {
        this.bFactor = 1 + bFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBFactor() {
        return bFactor - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int[] getLUT() {
        final int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = filterRGB(0, 0, (i << 24) | (i << 16) | (i << 8) | i);
        }
        return lut;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = PixelUtils.clamp((int)(r * rFactor));
        g = PixelUtils.clamp((int)(g * gFactor));
        b = PixelUtils.clamp((int)(b * bFactor));
        return a | (r << 16) | (g << 8) | b;
    }

    @Override
    public String toString() {
        return "Colors/Adjust RGB..."; // NOI18N
    }
}
