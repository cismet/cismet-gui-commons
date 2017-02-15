/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.menu.example;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

/**
 *
 * @author therter
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiAction.class)
public class NewProjectAction extends AbstractAction implements CidsUiAction {

    public NewProjectAction() {
        putValue(CidsUiAction.CIDS_ACTION_KEY, "newProject");
        putValue(SHORT_DESCRIPTION, "Neues Projekt");
        putValue(NAME, "Neues Projekt");
    }

    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("NewProjectAction pressed");
    }


}
