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
package de.cismet.commons.gui.protocol;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;

import static javax.swing.Action.SMALL_ICON;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class AbstractProtocolStepToolbarItemAction extends AbstractAction implements ProtocolStepToolbarItem {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractProtocolStepToolbarItemAction object.
     *
     * @param  name  DOCUMENT ME!
     */
    public AbstractProtocolStepToolbarItemAction(final String name) {
        this(name, null, null, null);
    }

    /**
     * Creates a new AbstractProtocolStepToolbarItemAction object.
     *
     * @param  name  DOCUMENT ME!
     * @param  icon  DOCUMENT ME!
     */
    public AbstractProtocolStepToolbarItemAction(final String name, final ImageIcon icon) {
        this(name, null, null, icon);
    }

    /**
     * Creates a new AbstractProtocolStepToolbarItemAction object.
     *
     * @param  name     DOCUMENT ME!
     * @param  tooltip  DOCUMENT ME!
     * @param  icon     DOCUMENT ME!
     */
    public AbstractProtocolStepToolbarItemAction(final String name, final String tooltip, final ImageIcon icon) {
        this(name, tooltip, null, icon);
    }

    /**
     * Creates a new AbstractProtocolStepToolbarItemAction object.
     *
     * @param  name     DOCUMENT ME!
     * @param  tooltip  DOCUMENT ME!
     * @param  command  DOCUMENT ME!
     * @param  icon     DOCUMENT ME!
     */
    public AbstractProtocolStepToolbarItemAction(final String name,
            final String tooltip,
            final String command,
            final ImageIcon icon) {
        if (name != null) {
            putValue(Action.NAME, name);
        }
        if (tooltip != null) {
            putValue(Action.SHORT_DESCRIPTION, tooltip);
        }
        if (command != null) {
            putValue(Action.ACTION_COMMAND_KEY, command);
        }
        if (icon != null) {
            putValue(SMALL_ICON, icon);
        }
    }
}
