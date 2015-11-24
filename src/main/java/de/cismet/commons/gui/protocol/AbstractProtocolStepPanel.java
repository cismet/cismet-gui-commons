/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol;

import java.awt.Component;

import javax.swing.JLabel;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class AbstractProtocolStepPanel extends javax.swing.JPanel implements ProtocolStepPanel {

    //~ Instance fields --------------------------------------------------------

    private final ProtocolStep protocolStep;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form AbstractProtocolStepPanel.
     *
     * @use  AbstractProtocolStepPanel(<? extends ProtocolStep>) instead
     */
    @Deprecated
    public AbstractProtocolStepPanel() {
        this(null);
    }

    /**
     * Creates new form AbstractProtocolStepPanel.
     *
     * @param  protocolStep  DOCUMENT ME!
     */
    public AbstractProtocolStepPanel(final ProtocolStep protocolStep) {
        this.protocolStep = protocolStep;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public ProtocolStep getProtocolStep() {
        return protocolStep;
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    } // </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public Component getMainComponent() {
        return this;
    }

    @Override
    public Component getIconComponent() {
        return new JLabel();
    }

    @Override
    public Component getTitleComponent() {
        return new JLabel(protocolStep.getMetaInfo().getDescription());
    }
}
