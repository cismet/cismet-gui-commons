/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.menu.example;

/**
 *
 * @author therter
 */
public class Menu {
    private Item[] mainMenu;
    private Item[] mainToolbar;
    private Item[] mapToolbar;

    /**
     * @return the mainMenu
     */
    public Item[] getMainMenu() {
        return mainMenu;
    }

    /**
     * @param mainMenu the mainMenu to set
     */
    public void setMainMenu(Item[] mainMenu) {
        this.mainMenu = mainMenu;
    }

    /**
     * @return the mainToolbar
     */
    public Item[] getMainToolbar() {
        return mainToolbar;
    }

    /**
     * @param mainToolbar the mainToolbar to set
     */
    public void setMainToolbar(Item[] mainToolbar) {
        this.mainToolbar = mainToolbar;
    }

    /**
     * @return the mapToolbar
     */
    public Item[] getMapToolbar() {
        return mapToolbar;
    }

    /**
     * @param mapToolbar the mapToolbar to set
     */
    public void setMapToolbar(Item[] mapToolbar) {
        this.mapToolbar = mapToolbar;
    }
}
