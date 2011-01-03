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
 * The superclass for some of the filters which work on binary images.
 *
 * @version  $Revision$, $Date$
 */
public abstract class BinaryFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    protected int newColor = 0xff000000;
    protected BinaryFunction blackFunction = new BlackFunction();
    protected int iterations = 1;
    protected Colormap colormap;

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the number of iterations the effect is performed.
     *
     * @param      iterations  the number of iterations
     *
     * @see        #getIterations
     * @min-value  0
     */
    public void setIterations(final int iterations) {
        this.iterations = iterations;
    }

    /**
     * Get the number of iterations the effect is performed.
     *
     * @return  the number of iterations
     *
     * @see     #setIterations
     */
    public int getIterations() {
        return iterations;
    }

    /**
     * Set the colormap to be used for the filter.
     *
     * @param  colormap  the colormap
     *
     * @see    #getColormap
     */
    public void setColormap(final Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * Get the colormap to be used for the filter.
     *
     * @return  the colormap
     *
     * @see     #setColormap
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  newColor  DOCUMENT ME!
     */
    public void setNewColor(final int newColor) {
        this.newColor = newColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getNewColor() {
        return newColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  blackFunction  DOCUMENT ME!
     */
    public void setBlackFunction(final BinaryFunction blackFunction) {
        this.blackFunction = blackFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BinaryFunction getBlackFunction() {
        return blackFunction;
    }
}
