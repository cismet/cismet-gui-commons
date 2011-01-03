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
 * An edge-detection filter.
 *
 * @version  $Revision$, $Date$
 */
public class EdgeFilter extends WholeImageFilter {

    //~ Static fields/initializers ---------------------------------------------

    public static final float R2 = (float)Math.sqrt(2);

    public static final float[] ROBERTS_V = {
            0, 0, -1,
            0, 1, 0,
            0, 0, 0,
        };
    public static final float[] ROBERTS_H = {
            -1, 0, 0,
            0, 1, 0,
            0, 0, 0,
        };
    public static final float[] PREWITT_V = {
            -1, 0, 1,
            -1, 0, 1,
            -1, 0, 1,
        };
    public static final float[] PREWITT_H = {
            -1, -1, -1,
            0, 0, 0,
            1, 1, 1,
        };
    public static final float[] SOBEL_V = {
            -1, 0, 1,
            -2, 0, 2,
            -1, 0, 1,
        };
    public static float[] SOBEL_H = {
            -1, -2, -1,
            0, 0, 0,
            1, 2, 1,
        };
    public static final float[] FREI_CHEN_V = {
            -1, 0, 1,
            -R2, 0, R2,
            -1, 0, 1,
        };
    public static float[] FREI_CHEN_H = {
            -1, -R2, -1,
            0, 0, 0,
            1, R2, 1,
        };

    //~ Instance fields --------------------------------------------------------

    protected float[] vEdgeMatrix = SOBEL_V;
    protected float[] hEdgeMatrix = SOBEL_H;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new EdgeFilter object.
     */
    public EdgeFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  vEdgeMatrix  DOCUMENT ME!
     */
    public void setVEdgeMatrix(final float[] vEdgeMatrix) {
        this.vEdgeMatrix = vEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float[] getVEdgeMatrix() {
        return vEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  hEdgeMatrix  DOCUMENT ME!
     */
    public void setHEdgeMatrix(final float[] hEdgeMatrix) {
        this.hEdgeMatrix = hEdgeMatrix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float[] getHEdgeMatrix() {
        return hEdgeMatrix;
    }

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        int index = 0;
        final int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0;
                int g = 0;
                int b = 0;
                int rh = 0;
                int gh = 0;
                int bh = 0;
                int rv = 0;
                int gv = 0;
                int bv = 0;
                final int a = inPixels[(y * width) + x] & 0xff000000;

                for (int row = -1; row <= 1; row++) {
                    final int iy = y + row;
                    int ioffset;
                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;
                    } else {
                        ioffset = y * width;
                    }
                    final int moffset = (3 * (row + 1)) + 1;
                    for (int col = -1; col <= 1; col++) {
                        int ix = x + col;
                        if (!((0 <= ix) && (ix < width))) {
                            ix = x;
                        }
                        final int rgb = inPixels[ioffset + ix];
                        final float h = hEdgeMatrix[moffset + col];
                        final float v = vEdgeMatrix[moffset + col];

                        r = (rgb & 0xff0000) >> 16;
                        g = (rgb & 0x00ff00) >> 8;
                        b = rgb & 0x0000ff;
                        rh += (int)(h * r);
                        gh += (int)(h * g);
                        bh += (int)(h * b);
                        rv += (int)(v * r);
                        gv += (int)(v * g);
                        bv += (int)(v * b);
                    }
                }
                r = (int)(Math.sqrt((rh * rh) + (rv * rv)) / 1.8);
                g = (int)(Math.sqrt((gh * gh) + (gv * gv)) / 1.8);
                b = (int)(Math.sqrt((bh * bh) + (bv * bv)) / 1.8);
                r = PixelUtils.clamp(r);
                g = PixelUtils.clamp(g);
                b = PixelUtils.clamp(b);
                outPixels[index++] = a | (r << 16) | (g << 8) | b;
            }
        }
        return outPixels;
    }

    @Override
    public String toString() {
        return "Blur/Detect Edges";
    }
}
