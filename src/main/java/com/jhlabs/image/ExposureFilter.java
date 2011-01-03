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
 * A filter which changes the exposure of an image.
 *
 * @version  $Revision$, $Date$
 */
public class ExposureFilter extends TransferFilter {

    //~ Instance fields --------------------------------------------------------

    private float exposure = 1.0f;

    //~ Methods ----------------------------------------------------------------

    @Override
    protected float transferFunction(final float f) {
        return 1 - (float)Math.exp(-f * exposure);
    }

    /**
     * Set the exposure level.
     *
     * @param      exposure  the exposure level
     *
     * @see        #getExposure
     * @min-value  0
     * @max-value  5+
     */
    public void setExposure(final float exposure) {
        this.exposure = exposure;
        initialized = false;
    }

    /**
     * Get the exposure level.
     *
     * @return  the exposure level
     *
     * @see     #setExposure
     */
    public float getExposure() {
        return exposure;
    }

    @Override
    public String toString() {
        return "Colors/Exposure..."; // NOI18N
    }
}
