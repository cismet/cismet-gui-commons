/*
 * ActionGroup.java
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
 * Created on 22. Februar 2006, 14:12
 *
 */

package de.cismet.tools.gui.actiongroup;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;

/**
 *
 * @author thorsten.hell@cismet.de
 */
public class ActionGroup {
    private List actions;
    private boolean notifyLock;
    private PropertyChangeListener selectedListener;
    
    public ActionGroup() {
        actions = new ArrayList();
        selectedListener = new SelectedListener();
    }
    
    public void add(Action action) {
        actions.add(action);
        action.addPropertyChangeListener(selectedListener);
    }
    
    public void remove(Action action) {
        actions.remove(action);
        action.removePropertyChangeListener(selectedListener);
    }
    public List getActions() {
        return new ArrayList(actions);
    }
    
    private class SelectedListener implements PropertyChangeListener {
        public void propertyChange(PropertyChangeEvent evt) {
            // prevent any poorly implemented components from
            // causing us to get stuck in a feedback loop.
            if(notifyLock) return;
            
            // If it isn't a selected key change, or
            // someone set it to false we just avoid doing anything.
            if(evt.getPropertyName().equals(ActionConstants.SELECTED_KEY) && evt.getNewValue().equals(Boolean.TRUE)) {
                try {
                    notifyLock = true;
                    for(int i=0; i<actions.size();i++) {
                        Action action = (Action)actions.get(i);
                        if(!action.equals(evt.getSource())) {
                            action.putValue(ActionConstants.SELECTED_KEY, Boolean.FALSE);
                        }
                    }
                } finally {
                    notifyLock = false;
                }
            }
        }
    }}
