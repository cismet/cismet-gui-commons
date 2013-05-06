/**
 * *************************************************
 *
 * cismet GmbH, Saarbruecken, Germany
 * 
* ... and it just works.
 * 
***************************************************
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.tools.gui.downloadmanager;

import org.openide.util.Exceptions;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

/**
 * DOCUMENT ME!
 *
 * @author daniel
 * @version $Revision$, $Date$
 */
public class DownloadDesktopNotification extends JWindow implements ActionListener {

    //~ Static fields/initializers ---------------------------------------------
    private static int XOFF = 15;
    private static int YOFF = 15;
    private static int PIXEL_PER_STEP = 2;
    private static int MILLIS_TO_WAIT = 20;
    //~ Instance fields --------------------------------------------------------
    private Frame parentFrame;
    final Timer t = new Timer(MILLIS_TO_WAIT, this);
    int yPosFinal = 0;

    //~ Constructors -----------------------------------------------------------
    /**
     * Creates a new DownloadDesktopNotification object.
     *
     * @param f DOCUMENT ME!
     */
    public DownloadDesktopNotification(final Frame f) {
        parentFrame = f;
        final JWindow tmp = new JWindow(f);
        tmp.getContentPane().add(new DownloadDesktopNotificationPanel());
        tmp.pack();
        this.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                final Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(new Color(80, 80, 80, 170));
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
            }
        });
        this.getContentPane().add(new DownloadDesktopNotificationPanel());
        this.setSize(tmp.getSize());
    }
    
    

    //~ Methods ----------------------------------------------------------------
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getLowerXOfFrame() {
        return parentFrame.getX() + parentFrame.getWidth();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private int getLowerYOfFrame() {
        return parentFrame.getY() + parentFrame.getHeight();
    }

    /**
     * DOCUMENT ME!
     */
    public void floatInFromLowerFrameBound() {
        this.pack();
        this.setVisible(true);
        this.setLocation(new Point(getLowerXOfFrame() - this.getSize().width - XOFF, getLowerYOfFrame()));
        yPosFinal = getLowerYOfFrame() - this.getSize().height - YOFF;
        t.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final JFrame f = new JFrame("Foo");
        f.setSize(1024, 800);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final DownloadDesktopNotification w = new DownloadDesktopNotification(f);
        w.floatInFromLowerFrameBound();
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        this.setLocation(new Point(this.getX(), this.getY() - PIXEL_PER_STEP));
        this.repaint();
        if (this.getY() <= yPosFinal) {
            t.setRepeats(false);
            t.stop();
        }
    }
}
