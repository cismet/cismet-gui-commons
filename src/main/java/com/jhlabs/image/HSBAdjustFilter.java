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
public class HSBAdjustFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    public float hFactor;
    public float sFactor;
    public float bFactor;
    private float[] hsb = new float[3];

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HSBAdjustFilter object.
     */
    public HSBAdjustFilter() {
        this(0, 0, 0);
    }

    /**
     * Creates a new HSBAdjustFilter object.
     *
     * @param  r  DOCUMENT ME!
     * @param  g  DOCUMENT ME!
     * @param  b  DOCUMENT ME!
     */
    public HSBAdjustFilter(final float r, final float g, final float b) {
        hFactor = r;
        sFactor = g;
        bFactor = b;
        canFilterIndexColorModel = true;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  hFactor  DOCUMENT ME!
     */
    public void setHFactor(final float hFactor) {
        this.hFactor = hFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getHFactor() {
        return hFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sFactor  DOCUMENT ME!
     */
    public void setSFactor(final float sFactor) {
        this.sFactor = sFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getSFactor() {
        return sFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bFactor  DOCUMENT ME!
     */
    public void setBFactor(final float bFactor) {
        this.bFactor = bFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBFactor() {
        return bFactor;
    }

    @Override
    public int filterRGB(final int x, final int y, int rgb) {
        final int a = rgb & 0xff000000;
        final int r = (rgb >> 16) & 0xff;
        final int g = (rgb >> 8) & 0xff;
        final int b = rgb & 0xff;
        Color.RGBtoHSB(r, g, b, hsb);
        hsb[0] += hFactor;
        while (hsb[0] < 0) {
            hsb[0] += Math.PI * 2;
        }
        hsb[1] += sFactor;
        if (hsb[1] < 0) {
            hsb[1] = 0;
        } else if (hsb[1] > 1.0) {
            hsb[1] = 1.0f;
        }
        hsb[2] += bFactor;
        if (hsb[2] < 0) {
            hsb[2] = 0;
        } else if (hsb[2] > 1.0) {
            hsb[2] = 1.0f;
        }
        rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
        return a | (rgb & 0xffffff);
    }

    @Override
    public String toString() {
        return "Colors/Adjust HSB..."; // NOI18N
    }
}
