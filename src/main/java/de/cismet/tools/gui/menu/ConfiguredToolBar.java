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

import javax.swing.JToolBar;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ConfiguredToolBar {

    //~ Instance fields --------------------------------------------------------

    private Toolbar toolbarDefinition;
    private JToolBar toolbar;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the toolbarDefinition
     */
    public Toolbar getToolbarDefinition() {
        return toolbarDefinition;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toolbarDefinition  the toolbarDefinition to set
     */
    public void setToolbarDefinition(final Toolbar toolbarDefinition) {
        this.toolbarDefinition = toolbarDefinition;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the toolbar
     */
    public JToolBar getToolbar() {
        return toolbar;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  toolbar  the toolbar to set
     */
    public void setToolbar(final JToolBar toolbar) {
        this.toolbar = toolbar;
    }
}
