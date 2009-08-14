/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.security;

/**
 *
 * @author spuhl
 */
public class Proxy {
    private String host;
    private int port;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }            
}
