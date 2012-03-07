/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Color;

import javax.swing.JRadioButtonMenuItem;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class HighlightingRadioButtonMenuItem extends JRadioButtonMenuItem {

    //~ Static fields/initializers ---------------------------------------------

    private static JRadioButtonMenuItem bsp = new JRadioButtonMenuItem("white");

    //~ Instance fields --------------------------------------------------------

    private Color selectedBackgroundColor;
    private Color selectedForegroundColor;
    private Color normalBackgroundColor;
    private Color normalForegroundColor;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new HighlightingCheckBoxMenuItem object.
     *
     * @param  selectedBackgroundColor  DOCUMENT ME!
     * @param  selectedForegroundColor  DOCUMENT ME!
     */
    public HighlightingRadioButtonMenuItem(
            final Color selectedBackgroundColor,
            final Color selectedForegroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
        this.selectedForegroundColor = selectedForegroundColor;
        normalBackgroundColor = bsp.getBackground();
        normalForegroundColor = bsp.getForeground();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Color getBackground() {
        if (isSelected()) {
            return selectedBackgroundColor;
        } else {
            return normalBackgroundColor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Color getForeground() {
        if (isSelected()) {
            return selectedForegroundColor;
        } else {
            return normalForegroundColor;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Color getSelectedBackgroundColor() {
        return selectedBackgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedBackgroundColor  DOCUMENT ME!
     */
    public void setSelectedBackgroundColor(final Color selectedBackgroundColor) {
        this.selectedBackgroundColor = selectedBackgroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Color getSelectedForegroundColor() {
        return selectedForegroundColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectedForegroundColor  DOCUMENT ME!
     */
    public void setSelectedForegroundColor(final Color selectedForegroundColor) {
        this.selectedForegroundColor = selectedForegroundColor;
    }
}
