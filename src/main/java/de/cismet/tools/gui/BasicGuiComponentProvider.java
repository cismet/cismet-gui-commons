/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 thorsten
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */
package de.cismet.tools.gui;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BasicGuiComponentProvider {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum GuiType {

        //~ Enum constants -----------------------------------------------------

        TOOLBARCOMPONENT, GUICOMPONENT, DUMMY
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getId();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getName();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getDescription();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getIcon();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JComponent getComponent();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    GuiType getType();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getPositionHint();

    /**
     * DOCUMENT ME!
     *
     * @param  link  DOCUMENT ME!
     */
    void setLinkObject(Object link);
}
