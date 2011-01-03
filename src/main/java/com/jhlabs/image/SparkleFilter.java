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
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SparkleFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int rays = 50;
    private int radius = 25;
    private int amount = 50;
    private int color = 0xffffffff;
    private int randomness = 25;
    private int width;
    private int height;
    private int centreX;
    private int centreY;
    private long seed = 371;
    private float[] rayLengths;
    private Random randomNumbers = new Random();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SparkleFilter object.
     */
    public SparkleFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColor(final int color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  randomness  DOCUMENT ME!
     */
    public void setRandomness(final int randomness) {
        this.randomness = randomness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getRandomness() {
        return randomness;
    }

    /**
     * Set the amount of sparkle.
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
     * Get the amount of sparkle.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rays  DOCUMENT ME!
     */
    public void setRays(final int rays) {
        this.rays = rays;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getRays() {
        return rays;
    }

    /**
     * Set the radius of the effect.
     *
     * @param      radius  the radius
     *
     * @see        #getRadius
     * @min-value  0
     */
    public void setRadius(final int radius) {
        this.radius = radius;
    }

    /**
     * Get the radius of the effect.
     *
     * @return  the radius
     *
     * @see     #setRadius
     */
    public int getRadius() {
        return radius;
    }

    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
        centreX = width / 2;
        centreY = height / 2;
        super.setDimensions(width, height);
        randomNumbers.setSeed(seed);
        rayLengths = new float[rays];
        for (int i = 0; i < rays; i++) {
            rayLengths[i] = radius + (randomness / 100.0f * radius * (float)randomNumbers.nextGaussian());
        }
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final float dx = x - centreX;
        final float dy = y - centreY;
        final float distance = (dx * dx) + (dy * dy);
        final float angle = (float)Math.atan2(dy, dx);
        final float d = (angle + ImageMath.PI) / (ImageMath.TWO_PI) * rays;
        final int i = (int)d;
        float f = d - i;

        if (radius != 0) {
            final float length = ImageMath.lerp(f, rayLengths[i % rays], rayLengths[(i + 1) % rays]);
            float g = length * length / (distance + 0.0001f);
            g = (float)Math.pow(g, (100 - amount) / 50.0);
            f -= 0.5f;
//                      f *= amount/50.0f;
            f = 1 - (f * f);
            f *= g;
        }
        f = ImageMath.clamp(f, 0, 1);
        return ImageMath.mixColors(f, rgb, color);
    }

    @Override
    public String toString() {
        return "Stylize/Sparkle...";
    }
}
