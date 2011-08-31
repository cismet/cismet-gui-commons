/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.breadcrumb;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

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

    private final transient Set<ActionListener> listeners;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BreadCrumb object.
     *
     * @param  name  DOCUMENT ME!
     */
    public BreadCrumb(final String name) {
        this(name, null);
    }

    /**
     * Creates a new BreadCrumb object.
     *
     * @param  name  DOCUMENT ME!
     * @param  icon  DOCUMENT ME!
     */
    public BreadCrumb(final String name, final Icon icon) {
        super(name, icon);

        listeners = new HashSet<ActionListener>();
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
        synchronized (listeners) {
            listeners.add(al);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  al  DOCUMENT ME!
     */
    public void removeActionListener(final ActionListener al) {
        synchronized (listeners) {
            listeners.remove(al);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireActionPerformed(final ActionEvent e) {
        final Iterator<ActionListener> it;
        synchronized (listeners) {
            it = new HashSet<ActionListener>(listeners).iterator();
        }

        while (it.hasNext()) {
            it.next().actionPerformed(e);
        }
    }
}
