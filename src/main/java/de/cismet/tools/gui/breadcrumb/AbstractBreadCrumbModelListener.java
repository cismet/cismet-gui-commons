/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.breadcrumb;

/**
 *
 * @author thorsten
 */
public abstract class AbstractBreadCrumbModelListener implements BreadCrumbModelListener {

    @Override
    public void breadCrumbActionPerformed(BreadCrumbEvent bce) {
        BreadCrumbModel bcm = bce.getSource();
        BreadCrumb bc = bce.getBreadCrumb();
        if (bcm != null & bc != null) {
            bcm.removeTill(bc);
        }
    }
}
