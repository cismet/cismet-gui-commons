/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * MultipleDownloadPanel.java
 *
 * Created on 13.07.2011, 11:57:37
 */
package de.cismet.tools.gui.downloadmanager;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.SystemColor;
import java.awt.geom.Path2D;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A subclass of JPanel which visualises one MultipleDownload.
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class MultipleDownloadPanel extends javax.swing.JPanel implements Observer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(MultipleDownloadPanel.class);

    //~ Instance fields --------------------------------------------------------

    Map<Download, JPanel> downloadsInSingleDownloadsPanel = new HashMap<Download, JPanel>();

    private MultipleDownload download;
    private JPanel pnlSingleDownloads;
    private boolean isSingleDownloadsPanelShown;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMessage;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JMenuItem mniRemove;
    private javax.swing.JPopupMenu popContextMenu;
    private javax.swing.JProgressBar prbProgress;
    private javax.swing.JSeparator sepDownloadPanels;
    private javax.swing.JSeparator sepSingleDownloadsPanel;
    private javax.swing.JToggleButton tbtDownloads;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form MultipleDownloadPanel.
     *
     * @param  download  The MultipleDownload object to visualise.
     */
    public MultipleDownloadPanel(final MultipleDownload download) {
        this.download = download;

        initComponents();
        updateComponents();

        initSingleDownloadsPanel();
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

        sepSingleDownloadsPanel = new javax.swing.JSeparator();
        popContextMenu = new javax.swing.JPopupMenu();
        mniRemove = new javax.swing.JMenuItem();
        lblIcon = new javax.swing.JLabel();
        lblTitle = new javax.swing.JLabel();
        lblMessage = new javax.swing.JLabel();
        prbProgress = new javax.swing.JProgressBar();
        sepDownloadPanels = new javax.swing.JSeparator();
        tbtDownloads = new javax.swing.JToggleButton();

        mniRemove.setText(org.openide.util.NbBundle.getMessage(
                MultipleDownloadPanel.class,
                "MultipleDownloadPanel.mniRemove.text")); // NOI18N
        mniRemove.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    mniRemoveActionPerformed(evt);
                }
            });
        popContextMenu.add(mniRemove);

        setComponentPopupMenu(popContextMenu);
        setMaximumSize(new java.awt.Dimension(2147483647, 54));
        setMinimumSize(new java.awt.Dimension(193, 54));
        addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    formMouseClicked(evt);
                }
            });
        setLayout(new java.awt.GridBagLayout());

        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/documenttypes/multiple_closed.png"))); // NOI18N
        lblIcon.setText(org.openide.util.NbBundle.getMessage(
                MultipleDownloadPanel.class,
                "MultipleDownloadPanel.lblIcon.text"));                                                             // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblIcon, gridBagConstraints);

        lblTitle.setFont(new java.awt.Font("Tahoma", 0, 14));
        lblTitle.setText(download.getTitle());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblTitle, gridBagConstraints);

        lblMessage.setBackground(new java.awt.Color(255, 102, 0));
        lblMessage.setText(org.openide.util.NbBundle.getMessage(
                MultipleDownloadPanel.class,
                "MultipleDownloadPanel.lblMessage.text")); // NOI18N
        lblMessage.setMaximumSize(new java.awt.Dimension(32767, 15));
        lblMessage.setMinimumSize(new java.awt.Dimension(10, 15));
        lblMessage.setPreferredSize(new java.awt.Dimension(8, 15));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(lblMessage, gridBagConstraints);

        prbProgress.setMaximum(download.getDownloadsTotal());
        prbProgress.setBorderPainted(false);
        prbProgress.setMaximumSize(new java.awt.Dimension(32767, 15));
        prbProgress.setMinimumSize(new java.awt.Dimension(10, 15));
        prbProgress.setPreferredSize(new java.awt.Dimension(146, 15));
        prbProgress.setString(org.openide.util.NbBundle.getMessage(
                MultipleDownloadPanel.class,
                "MultipleDownloadPanel.prbProgress.string")); // NOI18N
        prbProgress.setStringPainted(true);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(prbProgress, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sepDownloadPanels, gridBagConstraints);

        tbtDownloads.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/listDownloads.png")));          // NOI18N
        tbtDownloads.setText(org.openide.util.NbBundle.getMessage(
                MultipleDownloadPanel.class,
                "MultipleDownloadPanel.tbtDownloads.text"));                                                     // NOI18N
        tbtDownloads.setBorderPainted(false);
        tbtDownloads.setContentAreaFilled(false);
        tbtDownloads.setFocusPainted(false);
        tbtDownloads.setSelectedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/res/listDownloads_selected.png"))); // NOI18N
        tbtDownloads.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtDownloadsActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(tbtDownloads, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * The action listener for the more button.
     *
     * @param  evt  An action event.
     */
    private void tbtDownloadsActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_tbtDownloadsActionPerformed
        if (isSingleDownloadsPanelShown) {
            hideSingleDownloads();
        } else {
            showSingleDownloads();
        }
    }                                                                                //GEN-LAST:event_tbtDownloadsActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void formMouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_formMouseClicked
        if (evt.getClickCount() > 1) {
            if (download.getCaughtException() != null) {
                DownloadManagerDialog.showExceptionDialog(download);
            } else if (download.getDownloadsTotal() > 0) {
                if (isSingleDownloadsPanelShown) {
                    hideSingleDownloads();
                } else {
                    showSingleDownloads();
                }
            }
        }
    }                                                                    //GEN-LAST:event_formMouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void mniRemoveActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_mniRemoveActionPerformed
        DownloadManager.instance().removeDownload(download);
    }                                                                             //GEN-LAST:event_mniRemoveActionPerformed

    /**
     * As soon as a download changes its state, this method is called by the download (Observer pattern).
     *
     * @param  o    The Observable which called the method.
     * @param  arg  The arguments specified by the Observable.
     */
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
     * Updates the components according to the new state of a download.
     */
    private void updateComponents() {
        switch (download.getStatus()) {
            case WAITING: {
                prbProgress.setVisible(false);
                lblMessage.setVisible(true);
                mniRemove.setEnabled(true);

                break;
            }
            case RUNNING: {
                lblMessage.setVisible(false);
                mniRemove.setEnabled(false);
                lblTitle.setForeground(SystemColor.textText);

                prbProgress.setVisible(true);
                if (download.getDownloadsCompleted() == 0) {
                    prbProgress.setIndeterminate(true);
                    prbProgress.setString(NbBundle.getMessage(
                            MultipleDownloadPanel.class,
                            "MultipleDownloadPanel.prbProgress.string.running.allRunning"));
                } else {
                    prbProgress.setIndeterminate(false);
                    prbProgress.setString(NbBundle.getMessage(
                            MultipleDownloadPanel.class,
                            "MultipleDownloadPanel.prbProgress.string.running",
                            download.getDownloadsCompleted(),
                            download.getDownloadsTotal()));
                    prbProgress.setValue(download.getDownloadsCompleted());
                }
                break;
            }
            case RUNNING_WITH_ERROR: {
                lblMessage.setVisible(false);
                mniRemove.setEnabled(false);
                lblTitle.setForeground(SystemColor.textText);

                prbProgress.setVisible(true);
                prbProgress.setString(NbBundle.getMessage(
                        MultipleDownloadPanel.class,
                        "MultipleDownloadPanel.prbProgress.string.running_with_error",
                        download.getDownloadsCompleted(),
                        download.getDownloadsTotal(),
                        download.getDownloadsErroneous()));
                prbProgress.setValue(download.getDownloadsCompleted());
                prbProgress.setBackground(Color.pink);
                setBackground(Color.pink);
                break;
            }
            case COMPLETED: {
                prbProgress.setVisible(false);
                mniRemove.setEnabled(true);
                lblTitle.setForeground(SystemColor.textInactiveText);
                download.deleteObserver(this);

                lblMessage.setVisible(true);
                lblMessage.setText(NbBundle.getMessage(
                        MultipleDownloadPanel.class,
                        "MultipleDownloadPanel.lblMessage.text.completed",
                        download.getDownloadsCompleted()));
                break;
            }
            case COMPLETED_WITH_ERROR: {
                prbProgress.setVisible(false);
                mniRemove.setEnabled(true);
                lblTitle.setForeground(SystemColor.textInactiveText);
                setBackground(Color.pink);
                download.deleteObserver(this);

                lblMessage.setVisible(true);
                lblMessage.setText(NbBundle.getMessage(
                        MultipleDownloadPanel.class,
                        "MultipleDownloadPanel.lblMessage.text.completed_with_error",
                        download.getDownloadsCompleted(),
                        download.getDownloadsErroneous()));
                break;
            }
        }
    }

    /**
     * Initialises the panel which shows the single downloads.
     */
    private void initSingleDownloadsPanel() {
        if (pnlSingleDownloads != null) {
            return;
        }

        pnlSingleDownloads = new JPanel();
        pnlSingleDownloads.setLayout(new BoxLayout(pnlSingleDownloads, BoxLayout.PAGE_AXIS));

        addDownloadsToPnlSingleDownloads(download.getDownloads());
    }

    /**
     * Shows the panel visualising the single downloads.
     */
    private void showSingleDownloads() {
        if (isSingleDownloadsPanelShown) {
            return;
        }

        tbtDownloads.setSelected(true);
        lblIcon.setIcon(new ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/documenttypes/fallback_multiple.png")));

        remove(sepDownloadPanels);

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(sepDownloadPanels, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        add(pnlSingleDownloads, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(sepSingleDownloadsPanel, constraints);

        final int width = getMaximumSize().width;
        final int height = getMaximumSize().height;
        setMaximumSize(new Dimension(width, height + (download.getDownloadsTotal() * 44)));

        isSingleDownloadsPanelShown = true;

        revalidate();
        repaint();
    }

    /**
     * Hides the panel visualising the single downloads.
     */
    private void hideSingleDownloads() {
        if (!isSingleDownloadsPanelShown) {
            return;
        }

        tbtDownloads.setSelected(false);
        lblIcon.setIcon(new ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/downloadmanager/documenttypes/multiple_closed.png")));

        remove(sepDownloadPanels);
        remove(sepSingleDownloadsPanel);
        remove(pnlSingleDownloads);

        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        add(sepDownloadPanels, constraints);

        final int width = getMaximumSize().width;
        setMaximumSize(new Dimension(width, 54));

        isSingleDownloadsPanelShown = false;

        revalidate();
        repaint();
    }
    /**
     * DOCUMENT ME!
     */
    public void redrawEncapsulatedDownloads() {
        addDownloadsToPnlSingleDownloads(download.getDownloads());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  downloads  DOCUMENT ME!
     */
    private void addDownloadsToPnlSingleDownloads(final Collection<? extends Download> downloads) {
        final Iterator<? extends Download> iterDownloads = downloads.iterator();
        Branch.Position position = null;
        for (int i = 0; iterDownloads.hasNext(); i++) {
            final Download singleDownload = iterDownloads.next();
            if (!downloadsInSingleDownloadsPanel.containsKey(singleDownload)) {
                final DownloadPanel pnlSingleDownloadPanel = new DownloadPanel(
                        singleDownload,
                        true,
                        !iterDownloads.hasNext());
                singleDownload.addObserver(pnlSingleDownloadPanel);

                if (i == (download.getDownloads().size() - 1)) {
                    position = Branch.Position.LAST;
                } else if (i == 0) {
                    position = Branch.Position.FIRST;
                } else {
                    position = Branch.Position.NORMAL;
                }
                final JComponent braBranch = new Branch(pnlSingleDownloadPanel, position);

                final JPanel pnlSingleDownload = new JPanel();
                pnlSingleDownload.setLayout(new BoxLayout(pnlSingleDownload, BoxLayout.LINE_AXIS));
                pnlSingleDownload.add(braBranch);
                pnlSingleDownload.add(pnlSingleDownloadPanel);
                downloadsInSingleDownloadsPanel.put(singleDownload, pnlSingleDownload);
                pnlSingleDownloads.add(pnlSingleDownload);
            }
        }
        tbtDownloads.setVisible(!downloads.isEmpty());
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * A visual component drawing branches in the panel which shows the single downloads.
     *
     * @version  $Revision$, $Date$
     */
    private static class Branch extends JComponent {

        //~ Static fields/initializers -----------------------------------------

        private static final int INSET = 4;

        //~ Enums --------------------------------------------------------------

        /**
         * An enumeration for all positions a branch component can be used for.
         *
         * @version  $Revision$, $Date$
         */
        enum Position {

            //~ Enum constants -------------------------------------------------

            FIRST, NORMAL, LAST
        }

        //~ Instance fields ----------------------------------------------------

        private Position position;
        private JComponent parent;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Branch object.
         *
         * @param  parent  The parent component of the branch. It's needed to draw the background of the branch in the
         *                 same color.
         */
        public Branch(final JComponent parent) {
            this(parent, Position.NORMAL);
        }

        /**
         * Creates a new Branch object.
         *
         * @param  parent    The parent component of the branch. It's needed to draw the background of the branch in the
         *                   same color.
         * @param  position  The position of the branch.
         */
        public Branch(final JComponent parent, final Position position) {
            this.parent = parent;
            this.position = position;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void paintComponent(final Graphics g) {
            super.paintComponent(g);

            if (!(g instanceof Graphics2D)) {
                return;
            }

            final Graphics2D g2d = (Graphics2D)g;

            g2d.setColor(parent.getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.setColor(SystemColor.textInactiveText);
            g2d.setStroke(new BasicStroke(
                    1,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10,
                    new float[] { 1, 1 },
                    0));
            final Path2D shape = new Path2D.Float();

            if (position == Position.FIRST) {
                shape.moveTo((getWidth() / 2), INSET);
            } else {
                shape.moveTo((getWidth() / 2), 0);
            }

            if (position == Position.LAST) {
                shape.lineTo(getWidth() / 2, getHeight() / 2);
            } else {
                shape.lineTo(getWidth() / 2, getHeight());
            }

            shape.moveTo(getWidth() / 2, getHeight() / 2);
            shape.lineTo(getWidth() - INSET, getHeight() / 2);

            g2d.draw(shape);
        }

        @Override
        public Dimension getMinimumSize() {
            return new Dimension(42, 44);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(42, 44);
        }
    }
}
