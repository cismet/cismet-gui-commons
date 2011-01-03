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
public class OffsetFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private int width;
    private int height;
    private int xOffset;
    private int yOffset;
    private boolean wrap;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OffsetFilter object.
     */
    public OffsetFilter() {
        this(0, 0, true);
    }

    /**
     * Creates a new OffsetFilter object.
     *
     * @param  xOffset  DOCUMENT ME!
     * @param  yOffset  DOCUMENT ME!
     * @param  wrap     DOCUMENT ME!
     */
    public OffsetFilter(final int xOffset, final int yOffset, final boolean wrap) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.wrap = wrap;
        setEdgeAction(ZERO);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  xOffset  DOCUMENT ME!
     */
    public void setXOffset(final int xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getXOffset() {
        return xOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  yOffset  DOCUMENT ME!
     */
    public void setYOffset(final int yOffset) {
        this.yOffset = yOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getYOffset() {
        return yOffset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  wrap  DOCUMENT ME!
     */
    public void setWrap(final boolean wrap) {
        this.wrap = wrap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getWrap() {
        return wrap;
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        if (wrap) {
            out[0] = (x + width - xOffset) % width;
            out[1] = (y + height - yOffset) % height;
        } else {
            out[0] = x - xOffset;
            out[1] = y - yOffset;
        }
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        this.width = src.getWidth();
        this.height = src.getHeight();
        if (wrap) {
            while (xOffset < 0) {
                xOffset += width;
            }
            while (yOffset < 0) {
                yOffset += height;
            }
            xOffset %= width;
            yOffset %= height;
        }
        return super.filter(src, dst);
    }

    @Override
    public String toString() {
        return "Distort/Offset..."; // NOI18N
    }
}
