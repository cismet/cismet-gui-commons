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

import java.util.*;

/**
 * A filter which draws a coloured gradient. This is largely superceded by GradientPaint in Java1.2, but does provide a
 * few more gradient options.
 *
 * @version  $Revision$, $Date$
 */
public class GradientFilter extends AbstractBufferedImageOp {

    //~ Static fields/initializers ---------------------------------------------

    public static final int LINEAR = 0;
    public static final int BILINEAR = 1;
    public static final int RADIAL = 2;
    public static final int CONICAL = 3;
    public static final int BICONICAL = 4;
    public static final int SQUARE = 5;

    public static final int INT_LINEAR = 0;
    public static final int INT_CIRCLE_UP = 1;
    public static final int INT_CIRCLE_DOWN = 2;
    public static final int INT_SMOOTH = 3;

    //~ Instance fields --------------------------------------------------------

    private float angle = 0;
    private int color1 = 0xff000000;
    private int color2 = 0xffffffff;
    private Point p1 = new Point(0, 0);
    private Point p2 = new Point(64, 64);
    private boolean repeat = false;
    private float x1;
    private float y1;
    private float dx;
    private float dy;
    private Colormap colormap = null;
    private int type;
    private int interpolation = INT_LINEAR;
    private int paintMode = PixelUtils.NORMAL;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new GradientFilter object.
     */
    public GradientFilter() {
    }

