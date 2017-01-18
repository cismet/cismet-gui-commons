/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import de.cismet.commons.gui.protocol.AbstractProtocolStep;
import de.cismet.commons.gui.protocol.AbstractProtocolStepPanel;
import de.cismet.commons.gui.protocol.ProtocolStepMetaInfo;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CommentProtocolStepImpl extends AbstractProtocolStep implements CommentProtocolStep {

    //~ Static fields/initializers ---------------------------------------------

    public static ProtocolStepMetaInfo META_INFO = new ProtocolStepMetaInfo("comment", "comment step protocol");

    //~ Instance fields --------------------------------------------------------

    @Getter
    @JsonProperty(required = true)
    private final String message;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CommentProtocolStep object.
     *
     * @param  message  DOCUMENT ME!
     */
    @JsonCreator
    public CommentProtocolStepImpl(@JsonProperty("message") final String message) {
        this.message = message;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected ProtocolStepMetaInfo createMetaInfo() {
        return META_INFO;
    }

    @Override
    public AbstractProtocolStepPanel visualize() {
        return new CommentProtocolStepPanel(this);
    }
}
