/*
 * Static2DTools.java
 * Copyright (C) 2005 by:
 *
 *----------------------------
 * cismet GmbH
 * Goebenstrasse 40
 * 66117 Saarbruecken
 * http://www.cismet.de
 *----------------------------
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *----------------------------
 * Author:
 * thorsten.hell@cismet.de
 *----------------------------
 *
 * Created on 17. Februar 2006, 11:40
 *
 */
package de.cismet.tools.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author thorsten.hell@cismet.de
 */
public class Static2DTools {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(Static2DTools.class);
    public static int HORIZONTAL = 0;
    public static int VERTICAL = 1;
    public static int LEFT = 2;
    public static int RIGHT = 4;
    public static int TOP = 8;
    public static int BOTTOM = 16;
    public static int CENTER = 32;

    public static Icon joinIcons(Icon[] icons, int gap, int orientation, int alignment) {
        int maxWidth = 0;
        int addedWidth = 0;
        int maxHeight = 0;
        int addedHeight = 0;
        BufferedImage image = null;
        if (icons.length == 0) {
            throw new IllegalArgumentException("Icon[] with length=0 is not allowed.");
        }
        if (icons.length == 1) {
            return icons[0];
        }

        for (int i = 0; i < icons.length; i++) {
            if (maxWidth < icons[i].getIconWidth() || i == 0) {
                maxWidth = icons[i].getIconWidth();
            }
            if (maxHeight < icons[i].getIconHeight() | i == 0) {
                maxHeight = icons[i].getIconHeight();
            }
            addedWidth += icons[i].getIconWidth();
            addedHeight += icons[i].getIconHeight();
        }
        int iWidth, iHeight;
        if (orientation == HORIZONTAL) {
            iWidth = gap * (icons.length - 1) + addedWidth;
            iHeight = maxHeight;
        } else if (orientation == VERTICAL) {
            iWidth = maxWidth;
            iHeight = gap * (icons.length - 1) + addedHeight;
        } else {
            throw new IllegalArgumentException("OrientationParameter must be either Static2DTools.HORIZONTAL or Static2DTools.VERTICAL");
        }
        log.debug("JOIN(" + iWidth + "," + iHeight);

        image = new BufferedImage(iWidth, iHeight + 1, BufferedImage.TYPE_INT_ARGB);



        int runningPosition = 0;
        int alignmentPosition = 0;
        for (int i = 0; i < icons.length; i++) {
            if (orientation == HORIZONTAL) {
                if (alignment == BOTTOM) {
                    alignmentPosition = iHeight - icons[i].getIconHeight();
                } else if (alignment == TOP) {
                    alignmentPosition = 0;
                } else if (alignment == CENTER) {
                    alignmentPosition = (iHeight - icons[i].getIconHeight()) / 2;
                } else {
                    throw new IllegalArgumentException("If orientation is HORIZONTAL the aligment options must bei in {Static2DTools.TOP;Static2DTools.CENTER;Static2DTools.BOTTOM}");
                }
                Graphics g = image.getGraphics();
                icons[i].paintIcon(null, g, runningPosition, alignmentPosition);
                runningPosition += icons[i].getIconWidth() + gap;

            } else {
                //VERTICAL
                if (alignment == LEFT) {
                    alignmentPosition = 0;
                } else if (alignment == RIGHT) {
                    alignmentPosition = iWidth - icons[i].getIconWidth();
                    ;
                } else if (alignment == CENTER) {
                    alignmentPosition = (iWidth - icons[i].getIconWidth()) / 2;
                } else {
                    throw new IllegalArgumentException("If orientation is VERTICAL the aligment options must bei in {Static2DTools.LEFT;Static2DTools.CENTER;Static2DTools.RIGHT}");
                }
                Graphics g = image.getGraphics();
                icons[i].paintIcon(null, g, alignmentPosition, runningPosition);
                runningPosition += icons[i].getIconHeight() + gap;
            }
        }

        return new ImageIcon(image);
    }

    public static ImageIcon createOverlayIcon(ImageIcon overlayIcon, int width, int heigth) {
//        final BufferedImage resultImage = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
        final Image scaledOverlayImage = overlayIcon.getImage().getScaledInstance((int) (width / 1.5), (int) (heigth / 1.5), Image.SCALE_SMOOTH);
//        final BufferedImage result = new BufferedImage(width, heigth, BufferedImage.TYPE_INT_ARGB);
//        result.getGraphics().drawImage(scaledOverlayImage, 0, 0, null);
//        result.getGraphics().drawImage(scaledOverlayImage, width - scaledOverlayImage.getWidth(null), heigth - scaledOverlayImage.getHeight(null), null);
//            final Image scaledOverlayImage = scaleImage(overlayIcon.getImage(), 0.5);
//        final Graphics2D g2d = (Graphics2D) resultImage.getGraphics();
//        g2d.drawImage(scaledOverlayImage, 0, 0, null);
        return new ImageIcon(scaledOverlayImage);
    }

