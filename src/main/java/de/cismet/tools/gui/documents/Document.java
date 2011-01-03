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
package de.cismet.tools.gui.documents;

import java.awt.Image;

import java.net.URI;

import javax.swing.Icon;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public interface Document {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Icon getIcon();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getName();
    /**
     * DOCUMENT ME!
     *
     * @param   width   DOCUMENT ME!
     * @param   height  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Image getPreview(int width, int height);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getDocumentURI();
}
