/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.breadcrumb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class BreadCrumb extends AbstractAction {

    //~ Instance fields --------------------------------------------------------

    private Vector<ActionListener> listeners = new Vector<ActionListener>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BreadCrumb object.
     *
     * @param  name  DOCUMENT ME!
     */
    public BreadCrumb(final String name) {
        super(name);
    }

    /**
     * Creates a new BreadCrumb object.
     *
     * @param  name  DOCUMENT ME!
     * @param  icon  DOCUMENT ME!
     */
    public BreadCrumb(final String name, final Icon icon) {
        super(name, icon);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    @Override
    public void actionPerformed(final ActionEvent e) {
        crumbActionPerformed(e);
        fireActionPerformed(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  icon  DOCUMENT ME!
     */
    public void setIcon(final Icon icon) {
        this.putValue(BreadCrumb.SMALL_ICON, icon);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public abstract void crumbActionPerformed(ActionEvent e);

    /**
     * DOCUMENT ME!
     *
     * @param  al  DOCUMENT ME!
     */
    public void addActionListener(final ActionListener al) {
        listeners.add(al);
    }
    /**
     * DOCUMENT ME!
     *
     * @param  al  DOCUMENT ME!
     */
    public void removeActionListener(final ActionListener al) {
        listeners.remove(al);
    }
    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireActionPerformed(final ActionEvent e) {
        for (final ActionListener al : listeners) {
            al.actionPerformed(e);
        }
    }
}
