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

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import org.openide.util.NbBundle;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;

import java.io.File;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import de.cismet.tools.BrowserLauncher;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.downloadmanager.Download.State;

/**
 * This dialog contains a DownloadManagerPanel to visualise the list of current downloads. Additionally there are
 * controls to open the destination directory and to clear the download list.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class DownloadManagerDialog extends javax.swing.JDialog implements DownloadListChangedListener {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(DownloadManagerDialog.class);
    private static DownloadManagerDialog INSTANCE;

    //~ Instance fields --------------------------------------------------------

    private boolean askForJobNameEnabled = false;
    private boolean openAutomaticallyEnabled = true;
    private boolean closeAutomaticallyEnabled = true;
    private boolean closeMainDialogAfterClosingSubDialog = false;
    private String jobName = "";

    private boolean jobNameConfirmed = true;

    private final Collection<Download> downloadsToOpen = new LinkedList<Download>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnClearList;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnOK;
    private javax.swing.JDialog dlgExceptionDialog;
    private javax.swing.JDialog dlgJobname;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel lblDestinationDirectory;
    private javax.swing.JLabel lblDownloadsTotalKey;
    private javax.swing.JLabel lblDownloadsTotalValue;
    private javax.swing.JLabel lblJobname;
    private javax.swing.JLabel lblUserDirectory;
    private javax.swing.JLabel lblUserDirectoryLabel;
    private javax.swing.JPanel pnlControls;
    private de.cismet.tools.gui.downloadmanager.DownloadManagerPanel pnlDownloadManagerPanel;
    private javax.swing.JPanel pnlExceptionDialogContainer;
    private javax.swing.JPanel pnlJobnameControls;
    private javax.swing.JScrollPane scpDownloadManagerPanel;
    private javax.swing.JSeparator sepControls;
    private javax.swing.JSeparator sepExceptionDialogControls;
    private javax.swing.JSeparator sepJobnameControls;
    private javax.swing.JTextField txtJobname;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DownloadManagerDialog.
     */
    private DownloadManagerDialog() {
        super((Frame)null, false);

        initComponents();

        bindingGroup.unbind();
        if (txtJobname.getDocument() instanceof AbstractDocument) {
            ((AbstractDocument)txtJobname.getDocument()).setDocumentFilter(new UserDirectoryFilter());
        }
        bindingGroup.bind();

        DownloadManager.instance().addDownloadListChangedListener(pnlDownloadManagerPanel);
        final List<Download> reversedDownloads = new LinkedList<Download>();
        for (final Download download : DownloadManager.instance().getDownloads()) {
            reversedDownloads.add(0, download);
        }
        pnlDownloadManagerPanel.add(reversedDownloads);

        final int countDownloadsErroneous = DownloadManager.instance().getCountDownloadsErroneous();
        final int countDownloadsCompleted = DownloadManager.instance().getCountDownloadsCompleted();
        final int countDownlaodCancelled = DownloadManager.instance().getCountDownloadsCancelled();
        final int countDownloadsTotal = DownloadManager.instance().getCountDownloadsTotal();

        lblDownloadsTotalValue.setText(String.valueOf(countDownloadsTotal));
        btnClearList.setEnabled((countDownloadsCompleted + countDownloadsErroneous + countDownlaodCancelled) > 0);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static DownloadManagerDialog getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DownloadManagerDialog();
            INSTANCE.pack();
            DownloadManager.instance().addDownloadListChangedListener(INSTANCE);
        }

        return INSTANCE;
    }

    /**
     * Creates or returns the singleton object.
     *
     * @param       parent  The parent frame of this dialog.
     *
     * @return      The singleton instance.
     *
     * @deprecated  Use getInstance() instead.
     */
    public static DownloadManagerDialog instance(final Frame parent) {
        return getInstance();
    }

    /**
     * Creates or returns the singleton object.
     *
     * @param       parent  The parent frame of this dialog.
     *
     * @return      The singleton instance.
     *
     * @deprecated  Use getInstance() instead.
     */
    public static DownloadManagerDialog instance(final Component parent) {
        return getInstance();
    }

    /**
     * If no download manager dialog exists, this method will instantiate and display a download manager dialog.
     *
     * @param  parent  The parent frame of this dialog.
     */
    public static void show(final Component parent) {
        final DownloadManagerDialog dialog = getInstance();
        StaticSwingTools.showDialog(parent, dialog, true);
    }

    /**
     * Displays the DownloadManagerDialog and asks - if user wants to - for a jobname.
     *
     * @param       parent  The parent frame.
     *
     * @return      The jobname specified by the user.
     *
     * @deprecated  use getInstance().showAskingForUserTitleDialog(Component) instead.
     */
    public static boolean showAskingForUserTitle(final Frame parent) {
        return getInstance().showAskingForUserTitleDialog(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   parent  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean showAskingForUserTitleDialog(final Component parent) {
        final Frame parentFrame = (parent instanceof Frame) ? (Frame)parent : StaticSwingTools.getParentFrame(parent);

        final boolean close = !isVisible();
        show(parentFrame);
        boolean result = isJobNameConfirmed();

        if (isAskForJobNameEnabled()) {
            txtJobname.setText(getJobName());
            txtJobname.setCaretPosition(getJobName().length());
            if ((dlgJobname.getOwner() != null) && (parentFrame != null) && (parentFrame.getIconImage() != null)) {
                dlgJobname.getOwner().setIconImage(parentFrame.getIconImage());
            }
            dlgJobname.setPreferredSize(dlgJobname.getMinimumSize());
            dlgJobname.pack();

            StaticSwingTools.showDialog(dlgJobname);

            result = isJobNameConfirmed();

            if (!isJobNameConfirmed() && close) {
                closeWindow();
            }
        }

        return result;
    }

    /**
     * Displays the DownloadManagerDialog and asks - if user wants to - for a jobname.
     *
     * @param       parent  The parent frame.
     *
     * @return      The jobname specified by the user.
     *
     * @deprecated  use getInstance().showAskingForUserTitleDialog(Component) instead.
     */
    public static boolean showAskingForUserTitle(final Component parent) {
        return getInstance().showAskingForUserTitleDialog(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param       download  DOCUMENT ME!
     *
     * @deprecated  use getInstance().showTheExceptionDialog(download) instead
     */
    public static void showExceptionDialog(final Download download) {
        getInstance().showTheExceptionDialog(download);
    }

    /**
     * Shows an exception dialog which visualizes the exception provided by download.getCaughtException().
     *
     * @param  download  The erroneous download.
     */
    public void showTheExceptionDialog(final Download download) {
        if (download.getCaughtException() == null) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("There is no exception to visualize.");
            }
            return;
        }

        final Frame parent = StaticSwingTools.getParentFrame(this);
        if ((dlgExceptionDialog.getOwner() != null) && (parent != null) && (parent.getIconImage() != null)) {
            dlgJobname.getOwner().setIconImage(parent.getIconImage());
        }

        final JPanel exceptionPanel = download.getExceptionPanel(download.getCaughtException());

        if (exceptionPanel != null) {
            pnlExceptionDialogContainer.removeAll();
            pnlExceptionDialogContainer.add(exceptionPanel, BorderLayout.CENTER);
            dlgExceptionDialog.invalidate();
            dlgExceptionDialog.pack();
            StaticSwingTools.showDialog(dlgExceptionDialog);
        } else {
            final Exception ex = download.getCaughtException();
            final org.jdesktop.swingx.error.ErrorInfo ei = new ErrorInfo(
                    NbBundle.getMessage(
                        DownloadPanel.class,
                        "DownloadManagerDialog.showExceptionDialog(Download).error.title"),
                    ex.getMessage(),
                    null,
                    null,
                    ex,
                    Level.ALL,
                    null);
            JXErrorPane.showDialog(StaticSwingTools.getParentFrameIfNotNull(this), ei);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @deprecated  use getInstance().closeWindow() instead
     */
    public static void close() {
        getInstance().closeWindow();
    }

    /**
     * Closes the DownloadManagerDialog.
     */
    public void closeWindow() {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        closeWindow();
                    }
                });
        } else {
            if (dlgJobname.isVisible() || dlgExceptionDialog.isVisible()) {
                closeMainDialogAfterClosingSubDialog = true;
            } else {
                setVisible(false);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use getInstance().getJobName() instead
     */
    public static String getJobname() {
        return getInstance().getJobName();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       jobname  DOCUMENT ME!
     *
     * @deprecated  use getInstance().setJobName(jobname) instead
     */
    public static void setJobname(final String jobname) {
        getInstance().setJobName(jobname);
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use getInstance().isAskForJobNameEnabled() instead
     */
    public static boolean isAskForJobname() {
        return getInstance().isAskForJobNameEnabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       askForJobname  DOCUMENT ME!
     *
     * @deprecated  use getInstance().setAskForJobNameEnabled(askForJobname) instead
     */
    public static void setAskForJobname(final boolean askForJobname) {
        getInstance().setAskForJobNameEnabled(askForJobname);
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use getInstance().isOpenAutomaticallyEnabled() instead
     */
    public static boolean isOpenAutomatically() {
        return getInstance().isOpenAutomaticallyEnabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       openAutomatically  DOCUMENT ME!
     *
     * @deprecated  use getInstance().setOpenAutomaticallyEnabled(openAutomatically) instead
     */
    public static void setOpenAutomatically(final boolean openAutomatically) {
        getInstance().setOpenAutomaticallyEnabled(openAutomatically);
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use getInstance().isCloseAutomaticallyEnabled() instead
     */
    public static boolean isCloseAutomatically() {
        return getInstance().isCloseAutomaticallyEnabled();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       closeAutomatically  DOCUMENT ME!
     *
     * @deprecated  use getInstance().setCloseAutomaticallyEnabled(closeAutomatically) instead
     */
    public static void setCloseAutomatically(final boolean closeAutomatically) {
        getInstance().setCloseAutomaticallyEnabled(closeAutomatically);
    }

    /**
     * DOCUMENT ME!
     *
     * @return      DOCUMENT ME!
     *
     * @deprecated  use getInstance().isJobNameConfirmed() instead
     */
    public static boolean isJobnameConfirmed() {
        return getInstance().isJobNameConfirmed();
    }

    /**
     * DOCUMENT ME!
     *
     * @param       isJobNameConfirmed  DOCUMENT ME!
     *
     * @deprecated  use getInstance().setJobNameConfirmed(isJobnameConfirmed) instead
     */
    public static void setJobnameConfirmed(final boolean isJobNameConfirmed) {
        getInstance().setJobNameConfirmed(isJobNameConfirmed);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        dlgJobname = new JDialog(this);
        lblJobname = new javax.swing.JLabel();
        txtJobname = new javax.swing.JTextField();
        pnlJobnameControls = new javax.swing.JPanel();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        sepJobnameControls = new javax.swing.JSeparator();
        lblDestinationDirectory = new javax.swing.JLabel();
        lblUserDirectory = new javax.swing.JLabel();
        lblUserDirectoryLabel = new javax.swing.JLabel();
        dlgExceptionDialog = new JDialog(this);
        sepExceptionDialogControls = new javax.swing.JSeparator();
        pnlExceptionDialogContainer = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        scpDownloadManagerPanel = new javax.swing.JScrollPane();
        pnlDownloadManagerPanel = new de.cismet.tools.gui.downloadmanager.DownloadManagerPanel();
        sepControls = new javax.swing.JSeparator();
        pnlControls = new javax.swing.JPanel();
        btnClearList = new javax.swing.JButton();
        lblDownloadsTotalKey = new javax.swing.JLabel();
        lblDownloadsTotalValue = new javax.swing.JLabel();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 32767));

        dlgJobname.setTitle(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.dlgJobname.title")); // NOI18N
        dlgJobname.setMinimumSize(new java.awt.Dimension(400, 180));
        dlgJobname.setModal(true);
        dlgJobname.getContentPane().setLayout(new java.awt.GridBagLayout());

        lblJobname.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblJobname.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        dlgJobname.getContentPane().add(lblJobname, gridBagConstraints);

        txtJobname.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.txtJobname.text")); // NOI18N
        txtJobname.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    txtJobnameActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dlgJobname.getContentPane().add(txtJobname, gridBagConstraints);

        pnlJobnameControls.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.TRAILING));

        btnOK.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnOK.text")); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnOKActionPerformed(evt);
                }
            });
        pnlJobnameControls.add(btnOK);

        btnCancel.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCancelActionPerformed(evt);
                }
            });
        pnlJobnameControls.add(btnCancel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weightx = 1.0;
        dlgJobname.getContentPane().add(pnlJobnameControls, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        dlgJobname.getContentPane().add(sepJobnameControls, gridBagConstraints);

        lblDestinationDirectory.setText(DownloadManager.instance().getDestinationDirectory().getAbsolutePath()
                    + File.separator);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        dlgJobname.getContentPane().add(lblDestinationDirectory, gridBagConstraints);

        final org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ,
                txtJobname,
                org.jdesktop.beansbinding.ELProperty.create("${text}"),
                lblUserDirectory,
                org.jdesktop.beansbinding.BeanProperty.create("text"));
        binding.setSourceNullValue("Nothing entered");
        binding.setSourceUnreadableValue("Unreadable");
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        dlgJobname.getContentPane().add(lblUserDirectory, gridBagConstraints);

        lblUserDirectoryLabel.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblUserDirectoryLabel.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        dlgJobname.getContentPane().add(lblUserDirectoryLabel, gridBagConstraints);

        dlgExceptionDialog.setTitle(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.dlgExceptionDialog.title")); // NOI18N
        dlgExceptionDialog.setModal(true);
        dlgExceptionDialog.getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        dlgExceptionDialog.getContentPane().add(sepExceptionDialogControls, gridBagConstraints);

        pnlExceptionDialogContainer.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 5, 5));
        pnlExceptionDialogContainer.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 0.1;
        gridBagConstraints.weighty = 0.1;
        dlgExceptionDialog.getContentPane().add(pnlExceptionDialogContainer, gridBagConstraints);

        btnClose.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnClose.text")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnCloseActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LAST_LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        dlgExceptionDialog.getContentPane().add(btnClose, gridBagConstraints);

        setTitle(org.openide.util.NbBundle.getMessage(DownloadManagerDialog.class, "DownloadManagerDialog.title")); // NOI18N
        setMinimumSize(new java.awt.Dimension(423, 300));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        scpDownloadManagerPanel.setBorder(null);
        scpDownloadManagerPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
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

        btnClearList.setMnemonic(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnClearList.mnemonic").charAt(0));
        btnClearList.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.btnClearList.text")); // NOI18N
        btnClearList.setEnabled(false);
        btnClearList.setFocusPainted(false);
        btnClearList.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnClearListActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(btnClearList, gridBagConstraints);

        lblDownloadsTotalKey.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblDownloadsTotalKey.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(lblDownloadsTotalKey, gridBagConstraints);

        lblDownloadsTotalValue.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDownloadsTotalValue.setText(org.openide.util.NbBundle.getMessage(
                DownloadManagerDialog.class,
                "DownloadManagerDialog.lblDownloadsTotalValue.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        pnlControls.add(lblDownloadsTotalValue, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        pnlControls.add(filler1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(pnlControls, gridBagConstraints);

        bindingGroup.bind();

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * Notifies the DownloadManager singleton that all obsolete downloads should be removed.
     *
     * @param  evt  The event object.
     */
    private void btnClearListActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnClearListActionPerformed
        DownloadManager.instance().removeObsoleteDownloads();
    }                                                                                //GEN-LAST:event_btnClearListActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void btnOKActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnOKActionPerformed
        setJobName(txtJobname.getText());
        setJobNameConfirmed(true);
        dlgJobname.toFront();
        dlgJobname.setVisible(false);
        if (closeMainDialogAfterClosingSubDialog) {
            closeWindow();
        }
    }                                                                         //GEN-LAST:event_btnOKActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void txtJobnameActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_txtJobnameActionPerformed
        setJobName(txtJobname.getText());
        setJobNameConfirmed(true);
        dlgJobname.setVisible(false);
        if (closeMainDialogAfterClosingSubDialog) {
            closeWindow();
        }
    }                                                                              //GEN-LAST:event_txtJobnameActionPerformed

    /**
     * An action listener.
     *
     * @param  evt  The event.
     */
    private void btnCancelActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCancelActionPerformed
        setJobNameConfirmed(false);
        dlgJobname.setVisible(false);
        if (closeMainDialogAfterClosingSubDialog) {
            closeWindow();
        }
    }                                                                             //GEN-LAST:event_btnCancelActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnCloseActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnCloseActionPerformed
        dlgExceptionDialog.setVisible(false);
        if (closeMainDialogAfterClosingSubDialog) {
            closeWindow();
        }
    }                                                                            //GEN-LAST:event_btnCloseActionPerformed

    /**
     * DOCUMENT ME!
     */
    public void destinationDirectoryChanged() {
        lblDestinationDirectory.setText(DownloadManager.instance().getDestinationDirectory().getAbsolutePath()
                    + File.separator);
    }

    /**
     * An action listener.
     *
     * @return  DOCUMENT ME!
     */
    /**
     * Returns the jobname.
     *
     * @return  The jobname.
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * Sets the jobname. Should only be used by xome configuring component.
     *
     * @param  jobName  The jobname.
     */
    public void setJobName(final String jobName) {
        this.jobName = jobName;
    }

    /**
     * If the user wants to be asked for a jobname.
     *
     * @return  A flag indicating whether the user wants to be asked for a jobname.
     */
    public boolean isAskForJobNameEnabled() {
        return askForJobNameEnabled;
    }

    /**
     * Sets the flag whether the user wants to be asked for a jobname.
     *
     * @param  askForJobNameEnabled  The flag whether the user wants to be asked for a jobname.
     */
    public void setAskForJobNameEnabled(final boolean askForJobNameEnabled) {
        this.askForJobNameEnabled = askForJobNameEnabled;
    }

    /**
     * If the user wants the last download to be opened automatically.
     *
     * @return  A flag indicating whether the user wants the last download to be opened automatically.
     */
    public boolean isOpenAutomaticallyEnabled() {
        return openAutomaticallyEnabled;
    }

    /**
     * Sets the flag whether the user wants the last download to be opened automatically.
     *
     * @param  openAutomaticallyEnabled  The flag whether the user wants the last download to be opened automatically.
     */
    public void setOpenAutomaticallyEnabled(final boolean openAutomaticallyEnabled) {
        this.openAutomaticallyEnabled = openAutomaticallyEnabled;
    }

    /**
     * If the user the DownloadManagerDialog to close automatically.
     *
     * @return  A flag indicating whether the user wants the DownloadManagerDialog to close automatically.
     */
    public boolean isCloseAutomaticallyEnabled() {
        return closeAutomaticallyEnabled;
    }

    /**
     * Sets the flag whether the user wants the DownloadManagerDialog to close automatically.
     *
     * @param  closeAutomaticallyEnabled  The flag whether the user wants the DownloadManagerDialog to close
     *                                    automatically.
     */
    public void setCloseAutomaticallyEnabled(final boolean closeAutomaticallyEnabled) {
        this.closeAutomaticallyEnabled = closeAutomaticallyEnabled;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isJobNameConfirmed() {
        return jobNameConfirmed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  jobNameConfirmed  DOCUMENT ME!
     */
    public void setJobNameConfirmed(final boolean jobNameConfirmed) {
        this.jobNameConfirmed = jobNameConfirmed;
    }

    /**
     * Opens a file manager pointing to the destination directory for downloads.
     *
     * @param  event  The event object.
     */
    @Override
    public void downloadListChanged(final DownloadListChangedEvent event) {
        if (event == null) {
            return;
        }

        switch (event.getAction()) {
            case ADDED: {
                final Collection<Download> downloads = event.getDownloads();
                if (openAutomaticallyEnabled) {
                    downloadsToOpen.addAll(downloads);
                }
            }
            case CHANGED_COUNTERS: {
                final int countDownloadsErraneous = DownloadManager.instance().getCountDownloadsErroneous();
                final int countDownloadsCompleted = DownloadManager.instance().getCountDownloadsCompleted();
                final int countDownloadsCancelled = DownloadManager.instance().getCountDownloadsCancelled();
                final int countDownloadsTotal = DownloadManager.instance().getCountDownloadsTotal();

                lblDownloadsTotalValue.setText(String.valueOf(countDownloadsTotal));
                btnClearList.setEnabled((countDownloadsCompleted + countDownloadsErraneous + countDownloadsCancelled)
                            > 0);

                if (openAutomaticallyEnabled) {
                    final Iterator<Download> downloadToOpenIter = downloadsToOpen.iterator();
                    while (downloadToOpenIter.hasNext()) {
                        final Download downloadToOpen = downloadToOpenIter.next();

                        if ((downloadToOpen != null)
                                    && (downloadToOpen.getFileToSaveTo() != null)
                                    && (downloadToOpen.getStatus() == State.COMPLETED)) {
                            try {
                                BrowserLauncher.openURLorFile(downloadToOpen.getFileToSaveTo().getAbsolutePath());
                                downloadToOpenIter.remove();
                            } catch (RuntimeException e) {
                                downloadToOpenIter.remove();
                                LOG.warn("Cannot open download");
                            }
                        }
                    }
                }

                // The second condition ensures that the dialog isn't closed after the list was cleared
                if (closeAutomaticallyEnabled && (countDownloadsTotal > 0)
                            && (countDownloadsTotal == countDownloadsCompleted)) {
                    closeWindow();
                }
            }
        }
    }

    //J-
    private class UserDirectoryFilter extends DocumentFilter {
        /*
         * Most filesystems allow all characters (except NUL) for filenames.
         * NTFS doesn't allow NUL \ / : * ? " < > |
         */

        private static final String FILTER = "[^\u0000\\\\/:*?\"<>|]*";

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (string != null && string.length() > 0) {
                if (!string.matches(FILTER)) {
                    return;
                }
            }

            super.insertString(fb, offset, string, attr);
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (text != null && text.length() > 0) {
                if (!text.matches(FILTER)) {
                    return;
                }
            }

            super.replace(fb, offset, length, text, attrs);
        }

    }
    //J+
}
