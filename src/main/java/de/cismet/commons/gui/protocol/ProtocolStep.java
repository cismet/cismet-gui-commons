/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Date;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@JsonAutoDetect(
    fieldVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
// Jackson Polymorphic type handling
// FIXME: implement customized type handler to avoid the need for java class named in JSON
// see https://www.thomaskeller.biz/blog/2013/09/10/custom-polymorphic-type-handling-with-jackson/
// and http://stackoverflow.com/questions/31665620/is-jacksons-jsonsubtypes-still-necessary-for-polymorphic-deserialization
@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS,
    include = JsonTypeInfo.As.PROPERTY,
    property = "@javatype"
)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface ProtocolStep {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @JsonProperty(required = true)
    ProtocolStepMetaInfo getMetaInfo();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @JsonProperty(required = true)
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
     * @return  DOCUMENT ME!
     */
    ProtocolStepPanel visualize();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isInited();

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
