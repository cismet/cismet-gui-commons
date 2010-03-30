package de.cismet.lookupoptions.gui;

import com.l2fprod.common.swing.JButtonBar;
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI;
import de.cismet.lookupoptions.OptionsCategory;
import de.cismet.lookupoptions.OptionsPanelController;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.util.Hashtable;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/*
 * OptionsDialog.java
 *
 * Created on 14.09.2009, 14:37:44
 */
/**
 * Dialog for the LookupPanels.
 * Uses the OptionsClient to get the categories and the panels.
 * @author jruiz
 */
public class OptionsDialog extends javax.swing.JDialog {

    /** Creates new form OptionsDialog */
    public OptionsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        if(log.isDebugEnabled())
            log.debug("OptionsDialog constructor"); //NOI18N
        initComponents();
        initOptionsPanels();
        getRootPane().setDefaultButton(btnOk);
        setLocationRelativeTo(parent);
    }

    /**
     * Inits the Category-Buttons and the LookupPanels.
     */
    private void initOptionsPanels() {

        JButtonBar buttonbar = new JButtonBar(JButtonBar.HORIZONTAL);
        buttonbar.setUI(new BlueishButtonBarUI());
        ButtonGroup buttonGroup = new ButtonGroup();

        // Kategorien holen
        OptionsCategory[] categories = optionsClient.getCategories();
        // für jede Kategorie
        for (final OptionsCategory category : categories) {

            final Class<? extends OptionsCategory> categoryClass = category.getClass();
            // Icon und Name der Category für den Button
            final Icon categoryIcon = category.getIcon();
            final String categoryName = category.getName();
            // Klassen-Name als eindeutiger Bezeichner für die einzelnen Cards im CardLayout
            final String categoryCardName = categoryClass.getCanonicalName();

            if(log.isDebugEnabled())
                log.debug("OptionsCategory: " + categoryName); //NOI18N

            // Die OptionsController der entsprechenden Kategorie holen
            OptionsPanelController[] controllers = optionsClient.getControllers(categoryClass);

            // keine leeren Kategorien anzeigen
            if (controllers.length > 0) {
                // Kategorie-Button erzeugen
                final JToggleButton button = new JToggleButton();
                button.setPreferredSize(CATEGORYBUTTON_DIMENSION);
                button.setLayout(new BorderLayout());
                button.add(new JLabel(categoryIcon), BorderLayout.CENTER);
                button.add(new JLabel(categoryName, JLabel.CENTER), BorderLayout.SOUTH);

                // Verhalten wenn eine Kategorie gewählt wird wird
                button.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // alle OptionsController der Kategorie updaten (Werte neu laden)
                        optionsClient.update(categoryClass);
                        // und dann die Card der Kategorie anzeigen
                        ((CardLayout) panCategory.getLayout()).show(panCategory, categoryCardName);
                        selectedCategory = category;
                        selectedPanelChanged();
                    }
                });

                buttonGroup.add(button);
                buttonbar.add(button);

                if (selectedCategory == null) {
                    selectedCategory = category;
                }

                // prüfen ob nur ein einziger OptionsController gefunden wurde
                if (controllers.length == 1) {
                    final OptionsPanelController controller = controllers[0];
                    final JPanel panel = controller.getPanel();
                    // falls ja, dann brauchen wir keine Tabs
                    panCategory.add(panel, categoryCardName);
                    selectedControllerPerCategory.put(category, controller);
                } else if (controllers.length > 1) {
                    // sonst, TabbedPane erzeugen
                    final JTabbedPane tbpPanels = new JTabbedPane();
                    // für jeden OptionsController
                    for (final OptionsPanelController controller : controllers) {
                        if (selectedControllerPerCategory.get(category) == null) {
                            selectedControllerPerCategory.put(category, controller);
                        }

                        final String controllerName = controller.getName();
                        final String controllerTooltip = controller.getTooltip();
                        final JPanel controllerPanel = controller.getPanel();

                        if(log.isDebugEnabled())
                            log.debug("OptionsPanelController: " + controllerName); //NOI18N

                        // jeweils ein tab (mit title)
                        tbpPanels.addTab(controllerName, null, controllerPanel, controllerTooltip);
                        tbpPanels.addChangeListener(new ChangeListener() {

                            @Override
                            public void stateChanged(ChangeEvent e) {
                                JTabbedPane sourceTabbedPane = (JTabbedPane) e.getSource();
                                if (sourceTabbedPane.getSelectedComponent() == controllerPanel) {
                                    selectedControllerPerCategory.put(category, controller);
                                    selectedPanelChanged();
                                }
                            }
                        });
                    }
                    panCategory.add(tbpPanels, categoryCardName);
                }
            }
        }
        panCategories.add(buttonbar);

        // ersten button ermitteln
        final JToggleButton firstButton = (JToggleButton) buttonbar.getComponent(0);
        if (firstButton != null) {
            // und selected setzen
            buttonGroup.setSelected(firstButton.getModel(), true);
        }
        selectedPanelChanged();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panCategory = new javax.swing.JPanel();
        btnClose = new javax.swing.JButton();
        panCategories = new javax.swing.JPanel();
        btnOk = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.title")); // NOI18N

        panCategory.setLayout(new java.awt.CardLayout());

        btnClose.setText(org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.btnClose.text")); // NOI18N
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        panCategories.setMaximumSize(new java.awt.Dimension(32767, 90));
        panCategories.setMinimumSize(new java.awt.Dimension(10, 90));
        panCategories.setLayout(new java.awt.BorderLayout());

        btnOk.setText(org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.btnOk.text")); // NOI18N
        btnOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOkActionPerformed(evt);
            }
        });

        btnHelp.setText(org.openide.util.NbBundle.getMessage(OptionsDialog.class, "OptionsDialog.btnHelp.text")); // NOI18N
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(336, Short.MAX_VALUE)
                .addComponent(btnOk, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(panCategories, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
            .addComponent(panCategory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panCategories, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panCategory, javax.swing.GroupLayout.DEFAULT_SIZE, 371, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHelp)
                    .addComponent(btnClose)
                    .addComponent(btnOk))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectedPanelChanged() {
        OptionsPanelController controller = selectedControllerPerCategory.get(selectedCategory);
        if (controller != null) {
            btnHelp.setEnabled(controller.getHelp() != null);
        } else {
            btnHelp.setEnabled(false);
        }
    }

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        try {
            optionsClient.cancelAll();
        } catch (Throwable t) {
            if(log.isDebugEnabled())
                log.debug("btnCloseActionPerformed", t); //NOI18N
        } finally {
            dispose();
        }
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOkActionPerformed
        try {
            optionsClient.applyAll();
        } catch (Throwable t) {
            if(log.isDebugEnabled())
                log.debug("btnOkActionPerformed", t); //NOI18N
        } finally {
            dispose();
        }
    }//GEN-LAST:event_btnOkActionPerformed

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        OptionsPanelController controller = selectedControllerPerCategory.get(selectedCategory);
        String help = controller.getHelp();
        HelpDialog dialog = new HelpDialog((JFrame) getParent(), true);
        dialog.setContent(help);
        dialog.setVisible(true);
    }//GEN-LAST:event_btnHelpActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        // zum Testen das gewünschte LookAndFeel
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                OptionsDialog dialog = new OptionsDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnOk;
    private javax.swing.JPanel panCategories;
    private javax.swing.JPanel panCategory;
    // End of variables declaration//GEN-END:variables

    private static final Dimension CATEGORYBUTTON_DIMENSION = new Dimension(100, 70);

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private OptionsClient optionsClient = OptionsClient.getInstance();
    private Hashtable<OptionsCategory, OptionsPanelController> selectedControllerPerCategory = new Hashtable<OptionsCategory, OptionsPanelController>();
    private OptionsCategory selectedCategory;
}
