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

import java.awt.image.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class TextureFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    public float amount = 1.0f;
    public float turbulence = 1.0f;
    public float gain = 0.5f;
    public float bias = 0.5f;
    public int operation;

    private float scale = 32;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;
    private Colormap colormap = new Gradient();
    private Function2D function = new Noise();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TextureFilter object.
     */
    public TextureFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the amount of texture.
     *
     * @param      amount  the amount
     *
     * @see        #getAmount
     * @min-value  0
     * @max-value  1
     */
    public void setAmount(final float amount) {
        this.amount = amount;
    }

    /**
     * Get the amount of texture.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  function  DOCUMENT ME!
     */
    public void setFunction(final Function2D function) {
        this.function = function;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Function2D getFunction() {
        return function;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  operation  DOCUMENT ME!
     */
    public void setOperation(final int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getOperation() {
        return operation;
    }

    /**
     * Specifies the scale of the texture.
     *
     * @param      scale  the scale of the texture.
     *
     * @see        #getScale
     * @min-value  1
     * @max-value  300+
     */
    public void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * Returns the scale of the texture.
     *
     * @return  the scale of the texture.
     *
     * @see     #setScale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Specifies the stretch factor of the texture.
     *
     * @param      stretch  the stretch factor of the texture.
     *
     * @see        #getStretch
     * @min-value  1
     * @max-value  50+
     */
    public void setStretch(final float stretch) {
        this.stretch = stretch;
    }

    /**
     * Returns the stretch factor of the texture.
     *
     * @return  the stretch factor of the texture.
     *
     * @see     #setStretch
     */
    public float getStretch() {
        return stretch;
    }

    /**
     * Specifies the angle of the texture.
     *
     * @param  angle  the angle of the texture.
     *
     * @see    #getAngle
     * @angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
        final float cos = (float)Math.cos(angle);
        final float sin = (float)Math.sin(angle);
        m00 = cos;
        m01 = sin;
        m10 = -sin;
        m11 = cos;
    }

    /**
     * Returns the angle of the texture.
     *
     * @return  the angle of the texture.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Specifies the turbulence of the texture.
     *
     * @param      turbulence  the turbulence of the texture.
     *
     * @see        #getTurbulence
     * @min-value  0
     * @max-value  1
     */
    public void setTurbulence(final float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * Returns the turbulence of the texture.
     *
     * @return  the turbulence of the texture.
     *
     * @see     #setTurbulence
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * Set the colormap to be used for the filter.
     *
     * @param  colormap  the colormap
     *
     * @see    #getColormap
     */
    public void setColormap(final Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * Get the colormap to be used for the filter.
     *
     * @return  the colormap
     *
     * @see     #setColormap
     */
    public Colormap getColormap() {
        return colormap;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        float nx = (m00 * x) + (m01 * y);
        float ny = (m10 * x) + (m11 * y);
        nx /= scale;
        ny /= scale * stretch;
        float f = (turbulence == 1.0) ? Noise.noise2(nx, ny) : Noise.turbulence2(nx, ny, turbulence);
        f = (f * 0.5f) + 0.5f;
        f = ImageMath.gain(f, gain);
        f = ImageMath.bias(f, bias);
        f *= amount;
        final int a = rgb & 0xff000000;
        int v;
        if (colormap != null) {
            v = colormap.getColor(f);
        } else {
            v = PixelUtils.clamp((int)(f * 255));
            final int r = v << 16;
            final int g = v << 8;
            final int b = v;
            v = a | r | g | b;
        }
        if (operation != PixelUtils.REPLACE) {
            v = PixelUtils.combinePixels(rgb, v, operation);
        }
        return v;
    }

    @Override
    public String toString() {
        return "Texture/Noise...";
    }
}
