/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.jdesktop.swingx.JXTable;

import java.awt.BorderLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten.hell@cismet.de x
 * @version  $Revision$, $Date$
 */
public class TheProperties {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        showPropertyFrame();
    }

    /**
     * DOCUMENT ME!
     */
    public static void showPropertyFrame() {
        final String SPLIT_STRING = "<=>"; // NOI18N

        final List defaults = getUIDefaults(SPLIT_STRING);
        final Object[][] data = splitUIDefaults(defaults, SPLIT_STRING);
        showUIDefaultsGUI(data);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   split  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static List getUIDefaults(final String split) {
        final List list = new ArrayList();
        final UIDefaults uid = UIManager.getDefaults();
        final Enumeration e = uid.keys();
        while (e.hasMoreElements()) {
            final Object key = e.nextElement();
            final Object value = uid.get(key);
            list.add(key + split + value);
        }
        Collections.sort(list);
        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   list   DOCUMENT ME!
     * @param   split  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static Object[][] splitUIDefaults(final List list, final String split) {
        final Object[][] data = new Object[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            final Object[] row = new Object[2];
            final String unsplittedRow = (String)list.get(i);
            final String[] values = unsplittedRow.split(split);
            row[0] = values[0];
            row[1] = values[1];
            data[i] = row;
        }
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  modelData  DOCUMENT ME!
     */
    public static void showUIDefaultsGUI(final Object[][] modelData) {
        final Object[] colNames = new Object[2];
        colNames[0] = "Key";   // NOI18N
        colNames[1] = "Value"; // NOI18N

        final DefaultTableModel model = new DefaultTableModel(modelData, colNames) {

                @Override
                public boolean isCellEditable(final int row, final int column) {
                    return false;
                }
            };

        final JXTable table = new JXTable(model);
        // JTable table = new JTable(model);
        final JFrame frame = new JFrame("UIDefaults"); // NOI18N
        frame.getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);
        frame.setSize(1050, 950);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
