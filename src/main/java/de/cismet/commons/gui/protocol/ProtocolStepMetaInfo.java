/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@XmlRootElement
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolStepMetaInfo {

    //~ Instance fields --------------------------------------------------------

    private String key;
    private String description;
}
