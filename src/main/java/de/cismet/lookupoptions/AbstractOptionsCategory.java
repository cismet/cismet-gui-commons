/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.lookupoptions;

import javax.swing.Icon;

/**
 * This class provides a skeletal implementation of the OptionsCategory interface to minimize the effort required to
 * implement this interface.
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public abstract class AbstractOptionsCategory implements OptionsCategory {

    //~ Methods ----------------------------------------------------------------

    @Override
    public Icon getIcon() {
        return null;
    }

    @Override
    public abstract String getName();

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   o  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int compareTo(final OptionsCategory o) {
        final int orderCompare = new Integer(getOrder()).compareTo(o.getOrder());
        if (orderCompare == 0) {
            return getName().compareTo(o.getName());
        } else {
            return orderCompare;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTooltip() {
        return null;
    }
}
