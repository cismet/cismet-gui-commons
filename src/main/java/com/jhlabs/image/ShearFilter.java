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

import java.awt.*;
import java.awt.image.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ShearFilter extends TransformFilter {

    //~ Instance fields --------------------------------------------------------

    private float xangle = 0.0f;
    private float yangle = 0.0f;
    private float shx = 0.0f;
    private float shy = 0.0f;
    private float xoffset = 0.0f;
    private float yoffset = 0.0f;
    private boolean resize = true;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ShearFilter object.
     */
    public ShearFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  resize  DOCUMENT ME!
     */
    public void setResize(final boolean resize) {
        this.resize = resize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isResize() {
        return resize;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  xangle  DOCUMENT ME!
     */
    public void setXAngle(final float xangle) {
        this.xangle = xangle;
        initialize();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getXAngle() {
        return xangle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  yangle  DOCUMENT ME!
     */
    public void setYAngle(final float yangle) {
        this.yangle = yangle;
        initialize();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getYAngle() {
        return yangle;
    }

    /**
     * DOCUMENT ME!
     */
    private void initialize() {
        shx = (float)Math.sin(xangle);
        shy = (float)Math.sin(yangle);
    }

    @Override
    protected void transformSpace(final Rectangle r) {
        float tangent = (float)Math.tan(xangle);
        xoffset = -r.height * tangent;
        if (tangent < 0.0) {
            tangent = -tangent;
        }
        r.width = (int)((r.height * tangent) + r.width + 0.999999f);
        tangent = (float)Math.tan(yangle);
        yoffset = -r.width * tangent;
        if (tangent < 0.0) {
            tangent = -tangent;
        }
        r.height = (int)((r.width * tangent) + r.height + 0.999999f);
    }

/*
        public void imageComplete(int status) {
try {
                if (status == IMAGEERROR || status == IMAGEABORTED) {
                        consumer.imageComplete(status);
                        return;
                }

                int width = originalSpace.width;
                int height = originalSpace.height;

                float tangent = Math.tan(angle);
                if (tangent < 0.0)
                        tangent = -tangent;
                int newWidth = (int)(height * tangent + width + 0.999999);
                int[] outPixels = new int[height*newWidth];
                int inIndex = 0;
                int yOffset = 0;
                for (int y = 0; y < height; y++) {
                        float newCol;
                        if (angle >= 0.0)
                                newCol = y * tangent;
                        else
                                newCol = (height-y) * tangent;
                        int iNewCol = (int)newCol;
                        float f = newCol - iNewCol;
                        f = 1.0 - f;

                        int outIndex = yOffset+iNewCol;
                        int lastRGB = inPixels[inIndex];
                        for (int x = 0; x < width; x++) {
                                int rgb = inPixels[inIndex];
                                outPixels[outIndex] = ImageMath.mixColors(f, lastRGB, rgb);
                                lastRGB = rgb;
                                inIndex++;
                                outIndex++;
                        }
                        outPixels[outIndex] = ImageMath.mixColors(f, lastRGB, 0);
                        yOffset += newWidth;
                }
                consumer.setPixels(0, 0, newWidth, height, defaultRGBModel, outPixels, 0, newWidth);
                consumer.imageComplete(status);
                inPixels = null;
}
catch (Exception e) {
        e.printStackTrace();
}
        }
*/

    @Override
    protected void transformInverse(final int x, final int y, final float[] out) {
        out[0] = x + xoffset + (y * shx);
        out[1] = y + yoffset + (x * shy);
    }

    @Override
    public String toString() {
        return "Distort/Shear..."; // NOI18N
    }
}
