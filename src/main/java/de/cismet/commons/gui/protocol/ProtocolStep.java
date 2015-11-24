/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

import java.util.Date;
import java.util.Set;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface ProtocolStep {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ProtocolStepMetaInfo getMetaInfo();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Set<ProtocolStepParameter> getParameters();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Date getDate();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  JsonProcessingException  com.fasterxml.jackson.core.JsonProcessingException
     */
    String toJsonString() throws JsonProcessingException;

    /**
     * DOCUMENT ME!
     *
     * @param   jsonString  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    void fromJsonString(final String jsonString) throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    AbstractProtocolStepPanel visualize();

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean addProtocolStepListener(final ProtocolStepListener listener);

    /**
     * DOCUMENT ME!
     *
     * @param   listener  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean removeProtocolStepListener(final ProtocolStepListener listener);
}
