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
public class AxisAngle4f {

    //~ Instance fields --------------------------------------------------------

    public float x;
    public float y;
    public float z;
    public float angle;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AxisAngle4f object.
     */
    public AxisAngle4f() {
        this(0, 0, 0, 0);
    }

    /**
     * Creates a new AxisAngle4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public AxisAngle4f(final AxisAngle4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.angle = t.angle;
    }

    /**
     * Creates a new AxisAngle4f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public AxisAngle4f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.angle = x[2];
    }

    /**
     * Creates a new AxisAngle4f object.
     *
     * @param  v      DOCUMENT ME!
     * @param  angle  DOCUMENT ME!
     */
    public AxisAngle4f(final Vector3f v, final float angle) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.angle = angle;
    }

    /**
     * Creates a new AxisAngle4f object.
     *
     * @param  x      DOCUMENT ME!
     * @param  y      DOCUMENT ME!
     * @param  z      DOCUMENT ME!
     * @param  angle  DOCUMENT ME!
     */
    public AxisAngle4f(final float x, final float y, final float z, final float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  x      DOCUMENT ME!
     * @param  y      DOCUMENT ME!
     * @param  z      DOCUMENT ME!
     * @param  angle  DOCUMENT ME!
     */
    public void set(final float x, final float y, final float z, final float angle) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.angle = angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void set(final AxisAngle4f t) {
        x = t.x;
        y = t.y;
        z = t.z;
        angle = t.angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void get(final AxisAngle4f t) {
        t.x = x;
        t.y = y;
        t.z = z;
        t.angle = angle;
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
        t[3] = angle;
    }

    @Override
    public String toString() {
        return "[" + x + ", " + y + ", " + z + ", " + angle + "]"; // NOI18N
    }
}
