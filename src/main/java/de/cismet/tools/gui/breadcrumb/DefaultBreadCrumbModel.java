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

import java.util.List;
import java.util.Vector;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class DefaultBreadCrumbModel implements BreadCrumbModel {

    //~ Instance fields --------------------------------------------------------

    private Vector<BreadCrumbModelListener> listeners = new Vector<BreadCrumbModelListener>();
    private Vector<BreadCrumb> data = new Vector<BreadCrumb>();

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addBreadCrumbModelListener(final BreadCrumbModelListener bcListener) {
        listeners.add(bcListener);
    }

    @Override
    public void appendCrumb(final BreadCrumb bc) {
        appendCrumbSilently(bc);
        fireBreadCrumbAdded(new BreadCrumbEvent(this, bc));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bc  DOCUMENT ME!
     */
    private void appendCrumbSilently(final BreadCrumb bc) {
        data.add(bc);
        bc.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    final BreadCrumbEvent bce = new BreadCrumbEvent(DefaultBreadCrumbModel.this, bc);
                    fireBreadCrumbActionPerformed(bce);
                }
            });
    }

    @Override
    public List<BreadCrumb> getAllCrumbs() {
        return new Vector<BreadCrumb>(data);
    }

    @Override
    public BreadCrumb getCrumbAt(final int position) {
        return data.get(position);
    }

    @Override
    public int getPositionOf(final BreadCrumb bc) {
        return data.indexOf(bc);
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public BreadCrumb getLastCrumb() {
        return data.lastElement();
    }

    @Override
    public BreadCrumb getFirstCrumb() {
        return data.firstElement();
    }

    @Override
    public void clear() {
        data.clear();
    }

    @Override
    public void removeBreadCrumbModelListener(final BreadCrumbModelListener bcListener) {
        listeners.remove(bcListener);
    }

    @Override
    public void removeTill(final BreadCrumb bc) {
        final int lastIndex = data.lastIndexOf(bc);
        if (lastIndex != -1) {
            for (int i = data.size() - 1; i > lastIndex; --i) {
                data.remove(i);
            }
        }
        fireBreadCrumbModelChanged(new BreadCrumbEvent(this));
    }

    @Override
    public void startWithNewCrumb(final BreadCrumb bc) {
        data = new Vector<BreadCrumb>();
        appendCrumbSilently(bc);
        fireBreadCrumbModelChanged(new BreadCrumbEvent(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    public void fireBreadCrumbModelChanged(final BreadCrumbEvent bce) {
        for (final BreadCrumbModelListener listener : new Vector<BreadCrumbModelListener>(listeners)) {
            listener.breadCrumbModelChanged(bce);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    public void fireBreadCrumbAdded(final BreadCrumbEvent bce) {
        for (final BreadCrumbModelListener listener : new Vector<BreadCrumbModelListener>(listeners)) {
            listener.breadCrumbAdded(bce);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    public void fireBreadCrumbActionPerformed(final BreadCrumbEvent bce) {
        for (final BreadCrumbModelListener listener : new Vector<BreadCrumbModelListener>(listeners)) {
            listener.breadCrumbActionPerformed(bce);
        }
    }
}
