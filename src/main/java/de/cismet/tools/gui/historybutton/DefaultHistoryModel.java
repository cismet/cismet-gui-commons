/*
 * DefaultHistoryModel.java
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
 * Created on 14. Juli 2005, 14:32
 *
 */

package de.cismet.tools.gui.historybutton;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

/**
 * An implementation of the HistoryModel for the JHistoryButton. See a short 
 * <a href="http://flexo.cismet.de/gadgets/JHistory/">description and demo</a>
 * on the website.
 * <p>
 *
 * <p>
 * License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a>
 * <br><img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif">
 * </p>
 * @author thorsten.hell@cismet.de
 */
public class DefaultHistoryModel implements HistoryModel {
    
    Vector modelListeners=new Vector();
    
    Stack backHistory=new Stack();
    Stack forwardHistory=new Stack();
    Object currentElement=null;
    int maximumPossibilities=-1;
    /** Creates a new instance of DefaultHistoryModel */
    public DefaultHistoryModel() {
    }

    public void addToHistory(Object o) {
        boolean tmpFw=isForwardPossible();
        if (o!=null) {
            if (currentElement!=null) {
                if (currentElement.equals(o)) {
                    return;
                }
                backHistory.push(currentElement);
            }
            currentElement=o;
            if (tmpFw!=isForwardPossible()) {
                fireForwardStatusChanged();
            }
            forwardHistory.removeAllElements();
            fireBackStatusChanged();
            fireHistoryChanged();
            fireForwardStatusChanged();
        }
    }

    public void setMaximumPossibilities(int max) {
        maximumPossibilities=max;
    }

    public boolean isForwardPossible() {
        return !(forwardHistory.empty());
    }

    public boolean isBackPossible() {
        return !(backHistory.empty());
    }

    public java.util.Vector getForwardPossibilities() {
        return forwardHistory;
    }

    public java.util.Vector getBackPossibilities() {
        return backHistory;
    }

    public Object forward(boolean external) {
        if (isForwardPossible()) {
            backHistory.push(currentElement);
            currentElement=forwardHistory.pop();
            fireForwardStatusChanged();
            fireBackStatusChanged();
            if (external)fireHistoryChanged();
            return currentElement;
        }
        return null;
    }

    public Object back(boolean external) {
        if (isBackPossible()) {
            forwardHistory.push(currentElement);
            currentElement=backHistory.pop();
            fireBackStatusChanged();
            fireForwardStatusChanged();
            if (external) fireHistoryChanged();
            return currentElement;
        }
        return null;
    }
    
    public Object getCurrentElement() {
        return currentElement;
    }

    public void addHistoryModelListener(HistoryModelListener hml) {
        modelListeners.add(hml);
        hml.historyChanged();
    }

    public void removeHistoryModelListener(HistoryModelListener hml) {
        modelListeners.remove(hml);
    }
    
    private void fireForwardStatusChanged() {
        Iterator it=modelListeners.iterator();
        while (it.hasNext()) {
            Object o=it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).forwardStatusChanged();
            }
        }
    }
    
    private void fireBackStatusChanged() {
        Iterator it=modelListeners.iterator();
        while (it.hasNext()) {
            Object o=it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).backStatusChanged();
            }
        }
    }

    private void fireHistoryChanged() {
        Iterator it=modelListeners.iterator();
        while (it.hasNext()) {
            Object o=it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).historyChanged();
            }
        }
    }
    
    private void fireHistoryActionPerformed() {
        
    }

    
}
