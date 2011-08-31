/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.breadcrumb;

import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BreadCrumbModel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  bcListener  DOCUMENT ME!
     */
    void addBreadCrumbModelListener(BreadCrumbModelListener bcListener);
    /**
     * DOCUMENT ME!
     *
     * @param  bcListener  DOCUMENT ME!
     */
    void removeBreadCrumbModelListener(BreadCrumbModelListener bcListener);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getSize();
    /**
     * DOCUMENT ME!
     *
     * @param   position  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    BreadCrumb getCrumbAt(int position);
    /**
     * DOCUMENT ME!
     *
     * @param   bc  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getPositionOf(BreadCrumb bc);
    /**
     * DOCUMENT ME!
     *
     * @param  bc  DOCUMENT ME!
     */
    void appendCrumb(BreadCrumb bc);
    /**
     * DOCUMENT ME!
     *
     * @param  bc  DOCUMENT ME!
     */
    void startWithNewCrumb(BreadCrumb bc);
    /**
     * DOCUMENT ME!
     *
     * @param  bc  DOCUMENT ME!
     */
    void removeTill(BreadCrumb bc);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    List<BreadCrumb> getAllCrumbs();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    BreadCrumb getLastCrumb();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    BreadCrumb getFirstCrumb();
    /**
     * DOCUMENT ME!
     */
    void clear();
}
