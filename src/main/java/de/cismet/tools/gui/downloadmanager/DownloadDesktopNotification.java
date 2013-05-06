/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.Timer;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DownloadDesktopNotification extends JWindow implements ActionListener {

    //~ Static fields/initializers ---------------------------------------------

    private static int XOFF = 20;
    private static int YOFF = 30;
    private static int PIXEL_PER_STEP = 2;
    private static int MILLIS_TO_WAIT = 20;
    private static Color bgColor = new Color(80, 80, 80, 170);

    //~ Instance fields --------------------------------------------------------

    final Timer t = new Timer(MILLIS_TO_WAIT, this);
    int yPosFinal = 0;
    int currY = 0;
    int finalHeight;
    private Frame parentFrame;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DownloadDesktopNotification object.
     *
     * @param  f  DOCUMENT ME!
     */
    public DownloadDesktopNotification(final Frame f) {
        parentFrame = f;
        final JWindow tmp = new JWindow(f);
        tmp.getContentPane().add(new DownloadDesktopNotificationPanel());
        tmp.pack();
        this.setContentPane(new JPanel() {

                @Override
                protected void paintComponent(final Graphics g) {
                    final Graphics2D g2d = (Graphics2D)g;
                    super.paintComponent(g2d);
                    g2d.setPaint(bgColor);
                    g2d.fillRect(0, 0, this.getWidth(), currY);
                }
            });
        this.setBackground(bgColor);
        this.getContentPane().add(new DownloadDesktopNotificationPanel());
        this.setSize(tmp.getSize());
        finalHeight = tmp.getHeight();
        this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        final JDialog downloadManager = DownloadManagerDialog.instance(
                                StaticSwingTools.getParentFrame(DownloadDesktopNotification.this));
                        downloadManager.pack();
                        StaticSwingTools.showDialog(downloadManager);
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getLowerXOfFrame() {
        return parentFrame.getX() + parentFrame.getWidth();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
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
        this.setSize(this.getSize().width, currY);
        t.start();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
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
    public void actionPerformed(final ActionEvent ae) {
        this.setLocation(new Point(this.getX(), this.getY() - PIXEL_PER_STEP));
        currY += PIXEL_PER_STEP;
        setSize(this.getWidth(), (currY < finalHeight) ? currY : finalHeight);
        if (this.getY() <= yPosFinal) {
            t.setRepeats(false);
            t.stop();
        }
    }
}
