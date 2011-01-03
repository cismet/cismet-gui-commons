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
 * A filter which performs one round of the game of Life on an image.
 *
 * @version  $Revision$, $Date$
 */
public class LifeFilter extends BinaryFilter {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new LifeFilter object.
     */
    public LifeFilter() {
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
                final int r = 0;
                final int g = 0;
                final int b = 0;
                final int pixel = inPixels[(y * width) + x];
                final int a = pixel & 0xff000000;
                int neighbours = 0;

                for (int row = -1; row <= 1; row++) {
                    final int iy = y + row;
                    final int ioffset;
                    if ((0 <= iy) && (iy < height)) {
                        ioffset = iy * width;
                        for (int col = -1; col <= 1; col++) {
                            final int ix = x + col;
                            if (!((row == 0) && (col == 0)) && (0 <= ix) && (ix < width)) {
                                final int rgb = inPixels[ioffset + ix];
                                if (blackFunction.isBlack(rgb)) {
                                    neighbours++;
                                }
                            }
                        }
                    }
                }

                if (blackFunction.isBlack(pixel)) {
                    outPixels[index++] = ((neighbours == 2) || (neighbours == 3)) ? pixel : 0xffffffff;
                } else {
                    outPixels[index++] = (neighbours == 3) ? 0xff000000 : pixel;
                }
            }
        }
        return outPixels;
    }

    @Override
    public String toString() {
        return "Binary/Life"; // NOI18N
    }
}
