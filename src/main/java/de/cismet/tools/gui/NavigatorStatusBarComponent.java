/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Component;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface NavigatorStatusBarComponent {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    enum Side {

        //~ Enum constants -----------------------------------------------------

        LEFT, RIGHT
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Component getComponent();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    double getWeight();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Side getSide();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isVisibleInStatusBar();

    /**
     * DOCUMENT ME!
     */
    void initialize();
}
