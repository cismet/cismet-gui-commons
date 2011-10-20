/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands.interfaces;

import java.awt.event.ActionListener;

import javax.swing.Action;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface BandMemberActionProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Action getAction();
    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    void addActionListener(ActionListener listener);
}
