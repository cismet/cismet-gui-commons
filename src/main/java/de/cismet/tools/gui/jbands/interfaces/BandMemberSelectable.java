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
public interface BandMemberSelectable {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isSelected();
    /**
     * DOCUMENT ME!
     *
     * @param  selection  DOCUMENT ME!
     */
    void setSelected(boolean selection);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isSelectable();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    BandMember getBandMember();
}
