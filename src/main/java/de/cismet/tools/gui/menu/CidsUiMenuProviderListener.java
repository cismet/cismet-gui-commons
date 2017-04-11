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
public interface CidsUiMenuProviderListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    void menuItemAdded(CidsUiMenuProviderEvent e);

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    void menuItemRemoved(CidsUiMenuProviderEvent e);
}
