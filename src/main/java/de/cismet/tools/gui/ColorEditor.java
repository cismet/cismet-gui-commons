/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.openide.util.NbBundle;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import de.cismet.tools.gui.treetable.AbstractCellEditor;

/**
 * A table cell editor allowing to edit a color values.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class ColorEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final String EDIT = "edit";

    //~ Instance fields --------------------------------------------------------

    private Color color;
    private final JButton button;
    private final JColorChooser colorChooser;
    private final JDialog dialog;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ColorEditor object.
     */
    public ColorEditor() {
        button = new JButton();
        button.setActionCommand(EDIT);
        button.addActionListener(this);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Set up the dialog that the button brings up.
        colorChooser = new JColorChooser();
        dialog = JColorChooser.createDialog(
                button,
                NbBundle.getMessage(ColorEditor.class, "ColorEditor.dialog.title"),
                true,               // modal
                colorChooser,
                this,               // OK button handler
                null);              // no CANCEL button handler
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (EDIT.equals(e.getActionCommand())) {
            // The user has clicked the cell, so
            // bring up the dialog.
            button.setBackground(color);
            colorChooser.setColor(color);
            dialog.setVisible(true);

            fireEditingStopped(); // Make the renderer reappear.
        } else {                  // User pressed dialog's "OK" button.
            color = colorChooser.getColor();
        }
    }

    // Implement the one CellEditor method that AbstractCellEditor doesn't.
    @Override
    public Object getCellEditorValue() {
        return color;
    }

    // Implement the one method defined by TableCellEditor.
    @Override
    public Component getTableCellEditorComponent(final JTable table,
            final Object value,
            final boolean isSelected,
            final int row,
            final int column) {
        color = (Color)value;
        return button;
    }
}
