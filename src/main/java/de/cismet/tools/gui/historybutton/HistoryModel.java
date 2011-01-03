/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.historybutton;

import java.util.Vector;

/**
 * The interface of the HistoryModel for the JHistoryButton. See a short <a
 * href="http://flexo.cismet.de/gadgets/JHistory/">description and demo</a> on the website.
 *
 * <p>License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a><br>
 * <img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img
 * src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif"></p>
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public interface HistoryModel {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  max  DOCUMENT ME!
     */
    void setMaximumPossibilities(int max);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isBackPossible();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    boolean isForwardPossible();
    /**
     * DOCUMENT ME!
     *
     * @param   external  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object back(boolean external);
    /**
     * DOCUMENT ME!
     *
     * @param   external  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object forward(boolean external);
    /**
     * DOCUMENT ME!
     *
     * @param  hml  DOCUMENT ME!
     */
    void addHistoryModelListener(HistoryModelListener hml);
    /**
     * DOCUMENT ME!
     *
     * @param  hml  DOCUMENT ME!
     */
    void removeHistoryModelListener(HistoryModelListener hml);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Vector getBackPossibilities();
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Vector getForwardPossibilities();
    /**
     * DOCUMENT ME!
     *
     * @param  o  DOCUMENT ME!
     */
    void addToHistory(Object o);
    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    Object getCurrentElement();
}
