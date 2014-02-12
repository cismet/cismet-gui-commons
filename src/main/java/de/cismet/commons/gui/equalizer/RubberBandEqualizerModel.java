/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.equalizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Special {@link EqualizerModel} that guarantees that the sum of all categories is <code>100</code>. So if a new value
 * is set all the other categories' values are adapted so that the sum is <code>100</code> again. Thus this model does
 * not inform listeners about a specific change at a specific index but only fires a general change event.<br/>
 * <br/>
 * The adaption of other values follows these rules:
 *
 * <ul>
 *   <li>the delta of the changed value is distributed equally over the other categories</li>
 *   <li>if a specific category value is zero it will not be considered for distribution</li>
 *   <li>if a specific category value reaches zero during delta distribution it will also not be considered for
 *     distribution anymore</li>
 *   <li>if the (remaining) delta is smaller than the number of remaining categories a change of <code>1</code> is
 *     applied to the remaining categories, ascending by index</li>
 * </ul>
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
// TODO: allow custom ranges
public final class RubberBandEqualizerModel extends DefaultEqualizerModel {

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RubberBandEqualizerModel object using the given list of <code>EqualizerCategory</code>s. The
     * {@link Range} of this model is <code>[0, 100]</code>. In addition to the policies of <code>
     * DefaultEqualizerModel</code> this model implementation ensures that the sum of all values of the categories is
     * always <code>100</code>. For convenience this constructor permits a category collection that contains categories
     * with <code>0</code> values only. In this case the constructor distributes values among the categories equally so
     * that a sum of <code>100</code> is guaranteed.
     *
     * @param   equalizerCategories  the <code>EqualizerCategory</code>s of this model
     *
     * @throws  IllegalArgumentException  if any of the policies of <code>DefaultEqualizerModel</code> are not met or if
     *                                    the sum of the categories' values is not <code>0</code> or <code>100</code>
     *
     * @see     DefaultEqualizerModel
     */
    public RubberBandEqualizerModel(final Collection<EqualizerCategory> equalizerCategories) {
        super(equalizerCategories, new Range(0, 100));

        int sum = 0;
        for (final EqualizerCategory cat : this.equalizerCategories) {
            sum += cat.getValue();
        }

        if (sum == 0) {
            // integer div
            final int initial = 100 / this.equalizerCategories.size();
            final int delta = 100 % this.equalizerCategories.size();

            final Iterator<EqualizerCategory> it = this.equalizerCategories.iterator();
            while (it.hasNext()) {
                final EqualizerCategory cat = it.next();
                if ((delta == 0) || it.hasNext()) {
                    cat.setValue(initial);
                } else {
                    cat.setValue(initial + delta);
                }
            }
        } else if (sum != 100) {
            throw new IllegalArgumentException("equalizer categories do not sum up to 100: " + sum); // NOI18N
        }
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    public void setValueAt(final int index, final int value) {
        checkValueWithinRange(value);

        if (getValueAt(index) == value) {
            // no change, do nothing
            return;
        }

        final int[] newVals = calculateValues(index, value);

        for (int i = 0; i < getEqualizerCategoryCount(); ++i) {
            equalizerCategories.get(i).setValue(newVals[i]);
        }

        fireEqualizerModelEvent(new EqualizerModelEvent(this));
    }

    /**
     * DOCUMENT ME!
     *
     * @param   index  DOCUMENT ME!
     * @param   value  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int[] calculateValues(final int index, final int value) {
        // special case, no calculation has to be done
        if (value == 100) {
            final int[] values = new int[getEqualizerCategoryCount()];
            for (int i = 0; i < values.length; ++i) {
                values[i] = 0;
            }
            values[index] = 100;

            return values;
        }

        // reverse delta
        final int delta = getValueAt(index) - value;
        final int[] values = new int[getEqualizerCategoryCount()];
        final ArrayList<Integer> goodIndexes = new ArrayList<Integer>(getEqualizerCategoryCount());
        for (int i = 0; i < getEqualizerCategoryCount(); ++i) {
            values[i] = getValueAt(i);
            goodIndexes.add(i);
        }

        values[index] = value;
        removeIndex(goodIndexes, index);

        // special behaviour so that we can actually manage to have values fixed to zero
        if ((delta > 0) && ((values[index] + delta) != 100)) {
            final ListIterator<Integer> li = goodIndexes.listIterator();
            while (li.hasNext()) {
                if (values[li.next()] == 0) {
                    li.remove();
                }
            }
        }

        int valueDelta = delta / goodIndexes.size();
        int carryOver = delta % goodIndexes.size();

        while (valueDelta != 0) {
            final ListIterator<Integer> li = goodIndexes.listIterator();
            while (li.hasNext()) {
                final int i = li.next();
                values[i] += valueDelta;

                if (values[i] <= 0) {
                    li.remove();
                    carryOver += values[i];
                    values[i] = 0;
                }
            }

            valueDelta = carryOver / goodIndexes.size();
            carryOver = carryOver % goodIndexes.size();
        }

        final ListIterator<Integer> li = goodIndexes.listIterator();
        while (carryOver != 0) {
            final int i = li.next();

            if (carryOver > 0) {
                values[i]++;
                carryOver--;
            } else if (carryOver < 0) {
                values[i]--;
                carryOver++;
            }
        }

        return values;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  list      DOCUMENT ME!
     * @param  toRemove  DOCUMENT ME!
     */
    private void removeIndex(final List<Integer> list, final int toRemove) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) == toRemove) {
                list.remove(i);

                return;
            }
        }
    }
}
