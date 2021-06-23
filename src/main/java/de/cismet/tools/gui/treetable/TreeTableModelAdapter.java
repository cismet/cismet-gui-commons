/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.treetable;
/*
 * @(#)TreeTableModelAdapter.java       1.2 98/10/27
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

/**
 * This is a wrapper class takes a TreeTableModel and implements the table model interface. The implementation is
 * trivial, with all of the event dispatching support provided by the superclass: the AbstractTableModel.
 *
 * @author   Philip Milne
 * @author   Scott Violet
 * @version  1.2 10/27/98
 */
public class TreeTableModelAdapter extends AbstractTableModel {

    //~ Instance fields --------------------------------------------------------

    JTree tree;
    TreeTableModel treeTableModel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TreeTableModelAdapter object.
     *
     * @param  treeTableModel  DOCUMENT ME!
     * @param  tree            DOCUMENT ME!
     */
    public TreeTableModelAdapter(final TreeTableModel treeTableModel, final JTree tree) {
        this.tree = tree;
        this.treeTableModel = treeTableModel;

        tree.addTreeExpansionListener(new TreeExpansionListener() {

                // Don't use fireTableRowsInserted() here; the selection model
                // would get updated twice.
                @Override
                public void treeExpanded(final TreeExpansionEvent event) {
                    fireTableDataChanged();
                }
                @Override
                public void treeCollapsed(final TreeExpansionEvent event) {
                    fireTableDataChanged();
                }
            });

        // Install a TreeModelListener that can update the table when
        // tree changes. We use delayedFireTableDataChanged as we can
        // not be guaranteed the tree will have finished processing
        // the event before us.
        treeTableModel.addTreeModelListener(new TreeModelListener() {

                @Override
                public void treeNodesChanged(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeNodesInserted(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeNodesRemoved(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }

                @Override
                public void treeStructureChanged(final TreeModelEvent e) {
                    delayedFireTableDataChanged();
                }
            });

//        final JTree tempTree = tree;
//        treeTableModel.addTreeModelListener(new TreeModelListener()
//        {
//            //Diese \u00C4nderung wurde vorgenommen weil die Selection in der JTZreeTable manuell nicht zu setzen war
//
//            public void treeNodesChanged(TreeModelEvent e)
//            {
//                final int[] childIndices = e.getChildIndices();
//
//                if (childIndices!=null) {
//                    final int[] rows = new int[childIndices.length];
//                    TreePath parentPath = new TreePath(e.getPath());
//                    for (int i=0; i<childIndices.length; i++)
//                        rows[i] = tempTree.getRowForPath(parentPath.pathByAddingChild(e.getChildren()));
//
//                    SwingUtilities.invokeLater(new Runnable()
//                    {
//                        public void run()
//                        {
//                            for (int i=0; i<rows.length; i++)
//                            {
//                                if (rows[i] != -1)
//                                {
//                                    for (int col=0; col<getColumnCount(); col++)
//                                        fireTableCellUpdated(rows[i], col);
//                                }
//                            }
//                        }
//                    });
//                }
//
//            }
//          public void treeNodesInserted(TreeModelEvent e) {
//              delayedFireTableDataChanged();
//          }
//
//          public void treeNodesRemoved(TreeModelEvent e) {
//              delayedFireTableDataChanged();
//          }
//
//          public void treeStructureChanged(TreeModelEvent e) {
//              delayedFireTableDataChanged();
//          }
//
//        });
    }

    //~ Methods ----------------------------------------------------------------

// Wrappers, implementing TableModel interface.

    @Override
    public int getColumnCount() {
        return treeTableModel.getColumnCount();
    }

    @Override
    public String getColumnName(final int column) {
        return treeTableModel.getColumnName(column);
    }

    @Override
    public Class getColumnClass(final int column) {
        return treeTableModel.getColumnClass(column);
    }

    @Override
    public int getRowCount() {
        return tree.getRowCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   row  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Object nodeForRow(final int row) {
        final TreePath treePath = tree.getPathForRow(row);
        return (treePath != null) ? treePath.getLastPathComponent() : null;
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        return treeTableModel.getValueAt(nodeForRow(row), column);
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return treeTableModel.isCellEditable(nodeForRow(row), column);
    }

    @Override
    public void setValueAt(final Object value, final int row, final int column) {
        treeTableModel.setValueAt(value, nodeForRow(row), column);
    }

    /**
     * Invokes fireTableDataChanged after all the pending events have been processed. SwingUtilities.invokeLater is used
     * to handle this.
     */
    protected void delayedFireTableDataChanged() {
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    fireTableDataChanged();
                }
            });
    }
}
