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
 * A filter which adds random noise into an image.
 *
 * @version  $Revision$, $Date$
 */
public class NoiseFilter extends PointFilter {

    //~ Static fields/initializers ---------------------------------------------

    /** Gaussian distribution for the noise. */
    public static final int GAUSSIAN = 0;

    /** Uniform distribution for the noise. */
    public static final int UNIFORM = 1;

    //~ Instance fields --------------------------------------------------------

    private int amount = 25;
    private int distribution = UNIFORM;
    private boolean monochrome = false;
    private float density = 1;
    private Random randomNumbers = new Random();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NoiseFilter object.
     */
    public NoiseFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the amount of effect.
     *
     * @param      amount  the amount
     *
     * @see        #getAmount
     * @min-value  0
     * @max-value  1
     */
    public void setAmount(final int amount) {
        this.amount = amount;
    }

    /**
     * Get the amount of noise.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set the distribution of the noise.
     *
     * @param  distribution  the distribution
     *
     * @see    #getDistribution
     */
    public void setDistribution(final int distribution) {
        this.distribution = distribution;
    }

    /**
     * Get the distribution of the noise.
     *
     * @return  the distribution
     *
     * @see     #setDistribution
     */
    public int getDistribution() {
        return distribution;
    }

    /**
     * Set whether to use monochrome noise.
     *
     * @param  monochrome  true for monochrome noise
     *
     * @see    #getMonochrome
     */
    public void setMonochrome(final boolean monochrome) {
        this.monochrome = monochrome;
    }

    /**
     * Get whether to use monochrome noise.
     *
     * @return  true for monochrome noise
     *
     * @see     #setMonochrome
     */
    public boolean getMonochrome() {
        return monochrome;
    }

    /**
     * Set the density of the noise.
     *
     * @param  density  the density
     *
     * @see    #getDensity
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    /**
     * Get the density of the noise.
     *
     * @return  the density
     *
     * @see     #setDensity
     */
    public float getDensity() {
        return density;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int random(int x) {
        x += (int)(((distribution == GAUSSIAN) ? randomNumbers.nextGaussian() : ((2 * randomNumbers.nextFloat()) - 1))
                        * amount);
        if (x < 0) {
            x = 0;
        } else if (x > 0xff) {
            x = 0xff;
        }
        return x;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        if (randomNumbers.nextFloat() <= density) {
            final int a = rgb & 0xff000000;
            int r = (rgb >> 16) & 0xff;
            int g = (rgb >> 8) & 0xff;
            int b = rgb & 0xff;
            if (monochrome) {
                final int n = (int)(((distribution == GAUSSIAN) ? randomNumbers.nextGaussian()
                                                                : ((2 * randomNumbers.nextFloat()) - 1)) * amount);
                r = PixelUtils.clamp(r + n);
                g = PixelUtils.clamp(g + n);
                b = PixelUtils.clamp(b + n);
            } else {
                r = random(r);
                g = random(g);
                b = random(b);
            }
            return a | (r << 16) | (g << 8) | b;
        }
        return rgb;
    }

    @Override
    public String toString() {
        return "Stylize/Add Noise..."; // NOI18N
    }
}
