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
    private javax.swing.JLabel labHost;
    private javax.swing.JLabel labPort;
    private javax.swing.JLabel lblDomain;
    private javax.swing.JLabel lblExcludedHosts;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField pwdPassword;
    private javax.swing.JRadioButton rdoManualProxy;
    private javax.swing.JRadioButton rdoNoProxy;
    private javax.swing.JRadioButton rdoPreconfiguredProxy;
    private javax.swing.JSpinner spiPort;
    private javax.swing.JTextField txtDomain;
    private javax.swing.JTextArea txtExcludedHosts;
    private javax.swing.JTextField txtHost;
    private javax.swing.JTextField txtUsername;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form ProxyOptionsPanel.
     */
    public ProxyOptionsPanel() {
        super(OPTION_NAME, NetworkOptionsCategory.class);
        initComponents();

        final Proxy preconfiguredProxy = ProxyHandler.getInstance().getPreconfiguredProxy();
        if ((preconfiguredProxy == null) || !preconfiguredProxy.isValid()) {
            jPanel2.remove(rdoPreconfiguredProxy);
        }
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
                default: {
                    rdoNoProxyActionPerformed(null);
                }
            }
        } else {
            rdoNoProxyActionPerformed(null);
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
                    ProxyHandler.getInstance()
                            .useManualProxy(new Proxy(
                                    txtHost.getText().trim(),
                                    (int)spiPort.getValue(),
                                    txtUsername.getText(),
                                    String.valueOf(pwdPassword.getPassword()),
                                    txtDomain.getText(),
                                    txtExcludedHosts.getText().replaceAll(Pattern.quote("\n"), "|")));
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
            return ProxyHandler.Mode.NO_PROXY;
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
        final boolean enabled = (proxy != null) && proxy.isValid();
        rdoManualProxy.setSelected(ProxyHandler.Mode.MANUAL.equals(mode));
        rdoPreconfiguredProxy.setSelected(ProxyHandler.Mode.PRECONFIGURED.equals(mode));
        rdoNoProxy.setSelected((mode == null) || ProxyHandler.Mode.NO_PROXY.equals(mode));

        txtHost.setText(enabled ? proxy.getHost() : null);
        spiPort.setValue(enabled ? proxy.getPort() : 0);
        txtUsername.setText(enabled ? proxy.getUsername() : null);
        pwdPassword.setText(enabled ? proxy.getPassword() : null);
        txtExcludedHosts.setText((enabled && (proxy.getExcludedHosts() != null))
                ? proxy.getExcludedHosts().replaceAll(Pattern.quote("|"), "\n") : null);
        txtDomain.setText(enabled ? proxy.getDomain() : null);
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        rdoNoProxy = new javax.swing.JRadioButton();
        rdoPreconfiguredProxy = new javax.swing.JRadioButton();
        rdoManualProxy = new javax.swing.JRadioButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        labHost = new javax.swing.JLabel();
        txtHost = new javax.swing.JTextField();
        labPort = new javax.swing.JLabel();
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

        buttonGroup1.add(rdoNoProxy);
        rdoNoProxy.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.rdoNoProxy.text")); // NOI18N
        rdoNoProxy.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    rdoNoProxyActionPerformed(evt);
                }
            });
        jPanel2.add(rdoNoProxy);
        rdoNoProxy.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        ProxyOptionsPanel.class,
                        "ProxyOptionsPanel.rdoNoProxy.text")); // NOI18N

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

        labHost.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.labHost.text")); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                labHost,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        jPanel5.add(labHost, gridBagConstraints);
        labHost.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        ProxyOptionsPanel.class,
                        "ProxyOptionsPanel.labHost.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                txtHost,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(txtHost, gridBagConstraints);

        labPort.setText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.labPort.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                labPort,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
        jPanel5.add(labPort, gridBagConstraints);
        labPort.getAccessibleContext()
                .setAccessibleName(org.openide.util.NbBundle.getMessage(
                        ProxyOptionsPanel.class,
                        "ProxyOptionsPanel.labPort.text")); // NOI18N

        spiPort.setModel(new javax.swing.SpinnerNumberModel(0, 0, 65535, 1));
        spiPort.setEditor(new javax.swing.JSpinner.NumberEditor(spiPort, "0"));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                spiPort,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanel5.add(spiPort, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel4.add(jPanel5, gridBagConstraints);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(
                NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.jPanel1.border.title"))); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                jPanel1,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jPanel6.setLayout(new java.awt.GridBagLayout());

        lblUsername.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblUsername.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                lblUsername,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblUsername, gridBagConstraints);

        txtUsername.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.txtUsername.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                txtUsername,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel6.add(txtUsername, gridBagConstraints);

        lblPassword.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblPassword.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                lblPassword,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblPassword, gridBagConstraints);

        pwdPassword.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.pwdPassword.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                pwdPassword,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 0);
        jPanel6.add(pwdPassword, gridBagConstraints);

        lblDomain.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.lblDomain.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                lblDomain,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanel6.add(lblDomain, gridBagConstraints);

        txtDomain.setText(NbBundle.getMessage(ProxyOptionsPanel.class, "ProxyOptionsPanel.txtDomain.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                txtDomain,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

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

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                jPanel7,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        jPanel7.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());

        lblExcludedHosts.setText(NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblExcludedHosts.text"));        // NOI18N
        lblExcludedHosts.setToolTipText(org.openide.util.NbBundle.getMessage(
                ProxyOptionsPanel.class,
                "ProxyOptionsPanel.lblExcludedHosts.toolTipText")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                lblExcludedHosts,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(7, 0, 0, 0);
        jPanel8.add(lblExcludedHosts, gridBagConstraints);

        txtExcludedHosts.setColumns(20);
        txtExcludedHosts.setRows(5);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(
                org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE,
                rdoManualProxy,
                org.jdesktop.beansbinding.ELProperty.create("${selected}"),
                txtExcludedHosts,
                org.jdesktop.beansbinding.BeanProperty.create("enabled"));
        bindingGroup.addBinding(binding);

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

        bindingGroup.bind();
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void rdoPreconfiguredProxyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_rdoPreconfiguredProxyActionPerformed
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
    private void rdoNoProxyActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_rdoNoProxyActionPerformed
        updateFields(ProxyHandler.Mode.NO_PROXY, null);
    }                                                                              //GEN-LAST:event_rdoNoProxyActionPerformed
}
