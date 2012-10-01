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
package de.cismet.security;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public interface Tunnel extends AccessHandler {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   method  DOCUMENT ME!
     * @param   url     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isResponsible(AccessHandler.ACCESS_METHODS method, String url);
}
