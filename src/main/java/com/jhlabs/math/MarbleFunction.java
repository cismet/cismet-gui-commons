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
public class MarbleFunction extends CompoundFunction2D {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MarbleFunction object.
     */
    public MarbleFunction() {
        super(new TurbulenceFunction(new Noise(), 6));
    }

    /**
     * Creates a new MarbleFunction object.
     *
     * @param  basis  DOCUMENT ME!
     */
    public MarbleFunction(final Function2D basis) {
        super(basis);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public float evaluate(final float x, final float y) {
        return (float)Math.pow(0.5 * (Math.sin(8. * basis.evaluate(x, y)) + 1), 0.77);
    }
}
