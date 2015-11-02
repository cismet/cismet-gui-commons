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

import org.apache.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Collection;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
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

    private static final Logger log = Logger.getLogger(DownloadManagerStatusPanel.class);

    //~ Instance fields --------------------------------------------------------

    int completedDownlaods = 0;
    int erroneousDownloads = 0;
    Timer animationTimer;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
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
                        final JDialog downloadManager = DownloadManagerDialog.getInstance();
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
        jLabel1 = new javax.swing.JLabel();

        setLayout(new java.awt.GridBagLayout());

        lblRunning.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/downloadmanager.png"))); // NOI18N
        lblRunning.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblRunning.text"));                                           // NOI18N
        lblRunning.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblRunning, gridBagConstraints);

        lblRunningCounter.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblRunningCounter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(lblRunningCounter, gridBagConstraints);

        lblTotal.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblTotal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        add(lblTotal, gridBagConstraints);

        lblTotalCounter.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.lblTotalCounter.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 10);
        add(lblTotalCounter, gridBagConstraints);

        jLabel1.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerStatusPanel.class,
                "DownloadManagerStatusPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(jLabel1, gridBagConstraints);
    }                                                        // </editor-fold>//GEN-END:initComponents

    @Override
    public void downloadListChanged(final DownloadListChangedEvent event) {
        if (event.getAction() == DownloadListChangedEvent.Action.CHANGED_COUNTERS) {
            final Collection<Download> downloads = event.getDownloads();
            if (downloads.size() > 1) {
                // this should not happen...
                log.warn(
                    "It should not happend that a DownlaodListChangedEvent.Changed_Counters concerns more than one Download.");
            }
            final Download[] downloadArr = downloads.toArray(new Download[downloads.size()]);
            final int tmpComplDownloads = DownloadManager.instance().getCountDownloadsCompleted();
            final int tmpErrDownloads = DownloadManager.instance().getCountDownloadsErroneous();
            if (!DownloadManagerDialog.getInstance().isShowing()) {
                if (completedDownlaods < tmpComplDownloads) {
                    showNotification(downloadArr[0].getTitle(), false);
                } else if (erroneousDownloads < tmpErrDownloads) {
                    showNotification(downloadArr[0].getTitle(), true);
                }
            }
            completedDownlaods = tmpComplDownloads;
            erroneousDownloads = tmpErrDownloads;
        }
        updateLabels();
    }

    /**
     * DOCUMENT ME!
     */
    private void updateLabels() {
        // done are all completed,cancelled and erroneus downloads
        final int done = DownloadManager.instance().getCountDownloadsCompleted()
                    + DownloadManager.instance().getCountDownloadsErroneous()
                    + DownloadManager.instance().getCountDownloadsCancelled();
        lblTotalCounter.setText("" + DownloadManager.instance().getCountDownloadsTotal());
        lblRunningCounter.setText("" + done);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  downloadName  DOCUMENT ME!
     * @param  isErroneous   DOCUMENT ME!
     */
    private void showNotification(final String downloadName, final boolean isErroneous) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        showNotification(downloadName, isErroneous);
                    }
                });
        } else {
            final DownloadDesktopNotification notification = new DownloadDesktopNotification(StaticSwingTools
                            .getParentFrame(this),
                    downloadName,
                    isErroneous);
            notification.floatInFromLowerFrameBound();
            final Timer t = new Timer(DownloadManager.instance().getNotificationDisplayTime() * 1000,
                    new ActionListener() {

                        @Override
                        public void actionPerformed(final ActionEvent ae) {
                            SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        try {
                                            notification.dispose();
                                        } catch (final Exception ex) {
                                            log.warn(ex, ex);
                                        }
                                    }
                                });
                        }
                    });
            t.start();
        }
    }
}
