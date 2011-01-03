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
 * A filter which inverts the RGB channels of an image.
 *
 * @version  $Revision$, $Date$
 */
public class InvertFilter extends PointFilter {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new InvertFilter object.
     */
    public InvertFilter() {
        canFilterIndexColorModel = true;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = rgb & 0xff000000;
        return a | (~rgb & 0x00ffffff);
    }

    @Override
    public String toString() {
        return "Colors/Invert"; // NOI18N
    }
}
