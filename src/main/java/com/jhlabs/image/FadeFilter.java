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
import java.awt.image.*;

import java.io.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FadeFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int width;
    private int height;
    private float angle = 0.0f;
    private float fadeStart = 1.0f;
    private float fadeWidth = 10.0f;
    private int sides;
    private boolean invert;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

    //~ Methods ----------------------------------------------------------------

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
     * DOCUMENT ME!
     *
     * @param  sides  DOCUMENT ME!
     */
    public void setSides(final int sides) {
        this.sides = sides;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getSides() {
        return sides;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fadeStart  DOCUMENT ME!
     */
    public void setFadeStart(final float fadeStart) {
        this.fadeStart = fadeStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getFadeStart() {
        return fadeStart;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fadeWidth  DOCUMENT ME!
     */
    public void setFadeWidth(final float fadeWidth) {
        this.fadeWidth = fadeWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getFadeWidth() {
        return fadeWidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  invert  DOCUMENT ME!
     */
    public void setInvert(final boolean invert) {
        this.invert = invert;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getInvert() {
        return invert;
    }

    @Override
    public void setDimensions(final int width, final int height) {
        this.width = width;
        this.height = height;
        super.setDimensions(width, height);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        float nx = (m00 * x) + (m01 * y);
        final float ny = (m10 * x) + (m11 * y);
        if (sides == 2) {
            nx = (float)Math.sqrt((nx * nx) + (ny * ny));
        } else if (sides == 3) {
            nx = ImageMath.mod(nx, 16);
        } else if (sides == 4) {
            nx = symmetry(nx, 16);
        }
        int alpha = (int)(ImageMath.smoothStep(fadeStart, fadeStart + fadeWidth, nx) * 255);
        if (invert) {
            alpha = 255 - alpha;
        }
        return (alpha << 24) | (rgb & 0x00ffffff);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float symmetry(float x, final float b) {
/*
                int d = (int)(x / b);
                x = ImageMath.mod(x, b);
                if ((d & 1) == 1)
                        return b-x;
                return x;
*/
        x = ImageMath.mod(x, 2 * b);
        if (x > b) {
            return (2 * b) - x;
        }
        return x;
    }

/*
        public float star(float x, float y, int sides, float rMin, float rMax) {
                float sideAngle = 2*Math.PI / sides;
                float angle = Math.atan2(y, x);
                float r = Math.sqrt(x*x + y*y);
                float t = ImageMath.mod(angle, sideAngle) / sideAngle;
                if (t > 0.5)
                        t = 1.0-t;
        }
*/

    @Override
    public String toString() {
        return "Fade...";
    }
}
