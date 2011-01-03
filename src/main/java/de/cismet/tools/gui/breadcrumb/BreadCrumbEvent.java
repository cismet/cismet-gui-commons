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
public class BreadCrumbEvent {

    //~ Instance fields --------------------------------------------------------

    private BreadCrumbModel source;
    private BreadCrumb breadCrumb;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BreadCrumbEvent object.
     *
     * @param  source  DOCUMENT ME!
     */
    public BreadCrumbEvent(final BreadCrumbModel source) {
        this.source = source;
    }

    /**
     * Creates a new BreadCrumbEvent object.
     *
     * @param  source      DOCUMENT ME!
     * @param  breadCrumb  DOCUMENT ME!
     */
    public BreadCrumbEvent(final BreadCrumbModel source, final BreadCrumb breadCrumb) {
        this.source = source;
        this.breadCrumb = breadCrumb;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BreadCrumb getBreadCrumb() {
        return breadCrumb;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  breadCrumb  DOCUMENT ME!
     */
    public void setBreadCrumb(final BreadCrumb breadCrumb) {
        this.breadCrumb = breadCrumb;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BreadCrumbModel getSource() {
        return source;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  source  DOCUMENT ME!
     */
    public void setSource(final BreadCrumbModel source) {
        this.source = source;
    }
}
