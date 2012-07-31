/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

/**
 * A JLabel acting as table cell renderer for a color value. The color is used as background color.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class ColorRenderer extends JLabel implements TableCellRenderer {

    //~ Instance fields --------------------------------------------------------

    private Border unselectedBorder = null;
    private Border selectedBorder = null;
    private final boolean isBordered;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ColorRenderer object.
     *
     * @param  isBordered  If the cell shall be bordered.
     */
    public ColorRenderer(final boolean isBordered) {
        this.isBordered = isBordered;

        // MUST do this for background to show up.
        setOpaque(true);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public Component getTableCellRendererComponent(final JTable table,
            final Object color,
            final boolean isSelected,
            final boolean hasFocus,
            final int row,
            final int column) {
        final Color newColor = (Color)color;
        setBackground(newColor);

        if (isBordered) {
            if (isSelected) {
                if (selectedBorder == null) {
                    selectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getSelectionBackground());
                }

                setBorder(selectedBorder);
            } else {
                if (unselectedBorder == null) {
                    unselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, table.getBackground());
                }

                setBorder(unselectedBorder);
            }
        }

        return this;
    }
}
