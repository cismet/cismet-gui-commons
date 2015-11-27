/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.impl;

import com.fasterxml.jackson.annotation.JsonProperty;

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
public class CommentProtocolStep extends AbstractProtocolStep {

    //~ Instance fields --------------------------------------------------------

    @Getter
    @Setter
    @JsonProperty(required = true)
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
                "comment step protocol");
    }

    @Override
    public AbstractProtocolStepPanel visualize() {
        return new CommentProtocolStepPanel(this);
    }
}
