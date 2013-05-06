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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JDialog;
import javax.swing.JWindow;
import javax.swing.Timer;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   daniel
 * @version  $Revision$, $Date$
 */
public class DownloadManagerStatusPanel extends javax.swing.JPanel implements DownloadListChangedListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final int DISPOSING_TIME = 3000;

    //~ Instance fields --------------------------------------------------------

    int dowloads = 0;
    Timer animationTimer;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblRunning;
    private javax.swing.JLabel lblRunningCounter;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblTotalCounter;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DownloadManagerStatusPanel.
     */
    public DownloadManagerStatusPanel() {
        initComponents();
        DownloadManager.instance().addDownloadListChangedListener(this);
        this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent me) {
                    if (me.getClickCount() == 2) {
                        final JDialog downloadManager = DownloadManagerDialog.instance(
                                StaticSwingTools.getParentFrame(DownloadManagerStatusPanel.this));
                        downloadManager.pack();
                        StaticSwingTools.showDialog(downloadManager);
                    }
                }
            });
        updateLabels();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblRunning = new javax.swing.JLabel();
        lblRunningCounter = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        lblTotalCounter = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblRunning.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/downloadmanager.png"))); // NOI18N
        lblRunning.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblRunning.text"));                                           // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblRunning, gridBagConstraints);

        lblRunningCounter.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblRunningCounter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblRunningCounter, gridBagConstraints);

        lblTotal.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblTotal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblTotal, gridBagConstraints);

        lblTotalCounter.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblTotalCounter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        add(lblTotalCounter, gridBagConstraints);
    }                                                                // </editor-fold>//GEN-END:initComponents

    @Override
    public void downloadListChanged(final DownloadListChangedEvent event) {
        final int i = DownloadManager.instance().getCountDownloadsCompleted();
        if (dowloads < i) {
            showNotification();
        }
        dowloads = i;
        updateLabels();
    }

    /**
     * DOCUMENT ME!
     */
    private void updateLabels() {
        lblTotalCounter.setText("" + DownloadManager.instance().getCountDownloadsTotal());
        lblRunningCounter.setText("" + DownloadManager.instance().getCountDownloadsCompleted());
    }

    /**
     * DOCUMENT ME!
     */
    private void showNotification() {
        final DownloadDesktopNotification notification = new DownloadDesktopNotification(StaticSwingTools
                        .getParentFrame(this));
        notification.floatInFromLowerFrameBound();
        final Timer t = new Timer(DISPOSING_TIME, new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent ae) {
                        notification.setVisible(false);
                        notification.dispose();
                    }
                });
        t.start();
    }
}
