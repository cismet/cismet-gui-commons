/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.historybutton;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

/**
 * An implementation of the HistoryModel for the JHistoryButton. See a short <a
 * href="http://flexo.cismet.de/gadgets/JHistory/">description and demo</a> on the website.
 *
 * <p>License: <a href="http://www.gnu.org/copyleft/lesser.html#TOC1">GNU LESSER GENERAL PUBLIC LICENSE</a><br>
 * <img src="http://opensource.org/trademarks/osi-certified/web/osi-certified-60x50.gif"> <img
 * src="http://opensource.org/trademarks/opensource/web/opensource-55x48.gif"></p>
 *
 * @author   thorsten.hell@cismet.de
 * @version  $Revision$, $Date$
 */
public class DefaultHistoryModel implements HistoryModel {

    //~ Instance fields --------------------------------------------------------

    Vector modelListeners = new Vector();

    Stack backHistory = new Stack();
    Stack forwardHistory = new Stack();
    Object currentElement = null;
    int maximumPossibilities = -1;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of DefaultHistoryModel.
     */
    public DefaultHistoryModel() {
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void addToHistory(final Object o) {
        final boolean tmpFw = isForwardPossible();
        if (o != null) {
            if (currentElement != null) {
                if (currentElement.equals(o)) {
                    return;
                }
                backHistory.push(currentElement);
            }
            currentElement = o;
            if (tmpFw != isForwardPossible()) {
                fireForwardStatusChanged();
            }
            forwardHistory.removeAllElements();
            fireBackStatusChanged();
            fireHistoryChanged();
            fireForwardStatusChanged();
        }
    }

    @Override
    public void setMaximumPossibilities(final int max) {
        maximumPossibilities = max;
    }

    @Override
    public boolean isForwardPossible() {
        return !(forwardHistory.empty());
    }

    @Override
    public boolean isBackPossible() {
        return !(backHistory.empty());
    }

    @Override
    public java.util.Vector getForwardPossibilities() {
        return forwardHistory;
    }

    @Override
    public java.util.Vector getBackPossibilities() {
        return backHistory;
    }

    @Override
    public Object forward(final boolean external) {
        if (isForwardPossible()) {
            backHistory.push(currentElement);
            currentElement = forwardHistory.pop();
            fireForwardStatusChanged();
            fireBackStatusChanged();
            if (external) {
                fireHistoryChanged();
            }
            return currentElement;
        }
        return null;
    }

    @Override
    public Object back(final boolean external) {
        if (isBackPossible()) {
            forwardHistory.push(currentElement);
            currentElement = backHistory.pop();
            fireBackStatusChanged();
            fireForwardStatusChanged();
            if (external) {
                fireHistoryChanged();
            }
            return currentElement;
        }
        return null;
    }

    @Override
    public Object getCurrentElement() {
        return currentElement;
    }

    @Override
    public void addHistoryModelListener(final HistoryModelListener hml) {
        modelListeners.add(hml);
        hml.historyChanged();
    }

    @Override
    public void removeHistoryModelListener(final HistoryModelListener hml) {
        modelListeners.remove(hml);
    }

    /**
     * DOCUMENT ME!
     */
    private void fireForwardStatusChanged() {
        final Iterator it = modelListeners.iterator();
        while (it.hasNext()) {
            final Object o = it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).forwardStatusChanged();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void fireBackStatusChanged() {
        final Iterator it = modelListeners.iterator();
        while (it.hasNext()) {
            final Object o = it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).backStatusChanged();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void fireHistoryChanged() {
        final Iterator it = modelListeners.iterator();
        while (it.hasNext()) {
            final Object o = it.next();
            if (o instanceof HistoryModelListener) {
                ((HistoryModelListener)o).historyChanged();
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void fireHistoryActionPerformed() {
    }
}
