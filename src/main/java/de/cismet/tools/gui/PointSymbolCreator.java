/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author haffkeatcismet
 */
public class PointSymbolCreator {
    /**
     * Erstellt ein Punktsymbol als Image aus den übergebenen Parametern.
     */
    public static BufferedImage createPointSymbol(boolean drawLine, boolean drawFill, int symbolSize, int lineWidth, Color fillColor, Color lineColor) {
        BufferedImage symbol = new BufferedImage(2*lineWidth+symbolSize, 2*lineWidth+symbolSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = (Graphics2D) symbol.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = (symbol.getWidth()/2)-(symbolSize/2);
        int y = (symbol.getHeight()/2)-(symbolSize/2);
        if (drawFill) {
            g.setColor(fillColor);
            g.fillOval(x, y, symbolSize, symbolSize);
        }
        if (drawLine) {
            g.setColor(lineColor);
            g.setStroke(new BasicStroke(lineWidth));
            g.drawOval(x, y, symbolSize, symbolSize);
        }
        g.dispose();

        return symbol;
    }
}
