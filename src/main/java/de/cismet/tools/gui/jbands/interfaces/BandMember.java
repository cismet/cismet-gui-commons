/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BandMember {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    double getMin();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    double getMax();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JComponent getBandMemberComponent();
}
