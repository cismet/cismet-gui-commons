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
 * A filter which draws a gradient interpolated between four colors defined at the corners of the image.
 *
 * @version  $Revision$, $Date$
 */
public class FourColorFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int width;
    private int height;
    private int colorNW;
    private int colorNE;
    private int colorSW;
    private int colorSE;
    private int rNW;
    private int gNW;
    private int bNW;
    private int rNE;
    private int gNE;
    private int bNE;
    private int rSW;
    private int gSW;
    private int bSW;
    private int rSE;
    private int gSE;
    private int bSE;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FourColorFilter object.
     */
    public FourColorFilter() {
        setColorNW(0xffff0000);
        setColorNE(0xffff00ff);
        setColorSW(0xff0000ff);
        setColorSE(0xff00ffff);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColorNW(final int color) {
        this.colorNW = color;
        rNW = (color >> 16) & 0xff;
        gNW = (color >> 8) & 0xff;
        bNW = color & 0xff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColorNW() {
        return colorNW;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColorNE(final int color) {
        this.colorNE = color;
        rNE = (color >> 16) & 0xff;
        gNE = (color >> 8) & 0xff;
        bNE = color & 0xff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColorNE() {
        return colorNE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColorSW(final int color) {
        this.colorSW = color;
        rSW = (color >> 16) & 0xff;
        gSW = (color >> 8) & 0xff;
        bSW = color & 0xff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColorSW() {
        return colorSW;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColorSE(final int color) {
        this.colorSE = color;
        rSE = (color >> 16) & 0xff;
        gSE = (color >> 8) & 0xff;
        bSE = color & 0xff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColorSE() {
        return colorSE;
    }

    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
        super.setDimensions(width, height);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final float fx = (float)x / width;
        final float fy = (float)y / height;
        float p;
        float q;

        p = rNW + ((rNE - rNW) * fx);
        q = rSW + ((rSE - rSW) * fx);
        final int r = (int)(p + ((q - p) * fy) + 0.5f);

        p = gNW + ((gNE - gNW) * fx);
        q = gSW + ((gSE - gSW) * fx);
        final int g = (int)(p + ((q - p) * fy) + 0.5f);

        p = bNW + ((bNE - bNW) * fx);
        q = bSW + ((bSE - bSW) * fx);
        final int b = (int)(p + ((q - p) * fy) + 0.5f);

        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    @Override
    public String toString() {
        return "Texture/Four Color Fill..."; // NOI18N
    }
}
