/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.treetable;
/*
 * FileSystemModel2.java
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

import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.Stack;

import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;

/**
 * FileSystemModel2 is a TreeTableModel representing a hierarchical file system.
 *
 * <p>This will recursively load all the children from the path it is created with. The loading is done with the method
 * reloadChildren, and happens in another thread. The method isReloading can be invoked to check if there are active
 * threads. The total size of all the files are also accumulated.</p>
 *
 * <p>By default links are not descended. java.io.File does not have a way to distinguish links, so a file is assumed to
 * be a link if its canonical path does not start with its parent path. This may not cover all cases, but works for the
 * time being.</p>
 *
 * <p>Reloading happens such that all the files of the directory are loaded and immediately available. The background
 * thread then recursively loads all the files of each of those directories. When each directory has finished loading
 * all its sub files they are attached and an event is generated in the event dispatching thread. A more ambitious
 * approach would be to attach each set of directories as they are loaded and generate an event. Then, once all the
 * direct descendants of the directory being reloaded have finished loading, it is resorted based on total size.</p>
 *
 * <p>While you can invoke reloadChildren as many times as you want, care should be taken in doing this. You should not
 * invoke reloadChildren on a node that is already being reloaded, or going to be reloaded (meaning its parent is
 * reloading but it hasn't started reloading that directory yet). If this is done odd results may happen.
 * FileSystemModel2 does not enforce any policy in this manner, and it is up to the user of FileSystemModel2 to ensure
 * it doesn't happen.</p>
 *
 * @author   Philip Milne
 * @author   Scott Violet
 * @version  1.12 05/12/98
 */

public class FileSystemModel2 extends AbstractTreeTableModel {

    //~ Static fields/initializers ---------------------------------------------

    // Names of the columns.
    protected static String[] cNames = { "Name", "Size", "Type", "Modified" }; // NOI18N

    // Types of the columns.
    protected static Class[] cTypes = {
            TreeTableModel.class,
            Integer.class, String.class,
            Date.class
        };

    // The the returned file length for directories.
    public static final Integer ZERO = new Integer(0);

    /** An array of MergeSorter sorters, that will sort based on size. */
    static Stack sorters = new Stack();

    protected static FileNode[] EMPTY_CHILDREN = new FileNode[0];

    // Used to sort the file names.
    private static MergeSort fileMS = new MergeSort() {

            @Override
            public int compareElementsAt(final int beginLoc, final int endLoc) {
                return ((String)toSort[beginLoc]).compareTo((String)toSort[endLoc]);
            }
        };

    //~ Instance fields --------------------------------------------------------

    /** True if the receiver is valid, once set to false all Threads loading files will stop. */
    protected boolean isValid;

    /** Node currently being reloaded, this becomes somewhat muddy if reloading is happening in multiple threads. */
    protected FileNode reloadNode;

    /** Returns true if links are to be descended. */
    protected boolean descendLinks;

    /** > 0 indicates reloading some nodes. */
    int reloadCount;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a FileSystemModel2 rooted at File.separator, which is usually the root of the file system. This does not
     * load it, you should invoke <code>reloadChildren</code> with the root to start loading.
     */
    public FileSystemModel2() {
        this(File.separator);
    }

