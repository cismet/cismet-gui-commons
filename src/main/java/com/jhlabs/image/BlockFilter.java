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
 * A Filter to pixellate images.
 *
 * @version  $Revision$, $Date$
 */
public class BlockFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private int blockSize = 2;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BlockFilter object.
     */
    public BlockFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the pixel block size.
     *
     * @param      blockSize  the number of pixels along each block edge
     *
     * @see        #getBlockSize
     * @min-value  1
     * @max-value  100+
     */
    public void setBlockSize(final int blockSize) {
        this.blockSize = blockSize;
    }

    /**
     * Get the pixel block size.
     *
     * @return  the number of pixels along each block edge
     *
     * @see     #setBlockSize
     */
    public int getBlockSize() {
        return blockSize;
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        out[0] = (x / blockSize) * blockSize;
        out[1] = (y / blockSize) * blockSize;
    }

    @Override
    public String toString() {
        return "Stylize/Mosaic...";
    }
}
