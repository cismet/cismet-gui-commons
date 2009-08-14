/*
 * StaticSwingTools.java
 * Copyright (C) 2005 by:
 *
 *----------------------------
 * cismet GmbH
 * Goebenstrasse 40
 * 66117 Saarbruecken
 * http://www.cismet.de
 *----------------------------
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *----------------------------
 * Author:
 * thorsten.hell@cismet.de
 *----------------------------
 *
 * Created on 2. Januar 2006, 12:59
 *
 */
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
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

/**
 *
 * @author thorsten.hell@cismet.de
 */
public class StaticSwingTools {

    private final static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(StaticSwingTools.class);

    public static void jTreeExpandAllNodes(JTree tree) {
        int row = 0;
        while (row < tree.getRowCount()) {
            tree.expandRow(row);
            row++;
        }
    }
    
    public static void jTreeExpandAllNodesAndScroll2Last(JTree tree) {
        // expand to the last leaf from the root
        DefaultMutableTreeNode root;
        root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        tree.scrollPathToVisible(new TreePath(root.getLastLeaf().getPath()));
    }

    public static void jTreeCollapseAllNodes(JTree tree) {
        TreePath p = tree.getSelectionPath();
        
        int row = tree.getRowCount() - 1;
        tree.setSelectionPath(p);
        while (row >= 0) {
            tree.collapseRow(row);
            row--;
        }
        tree.setSelectionPath(p);
    }
    
    @SuppressWarnings("unchecked")
    public static <T extends Component> T findSpecificParentComponent(Container c, Class<T> clazz) {
//        while (c != null && !(c.getClass().equals(clazz))) {
        while (c != null && !(clazz.isAssignableFrom(c.getClass()))) {
            c = c.getParent();
        }
        return (T) c;
    }
    //From The Java Developers Almanac
    public static void jTableScrollToVisible(JTable table, int rowIndex, int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        JViewport viewport = (JViewport) table.getParent();

        // This rectangle is relative to the table where the
        // northwest corner of cell (0,0) is always (0,0).
        Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);

        // The location of the viewport relative to the table
        Point pt = viewport.getViewPosition();

