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
 * A filter which draws contours on an image at given brightness levels.
 *
 * @version  $Revision$, $Date$
 */
public class ContourFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    private float levels = 5;
    private float scale = 1;
    private float offset = 0;
    private int contourColor = 0xff000000;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContourFilter object.
     */
    public ContourFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  levels  DOCUMENT ME!
     */
    public void setLevels(final float levels) {
        this.levels = levels;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getLevels() {
        return levels;
    }

    /**
     * Specifies the scale of the contours.
     *
     * @param      scale  the scale of the contours.
     *
     * @see        #getScale
     * @min-value  0
     * @max-value  1
     */
    public void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * Returns the scale of the contours.
     *
     * @return  the scale of the contours.
     *
     * @see     #setScale
     */
    public float getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  offset  DOCUMENT ME!
     */
    public void setOffset(final float offset) {
        this.offset = offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getOffset() {
        return offset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  contourColor  DOCUMENT ME!
     */
    public void setContourColor(final int contourColor) {
        this.contourColor = contourColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getContourColor() {
        return contourColor;
    }

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        int index = 0;
        final short[][] r = new short[3][width];
        final int[] outPixels = new int[width * height];

        final short[] table = new short[256];
        final int offsetl = (int)(offset * 256 / levels);
        for (int i = 0; i < 256; i++) {
            table[i] = (short)PixelUtils.clamp((int)((255 * Math.floor(levels * (i + offsetl) / 256) / (levels - 1))
                                - offsetl));
        }

        for (int x = 0; x < width; x++) {
            final int rgb = inPixels[x];
            r[1][x] = (short)PixelUtils.brightness(rgb);
        }
        for (int y = 0; y < height; y++) {
            final boolean yIn = (y > 0) && (y < (height - 1));
            int nextRowIndex = index + width;
            if (y < (height - 1)) {
                for (int x = 0; x < width; x++) {
                    final int rgb = inPixels[nextRowIndex++];
                    r[2][x] = (short)PixelUtils.brightness(rgb);
                }
            }
            for (int x = 0; x < width; x++) {
                final boolean xIn = (x > 0) && (x < (width - 1));
                final int w = x - 1;
                final int e = x + 1;
                int v = 0;

                if (yIn && xIn) {
                    final short nwb = r[0][w];
                    final short neb = r[0][x];
                    final short swb = r[1][w];
                    final short seb = r[1][x];
                    final short nw = table[nwb];
                    final short ne = table[neb];
                    final short sw = table[swb];
                    final short se = table[seb];

                    if ((nw != ne) || (nw != sw) || (ne != se) || (sw != se)) {
                        v = (int)(scale
                                        * (Math.abs(nwb - neb) + Math.abs(nwb - swb) + Math.abs(neb - seb)
                                            + Math.abs(swb - seb)));
//                                              v /= 255;
                        if (v > 255) {
                            v = 255;
                        }
                    }
                }

                if (v != 0) {
                    outPixels[index] = PixelUtils.combinePixels(inPixels[index], contourColor, PixelUtils.NORMAL, v);
                }
//                                      outPixels[index] = PixelUtils.combinePixels( (contourColor & 0xff)|(v << 24), inPixels[index], PixelUtils.NORMAL );
                else {
                    outPixels[index] = inPixels[index];
                }
                index++;
            }
            final short[] t;
            t = r[0];
            r[0] = r[1];
            r[1] = r[2];
            r[2] = t;
        }

        return outPixels;
    }

    @Override
    public String toString() {
        return "Stylize/Contour..."; // NOI18N
    }
}
