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
public abstract class TransferFilter extends PointFilter {

    //~ Instance fields --------------------------------------------------------

    protected int[] rTable;
    protected int[] gTable;
    protected int[] bTable;
    protected boolean initialized = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TransferFilter object.
     */
    public TransferFilter() {
        canFilterIndexColorModel = true;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        final int a = rgb & 0xff000000;
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r = rTable[r];
        g = gTable[g];
        b = bTable[b];
        return a | (r << 16) | (g << 8) | b;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        if (!initialized) {
            initialize();
        }
        return super.filter(src, dst);
    }

    /**
     * DOCUMENT ME!
     */
    protected void initialize() {
        initialized = true;
        rTable = gTable = bTable = makeTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected int[] makeTable() {
        final int[] table = new int[256];
        for (int i = 0; i < 256; i++) {
            table[i] = PixelUtils.clamp((int)(255 * transferFunction(i / 255.0f)));
        }
        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   v  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected float transferFunction(final float v) {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int[] getLUT() {
        if (!initialized) {
            initialize();
        }
        final int[] lut = new int[256];
        for (int i = 0; i < 256; i++) {
            lut[i] = filterRGB(0, 0, (i << 24) | (i << 16) | (i << 8) | i);
        }
        return lut;
    }
}
