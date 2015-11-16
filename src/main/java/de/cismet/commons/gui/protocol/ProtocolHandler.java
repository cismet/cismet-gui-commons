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
     * @param  recordEnabled  DOCUMENT ME!
     */
    public void setRecordEnabled(final boolean recordEnabled) {
        this.recordEnabled = recordEnabled;
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
    public void recordStep(final ProtocolStep protocolStep) {
        protocolStepList.add(protocolStep);
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
    }
}
