/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui;

import javax.swing.border.Border;

/**
 * DOCUMENT ME!
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public interface BorderProvider {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Border getTitleBorder();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Border getFooterBorder();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Border getCenterrBorder();
}
