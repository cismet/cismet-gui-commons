/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import de.cismet.commons.gui.protocol.impl.DummyStep;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolHandler {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            ProtocolHandler.class);

    private static ProtocolHandler INSTANCE;

    //~ Instance fields --------------------------------------------------------

    private final ProtocolStorage storage = new ProtocolStorage();
    private final ProtocolHandlerListenerHandler listenerHandler = new ProtocolHandlerListenerHandler();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private boolean recordEnabled = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolHandler object.
     */
    private ProtocolHandler() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        final SimpleModule module = new SimpleModule(
                "PolymorphicProtocolStepDeserializerModule",
                new Version(1, 0, 0, null, null, null));
        module.addSerializer(ProtocolStep.class, new ProtocolStepSerializer());
        module.addSerializer(ProtocolStepParameter.class, new ProtocolStepParameterSerializer());
        module.addDeserializer(ProtocolStep.class, new ProtocolStepDeserializer());
        module.addDeserializer(ProtocolStepParameter.class, new ProtocolStepParameterDeserializer());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        objectMapper.registerModule(module);
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
    public boolean recordStep(final AbstractProtocolStep protocolStep) {
        if (isRecordEnabled()) {
            synchronized (storage) {
                storage.addStep(protocolStep);
            }
            fireStepAdded(new ProtocolHandlerListenerEvent(this, ProtocolHandlerListenerEvent.PROTOCOL_STEP_ADDED));
            new Thread(new Runnable() {

                    @Override
                    public void run() {
                        protocolStep.setParameters(protocolStep.createParameters());
                    }
                }).start();
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
        return ((LinkedList<ProtocolStep>)storage.getSteps()).getLast();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ProtocolStep> getAllSteps() {
        return new ArrayList<ProtocolStep>(storage.getSteps());
    }

    /**
     * DOCUMENT ME!
     */
    public void clearSteps() {
        synchronized (storage) {
            storage.clearSteps();
        }
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
        return objectMapper.writeValueAsString(storage);
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
        final ProtocolStorage newStorage = (ProtocolStorage)objectMapper.readValue(jsonString, ProtocolStorage.class);
        synchronized (storage) {
            storage.clearSteps();
            storage.addSteps(newStorage.getSteps());
        }

        fireStepsRestored(new ProtocolHandlerListenerEvent(this, ProtocolHandlerListenerEvent.PROTOCOL_STEPS_RESTORED));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   file  DOCUMENT ME!
     *
     * @throws  IOException             DOCUMENT ME!
     * @throws  ClassNotFoundException  DOCUMENT ME!
     */
    public void readFromFile(final File file) throws IOException, ClassNotFoundException {
        final BufferedReader br = new BufferedReader(new FileReader(file));
        try {
            final StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            fromJsonString(sb.toString());
        } finally {
            br.close();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   file  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public void writeToFile(final File file) throws IOException {
        final FileWriter fw = new FileWriter(file);
        fw.write(toJsonString());
        fw.close();
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

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ProtocolStepDeserializer extends StdDeserializer<ProtocolStep> {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProtocolStepDeserializer object.
         */
        ProtocolStepDeserializer() {
            super(ProtocolStep.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public ProtocolStep deserialize(final JsonParser jp, final DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
            final ObjectMapper mapper = (ObjectMapper)jp.getCodec();
            final ObjectNode root = (ObjectNode)mapper.readTree(jp);
            try {
                final DummyStep dummyStep = mapper.readValue(root.toString(), DummyStep.class);
                final String stepClassString = dummyStep.getMetaInfo().getJavaCanonicalClassName();
                final Class stepClass = Class.forName(stepClassString);
                final AbstractProtocolStep step = (AbstractProtocolStep)stepClass.newInstance();
                step.setDate(dummyStep.getDate());
                step.setMetaInfo(dummyStep.getMetaInfo());
                step.setParameters(dummyStep.getParameters());
                return step;
            } catch (final Exception ex) {
                LOG.error("error while deserializing step", ex);
                return null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ProtocolStepSerializer extends StdSerializer<ProtocolStep> {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProtocolStepSerializer object.
         */
        ProtocolStepSerializer() {
            super(ProtocolStep.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void serialize(final ProtocolStep step, final JsonGenerator jg, final SerializerProvider sp)
                throws IOException, JsonGenerationException {
            jg.writeStartObject();
            jg.writeObjectField("date", step.getDate());
            jg.writeObjectField("metaInfo", step.getMetaInfo());
            jg.writeObjectField("parameters", step.getParameters());
            jg.writeEndObject();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ProtocolStepParameterSerializer extends StdSerializer<ProtocolStepParameter> {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProtocolStepSerializer object.
         */
        ProtocolStepParameterSerializer() {
            super(ProtocolStepParameter.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void serialize(final ProtocolStepParameter param, final JsonGenerator jg, final SerializerProvider sp)
                throws IOException, JsonGenerationException {
            jg.writeStartObject();
            jg.writeObjectField("key", param.getKey());
            jg.writeObjectField("value", param.getValue());
            jg.writeObjectField("className", param.getClassName());
            jg.writeEndObject();
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class ProtocolStepParameterDeserializer extends StdDeserializer<ProtocolStepParameter> {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new ProtocolStepParameterDeserializer object.
         */
        ProtocolStepParameterDeserializer() {
            super(ProtocolStepParameter.class);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public ProtocolStepParameter deserialize(final JsonParser jp, final DeserializationContext dc)
                throws IOException, JsonProcessingException {
            final ObjectMapper mapper = (ObjectMapper)jp.getCodec();
            final ObjectNode root = (ObjectNode)mapper.readTree(jp);
            try {
                final String key = root.get("key").textValue();
                final String className = root.get("className").textValue();

                final Class paramValueClass = Class.forName(className);
                final Object valueObject = mapper.readValue(root.get("value").toString(), paramValueClass);
                return new ProtocolStepParameter(key, valueObject);
            } catch (final Exception ex) {
                LOG.error("error while deserializing parameter", ex);
                return null;
            }
        }
    }
}
