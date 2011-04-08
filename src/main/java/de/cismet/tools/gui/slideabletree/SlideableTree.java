/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.slideabletree;

import org.apache.log4j.Logger;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;

import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.plaf.TreeUI;
import javax.swing.text.Position.Bias;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.cismet.tools.gui.StaticSwingTools;

/**
 * DOCUMENT ME!
 *
 * <p>SlideableTree visualize the children of the trees root node with an JXTaskpane. The TaskPanes contains a
 * SlideablesubTree, which represents the tree under this child.</p>
 *
 * <p>To use this specialized View, you have to use the constructor with an boolean flag e.g</p>
 *
 * <pre>
{@Code
 *          final JTree t = new SlideableTree(true);
 *      }
 *    </pre>
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class SlideableTree extends JTree implements TreeExpansionListener,
    TreeSelectionListener,
    TreeWillExpandListener {

    //~ Instance fields --------------------------------------------------------

    protected boolean useSlideableTreeView = false;
    private final Logger logger;
    private JXTaskPaneContainer container;
    private ArrayList<SlideableSubTree> trees;
    private ArrayList<SubTreePane> panes;
    private JScrollPane containerScrollPane;
    private JPanel panel;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new SlideableTree object.
     */
    public SlideableTree() {
        this(false);
    }

    /**
     * Creates a new SlideableTree object.
     *
     * @param  useSlideableView  a flag for using a specialized view with JXTaskPanes
     */
    public SlideableTree(final boolean useSlideableView) {
        this.useSlideableTreeView = useSlideableView;
        logger = Logger.getLogger(getClass());

        if (useSlideableTreeView) {
            trees = new ArrayList<SlideableSubTree>();
            panes = new ArrayList<SubTreePane>();
            container = new JXTaskPaneContainer();

            /*
             * Je nach LookAndFeel wird der BackgroundPainter benutzt um den Hintergrund eines TaskPanecontainers zu
             * zeichen. In diesem Fall funktioniert setBackground nicht mehr...
             */
            container.setBackgroundPainter(null);
            // change titlebar background of JXTaskPanes
            UIManager.getDefaults().put("TaskPane.titleBackgroundGradientStart", new Color(222, 222, 222));
            UIManager.getDefaults().put("TaskPane.titleBackgroundGradientEnd", new Color(244, 244, 244));
            this.setLayout(new BorderLayout());
            final VerticalLayout verticalLayout = new VerticalLayout();
            verticalLayout.setGap(7);
            container.setLayout(verticalLayout);
            container.setBorder(new EmptyBorder(0, 5, 0, 5));
            container.setBackground(new Color(255, 255, 255));

            containerScrollPane = new JScrollPane(container);
            // fuer niftyScrollBar
            StaticSwingTools.setNiftyScrollBars(containerScrollPane);

            /*
             * Create for all children of rootNode a new SlideableSubTree and add it to a JXTaskPane
             */
            createSubTrees(this.getModel());
            addToTreeContainer(panes);
            this.setDragEnabled(true);
            this.setEditable(false);

            this.add(containerScrollPane, BorderLayout.CENTER);
        }
    }

    /**
     * Creates a new SlideableTree object.
     *
     * @param  model  DOCUMENT ME!
     */
    public SlideableTree(final TreeModel model) {
        this(false);
        this.setModel(model);
    }

    /**
     * Creates a new SlideableTree object.
     *
     * @param  node  DOCUMENT ME!
     */
    public SlideableTree(final TreeNode node) {
        this(new DefaultTreeModel(node));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isUseSlideableTreeView() {
        return useSlideableTreeView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JXTaskPaneContainer getContainer() {
        return container;
    }

    @Override
    public void addSelectionPath(final TreePath path) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final SlideableSubTree t = getSubTreeForPath(path);
                final TreePath subTreePath = getPathForSubTree(path);
                t.addSelectionPath(path);
            }
        } else {
            super.addSelectionPath(path);
        }
    }

    @Override
    public void addSelectionPaths(final TreePath[] paths) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (int i = 0; i < paths.length; i++) {
                    addSelectionPath(paths[i]);
                }
            }
            super.addSelectionPaths(paths);
        } else {
            super.addSelectionPaths(paths);
        }
    }

    @Override
    public void addSelectionRow(final int row) {
        final TreePath path = getPathForRow(row);
        addSelectionPath(path);
        super.addSelectionRow(row);
    }

    @Override
    public void addSelectionRows(final int[] rows) {
        if (useSlideableTreeView) {
            for (int i = 0; i < rows.length; i++) {
                addSelectionRow(rows[i]);
            }
            super.addSelectionRows(rows);
        } else {
            super.addSelectionRows(rows);
        }
    }

    /*
     * das Intervall wird auf die einzelnen SubTrees umgelenkt. Dabei kann durch index0 und die Anzahl der jeweils
     * sichtbaren Elemente im Subtree, der Subtree herausgefunden werden, in dem die Selektion beginnt. (index0>=anzahl
     * sichtbarer Elemente im Subtree) Das Ende der Selektion kann auf gleiche Weise hereausgefunden werden
     * Dazwischenliegenede Subtrees sind komplett selektiert.
     */
    @Override
    public void addSelectionInterval(final int index0, final int index1) {
        if (useSlideableTreeView) {
            if (index1 < index0) {
                return;
            } else {
                for (int i = index0; i <= index1; i++) {
                    final TreePath path = getPathForRow(i);
                    addSelectionPath(path);
                }
            }
        }
        super.addSelectionInterval(index0, index1);
    }

    @Override
    public void cancelEditing() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.cancelEditing();
                }
            }
        } else {
            super.cancelEditing();
        }
    }

    @Override
    public void clearSelection() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    panes.get(trees.indexOf(t)).setSelected(false);
                    t.clearSelection();
                }
            }
        } else {
            super.clearSelection();
        }
    }

    @Override
    protected void clearToggledPaths() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.clearToggledPaths();
                }
            }
        }
        super.clearToggledPaths();
    }

    @Override
    public void expandRow(final int row) {
        if (useSlideableTreeView) {
            if ((row < 0) || (row > getRowCount())) {
                return;
            }
            if ((trees != null) && (panes != null)) {
                final SlideableSubTree t = getSubTreeForRow(row);
                final int index = trees.indexOf(t);
                final SubTreePane pane = panes.get(index);
                pane.setCollapsed(false);
                final TreePath subTreePath = getPathForSubTree(getPathForRow(row));
                t.expandPath(subTreePath);
            }
        } else {
            super.expandRow(row);
        }
    }

    @Override
    public Enumeration<TreePath> getExpandedDescendants(final TreePath parent) {
        if (useSlideableTreeView) {
            final Vector<TreePath> paths = new Vector<TreePath>();
            final Object lastPathElement = parent.getLastPathComponent();
            final Object origRoot = this.getModel().getRoot();

            if (trees != null) {
                // falls der durch parent representierte knoten der Rootnode des
                // original baum ist, alle SubTreeRoots falls das JXTaskPane aufgeklappt zurueckgeben
                if (lastPathElement.equals(origRoot)) {
                    for (final SlideableSubTree t : trees) {
                        final SubTreePane pane = panes.get(trees.indexOf(t));
                        if (!(pane.isCollapsed())) {
                            final Object subTreeRoot = t.getModel().getRoot();
                            paths.add(getPathforOriginalTree(new TreePath(subTreeRoot)));
                        }
                    }
                } // sonst lediglich fuer den Subtree der den durch parent representierten
                // knoten enthaelt
                else {
                    final SlideableSubTree subTree = getSubTreeForPath(parent);
                    final Enumeration<TreePath> newPaths = subTree.getExpandedDescendants(getPathForSubTree(parent));
                    if (newPaths != null) {
                        while (newPaths.hasMoreElements()) {
                            paths.add(getPathforOriginalTree(newPaths.nextElement()));
                        }
                    }
                }
            }
            return paths.elements();
        } else {
            return super.getExpandedDescendants(parent);
        }
    }

    @Override
    public TreePath getEditingPath() {
        if (useSlideableTreeView) {
            for (final JTree t : trees) {
                final TreePath path = t.getEditingPath();
                if (path != null) {
                    return getPathforOriginalTree(path);
                }
            }
        }
        return super.getEditingPath();
    }

    @Override
    public TreePath getPathForLocation(final int x, final int y) {
        if (useSlideableTreeView) {
            final TreePath closestPath = getClosestPathForLocation(x, y);
            // closest Path ist null wenn koordinaten nicht innerhalb des SlideableTree liegen..
            if (closestPath == null) {
                return null;
            } else {
                final SubTreePane pane = panes.get(trees.indexOf(getSubTreeForPath(closestPath)));
                final int paneX = pane.getX();
                final int paneY = pane.getY();
                final int titleBarHeight = (pane.getHeight() - pane.getContentPane().getHeight());
                // Sonderfall closest ist Rootnode eines subTrees
                if (closestPath.getPathCount() == 2) {
                    if ((y >= paneY) && (y <= (paneY + titleBarHeight))) {
                        return closestPath;
                    }
                }

                final int treeX = getSubTreeForPath(closestPath).getX();
                final int treeY = getSubTreeForPath(closestPath).getY();
                final int newX = x - paneX - treeX;
                final int newY = y - paneY - treeY - titleBarHeight;

                final Rectangle r = getPathBounds(closestPath);
                double recX = 0;
                double recY = 0;
                double recWidth = 0;
                double recHeight = 0;
                if (r != null) {
                    recX = r.getX();
                    recY = r.getY();
                    recWidth = r.getWidth();
                    recHeight = r.getHeight();
                }
                // liegen Koordinaten innerhalb des closestPath?
                if ((newX >= recX) && (newX <= (recX + recWidth)) && (newY >= recY) && (newY <= (recY + recHeight))) {
                    return closestPath;
                }
                return null;
            }
        } else {
            return super.getPathForLocation(x, y);
        }
    }

    @Override
    public TreePath getClosestPathForLocation(final int x, final int y) {
        if (useSlideableTreeView) {
            if (logger.isDebugEnabled()) {
                logger.debug("VisibleRect: " + this.getVisibleRect());
                logger.debug("Tree X/Y von: " + this.getX() + "/" + this.getY());
                logger.debug("Tree X/Y bis: " + this.getX() + this.getWidth() + "/" + this.getY() + this.getHeight());
            }
            final Component c = container.getComponentAt(x, y);
            if (c instanceof SubTreePane) {
                final SubTreePane pane = (SubTreePane)c;
                final SlideableSubTree t = trees.get(panes.indexOf(pane));
                final int titleBarHeight = (pane.getHeight() - pane.getContentPane().getHeight());
                if (y <= (titleBarHeight + pane.getY())) {
                    // der rootNode des SubTrees ist der closestNode
                    return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
                } else if ((y > (titleBarHeight + pane.getY())) && (y < (pane.getY() + titleBarHeight + t.getY()))) {
                    final int distanceToTitle = Math.abs(y - (titleBarHeight + pane.getY()));
                    final int distanceToTree = Math.abs(y - (pane.getY() + titleBarHeight + t.getY()));
                    if (distanceToTitle < distanceToTree) {
                        return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
                    }
                }
                final int newY = y - pane.getY() - t.getY() - titleBarHeight;
                final int newX = x - pane.getX() - t.getX();
                TreePath subTreePath = t.getClosestPathForLocation(x, y);
                // ist null falls kein Knoten sichtbar ist, z.b wenn der rootNode des
                // subTree ein blatt ist
                if (subTreePath == null) {
                    subTreePath = new TreePath(t.getModel().getRoot());
                }
                return getPathforOriginalTree(subTreePath);

                // y liegt zwischen titlebar und tree, geringsten abstand bestimmen
            } else if (c instanceof JXTaskPaneContainer) {
                // falls berechne den nahestehendsten JXTaskpane..
                SubTreePane closest = null;
                boolean lastComponent = false;
                for (final SubTreePane p : panes) {
                    final int paneY = p.getY();
                    // liegt y vor p?
                    if (y < paneY) {
                        if (panes.indexOf(p) == 0) {
                            closest = p;
                            lastComponent = false;
                        } else {
                            final int distance = Math.abs(y - paneY);
                            final SubTreePane predecessor = panes.get(panes.indexOf(p) - 1);
                            final int distanceToPredecessor = Math.abs(y
                                            - (predecessor.getY() + predecessor.getHeight()));
                            if (distance <= distanceToPredecessor) {
                                closest = p;
                                lastComponent = false;
                            } else {
                                closest = predecessor;
                                lastComponent = true;
                            }
                        }
                    }
                }
                if (closest == null) {
                    closest = panes.get(panes.size() - 1);
                    lastComponent = true;
                }
                final SlideableSubTree t = trees.get(panes.indexOf(closest));
                if (lastComponent) {
                    final int newY = closest.getY() + closest.getHeight();
                    TreePath subTreePath = t.getClosestPathForLocation(0, newY);
                    if (subTreePath == null) {
                        subTreePath = new TreePath(t.getModel().getRoot());
                    }
                    return getPathforOriginalTree(subTreePath);
                }
                return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
            } // falls x/y nicht innerhalb des container liegen return null
            else {
                return null;
            }
        } else {
            return super.getClosestPathForLocation(x, y);
        }
    }

    @Override
    protected Enumeration<TreePath> getDescendantToggledPaths(final TreePath parent) {
        if (useSlideableTreeView) {
            final Vector<TreePath> toggledPaths = new Vector<TreePath>();
            final Object lastPathComponent = parent.getLastPathComponent();
            final Object parentRoot = this.getModel().getRoot();

            if (trees != null) {
                if (lastPathComponent.equals(parentRoot)) {
                    // falls parent gleich dem RootNode des ParentTree ist
                    // was tun ??
                } else {
                    final SlideableSubTree t = getSubTreeForPath(parent);
                    final TreePath subTreePath = getPathForSubTree(parent);
                    final Enumeration toggledSubPaths = t.getDescendantToggledPaths(subTreePath);

                    while (toggledSubPaths.hasMoreElements()) {
                        final TreePath originPath = getPathforOriginalTree((TreePath)toggledSubPaths.nextElement());
                        toggledPaths.add(getPathforOriginalTree(originPath));
                    }
                }
                return toggledPaths.elements();
            }
        }
        return super.getDescendantToggledPaths(parent);
    }

    @Override
    public int getMaxSelectionRow() {
        if (useSlideableTreeView) {
            if (trees != null) {
                int offset = 0;
                final int maxSelection = 0;
                int indexOfTree = 0;
                /*
                 * letzen Baum mit selektion herausfinden
                 */
                for (int i = trees.size() - 1; i
                            >= 0; i--) {
                    final SlideableSubTree t = trees.get(i);
                    if (maxSelection != -1) {
                        indexOfTree = i;
                        break;
                    }
                }
                if (maxSelection == -1) {
                    return maxSelection;
                } else {
                    /*
                     * Offset berechnen
                     */
                    for (int i = 0; i
                                < indexOfTree; i++) {
                        offset += trees.get(i).getRowCount() + 1;
                    }
                    return maxSelection + offset;
                }
            }
        }
        return super.getMaxSelectionRow();
    }

    @Override
    public int getMinSelectionRow() {
        if (useSlideableTreeView) {
            if (trees != null) {
                int offset = 0;
                int minSelection = 0;
                final int indexOfTree = 0;

                for (final SlideableSubTree t : trees) {
                    minSelection = t.getMinSelectionRow();
                    if (minSelection == -1) {
                        offset += t.getRowCount() + 1;
                    } else {
                        break;
                    }
                }
                if (minSelection == -1) {
                    return minSelection;
                } else {
                    return minSelection + offset;
                }
            }
        }
        return super.getMinSelectionRow();
    }

    @Override
    public Rectangle getPathBounds(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);
            // wenn durch path representierter Knoten die Wurzel des Originalbaum ist

            if (t == null) {
                return super.getPathBounds(path);
            } else {
                final Rectangle rec = t.getPathBounds(getPathForSubTree(path));
                final SubTreePane pane = panes.get(trees.indexOf(t));
                rec.setLocation((int)rec.getX() + pane.getX(), (int)rec.getY() + pane.getY());
                return rec;
            }
        } else {
            return super.getPathBounds(path);
        }
    }

    @Override
    public int getRowCount() {
        if (useSlideableTreeView) {
            int sum = 0;

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    // Anzahl der sichtbaren Zeilen + 1 fuer den jeweiligen Root des SubTree
                    sum += t.getRowCount();

                    if (!t.isRootVisible()) {
                        sum++;
                    }
                }
            }
            return sum;
        } else {
            return super.getRowCount();
        }
    }

    @Override
    public TreePath getSelectionPath() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    /*final SubTreePane pane = panes.get(trees.indexOf(t));
                     * if (pane.isSelected()) { return getPathforOriginalTree(new TreePath(t.getModel().getRoot())); }
                     */
                    final TreePath firstSelection = t.getSelectionPath();

                    if (firstSelection != null) {
                        return getPathforOriginalTree(firstSelection);
                    }
                }
            }
            return null;
        } else {
            return super.getSelectionPath();
        }
    }

    @Override
    public TreePath[] getSelectionPaths() {
        if (useSlideableTreeView) {
            final ArrayList<TreePath> paths = new ArrayList<TreePath>();

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    final TreePath[] selections = t.getSelectionPaths();

                    if (selections != null) {
                        for (int i = 0; i
                                    < selections.length; i++) {
                            paths.add(getPathforOriginalTree(selections[i]));
                        }
                    }
                }
                if (paths.isEmpty()) {
                    return null;
                }
                final TreePath[] path = new TreePath[paths.size()];
                paths.toArray(path);

                return path;
            }
            return null;
        } else {
            return super.getSelectionPaths();
        }
    }

    @Override
    public int getSelectionCount() {
        if (useSlideableTreeView) {
            int sum = 0;

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    sum += t.getSelectionCount();
                }
                return sum;
            }
        }
        return super.getSelectionCount();
    }

    @Override
    public TreePath getPathForRow(final int row) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForRow(row);
            final int subTreeRow = getRowForSubTree(row);

            if (t != null) {
                if ((subTreeRow < 0) && !t.isRootVisible()) {
                    return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
                } else {
                    final TreePath tmp = t.getPathForRow(subTreeRow);
                    final TreePath path = getPathforOriginalTree(tmp);

                    return path;
                }
            }
            return null;
        } else {
            return getPathForRow(row);
        }
    }

    @Override
    public int getRowForPath(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree subTree = getSubTreeForPath(path);
            final TreePath subPath = getPathForSubTree(path);
            int offset = 0;
            int row = -1;

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    if (t.equals(subTree)) {
                        break;
                    } else {
                        offset += t.getRowCount() + 1;
                    }
                }
                row = subTree.getRowForPath(subPath) + offset;
            }
            return row;
        } else {
            return super.getRowForPath(path);
        }
    }

    @Override
    public TreePath getNextMatch(final String prefix, final int startingRow, final Bias bias) {
        return super.getNextMatch(prefix, startingRow, bias);
    }

    @Override
    protected TreePath[] getPathBetweenRows(final int index0, final int index1) {
        if (useSlideableTreeView) {
            final ArrayList<TreePath> list = new ArrayList<TreePath>();

            for (int i = index0 + 1; i
                        <= index1; i++) {
                list.add(getPathForRow(i));
            }
            final TreePath[] finalPaths = new TreePath[list.size()];

            for (int i = 0; i
                        < finalPaths.length; i++) {
                finalPaths[i] = list.get(i);
            }
            return finalPaths;
        } else {
            return getPathBetweenRows(index0, index1);
        }
    }

    @Override
    public boolean hasBeenExpanded(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);
            // keinen SubTree gefunden, Knoten ist also die Wurzel oder nicht enthalten

            if (t == null) {
                return super.hasBeenExpanded(path);
            } else {
                return t.hasBeenExpanded(getPathForSubTree(path));
            }
        } else {
            return super.hasBeenExpanded(path);
        }
    }

    @Override
    public boolean isEditing() {
        if (useSlideableTreeView) {
            boolean isEditing = false;

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    if (t.isEditing()) {
                        isEditing = true;
                    }
                }
            }
            return isEditing;
        } else {
            return super.isEditable();
        }
    }

    @Override
    public boolean isExpanded(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);

            if (t == null) {
                return super.isExpanded(path);
            } else {
                return t.isExpanded(getPathForSubTree(path));
            }
        } else {
            return super.isExpanded(path);
        }
    }

    @Override
    public boolean isExpanded(final int row) {
        if (useSlideableTreeView) {
            final TreePath path = getPathForRow(row);

            return isExpanded(path);
        } else {
            return super.isExpanded(row);
        }
    }

    @Override
    public boolean isPathSelected(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);

            if (t == null) {
                // was passiert falls der Knoten die wurzel ist?
                return super.isPathSelected(path);
            } else {
                return t.isPathSelected(getPathForSubTree(path));
            }
        } else {
            return super.isPathSelected(path);
        }
    }

    @Override
    public boolean isRowSelected(final int row) {
        if (useSlideableTreeView) {
            final TreePath path = getPathForRow(row);
            if (path == null) {
                return false;
            }
            final boolean isSelected = isPathSelected(path);

            return isSelected;
        } else {
            return super.isRowSelected(row);
        }
    }

    @Override
    public boolean isSelectionEmpty() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    if (!(t.isSelectionEmpty())) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return super.isSelectionEmpty();
        }
    }

    @Override
    public boolean isVisible(final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);

            if (t == null) {
                return super.isVisible(path);
            } else {
                return t.isVisible(getPathForSubTree(path));
            }
        } else {
            return super.isVisible();
        }
    }

    @Override
    public void makeVisible(final TreePath path) {
        if (useSlideableTreeView) {
            final JTree t = getSubTreeForPath(path);

            if (t == null) {
                super.makeVisible(path);
            } else {
                t.makeVisible(getPathForSubTree(path));
            }
        } else {
            super.makeVisible(path);
        }
    }

    @Override
    public void removeSelectionInterval(final int index0, final int index1) {
        if (useSlideableTreeView) {
            if (trees != null) {
                if (index1 < index0) {
                    return;
                } else {
                    for (int i = index0; i
                                <= index1; i++) {
                        final TreePath path = getPathForRow(i);
                        removeSelectionPath(path);
                    }
                }
                super.removeSelectionInterval(index0, index1);
            }
        } else {
            super.removeSelectionInterval(index0, index1);
        }
    }

    @Override
    public void removeSelectionPath(final TreePath path) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final SlideableSubTree subTree = getSubTreeForPath(path);
                subTree.removeSelectionPath(getPathForSubTree(path));
            }
        }
        super.removeSelectionPath(path);
    }

    @Override
    public void removeSelectionPaths(final TreePath[] paths) {
        if (useSlideableTreeView) {
            for (int i = 0; i
                        < paths.length; i++) {
                removeSelectionPath(paths[i]);
            }
        }
        super.removeSelectionPaths(paths);
    }

    @Override
    public void removeSelectionRow(final int row) {
        if (useSlideableTreeView) {
            final TreePath path = getPathForRow(row);
            removeSelectionPath(
                path);
        }
        super.removeSelectionRow(row);
    }

    @Override
    public void removeSelectionRows(final int[] rows) {
        if (useSlideableTreeView) {
            for (int i = 0; i
                        < rows.length; i++) {
                final TreePath path = getPathForRow(i);
                removeSelectionPath(path);
            }
        }
        super.removeSelectionRows(rows);
    }

    @Override
    protected boolean removeDescendantSelectedPaths(final TreePath path, final boolean includePath) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);
            final TreePath subTreePath = getPathForSubTree(path);

            if (trees != null) {
                return t.removeDescendantSelectedPaths(subTreePath, includePath);
            } else {
                return super.removeDescendantSelectedPaths(path, includePath);
            }
        } else {
            return super.removeDescendantSelectedPaths(path, includePath);
        }
    }

    @Override
    protected void removeDescendantToggledPaths(final Enumeration<TreePath> toRemove) {
        if (useSlideableTreeView) {
            if (trees != null) {
                while (toRemove.hasMoreElements()) {
                    final TreePath path = toRemove.nextElement();
                    final SlideableSubTree t = getSubTreeForPath(path);
                    final Vector<TreePath> subToRemove = new Vector<TreePath>();
                    subToRemove.add(getPathForSubTree(path));
                    t.removeDescendantToggledPaths(subToRemove.elements());
                }
            } else {
                super.removeDescendantToggledPaths(toRemove);
            }
        } else {
            super.removeDescendantToggledPaths(toRemove);
        }
    }

    @Override
    public int[] getSelectionRows() {
        if (useSlideableTreeView) {
            final int[][] result = new int[trees.size()][];
            int offset = 0;
            int count = 0;

            if (trees != null) {
                // getSelectionRows fuer jeden Subtree
                for (final SlideableSubTree t : trees) {
                    result[trees.indexOf(t)] = t.getSelectionRows();
                    count += result[trees.indexOf(t)].length;
                    // Offset aufaddieren fuer korrekte Inidzes
                    for (int i = 0; i
                                < result[trees.indexOf(t)].length; i++) {
                        result[trees.indexOf(t)][i] += offset;
                    }
                    offset += t.getRowCount() + 1;
                }
                final int[] selectionRows = new int[count];
                // Ergebnisse zusammenfassen

                for (int i = 0; i
                            < selectionRows.length; i++) {
                    for (int j = 0; j
                                < result[i].length; j++) {
                        selectionRows[i] = result[i][j];
                    }
                }
                return selectionRows;
            }
        }
        return super.getSelectionRows();
    }

    @Override
    public void scrollPathToVisible(final TreePath path) {
        if (useSlideableTreeView) {
            if (trees != null) {
//                final TreePath selectionPath = path;
                if ((path == null) || (path.getPathCount() <= 2)) {
                    return;
                }
                final SlideableSubTree t = getSubTreeForPath(path);

                if (t != null) {
                    // path ist nicht die wurzel des baums, und Subroot ist vorhanden
                    final SubTreePane pane = panes.get(trees.indexOf(t));

                    if (!pane.isSelected() && pane.isCollapsed()) {
                        final PropertyChangeListener propChangedListener = new PropertyChangeListener() {

                                @Override
                                public void propertyChange(final PropertyChangeEvent e) {
                                    if (e.getNewValue().equals("expanded")) {
                                        SwingUtilities.invokeLater(new Runnable() {

                                                @Override
                                                public void run() {
                                                    final TreePath tmpPath = path;
                                                    final Rectangle r = SlideableTree.this.getPathBounds(tmpPath);
                                                    final JViewport vp = containerScrollPane.getViewport();
                                                    if ((r != null)
                                                                && ((r.getY() + pane.getTitleBarHeight())
                                                                    > vp.getHeight())) {
                                                        final double y = r.getY() + pane.getTitleBarHeight();
                                                        vp.setViewPosition(new Point(0, (int)y));
                                                    }
                                                }
                                            });

                                        pane.removePropertyChangeListener(this);
                                    }
                                }
                            };

                        pane.addPropertyChangeListener(JXCollapsiblePane.ANIMATION_STATE_KEY, propChangedListener);
                        pane.setCollapsed(false);
                        return;
                    }
                    final Rectangle r = SlideableTree.this.getPathBounds(path);
                    final JViewport vp = containerScrollPane.getViewport();
                    if ((r != null) && ((r.getY() + pane.getTitleBarHeight()) > vp.getHeight())) {
                        final double y = r.getY() + pane.getTitleBarHeight();
                        vp.setViewPosition(new Point(0, (int)y));
                    }

                    return;
                }
            }
        }
        super.scrollPathToVisible(path);
    }

    @Override
    public void scrollRowToVisible(
            final int row) {
        if (useSlideableTreeView) {
            final TreePath path = getPathForRow(row);
            scrollPathToVisible(
                path);
        } else {
            super.scrollRowToVisible(row);
        }
    }

    @Override
    public void setAnchorSelectionPath(final TreePath newPath) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final SlideableSubTree t = getSubTreeForPath(newPath);
                final TreePath subTreePath = getPathForSubTree(newPath);

                if (t != null) {
                    t.setAnchorSelectionPath(subTreePath);
                }
            }
        }
        super.setAnchorSelectionPath(newPath);
    }

    @Override
    public void setCellEditor(
            final TreeCellEditor cellEditor) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setCellEditor(cellEditor);
                }
            }
        }
        super.setCellEditor(cellEditor);
    }

    @Override
    public void setCellRenderer(
            final TreeCellRenderer x) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setCellRenderer(x);
                    final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)t.getModel().getRoot();
                    final SubTreePane pane = panes.get(trees.indexOf(t));
                    final DefaultTreeCellRenderer renderer;

                    if (x instanceof DefaultTreeCellRenderer) {
                        renderer = (DefaultTreeCellRenderer)t.getCellRenderer();
                        final JLabel l = (JLabel)renderer.getTreeCellRendererComponent(
                                this,
                                rootNode,
                                false,
                                !(pane.isCollapsed()),
                                rootNode.isLeaf(),
                                0,
                                false);
                        pane.setIcon(l.getIcon());
                    }
                }
            }
        }
        super.setCellRenderer(x);
    }

    @Override
    public void setDragEnabled(
            final boolean b) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setDragEnabled(b);
                }
            }
        }
        super.setDragEnabled(b);
    }

    @Override
    public void setEditable(
            final boolean flag) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setEditable(flag);
                }
            }
        }
        super.setEditable(flag);
    }

    @Override
    public void setExpandsSelectedPaths(
            final boolean newValue) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setExpandsSelectedPaths(newValue);
                }
            }
        }
        super.setExpandsSelectedPaths(newValue);
    }

    @Override
    protected void setExpandedState(
            final TreePath path,
            final boolean state) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final SlideableSubTree t = getSubTreeForPath(path);
                final TreePath subTreePath = getPathForSubTree(path);
                t.setExpandedState(subTreePath, state);
            }
        } else {
            super.setExpandedState(path, state);
        }
    }

    @Override
    public void setInvokesStopCellEditing(
            final boolean newValue) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setInvokesStopCellEditing(newValue);
                }
            }
        }
        super.setInvokesStopCellEditing(newValue);
    }

    @Override
    public void setLargeModel(
            final boolean newValue) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setLargeModel(newValue);
                }
                super.setLargeModel(newValue);
            }
        } else {
            super.setLargeModel(newValue);
        }
    }

    @Override
    public void setLeadSelectionPath(
            final TreePath newPath) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setLeadSelectionPath(newPath);
                }
            }
        }
        super.setLeadSelectionPath(newPath);
    }

    @Override
    public void setModel(
            final TreeModel newModel) {
        if (useSlideableTreeView) {
            final TreeModel oldModel = this.getModel();
            treeModel = newModel;
            firePropertyChange(
                TREE_MODEL_PROPERTY,
                oldModel,
                newModel);

            if (trees != null) {
                createSubTrees(newModel);
                flushTreeContainer();
                addToTreeContainer(
                    panes);
            }
        } else {
            super.setModel(newModel);
        }
    }

    @Override
    public void setRootVisible(
            final boolean rootVisible) {
        if (useSlideableTreeView) {
            for (final SlideableSubTree t : trees) {
                t.setRootVisible(rootVisible);
            }
        }
        super.setRootVisible(rootVisible);
    }

    @Override
    public void setRowHeight(final int rowHeight) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setRowHeight(rowHeight);
                }
            } else {
                super.setRowHeight(rowHeight);
            }
        } else {
            super.setRowHeight(rowHeight);
        }
    }

    @Override
    public void setScrollsOnExpand(
            final boolean newValue) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setScrollsOnExpand(newValue);
                }
            }
        } else {
            super.setScrollsOnExpand(newValue);
        }
    }

    @Override
    public void setSelectionInterval(
            final int index0,
            final int index1) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final ArrayList<TreePath> pathList = new ArrayList<TreePath>();

                if (index1 < index0) {
                    return;
                } else {
                    for (int i = index0; i
                                <= index1; i++) {
                        final TreePath path = getPathForRow(i);
                        pathList.add(path);
                    }
                    final TreePath[] finalPaths = new TreePath[pathList.size()];

                    for (int i = 0; i
                                < finalPaths.length; i++) {
                        finalPaths[i] = pathList.get(i);
                    }
                    setSelectionPaths(finalPaths);
                }
            } else {
                super.setSelectionInterval(index0, index1);
            }
        } else {
            super.setSelectionInterval(index0, index1);
        }
    }

    @Override
    public void setSelectionModel(
            final TreeSelectionModel selectionModel) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setSelectionModel(selectionModel);
                }
            } else {
                super.setSelectionModel(selectionModel);
            }
        } else {
            super.setSelectionModel(selectionModel);
        }
    }

    @Override
    public void setSelectionPath(
            final TreePath path) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final SlideableSubTree t = getSubTreeForPath(path);

                for (final JTree tmpTree : trees) {
                    if (t.equals(tmpTree)) {
                        continue;
                    } else {
                        tmpTree.clearSelection();
                    }
                }
                if (t == null) {
                    // was wenn der Root knoten selektiert werden soll
                } else {
                    t.setSelectionPath(getPathForSubTree(path));
                }
            }
        } else {
            super.setSelectionPath(path);
        }
    }

    @Override
    public void setSelectionPaths(
            final TreePath[] paths) {
        if (useSlideableTreeView) {
            for (int i = 0; i
                        < paths.length; i++) {
                setSelectionPath(paths[i]);
            }
        } else {
            super.setSelectionPaths(paths);
        }
    }

    @Override
    public void setSelectionRow(
            final int row) {
        if (useSlideableTreeView) {
            final TreePath path = getPathForRow(row);
            final SlideableSubTree subTree = getSubTreeForPath(path);
            subTree.setSelectionPath(getPathForSubTree(path));
        } else {
            super.setSelectionRow(row);
        }
    }

    @Override
    public void setSelectionRows(final int[] rows) {
        if (useSlideableTreeView) {
            for (int i = 0; i
                        < rows.length; i++) {
                final TreePath path = getPathForRow(rows[i]);
                final SlideableSubTree subTree = getSubTreeForPath(path);
                subTree.setSelectionPath(getPathForSubTree(path));
            }
        } else {
            super.setSelectionRows(rows);
        }
    }

    @Override
    public void setShowsRootHandles(
            final boolean newValue) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setShowsRootHandles(newValue);
                }
            } else {
                super.setShowsRootHandles(newValue);
            }
        } else {
            super.setShowsRootHandles(newValue);
        }
    }

    @Override
    public void setToggleClickCount(
            final int clickCount) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setToggleClickCount(clickCount);
                }
            } else {
                super.setToggleClickCount(clickCount);
            }
        } else {
            super.setToggleClickCount(clickCount);
        }
    }

    @Override
    public void setUI(
            final TreeUI ui) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setUI(ui);
                }
            } else {
                super.setUI(ui);
            }
        } else {
            super.setUI(ui);
        }
    }

    @Override
    public void setVisibleRowCount(
            final int newCount) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.setVisibleRowCount(newCount);
                }
            }
        }
        super.setVisibleRowCount(newCount);
    }

    @Override
    public void startEditingAtPath(
            final TreePath path) {
        if (useSlideableTreeView) {
            final SlideableSubTree t = getSubTreeForPath(path);

            if (t == null) {
                super.startEditingAtPath(path);
            } else {
                t.startEditingAtPath(getPathForSubTree(path));
            }
        } else {
            super.startEditingAtPath(path);
        }
    }

    @Override
    public boolean stopEditing() {
        if (useSlideableTreeView) {
            boolean stopped = false;

            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    if (t.stopEditing()) {
                        stopped = true;
                    }
                }
            } else {
                return super.stopEditing();
            }
            return stopped;
        } else {
            return super.stopEditing();
        }
    }

    @Override
    public void updateUI() {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final SlideableSubTree t : trees) {
                    t.updateUI();
                }
            }
        }
        super.updateUI();
    }

    /**
     * creates for each child of the root node a JXTaskPane with an JTree and add it to the container.
     *
     * @param  model  the TreeModel which contains the root node
     */
    private void createSubTrees(final TreeModel model) {
        final TreeModelListener listener = createTreeModelListener();
        treeModel.addTreeModelListener(listener);
        flushTreeContainer();
        trees = new ArrayList<SlideableSubTree>();
        panes = new ArrayList<SubTreePane>();
        final Object root = this.getModel().getRoot();
        final int childCount = this.getModel().getChildCount(root);

        for (int i = 0; i
                    < childCount; i++) {
            final Object child = model.getChild(root, i);
            createTreeNode(
                child);
            addToTreeContainer(
                panes);
        }
    }

    /**
     * creates the JXTaskPane and the SlideableSubTree for a tree node.
     *
     * @param  node  the root fo the SlideableSubTree
     */
    private void createTreeNode(final Object node) {
        final TreeNode newRootNode = new DefaultMutableTreeNode(node);
        // use specialSelection as default...
        final SlideableSubTree subTree = new SlideableSubTree(newRootNode, true);
        final DelegatingModel modelDelegate = new DelegatingModel(node, this.getModel());
        subTree.setModel(modelDelegate);
        subTree.setEditable(false);
        subTree.setRootVisible(false);
        subTree.addTreeExpansionListener(this);
        subTree.addTreeSelectionListener(this);
        subTree.addTreeWillExpandListener(this);
        subTree.setBorder(new EmptyBorder(1, 1, 1, 1));
        trees.add(subTree);
        final SubTreePane tmpPane = new SubTreePane();
        tmpPane.setCollapsed(true);
        final DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode)subTree.getModel().getRoot();
        final TreeCellRenderer treeCellRenderer = subTree.getCellRenderer();
        final JLabel l = (JLabel)treeCellRenderer.getTreeCellRendererComponent(
                SlideableTree.this,
                (DefaultMutableTreeNode)subTree.getModel().getRoot(),
                false,
                (tmpPane.isCollapsed()),
                false,
                0,
                false);
        tmpPane.setIcon(l.getIcon());

        final Border border = BorderFactory.createLineBorder(new Color(234, 234, 234));

        ((JComponent)tmpPane.getContentPane()).setBorder(border);
        tmpPane.getContentPane().setBackground(Color.white);
        tmpPane.setTitle(newRootNode.toString());
        tmpPane.addMouseListener(new ClickAndSelectListener());
        // ((JComponent)tmpPane.getContentPane()).setBorder(new EmptyBorder(1, 16, 1, 1));
        tmpPane.add(subTree);

        panes.add(tmpPane);
    }

    /**
     * flushs the container which contains the SlideableSubTrees.
     */
    private void flushTreeContainer() {
        if (container != null) {
            container.removeAll();
        }
    }

    /**
     * auxiliary method to fill the container which contains the lideableSubTrees.
     *
     * @param  list  a list, that contains the panes
     */
    private void addToTreeContainer(final ArrayList<SubTreePane> list) {
        for (final SubTreePane p : list) {
            p.setCollapsed(true);
//            p.setSelected(false);
            container.add(p);
        }
    }

    /**
     * auxiliary method, to locate the SlideableSubTree that contains the TreeNode repesents by path.
     *
     * @param   path  a TreePath which represents a TreeNode
     *
     * @return  returns the SlideableSubTree which contains the TreeNode or null if there is no SubTree
     */
    private SlideableSubTree getSubTreeForPath(final TreePath path) {
        if (path == null) {
            return null;
        }
        final Object pathRoot = path.getPathComponent(0);
        // Der Pfad entaehlt nur einen Knoten, also die Wurzel
        if (path.getPathCount() <= 1) {
            return null;
        } else {
            final Object pathSubRoot = path.getPathComponent(1);
            if (trees != null) {
                for (final JTree t : trees) {
                    final Object subTreeRoot = t.getModel().getRoot();

                    if (pathSubRoot.equals(subTreeRoot)) {
                        return (SlideableSubTree)t;
                    }
                }
            }
        }
        return null;
    }

    /**
     * auxiliary method that turns a path of a SlideableSubTree to a path of the original tree.
     *
     * @param   subTreePath  a path of a SlideableSubTree
     *
     * @return  the TreePath for the original Tree
     */
    protected TreePath getPathforOriginalTree(final TreePath subTreePath) {
        final Object pathRoot = subTreePath.getPathComponent(0);
        final Object origRoot = this.getModel().getRoot();
        // falls der Methode bereits ein pfad des original Baum uebergeben wird, diesen einfach zurueckgeben

        if (pathRoot.equals(origRoot)) {
            return subTreePath;
        }
        final Object[] oldPath = subTreePath.getPath();
        final Object[] newPath = new Object[oldPath.length + 1];
        newPath[

            0] = origRoot;
        System.arraycopy(oldPath, 0, newPath, 1, oldPath.length);

        return new TreePath(newPath);
    }

    /**
     * auxiliary method which turns a path of the original tree to a path of the SlideableSubTree.
     *
     * @param   originPath  a path of the original Tree
     *
     * @return  returns the path for the SlideableSubTree or null if originPath represents the root of the original tree
     */
    private TreePath getPathForSubTree(final TreePath originPath) {
        final Object pathRoot = originPath.getPathComponent(0);
        final Object originRoot = this.getModel().getRoot();

        // wenn path nicht zum originalbaum gehoert

        if (!pathRoot.equals(originRoot)) {
            return null;
        } // der pfad enthaelt nur den Root knoten des Originalbaums
        else if (originPath.getPathCount() <= 1) {
            return null;
        } else {
            final Object[] oldPath = originPath.getPath();
            final Object[] newPath = new Object[oldPath.length - 1];
            System.arraycopy(oldPath, 1, newPath, 0, newPath.length);

            return new TreePath(newPath);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   row  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private SlideableSubTree getSubTreeForRow(final int row) {
        int sum = 0;

        if (trees != null) {
            for (final SlideableSubTree t : trees) {
                sum += t.getRowCount();

                if (!t.isRootVisible()) {
                    sum += 1;
                }
                if (sum > row) {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   originRow  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getRowForSubTree(final int originRow) {
        final SlideableSubTree tmpTree = getSubTreeForRow(originRow);

        int offset = 1;

        if (tmpTree != null) {
            for (final SlideableSubTree t : trees) {
                if (t.equals(tmpTree)) {
                    break;
                } else {
                    offset += t.getRowCount();

                    if (!t.isRootVisible()) {
                        offset++;
                    }
                }
            }
        }
        return originRow - offset;
    }

    @Override
    public Object getLastSelectedPathComponent() {
        final TreePath path = this.getSelectionPath();

        if (path != null) {
            return path.getLastPathComponent();
        }
        return null;
    }

    @Override
    public void addTreeExpansionListener(final TreeExpansionListener tel) {
        super.addTreeExpansionListener(tel);
    }

    @Override
    public void fireTreeExpanded(final TreePath path) {
        if (useSlideableTreeView) {
            final TreeExpansionListener[] listener = this.getTreeExpansionListeners();

            for (int i = 0; i
                        < listener.length; i++) {
                listener[i].treeExpanded(new TreeExpansionEvent(this, path));
            }
            // scrollPathToVisible(path);
        } else {
            super.fireTreeExpanded(path);
        }
    }

    @Override
    public void treeExpanded(final TreeExpansionEvent event) {
        fireTreeExpanded(getPathforOriginalTree(event.getPath()));
    }

    @Override
    public void treeCollapsed(final TreeExpansionEvent event) {
        fireTreeCollapsed(getPathforOriginalTree(event.getPath()));
    }

    /*
     * wird aufgerufen wenn sich die Slektion innerhalb eines Subtrees aendert
     */
    @Override
    public void valueChanged(final TreeSelectionEvent e) {
        if (useSlideableTreeView) {
            if (trees != null) {
                final TreePath path = getPathforOriginalTree(e.getPath());

                if (e.isAddedPath()) {
                    for (final JTree tmpTree : trees) {
                        final SubTreePane pane = panes.get(trees.indexOf(tmpTree));
                        pane.setSelected(false);

                        if (!tmpTree.equals((SlideableSubTree)e.getSource())) {
                            tmpTree.clearSelection();
                        }
                    }
                }
                fireValueChanged(e);
            }
        }
    }

    @Override
    public void treeWillExpand(final TreeExpansionEvent event) throws ExpandVetoException {
        fireTreeWillExpand(getPathforOriginalTree(event.getPath()));
    }

    @Override
    public void treeWillCollapse(final TreeExpansionEvent event) throws ExpandVetoException {
        fireTreeWillCollapse(getPathforOriginalTree(event.getPath()));
    }

    @Override
    public TreeModelListener createTreeModelListener() {
        if (useSlideableTreeView) {
            return new MyTreeModelHandler(this);
        } else {
            return super.createTreeModelListener();
        }
    }

    @Override
    public void addMouseListener(final MouseListener l) {
        if (useSlideableTreeView) {
            if (trees != null) {
                for (final JTree t : trees) {
                    t.addMouseListener(l);
                    panes.get(trees.indexOf(t)).addMouseListener(l);
                }
            }
        } else {
            super.addMouseListener(l);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ArrayList<SlideableSubTree> getTrees() {
        return trees;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public ArrayList<SubTreePane> getPanes() {
        return panes;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class MyTreeModelHandler implements TreeModelListener {

        //~ Instance fields ----------------------------------------------------

        private SlideableTree tree;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new MyTreeModelHandler object.
         */
        public MyTreeModelHandler() {
        }

        /**
         * Creates a new MyTreeModelHandler object.
         *
         * @param  t  DOCUMENT ME!
         */
        public MyTreeModelHandler(final SlideableTree t) {
            tree = t;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public void treeNodesChanged(final TreeModelEvent e) {
            if (useSlideableTreeView) {
                final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
                EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // t.updateUI();
                        }
                    });
                tree.scrollPathToVisible(e.getTreePath());
            }
        }

        @Override
        public void treeNodesInserted(final TreeModelEvent e) {
            if (useSlideableTreeView) {
                final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
                EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // t.updateUI();
                        }
                    });
                tree.scrollPathToVisible(e.getTreePath());
            }
        }

        @Override
        public void treeStructureChanged(final TreeModelEvent e) {
            if (useSlideableTreeView) {
                final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
                if (t == null) {
                    if (tree.getModel().getRoot().equals(e.getTreePath().getLastPathComponent())) {
                        // ein Knoten dirket unterhalb des Root Knoten hat sich geaendert..
                        final TreeNode treeRoot = (DefaultMutableTreeNode)tree.getModel().getRoot();
                        final int childCount = treeRoot.getChildCount();
                        if (childCount < panes.size()) {
                            // nodes were removed...
                            final int treesToRemove = panes.size() - childCount;
                            for (int i = 0; i < treesToRemove; i++) {
                                trees.remove((trees.size() - 1) - i);
                                panes.remove((panes.size() - 1) - i);
                            }
                        } else if (childCount > panes.size()) {
                            // nodes were added
                            final int treesToAdd = childCount - panes.size();
                            for (int i = 0; i < treesToAdd; i++) {
                                createTreeNode(treeRoot.getChildAt((childCount - 1) - i));
                            }
                        }

                        for (int i = 0; i < treeRoot.getChildCount(); i++) {
                            final TreeNode child = treeRoot.getChildAt(i);
                            final SlideableSubTree subTree = trees.get(i);
                            if (subTree != null) {
                                final TreeNode subRoot = (TreeNode)subTree.getModel().getRoot();

                                if (subRoot.equals(child)) {
                                    // this node doesnt changed
                                    continue;
                                } else {
                                    // node has changed, modify pane and subtree
                                    final SubTreePane pane = panes.get(i);
                                    pane.setTitle(child.toString());
                                    pane.removeAll();
                                    final TreeModel newModel = new DelegatingModel(child, tree.getModel());
                                    subTree.setModel(newModel);
                                    pane.add(subTree);
                                }
                            }
                        }
                        tree.flushTreeContainer();
                        tree.addToTreeContainer(panes);
                        tree.updateUI();
                    }
                } else {
                    EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                t.updateUI();
                            }
                        });
                    tree.scrollPathToVisible(e.getTreePath());
                }
            }
        }

        @Override
        public void treeNodesRemoved(final TreeModelEvent e) {
            if (useSlideableTreeView) {
                final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
                EventQueue.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            // t.updateUI();
                        }
                    });
                tree.scrollPathToVisible(e.getTreePath());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    protected class ClickAndSelectListener extends MouseAdapter {

        //~ Methods ------------------------------------------------------------

        @Override
        public void mousePressed(final MouseEvent e) {
            final SubTreePane pane = (SubTreePane)e.getSource();
            // liegen Koordianten innerhalb der Titlebar?
            if ((e.getX() < pane.getWidth()) && (e.getY() < pane.getTitleBarHeight())) {
                {
                    if (!e.isPopupTrigger()) {
                        final SlideableSubTree t = trees.get(panes.indexOf(pane));
                        final TreePath path = SlideableTree.this.getPathforOriginalTree(new TreePath(
                                    t.getModel().getRoot()));
                        // setze RootKnoten des SubTrees als Selektion
                        SlideableTree.this.clearSelection();
                        SlideableTree.this.setSelectionPath(path);

                        // setze icon neu
                        final TreeCellRenderer cellRenderer = t.getCellRenderer();
                        final JLabel l = (JLabel)cellRenderer.getTreeCellRendererComponent(
                                SlideableTree.this,
                                (DefaultMutableTreeNode)t.getModel().getRoot(),
                                false,
                                (pane.isCollapsed()),
                                false,
                                0,
                                false);
                        pane.setIcon(l.getIcon());

                        if (!pane.isCollapsed()) {
                            // pane soll geschlossen werden
                            pane.setSelected(false);
                            SlideableTree.this.fireTreeCollapsed(path);
                        } else {
                            // pane soll geoeffnet werden
                            pane.setSelected(true);
                            SlideableTree.this.fireTreeExpanded(path);
                        }
                    }
                }
            }
        }
    }
}
