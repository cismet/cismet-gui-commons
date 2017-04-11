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
package de.cismet.tools.gui.menu.example;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class Menu {

    //~ Instance fields --------------------------------------------------------

    private Item[] mainMenu;
    private Item[] mainToolbar;
    private Item[] mapToolbar;

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
     * @return  the mainToolbar
     */
    public Item[] getMainToolbar() {
        return mainToolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mainToolbar  the mainToolbar to set
     */
    public void setMainToolbar(final Item[] mainToolbar) {
        this.mainToolbar = mainToolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the mapToolbar
     */
    public Item[] getMapToolbar() {
        return mapToolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mapToolbar  the mapToolbar to set
     */
    public void setMapToolbar(final Item[] mapToolbar) {
        this.mapToolbar = mapToolbar;
    }
}
