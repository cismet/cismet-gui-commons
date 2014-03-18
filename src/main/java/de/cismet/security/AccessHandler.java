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

import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.HashMap;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public interface AccessHandler {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum ACCESS_METHODS {

        //~ Enum constants -----------------------------------------------------

        POST_REQUEST, GET_REQUEST, POST_REQUEST_NO_TUNNEL, GET_REQUEST_NO_TUNNEL, HEAD_REQUEST, HEAD_REQUEST_NO_TUNNEL
    }
    /**
     * todo ein handler könnte mehr als einen Typ verarbeiten.
     *
     * @version  $Revision$, $Date$
     */
    public enum ACCESS_HANDLER_TYPES {

        //~ Enum constants -----------------------------------------------------

        WSS, HTTP, SOAP, SANY
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   method  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isAccessMethodSupported(ACCESS_METHODS method);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    ACCESS_HANDLER_TYPES getHandlerType();
    /**
     * todo idee default dorequest ohne accessMethod jeder handler entscheided selbst wie der default fall aussieht.
     *
     * @param   url               DOCUMENT ME!
     * @param   requestParameter  DOCUMENT ME!
     * @param   method            DOCUMENT ME!
     * @param   options           DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    InputStream doRequest(URL url,
            Reader requestParameter,
            AccessHandler.ACCESS_METHODS method,
            HashMap<String, String> options) throws Exception;

    /**
     * Send binary data in a POST request.
     *
     * @param   url               The URL where the data is sent to.
     * @param   requestParameter  The payload.
     * @param   options           The headers to add to the POST request.
     *
     * @return  The response.
     *
     * @throws  Exception  DOCUMENT ME!
     */
    InputStream doRequest(URL url,
            InputStream requestParameter, HashMap<String, String> options) throws Exception;
}
