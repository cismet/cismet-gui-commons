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
package de.cismet.tools.gui.jtable.sorting;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * Decorates JTables for alphanumeric sorting support.
 *
 * <p>WARNING: DO NEVER EVER USE WITH JXTABLE!!! (JXTable implements another sorting concept that isn't compatible with
 * JTable's RowSorter!)</p>
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
public final class TableSortDecorator {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TableSortDecorator object.
     */
    private TableSortDecorator() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Makes the parameter table alphanumerically sortable.
     *
     * @param  tbl  DOCUMENT ME!
     */
    public static void decorate(final JTable tbl) {
        final TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(tbl.getModel());
        sorter.setSortsOnUpdates(true);
        for (int i = 0; i < tbl.getColumnCount(); ++i) {
            sorter.setComparator(i, AlphanumComparator.getInstance());
        }
        tbl.setRowSorter(sorter);
        tbl.getTableHeader().addMouseListener(new TableHeaderUnsortMouseAdapter(tbl));
    }
}

/**
 * MouseAdapter for remove sorting from the table when perfoming a right-clck on the header.
 *
 * @author   srichter
 * @version  $Revision$, $Date$
 */
final class TableHeaderUnsortMouseAdapter extends MouseAdapter {

    //~ Instance fields --------------------------------------------------------

    private JTable tbl;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new TableHeaderUnsortMouseAdapter object.
     *
     * @param  tbl  DOCUMENT ME!
     */
    public TableHeaderUnsortMouseAdapter(final JTable tbl) {
        this.tbl = tbl;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void mousePressed(final MouseEvent e) {
        if (e.isPopupTrigger()) {
            tbl.getRowSorter().setSortKeys(null);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.isPopupTrigger()) {
            tbl.getRowSorter().setSortKeys(null);
        }
    }
}
