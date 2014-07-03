/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * LineBandMember.java
 *
 * Created on 8.03.2012
 */
package de.cismet.tools.gui.jbands;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.*;
import org.jdesktop.swingx.painter.Painter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import de.cismet.tools.gui.jbands.interfaces.BandMember;
import de.cismet.tools.gui.jbands.interfaces.BandMemberListener;
import de.cismet.tools.gui.jbands.interfaces.BandMemberSelectable;
import de.cismet.tools.gui.jbands.interfaces.ModifiableBandMember;
import de.cismet.tools.gui.jbands.interfaces.Section;
import de.cismet.tools.gui.jbands.interfaces.StationaryBandMemberMouseListeningComponent;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class SimpleModifiableBandMember extends JXPanel implements ModifiableBandMember,
    Section,
    StationaryBandMemberMouseListeningComponent,
    BandMemberSelectable,
    ActionListener,
    PopupMenuListener {

    //~ Static fields/initializers ---------------------------------------------

    protected static final Logger LOG = Logger.getLogger(SimpleModifiableBandMember.class);

    //~ Instance fields --------------------------------------------------------

    protected Painter unselectedBackgroundPainter = null;
    protected Painter selectedBackgroundPainter = null;
    protected boolean isSelected = false;
    protected JPopupMenu popup = new JPopupMenu();
    protected int mouseClickedXPosition = 0;
    protected String lineFieldName = "linie";

    double von = 0;
    double bis = 0;

    private boolean dragStart = false;
    private int dragSide = 0;
    private JMenuItem deleteItem = new JMenuItem("löschen");
    private SimpleModifiableBand parent;
    private List<BandMemberListener> listenerList = new ArrayList<BandMemberListener>();
    private boolean readOnly;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel labText;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form MassnahmenBandMember.
     *
     * @param  parent  DOCUMENT ME!
     */
    public SimpleModifiableBandMember(final SimpleModifiableBand parent) {
        this(parent, false, 0, 0);
    }

    /**
     * Creates new form MassnahmenBandMember.
     *
     * @param  parent    DOCUMENT ME!
     * @param  readOnly  DOCUMENT ME!
     * @param  from      DOCUMENT ME!
     * @param  to        DOCUMENT ME!
     */
    public SimpleModifiableBandMember(final SimpleModifiableBand parent,
            final boolean readOnly,
            final double from,
            final double to) {
        this.readOnly = readOnly;
        initComponents();
        setAlpha(0.8f);
        this.parent = parent;
        this.von = from;
        this.bis = to;
        popup.addPopupMenuListener(this);
        initMember();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    private void initMember() {
        determineBackgroundColour();
        configurePopupMenu();
    }

    @Override
    public JComponent getBandMemberComponent() {
        return this;
    }

    @Override
    public double getMax() {
        return (von < bis) ? bis : von;
    }

    @Override
    public double getMin() {
        return (von < bis) ? von : bis;
    }

    @Override
    public double getFrom() {
        return von;
    }

    @Override
    public double getTo() {
        return bis;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  from  DOCUMENT ME!
     */
    public void setFrom(final Double from) {
        this.von = from;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  to  DOCUMENT ME!
     */
    public void setTo(final Double to) {
        this.bis = to;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  text  DOCUMENT ME!
     */
    public void setText(final String text) {
        labText.setText(text);
    }

    /**
     * DOCUMENT ME!
     */
    protected void setDefaultBackground() {
        unselectedBackgroundPainter = new MattePainter(new Color(229, 0, 0));
        selectedBackgroundPainter = new CompoundPainter(
                unselectedBackgroundPainter,
                new RectanglePainter(
                    3,
                    3,
                    3,
                    3,
                    3,
                    3,
                    true,
                    new Color(100, 100, 100, 100),
                    2f,
                    new Color(50, 50, 50, 100)));
        if (isSelected) {
            setBackgroundPainter(selectedBackgroundPainter);
        } else {
            setBackgroundPainter(unselectedBackgroundPainter);
        }
    }

    /**
     * DOCUMENT ME!
     */
    protected void configurePopupMenu() {
        deleteItem.addActionListener(this);
        popup.add(deleteItem);
    }

    /**
     * DOCUMENT ME!
     */
    protected void determineBackgroundColour() {
        setDefaultBackground();
    }

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        final java.awt.GridBagConstraints gridBagConstraints;

        labText = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        setLayout(new java.awt.GridBagLayout());

        labText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(labText, gridBagConstraints);
    } // </editor-fold>//GEN-END:initComponents

    @Override
    public void mouseClicked(final MouseEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        if (JBandCursorManager.getInstance().isLocked()) {
            JBandCursorManager.getInstance().setCursor(this);
        }
        setAlpha(1f);
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        setAlpha(0.8f);
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (e.isPopupTrigger() && isSelected) {
            showPopupMenu(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (dragStart) {
            dragStart = false;
            JBandCursorManager.getInstance().setLocked(false);
        }
        if (e.isPopupTrigger() && isSelected) {
            showPopupMenu(e.getX(), e.getY());
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (JBandCursorManager.getInstance().isLocked()) {
            JBandCursorManager.getInstance().setCursor(this);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e, final double station) {
        if (JBandCursorManager.getInstance().isLocked()) {
            JBandCursorManager.getInstance().setCursor(this);
        }

        if (!dragStart) {
            if (JBandCursorManager.getInstance().getCursor().equals(
                            Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR))) {
                dragSide = 1;
                dragStart = true;
                JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                JBandCursorManager.getInstance().setLocked(true);
                JBandCursorManager.getInstance().setCursor(this);
            } else if (JBandCursorManager.getInstance().getCursor().equals(
                            Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR))) {
                dragSide = 2;
                dragStart = true;
                JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                JBandCursorManager.getInstance().setLocked(true);
                JBandCursorManager.getInstance().setCursor(this);
            }
        } else {
            if (dragSide == 1) {
                von = Math.floor(station);
            } else {
                bis = Math.floor(station);
            }
            fireBandMemberChanged(false);
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (!JBandCursorManager.getInstance().isLocked()) {
            if (isSelected && !isReadOnly()) {
                if (e.getX() < 5) {
                    JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
                    JBandCursorManager.getInstance().setCursor(this);
                } else if (e.getX() > (getWidth() - 5)) {
                    JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
                    JBandCursorManager.getInstance().setCursor(this);
                } else {
                    JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    JBandCursorManager.getInstance().setCursor(this);
                }
            } else {
                JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                JBandCursorManager.getInstance().setCursor(this);
            }
        } else {
            JBandCursorManager.getInstance().setCursor(this);
        }
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(final boolean selection) {
        isSelected = selection;
        if (!isSelected) {
            setBackgroundPainter(unselectedBackgroundPainter);
        } else {
            setBackgroundPainter(selectedBackgroundPainter);
        }
    }

    @Override
    public BandMember getBandMember() {
        return this;
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == deleteItem) {
            deleteMember();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void deleteMember() {
        parent.deleteMember(this);
    }

    @Override
    public void popupMenuWillBecomeVisible(final PopupMenuEvent e) {
    }

    @Override
    public void popupMenuWillBecomeInvisible(final PopupMenuEvent e) {
    }

    @Override
    public void popupMenuCanceled(final PopupMenuEvent e) {
    }

    @Override
    public void addBandMemberListener(final BandMemberListener listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeBandMemberListener(final BandMemberListener listener) {
        listenerList.remove(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  modelChanged  DOCUMENT ME!
     */
    public void fireBandMemberChanged(final boolean modelChanged) {
        for (final BandMemberListener l : listenerList) {
            l.bandMemberChanged(new BandMemberEvent(modelChanged));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    public void fireBandMemberChanged(final BandMemberEvent e) {
        for (final BandMemberListener l : listenerList) {
            l.bandMemberChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  x  DOCUMENT ME!
     * @param  y  DOCUMENT ME!
     */
    private void showPopupMenu(final int x, final int y) {
        mouseClickedXPosition = x;
        popup.show(this, x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public SimpleModifiableBand getParentBand() {
        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the readOnly
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  readOnly  the readOnly to set
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }
}
