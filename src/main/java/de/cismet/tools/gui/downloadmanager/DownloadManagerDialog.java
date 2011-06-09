/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DownloadManagerDialog.java
 *
 * Created on 13.05.2011, 13:33:00
 */
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import de.cismet.tools.BrowserLauncher;

/**
 * This dialog contains a DownloadManagerPanel to visualise the list of current downloads. Additionally there are
 * controls to open the destination directory and to clear the download list.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadManagerDialog extends javax.swing.JDialog implements WindowListener, DownloadListChangedListener {

    //~ Static fields/initializers ---------------------------------------------

    private static Logger LOG = Logger.getLogger(DownloadManagerDialog.class);

    private static DownloadManagerDialog instance;

    //~ Instance fields --------------------------------------------------------

    private boolean closeAfterLastDownload = true;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClearList;
    private javax.swing.JButton btnOpenDestinationDirectory;
    private javax.swing.Box.Filler horizontalGlue;
    private javax.swing.JLabel lblDownloadsTotalKey;
    private javax.swing.JLabel lblDownloadsTotalValue;
    private javax.swing.JPanel pnlControls;
    private de.cismet.tools.gui.downloadmanager.DownloadManagerPanel pnlDownloadManagerPanel;
    private javax.swing.JScrollPane scpDownloadManagerPanel;
    private javax.swing.JSeparator sepControls;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DownloadManagerDialog.
     *
     * @param  parent  The parent frame.
     */
    private DownloadManagerDialog(final java.awt.Frame parent) {
        super(parent, false);

        initComponents();

        DownloadManager.instance().addDownloadListChangedListener(pnlDownloadManagerPanel);
        pnlDownloadManagerPanel.add(DownloadManager.instance().getDownloads());

        final int countDownloadsErraneous = DownloadManager.instance().getCountDownloadsErraneous();
        final int countDownloadsCompleted = DownloadManager.instance().getCountDownloadsCompleted();
        final int countDownloadsTotal = DownloadManager.instance().getCountDownloadsTotal();

        lblDownloadsTotalValue.setText(String.valueOf(countDownloadsTotal));
        btnClearList.setEnabled((countDownloadsCompleted + countDownloadsErraneous) > 0);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Creates or returns the singleton object.
     *
     * @param   parent  The parent frame of this dialog.
     *
     * @return  The singleton instance.
     */
    public static DownloadManagerDialog instance(final Frame parent) {
        if (instance == null) {
            instance = new DownloadManagerDialog(parent);
            DownloadManager.instance().addDownloadListChangedListener(instance);

            instance.addWindowListener(instance);
            instance.setPreferredSize(new Dimension(650, 300));
            instance.setLocationRelativeTo(parent);
        }

        return instance;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        scpDownloadManagerPanel = new javax.swing.JScrollPane();
        pnlDownloadManagerPanel = new de.cismet.tools.gui.downloadmanager.DownloadManagerPanel();
        sepControls = new javax.swing.JSeparator();
        pnlControls = new javax.swing.JPanel();
        btnOpenDestinationDirectory = new javax.swing.JButton();
        btnClearList = new javax.swing.JButton();
        horizontalGlue = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        lblDownloadsTotalKey = new javax.swing.JLabel();
        lblDownloadsTotalValue = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(DownloadManagerDialog.class, "DownloadManagerDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(426, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        scpDownloadManagerPanel.setBorder(null);
        scpDownloadManagerPanel.setViewportView(pnlDownloadManagerPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(scpDownloadManagerPanel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(sepControls, gridBagConstraints);

        pnlControls.setLayout(new java.awt.GridBagLayout());

        btnOpenDestinationDirectory.setMnemonic(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnOpenDestinationDirectory.mnemonic").charAt(0));
        btnOpenDestinationDirectory.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnOpenDestinationDirectory.text")); // NOI18N
        btnOpenDestinationDirectory.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnOpenDestinationDirectoryActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(btnOpenDestinationDirectory, gridBagConstraints);

        btnClearList.setMnemonic(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnClearList.mnemonic").charAt(0));
        btnClearList.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnClearList.text")); // NOI18N
        btnClearList.setEnabled(false);
        btnClearList.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnClearListActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(btnClearList, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlControls.add(horizontalGlue, gridBagConstraints);

        lblDownloadsTotalKey.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblDownloadsTotalKey.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(lblDownloadsTotalKey, gridBagConstraints);

        lblDownloadsTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDownloadsTotalValue.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblDownloadsTotalValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(lblDownloadsTotalValue, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(pnlControls, gridBagConstraints);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Notifies the DownloadManager singleton that all obsolete downloads should be removed.
     *
     * @param  evt  The event object.
     */
    private void btnClearListActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnClearListActionPerformed
        closeAfterLastDownload = false;
        DownloadManager.instance().removeObsoleteDownloads();
    }                                                                                //GEN-LAST:event_btnClearListActionPerformed

    /**
     * Opens a file manager pointing to the destination directory for downloads.
     *
     * @param  evt  The event object.
     */
    private void btnOpenDestinationDirectoryActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnOpenDestinationDirectoryActionPerformed
        closeAfterLastDownload = false;
        BrowserLauncher.openURLorFile(DownloadManager.instance().getDestinationDirectory().getAbsolutePath());
    }                                                                                               //GEN-LAST:event_btnOpenDestinationDirectoryActionPerformed

    @Override
    public void downloadListChanged(final DownloadListChangedEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getAction()) {
            case CHANGED_COUNTERS: {
                final int countDownloadsErraneous = DownloadManager.instance().getCountDownloadsErraneous();
                final int countDownloadsCompleted = DownloadManager.instance().getCountDownloadsCompleted();
                final int countDownloadsTotal = DownloadManager.instance().getCountDownloadsTotal();

                lblDownloadsTotalValue.setText(String.valueOf(countDownloadsTotal));

                // Don't close window if at least one download is erraneous
                closeAfterLastDownload &= countDownloadsErraneous == 0;
                if (closeAfterLastDownload && (countDownloadsCompleted == countDownloadsTotal)) {
                    dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                }

                btnClearList.setEnabled((countDownloadsCompleted + countDownloadsErraneous) > 0);
            }
        }
    }

    @Override
    public void windowOpened(final WindowEvent e) {
    }

    @Override
    public void windowClosing(final WindowEvent e) {
        instance = null;
        DownloadManager.instance().removeDownloadListChangedListener(pnlDownloadManagerPanel);
    }

    @Override
    public void windowClosed(final WindowEvent e) {
    }

    @Override
    public void windowIconified(final WindowEvent e) {
    }

    @Override
    public void windowDeiconified(final WindowEvent e) {
    }

    @Override
    public void windowActivated(final WindowEvent e) {
    }

    @Override
    public void windowDeactivated(final WindowEvent e) {
    }
}
