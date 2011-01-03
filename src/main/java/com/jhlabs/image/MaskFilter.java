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

import java.io.*;

/**
 * Applies a bit mask to each ARGB pixel of an image. You can use this for, say, masking out the red channel.
 *
 * @version  $Revision$, $Date$
 */
public class MaskFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    private int mask;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MaskFilter object.
     */
    public MaskFilter() {
        this(0xff00ffff);
    }

    /**
     * Creates a new MaskFilter object.
     *
     * @param  mask  DOCUMENT ME!
     */
    public MaskFilter(final int mask) {
        canFilterIndexColorModel = true;
        setMask(mask);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  mask  DOCUMENT ME!
     */
    public void setMask(final int mask) {
        this.mask = mask;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getMask() {
        return mask;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        return rgb & mask;
    }

    @Override
    public String toString() {
        return "Mask"; // NOI18N
    }
}
