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

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProtocolStepMetaInfo {

    //~ Instance fields --------------------------------------------------------

    private String key;
    private String description;
    private String javaCanonicalClassName;
}