        // Translate the cell location so that it is relative
        // to the view, assuming the northwest corner of the
        // view is (0,0)
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);

        // Scroll the area into view
        viewport.scrollRectToVisible(rect);
    }

    public static String getLinkFromDropEvent(DropTargetDropEvent dtde) {
        //System.out.println("getLinkFromDropEvent");
        String link = null;
        Transferable t = dtde.getTransferable();
        DataFlavor[] flavors = t.getTransferDataFlavors();
        for (int i = 0; i < flavors.length; i++) {
            DataFlavor flavor = flavors[i];
            try {
                if (flavor.equals(DataFlavor.javaFileListFlavor)) {
                    //System.out.println("importData: FileListFlavor");
                    List l = (List) t.getTransferData(DataFlavor.javaFileListFlavor);
                    Iterator iter = l.iterator();
                    while (iter.hasNext()) {
                        File file = (File) iter.next();
                        System.out.println(file);
                        try {
                            String can = file.getCanonicalPath();
                            return can;
                        } catch (IOException ex) {
                            log.warn("Fehler bei file.getCanonicalPath()", ex);
                        }
                    }
                } else if (flavor.equals(DataFlavor.stringFlavor)) {
                    String fileOrURL = (String) t.getTransferData(flavor);
                    System.out.println("GOT STRING: " + fileOrURL);
                    try {
                        URL url = new URL(fileOrURL);
                        return url.toString();
                    } catch (MalformedURLException ex) {
                        log.warn("Keine g\u00FCltige URL", ex);
                        return null;
                    }
                    
                } else {
                    //log.warn("importData rejected: "+ flavor);
                    //mach nix und probiers weiter
                }
            } catch (IOException ex) {
                log.warn("IOError getting data: " + ex);
            } catch (UnsupportedFlavorException e) {
                log.warn("Unsupported Flavor: ", e);
            }
        }
        return null;
    }
    
    public static void jTabbedPaneWithVerticalTextAddTab(JTabbedPane tabPane, String text, JComponent comp) {
        jTabbedPaneWithVerticalTextAddTab(tabPane, text, null, comp);
    }

    public static void jTabbedPaneWithVerticalTextAddTab(JTabbedPane tabPane, String text, Icon icon, JComponent comp) {
        int tabPlacement = tabPane.getTabPlacement();
        Object textIconGap = UIManager.get("TabbedPane.textIconGap");
        Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
        tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
        UIManager.put("TabbedPane.textIconGap", new Integer(1));
        // UIManager.put("TabbedPane.tabInsets", new Insets(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom));
        UIManager.put("TabbedPane.tabInsets", tabInsets);
        SwingUtilities.updateComponentTreeUI(tabPane);
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT:
                if (icon == null) {
                    tabPane.addTab(null, new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT), comp);
                    
                } else {
                    Icon newIcon;
                    if (tabPlacement == JTabbedPane.RIGHT) {
                        Icon[] icons = new Icon[2];
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT);
                        icons[1] = icon;
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    } else {
                        Icon[] icons = new Icon[2];
                        icons[1] = icon;
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT);
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    }
                    log.debug("newIconHeight" + newIcon.getIconHeight());
                    tabPane.addTab(null, newIcon, comp);
                }
                break;
            default:
                tabPane.addTab(text, null, comp);
        }
        tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
        UIManager.put("TabbedPane.tabInsets", tabInsets);
        
    }
    
    public static void jTabbedPaneWithVerticalTextSetNewText(JTabbedPane tabPane, String text, JComponent comp) {
        jTabbedPaneWithVerticalTextSetNewText(tabPane, text, null, comp);
    }

    public static void jTabbedPaneWithVerticalTextSetNewText(JTabbedPane tabPane, String text, Icon icon, JComponent comp) {
        jTabbedPaneWithVerticalTextSetNewText(tabPane, text, icon, Color.black, comp);
    }

    public static void jTabbedPaneWithVerticalTextSetNewText(JTabbedPane tabPane, String text, Icon icon, Color textColor, JComponent comp) {
        int tabPlacement = tabPane.getTabPlacement();
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT:
                if (icon == null) {
                    tabPane.setIconAt(tabPane.indexOfComponent(comp), new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor));
                } else {
                    Icon newIcon;
                    if (tabPlacement == JTabbedPane.RIGHT) {
                        Icon[] icons = new Icon[2];
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor);
                        icons[1] = icon;
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    } else {
                        Icon[] icons = new Icon[2];
                        icons[1] = icon;
                        icons[0] = new VerticalTextIcon(text, tabPlacement == JTabbedPane.RIGHT, textColor);
                        newIcon = Static2DTools.joinIcons(icons, 6, Static2DTools.VERTICAL, Static2DTools.CENTER);
                    }
                    log.debug("newIconHeight" + newIcon.getIconHeight());
                    tabPane.setIconAt(tabPane.indexOfComponent(comp), newIcon);
                }
                return;
            default:
                tabPane.setTitleAt(tabPane.indexOfComponent(comp), text);
        }
    }
    
    public static JTabbedPane jTabbedPaneWithVerticalTextCreator(int tabPlacement) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (key.toString().indexOf("abbed") != -1) {
                log.debug(key + "," + value);
            }
        }
        switch (tabPlacement) {
            case JTabbedPane.LEFT:
            case JTabbedPane.RIGHT:
//                Object textIconGap = UIManager.get("TabbedPane.textIconGap");
//                Insets tabInsets = UIManager.getInsets("TabbedPane.tabInsets");
//                tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
//                UIManager.put("TabbedPane.textIconGap", new Integer(1));
//                // UIManager.put("TabbedPane.tabInsets", new Insets(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom));
//                UIManager.put("TabbedPane.tabInsets", tabInsets);
                JTabbedPane tabPane = new JTabbedPane(tabPlacement);
//
//                UIManager.put("TabbedPane.textIconGap", textIconGap);
//                UIManager.put("TabbedPane.tabInsets", tabInsets);
//                //SwingUtilities.updateComponentTreeUI(tabPane);
                // tabInsets.set(tabInsets.left, tabInsets.top, tabInsets.right, tabInsets.bottom);
                //UIManager.put("TabbedPane.tabInsets", tabInsets);
                return tabPane;
            default:
                return new JTabbedPane(tabPlacement);
        }
    }
    
    public static JTabbedPane jTabbedPaneWithVerticalTextCreator(int tabPlacement, int tabLayoutPolicy) {
        JTabbedPane jtp = jTabbedPaneWithVerticalTextCreator(tabPlacement);
        return jtp;
    }
    
    public static void setNiftyScrollBars(JScrollPane sPane) {
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
    
    public static Frame getFirstParentFrame(Component c) {
        return getParentFrame(c, true);
    }
    
    public static Frame getParentFrame(Component c) {
        return getParentFrame(c, false);
    }
    
    public static Frame getParentFrame(Component c, boolean first) {
        try {
            Object o = c;
            do {
                o = ((Component) o).getParent();
                log.debug("getParent:" + o);
            } while (!(o instanceof Frame && ((Component) o).getParent() == null || first));
            log.debug("getParentFrame returns " + o);
            return (Frame) o;
        } catch (Exception e) {
            log.warn("getParentFrame returns null", e);
            return null;
            
        }
    }
    
    public static Rectangle getComponentsExtent(Component... components) {
        Rectangle r = new Rectangle();
        Integer maxX = null;
        Integer minX = null;
        Integer maxY = null;
        Integer minY = null;
        
        
        for (Component comp : components) {
            //firsttimecheck
            if (maxX == null) {
                minX = comp.getX();
                minY = comp.getY();
                maxX = comp.getWidth() + minX;
                maxY = comp.getHeight() + minY;
                
            } else {
                //min
                minX = Math.min(minX, comp.getX());
                minY = Math.min(minY, comp.getY());

                //max
                maxX = Math.max(maxX, comp.getX() + comp.getWidth());
                maxY = Math.max(maxY, comp.getY() + comp.getHeight());
            }
        }
        
        r.setRect(minX, minY, maxX - minX, maxY - minY);
        return r;
    }
}
