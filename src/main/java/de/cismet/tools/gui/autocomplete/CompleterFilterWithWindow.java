/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.autocomplete;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CompleterFilterWithWindow extends CompleterFilter {

    //~ Static fields/initializers ---------------------------------------------

    public static int MAX_VISIBLE_ROWS = 8;

    //~ Instance fields --------------------------------------------------------

    private FilterWindowListener fwl;
    private JWindow win;
    private TextFieldKeyListener tfkl;
    private ListSelListener lsl;
    private ListMouseListener lml;
    private JList list;
    private JScrollPane sp;
    private FilterListModel lm;
    private boolean isAdjusting = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CompleterFilterWithWindow object.
     *
     * @param  completerObjs  DOCUMENT ME!
     * @param  textField      DOCUMENT ME!
     */
    public CompleterFilterWithWindow(final Object[] completerObjs, final JTextField textField) {
        super(completerObjs, textField);
        _init();
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void insertString(final FilterBypass filterBypass,
            final int offset,
            final String string,
            final AttributeSet attributeSet) throws BadLocationException {
        setFilterWindowVisible(false);
        super.insertString(filterBypass, offset, string, attributeSet);
    }

    @Override
    public void remove(final FilterBypass filterBypass, final int offset, final int length)
            throws BadLocationException {
        setFilterWindowVisible(false);
        super.remove(filterBypass, offset, length);
    }

    @Override
    public void replace(final FilterBypass filterBypass,
            final int offset,
            final int length,
            final String string,
            final AttributeSet attributeSet) throws BadLocationException {
        if (isAdjusting) {
            filterBypass.replace(offset, length, string, attributeSet);
            return;
        }

        super.replace(filterBypass, offset, length, string, attributeSet);

        if (getLeadingSelectedIndex() == -1) {
            if (isFilterWindowVisible()) {
                setFilterWindowVisible(false);
            }

            return;
        }

        lm.setFilter(preText);

        if (!isFilterWindowVisible()) {
            setFilterWindowVisible(true);
        } else {
            _setWindowHeight();
        }

        list.setSelectedValue(textField.getText(), true);
    }

    /**
     * DOCUMENT ME!
     */
    private void _init() {
        fwl = new FilterWindowListener();
        lm = new FilterListModel(objectList);
        tfkl = new TextFieldKeyListener();
        textField.addKeyListener(tfkl);

        final EscapeAction escape = new EscapeAction();
        textField.registerKeyboardAction(
            escape,
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
            JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isFilterWindowVisible() {
        return ((win != null) && (win.isVisible()));
    }

    @Override
    public void setCaseSensitive(final boolean caseSensitive) {
        super.setCaseSensitive(caseSensitive);
        lm.setCaseSensitive(caseSensitive);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  visible  DOCUMENT ME!
     */
    public void setFilterWindowVisible(final boolean visible) {
        if (visible) {
            _initWindow();
            list.setModel(lm);
            win.setVisible(true);
            textField.requestFocus();
            textField.addFocusListener(fwl);
        } else {
            if (win == null) {
                return;
            }

            win.setVisible(false);
            win.removeFocusListener(fwl);
            final Window ancestor = SwingUtilities.getWindowAncestor(textField);
            ancestor.removeMouseListener(fwl);
            textField.removeFocusListener(fwl);
            textField.removeAncestorListener(fwl);
            list.removeMouseListener(lml);
            list.removeListSelectionListener(lsl);
            lsl = null;
            lml = null;
            win.dispose();
            win = null;
            list = null;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void _initWindow() {
        final Window ancestor = SwingUtilities.getWindowAncestor(textField);
        win = new JWindow(ancestor);
        win.addWindowFocusListener(fwl);
        textField.addAncestorListener(fwl);
        ancestor.addMouseListener(fwl);
        lsl = new ListSelListener();
        lml = new ListMouseListener();

        list = new JList(lm);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFocusable(false);
        list.setPrototypeCellValue("Prototype"); // NOI18N
        list.addListSelectionListener(lsl);
        list.addMouseListener(lml);

        sp = new JScrollPane(list,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setFocusable(false);
        sp.getVerticalScrollBar().setFocusable(false);

        _setWindowHeight();
        win.setLocation(textField.getLocationOnScreen().x, textField.getLocationOnScreen().y + textField.getHeight());
        win.getContentPane().add(sp);
    }

    /**
     * DOCUMENT ME!
     */
    private void _setWindowHeight() {
        int height = list.getFixedCellHeight() * Math.min(MAX_VISIBLE_ROWS, lm.getSize());
        height += list.getInsets().top + list.getInsets().bottom;
        height += sp.getInsets().top + sp.getInsets().bottom;

        win.setSize(textField.getWidth(), height);
        sp.setSize(textField.getWidth(), height); // bottom border fails to draw without this
    }

    @Override
    public void setCompleterMatches(final Object[] objectsToMatch) {
        if (isFilterWindowVisible()) {
            setFilterWindowVisible(false);
        }

        super.setCompleterMatches(objectsToMatch);
        lm.setCompleterMatches(objectsToMatch);
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class EscapeAction extends AbstractAction {

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  e  DOCUMENT ME!
         */
        @Override
        public void actionPerformed(final ActionEvent e) {
            if (isFilterWindowVisible()) {
                setFilterWindowVisible(false);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class FilterWindowListener extends MouseAdapter implements AncestorListener,
        FocusListener,
        WindowFocusListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void ancestorMoved(final AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        @Override
        public void ancestorAdded(final AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        @Override
        public void ancestorRemoved(final AncestorEvent event) {
            setFilterWindowVisible(false);
        }

        @Override
        public void focusLost(final FocusEvent e) {
            if (e.getOppositeComponent() != win) {
                setFilterWindowVisible(false);
            }
        }

        @Override
        public void focusGained(final FocusEvent e) {
        }

        @Override
        public void windowLostFocus(final WindowEvent e) {
            final Window w = e.getOppositeWindow();

            if (w.getFocusOwner() != textField) {
                setFilterWindowVisible(false);
            }
        }

        @Override
        public void windowGainedFocus(final WindowEvent e) {
        }

        @Override
        public void mousePressed(final MouseEvent e) {
            setFilterWindowVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class TextFieldKeyListener extends KeyAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void keyPressed(final KeyEvent e) {
            if (!((e.getKeyCode() == KeyEvent.VK_DOWN)
                            || (e.getKeyCode() == KeyEvent.VK_UP)
                            || ((e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) && (isFilterWindowVisible()))
                            || ((e.getKeyCode() == KeyEvent.VK_PAGE_UP) && (isFilterWindowVisible()))
                            || (e.getKeyCode() == KeyEvent.VK_ENTER))) {
                return;
            }

            if ((e.getKeyCode() == KeyEvent.VK_DOWN) && !isFilterWindowVisible()) {
                preText = textField.getText();
                lm.setFilter(preText);

                if (lm.getSize() > 0) {
                    setFilterWindowVisible(true);
                } else {
                    return;
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (isFilterWindowVisible()) {
                    setFilterWindowVisible(false);
                }

                textField.setCaretPosition(textField.getText().length());
                return;
            }

            int index = -1;

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                index = Math.min(list.getSelectedIndex() + 1, list.getModel().getSize() - 1);
            } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                index = Math.max(list.getSelectedIndex() - 1, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                index = Math.max(list.getSelectedIndex() - MAX_VISIBLE_ROWS, 0);
            } else if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                index = Math.min(list.getSelectedIndex() + MAX_VISIBLE_ROWS, list.getModel().getSize() - 1);
            }

            if (index == -1) {
                return;
            }

            list.setSelectedIndex(index);
            list.scrollRectToVisible(list.getCellBounds(index, index));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ListSelListener implements ListSelectionListener {

        //~ Methods ------------------------------------------------------------

        @Override
        public void valueChanged(final ListSelectionEvent e) {
            isAdjusting = true;
            textField.setText(list.getSelectedValue().toString());
            isAdjusting = false;
            textField.select(preText.length(), textField.getText().length());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class ListMouseListener extends MouseAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void mouseClicked(final MouseEvent e) {
            if (e.getClickCount() == 2) {
                setFilterWindowVisible(false);
            }
        }
    }
}
