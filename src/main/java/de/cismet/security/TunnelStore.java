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
public interface TunnelStore {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  tunnel  DOCUMENT ME!
     */
    void setTunnel(Tunnel tunnel);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Tunnel getTunnel();
}
