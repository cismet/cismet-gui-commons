/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.jbands.interfaces;

/**
 * This interface can be used by bands, which should do some clean up activities when
 * the band is not used anymore.
 * 
 * @author therter
 */
public interface DisposableBand {
    
    /**
     * This method will be invoked by its parent JBand, when it is disposed.
     * 
     */
    public void dispose();
}
