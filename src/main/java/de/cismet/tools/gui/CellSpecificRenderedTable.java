/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui;

import org.jdesktop.swingx.JXTable;

import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * A JXTable that can use different TableCellRenderer/TableCellEditor for one column.<br />
 * The renderer/editor order:
 *
 * <ul>
 *   <li>the renderer that was specified for the individual cell</li>
 *   <li>the renderer that was specified for the row of the cell</li>
 *   <li>the renderer that was specified for the column of the cell</li>
 * </ul>
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CellSpecificRenderedTable extends JXTable {

    //~ Instance fields --------------------------------------------------------

    private Map<Integer, TableCellRenderer> rowRenderer = new HashMap<Integer, TableCellRenderer>();
    private Map<TablePosition, TableCellRenderer> customCellRenderer = new HashMap<TablePosition, TableCellRenderer>();
    private Map<Integer, TableCellEditor> rowEditor = new HashMap<Integer, TableCellEditor>();
    private Map<TablePosition, TableCellEditor> customCellEditor = new HashMap<TablePosition, TableCellEditor>();

    //~ Methods ----------------------------------------------------------------

    /**
     * Sets the <code>TableCellRenderer</code> used by <code>JTable</code> to draw individual values for this row.
     *
     * @param  row       the row that should use the renderer
     * @param  renderer  the renderer to be used
     */
    public void addRowRenderer(final int row, final TableCellRenderer renderer) {
        rowRenderer.put(row, renderer);
    }

    /**
     * Sets the <code>TableCellRenderer</code> used by <code>JTable</code> to draw individual values for this cell.
     *
     * @param  column    the column of the cell
     * @param  row       the row of the cell
     * @param  renderer  the renderer to be used
     */
    public void addCellRenderer(final int column, final int row, final TableCellRenderer renderer) {
        customCellRenderer.put(new TablePosition(column, row), renderer);
    }

    /**
     * Sets the editor to used by when a cell in this row is edited.
     *
     * @param  row     the row that should use the editor
     * @param  editor  the editor to be used
     */
    public void addRowEditor(final int row, final TableCellEditor editor) {
        rowEditor.put(row, editor);
    }

    /**
     * Sets the editor to used by when a cell in this cell is edited.
     *
     * @param  column  the column of the cell
     * @param  row     the row of the cell
     * @param  editor  the editor to be used
     */
    public void addCellEditor(final int column, final int row, final TableCellEditor editor) {
        customCellEditor.put(new TablePosition(column, row), editor);
    }

    /**
     * remove all cell renderers. This does not include the renderers for a complete row. See
     * {@link #removeRowRenderer(int)}
     */
    public void removeAllCellRenderers() {
        customCellRenderer.clear();
    }

    /**
     * remove all cell editors. This does not include the editors for a complete row. See {@link #removeRowEditor(int) }
     */
    public void removeAllCellEditors() {
        customCellEditor.clear();
    }

    /**
     * Remove the renderer from the given cell. A row renderer or a column renderer can still be active for the given
     * cell.
     *
     * @param  column  the column of the cell
     * @param  row     the row of the cell!
     */
    public void removeCellRenderer(final int column, final int row) {
        customCellRenderer.remove(new TablePosition(column, row));
    }

    /**
     * Remove the editor from the given cell. A row editor or a column editor can still be active for the given cell.
     *
     * @param  column  the column of the cell
     * @param  row     the row of the cell
     */
    public void removeCellEditor(final int column, final int row) {
        customCellEditor.remove(new TablePosition(column, row));
    }

    /**
     * Remove the renderer from the given row. A cell renderer or a column renderer can still be active for the given
     * row.
     *
     * @param  row  the row, the renderer should be removed
     */
    public void removeRowRenderer(final int row) {
        rowRenderer.remove(row);
    }

    /**
     * Remove the editor from the given row. A cell editor or a column editor can still be active for the given row.
     *
     * @param  row  the row, the editor should be removed
     */
    public void removeRowEditor(final int row) {
        rowEditor.remove(row);
    }

    @Override
    public TableCellRenderer getCellRenderer(final int row, final int column) {
        TableCellRenderer customRenderer = customCellRenderer.get(new TablePosition(column, row));

        if (customRenderer == null) {
            customRenderer = rowRenderer.get(row);
        }

        if (customRenderer != null) {
            return customRenderer;
        } else {
            return super.getCellRenderer(row, column);
        }
    }

    @Override
    public TableCellEditor getCellEditor(final int row, final int column) {
        TableCellEditor customEditor = customCellEditor.get(new TablePosition(column, row));

        if (customEditor == null) {
            customEditor = rowEditor.get(row);
        }

        if (customEditor != null) {
            return customEditor;
        } else {
            return super.getCellEditor(row, column);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Specifies a cell of a table.
     *
     * @version  $Revision$, $Date$
     */
    class TablePosition {

        //~ Instance fields ----------------------------------------------------

        private int column;
        private int row;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TablePosition object.
         *
         * @param  column  DOCUMENT ME!
         * @param  row     DOCUMENT ME!
         */
        public TablePosition(final int column, final int row) {
            this.column = column;
            this.row = row;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Returns the column of the cell.
         *
         * @return  the column of the cell
         */
        public int getColumn() {
            return column;
        }

        /**
         * Set the column of the cell.
         *
         * @param  column  the column to set
         */
        public void setColumn(final int column) {
            this.column = column;
        }

        /**
         * Returns the row of the cell.
         *
         * @return  the row of the cell
         */
        public int getRow() {
            return row;
        }

        /**
         * Set the row of the ecll.
         *
         * @param  row  the row to set
         */
        public void setRow(final int row) {
            this.row = row;
        }

        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof TablePosition) {
                final TablePosition other = (TablePosition)obj;

                return (this.row == other.row) && (this.column == other.column);
            }

            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = (97 * hash) + this.column;
            hash = (97 * hash) + this.row;
            return hash;
        }
    }
}
