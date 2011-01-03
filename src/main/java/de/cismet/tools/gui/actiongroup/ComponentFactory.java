/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.actiongroup;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public final class ComponentFactory {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ComponentFactory object.
     */
    private ComponentFactory() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   action  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static AbstractButton getRadioButton(final Action action) {
        final JRadioButton button = new JRadioButton(action);
        connectActionAndButton(action, button);
        return button;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   action  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static AbstractButton getToggleButton(final Action action) {
        final JToggleButton button = new JToggleButton(action);
        connectActionAndButton(action, button);
        return button;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   action  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JMenuItem getRadioMenuItem(final Action action) {
        final JRadioButtonMenuItem menu = new JRadioButtonMenuItem(action);
        connectActionAndButton(action, menu);
        return menu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  action  DOCUMENT ME!
     * @param  button  DOCUMENT ME!
     */
    private static void connectActionAndButton(final Action action, final AbstractButton button) {
        final SelectionStateAdapter adapter = new SelectionStateAdapter(action, button);
        adapter.configure();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * Class that connects the selection state of the action to the selection state of the button.
     *
     * @author   R.J. Lorimer
     * @version  $Revision$, $Date$
     */
    private static class SelectionStateAdapter implements PropertyChangeListener, ItemListener {

        //~ Instance fields ----------------------------------------------------

        private Action action;
        private AbstractButton button;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SelectionStateAdapter object.
         *
         * @param  theAction  DOCUMENT ME!
         * @param  theButton  DOCUMENT ME!
         */
        public SelectionStateAdapter(final Action theAction, final AbstractButton theButton) {
            action = theAction;
            button = theButton;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         */
        protected void configure() {
            action.addPropertyChangeListener(this);
            button.addItemListener(this);
        }
        @Override
        public void itemStateChanged(final ItemEvent e) {
            final boolean value = e.getStateChange() == ItemEvent.SELECTED;
            final Boolean valueObj = Boolean.valueOf(value);
            action.putValue(ActionConstants.SELECTED_KEY, valueObj);
        }

        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(ActionConstants.SELECTED_KEY)) {
                final Boolean newSelectedState = (Boolean)evt.getNewValue();
                button.setSelected(newSelectedState.booleanValue());
            }
        }
    }
}
