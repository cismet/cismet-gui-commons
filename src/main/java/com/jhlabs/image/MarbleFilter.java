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
 * This filter applies a marbling effect to an image, displacing pixels by random amounts.
 *
 * @version  $Revision$, $Date$
 */
public class MarbleFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float[] sinTable;
    private float[] cosTable;
    private float xScale = 4;
    private float yScale = 4;
    private float amount = 1;
    private float turbulence = 1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MarbleFilter object.
     */
    public MarbleFilter() {
        setEdgeAction(CLAMP);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the X scale of the effect.
     *
     * @param  xScale  the scale.
     *
     * @see    #getXScale
     */
    public void setXScale(final float xScale) {
        this.xScale = xScale;
    }

    /**
     * Get the X scale of the effect.
     *
     * @return  the scale.
     *
     * @see     #setXScale
     */
    public float getXScale() {
        return xScale;
    }

    /**
     * Set the Y scale of the effect.
     *
     * @param  yScale  the scale.
     *
     * @see    #getYScale
     */
    public void setYScale(final float yScale) {
        this.yScale = yScale;
    }

    /**
     * Get the Y scale of the effect.
     *
     * @return  the scale.
     *
     * @see     #setYScale
     */
    public float getYScale() {
        return yScale;
    }

    /**
     * Set the amount of effect.
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
     * Get the amount of effect.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Specifies the turbulence of the effect.
     *
     * @param      turbulence  the turbulence of the effect.
     *
     * @see        #getTurbulence
     * @min-value  0
     * @max-value  1
     */
    public void setTurbulence(final float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * Returns the turbulence of the effect.
     *
     * @return  the turbulence of the effect.
     *
     * @see     #setTurbulence
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        sinTable = new float[256];
        cosTable = new float[256];
        for (int i = 0; i < 256; i++) {
            final float angle = ImageMath.TWO_PI * i / 256f * turbulence;
            sinTable[i] = (float)(-yScale * Math.sin(angle));
            cosTable[i] = (float)(yScale * Math.cos(angle));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     * @param   y  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int displacementMap(final int x, final int y) {
        return PixelUtils.clamp((int)(127 * (1 + Noise.noise2(x / xScale, y / xScale))));
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final int displacement = displacementMap(x, y);
        out[0] = x + sinTable[displacement];
        out[1] = y + cosTable[displacement];
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        initialize();
        return super.filter(src, dst);
    }

    @Override
    public String toString() {
        return "Distort/Marble...";
    }
}
