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
public class Point3f extends Tuple3f {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Point3f object.
     */
    public Point3f() {
        this(0, 0, 0);
    }

    /**
     * Creates a new Point3f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Point3f(final Point3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    /**
     * Creates a new Point3f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Point3f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
    }

    /**
     * Creates a new Point3f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Point3f(final Tuple3f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
    }

    /**
     * Creates a new Point3f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     */
    public Point3f(final float x, final float y, final float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distanceL1(final Point3f p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y) + Math.abs(z - p.z);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distanceSquared(final Point3f p) {
        final float dx = x - p.x;
        final float dy = y - p.y;
        final float dz = z - p.z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distance(final Point3f p) {
        final float dx = x - p.x;
        final float dy = y - p.y;
        final float dz = z - p.z;
        return (float)Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
    }
}
