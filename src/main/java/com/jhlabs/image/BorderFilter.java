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
import java.awt.geom.*;
import java.awt.image.*;

/**
 * A filter to add a border around an image using the supplied Paint, which may be null for no painting.
 *
 * @version  $Revision$, $Date$
 */
public class BorderFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private int leftBorder;
    private int rightBorder;
    private int topBorder;
    private int bottomBorder;
    private Paint borderPaint;

    //~ Constructors -----------------------------------------------------------

    /**
     * Construct a BorderFilter which does nothing.
     */
    public BorderFilter() {
    }

    /**
     * Construct a BorderFilter.
     *
     * @param  leftBorder    the left border value
     * @param  topBorder     the top border value
     * @param  rightBorder   the right border value
     * @param  bottomBorder  the bottom border value
     * @param  borderPaint   the paint with which to fill the border
     */
    public BorderFilter(final int leftBorder,
            final int topBorder,
            final int rightBorder,
            final int bottomBorder,
            final Paint borderPaint) {
        this.leftBorder = leftBorder;
        this.topBorder = topBorder;
        this.rightBorder = rightBorder;
        this.bottomBorder = bottomBorder;
        this.borderPaint = borderPaint;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the border size on the left edge.
     *
     * @param      leftBorder  the number of pixels of border to add to the edge
     *
     * @see        #getLeftBorder
     * @min-value  0
     */
    public void setLeftBorder(final int leftBorder) {
        this.leftBorder = leftBorder;
    }

    /**
     * Returns the left border value.
     *
     * @return  the left border value.
     *
     * @see     #setLeftBorder
     */
    public int getLeftBorder() {
        return leftBorder;
    }

    /**
     * Set the border size on the right edge.
     *
     * @param      rightBorder  the number of pixels of border to add to the edge
     *
     * @see        #getRightBorder
     * @min-value  0
     */
    public void setRightBorder(final int rightBorder) {
        this.rightBorder = rightBorder;
    }

    /**
     * Returns the right border value.
     *
     * @return  the right border value.
     *
     * @see     #setRightBorder
     */
    public int getRightBorder() {
        return rightBorder;
    }

    /**
     * Set the border size on the top edge.
     *
     * @param      topBorder  the number of pixels of border to add to the edge
     *
     * @see        #getTopBorder
     * @min-value  0
     */
    public void setTopBorder(final int topBorder) {
        this.topBorder = topBorder;
    }

    /**
     * Returns the top border value.
     *
     * @return  the top border value.
     *
     * @see     #setTopBorder
     */
    public int getTopBorder() {
        return topBorder;
    }

    /**
     * Set the border size on the bottom edge.
     *
     * @param      bottomBorder  the number of pixels of border to add to the edge
     *
     * @see        #getBottomBorder
     * @min-value  0
     */
    public void setBottomBorder(final int bottomBorder) {
        this.bottomBorder = bottomBorder;
    }

    /**
     * Returns the border border value.
     *
     * @return  the border border value.
     *
     * @see     #setBottomBorder
     */
    public int getBottomBorder() {
        return bottomBorder;
    }

    /**
     * Set the border paint.
     *
     * @param  borderPaint  the paint with which to fill the border
     *
     * @see    #getBorderPaint
     */
    public void setBorderPaint(final Paint borderPaint) {
        this.borderPaint = borderPaint;
    }

    /**
     * Get the border paint.
     *
     * @return  the paint with which to fill the border
     *
     * @see     #setBorderPaint
     */
    public Paint getBorderPaint() {
        return borderPaint;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = new BufferedImage(width + leftBorder + rightBorder, height + topBorder + bottomBorder, src.getType());
        }
        final Graphics2D g = dst.createGraphics();
        if (borderPaint != null) {
            g.setPaint(borderPaint);
            if (leftBorder > 0) {
                g.fillRect(0, 0, leftBorder, height);
            }
            if (rightBorder > 0) {
                g.fillRect(width - rightBorder, 0, rightBorder, height);
            }
            if (topBorder > 0) {
                g.fillRect(leftBorder, 0, width - leftBorder - rightBorder, topBorder);
            }
            if (bottomBorder > 0) {
                g.fillRect(leftBorder, height - bottomBorder, width - leftBorder - rightBorder, bottomBorder);
            }
        }
        g.drawRenderedImage(src, AffineTransform.getTranslateInstance(leftBorder, rightBorder));
        g.dispose();
        return dst;
    }

    @Override
    public String toString() {
        return "Distort/Border..."; // NOI18N
    }
}
