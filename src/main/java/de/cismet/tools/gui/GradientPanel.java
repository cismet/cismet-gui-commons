/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * GradientPanel.java
 *
 * Created on 16. Dezember 2004, 13:31
 */
package de.cismet.tools.gui;
import java.awt.*;
import java.awt.Color;
/**
 * JPanel mit einem Farbverlauf als Hintergrund.
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class GradientPanel extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    private Color leftColor = Color.GRAY;
    private Color rightColor = Color.WHITE;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of GradientPanel.
     */
    public GradientPanel() {
        setLeftColor(this.getBackground());
        setRightColor(this.getForeground());
    }

    /**
     * Erzeugt eine neue Instanz und setzt gleichzeitig den Farbverlauf.
     *
     * @param  leftColor   Linke Farbe
     * @param  rightColor  Rechte Farbe
     */
    public GradientPanel(final Color leftColor, final Color rightColor) {
        this.setLeftColor(leftColor);
        this.setRightColor(rightColor);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void paintComponent(final java.awt.Graphics g) {
        final int w = getWidth();
        final int h = getHeight();
        final Graphics2D g2d = (Graphics2D)g;
        g2d.setPaint(new GradientPaint(0, 0, getLeftColor(), w, 0, getRightColor()));
        g2d.fillRect(0, 0, w, h);
    }

    /**
     * Liefert die rechte Farbe.
     *
     * @return  Rechte Farbe
     */
    public Color getRightColor() {
        return rightColor;
    }

    /**
     * setzt die rechte Farbe.
     *
     * @param  rightColor  Rechte Farbe
     */
    public void setRightColor(final Color rightColor) {
        this.rightColor = rightColor;
    }

    /**
     * Liefert die linke Farbe.
     *
     * @return  Linke Farbe
     */
    public Color getLeftColor() {
        return leftColor;
    }

    /**
     * Setzt die linke Farbe.
     *
     * @param  leftColor  Linke Farbe
     */
    public void setLeftColor(final Color leftColor) {
        this.leftColor = leftColor;
    }
}
