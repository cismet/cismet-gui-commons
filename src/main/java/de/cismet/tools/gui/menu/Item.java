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
public class Item {

    //~ Instance fields --------------------------------------------------------

    private String actionKey;
    private String name;
    private String i18nKey;
    private Item[] items;
    private boolean toggle;
    private String radio;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the actionKey
     */
    public String getActionKey() {
        return actionKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  actionKey  the actionKey to set
     */
    public void setActionKey(final String actionKey) {
        this.actionKey = actionKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the name
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  name  the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the i18nKey
     */
    public String getI18nKey() {
        return i18nKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  i18nKey  the i18nKey to set
     */
    public void setI18nKey(final String i18nKey) {
        this.i18nKey = i18nKey;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the items
     */
    public Item[] getItems() {
        return items;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  items  the items to set
     */
    public void setItems(final Item[] items) {
        this.items = items;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the toggle
     */
    public boolean isToggle() {
        return toggle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toggle  the toggle to set
     */
    public void setToggle(final boolean toggle) {
        this.toggle = toggle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the radio
     */
    public String getRadio() {
        return radio;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  radio  the radio to set
     */
    public void setRadio(final String radio) {
        this.radio = radio;
    }
}
