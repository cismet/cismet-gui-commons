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
package com.jhlabs.vecmath;

/**
 * Vector math package, converted to look similar to javax.vecmath.
 *
 * @version  $Revision$, $Date$
 */
public class Tuple4f {

    //~ Instance fields --------------------------------------------------------

    public float x;
    public float y;
    public float z;
    public float w;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Tuple4f object.
     */
    public Tuple4f() {
        this(0, 0, 0, 0);
    }

    /**
     * Creates a new Tuple4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Tuple4f(final Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    /**
     * Creates a new Tuple4f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Tuple4f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    /**
     * Creates a new Tuple4f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     * @param  w  DOCUMENT ME!
     */
    public Tuple4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public void absolute() {
        x = Math.abs(x);
        y = Math.abs(y);
        z = Math.abs(z);
        w = Math.abs(w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void absolute(final Tuple4f t) {
        x = Math.abs(t.x);
        y = Math.abs(t.y);
        z = Math.abs(t.z);
        w = Math.abs(t.w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  min  DOCUMENT ME!
     * @param  max  DOCUMENT ME!
     */
    public void clamp(final float min, final float max) {
        if (x < min) {
            x = min;
        } else if (x > max) {
            x = max;
        }
        if (y < min) {
            y = min;
        } else if (y > max) {
            y = max;
        }
        if (z < min) {
            z = min;
        } else if (z > max) {
            z = max;
        }
        if (w < min) {
            w = min;
        } else if (w > max) {
            w = max;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     * @param  w  DOCUMENT ME!
     */
    public void set(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  x  DOCUMENT ME!
     */
    public void set(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void set(final Tuple4f t) {
        x = t.x;
        y = t.y;
        z = t.z;
        w = t.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void get(final Tuple4f t) {
        t.x = x;
        t.y = y;
        t.z = z;
        t.w = w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void get(final float[] t) {
        t[0] = x;
        t[1] = y;
        t[2] = z;
        t[3] = w;
    }

    /**
     * DOCUMENT ME!
     */
    public void negate() {
        x = -x;
        y = -y;
        z = -z;
        w = -w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void negate(final Tuple4f t) {
        x = -t.x;
        y = -t.y;
        z = -t.z;
        w = -t.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t      DOCUMENT ME!
     * @param  alpha  DOCUMENT ME!
     */
    public void interpolate(final Tuple4f t, final float alpha) {
        final float a = 1 - alpha;
        x = (a * x) + (alpha * t.x);
        y = (a * y) + (alpha * t.y);
        z = (a * z) + (alpha * t.z);
        w = (a * w) + (alpha * t.w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  s  DOCUMENT ME!
     */
    public void scale(final float s) {
        x *= s;
        y *= s;
        z *= s;
        w *= s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void add(final Tuple4f t) {
        x += t.x;
        y += t.y;
        z += t.z;
        w += t.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t1  DOCUMENT ME!
     * @param  t2  DOCUMENT ME!
     */
    public void add(final Tuple4f t1, final Tuple4f t2) {
        x = t1.x + t2.x;
        y = t1.y + t2.y;
        z = t1.z + t2.z;
        w = t1.w + t2.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void sub(final Tuple4f t) {
        x -= t.x;
        y -= t.y;
        z -= t.z;
        w -= t.w;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t1  DOCUMENT ME!
     * @param  t2  DOCUMENT ME!
     */
    public void sub(final Tuple4f t1, final Tuple4f t2) {
        x = t1.x - t2.x;
        y = t1.y - t2.y;
        z = t1.z - t2.z;
        w = t1.w - t2.w;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + w + "]"; // NOI18N
    }
}
