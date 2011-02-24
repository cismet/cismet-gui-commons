/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.slideabletree;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class SlideableSubTree extends JTree {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SlideableSubTree object.
     *
     * @param  node  DOCUMENT ME!
     */
    SlideableSubTree(final TreeNode node) {
        super(node);
    }

    //~ Methods ----------------------------------------------------------------

    /*
     * subtree zu knoten herausfinden, neuen path erstellen, mehtode weiterleiten ! Achtung Methode protected!!
     */
    @Override
    public void setExpandedState(final TreePath path, final boolean state) {
        super.setExpandedState(path, state);
    }
    /*
     * subtree ausfindig machen, mehtode weiterleiten, Paths in Enuemration anpassen Problem Protected!!
     */

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
}
