/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.historybutton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.cismet.tools.gui.JPopupMenuButton;

/**
 * The implementation of the JHistoryButton. See a short <a href="http://flexo.cismet.de/gadgets/JHistory/">description
 * and demo</a> on the website.
 *
 * <p>License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a><br>
 * <img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img
 * src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif"></p>
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class JHistoryButton extends JPopupMenuButton implements ActionListener, HistoryModelListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final int DIRECTION_FORWARD = 1;
    public static final int DIRECTION_BACKWARD = 2;

    public static final int ICON_SIZE_16 = 4;
    public static final int ICON_SIZE_22 = 8;
    public static final int ICON_SIZE_32 = 16;
    public static final int ICON_SIZE_64 = 32;
    public static final int ICON_SIZE_128 = 64;

    //~ Instance fields --------------------------------------------------------

    private int maxHistoryMenuLength = 10;

    private JPopupMenu popupMenu = new JPopupMenu();

    private int direction = 1;
    private boolean localEnabled = true;
    private HistoryModel historyModel = null;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of JHistoryButton.
     */
    public JHistoryButton() {
        this(true);
    }

    /**
     * Creates a new instance of JHistoryButton.
     *
     * @param  showPopupMenu  do not use the popup menu funtionality
     */
    public JHistoryButton(final boolean showPopupMenu) {
        super(showPopupMenu);
        super.setEnabled(false);
        this.setPopupMenu(popupMenu);
        this.addActionListener(this);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   direction  DOCUMENT ME!
     * @param   iconSize   DOCUMENT ME!
     * @param   model      DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JHistoryButton getDefaultJHistoryButton(final int direction,
            final int iconSize,
            final HistoryModel model) {
        final JHistoryButton ret = new JHistoryButton();
        ret.setDirection(direction);
        ret.setHistoryModel(model);
        final String ressourcePath = "/de/cismet/tools/gui/historybutton/res/";                // NOI18N
        final String forward = "forward";                                                      // NOI18N
        final String back = "back";                                                            // NOI18N
        String name;
        final String filetype = ".png";                                                        // NOI18N
        if (direction == DIRECTION_FORWARD) {
            name = forward;
        } else {
            name = back;
        }
        switch (iconSize) {
            case ICON_SIZE_16: {
                ret.setIcon(new javax.swing.ImageIcon(
                        ret.getClass().getResource(ressourcePath + name + "16" + filetype)));  // NOI18N
                break;
            }
            case ICON_SIZE_22: {
                ret.setIcon(new javax.swing.ImageIcon(
                        ret.getClass().getResource(ressourcePath + name + "22" + filetype)));  // NOI18N
                break;
            }
            case ICON_SIZE_32: {
                ret.setIcon(new javax.swing.ImageIcon(
                        ret.getClass().getResource(ressourcePath + name + "32" + filetype)));  // NOI18N
                break;
            }
            case ICON_SIZE_64: {
                ret.setIcon(new javax.swing.ImageIcon(
                        ret.getClass().getResource(ressourcePath + name + "64" + filetype)));  // NOI18N
                break;
            }
            case ICON_SIZE_128: {
                ret.setIcon(new javax.swing.ImageIcon(
                        ret.getClass().getResource(ressourcePath + name + "128" + filetype))); // NOI18N
                break;
            }
        }
        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public HistoryModel getHistoryModel() {
        return historyModel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  historyModel  DOCUMENT ME!
     */
    public void setHistoryModel(final HistoryModel historyModel) {
        this.historyModel = historyModel;
        historyModel.addHistoryModelListener(this);
        setEnabled(localEnabled);
        if (localEnabled && (direction == DIRECTION_FORWARD)) {
            forwardStatusChanged();
        } else {
            backStatusChanged();
        }
    }

    @Override
    public void historyChanged() {
    }

    @Override
    public void forwardStatusChanged() {
        // you have to check whether the component is enabled
        // if you have to disable the component due to history reasons
        // you have to modify the super component directly
        // otherwise there would be no chance to disable the component permanently
        if (localEnabled && (direction == DIRECTION_FORWARD)) {
            if (historyModel.isForwardPossible()) {
                setEnabled(true);
                final Vector poss = historyModel.getForwardPossibilities();
                refreshPopup(poss);
            } else {
                super.setEnabled(false);
            }
        }
    }

    @Override
    public void backStatusChanged() {
        // you have to check whether the component is enabled
        // if you have to disable the component due to history reasons
        // you have to modify the super component directly
        // otherwise there would be no chance to disable the component permanently
        if (localEnabled && (direction == DIRECTION_BACKWARD)) {
            if (historyModel.isBackPossible()) {
                setEnabled(true);
                final Vector poss = historyModel.getBackPossibilities();
                refreshPopup(poss);
            } else {
                super.setEnabled(false);
                refreshPopup(null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  possibilities  DOCUMENT ME!
     */
    private void refreshPopup(Vector possibilities) {
        if (showPopupMenu) {
            if (possibilities == null) {
                possibilities = new Vector();
            }
            popupMenu.removeAll();

            for (int i = possibilities.size() - 1; i >= 0; --i) {
                final Object o = possibilities.get(i);
                final JHistoryMenuItem item = new JHistoryMenuItem(o, possibilities.size() - i);
                popupMenu.add(item);
                item.addActionListener(this);
            }
            if (possibilities.size() > maxHistoryMenuLength) {
                final int tooMuch = possibilities.size() - maxHistoryMenuLength;
                for (int i = 0; i < tooMuch; ++i) {
                    popupMenu.remove(possibilities.size() - 1 - i);
                }
            }
        }
    }

    @Override
    public void setEnabled(final boolean enabled) {
        localEnabled = enabled;
        if (historyModel != null) {
            super.setEnabled(localEnabled);
        } else {
            super.setEnabled(false);
        }
    }

    @Override
    public void actionPerformed(final java.awt.event.ActionEvent e) {
        if ((e != null) && (e.getSource() instanceof JHistoryMenuItem)) {
            final JHistoryMenuItem source = (JHistoryMenuItem)e.getSource();
            for (int i = 0; i < (source.getPosition() - 1); ++i) {
                if (direction == DIRECTION_BACKWARD) {
                    historyModel.back(false);
                } else if (direction == DIRECTION_FORWARD) {
                    historyModel.forward(false);
                }
            }
            fireActionPerformed(new ActionEvent(this, 0, "JHistoryButtonMenuActionPerformed")); // NOI18N
        } else if ((e != null) && (e.getSource() instanceof JHistoryButton)) {                  // &&e.getActionCommand()!="JHistoryButtonMenuActionPerformed"
            Object o = null;
            if (direction == DIRECTION_BACKWARD) {
                o = historyModel.back(true);
            } else if (direction == DIRECTION_FORWARD) {
                o = historyModel.forward(true);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getDirection() {
        return direction;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  direction  DOCUMENT ME!
     *
     * @beaninfo
     *     attribute: visualUpdate true
     *         bound: true
     *   description: The alignment of the label's content along the X axis.
     *          enum: DIRECTION_FORWARD  JHistoryButton.DIRECTION_FORWARD
     *                DIRECTION_BACKWARD JHistoryButton.DIRECTION_BACKWARD
     */

    public void setDirection(final int direction) {
        this.direction = direction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JLabel getJHistoryLabel() {
        return new JHistoryLabel();
    }

    @Override
    public void historyActionPerformed() {
    }

    /**
     * Notifies all listeners that have registered interest for notification on this event type. The event instance is
     * lazily created using the <code>event</code> parameter.
     *
     * @param  event  the <code>ActionEvent</code> object
     *
     * @see    EventListenerList
     */
    @Override
    protected void fireActionPerformed(final ActionEvent event) {
        if ((popupMenu == null) || (mouseInPopupArea == false)) {
            super.fireActionPerformed(event);
        }
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class JHistoryLabel extends JLabel implements HistoryModelListener {

        //~ Instance fields ----------------------------------------------------

        String text;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new JHistoryLabel object.
         */
        public JHistoryLabel() {
            historyModel.addHistoryModelListener(this);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void historyChanged() {
            text = historyModel.getCurrentElement().toString();
            setText(text);
        }

        @Override
        public void forwardStatusChanged() {
        }

        @Override
        public void backStatusChanged() {
        }

        @Override
        public String getText() {
            if ((text != null) && (text.length() > 0)) {
                return text;
            } else {
                historyChanged();
                return text;
            }
        }

        @Override
        public void historyActionPerformed() {
        }
    }
}
