/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface BandModificationProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * the end station can also be null.
     *
     * @param  startStation  DOCUMENT ME!
     * @param  endStation    DOCUMENT ME!
     * @param  minStart      DOCUMENT ME!
     * @param  maxEnd        DOCUMENT ME!
     * @param  members       DOCUMENT ME!
     */
    void addMember(Double startStation, Double endStation, Double minStart, Double maxEnd, List<BandMember> members);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addBandListener(BandListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void removeBandListener(BandListener listener);
}
