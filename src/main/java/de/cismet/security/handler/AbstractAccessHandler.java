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
package de.cismet.security.handler;

import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.HashMap;

import de.cismet.security.AccessHandler;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public abstract class AbstractAccessHandler implements AccessHandler {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   method            DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public InputStream doRequest(final URL url, final Reader requestParameter, final ACCESS_METHODS method)
            throws Exception {
        return doRequest(url, requestParameter, method, null);
    }

    // idee methode die prüft ob ein Array/Liste von Options gesetzt ist
}
