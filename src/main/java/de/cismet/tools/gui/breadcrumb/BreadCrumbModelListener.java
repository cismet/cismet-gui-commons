/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui.breadcrumb;

/**
 *
 * @author thorsten
 */
public interface BreadCrumbModelListener {
    public void breadCrumbModelChanged(BreadCrumbEvent bce);
    public void breadCrumbAdded(BreadCrumbEvent bce);
    public void breadCrumbActionPerformed(BreadCrumbEvent bce);

}
