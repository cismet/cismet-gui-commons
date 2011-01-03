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
 * A filter which produces lighting and embossing effects.
 *
 * @version  $Revision$, $Date$
 */
public class LightFilter extends WholeImageFilter {

    //~ Static fields/initializers ---------------------------------------------

    /** Take the output colors from the input image. */
    public static final int COLORS_FROM_IMAGE = 0;

    /** Use constant material color. */
    public static final int COLORS_CONSTANT = 1;

    /** Use the input image brightness as the bump map. */
    public static final int BUMPS_FROM_IMAGE = 0;

    /** Use the input image alpha as the bump map. */
    public static final int BUMPS_FROM_IMAGE_ALPHA = 1;

    /** Use a separate image alpha channel as the bump map. */
    public static final int BUMPS_FROM_MAP = 2;

    /** Use a custom function as the bump map. */
    public static final int BUMPS_FROM_BEVEL = 3;

    protected static final float r255 = 1.0f / 255.0f;

    public static final int AMBIENT = 0;
    public static final int DISTANT = 1;
    public static final int POINT = 2;
    public static final int SPOT = 3;

    //~ Instance fields --------------------------------------------------------

    Material material;

    private float bumpHeight;
    private float bumpSoftness;
    private int bumpShape;
    private float viewDistance = 10000.0f;
    private Vector lights;
    private int colorSource = COLORS_FROM_IMAGE;
    private int bumpSource = BUMPS_FROM_IMAGE;
    private Function2D bumpFunction;
    private Image environmentMap;
    private int[] envPixels;
    private int envWidth = 1;
    private int envHeight = 1;