    /**
     * Creates a new GradientFilter object.
     *
     * @param  p1             DOCUMENT ME!
     * @param  p2             DOCUMENT ME!
     * @param  color1         DOCUMENT ME!
     * @param  color2         DOCUMENT ME!
     * @param  repeat         DOCUMENT ME!
     * @param  type           DOCUMENT ME!
     * @param  interpolation  DOCUMENT ME!
     */
    public GradientFilter(final Point p1,
            final Point p2,
            final int color1,
            final int color2,
            final boolean repeat,
            final int type,
            final int interpolation) {
        this.p1 = p1;
        this.p2 = p2;
        this.color1 = color1;
        this.color2 = color2;
        this.repeat = repeat;
        this.type = type;
        this.interpolation = interpolation;
        colormap = new LinearColormap(color1, color2);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  point1  DOCUMENT ME!
     */
    public void setPoint1(final Point point1) {
        this.p1 = point1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Point getPoint1() {
        return p1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  point2  DOCUMENT ME!
     */
    public void setPoint2(final Point point2) {
        this.p2 = point2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Point getPoint2() {
        return p2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  type  DOCUMENT ME!
     */
    public void setType(final int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  interpolation  DOCUMENT ME!
     */
    public void setInterpolation(final int interpolation) {
        this.interpolation = interpolation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getInterpolation() {
        return interpolation;
    }

    /**
     * Specifies the angle of the texture.
     *
     * @param  angle  the angle of the texture.
     *
     * @see    #getAngle
     * @angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
        p2 = new Point((int)(64 * Math.cos(angle)), (int)(64 * Math.sin(angle)));
    }

    /**
     * Returns the angle of the texture.
     *
     * @return  the angle of the texture.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Set the colormap to be used for the filter.
     *
     * @param  colormap  the colormap
     *
     * @see    #getColormap
     */
    public void setColormap(final Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * Get the colormap to be used for the filter.
     *
     * @return  the colormap
     *
     * @see     #setColormap
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  paintMode  DOCUMENT ME!
     */
    public void setPaintMode(final int paintMode) {
        this.paintMode = paintMode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPaintMode() {
        return paintMode;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        int rgb1;
        int rgb2;
        float x1;
        float y1;
        float x2;
        float y2;
        x1 = p1.x;
        x2 = p2.x;

        if ((x1 > x2) && (type != RADIAL)) {
            y1 = x1;
            x1 = x2;
            x2 = y1;
            y1 = p2.y;
            y2 = p1.y;
            rgb1 = color2;
            rgb2 = color1;
        } else {
            y1 = p1.y;
            y2 = p2.y;
            rgb1 = color1;
            rgb2 = color2;
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        final float lenSq = (dx * dx) + (dy * dy);
        this.x1 = x1;
        this.y1 = y1;
        if (lenSq >= Float.MIN_VALUE) {
            dx = dx / lenSq;
            dy = dy / lenSq;
            if (repeat) {
                dx = dx % 1.0f;
                dy = dy % 1.0f;
            }
        }
        this.dx = dx;
        this.dy = dy;

        final int[] pixels = new int[width];
        for (int y = 0; y < height; y++) {
            getRGB(src, 0, y, width, 1, pixels);
            switch (type) {
                case LINEAR:
                case BILINEAR: {
                    linearGradient(pixels, y, width, 1);
                    break;
                }
                case RADIAL: {
                    radialGradient(pixels, y, width, 1);
                    break;
                }
                case CONICAL:
                case BICONICAL: {
                    conicalGradient(pixels, y, width, 1);
                    break;
                }
                case SQUARE: {
                    squareGradient(pixels, y, width, 1);
                    break;
                }
            }
            setRGB(dst, 0, y, width, 1, pixels);
        }
        return dst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     * @param  rowrel  DOCUMENT ME!
     * @param  dx      DOCUMENT ME!
     * @param  dy      DOCUMENT ME!
     */
    private void repeatGradient(final int[] pixels,
            final int w,
            final int h,
            float rowrel,
            final float dx,
            final float dy) {
        int off = 0;
        for (int y = 0; y < h; y++) {
            float colrel = rowrel;
            int j = w;
            int rgb;
            while (--j >= 0) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(map(ImageMath.triangle(colrel)));
                } else {
                    rgb = colormap.getColor(map(ImageMath.mod(colrel, 1.0f)));
                }
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
                off++;
                colrel += dx;
            }
            rowrel += dy;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     * @param  rowrel  DOCUMENT ME!
     * @param  dx      DOCUMENT ME!
     * @param  dy      DOCUMENT ME!
     */
    private void singleGradient(final int[] pixels,
            final int w,
            final int h,
            float rowrel,
            final float dx,
            final float dy) {
        int off = 0;
        for (int y = 0; y < h; y++) {
            float colrel = rowrel;
            int j = w;
            int rgb;
            if (colrel <= 0.0) {
                rgb = colormap.getColor(0);
                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
                    off++;
                    colrel += dx;
                } while ((--j > 0) && (colrel <= 0.0));
            }
            while ((colrel < 1.0) && (--j >= 0)) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(map(ImageMath.triangle(colrel)));
                } else {
                    rgb = colormap.getColor(map(colrel));
                }
                pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
                off++;
                colrel += dx;
            }
            if (j > 0) {
                if (type == BILINEAR) {
                    rgb = colormap.getColor(0.0f);
                } else {
                    rgb = colormap.getColor(1.0f);
                }
                do {
                    pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
                    off++;
                } while (--j > 0);
            }
            rowrel += dy;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     */
    private void linearGradient(final int[] pixels, final int y, final int w, final int h) {
        final int x = 0;
        final float rowrel = ((x - x1) * dx) + ((y - y1) * dy);
        if (repeat) {
            repeatGradient(pixels, w, h, rowrel, dx, dy);
        } else {
            singleGradient(pixels, w, h, rowrel, dx, dy);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     */
    private void radialGradient(final int[] pixels, final int y, final int w, final int h) {
        int off = 0;
        final float radius = distance(p2.x - p1.x, p2.y - p1.y);
        for (int x = 0; x < w; x++) {
            final float distance = distance(x - p1.x, y - p1.y);
            float ratio = distance / radius;
            if (repeat) {
                ratio = ratio % 2;
            } else if (ratio > 1.0) {
                ratio = 1.0f;
            }
            final int rgb = colormap.getColor(map(ratio));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
            off++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     */
    private void squareGradient(final int[] pixels, final int y, final int w, final int h) {
        int off = 0;
        final float radius = Math.max(Math.abs(p2.x - p1.x), Math.abs(p2.y - p1.y));
        for (int x = 0; x < w; x++) {
            final float distance = Math.max(Math.abs(x - p1.x), Math.abs(y - p1.y));
            float ratio = distance / radius;
            if (repeat) {
                ratio = ratio % 2;
            } else if (ratio > 1.0) {
                ratio = 1.0f;
            }
            final int rgb = colormap.getColor(map(ratio));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
            off++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels  DOCUMENT ME!
     * @param  y       DOCUMENT ME!
     * @param  w       DOCUMENT ME!
     * @param  h       DOCUMENT ME!
     */
    private void conicalGradient(final int[] pixels, final int y, final int w, final int h) {
        int off = 0;
        final float angle0 = (float)Math.atan2(p2.x - p1.x, p2.y - p1.y);
        for (int x = 0; x < w; x++) {
            float angle = (float)(Math.atan2(x - p1.x, y - p1.y) - angle0) / (ImageMath.TWO_PI);
            angle += 1.0f;
            angle %= 1.0f;
            if (type == BICONICAL) {
                angle = ImageMath.triangle(angle);
            }
            final int rgb = colormap.getColor(map(angle));
            pixels[off] = PixelUtils.combinePixels(rgb, pixels[off], paintMode);
            off++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private float map(float v) {
        if (repeat) {
            v = (v > 1.0) ? (2.0f - v) : v;
        }
        switch (interpolation) {
            case INT_CIRCLE_UP: {
                v = ImageMath.circleUp(ImageMath.clamp(v, 0.0f, 1.0f));
                break;
            }
            case INT_CIRCLE_DOWN: {
                v = ImageMath.circleDown(ImageMath.clamp(v, 0.0f, 1.0f));
                break;
            }
            case INT_SMOOTH: {
                v = ImageMath.smoothStep(0, 1, v);
                break;
            }
        }
        return v;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private float distance(final float a, final float b) {
        return (float)Math.sqrt((a * a) + (b * b));
    }

    @Override
    public String toString() {
        return "Other/Gradient Fill..."; // NOI18N
    }
}
