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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MapFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private Function2D xMapFunction;
    private Function2D yMapFunction;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MapFilter object.
     */
    public MapFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  xMapFunction  DOCUMENT ME!
     */
    public void setXMapFunction(final Function2D xMapFunction) {
        this.xMapFunction = xMapFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Function2D getXMapFunction() {
        return xMapFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  yMapFunction  DOCUMENT ME!
     */
    public void setYMapFunction(final Function2D yMapFunction) {
        this.yMapFunction = yMapFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Function2D getYMapFunction() {
        return yMapFunction;
    }

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        final float xMap;
        final float yMap;
        xMap = xMapFunction.evaluate(x, y);
        yMap = yMapFunction.evaluate(x, y);
        out[0] = xMap * transformedSpace.width;
        out[1] = yMap * transformedSpace.height;
    }

    @Override
    public String toString() {
        return "Distort/Map Coordinates..."; // NOI18N
    }
}
