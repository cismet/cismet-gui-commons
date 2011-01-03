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

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class MirrorFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private float opacity = 1.0f;
    private float centreY = 0.5f;
    private float distance;
    private float angle;
    private float rotation;
    private float gap;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MirrorFilter object.
     */
    public MirrorFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Specifies the angle of the mirror.
     *
     * @param  angle  the angle of the mirror.
     *
     * @see    #getAngle
     * @angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
    }

    /**
     * Returns the angle of the mirror.
     *
     * @return  the angle of the mirror.
     *
     * @see     #setAngle
     */
    public float getAngle() {
        return angle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  distance  DOCUMENT ME!
     */
    public void setDistance(final float distance) {
        this.distance = distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getDistance() {
        return distance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rotation  DOCUMENT ME!
     */
    public void setRotation(final float rotation) {
        this.rotation = rotation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  gap  DOCUMENT ME!
     */
    public void setGap(final float gap) {
        this.gap = gap;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getGap() {
        return gap;
    }

    /**
     * Set the opacity of the reflection.
     *
     * @param  opacity  the opacity.
     *
     * @see    #getOpacity
     */
    public void setOpacity(final float opacity) {
        this.opacity = opacity;
    }

    /**
     * Get the opacity of the reflection.
     *
     * @return  the opacity.
     *
     * @see     #setOpacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  centreY  DOCUMENT ME!
     */
    public void setCentreY(final float centreY) {
        this.centreY = centreY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getCentreY() {
        return centreY;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        final BufferedImage tsrc = src;
        final Shape clip;
        final int width = src.getWidth();
        final int height = src.getHeight();
        final int h = (int)(centreY * height);
        final int d = (int)(gap * height);

        final Graphics2D g = dst.createGraphics();
        clip = g.getClip();
        g.clipRect(0, 0, width, h);
        g.drawRenderedImage(src, null);
        g.setClip(clip);
        g.clipRect(0, h + d, width, height - h - d);
        g.translate(0, (2 * h) + d);
        g.scale(1, -1);
        g.drawRenderedImage(src, null);
        g.setPaint(new GradientPaint(
                0,
                0,
                new Color(1.0f, 0.0f, 0.0f, 0.0f),
                0,
                h,
                new Color(0.0f, 1.0f, 0.0f, opacity)));
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
        g.fillRect(0, 0, width, h);
        g.setClip(clip);
        g.dispose();

        return dst;
    }

    @Override
    public String toString() {
        return "Effects/Mirror..."; // NOI18N
    }
}
