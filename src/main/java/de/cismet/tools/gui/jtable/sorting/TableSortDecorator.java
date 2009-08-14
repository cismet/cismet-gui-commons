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
 * WARNING: DO NEVER EVER USE WITH JXTABLE!!! 
 * (JXTable implements another sorting concept that isn't compatible with 
 *  JTable's RowSorter!)
 * 
 * @author srichter
 */
public final class TableSortDecorator {

    private TableSortDecorator() {
    }

    /**
     * Makes the parameter table alphanumerically sortable.
     * 
     * @param tbl
     */
    public static final void decorate(JTable tbl) {

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
 * MouseAdapter for remove sorting from the table when perfoming a right-clck 
 * on the header
 * 
 * @author srichter
 */
final class TableHeaderUnsortMouseAdapter extends MouseAdapter {

    public TableHeaderUnsortMouseAdapter(JTable tbl) {
        this.tbl = tbl;
    }
    private JTable tbl;

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            tbl.getRowSorter().setSortKeys(null);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            tbl.getRowSorter().setSortKeys(null);
        }
    }
}
