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
import java.awt.image.*;

/**
 * A filter which distorts an image by rippling it in the X or Y directions. The amplitude and wavelength of rippling
 * can be specified as well as whether pixels going off the edges are wrapped or not.
 *
 * @version  $Revision$, $Date$
 */
public class RippleFilter extends TransformFilter {

    //~ Static fields/initializers ---------------------------------------------

    /** Sine wave ripples. */
    public static final int SINE = 0;

    /** Sawtooth wave ripples. */
    public static final int SAWTOOTH = 1;

    /** Triangle wave ripples. */
    public static final int TRIANGLE = 2;

    /** Noise ripples. */
    public static final int NOISE = 3;

    //~ Instance fields --------------------------------------------------------

    private float xAmplitude;
    private float yAmplitude;
    private float xWavelength;
    private float yWavelength;
    private int waveType;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a RippleFilter.
     */
    public RippleFilter() {
        xAmplitude = 5.0f;
        yAmplitude = 0.0f;
        xWavelength = yWavelength = 16.0f;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the amplitude of ripple in the X direction.
     *
     * @param  xAmplitude  the amplitude (in pixels).
     *
     * @see    #getXAmplitude
     */
    public void setXAmplitude(final float xAmplitude) {
        this.xAmplitude = xAmplitude;
    }

    /**
     * Get the amplitude of ripple in the X direction.
     *
     * @return  the amplitude (in pixels).
     *
     * @see     #setXAmplitude
     */
    public float getXAmplitude() {
        return xAmplitude;
    }

    /**
     * Set the wavelength of ripple in the X direction.
     *
     * @param  xWavelength  the wavelength (in pixels).
     *
     * @see    #getXWavelength
     */
    public void setXWavelength(final float xWavelength) {
        this.xWavelength = xWavelength;
    }

    /**
     * Get the wavelength of ripple in the X direction.
     *
     * @return  the wavelength (in pixels).
     *
     * @see     #setXWavelength
     */
    public float getXWavelength() {
        return xWavelength;
    }

    /**
     * Set the amplitude of ripple in the Y direction.
     *
     * @param  yAmplitude  the amplitude (in pixels).
     *
     * @see    #getYAmplitude
     */
    public void setYAmplitude(final float yAmplitude) {
        this.yAmplitude = yAmplitude;
    }

    /**
     * Get the amplitude of ripple in the Y direction.
     *
     * @return  the amplitude (in pixels).
     *
     * @see     #setYAmplitude
     */
    public float getYAmplitude() {
        return yAmplitude;
    }

    /**
     * Set the wavelength of ripple in the Y direction.
     *
     * @param  yWavelength  the wavelength (in pixels).
     *
     * @see    #getYWavelength
     */
    public void setYWavelength(final float yWavelength) {
        this.yWavelength = yWavelength;
    }

    /**
     * Get the wavelength of ripple in the Y direction.
     *
     * @return  the wavelength (in pixels).
     *
     * @see     #setYWavelength
     */
    public float getYWavelength() {
        return yWavelength;
    }

    /**
     * Set the wave type.
     *
     * @param  waveType  the type.
     *
     * @see    #getWaveType
     */
    public void setWaveType(final int waveType) {
        this.waveType = waveType;
    }

    /**
     * Get the wave type.
     *
     * @return  the type.
     *
     * @see     #setWaveType
     */
    public int getWaveType() {
        return waveType;
    }

    @Override
    protected void transformSpace(final Rectangle r) {
        if (edgeAction == ZERO) {
            r.x -= (int)xAmplitude;
            r.width += (int)(2 * xAmplitude);
            r.y -= (int)yAmplitude;
            r.height += (int)(2 * yAmplitude);
        }
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final float nx = (float)y / xWavelength;
        final float ny = (float)x / yWavelength;
        float fx;
        float fy;
        switch (waveType) {
            case SINE:
            default: {
                fx = (float)Math.sin(nx);
                fy = (float)Math.sin(ny);
                break;
            }
            case SAWTOOTH: {
                fx = ImageMath.mod(nx, 1);
                fy = ImageMath.mod(ny, 1);
                break;
            }
            case TRIANGLE: {
                fx = ImageMath.triangle(nx);
                fy = ImageMath.triangle(ny);
                break;
            }
            case NOISE: {
                fx = Noise.noise1(nx);
                fy = Noise.noise1(ny);
                break;
            }
        }
        out[0] = x + (xAmplitude * fx);
        out[1] = y + (yAmplitude * fy);
    }

    @Override
    public String toString() {
        return "Distort/Ripple..."; // NOI18N
    }
}
