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

import java.awt.image.*;

/**
 * A simple blur filter. You should probably use BoxBlurFilter instead.
 *
 * @version  $Revision$, $Date$
 */
public class BlurFilter extends ConvolveFilter {

    //~ Static fields/initializers ---------------------------------------------

    /** A 3x3 convolution kernel for a simple blur. */
    protected static float[] blurMatrix = {
            1
                    / 14f,
            2
                    / 14f,
            1
                    / 14f,
            2
                    / 14f,
            2
                    / 14f,
            2
                    / 14f,
            1
                    / 14f,
            2
                    / 14f,
            1
                    / 14f
        };

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BlurFilter object.
     */
    public BlurFilter() {
        super(blurMatrix);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String toString() {
        return "Blur/Simple Blur"; // NOI18N
    }
}
