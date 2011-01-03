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

import com.jhlabs.composite.*;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ShineFilter extends AbstractBufferedImageOp {

    //~ Instance fields --------------------------------------------------------

    private float radius = 5;
    private float angle = (float)Math.PI * 7 / 4;
    private float distance = 5;
    private float bevel = 0.5f;
    private boolean shadowOnly = false;
    private int shineColor = 0xffffffff;
    private float brightness = 0.2f;
    private float softness = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ShineFilter object.
     */
    public ShineFilter() {
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
     * Set the radius of the kernel, and hence the amount of blur. The bigger the radius, the longer this filter will
     * take.
     *
     * @param  radius  the radius of the blur in pixels.
     */
    public void setRadius(final float radius) {
        this.radius = radius;
    }

    /**
     * Get the radius of the kernel.
     *
     * @return  the radius
     */
    public float getRadius() {
        return radius;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bevel  DOCUMENT ME!
     */
    public void setBevel(final float bevel) {
        this.bevel = bevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBevel() {
        return bevel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  shineColor  DOCUMENT ME!
     */
    public void setShineColor(final int shineColor) {
        this.shineColor = shineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getShineColor() {
        return shineColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  shadowOnly  DOCUMENT ME!
     */
    public void setShadowOnly(final boolean shadowOnly) {
        this.shadowOnly = shadowOnly;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getShadowOnly() {
        return shadowOnly;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  brightness  DOCUMENT ME!
     */
    public void setBrightness(final float brightness) {
        this.brightness = brightness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBrightness() {
        return brightness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  softness  DOCUMENT ME!
     */
    public void setSoftness(final float softness) {
        this.softness = softness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getSoftness() {
        return softness;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final int width = src.getWidth();
        final int height = src.getHeight();

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }

        final float xOffset = distance * (float)Math.cos(angle);
        final float yOffset = -distance * (float)Math.sin(angle);

        BufferedImage matte = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        final ErodeAlphaFilter s = new ErodeAlphaFilter(bevel * 10, 0.75f, 0.1f);
        matte = s.filter(src, null);

        BufferedImage shineLayer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = shineLayer.createGraphics();
        g.setColor(new Color(shineColor));
        g.fillRect(0, 0, width, height);
        g.setComposite(AlphaComposite.DstIn);
        g.drawRenderedImage(matte, null);
        g.setComposite(AlphaComposite.DstOut);
        g.translate(xOffset, yOffset);
        g.drawRenderedImage(matte, null);
        g.dispose();
        shineLayer = new GaussianFilter(radius).filter(shineLayer, null);
        shineLayer = new RescaleFilter(3 * brightness).filter(shineLayer, shineLayer);

        g = dst.createGraphics();
        g.drawRenderedImage(src, null);
        g.setComposite(new AddComposite(1.0f));
        g.drawRenderedImage(shineLayer, null);
        g.dispose();

        return dst;
    }

    @Override
    public String toString() {
        return "Stylize/Shine..."; // NOI18N
    }
}
