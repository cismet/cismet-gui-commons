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
 * This filter diffuses an image by moving its pixels in random directions.
 *
 * @version  $Revision$, $Date$
 */
public class DiffuseFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float[] sinTable;
    private float[] cosTable;
    private float scale = 4;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DiffuseFilter object.
     */
    public DiffuseFilter() {
        setEdgeAction(CLAMP);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Specifies the scale of the texture.
     *
     * @param      scale  the scale of the texture.
     *
     * @see        #getScale
     * @min-value  1
     * @max-value  100+
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

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final int angle = (int)(Math.random() * 255);
        final float distance = (float)Math.random();
        out[0] = x + (distance * sinTable[angle]);
        out[1] = y + (distance * cosTable[angle]);
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        sinTable = new float[256];
        cosTable = new float[256];
        for (int i = 0; i < 256; i++) {
            final float angle = ImageMath.TWO_PI * i / 256f;
            sinTable[i] = (float)(scale * Math.sin(angle));
            cosTable[i] = (float)(scale * Math.cos(angle));
        }
        return super.filter(src, dst);
    }

    @Override
    public String toString() {
        return "Distort/Diffuse..."; // NOI18N
    }
}
