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

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;

/**
 * Simple panel that draws its background using rounded rectangles thus looking like it had rounded corners.
 *
 * @author   stefan
 * @author   mscholl
 * @version  1.1
 */
public class SemiRoundedPanel extends javax.swing.JPanel {

    //~ Enums ------------------------------------------------------------------

    /**
     * The most well-known orientations.
     *
     * @version  1.0
     */
    public enum Orientation {

        //~ Enum constants -----------------------------------------------------

        NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    //~ Instance fields --------------------------------------------------------

    private transient int curveRadius;
    private transient int alpha;

    private transient Orientation orientation;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new SemiRoundedPanel using <code>Color.BLACK</code> as background. See
     * {@link #SemiRoundedPanel(java.awt.Color)}.
     */
    public SemiRoundedPanel() {
        this(Color.BLACK);
    }
    /**
     * Creates a new SemiRoundedPanel using the given <code>Color</code> and the {@link BorderLayout} as <code>
     * LayoutManager</code>. See {@link #SemiRoundedPanel(java.awt.LayoutManager, java.awt.Color)}.
     *
     * @param  background  the <code>Color</code> of the background of this component
     *
     * @see    #setBackground(java.awt.Color)
     */
    public SemiRoundedPanel(final Color background) {
        this(new BorderLayout(), background);
    }

    /**
     * Creates a new SemiRoundedPanel using the given <code>LayoutManager</code> and the background <code>
     * Color.BLACK</code>. See {@link #SemiRoundedPanel(java.awt.LayoutManager, java.awt.Color)}
     *
     * @param  layout  the <code>LayoutManager of this component</code>
     */
    public SemiRoundedPanel(final LayoutManager layout) {
        this(layout, Color.BLACK);
    }

    /**
     * Creates a new SemiRoundedPanel using the given <code>LayoutManager</code> and <code>Color</code>. By default the
     * rounded corners of this rounded panel are at the top ( <code>Orientation.NORTH</code>), the filling <code>
     * Color</code> alpha is <code>0</code> and the curve radius is <code>10</code>.
     *
     * @param  layout  DOCUMENT ME!
     * @param  color   DOCUMENT ME!
     *
     * @see    #setCurveLocation(de.cismet.tools.gui.SemiRoundedPanel.Orientation)
     * @see    #setAlpha(int)
     * @see    #setCurveRadius(int)
     */
    public SemiRoundedPanel(final LayoutManager layout, final Color color) {
        super(layout);
        initComponents();
        setOpaque(false);
        setBackground(color);
        setCurveLocation(Orientation.NORTH);
        setCurveRadius(10);
        setAlpha(0);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final Graphics2D g2d = (Graphics2D)g;
        final Color old = g2d.getColor();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(getBackground());
        final int diameter = curveRadius + curveRadius;
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), diameter, diameter);
        final Composite save = g2d.getComposite();
        g2d.setComposite(AlphaComposite.Src);
        switch (orientation) {
            case NORTH: {
                g2d.fillRect(0, curveRadius, getWidth(), getHeight() - curveRadius);
                break;
            }
            case NORTHEAST: {
                g2d.fillRect(0, curveRadius, getWidth(), getHeight() - curveRadius);
                g2d.fillRect(0, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            case EAST: {
                g2d.fillRect(0, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            case SOUTHEAST: {
                g2d.fillRect(0, 0, getWidth(), getHeight() - curveRadius);
                g2d.fillRect(0, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            case SOUTH: {
                g2d.fillRect(0, 0, getWidth(), getHeight() - curveRadius);
                break;
            }
            case SOUTHWEST: {
                g2d.fillRect(0, 0, getWidth(), getHeight() - curveRadius);
                g2d.fillRect(curveRadius, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            case WEST: {
                g2d.fillRect(curveRadius, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            case NORTHWEST: {
                g2d.fillRect(0, curveRadius, getWidth(), getHeight() - curveRadius);
                g2d.fillRect(curveRadius, 0, getWidth() - curveRadius, getHeight());
                break;
            }
            default: {
                // all four corners stay rounded
            }
        }
        g2d.setComposite(save);
        g2d.setColor(old);
    }

    /**
     * Sets the radius of the curve of the edges of this panel.
     *
     * @param  curve  the radius in pixels
     */
    public final void setCurveRadius(final int curve) {
        this.curveRadius = curve;
    }

    /**
     * Gets the radius of the curve of the edges of this panel.
     *
     * @return  the radius in pixels
     */
    public final int getCurveRadius() {
        return curveRadius;
    }

    /**
     * Sets the location of the curves of this panel. All edges that lie in the direction of the orientation will be
     * rounded. E.g. if the orientation is <code>Orientation.NORTH</code> the upper right and upper left corner of this
     * panel will be rounded. If the orientation is <code>Orientation.SOUTHWEST</code> the lower left corner will be
     * rounded only.
     *
     * @param  orientation  the location of the rounded corners
     */
    public final void setCurveLocation(final Orientation orientation) {
        this.orientation = orientation;
    }

    /**
     * Gets the location of the curves of this panel.
     *
     * @return  the location of the rounded corners
     */
    public final Orientation getCurveLocation() {
        return orientation;
    }

    /**
     * Gets the alpha value of the background <code>Color</code>.
     *
     * @return  the alpha value of the background <code>Color</code>
     */
    public final int getAlpha() {
        return alpha;
    }

    /**
     * Sets the alpha value of the background <code>Color</code>.
     *
     * @param  alpha  the new alpha value of the background <code>Color</code>
     */
    public final void setAlpha(final int alpha) {
        this.alpha = alpha;
        setBackground(new Color(
                getBackground().getRed(),
                getBackground().getGreen(),
                getBackground().getBlue(),
                alpha));
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setLayout(new java.awt.BorderLayout());
    } // </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
