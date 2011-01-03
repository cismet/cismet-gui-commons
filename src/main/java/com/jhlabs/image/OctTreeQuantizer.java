/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.jhlabs.image;

import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.image.*;

import java.io.*;

import java.util.*;

/**
 * An image Quantizer based on the Octree algorithm. This is a very basic implementation at present and could be much
 * improved by picking the nodes to reduce more carefully (i.e. not completely at random) when I get the time.
 *
 * @version  $Revision$, $Date$
 */
public class OctTreeQuantizer implements Quantizer {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger log = Logger.getLogger(OctTreeQuantizer.class);

    /** The greatest depth the tree is allowed to reach. */
    static final int MAX_LEVEL = 5;

    //~ Instance fields --------------------------------------------------------

    private int nodes = 0;
    private OctTreeNode root;
    private int reduceColors;
    private int maximumColors;
    private int colors = 0;
    private Vector[] colorList;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new OctTreeQuantizer object.
     */
    public OctTreeQuantizer() {
        setup(256);
        colorList = new Vector[MAX_LEVEL + 1];
        for (int i = 0; i < (MAX_LEVEL + 1); i++) {
            colorList[i] = new Vector();
        }
        root = new OctTreeNode();
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Initialize the quantizer. This should be called before adding any pixels.
     *
     * @param  numColors  the number of colors we're quantizing to.
     */
    @Override
    public void setup(final int numColors) {
        maximumColors = numColors;
        reduceColors = Math.max(512, numColors * 2);
    }

    /**
     * Add pixels to the quantizer.
     *
     * @param  pixels  the array of ARGB pixels
     * @param  offset  the offset into the array
     * @param  count   the count of pixels
     */
    @Override
    public void addPixels(final int[] pixels, final int offset, final int count) {
        for (int i = 0; i < count; i++) {
            insertColor(pixels[i + offset]);
            if (colors > reduceColors) {
                reduceTree(reduceColors);
            }
        }
    }

    /**
     * Get the color table index for a color.
     *
     * @param   rgb  the color
     *
     * @return  the index
     */
    @Override
    public int getIndexForColor(final int rgb) {
        final int red = (rgb >> 16) & 0xff;
        final int green = (rgb >> 8) & 0xff;
        final int blue = rgb & 0xff;

        OctTreeNode node = root;

        for (int level = 0; level <= MAX_LEVEL; level++) {
            final OctTreeNode child;
            final int bit = 0x80 >> level;

            int index = 0;
            if ((red & bit) != 0) {
                index += 4;
            }
            if ((green & bit) != 0) {
                index += 2;
            }
            if ((blue & bit) != 0) {
                index += 1;
            }

            child = node.leaf[index];

            if (child == null) {
                return node.index;
            } else if (child.isLeaf) {
                return child.index;
            } else {
                node = child;
            }
        }
        if (log.isInfoEnabled()) {
            log.info("getIndexForColor failed"); // NOI18N
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  rgb  DOCUMENT ME!
     */
    private void insertColor(final int rgb) {
        final int red = (rgb >> 16) & 0xff;
        final int green = (rgb >> 8) & 0xff;
        final int blue = rgb & 0xff;

        OctTreeNode node = root;

//              System.out.println("insertColor="+Integer.toHexString(rgb));
        for (int level = 0; level <= MAX_LEVEL; level++) {
            OctTreeNode child;
            final int bit = 0x80 >> level;

            int index = 0;
            if ((red & bit) != 0) {
                index += 4;
            }
            if ((green & bit) != 0) {
                index += 2;
            }
            if ((blue & bit) != 0) {
                index += 1;
            }

            child = node.leaf[index];

            if (child == null) {
                node.children++;

                child = new OctTreeNode();
                child.parent = node;
                node.leaf[index] = child;
                node.isLeaf = false;
                nodes++;
                colorList[level].addElement(child);

                if (level == MAX_LEVEL) {
                    child.isLeaf = true;
                    child.count = 1;
                    child.totalRed = red;
                    child.totalGreen = green;
                    child.totalBlue = blue;
                    child.level = level;
                    colors++;
                    return;
                }

                node = child;
            } else if (child.isLeaf) {
                child.count++;
                child.totalRed += red;
                child.totalGreen += green;
                child.totalBlue += blue;
                return;
            } else {
                node = child;
            }
        }
        if (log.isInfoEnabled()) {
            log.info("insertColor failed"); // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  numColors  DOCUMENT ME!
     */
    private void reduceTree(final int numColors) {
        for (int level = MAX_LEVEL - 1; level >= 0; level--) {
            final Vector v = colorList[level];
            if ((v != null) && (v.size() > 0)) {
                for (int j = 0; j < v.size(); j++) {
                    final OctTreeNode node = (OctTreeNode)v.elementAt(j);
                    if (node.children > 0) {
                        for (int i = 0; i < 8; i++) {
                            final OctTreeNode child = node.leaf[i];
                            if (child != null) {
                                if (!child.isLeaf) {
                                    if (log.isDebugEnabled()) {
                                        log.debug("not a leaf!"); // NOI18N
                                    }
                                }

                                node.count += child.count;
                                node.totalRed += child.totalRed;
                                node.totalGreen += child.totalGreen;
                                node.totalBlue += child.totalBlue;
                                node.leaf[i] = null;
                                node.children--;
                                colors--;
                                nodes--;
                                colorList[level + 1].removeElement(child);
                            }
                        }
                        node.isLeaf = true;
                        colors++;
                        if (colors <= numColors) {
                            return;
                        }
                    }
                }
            }
        }

        if (log.isInfoEnabled()) {
            log.info("Unable to reduce the OctTree"); // NOI18N
        }
    }

    /**
     * Build the color table.
     *
     * @return  the color table
     */
    @Override
    public int[] buildColorTable() {
        final int[] table = new int[colors];
        buildColorTable(root, table, 0);
        return table;
    }

    /**
     * A quick way to use the quantizer. Just create a table the right size and pass in the pixels.
     *
     * @param  inPixels  the input colors
     * @param  table     the output color table
     */
    public void buildColorTable(final int[] inPixels, final int[] table) {
        final int count = inPixels.length;
        maximumColors = table.length;
        for (int i = 0; i < count; i++) {
            insertColor(inPixels[i]);
            if (colors > reduceColors) {
                reduceTree(reduceColors);
            }
        }
        if (colors > maximumColors) {
            reduceTree(maximumColors);
        }
        buildColorTable(root, table, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param   node   DOCUMENT ME!
     * @param   table  DOCUMENT ME!
     * @param   index  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int buildColorTable(final OctTreeNode node, final int[] table, int index) {
        if (colors > maximumColors) {
            reduceTree(maximumColors);
        }

        if (node.isLeaf) {
            final int count = node.count;
            table[index] = 0xff000000
                        | ((node.totalRed / count) << 16)
                        | ((node.totalGreen / count) << 8)
                        | (node.totalBlue / count);
            node.index = index++;
        } else {
            for (int i = 0; i < 8; i++) {
                if (node.leaf[i] != null) {
                    node.index = index;
                    index = buildColorTable(node.leaf[i], table, index);
                }
            }
        }
        return index;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * An Octtree node.
     *
     * @version  $Revision$, $Date$
     */
    class OctTreeNode {

        //~ Instance fields ----------------------------------------------------

        int children;
        int level;
        OctTreeNode parent;
        OctTreeNode[] leaf = new OctTreeNode[8];
        boolean isLeaf;
        int count;
        int totalRed;
        int totalGreen;
        int totalBlue;
        int index;

        //~ Methods ------------------------------------------------------------

        /**
         * A debugging method which prints the tree out.
         *
         * @param  s      DOCUMENT ME!
         * @param  level  DOCUMENT ME!
         */
        public void list(final PrintStream s, final int level) {
            for (int i = 0; i < level; i++) {
                System.out.print(' ');
            }
            if (count == 0) {
                System.out.println(index + ": count=" + count);                       // NOI18N
            } else {
                System.out.println(index + ": count=" + count + " red=" + (totalRed / count) + " green="
                            + (totalGreen / count) + " blue=" + (totalBlue / count)); // NOI18N
            }
            for (int i = 0; i < 8; i++) {
                if (leaf[i] != null) {
                    leaf[i].list(s, level + 2);
                }
            }
        }
    }
}
