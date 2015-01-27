/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.openide.util.Exceptions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.text.ParseException;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class PaginationPanel extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    final ActionListener actionListener;

    private int pageSize = 100;
    private int page = 1;
    private long total = -1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnForward;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JLabel lblPage;
    private javax.swing.JLabel lblPageTotal;
    private javax.swing.JLabel lblRowsTotal;
    private javax.swing.JPanel panPaginationControl;
    private javax.swing.JPanel panPaginationPages;
    private javax.swing.JFormattedTextField tfPageSize;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PaginationPanel object.
     */
    public PaginationPanel() {
        this(null);
    }

    /**
     * Creates new form PaginationPanel.
     *
     * @param  actionListener  DOCUMENT ME!
     */
    public PaginationPanel(final ActionListener actionListener) {
        initComponents();

        this.actionListener = actionListener;
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

        panPaginationControl = new javax.swing.JPanel();
        btnRefresh = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnForward = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        panPaginationPages = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        lblRowsTotal = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        lblPage = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        lblPageTotal = new javax.swing.JLabel();
        tfPageSize = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jSeparator3 = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        panPaginationControl.setMinimumSize(new java.awt.Dimension(145, 10));
        panPaginationControl.setPreferredSize(new java.awt.Dimension(145, 10));
        panPaginationControl.setLayout(new java.awt.GridBagLayout());

        btnRefresh.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/arrow-circle-double.png")));                    // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnRefresh,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.btnRefresh.text")); // NOI18N
        btnRefresh.setToolTipText(org.openide.util.NbBundle.getMessage(
                PaginationPanel.class,
                "PaginationPanel.btnRefresh.toolTipText"));                                                  // NOI18N
        btnRefresh.setBorderPainted(false);
        btnRefresh.setContentAreaFilled(false);
        btnRefresh.setEnabled(false);
        btnRefresh.setMaximumSize(new java.awt.Dimension(29, 29));
        btnRefresh.setMinimumSize(new java.awt.Dimension(29, 29));
        btnRefresh.setPreferredSize(new java.awt.Dimension(29, 29));
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnRefreshActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPaginationControl.add(btnRefresh, gridBagConstraints);

        btnFirst.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/tools/gui/control-stop-180.png"))); // NOI18N
        btnFirst.setToolTipText(org.openide.util.NbBundle.getMessage(
                PaginationPanel.class,
                "PaginationPanel.btnFirst.toolTipText"));                              // NOI18N
        btnFirst.setBorderPainted(false);
        btnFirst.setContentAreaFilled(false);
        btnFirst.setEnabled(false);
        btnFirst.setMaximumSize(new java.awt.Dimension(29, 29));
        btnFirst.setMinimumSize(new java.awt.Dimension(29, 29));
        btnFirst.setPreferredSize(new java.awt.Dimension(29, 29));
        btnFirst.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnFirstActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPaginationControl.add(btnFirst, gridBagConstraints);

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/tools/gui/control-180.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnBack,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.btnBack.text"));           // NOI18N
        btnBack.setToolTipText(org.openide.util.NbBundle.getMessage(
                PaginationPanel.class,
                "PaginationPanel.btnBack.toolTipText"));                                                            // NOI18N
        btnBack.setBorderPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setEnabled(false);
        btnBack.setMaximumSize(new java.awt.Dimension(29, 29));
        btnBack.setMinimumSize(new java.awt.Dimension(29, 29));
        btnBack.setPreferredSize(new java.awt.Dimension(29, 29));
        btnBack.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnBackActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPaginationControl.add(btnBack, gridBagConstraints);

        btnForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/tools/gui/control.png"))); // NOI18N
        btnForward.setToolTipText(org.openide.util.NbBundle.getMessage(
                PaginationPanel.class,
                "PaginationPanel.btnForward.toolTipText"));                                                        // NOI18N
        btnForward.setBorderPainted(false);
        btnForward.setContentAreaFilled(false);
        btnForward.setEnabled(false);
        btnForward.setMaximumSize(new java.awt.Dimension(29, 29));
        btnForward.setMinimumSize(new java.awt.Dimension(29, 29));
        btnForward.setPreferredSize(new java.awt.Dimension(29, 29));
        btnForward.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnForwardActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPaginationControl.add(btnForward, gridBagConstraints);

        btnLast.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/tools/gui/control-stop.png"))); // NOI18N
        btnLast.setToolTipText(org.openide.util.NbBundle.getMessage(
                PaginationPanel.class,
                "PaginationPanel.btnLast.toolTipText"));                                                             // NOI18N
        btnLast.setBorderPainted(false);
        btnLast.setContentAreaFilled(false);
        btnLast.setEnabled(false);
        btnLast.setMaximumSize(new java.awt.Dimension(29, 29));
        btnLast.setMinimumSize(new java.awt.Dimension(29, 29));
        btnLast.setPreferredSize(new java.awt.Dimension(29, 29));
        btnLast.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnLastActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panPaginationControl.add(btnLast, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(panPaginationControl, gridBagConstraints);

        panPaginationPages.setLayout(new java.awt.GridBagLayout());

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel1,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.jLabel1.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panPaginationPages.add(jLabel1, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel2,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.jLabel2.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        panPaginationPages.add(jLabel2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblRowsTotal,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.lblRowsTotal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panPaginationPages.add(lblRowsTotal, gridBagConstraints);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panPaginationPages.add(jSeparator2, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel4,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.jLabel4.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 15, 0, 5);
        panPaginationPages.add(jLabel4, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblPage,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.lblPage.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panPaginationPages.add(lblPage, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            jLabel6,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.jLabel6.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        panPaginationPages.add(jLabel6, gridBagConstraints);

        org.openide.awt.Mnemonics.setLocalizedText(
            lblPageTotal,
            org.openide.util.NbBundle.getMessage(PaginationPanel.class, "PaginationPanel.lblPageTotal.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 8;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panPaginationPages.add(lblPageTotal, gridBagConstraints);

        tfPageSize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        tfPageSize.setMinimumSize(new java.awt.Dimension(50, 27));
        tfPageSize.setPreferredSize(new java.awt.Dimension(50, 27));
        tfPageSize.setValue(50L);
        tfPageSize.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(final java.awt.event.KeyEvent evt) {
                    tfPageSizeKeyPressed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        panPaginationPages.add(tfPageSize, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(panPaginationPages, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(jPanel5, gridBagConstraints);

        jSeparator3.setOrientation(javax.swing.SwingConstants.VERTICAL);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        add(jSeparator3, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnForwardActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnForwardActionPerformed
        doPagination(getPage() + 1);
    }                                                                              //GEN-LAST:event_btnForwardActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnLastActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnLastActionPerformed
        doPagination(getLastPage());
    }                                                                           //GEN-LAST:event_btnLastActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnFirstActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnFirstActionPerformed
        doPagination(getFirstPage());
    }                                                                            //GEN-LAST:event_btnFirstActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnBackActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnBackActionPerformed
        doPagination(getPage() - 1);
    }                                                                           //GEN-LAST:event_btnBackActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRefreshActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRefreshActionPerformed
        refresh();
    }                                                                              //GEN-LAST:event_btnRefreshActionPerformed

    @Override
    public void setEnabled(final boolean enabled) {
        super.setEnabled(enabled);
        btnBack.setEnabled(enabled);
        btnFirst.setEnabled(enabled);
        btnForward.setEnabled(enabled);
        btnLast.setEnabled(enabled);
        btnRefresh.setEnabled(enabled);
        tfPageSize.setEditable(enabled);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tfPageSizeKeyPressed(final java.awt.event.KeyEvent evt) { //GEN-FIRST:event_tfPageSizeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                tfPageSize.commitEdit();
                refresh();
            } catch (final ParseException ex) {
            }
        }
    }                                                                      //GEN-LAST:event_tfPageSizeKeyPressed

    /**
     * DOCUMENT ME!
     */
    private void refresh() {
        doPagination(getPage());
    }

    /**
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
     */
    private void setPage(int page) {
        setPageSize(((Long)tfPageSize.getValue()).intValue());

        if (page > getLastPage()) {
            page = getLastPage();
        }
        if (page < getFirstPage()) {
            page = 1;
        }

        this.page = page;
        updateGuiElements();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  page  DOCUMENT ME!
     */
    private void doPagination(final int page) {
        setPage(page);

        if (actionListener != null) {
            actionListener.actionPerformed(new ActionEvent(this, -1, null));
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void updateGuiElements() {
        lblPage.setText(Integer.toString(page));
        lblRowsTotal.setText(Long.toString(total));
        lblPageTotal.setText(Integer.toString(getLastPage()));
        tfPageSize.setText(Integer.toString(pageSize));

        btnFirst.setEnabled(isEnabled() && (getTotal() > 0) && (page != getFirstPage()));
        btnLast.setEnabled(isEnabled() && (getTotal() > 0) && (page != getLastPage()));
        btnBack.setEnabled(isEnabled() && (getTotal() > 0) && (page > getFirstPage()));
        btnForward.setEnabled(isEnabled() && (getTotal() > 0) && (page < getLastPage()));
        btnRefresh.setEnabled(isEnabled() && (getTotal() > 0));
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        setPage(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  pageSize  DOCUMENT ME!
     */
    private void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getPage() {
        return page;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public long getTotal() {
        return total;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  total  DOCUMENT ME!
     */
    public void setTotal(final long total) {
        this.total = total;
        setPage(getPage());
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getFirstPage() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getLastPage() {
        if (pageSize > 0) {
            return (int)Math.ceil(total / (double)pageSize);
        } else {
            return 0;
        }
    }
}
