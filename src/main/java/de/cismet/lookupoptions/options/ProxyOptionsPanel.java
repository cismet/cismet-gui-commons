/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions.options;

import org.apache.log4j.Logger;

import org.openide.util.NbBundle;
import org.openide.util.lookup.ServiceProvider;

import java.util.Objects;
import java.util.regex.Pattern;

import javax.swing.SwingUtilities;

import de.cismet.lookupoptions.AbstractOptionsPanel;
import de.cismet.lookupoptions.OptionsPanelController;

import de.cismet.netutil.Proxy;
import de.cismet.netutil.ProxyHandler;

/**
 * OptionsPanel for the Proxy Options.
 *
 * <p>This panel allows to configure the proxy. Proxy-configuration affects the WebAccessManager and the proxy of the
 * java http protocol handler (System.setProperty("http.proxyHost") & System.setProperty("http.proxyPort")).</p>
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
@ServiceProvider(service = OptionsPanelController.class)
public class ProxyOptionsPanel extends AbstractOptionsPanel implements OptionsPanelController {

    //~ Static fields/initializers ---------------------------------------------

    private static final transient Logger LOG = Logger.getLogger(ProxyOptionsPanel.class);

    private static final String OPTION_NAME = org.openide.util.NbBundle.getMessage(
            ProxyOptionsPanel.class,
            "ProxyOptionsPanel.OptionController.name"); // NOI18N

    // Variables declaration - do not modify
    // NOI18N

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbEnabled;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblExcludedHosts;
    private javax.swing.JLabel lblHost;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPort;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JRadioButton rdoManualProxy;
    private javax.swing.JRadioButton rdoPreconfiguredProxy;
    private javax.swing.JSpinner spiPort;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JTextArea txtExcludedHosts;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ProxyOptionsPanel.
     */
    public ProxyOptionsPanel() {
        super(OPTION_NAME, NetworkOptionsCategory.class);
        initComponents();

        final Proxy preconfiguredProxy = ProxyHandler.getInstance().getPreconfiguredProxy();
        jPanel2.setVisible((preconfiguredProxy != null) && preconfiguredProxy.isValid());
        update();
        ProxyHandler.getInstance().addListener(new ProxyHandler.Listener() {

                @Override
                public void proxyChanged(final ProxyHandler.Event event) {
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                updateFields(event.getNewMode(), event.getNewProxy());
                            }
                        });
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getOrder() {
        return 1;
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public final void update() {
        final ProxyHandler.Mode mode = ProxyHandler.getInstance().getMode();
        if (mode != null) {
            switch (mode) {
                case MANUAL: {
                    rdoManualProxyActionPerformed(null);
                    break;
                }
                case PRECONFIGURED: {
                    rdoPreconfiguredProxyActionPerformed(null);
                    break;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    @Override
    public void applyChanges() {
        final ProxyHandler.Mode selectedMode = getSelectedMode();
        if (selectedMode != null) {
            switch (selectedMode) {
                case MANUAL: {
                    ProxyHandler.getInstance().useManualProxy(getProxyFromFields());
                    break;
                }
                case PRECONFIGURED: {
                    ProxyHandler.getInstance().usePreconfiguredProxy();
                    break;
                }
                default: {
                    ProxyHandler.getInstance().useNoProxy();
                }
            }
        } else {
            ProxyHandler.getInstance().useNoProxy();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private ProxyHandler.Mode getSelectedMode() {
        if (rdoPreconfiguredProxy.isSelected()) {
            return ProxyHandler.Mode.PRECONFIGURED;
        } else if (rdoManualProxy.isSelected()) {
            return ProxyHandler.Mode.MANUAL;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isChanged() {
        final ProxyHandler.Mode mode = ProxyHandler.getInstance().getMode();
        final Proxy proxy = ProxyHandler.getInstance().getProxy();

        final ProxyHandler.Mode selectedMode = getSelectedMode();
        if (!Objects.equals(mode, selectedMode)) {
            return true;
        } else if (ProxyHandler.Mode.MANUAL.equals(selectedMode)) {
            final String host = (proxy != null) ? proxy.getHost() : "";
            final int port = (proxy != null) ? proxy.getPort() : 0;
            final String username = (proxy != null) ? proxy.getUsername() : "";
            final String password = (proxy != null) ? proxy.getPassword() : "";
            final String domain = (proxy != null) ? proxy.getDomain() : "";
            final String excludeHosts = (proxy != null) ? proxy.getExcludedHosts() : "";

            return (!txtHost.getText().equals(host)
                            || !((int)spiPort.getValue() == port)
                            || !txtUsername.getText().equals(username)
                            || !String.valueOf(pwdPassword.getPassword()).equals(password)
                            || !txtDomain.getText().equals(domain)
                            || !txtExcludedHosts.getText().replaceAll("Pattern.quote(\n)", "|").equals(excludeHosts));
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getTooltip() {
        return org.openide.util.NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.getTooltip().text"); // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  mode   DOCUMENT ME!
     * @param  proxy  DOCUMENT ME!
     */
    private void updateFields(final ProxyHandler.Mode mode, final Proxy proxy) {
        cbEnabled.setEnabled(ProxyHandler.Mode.MANUAL.equals(mode));
        final boolean enabledFields = ProxyHandler.Mode.MANUAL.equals(mode) && (proxy != null) && proxy.isEnabled();
        jPanel1.setEnabled(enabledFields);
        lblHost.setEnabled(enabledFields);
        txtHost.setEnabled(enabledFields);
        lblPort.setEnabled(enabledFields);
        spiPort.setEnabled(enabledFields);
        lblUsername.setEnabled(enabledFields);
        txtUsername.setEnabled(enabledFields);
        lblPassword.setEnabled(enabledFields);
        pwdPassword.setEnabled(enabledFields);
        lblDomain.setEnabled(enabledFields);
        txtDomain.setEnabled(enabledFields);
        jPanel7.setEnabled(enabledFields);
        lblExcludedHosts.setEnabled(enabledFields);
        txtExcludedHosts.setEnabled(enabledFields);

        rdoManualProxy.setSelected(ProxyHandler.Mode.MANUAL.equals(mode));
        rdoPreconfiguredProxy.setSelected(ProxyHandler.Mode.PRECONFIGURED.equals(mode));

        cbEnabled.setSelected((proxy != null) ? proxy.isEnabled() : false);
        txtHost.setText((proxy != null) ? proxy.getHost() : null);
        spiPort.setValue((proxy != null) ? proxy.getPort() : 0);
        txtUsername.setText((proxy != null) ? proxy.getUsername() : null);
        pwdPassword.setText((proxy != null) ? proxy.getPassword() : null);
        txtExcludedHosts.setText(((proxy != null) && (proxy.getExcludedHosts() != null))
                ? proxy.getExcludedHosts().replaceAll(Pattern.quote("|"), "\n") : null);
        txtDomain.setText((proxy != null) ? proxy.getDomain() : null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rdoPreconfiguredProxy = new javax.swing.JRadioButton();
        rdoManualProxy = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        cbEnabled = new javax.swing.JCheckBox();
        lblHost = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        lblPort = new javax.swing.JLabel();
        spiPort = new javax.swing.JSpinner();
        jPanel1 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        pwdPassword = new javax.swing.JPasswordField();
        lblDomain = new javax.swing.JLabel();
        txtDomain = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        lblExcludedHosts = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtExcludedHosts = new javax.swing.JTextArea();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 32767));

        setLayout(new java.awt.GridBagLayout());

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jPanel2.setLayout(new java.awt.GridLayout(0, 1));

        buttonGroup1.add(rdoPreconfiguredProxy);
        rdoPreconfiguredProxy.setSelected(true);
        rdoPreconfiguredProxy.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.rdoPreconfiguredProxy.text")); // NOI18N
        rdoPreconfiguredProxy.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    rdoPreconfiguredProxyActionPerformed(evt);
                }
            });
        jPanel2.add(rdoPreconfiguredProxy);

        buttonGroup1.add(rdoManualProxy);
        rdoManualProxy.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.rdoManualProxy.text")); // NOI18N
        rdoManualProxy.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    rdoManualProxyActionPerformed(evt);
                }
            });
        jPanel2.add(rdoManualProxy);
        rdoManualProxy.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        ProxyOptionsPanel.class,
                        "ProxyOptionsPanel.rdoManualProxy.text")); // NOI18N

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel3.add(jPanel2, gridBagConstraints);

        jPanel4.setLayout(new java.awt.GridBagLayout());

        jPanel5.setLayout(new java.awt.GridBagLayout());

        cbEnabled.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.cbEnabled.text")); // NOI18N
        cbEnabled.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    cbEnabledActionPerformed(evt);
                }
            });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 15);
        jPanel5.add(cbEnabled, gridBagConstraints);

        lblHost.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblHost.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel5.add(lblHost, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(txtHost, gridBagConstraints);

        lblPort.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblPort.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel5.add(lblPort, gridBagConstraints);

        spiPort.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65535, 1));
        spiPort.setEditor(new javax.swing.JSpinner.NumberEditor(spiPort, "0"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(spiPort, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(jPanel5, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.jPanel1.border.title"))); // NOI18N
        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        lblUsername.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblUsername.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblUsername, gridBagConstraints);

        txtUsername.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.txtUsername.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel6.add(txtUsername, gridBagConstraints);

        lblPassword.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblPassword.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblPassword, gridBagConstraints);

        pwdPassword.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.pwdPassword.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel6.add(pwdPassword, gridBagConstraints);

        lblDomain.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblDomain.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblDomain, gridBagConstraints);

        txtDomain.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.txtDomain.text")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel6.add(txtDomain, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel1.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
        jPanel4.add(jPanel1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel3.add(jPanel4, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(
                org.openide.util.NbBundle.getMessage(
                    ProxyOptionsPanel.class,
                    "ProxyOptionsPanel.jPanel7.border.title"))); // NOI18N
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        lblExcludedHosts.setText(NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblExcludedHosts.text"));        // NOI18N
        lblExcludedHosts.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblExcludedHosts.toolTipText")); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        jPanel8.add(lblExcludedHosts, gridBagConstraints);

        txtExcludedHosts.setColumns(20);
        txtExcludedHosts.setRows(5);
        jScrollPane1.setViewportView(txtExcludedHosts);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel8.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jPanel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel3.add(jPanel7, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(filler1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        add(jPanel3, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Proxy getProxyFromFields() {
        return new Proxy(
                cbEnabled.isSelected(),
                txtHost.getText().trim(),
                (int)spiPort.getValue(),
                txtUsername.getText(),
                String.valueOf(pwdPassword.getPassword()),
                txtDomain.getText(),
                txtExcludedHosts.getText().replaceAll(Pattern.quote("\n"), "|"));
    }
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void rdoPreconfiguredProxyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_rdoPreconfiguredProxyActionPerformed
        ProxyHandler.getInstance().setManualProxy(getProxyFromFields());
        updateFields(ProxyHandler.Mode.PRECONFIGURED, ProxyHandler.getInstance().getPreconfiguredProxy());
    }                                                                                         //GEN-LAST:event_rdoPreconfiguredProxyActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void rdoManualProxyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_rdoManualProxyActionPerformed
        updateFields(ProxyHandler.Mode.MANUAL, ProxyHandler.getInstance().getManualProxy());
    }                                                                                  //GEN-LAST:event_rdoManualProxyActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void cbEnabledActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_cbEnabledActionPerformed
        final Proxy proxy = ProxyHandler.getInstance().getManualProxy();
        proxy.setEnabled(cbEnabled.isSelected());
        updateFields(getSelectedMode(), proxy);
    }                                                                             //GEN-LAST:event_cbEnabledActionPerformed
}
