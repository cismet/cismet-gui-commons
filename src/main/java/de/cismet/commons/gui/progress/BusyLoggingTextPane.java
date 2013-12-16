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
package de.cismet.commons.gui.progress;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.icon.PainterIcon;
import org.jdesktop.swingx.painter.BusyPainter;

import org.openide.util.Exceptions;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

/**
 * Simple TextPane that displays an animated busy label as long as it is busy. There different predefined styles for
 * different Log Levels
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class BusyLoggingTextPane extends JTextPane {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(BusyLoggingTextPane.class);

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public enum Styles {

        //~ Enum constants -----------------------------------------------------

        TIP, INFO, SUCCESS, EXPERT, WARN, ERROR, ERROR_REASON,
    }

    //~ Instance fields --------------------------------------------------------

    private PainterIcon icon;

    private BusyPainter busyPainter;
    private Timer busy;
    private boolean showWaitAnimation = false;
    private final int busyWidth;
    private final int busyHeight;
    private Style styleTip;
    private Style styleSuccess;
    private Style styleInfo;
    private Style styleExpert;
    private Style styleWarn;
    private Style styleError;
    private Style styleErrorReason;
    private HashMap<Styles, Style> styles = new HashMap<Styles, Style>();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new BusyLoggingTextPane object.
     */
    public BusyLoggingTextPane() {
        this(20, 20);
    }

    /**
     * Creates new form SemiTransparentBusyPanel.
     *
     * @param  height  DOCUMENT ME!
     * @param  width   DOCUMENT ME!
     */
    public BusyLoggingTextPane(final int height, final int width) {
        initComponents();
        busyWidth = width;
        busyHeight = height;
        busyPainter = new BusyPainter(height);
        icon = new PainterIcon(new Dimension(busyWidth, busyHeight));
        icon.setPainter(busyPainter);
        // set styles
        styleTip = this.addStyle(Styles.TIP.toString(), null);
        StyleConstants.setForeground(styleTip, Color.blue);
        StyleConstants.setFontSize(styleTip, 10);
        styles.put(Styles.TIP, styleTip);
        styleSuccess = this.addStyle(Styles.SUCCESS.toString(), null);
        StyleConstants.setForeground(styleSuccess, Color.green.darker());
        StyleConstants.setFontSize(styleSuccess, 10);

        styles.put(Styles.SUCCESS, styleSuccess);
        styleInfo = this.addStyle(Styles.INFO.toString(), null);
        StyleConstants.setForeground(styleInfo, Color.DARK_GRAY);
        StyleConstants.setFontSize(styleInfo, 10);
        styles.put(Styles.INFO, styleInfo);
        styleExpert = this.addStyle(Styles.EXPERT.toString(), null);
        StyleConstants.setForeground(styleExpert, Color.gray);
        StyleConstants.setFontSize(styleExpert, 10);
        styles.put(Styles.EXPERT, styleExpert);
        styleWarn = this.addStyle(Styles.WARN.toString(), null);
        StyleConstants.setForeground(styleWarn, Color.orange.darker());
        StyleConstants.setFontSize(styleWarn, 10);
        styles.put(Styles.WARN, styleWarn);
        styleError = this.addStyle(Styles.ERROR.toString(), null);
        StyleConstants.setForeground(styleError, Color.red);
        StyleConstants.setFontSize(styleError, 10);
        StyleConstants.setBold(styleError, true);
        styles.put(Styles.ERROR, styleError);
        styleErrorReason = this.addStyle(Styles.ERROR_REASON.toString(), null);
        StyleConstants.setForeground(styleErrorReason, Color.red);
        StyleConstants.setFontSize(styleErrorReason, 10);
        styles.put(Styles.ERROR_REASON, styleErrorReason);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  msg     DOCUMENT ME!
     * @param  reason  DOCUMENT ME!
     */
    public void addMessage(final String msg, final Styles reason) {
        synchronized (this) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            BusyLoggingTextPane.this.getStyledDocument()
                                    .insertString(
                                        BusyLoggingTextPane.this.getStyledDocument().getLength(),
                                        msg
                                        + "\n",
                                        styles.get(reason));       // NOI18N
                        } catch (BadLocationException ble) {
                            LOG.error("error during Insert", ble); // NOI18N
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void stopWaitAnimation() {
        if (busy != null) {
            showWaitAnimation = false;
            busy.stop();
            busyPainter.setFrame(-1);
            repaint();
            busy = null;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void startWaitAnimation() {
        busy = new Timer(100, new ActionListener() {

                    int frame = busyPainter.getPoints();

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        frame = (frame + 1) % busyPainter.getPoints();
                        busyPainter.setFrame(frame);
                        repaint();
                    }
                });
        showWaitAnimation = true;
        busy.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  flag  DOCUMENT ME!
     */
    public void setBusy(final boolean flag) {
        if (flag && !showWaitAnimation) {
            startWaitAnimation();
        } else if (!flag && showWaitAnimation) {
            stopWaitAnimation();
        }
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2 = (Graphics2D)g.create();
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        if (showWaitAnimation) {
            icon.paintIcon(
                this,
                g2,
                (this.getWidth() / 2)
                        - (busyWidth / 2),
                (this.getHeight() / 2)
                        - (busyHeight / 2));
        }
        g2.dispose();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
    } // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
