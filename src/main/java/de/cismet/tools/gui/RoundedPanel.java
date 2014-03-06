/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * RoundedPanel.java
 *
 * Created on 18. Oktober 2007, 10:10
 */
package de.cismet.tools.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

/**
 * DOCUMENT ME!
 *
 * @author   nh
 * @version  $Revision$, $Date$
 */
public class RoundedPanel extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    protected int curve = 20;

    protected int alpha = 60;
    private Color alphaColor;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form RoundedPanel.
     */
    public RoundedPanel() {
        super();
        setOpaque(false);
        initComponents();
        this.setBackground(new Color(255, 255, 255));
    }

    /**
     * Creates a new RoundedPanel object.
     *
     * @param  layout  DOCUMENT ME!
     */
    public RoundedPanel(final LayoutManager layout) {
        super();
        setOpaque(false);
        initComponents();
        setLayout(layout);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D)g;
        final Color old = g2d.getColor();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(alphaColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), curve, curve);
        g2d.setColor(old);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public void setBackground(final Color bg) {
        alphaColor = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), alpha);
        super.setBackground(alphaColor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getAlpha() {
        return alpha;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  alpha  DOCUMENT ME!
     */
    public void setAlpha(final int alpha) {
        this.alpha = alpha;
        alphaColor = new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue(), alpha);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getCurve() {
        return this.curve;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  curve  DOCUMENT ME!
     */
    public void setCurve(final int curve) {
        this.curve = curve;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
