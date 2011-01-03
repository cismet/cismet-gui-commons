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

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class PointillizeFilter extends CellularFilter {

    //~ Instance fields --------------------------------------------------------

    private float edgeThickness = 0.4f;
    private boolean fadeEdges = false;
    private int edgeColor = 0xff000000;
    private float fuzziness = 0.1f;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PointillizeFilter object.
     */
    public PointillizeFilter() {
        setScale(16);
        setRandomness(0.0f);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  edgeThickness  DOCUMENT ME!
     */
    public void setEdgeThickness(final float edgeThickness) {
        this.edgeThickness = edgeThickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getEdgeThickness() {
        return edgeThickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fadeEdges  DOCUMENT ME!
     */
    public void setFadeEdges(final boolean fadeEdges) {
        this.fadeEdges = fadeEdges;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getFadeEdges() {
        return fadeEdges;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  edgeColor  DOCUMENT ME!
     */
    public void setEdgeColor(final int edgeColor) {
        this.edgeColor = edgeColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getEdgeColor() {
        return edgeColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fuzziness  DOCUMENT ME!
     */
    public void setFuzziness(final float fuzziness) {
        this.fuzziness = fuzziness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getFuzziness() {
        return fuzziness;
    }

    @Override
    public int getPixel(final int x, final int y, final int[] inPixels, final int width, final int height) {
        float nx = (m00 * x) + (m01 * y);
        float ny = (m10 * x) + (m11 * y);
        nx /= scale;
        ny /= scale * stretch;
        nx += 1000;
        ny += 1000; // Reduce artifacts around 0,0
        float f = evaluate(nx, ny);

        final float f1 = results[0].distance;
        int srcx = ImageMath.clamp((int)((results[0].x - 1000) * scale), 0, width - 1);
        int srcy = ImageMath.clamp((int)((results[0].y - 1000) * scale), 0, height - 1);
        int v = inPixels[(srcy * width) + srcx];

        if (fadeEdges) {
            final float f2 = results[1].distance;
            srcx = ImageMath.clamp((int)((results[1].x - 1000) * scale), 0, width - 1);
            srcy = ImageMath.clamp((int)((results[1].y - 1000) * scale), 0, height - 1);
            final int v2 = inPixels[(srcy * width) + srcx];
            v = ImageMath.mixColors(0.5f * f1 / f2, v, v2);
        } else {
            f = 1 - ImageMath.smoothStep(edgeThickness, edgeThickness + fuzziness, f1);
            v = ImageMath.mixColors(f, edgeColor, v);
        }
        return v;
    }

    @Override
    public String toString() {
        return "Stylize/Pointillize...";
    }
}