    /**
     * Creates a FileSystemModel2 with the root being <code>rootPath</code>. This does not load it, you should invoke
     * <code>reloadChildren</code> with the root to start loading.
     *
     * @param  rootPath  DOCUMENT ME!
     */
    public FileSystemModel2(final String rootPath) {
        super(null);
        isValid = true;
        root = new FileNode(new File(rootPath));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Returns a MergeSort that can sort on the totalSize of a FileNode.
     *
     * @return  DOCUMENT ME!
     */
    protected static MergeSort getSizeSorter() {
        synchronized (sorters) {
            if (sorters.size() == 0) {
                return new SizeSorter();
            }
            return (MergeSort)sorters.pop();
        }
    }

    /**
     * Should be invoked when a MergeSort is no longer needed.
     *
     * @param  sorter  DOCUMENT ME!
     */
    protected static void recycleSorter(final MergeSort sorter) {
        synchronized (sorters) {
            sorters.push(sorter);
        }
    }

    //
    // The TreeModel interface
    //

    /**
     * Returns the number of children of <code>node</code>.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getChildCount(final Object node) {
        final Object[] children = getChildren(node);
        return (children == null) ? 0 : children.length;
    }

    /**
     * Returns the child of <code>node</code> at index <code>i</code>.
     *
     * @param   node  DOCUMENT ME!
     * @param   i     DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object getChild(final Object node, final int i) {
        return getChildren(node)[i];
    }

    /**
     * Returns true if the passed in object represents a leaf, false otherwise.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public boolean isLeaf(final Object node) {
        return ((FileNode)node).isLeaf();
    }

    //
    // The TreeTableNode interface.
    //

    /**
     * Returns the number of columns.
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public int getColumnCount() {
        return cNames.length;
    }

    /**
     * Returns the name for a particular column.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public String getColumnName(final int column) {
        return cNames[column];
    }

    /**
     * Returns the class for the particular column.
     *
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Class getColumnClass(final int column) {
        return cTypes[column];
    }

    /**
     * Returns the value of the particular column.
     *
     * @param   node    DOCUMENT ME!
     * @param   column  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    @Override
    public Object getValueAt(final Object node, final int column) {
        final FileNode fn = (FileNode)node;

        try {
            switch (column) {
                case 0: {
                    return fn.getFile().getName();
                }
                case 1: {
                    if (fn.isTotalSizeValid()) {
                        return new Integer((int)((FileNode)node).totalSize());
                    }
                    return null;
                }
                case 2: {
                    return fn.isLeaf() ? "File" : "Directory"; // NOI18N
                }
                case 3: {
                    return fn.lastModified();
                }
            }
        } catch (SecurityException se) {
        }

        return null;
    }

    //
    // Some convenience methods.
    //

    /**
     * Reloads the children of the specified node.
     *
     * @param  node  DOCUMENT ME!
     */
    public void reloadChildren(final Object node) {
        final FileNode fn = (FileNode)node;

        synchronized (this) {
            reloadCount++;
        }
        fn.resetSize();
        new Thread(new FileNodeLoader((FileNode)node)).start();
    }

    /**
     * Stops and waits for all threads to finish loading.
     */
    public void stopLoading() {
        isValid = false;
        synchronized (this) {
            while (reloadCount > 0) {
                try {
                    wait();
                } catch (InterruptedException ie) {
                }
            }
        }
        isValid = true;
    }

    /**
     * If <code>newValue</code> is true, links are descended. Odd results may happen if you set this while other threads
     * are loading.
     *
     * @param  newValue  DOCUMENT ME!
     */
    public void setDescendsLinks(final boolean newValue) {
        descendLinks = newValue;
    }

    /**
     * Returns true if links are to be automatically descended.
     *
     * @return  DOCUMENT ME!
     */
    public boolean getDescendsLinks() {
        return descendLinks;
    }

    /**
     * Returns the path <code>node</code> represents.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getPath(final Object node) {
        return ((FileNode)node).getFile().getPath();
    }

    /**
     * Returns the total size of the receiver.
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public long getTotalSize(final Object node) {
        return ((FileNode)node).totalSize();
    }

    /**
     * Returns true if the receiver is loading any children.
     *
     * @return  DOCUMENT ME!
     */
    public boolean isReloading() {
        return (reloadCount > 0);
    }

    /**
     * Returns the path to the node that is being loaded.
     *
     * @return  DOCUMENT ME!
     */
    public TreePath getPathLoading() {
        final FileNode rn = reloadNode;

        if (rn != null) {
            return new TreePath(rn.getPath());
        }
        return null;
    }

    /**
     * Returns the node being loaded.
     *
     * @return  DOCUMENT ME!
     */
    public Object getNodeLoading() {
        return reloadNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected File getFile(final Object node) {
        final FileNode fileNode = ((FileNode)node);
        return fileNode.getFile();
    }

    /**
     * DOCUMENT ME!
     *
     * @param   node  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    protected Object[] getChildren(final Object node) {
        final FileNode fileNode = ((FileNode)node);
        return fileNode.getChildren();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * A FileNode is a derivative of the File class - though we delegate to the File object rather than subclassing it.
     * It is used to maintain a cache of a directory's children and therefore avoid repeated access to the underlying
     * file system during rendering.
     *
     * @version  $Revision$, $Date$
     */
    class FileNode {

        //~ Instance fields ----------------------------------------------------

        /** java.io.File the receiver represents. */
        protected File file;
        /** Children of the receiver. */
        protected FileNode[] children;
        /** Size of the receiver and all its children. */
        protected long totalSize;
        /** Valid if the totalSize has finished being calced. */
        protected boolean totalSizeValid;
        /** Path of the receiver. */
        protected String canonicalPath;
        /** True if the canonicalPath of this instance does not start with the canonical path of the parent. */
        protected boolean isLink;
        /** Date last modified. */
        protected Date lastModified;
        /** Parent FileNode of the receiver. */
        private FileNode parent;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FileNode object.
         *
         * @param  file  DOCUMENT ME!
         */
        protected FileNode(final File file) {
            this(null, file);
        }

        /**
         * Creates a new FileNode object.
         *
         * @param  parent  DOCUMENT ME!
         * @param  file    DOCUMENT ME!
         */
        protected FileNode(final FileNode parent, final File file) {
            this.parent = parent;
            this.file = file;
            try {
                canonicalPath = file.getCanonicalPath();
            } catch (IOException ioe) {
                canonicalPath = ""; // NOI18N
            }
            if (parent != null) {
                isLink = !canonicalPath.startsWith(parent.getCanonicalPath());
            } else {
                isLink = false;
            }
            if (isLeaf()) {
                totalSize = file.length();
                totalSizeValid = true;
            }
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Returns the date the receiver was last modified.
         *
         * @return  DOCUMENT ME!
         */
        public Date lastModified() {
            if ((lastModified == null) && (file != null)) {
                lastModified = new Date(file.lastModified());
            }
            return lastModified;
        }

        /**
         * Returns the the string to be used to display this leaf in the JTree.
         *
         * @return  DOCUMENT ME!
         */
        @Override
        public String toString() {
            return file.getName();
        }

        /**
         * Returns the java.io.File the receiver represents.
         *
         * @return  DOCUMENT ME!
         */
        public File getFile() {
            return file;
        }

        /**
         * Returns size of the receiver and all its children.
         *
         * @return  DOCUMENT ME!
         */
        public long totalSize() {
            return totalSize;
        }

        /**
         * Returns the parent of the receiver.
         *
         * @return  DOCUMENT ME!
         */
        public FileNode getParent() {
            return parent;
        }

        /**
         * Returns true if the receiver represents a leaf, that is it is isn't a directory.
         *
         * @return  DOCUMENT ME!
         */
        public boolean isLeaf() {
            return file.isFile();
        }

        /**
         * Returns true if the total size is valid.
         *
         * @return  DOCUMENT ME!
         */
        public boolean isTotalSizeValid() {
            return totalSizeValid;
        }

        /**
         * Clears the date.
         */
        protected void resetLastModified() {
            lastModified = null;
        }

        /**
         * Sets the size of the receiver to be 0.
         */
        protected void resetSize() {
            alterTotalSize(-totalSize);
        }

        /**
         * Loads the children, caching the results in the children instance variable.
         *
         * @return  DOCUMENT ME!
         */
        protected FileNode[] getChildren() {
            return children;
        }

        /**
         * Recursively loads all the children of the receiver.
         *
         * @param  sorter  DOCUMENT ME!
         */
        protected void loadChildren(final MergeSort sorter) {
            totalSize = file.length();
            children = createChildren(null);
            for (int counter = children.length - 1; counter >= 0; counter--) {
                Thread.yield(); // Give the GUI CPU time to draw itself.
                if (!children[counter].isLeaf()
                            && (descendLinks || !children[counter].isLink())) {
                    children[counter].loadChildren(sorter);
                }
                totalSize += children[counter].totalSize();
                if (!isValid) {
                    counter = 0;
                }
            }
            if (isValid) {
                if (sorter != null) {
                    sorter.sort(children);
                }
                totalSizeValid = true;
            }
        }

        /**
         * Loads the children of of the receiver.
         *
         * @param   sorter  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected FileNode[] createChildren(final MergeSort sorter) {
            FileNode[] retArray = null;

            try {
                final String[] files = file.list();
                if (files != null) {
                    if (sorter != null) {
                        sorter.sort(files);
                    }
                    retArray = new FileNode[files.length];
                    final String path = file.getPath();
                    for (int i = 0; i < files.length; i++) {
                        final File childFile = new File(path, files[i]);
                        retArray[i] = new FileNode(this, childFile);
                    }
                }
            } catch (SecurityException se) {
            }
            if (retArray == null) {
                retArray = EMPTY_CHILDREN;
            }
            return retArray;
        }

        /**
         * Returns true if the children have been loaded.
         *
         * @return  DOCUMENT ME!
         */
        protected boolean loadedChildren() {
            return (file.isFile() || (children != null));
        }

        /**
         * Gets the path from the root to the receiver.
         *
         * @return  DOCUMENT ME!
         */
        public FileNode[] getPath() {
            return getPathToRoot(this, 0);
        }

        /**
         * Returns the canonical path for the receiver.
         *
         * @return  DOCUMENT ME!
         */
        public String getCanonicalPath() {
            return canonicalPath;
        }

        /**
         * Returns true if the receiver's path does not begin with the parent's canonical path.
         *
         * @return  DOCUMENT ME!
         */
        public boolean isLink() {
            return isLink;
        }

        /**
         * DOCUMENT ME!
         *
         * @param   aNode  DOCUMENT ME!
         * @param   depth  DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        protected FileNode[] getPathToRoot(final FileNode aNode, int depth) {
            FileNode[] retNodes;

            if (aNode == null) {
                if (depth == 0) {
                    return null;
                } else {
                    retNodes = new FileNode[depth];
                }
            } else {
                depth++;
                retNodes = getPathToRoot(aNode.getParent(), depth);
                retNodes[retNodes.length - depth] = aNode;
            }
            return retNodes;
        }

        /**
         * Sets the children of the receiver, updates the total size, and if generateEvent is true a tree structure
         * changed event is created.
         *
         * @param  newChildren    DOCUMENT ME!
         * @param  generateEvent  DOCUMENT ME!
         */
        protected void setChildren(final FileNode[] newChildren, final boolean generateEvent) {
            final long oldSize = totalSize;

            totalSize = file.length();
            children = newChildren;
            for (int counter = children.length - 1; counter >= 0; counter--) {
                totalSize += children[counter].totalSize();
            }

            if (generateEvent) {
                final FileNode[] path = getPath();

                fireTreeStructureChanged(FileSystemModel2.this, path, null,
                    null);

                final FileNode parent = getParent();

                if (parent != null) {
                    parent.alterTotalSize(totalSize - oldSize);
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  sizeDelta  DOCUMENT ME!
         */
        protected synchronized void alterTotalSize(final long sizeDelta) {
            if ((sizeDelta != 0) && ((parent = getParent()) != null)) {
                totalSize += sizeDelta;
                nodeChanged();
                parent.alterTotalSize(sizeDelta);
            } else {
                // Need a way to specify the root.
                totalSize += sizeDelta;
            }
        }

        /**
         * This should only be invoked on the event dispatching thread.
         *
         * @param  newValue  DOCUMENT ME!
         */
        protected synchronized void setTotalSizeValid(final boolean newValue) {
            if (totalSizeValid != newValue) {
                nodeChanged();
                totalSizeValid = newValue;

                final FileNode parent = getParent();

                if (parent != null) {
                    parent.childTotalSizeChanged(this);
                }
            }
        }

        /**
         * Marks the receivers total size as valid, but does not invoke node changed, nor message the parent.
         */
        protected synchronized void forceTotalSizeValid() {
            totalSizeValid = true;
        }

        /**
         * Invoked when a childs total size has changed.
         *
         * @param  child  DOCUMENT ME!
         */
        protected synchronized void childTotalSizeChanged(final FileNode child) {
            if (totalSizeValid != child.isTotalSizeValid()) {
                if (totalSizeValid) {
                    setTotalSizeValid(false);
                } else {
                    final FileNode[] children = getChildren();

                    for (int counter = children.length - 1; counter >= 0; counter--) {
                        if (!children[counter].isTotalSizeValid()) {
                            return;
                        }
                    }
                    setTotalSizeValid(true);
                }
            }
        }

        /**
         * Can be invoked when a node has changed, will create the appropriate event.
         */
        protected void nodeChanged() {
            final FileNode parent = getParent();

            if (parent != null) {
                final FileNode[] path = parent.getPath();
                final int[] index = { getIndexOfChild(parent, this) };
                final Object[] children = { this };

                fireTreeNodesChanged(FileSystemModel2.this, path, index,
                    children);
            }
        }
    }

    /**
     * FileNodeLoader can be used to reload all the children of a particular node. It first resets the children of the
     * FileNode it is created with, and in its run method will reload all of that nodes children. FileNodeLoader may not
     * be running in the event dispatching thread. As swing is not thread safe it is important that we don't generate
     * events in this thread. SwingUtilities.invokeLater is used so that events are generated in the event dispatching
     * thread.
     *
     * @version  $Revision$, $Date$
     */
    class FileNodeLoader implements Runnable {

        //~ Instance fields ----------------------------------------------------

        /** Node creating children for. */
        FileNode node;
        /** Sorter. */
        MergeSort sizeMS;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new FileNodeLoader object.
         *
         * @param  node  DOCUMENT ME!
         */
        FileNodeLoader(final FileNode node) {
            this.node = node;
            node.resetLastModified();
            node.setChildren(node.createChildren(fileMS), true);
            node.setTotalSizeValid(false);
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void run() {
            final FileNode[] children = node.getChildren();

            sizeMS = getSizeSorter();
            for (int counter = children.length - 1; counter >= 0; counter--) {
                if (!children[counter].isLeaf()) {
                    reloadNode = children[counter];
                    loadChildren(children[counter]);
                    reloadNode = null;
                }
                if (!isValid) {
                    counter = 0;
                }
            }
            recycleSorter(sizeMS);
            if (isValid) {
                SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            final MergeSort sorter = getSizeSorter();

                            sorter.sort(node.getChildren());
                            recycleSorter(sorter);
                            node.setChildren(node.getChildren(), true);
                            synchronized (FileSystemModel2.this) {
                                reloadCount--;
                                FileSystemModel2.this.notifyAll();
                            }
                        }
                    });
            } else {
                synchronized (FileSystemModel2.this) {
                    reloadCount--;
                    FileSystemModel2.this.notifyAll();
                }
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @param  node  DOCUMENT ME!
         */
        protected void loadChildren(final FileNode node) {
            if (!node.isLeaf() && (descendLinks || !node.isLink())) {
                final FileNode[] children = node.createChildren(null);

                for (int counter = children.length - 1; counter >= 0; counter--) {
                    if (!children[counter].isLeaf()) {
                        if (descendLinks || !children[counter].isLink()) {
                            children[counter].loadChildren(sizeMS);
                        } else {
                            children[counter].forceTotalSizeValid();
                        }
                    }
                    if (!isValid) {
                        counter = 0;
                    }
                }
                if (isValid) {
                    final FileNode fn = node;

                    // Reset the children
                    SwingUtilities.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                final MergeSort sorter = getSizeSorter();

                                sorter.sort(children);
                                recycleSorter(sorter);
                                fn.setChildren(children, true);
                                fn.setTotalSizeValid(true);
                                fn.nodeChanged();
                            }
                        });
                }
            } else {
                node.forceTotalSizeValid();
            }
        }
    }

    /**
     * Sorts the contents, which must be instances of FileNode based on totalSize.
     *
     * @version  $Revision$, $Date$
     */
    static class SizeSorter extends MergeSort {

        //~ Methods ------------------------------------------------------------

        @Override
        public int compareElementsAt(final int beginLoc, final int endLoc) {
            final long firstSize = ((FileNode)toSort[beginLoc]).totalSize();
            final long secondSize = ((FileNode)toSort[endLoc]).totalSize();

            if (firstSize != secondSize) {
                return (int)(secondSize - firstSize);
            }
            return ((FileNode)toSort[beginLoc]).toString().compareTo(((FileNode)toSort[endLoc]).toString());
        }
    }
}
