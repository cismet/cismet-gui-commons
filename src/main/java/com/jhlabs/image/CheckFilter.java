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

/**
 * A Filter to draw grids and check patterns.
 *
 * @version  $Revision$, $Date$
 */
public class CheckFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int xScale = 8;
    private int yScale = 8;
    private int foreground = 0xffffffff;
    private int background = 0xff000000;
    private int fuzziness = 0;
    private float angle = 0.0f;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CheckFilter object.
     */
    public CheckFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the foreground color.
     *
     * @param  foreground  the color.
     *
     * @see    #getForeground
     */
    public void setForeground(final int foreground) {
        this.foreground = foreground;
    }

    /**
     * Get the foreground color.
     *
     * @return  the color.
     *
     * @see     #setForeground
     */
    public int getForeground() {
        return foreground;
    }

    /**
     * Set the background color.
     *
     * @param  background  the color.
     *
     * @see    #getBackground
     */
    public void setBackground(final int background) {
        this.background = background;
    }

    /**
     * Get the background color.
     *
     * @return  the color.
     *
     * @see     #setBackground
     */
    public int getBackground() {
        return background;
    }

    /**
     * Set the X scale of the texture.
     *
     * @param  xScale  the scale.
     *
     * @see    #getXScale
     */
    public void setXScale(final int xScale) {
        this.xScale = xScale;
    }

    /**
     * Get the X scale of the texture.
     *
     * @return  the scale.
     *
     * @see     #setXScale
     */
    public int getXScale() {
        return xScale;
    }

    /**
     * Set the Y scale of the texture.
     *
     * @param  yScale  the scale.
     *
     * @see    #getYScale
     */
    public void setYScale(final int yScale) {
        this.yScale = yScale;
    }

    /**
     * Get the Y scale of the texture.
     *
     * @return  the scale.
     *
     * @see     #setYScale
     */
    public int getYScale() {
        return yScale;
    }

    /**
     * Set the fuzziness of the texture.
     *
     * @param  fuzziness  the fuzziness.
     *
     * @see    #getFuzziness
     */
    public void setFuzziness(final int fuzziness) {
        this.fuzziness = fuzziness;
    }

    /**
     * Get the fuzziness of the texture.
     *
     * @return  the fuzziness.
     *
     * @see     #setFuzziness
     */
    public int getFuzziness() {
        return fuzziness;
    }

    /**
     * Set the angle of the texture.
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
     * Get the angle of the texture.
     *
     * @return  the angle of the texture.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final float nx = ((m00 * x) + (m01 * y)) / xScale;
        final float ny = ((m10 * x) + (m11 * y)) / yScale;
        float f = (((int)(nx + 100000) % 2) != ((int)(ny + 100000) % 2)) ? 1.0f : 0.0f;
        if (fuzziness != 0) {
            final float fuzz = (fuzziness / 100.0f);
            final float fx = ImageMath.smoothPulse(0, fuzz, 1 - fuzz, 1, ImageMath.mod(nx, 1));
            final float fy = ImageMath.smoothPulse(0, fuzz, 1 - fuzz, 1, ImageMath.mod(ny, 1));
            f *= fx * fy;
        }
        return ImageMath.mixColors(f, foreground, background);
    }

    @Override
    public String toString() {
        return "Texture/Checkerboard..."; // NOI18N
    }
}
