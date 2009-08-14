/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.security.exceptions;

/**
 *
 * @author spuhl
 */
public class MissingArgumentException extends Exception{

    public MissingArgumentException(String string) {
        super(string);
    }

}
