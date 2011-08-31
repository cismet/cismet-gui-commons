/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.breadcrumb;

import java.util.EventObject;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class BreadCrumbEvent extends EventObject {

    //~ Instance fields --------------------------------------------------------

    private BreadCrumb breadCrumb;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BreadCrumbEvent object.
     *
     * @param  source  DOCUMENT ME!
     */
    public BreadCrumbEvent(final BreadCrumbModel source) {
        super(source);
    }

    /**
     * Creates a new BreadCrumbEvent object.
     *
     * @param  source      DOCUMENT ME!
     * @param  breadCrumb  DOCUMENT ME!
     */
    public BreadCrumbEvent(final BreadCrumbModel source, final BreadCrumb breadCrumb) {
        super(source);

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
     * @return  DOCUMENT ME!
     */
    @Override
    public BreadCrumbModel getSource() {
        return (BreadCrumbModel)source;
    }
}
