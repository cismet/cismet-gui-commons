/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

import java.awt.event.MouseEvent;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface StationaryBandMemberMouseListeningComponent extends BandMemberMouseListeningComponent {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  e        DOCUMENT ME!
     * @param  station  DOCUMENT ME!
     */
    void mouseDragged(MouseEvent e, double station);
}
