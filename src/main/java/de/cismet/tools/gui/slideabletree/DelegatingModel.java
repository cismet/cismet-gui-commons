/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.cismet.tools.gui.slideabletree;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 *
 * @author dmeiers
 *
 * Diese Klasse wird als Model für die einzelenen SlideableSubTrees des SlideableTrees
 * benutzt. Dabei werden die anfragen lediglich an das Originalmodel des
 * SlideableTree weitergeleitet.
 */
public class DelegatingModel implements TreeModel{
    private Object root;
    private TreeModel model;

    public DelegatingModel (Object newRoot, TreeModel originalModel){
        root = newRoot;
        model = originalModel;
    }
    public void addTreeModelListener(TreeModelListener l) {
        model.addTreeModelListener(l);
    }

    public Object getChild(Object parent, int index) {
       return  model.getChild(parent, index);
    }

    public int getChildCount(Object parent) {
        return model.getChildCount(parent);
    }

    public int getIndexOfChild(Object parent, Object child) {
        return model.getIndexOfChild(parent, child);
    }

    public Object getRoot() {
        return root;
    }

    public boolean isLeaf(Object node) {
        return model.isLeaf(node);
    }

    public void removeTreeModelListener(TreeModelListener l) {
        model.removeTreeModelListener(l);
    }

    public void valueForPathChanged(TreePath path, Object newValue) {
        model.valueForPathChanged(path, newValue);
    }
}
