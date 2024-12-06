/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import lombok.Getter;
import lombok.Setter;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JLabel;

import de.cismet.tools.BrowserLauncher;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
@Getter
@Setter
public class ContinueOrExitDialog extends javax.swing.JDialog {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ContinueOrExitDialog.class);

    //~ Instance fields --------------------------------------------------------

    private String contentTitle;
    private String content;
    private String continueButtonText;
    private String exitButtonText;
    private Integer contentWidth;

    private boolean showContinueButton = true;
    private boolean showExitButton = true;

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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ContinueOrExitDialog object.
     */
    public ContinueOrExitDialog() {
        this((Frame)null);
    }

    /**
     * Creates a new ContinueOrExitDialog object.
     *
     * @param  component  DOCUMENT ME!
     */
    public ContinueOrExitDialog(final Component component) {
        this((Frame)StaticSwingTools.getParentFrame(component));
    }

    /**
     * Creates a new ContinueOrExitDialog object.
     *
     * @param  parent  DOCUMENT ME!
     */
    public ContinueOrExitDialog(final Frame parent) {
        super(parent, true);
    }

    /**
     * Creates a new ContinueOrExitDialog object.
     *
     * @param  parent              DOCUMENT ME!
     * @param  contentTitle        DOCUMENT ME!
     * @param  content             DOCUMENT ME!
     * @param  continueButtonText  DOCUMENT ME!
     * @param  exitButtonText      DOCUMENT ME!
     */
    public ContinueOrExitDialog(final Component parent,
            final String contentTitle,
            final String content,
            final String continueButtonText,
            final String exitButtonText) {
        this(parent);
        setContentTitle(contentTitle);
        setContent(content);
        setContinueButtonText(continueButtonText);
        setExitButtonText(exitButtonText);
    }

    /**
     * Creates a new ContinueOrExitDialog object.
     *
     * @param  parent              DOCUMENT ME!
     * @param  contentTitle        DOCUMENT ME!
     * @param  content             DOCUMENT ME!
     * @param  continueButtonText  DOCUMENT ME!
     * @param  exitButtonText      DOCUMENT ME!
     */
    public ContinueOrExitDialog(final Frame parent,
            final String contentTitle,
            final String content,
            final String continueButtonText,
            final String exitButtonText) {
        this(parent);
        setContentTitle(contentTitle);
        setContent(content);
        setContinueButtonText(continueButtonText);
        setExitButtonText(exitButtonText);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
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
        fllButtons = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0),
                new java.awt.Dimension(0, 0),
                new java.awt.Dimension(32767, 0));
        panButtons = new javax.swing.JPanel();
        btnExit = new javax.swing.JButton();
        btnContinue = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.title")); // NOI18N
        setAlwaysOnTop(true);
        getContentPane().setLayout(new java.awt.GridBagLayout());

        panMain.setLayout(new java.awt.BorderLayout());

        panCenter.setLayout(new java.awt.GridBagLayout());

        panContent.setLayout(new java.awt.GridBagLayout());

        scpContent.setBorder(null);

        txpContent.setEditable(false);
        txpContent.setContentType("text/html"); // NOI18N
        txpContent.setOpaque(false);
        txpContent.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {

                @Override
                public void hyperlinkUpdate(final javax.swing.event.HyperlinkEvent evt) {
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

        org.openide.awt.Mnemonics.setLocalizedText(
            btnExit,
            org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.btnExit.text")); // NOI18N
        btnExit.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
                    btnExitActionPerformed(evt);
                }
            });
        panButtons.add(btnExit);

        org.openide.awt.Mnemonics.setLocalizedText(
            btnContinue,
            org.openide.util.NbBundle.getMessage(ContinueOrExitDialog.class, "ContinueOrExitDialog.btnContinue.text")); // NOI18N
        btnContinue.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(final java.awt.event.ActionEvent evt) {
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
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnExitActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnExitActionPerformed
        doExit();
    }                                                                           //GEN-LAST:event_btnExitActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void btnContinueActionPerformed(final java.awt.event.ActionEvent evt) { //GEN-FIRST:event_btnContinueActionPerformed
        doContinue();
    }                                                                               //GEN-LAST:event_btnContinueActionPerformed

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void txpContentHyperlinkUpdate(final javax.swing.event.HyperlinkEvent evt) { //GEN-FIRST:event_txpContentHyperlinkUpdate
        if (evt.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
            try {
                BrowserLauncher.openURL(evt.getDescription());
            } catch (final Exception ex) {
                LOG.error(ex, ex);
            }
        }
    }                                                                                    //GEN-LAST:event_txpContentHyperlinkUpdate

    /**
     * DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public final void doShow() throws IOException {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s.doShow()", ContinueOrExitDialog.class.getName()));
        }

        initComponents();

        setTitle(getContentTitle());
        txpContent.setText(getContent());

        panButtons.removeAll();
        if (getContinueButtonText() != null) {
            btnContinue.setText(getContinueButtonText());
            panButtons.add(btnContinue);
        }
        if (getExitButtonText() != null) {
            btnExit.setText(getExitButtonText());
            panButtons.add(btnExit);
        }
        pack();

        final Dimension buttonExitSize = btnExit.getSize();
        final Dimension buttonContinueSize = btnContinue.getSize();

        final int buttonWidth = Math.max(buttonExitSize.width, buttonContinueSize.width);
        final int buttonHeight = Math.max(buttonExitSize.height, buttonContinueSize.height);

        final int minContentWidth = (buttonWidth * 2) + 10;
        final int maxContentWidth = 600;
        final int contentWidth = Math.max(
                minContentWidth,
                (this.contentWidth != null) ? this.contentWidth : Math.min((buttonWidth * 3) + 10, maxContentWidth));
        final int contentHeight = determineHeight(txpContent, contentWidth);

        final int maxDialogHeigth = 500;
        final int dialogWidth = contentWidth + 40;
        final int dialogHeight = Math.min(contentHeight + buttonHeight + 70, maxDialogHeigth);

        setPreferredSize(new Dimension(dialogWidth, dialogHeight));

        pack();
        StaticSwingTools.showDialog(this, true);
    }

    /**
     * Creating an "offscreen" EditorPane with the same text and contentType than the original one, for calculating the
     * Preferred Height for a given Width.
     *
     * @param   editorPaneOrig  DOCUMENT ME!
     * @param   newWidth        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static int determineHeight(final JEditorPane editorPaneOrig, final int newWidth) {
        final JEditorPane editorPane = new JEditorPane();
        editorPane.setContentType(editorPaneOrig.getContentType());
        editorPane.setText(editorPaneOrig.getText());
        editorPane.setSize(new Dimension(newWidth, Integer.MAX_VALUE));
        return editorPane.getPreferredSize().height;
    }

    @Override
    public Dimension getPreferredSize() {
        final Dimension preferred = super.getPreferredSize();
        final Dimension maximum = getMaximumSize();
        final int width = Math.min(preferred.width, maximum.width);
        final int height = Math.min(preferred.height, maximum.height);
        return new Dimension(width, height);
    }

    /**
     * DOCUMENT ME!
     */
    private void doContinue() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s.doContinue()", ContinueOrExitDialog.class.getName()));
        }
        dispose();
    }

    /**
     * DOCUMENT ME!
     */
    private void doExit() {
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("%s.doExit()", ContinueOrExitDialog.class.getName()));
        }
        System.exit(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   args  DOCUMENT ME!
     *
     * @throws  IOException  DOCUMENT ME!
     */
    public static void main(final String[] args) throws IOException {
        final String contentTitle = "LagIS - Wichtiger Hinweis";

        final Font font = new JLabel().getFont();

        final String test = ""
                    + "<p>Sie sind in einer Nutzergruppe, die ausschließlich lesenden Zugriff auf die LagIS Daten hat. In Zukunft soll dazu die Applikation <a href='http://lagis-online.s10222.wuppertal-intra.de/lagis-desktop/#/login'>LagIS-Desktop</a> genutzt werden.</p>"
                    + "<p>Sollten Sie Fragen zur Applikation haben, wenden Sie sich bitte an Ilmo Gimmler oder Michael Stosch.</p>"
                    + "<p>Für eine Übergangszeit ist die Java Anwendung noch verfügbar.</p>";
        // final String test = "Mini-Text";
        final String content = String.format(
                "<html><html><body style='font-family: %s; font-size: %dpt; margin: 0px'>%s",
                font.getFamily(),
                font.getSize(),
                test);
        final String continueButtonText = "Weiter mit der Java Anwendung";
        final String exitButtonText = "Java Anwendung schließen";

        try {
            javax.swing.UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        } catch (Exception e) {
            LOG.warn("Fehler beim Einstellen des Look&Feels's!", e);
        }
        final ContinueOrExitDialog instance = new ContinueOrExitDialog();
        instance.setContentTitle(contentTitle);
        instance.setContent(content);
        instance.setContinueButtonText(continueButtonText);
        instance.setExitButtonText(exitButtonText);
        instance.setContentWidth(400);
        instance.doShow();
    }
}
