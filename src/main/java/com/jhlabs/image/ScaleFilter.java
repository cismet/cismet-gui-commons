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
 * Scales an image using the area-averaging algorithm, which can't be done with AffineTransformOp.
 *
 * @version  $Revision$, $Date$
 */
public class ScaleFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private int width;
    private int height;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a ScaleFilter.
     */
    public ScaleFilter() {
        this(32, 32);
    }

    /**
     * Construct a ScaleFilter.
     *
     * @param  width   the width to scale to
     * @param  height  the height to scale to
     */
    public ScaleFilter(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int w = src.getWidth();
        final int h = src.getHeight();

        if (dst == null) {
            final ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(
                    dstCM,
                    dstCM.createCompatibleWritableRaster(w, h),
                    dstCM.isAlphaPremultiplied(),
                    null);
        }

        final Image scaleImage = src.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING);
        final Graphics2D g = dst.createGraphics();
        g.drawImage(src, 0, 0, width, height, null);
        g.dispose();

        return dst;
    }

    @Override
    public String toString() {
        return "Distort/Scale"; // NOI18N
    }
}