    public static Icon mergeIcons(Icon base, Icon overlay) {
        int maxWidth = Math.max(base.getIconWidth(), overlay.getIconWidth());
        int maxHeight = Math.max(base.getIconHeight(), overlay.getIconHeight());
        final BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        final int midX = maxWidth / 2;
        final int midY = maxHeight / 2;
        final Graphics2D graphics2D = image.createGraphics();
        base.paintIcon(null, graphics2D, midX - (base.getIconWidth() / 2), midY - (base.getIconHeight() / 2));
        overlay.paintIcon(null, graphics2D, maxWidth - overlay.getIconWidth(), maxHeight - overlay.getIconHeight());
//        overlay.paintIcon(null, graphics2D, midX - (overlay.getIconWidth() / 2), midY - (overlay.getIconHeight() / 2));
        return new ImageIcon(image);
    }

//    public static Icon mergeIcons(Icon... icons) {
//        if (icons.length == 0) {
//            throw new IllegalArgumentException("Icon[] with length=0 is not allowed.");
//        }
//        if (icons.length == 1) {
//            return icons[0];
//        }
//        int maxWidth = 0;
//        int maxHeight = 0;
//        for (final Icon currentIcon : icons) {
//            if (currentIcon != null) {
//                maxWidth = Math.max(maxWidth, currentIcon.getIconWidth());
//                maxHeight = Math.max(maxHeight, currentIcon.getIconHeight());
//            }
//        }
//        final int midX = maxWidth / 2;
//        final int midY = maxHeight / 2;
//        final BufferedImage image = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
//        final Graphics2D graphics2D = image.createGraphics();
//        for (final Icon currentIcon : icons) {
//            if (currentIcon != null) {
//                currentIcon.paintIcon(null, graphics2D, midX - (currentIcon.getIconWidth() / 2), midY - (currentIcon.getIconHeight() / 2));
//            }
//        }
//
//        return new ImageIcon(image);
//    }
    public static ImageIcon turnIcon(ImageIcon turnIcon, boolean clockwise) {
        BufferedImage b = new BufferedImage(turnIcon.getIconWidth(), turnIcon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = (Graphics2D) b.getGraphics();

        //g2.drawImage(turnIcon.getImage(),0,0,null);
        return new ImageIcon(tilt(b, Math.PI));
    }

    private static BufferedImage tilt(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int) Math.floor(w * cos + h * sin), newh = (int) Math.floor(h * cos + w * sin);
        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(angle, w / 2, h / 2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }

    private static GraphicsConfiguration getDefaultConfiguration() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        return gd.getDefaultConfiguration();
    }

    public static Icon borderIcon(Icon icon, int left, int right, int top, int bottom) {

        BufferedImage bi = new BufferedImage(icon.getIconWidth() + left + right, icon.getIconHeight() + top + bottom, BufferedImage.TYPE_INT_ARGB);
        icon.paintIcon(null, bi.getGraphics(), left, top);
        return new ImageIcon(bi);
    }

    public static Image removeUnusedBorder(Image i, int borderPixelsAfterwards, double scalingFactor) {
        log.debug("removeUnusedBorder");//NOI18N

        int width = (int) (i.getWidth(null) * scalingFactor);
        int height = (int) (i.getHeight(null) * scalingFactor);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) bi.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.drawImage(i, 0, 0, width, height, null);
        g.drawLine(10, 0, 20, 0);

        int maxX = 0;
        int maxY = 0;
        int minX = width;
        int minY = height;
        int white = -1;
        int alpha = 0;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                int val = bi.getRGB(x, y);

