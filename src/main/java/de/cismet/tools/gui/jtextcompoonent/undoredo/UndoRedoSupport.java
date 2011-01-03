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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.table.TableModel;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.UndoManager;

/**
 * A class that adds undo/redo support to JTextComponents.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public class UndoRedoSupport {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new UndoRedoSupport object.
     */
    private UndoRedoSupport() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Add undo/redo support to the given JTextComponent.
     *
     * <p>Maps: Undo -> CONTROL-Z Redo -> CONTROL-Y</p>
     *
     * @param  c  the JTextComponent to decorate with undo/redo support.
     */
    public static void decorate(final JTextComponent c) {
        final Document doc = c.getDocument();
        final UndoManager undo = new UndoManager();
        doc.addUndoableEditListener(undo);
        addCommandsToComponent(c, undo);
    }

    /**
     * DANGER: might not work with sorted Tables!
     *
     * @param       c  DOCUMENT ME!
     *
     * @deprecated  DOCUMENT ME!
     */
    @Deprecated
    public static void decorate(final JTable c) {
        final TableModel mo = c.getModel();
        final UndoableTableModel umo = new UndoableTableModel(mo);
        final UndoManager undo = new UndoManager();
        umo.addUndoableEditListener(undo);
        c.setModel(umo);
        addCommandsToComponent(c, undo);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c     DOCUMENT ME!
     * @param  undo  DOCUMENT ME!
     */
    private static void addCommandsToComponent(final JComponent c, final UndoManager undo) {
        final InputMap im = c.getInputMap(JTable.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        final UndoAction ua = new UndoAction(undo);
        final KeyStroke controlZ = KeyStroke.getKeyStroke(KeyEvent.VK_Z, Event.CTRL_MASK);
        im.put(controlZ, "control z");         // NOI18N
        c.getActionMap().put("control z", ua); // NOI18N
        final RedoAction ra = new RedoAction(undo);
        final KeyStroke controlY = KeyStroke.getKeyStroke(KeyEvent.VK_Y, Event.CTRL_MASK);
        im.put(controlY, "control y");         // NOI18N
        c.getActionMap().put("control y", ra); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  c  DOCUMENT ME!
     */
    public static final void discardAllEdits(final JTextComponent c) {
        Action aa = c.getActionMap().get("control z"); // NOI18N
        if ((aa != null) && (aa instanceof ManagerAction)) {
            ((ManagerAction)aa).discardAllEdits();
        }
        aa = c.getActionMap().get("control y");        // NOI18N
        if ((aa != null) && (aa instanceof ManagerAction)) {
            ((ManagerAction)aa).discardAllEdits();
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private abstract static class ManagerAction extends AbstractAction {

        //~ Instance fields ----------------------------------------------------

        private final UndoManager undo;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ManagerAction object.
         *
         * @param   undo  DOCUMENT ME!
         *
         * @throws  IllegalArgumentException  DOCUMENT ME!
         */
        public ManagerAction(final UndoManager undo) {
            if (undo == null) {
                throw new IllegalArgumentException("UndoManager can not be null for ManagerAction construction!"); // NOI18N
            }
            this.undo = undo;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        public void discardAllEdits() {
            getUndo().discardAllEdits();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public UndoManager getUndo() {
            return undo;
        }
    }

    /**
     * An Undo action on the given UndoManager.
     *
     * @author   srichter
     * @version  $Revision$, $Date$
     */
    private static class UndoAction extends ManagerAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new UndoAction object.
         *
         * @param  undo  DOCUMENT ME!
         */
        public UndoAction(final UndoManager undo) {
            super(undo);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (getUndo().canUndo()) {
                getUndo().undo();
            }
        }
    }

    /**
     * A Redo action on the given UndoManager.
     *
     * @author   srichter
     * @version  $Revision$, $Date$
     */
    private static class RedoAction extends ManagerAction {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RedoAction object.
         *
         * @param  undo  DOCUMENT ME!
         */
        public RedoAction(final UndoManager undo) {
            super(undo);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (getUndo().canRedo()) {
                getUndo().redo();
            }
        }
    }
}
