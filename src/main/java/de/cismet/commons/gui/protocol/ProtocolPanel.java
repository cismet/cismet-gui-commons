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

import com.jgoodies.looks.HeaderStyle;
import com.jgoodies.looks.Options;

import org.jfree.ui.ExtensionFileFilter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.io.File;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import de.cismet.commons.gui.protocol.impl.CommentProtocolStepImpl;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolPanel extends javax.swing.JPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProtocolPanel.class);
    private static final transient String FILE_SUFFIX = "prot";

    //~ Instance fields --------------------------------------------------------

    private final ProtocolHandler handler;
    private final FileFilter fileFilter = new ExtensionFileFilter(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.filefilter.jsonfiledesc"),
            FILE_SUFFIX);
    private final Map<ProtocolStep, ProtocolStepPanelWrapper> protocolStepToWrapperMap =
        new HashMap<ProtocolStep, ProtocolStepPanelWrapper>();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JFileChooser jFileChooser1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panFiller;
    private javax.swing.JPanel panSteps;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ProtocolPanel.
     */
    public ProtocolPanel() {
        this(null);
    }

    /**
     * Creates a new ProtocolPanel object.
     *
     * @param  handler  DOCUMENT ME!
     */
    public ProtocolPanel(final ProtocolHandler handler) {
        initComponents();

        if (handler != null) {
            this.handler = handler;
        } else {
            this.handler = ProtocolHandler.getInstance();
        }
        this.handler.addProtocolHandlerListener(new ProtocolHandlerListenerImpl());

        jToggleButton1.setSelected(this.handler.isRecordEnabled());
        for (final ProtocolStep step : this.handler.getAllSteps()) {
            addStep(step, true);
        }

        for (final ProtocolStepToolbarItem toolbarItem : this.handler.getToolbarItems()) {
            if (toolbarItem instanceof Component) {
                jToolBar1.add((Component)toolbarItem);
            } else if (toolbarItem instanceof Action) {
                jToolBar1.add((Action)toolbarItem);
            }
        }
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

        jFileChooser1 = new javax.swing.JFileChooser();
        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        filler1 = (javax.swing.Box.Filler)Box.createHorizontalGlue();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        panSteps = new javax.swing.JPanel();
        panFiller = new javax.swing.JPanel();

        jFileChooser1.setFileFilter(fileFilter);

        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_off.png")));                  // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jToggleButton1,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.jToggleButton1.text")); // NOI18N
        jToggleButton1.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.jToggleButton1.toolTipText"));                                                // NOI18N
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setRolloverIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_on.png")));                   // NOI18N
        jToggleButton1.setRolloverSelectedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_off.png")));                  // NOI18N
        jToggleButton1.setSelectedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_on.png")));                   // NOI18N
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jToggleButton1ActionPerformed(evt);
                }
            });
        jToolBar1.add(jToggleButton1);
        jToolBar1.add(jSeparator2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/commons/gui/protocol/load.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton3,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.jButton3.text"));                   // NOI18N
        jButton3.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.jButton3.toolTipText"));                                                                  // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton3ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton3);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/commons/gui/protocol/save.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            jButton2,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.jButton2.text"));                   // NOI18N
        jButton2.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.jButton2.toolTipText"));                                                                  // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator3);

        org.openide.awt.Mnemonics.setLocalizedText(
            jButton1,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.jButton1.text")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });
        jToolBar1.add(jButton1);

        jPanel1.setOpaque(false);
        jPanel1.add(filler1);

        jToolBar1.add(jPanel1);

        jToolBar1.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        add(jToolBar1, java.awt.BorderLayout.NORTH);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        panSteps.setLayout(new java.awt.GridBagLayout());

        panFiller.setPreferredSize(new java.awt.Dimension(100, 1));

        final javax.swing.GroupLayout panFillerLayout = new javax.swing.GroupLayout(panFiller);
        panFiller.setLayout(panFillerLayout);
        panFillerLayout.setHorizontalGroup(
            panFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                100,
                Short.MAX_VALUE));
        panFillerLayout.setVerticalGroup(
            panFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                315,
                Short.MAX_VALUE));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panSteps.add(panFiller, gridBagConstraints);

        jScrollPane1.setViewportView(panSteps);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel4.add(jScrollPane1, gridBagConstraints);

        add(jPanel4, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton1ActionPerformed
        handler.clearSteps();
    }                                                                            //GEN-LAST:event_jButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jToggleButton1ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jToggleButton1ActionPerformed
        handler.setRecordEnabled(jToggleButton1.isSelected());
    }                                                                                  //GEN-LAST:event_jToggleButton1ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton2ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton2ActionPerformed
        final int status = jFileChooser1.showSaveDialog(StaticSwingTools.getParentFrame(this));
        if (status == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = jFileChooser1.getSelectedFile();

            new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            final File fileToSave;
                            if (selectedFile.getName().toLowerCase().endsWith("." + FILE_SUFFIX)) {
                                fileToSave = selectedFile;
                            } else {
                                fileToSave = new File(selectedFile.getAbsolutePath() + "." + FILE_SUFFIX);
                            }
                            handler.writeToFile(fileToSave);
                        } catch (final Exception ex) {
                            LOG.error(ex, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                    }
                }.execute();
        } else if (status == JFileChooser.CANCEL_OPTION) {
        }
    } //GEN-LAST:event_jButton2ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void jButton3ActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_jButton3ActionPerformed
        final int status = jFileChooser1.showOpenDialog(StaticSwingTools.getParentFrame(this));
        if (status == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = jFileChooser1.getSelectedFile();
            new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            handler.readFromFile(selectedFile);
                        } catch (final Exception ex) {
                            LOG.error(ex, ex);
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                    }
                }.execute();
        } else if (status == JFileChooser.CANCEL_OPTION) {
        }
    } //GEN-LAST:event_jButton3ActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  step  DOCUMENT ME!
     */
    private void removeStep(final ProtocolStep step) {
        final ProtocolStepPanelWrapper wrapper = protocolStepToWrapperMap.get(step);
        if (wrapper != null) {
            panSteps.remove(wrapper);
            panSteps.revalidate();
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  step           DOCUMENT ME!
     * @param  showImmediate  DOCUMENT ME!
     */
    private void addStep(final ProtocolStep step, final boolean showImmediate) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("adding GUI for protocol " + step.getMetaInfo().getKey()
                        + "' and rendering it immediately: " + showImmediate);
        }

        if (SwingUtilities.isEventDispatchThread()) {
            final ProtocolStepPanelWrapper wrapper = new ProtocolStepPanelWrapper(
                    step,
                    showImmediate
                            || step.isInited());

            panSteps.remove(panFiller);
            final GridBagConstraints constraints = new GridBagConstraints();
            constraints.fill = GridBagConstraints.BOTH;
            constraints.weightx = 1;
            constraints.gridx = 0;
            constraints.gridy = GridBagConstraints.RELATIVE;
            constraints.insets = new Insets(5, 5, 5, 5);
//            wrapper.setPreferredSize(panSteps.getSize());
            panSteps.add(wrapper, constraints);

            constraints.weighty = 1;
            panSteps.add(panFiller, constraints);

            protocolStepToWrapperMap.put(step, wrapper);

            panSteps.revalidate();

            jScrollPane1.getVerticalScrollBar().setValue(jScrollPane1.getVerticalScrollBar().getMaximum());
            repaint();
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        addStep(step, showImmediate);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void clearSteps() {
        if (SwingUtilities.isEventDispatchThread()) {
            panSteps.removeAll();

            panSteps.revalidate();
            repaint();
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        clearSteps();
                    }
                });
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ProtocolHandlerListenerImpl extends ProtocolHandlerAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void recordStateChanged(final ProtocolHandlerListenerEvent event) {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        jToggleButton1.setSelected(event.getSourceProtocolHander().isRecordEnabled());
                    }
                });
        }

        @Override
        public void stepAdded(final ProtocolHandlerListenerEvent event) {
            // -> don't render GUI after add! wait for initParameters()!
            addStep((ProtocolStep)event.getEventObject(), false);
        }

        @Override
        public void stepRemoved(final ProtocolHandlerListenerEvent event) {
            removeStep((ProtocolStep)event.getEventObject());
        }

        @Override
        public void stepsCleared(final ProtocolHandlerListenerEvent event) {
            clearSteps();
        }

        @Override
        public void stepsRestored(final ProtocolHandlerListenerEvent event) {
            clearSteps();
            for (final ProtocolStep step : event.getSourceProtocolHander().getAllSteps()) {
                // -> render GUI after restore!
                addStep(step, true);
            }
        }
    }
}
