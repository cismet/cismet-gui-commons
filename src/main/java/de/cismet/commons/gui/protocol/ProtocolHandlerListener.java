/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.commons.gui.protocol;

import java.util.EventListener;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface ProtocolHandlerListener extends EventListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void recordStateChanged(final ProtocolHandlerListenerEvent event);

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void stepAdded(final ProtocolHandlerListenerEvent event);

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void stepsCleared(final ProtocolHandlerListenerEvent event);

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    void stepsRestored(final ProtocolHandlerListenerEvent event);
}
