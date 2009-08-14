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
 *
 * @author spuhl
 */
public interface AccessHandler {
    public static enum  ACCESS_METHODS{POST_REQUEST,GET_REQUEST};
    //todo ein handler könnte mehr als einen Typ verarbeiten
    public enum ACCESS_HANDLER_TYPES{WSS,HTTP,SOAP,SANY};
    public boolean isAccessMethodSupported(ACCESS_METHODS method);
    public ACCESS_HANDLER_TYPES getHandlerType();
    //todo idee default dorequest ohne accessMethod jeder handler entscheided selbst wie der default fall aussieht        
    public InputStream doRequest(URL url, Reader requestParameter, AccessHandler.ACCESS_METHODS method,HashMap<String,String> options) throws Exception;
}
