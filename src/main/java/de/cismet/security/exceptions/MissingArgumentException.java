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
public class MissingArgumentException extends Exception {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new MissingArgumentException object.
     *
     * @param  string  DOCUMENT ME!
     */
    public MissingArgumentException(final String string) {
        super(string);
    }
}
