/*
 * ComponentFactory.java
 * Copyright (C) 2005 by:
 *
 *----------------------------
 * cismet GmbH
 * Goebenstrasse 40
 * 66117 Saarbruecken
 * http://www.cismet.de
 *----------------------------
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *----------------------------
 * Author:
 * thorsten.hell@cismet.de
 *----------------------------
 *
 * Created on 22. Februar 2006, 14:19
 *
 */

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
public final class ComponentFactory {
    
    private ComponentFactory() { }
    
    public static AbstractButton getRadioButton(Action action) {
        JRadioButton button = new JRadioButton(action);
        connectActionAndButton(action, button);
        return button;
    }
    
    public static AbstractButton getToggleButton(Action action) {
        JToggleButton button = new JToggleButton(action);
        connectActionAndButton(action, button);
        return button;
    }
    
    public static JMenuItem getRadioMenuItem(Action action) {
        JRadioButtonMenuItem menu = new JRadioButtonMenuItem(action);
        connectActionAndButton(action, menu);
        return menu;
    }
    
    private static void connectActionAndButton(Action action, AbstractButton button) {
        SelectionStateAdapter adapter = new SelectionStateAdapter(action, button);
        adapter.configure();
    }
    
    /**
     * Class that connects the selection state of the action
     * to the selection state of the button.
     *
     * @author R.J. Lorimer
     */
    private static class SelectionStateAdapter implements PropertyChangeListener, ItemListener {
        private Action action;
        private AbstractButton button;
        public SelectionStateAdapter(Action theAction, AbstractButton theButton) {
            action = theAction;
            button = theButton;
        }
        protected void configure() {
            action.addPropertyChangeListener(this);
            button.addItemListener(this);
        }
        public void itemStateChanged(ItemEvent e) {
            boolean value = e.getStateChange() == ItemEvent.SELECTED;
            Boolean valueObj = Boolean.valueOf(value);
            action.putValue(ActionConstants.SELECTED_KEY, valueObj);
        }
        
        public void propertyChange(PropertyChangeEvent evt) {
            if(evt.getPropertyName().equals(ActionConstants.SELECTED_KEY)) {
                Boolean newSelectedState = (Boolean)evt.getNewValue();
                button.setSelected(newSelectedState.booleanValue());
            }
        }
    }
}