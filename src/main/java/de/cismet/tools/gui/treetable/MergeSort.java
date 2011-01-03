/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.treetable;
/*
 * MergeSort.java
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

/**
 * An implementation of MergeSort, needs to be subclassed to compare the terms.
 *
 * @author   Scott Violet
 * @version  $Revision$, $Date$
 */
public abstract class MergeSort extends Object {

    //~ Instance fields --------------------------------------------------------

    protected Object[] toSort;
    protected Object[] swapSpace;

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  array  DOCUMENT ME!
     */
    public void sort(final Object[] array) {
        if ((array != null) && (array.length > 1)) {
            final int maxLength;

            maxLength = array.length;
            swapSpace = new Object[maxLength];
            toSort = array;
            this.mergeSort(0, maxLength - 1);
            swapSpace = null;
            toSort = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   beginLoc  DOCUMENT ME!
     * @param   endLoc    DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public abstract int compareElementsAt(int beginLoc, int endLoc);

    /**
     * DOCUMENT ME!
     *
     * @param  begin  DOCUMENT ME!
     * @param  end    DOCUMENT ME!
     */
    protected void mergeSort(final int begin, final int end) {
        if (begin != end) {
            final int mid;

            mid = (begin + end) / 2;
            this.mergeSort(begin, mid);
            this.mergeSort(mid + 1, end);
            this.merge(begin, mid, end);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  begin   DOCUMENT ME!
     * @param  middle  DOCUMENT ME!
     * @param  end     DOCUMENT ME!
     */
    protected void merge(final int begin, final int middle, final int end) {
        int firstHalf;
        int secondHalf;
        int count;

        firstHalf = count = begin;
        secondHalf = middle + 1;
        while ((firstHalf <= middle) && (secondHalf <= end)) {
            if (this.compareElementsAt(secondHalf, firstHalf) < 0) {
                swapSpace[count++] = toSort[secondHalf++];
            } else {
                swapSpace[count++] = toSort[firstHalf++];
            }
        }
        if (firstHalf <= middle) {
            while (firstHalf <= middle) {
                swapSpace[count++] = toSort[firstHalf++];
            }
        } else {
            while (secondHalf <= end) {
                swapSpace[count++] = toSort[secondHalf++];
            }
        }
        for (count = begin; count <= end; count++) {
            toSort[count] = swapSpace[count];
        }
    }
}
