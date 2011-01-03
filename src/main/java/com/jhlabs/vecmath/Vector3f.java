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
public class Vector3f extends Tuple3f {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Vector3f object.
     */
    public Vector3f() {
        this(0, 0, 0);
    }

    /**
     * Creates a new Vector3f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Vector3f(final Vector3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    /**
     * Creates a new Vector3f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Vector3f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
    }

    /**
     * Creates a new Vector3f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Vector3f(final Tuple3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    /**
     * Creates a new Vector3f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     */
    public Vector3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float angle(final Vector3f v) {
        return (float)Math.acos(dot(v) / (length() * v.length()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float dot(final Vector3f v) {
        return (v.x * x) + (v.y * y) + (v.z * z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  v1  DOCUMENT ME!
     * @param  v2  DOCUMENT ME!
     */
    public void cross(final Vector3f v1, final Vector3f v2) {
        x = (v1.y * v2.z) - (v1.z * v2.y);
        y = (v1.z * v2.x) - (v1.x * v2.z);
        z = (v1.x * v2.y) - (v1.y * v2.x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float length() {
        return (float)Math.sqrt((x * x) + (y * y) + (z * z));
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        final float d = 1.0f / (float)Math.sqrt((x * x) + (y * y) + (z * z));
        x *= d;
        y *= d;
        z *= d;
    }
}
