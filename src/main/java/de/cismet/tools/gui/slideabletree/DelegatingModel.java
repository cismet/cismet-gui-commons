/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
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
 * DOCUMENT ME!
 *
 * @author   dmeiers
 *
 *           <p>Diese Klasse wird als Model fuer die einzelenen SlideableSubTrees des SlideableTrees benutzt. Dabei
 *           werden die anfragen lediglich an das Originalmodel des SlideableTree weitergeleitet.</p>
 * @version  $Revision$, $Date$
 */
public class DelegatingModel implements TreeModel {

    //~ Instance fields --------------------------------------------------------

    private Object root;
    private TreeModel model;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DelegatingModel object.
     *
     * @param  newRoot        DOCUMENT ME!
     * @param  originalModel  DOCUMENT ME!
     */
    public DelegatingModel(final Object newRoot, final TreeModel originalModel) {
        root = newRoot;
        model = originalModel;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addTreeModelListener(final TreeModelListener l) {
        model.addTreeModelListener(l);
    }

    @Override
    public Object getChild(final Object parent, final int index) {
        return model.getChild(parent, index);
    }

    @Override
    public int getChildCount(final Object parent) {
        return model.getChildCount(parent);
    }

    @Override
    public int getIndexOfChild(final Object parent, final Object child) {
        return model.getIndexOfChild(parent, child);
    }

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public boolean isLeaf(final Object node) {
        return model.isLeaf(node);
    }

    @Override
    public void removeTreeModelListener(final TreeModelListener l) {
        model.removeTreeModelListener(l);
    }

    @Override
    public void valueForPathChanged(final TreePath path, final Object newValue) {
        model.valueForPathChanged(path, newValue);
    }
}
