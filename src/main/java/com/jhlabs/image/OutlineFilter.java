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
 * Given a binary image, this filter converts it to its outline, replacing all interior pixels with the 'new' color.
 *
 * @version  $Revision$, $Date$
 */
public class OutlineFilter extends BinaryFilter {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OutlineFilter object.
     */
    public OutlineFilter() {
        newColor = 0xffffffff;
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
                int pixel = inPixels[(y * width) + x];
                if (blackFunction.isBlack(pixel)) {
                    int neighbours = 0;

                    for (int dy = -1; dy <= 1; dy++) {
                        final int iy = y + dy;
                        final int ioffset;
                        if ((0 <= iy) && (iy < height)) {
                            ioffset = iy * width;
                            for (int dx = -1; dx <= 1; dx++) {
                                final int ix = x + dx;
                                if (!((dy == 0) && (dx == 0)) && (0 <= ix) && (ix < width)) {
                                    final int rgb = inPixels[ioffset + ix];
                                    if (blackFunction.isBlack(rgb)) {
                                        neighbours++;
                                    }
                                } else {
                                    neighbours++;
                                }
                            }
                        }
                    }

                    if (neighbours == 9) {
                        pixel = newColor;
                    }
                }
                outPixels[index++] = pixel;
            }
        }
        return outPixels;
    }

    @Override
    public String toString() {
        return "Binary/Outline..."; // NOI18N
    }
}
