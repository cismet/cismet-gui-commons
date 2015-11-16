/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.impl;

import lombok.Getter;
import lombok.Setter;

import de.cismet.commons.gui.protocol.AbstractProtocolStep;
import de.cismet.commons.gui.protocol.AbstractProtocolStepPanel;
import de.cismet.commons.gui.protocol.ProtocolStepMetaInfo;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
public class CommentProtocolStep extends AbstractProtocolStep {

    //~ Instance fields --------------------------------------------------------

    private String message;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CommentProtocolStep object.
     *
     * @param  message  DOCUMENT ME!
     */
    public CommentProtocolStep(final String message) {
        this.message = message;
    }

    /**
     * Creates a new CommentProtocolStep object.
     */
    private CommentProtocolStep() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ProtocolStepMetaInfo createMetaInfo() {
        return new ProtocolStepMetaInfo(
                "comment",
                "comment step protocol",
                CommentProtocolStep.class.getCanonicalName());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getMessage() {
        return message;
    }

    @Override
    public AbstractProtocolStepPanel visualize() {
        return new CommentProtocolStepPanel(this);
    }

    @Override
    protected void copyParams(final AbstractProtocolStep other) {
        super.copyParams(other);
        final CommentProtocolStep otherCommentProtocolStep = (CommentProtocolStep)other;
        setMessage(otherCommentProtocolStep.getMessage());
    }
}
