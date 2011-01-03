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
 * A Filter which produces the effect of looking into a kaleidoscope.
 *
 * @version  $Revision$, $Date$
 */
public class KaleidoscopeFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float angle = 0;
    private float angle2 = 0;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private int sides = 3;
    private float radius = 0;

    private float icentreX;
    private float icentreY;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a KaleidoscopeFilter with no distortion.
     */
    public KaleidoscopeFilter() {
        setEdgeAction(CLAMP);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the number of sides of the kaleidoscope.
     *
     * @param      sides  the number of sides
     *
     * @see        #getSides
     * @min-value  2
     */
    public void setSides(final int sides) {
        this.sides = sides;
    }

    /**
     * Get the number of sides of the kaleidoscope.
     *
     * @return  the number of sides
     *
     * @see     #setSides
     */
    public int getSides() {
        return sides;
    }

    /**
     * Set the angle of the kaleidoscope.
     *
     * @param  angle  the angle of the kaleidoscope.
     *
     * @see    #getAngle
     * @angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     * Get the angle of the kaleidoscope.
     *
     * @return  the angle of the kaleidoscope.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Set the secondary angle of the kaleidoscope.
     *
     * @param  angle2  the angle
     *
     * @see    #getAngle2
     * @angle  DOCUMENT ME!
     */
    public void setAngle2(final float angle2) {
        this.angle2 = angle2;
    }

    /**
     * Get the secondary angle of the kaleidoscope.
     *
     * @return  the angle
     *
     * @see     #setAngle2
     */
    public float getAngle2() {
        return angle2;
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

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        icentreX = src.getWidth() * centreX;
        icentreY = src.getHeight() * centreY;
        return super.filter(src, dst);
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final double dx = x - icentreX;
        final double dy = y - icentreY;
        double r = Math.sqrt((dx * dx) + (dy * dy));
        double theta = Math.atan2(dy, dx) - angle - angle2;
        theta = ImageMath.triangle((float)(theta / Math.PI * sides * .5));
        if (radius != 0) {
            final double c = Math.cos(theta);
            final double radiusc = radius / c;
            r = radiusc * ImageMath.triangle((float)(r / radiusc));
        }
        theta += angle;

        out[0] = (float)(icentreX + (r * Math.cos(theta)));
        out[1] = (float)(icentreY + (r * Math.sin(theta)));
    }

    @Override
    public String toString() {
        return "Distort/Kaleidoscope...";
    }
}
