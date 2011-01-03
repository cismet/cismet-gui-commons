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
 * A filter which replcaes each pixel by the mimimum of itself and its eight neightbours.
 *
 * @version  $Revision$, $Date$
 */
public class MinimumFilter extends WholeImageFilter {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MinimumFilter object.
     */
    public MinimumFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        int index = 0;
        final int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = 0xffffffff;
                for (int dy = -1; dy <= 1; dy++) {
                    final int iy = y + dy;
                    final int ioffset;
                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;
                        for (int dx = -1; dx <= 1; dx++) {
                            final int ix = x + dx;
                            if ((0 <= ix) && (ix < width)) {
                                pixel = PixelUtils.combinePixels(pixel, inPixels[ioffset + ix], PixelUtils.MIN);
                            }
                        }
                    }
                }
                outPixels[index++] = pixel;
            }
        }
        return outPixels;
    }

    @Override
    public String toString() {
        return "Blur/Minimum"; // NOI18N
    }
}
