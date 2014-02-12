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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * This is the default implementation of the {@link EqualizerModel}. It ensures that the initial values of the
 * categories are within the <code>Range</code> and that there are no duplicate category names.
 *
 * @author   martin.scholl@cismet.de
 * @version  1.0
 */
public class DefaultEqualizerModel extends AbstractEqualizerModel {

    //~ Static fields/initializers ---------------------------------------------

    /** The default range of this model: <code>[0, 100]</code> */
    public static final Range DEFAULT_RANGE = new Range(0, 100);

    //~ Instance fields --------------------------------------------------------

    protected final List<EqualizerCategory> equalizerCategories;
    private final Range range;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new DefaultEqualizerModel object using the given list of <code>EqualizerCategory</code>s. Simply
     * delegates to {@link #DefaultEqualizerModel(java.util.Collection, de.cismet.commons.gui.equalizer.Range)} using
     * the {@link #DEFAULT_RANGE}.
     *
     * @param  equalizerCategories  the <code>EqualizerCategory</code>s of this model
     *
     * @see    #DefaultEqualizerModel(java.util.Collection, de.cismet.commons.gui.equalizer.Range)
     */
    public DefaultEqualizerModel(final Collection<EqualizerCategory> equalizerCategories) {
        this(equalizerCategories, DEFAULT_RANGE);
    }

    /**
     * Creates a new DefaultEqualizerModel object using the given list of <code>EqualizerCategory</code>s and the given
     * <code>Range</code>. <code>null</code> is not permitted as parameter value and results in an <code>
     * IllegalArgumentException</code>. Additionally, <code>IllegalArgumentException</code> will also be thrown if any
     * of the initial values of the categories is not within <code>Range</code> or there are duplicate category names
     * <br/>
     * <br/>
     * <b>NOTE:</b> The element sorting of the collection's {@link Iterator} is responsible for the indexing of
     * available categories. This means that the first element of the <code>Iterator</code> will be available at index
     * <code>0</code> while the last element returned by the <code>Iterator</code> will be available at index <code>
     * collection.size() - 1</code>.
     *
     * @param   equalizerCategories  the <code>EqualizerCategory</code>s of this model
     * @param   range                the <code>Range</code> of the values of this model's categories
     *
     * @throws  IllegalArgumentException  if any argument is <code>null</code>, if there are duplicate category names or
     *                                    if any initial value is not within <code>Range</code>
     */
    public DefaultEqualizerModel(final Collection<EqualizerCategory> equalizerCategories, final Range range) {
        if (equalizerCategories == null) {
            throw new IllegalArgumentException("equalizerCategories must not be null"); // NOI18N
        }
        if (range == null) {
            throw new IllegalArgumentException("range must not be null");               // NOI18N
        }

        checkDuplicateCategoryNames(equalizerCategories);

        this.range = range;
        for (final EqualizerCategory cat : equalizerCategories) {
            checkValueWithinRange(cat.getValue());
        }

        this.equalizerCategories = cloneCategories(equalizerCategories);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * checks if value is within the range of the model.
     *
     * @param   value  value to check
     *
     * @throws  IllegalArgumentException  if value is not within range of the model
     */
    private void checkValueWithinRange(final int value) {
        if ((range.getMin() > value) || (range.getMax() < value)) {
            throw new IllegalArgumentException("value is not within range: [value=" + value + "|range=" + range + "]"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   categories  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private void checkDuplicateCategoryNames(final Collection<EqualizerCategory> categories) {
        final HashSet<String> names = new HashSet<String>(categories.size(), 1);
        final Iterator<EqualizerCategory> it = categories.iterator();
        while (it.hasNext()) {
            final String name = it.next().getName();
            if (!names.add(name)) {
                throw new IllegalArgumentException("duplicate category name: " + name); // NOI18N
            }
        }
    }

    /**
     * Clones the given categories.
     *
     * @param   categories  categories to clone
     *
     * @return  a deep clone of the given collection
     */
    private List<EqualizerCategory> cloneCategories(final Collection<EqualizerCategory> categories) {
        final ArrayList<EqualizerCategory> clone = new ArrayList<EqualizerCategory>(categories.size());
        final Iterator<EqualizerCategory> it = categories.iterator();
        while (it.hasNext()) {
            clone.add((EqualizerCategory)it.next().clone());
        }

        return clone;
    }

    /**
     * Getter for the <code>EqualizerCategory</code>s of this model. Returns a deep clone of the categories and not the
     * internal collection itself.
     *
     * @return  the categories of this model
     */
    public List<EqualizerCategory> getEqualizerCategories() {
        return cloneCategories(equalizerCategories);
    }

    @Override
    public Range getRange() {
        return range;
    }

    @Override
    public String getEqualizerCategory(final int index) {
        return equalizerCategories.get(index).getName();
    }

    @Override
    public int getEqualizerCategoryCount() {
        return equalizerCategories.size();
    }

    @Override
    public int getValueAt(final int index) {
        return equalizerCategories.get(index).getValue();
    }

    @Override
    public void setValueAt(final int index, final int value) {
        final EqualizerCategory cat = equalizerCategories.get(index);

        checkValueWithinRange(value);

        final int oldValue = cat.getValue();

        if (oldValue != value) {
            cat.setValue(value);

            fireEqualizerModelEvent(new EqualizerModelEvent(this, index, oldValue, value));
        }
    }
}
