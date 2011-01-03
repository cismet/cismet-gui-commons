/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.historybutton;

/**
 * The interface of the HistoryModelListener for the JHistoryButton. See a short <a
 * href="http://flexo.cismet.de/gadgets/JHistory/">description and demo</a> on the website.
 *
 * <p>License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a><br>
 * <img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img
 * src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif"></p>
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public interface HistoryModelListener {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     */
    void backStatusChanged();
    /**
     * DOCUMENT ME!
     */
    void forwardStatusChanged();
    /**
     * DOCUMENT ME!
     */
    void historyChanged();
    /**
     * DOCUMENT ME!
     */
    void historyActionPerformed();
}
