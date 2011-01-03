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

import java.util.*;

/**
 * A filter which simulates underwater caustics. This can be animated to get a bottom-of-the-swimming-pool effect.
 *
 * @version  $Revision$, $Date$
 */
public class CausticsFilter extends WholeImageFilter {

    //~ Instance fields --------------------------------------------------------

    private float scale = 32;
    private float angle = 0.0f;
    private int brightness = 10;
    private float amount = 1.0f;
    private float turbulence = 1.0f;
    private float dispersion = 0.0f;
    private float time = 0.0f;
    private int samples = 2;
    private int bgColor = 0xff799fff;

    private float s;
    private float c;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CausticsFilter object.
     */
    public CausticsFilter() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Specifies the scale of the texture.
     *
     * @param      scale  the scale of the texture.
     *
     * @see        #getScale
     * @min-value  1
     * @max-value  300+
     */
    public void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * Returns the scale of the texture.
     *
     * @return  the scale of the texture.
     *
     * @see     #setScale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Set the brightness.
     *
     * @param      brightness  the brightness.
     *
     * @see        #getBrightness
     * @min-value  0
     * @max-value  1
     */
    public void setBrightness(final int brightness) {
        this.brightness = brightness;
    }

    /**
     * Get the brightness.
     *
     * @return  the brightness.
     *
     * @see     #setBrightness
     */
    public int getBrightness() {
        return brightness;
    }

    /**
     * Specifies the turbulence of the texture.
     *
     * @param      turbulence  the turbulence of the texture.
     *
     * @see        #getTurbulence
     * @min-value  0
     * @max-value  1
     */
    public void setTurbulence(final float turbulence) {
        this.turbulence = turbulence;
    }

    /**
     * Returns the turbulence of the effect.
     *
     * @return  the turbulence of the effect.
     *
     * @see     #setTurbulence
     */
    public float getTurbulence() {
        return turbulence;
    }

    /**
     * Set the amount of effect.
     *
     * @param      amount  the amount
     *
     * @see        #getAmount
     * @min-value  0
     * @max-value  1
     */
    public void setAmount(final float amount) {
        this.amount = amount;
    }

    /**
     * Get the amount of effect.
     *
     * @return  the amount
     *
     * @see     #setAmount
     */
    public float getAmount() {
        return amount;
    }

    /**
     * Set the dispersion.
     *
     * @param      dispersion  the dispersion
     *
     * @see        #getDispersion
     * @min-value  0
     * @max-value  1
     */
    public void setDispersion(final float dispersion) {
        this.dispersion = dispersion;
    }

    /**
     * Get the dispersion.
     *
     * @return  the dispersion
     *
     * @see     #setDispersion
     */
    public float getDispersion() {
        return dispersion;
    }

    /**
     * Set the time. Use this to animate the effect.
     *
     * @param  time  the time
     *
     * @see    #getTime
     */
    public void setTime(final float time) {
        this.time = time;
    }

    /**
     * Set the time.
     *
     * @return  the time
     *
     * @see     #setTime
     */
    public float getTime() {
        return time;
    }

    /**
     * Set the number of samples per pixel. More samples means better quality, but slower rendering.
     *
     * @param  samples  the number of samples
     *
     * @see    #getSamples
     */
    public void setSamples(final int samples) {
        this.samples = samples;
    }

    /**
     * Get the number of samples per pixel.
     *
     * @return  the number of samples
     *
     * @see     #setSamples
     */
    public int getSamples() {
        return samples;
    }

    /**
     * Set the background color.
     *
     * @param  c  the color
     *
     * @see    #getBgColor
     */
    public void setBgColor(final int c) {
        bgColor = c;
    }

    /**
     * Get the background color.
     *
     * @return  the color
     *
     * @see     #setBgColor
     */
    public int getBgColor() {
        return bgColor;
    }

