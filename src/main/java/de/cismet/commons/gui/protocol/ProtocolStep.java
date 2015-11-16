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
}