                if (val != white && val != alpha) {
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (x < minX) {
                        minX = x;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                }
            }
        }
        int border = 5;
        try {
            if (minX - border < 0) {
                minX = 0;
            } else {
                minX -= border;
            }
            if (minY - border < 0) {
                minY = 0;
            } else {
                minY -= border;
            }
            if (maxX + border > width) {
                maxX = width;
            } else {
                maxX += border;
            }
            if (maxY + border > height) {
                maxY = height;
            } else {
                maxY += border;
            }
            bi = bi.getSubimage(minX, minY, maxX, maxY);
        } catch (Exception e) {
            log.error("fehler in getSubimage. ver\u00E4ndere nichts am Bild", e);
        }
        return bi;
    }

    public static Image scaleImage(Image i, double scalingFactor) {
        int newWidth = (int) (i.getWidth(null) * scalingFactor);
        int newHeight = (int) (i.getHeight(null) * scalingFactor);
        Image ii = i.getScaledInstance(newWidth, newHeight, i.SCALE_SMOOTH);
        return ii;
    }

    public static BufferedImage toCompatibleImage(Image image) {
        GraphicsEnvironment e =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice d = e.getDefaultScreenDevice();
        GraphicsConfiguration c = d.getDefaultConfiguration();

        BufferedImage compatibleImage = c.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), Transparency.BITMASK);

        Graphics g = compatibleImage.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return compatibleImage;
    }

    public static Color getAlphaColor(Color c, int alpha) {
        return getOffsetAlphaColor(c, new Color(0, 0, 0), alpha);
    }

    public static Color getOffsetAlphaColor(Color c, int offset, int alpha) {
        Color o = new Color(offset, offset, offset);
        return getOffsetAlphaColor(c, o, alpha);
    }

    public static Color getOffsetAlphaColor(Color c, Color offset, int alpha) {
        return new Color(addRGB(c.getRed(), offset.getRed()), addRGB(c.getGreen(), offset.getGreen()), addRGB(c.getBlue(), offset.getBlue()), alpha);
    }

    private static int addRGB(int value, int offset) {
        int ret = 0;
        if (value + offset > 255) {
            ret = 255;
        } else if (value + offset < 0) {
            ret = 0;
        } else {
            ret = value + offset;
        }
        return ret;
    }

    /**
     * Convenience method that returns a scaled instance of the
     * provided BufferedImage.
     * 
     * 
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    RenderingHints.KEY_INTERPOLATION (e.g.
     *    RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR,
     *    RenderingHints.VALUE_INTERPOLATION_BILINEAR,
     *    RenderingHints.VALUE_INTERPOLATION_BICUBIC)
     * @param progressiveBilinear if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in down-scaling cases, where
     *    targetWidth or targetHeight is
     *    smaller than the original dimensions)
     * @return a scaled version of the original BufferedImage
     */
    public static BufferedImage getFasterScaledInstance(BufferedImage img,
            int targetWidth, int targetHeight, Object hint,
            boolean progressiveBilinear) {
        int type = (img.getTransparency() == Transparency.OPAQUE) ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = (BufferedImage) img;
        BufferedImage scratchImage = null;
        Graphics2D g2 = null;
        int w, h;
        int prevW = ret.getWidth();
        int prevH = ret.getHeight();
        if (progressiveBilinear) {
// Use multistep technique: start with original size,
// then scale down in multiple passes with drawImage()
// until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
        } else {
// Use one-step technique: scale directly from original
// size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }
        //BUGFIX to avoid endless-loop when image would be
        if (progressiveBilinear && w < targetWidth && h < targetHeight) {
            return img;
        }

        do {
            if (progressiveBilinear) {
                if (w > targetWidth) {
                    w /= 2;
                    if (w < targetWidth) {
                        w = targetWidth;
                    }
                }
                if (h > targetHeight) {
                    h /= 2;
                    if (h < targetHeight) {
                        h = targetHeight;
                    }
                }
            }
            if (scratchImage == null) {
// Use a single scratch buffer for all iterations
// and then copy to the final, correctly sized image
// before returning
                scratchImage = new BufferedImage(w, h, type);
                g2 = scratchImage.createGraphics();
            }
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    hint);
            g2.drawImage(ret, 0, 0, w, h, 0, 0, prevW, prevH, null);
            prevW = w;
            prevH = h;
            ret = scratchImage;
        } while ((w != targetWidth || h != targetHeight));
        if (g2 != null) {
            g2.dispose();
        }
// If we used a scratch buffer that is larger than our
// target size, create an image of the right size and copy
// the results into it
        if (targetWidth != ret.getWidth() ||
                targetHeight != ret.getHeight()) {
            scratchImage = new BufferedImage(targetWidth,
                    targetHeight, type);
            g2 = scratchImage.createGraphics();
            g2.drawImage(ret, 0, 0, null);
            g2.dispose();
            ret = scratchImage;
        }
        return ret;
    }
}