    @Override
    protected int[] filterPixels(final int width,
            final int height,
            final int[] inPixels,
            final Rectangle transformedSpace) {
        final Random random = new Random(0);

        s = (float)Math.sin(0.1);
        c = (float)Math.cos(0.1);

        final int srcWidth = originalSpace.width;
        final int srcHeight = originalSpace.height;
        final int outWidth = transformedSpace.width;
        final int outHeight = transformedSpace.height;
        int index = 0;
        final int[] pixels = new int[outWidth * outHeight];

        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                pixels[index++] = bgColor;
            }
        }

        int v = brightness / samples;
        if (v == 0) {
            v = 1;
        }

        final float rs = 1.0f / scale;
        final float d = 0.95f;
        index = 0;
        for (int y = 0; y < outHeight; y++) {
            for (int x = 0; x < outWidth; x++) {
                for (int s = 0; s < samples; s++) {
                    final float sx = x + random.nextFloat();
                    final float sy = y + random.nextFloat();
                    final float nx = sx * rs;
                    final float ny = sy * rs;
                    final float xDisplacement;
                    final float yDisplacement;
                    final float focus = 0.1f + amount;
                    xDisplacement = evaluate(nx - d, ny) - evaluate(nx + d, ny);
                    yDisplacement = evaluate(nx, ny + d) - evaluate(nx, ny - d);

                    if (dispersion > 0) {
                        for (int c = 0; c < 3; c++) {
                            final float ca = (1 + (c * dispersion));
                            final float srcX = sx + (scale * focus * xDisplacement * ca);
                            final float srcY = sy + (scale * focus * yDisplacement * ca);

                            if ((srcX < 0) || (srcX >= (outWidth - 1)) || (srcY < 0) || (srcY >= (outHeight - 1))) {
                            } else {
                                final int i = (((int)srcY) * outWidth) + (int)srcX;
                                final int rgb = pixels[i];
                                int r = (rgb >> 16) & 0xff;
                                int g = (rgb >> 8) & 0xff;
                                int b = rgb & 0xff;
                                if (c == 2) {
                                    r += v;
                                } else if (c == 1) {
                                    g += v;
                                } else {
                                    b += v;
                                }
                                if (r > 255) {
                                    r = 255;
                                }
                                if (g > 255) {
                                    g = 255;
                                }
                                if (b > 255) {
                                    b = 255;
                                }
                                pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
                            }
                        }
                    } else {
                        final float srcX = sx + (scale * focus * xDisplacement);
                        final float srcY = sy + (scale * focus * yDisplacement);

                        if ((srcX < 0) || (srcX >= (outWidth - 1)) || (srcY < 0) || (srcY >= (outHeight - 1))) {
                        } else {
                            final int i = (((int)srcY) * outWidth) + (int)srcX;
                            final int rgb = pixels[i];
                            int r = (rgb >> 16) & 0xff;
                            int g = (rgb >> 8) & 0xff;
                            int b = rgb & 0xff;
                            r += v;
                            g += v;
                            b += v;
                            if (r > 255) {
                                r = 255;
                            }
                            if (g > 255) {
                                g = 255;
                            }
                            if (b > 255) {
                                b = 255;
                            }
                            pixels[i] = 0xff000000 | (r << 16) | (g << 8) | b;
                        }
                    }
                }
            }
        }
        return pixels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rgb         DOCUMENT ME!
     * @param   brightness  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int add(final int rgb, final float brightness) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        r += brightness;
        g += brightness;
        b += brightness;
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   rgb         DOCUMENT ME!
     * @param   brightness  DOCUMENT ME!
     * @param   c           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int add(final int rgb, final float brightness, final int c) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = rgb & 0xff;
        if (c == 2) {
            r += brightness;
        } else if (c == 1) {
            g += brightness;
        } else {
            b += brightness;
        }
        if (r > 255) {
            r = 255;
        }
        if (g > 255) {
            g = 255;
        }
        if (b > 255) {
            b = 255;
        }
        return 0xff000000 | (r << 16) | (g << 8) | b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x        DOCUMENT ME!
     * @param   y        DOCUMENT ME!
     * @param   time     DOCUMENT ME!
     * @param   octaves  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static float turbulence2(float x, float y, final float time, final float octaves) {
        float value = 0.0f;
        final float remainder;
        final float lacunarity = 2.0f;
        float f = 1.0f;
        int i;

        // to prevent "cascading" effects
        x += 371;
        y += 529;

        for (i = 0; i < (int)octaves; i++) {
            value += Noise.noise3(x, y, time) / f;
            x *= lacunarity;
            y *= lacunarity;
            f *= 2;
        }

        remainder = octaves - (int)octaves;
        if (remainder != 0) {
            value += remainder * Noise.noise3(x, y, time) / f;
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     * @param   y  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private float evaluate(final float x, final float y) {
        final float xt = (s * x) + (c * time);
        final float tt = (c * x) - (c * time);
        final float f = (turbulence == 0.0) ? Noise.noise3(xt, y, tt) : turbulence2(xt, y, tt, turbulence);
        return f;
    }

    @Override
    public String toString() {
        return "Texture/Caustics..."; // NOI18N
    }
}
