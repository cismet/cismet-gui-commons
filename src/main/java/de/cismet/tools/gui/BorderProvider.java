/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui;

import javax.swing.border.Border;

/**
 *
 * @author srichter
 */
public interface BorderProvider {

    Border getTitleBorder();
    Border getFooterBorder();
    Border getCenterrBorder();

}
