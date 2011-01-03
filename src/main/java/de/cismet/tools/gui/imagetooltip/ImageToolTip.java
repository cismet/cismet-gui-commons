/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class ImageToolTip extends JToolTip {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of ImageToolTip.
     *
     * @param  image  DOCUMENT ME!
     */
    public ImageToolTip(final Image image) {
        setUI(new ImageToolTipUI(image));
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class ImageToolTipUI extends MetalToolTipUI {

        //~ Instance fields ----------------------------------------------------

        private Image m_image;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ImageToolTipUI object.
         *
         * @param  image  DOCUMENT ME!
         */
        public ImageToolTipUI(final Image image) {
            m_image = image;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * This method is overriden from the MetalToolTipUI to draw the given image and text.
         *
         * @param  g  DOCUMENT ME!
         * @param  c  DOCUMENT ME!
         */
        @Override
        public void paint(final Graphics g, final JComponent c) {
            final FontMetrics metrics = c.getFontMetrics(g.getFont());
            // Dimension size = c.getSize();
            // g.setColor(c.getBackground());
            // g.fillRect(0, 0, size.width, size.height);
            g.setColor(c.getForeground());

            g.drawString(((JToolTip)c).getTipText(), 3, 15);

            g.drawImage(m_image, 3, metrics.getHeight() + 3, c);
        }

        /**
         * This method is overriden from the MetalToolTipUI to return the appropiate preferred size to size the ToolTip
         * to show both the text and image.
         *
         * @param   c  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public Dimension getPreferredSize(final JComponent c) {
            final FontMetrics metrics = c.getFontMetrics(c.getFont());
            String tipText = ((JToolTip)c).getTipText();
            if (tipText == null) {
                tipText = "";
            }

            int width = SwingUtilities.computeStringWidth(metrics, tipText);
            final int height = metrics.getHeight() + m_image.getHeight(c) + 6;

            if (width < m_image.getWidth(c)) {
                width = m_image.getWidth(c);
            }

            return new Dimension(width, height);
        }
    }
}
