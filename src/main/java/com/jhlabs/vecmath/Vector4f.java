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
public class Vector4f extends Tuple4f {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Vector4f object.
     */
    public Vector4f() {
        this(0, 0, 0, 0);
    }

    /**
     * Creates a new Vector4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Vector4f(final Vector4f t) {
        x = t.x;
        y = t.y;
        z = t.z;
        w = t.w;
    }

    /**
     * Creates a new Vector4f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Vector4f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[2];
    }

    /**
     * Creates a new Vector4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Vector4f(final Tuple4f t) {
        x = t.x;
        y = t.y;
        z = t.z;
        w = t.w;
    }

    /**
     * Creates a new Vector4f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     * @param  w  DOCUMENT ME!
     */
    public Vector4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float dot(final Vector4f v) {
        return (v.x * x) + (v.y * y) + (v.z * z) + (v.w * w);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float length() {
        return (float)Math.sqrt((x * x) + (y * y) + (z * z) + (w * w));
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        final float d = 1.0f / ((x * x) + (y * y) + (z * z) + (w * w));
        x *= d;
        y *= d;
        z *= d;
        w *= d;
    }
}
