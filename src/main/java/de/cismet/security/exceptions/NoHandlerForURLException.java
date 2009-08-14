/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.security.exceptions;

/**
 *
 * @author spuhl
 */
public class NoHandlerForURLException extends Exception{
    public NoHandlerForURLException(String message) {
        super(message);
    }    
}
