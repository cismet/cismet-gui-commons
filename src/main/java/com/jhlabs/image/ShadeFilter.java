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
import com.jhlabs.vecmath.*;

import java.awt.*;
import java.awt.image.*;

import java.io.*;

import java.util.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ShadeFilter extends WholeImageFilter {

    //~ Static fields/initializers ---------------------------------------------

    public static final int COLORS_FROM_IMAGE = 0;
    public static final int COLORS_CONSTANT = 1;

    public static final int BUMPS_FROM_IMAGE = 0;
    public static final int BUMPS_FROM_IMAGE_ALPHA = 1;
    public static final int BUMPS_FROM_MAP = 2;
    public static final int BUMPS_FROM_BEVEL = 3;

    protected static final float r255 = 1.0f / 255.0f;

    //~ Instance fields --------------------------------------------------------

    private float bumpHeight;
    private float bumpSoftness;
    private float viewDistance = 10000.0f;
    private int colorSource = COLORS_FROM_IMAGE;
    private int bumpSource = BUMPS_FROM_IMAGE;
    private Function2D bumpFunction;
    private BufferedImage environmentMap;
    private int[] envPixels;
    private int envWidth = 1;
    private int envHeight = 1;
    private Vector3f l;
    private Vector3f v;
    private Vector3f n;
    private Color4f shadedColor;
    private Color4f diffuse_color;
    private Color4f specular_color;
    private Vector3f tmpv;
    private Vector3f tmpv2;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ShadeFilter object.
     */
    public ShadeFilter() {
        bumpHeight = 1.0f;
        bumpSoftness = 5.0f;
        l = new Vector3f();
        v = new Vector3f();
        n = new Vector3f();
        shadedColor = new Color4f();
        diffuse_color = new Color4f();
        specular_color = new Color4f();
        tmpv = new Vector3f();
        tmpv2 = new Vector3f();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  bumpFunction  DOCUMENT ME!
     */
    public void setBumpFunction(final Function2D bumpFunction) {
        this.bumpFunction = bumpFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Function2D getBumpFunction() {
        return bumpFunction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bumpHeight  DOCUMENT ME!
     */
    public void setBumpHeight(final float bumpHeight) {
        this.bumpHeight = bumpHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBumpHeight() {
        return bumpHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bumpSoftness  DOCUMENT ME!
     */
    public void setBumpSoftness(final float bumpSoftness) {
        this.bumpSoftness = bumpSoftness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBumpSoftness() {
        return bumpSoftness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  environmentMap  DOCUMENT ME!
     */
    public void setEnvironmentMap(final BufferedImage environmentMap) {
        this.environmentMap = environmentMap;
        if (environmentMap != null) {
            envWidth = environmentMap.getWidth();
            envHeight = environmentMap.getHeight();
            envPixels = getRGB(environmentMap, 0, 0, envWidth, envHeight, null);
        } else {
            envWidth = envHeight = 1;
            envPixels = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BufferedImage getEnvironmentMap() {
        return environmentMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bumpSource  DOCUMENT ME!
     */
    public void setBumpSource(final int bumpSource) {
        this.bumpSource = bumpSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getBumpSource() {
        return bumpSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  argb  DOCUMENT ME!
     */
    protected void setFromRGB(final Color4f c, final int argb) {
        c.set(((argb >> 16) & 0xff) * r255,
            ((argb >> 8) & 0xff)
                    * r255,
            (argb & 0xff)
                    * r255,
            ((argb >> 24) & 0xff)
                    * r255);
    }

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        int index = 0;
        final int[] outPixels = new int[width * height];
        final float width45 = Math.abs(6.0f * bumpHeight);
        final boolean invertBumps = bumpHeight < 0;
        final Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
        final Vector3f viewpoint = new Vector3f((float)width / 2.0f, (float)height / 2.0f, viewDistance);
        final Vector3f normal = new Vector3f();
        final Color4f c = new Color4f();
        Function2D bump = bumpFunction;

        if ((bumpSource == BUMPS_FROM_IMAGE) || (bumpSource == BUMPS_FROM_IMAGE_ALPHA) || (bumpSource == BUMPS_FROM_MAP)
                    || (bump == null)) {
            if (bumpSoftness != 0) {
                int bumpWidth = width;
                int bumpHeight = height;
                int[] bumpPixels = inPixels;
                if ((bumpSource == BUMPS_FROM_MAP) && (bumpFunction instanceof ImageFunction2D)) {
                    final ImageFunction2D if2d = (ImageFunction2D)bumpFunction;
                    bumpWidth = if2d.getWidth();
                    bumpHeight = if2d.getHeight();
                    bumpPixels = if2d.getPixels();
                }
                final Kernel kernel = GaussianFilter.makeKernel(bumpSoftness);
                final int[] tmpPixels = new int[bumpWidth * bumpHeight];
                final int[] softPixels = new int[bumpWidth * bumpHeight];
                GaussianFilter.convolveAndTranspose(
                    kernel,
                    bumpPixels,
                    tmpPixels,
                    bumpWidth,
                    bumpHeight,
                    true,
                    false,
                    false,
                    ConvolveFilter.CLAMP_EDGES);
                GaussianFilter.convolveAndTranspose(
                    kernel,
                    tmpPixels,
                    softPixels,
                    bumpHeight,
                    bumpWidth,
                    true,
                    false,
                    false,
                    ConvolveFilter.CLAMP_EDGES);
                bump = new ImageFunction2D(
                        softPixels,
                        bumpWidth,
                        bumpHeight,
                        ImageFunction2D.CLAMP,
                        bumpSource
                                == BUMPS_FROM_IMAGE_ALPHA);
            } else {
                bump = new ImageFunction2D(
                        inPixels,
                        width,
                        height,
                        ImageFunction2D.CLAMP,
                        bumpSource
                                == BUMPS_FROM_IMAGE_ALPHA);
            }
        }

        final Vector3f v1 = new Vector3f();
        final Vector3f v2 = new Vector3f();
        final Vector3f n = new Vector3f();

        // Loop through each source pixel
        for (int y = 0; y < height; y++) {
            final float ny = y;
            position.y = y;
            for (int x = 0; x < width; x++) {
                final float nx = x;

                // Calculate the normal at this point
                if (bumpSource != BUMPS_FROM_BEVEL) {
                    // Complicated and slower method
                    // Calculate four normals using the gradients in +/- X/Y directions
                    int count = 0;
                    normal.x = normal.y = normal.z = 0;
                    final float m0 = width45 * bump.evaluate(nx, ny);
                    final float m1 = (x > 0) ? ((width45 * bump.evaluate(nx - 1.0f, ny)) - m0) : -2;
                    final float m2 = (y > 0) ? ((width45 * bump.evaluate(nx, ny - 1.0f)) - m0) : -2;
                    final float m3 = (x < (width - 1)) ? ((width45 * bump.evaluate(nx + 1.0f, ny)) - m0) : -2;
                    final float m4 = (y < (height - 1)) ? ((width45 * bump.evaluate(nx, ny + 1.0f)) - m0) : -2;

                    if ((m1 != -2) && (m4 != -2)) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        count++;
                    }

                    if ((m1 != -2) && (m2 != -2)) {
                        v1.x = -1.0f;
                        v1.y = 0.0f;
                        v1.z = m1;
                        v2.x = 0.0f;
                        v2.y = -1.0f;
                        v2.z = m2;
                        n.cross(v1, v2);
                        n.normalize();
                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        count++;
                    }

                    if ((m2 != -2) && (m3 != -2)) {
                        v1.x = 0.0f;
                        v1.y = -1.0f;
                        v1.z = m2;
                        v2.x = 1.0f;
                        v2.y = 0.0f;
                        v2.z = m3;
                        n.cross(v1, v2);
                        n.normalize();
                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        count++;
                    }

                    if ((m3 != -2) && (m4 != -2)) {
                        v1.x = 1.0f;
                        v1.y = 0.0f;
                        v1.z = m3;
                        v2.x = 0.0f;
                        v2.y = 1.0f;
                        v2.z = m4;
                        n.cross(v1, v2);
                        n.normalize();
                        if (n.z < 0.0) {
                            n.z = -n.z;
                        }
                        normal.add(n);
                        count++;
                    }

                    // Average the four normals
                    normal.x /= count;
                    normal.y /= count;
                    normal.z /= count;
                }

/* For testing - generate a sphere bump map
                                double dx = x-120;
                                double dy = y-80;
                                double r2 = dx*dx+dy*dy;
//                              double r = Math.sqrt( r2 );
//                              double t = Math.atan2( dy, dx );
                                if ( r2 < 80*80 ) {
                                        double z = Math.sqrt( 80*80 - r2 );
                                        normal.x = (float)dx;
                                        normal.y = (float)dy;
                                        normal.z = (float)z;
                                        normal.normalize();
                                } else {
                                        normal.x = 0;
                                        normal.y = 0;
                                        normal.z = 1;
                                }
*/

                if (invertBumps) {
                    normal.x = -normal.x;
                    normal.y = -normal.y;
                }
                position.x = x;

                if (normal.z >= 0) {
                    // Get the material colour at this point
                    if (environmentMap != null) {
                        // FIXME-too much normalizing going on here
                        tmpv2.set(viewpoint);
                        tmpv2.sub(position);
                        tmpv2.normalize();
                        tmpv.set(normal);
                        tmpv.normalize();

                        // Reflect
                        tmpv.scale(2.0f * tmpv.dot(tmpv2));
                        tmpv.sub(v);

                        tmpv.normalize();
                        setFromRGB(c, getEnvironmentMapP(normal, inPixels, width, height)); // FIXME-interpolate()
                        final int alpha = inPixels[index] & 0xff000000;
                        final int rgb = ((int)(c.x * 255) << 16) | ((int)(c.y * 255) << 8) | (int)(c.z * 255);
                        outPixels[index++] = alpha | rgb;
                    } else {
                        outPixels[index++] = 0;
                    }
                } else {
                    outPixels[index++] = 0;
                }
            }
        }
        return outPixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   normal    DOCUMENT ME!
     * @param   inPixels  DOCUMENT ME!
     * @param   width     DOCUMENT ME!
     * @param   height    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getEnvironmentMapP(final Vector3f normal, final int[] inPixels, final int width, final int height) {
        if (environmentMap != null) {
            float x = 0.5f * (1 + normal.x);
            float y = 0.5f * (1 + normal.y);
            x = ImageMath.clamp(x * envWidth, 0, envWidth - 1);
            y = ImageMath.clamp(y * envHeight, 0, envHeight - 1);
            final int ix = (int)x;
            final int iy = (int)y;

            final float xWeight = x - ix;
            final float yWeight = y - iy;
            final int i = (envWidth * iy) + ix;
            final int dx = (ix == (envWidth - 1)) ? 0 : 1;
            final int dy = (iy == (envHeight - 1)) ? 0 : envWidth;
            return ImageMath.bilinearInterpolate(
                    xWeight,
                    yWeight,
                    envPixels[i],
                    envPixels[i + dx],
                    envPixels[i + dy],
                    envPixels[i + dx + dy]);
        }
        return 0;
    }

    @Override
    public String toString() {
        return "Stylize/Shade..."; // NOI18N
    }
}
