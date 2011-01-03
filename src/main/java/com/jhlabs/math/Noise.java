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
package com.jhlabs.math;

import java.util.*;

/**
 * Perlin Noise functions.
 *
 * @version  $Revision$, $Date$
 */
public class Noise implements Function1D, Function2D, Function3D {

    //~ Static fields/initializers ---------------------------------------------

    private static Random randomGenerator = new Random();

    private static final int B = 0x100;
    private static final int BM = 0xff;
    private static final int N = 0x1000;

    static int[] p = new int[B + B + 2];
    static float[][] g3 = new float[B + B + 2][3];
    static float[][] g2 = new float[B + B + 2][2];
    static float[] g1 = new float[B + B + 2];
    static boolean start = true;

    //~ Methods ----------------------------------------------------------------

    @Override
    public float evaluate(final float x) {
        return noise1(x);
    }

    @Override
    public float evaluate(final float x, final float y) {
        return noise2(x, y);
    }

    @Override
    public float evaluate(final float x, final float y, final float z) {
        return noise3(x, y, z);
    }

    /**
     * Compute turbulence using Perlin noise.
     *
     * @param   x        the x value
     * @param   y        the y value
     * @param   octaves  number of octaves of turbulence
     *
     * @return  turbulence value at (x,y)
     */
    public static float turbulence2(final float x, final float y, final float octaves) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2) {
            t += Math.abs(noise2(f * x, f * y)) / f;
        }
        return t;
    }

    /**
     * Compute turbulence using Perlin noise.
     *
     * @param   x        the x value
     * @param   y        the y value
     * @param   z        DOCUMENT ME!
     * @param   octaves  number of octaves of turbulence
     *
     * @return  turbulence value at (x,y)
     */
    public static float turbulence3(final float x, final float y, final float z, final float octaves) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2) {
            t += Math.abs(noise3(f * x, f * y, f * z)) / f;
        }
        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   t  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static float sCurve(final float t) {
        return t * t * (3.0f - (2.0f * t));
    }

    /**
     * Compute 1-dimensional Perlin noise.
     *
     * @param   x  the x value
     *
     * @return  noise value at x in the range -1..1
     */
    public static float noise1(final float x) {
        final int bx0;
        final int bx1;
        final float rx0;
        final float rx1;
        final float sx;
        final float t;
        final float u;
        final float v;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int)t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int)t;
        rx1 = rx0 - 1.0f;

        sx = sCurve(rx0);

        u = rx0 * g1[p[bx0]];
        v = rx1 * g1[p[bx1]];
        return 2.3f * lerp(sx, u, v);
    }

    /**
     * Compute 2-dimensional Perlin noise.
     *
     * @param   x  the x coordinate
     * @param   y  the y coordinate
     *
     * @return  noise value at (x,y)
     */
    public static float noise2(final float x, final float y) {
        final int bx0;
        final int bx1;
        final int by0;
        final int by1;
        final int b00;
        final int b10;
        final int b01;
        final int b11;
        final float rx0;
        final float rx1;
        final float ry0;
        final float ry1;
        float[] q;
        final float sx;
        final float sy;
        final float a;
        final float b;
        float t;
        float u;
        float v;
        final int i;
        final int j;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int)t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int)t;
        rx1 = rx0 - 1.0f;

        t = y + N;
        by0 = ((int)t) & BM;
        by1 = (by0 + 1) & BM;
        ry0 = t - (int)t;
        ry1 = ry0 - 1.0f;

        i = p[bx0];
        j = p[bx1];

        b00 = p[i + by0];
        b10 = p[j + by0];
        b01 = p[i + by1];
        b11 = p[j + by1];

        sx = sCurve(rx0);
        sy = sCurve(ry0);

        q = g2[b00];
        u = (rx0 * q[0]) + (ry0 * q[1]);
        q = g2[b10];
        v = (rx1 * q[0]) + (ry0 * q[1]);
        a = lerp(sx, u, v);

        q = g2[b01];
        u = (rx0 * q[0]) + (ry1 * q[1]);
        q = g2[b11];
        v = (rx1 * q[0]) + (ry1 * q[1]);
        b = lerp(sx, u, v);

        return 1.5f * lerp(sy, a, b);
    }

    /**
     * Compute 3-dimensional Perlin noise.
     *
     * @param   x  the x coordinate
     * @param   y  the y coordinate
     * @param   z  y the y coordinate
     *
     * @return  noise value at (x,y,z)
     */
    public static float noise3(final float x, final float y, final float z) {
        final int bx0;
        final int bx1;
        final int by0;
        final int by1;
        final int bz0;
        final int bz1;
        final int b00;
        final int b10;
        final int b01;
        final int b11;
        final float rx0;
        final float rx1;
        final float ry0;
        final float ry1;
        final float rz0;
        final float rz1;
        float[] q;
        final float sy;
        final float sz;
        float a;
        float b;
        final float c;
        final float d;
        float t;
        float u;
        float v;
        final int i;
        final int j;

        if (start) {
            start = false;
            init();
        }

        t = x + N;
        bx0 = ((int)t) & BM;
        bx1 = (bx0 + 1) & BM;
        rx0 = t - (int)t;
        rx1 = rx0 - 1.0f;

        t = y + N;
        by0 = ((int)t) & BM;
        by1 = (by0 + 1) & BM;
        ry0 = t - (int)t;
        ry1 = ry0 - 1.0f;

        t = z + N;
        bz0 = ((int)t) & BM;
        bz1 = (bz0 + 1) & BM;
        rz0 = t - (int)t;
        rz1 = rz0 - 1.0f;

        i = p[bx0];
        j = p[bx1];

        b00 = p[i + by0];
        b10 = p[j + by0];
        b01 = p[i + by1];
        b11 = p[j + by1];

        t = sCurve(rx0);
        sy = sCurve(ry0);
        sz = sCurve(rz0);

        q = g3[b00 + bz0];
        u = (rx0 * q[0]) + (ry0 * q[1]) + (rz0 * q[2]);
        q = g3[b10 + bz0];
        v = (rx1 * q[0]) + (ry0 * q[1]) + (rz0 * q[2]);
        a = lerp(t, u, v);

        q = g3[b01 + bz0];
        u = (rx0 * q[0]) + (ry1 * q[1]) + (rz0 * q[2]);
        q = g3[b11 + bz0];
        v = (rx1 * q[0]) + (ry1 * q[1]) + (rz0 * q[2]);
        b = lerp(t, u, v);

        c = lerp(sy, a, b);

        q = g3[b00 + bz1];
        u = (rx0 * q[0]) + (ry0 * q[1]) + (rz1 * q[2]);
        q = g3[b10 + bz1];
        v = (rx1 * q[0]) + (ry0 * q[1]) + (rz1 * q[2]);
        a = lerp(t, u, v);

        q = g3[b01 + bz1];
        u = (rx0 * q[0]) + (ry1 * q[1]) + (rz1 * q[2]);
        q = g3[b11 + bz1];
        v = (rx1 * q[0]) + (ry1 * q[1]) + (rz1 * q[2]);
        b = lerp(t, u, v);

        d = lerp(sy, a, b);

        return 1.5f * lerp(sz, c, d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   t  DOCUMENT ME!
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static float lerp(final float t, final float a, final float b) {
        return a + (t * (b - a));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  v  DOCUMENT ME!
     */
    private static void normalize2(final float[] v) {
        final float s = (float)Math.sqrt((v[0] * v[0]) + (v[1] * v[1]));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  v  DOCUMENT ME!
     */
    static void normalize3(final float[] v) {
        final float s = (float)Math.sqrt((v[0] * v[0]) + (v[1] * v[1]) + (v[2] * v[2]));
        v[0] = v[0] / s;
        v[1] = v[1] / s;
        v[2] = v[2] / s;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int random() {
        return randomGenerator.nextInt() & 0x7fffffff;
    }

    /**
     * DOCUMENT ME!
     */
    private static void init() {
        int i;
        int j;
        int k;

        for (i = 0; i < B; i++) {
            p[i] = i;

            g1[i] = (float)((random() % (B + B)) - B) / B;

            for (j = 0; j < 2; j++) {
                g2[i][j] = (float)((random() % (B + B)) - B) / B;
            }
            normalize2(g2[i]);

            for (j = 0; j < 3; j++) {
                g3[i][j] = (float)((random() % (B + B)) - B) / B;
            }
            normalize3(g3[i]);
        }

        for (i = B - 1; i >= 0; i--) {
            k = p[i];
            p[i] = p[j = random() % B];
            p[j] = k;
        }

        for (i = 0; i < (B + 2); i++) {
            p[B + i] = p[i];
            g1[B + i] = g1[i];
            for (j = 0; j < 2; j++) {
                g2[B + i][j] = g2[i][j];
            }
            for (j = 0; j < 3; j++) {
                g3[B + i][j] = g3[i][j];
            }
        }
    }

    /**
     * Returns the minimum and maximum of a number of random values of the given function. This is useful for making
     * some stab at normalising the function.
     *
     * @param   f       DOCUMENT ME!
     * @param   minmax  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static float[] findRange(final Function1D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0;
        float max = 0;
        // Some random numbers here...
        for (float x = -100; x < 100; x += 1.27139) {
            final float n = f.evaluate(x);
            min = Math.min(min, n);
            max = Math.max(max, n);
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }

    /**
     * Returns the minimum and maximum of a number of random values of the given function. This is useful for making
     * some stab at normalising the function.
     *
     * @param   f       DOCUMENT ME!
     * @param   minmax  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static float[] findRange(final Function2D f, float[] minmax) {
        if (minmax == null) {
            minmax = new float[2];
        }
        float min = 0;
        float max = 0;
        // Some random numbers here...
        for (float y = -100; y < 100; y += 10.35173) {
            for (float x = -100; x < 100; x += 10.77139) {
                final float n = f.evaluate(x, y);
                min = Math.min(min, n);
                max = Math.max(max, n);
            }
        }
        minmax[0] = min;
        minmax[1] = max;
        return minmax;
    }
}
