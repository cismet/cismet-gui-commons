/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
public abstract class AbstractProtocolStep implements ProtocolStep {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            AbstractProtocolStep.class);
    protected static final transient ObjectMapper MAPPER = new ObjectMapper();

    //~ Instance fields --------------------------------------------------------

    @JsonProperty(required = true)
    private ProtocolStepMetaInfo metaInfo;

    @JsonProperty(required = true)
    private Date date;

    @Getter(AccessLevel.PRIVATE)
    private final transient ProtocolStepListenerHandler listenerHandler = new ProtocolStepListenerHandler();

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
     * Is called before visualize() is called. E.g constructut and set serializable object properties (those annotated
     * with @JsonProperty)
     */
    public void initParameters() {
    }

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
    public AbstractProtocolStep fromJsonString(final String jsonString) throws IOException {
        final AbstractProtocolStep protocolStep = (AbstractProtocolStep)fromJsonString(jsonString, getClass());
        return protocolStep;
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

    @Override
    public boolean addProtocolStepListener(final ProtocolStepListener listener) {
        return listenerHandler.addProtocolStepListener(listener);
    }

    @Override
    public boolean removeProtocolStepListener(final ProtocolStepListener listener) {
        return listenerHandler.removeProtocolStepListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void fireParametersChanged(final ProtocolStepListenerEvent event) {
        listenerHandler.parametersChanged(event);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ProtocolStepListenerHandler implements ProtocolStepListener {

        //~ Instance fields ----------------------------------------------------

        private final Collection<ProtocolStepListener> listeners = new ArrayList<ProtocolStepListener>();

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   listener  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean addProtocolStepListener(final ProtocolStepListener listener) {
            return listeners.add(listener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   listener  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean removeProtocolStepListener(final ProtocolStepListener listener) {
            return listeners.remove(listener);
        }

        @Override
        public void parametersChanged(final ProtocolStepListenerEvent event) {
            for (final ProtocolStepListener listener : listeners) {
                listener.parametersChanged(event);
            }
        }
    }
}
