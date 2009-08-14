/*
 * HistoryModel.java
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

import java.util.Vector;

/**
 * The interface of the HistoryModel for the JHistoryButton. See a short 
 * <a href="http://flexo.cismet.de/gadgets/JHistory/">description and demo</a>
 * on the website.
 * <p>
 *
 * <p>
 * License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a>
 * <br><img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif">
 * </p>
 *
 * @author thorsten.hell@cismet.de
 */
public interface HistoryModel {
    public void setMaximumPossibilities(int max);
    public boolean isBackPossible();
    public boolean isForwardPossible();
    public Object back(boolean external);
    public Object forward(boolean external);
    public void addHistoryModelListener(HistoryModelListener hml);
    public void removeHistoryModelListener(HistoryModelListener hml);
    public Vector getBackPossibilities();
    public Vector getForwardPossibilities();
    public void addToHistory(Object o);
    public Object getCurrentElement();
}
