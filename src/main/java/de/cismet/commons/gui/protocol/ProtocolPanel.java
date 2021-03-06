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

import lombok.AccessLevel;
import lombok.Getter;

import org.jfree.ui.ExtensionFileFilter;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import java.io.File;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;

import de.cismet.commons.gui.protocol.listener.ProtocolHandlerAdapter;
import de.cismet.commons.gui.protocol.listener.ProtocolHandlerListenerEvent;

import de.cismet.tools.gui.GUIWindow;
import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@org.openide.util.lookup.ServiceProvider(service = GUIWindow.class)
public class ProtocolPanel extends javax.swing.JPanel implements GUIWindow {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ProtocolPanel.class);
    private static final transient String FILE_SUFFIX = "prot";

    //~ Instance fields --------------------------------------------------------

    @Getter(AccessLevel.PRIVATE)
    private final ProtocolHandler handler;

    @Getter(AccessLevel.PRIVATE)
    private final FileFilter fileFilter = new ExtensionFileFilter(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.filefilter.jsonfiledesc"),
            FILE_SUFFIX);

    @Getter(AccessLevel.PRIVATE)
    private final Map<ProtocolStep, ProtocolStepPanelWrapper> protocolStepToWrapperMap =
        new HashMap<ProtocolStep, ProtocolStepPanelWrapper>();

    @Getter(AccessLevel.PRIVATE)
    private final GridBagConstraints wrapperConstraints = createWrapperConstraints();

    @Getter(AccessLevel.PRIVATE)
    private final GridBagConstraints fillerConstraints = createFillerConstraints();

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnLoad;
    private javax.swing.JButton btnSave;
    private javax.swing.JFileChooser fchMain;
    private javax.swing.Box.Filler filToolbar;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panSteps;
    private javax.swing.JPanel panStepsFiller;
    private javax.swing.JPanel panToolbarFiller;
    private javax.swing.JScrollPane scpSteps;
    private javax.swing.JToolBar.Separator sep1;
    private javax.swing.JToolBar.Separator sep2;
    private javax.swing.JToggleButton tbtRecordOnOff;
    private javax.swing.JToolBar tlbMain;
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
        if (handler != null) {
            this.handler = handler;
        } else {
            this.handler = ProtocolHandler.getInstance();
        }

        initComponents();

        getHandler().addProtocolHandlerListener(new ProtocolHandlerListenerImpl());

        addSteps(getHandler().getAllSteps(), true);
        addToolbarItems(getHandler().getToolbarItems());
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static GridBagConstraints createWrapperConstraints() {
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1;
        constraints.gridx = 0;
        constraints.gridy = GridBagConstraints.RELATIVE;
        constraints.insets = new Insets(5, 5, 5, 5);
        return constraints;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static GridBagConstraints createFillerConstraints() {
        final GridBagConstraints constraints = createWrapperConstraints();
        constraints.weighty = 1;
        return constraints;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        fchMain = new javax.swing.JFileChooser();
        tlbMain = new javax.swing.JToolBar();
        tbtRecordOnOff = new javax.swing.JToggleButton();
        sep1 = new javax.swing.JToolBar.Separator();
        btnLoad = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        sep2 = new javax.swing.JToolBar.Separator();
        btnClear = new javax.swing.JButton();
        panToolbarFiller = new javax.swing.JPanel();
        filToolbar = (javax.swing.Box.Filler)Box.createHorizontalGlue();
        panMain = new javax.swing.JPanel();
        scpSteps = new javax.swing.JScrollPane();
        panSteps = new javax.swing.JPanel();
        panStepsFiller = new javax.swing.JPanel();

        fchMain.setFileFilter(getFileFilter());

        setLayout(new java.awt.BorderLayout());

        tlbMain.setFloatable(false);
        tlbMain.setRollover(true);
        tlbMain.setFocusable(false);

        tbtRecordOnOff.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_off.png")));                  // NOI18N
        tbtRecordOnOff.setSelected(getHandler().isRecordEnabled());
        org.openide.awt.Mnemonics.setLocalizedText(
            tbtRecordOnOff,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.tbtRecordOnOff.text")); // NOI18N
        tbtRecordOnOff.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.tbtRecordOnOff.toolTipText"));                                                // NOI18N
        tbtRecordOnOff.setBorderPainted(false);
        tbtRecordOnOff.setFocusPainted(false);
        tbtRecordOnOff.setFocusable(false);
        tbtRecordOnOff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        tbtRecordOnOff.setSelectedIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/record_on.png")));                   // NOI18N
        tbtRecordOnOff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        tbtRecordOnOff.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    tbtRecordOnOffActionPerformed(evt);
                }
            });
        tlbMain.add(tbtRecordOnOff);
        tlbMain.add(sep1);

        btnLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/commons/gui/protocol/load.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnLoad,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.btnLoad.text"));                   // NOI18N
        btnLoad.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.btnLoad.toolTipText"));                                                                  // NOI18N
        btnLoad.setFocusPainted(false);
        btnLoad.setFocusable(false);
        btnLoad.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnLoad.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnLoad.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnLoadActionPerformed(evt);
                }
            });
        tlbMain.add(btnLoad);

        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/cismet/commons/gui/protocol/save.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnSave,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.btnSave.text"));                   // NOI18N
        btnSave.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.btnSave.toolTipText"));                                                                  // NOI18N
        btnSave.setFocusPainted(false);
        btnSave.setFocusable(false);
        btnSave.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSave.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSave.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnSaveActionPerformed(evt);
                }
            });
        tlbMain.add(btnSave);
        tlbMain.add(sep2);

        btnClear.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/remove_all.png")));            // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            btnClear,
            org.openide.util.NbBundle.getMessage(ProtocolPanel.class, "ProtocolPanel.btnClear.text")); // NOI18N
        btnClear.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProtocolPanel.class,
                "ProtocolPanel.btnClear.toolTipText"));                                                // NOI18N
        btnClear.setFocusPainted(false);
        btnClear.setFocusable(false);
        btnClear.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnClear.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnClear.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnClearActionPerformed(evt);
                }
            });
        tlbMain.add(btnClear);

        panToolbarFiller.setOpaque(false);
        panToolbarFiller.add(filToolbar);

        tlbMain.add(panToolbarFiller);

        tlbMain.putClientProperty(Options.HEADER_STYLE_KEY, HeaderStyle.BOTH);

        add(tlbMain, java.awt.BorderLayout.NORTH);

        panMain.setLayout(new java.awt.GridBagLayout());

        panSteps.setLayout(new java.awt.GridBagLayout());

        panStepsFiller.setPreferredSize(new java.awt.Dimension(100, 1));

        final javax.swing.GroupLayout panStepsFillerLayout = new javax.swing.GroupLayout(panStepsFiller);
        panStepsFiller.setLayout(panStepsFillerLayout);
        panStepsFillerLayout.setHorizontalGroup(
            panStepsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                100,
                Short.MAX_VALUE));
        panStepsFillerLayout.setVerticalGroup(
            panStepsFillerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(
                0,
                300,
                Short.MAX_VALUE));

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        panSteps.add(panStepsFiller, gridBagConstraints);

        scpSteps.setViewportView(panSteps);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panMain.add(scpSteps, gridBagConstraints);

        add(panMain, java.awt.BorderLayout.CENTER);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnClearActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnClearActionPerformed
        getHandler().clearSteps();
    }                                                                            //GEN-LAST:event_btnClearActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void tbtRecordOnOffActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_tbtRecordOnOffActionPerformed
        getHandler().setRecordEnabled(tbtRecordOnOff.isSelected());
    }                                                                                  //GEN-LAST:event_tbtRecordOnOffActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnSaveActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnSaveActionPerformed
        final int status = fchMain.showSaveDialog(StaticSwingTools.getParentFrame(this));
        if (status == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fchMain.getSelectedFile();

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
                            getHandler().writeToFile(fileToSave);
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
    } //GEN-LAST:event_btnSaveActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnLoadActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnLoadActionPerformed
        final int status = fchMain.showOpenDialog(StaticSwingTools.getParentFrame(this));
        if (status == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = fchMain.getSelectedFile();
            new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        try {
                            getHandler().readFromFile(selectedFile);
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
    } //GEN-LAST:event_btnLoadActionPerformed

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
     * @param  toolbarItems  DOCUMENT ME!
     */
    private void addToolbarItems(final List<ProtocolStepToolbarItem> toolbarItems) {
        for (final ProtocolStepToolbarItem toolbarItem : toolbarItems) {
            if (toolbarItem instanceof Component) {
                tlbMain.add((Component)toolbarItem);
            } else if (toolbarItem instanceof Action) {
                tlbMain.add((Action)toolbarItem);
            } else {
                LOG.warn("toolbar item not added. was neither Component nor Action");
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  steps          DOCUMENT ME!
     * @param  showImmediate  DOCUMENT ME!
     */
    private void addSteps(final List<ProtocolStep> steps, final boolean showImmediate) {
        if (SwingUtilities.isEventDispatchThread()) {
            for (final ProtocolStep step : steps) {
                try {
                    addStep(step, showImmediate);
                } catch (final Exception ex) {
                    LOG.warn("exception while adding step", ex);
                }
            }
        } else {
            SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        addSteps(steps, showImmediate);
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  step           DOCUMENT ME!
     * @param  showImmediate  DOCUMENT ME!
     */
    private void addStep(final ProtocolStep step, final boolean showImmediate) {
        if (SwingUtilities.isEventDispatchThread()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("adding GUI for protocol " + step.getMetaInfo().getKey()
                            + "' and rendering it immediately: " + showImmediate);
            }

            final ProtocolStepPanelWrapper wrapper = new ProtocolStepPanelWrapper(step, showImmediate);
            panSteps.remove(panStepsFiller);
            panSteps.add(wrapper, getWrapperConstraints());
            panSteps.add(panStepsFiller, getFillerConstraints());
            panSteps.revalidate();

            protocolStepToWrapperMap.put(step, wrapper);

            scpSteps.getVerticalScrollBar().setValue(scpSteps.getVerticalScrollBar().getMaximum());
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

    @Override
    public JComponent getGuiComponent() {
        return this;
    }

    @Override
    public String getPermissionString() {
        return GUIWindow.NO_PERMISSION;
    }

    @Override
    public String getViewTitle() {
        return "";
    }

    @Override
    public Icon getViewIcon() {
        return null;
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
                        tbtRecordOnOff.setSelected(event.getSourceProtocolHander().isRecordEnabled());
                    }
                });
        }

        @Override
        public void stepAdded(final ProtocolHandlerListenerEvent event) {
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
            addSteps(event.getSourceProtocolHander().getAllSteps(), true);
        }
    }
}
