/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CismetDeveloperOptionsPanel.java
 *
 * Created on 15.09.2011, 16:11:14
 */
package de.cismet.lookupoptions.options;

import groovy.ui.Console;

import org.openide.util.lookup.ServiceProvider;

import java.awt.CardLayout;

import de.cismet.lookupoptions.AbstractOptionsPanel;
import de.cismet.lookupoptions.OptionsPanelController;

import de.cismet.tools.BrowserLauncher;
import de.cismet.tools.StaticDebuggingTools;

import de.cismet.tools.gui.StaticSwingTools;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsPanelController.class)
public class CismetDeveloperOptionsPanel extends AbstractOptionsPanel implements OptionsPanelController {

    //~ Static fields/initializers ---------------------------------------------

    private static final String OPTION_NAME = org.openide.util.NbBundle.getMessage(
            ProxyOptionsPanel.class,
            "CismetDeveloperOptionsPanel.OptionController.name");

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JPanel panLock;
    private javax.swing.JPanel panUnlocked;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form CismetDeveloperOptionsPanel.
     */
    public CismetDeveloperOptionsPanel() {
        super(OPTION_NAME, CismetDeveloperCategory.class);
        initComponents();
        ((CardLayout)getLayout()).show(this, "Locked");
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        panLock = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPasswordField1 = new javax.swing.JPasswordField();
        panUnlocked = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        panLock.setLayout(new java.awt.BorderLayout());

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/lookupoptions/options/locked.png"))); // NOI18N
        jLabel1.setText(org.openide.util.NbBundle.getMessage(
                CismetDeveloperOptionsPanel.class,
                "CismetDeveloperOptionsPanel.jLabel1.text"));                            // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    jLabel1MouseClicked(evt);
                }
            });
        panLock.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.setMinimumSize(new java.awt.Dimension(100, 100));
        jPanel1.setOpaque(false);
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 50));
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPasswordField1.setText(org.openide.util.NbBundle.getMessage(
                CismetDeveloperOptionsPanel.class,
                "CismetDeveloperOptionsPanel.jPasswordField1.text")); // NOI18N
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jPasswordField1ActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 120;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(33, 225, 39, 230);
        jPanel1.add(jPasswordField1, gridBagConstraints);

        panLock.add(jPanel1, java.awt.BorderLayout.PAGE_END);

        add(panLock, "Locked");

        jButton1.setText(org.openide.util.NbBundle.getMessage(
                CismetDeveloperOptionsPanel.class,
                "CismetDeveloperOptionsPanel.jButton1.text")); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

        jButton2.setText(org.openide.util.NbBundle.getMessage(
                CismetDeveloperOptionsPanel.class,
                "CismetDeveloperOptionsPanel.jButton2.text")); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

        jButton3.setText(org.openide.util.NbBundle.getMessage(
                CismetDeveloperOptionsPanel.class,
                "CismetDeveloperOptionsPanel.jButton3.text")); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });

        final org.jdesktop.layout.GroupLayout panUnlockedLayout = new org.jdesktop.layout.GroupLayout(panUnlocked);
        panUnlocked.setLayout(panUnlockedLayout);
        panUnlockedLayout.setHorizontalGroup(
            panUnlockedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                panUnlockedLayout.createSequentialGroup().addContainerGap(339, Short.MAX_VALUE).add(jButton1)
                            .addContainerGap()).add(
                panUnlockedLayout.createSequentialGroup().addContainerGap().add(
                    panUnlockedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(
                        org.jdesktop.layout.GroupLayout.LEADING,
                        jButton3,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE).add(
                        org.jdesktop.layout.GroupLayout.LEADING,
                        jButton2,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        org.jdesktop.layout.GroupLayout.DEFAULT_SIZE,
                        Short.MAX_VALUE)).addContainerGap(275, Short.MAX_VALUE)));
        panUnlockedLayout.setVerticalGroup(
            panUnlockedLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                panUnlockedLayout.createSequentialGroup().addContainerGap().add(jButton2).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED).add(jButton3).addPreferredGap(
                    org.jdesktop.layout.LayoutStyle.RELATED,
                    251,
                    Short.MAX_VALUE).add(jButton1).addContainerGap()));

        add(panUnlocked, "Unlocked");
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jLabel1MouseClicked(final java.awt.event.MouseEvent evt) { //GEN-FIRST:event_jLabel1MouseClicked
        if (StaticDebuggingTools.checkHomeForFile("cismetDeveloper")) {
            ((CardLayout)getLayout()).show(this, "Unlocked");
        }
    }                                                                       //GEN-LAST:event_jLabel1MouseClicked

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        ((CardLayout)getLayout()).show(this, "Locked");
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        final Console console = new Console();
        console.getShell().evaluate("import de.cismet.cismap.commons.interaction.*;");
        new Thread(new Runnable() {

                @Override
                public void run() {
                    console.run();
                }
            }).start();
        try {
            BrowserLauncher.openURL("https://gist.github.com/4ef6a11c7ad17e2390ed/");
        } catch (Exception ex) {
            log.error("Could not open Groovy GIST");
        }
    } //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jPasswordField1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jPasswordField1ActionPerformed
// TODO add your handling code here:
    } //GEN-LAST:event_jPasswordField1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton3ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton3ActionPerformed
        final Log4JQuickConfig dialog = Log4JQuickConfig.getSingletonInstance();
        StaticSwingTools.showDialog(this, dialog, true);
    }                                                                            //GEN-LAST:event_jButton3ActionPerformed

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip() {
        return org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "CismetDeveloperOptionsPanel.OptionController.tooltip");
    }
}
