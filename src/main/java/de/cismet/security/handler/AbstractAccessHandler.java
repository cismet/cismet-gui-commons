/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.security.handler;

import de.cismet.security.AccessHandler;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;

/**
 *
 * @author spuhl
 */
public abstract class AbstractAccessHandler implements AccessHandler{    
    public InputStream doRequest(URL url, Reader requestParameter, ACCESS_METHODS method) throws Exception {
        return doRequest(url, requestParameter, method,null);
    }     
    
    //idee methode die prüft ob ein Array/Liste von Options gesetzt ist
}
