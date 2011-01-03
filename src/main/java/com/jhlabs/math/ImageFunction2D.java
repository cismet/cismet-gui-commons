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

import com.jhlabs.image.*;

import java.awt.*;
import java.awt.image.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class ImageFunction2D implements Function2D {

    //~ Static fields/initializers ---------------------------------------------

    public static final int ZERO = 0;
    public static final int CLAMP = 1;
    public static final int WRAP = 2;

    //~ Instance fields --------------------------------------------------------

    protected int[] pixels;
    protected int width;
    protected int height;
    protected int edgeAction = ZERO;
    protected boolean alpha = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param  image  DOCUMENT ME!
     */
    public ImageFunction2D(final BufferedImage image) {
        this(image, false);
    }

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param  image  DOCUMENT ME!
     */
    public ImageFunction2D(final Image image) {
        this(image, ZERO, false);
    }

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param  image  DOCUMENT ME!
     * @param  alpha  DOCUMENT ME!
     */
    public ImageFunction2D(final BufferedImage image, final boolean alpha) {
        this(image, ZERO, alpha);
    }

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param  image       DOCUMENT ME!
     * @param  edgeAction  DOCUMENT ME!
     * @param  alpha       DOCUMENT ME!
     */
    public ImageFunction2D(final BufferedImage image, final int edgeAction, final boolean alpha) {
        init(getRGB(image, 0, 0, image.getWidth(), image.getHeight(), null),
            image.getWidth(),
            image.getHeight(),
            edgeAction,
            alpha);
    }

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param   image       DOCUMENT ME!
     * @param   edgeAction  DOCUMENT ME!
     * @param   alpha       DOCUMENT ME!
     *
     * @throws  RuntimeException  DOCUMENT ME!
     */
    public ImageFunction2D(final Image image, final int edgeAction, final boolean alpha) {
        final PixelGrabber pg = new PixelGrabber(image, 0, 0, -1, -1, null, 0, -1);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException("interrupted waiting for pixels!");
        }
        if ((pg.status() & ImageObserver.ABORT) != 0) {
            throw new RuntimeException("image fetch aborted");
        }
        init((int[])pg.getPixels(), pg.getWidth(), pg.getHeight(), edgeAction, alpha);
    }

    /**
     * Creates a new ImageFunction2D object.
     *
     * @param  pixels      DOCUMENT ME!
     * @param  width       DOCUMENT ME!
     * @param  height      DOCUMENT ME!
     * @param  edgeAction  DOCUMENT ME!
     * @param  alpha       DOCUMENT ME!
     */
    public ImageFunction2D(final int[] pixels,
            final int width,
            final int height,
            final int edgeAction,
            final boolean alpha) {
        init(pixels, width, height, edgeAction, alpha);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * A convenience method for getting ARGB pixels from an image. This tries to avoid the performance penalty of
     * BufferedImage.getRGB unmanaging the image.
     *
     * @param   image   DOCUMENT ME!
     * @param   x       DOCUMENT ME!
     * @param   y       DOCUMENT ME!
     * @param   width   DOCUMENT ME!
     * @param   height  DOCUMENT ME!
     * @param   pixels  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int[] getRGB(final BufferedImage image,
            final int x,
            final int y,
            final int width,
            final int height,
            final int[] pixels) {
        final int type = image.getType();
        if ((type == BufferedImage.TYPE_INT_ARGB) || (type == BufferedImage.TYPE_INT_RGB)) {
            return (int[])image.getRaster().getDataElements(x, y, width, height, pixels);
        }
        return image.getRGB(x, y, width, height, pixels, 0, width);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pixels      DOCUMENT ME!
     * @param  width       DOCUMENT ME!
     * @param  height      DOCUMENT ME!
     * @param  edgeAction  DOCUMENT ME!
     * @param  alpha       DOCUMENT ME!
     */
    public void init(final int[] pixels, final int width, final int height, final int edgeAction, final boolean alpha) {
        this.pixels = pixels;
        this.width = width;
        this.height = height;
        this.edgeAction = edgeAction;
        this.alpha = alpha;
    }

    @Override
    public float evaluate(final float x, final float y) {
        int ix = (int)x;
        int iy = (int)y;
        if (edgeAction == WRAP) {
            ix = ImageMath.mod(ix, width);
            iy = ImageMath.mod(iy, height);
        } else if ((ix < 0) || (iy < 0) || (ix >= width) || (iy >= height)) {
            if (edgeAction == ZERO) {
                return 0;
            }
            if (ix < 0) {
                ix = 0;
            } else if (ix >= width) {
                ix = width - 1;
            }
            if (iy < 0) {
                iy = 0;
            } else if (iy >= height) {
                iy = height - 1;
            }
        }
        return alpha ? (((pixels[(iy * width) + ix] >> 24) & 0xff) / 255.0f)
                     : (PixelUtils.brightness(pixels[(iy * width) + ix]) / 255.0f);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  edgeAction  DOCUMENT ME!
     */
    public void setEdgeAction(final int edgeAction) {
        this.edgeAction = edgeAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getEdgeAction() {
        return edgeAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int[] getPixels() {
        return pixels;
    }
}
