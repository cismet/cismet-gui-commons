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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FractalSumFunction extends CompoundFunction2D {

    //~ Instance fields --------------------------------------------------------

    private float octaves = 1.0f;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FractalSumFunction object.
     *
     * @param  basis  DOCUMENT ME!
     */
    public FractalSumFunction(final Function2D basis) {
        super(basis);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public float evaluate(final float x, final float y) {
        float t = 0.0f;

        for (float f = 1.0f; f <= octaves; f *= 2) {
            t += basis.evaluate(f * x, f * y) / f;
        }
        return t;
    }
}
