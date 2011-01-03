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
public class Point4f extends Tuple4f {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Point4f object.
     */
    public Point4f() {
        this(0, 0, 0, 0);
    }

    /**
     * Creates a new Point4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Point4f(final Point4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    /**
     * Creates a new Point4f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Point4f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[3];
    }

    /**
     * Creates a new Point4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Point4f(final Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    /**
     * Creates a new Point4f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     * @param  w  DOCUMENT ME!
     */
    public Point4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distanceL1(final Point4f p) {
        return Math.abs(x - p.x) + Math.abs(y - p.y) + Math.abs(z - p.z) + Math.abs(w - p.w);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distanceSquared(final Point4f p) {
        final float dx = x - p.x;
        final float dy = y - p.y;
        final float dz = z - p.z;
        final float dw = w - p.w;
        return (dx * dx) + (dy * dy) + (dz * dz) + (dw * dw);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float distance(final Point4f p) {
        final float dx = x - p.x;
        final float dy = y - p.y;
        final float dz = z - p.z;
        final float dw = w - p.w;
        return (float)Math.sqrt((dx * dx) + (dy * dy) + (dz * dz) + (dw * dw));
    }
}
