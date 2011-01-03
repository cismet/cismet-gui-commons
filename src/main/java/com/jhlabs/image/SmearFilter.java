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

import com.jhlabs.math.*;

import java.awt.*;
import java.awt.image.*;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SmearFilter extends WholeImageFilter {

    //~ Static fields/initializers ---------------------------------------------

    public static final int CROSSES = 0;
    public static final int LINES = 1;
    public static final int CIRCLES = 2;
    public static final int SQUARES = 3;

    //~ Instance fields --------------------------------------------------------

    private Colormap colormap = new LinearColormap();
    private float angle = 0;
    private float density = 0.5f;
    private float scatter = 0.0f;
    private int distance = 8;
    private Random randomGenerator;
    private long seed = 567;
    private int shape = LINES;
    private float mix = 0.5f;
    private int fadeout = 0;
    private boolean background = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SmearFilter object.
     */
    public SmearFilter() {
        randomGenerator = new Random();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  shape  DOCUMENT ME!
     */
    public void setShape(final int shape) {
        this.shape = shape;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getShape() {
        return shape;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  distance  DOCUMENT ME!
     */
    public void setDistance(final int distance) {
        this.distance = distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDistance() {
        return distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  density  DOCUMENT ME!
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getDensity() {
        return density;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scatter  DOCUMENT ME!
     */
    public void setScatter(final float scatter) {
        this.scatter = scatter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getScatter() {
        return scatter;
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
     * DOCUMENT ME!
     *
     * @param  mix  DOCUMENT ME!
     */
    public void setMix(final float mix) {
        this.mix = mix;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getMix() {
        return mix;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fadeout  DOCUMENT ME!
     */
    public void setFadeout(final int fadeout) {
        this.fadeout = fadeout;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getFadeout() {
        return fadeout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  background  DOCUMENT ME!
     */
    public void setBackground(final boolean background) {
        this.background = background;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getBackground() {
        return background;
    }

    /**
     * DOCUMENT ME!
     */
    public void randomize() {
        seed = new Date().getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   low   DOCUMENT ME!
     * @param   high  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private float random(final float low, final float high) {
        return low + ((high - low) * randomGenerator.nextFloat());
    }

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        final int[] outPixels = new int[width * height];

        randomGenerator.setSeed(seed);
        final float sinAngle = (float)Math.sin(angle);
        final float cosAngle = (float)Math.cos(angle);

        int i = 0;
        int numShapes;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                outPixels[i] = background ? 0xffffffff : inPixels[i];
                i++;
            }
        }

        switch (shape) {
            case CROSSES: {
                // Crosses
                numShapes = (int)(2 * density * width * height / (distance + 1));
                for (i = 0; i < numShapes; i++) {
                    final int x = (randomGenerator.nextInt() & 0x7fffffff) % width;
                    final int y = (randomGenerator.nextInt() & 0x7fffffff) % height;
                    final int length = (randomGenerator.nextInt() % distance) + 1;
                    final int rgb = inPixels[(y * width) + x];
                    for (int x1 = x - length; x1 < (x + length + 1); x1++) {
                        if ((x1 >= 0) && (x1 < width)) {
                            final int rgb2 = background ? 0xffffffff : outPixels[(y * width) + x1];
                            outPixels[(y * width) + x1] = ImageMath.mixColors(mix, rgb2, rgb);
                        }
                    }
                    for (int y1 = y - length; y1 < (y + length + 1); y1++) {
                        if ((y1 >= 0) && (y1 < height)) {
                            final int rgb2 = background ? 0xffffffff : outPixels[(y1 * width) + x];
                            outPixels[(y1 * width) + x] = ImageMath.mixColors(mix, rgb2, rgb);
                        }
                    }
                }
                break;
            }
            case LINES: {
                numShapes = (int)(2 * density * width * height / 2);

                for (i = 0; i < numShapes; i++) {
                    final int sx = (randomGenerator.nextInt() & 0x7fffffff) % width;
                    final int sy = (randomGenerator.nextInt() & 0x7fffffff) % height;
                    final int rgb = inPixels[(sy * width) + sx];
                    final int length = (randomGenerator.nextInt() & 0x7fffffff) % distance;
                    int dx = (int)(length * cosAngle);
                    int dy = (int)(length * sinAngle);

                    final int x0 = sx - dx;
                    final int y0 = sy - dy;
                    final int x1 = sx + dx;
                    final int y1 = sy + dy;
                    int x;
                    int y;
                    int d;
                    int incrE;
                    int incrNE;
                    int ddx;
                    int ddy;

                    if (x1 < x0) {
                        ddx = -1;
                    } else {
                        ddx = 1;
                    }
                    if (y1 < y0) {
                        ddy = -1;
                    } else {
                        ddy = 1;
                    }
                    dx = x1 - x0;
                    dy = y1 - y0;
                    dx = Math.abs(dx);
                    dy = Math.abs(dy);
                    x = x0;
                    y = y0;

                    if ((x < width) && (x >= 0) && (y < height) && (y >= 0)) {
                        final int rgb2 = background ? 0xffffffff : outPixels[(y * width) + x];
                        outPixels[(y * width) + x] = ImageMath.mixColors(mix, rgb2, rgb);
                    }
                    if (Math.abs(dx) > Math.abs(dy)) {
                        d = (2 * dy) - dx;
                        incrE = 2 * dy;
                        incrNE = 2 * (dy - dx);

                        while (x != x1) {
                            if (d <= 0) {
                                d += incrE;
                            } else {
                                d += incrNE;
                                y += ddy;
                            }
                            x += ddx;
                            if ((x < width) && (x >= 0) && (y < height) && (y >= 0)) {
                                final int rgb2 = background ? 0xffffffff : outPixels[(y * width) + x];
                                outPixels[(y * width) + x] = ImageMath.mixColors(mix, rgb2, rgb);
                            }
                        }
                    } else {
                        d = (2 * dx) - dy;
                        incrE = 2 * dx;
                        incrNE = 2 * (dx - dy);

                        while (y != y1) {
                            if (d <= 0) {
                                d += incrE;
                            } else {
                                d += incrNE;
                                x += ddx;
                            }
                            y += ddy;
                            if ((x < width) && (x >= 0) && (y < height) && (y >= 0)) {
                                final int rgb2 = background ? 0xffffffff : outPixels[(y * width) + x];
                                outPixels[(y * width) + x] = ImageMath.mixColors(mix, rgb2, rgb);
                            }
                        }
                    }
                }
                break;
            }
            case SQUARES:
            case CIRCLES: {
                final int radius = distance + 1;
                final int radius2 = radius * radius;
                numShapes = (int)(2 * density * width * height / radius);
                for (i = 0; i < numShapes; i++) {
                    final int sx = (randomGenerator.nextInt() & 0x7fffffff) % width;
                    final int sy = (randomGenerator.nextInt() & 0x7fffffff) % height;
                    final int rgb = inPixels[(sy * width) + sx];
                    for (int x = sx - radius; x < (sx + radius + 1); x++) {
                        for (int y = sy - radius; y < (sy + radius + 1); y++) {
                            int f;
                            if (shape == CIRCLES) {
                                f = ((x - sx) * (x - sx)) + ((y - sy) * (y - sy));
                            } else {
                                f = 0;
                            }
                            if ((x >= 0) && (x < width) && (y >= 0) && (y < height) && (f <= radius2)) {
                                final int rgb2 = background ? 0xffffffff : outPixels[(y * width) + x];
                                outPixels[(y * width) + x] = ImageMath.mixColors(mix, rgb2, rgb);
                            }
                        }
                    }
                }
            }
        }

        return outPixels;
    }

    @Override
    public String toString() {
        return "Effects/Smear..."; // NOI18N
    }
}
