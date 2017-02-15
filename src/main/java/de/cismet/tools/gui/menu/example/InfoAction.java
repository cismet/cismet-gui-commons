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
public class InfoAction extends AbstractAction implements CidsUiAction {

    public InfoAction() {
        putValue(CidsUiAction.CIDS_ACTION_KEY, "info");
        putValue(SHORT_DESCRIPTION, "Info Fenster");
        putValue(NAME, "Info");
    }

    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Info pressed");
    }


}
