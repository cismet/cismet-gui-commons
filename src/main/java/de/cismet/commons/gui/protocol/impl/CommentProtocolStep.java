/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.impl;

import java.util.HashSet;
import java.util.Set;

import de.cismet.commons.gui.protocol.AbstractProtocolStep;
import de.cismet.commons.gui.protocol.AbstractProtocolStepPanel;
import de.cismet.commons.gui.protocol.ProtocolStepMetaInfo;
import de.cismet.commons.gui.protocol.ProtocolStepParameter;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CommentProtocolStep extends AbstractProtocolStep {

    //~ Instance fields --------------------------------------------------------

    private String message;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CommentProtocolStep object.
     */
    public CommentProtocolStep() {
    }

    /**
     * Creates a new CommentProtocolStep object.
     *
     * @param  message  DOCUMENT ME!
     */
    public CommentProtocolStep(final String message) {
        this.message = message;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ProtocolStepMetaInfo createMetaInfo() {
        return new ProtocolStepMetaInfo(
                "comment",
                "comment step protocol",
                CommentProtocolStep.class.getCanonicalName());
    }

    @Override
    public AbstractProtocolStepPanel visualize() {
        return new CommentProtocolStepPanel(this);
    }

    @Override
    public Set<ProtocolStepParameter> createParameters() {
        final Set<ProtocolStepParameter> parameters = new HashSet<ProtocolStepParameter>();
        parameters.add(new ProtocolStepParameter("message", message));
        return parameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getMessage() {
        return (String)getParameterValue("message");
    }
}
