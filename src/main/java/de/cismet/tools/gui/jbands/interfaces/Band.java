/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface Band {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    int getNumberOfMembers();
    /**
     * DOCUMENT ME!
     *
     * @param   i  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    BandMember getMember(int i);

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
}
