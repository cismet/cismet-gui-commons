/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.security.exceptions;

/**
 *
 * @author spuhl
 */
public class RequestFailedException extends Exception{

    public RequestFailedException(String string, Exception ex) {
        super(string, ex);
    }

}
