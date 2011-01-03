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
import java.awt.image.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public abstract class RGBComposite implements Composite {

    //~ Instance fields --------------------------------------------------------

    protected float extraAlpha;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RGBComposite object.
     */
    public RGBComposite() {
        this(1.0f);
    }

    /**
     * Creates a new RGBComposite object.
     *
     * @param   alpha  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    public RGBComposite(final float alpha) {
        if ((alpha < 0.0f) || (alpha > 1.0f)) {
            throw new IllegalArgumentException("RGBComposite: alpha must be between 0 and 1"); // NOI18N
        }
        this.extraAlpha = alpha;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getAlpha() {
        return extraAlpha;
    }

    @Override
    public int hashCode() {
        return Float.floatToIntBits(extraAlpha);
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof RGBComposite)) {
            return false;
        }
        final RGBComposite c = (RGBComposite)o;

        if (extraAlpha != c.extraAlpha) {
            return false;
        }
        return true;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public abstract static class RGBCompositeContext implements CompositeContext {

        //~ Instance fields ----------------------------------------------------

        private float alpha;
        private ColorModel srcColorModel;
        private ColorModel dstColorModel;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RGBCompositeContext object.
         *
         * @param  alpha          DOCUMENT ME!
         * @param  srcColorModel  DOCUMENT ME!
         * @param  dstColorModel  DOCUMENT ME!
         */
        public RGBCompositeContext(final float alpha, final ColorModel srcColorModel, final ColorModel dstColorModel) {
            this.alpha = alpha;
            this.srcColorModel = srcColorModel;
            this.dstColorModel = dstColorModel;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void dispose() {
        }
        /**
         * Multiply two numbers in the range 0..255 such that 255*255=255.
         *
         * @param   a  DOCUMENT ME!
         * @param   b  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        static int multiply255(final int a, final int b) {
            final int t = (a * b) + 0x80;
            return ((t >> 8) + t) >> 8;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   a  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        static int clamp(final int a) {
            return (a < 0) ? 0 : ((a > 255) ? 255 : a);
        }

        /**
         * DOCUMENT ME!
         *
         * @param  src    DOCUMENT ME!
         * @param  dst    DOCUMENT ME!
         * @param  alpha  DOCUMENT ME!
         */
        public abstract void composeRGB(int[] src, int[] dst, float alpha);

        @Override
        public void compose(final Raster src, final Raster dstIn, final WritableRaster dstOut) {
            final float alpha = this.alpha;

            int[] srcPix = null;
            int[] dstPix = null;

            final int x = dstOut.getMinX();
            final int w = dstOut.getWidth();
            final int y0 = dstOut.getMinY();
            final int y1 = y0 + dstOut.getHeight();

            for (int y = y0; y < y1; y++) {
                srcPix = src.getPixels(x, y, w, 1, srcPix);
                dstPix = dstIn.getPixels(x, y, w, 1, dstPix);
                composeRGB(srcPix, dstPix, alpha);
                dstOut.setPixels(x, y, w, 1, dstPix);
            }
        }
    }
}
