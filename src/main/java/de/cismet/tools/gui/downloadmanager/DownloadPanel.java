/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * SingleDownloadPanel.java
 *
 * Created on 18.05.2011, 17:05:07
 */
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import org.openide.util.Cancellable;
import org.openide.util.NbBundle;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.SystemColor;

import java.net.URL;

import java.text.DateFormat;

import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;

import de.cismet.security.exceptions.BadHttpStatusCodeException;

import de.cismet.tools.BrowserLauncher;

/**
 * A panel which represents a download. In order to visualize the different states of a Download object, each
 * DownloadPanel object registers as observer on its download.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadPanel extends javax.swing.JPanel implements Observer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DownloadPanel.class);

    //~ Instance fields --------------------------------------------------------

    private Download download;
    private boolean small;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private org.jdesktop.swingx.JXHyperlink jxlOpenFile;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuItem mniOpenDirectory;
    private javax.swing.JMenuItem mniOpenFile;
    private javax.swing.JMenuItem mniRemove;
    private javax.swing.JPopupMenu popContextMenu;
    private javax.swing.JProgressBar prbProgress;
    private javax.swing.JPopupMenu.Separator sepContextMenuOpenRemove;
    private javax.swing.JSeparator sepDownloadPanels;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DownloadPanel object.
     *
     * @param  download  The download to visualize;
     */
    public DownloadPanel(final Download download) {
        this(download, false, false);
    }

    /**
     * Creates new form DownloadPanel.
     *
     * @param  download       The Download object to visualize.
     * @param  small          A flag indicating whether this panel is to be displayed small or not.
     * @param  hideSeparator  A flag indicating whether this panel should hide the separator at the bottom or not.
     */
    public DownloadPanel(final Download download, final boolean small, final boolean hideSeparator) {
        this.download = download;
        this.small = small;

        initComponents();

        if (this.small) {
            setMaximumSize(new java.awt.Dimension(2147483647, 44));
            setMinimumSize(new java.awt.Dimension(135, 44));
            lblIcon.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource(
                        "/de/cismet/tools/gui/downloadmanager/documenttypes/fallback_single_16.png")));

            remove(lblTitle);
            remove(lblTime);
            remove(lblMessage);
            remove(jxlOpenFile);
            remove(prbProgress);
            popContextMenu.remove(sepContextMenuOpenRemove);
            popContextMenu.remove(mniRemove);

            lblTitle.setFont(new Font("Tahoma", 0, 12));
            GridBagConstraints gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
            add(lblTitle, gridBagConstraints);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
            gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
            add(lblTime, gridBagConstraints);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
            add(lblMessage, gridBagConstraints);

            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
            add(jxlOpenFile, gridBagConstraints);

            gridBagConstraints = new GridBagConstraints();
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.gridwidth = 2;
            gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
            add(prbProgress, gridBagConstraints);
        }

        sepDownloadPanels.setVisible(!hideSeparator);
        initIcon();

        updateComponents();
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

        popContextMenu = new javax.swing.JPopupMenu();
        mniOpenFile = new javax.swing.JMenuItem();
        mniOpenDirectory = new javax.swing.JMenuItem();
        sepContextMenuOpenRemove = new javax.swing.JPopupMenu.Separator();
        mniRemove = new javax.swing.JMenuItem();
        lblIcon = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        sepDownloadPanels = new javax.swing.JSeparator();
        prbProgress = new javax.swing.JProgressBar();
        lblTime = new javax.swing.JLabel();
        jxlOpenFile = new org.jdesktop.swingx.JXHyperlink();
        btnCancel = new javax.swing.JButton();

        mniOpenFile.setText(org.openide.util.NbBundle.getMessage(
                DownloadPanel.class,
                "DownloadPanel.mniOpenFile.text")); // NOI18N
        mniOpenFile.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mniOpenFileActionPerformed(evt);
                }
            });
        popContextMenu.add(mniOpenFile);

        mniOpenDirectory.setText(org.openide.util.NbBundle.getMessage(
                DownloadPanel.class,
                "DownloadPanel.mniOpenDirectory.text")); // NOI18N
        mniOpenDirectory.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mniOpenDirectoryActionPerformed(evt);
                }
            });
        popContextMenu.add(mniOpenDirectory);
        popContextMenu.add(sepContextMenuOpenRemove);

        mniRemove.setText(org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.mniRemove.text")); // NOI18N
        mniRemove.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mniRemoveActionPerformed(evt);
                }
            });
        popContextMenu.add(mniRemove);

        setComponentPopupMenu(popContextMenu);
        setMaximumSize(new java.awt.Dimension(2147483647, 54));
        setMinimumSize(new java.awt.Dimension(135, 54));
        addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    formMouseClicked(evt);
                }
            });
        setLayout(new java.awt.GridBagLayout());

        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/documenttypes/fallback_single_32.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblIcon, gridBagConstraints);

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblTitle.setText(download.getTitle());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        lblMessage.setBackground(new java.awt.Color(255, 102, 0));
        lblMessage.setText(org.openide.util.NbBundle.getMessage(DownloadPanel.class, "DownloadPanel.lblMessage.text")); // NOI18N
        lblMessage.setMaximumSize(new java.awt.Dimension(32767, 15));
        lblMessage.setMinimumSize(new java.awt.Dimension(10, 15));
        lblMessage.setPreferredSize(new java.awt.Dimension(8, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblMessage, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sepDownloadPanels, gridBagConstraints);

        prbProgress.setBorderPainted(false);
        prbProgress.setIndeterminate(true);
        prbProgress.setMaximumSize(new java.awt.Dimension(32767, 15));
        prbProgress.setMinimumSize(new java.awt.Dimension(10, 15));
        prbProgress.setPreferredSize(new java.awt.Dimension(146, 15));
        prbProgress.setString(org.openide.util.NbBundle.getMessage(
                DownloadPanel.class,
                "DownloadPanel.prbProgress.string")); // NOI18N
        prbProgress.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(prbProgress, gridBagConstraints);

        lblTime.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(new Date()));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTime, gridBagConstraints);

        jxlOpenFile.setText((download.getFileToSaveTo() != null) ? download.getFileToSaveTo().getAbsolutePath() : "");
        jxlOpenFile.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jxlOpenFileActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(jxlOpenFile, gridBagConstraints);

        if (download instanceof Cancellable) {
            btnCancel.setIcon(new javax.swing.ImageIcon(
                    getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/cross-circle.png_16x16.png"))); // NOI18N
            btnCancel.setText(org.openide.util.NbBundle.getMessage(
                    DownloadPanel.class,
                    "DownloadPanel.btnCancel.text"));                                                                // NOI18N
            btnCancel.setToolTipText(org.openide.util.NbBundle.getMessage(
                    DownloadPanel.class,
                    "DownloadPanel.btnCancel.toolTipText"));                                                         // NOI18N
            btnCancel.setBorderPainted(false);
            btnCancel.setContentAreaFilled(false);
            btnCancel.setFocusPainted(false);
            btnCancel.setRolloverIcon(new javax.swing.ImageIcon(
                    getClass().getResource(
                        "/de/cismet/tools/gui/downloadmanager/res/cross-circle-frame.png_16x16.png")));              // NOI18N
        }
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCancelActionPerformed(evt);
                }
            });
        if (download instanceof Cancellable) {
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = 3;
            gridBagConstraints.gridy = 1;
            gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
            add(btnCancel, gridBagConstraints);
        }
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Opens a message dialog if the downloads is erraneous and the user double-clicked the panel.
     *
     * @param  evt  The event object.
     */
    private void formMouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() > 1) {
            if (download.getCaughtException() != null) {
                DownloadManagerDialog.showExceptionDialog(download);
            } else if ((download.getStatus() == Download.State.COMPLETED) && (download.getFileToSaveTo() != null)) {
                BrowserLauncher.openURLorFile(download.getFileToSaveTo().getParentFile().getAbsolutePath());
            }
        }
    }                                                                    //GEN-LAST:event_formMouseClicked

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void jxlOpenFileActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jxlOpenFileActionPerformed
        BrowserLauncher.openURLorFile(download.getFileToSaveTo().getAbsolutePath());
    }                                                                               //GEN-LAST:event_jxlOpenFileActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void mniOpenFileActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mniOpenFileActionPerformed
        BrowserLauncher.openURLorFile(download.getFileToSaveTo().getAbsolutePath());
    }                                                                               //GEN-LAST:event_mniOpenFileActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void mniOpenDirectoryActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mniOpenDirectoryActionPerformed
        BrowserLauncher.openURLorFile(download.getFileToSaveTo().getParentFile().getAbsolutePath());
    }                                                                                    //GEN-LAST:event_mniOpenDirectoryActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void mniRemoveActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mniRemoveActionPerformed
        DownloadManager.instance().removeDownload(download);
    }                                                                             //GEN-LAST:event_mniRemoveActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
        if (download instanceof Cancellable) {
            ((Cancellable)download).cancel();
        }
    }                                                                             //GEN-LAST:event_btnCancelActionPerformed

    /**
     * Initiates the icon. According to the file extension of the download, an appropriate icon will be shown.
     */
    private void initIcon() {
        if (download.getFileToSaveTo() == null) {
            return;
        }

        String extensionOfDownload = download.getFileToSaveTo().getName().trim();
        final String extensionOfIcon = small ? "_16.png" : "_32.png";

        if ((extensionOfDownload == null) || (extensionOfDownload.lastIndexOf('.') < 0)) {
            return;
        }

        extensionOfDownload = extensionOfDownload.substring(extensionOfDownload.lastIndexOf('.') + 1,
                    extensionOfDownload.length()).toLowerCase();
        final URL iconURL = getClass().getResource("/de/cismet/tools/gui/downloadmanager/documenttypes/"
                        + extensionOfDownload
                        + extensionOfIcon);
        if (iconURL != null) {
            lblIcon.setIcon(new ImageIcon(iconURL));
        }
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if ((o == null) || !(o.equals(download))) {
            return;
        }

        updateComponents();

        revalidate();
        repaint();
    }

    /**
     * Modifies the components in order to visualize the current state of this panel's download.
     */
    private void updateComponents() {
        switch (download.getStatus()) {
            case WAITING: {
                prbProgress.setVisible(false);
                lblMessage.setVisible(true);
                jxlOpenFile.setVisible(false);
                mniOpenFile.setEnabled(false);
                mniOpenDirectory.setEnabled(download.getFileToSaveTo() != null);
                mniRemove.setEnabled(true);

                break;
            }
            case RUNNING: {
                prbProgress.setVisible(true);
                lblMessage.setVisible(false);
                jxlOpenFile.setVisible(false);
                mniOpenFile.setEnabled(false);
                mniOpenDirectory.setEnabled(download.getFileToSaveTo() != null);
                mniRemove.setEnabled(false);
                lblTitle.setForeground(SystemColor.textText);
                break;
            }
            case COMPLETED: {
                prbProgress.setVisible(false);
                lblMessage.setVisible(false);
                btnCancel.setVisible(false);
                jxlOpenFile.setVisible(download.getFileToSaveTo() != null);
                mniOpenFile.setEnabled(download.getFileToSaveTo() != null);
                mniOpenDirectory.setEnabled(download.getFileToSaveTo() != null);
                mniRemove.setEnabled(true);
                lblTitle.setForeground(SystemColor.textInactiveText);
                download.deleteObserver(this);

                break;
            }
            case COMPLETED_WITH_ERROR: {
                prbProgress.setVisible(false);
                jxlOpenFile.setVisible(false);
                mniOpenFile.setEnabled(false);
                mniOpenDirectory.setEnabled(download.getFileToSaveTo() != null);
                mniRemove.setEnabled(true);
                btnCancel.setVisible(false);
                lblTitle.setForeground(SystemColor.textInactiveText);
                setBackground(Color.pink);
                download.deleteObserver(this);

                lblMessage.setVisible(true);
                lblMessage.setForeground(Color.red);
                if (download.getCaughtException() instanceof BadHttpStatusCodeException) {
                    final BadHttpStatusCodeException exception = (BadHttpStatusCodeException)
                        download.getCaughtException();
                    if (exception.getStatuscode() == 204) {
                        lblMessage.setText(NbBundle.getMessage(
                                DownloadPanel.class,
                                "DownloadPanel.lblMessage.noData",
                                download.getTitle()));
                    } else {
                        lblMessage.setText(NbBundle.getMessage(
                                DownloadPanel.class,
                                "DownloadPanel.lblMessage.error"));
                    }
                } else {
                    lblMessage.setText(NbBundle.getMessage(
                            DownloadPanel.class,
                            "DownloadPanel.lblMessage.error"));
                }
                break;
            }
            case ABORTED: {
                prbProgress.setVisible(false);
                jxlOpenFile.setVisible(false);
                mniOpenFile.setEnabled(false);
                mniOpenDirectory.setEnabled(download.getFileToSaveTo() != null);
                mniRemove.setEnabled(true);
                btnCancel.setVisible(false);
                lblTitle.setForeground(SystemColor.textInactiveText);
                download.deleteObserver(this);

                lblMessage.setVisible(true);
                lblMessage.setText(NbBundle.getMessage(
                        DownloadPanel.class,
                        "DownloadPanel.lblMessage.aborted",
                        download.getTitle()));
            }
        }
    }
}
