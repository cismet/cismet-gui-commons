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
public final class BurnComposite extends RGBComposite {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BurnComposite object.
     *
     * @param  alpha  DOCUMENT ME!
     */
    public BurnComposite(final float alpha) {
        super(alpha);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public CompositeContext createContext(final ColorModel srcColorModel,
            final ColorModel dstColorModel,
            final RenderingHints hints) {
        return new Context(extraAlpha, srcColorModel, dstColorModel);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static class Context extends RGBCompositeContext {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Context object.
         *
         * @param  alpha          DOCUMENT ME!
         * @param  srcColorModel  DOCUMENT ME!
         * @param  dstColorModel  DOCUMENT ME!
         */
        public Context(final float alpha, final ColorModel srcColorModel, final ColorModel dstColorModel) {
            super(alpha, srcColorModel, dstColorModel);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void composeRGB(final int[] src, final int[] dst, final float alpha) {
            final int w = src.length;

            for (int i = 0; i < w; i += 4) {
                final int sr = src[i];
                final int dir = dst[i];
                final int sg = src[i + 1];
                final int dig = dst[i + 1];
                final int sb = src[i + 2];
                final int dib = dst[i + 2];
                final int sa = src[i + 3];
                final int dia = dst[i + 3];
                int dor;
                int dog;
                int dob;

                if (dir != 255) {
                    dor = clamp(255 - (((int)(255 - sr) << 8) / (dir + 1)));
                } else {
                    dor = sr;
                }
                if (dig != 255) {
                    dog = clamp(255 - (((int)(255 - sg) << 8) / (dig + 1)));
                } else {
                    dog = sg;
                }
                if (dib != 255) {
                    dob = clamp(255 - (((int)(255 - sb) << 8) / (dib + 1)));
                } else {
                    dob = sb;
                }

                final float a = alpha * sa / 255f;
                final float ac = 1 - a;

                dst[i] = (int)((a * dor) + (ac * dir));
                dst[i + 1] = (int)((a * dog) + (ac * dig));
                dst[i + 2] = (int)((a * dob) + (ac * dib));
                dst[i + 3] = (int)((sa * alpha) + (dia * ac));
            }
        }
    }
}
