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
package de.cismet.tools.gui.jtextcompoonent.undoredo;

import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.undo.*;

/**
 * TableModel decoration for Undo/Redo Support.
 *
 * <p>WARNING: might fail on sorted Tables!</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class UndoableTableModel extends AbstractTableModel implements TableModelListener {

    //~ Instance fields --------------------------------------------------------

    private TableModel delegate;
    private UndoableEditSupport support = new UndoableEditSupport();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new UndoableTableModel object.
     *
     * @param  delegate  DOCUMENT ME!
     */
    public UndoableTableModel(final TableModel delegate) {
        setDelegate(delegate);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  delegate  DOCUMENT ME!
     */
    public void setDelegate(final TableModel delegate) {
        if (this.delegate != null) {
            this.delegate.removeTableModelListener(this);
        }
        this.delegate = delegate;
        if (this.delegate != null) {
            this.delegate.addTableModelListener(this);
        }
        fireTableStructureChanged();
    }

    @Override
    public Class<?> getColumnClass(final int columnIndex) {
        return delegate.getColumnClass(columnIndex);
    }

    @Override
    public int getColumnCount() {
        return delegate.getColumnCount();
    }

    @Override
    public String getColumnName(final int columnIndex) {
        return delegate.getColumnName(columnIndex);
    }

    @Override
    public int getRowCount() {
        return delegate.getRowCount();
    }

    @Override
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        return delegate.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return delegate.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        final Object oldValue = delegate.getValueAt(rowIndex, columnIndex);
        delegate.setValueAt(aValue, rowIndex, columnIndex);
        final Object newValue = delegate.getValueAt(rowIndex, columnIndex);
        fireChangeEdit(rowIndex, columnIndex, oldValue, newValue);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void addUndoableEditListener(final UndoableEditListener l) {
        support.addUndoableEditListener(l);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  l  DOCUMENT ME!
     */
    public void removeUndoableEditListener(final UndoableEditListener l) {
        support.removeUndoableEditListener(l);
    }

    @Override
    public void tableChanged(final TableModelEvent e) {
        final TableModelEvent newEvent = new TableModelEvent(
                this,
                e.getFirstRow(),
                e.getLastRow(),
                e.getColumn(),
                e.getType());
        fireTableChanged(newEvent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  row       DOCUMENT ME!
     * @param  col       DOCUMENT ME!
     * @param  oldValue  DOCUMENT ME!
     * @param  newValue  DOCUMENT ME!
     */
    protected void fireChangeEdit(final int row, final int col, final Object oldValue, final Object newValue) {
        final UndoableEdit edit = new TableChangeEdit(row, col, oldValue, newValue);
        support.beginUpdate();
        support.postEdit(edit);
        support.endUpdate();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class TableChangeEdit extends AbstractUndoableEdit {

        //~ Instance fields ----------------------------------------------------

        private int columnIndex;
        private int rowIndex;
        private Object oldValue;
        private Object newValue;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new TableChangeEdit object.
         *
         * @param  rowIndex     DOCUMENT ME!
         * @param  columnIndex  DOCUMENT ME!
         * @param  oldValue     DOCUMENT ME!
         * @param  newValue     DOCUMENT ME!
         */
        public TableChangeEdit(final int rowIndex,
                final int columnIndex,
                final Object oldValue,
                final Object newValue) {
            this.columnIndex = columnIndex;
            this.rowIndex = rowIndex;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void undo() {
            super.undo();
            delegate.setValueAt(oldValue, rowIndex, columnIndex);
        }

        @Override
        public void redo() {
            super.redo();
            delegate.setValueAt(newValue, rowIndex, columnIndex);
        }
    }
}
