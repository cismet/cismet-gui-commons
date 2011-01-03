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
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ErodeAlphaFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    protected float radius = 5;

    private float threshold;
    private float softness = 0;
    private float lowerThreshold;
    private float upperThreshold;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ErodeAlphaFilter object.
     */
    public ErodeAlphaFilter() {
        this(3, 0.75f, 0);
    }

    /**
     * Creates a new ErodeAlphaFilter object.
     *
     * @param  radius     DOCUMENT ME!
     * @param  threshold  DOCUMENT ME!
     * @param  softness   DOCUMENT ME!
     */
    public ErodeAlphaFilter(final float radius, final float threshold, final float softness) {
        this.radius = radius;
        this.threshold = threshold;
        this.softness = softness;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  radius  DOCUMENT ME!
     */
    public void setRadius(final float radius) {
        this.radius = radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRadius() {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  threshold  DOCUMENT ME!
     */
    public void setThreshold(final float threshold) {
        this.threshold = threshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getThreshold() {
        return threshold;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  softness  DOCUMENT ME!
     */
    public void setSoftness(final float softness) {
        this.softness = softness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getSoftness() {
        return softness;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        dst = new GaussianFilter((int)radius).filter(src, null);
        lowerThreshold = 255 * (threshold - (softness * 0.5f));
        upperThreshold = 255 * (threshold + (softness * 0.5f));
        return super.filter(dst, dst);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        int a = (rgb >> 24) & 0xff;
        final int r = (rgb >> 16) & 0xff;
        final int g = (rgb >> 8) & 0xff;
        final int b = rgb & 0xff;
        if (a == 255) {
            return 0xffffffff;
        }
        final float f = ImageMath.smoothStep(lowerThreshold, upperThreshold, (float)a);
        a = (int)(f * 255);
        if (a < 0) {
            a = 0;
        } else if (a > 255) {
            a = 255;
        }
        return (a << 24) | 0xffffff;
    }

    @Override
    public String toString() {
        return "Alpha/Erode..."; // NOI18N
    }
}
