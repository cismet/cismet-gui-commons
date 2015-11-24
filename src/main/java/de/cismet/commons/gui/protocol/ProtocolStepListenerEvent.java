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
public class ProtocolStepListenerEvent extends EventObject {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolStepListenerEvent object.
     *
     * @param  source  DOCUMENT ME!
     */
    public ProtocolStepListenerEvent(final ProtocolStep source) {
        super(source);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProtocolStep getProtocolStep() {
        return (ProtocolStep)getSource();
    }
}
