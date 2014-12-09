/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * DocumentPanel.java
 *
 * Created on 26. August 2008, 11:18
 */
package de.cismet.tools.gui.documents;

import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * DOCUMENT ME!
 *
 * @author   hell
 * @version  $Revision$, $Date$
 */
public class DocumentPanel extends javax.swing.JPanel {

    //~ Instance fields --------------------------------------------------------

    DocumentListModel dlm = new DocumentListModel();

    private final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblPreview;
    private javax.swing.JLabel lblUrl;
    private javax.swing.JList lstDocuments;
    private javax.swing.JPanel panPreview;
    private javax.swing.JScrollPane scpLstDocuments;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form DocumentPanel.
     */
    public DocumentPanel() {
        initComponents();
        lstDocuments.setModel(dlm);

        dlm.addListDataListener(new ListDataListener() {

                @Override
                public void intervalAdded(final ListDataEvent e) {
                    handleVisibility();
                }

                @Override
                public void intervalRemoved(final ListDataEvent e) {
                    handleVisibility();
                }

                @Override
                public void contentsChanged(final ListDataEvent e) {
                }
            });

        lstDocuments.setCellRenderer(new DocumentListCellRenderer());
        lstDocuments.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(final ListSelectionEvent e) {
                    final Object selection = lstDocuments.getSelectedValue();
                    if (selection != null) {
                        final Document d = (Document)selection;
                        new Thread("DocumentPanel valueChanged()") {

                            @Override
                            public void run() {
                                EventQueue.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            lblPreview.setIcon(null);
                                            lblPreview.setText(
                                                org.openide.util.NbBundle.getMessage(
                                                    DocumentPanel.class,
                                                    "DocumentPanel.lblPreview.text.progress"));     // NOI18N
                                            lblUrl.setText(d.getDocumentURI());
                                        }
                                    });

                                int w = lblPreview.getWidth() - 10;
                                int h = lblPreview.getHeight() - 10;
                                if (w < 2) {
                                    w = 2;
                                }
                                if (h < 2) {
                                    h = 2;
                                }
                                final Image i = d.getPreview(w, h);
                                EventQueue.invokeLater(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (i != null) {
                                                lblPreview.setText("");                                // NOI18N
                                                lblPreview.setIcon(new ImageIcon(i));
                                            } else {
                                                lblPreview.setIcon(null);
                                                lblPreview.setText(
                                                    org.openide.util.NbBundle.getMessage(
                                                        DocumentPanel.class,
                                                        "DocumentPanel.lblPreview.text.default"));     // NOI18N
                                            }
                                            lblUrl.setText(d.getDocumentURI());
                                        }
                                    });
                            }
                        }.start();
                    }
                }
            });
        handleVisibility();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        scpLstDocuments = new javax.swing.JScrollPane();
        lstDocuments = new javax.swing.JList();
        panPreview = new javax.swing.JPanel();
        lblPreview = new javax.swing.JLabel();
        lblUrl = new javax.swing.JLabel();

        scpLstDocuments.setPreferredSize(new java.awt.Dimension(120, 138));

        lstDocuments.setModel(new javax.swing.AbstractListModel() {

                String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

                @Override
                public int getSize() {
                    return strings.length;
                }
                @Override
                public Object getElementAt(final int i) {
                    return strings[i];
                }
            });
        scpLstDocuments.setViewportView(lstDocuments);

        panPreview.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createEmptyBorder(0, 10, 0, 0),
                javax.swing.BorderFactory.createTitledBorder(
                    org.openide.util.NbBundle.getMessage(
                        DocumentPanel.class,
                        "DocumentPanel.panPreview.border.insideBorder.title")))); // NOI18N
        panPreview.setOpaque(false);
        panPreview.addComponentListener(new java.awt.event.ComponentAdapter() {

                @Override
                public void componentResized(final java.awt.event.ComponentEvent evt) {
                    panPreviewComponentResized(evt);
                }
            });
        panPreview.setLayout(new java.awt.BorderLayout());

        lblPreview.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPreview.setText(org.openide.util.NbBundle.getMessage(
                DocumentPanel.class,
                "DocumentPanel.lblPreview.text.default")); // NOI18N
        lblPreview.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblPreview.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseClicked(final java.awt.event.MouseEvent evt) {
                    lblPreviewMouseClicked(evt);
                }
            });
        panPreview.add(lblPreview, java.awt.BorderLayout.CENTER);

        lblUrl.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblUrl.setText(org.openide.util.NbBundle.getMessage(DocumentPanel.class, "DocumentPanel.lblUrl.text")); // NOI18N

        final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addComponent(
                    scpLstDocuments,
                    javax.swing.GroupLayout.PREFERRED_SIZE,
                    166,
                    javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(
                    panPreview,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    240,
                    Short.MAX_VALUE).addContainerGap()).addGroup(
                layout.createSequentialGroup().addComponent(
                    lblUrl,
                    javax.swing.GroupLayout.DEFAULT_SIZE,
                    399,
                    Short.MAX_VALUE).addGap(19, 19, 19)));
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                layout.createSequentialGroup().addGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                        layout.createSequentialGroup().addGap(8, 8, 8).addComponent(
                            scpLstDocuments,
                            javax.swing.GroupLayout.DEFAULT_SIZE,
                            180,
                            Short.MAX_VALUE).addGap(3, 3, 3)).addComponent(
                        panPreview,
                        javax.swing.GroupLayout.DEFAULT_SIZE,
                        191,
                        Short.MAX_VALUE)).addComponent(lblUrl)));
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     */
    private void handleVisibility() {
        if (dlm.size() <= 1) {
            lstDocuments.setSelectedIndex(0);
            scpLstDocuments.setVisible(false);
        } else {
            scpLstDocuments.setVisible(true);
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void panPreviewComponentResized(final java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panPreviewComponentResized
        final Object sel = lstDocuments.getSelectedValue();
        if (sel != null) {
            final Document selD = (Document)sel;
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        lstDocuments.clearSelection();
                        lstDocuments.setSelectedValue(selD, true);
                    }
                });
        }
    }//GEN-LAST:event_panPreviewComponentResized

    /**
     * DOCUMENT ME!
     *
     * @param  evt  DOCUMENT ME!
     */
    private void lblPreviewMouseClicked(final java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPreviewMouseClicked
        final Document selD = (Document)lstDocuments.getSelectedValue();
        if (!evt.isPopupTrigger() && (evt.getClickCount() > 1) && (selD != null)) {
            String gotoUrl = selD.getDocumentURI();
            try {
                de.cismet.tools.BrowserLauncher.openURL(gotoUrl);
            } catch (Exception e2) {
                log.warn("das 1te Mal ging schief.Fehler beim Oeffnen von:" + gotoUrl + "\nLetzter Versuch", e2); // NOI18N
                try {
                    gotoUrl = gotoUrl.replaceAll("\\\\", "/");                                                    // NOI18N
                    gotoUrl = gotoUrl.replaceAll(" ", "%20");                                                     // NOI18N
                    de.cismet.tools.BrowserLauncher.openURL("file:///" + gotoUrl);                                // NOI18N
                } catch (Exception e3) {
                    log.error("Auch das 2te Mal ging schief.Fehler beim Oeffnen von:file://" + gotoUrl, e3);      // NOI18N
                }
            }
        }
    }//GEN-LAST:event_lblPreviewMouseClicked

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public DocumentListModel getDocumentListModel() {
        return dlm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JList getDocumentListComponent() {
        return lstDocuments;
    }
}
