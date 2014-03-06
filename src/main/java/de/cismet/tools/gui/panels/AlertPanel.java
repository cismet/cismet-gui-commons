/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;

import de.cismet.tools.gui.RoundedPanel;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class AlertPanel extends RoundedPanel {

    //~ Static fields/initializers ---------------------------------------------

    public static final Color dangerBgColor = new Color(242, 222, 222);
    public static final Color dangerBorderColor = new Color(235, 204, 209);
    public static final Color dangerMessageColor = new Color(169, 69, 66);

    public static final Color infoBgColor = new Color(217, 237, 247);
    public static final Color infoBorderColor = new Color(188, 232, 241);
    public static final Color infoMessageColor = new Color(80, 112, 152);

    public static final Color successBgColor = new Color(223, 240, 216);
    public static final Color successBorderColor = new Color(214, 233, 198);
    public static final Color successMessageColor = new Color(90, 118, 75);

    public static final Color warningBgColor = new Color(252, 248, 227);
    public static final Color warningBorderColor = new Color(250, 235, 204);
    public static final Color warningMessageColor = new Color(159, 109, 74);

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum TYPE {

        //~ Enum constants -----------------------------------------------------

        INFO, SUCCESS, WARNING, DANGER
    }

    //~ Instance fields --------------------------------------------------------

    private TYPE type;
    private boolean closeable = false;
    private AbstractBorder border;
    private Component content;
    private final ArrayList<ActionListener> closeListeners = new ArrayList<ActionListener>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel pnlContent;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AlertPanel object.
     */
    public AlertPanel() {
        this(TYPE.INFO, new JLabel("insert a message"), true);
    }

    /**
     * Creates a new AlertPanel object.
     *
     * @param  t          DOCUMENT ME!
     * @param  message    DOCUMENT ME!
     * @param  closeable  DOCUMENT ME!
     */
    public AlertPanel(final TYPE t, final String message, final boolean closeable) {
        this(t, new JLabel("insert a message"), closeable);
    }

    /**
     * Creates new form AlertPanel.
     *
     * @param  t          DOCUMENT ME!
     * @param  content    message DOCUMENT ME!
     * @param  closeable  DOCUMENT ME!
     */
    public AlertPanel(final TYPE t, final Component content, final boolean closeable) {
        this.alpha = 255;
        this.type = t;
        this.closeable = closeable;
        this.content = content;
        initComponents();
        this.pnlContent.add(content, BorderLayout.CENTER);
        stylePanel();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  al  DOCUMENT ME!
     */
    public void addCloseButtonActionListener(final ActionListener al) {
        this.closeListeners.add(al);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  al  DOCUMENT ME!
     */
    public void removeCloseButtonActionListener(final ActionListener al) {
        this.closeListeners.remove(al);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  ae  DOCUMENT ME!
     */
    private void fireCloseEvent(final ActionEvent ae) {
        for (final ActionListener al : closeListeners) {
            al.actionPerformed(ae);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void styleDanger() {
        setBackground(dangerBgColor);
        border = new RoundedBorder(dangerBorderColor, curve, 1);
        if (content instanceof JLabel) {
            ((JLabel)content).setForeground(dangerMessageColor);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void styleSuccess() {
        setBackground(successBgColor);
        border = new RoundedBorder(successBorderColor, curve, 1);
        if (content instanceof JLabel) {
            ((JLabel)content).setForeground(successMessageColor);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void styleWarning() {
        setBackground(warningBgColor);
        border = new RoundedBorder(warningBorderColor, curve, 1);
        if (content instanceof JLabel) {
            ((JLabel)content).setForeground(warningMessageColor);
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void styleInfo() {
        setBackground(infoBgColor);
        border = new RoundedBorder(infoBorderColor, curve, 1);
        if (content instanceof JLabel) {
            ((JLabel)content).setForeground(infoMessageColor);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  content  DOCUMENT ME!
     */
    public void setContent(final Component content) {
        this.pnlContent.removeAll();
        this.pnlContent.add(content, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  t  DOCUMENT ME!
     */
    public void setType(final TYPE t) {
        this.type = t;
        stylePanel();
    }

    /**
     * DOCUMENT ME!
     */
    private void stylePanel() {
        switch (type) {
            case DANGER: {
                styleDanger();
                break;
            }
            case SUCCESS: {
                styleSuccess();
                break;
            }
            case WARNING: {
                styleWarning();
                break;
            }
            default: {
                styleInfo();
                break;
            }
        }
        this.setBorder(BorderFactory.createCompoundBorder(border, new EmptyBorder(10, 30, 10, 10)));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final AlertPanel info = new AlertPanel(
                TYPE.INFO,
                new JLabel("<html> <b>INFO! </b> What a cool alert message box</html>"),
                true);
        final AlertPanel success = new AlertPanel(
                TYPE.SUCCESS,
                new JLabel("<html> <b>SUCCESS! </b>What a cool alert message box</html>"),
                true);
        final AlertPanel warn = new AlertPanel(
                TYPE.WARNING,
                new JLabel("<html> <b>WARNING! </b>What a cool alert message box</html>"),
                true);
        final AlertPanel danger = new AlertPanel(
                TYPE.DANGER,
                new JLabel("<html> <b>DANGER! </b>What a cool alert message box</html>"),
                true);

        final JFrame f = new JFrame();
        f.setSize(500, 500);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JPanel p = new JPanel();
        p.setBorder(new EmptyBorder(5, 5, 5, 5));
        final GridLayout layout = new GridLayout(4, 1);
        layout.setHgap(5);
        layout.setVgap(5);
        p.setLayout(layout);
        p.add(info);
        p.add(success);
        p.add(warn);
        p.add(danger);
        f.getContentPane().add(p);
        f.setVisible(true);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        pnlContent = new javax.swing.JPanel();
        if (closeable) {
            closeButton = new javax.swing.JButton();
        }

        setMinimumSize(new java.awt.Dimension(44, 50));
        setPreferredSize(new java.awt.Dimension(36, 50));
        setLayout(new java.awt.GridBagLayout());

        pnlContent.setOpaque(false);
        pnlContent.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(pnlContent, gridBagConstraints);

        if (closeable) {
            closeButton.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/de/cismet/tools/gui/res/glyphicons_207_remove_2.png")));   // NOI18N
            org.openide.awt.Mnemonics.setLocalizedText(
                closeButton,
                org.openide.util.NbBundle.getMessage(AlertPanel.class, "AlertPanel.closeButton.text")); // NOI18N
            closeButton.setBorderPainted(false);
            closeButton.setContentAreaFilled(false);
            closeButton.setFocusPainted(false);
            closeButton.setPreferredSize(new java.awt.Dimension(16, 16));
        }
        closeButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    closeButtonActionPerformed(evt);
                }
            });
        if (closeable) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 0;
            gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
            add(closeButton, gridBagConstraints);
        }
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void closeButtonActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_closeButtonActionPerformed
        this.setVisible(false);
        fireCloseEvent(evt);
    }                                                                               //GEN-LAST:event_closeButtonActionPerformed

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static final class RoundedBorder extends AbstractBorder {

        //~ Instance fields ----------------------------------------------------

        private Color color;
        private int arc;
        private int thickness;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new RoundedBorder object.
         *
         * @param  c          DOCUMENT ME!
         * @param  arc        DOCUMENT ME!
         * @param  thickness  DOCUMENT ME!
         */
        public RoundedBorder(final Color c, final int arc, final int thickness) {
            this.color = c;
            this.arc = arc;
            this.thickness = thickness;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  g  DOCUMENT ME!
         */
        public void setColor(final Color g) {
            this.color = g;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  arc  DOCUMENT ME!
         */
        public void setArc(final int arc) {
            this.arc = arc;
        }

        @Override
        public void paintBorder(final Component c,
                final Graphics g,
                final int x,
                final int y,
                final int width,
                final int height) {
            final Graphics2D g2 = (Graphics2D)g;
            g2.setColor(this.color);
            final int offs = this.thickness;
            final int size = offs + offs;
            final Shape outer = new RoundRectangle2D.Float(x, y, width, height, arc, arc);
            final Shape inner = new RoundRectangle2D.Float(x + offs, y + offs, width - size, height - size, arc, arc);
            final Path2D path = new Path2D.Float(Path2D.WIND_EVEN_ODD);
            path.append(outer, false);
            path.append(inner, false);
            g2.fill(path);
        }

        @Override
        public Insets getBorderInsets(final Component c, final Insets insets) {
            insets.set(thickness, thickness, thickness, thickness);
            return insets;
        }
    }
}
