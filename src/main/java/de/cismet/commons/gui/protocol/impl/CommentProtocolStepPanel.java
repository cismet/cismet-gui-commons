/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.impl;

import java.awt.Component;

import de.cismet.commons.gui.protocol.AbstractProtocolStepPanel;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class CommentProtocolStepPanel extends AbstractProtocolStepPanel<CommentProtocolStep> {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblComment;
    private javax.swing.JLabel lblIcon;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel panMain;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form CommentProtocolStepPanel.
     *
     * @param  commentProtocolStep  DOCUMENT ME!
     */
    public CommentProtocolStepPanel(final CommentProtocolStep commentProtocolStep) {
        super(commentProtocolStep);

        initComponents();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   text  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private String htmlifyText(final String text) {
        return "<html>" + text.replaceAll("\n", "<br/>") + "<html>";
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        lblTitle = new javax.swing.JLabel();
        lblIcon = new javax.swing.JLabel();
        panMain = new javax.swing.JPanel();
        lblComment = new javax.swing.JLabel();

        org.openide.awt.Mnemonics.setLocalizedText(
            lblTitle,
            org.openide.util.NbBundle.getMessage(
                CommentProtocolStepPanel.class,
                "CommentProtocolStepPanel.lblTitle.text")); // NOI18N

        lblIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIcon.setIcon(new javax.swing.ImageIcon(
                getClass().getResource("/de/cismet/commons/gui/protocol/impl/comment.png"))); // NOI18N
        org.openide.awt.Mnemonics.setLocalizedText(
            lblIcon,
            org.openide.util.NbBundle.getMessage(
                CommentProtocolStepPanel.class,
                "CommentProtocolStepPanel.lblIcon.text"));                                    // NOI18N

        setLayout(new java.awt.GridBagLayout());

        panMain.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        panMain.setLayout(new java.awt.GridBagLayout());

        lblComment.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        org.openide.awt.Mnemonics.setLocalizedText(lblComment, htmlifyText(getProtocolStep().getMessage()));
        lblComment.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panMain.add(lblComment, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(panMain, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public Component getIconComponent() {
        return lblIcon;
    }

    @Override
    public Component getTitleComponent() {
        return lblTitle;
    }
}
