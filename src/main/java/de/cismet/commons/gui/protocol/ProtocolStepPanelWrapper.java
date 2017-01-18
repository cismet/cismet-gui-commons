/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.commons.gui.protocol;

import lombok.AccessLevel;
import lombok.Getter;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.SwingUtilities;

import de.cismet.commons.gui.protocol.listener.ProtocolStepListener;
import de.cismet.commons.gui.protocol.listener.ProtocolStepListenerEvent;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolStepPanelWrapper extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            ProtocolStepPanelWrapper.class);

    //~ Instance fields --------------------------------------------------------

    @Getter(AccessLevel.PROTECTED)
    private final ProtocolStep protocolStep;

    @Getter(AccessLevel.PROTECTED)
    private final ProtocolStepListener protocolStepListener;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnRemove;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblMain;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panHeader;
    private javax.swing.JPanel panIcon;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panRemove;
    private javax.swing.JPanel panTitle;
    private javax.swing.JSeparator sepHeaderMain;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ProtocolStepPanelWrapper.
     *
     * @param  protocolStep   DOCUMENT ME!
     * @param  showImmediate  DOCUMENT ME!
     */
    public ProtocolStepPanelWrapper(final ProtocolStep protocolStep, final boolean showImmediate) {
        this.protocolStep = protocolStep;

        initComponents();

        if (showImmediate) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("showImmediate is true => show step");
            }
            protocolStepListener = null;
            showStep();
        } else if (protocolStep.isInited()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("protocolStep is already initialized => show step");
            }
            protocolStepListener = null;
            showStep();
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("protocolStep not yet initialized => registering listener");
            }
            protocolStepListener = new ProtocolStepListener() {

                    @Override
                    public void parametersChanged(final ProtocolStepListenerEvent event) {
                        final ProtocolStep step = event.getProtocolStep();
                        if (step.equals(protocolStep)) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("protocolStep initialized => show step");
                            }
                            SwingUtilities.invokeLater(new Runnable() {

                                    @Override
                                    public void run() {
                                        showStep();
                                    }
                                });
                        }
                    }
                };
            protocolStep.addProtocolStepListener(getProtocolStepListener());
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    public final void showStep() {
        ProtocolStepPanel protocolStepPanel = null;
        try {
            protocolStepPanel = getProtocolStep().visualize();
        } catch (final Exception ex) {
            LOG.warn("error while creating step panel", ex);
        }

        if (protocolStepPanel != null) {
            final Component iconComponent = protocolStepPanel.getIconComponent();
            final Component titleComponent = protocolStepPanel.getTitleComponent();
            final Component mainComponent = protocolStepPanel.getMainComponent();

            if (iconComponent != null) {
                panIcon.removeAll();
                panIcon.add(iconComponent, BorderLayout.CENTER);
            }

            if (titleComponent != null) {
                panTitle.removeAll();
                panTitle.add(titleComponent, BorderLayout.CENTER);
            }

            if (mainComponent != null) {
                panMain.removeAll();
                panMain.add(mainComponent, BorderLayout.CENTER);
            }
            revalidate();
            repaint();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panHeader = new javax.swing.JPanel();
        panRemove = new javax.swing.JPanel();
        btnRemove = new javax.swing.JButton();
        panTitle = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        panIcon = new javax.swing.JPanel();
        lblIcon = new javax.swing.JLabel();
        panMain = new javax.swing.JPanel();
        lblMain = new javax.swing.JLabel();
        sepHeaderMain = new javax.swing.JSeparator();

        setBorder(javax.swing.BorderFactory.createEtchedBorder());
        setLayout(new java.awt.GridBagLayout());

        panHeader.setLayout(new java.awt.GridBagLayout());

        panRemove.setLayout(new java.awt.GridBagLayout());

        btnRemove.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/remove_step.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnRemove,
            org.openide.util.NbBundle.getMessage(
                ProtocolStepPanelWrapper.class,
                "ProtocolStepPanelWrapper.btnRemove.text"));                                 // NOI18N
        btnRemove.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolStepPanelWrapper.class,
                "ProtocolStepPanelWrapper.btnRemove.toolTipText"));                          // NOI18N
        btnRemove.setBorderPainted(false);
        btnRemove.setContentAreaFilled(false);
        btnRemove.setFocusPainted(false);
        btnRemove.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnRemoveActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_END;
        panRemove.add(btnRemove, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panHeader.add(panRemove, gridBagConstraints);

        panTitle.setMinimumSize(new java.awt.Dimension(100, 30));
        panTitle.setLayout(new java.awt.BorderLayout());

        org.openide.awt.Mnemonics.setLocalizedText(lblTitle, protocolStep.getMetaInfo().getDescription());
        panTitle.add(lblTitle, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panHeader.add(panTitle, gridBagConstraints);

        panIcon.setMinimumSize(new java.awt.Dimension(30, 30));
        panIcon.setPreferredSize(new java.awt.Dimension(30, 30));
        panIcon.setLayout(new java.awt.BorderLayout());

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(
            lblIcon,
            org.openide.util.NbBundle.getMessage(
                ProtocolStepPanelWrapper.class,
                "ProtocolStepPanelWrapper.lblIcon.text")); // NOI18N
        panIcon.add(lblIcon, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panHeader.add(panIcon, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(panHeader, gridBagConstraints);

        panMain.setLayout(new java.awt.BorderLayout());

        lblMain.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        org.openide.awt.Mnemonics.setLocalizedText(
            lblMain,
            org.openide.util.NbBundle.getMessage(
                ProtocolStepPanelWrapper.class,
                "ProtocolStepPanelWrapper.lblMain.text")); // NOI18N
        panMain.add(lblMain, java.awt.BorderLayout.CENTER);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(panMain, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        add(sepHeaderMain, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnRemoveActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnRemoveActionPerformed
        ProtocolHandler.getInstance().removeStep(getProtocolStep());

        if (getProtocolStepListener() != null) {
            getProtocolStep().removeProtocolStepListener(getProtocolStepListener());
        }
    } //GEN-LAST:event_btnRemoveActionPerformed
}
