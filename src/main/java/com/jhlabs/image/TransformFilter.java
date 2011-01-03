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
 * An abstract superclass for filters which distort images in some way. The subclass only needs to override two methods
 * to provide the mapping between source and destination pixels.
 *
 * @version  $Revision$, $Date$
 */
public abstract class TransformFilter extends AbstractBufferedImageOp {

    //~ Static fields/initializers ---------------------------------------------

    /** Treat pixels off the edge as zero. */
    public static final int ZERO = 0;

    /** Clamp pixels to the image edges. */
    public static final int CLAMP = 1;

    /** Wrap pixels off the edge onto the oppsoite edge. */
    public static final int WRAP = 2;

    /** Use nearest-neighbout interpolation. */
    public static final int NEAREST_NEIGHBOUR = 0;

    /** Use bilinear interpolation. */
    public static final int BILINEAR = 1;

    //~ Instance fields --------------------------------------------------------

    /** The action to take for pixels off the image edge. */
    protected int edgeAction = ZERO;

    /** The type of interpolation to use. */
    protected int interpolation = BILINEAR;

    /** The output image rectangle. */
    protected Rectangle transformedSpace;

    /** The input image rectangle. */
    protected Rectangle originalSpace;

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the action to perform for pixels off the edge of the image.
     *
     * @param  edgeAction  one of ZERO, CLAMP or WRAP
     *
     * @see    #getEdgeAction
     */
    public void setEdgeAction(final int edgeAction) {
        this.edgeAction = edgeAction;
    }

    /**
     * Get the action to perform for pixels off the edge of the image.
     *
     * @return  one of ZERO, CLAMP or WRAP
     *
     * @see     #setEdgeAction
     */
    public int getEdgeAction() {
        return edgeAction;
    }

