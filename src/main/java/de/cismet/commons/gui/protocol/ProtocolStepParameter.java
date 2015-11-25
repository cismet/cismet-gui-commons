/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public class ProtocolStepParameter {

    //~ Instance fields --------------------------------------------------------

    private String key;
    private Object value;
    private String className;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolStepParameter object.
     *
     * @param  key    DOCUMENT ME!
     * @param  value  DOCUMENT ME!
     */
    public ProtocolStepParameter(final String key, final Object value) {
        setKey(key);
        setValue(value);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  value  DOCUMENT ME!
     */
    public final void setValue(final Object value) {
        this.value = value;
        if (value != null) {
            className = value.getClass().getCanonicalName();
        }
    }
}
