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
public class Item {
    private String actionKey;
    private String name;
    private String i18nKey;
    private Item[] items;

    /**
     * @return the actionKey
     */
    public String getActionKey() {
        return actionKey;
    }

    /**
     * @param actionKey the actionKey to set
     */
    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the i18nKey
     */
    public String getI18nKey() {
        return i18nKey;
    }

    /**
     * @param i18nKey the i18nKey to set
     */
    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * @return the items
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public void setItems(Item[] items) {
        this.items = items;
    }
}
