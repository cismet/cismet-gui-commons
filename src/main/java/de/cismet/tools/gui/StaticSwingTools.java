/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTargetDropEvent;

import java.io.File;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

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
    public static void showDialog(final Component parent, final JDialog dialog, final boolean isRelativeToParentFrame) {
        if (dialog.isVisible()) {
            dialog.toFront();
        } else {
            if (isRelativeToParentFrame && (parent != null)) {
                final Frame parentFrame = getParentFrame(parent);
                if (parentFrame == null) {
                    dialog.setLocationRelativeTo(parent);
                } else {
                    dialog.setLocationRelativeTo(parentFrame);
                }
            } else {
                dialog.setLocationRelativeTo(parent);
            }

            dialog.setVisible(true);
        }
    }
}
