/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import java.util.EventListener;

/**
 * EventListener interface for {@link EqualizerModel} events.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public interface EqualizerModelListener extends EventListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * Callback which shall be called whenever a change happened in the 
     * <code>EqualizerModel</code>. The 
     * <code>EqualizerModelEvent</code> shall provide additional information about the nature of the change.
     *
     * @param  event  the event object
     */
    void equalizerChanged(final EqualizerModelEvent event);
}
