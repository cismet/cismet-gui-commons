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

import java.awt.image.*;

import java.util.*;

/**
 * A filter which produces textures from fractal Brownian motion.
 *
 * @version  $Revision$, $Date$
 */
public class FBMFilter extends PointFilter implements Cloneable {

    //~ Static fields/initializers ---------------------------------------------

    public static final int NOISE = 0;
    public static final int RIDGED = 1;
    public static final int VLNOISE = 2;
    public static final int SCNOISE = 3;
    public static final int CELLULAR = 4;

    //~ Instance fields --------------------------------------------------------

    protected Random random = new Random();

    private float scale = 32;
    private float stretch = 1.0f;
    private float angle = 0.0f;
    private float amount = 1.0f;
    private float H = 1.0f;
    private float octaves = 4.0f;
    private float lacunarity = 2.0f;
    private float gain = 0.5f;
    private float bias = 0.5f;
    private int operation;
    private float m00 = 1.0f;
    private float m01 = 0.0f;
    private float m10 = 0.0f;
    private float m11 = 1.0f;
    private float min;
    private float max;
    private Colormap colormap = new Gradient();
    private boolean ridged;
    private FBM fBm;
    private int basisType = NOISE;
    private Function2D basis;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FBMFilter object.
     */
    public FBMFilter() {
        setBasisType(NOISE);
    }

    //~ Methods ----------------------------------------------------------------

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
     * Get the amount of texture.
     *
     * @return  the amount
     *
     * @see     #setAmount
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
     * Specifies the stretch factor of the texture.
     *
     * @param      stretch  the stretch factor of the texture.
     *
     * @see        #getStretch
     * @min-value  1
     * @max-value  50+
     */
    public void setStretch(final float stretch) {
        this.stretch = stretch;
    }

    /**
     * Returns the stretch factor of the texture.
     *
     * @return  the stretch factor of the texture.
     *
     * @see     #setStretch
     */
    public float getStretch() {
        return stretch;
    }

    /**
     * Specifies the angle of the texture.
     *
     * @param  angle  the angle of the texture.
     *
     * @see    #getAngle
     * @angle  DOCUMENT ME!
     */
    public void setAngle(final float angle) {
        this.angle = angle;
        final float cos = (float)Math.cos(this.angle);
        final float sin = (float)Math.sin(this.angle);
        m00 = cos;
        m01 = sin;
        m10 = -sin;
        m11 = cos;
    }

    /**
     * Returns the angle of the texture.
     *
     * @return  the angle of the texture.
     *
     * @see     #setAngle
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
     * Set the colormap to be used for the filter.
     *
     * @param  colormap  the colormap
     *
     * @see    #getColormap
     */
    public void setColormap(final Colormap colormap) {
        this.colormap = colormap;
    }

    /**
     * Get the colormap to be used for the filter.
     *
     * @return  the colormap
     *
     * @see     #setColormap
     */
    public Colormap getColormap() {
        return colormap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  basisType  DOCUMENT ME!
     */
    public void setBasisType(final int basisType) {
        this.basisType = basisType;
        switch (basisType) {
            default:
            case NOISE: {
                basis = new Noise();
                break;
            }
            case RIDGED: {
                basis = new RidgedFBM();
                break;
            }
            case VLNOISE: {
                basis = new VLNoise();
                break;
            }
            case SCNOISE: {
                basis = new SCNoise();
                break;
            }
            case CELLULAR: {
                basis = new CellularFunction2D();
                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getBasisType() {
        return basisType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  basis  DOCUMENT ME!
     */
    public void setBasis(final Function2D basis) {
        this.basis = basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Function2D getBasis() {
        return basis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   H           DOCUMENT ME!
     * @param   lacunarity  DOCUMENT ME!
     * @param   octaves     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected FBM makeFBM(final float H, final float lacunarity, final float octaves) {
        final FBM fbm = new FBM(H, lacunarity, octaves, basis);
        final float[] minmax = Noise.findRange(fbm, null);
        min = minmax[0];
        max = minmax[1];
        return fbm;
    }

    @Override
    public BufferedImage filter(final BufferedImage src, final BufferedImage dst) {
        fBm = makeFBM(H, lacunarity, octaves);
        return super.filter(src, dst);
    }

    @Override
    public int filterRGB(final int x, final int y, final int rgb) {
        float nx = (m00 * x) + (m01 * y);
        float ny = (m10 * x) + (m11 * y);
        nx /= scale;
        ny /= scale * stretch;
        float f = fBm.evaluate(nx, ny);
        // Normalize to 0..1
        f = (f - min) / (max - min);
        f = ImageMath.gain(f, gain);
        f = ImageMath.bias(f, bias);
        f *= amount;
        final int a = rgb & 0xff000000;
        int v;
        if (colormap != null) {
            v = colormap.getColor(f);
        } else {
            v = PixelUtils.clamp((int)(f * 255));
            final int r = v << 16;
            final int g = v << 8;
            final int b = v;
            v = a | r | g | b;
        }
        if (operation != PixelUtils.REPLACE) {
            v = PixelUtils.combinePixels(rgb, v, operation);
        }
        return v;
    }

    @Override
    public String toString() {
        return "Texture/Fractal Brownian Motion...";
    }
}
