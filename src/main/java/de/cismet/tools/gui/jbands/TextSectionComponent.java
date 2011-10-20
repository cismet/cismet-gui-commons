/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * TextSectionComponent.java
 *
 * Created on 17.10.2011, 15:39:35
 */
package de.cismet.tools.gui.jbands;

import javax.swing.JPanel;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class TextSectionComponent extends JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblLeft;
    private javax.swing.JLabel lblRight;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JSeparator sepLeft;
    private javax.swing.JSeparator sepRight;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form TextSectionComponent.
     */
    public TextSectionComponent() {
        initComponents();
    }

    /**
     * Creates a new TextSectionComponent object.
     *
     * @param  title  DOCUMENT ME!
     */
    public TextSectionComponent(final String title) {
        this(title, false, false);
    }

    /**
     * Creates a new TextSectionComponent object.
     *
     * @param  title      DOCUMENT ME!
     * @param  openLeft   DOCUMENT ME!
     * @param  openRight  DOCUMENT ME!
     */
    public TextSectionComponent(final String title, final boolean openLeft, final boolean openRight) {
        this();
        lblTitle.setText(title);
        lblLeft.setVisible(!openLeft);
        lblRight.setVisible(!openRight);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  title  DOCUMENT ME!
     */
    public void setTitle(final String title) {
        lblTitle.setText(title);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblLeft = new javax.swing.JLabel();
        sepLeft = new javax.swing.JSeparator();
        lblTitle = new javax.swing.JLabel();
        sepRight = new javax.swing.JSeparator();
        lblRight = new javax.swing.JLabel();

        setOpaque(false);
        setLayout(new java.awt.GridBagLayout());

        lblLeft.setText(org.openide.util.NbBundle.getMessage(
                TextSectionComponent.class,
                "TextSectionComponent.lblLeft.text")); // NOI18N
        add(lblLeft, new java.awt.GridBagConstraints());

        sepLeft.setForeground(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sepLeft, gridBagConstraints);

        lblTitle.setText(org.openide.util.NbBundle.getMessage(
                TextSectionComponent.class,
                "TextSectionComponent.lblTitle.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 9);
        add(lblTitle, gridBagConstraints);

        sepRight.setForeground(new java.awt.Color(0, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(sepRight, gridBagConstraints);

        lblRight.setText(org.openide.util.NbBundle.getMessage(
                TextSectionComponent.class,
                "TextSectionComponent.lblRight.text")); // NOI18N
        add(lblRight, new java.awt.GridBagConstraints());
    }                                                   // </editor-fold>//GEN-END:initComponents
}
