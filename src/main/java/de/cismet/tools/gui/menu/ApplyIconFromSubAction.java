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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ApplyIconFromSubAction extends AbstractAction implements CidsUiAction {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof Action) {
            final Action a = (Action)e.getSource();
            putValue(LARGE_ICON_KEY, a.getValue(LARGE_ICON_KEY));
            putValue(SMALL_ICON, a.getValue(SMALL_ICON));
        }
    }
}
