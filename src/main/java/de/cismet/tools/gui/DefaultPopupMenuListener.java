/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @author   therter
 * @version  $Revision$, $Date$
 */
public class DefaultPopupMenuListener extends MouseAdapter {

    //~ Instance fields --------------------------------------------------------

    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private final JPopupMenu popupMenu;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of CataloguePopupMenuListener.
     *
     * @param  popupMenu  DOCUMENT ME!
     */
    public DefaultPopupMenuListener(final JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component.
     *
     * @param  e  DOCUMENT ME!
     */
    /* public void mouseClicked(MouseEvent e)
     * { if(e.isPopupTrigger()) { popupMenu.show((Component)e.getSource(), e.getX(), e.getY()); }}*/
    @Override
    public void mousePressed(final MouseEvent e) {
        processPopupTrigger(e);
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        processPopupTrigger(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  e  DOCUMENT ME!
     */
    private void processPopupTrigger(final MouseEvent e) {
        // windows uses the mouseReleased event for popup menus and Linux uses the mousePressed event.
        if (e.isPopupTrigger()) {
            try {
                if (e.getSource() instanceof JTree) {
                    final JTree currentTree = (JTree)e.getSource();
                    final TreePath[] paths = currentTree.getSelectionPaths();
                    final List<TreePath> pathList = new ArrayList<TreePath>();

                    if ((paths != null) && (paths.length > 0)) {
                        pathList.addAll(Arrays.asList(paths));
                    }

                    final TreePath selPath = currentTree.getPathForLocation(e.getX(), e.getY());
                    if ((selPath != null) && !pathList.contains(selPath)) {
                        if (e.isControlDown()) {
                            pathList.add(selPath);
                            currentTree.setSelectionPaths(pathList.toArray(new TreePath[pathList.size()]));
                        } else if (e.isShiftDown() && (pathList.size() > 0)) {
                            final TreePath first = pathList.get(0);
                            final int firstIndex = currentTree.getRowForPath(first);
                            final int selectedIndex = currentTree.getRowForPath(selPath);
                            currentTree.setSelectionInterval(firstIndex, selectedIndex);
                        } else {
                            currentTree.setSelectionPath(selPath);
                        }
                    }
                }
            } catch (Exception ex) {
                log.error("Error during on-the-fly-selection", ex);
            }

            popupMenu.show((Component)e.getSource(), e.getX(), e.getY());
        }
    }
}
