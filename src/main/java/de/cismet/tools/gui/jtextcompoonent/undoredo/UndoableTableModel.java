/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.jtextcompoonent.undoredo;

import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.undo.*;

/**
 * TableModel decoration for Undo/Redo Support
 * 
 * WARNING: might fail on sorted Tables!
 * 
 * @author srichter
 */
public class UndoableTableModel extends AbstractTableModel implements TableModelListener {

    private TableModel delegate;
    private UndoableEditSupport support = new UndoableEditSupport();

    public UndoableTableModel(TableModel delegate) {
        setDelegate(delegate);
    }

    public void setDelegate(TableModel delegate) {
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
    public Class<?> getColumnClass(int columnIndex) {
        return delegate.getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return delegate.getColumnCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return delegate.getColumnName(columnIndex);
    }

    public int getRowCount() {
        return delegate.getRowCount();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return delegate.getValueAt(rowIndex, columnIndex);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return delegate.isCellEditable(rowIndex, columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Object oldValue = delegate.getValueAt(rowIndex, columnIndex);
        delegate.setValueAt(aValue, rowIndex, columnIndex);
        Object newValue = delegate.getValueAt(rowIndex, columnIndex);
        fireChangeEdit(rowIndex, columnIndex, oldValue, newValue);
    }

    public void addUndoableEditListener(UndoableEditListener l) {
        support.addUndoableEditListener(l);
    }

    public void removeUndoableEditListener(UndoableEditListener l) {
        support.removeUndoableEditListener(l);
    }

    public void tableChanged(TableModelEvent e) {
        TableModelEvent newEvent = new TableModelEvent(this, e.getFirstRow(), e.getLastRow(), e.getColumn(), e.getType());
        fireTableChanged(newEvent);
    }

    protected void fireChangeEdit(int row, int col, Object oldValue, Object newValue) {
        UndoableEdit edit = new TableChangeEdit(row, col, oldValue, newValue);
        support.beginUpdate();
        support.postEdit(edit);
        support.endUpdate();
    }

    private class TableChangeEdit extends AbstractUndoableEdit {

        private int columnIndex;
        private int rowIndex;
        private Object oldValue;
        private Object newValue;

        public TableChangeEdit(int rowIndex, int columnIndex, Object oldValue, Object newValue) {
            this.columnIndex = columnIndex;
            this.rowIndex = rowIndex;
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

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