    /**
     * Set the type of interpolation to perform.
     *
     * @param  interpolation  one of NEAREST_NEIGHBOUR or BILINEAR
     *
     * @see    #getInterpolation
     */
    public void setInterpolation(final int interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * Get the type of interpolation to perform.
     *
     * @return  one of NEAREST_NEIGHBOUR or BILINEAR
     *
     * @see     #setInterpolation
     */
    public int getInterpolation() {
        return interpolation;
    }

    /**
     * Inverse transform a point. This method needs to be overriden by all subclasses.
     *
     * @param  x    the X position of the pixel in the output image
     * @param  y    the Y position of the pixel in the output image
     * @param  out  the position of the pixel in the input image
     */
    protected abstract void transformInverse(int x, int y, float[] out);

    /**
     * Forward transform a rectangle. Used to determine the size of the output image.
     *
     * @param  rect  the rectangle to transform
     */
    protected void transformSpace(final Rectangle rect) {
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int type = src.getType();
        final WritableRaster srcRaster = src.getRaster();

        originalSpace = new Rectangle(0, 0, width, height);
        transformedSpace = new Rectangle(0, 0, width, height);
        transformSpace(transformedSpace);

        if (dst == null) {
            final ColorModel dstCM = src.getColorModel();
            dst = new BufferedImage(
                    dstCM,
                    dstCM.createCompatibleWritableRaster(transformedSpace.width, transformedSpace.height),
                    dstCM.isAlphaPremultiplied(),
                    null);
        }
        final WritableRaster dstRaster = dst.getRaster();

        final int[] inPixels = getRGB(src, 0, 0, width, height, null);

        if (interpolation == NEAREST_NEIGHBOUR) {
            return filterPixelsNN(dst, width, height, inPixels, transformedSpace);
        }

        final int srcWidth = width;
        final int srcHeight = height;
        final int srcWidth1 = width - 1;
        final int srcHeight1 = height - 1;
        final int outWidth = transformedSpace.width;
        final int outHeight = transformedSpace.height;
        final int outX;
        final int outY;
        final int index = 0;
        final int[] outPixels = new int[outWidth];

        outX = transformedSpace.x;
        outY = transformedSpace.y;
        final float[] out = new float[2];

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                transformInverse(outX + x, outY + y, out);
                final int srcX = (int)Math.floor(out[0]);
                final int srcY = (int)Math.floor(out[1]);
                final float xWeight = out[0] - srcX;
                final float yWeight = out[1] - srcY;
                int nw;
                int ne;
                int sw;
                int se;

                if ((srcX >= 0) && (srcX < srcWidth1) && (srcY >= 0) && (srcY < srcHeight1)) {
                    // Easy case, all corners are in the image
                    final int i = (srcWidth * srcY) + srcX;
                    nw = inPixels[i];
                    ne = inPixels[i + 1];
                    sw = inPixels[i + srcWidth];
                    se = inPixels[i + srcWidth + 1];
                } else {
                    // Some of the corners are off the image
                    nw = getPixel(inPixels, srcX, srcY, srcWidth, srcHeight);
                    ne = getPixel(inPixels, srcX + 1, srcY, srcWidth, srcHeight);
                    sw = getPixel(inPixels, srcX, srcY + 1, srcWidth, srcHeight);
                    se = getPixel(inPixels, srcX + 1, srcY + 1, srcWidth, srcHeight);
                }
                outPixels[x] = ImageMath.bilinearInterpolate(xWeight, yWeight, nw, ne, sw, se);
            }
            setRGB(dst, 0, y, transformedSpace.width, 1, outPixels);
        }
        return dst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   pixels  DOCUMENT ME!
     * @param   x       DOCUMENT ME!
     * @param   y       DOCUMENT ME!
     * @param   width   DOCUMENT ME!
     * @param   height  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getPixel(final int[] pixels, final int x, final int y, final int width, final int height) {
        if ((x < 0) || (x >= width) || (y < 0) || (y >= height)) {
            switch (edgeAction) {
                case ZERO:
                default: {
                    return 0;
                }
                case WRAP: {
                    return pixels[(ImageMath.mod(y, height) * width) + ImageMath.mod(x, width)];
                }
                case CLAMP: {
                    return pixels[(ImageMath.clamp(y, 0, height - 1) * width) + ImageMath.clamp(x, 0, width - 1)];
                }
            }
        }
        return pixels[(y * width) + x];
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dst               DOCUMENT ME!
     * @param   width             DOCUMENT ME!
     * @param   height            DOCUMENT ME!
     * @param   inPixels          DOCUMENT ME!
     * @param   transformedSpace  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected BufferedImage filterPixelsNN(final BufferedImage dst,
            final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        final int srcWidth = width;
        final int srcHeight = height;
        final int outWidth = transformedSpace.width;
        final int outHeight = transformedSpace.height;
        final int outX;
        final int outY;
        int srcX;
        int srcY;
        final int[] outPixels = new int[outWidth];

        outX = transformedSpace.x;
        outY = transformedSpace.y;
        final int[] rgb = new int[4];
        final float[] out = new float[2];

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                transformInverse(outX + x, outY + y, out);
                srcX = (int)out[0];
                srcY = (int)out[1];
                // int casting rounds towards zero, so we check out[0] < 0, not srcX < 0
                if ((out[0] < 0) || (srcX >= srcWidth) || (out[1] < 0) || (srcY >= srcHeight)) {
                    int p;
                    switch (edgeAction) {
                        case ZERO:
                        default: {
                            p = 0;
                            break;
                        }
                        case WRAP: {
                            p = inPixels[(ImageMath.mod(srcY, srcHeight) * srcWidth) + ImageMath.mod(srcX, srcWidth)];
                            break;
                        }
                        case CLAMP: {
                            p = inPixels[(ImageMath.clamp(srcY, 0, srcHeight - 1) * srcWidth)
                                            + ImageMath.clamp(srcX, 0, srcWidth - 1)];
                            break;
                        }
                    }
                    outPixels[x] = p;
                } else {
                    final int i = (srcWidth * srcY) + srcX;
                    rgb[0] = inPixels[i];
                    outPixels[x] = inPixels[i];
                }
            }
            setRGB(dst, 0, y, transformedSpace.width, 1, outPixels);
        }
        return dst;
    }
}
