/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.treetable;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.*;

import java.io.Serializable;

import java.util.EventObject;

import javax.swing.*;
import javax.swing.event.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class AbstractCellEditor implements CellEditor {

    //~ Instance fields --------------------------------------------------------

    protected EventListenerList listenerList = new EventListenerList();

    //~ Methods ----------------------------------------------------------------

    @Override
    public Object getCellEditorValue() {
        return null;
    }
    @Override
    public boolean isCellEditable(final EventObject e) {
        return true;
    }
    @Override
    public boolean shouldSelectCell(final EventObject anEvent) {
        return false;
    }
    @Override
    public boolean stopCellEditing() {
        return true;
    }
    @Override
    public void cancelCellEditing() {
    }

    @Override
    public void addCellEditorListener(final CellEditorListener l) {
        listenerList.add(CellEditorListener.class, l);
    }

    @Override
    public void removeCellEditorListener(final CellEditorListener l) {
        listenerList.remove(CellEditorListener.class, l);
    }
    /**
     * Notify all listeners that have registered interest for notification on this event type.
     *
     * @see  EventListenerList
     */
    protected void fireEditingStopped() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener)listeners[i + 1]).editingStopped(new ChangeEvent(this));
            }
        }
    }
    /**
     * Notify all listeners that have registered interest for notification on this event type.
     *
     * @see  EventListenerList
     */
    protected void fireEditingCanceled() {
        // Guaranteed to return a non-null array
        final Object[] listeners = listenerList.getListenerList();
        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) {
            if (listeners[i] == CellEditorListener.class) {
                ((CellEditorListener)listeners[i + 1]).editingCanceled(new ChangeEvent(this));
            }
        }
    }
}
