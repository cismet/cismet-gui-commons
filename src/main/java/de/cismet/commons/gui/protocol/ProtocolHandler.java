/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolHandler {

    //~ Static fields/initializers ---------------------------------------------

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            ProtocolHandler.class);

    private static ProtocolHandler INSTANCE;

    //~ Instance fields --------------------------------------------------------

    private boolean recordEnabled = false;
    private final LinkedList<ProtocolStep> protocolStepList = new LinkedList<ProtocolStep>();
    private final ProtocolHandlerListenerHandler listenerHandler = new ProtocolHandlerListenerHandler();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolHandler object.
     */
    private ProtocolHandler() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static ProtocolHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProtocolHandler();
        }
        return INSTANCE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean addProtocolHandlerListener(final ProtocolHandlerListener listener) {
        return listenerHandler.addProtocolHandlerListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean removeProtocolHandlerListener(final ProtocolHandlerListener listener) {
        return listenerHandler.removeProtocolHandlerListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  recordEnabled  DOCUMENT ME!
     */
    public void setRecordEnabled(final boolean recordEnabled) {
        this.recordEnabled = recordEnabled;
        fireRecordStateChanged(new ProtocolHandlerListenerEvent(
                this,
                ProtocolHandlerListenerEvent.PROTOCOL_RECORD_STATE));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isRecordEnabled() {
        return recordEnabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   protocolStep  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean recordStep(final ProtocolStep protocolStep) {
        if (isRecordEnabled()) {
            protocolStepList.add(protocolStep);
            fireStepAdded(new ProtocolHandlerListenerEvent(this, ProtocolHandlerListenerEvent.PROTOCOL_STEP_ADDED));
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProtocolStep getLastStep() {
        return protocolStepList.getLast();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ProtocolStep> getAllSteps() {
        return new ArrayList<ProtocolStep>(protocolStepList);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearSteps() {
        protocolStepList.clear();
        fireStepsCleared(new ProtocolHandlerListenerEvent(this, ProtocolHandlerListenerEvent.PROTOCOL_STEPS_CLEARED));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JsonProcessingException  DOCUMENT ME!
     */
    public String toJsonString() throws JsonProcessingException {
        return MAPPER.writeValueAsString(protocolStepList);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   jsonString  DOCUMENT ME!
     *
     * @throws  IOException             DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    public void fromJsonString(final String jsonString) throws IOException, ClassNotFoundException {
        final List<ProtocolStep> newSteps = new ArrayList<ProtocolStep>();
        final List<HashMap> list = (List)MAPPER.readValue(jsonString, List.class);
        for (final HashMap<String, Object> item : list) {
            final String metaInfoJson = MAPPER.writeValueAsString(item.get("metaInfo"));
            final ProtocolStepMetaInfo metaInfo = MAPPER.readValue(metaInfoJson, ProtocolStepMetaInfo.class);
            final String javaCanonicalClassName = metaInfo.getJavaCanonicalClassName();
            final Class stepClass = Class.forName(javaCanonicalClassName);

            final String itemJson = MAPPER.writeValueAsString(item);
            newSteps.add((ProtocolStep)MAPPER.readValue(itemJson, stepClass));
        }

        protocolStepList.clear();
        protocolStepList.addAll(newSteps);

        fireStepsRestored(new ProtocolHandlerListenerEvent(this, ProtocolHandlerListenerEvent.PROTOCOL_STEPS_RESTORED));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void fireRecordStateChanged(final ProtocolHandlerListenerEvent event) {
        listenerHandler.recordStateChanged(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void fireStepAdded(final ProtocolHandlerListenerEvent event) {
        listenerHandler.stepAdded(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void fireStepsCleared(final ProtocolHandlerListenerEvent event) {
        listenerHandler.stepsCleared(event);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  event  DOCUMENT ME!
     */
    protected void fireStepsRestored(final ProtocolHandlerListenerEvent event) {
        listenerHandler.stepsRestored(event);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ProtocolHandlerListenerHandler implements ProtocolHandlerListener {

        //~ Instance fields ----------------------------------------------------

        private final Collection<ProtocolHandlerListener> listeners = new ArrayList<ProtocolHandlerListener>();

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param   listener  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean addProtocolHandlerListener(final ProtocolHandlerListener listener) {
            return listeners.add(listener);
        }

        /**
         * DOCUMENT ME!
         *
         * @param   listener  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean removeProtocolHandlerListener(final ProtocolHandlerListener listener) {
            return listeners.remove(listener);
        }

        @Override
        public void recordStateChanged(final ProtocolHandlerListenerEvent event) {
            for (final ProtocolHandlerListener listener : listeners) {
                listener.recordStateChanged(event);
            }
        }

        @Override
        public void stepAdded(final ProtocolHandlerListenerEvent event) {
            for (final ProtocolHandlerListener listener : listeners) {
                listener.stepAdded(event);
            }
        }

        @Override
        public void stepsCleared(final ProtocolHandlerListenerEvent event) {
            for (final ProtocolHandlerListener listener : listeners) {
                listener.stepsCleared(event);
            }
        }

        @Override
        public void stepsRestored(final ProtocolHandlerListenerEvent event) {
            for (final ProtocolHandlerListener listener : listeners) {
                listener.stepsRestored(event);
            }
        }
    }
}
