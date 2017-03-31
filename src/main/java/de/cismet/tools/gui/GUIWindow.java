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
package de.cismet.tools.gui;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public interface GUIWindow {

    //~ Instance fields --------------------------------------------------------

    String NO_PERMISSION = "NoPermissionRequired";

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getViewTitle();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getViewIcon();

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    JComponent getGuiComponent();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getPermissionString();
}
