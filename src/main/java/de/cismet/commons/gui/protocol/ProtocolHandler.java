/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.jdom.Element;

import org.openide.util.Lookup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.cismet.tools.configuration.Configurable;
import de.cismet.tools.configuration.NoWriteError;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolHandler implements Configurable {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            ProtocolHandler.class);

    private static ProtocolHandler INSTANCE;

    //~ Instance fields --------------------------------------------------------

    private final LinkedList<ProtocolStep> storage = new LinkedList<ProtocolStep>();
    private final ProtocolHandlerListenerHandler listenerHandler = new ProtocolHandlerListenerHandler();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, ProtocolStepConfiguration> configMap = new HashMap<String, ProtocolStepConfiguration>();
    private final List<ProtocolStepToolbarItem> toolbarItems = new ArrayList<ProtocolStepToolbarItem>();

    private boolean recordEnabled = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolHandler object.
     */
    private ProtocolHandler() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        // objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final Collection<? extends ProtocolStepConfiguration> configs = Lookup.getDefault()
                    .lookupAll(ProtocolStepConfiguration.class);
        for (final ProtocolStepConfiguration config : configs) {
            final String configKey = config.getProtocolStepKey();
            if (configKey != null) {
                configMap.put(configKey, config);
            }
        }

        final Collection<? extends ProtocolStepToolbarItem> toolbarItems = Lookup.getDefault()
                    .lookupAll(ProtocolStepToolbarItem.class);
        for (final ProtocolStepToolbarItem toolbarItem : toolbarItems) {
            if (toolbarItem.isVisible()) {
                this.toolbarItems.add(toolbarItem);
            }
        }
        Collections.sort(this.toolbarItems, new Comparator<ProtocolStepToolbarItem>() {

                @Override
                public int compare(final ProtocolStepToolbarItem o1, final ProtocolStepToolbarItem o2) {
                    return o1.getSorterString().compareTo(o2.getSorterString());
                }
            });
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
     * @param   stepKey  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProtocolStepConfiguration getProtocolStepConfiguration(final String stepKey) {
        return configMap.get(stepKey);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  recordEnabled  DOCUMENT ME!
     */
    public void setRecordEnabled(final boolean recordEnabled) {
        LOG.info("protocol globally enabled: " + recordEnabled);
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
     * @param  protocolStep  DOCUMENT ME!
     */
    public void removeStep(final ProtocolStep protocolStep) {
        storage.remove(protocolStep);
        fireStepRemoved(new ProtocolHandlerListenerEvent(
                this,
                protocolStep,
                ProtocolHandlerListenerEvent.PROTOCOL_STEP_REMOVED));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   protocolStep  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean recordStep(final AbstractProtocolStep protocolStep) {
        return recordStep(protocolStep, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   protocolStep            DOCUMENT ME!
     * @param   checkIfRecordIsEnabled  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean recordStep(final AbstractProtocolStep protocolStep, final boolean checkIfRecordIsEnabled) {
        if (!checkIfRecordIsEnabled || isRecordEnabled()) {
            synchronized (storage) {
                storage.add(protocolStep);
            }
            fireStepAdded(new ProtocolHandlerListenerEvent(
                    this,
                    protocolStep,
                    ProtocolHandlerListenerEvent.PROTOCOL_STEP_ADDED));
            new Thread(new Runnable() {

                    @Override
                    public void run() {
                        protocolStep.setInited(false);
                        protocolStep.initParameters();
                        protocolStep.setInited(true);
                        if (LOG.isDebugEnabled()) {
                            LOG.debug("Parameters initialized, building GUI");
                        }
                        protocolStep.fireParametersChanged(new ProtocolStepListenerEvent(protocolStep));
                    }
                }).start();
            if (LOG.isDebugEnabled()) {
                LOG.debug("protocol step '" + protocolStep.getMetaInfo().getKey()
                            + "' added.");
            }
            return true;
        } else {
            LOG.warn("protocol step '" + protocolStep.getMetaInfo().getKey()
                        + "' not added: protocol globally  disabled!");
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ProtocolStep getLastStep() {
        return storage.getLast();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ProtocolStep> getAllSteps() {
        return new ArrayList<ProtocolStep>(storage);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<ProtocolStepToolbarItem> getToolbarItems() {
        return new ArrayList<ProtocolStepToolbarItem>(toolbarItems);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearSteps() {
        synchronized (storage) {
            storage.clear();
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug("all protocol step cleared");
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
        if (LOG.isDebugEnabled()) {
            LOG.debug("saving " + storage.size() + " protocol objects to JSON");
        }

        return objectMapper.writerWithType(new TypeReference<Collection<ProtocolStep>>() {
                }).writeValueAsString(storage);
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
        final Collection<ProtocolStep> loadedStorage = objectMapper.readValue(
                jsonString,
                new TypeReference<Collection<ProtocolStep>>() {
                });
        if (LOG.isDebugEnabled()) {
            LOG.debug(loadedStorage.size() + " protocol objects restored from JSON");
        }
        synchronized (storage) {
            storage.clear();
            storage.addAll(loadedStorage);
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
    protected void fireStepRemoved(final ProtocolHandlerListenerEvent event) {
        listenerHandler.stepRemoved(event);
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

    @Override
    public void configure(final Element parent) {
        final Element root = parent.getChild("protocolHandler");
        final Element steps = root.getChild("protocolSteps");
        for (final String key : configMap.keySet()) {
            try {
                final ProtocolStepConfiguration config = configMap.get(key);
                config.configure(steps);
            } catch (final Exception ex) {
                LOG.warn("error while configuration of " + key, ex);
            }
        }
    }

    @Override
    public void masterConfigure(final Element parent) {
        configure(parent);
    }

    @Override
    public Element getConfiguration() throws NoWriteError {
        final Element root = new Element("protocolHandler");
        final Element steps = new Element("protocolSteps");

        root.addContent(steps);

        for (final Configurable config : configMap.values()) {
            try {
                final Element element = config.getConfiguration();
                if (element != null) {
                    steps.addContent(element);
                }
            } catch (final Exception t) {
                LOG.warn("error while writing config part", t); // NOI18N
            }
        }
        return root;
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
        public void stepRemoved(final ProtocolHandlerListenerEvent event) {
            for (final ProtocolHandlerListener listener : listeners) {
                listener.stepRemoved(event);
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
