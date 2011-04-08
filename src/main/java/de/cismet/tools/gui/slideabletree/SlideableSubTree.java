/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.slideabletree;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class SlideableSubTree extends JTree {

    //~ Instance fields --------------------------------------------------------

    private boolean specialSelection = false;
    private boolean hasDragGestureRecognizer = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SlideableSubTree object.
     *
     * @param  node                 the root node of the tree
     * @param  useSpecialSelection  flag to use SpecialSelectionUI
     */
    SlideableSubTree(final TreeNode node, final boolean useSpecialSelection) {
        super(node);
        specialSelection = useSpecialSelection;
        if (specialSelection) {
            this.setUI(new SpecialSelectionUI(this));
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setExpandedState(final TreePath path, final boolean state) {
        super.setExpandedState(path, state);
    }

    @Override
    public boolean removeDescendantSelectedPaths(final TreePath path, final boolean includePath) {
        return super.removeDescendantSelectedPaths(path, includePath);
    }

    @Override
    public void removeDescendantToggledPaths(final Enumeration<TreePath> toRemove) {
        super.removeDescendantToggledPaths(toRemove);
    }

    @Override
    public Enumeration<TreePath> getDescendantToggledPaths(final TreePath parent) {
        return super.getDescendantToggledPaths(parent);
    }

    @Override
    public void clearToggledPaths() {
        super.clearToggledPaths();
    }

    @Override
    public void setUI(final TreeUI ui) {
        if (specialSelection) {
            super.setUI(new SpecialSelectionUI(this));
        } else {
            super.setUI(ui);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean hasDragGestureRecognizer() {
        return hasDragGestureRecognizer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  hasDragGestureRecognizer  DOCUMENT ME!
     */
    public void setHasDragGestureRecognizer(final boolean hasDragGestureRecognizer) {
        this.hasDragGestureRecognizer = hasDragGestureRecognizer;
    }

    @Override
    public void setEditable(final boolean flag) {
        super.setEditable(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   path  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isPathValid(final TreePath path) {
        if (path != null) {
            final TreeModel model = this.getModel();

            if (path.getPathCount() == 0) {
                // Pfad representiert Wurzel
                return model.getRoot().equals(path.getPathComponent(0));
            }

            for (int i = 1; i < path.getPathCount(); i++) {
                final int childIndex = model.getIndexOfChild(path.getPathComponent(i - 1),
                        path.getPathComponent(i - 1));
                if (childIndex == -1) {
                    // parent oder childknoten null oder nicht vorhanden...
                    return false;
                }
            }
        }
        return true;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * This class is used for an improved selection behavior on muliple selections To remove an node from an mulitple
     * selection, this class use the mouseRelease event instead the mousePressed event.
     *
     * @version  $Revision$, $Date$
     */
    protected class SpecialSelectionUI extends BasicTreeUI {

        //~ Instance fields ----------------------------------------------------

        protected JTree tree;
        private boolean releasedAction;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SpecialSelectionUI object.
         *
         * @param  t  a reference to a JTree
         */
        public SpecialSelectionUI(final JTree t) {
            tree = t;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        protected MouseListener createMouseListener() {
            return new MouseListener() {

                    @Override
                    public void mouseClicked(final MouseEvent e) {
                    }

                    @Override
                    public void mousePressed(final MouseEvent e) {
                        final int selCount = tree.getSelectionCount();
                        final TreePath clickPath = tree.getPathForLocation(e.getX(), e.getY());

                        if (selCount > 1) {
                            if (tree.getSelectionModel().isPathSelected(clickPath)) {
                                releasedAction = true;
                                return;
                            }
                        }
                        SpecialSelectionUI.super.createMouseListener().mousePressed(e);
                    }

                    @Override
                    public void mouseReleased(final MouseEvent e) {
                        final TreePath clickPath = tree.getPathForLocation(e.getX(), e.getY());
                        if (releasedAction && tree.getSelectionModel().isPathSelected(clickPath)) {
                            releasedAction = false;
                            SpecialSelectionUI.super.createMouseListener().mousePressed(e);
                        }
                    }

                    @Override
                    public void mouseEntered(final MouseEvent e) {
                        SpecialSelectionUI.super.createMouseListener().mouseEntered(e);
                    }

                    @Override
                    public void mouseExited(final MouseEvent e) {
                        SpecialSelectionUI.super.createMouseListener().mouseExited(e);
                    }
                };
        }
    }
}
