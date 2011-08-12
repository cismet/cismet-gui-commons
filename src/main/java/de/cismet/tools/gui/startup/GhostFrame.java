/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.startup;

import org.jdesktop.swingx.JXBusyLabel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;

import de.cismet.tools.gui.Static2DTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class GhostFrame extends javax.swing.JFrame {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form GhostFrame.
     *
     * @param   file   DOCUMENT ME!
     * @param   title  DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public GhostFrame(final String file, final String title) throws Exception {
        initComponents();
        final File boundsFile = new File(file + ".bounds");
        Rectangle rectangle = null;
        setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
        final BufferedReader reader = new BufferedReader(new FileReader(boundsFile));
        final String x = reader.readLine();
        final String y = reader.readLine();
        final String width = reader.readLine();
        final String height = reader.readLine();
        reader.close();
        rectangle = new Rectangle();
        rectangle.setBounds(Integer.parseInt(x),
            Integer.parseInt(y),
            Integer.parseInt(width),
            Integer.parseInt(height));
        final ImageIcon ii = new ImageIcon(file + ".png");
        final ImageIcon iii = new ImageIcon(Static2DTools.removeUnusedBorder(ii.getImage(), 0, 1));
        final JPanel p = new JPanel(new BorderLayout()) {

                @Override
                public void paint(final Graphics g) {
                    super.paint(g);
                }

                @Override
                protected void paintChildren(final Graphics g) {
                    g.drawImage(iii.getImage(), 0, 0, null);
                    super.paintChildren(g);
                }
            };

        getContentPane().add(p, BorderLayout.CENTER);
        if (rectangle != null) {
            setBounds(rectangle);
        }

        final JXBusyLabel busy = new JXBusyLabel(new Dimension(100, 100));
        busy.setDelay(100);
        busy.setOpaque(false);
        busy.setBusy(true);
        busy.setHorizontalAlignment(busy.CENTER);

        p.add(busy, BorderLayout.CENTER);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param   args  the command line arguments
     *
     * @throws  Exception  DOCUMENT ME!
     */
    public static void main(final String[] args) throws Exception {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        new GhostFrame("/Users/thorsten/.verdis/verdis.screen", "xxx").setVisible(true);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}