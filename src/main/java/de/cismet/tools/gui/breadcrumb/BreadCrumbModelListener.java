/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.breadcrumb;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BreadCrumbModelListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    void breadCrumbModelChanged(BreadCrumbEvent bce);
    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    void breadCrumbAdded(BreadCrumbEvent bce);
    /**
     * DOCUMENT ME!
     *
     * @param  bce  DOCUMENT ME!
     */
    void breadCrumbActionPerformed(BreadCrumbEvent bce);
}
