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
public class MathFunction1D implements Function1D {

    //~ Static fields/initializers ---------------------------------------------

    public static final int SIN = 1;
    public static final int COS = 2;
    public static final int TAN = 3;
    public static final int SQRT = 4;
    public static final int ASIN = -1;
    public static final int ACOS = -2;
    public static final int ATAN = -3;
    public static final int SQR = -4;

    //~ Instance fields --------------------------------------------------------

    private int operation;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MathFunction1D object.
     *
     * @param  operation  DOCUMENT ME!
     */
    public MathFunction1D(final int operation) {
        this.operation = operation;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public float evaluate(final float v) {
        switch (operation) {
            case SIN: {
                return (float)Math.sin(v);
            }
            case COS: {
                return (float)Math.cos(v);
            }
            case TAN: {
                return (float)Math.tan(v);
            }
            case SQRT: {
                return (float)Math.sqrt(v);
            }
            case ASIN: {
                return (float)Math.asin(v);
            }
            case ACOS: {
                return (float)Math.acos(v);
            }
            case ATAN: {
                return (float)Math.atan(v);
            }
            case SQR: {
                return v * v;
            }
        }
        return v;
    }
}
