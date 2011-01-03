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
package com.jhlabs.composite;

import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;

/**
 * A special Composite used for drawing "marching ants". It draws the ants at the 127 contour of the alpha channel of
 * the source. This can only be used on TYPE_INT_RGBA images.
 *
 * @version  $Revision$, $Date$
 */
public final class ContourComposite implements Composite {

    //~ Instance fields --------------------------------------------------------

    private int offset;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContourComposite object.
     *
     * @param  offset  DOCUMENT ME!
     */
    public ContourComposite(final int offset) {
        this.offset = offset;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public CompositeContext createContext(final ColorModel srcColorModel,
            final ColorModel dstColorModel,
            final RenderingHints hints) {
        return new ContourCompositeContext(offset, srcColorModel, dstColorModel);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof ContourComposite)) {
            return false;
        }
        return true;
    }
}

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
class ContourCompositeContext implements CompositeContext {

    //~ Instance fields --------------------------------------------------------

    private int offset;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContourCompositeContext object.
     *
     * @param  offset         DOCUMENT ME!
     * @param  srcColorModel  DOCUMENT ME!
     * @param  dstColorModel  DOCUMENT ME!
     */
    public ContourCompositeContext(final int offset, final ColorModel srcColorModel, final ColorModel dstColorModel) {
        this.offset = offset;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void dispose() {
    }

    @Override
    public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
        final int x = src.getMinX();
        int y = src.getMinY();
        final int w = src.getWidth();
        final int h = src.getHeight();

        int[] srcPix = null;
        int[] srcPix2 = null;
        int[] dstInPix = null;
        final int[] dstOutPix = new int[w * 4];

        for (int i = 0; i < h; i++) {
            srcPix = src.getPixels(x, y, w, 1, srcPix);
            dstInPix = dstIn.getPixels(x, y, w, 1, dstInPix);

            int lastAlpha = 0;
            int k = 0;
            for (int j = 0; j < w; j++) {
                final int alpha = srcPix[k + 3];
                final int alphaAbove = (i != 0) ? srcPix2[k + 3] : alpha;

                if (((i != 0) && (j != 0) && (((alpha ^ lastAlpha) & 0x80) != 0))
                            || (((alpha ^ alphaAbove) & 0x80) != 0)) {
                    if (((offset + i + j) % 10) > 4) {
                        dstOutPix[k] = 0x00;
                        dstOutPix[k + 1] = 0x00;
                        dstOutPix[k + 2] = 0x00;
                    } else {
                        dstOutPix[k] = 0xff;
                        dstOutPix[k + 1] = 0xff;
                        dstOutPix[k + 2] = 0x7f;
                    }
                    dstOutPix[k + 3] = 0xff;
                } else {
                    dstOutPix[k] = dstInPix[k];
                    dstOutPix[k + 1] = dstInPix[k + 1];
                    dstOutPix[k + 2] = dstInPix[k + 2];
//                                      if ( dstOut == dstIn )
                    dstOutPix[k] = 0xff;
                    dstOutPix[k + 1] = 0;
                    dstOutPix[k + 2] = 0;
                    dstOutPix[k + 3] = 0;
//                                      else
//                                              dstOutPix[k+3] = dstInPix[k+3];
                }

                lastAlpha = alpha;
                k += 4;
            }

            dstOut.setPixels(x, y, w, 1, dstOutPix);

            final int[] t = srcPix;
            srcPix = srcPix2;
            srcPix2 = t;
            y++;
        }
    }
}
