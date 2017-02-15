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
public class ExitAction extends AbstractAction implements CidsUiAction {

    public ExitAction() {
        putValue(CidsUiAction.CIDS_ACTION_KEY, "exit");
        putValue(SHORT_DESCRIPTION, "Anwendung beenden");
        putValue(NAME, "Beenden");
    }

    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }


}
