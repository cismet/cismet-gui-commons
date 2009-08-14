/*
 * ImageToolTip.java
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
 * Created on 5. September 2005, 13:03
 *
 */
package de.cismet.tools.gui.imagetooltip;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JComponent;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalToolTipUI;

/**
 *
 * @author thorsten.hell@cismet.de
 */
public class ImageToolTip extends JToolTip {

    /** Creates a new instance of ImageToolTip */
    public ImageToolTip(Image image) {
        setUI(new ImageToolTipUI(image));
    }

    public class ImageToolTipUI extends MetalToolTipUI {

        private Image m_image;

        public ImageToolTipUI(Image image) {
            m_image = image;
        }

        /**
         * This method is overriden from the MetalToolTipUI
         * to draw the given image and text
         */
        @Override
        public void paint(Graphics g, JComponent c) {
            FontMetrics metrics = c.getFontMetrics(g.getFont());
            //Dimension size = c.getSize();
            //g.setColor(c.getBackground());
            //g.fillRect(0, 0, size.width, size.height);
            g.setColor(c.getForeground());

            g.drawString(((JToolTip) c).getTipText(), 3, 15);

            g.drawImage(m_image, 3, metrics.getHeight() + 3, c);
        }

        /**
         * This method is overriden from the MetalToolTipUI
         * to return the appropiate preferred size to size the 
         * ToolTip to show both the text and image.
         */
        @Override
        public Dimension getPreferredSize(JComponent c) {
            FontMetrics metrics = c.getFontMetrics(c.getFont());
            String tipText = ((JToolTip) c).getTipText();
            if (tipText == null) {
                tipText = "";
            }

            int width = SwingUtilities.computeStringWidth(metrics, tipText);
            int height = metrics.getHeight() + m_image.getHeight(c) + 6;

            if (width < m_image.getWidth(c)) {
                width = m_image.getWidth(c);
            }

            return new Dimension(width, height);
        }
    }
}
