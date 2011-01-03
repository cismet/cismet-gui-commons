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
public class CompositeFunction1D implements Function1D {

    //~ Instance fields --------------------------------------------------------

    private Function1D f1;
    private Function1D f2;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CompositeFunction1D object.
     *
     * @param  f1  DOCUMENT ME!
     * @param  f2  DOCUMENT ME!
     */
    public CompositeFunction1D(final Function1D f1, final Function1D f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public float evaluate(final float v) {
        return f1.evaluate(f2.evaluate(v));
    }
}