    // Temporary variables used to avoid per-pixel memory allocation while filtering
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
     * Creates a new LightFilter object.
     */
    public LightFilter() {
        lights = new Vector();
        addLight(new DistantLight());
        bumpHeight = 1.0f;
        bumpSoftness = 5.0f;
        bumpShape = 0;
        material = new Material();
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
     * @param  material  DOCUMENT ME!
     */
    public void setMaterial(final Material material) {
        this.material = material;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Material getMaterial() {
        return material;
    }

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
     * @param  bumpShape  DOCUMENT ME!
     */
    public void setBumpShape(final int bumpShape) {
        this.bumpShape = bumpShape;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getBumpShape() {
        return bumpShape;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  viewDistance  DOCUMENT ME!
     */
    public void setViewDistance(final float viewDistance) {
        this.viewDistance = viewDistance;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getViewDistance() {
        return viewDistance;
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
    public Image getEnvironmentMap() {
        return environmentMap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  colorSource  DOCUMENT ME!
     */
    public void setColorSource(final int colorSource) {
        this.colorSource = colorSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getColorSource() {
        return colorSource;
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
     * @param  diffuseColor  DOCUMENT ME!
     */
    public void setDiffuseColor(final int diffuseColor) {
        material.diffuseColor = diffuseColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDiffuseColor() {
        return material.diffuseColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  light  DOCUMENT ME!
     */
    public void addLight(final Light light) {
        lights.addElement(light);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  light  DOCUMENT ME!
     */
    public void removeLight(final Light light) {
        lights.removeElement(light);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Vector getLights() {
        return lights;
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
        final Color4f envColor = new Color4f();
        final Color4f diffuseColor = new Color4f(new Color(material.diffuseColor));
        final Color4f specularColor = new Color4f(new Color(material.specularColor));
        Function2D bump = bumpFunction;

        // Apply the bump softness
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
                final int[] tmpPixels = new int[bumpWidth * bumpHeight];
                final int[] softPixels = new int[bumpWidth * bumpHeight];
/*
                                for (int i = 0; i < 3; i++ ) {
                                        BoxBlurFilter.blur( bumpPixels, tmpPixels, bumpWidth, bumpHeight, (int)bumpSoftness );
                                        BoxBlurFilter.blur( tmpPixels, softPixels, bumpHeight, bumpWidth, (int)bumpSoftness );
                                }
*/
                final Kernel kernel = GaussianFilter.makeKernel(bumpSoftness);
                GaussianFilter.convolveAndTranspose(
                    kernel,
                    bumpPixels,
                    tmpPixels,
                    bumpWidth,
                    bumpHeight,
                    true,
                    false,
                    false,
                    GaussianFilter.WRAP_EDGES);
                GaussianFilter.convolveAndTranspose(
                    kernel,
                    tmpPixels,
                    softPixels,
                    bumpHeight,
                    bumpWidth,
                    true,
                    false,
                    false,
                    GaussianFilter.WRAP_EDGES);
                bump = new ImageFunction2D(
                        softPixels,
                        bumpWidth,
                        bumpHeight,
                        ImageFunction2D.CLAMP,
                        bumpSource
                                == BUMPS_FROM_IMAGE_ALPHA);
                final Function2D bbump = bump;
                if (bumpShape != 0) {
                    bump = new Function2D() {

                            private Function2D original = bbump;

                            @Override
                            public float evaluate(final float x, final float y) {
                                float v = original.evaluate(x, y);
                                switch (bumpShape) {
                                    case 1: {
//                              v = v > 0.5f ? 0.5f : v;
                                        v *= ImageMath.smoothStep(0.45f, 0.55f, v);
                                        break;
                                    }
                                    case 2: {
                                        v = (v < 0.5f) ? 0.5f : v;
                                        break;
                                    }
                                    case 3: {
                                        v = ImageMath.triangle(v);
                                        break;
                                    }
                                    case 4: {
                                        v = ImageMath.circleDown(v);
                                        break;
                                    }
                                    case 5: {
                                        v = ImageMath.gain(v, 0.75f);
                                        break;
                                    }
                                }
                                return v;
                            }
                        };
                }
            } else if (bumpSource != BUMPS_FROM_MAP) {
                bump = new ImageFunction2D(
                        inPixels,
                        width,
                        height,
                        ImageFunction2D.CLAMP,
                        bumpSource
                                == BUMPS_FROM_IMAGE_ALPHA);
            }
        }

        final float reflectivity = material.reflectivity;
        final float areflectivity = (1 - reflectivity);
        final Vector3f v1 = new Vector3f();
        final Vector3f v2 = new Vector3f();
        final Vector3f n = new Vector3f();
        final Light[] lightsArray = new Light[lights.size()];
        lights.copyInto(lightsArray);
        for (int i = 0; i < lightsArray.length; i++) {
            lightsArray[i].prepare(width, height);
        }

        final float[][] heightWindow = new float[3][width];
        for (int x = 0; x < width; x++) {
            heightWindow[1][x] = width45 * bump.evaluate(x, 0);
        }

        // Loop through each source pixel
        for (int y = 0; y < height; y++) {
            final boolean y0 = y > 0;
            final boolean y1 = y < (height - 1);
            position.y = y;
            for (int x = 0; x < width; x++) {
                heightWindow[2][x] = width45 * bump.evaluate(x, y + 1);
            }
            for (int x = 0; x < width; x++) {
                final boolean x0 = x > 0;
                final boolean x1 = x < (width - 1);

                // Calculate the normal at this point
                if (bumpSource != BUMPS_FROM_BEVEL) {
                    // Complicated and slower method
                    // Calculate four normals using the gradients in +/- X/Y directions
                    int count = 0;
                    normal.x = normal.y = normal.z = 0;
                    final float m0 = heightWindow[1][x];
                    final float m1 = x0 ? (heightWindow[1][x - 1] - m0) : 0;
                    final float m2 = y0 ? (heightWindow[0][x] - m0) : 0;
                    final float m3 = x1 ? (heightWindow[1][x + 1] - m0) : 0;
                    final float m4 = y1 ? (heightWindow[2][x] - m0) : 0;

                    if (x0 && y1) {
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

                    if (x0 && y0) {
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

                    if (y0 && x1) {
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

                    if (x1 && y1) {
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
                if (invertBumps) {
                    normal.x = -normal.x;
                    normal.y = -normal.y;
                }
                position.x = x;

                if (normal.z >= 0) {
                    // Get the material colour at this point
                    if (colorSource == COLORS_FROM_IMAGE) {
                        setFromRGB(diffuseColor, inPixels[index]);
                    } else {
                        setFromRGB(diffuseColor, material.diffuseColor);
                    }
                    if ((reflectivity != 0) && (environmentMap != null)) {
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
                        setFromRGB(envColor, getEnvironmentMap(tmpv, inPixels, width, height)); // FIXME-interpolate()
                        diffuseColor.x = (reflectivity * envColor.x) + (areflectivity * diffuseColor.x);
                        diffuseColor.y = (reflectivity * envColor.y) + (areflectivity * diffuseColor.y);
                        diffuseColor.z = (reflectivity * envColor.z) + (areflectivity * diffuseColor.z);
                    }
                    // Shade the pixel
                    final Color4f c = phongShade(
                            position,
                            viewpoint,
                            normal,
                            diffuseColor,
                            specularColor,
                            material,
                            lightsArray);
                    final int alpha = inPixels[index] & 0xff000000;
                    final int rgb = ((int)(c.x * 255) << 16) | ((int)(c.y * 255) << 8) | (int)(c.z * 255);
                    outPixels[index++] = alpha | rgb;
                } else {
                    outPixels[index++] = 0;
                }
            }
            final float[] t = heightWindow[0];
            heightWindow[0] = heightWindow[1];
            heightWindow[1] = heightWindow[2];
            heightWindow[2] = t;
        }
        return outPixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   position       DOCUMENT ME!
     * @param   viewpoint      DOCUMENT ME!
     * @param   normal         DOCUMENT ME!
     * @param   diffuseColor   DOCUMENT ME!
     * @param   specularColor  DOCUMENT ME!
     * @param   material       DOCUMENT ME!
     * @param   lightsArray    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Color4f phongShade(final Vector3f position,
            final Vector3f viewpoint,
            final Vector3f normal,
            final Color4f diffuseColor,
            final Color4f specularColor,
            final Material material,
            final Light[] lightsArray) {
        shadedColor.set(diffuseColor);
        shadedColor.scale(material.ambientIntensity);

        for (int i = 0; i < lightsArray.length; i++) {
            final Light light = lightsArray[i];
            n.set(normal);
            l.set(light.position);
            if (light.type != DISTANT) {
                l.sub(position);
            }
            l.normalize();
            float nDotL = n.dot(l);
            if (nDotL >= 0.0) {
                float dDotL = 0;

                v.set(viewpoint);
                v.sub(position);
                v.normalize();

                // Spotlight
                if (light.type == SPOT) {
                    dDotL = light.direction.dot(l);
                    if (dDotL < light.cosConeAngle) {
                        continue;
                    }
                }

                n.scale(2.0f * nDotL);
                n.sub(l);
                final float rDotV = n.dot(v);

                float rv;
                if (rDotV < 0.0) {
                    rv = 0.0f;
                } else {
//                                      rv = (float)Math.pow(rDotV, material.highlight);
                    rv = rDotV / (material.highlight - (material.highlight * rDotV) + rDotV); // Fast approximation
                                                                                              // to pow
                }

                // Spotlight
                if (light.type == SPOT) {
                    dDotL = light.cosConeAngle / dDotL;
                    float e = dDotL;
                    e *= e;
                    e *= e;
                    e *= e;
                    e = (float)Math.pow(dDotL, light.focus * 10) * (1 - e);
                    rv *= e;
                    nDotL *= e;
                }

                diffuse_color.set(diffuseColor);
                diffuse_color.scale(material.diffuseReflectivity);
                diffuse_color.x *= light.realColor.x * nDotL;
                diffuse_color.y *= light.realColor.y * nDotL;
                diffuse_color.z *= light.realColor.z * nDotL;
                specular_color.set(specularColor);
                specular_color.scale(material.specularReflectivity);
                specular_color.x *= light.realColor.x * rv;
                specular_color.y *= light.realColor.y * rv;
                specular_color.z *= light.realColor.z * rv;
                diffuse_color.add(specular_color);
                diffuse_color.clamp(0, 1);
                shadedColor.add(diffuse_color);
            }
        }
        shadedColor.clamp(0, 1);
        return shadedColor;
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
    private int getEnvironmentMap(final Vector3f normal, final int[] inPixels, final int width, final int height) {
        if (environmentMap != null) {
            final float angle = (float)Math.acos(-normal.y);

            float x;
            float y;
            y = angle / ImageMath.PI;

            if ((y == 0.0f) || (y == 1.0f)) {
                x = 0.0f;
            } else {
                float f = normal.x / (float)Math.sin(angle);

                if (f > 1.0f) {
                    f = 1.0f;
                } else if (f < -1.0f) {
                    f = -1.0f;
                }

                x = (float)Math.acos(f) / ImageMath.PI;
            }
            // A bit of empirical scaling....
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
        return "Stylize/Light Effects...";
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * A class representing material properties.
     *
     * @version  $Revision$, $Date$
     */
    public static class Material {

        //~ Instance fields ----------------------------------------------------

        int diffuseColor;
        int specularColor;
        float ambientIntensity;
        float diffuseReflectivity;
        float specularReflectivity;
        float highlight;
        float reflectivity;
        float opacity = 1;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Material object.
         */
        public Material() {
            ambientIntensity = 0.5f;
            diffuseReflectivity = 1.0f;
            specularReflectivity = 1.0f;
            highlight = 3.0f;
            reflectivity = 0.0f;
            diffuseColor = 0xff888888;
            specularColor = 0xffffffff;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  diffuseColor  DOCUMENT ME!
         */
        public void setDiffuseColor(final int diffuseColor) {
            this.diffuseColor = diffuseColor;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getDiffuseColor() {
            return diffuseColor;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  opacity  DOCUMENT ME!
         */
        public void setOpacity(final float opacity) {
            this.opacity = opacity;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getOpacity() {
            return opacity;
        }
    }

    /**
     * A class representing a light.
     *
     * @version  $Revision$, $Date$
     */
    public static class Light implements Cloneable {

        //~ Instance fields ----------------------------------------------------

        int type = AMBIENT;
        Vector3f position;
        Vector3f direction;
        Color4f realColor = new Color4f();
        int color = 0xffffffff;
        float intensity;
        float azimuth;
        float elevation;
        float focus = 0.5f;
        float centreX = 0.5f;
        float centreY = 0.5f;
        float coneAngle = ImageMath.PI / 6;
        float cosConeAngle;
        float distance = 100.0f;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Light object.
         */
        public Light() {
            this(270 * ImageMath.PI / 180.0f, 0.5235987755982988f, 1.0f);
        }

        /**
         * Creates a new Light object.
         *
         * @param  azimuth    DOCUMENT ME!
         * @param  elevation  DOCUMENT ME!
         * @param  intensity  DOCUMENT ME!
         */
        public Light(final float azimuth, final float elevation, final float intensity) {
            this.azimuth = azimuth;
            this.elevation = elevation;
            this.intensity = intensity;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  azimuth  DOCUMENT ME!
         */
        public void setAzimuth(final float azimuth) {
            this.azimuth = azimuth;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getAzimuth() {
            return azimuth;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  elevation  DOCUMENT ME!
         */
        public void setElevation(final float elevation) {
            this.elevation = elevation;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getElevation() {
            return elevation;
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
         * @param  intensity  DOCUMENT ME!
         */
        public void setIntensity(final float intensity) {
            this.intensity = intensity;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getIntensity() {
            return intensity;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  coneAngle  DOCUMENT ME!
         */
        public void setConeAngle(final float coneAngle) {
            this.coneAngle = coneAngle;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getConeAngle() {
            return coneAngle;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  focus  DOCUMENT ME!
         */
        public void setFocus(final float focus) {
            this.focus = focus;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public float getFocus() {
            return focus;
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
         * Set the centre of the light in the X direction as a proportion of the image size.
         *
         * @param  x  centreX the center
         *
         * @see    #getCentreX
         */
        public void setCentreX(final float x) {
            centreX = x;
        }

        /**
         * Get the centre of the light in the X direction as a proportion of the image size.
         *
         * @return  the center
         *
         * @see     #setCentreX
         */
        public float getCentreX() {
            return centreX;
        }

        /**
         * Set the centre of the light in the Y direction as a proportion of the image size.
         *
         * @param  y  centreY the center
         *
         * @see    #getCentreY
         */
        public void setCentreY(final float y) {
            centreY = y;
        }

        /**
         * Get the centre of the light in the Y direction as a proportion of the image size.
         *
         * @return  the center
         *
         * @see     #setCentreY
         */
        public float getCentreY() {
            return centreY;
        }

        /**
         * Prepare the light for rendering.
         *
         * @param  width   the output image width
         * @param  height  the output image height
         */
        public void prepare(final int width, final int height) {
            float lx = (float)(Math.cos(azimuth) * Math.cos(elevation));
            float ly = (float)(Math.sin(azimuth) * Math.cos(elevation));
            float lz = (float)Math.sin(elevation);
            direction = new Vector3f(lx, ly, lz);
            direction.normalize();
            if (type != DISTANT) {
                lx *= distance;
                ly *= distance;
                lz *= distance;
                lx += width * centreX;
                ly += height * centreY;
            }
            position = new Vector3f(lx, ly, lz);
            realColor.set(new Color(color));
            realColor.scale(intensity);
            cosConeAngle = (float)Math.cos(coneAngle);
        }

        @Override
        public Object clone() {
            try {
                final Light copy = (Light)super.clone();
                return copy;
            } catch (CloneNotSupportedException e) {
                return null;
            }
        }

        @Override
        public String toString() {
            return "Light"; // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class AmbientLight extends Light {

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return "Ambient Light"; // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class PointLight extends Light {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new PointLight object.
         */
        public PointLight() {
            type = POINT;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return "Point Light"; // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class DistantLight extends Light {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DistantLight object.
         */
        public DistantLight() {
            type = DISTANT;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return "Distant Light"; // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class SpotLight extends Light {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SpotLight object.
         */
        public SpotLight() {
            type = SPOT;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return "Spotlight"; // NOI18N
        }
    }
}
