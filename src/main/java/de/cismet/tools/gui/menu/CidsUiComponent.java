/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.menu;

import java.awt.Component;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is a marker for cids ui components.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface CidsUiComponent {

    //~ Instance fields --------------------------------------------------------

    String CIDS_ACTION_KEY = "CidsActionKey";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   key  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getValue(String key);

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getComponent();
}
