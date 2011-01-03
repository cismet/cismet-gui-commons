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

import java.awt.*;

/**
 * Vector math package, converted to look similar to javax.vecmath.
 *
 * @version  $Revision$, $Date$
 */
public class Color4f extends Tuple4f {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new Color4f object.
     */
    public Color4f() {
        this(0, 0, 0, 0);
    }

    /**
     * Creates a new Color4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Color4f(final Color4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    /**
     * Creates a new Color4f object.
     *
     * @param  x  DOCUMENT ME!
     */
    public Color4f(final float[] x) {
        this.x = x[0];
        this.y = x[1];
        this.z = x[2];
        this.w = x[3];
    }

    /**
     * Creates a new Color4f object.
     *
     * @param  t  DOCUMENT ME!
     */
    public Color4f(final Tuple4f t) {
        this.x = t.x;
        this.y = t.y;
        this.z = t.z;
        this.w = t.w;
    }

    /**
     * Creates a new Color4f object.
     *
     * @param  c  DOCUMENT ME!
     */
    public Color4f(final Color c) {
        set(c);
    }

    /**
     * Creates a new Color4f object.
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     * @param  z  DOCUMENT ME!
     * @param  w  DOCUMENT ME!
     */
    public Color4f(final float x, final float y, final float z, final float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public void set(final Color c) {
        set(c.getRGBComponents(null));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Color get() {
        return new Color(x, y, z, w);
    }
}
