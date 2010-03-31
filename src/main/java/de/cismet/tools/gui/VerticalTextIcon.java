/*
 * VerticalTextIcon.java
 * Copyright (C) 2005 by:
 *
 *----------------------------
 * cismet GmbH
 * Goebenstrasse 40
 * 66117 Saarbruecken
 * http://www.cismet.de
 *----------------------------
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *----------------------------
 * Author:
 * thorsten.hell@cismet.de
 * This is code from Santhosh Kumar's Weblog 
 * santhosh@in.fiorano.com
 * All honors to him
 *----------------------------
 *
 * Created on 17. Februar 2006, 09:29
 *
 */

package de.cismet.tools.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * 
 */
public class VerticalTextIcon implements Icon, SwingConstants{
    private Font font = UIManager.getFont("Label.font");  //NOI18N
    private FontMetrics fm = Toolkit.getDefaultToolkit().getFontMetrics(font);
    
    private String text;
    private int width, height;
    private boolean clockwize;
    private Color color;
     public VerticalTextIcon(String text, boolean clockwize,Color color){
        this.text = text;
        width = SwingUtilities.computeStringWidth(fm, text)+2;
        height = fm.getHeight();
        this.clockwize = clockwize;
        this.color=color;
    }
     
    public VerticalTextIcon(String text, boolean clockwize){
        this(text,clockwize,Color.black);
    }
    
    public void paintIcon(Component c, Graphics g, int x, int y){
        Graphics2D g2 = (Graphics2D)g;
        Font oldFont = g.getFont();
        Color oldColor = g.getColor();
        AffineTransform oldTransform = g2.getTransform();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        g.setColor(color);
        if(clockwize){
            g2.translate(x+getIconWidth(), y);
            g2.rotate(Math.PI/2);
        }else{
            g2.translate(x, y+getIconHeight());
            g2.rotate(-Math.PI/2);
        }
        g.drawString(text, 0, fm.getLeading()+fm.getAscent());
        
        g.setFont(oldFont);
        g.setColor(oldColor);
        g2.setTransform(oldTransform);
    }
    
    public int getIconWidth(){
        return height;
    }
    
    public int getIconHeight(){
        return width;
    }
}
