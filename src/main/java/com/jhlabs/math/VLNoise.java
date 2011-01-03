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
public class VLNoise implements Function2D {

    //~ Instance fields --------------------------------------------------------

    private float distortion = 10.0f;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  distortion  DOCUMENT ME!
     */
    public void setDistortion(final float distortion) {
        this.distortion = distortion;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public float getDistortion() {
        return distortion;
    }

    @Override
    public float evaluate(final float x, final float y) {
        final float ox = Noise.noise2(x + 0.5f, y) * distortion;
        final float oy = Noise.noise2(x, y + 0.5f) * distortion;
        return Noise.noise2(x + ox, y + oy);
    }
}
