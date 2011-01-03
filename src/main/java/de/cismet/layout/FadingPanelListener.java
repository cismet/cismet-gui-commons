/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.layout;

import java.util.EventListener;

/**
 * Listener interface for the fading panel.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface FadingPanelListener extends EventListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Is called when the fade animation is finished.
     */
    void fadeFinished();
}
