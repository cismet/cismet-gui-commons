/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.menu;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Menu {

    //~ Instance fields --------------------------------------------------------

    private Item[] mainMenu;
    private Toolbar[] toolbars;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the mainMenu
     */
    public Item[] getMainMenu() {
        return mainMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mainMenu  the mainMenu to set
     */
    public void setMainMenu(final Item[] mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the toolbars
     */
    public Toolbar[] getToolbars() {
        return toolbars;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toolbars  the toolbars to set
     */
    public void setToolbars(final Toolbar[] toolbars) {
        this.toolbars = toolbars;
    }
}
