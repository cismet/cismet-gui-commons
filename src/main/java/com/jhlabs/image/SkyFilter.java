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
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class SkyFilter extends PointFilter {

    //~ Static fields/initializers ---------------------------------------------

    private static final float r255 = 1.0f / 255.0f;

    //~ Instance fields --------------------------------------------------------

    protected Random random = new Random();

    float mn;
    float mx;

    private float scale = 0.1f;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float amount = 1.0f;
    private float H = 1.0f;
    private float octaves = 8.0f;
    private float lacunarity = 2.0f;
    private float gain = 1.0f;
    private float bias = 0.6f;
    private int operation;
    private float min;
    private float max;
    private boolean ridged;
    private FBM fBm;
    private Function2D basis;

    private float cloudCover = 0.5f;
    private float cloudSharpness = 0.5f;
    private float time = 0.3f;
    private float glow = 0.5f;
    private float glowFalloff = 0.5f;
    private float haziness = 0.96f;
    private float t = 0.0f;
    private float sunRadius = 10f;
    private int sunColor = 0xffffffff;
    private float sunR;
    private float sunG;
    private float sunB;
    private float sunAzimuth = 0.5f;
    private float sunElevation = 0.5f;
    private float windSpeed = 0.0f;

    private float cameraAzimuth = 0.0f;
    private float cameraElevation = 0.0f;
    private float fov = 1.0f;

    private float[] exponents;
    private float[] tan;
    private BufferedImage skyColors;
    private int[] skyPixels;

    private float width;
    private float height;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SkyFilter object.
     */
    public SkyFilter() {
        if (skyColors == null) {
            skyColors = ImageUtils.createImage(Toolkit.getDefaultToolkit().getImage(
                        getClass().getResource("SkyColors.png")).getSource()); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  amount  DOCUMENT ME!
     */
    public void setAmount(final float amount) {
        this.amount = amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getAmount() {
        return amount;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  operation  DOCUMENT ME!
     */
    public void setOperation(final int operation) {
        this.operation = operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getOperation() {
        return operation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  scale  DOCUMENT ME!
     */
    public void setScale(final float scale) {
        this.scale = scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getScale() {
        return scale;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  stretch  DOCUMENT ME!
     */
    public void setStretch(final float stretch) {
        this.stretch = stretch;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getStretch() {
        return stretch;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void setT(final float t) {
        this.t = t;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getT() {
        return t;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  fov  DOCUMENT ME!
     */
    public void setFOV(final float fov) {
        this.fov = fov;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getFOV() {
        return fov;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cloudCover  DOCUMENT ME!
     */
    public void setCloudCover(final float cloudCover) {
        this.cloudCover = cloudCover;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getCloudCover() {
        return cloudCover;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cloudSharpness  DOCUMENT ME!
     */
    public void setCloudSharpness(final float cloudSharpness) {
        this.cloudSharpness = cloudSharpness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getCloudSharpness() {
        return cloudSharpness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  time  DOCUMENT ME!
     */
    public void setTime(final float time) {
        this.time = time;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getTime() {
        return time;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  glow  DOCUMENT ME!
     */
    public void setGlow(final float glow) {
        this.glow = glow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getGlow() {
        return glow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  glowFalloff  DOCUMENT ME!
     */
    public void setGlowFalloff(final float glowFalloff) {
        this.glowFalloff = glowFalloff;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getGlowFalloff() {
        return glowFalloff;
    }

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
     * @param  octaves  DOCUMENT ME!
     */
    public void setOctaves(final float octaves) {
        this.octaves = octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getOctaves() {
        return octaves;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  H  DOCUMENT ME!
     */
    public void setH(final float H) {
        this.H = H;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getH() {
        return H;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  lacunarity  DOCUMENT ME!
     */
    public void setLacunarity(final float lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getLacunarity() {
        return lacunarity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  gain  DOCUMENT ME!
     */
    public void setGain(final float gain) {
        this.gain = gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getGain() {
        return gain;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bias  DOCUMENT ME!
     */
    public void setBias(final float bias) {
        this.bias = bias;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getBias() {
        return bias;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  haziness  DOCUMENT ME!
     */
    public void setHaziness(final float haziness) {
        this.haziness = haziness;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getHaziness() {
        return haziness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sunElevation  DOCUMENT ME!
     */
    public void setSunElevation(final float sunElevation) {
        this.sunElevation = sunElevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getSunElevation() {
        return sunElevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sunAzimuth  DOCUMENT ME!
     */
    public void setSunAzimuth(final float sunAzimuth) {
        this.sunAzimuth = sunAzimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getSunAzimuth() {
        return sunAzimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sunColor  DOCUMENT ME!
     */
    public void setSunColor(final int sunColor) {
        this.sunColor = sunColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getSunColor() {
        return sunColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cameraElevation  DOCUMENT ME!
     */
    public void setCameraElevation(final float cameraElevation) {
        this.cameraElevation = cameraElevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getCameraElevation() {
        return cameraElevation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cameraAzimuth  DOCUMENT ME!
     */
    public void setCameraAzimuth(final float cameraAzimuth) {
        this.cameraAzimuth = cameraAzimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getCameraAzimuth() {
        return cameraAzimuth;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  windSpeed  DOCUMENT ME!
     */
    public void setWindSpeed(final float windSpeed) {
        this.windSpeed = windSpeed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getWindSpeed() {
        return windSpeed;
    }
    @Override
    public BufferedImage filter(final BufferedImage src, BufferedImage dst) {
        final long start = System.currentTimeMillis();
        sunR = (float)((sunColor >> 16) & 0xff) * r255;
        sunG = (float)((sunColor >> 8) & 0xff) * r255;
        sunB = (float)(sunColor & 0xff) * r255;

        mn = 10000;
        mx = -10000;
        exponents = new float[(int)octaves + 1];
        float frequency = 1.0f;
        for (int i = 0; i <= (int)octaves; i++) {
            exponents[i] = (float)Math.pow(2, -i);
            frequency *= lacunarity;
        }

        min = -1;
        max = 1;

//min = -1.2f;
//max = 1.2f;

        width = src.getWidth();
        height = src.getHeight();

        final int h = src.getHeight();
        tan = new float[h];
        for (int i = 0; i < h; i++) {
            tan[i] = (float)Math.tan(fov * (float)i / h * Math.PI * 0.5);
        }

        if (dst == null) {
            dst = createCompatibleDestImage(src, null);
        }
        final int t = (int)(63 * time);
//              skyPixels = getRGB( skyColors, t, 0, 1, 64, skyPixels );
        final Graphics2D g = dst.createGraphics();
        g.drawImage(skyColors, 0, 0, dst.getWidth(), dst.getHeight(), t, 0, t + 1, 64, null);
        g.dispose();
        final BufferedImage clouds = super.filter(dst, dst);
//              g.drawRenderedImage( clouds, null );
//              g.dispose();
        final long finish = System.currentTimeMillis();
        System.out.println(mn + " " + mx + " " + ((finish - start) * 0.001f));
        exponents = null;
        tan = null;
        return dst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     * @param   y  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float evaluate(float x, float y) {
        float value = 0.0f;
        final float remainder;
        int i;

        // to prevent "cascading" effects
        x += 371;
        y += 529;

        for (i = 0; i < (int)octaves; i++) {
            value += Noise.noise3(x, y, t) * exponents[i];
            x *= lacunarity;
            y *= lacunarity;
        }

        remainder = octaves - (int)octaves;
        if (remainder != 0) {
            value += remainder * Noise.noise3(x, y, t) * exponents[i];
        }

        return value;
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
// Curvature
        final float fx = (float)x / width;
//y += 20*Math.sin( fx*Math.PI*0.5 );
        final float fy = y / height;
        final float haze = (float)Math.pow(haziness, 100 * fy * fy);
//              int argb = skyPixels[(int)fy];
        float r = (float)((rgb >> 16) & 0xff) * r255;
        float g = (float)((rgb >> 8) & 0xff) * r255;
        float b = (float)(rgb & 0xff) * r255;

        final float cx = width * 0.5f;
        float nx = x - cx;
        float ny = y;
// FOV
//ny = (float)Math.tan( fov * fy * Math.PI * 0.5 );
        ny = tan[y];
        nx = (fx - 0.5f) * (1 + ny);
        ny += t * windSpeed; // Wind towards the camera

//              float xscale = scale/(1+y*bias*0.1f);
        nx /= scale;
        ny /= scale * stretch;
        float f = evaluate(nx, ny);
        final float fg = f; // FIXME-bump map
        // Normalize to 0..1
// f = (f-min)/(max-min);

        f = (f + 1.23f) / 2.46f;

//              f *= amount;
        final int a = rgb & 0xff000000;
        final int v;

        // Work out cloud cover
        float c = f - cloudCover;
        if (c < 0) {
            c = 0;
        }

        float cloudAlpha = 1 - (float)Math.pow(cloudSharpness, c);
//cloudAlpha *= amount;
//if ( cloudAlpha > 1 )
//      cloudAlpha = 1;
        mn = Math.min(mn, cloudAlpha);
        mx = Math.max(mx, cloudAlpha);

        // Sun glow
        final float centreX = width * sunAzimuth;
        final float centreY = height * sunElevation;
        final float dx = x - centreX;
        final float dy = y - centreY;
        float distance2 = (dx * dx) + (dy * dy);
//              float sun = 0;
        // distance2 = (float)Math.sqrt(distance2);
        distance2 = (float)Math.pow(distance2, glowFalloff);
        final float sun = /*amount**/ 10 * (float)Math.exp(-distance2 * glow * 0.1f);
//              sun = glow*10*(float)Math.exp(-distance2);

        // Sun glow onto sky
        r += sun * sunR;
        g += sun * sunG;
        b += sun * sunB;

//              float cloudColor = cloudAlpha *sun;
// Bump map
/*
                float nnx = x-cx;
                float nny = y-1;
                nnx /= xscale;
                nny /= xscale * stretch;
                float gf = evaluate(nnx, nny);
                float gradient = fg-gf;
if (y == 100)System.out.println(fg+" "+gf+gradient);
                cloudColor += amount * gradient;
*/
// ...
/*
                r += (cloudColor-r) * cloudAlpha;
                g += (cloudColor-g) * cloudAlpha;
                b += (cloudColor-b) * cloudAlpha;
*/
        // Clouds get darker as they get thicker
        final float ca = (1 - (cloudAlpha * cloudAlpha * cloudAlpha * cloudAlpha)) /** (1 + sun)*/ * amount;
        final float cloudR = sunR * ca;
        final float cloudG = sunG * ca;
        final float cloudB = sunB * ca;

        // Apply the haziness as we move further away
        cloudAlpha *= haze;

        // Composite the clouds on the sky
        final float iCloudAlpha = (1 - cloudAlpha);
        r = (iCloudAlpha * r) + (cloudAlpha * cloudR);
        g = (iCloudAlpha * g) + (cloudAlpha * cloudG);
        b = (iCloudAlpha * b) + (cloudAlpha * cloudB);

        // Exposure
        final float exposure = gain;
        r = 1 - (float)Math.exp(-r * exposure);
        g = 1 - (float)Math.exp(-g * exposure);
        b = 1 - (float)Math.exp(-b * exposure);

        final int ir = (int)(255 * r) << 16;
        final int ig = (int)(255 * g) << 8;
        final int ib = (int)(255 * b);
        v = 0xff000000 | ir | ig | ib;
        return v;
    }

    @Override
    public String toString() {
        return "Texture/Sky..."; // NOI18N
    }
}
