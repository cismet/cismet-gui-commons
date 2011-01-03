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
 * A filter which allows the red, green and blue channels of an image to be mixed into each other.
 *
 * @version  $Revision$, $Date$
 */
public class ChannelMixFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int blueGreen;
    private int redBlue;
    private int greenRed;
    private int intoR;
    private int intoG;
    private int intoB;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ChannelMixFilter object.
     */
    public ChannelMixFilter() {
        canFilterIndexColorModel = true;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  blueGreen  DOCUMENT ME!
     */
    public void setBlueGreen(final int blueGreen) {
        this.blueGreen = blueGreen;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getBlueGreen() {
        return blueGreen;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  redBlue  DOCUMENT ME!
     */
    public void setRedBlue(final int redBlue) {
        this.redBlue = redBlue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getRedBlue() {
        return redBlue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  greenRed  DOCUMENT ME!
     */
    public void setGreenRed(final int greenRed) {
        this.greenRed = greenRed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getGreenRed() {
        return greenRed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  intoR  DOCUMENT ME!
     */
    public void setIntoR(final int intoR) {
        this.intoR = intoR;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIntoR() {
        return intoR;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  intoG  DOCUMENT ME!
     */
    public void setIntoG(final int intoG) {
        this.intoG = intoG;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIntoG() {
        return intoG;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  intoB  DOCUMENT ME!
     */
    public void setIntoB(final int intoB) {
        this.intoB = intoB;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIntoB() {
        return intoB;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = rgb & 0xff000000;
        final int r = (rgb >> 16) & 0xff;
        final int g = (rgb >> 8) & 0xff;
        final int b = rgb & 0xff;
        final int nr = PixelUtils.clamp(((intoR * ((blueGreen * g) + ((255 - blueGreen) * b)) / 255)
                            + ((255 - intoR) * r))
                        / 255);
        final int ng = PixelUtils.clamp(((intoG * ((redBlue * b) + ((255 - redBlue) * r)) / 255)
                            + ((255 - intoG) * g))
                        / 255);
        final int nb = PixelUtils.clamp(((intoB * ((greenRed * r) + ((255 - greenRed) * g)) / 255)
                            + ((255 - intoB) * b))
                        / 255);
        return a | (nr << 16) | (ng << 8) | nb;
    }

    @Override
    public String toString() {
        return "Colors/Mix Channels...";
    }
}
