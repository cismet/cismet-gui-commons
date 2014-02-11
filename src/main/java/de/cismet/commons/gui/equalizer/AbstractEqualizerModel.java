/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This abstract <code>EqualizerModel</code> implementation takes care of the listener mechanism. The implementation is
 * thread-safe and obeys the rules specified by the <code>EqualizerModel</code> interface.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public abstract class AbstractEqualizerModel implements EqualizerModel {

    //~ Instance fields --------------------------------------------------------

    private final Set<EqualizerModelListener> listeners;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new AbstractEqualizerModel object.
     */
    public AbstractEqualizerModel() {
        this.listeners = new HashSet<EqualizerModelListener>(0, 1);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Informs all registered listeners that a new <code>EqualizerModelEvent</code> happened.
     *
     * @param   eme  the <code>EqualizerModelEvent</code>
     *
     * @throws  IllegalArgumentException  if the <code>EqualizerModelEvent</code> is <code>null</code>
     */
    public void fireEqualizerModelEvent(final EqualizerModelEvent eme) {
        if (eme == null) {
            throw new IllegalArgumentException("event object must not be null"); // NOI18N
        }

        final Iterator<EqualizerModelListener> it;

        synchronized (listeners) {
            it = new HashSet<EqualizerModelListener>(listeners).iterator();
        }

        while (it.hasNext()) {
            it.next().equalizerChanged(eme);
        }
    }

    @Override
    public void addEqualizerModelListener(final EqualizerModelListener eml) {
        if (eml != null) {
            synchronized (listeners) {
                listeners.add(eml);
            }
        }
    }

    @Override
    public void removeEqualizerModelListener(final EqualizerModelListener eml) {
        if (eml != null) {
            synchronized (listeners) {
                listeners.remove(eml);
            }
        }
    }
}
