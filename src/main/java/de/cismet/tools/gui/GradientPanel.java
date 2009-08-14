/*
 * GradientPanel.java
 *
 * Created on 16. Dezember 2004, 13:31
 */

package de.cismet.tools.gui;
import java.awt.Color;
import java.awt.*;
/**
 * JPanel mit einem Farbverlauf als Hintergrund
 * @author hell
 */
public class GradientPanel extends javax.swing.JPanel{
    private Color leftColor=Color.GRAY;
    private Color rightColor=Color.WHITE;

    /** Creates a new instance of GradientPanel */
    public GradientPanel() {
        setLeftColor(this.getBackground());
        setRightColor(this.getForeground());
        
    }
    
     /**
      * Erzeugt eine neue Instanz und setzt gleichzeitig den Farbverlauf
      * @param leftColor Linke Farbe
      * @param rightColor Rechte Farbe
      */
     public GradientPanel(Color leftColor, Color rightColor) {
            this.setLeftColor(leftColor);
            this.setRightColor(rightColor);
    }
    
 protected void paintComponent(java.awt.Graphics g) {
      int w = getWidth();
      int h = getHeight();
      Graphics2D g2d = (Graphics2D) g;
      g2d.setPaint(new GradientPaint(0, 0, getLeftColor(), w, 0, getRightColor()));
      g2d.fillRect(0, 0, w, h);
 }

    /**
     * Liefert die rechte Farbe.
     * @return Rechte Farbe
     */
    public Color getRightColor() {
        return rightColor;
    }

    /**
     * setzt die rechte Farbe
     * @param rightColor Rechte Farbe
     */
    public void setRightColor(Color rightColor) {
        this.rightColor = rightColor;
    }

    /**
     * Liefert die linke Farbe.
     * @return Linke Farbe
     */
    public Color getLeftColor() {
        return leftColor;
    }

    /**
     * Setzt die linke Farbe.
     * @param leftColor Linke Farbe
     */
    public void setLeftColor(Color leftColor) {
        this.leftColor = leftColor;
    }
 
    
}




