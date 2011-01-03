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

import java.awt.image.*;

/**
 * A filter which performs a threshold operation on an image.
 *
 * @version  $Revision$, $Date$
 */
public class ThresholdFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int lowerThreshold;
    private int lowerThreshold3;
    private int upperThreshold;
    private int upperThreshold3;
    private int white = 0xffffff;
    private int black = 0x000000;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a ThresholdFilter.
     */
    public ThresholdFilter() {
        this(127);
    }

    /**
     * Construct a ThresholdFilter.
     *
     * @param  t  the threshold value
     */
    public ThresholdFilter(final int t) {
        setLowerThreshold(t);
        setUpperThreshold(t);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the lower threshold value.
     *
     * @param  lowerThreshold  the threshold value
     *
     * @see    #getLowerThreshold
     */
    public void setLowerThreshold(final int lowerThreshold) {
        this.lowerThreshold = lowerThreshold;
        lowerThreshold3 = lowerThreshold * 3;
    }

    /**
     * Get the lower threshold value.
     *
     * @return  the threshold value
     *
     * @see     #setLowerThreshold
     */
    public int getLowerThreshold() {
        return lowerThreshold;
    }

    /**
     * Set the upper threshold value.
     *
     * @param  upperThreshold  the threshold value
     *
     * @see    #getUpperThreshold
     */
    public void setUpperThreshold(final int upperThreshold) {
        this.upperThreshold = upperThreshold;
        upperThreshold3 = upperThreshold * 3;
    }

    /**
     * Get the upper threshold value.
     *
     * @return  the threshold value
     *
     * @see     #setUpperThreshold
     */
    public int getUpperThreshold() {
        return upperThreshold;
    }

    /**
     * Set the color to be used for pixels above the upper threshold.
     *
     * @param  white  the color
     *
     * @see    #getWhite
     */
    public void setWhite(final int white) {
        this.white = white;
    }

    /**
     * Get the color to be used for pixels above the upper threshold.
     *
     * @return  the color
     *
     * @see     #setWhite
     */
    public int getWhite() {
        return white;
    }

    /**
     * Set the color to be used for pixels below the lower threshold.
     *
     * @param  black  the color
     *
     * @see    #getBlack
     */
    public void setBlack(final int black) {
        this.black = black;
    }

    /**
     * Set the color to be used for pixels below the lower threshold.
     *
     * @return  the color
     *
     * @see     #setBlack
     */
    public int getBlack() {
        return black;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = rgb & 0xff000000;
        final int r = (rgb >> 16) & 0xff;
        final int g = (rgb >> 8) & 0xff;
        final int b = rgb & 0xff;
        final int l = r + g + b;
        if (l < lowerThreshold3) {
            return a | black;
        } else if (l > upperThreshold3) {
            return a | white;
        }
        return rgb;
    }

    @Override
    public String toString() {
        return "Stylize/Threshold..."; // NOI18N
    }
}
