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

import com.jhlabs.math.*;

import java.awt.geom.*;
import java.awt.image.*;

import java.util.*;

/**
 * An experimental filter for rendering lens flares.
 *
 * @version  $Revision$, $Date$
 */
public class FlareFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int rays = 50;
    private float radius;
    private float baseAmount = 1.0f;
    private float ringAmount = 0.2f;
    private float rayAmount = 0.1f;
    private int color = 0xffffffff;
    private int width;
    private int height;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float ringWidth = 1.6f;

    private float linear = 0.03f;
    private float gauss = 0.006f;
    private float mix = 0.50f;
    private float falloff = 6.0f;
    private float sigma;

    private float icentreX;
    private float icentreY;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FlareFilter object.
     */
    public FlareFilter() {
        setRadius(50.0f);
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
     * @param  ringWidth  DOCUMENT ME!
     */
    public void setRingWidth(final float ringWidth) {
        this.ringWidth = ringWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRingWidth() {
        return ringWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  baseAmount  DOCUMENT ME!
     */
    public void setBaseAmount(final float baseAmount) {
        this.baseAmount = baseAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBaseAmount() {
        return baseAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ringAmount  DOCUMENT ME!
     */
    public void setRingAmount(final float ringAmount) {
        this.ringAmount = ringAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRingAmount() {
        return ringAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rayAmount  DOCUMENT ME!
     */
    public void setRayAmount(final float rayAmount) {
        this.rayAmount = rayAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRayAmount() {
        return rayAmount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  centre  DOCUMENT ME!
     */
    public void setCentre(final Point2D centre) {
        this.centreX = (float)centre.getX();
        this.centreY = (float)centre.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Point2D getCentre() {
        return new Point2D.Float(centreX, centreY);
    }

    /**
     * Set the radius of the effect.
     *
     * @param      radius  the radius
     *
     * @see        #getRadius
     * @min-value  0
     */
    public void setRadius(final float radius) {
        this.radius = radius;
        sigma = radius / 3;
    }

    /**
     * Get the radius of the effect.
     *
     * @return  the radius
     *
     * @see     #setRadius
     */
    public float getRadius() {
        return radius;
    }

    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
        icentreX = centreX * width;
        icentreY = centreY * height;
        super.setDimensions(width, height);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final float dx = x - icentreX;
        final float dy = y - icentreY;
        final float distance = (float)Math.sqrt((dx * dx) + (dy * dy));
        float a = ((float)Math.exp(-distance * distance * gauss) * mix)
                    + ((float)Math.exp(-distance * linear) * (1 - mix));
        float ring;

        a *= baseAmount;

        if (distance > (radius + ringWidth)) {
            a = ImageMath.lerp((distance - (radius + ringWidth)) / falloff, a, 0);
        }

        if ((distance < (radius - ringWidth)) || (distance > (radius + ringWidth))) {
            ring = 0;
        } else {
            ring = Math.abs(distance - radius) / ringWidth;
            ring = 1 - (ring * ring * (3 - (2 * ring)));
            ring *= ringAmount;
        }

        a += ring;

        float angle = (float)Math.atan2(dx, dy) + ImageMath.PI;
        angle = (ImageMath.mod((angle / ImageMath.PI * 17) + 1.0f + Noise.noise1(angle * 10), 1.0f) - 0.5f) * 2;
        angle = Math.abs(angle);
        angle = (float)Math.pow(angle, 5.0);

        final float b = rayAmount * angle / (1 + (distance * 0.1f));
        a += b;

        a = ImageMath.clamp(a, 0, 1);
        return ImageMath.mixColors(a, rgb, color);
    }

    @Override
    public String toString() {
        return "Stylize/Flare...";
    }
}
