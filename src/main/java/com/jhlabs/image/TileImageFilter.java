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
 * A filter which tiles an image into a lerger one.
 *
 * @version  $Revision$, $Date$
 */
public class TileImageFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private int width;
    private int height;
    private int tileWidth;
    private int tileHeight;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a TileImageFilter.
     */
    public TileImageFilter() {
        this(32, 32);
    }

    /**
     * Construct a TileImageFilter.
     *
     * @param  width   the output image width
     * @param  height  the output image height
     */
    public TileImageFilter(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the output image width.
     *
     * @param  width  the width
     *
     * @see    #getWidth
     */
    public void setWidth(final int width) {
        this.width = width;
    }

    /**
     * Get the output image width.
     *
     * @return  the width
     *
     * @see     #setWidth
     */
    public int getWidth() {
        return width;
    }

    /**
     * Set the output image height.
     *
     * @param  height  the height
     *
     * @see    #getHeight
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Get the output image height.
     *
     * @return  the height
     *
     * @see     #setHeight
     */
    public int getHeight() {
        return height;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int tileWidth = src.getWidth();
        final int tileHeight = src.getHeight();

        if (dst == null) {
            final ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(
                    dstCM,
                    dstCM.createCompatibleWritableRaster(width, height),
                    dstCM.isAlphaPremultiplied(),
                    null);
        }

        final Graphics2D g = dst.createGraphics();
        for (int y = 0; y < height; y += tileHeight) {
            for (int x = 0; x < width; x += tileWidth) {
                g.drawImage(src, null, x, y);
            }
        }
        g.dispose();

        return dst;
    }

    @Override
    public String toString() {
        return "Tile"; // NOI18N
    }
}
