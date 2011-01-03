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
import java.awt.geom.*;
import java.awt.image.*;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ScratchFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private float density = 0.1f;
    private float angle;
    private float angleVariation = 1.0f;
    private float width = 0.5f;
    private float length = 0.5f;
    private int color = 0xffffffff;
    private int seed = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ScratchFilter object.
     */
    public ScratchFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  angleVariation  DOCUMENT ME!
     */
    public void setAngleVariation(final float angleVariation) {
        this.angleVariation = angleVariation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getAngleVariation() {
        return angleVariation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  density  DOCUMENT ME!
     */
    public void setDensity(final float density) {
        this.density = density;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getDensity() {
        return density;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  length  DOCUMENT ME!
     */
    public void setLength(final float length) {
        this.length = length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getLength() {
        return length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  width  DOCUMENT ME!
     */
    public void setWidth(final float width) {
        this.width = width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  color  DOCUMENT ME!
     */
    public void setColor(final int color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  seed  DOCUMENT ME!
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getSeed() {
        return seed;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        final int width = src.getWidth();
        final int height = src.getHeight();
        final int numScratches = (int)(density * width * height / 100);
        final ArrayList lines = new ArrayList();
        {
            final float l = length * width;
            final Random random = new Random(seed);
            final Graphics2D g = dst.createGraphics();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setColor(new Color(color));
            g.setStroke(new BasicStroke(this.width));
            for (int i = 0; i < numScratches; i++) {
                final float x = width * random.nextFloat();
                final float y = height * random.nextFloat();
                final float a = angle + (ImageMath.TWO_PI * (angleVariation * (random.nextFloat() - 0.5f)));
                final float s = (float)Math.sin(a) * l;
                final float c = (float)Math.cos(a) * l;
                final float x1 = x - c;
                final float y1 = y - s;
                final float x2 = x + c;
                final float y2 = y + s;
                g.drawLine((int)x1, (int)y1, (int)x2, (int)y2);
                lines.add(new Line2D.Float(x1, y1, x2, y2));
            }
            g.dispose();
        }

        if (false) {
//              int[] inPixels = getRGB( src, 0, 0, width, height, null );
            final int[] inPixels = new int[width * height];
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final float sx = x;
                    final float sy = y;
                    for (int i = 0; i < numScratches; i++) {
                        final Line2D.Float l = (Line2D.Float)lines.get(i);
                        final float dot = ((l.x2 - l.x1) * (sx - l.x1)) + ((l.y2 - l.y1) * (sy - l.y1));
                        if (dot > 0) {
                            inPixels[index] |= (1 << i);
                        }
                    }
                    index++;
                }
            }

            final Colormap colormap = new LinearColormap();
            index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    final float f = (float)(inPixels[index] & 0x7fffffff) / 0x7fffffff;
                    inPixels[index] = colormap.getColor(f);
                    index++;
                }
            }
            setRGB(dst, 0, 0, width, height, inPixels);
        }
        return dst;
    }

    @Override
    public String toString() {
        return "Render/Scratches..."; // NOI18N
    }
}
