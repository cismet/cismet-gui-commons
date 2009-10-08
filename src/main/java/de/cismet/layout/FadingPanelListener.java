package de.cismet.layout;

import java.util.EventListener;

/**
 * Listener interface for the fading panel.
 * @author jruiz
 */
public interface FadingPanelListener extends EventListener {

    /**
     * Is called when the fade animation is finished
     */
    public void fadeFinished();

}
