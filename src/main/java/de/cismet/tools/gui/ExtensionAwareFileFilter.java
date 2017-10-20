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

import javax.swing.filechooser.FileFilter;

/**
 * This file filter knows its extension. This can be used to add the extension to the file, if a file name without
 * extension was choosed.
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public abstract class ExtensionAwareFileFilter extends FileFilter {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the allowed file extension without starting point.
     */
    public abstract String getExtension();
}
