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
package de.cismet.tools.gui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSeparator;
import javax.swing.JToolBar;

import de.cismet.tools.gui.menu.CidsUiComponent;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = CidsUiComponent.class)
public class ToolbarSeparator extends JSeparator implements CidsUiComponent {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ToolbarSeparator object.
     */
    public ToolbarSeparator() {
        setOrientation(JSeparator.VERTICAL);
        setMaximumSize(new Dimension(3, 34));
        setMinimumSize(new Dimension(3, 34));
        setPreferredSize(new Dimension(3, 34));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public String getValue(final String key) {
        return "ToolbarSeparator";
    }

    @Override
    public Component getComponent() {
        return new ToolbarSeparator();
    }
}
