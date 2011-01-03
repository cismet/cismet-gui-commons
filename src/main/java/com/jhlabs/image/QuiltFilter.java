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
public class QuiltFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    private Random randomGenerator;
    private long seed = 567;
    private int iterations = 25000;
    private float a = -0.59f;
    private float b = 0.2f;
    private float c = 0.1f;
    private float d = 0;
    private int k = 0;
    private Colormap colormap = new LinearColormap();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new QuiltFilter object.
     */
    public QuiltFilter() {
        randomGenerator = new Random();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void randomize() {
        seed = new Date().getTime();
        randomGenerator.setSeed(seed);
        a = randomGenerator.nextFloat();
        b = randomGenerator.nextFloat();
        c = randomGenerator.nextFloat();
        d = randomGenerator.nextFloat();
        k = (randomGenerator.nextInt() % 20) - 10;
    }

    /**
     * Set the number of iterations the effect is performed.
     *
     * @param      iterations  the number of iterations
     *
     * @see        #getIterations
     * @min-value  0
     */
    public void setIterations(final int iterations) {
        this.iterations = iterations;
    }

    /**
     * Get the number of iterations the effect is performed.
     *
     * @return  the number of iterations
     *
     * @see     #setIterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  a  DOCUMENT ME!
     */
    public void setA(final float a) {
        this.a = a;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getA() {
        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  b  DOCUMENT ME!
     */
    public void setB(final float b) {
        this.b = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getB() {
        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public void setC(final float c) {
        this.c = c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getC() {
        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  d  DOCUMENT ME!
     */
    public void setD(final float d) {
        this.d = d;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getD() {
        return d;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  k  DOCUMENT ME!
     */
    public void setK(final int k) {
        this.k = k;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getK() {
        return k;
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

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        final int[] outPixels = new int[width * height];

        final int i = 0;
        int max = 0;

        float x = 0.1f;
        float y = 0.3f;

        for (int n = 0; n < 20; n++) {
            final float mx = ImageMath.PI * x;
            final float my = ImageMath.PI * y;
            final float smx2 = (float)Math.sin(2 * mx);
            final float smy2 = (float)Math.sin(2 * my);
            float x1 = (float)((a * smx2) + (b * smx2 * Math.cos(2 * my))
                            + (c * Math.sin(4 * mx))
                            + (d * Math.sin(6 * mx) * Math.cos(4 * my))
                            + (k * x));
            x1 = (x1 >= 0) ? (x1 - (int)x1) : (x1 - (int)x1 + 1);

            float y1 = (float)((a * smy2) + (b * smy2 * Math.cos(2 * mx))
                            + (c * Math.sin(4 * my))
                            + (d * Math.sin(6 * my) * Math.cos(4 * mx))
                            + (k * y));
            y1 = (y1 >= 0) ? (y1 - (int)y1) : (y1 - (int)y1 + 1);
            x = x1;
            y = y1;
        }

        for (int n = 0; n < iterations; n++) {
            final float mx = ImageMath.PI * x;
            final float my = ImageMath.PI * y;
            float x1 = (float)((a * Math.sin(2 * mx)) + (b * Math.sin(2 * mx) * Math.cos(2 * my))
                            + (c * Math.sin(4 * mx))
                            + (d * Math.sin(6 * mx) * Math.cos(4 * my))
                            + (k * x));
            x1 = (x1 >= 0) ? (x1 - (int)x1) : (x1 - (int)x1 + 1);

            float y1 = (float)((a * Math.sin(2 * my)) + (b * Math.sin(2 * my) * Math.cos(2 * mx))
                            + (c * Math.sin(4 * my))
                            + (d * Math.sin(6 * my) * Math.cos(4 * mx))
                            + (k * y));
            y1 = (y1 >= 0) ? (y1 - (int)y1) : (y1 - (int)y1 + 1);
            x = x1;
            y = y1;
            final int ix = (int)(width * x);
            final int iy = (int)(height * y);
            if ((ix >= 0) && (ix < width) && (iy >= 0) && (iy < height)) {
                final int t = outPixels[(width * iy) + ix]++;
                if (t > max) {
                    max = t;
                }
            }
        }

        if (colormap != null) {
            int index = 0;
            for (y = 0; y < height; y++) {
                for (x = 0; x < width; x++) {
                    outPixels[index] = colormap.getColor(outPixels[index] / (float)max);
                    index++;
                }
            }
        }
        return outPixels;
    }

    @Override
    public String toString() {
        return "Texture/Chaotic Quilt...";
    }
}
