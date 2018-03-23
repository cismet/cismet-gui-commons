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

import java.util.EventObject;

import javax.swing.JMenuItem;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class CidsUiMenuProviderEvent extends EventObject {

    //~ Instance fields --------------------------------------------------------

    private final JMenuItem affectedMenuItem;
    private final int index;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsUiMenuProviderEvent object.
     *
     * @param  affectedMenuItem  DOCUMENT ME!
     * @param  index             DOCUMENT ME!
     * @param  source            DOCUMENT ME!
     */
    public CidsUiMenuProviderEvent(final JMenuItem affectedMenuItem, final int index, final Object source) {
        super(source);
        this.affectedMenuItem = affectedMenuItem;
        this.index = index;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JMenuItem getAffectedMenuItem() {
        return affectedMenuItem;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getIndex() {
        return index;
    }
}
