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
package de.cismet.tools.gui.menu.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.openide.util.Lookup;
import org.openide.util.NbBundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class ExampleApp extends javax.swing.JFrame {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form Test.
     */
    public ExampleApp() {
        initComponents();

        final Map<String, CidsUiAction> actionMap = new HashMap<String, CidsUiAction>();

        final Collection<? extends CidsUiAction> actions = Lookup.getDefault().lookupAll(CidsUiAction.class);

        final Collection<? extends CidsUiActionProvider> actionProvider = Lookup.getDefault()
                    .lookupAll(CidsUiActionProvider.class);

        for (final CidsUiAction action : actions) {
            actionMap.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
        }

        for (final CidsUiActionProvider provider : actionProvider) {
            for (final CidsUiAction action : provider.getActions()) {
                actionMap.put((String)action.getValue(CidsUiAction.CIDS_ACTION_KEY), action);
            }
        }

        final ObjectMapper mapper = new ObjectMapper();

        try {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(
                        this.getClass().getResourceAsStream("MenuConfiguration.json")));
            final Menu menu = mapper.readValue(reader, Menu.class);

            final Item[] menuItems = menu.getMainMenu();

            for (final Item tmp : menuItems) {
                if (tmp.getActionKey() == null) {
                    final JMenu m = new JMenu();

                    if (tmp.getName() != null) {
                        m.setText(tmp.getName());
                    } else if (tmp.getI18nKey() != null) {
                        m.setText(NbBundle.getMessage(ExampleApp.class, tmp.getI18nKey()));
                    }
                    jMenuBar1.add(m);
                    addMenuItems(tmp.getItems(), m, actionMap);
                }
            }

            final Item[] toolbarItems = menu.getMainToolbar();

            for (final Item tmp : toolbarItems) {
                if (tmp.getActionKey() != null) {
                    final CidsUiAction action = actionMap.get(tmp.getActionKey());

                    if (action != null) {
                        final JButton button = jToolBar1.add(action);

                        final String name = getActionName(tmp);

                        if (name != null) {
                            button.setText(name);
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Add the given items to the given menu.
     *
     * @param  items      DOCUMENT ME!
     * @param  menu       DOCUMENT ME!
     * @param  actionMap  DOCUMENT ME!
     */
    private void addMenuItems(final Item[] items, final JMenu menu, final Map<String, CidsUiAction> actionMap) {
        for (final Item tmp : items) {
            if (tmp.getActionKey() == null) {
                final JMenu m = new JMenu(tmp.getName());
                menu.add(m);
                addMenuItems(tmp.getItems(), m, actionMap);
            } else {
                final String actionKey = tmp.getActionKey();
                final CidsUiAction action = actionMap.get(actionKey);

                if (action != null) {
                    final JMenuItem menuItem = new JMenuItem(action);
                    menu.add(menuItem);
                    final String name = getActionName(tmp);

                    if (name != null) {
                        menuItem.setText(name);
                    }
                }
            }
        }
    }

    /**
     * Determines the name of the given item.
     *
     * @param   item  DOCUMENT ME!
     *
     * @return  the name of the given item
     */
    private String getActionName(final Item item) {
        String name = null;

        if (item.getName() != null) {
            name = item.getName();
        } else if (item.getI18nKey() != null) {
            name = NbBundle.getMessage(ExampleApp.class, item.getI18nKey());
        }

        return name;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        jToolBar1 = new javax.swing.JToolBar();
        jMenuBar1 = new javax.swing.JMenuBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jToolBar1.setFloatable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jToolBar1, gridBagConstraints);
        setJMenuBar(jMenuBar1);

        pack();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (final javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ExampleApp.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExampleApp.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExampleApp.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExampleApp.class.getName())
                    .log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    final ExampleApp t = new ExampleApp();

                    t.setSize(300, 300);
                    t.setVisible(true);
                }
            });
    }
}
