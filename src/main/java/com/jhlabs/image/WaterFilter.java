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

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * A filter which produces a water ripple distortion.
 *
 * @version  $Revision$, $Date$
 */
public class WaterFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float wavelength = 16;
    private float amplitude = 10;
    private float phase = 0;
    private float centreX = 0.5f;
    private float centreY = 0.5f;
    private float radius = 50;

    private float radius2 = 0;
    private float icentreX;
    private float icentreY;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new WaterFilter object.
     */
    public WaterFilter() {
        setEdgeAction(CLAMP);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the wavelength of the ripples.
     *
     * @param  wavelength  the wavelength
     *
     * @see    #getWavelength
     */
    public void setWavelength(final float wavelength) {
        this.wavelength = wavelength;
    }

    /**
     * Get the wavelength of the ripples.
     *
     * @return  the wavelength
     *
     * @see     #setWavelength
     */
    public float getWavelength() {
        return wavelength;
    }

    /**
     * Set the amplitude of the ripples.
     *
     * @param  amplitude  the amplitude
     *
     * @see    #getAmplitude
     */
    public void setAmplitude(final float amplitude) {
        this.amplitude = amplitude;
    }

    /**
     * Get the amplitude of the ripples.
     *
     * @return  the amplitude
     *
     * @see     #setAmplitude
     */
    public float getAmplitude() {
        return amplitude;
    }

    /**
     * Set the phase of the ripples.
     *
     * @param  phase  the phase
     *
     * @see    #getPhase
     */
    public void setPhase(final float phase) {
        this.phase = phase;
    }

    /**
     * Get the phase of the ripples.
     *
     * @return  the phase
     *
     * @see     #setPhase
     */
    public float getPhase() {
        return phase;
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
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean inside(final int v, final int a, final int b) {
        return (a <= v) && (v <= b);
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        icentreX = src.getWidth() * centreX;
        icentreY = src.getHeight() * centreY;
        if (radius == 0) {
            radius = Math.min(icentreX, icentreY);
        }
        radius2 = radius * radius;
        return super.filter(src, dst);
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final float dx = x - icentreX;
        final float dy = y - icentreY;
        final float distance2 = (dx * dx) + (dy * dy);
        if (distance2 > radius2) {
            out[0] = x;
            out[1] = y;
        } else {
            final float distance = (float)Math.sqrt(distance2);
            float amount = amplitude * (float)Math.sin((distance / wavelength * ImageMath.TWO_PI) - phase);
            amount *= (radius - distance) / radius;
            if (distance != 0) {
                amount *= wavelength / distance;
            }
            out[0] = x + (dx * amount);
            out[1] = y + (dy * amount);
        }
    }

    @Override
    public String toString() {
        return "Distort/Water Ripples...";
    }
}
