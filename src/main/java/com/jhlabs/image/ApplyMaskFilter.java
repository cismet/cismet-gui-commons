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

import java.util.*;

/**
 * A filter which uses the alpha channel of a "mask" image to interpolate between a source and destination image.
 *
 * @version  $Revision$, $Date$
 */
public class ApplyMaskFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private BufferedImage destination;
    private BufferedImage maskImage;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct an ApplyMaskFIlter.
     */
    public ApplyMaskFilter() {
    }

    /**
     * Construct an ApplyMaskFIlter.
     *
     * @param  maskImage    the mask image
     * @param  destination  the destination image
     */
    public ApplyMaskFilter(final BufferedImage maskImage, final BufferedImage destination) {
        this.maskImage = maskImage;
        this.destination = destination;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the destination image.
     *
     * @param  destination  the destination image
     *
     * @see    #getDestination
     */
    public void setDestination(final BufferedImage destination) {
        this.destination = destination;
    }

    /**
     * Get the destination image.
     *
     * @return  the destination image
     *
     * @see     #setDestination
     */
    public BufferedImage getDestination() {
        return destination;
    }

    /**
     * Set the mask image.
     *
     * @param  maskImage  the mask image
     *
     * @see    #getMaskImage
     */
    public void setMaskImage(final BufferedImage maskImage) {
        this.maskImage = maskImage;
    }

    /**
     * Get the mask image.
     *
     * @return  the mask image
     *
     * @see     #setMaskImage
     */
    public BufferedImage getMaskImage() {
        return maskImage;
    }

    /**
     * Interpolates between two rasters according to the alpha level of a mask raster.
     *
     * @param  src  the source raster
     * @param  dst  the destination raster
     * @param  sel  the mask raster
     */
    public static void composeThroughMask(final Raster src, final WritableRaster dst, final Raster sel) {
        final int x = src.getMinX();
        int y = src.getMinY();
        final int w = src.getWidth();
        final int h = src.getHeight();

        int[] srcRGB = null;
        int[] selRGB = null;
        int[] dstRGB = null;

        for (int i = 0; i < h; i++) {
            srcRGB = src.getPixels(x, y, w, 1, srcRGB);
            selRGB = sel.getPixels(x, y, w, 1, selRGB);
            dstRGB = dst.getPixels(x, y, w, 1, dstRGB);

            int k = x;
            for (int j = 0; j < w; j++) {
                final int sr = srcRGB[k];
                final int dir = dstRGB[k];
                final int sg = srcRGB[k + 1];
                final int dig = dstRGB[k + 1];
                final int sb = srcRGB[k + 2];
                final int dib = dstRGB[k + 2];
                final int sa = srcRGB[k + 3];
                final int dia = dstRGB[k + 3];

                final float a = selRGB[k + 3] / 255f;
                final float ac = 1 - a;

                dstRGB[k] = (int)((a * sr) + (ac * dir));
                dstRGB[k + 1] = (int)((a * sg) + (ac * dig));
                dstRGB[k + 2] = (int)((a * sb) + (ac * dib));
                dstRGB[k + 3] = (int)((a * sa) + (ac * dia));
                k += 4;
            }

            dst.setPixels(x, y, w, 1, dstRGB);
            y++;
        }
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int type = src.getType();
        final WritableRaster srcRaster = src.getRaster();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        final WritableRaster dstRaster = dst.getRaster();

        if ((destination != null) && (maskImage != null)) {
            composeThroughMask(src.getRaster(), dst.getRaster(), maskImage.getRaster());
        }

        return dst;
    }

    @Override
    public String toString() {
        return "Keying/Key...";
    }
}
