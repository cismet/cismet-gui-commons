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

import java.util.EventObject;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolHandlerListenerEvent extends EventObject {

    //~ Static fields/initializers ---------------------------------------------

    public static int PROTOCOL_RECORD_STATE = 0;
    public static int PROTOCOL_STEP_ADDED = 1;
    public static int PROTOCOL_STEPS_CLEARED = 2;
    public static int PROTOCOL_STEPS_RESTORED = 3;

    //~ Instance fields --------------------------------------------------------

    private final int id;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolHandlerListenerEvent object.
     *
     * @param  source  DOCUMENT ME!
     * @param  id      DOCUMENT ME!
     */
    public ProtocolHandlerListenerEvent(final ProtocolHandler source, final int id) {
        super(source);
        this.id = id;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isRecordStateChanged() {
        return id == PROTOCOL_RECORD_STATE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isStepAdded() {
        return id == PROTOCOL_STEP_ADDED;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isStepsCleared() {
        return id == PROTOCOL_STEPS_CLEARED;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isStepsRestored() {
        return id == PROTOCOL_STEPS_RESTORED;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProtocolHandler getSourceProtocolHander() {
        return (ProtocolHandler)getSource();
    }
}
