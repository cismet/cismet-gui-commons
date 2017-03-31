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
public class Toolbar {

    //~ Instance fields --------------------------------------------------------

    private String id;
    private Item[] items;

    //~ Methods ----------------------------------------------------------------

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
     * @return  the id
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  id  the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }
}
