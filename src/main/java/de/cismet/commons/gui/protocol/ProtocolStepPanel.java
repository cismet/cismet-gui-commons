/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import java.awt.Component;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public interface ProtocolStepPanel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getMainComponent();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getIconComponent();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getTitleComponent();
}
