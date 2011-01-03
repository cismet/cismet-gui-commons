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
package de.cismet.security.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   spuhl
 * @version  $Revision$, $Date$
 */
public class RequestFailedException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RequestFailedException object.
     *
     * @param  string  DOCUMENT ME!
     * @param  ex      DOCUMENT ME!
     */
    public RequestFailedException(final String string, final Exception ex) {
        super(string, ex);
    }
}
