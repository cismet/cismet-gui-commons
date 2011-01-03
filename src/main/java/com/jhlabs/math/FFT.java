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
package com.jhlabs.math;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class FFT {

    //~ Instance fields --------------------------------------------------------

    // Weighting factors
    protected float[] w1;
    protected float[] w2;
    protected float[] w3;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FFT object.
     *
     * @param  logN  DOCUMENT ME!
     */
    public FFT(final int logN) {
        // Prepare the weighting factors
        w1 = new float[logN];
        w2 = new float[logN];
        w3 = new float[logN];
        int N = 1;
        for (int k = 0; k < logN; k++) {
            N <<= 1;
            final double angle = -2.0 * Math.PI / N;
            w1[k] = (float)Math.sin(0.5 * angle);
            w2[k] = -2.0f * w1[k] * w1[k];
            w3[k] = (float)Math.sin(angle);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  n     DOCUMENT ME!
     * @param  real  DOCUMENT ME!
     * @param  imag  DOCUMENT ME!
     */
    private void scramble(final int n, final float[] real, final float[] imag) {
        int j = 0;

        for (int i = 0; i < n; i++) {
            if (i > j) {
                float t;
                t = real[j];
                real[j] = real[i];
                real[i] = t;
                t = imag[j];
                imag[j] = imag[i];
                imag[i] = t;
            }
            int m = n >> 1;
            while ((j >= m) && (m >= 2)) {
                j -= m;
                m >>= 1;
            }
            j += m;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  n          DOCUMENT ME!
     * @param  logN       DOCUMENT ME!
     * @param  direction  DOCUMENT ME!
     * @param  real       DOCUMENT ME!
     * @param  imag       DOCUMENT ME!
     */
    private void butterflies(final int n, final int logN, final int direction, final float[] real, final float[] imag) {
        int N = 1;

        for (int k = 0; k < logN; k++) {
            float w_re;
            float w_im;
            final float wp_re;
            final float wp_im;
            float temp_re;
            float temp_im;
            float wt;
            final int half_N = N;
            N <<= 1;
            wt = direction * w1[k];
            wp_re = w2[k];
            wp_im = direction * w3[k];
            w_re = 1.0f;
            w_im = 0.0f;
            for (int offset = 0; offset < half_N; offset++) {
                for (int i = offset; i < n; i += N) {
                    final int j = i + half_N;
                    final float re = real[j];
                    final float im = imag[j];
                    temp_re = (w_re * re) - (w_im * im);
                    temp_im = (w_im * re) + (w_re * im);
                    real[j] = real[i] - temp_re;
                    real[i] += temp_re;
                    imag[j] = imag[i] - temp_im;
                    imag[i] += temp_im;
                }
                wt = w_re;
                w_re = (wt * wp_re) - (w_im * wp_im) + w_re;
                w_im = (w_im * wp_re) + (wt * wp_im) + w_im;
            }
        }
        if (direction == -1) {
            final float nr = 1.0f / n;
            for (int i = 0; i < n; i++) {
                real[i] *= nr;
                imag[i] *= nr;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  real     DOCUMENT ME!
     * @param  imag     DOCUMENT ME!
     * @param  logN     DOCUMENT ME!
     * @param  n        DOCUMENT ME!
     * @param  forward  DOCUMENT ME!
     */
    public void transform1D(final float[] real,
            final float[] imag,
            final int logN,
            final int n,
            final boolean forward) {
        scramble(n, real, imag);
        butterflies(n, logN, forward ? 1 : -1, real, imag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  real     DOCUMENT ME!
     * @param  imag     DOCUMENT ME!
     * @param  cols     DOCUMENT ME!
     * @param  rows     DOCUMENT ME!
     * @param  forward  DOCUMENT ME!
     */
    public void transform2D(final float[] real,
            final float[] imag,
            final int cols,
            final int rows,
            final boolean forward) {
        final int log2cols = log2(cols);
        final int log2rows = log2(rows);
        final int n = Math.max(rows, cols);
        final float[] rtemp = new float[n];
        final float[] itemp = new float[n];

        // FFT the rows
        for (int y = 0; y < rows; y++) {
            final int offset = y * cols;
            System.arraycopy(real, offset, rtemp, 0, cols);
            System.arraycopy(imag, offset, itemp, 0, cols);
            transform1D(rtemp, itemp, log2cols, cols, forward);
            System.arraycopy(rtemp, 0, real, offset, cols);
            System.arraycopy(itemp, 0, imag, offset, cols);
        }

        // FFT the columns
        for (int x = 0; x < cols; x++) {
            int index = x;
            for (int y = 0; y < rows; y++) {
                rtemp[y] = real[index];
                itemp[y] = imag[index];
                index += cols;
            }
            transform1D(rtemp, itemp, log2rows, rows, forward);
            index = x;
            for (int y = 0; y < rows; y++) {
                real[index] = rtemp[y];
                imag[index] = itemp[y];
                index += cols;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   n  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int log2(final int n) {
        int m = 1;
        int log2n = 0;

        while (m < n) {
            m *= 2;
            log2n++;
        }
        return (m == n) ? log2n : -1;
    }
}
