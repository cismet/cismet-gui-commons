/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
public class ProtocolStepMetaInfo {

    //~ Instance fields --------------------------------------------------------

    @JsonProperty(required = true)
    private final String key;
    @JsonProperty(required = true)
    private final String description;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolStepMetaInfo object.
     *
     * @param  key          DOCUMENT ME!
     * @param  description  DOCUMENT ME!
     */
    @JsonCreator
    public ProtocolStepMetaInfo(@JsonProperty("key") final String key,
            @JsonProperty("description") final String description) {
        this.key = key;
        this.description = description;
    }
}
