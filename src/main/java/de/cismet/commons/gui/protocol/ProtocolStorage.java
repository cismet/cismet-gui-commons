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

import java.util.LinkedList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolStorage {

    //~ Instance fields --------------------------------------------------------

    private final LinkedList<ProtocolStep> steps = new LinkedList<ProtocolStep>();

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   step  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean addStep(final ProtocolStep step) {
        return steps.add(step);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   steps  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean addSteps(final List<ProtocolStep> steps) {
        return this.steps.addAll(steps);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ProtocolStep> getSteps() {
        return new LinkedList<ProtocolStep>(steps);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearSteps() {
        steps.clear();
    }
}
