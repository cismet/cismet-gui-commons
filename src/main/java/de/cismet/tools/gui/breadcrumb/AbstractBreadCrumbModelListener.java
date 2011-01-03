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

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public abstract class AbstractBreadCrumbModelListener implements BreadCrumbModelListener {

    //~ Methods ----------------------------------------------------------------

    @Override
    public void breadCrumbActionPerformed(final BreadCrumbEvent bce) {
        final BreadCrumbModel bcm = bce.getSource();
        final BreadCrumb bc = bce.getBreadCrumb();
        if ((bcm != null) & (bc != null)) {
            bcm.removeTill(bc);
        }
    }
}
