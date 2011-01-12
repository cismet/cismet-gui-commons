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
 *
 * @author dmeiers
 */
public class SlideableSubTree extends JTree {

    SlideableSubTree(TreeNode node) {
        super(node);
    }

    public void print(TreeNode node, String indent) {
        System.out.println(indent + node.toString());
        indent+="\t";
        for (int i = 0; i < node.getChildCount(); i++) {
            if (node.getChildCount() > 0) {
                final TreeNode child = node.getChildAt(i);
                if (child.isLeaf()) {
                    System.out.println(indent + child.toString());
                } else {
                    indent += "\t";
                    this.print(child, indent);
                }
            }
        }
    }

    /*
     * subtree zu knoten herausfinden, neuen path erstellen,
     * mehtode weiterleiten ! Achtung Methode protected!!
     */
    @Override
    public void setExpandedState(TreePath path, boolean state) {
        super.setExpandedState(path, state);
    }
    /*
     * subtree ausfindig machen, mehtode weiterleiten, Paths in Enuemration
     *  anpassen
     * Problem Protected!!
     */

    @Override
    public boolean removeDescendantSelectedPaths(TreePath path, boolean includePath) {
        return super.removeDescendantSelectedPaths(path, includePath);


    }

    @Override
    public void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {
        super.removeDescendantToggledPaths(toRemove);


    }

    @Override
    public Enumeration<TreePath> getDescendantToggledPaths(TreePath parent) {
        return super.getDescendantToggledPaths(parent);

    }

    @Override
    public void clearToggledPaths() {
        super.clearToggledPaths();
    }



}
