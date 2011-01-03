/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.treetable;
/*
 * TreeTableModel.java
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

import javax.swing.tree.TreeModel;

/**
 * TreeTableModel is the model used by a JTreeTable. It extends TreeModel to add methods for getting inforamtion about
 * the set of columns each node in the TreeTableModel may have. Each column, like a column in a TableModel, has a name
 * and a type associated with it. Each node in the TreeTableModel can return a value for each of the columns and set
 * that value if isCellEditable() returns true.
 *
 * @author   Philip Milne
 * @author   Scott Violet
 * @version  $Revision$, $Date$
 */
public interface TreeTableModel extends TreeModel {

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns the number ofs availible column.
     *
     * @return  DOCUMENT ME!
     */
    int getColumnCount();

    /**
     * Returns the name for column number <code>column</code>.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    String getColumnName(int column);

    /**
     * Returns the type for column number <code>column</code>.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Class getColumnClass(int column);

    /**
     * Returns the value to be displayed for node <code>node</code>, at column number <code>column</code>.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getValueAt(Object node, int column);

    /**
     * Indicates whether the the value for node <code>node</code>, at column number <code>column</code> is editable.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isCellEditable(Object node, int column);

    /**
     * Sets the value for node <code>node</code>, at column number <code>column</code>.
     *
     * @param  aValue  DOCUMENT ME!
     * @param  node    DOCUMENT ME!
     * @param  column  DOCUMENT ME!
     */
    void setValueAt(Object aValue, Object node, int column);
}
