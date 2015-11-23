/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public abstract class AbstractProtocolStep implements ProtocolStep {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            AbstractProtocolStep.class);
    protected static final transient ObjectMapper MAPPER = new ObjectMapper();

    //~ Instance fields --------------------------------------------------------

    private final ProtocolStepMetaInfo metaInfo;

    private Date date;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractProtocolStep object.
     */
    protected AbstractProtocolStep() {
        this.metaInfo = createMetaInfo();
        this.date = new Date();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected abstract ProtocolStepMetaInfo createMetaInfo();

    @Override
    public String toJsonString() throws JsonProcessingException {
        synchronized (MAPPER) {
            return MAPPER.writeValueAsString(this);
        }
    }

    @Override
    public void fromJsonString(final String jsonString) throws IOException {
        final AbstractProtocolStep protocolStep = (AbstractProtocolStep)fromJsonString(jsonString, getClass());
        copyParams(protocolStep);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   jsonString    DOCUMENT ME!
     * @param   protocolStep  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static ProtocolStep fromJsonString(final String jsonString,
            final Class<? extends AbstractProtocolStep> protocolStep) throws IOException {
        synchronized (MAPPER) {
            return (AbstractProtocolStep)MAPPER.readValue(jsonString, protocolStep);
        }
    }

    @Override
    public abstract AbstractProtocolStepPanel visualize();

    /**
     * DOCUMENT ME!
     *
     * @param  other  DOCUMENT ME!
     */
    protected void copyParams(final AbstractProtocolStep other) {
        setDate(other.getDate());
    }
}
