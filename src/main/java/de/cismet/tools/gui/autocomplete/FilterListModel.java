/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.AbstractListModel;

/**
 * Class to hold the remaining objects that still match the users input.
 *
 * @author   ncochran
 * @version  $Revision$, $Date$
 */
public class FilterListModel extends AbstractListModel {

    //~ Instance fields --------------------------------------------------------

    private Object[] fullList;
    private ArrayList<Object> filteredList;
    private String filter;
    private boolean caseSensitive = false;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new FilterListModel object.
     *
     * @param  unfilteredList  DOCUMENT ME!
     */
    public FilterListModel(final Object[] unfilteredList) {
        fullList = unfilteredList;
        filteredList = new ArrayList<Object>(Arrays.asList(unfilteredList));
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public int getSize() {
        return filteredList.size();
    }

    @Override
    public Object getElementAt(final int index) {
        return filteredList.get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getFilter() {
        return filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  filter  DOCUMENT ME!
     */
    public void setFilter(final String filter) {
        filteredList.clear();
        for (final Object obj : fullList) {
            if (obj.toString().length() < filter.length()) {
                continue;
            }

            if (this.caseSensitive) {
                if (obj.toString().startsWith(filter)) {
                    filteredList.add(obj);
                }
            } else {
                if (obj.toString().substring(0, filter.length()).compareToIgnoreCase(filter) == 0) {
                    filteredList.add(obj);
                }
            }
        }
        fireContentsChanged(this, 0, filteredList.size());
    }

    /**
     * DOCUMENT ME!
     */
    public void clearFilter() {
        filter = null;
        filteredList = new ArrayList<Object>(Arrays.asList(fullList));
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean getCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  caseSensitive  DOCUMENT ME!
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        clearFilter();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  objectsToMatch  DOCUMENT ME!
     */
    public void setCompleterMatches(final Object[] objectsToMatch) {
        fullList = objectsToMatch;
        clearFilter();
    }
}
