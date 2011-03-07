/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.slideabletree;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.plaf.TreeUI;
import javax.swing.plaf.basic.BasicTreeUI;
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

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SlideableSubTree object.
     *
     * @param  node                 DOCUMENT ME!
     * @param  useSpecialSelection  DOCUMENT ME!
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

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
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
         * @param  t  DOCUMENT ME!
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
                        // SpecialTreeSelectionUI.super.createMouseListener().mousePressed(e);
                        // System.out.println("mouseClicked");
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

                        // System.out.println("mousePressed");
                    }

                    @Override
                    public void mouseReleased(final MouseEvent e) {
                        if (releasedAction) {
                            releasedAction = false;
                            SpecialSelectionUI.super.createMouseListener().mousePressed(e);
                        }

                        // System.out.println("mouseReleased");
                    }

                    @Override
                    public void mouseEntered(final MouseEvent e) {
                        SpecialSelectionUI.super.createMouseListener().mouseEntered(e);
                        // System.out.println("mouseEntered");
                    }

                    @Override
                    public void mouseExited(final MouseEvent e) {
                        SpecialSelectionUI.super.createMouseListener().mouseExited(e);
                        // System.out.println("MouseExited");
                    }
                };
        }
    }
}
