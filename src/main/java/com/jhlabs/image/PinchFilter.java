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
import java.awt.geom.*;
import java.awt.image.*;

/**
 * A filter which performs the popular whirl-and-pinch distortion effect.
 *
 * @version  $Revision$, $Date$
 */
public class PinchFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float angle = 0;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float radius = 100;
    private float amount = 0.5f;

    private float radius2 = 0;
    private float icentreX;
    private float icentreY;
    private float width;
    private float height;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PinchFilter object.
     */
    public PinchFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the angle of twirl in radians. 0 means no distortion.
     *
     * @param  angle  the angle of twirl. This is the angle by which pixels at the nearest edge of the image will move.
     *
     * @see    #getAngle
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     * Get the angle of twist.
     *
     * @return  the angle in radians.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Set the centre of the effect in the X direction as a proportion of the image size.
     *
     * @param  centreX  the center
     *
     * @see    #getCentreX
     */
    public void setCentreX(final float centreX) {
        this.centreX = centreX;
    }

    /**
     * Get the centre of the effect in the X direction as a proportion of the image size.
     *
     * @return  the center
     *
     * @see     #setCentreX
     */
    public float getCentreX() {
        return centreX;
    }

    /**
     * Set the centre of the effect in the Y direction as a proportion of the image size.
     *
     * @param  centreY  the center
     *
     * @see    #getCentreY
     */
    public void setCentreY(final float centreY) {
        this.centreY = centreY;
    }

    /**
     * Get the centre of the effect in the Y direction as a proportion of the image size.
     *
     * @return  the center
     *
     * @see     #setCentreY
     */
    public float getCentreY() {
        return centreY;
    }

    /**
     * Set the centre of the effect as a proportion of the image size.
     *
     * @param  centre  the center
     *
     * @see    #getCentre
     */
    public void setCentre(final Point2D centre) {
        this.centreX = (float)centre.getX();
        this.centreY = (float)centre.getY();
    }

    /**
     * Get the centre of the effect as a proportion of the image size.
     *
     * @return  the center
     *
     * @see     #setCentre
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

    /**
     * Set the amount of pinch.
     *
     * @param      amount  the amount
     *
     * @see        #getAmount
     * @min-value  -1
     * @max-value  1
     */
    public void setAmount(final float amount) {
        this.amount = amount;
    }

    /**
     * Get the amount of pinch.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public float getAmount() {
        return amount;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        width = src.getWidth();
        height = src.getHeight();
        icentreX = width * centreX;
        icentreY = height * centreY;
        if (radius == 0) {
            radius = Math.min(icentreX, icentreY);
        }
        radius2 = radius * radius;
        return super.filter(src, dst);
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        float dx = x - icentreX;
        float dy = y - icentreY;
        final float distance = (dx * dx) + (dy * dy);

        if ((distance > radius2) || (distance == 0)) {
            out[0] = x;
            out[1] = y;
        } else {
            final float d = (float)Math.sqrt(distance / radius2);
            final float t = (float)Math.pow(Math.sin(Math.PI * 0.5 * d), -amount);

            dx *= t;
            dy *= t;

            final float e = 1 - d;
            final float a = angle * e * e;

            final float s = (float)Math.sin(a);
            final float c = (float)Math.cos(a);

            out[0] = icentreX + (c * dx) - (s * dy);
            out[1] = icentreY + (s * dx) + (c * dy);
        }
    }

    @Override
    public String toString() {
        return "Distort/Pinch..."; // NOI18N
    }
}
