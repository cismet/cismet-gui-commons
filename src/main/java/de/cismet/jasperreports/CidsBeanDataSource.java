/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 jweintraut
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cismet.jasperreports;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRewindableDataSource;

import java.util.Collection;

import de.cismet.cids.dynamics.CidsBean;

/**
 * DOCUMENT ME!
 *
 * @author   jweintraut
 * @version  $Revision$, $Date$
 */
public class CidsBeanDataSource implements JRRewindableDataSource {

    //~ Instance fields --------------------------------------------------------

    private CidsBean[] cidsBeans;
    private int index = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CidsBeanDataSource object.
     *
     * @param  cidsBeans  DOCUMENT ME!
     */
    public CidsBeanDataSource(final CidsBean[] cidsBeans) {
        this.cidsBeans = cidsBeans;
    }

    /**
     * Creates a new CidsBeanDataSource object.
     *
     * @param  cidsBeans  DOCUMENT ME!
     */
    public CidsBeanDataSource(final Collection<CidsBean> cidsBeans) {
        this.cidsBeans = cidsBeans.toArray(new CidsBean[0]);
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void moveFirst() throws JRException {
        index = -1;
    }

    @Override
    public boolean next() throws JRException {
        index++;

        if (cidsBeans != null) {
            return (index < cidsBeans.length);
        } else {
            return false;
        }
    }

    @Override
    public Object getFieldValue(final JRField jrf) throws JRException {
        Object result = null;

        String name = null;
        try {
            name = jrf.getName();
        } catch (Throwable t) {
            result = "Getting this field's name caused an error.";
        }

        try {
            result = cidsBeans[index].getProperty(name);
        } catch (Throwable t) {
            if ((name == null) || (name.trim().length() == 0)) {
                result = "This field has no name.";
            } else {
                result = "An error occurred while getting this field's (" + name + ") value.";
            }
        }

        return result;
    }
}
