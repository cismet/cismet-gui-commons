/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jdesktop.swingx.autocomplete.AutoCompleteDecorator;

import org.openide.util.Exceptions;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.io.File;
import java.io.IOException;

import java.lang.reflect.InvocationTargetException;

import java.net.MalformedURLException;
import java.net.URL;

import java.text.MessageFormat;

import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.cismet.tools.Static2DTools;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class StaticSwingTools {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StaticSwingTools.class);

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  tree  DOCUMENT ME!
     */
    public static void jTreeExpandAllNodes(final JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tree  DOCUMENT ME!
     */
    public static void jTreeExpandAllNodesAndScroll2Last(final JTree tree) {
        // expand to the last leaf from the root
        final DefaultMutableTreeNode root;
        root = (DefaultMutableTreeNode)tree.getModel().getRoot();
        tree.scrollPathToVisible(new TreePath(root.getLastLeaf().getPath()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tree  DOCUMENT ME!
     */
    public static void jTreeCollapseAllNodes(final JTree tree) {
        final TreePath p = tree.getSelectionPath();

        int row = tree.getRowCount() - 1;
        tree.setSelectionPath(p);
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
        tree.setSelectionPath(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  button     DOCUMENT ME!
     * @param  keyStroke  DOCUMENT ME!
     * @param  rootPane   DOCUMENT ME!
     */
    public static void doClickButtonOnKeyStroke(final JButton button,
            final KeyStroke keyStroke,
            final JRootPane rootPane) {
        rootPane.registerKeyboardAction(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    button.doClick();
                }
            },
            keyStroke,
            JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   <T>    DOCUMENT ME!
     * @param   c      DOCUMENT ME!
     * @param   clazz  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @SuppressWarnings("unchecked")
    public static <T extends Component> T findSpecificParentComponent(Container c, final Class<T> clazz) {
//        while (c != null && !(c.getClass().equals(clazz))) {
        while ((c != null) && !(clazz.isAssignableFrom(c.getClass()))) {
            c = c.getParent();
        }
        return (T)c;
    }
    /**
     * From The Java Developers Almanac.
     *
     * @param  table      DOCUMENT ME!
     * @param  rowIndex   DOCUMENT ME!
     * @param  vColIndex  DOCUMENT ME!
     */
    public static void jTableScrollToVisible(final JTable table, final int rowIndex, final int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        final JViewport viewport = (JViewport)table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        final Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        final Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   dtde  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static String getLinkFromDropEvent(final DropTargetDropEvent dtde) {
        // System.out.println("getLinkFromDropEvent");
        final String link = null;
        final Transferable t = dtde.getTransferable();
        final DataFlavor[] flavors = t.getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            final DataFlavor flavor = flavors[i];
            try {
                if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                    // System.out.println("importData: FileListFlavor");
                    final List l = (List)t.getTransferData(DataFlavor.javaFileListFlavor);
                    final Iterator iter = l.iterator();
                    while (iter.hasNext()) {
                        final File file = (File)iter.next();
                        System.out.println(file);
                        try {
                            final String can = file.getCanonicalPath();
                            return can;
                        } catch (IOException ex) {
                            log.warn("Fehler bei file.getCanonicalPath()", ex); // NOI18N
                        }
                    }
                } else if (flavor.equals(DataFlavor.stringFlavor)) {
                    final String fileOrURL = (String)t.getTransferData(flavor);
                    if (log.isDebugEnabled()) {
                        log.debug("GOT STRING: " + fileOrURL);                  // NOI18N
                    }

                    try {
                        final URL url = new URL(fileOrURL);
                        return url.toString();
                    } catch (MalformedURLException ex) {
                        log.warn("Illegal URL", ex); // NOI18N
                        return null;
                    }
                } else {
                    // log.warn("importData rejected: "+ flavor);
                    // mach nix und probiers weiter
                }
            } catch (IOException ex) {
                log.warn("IOError getting data: " + ex); // NOI18N
            } catch (UnsupportedFlavorException e) {
                log.warn("Unsupported Flavor: ", e); // NOI18N
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tabPane  DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  comp     DOCUMENT ME!
     */
    public static void jTabbedPaneWithVerticalTextAddTab(final JTabbedPane tabPane,
            final String text,
            final JComponent comp) {
        jTabbedPaneWithVerticalTextAddTab(tabPane, text, null, comp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tabPane  DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  icon     DOCUMENT ME!
     * @param  comp     DOCUMENT ME!
     */
    public static void jTabbedPaneWithVerticalTextAddTab(final JTabbedPane tabPane,
            final String text,
            final Icon icon,
            final JComponent comp) {
        final int tabPlacement = tabPane.getTabPlacement();
        final Object textIconGap = UIManager.get("TabbedPane.textIconGap");   // NOI18N
        final Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets"); // NOI18N
        tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
        UIManager.put("TabbedPane.textIconGap", new Integer(1));              // NOI18N
        // UIManager.put("TabbedPane.tabInsets", new Insets(tabInsets.left, tabInsets.top, tabInsets.right,
        // tabInsets.bottom));
        UIManager.put("TabbedPane.tabInsets", tabInsets);                     // NOI18N
        SwingUtilities.updateComponentTreeUI(tabPane);
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT: {
                if (icon == null) {
                    tabPane.addTab(null, new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT), comp);
                } else {
                    Icon newIcon;
                    if (tabPlacement == JTabbedPane.RIGHT) {
                        final Icon[] icons = new Icon[2];
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT);
                        icons[1] = icon;
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    } else {
                        final Icon[] icons = new Icon[2];
                        icons[1] = icon;
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT);
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("newIconHeight" + newIcon.getIconHeight()); // NOI18N
                    }
                    tabPane.addTab(null, newIcon, comp);
                }
                break;
            }
            default: {
                tabPane.addTab(text, null, comp);
            }
        }
        tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
        UIManager.put("TabbedPane.tabInsets", tabInsets);                     // NOI18N
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tabPane  DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  comp     DOCUMENT ME!
     */
    public static void jTabbedPaneWithVerticalTextSetNewText(final JTabbedPane tabPane,
            final String text,
            final JComponent comp) {
        jTabbedPaneWithVerticalTextSetNewText(tabPane, text, null, comp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tabPane  DOCUMENT ME!
     * @param  text     DOCUMENT ME!
     * @param  icon     DOCUMENT ME!
     * @param  comp     DOCUMENT ME!
     */
    public static void jTabbedPaneWithVerticalTextSetNewText(final JTabbedPane tabPane,
            final String text,
            final Icon icon,
            final JComponent comp) {
        jTabbedPaneWithVerticalTextSetNewText(tabPane, text, icon, Color.black, comp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  tabPane    DOCUMENT ME!
     * @param  text       DOCUMENT ME!
     * @param  icon       DOCUMENT ME!
     * @param  textColor  DOCUMENT ME!
     * @param  comp       DOCUMENT ME!
     */
    public static void jTabbedPaneWithVerticalTextSetNewText(final JTabbedPane tabPane,
            final String text,
            final Icon icon,
            final Color textColor,
            final JComponent comp) {
        final int tabPlacement = tabPane.getTabPlacement();
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT: {
                if (icon == null) {
                    tabPane.setIconAt(tabPane.indexOfComponent(comp),
                        new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor));
                } else {
                    Icon newIcon;
                    if (tabPlacement == JTabbedPane.RIGHT) {
                        final Icon[] icons = new Icon[2];
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor);
                        icons[1] = icon;
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    } else {
                        final Icon[] icons = new Icon[2];
                        icons[1] = icon;
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor);
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    }
                    if (log.isDebugEnabled()) {
                        log.debug("newIconHeight" + newIcon.getIconHeight()); // NOI18N
                    }
                    tabPane.setIconAt(tabPane.indexOfComponent(comp), newIcon);
                }
                return;
            }
            default: {
                tabPane.setTitleAt(tabPane.indexOfComponent(comp), text);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   tabPlacement  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JTabbedPane jTabbedPaneWithVerticalTextCreator(final int tabPlacement) {
        final java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            final Object key = keys.nextElement();
            final Object value = UIManager.get(key);
            if (key.toString().indexOf("abbed") != -1) { // NOI18N
                if (log.isDebugEnabled()) {
                    log.debug(key + "," + value);        // NOI18N
                }
            }
        }
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT: {
//                Object textIconGap = UIManager.get("TabbedPane.textIconGap");
//                Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
//                tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
//                UIManager.put("TabbedPane.textIconGap", new Integer(1));
//                // UIManager.put("TabbedPane.tabInsets", new Insets(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom));
//                UIManager.put("TabbedPane.tabInsets", tabInsets);
                final JTabbedPane tabPane = new JTabbedPane(tabPlacement);
//
//                UIManager.put("TabbedPane.textIconGap", textIconGap);
//                UIManager.put("TabbedPane.tabInsets", tabInsets);
//                //SwingUtilities.updateComponentTreeUI(tabPane);
                // tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
                // UIManager.put("TabbedPane.tabInsets", tabInsets);
                return tabPane;
            }
            default: {
                return new JTabbedPane(tabPlacement);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   tabPlacement     DOCUMENT ME!
     * @param   tabLayoutPolicy  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static JTabbedPane jTabbedPaneWithVerticalTextCreator(final int tabPlacement, final int tabLayoutPolicy) {
        final JTabbedPane jtp = jTabbedPaneWithVerticalTextCreator(tabPlacement);
        return jtp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  sPane  DOCUMENT ME!
     */
    public static void setNiftyScrollBars(final JScrollPane sPane) {
        try {
            Dimension d = sPane.getVerticalScrollBar().getPreferredSize();
            sPane.getVerticalScrollBar().getComponent(0).setVisible(false);
            sPane.getVerticalScrollBar().getComponent(1).setVisible(false);
            sPane.getVerticalScrollBar().setPreferredSize(new Dimension(8, d.height));
            d = sPane.getHorizontalScrollBar().getPreferredSize();
            sPane.getHorizontalScrollBar().getComponent(0).setVisible(false);
            sPane.getHorizontalScrollBar().getComponent(1).setVisible(false);
            sPane.getHorizontalScrollBar().setPreferredSize(new Dimension(d.width, 8));
            sPane.setBackground(sPane.getViewport().getBackground());
            sPane.getHorizontalScrollBar().setBackground(sPane.getViewport().getBackground());
            sPane.getVerticalScrollBar().setBackground(sPane.getViewport().getBackground());
        } catch (Exception e) {
            log.warn("Cannot create the nifty scrollbars.", e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Frame getFirstParentFrame(final Component c) {
        return getParentFrame(c, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Frame getParentFrame(final Component c) {
        return getParentFrame(c, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   c      DOCUMENT ME!
     * @param   first  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Frame getParentFrame(final Component c, final boolean first) {
        try {
            Object o = c;
            do {
                o = ((Component)o).getParent();
                if (log.isDebugEnabled()) {
                    log.debug("getParent:" + o);          // NOI18N
                }
            } while (!(((o instanceof Frame) && (((Component)o).getParent() == null)) || first));
            if (log.isDebugEnabled()) {
                log.debug("getParentFrame returns " + o); // NOI18N
            }
            return (Frame)o;
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("getParentFrame returns null", e); // NOI18N
            }
            return null;
        }
    }

    /**
     * Returns the parent frame of the given component or the given component if there is no parent frame.
     *
     * @param   c  component whose parent frame shall be determined
     *
     * @return  parent frame or c if there is no parent frame
     */
    public static Component getParentFrameIfNotNull(final Component c) {
        final Component parent = getParentFrame(c);
        if (parent == null) {
            return c;
        }

        return parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   components  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public static Rectangle getComponentsExtent(final Component... components) {
        final Rectangle r = new Rectangle();
        Integer maxX = null;
        Integer minX = null;
        Integer maxY = null;
        Integer minY = null;

        for (final Component comp : components) {
            // firsttimecheck
            if (maxX == null) {
                minX = comp.getX();
                minY = comp.getY();
                maxX = comp.getWidth() + minX;
                maxY = comp.getHeight() + minY;
            } else {
                // min
                minX = Math.min(minX, comp.getX());
                minY = Math.min(minY, comp.getY());

                // max
                maxX = Math.max(maxX, comp.getX() + comp.getWidth());
                maxY = Math.max(maxY, comp.getY() + comp.getHeight());
            }
        }

        r.setRect(minX, minY, maxX - minX, maxY - minY);
        return r;
    }

    /**
     * Shows given dialog centered relative to the given parent.
     *
     * @param  parent                   dialog parent
     * @param  dialog                   dialog
     * @param  isRelativeToParentFrame  true if the dialog shall be centered relative to the parent frame (determined by
     *                                  the given parent), false otherwise
     */
    public static void showDialog(final Component parent,
            final JDialog dialog,
            final boolean isRelativeToParentFrame) {
        if (dialog.isVisible()) {
            dialog.toFront();
        } else {
            if (isRelativeToParentFrame && (parent != null)) {
                final Frame parentFrame = getParentFrame(parent);

                // UGLY BUT IT WORKS - this part is for Windows users
                // enforcing dialog to be set on top of other dialogs to prevent
                // that the dialog becomes unreachable behind another modal dialog
                dialog.setAlwaysOnTop(true);
                dialog.toFront();
                dialog.requestFocus();
                dialog.setAlwaysOnTop(false);

                if (parentFrame == null) {
                    centerWindowOnScreen(dialog);
                } else {
                    dialog.setLocationRelativeTo(parentFrame);
                    dialog.setVisible(true);
                }
            } else {
                centerWindowOnScreen(dialog);
            }
        }
    }

    /**
     * Centers a Window instance on the screen on which the mouse pointer is located.
     *
     * @param  w  window instance to be centered
     */
    public static void centerWindowOnScreen(final Window w) {
        final PointerInfo pInfo = MouseInfo.getPointerInfo();
        final Point pointerLocation = pInfo.getLocation();

        // determine screen boundaries w.r.t. the current mouse position
        final GraphicsConfiguration[] cfgArr = pInfo.getDevice().getConfigurations();

        Rectangle bounds = null;
        for (int i = 0; i < cfgArr.length; i++) {
            bounds = cfgArr[i].getBounds();

            if (pointerLocation.x <= bounds.x) {
                break;
            }
        }

        // determine coordinates in the center of the current mouse location
        final int x = bounds.x + ((bounds.width - w.getWidth()) / 2);
        final int y = bounds.y + ((bounds.height - w.getHeight()) / 2);

        if (!EventQueue.isDispatchThread()) {
            try {
                EventQueue.invokeAndWait(new Thread("centerWindowOnScreen") {

                        @Override
                        public void run() {
                            // show window
                            w.setLocation(x, y);
                            w.setVisible(true);
                        }
                    });
            } catch (InterruptedException | InvocationTargetException ex) {
                log.error("Error while center window on screen", ex);
                // nothing to do
            }
        } else {
            // show window
            w.setLocation(x, y);
            w.setVisible(true);
        }
    }

    /**
     * Determines the bounds of a component to centers it on the screen on which the mouse pointer is located.
     *
     * @param   c  Component instance to be centered
     *
     * @return  The bounds of the component
     */
    public static Rectangle getCenterBoundsForComponent(final Component c) {
        final PointerInfo pInfo = MouseInfo.getPointerInfo();
        final Point pointerLocation = pInfo.getLocation();

        // determine screen boundaries w.r.t. the current mouse position
        final GraphicsConfiguration[] cfgArr = pInfo.getDevice().getConfigurations();

        Rectangle bounds = null;
        for (int i = 0; i < cfgArr.length; i++) {
            bounds = cfgArr[i].getBounds();

            if (pointerLocation.x <= bounds.x) {
                break;
            }
        }

        // determine coordinates in the center of the current mouse location
        final int x = bounds.x + ((bounds.width - c.getWidth()) / 2);
        final int y = bounds.y + ((bounds.height - c.getHeight()) / 2);

        return new Rectangle(x, y, c.getWidth(), c.getHeight());
    }

    /**
     * Shows given dialog centered relative to its parent frame.
     *
     * @param  dialog  dialog
     */
    public static void showDialog(final JDialog dialog) {
        showDialog(dialog.getParent(), dialog, true);
    }

    /**
     * Shows given dialog centered relative to its parent frame.
     *
     * @param  dialog                   dialog
     * @param  isRelativeToParentFrame  true if the dialog shall be centered relative to the parent frame (determined by
     *                                  the given parent), false if it shall be centered relative to its parent
     */
    public static void showDialog(final JDialog dialog, final boolean isRelativeToParentFrame) {
        showDialog(dialog.getParent(), dialog, isRelativeToParentFrame);
    }

    /**
     * Calls pack() on a parent JDialog if available. Very useful to resize JDialogs from an embedded JPanel.
     *
     * @param  component  The component from which a parent JDialog is searched.
     */
    public static void tryPackingMyParentDialog(final Component component) {
        Component parent = component;

        while (!(parent instanceof JDialog) && (parent != null)) {
            parent = parent.getParent();
        }

        if (parent instanceof JDialog) {
            ((JDialog)parent).pack();
        }
    }

    /**
     * Adds a change listener to the given slider which ensures that the current value of the slider is permanently
     * shown in the tooltip. Found here: http://www.jroller.com/santhosh/entry/tooltips_can_say_more
     *
     * @param  slider  The slider to modify.
     * @param  format  The format to be used to print the value (e. g. "Percentage: {0,number,#0.0}%").
     * @param  factor  A factor for the value. E. g. if the slider allows sliding between 0 and 1000 but we want to show
     *                 the percentage, we have to multiply the value by 0.1D. Set to Double.NaN if it shouldn't be used.
     */
    public static void enableSliderToolTips(final JSlider slider,
            final MessageFormat format,
            final double factor) {
        slider.addChangeListener(new ChangeListener() {

                private boolean adjusting = false;
                private String oldTooltip;

                @Override
                public void stateChanged(final ChangeEvent e) {
                    if (slider.getModel().getValueIsAdjusting()) {
                        if (!adjusting) {
                            oldTooltip = slider.getToolTipText();
                            adjusting = true;
                        }

                        final double value = slider.getValue() * (Double.isNaN(factor) ? 1D : factor);
                        slider.setToolTipText(
                            (format != null) ? format.format(new Object[] { value }) : Double.toString(value));

                        hideToolTip(slider); // to avoid flickering
                        postToolTip(slider);
                    } else {
                        hideToolTip(slider);
                        slider.setToolTipText(oldTooltip);
                        adjusting = false;
                        oldTooltip = null;
                    }
                }
            });
    }

    /**
     * Shows a component's tooltip.
     *
     * @param  comp  The component whose tooltip shall appear.
     */
    public static void postToolTip(final JComponent comp) {
        final Action action = comp.getActionMap().get("postTip");
        if (action == null) {
            // no tooltip
            return;
        }

        final ActionEvent actionEvent = new ActionEvent(
                comp,
                ActionEvent.ACTION_PERFORMED,
                "postTip",
                EventQueue.getMostRecentEventTime(),
                0);
        action.actionPerformed(actionEvent);
    }

    /**
     * Hides a component's tooltip.
     *
     * @param  comp  The component whose tooltip shall disappear.
     */
    public static void hideToolTip(final JComponent comp) {
        final Action action = comp.getActionMap().get("hideTip");
        if (action == null) {
            // no tooltip
            return;
        }

        final ActionEvent actionEvent = new ActionEvent(
                comp,
                ActionEvent.ACTION_PERFORMED,
                "hideTip",
                EventQueue.getMostRecentEventTime(),
                0);
        action.actionPerformed(actionEvent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  cbo  DOCUMENT ME!
     */
    public static void decorateWithFixedAutoCompleteDecorator(final JComboBox cbo) {
        AutoCompleteDecorator.decorate(cbo);
        final JList pop = ((ComboPopup)cbo.getUI().getAccessibleChild(cbo, 0)).getList();
        final JTextField txt = (JTextField)cbo.getEditor().getEditorComponent();

        txt.addKeyListener(new KeyAdapter() {

                @Override
                public void keyReleased(final KeyEvent event) {
                    if ((event.getKeyCode() == KeyEvent.VK_DOWN) || (event.getKeyCode() == KeyEvent.VK_UP)) {
                        final Object selectedValue = pop.getSelectedValue();
                        if (selectedValue != null) {
                            txt.setText(String.valueOf(selectedValue));
                        }
                        txt.selectAll();
                    }
                }
            });
    }

    /**
     * A Method to make UIManger.put comands configurable via a json file.
     *
     * <p>The json File has to placed to <code>/de/cismet/tools/gui/uitweaks.json</code>.</p>
     *
     * <p>An example json file can be found on <a href="https://github.com/cismet/cismet-gui-commons/issues/39">
     * GitHub</a>.</p>
     */
    public static void tweakUI() {
        final ObjectMapper mapper = new ObjectMapper();
        try {
            final UITweaks config = mapper.readValue(UITweaks.class.getResourceAsStream(
                        "/de/cismet/tools/gui/uitweaks.json"),
                    UITweaks.class);
            config.apply();
        } catch (Exception e) {
            log.warn("Problem during TweakingUI", e);
        }
    }

    /**
     * Opens a JFileChooser with a filter for the given file extensions and checks if the chosen file has the right
     * extension. If not the first right extension is added.
     *
     * @param   currentDirectoryPath      The currebnt path that should be shown in the dialog
     * @param   isSaveDialog              True, if a save dialog should be shown. Otherwise a open dialog will be shown
     * @param   allowedFileExtension      all allowed file extensions or null, if every extension should be allowed
     * @param   fileExtensionDescription  The description of the file extension.
     * @param   parent                    the parent component of the dialog
     *
     * @return  DOCUMENT ME!
     */
    public static File chooseFile(final String currentDirectoryPath,
            final boolean isSaveDialog,
            final String[] allowedFileExtension,
            final String fileExtensionDescription,
            final Component parent) {
        JFileChooser fc;

        try {
            fc = new ConfirmationJFileChooser(currentDirectoryPath);
        } catch (Exception bug) {
            // Bug Workaround http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6544857
            fc = new JFileChooser(currentDirectoryPath, new RestrictedFileSystemView());
        }

        FileFilter fileFilter;

        if ((allowedFileExtension != null) && (allowedFileExtension.length == 1)) {
            fileFilter = new ExtensionAwareFileFilter() {

                    @Override
                    public boolean accept(final File f) {
                        boolean fileAllowed = f.isDirectory();

                        if (!fileAllowed) {
                            final String extension = (f.getName().contains(".")
                                    ? f.getName().substring(f.getName().indexOf(".") + 1) : "");

                            if (extension.equals(allowedFileExtension[0])) {
                                fileAllowed = true;
                            }
                        }

                        return fileAllowed;
                    }

                    @Override
                    public String getDescription() {
                        String description = fileExtensionDescription;

                        if (description == null) {
                            description = allowedFileExtension[0];
                        }

                        return description;
                    }

                    @Override
                    public String getExtension() {
                        return allowedFileExtension[0];
                    }
                };
        } else {
            fileFilter = new FileFilter() {

                    @Override
                    public boolean accept(final File f) {
                        boolean fileAllowed = f.isDirectory();

                        if (allowedFileExtension == null) {
                            fileAllowed = true;
                        } else if (!fileAllowed) {
                            final String extension = (f.getName().contains(".")
                                    ? f.getName().substring(f.getName().indexOf(".") + 1) : "");

                            for (final String allowedExt : allowedFileExtension) {
                                if (extension.equals(allowedExt)) {
                                    fileAllowed = true;
                                    break;
                                }
                            }
                        }

                        return fileAllowed;
                    }

                    @Override
                    public String getDescription() {
                        String description = fileExtensionDescription;

                        if ((description == null) && (allowedFileExtension != null)) {
                            for (final String allowedExt : allowedFileExtension) {
                                if (description == null) {
                                    description = allowedExt;
                                } else {
                                    description += ", " + allowedExt;
                                }
                            }
                        } else {
                            description += "";
                        }

                        return description;
                    }
                };
        }

        fc.setAcceptAllFileFilterUsed(false);
        fc.setFileFilter(fileFilter);

        final int state = (isSaveDialog ? fc.showSaveDialog(parent) : fc.showOpenDialog(parent));
        if (log.isDebugEnabled()) {
            log.debug("state:" + state); // NOI18N
        }

        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (!fileFilter.accept(file)) {
                file = new File(file.getAbsolutePath() + "." + allowedFileExtension[0]);

                if (file.exists() && ((allowedFileExtension == null) || (allowedFileExtension.length != 1))) {
                    final String message = org.openide.util.NbBundle.getMessage(
                            ConfirmationJFileChooser.class,
                            "ConfirmationJFileChooser.approveSelection.message");
                    final String title = org.openide.util.NbBundle.getMessage(
                            ConfirmationJFileChooser.class,
                            "ConfirmationJFileChooser.approveSelection.title");

                    final int result = JOptionPane.showConfirmDialog(
                            parent,
                            message,
                            title,
                            JOptionPane.YES_NO_CANCEL_OPTION);

                    if (result == JOptionPane.YES_OPTION) {
                        return file;
                    } else {
                        return null;
                    }
                }
            }

            return file;
        } else {
            return null;
        }
    }

    /**
     * Opens a JFileChooser with a filter for the given file extensions and checks if the chosen file has the right
     * extension. If not the first right extension is added.
     *
     * @param   currentDirectoryPath      The currebnt path that should be shown in the dialog
     * @param   isSaveDialog              True, if a save dialog should be shown. Otherwise a open dialog will be shown
     * @param   allowedFileExtension      all allowed file extensions or null, if every extension should be allowed
     * @param   fileExtensionDescription  The descriptions for the allowed file extensions.
     * @param   parent                    the parent component of the dialog
     *
     * @return  DOCUMENT ME!
     */
    public static File chooseFileWithMultipleFilters(final String currentDirectoryPath,
            final boolean isSaveDialog,
            final String[] allowedFileExtension,
            final String[] fileExtensionDescription,
            final Component parent) {
        JFileChooser fc;

        try {
            fc = new ConfirmationJFileChooser(currentDirectoryPath);
        } catch (Exception bug) {
            // Bug Workaround http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6544857
            fc = new JFileChooser(currentDirectoryPath, new RestrictedFileSystemView());
        }

        for (int i = 0; i < allowedFileExtension.length; ++i) {
            final String description;
            final String ext = allowedFileExtension[i];

            if ((fileExtensionDescription != null) && (i < fileExtensionDescription.length)) {
                description = fileExtensionDescription[i];
            } else {
                description = allowedFileExtension[i];
            }

            final FileFilter fileFilter = new ExtensionAwareFileFilter() {

                    @Override
                    public boolean accept(final File f) {
                        final boolean fileAllowed = f.isDirectory();
                        final String extension = (f.getName().contains(".")
                                ? f.getName().substring(f.getName().indexOf(".") + 1) : "");

                        return fileAllowed || extension.equals(ext);
                    }

                    @Override
                    public String getDescription() {
                        return description;
                    }

                    @Override
                    public String getExtension() {
                        return ext;
                    }
                };

            fc.addChoosableFileFilter(fileFilter);

            if (i == 0) {
                fc.setFileFilter(fileFilter);
            }
        }

        fc.setAcceptAllFileFilterUsed(false);

        final int state = (isSaveDialog ? fc.showSaveDialog(parent) : fc.showOpenDialog(parent));
        if (log.isDebugEnabled()) {
            log.debug("state:" + state); // NOI18N
        }

        if (state == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();

            if (!fc.accept(file)) {
                final ExtensionAwareFileFilter ff = (ExtensionAwareFileFilter)fc.getFileFilter();
                file = new File(file.getAbsolutePath() + "." + ff.getExtension());
            }

            return file;
        } else {
            return null;
        }
    }
}
