/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 dmeiers
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.tools.gui.slideabletree;

import org.jdesktop.swingx.JXTaskPane;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class SubTreePane extends JXTaskPane {

    //~ Instance fields --------------------------------------------------------

    private boolean selected = false;
    private Color defaultTitleColor = Color.BLACK;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SubTreePane object.
     */
    public SubTreePane() {
        this.setUI(new SpecialTaskPanebackgroundUI());
        this.setForeground(defaultTitleColor);
//        this.setAnimated(false);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getTitleBarHeight() {
        return (this.getHeight() - this.getContentPane().getHeight());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isSelected() {
        return selected;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selected  DOCUMENT ME!
     */
    public void setSelected(final boolean selected) {
        if (selected) {
            this.setForeground(Color.blue);
        } else {
            this.setForeground(defaultTitleColor);
        }
        this.selected = selected;
    }
}
