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
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface ModifiableBandMember extends BandMember {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addBandMemberListener(BandMemberListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void removeBandMemberListener(BandMemberListener listener);
}
