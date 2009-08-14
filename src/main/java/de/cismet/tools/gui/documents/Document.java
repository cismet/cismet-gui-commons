/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui.documents;

import java.awt.Image;
import java.net.URI;
import javax.swing.Icon;

/**
 *
 * @author hell
 */
public interface Document {
    public Icon getIcon();
    public String getName();
    public Image getPreview(int width, int height);
    public String getDocumentURI();
}
