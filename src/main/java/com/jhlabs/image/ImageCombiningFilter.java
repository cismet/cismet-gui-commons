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
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ImageCombiningFilter {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   x     DOCUMENT ME!
     * @param   y     DOCUMENT ME!
     * @param   rgb1  DOCUMENT ME!
     * @param   rgb2  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int filterRGB(final int x, final int y, final int rgb1, final int rgb2) {
        final int a1 = (rgb1 >> 24) & 0xff;
        final int r1 = (rgb1 >> 16) & 0xff;
        final int g1 = (rgb1 >> 8) & 0xff;
        final int b1 = rgb1 & 0xff;
        final int a2 = (rgb2 >> 24) & 0xff;
        final int r2 = (rgb2 >> 16) & 0xff;
        final int g2 = (rgb2 >> 8) & 0xff;
        final int b2 = rgb2 & 0xff;
        final int r = PixelUtils.clamp(r1 + r2);
        final int g = PixelUtils.clamp(r1 + r2);
        final int b = PixelUtils.clamp(r1 + r2);
        return (a1 << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   image1  DOCUMENT ME!
     * @param   image2  DOCUMENT ME!
     * @param   x       DOCUMENT ME!
     * @param   y       DOCUMENT ME!
     * @param   w       DOCUMENT ME!
     * @param   h       DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ImageProducer filter(final Image image1,
            final Image image2,
            final int x,
            final int y,
            final int w,
            final int h) {
        final int[] pixels1 = new int[w * h];
        final int[] pixels2 = new int[w * h];
        final int[] pixels3 = new int[w * h];
        final PixelGrabber pg1 = new PixelGrabber(image1, x, y, w, h, pixels1, 0, w);
        final PixelGrabber pg2 = new PixelGrabber(image2, x, y, w, h, pixels2, 0, w);
        try {
            pg1.grabPixels();
            pg2.grabPixels();
        } catch (InterruptedException e) {
            System.err.println("interrupted waiting for pixels!"); // NOI18N
            return null;
        }
        if ((pg1.status() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");  // NOI18N
            return null;
        }
        if ((pg2.status() & ImageObserver.ABORT) != 0) {
            System.err.println("image fetch aborted or errored");  // NOI18N
            return null;
        }

        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                final int k = (j * w) + i;
                pixels3[k] = filterRGB(x + i, y + j, pixels1[k], pixels2[k]);
            }
        }
        return new MemoryImageSource(w, h, pixels3, 0, w);
    }
}
