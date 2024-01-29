package de.cismet.tools.gui;


import de.cismet.tools.BrowserLauncher;
import java.awt.Component;
import java.awt.Frame;
import java.io.IOException;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContinueOrExitDialog extends javax.swing.JDialog {

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        panMain = new javax.swing.JPanel();
        panCenter = new javax.swing.JPanel();
        panContent = new javax.swing.JPanel();
        scpContent = new javax.swing.JScrollPane();
        txpContent = new javax.swing.JTextPane();
        panSouth = new javax.swing.JPanel();
        fllButtons = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 0));
        panButtons = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnContinue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(800, 600));
        setMinimumSize(new java.awt.Dimension(300, 150));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setLayout(new java.awt.BorderLayout());

        panCenter.setLayout(new java.awt.GridBagLayout());

        panContent.setLayout(new java.awt.GridBagLayout());

        scpContent.setBorder(null);

        txpContent.setEditable(false);
        txpContent.setContentType("text/html"); // NOI18N
        txpContent.setOpaque(false);
        txpContent.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
            public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {
                txpContentHyperlinkUpdate(evt);
            }
        });
        scpContent.setViewportView(txpContent);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        panContent.add(scpContent, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panCenter.add(panContent, gridBagConstraints);

        panMain.add(panCenter, java.awt.BorderLayout.CENTER);

        panSouth.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        panSouth.add(fllButtons, gridBagConstraints);

        panButtons.setLayout(new java.awt.GridLayout(1, 0, 10, 0));

        org.openide.awt.Mnemonics.setLocalizedText(btnExit, org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.btnExit.text")); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });
        panButtons.add(btnExit);

        org.openide.awt.Mnemonics.setLocalizedText(btnContinue, org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.btnContinue.text")); // NOI18N
        btnContinue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinueActionPerformed(evt);
            }
        });
        panButtons.add(btnContinue);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        panSouth.add(panButtons, gridBagConstraints);

        panMain.add(panSouth, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(10, 10, 10, 10);
        getContentPane().add(panMain, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
        doExit();
    }//GEN-LAST:event_btnExitActionPerformed

    private void btnContinueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnContinueActionPerformed
        doContinue();
    }//GEN-LAST:event_btnContinueActionPerformed

    private void txpContentHyperlinkUpdate(javax.swing.event.HyperlinkEvent evt) {//GEN-FIRST:event_txpContentHyperlinkUpdate
        if (evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            try {
                BrowserLauncher.openURL(evt.getDescription());
            } catch (final Exception ex) {
                LOG.error(ex, ex);
            }
        }
    }//GEN-LAST:event_txpContentHyperlinkUpdate

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnContinue;
    private javax.swing.JButton btnExit;
    private javax.swing.Box.Filler fllButtons;
    private javax.swing.JPanel panButtons;
    private javax.swing.JPanel panCenter;
    private javax.swing.JPanel panContent;
    private javax.swing.JPanel panMain;
    private javax.swing.JPanel panSouth;
    private javax.swing.JScrollPane scpContent;
    private javax.swing.JTextPane txpContent;
    // End of variables declaration//GEN-END:variables

    private final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContinueOrExitDialog.class);
    
    private String contentTitle;
    private String content;
    private String continueButtonText;
    private String exitButtonText;
    
    private boolean showContinueButton = true;
    private boolean showExitButton = true;

    public ContinueOrExitDialog() {
        this((Frame)null);
    }

    public ContinueOrExitDialog(final Component component) {
        this((Frame)StaticSwingTools.getParentFrame(component));
    }

    public ContinueOrExitDialog(final Frame parent) {
        super(parent, true);
    }
        
    public ContinueOrExitDialog(final Component parent, final String contentTitle, final String content, final String continueButtonText, final String exitButtonText) {
        this(parent);
        setContentTitle(contentTitle);
        setContent(content);
        setContinueButtonText(continueButtonText);
        setExitButtonText(exitButtonText);        
    }

    public ContinueOrExitDialog(final Frame parent, final String contentTitle, final String content, final String continueButtonText, final String exitButtonText) {
        this(parent);
        setContentTitle(contentTitle);
        setContent(content);
        setContinueButtonText(continueButtonText);
        setExitButtonText(exitButtonText);        
    }    

    public static final void doShow(final Component component, final String contentTitle, final String content, final String continueButtonText, final String exitButtonText) throws IOException {        
        final ContinueOrExitDialog instance = new ContinueOrExitDialog(component);
        instance.setContentTitle(contentTitle);
        instance.setContent(content);
        instance.setContinueButtonText(continueButtonText);
        instance.setExitButtonText(exitButtonText);        
        instance.doShow();
    }
    
    public final void doShow() throws IOException {
        LOG.debug(String.format("%s.doShow()", ContinueOrExitDialog.class.getName()));
        
        initComponents();
        
        setTitle(getContentTitle());        
        txpContent.setText(getContent());

        panButtons.removeAll();
        if (getContinueButtonText() != null) {
            panButtons.add(btnContinue);
        }
        if (getExitButtonText() != null) {
            panButtons.add(btnExit);
        }
        
        pack();
        
        StaticSwingTools.showDialog(this, true);
    }

    private void doContinue() {
        LOG.debug(String.format("%s.doContinue()", ContinueOrExitDialog.class.getName()));
        dispose();
    }

    private void doExit() {
        LOG.debug(String.format("%s.doExit()", ContinueOrExitDialog.class.getName()));
        System.exit(0);
    }

    public static void main(String[] args) throws IOException {
        final String contentTitle = "Title from ConfAttr";
        final String content = "<html><head><style>body { font-family: 'Arial', sans-serif; }</style></head><body>This is a <a href='http://www.cismet.de'>test link</a>.<br/><br/>Fingers crossed ! :)";
        final String continueButtonText = null;
        final String exitButtonText = "exit";

        ContinueOrExitDialog.doShow(null, contentTitle, content, continueButtonText, exitButtonText);        
    }
}
