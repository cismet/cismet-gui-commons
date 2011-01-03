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
 * A filter which "dissolves" an image by thresholding the alpha channel with random numbers.
 *
 * @version  $Revision$, $Date$
 */
public class DissolveFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private float density = 1;
    private float softness = 0;
    private float minDensity;
    private float maxDensity;
    private Random randomNumbers;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DissolveFilter object.
     */
    public DissolveFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the density of the image in the range 0..1.
     *
     * @param      density  the density
     *
     * @see        #getDensity
     * @min-value  0
     * @max-value  1
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    /**
     * Get the density of the image.
     *
     * @return  the density
     *
     * @see     #setDensity
     */
    public float getDensity() {
        return density;
    }

    /**
     * Set the softness of the dissolve in the range 0..1.
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
     * Get the softness of the dissolve.
     *
     * @return  the softness
     *
     * @see     #setSoftness
     */
    public float getSoftness() {
        return softness;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        final float d = (1 - density) * (1 + softness);
        minDensity = d - softness;
        maxDensity = d;
        randomNumbers = new Random(0);
        return super.filter(src, dst);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = (rgb >> 24) & 0xff;
        final float v = randomNumbers.nextFloat();
        final float f = ImageMath.smoothStep(minDensity, maxDensity, v);
        return ((int)(a * f) << 24) | (rgb & 0x00ffffff);
    }

    @Override
    public String toString() {
        return "Stylize/Dissolve..."; // NOI18N
    }
}
