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
public class NoHandlerForURLException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new NoHandlerForURLException object.
     *
     * @param  message  DOCUMENT ME!
     */
    public NoHandlerForURLException(final String message) {
        super(message);
    }
}
