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
import javax.swing.Action;

import de.cismet.tools.gui.menu.CidsUiAction;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiAction.class)
public class NewProjectAction extends AbstractAction implements CidsUiAction {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NewProjectAction object.
     */
    public NewProjectAction() {
        putValue(CidsUiAction.CIDS_ACTION_KEY, "newProject");
        putValue(SHORT_DESCRIPTION, "Neues Projekt");
        putValue(NAME, "Neues Projekt");
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof Action) {
            System.out.println("NewProjectAction pressed by "
                        + ((Action)e.getSource()).getValue(CidsUiAction.CIDS_ACTION_KEY));
        } else {
            System.out.println("NewProjectAction pressed");
        }
    }
}
