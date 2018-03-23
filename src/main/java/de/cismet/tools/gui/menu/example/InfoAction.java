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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiAction.class)
public class InfoAction extends AbstractAction implements CidsUiAction {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new InfoAction object.
     */
    public InfoAction() {
        putValue(CidsUiAction.CIDS_ACTION_KEY, "info");
        putValue(SHORT_DESCRIPTION, "Info Fenster");
        putValue(NAME, "Info");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        System.out.println("Info pressed");
    }
}
