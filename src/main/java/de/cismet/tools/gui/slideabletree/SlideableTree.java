package de.cismet.tools.gui.slideabletree;

import de.cismet.tools.gui.StaticSwingTools;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
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
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.JXTaskPaneContainer;
import org.jdesktop.swingx.VerticalLayout;

/**
 *
 * @author dmeiers
 */
public class SlideableTree extends JTree implements TreeExpansionListener, TreeSelectionListener, TreeWillExpandListener {

    private JXTaskPaneContainer container;
    private ArrayList<SlideableSubTree> trees;
    private ArrayList<JXTaskPane> panes;
    private JScrollPane containerScrollPane;

    public SlideableTree() {

        trees = new ArrayList<SlideableSubTree>();
        panes = new ArrayList<JXTaskPane>();
        container = new JXTaskPaneContainer();
        this.setLayout(new BorderLayout());

        /*
         * Erzeuge für alle obersten Knoten einen eigenen SubTree
         * dieser wird einem JXTaskpane zugeordnet
         */
        createSubTrees(this.getModel());
        addToTreeContainer(panes);

        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setGap(2);
        container.setLayout(verticalLayout);
        container.setBorder(new EmptyBorder(0, 0, 0, 0));

        // die Panes mit den SubTrees zu dem Container hinzufügen
        addToTreeContainer(panes);

        containerScrollPane = new JScrollPane(container);
        //für niftyScrollBar
        StaticSwingTools.setNiftyScrollBars(containerScrollPane);
        this.add(containerScrollPane, BorderLayout.CENTER);
    }

    public SlideableTree(TreeModel model) {
        this();
        this.setModel(model);
    }

    public SlideableTree(TreeNode node) {
        this(new DefaultTreeModel(node));
    }

    @Override
    public void addSelectionPath(TreePath path) {
        if (trees != null) {
            final SlideableSubTree t = getSubTreeForPath(path);
            final TreePath subTreePath = getPathForSubTree(path);
            t.addSelectionPath(path);
        }
        //super.addSelectionPath(path);
    }

    @Override
    public void addSelectionPaths(TreePath[] paths) {
        if (trees != null) {
            for (int i = 0; i < paths.length; i++) {
                addSelectionPath(paths[i]);
            }
        }
        super.addSelectionPaths(paths);
    }

    @Override
    public void addSelectionRow(int row) {
        final TreePath path = getPathForRow(row);
        addSelectionPath(path);
        super.addSelectionRow(row);

    }

    @Override
    public void addSelectionRows(int[] rows) {
        for (int i = 0; i < rows.length; i++) {
            addSelectionRow(rows[i]);
        }
        super.addSelectionRows(rows);
    }


    /*
     * das Intervall wird auf die einzelnen SubTrees umgelenkt.
     * Dabei kann durch index0 und die Anzahl der jeweils sichtbaren
     * Elemente im Subtree, der Subtree herausgefunden werden, in dem die
     * Selektion beginnt. (index0>=anzahl sichtbarer Elemente im Subtree)
     * Das Ende der Selektion kann auf gleiche Weise hereausgefunden werden
     * Dazwischenliegenede Subtrees sind komplett selektiert.
     */
    @Override
    public void addSelectionInterval(int index0, int index1) {
        if (index1 < index0) {
            return;
        } else {
            for (int i = index0; i <= index1; i++) {
                final TreePath path = getPathForRow(i);
                addSelectionPath(path);
            }
        }
        super.addSelectionInterval(index0, index1);
    }

    /*
     * für alle subtrees, da pro subtree editiert werden kann
     */
    @Override
    public void cancelEditing() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.cancelEditing();
            }
        }
        //super.cancelEditing();
    }

    /*
     * für alle subtrees
     */
    @Override
    public void clearSelection() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.clearSelection();
            }
        }
        //super.clearSelection();
    }

    @Override
    protected void clearToggledPaths() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.clearToggledPaths();
            }
        }
        super.clearToggledPaths();
    }

    @Override
    public void expandRow(int row) {
        if (row < 0 || row > getRowCount()) {
            return;
        }
        if (trees != null && panes != null) {
            final SlideableSubTree t = getSubTreeForRow(row);
            final int index = trees.indexOf(t);
            final JXTaskPane pane = panes.get(index);
            pane.setCollapsed(false);
            final TreePath subTreePath = getPathForSubTree(getPathForRow(row));
            t.expandPath(subTreePath);
        }
    }

    /*
     * subtree für knoten ausfindig machen, neuen path erstellen,
     * methode weiterleiten, Paths in der enumeration anpassen
     */
    @Override
    public Enumeration<TreePath> getExpandedDescendants(TreePath parent) {

        Vector<TreePath> paths = new Vector<TreePath>();
        final Object lastPathElement = parent.getLastPathComponent();
        final Object origRoot = this.getModel().getRoot();

        if (trees != null) {
            //falls der durch parent representierte knoten der Rootnode des
            //original baum ist, alle SubTreeRoots falls das JXTaskPane aufgeklappt zurückgeben
            if (lastPathElement.equals(origRoot)) {
                for (SlideableSubTree t : trees) {
                    final JXTaskPane pane = panes.get(trees.indexOf(t));
                    if (!(pane.isCollapsed())) {
                        final Object subTreeRoot = t.getModel().getRoot();
                        paths.add(getPathforOriginalTree(new TreePath(subTreeRoot)));
                    }
                }

            } //sonst lediglich für den Subtree der den durch parent representierten
            //knoten enthält
            else {
                final SlideableSubTree subTree = getSubTreeForPath(parent);
                final Enumeration<TreePath> newPaths = subTree.getExpandedDescendants(getPathforOriginalTree(parent));
                while (newPaths.hasMoreElements()) {
                    paths.add(getPathforOriginalTree(newPaths.nextElement()));
                }
            }

        }
        return paths.elements();
    }

    /*
     * Durch alle subtress durch, angepasste pfad zu dem knoten
     * der editiert wird
     */
    @Override
    public TreePath getEditingPath() {
        for (JTree t : trees) {
            final TreePath path = t.getEditingPath();
            if (path != null) {
                return getPathforOriginalTree(path);
            }
        }
        return super.getEditingPath();
    }

    /*
     * laut javadoc:
     * returns null wenn koordinaten nicht innerhalb des Closestpath
     */
    @Override
    public TreePath getPathForLocation(int x, int y) {
        TreePath closestPath = getClosestPathForLocation(x, y);
        //closest Path ist null wenn koordinaten nicht innerhalb des SlideableTree liegen..
        if (closestPath == null) {
            return null;
        } else {
            final JXTaskPane pane = panes.get(trees.indexOf(getSubTreeForPath(closestPath)));
            final int paneX = pane.getX();
            final int paneY = pane.getY();
            final int titleBarHeight = (pane.getHeight() - pane.getContentPane().getHeight());
            //Sonderfall closest ist Rootnode eines subTrees
            if (closestPath.getPathCount() == 2) {
                if ((y >= paneY) && y <= (paneY + titleBarHeight)) {
                    return closestPath;
                }

            }

            final int treeX = getSubTreeForPath(closestPath).getX();
            final int treeY = getSubTreeForPath(closestPath).getY();
            int newX = x - paneX - treeX;
            int newY = y - paneY - treeY - titleBarHeight;

            Rectangle r = getPathBounds(closestPath);
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
            //liegen Koordinaten innerhalb des closestPath?
            if (newX >= recX && newX <= (recX + recWidth) && newY >= recY && newY <= (recY + recHeight)) {
                return closestPath;
            }
            return null;
        }

    }

    /*
     * was ist wenn X/Y nicht in einem JXTaskpane liegen?
     */
    @Override
    public TreePath getClosestPathForLocation(int x, int y) {
        final Component c = container.getComponentAt(x, y);
        if (c instanceof JXTaskPane) {
            final JXTaskPane pane = (JXTaskPane) c;
            final SlideableSubTree t = trees.get(panes.indexOf(pane));
            final int titleBarHeight = (pane.getHeight() - pane.getContentPane().getHeight());
            if (y <= titleBarHeight + pane.getY()) {
                //der rootNode des SubTrees ist der closestNode
                return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
            } else if (y > titleBarHeight + pane.getY() && y < pane.getY() + titleBarHeight + t.getY()) {
                int distanceToTitle = Math.abs(y - (titleBarHeight + pane.getY()));
                int distanceToTree = Math.abs(y - (pane.getY() + titleBarHeight + t.getY()));
                if (distanceToTitle < distanceToTree) {
                    return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
                }
            }
            final int newY = y - pane.getY() - t.getY() - titleBarHeight;
            final int newX = x - pane.getX() - t.getX();
            TreePath subTreePath = t.getClosestPathForLocation(newX, newY);
            //ist null falls kein Knoten sichtbar ist, z.b wenn der rootNode des
            //subTree ein blatt ist
            if (subTreePath == null) {
                subTreePath = new TreePath(t.getModel().getRoot());
            }
            return getPathforOriginalTree(subTreePath);

            //y liegt zwischen titlebar und tree, geringsten abstand bestimmen
        } else if (c instanceof JXTaskPaneContainer) {
            //falls berechne den nahestehendsten JXTaskpane..
            JXTaskPane closest = null;
            boolean lastComponent = false;
            for (JXTaskPane p : panes) {
                final int paneY = p.getY();
                //liegt y vor p?
                if (y < paneY) {
                    if (panes.indexOf(p) == 0) {
                        closest = p;
                        lastComponent = false;
                    } else {
                        int distance = Math.abs(y - paneY);
                        final JXTaskPane predecessor = panes.get(panes.indexOf(p) - 1);
                        int distanceToPredecessor = Math.abs(y - (predecessor.getY() + predecessor.getHeight()));
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
        } //falls x/y nicht innerhalb des container liegen return null
        else {
            return null;
        }
    }

    @Override
    protected Enumeration<TreePath> getDescendantToggledPaths(TreePath parent) {
        final Vector<TreePath> toggledPaths = new Vector<TreePath>();
        final Object lastPathComponent = parent.getLastPathComponent();
        final Object parentRoot = this.getModel().getRoot();

        if (trees != null) {
            if (lastPathComponent.equals(parentRoot)) {
                //falls parent gleich dem RootNode des ParentTree ist
                //was tun ??
            } else {
                final SlideableSubTree t = getSubTreeForPath(parent);
                final TreePath subTreePath = getPathForSubTree(parent);
                Enumeration toggledSubPaths = t.getDescendantToggledPaths(subTreePath);

                while (toggledSubPaths.hasMoreElements()) {
                    final TreePath originPath = getPathforOriginalTree((TreePath) toggledSubPaths.nextElement());
                    toggledPaths.add(getPathforOriginalTree(originPath));
                }
            }
            return toggledPaths.elements();
        }
        return super.getDescendantToggledPaths(parent);
    }

    /*
     * für alle subtrees rückwärts durchlaufen, wenn selektion gefunden
     * richtige Row berechnen (offset addieren)
     */
    @Override
    public int getMaxSelectionRow() {
        if (trees != null) {
            int offset = 0;
            int maxSelection = 0;
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
        return super.getMaxSelectionRow();
    }

    /*
     * für alle subtrees durhclaufen, wenn selektion gefunden
     * richtige Row berechnen (offset addieren)
     */
    @Override
    public int getMinSelectionRow() {
        if (trees != null) {
            int offset = 0;
            int minSelection = 0;
            int indexOfTree = 0;

            for (SlideableSubTree t : trees) {
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
        return super.getMinSelectionRow();
    }

    /*
     * subtree ausfindig machen, neuen Path machen, methode weiterleiten
     */
    @Override
    public Rectangle getPathBounds(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);
        //wenn durch path representierter Knoten die Wurzel des Originalbaum ist

        if (t == null) {
            return super.getPathBounds(path);

        } else {
            Rectangle rec = t.getPathBounds(getPathForSubTree(path));
            JXTaskPane pane = panes.get(trees.indexOf(t));
            rec.setLocation((int) rec.getX() + pane.getX(), (int) rec.getY() + pane.getY());
            return rec;
        }
    }

    /*
     * number of rows that are currently displayed
     * durch alle subtrees durch und addieren (plus offset für root nodes)
     */
    @Override
    public int getRowCount() {
        int sum = 0;

        if (trees != null) {
            for (SlideableSubTree t : trees) {
                //Anzahl der sichtbaren Zeilen + 1 für den jeweiligen Root des SubTree
                sum += t.getRowCount();

                if (!t.isRootVisible()) {
                    sum++;
                }
            }
        }
        return sum;
    }

    /*
     * liefert  Path zu dem ERSTEN selektieren knoten oder null wenn nichts selektiert
     * Die einzelnen subtrees durchgehen, mehtode aufrufen
     * Einschränkung, RootNodes der SubTrees und des Originaltrees können nicht
     * selektiert werden
     */
    @Override
    public TreePath getSelectionPath() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                final TreePath firstSelection = t.getSelectionPath();

                if (firstSelection != null) {
                    return getPathforOriginalTree(firstSelection);

                }
            }
        }
        return null;
    }

    /*
     * liefert null falls keine selektion vorhanden, sonst die angepassten path
     * objekte
     */
    @Override
    public TreePath[] getSelectionPaths() {
        ArrayList<TreePath> paths = new ArrayList<TreePath>();

        if (trees != null) {
            for (SlideableSubTree t : trees) {
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
            TreePath[] path = new TreePath[paths.size()];
            paths.toArray(path);

            return path;
        }
        return null;
    }

    /*
     * für jeden Subtree aufrufen und aufaddieren
     */
    @Override
    public int getSelectionCount() {
        int sum = 0;

        if (trees != null) {
            for (SlideableSubTree t : trees) {
                sum += t.getSelectionCount();
            }
            return sum;
        }
        return super.getSelectionCount();
    }

    /*
     * return null wenn SubTree nicht vorhanden
     *
     */
    @Override
    public TreePath getPathForRow(int row) {
        final SlideableSubTree t = getSubTreeForRow(row);
        final int subTreeRow = getRowForSubTree(row);

        if (t != null) {
            if (subTreeRow < 0 && !t.isRootVisible()) {
                return getPathforOriginalTree(new TreePath(t.getModel().getRoot()));
            } else {
                TreePath tmp = t.getPathForRow(subTreeRow);
                final TreePath path = getPathforOriginalTree(tmp);

                return path;
            }
        }
        return null;
    }

    @Override
    public int getRowForPath(TreePath path) {
        final SlideableSubTree subTree = getSubTreeForPath(path);
        final TreePath subPath = getPathForSubTree(path);
        int offset = 0;
        int row = -1;

        if (trees != null) {
            for (SlideableSubTree t : trees) {
                if (t.equals(subTree)) {
                    break;
                } else {
                    offset += t.getRowCount() + 1;
                }
            }
            row = subTree.getRowForPath(subPath) + offset;
        }
        return row;
    }

    @Override
    public TreePath getNextMatch(String prefix, int startingRow, Bias bias) {
        return super.getNextMatch(prefix, startingRow, bias);
    }

    @Override
    protected TreePath[] getPathBetweenRows(int index0, int index1) {
        ArrayList<TreePath> list = new ArrayList<TreePath>();

        for (int i = index0 + 1; i
                <= index1; i++) {
            list.add(getPathForRow(i));
        }
        TreePath[] finalPaths = new TreePath[list.size()];

        for (int i = 0; i
                < finalPaths.length; i++) {
            finalPaths[i] = list.get(i);
        }
        return finalPaths;
    }

    /*
     * zu knoten der durch path representiert ist zugehörigen subtree
     * ausfindig machen, neuen path erstellen mehtode weiterleiten
     */
    @Override
    public boolean hasBeenExpanded(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);
        //keinen SubTree gefunden, Knoten ist also die Wurzel oder nicht enthalten

        if (t == null) {
            return super.hasBeenExpanded(path);
        } else {
            return t.hasBeenExpanded(getPathForSubTree(path));

        }
    }

    /*
     * uberprüfen ob ein knoten in einem subtree editiert wird
     * mehrfache editierung möglich?
     */
    @Override
    public boolean isEditing() {
        boolean isEditing = false;

        if (trees != null) {
            for (SlideableSubTree t : trees) {
                if (t.isEditing()) {
                    isEditing = true;
                }
            }
        }
        return isEditing;
    }

    /*
     * herausfinden welcher knoten mit path identifiziert wird, 
     * methode weiterleiten
     */
    @Override
    public boolean isExpanded(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);

        if (t == null) {
            return super.isExpanded(path);
        } else {
            return t.isExpanded(getPathForSubTree(path));
        }
    }

    /*
     * herausfinden welcher knoten mit row identifiziert wird,
     * methode weiterleiten
     */
    @Override
    public boolean isExpanded(int row) {
        final TreePath path = getPathForRow(row);

        return isExpanded(path);
    }


    /*
     * Vorgehensweise
     * Was passiert falls der Knoten der Root eines Subtrees ist??
     * diese können nicht selektiert werden....
     */
    @Override
    public boolean isPathSelected(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);

        if (t == null) {
            //was passiert falls der Knoten die wurzel ist?
            return super.isPathSelected(path);
        } else {
            return t.isPathSelected(getPathForSubTree(path));
        }
    }

    /*
     * Vorgehensweise
     * herausfinden welcher Knoten durch row represntiert wird,
    methode weiterleiten
     */
    @Override
    public boolean isRowSelected(int row) {
        final TreePath path = getPathForRow(row);
        final boolean isSelected = isPathSelected(path);

        return isSelected;
    }

    /*
     * Vorgehensweise
     * für jeden subtree überprüfen ob selection emtpy
     */
    @Override
    public boolean isSelectionEmpty() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                if (!(t.isSelectionEmpty())) {
                    return false;
                }
            }
        }
        return true;
    }
    /*
     * Vorgehensweise wie bei makeVisible
     */

    @Override
    public boolean isVisible(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);

        if (t == null) {
            return super.isVisible(path);

        } else {
            return t.isVisible(getPathForSubTree(path));
        }
    }

    /*
     * subtree herausfinden der den durch path beschriebenen Knoten enthält und
     * und diesen sichtbar machen(methode aufrufen)
     */
    @Override
    public void makeVisible(TreePath path) {
        final JTree t = getSubTreeForPath(path);

        if (t == null) {
            super.makeVisible(path);
        } else {
            t.makeVisible(getPathForSubTree(path));
        }
    }

    @Override
    public void removeSelectionInterval(int index0, int index1) {
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
    }

    @Override
    public void removeSelectionPath(TreePath path) {
        if (trees != null) {
            final SlideableSubTree subTree = getSubTreeForPath(path);
            subTree.removeSelectionPath(getPathForSubTree(path));
        }
        super.removeSelectionPath(path);
    }

    @Override
    public void removeSelectionPaths(TreePath[] paths) {
        for (int i = 0; i
                < paths.length; i++) {
            removeSelectionPath(paths[i]);
        }
        super.removeSelectionPaths(paths);
    }

    @Override
    public void removeSelectionRow(int row) {
        final TreePath path = getPathForRow(row);
        removeSelectionPath(
                path);
        super.removeSelectionRow(row);
    }

    @Override
    public void removeSelectionRows(int[] rows) {
        for (int i = 0; i
                < rows.length; i++) {
            final TreePath path = getPathForRow(i);
            removeSelectionPath(path);
        }
        super.removeSelectionRows(rows);
    }

    /*
     * subtree ausfindig machen, mehtode weiterleiten, Paths in Enuemration
     *  anpassen
     */
    @Override
    protected boolean removeDescendantSelectedPaths(TreePath path, boolean includePath) {
        final SlideableSubTree t = getSubTreeForPath(path);
        final TreePath subTreePath = getPathForSubTree(path);

        if (trees != null) {
            return t.removeDescendantSelectedPaths(subTreePath, includePath);
        } else {
            return super.removeDescendantSelectedPaths(path, includePath);
        }
    }

    @Override
    protected void removeDescendantToggledPaths(Enumeration<TreePath> toRemove) {

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
    }

    /*
     * prinizipiell methode an subtrees weiterreichen, evtl offsett addieren
     */
    @Override
    public int[] getSelectionRows() {
        int[][] result = new int[trees.size()][];
        int offset = 0;
        int count = 0;

        if (trees != null) {
            //getSelectionRows für jeden Subtree
            for (SlideableSubTree t : trees) {
                result[trees.indexOf(t)] = t.getSelectionRows();
                count += result[trees.indexOf(t)].length;
                //Offset aufaddieren für korrekte Inidzes
                for (int i = 0; i
                        < result[trees.indexOf(t)].length; i++) {
                    result[trees.indexOf(t)][i] += offset;
                }
                offset += t.getRowCount() + 1;
            }
            int[] selectionRows = new int[count];
            //Ergebnisse zusammenfassen

            for (int i = 0; i
                    < selectionRows.length; i++) {
                for (int j = 0; j
                        < result[i].length; j++) {
                    selectionRows[i] = result[i][j];
                }
            }
            return selectionRows;

        }
        return super.getSelectionRows();

    }


    /*
     * Mögliche Vorgehensweise
     *herausfinden welches element sichtbar werden soll, überprüfen in welchem baum
     * es ist,und für diesen die methode aufrufen
     */
    @Override
    public void scrollPathToVisible(TreePath path) {
        if (trees != null) {
            final SlideableSubTree t = getSubTreeForPath(path);
            //path ist die wurzel des baums, oder gar nicht enthalten

            if (t == null) {
                super.scrollPathToVisible(path);

            } else {
                t.scrollPathToVisible(getPathForSubTree(path));
            }
        }

    }

    /*
     * mögliche vorgehensweise
     * herausfinden welches element sichtbar werden soll, überprüfen in welchem baum
     * es ist,und für diesen die methode aufrufen
     */
    @Override
    public void scrollRowToVisible(int row) {
        final TreePath path = getPathForRow(row);
        scrollPathToVisible(
                path);
    }

    @Override
    public void setAnchorSelectionPath(TreePath newPath) {
        if (trees != null) {
            final SlideableSubTree t = getSubTreeForPath(newPath);
            final TreePath subTreePath = getPathForSubTree(newPath);

            if (t != null) {
                t.setAnchorSelectionPath(subTreePath);
            }
        }
        super.setAnchorSelectionPath(newPath);
    }

    @Override
    public void setCellEditor(TreeCellEditor cellEditor) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setCellEditor(cellEditor);
            }
        }
        super.setCellEditor(cellEditor);
    }

    @Override
    public void setCellRenderer(TreeCellRenderer x) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setCellRenderer(x);
            }
        }
        super.setCellRenderer(x);
    }

    @Override
    public void setDragEnabled(boolean b) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setDragEnabled(b);
            }
        }
        super.setDragEnabled(b);
    }

    @Override
    public void setEditable(boolean flag) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setEditable(flag);
            }
        }
        super.setEditable(flag);
    }

    @Override
    public void setExpandsSelectedPaths(boolean newValue) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setExpandsSelectedPaths(newValue);
            }
        }
        super.setExpandsSelectedPaths(newValue);
    }

    /*
     * subtree zu knoten herausfinden, neuen path erstellen,
     * mehtode weiterleiten ! Achtung Methode protected!!
     */
    @Override
    protected void setExpandedState(TreePath path, boolean state) {
        final SlideableSubTree t = getSubTreeForPath(path);
        final TreePath subTreePath = getPathForSubTree(path);

        if (trees != null) {
            t.setExpandedState(subTreePath, state);
        }
    }

    @Override
    public void setInvokesStopCellEditing(boolean newValue) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setInvokesStopCellEditing(newValue);
            }
        }
        super.setInvokesStopCellEditing(newValue);

    }

    @Override
    public void setLargeModel(boolean newValue) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setLargeModel(newValue);
            }
            super.setLargeModel(newValue);
        }
    }

    @Override
    public void setLeadSelectionPath(TreePath newPath) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setLeadSelectionPath(newPath);
            }
        }
        super.setLeadSelectionPath(newPath);
    }

    @Override
    /*
     *  Ändert sich das Model, müsssen neue Subtrees mit neuem
     *  DelegatingModel erzeugt werden.
     */
    public void setModel(TreeModel newModel) {

        this.treeModel = newModel;

        if (trees != null) {
            createSubTrees(newModel);
            flushTreeContainer();
            addToTreeContainer(panes);
        }
    }

    @Override
    public void setRootVisible(boolean rootVisible) {
        for (SlideableSubTree t : trees) {
            t.setRootVisible(rootVisible);

        }
        super.setRootVisible(rootVisible);

    }

    @Override
    public void setRowHeight(int rowHeight) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setRowHeight(rowHeight);

            }
        } else {
            super.setRowHeight(rowHeight);

        }
    }

    @Override
    public void setScrollsOnExpand(boolean newValue) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setScrollsOnExpand(newValue);
            }
        }
    }

    @Override
    public void setSelectionInterval(int index0, int index1) {
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
                TreePath[] finalPaths = new TreePath[pathList.size()];

                for (int i = 0; i
                        < finalPaths.length; i++) {
                    finalPaths[i] = pathList.get(i);
                }
                setSelectionPaths(finalPaths);
            }
        } else {
            super.setSelectionInterval(index0, index1);
        }
    }

    @Override
    public void setSelectionModel(TreeSelectionModel selectionModel) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setSelectionModel(selectionModel);
            }
        } else {
            super.setSelectionModel(selectionModel);
        }
    }

    @Override
    public void setSelectionPath(TreePath path) {
        if (trees != null) {
            final SlideableSubTree t = getSubTreeForPath(path);

            for (JTree tmpTree : trees) {
                if (t.equals(tmpTree)) {
                    continue;
                } else {
                    tmpTree.clearSelection();
                }
            }
            if (t == null) {
                //was wenn der Root knoten selektiert werden soll
            } else {
                t.setSelectionPath(getPathForSubTree(path));
            }
        }
        //super.setSelectionPath(path);
    }

    @Override
    public void setSelectionPaths(TreePath[] paths) {
        for (int i = 0; i
                < paths.length; i++) {
            setSelectionPath(paths[i]);
        }
    }

    @Override
    public void setSelectionRow(int row) {
        final TreePath path = getPathForRow(row);
        final SlideableSubTree subTree = getSubTreeForPath(path);
        subTree.setSelectionPath(getPathForSubTree(path));
    }

    @Override
    public void setSelectionRows(int[] rows) {
        for (int i = 0; i
                < rows.length; i++) {
            final TreePath path = getPathForRow(rows[i]);
            final SlideableSubTree subTree = getSubTreeForPath(path);
            subTree.setSelectionPath(getPathForSubTree(path));
        }
    }

    @Override
    public void setShowsRootHandles(boolean newValue) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setShowsRootHandles(newValue);
            }
        } else {
            super.setShowsRootHandles(newValue);
        }
    }

    @Override
    public void setToggleClickCount(int clickCount) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setToggleClickCount(clickCount);
            }
        } else {
            super.setToggleClickCount(clickCount);
        }
    }

    @Override
    public void setUI(TreeUI ui) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setUI(ui);
            }
        } else {
            super.setUI(ui);
        }
    }

    @Override
    public void setVisibleRowCount(int newCount) {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.setVisibleRowCount(newCount);
            }
        }
        super.setVisibleRowCount(newCount);
    }

    /*
     * den Subtree zu Path herusfinden und methode delegieren
     * was ist wenn der RootNode eines SubTrees editiert werden soll
     */
    @Override
    public void startEditingAtPath(TreePath path) {
        final SlideableSubTree t = getSubTreeForPath(path);
        if (t == null) {
            super.startEditingAtPath(path);

        } else {
            t.startEditingAtPath(getPathForSubTree(path));
        }
    }

    /*
     * liefert false, wenn der baum nicht editiert wurde,
     * daher nur true wenn bei einem der Subtrees true zurück kommt
     */
    @Override
    public boolean stopEditing() {
        boolean stopped = false;
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                if (t.stopEditing()) {
                    stopped = true;
                }
            }
        } else {
            return super.stopEditing();
        }
        return stopped;
    }

    @Override
    public void updateUI() {
        if (trees != null) {
            for (SlideableSubTree t : trees) {
                t.updateUI();
            }
        }
        super.updateUI();
    }

    /*
     * Hilfsmethode, die den Inhalt des JXTaskPaneContainer erstellt
     * (also die einzelnen JXTaskPanes mit SubTree)
     */
    private void createSubTrees(TreeModel model) {
        final TreeModelListener listener = createTreeModelListener();
        treeModel.addTreeModelListener(listener);
        flushTreeContainer();
        trees = new ArrayList<SlideableSubTree>();
        panes = new ArrayList<JXTaskPane>();
        final Object root = this.getModel().getRoot();
        final int childCount = this.getModel().getChildCount(root);

        for (int i = 0; i
                < childCount; i++) {
            final Object child = model.getChild(root, i);
            final TreeNode newRootNode = new DefaultMutableTreeNode(child);
            final SlideableSubTree subTree = new SlideableSubTree(newRootNode);
            DelegatingModel modelDelegate = new DelegatingModel(child, model);
            subTree.setModel(modelDelegate);
            subTree.setRootVisible(false);
            subTree.addTreeExpansionListener(this);
            subTree.addTreeSelectionListener(this);
            subTree.addTreeWillExpandListener(this);
            subTree.setDragEnabled(true);
            subTree.setBorder(new EmptyBorder(1, 1, 1, 1));
            trees.add(subTree);
            final JXTaskPane tmpPane = new JXTaskPane();
            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) subTree.getModel().getRoot();
            DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) trees.get(i).getCellRenderer();
            Icon icon = null;

            if (rootNode.isLeaf()) {
                icon = renderer.getLeafIcon();
                tmpPane.setCollapsed(true);
            } else if (tmpPane.isCollapsed()) {
                icon = renderer.getClosedIcon();
            } else {
                icon = renderer.getOpenIcon();
            }
            tmpPane.setIcon(icon);
            tmpPane.setTitle(newRootNode.toString());
            tmpPane.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    System.out.println("xxx");
                    if (e.isPopupTrigger()) {
                        System.out.println("popup menu");
                    } else {
                        /*
                         * final JXTaskPane pane = (JXTaskPane)e.getSource();
                         * ((JComponent) pane.getContentPane()).setBackground(Color.red);
                         */
                        System.out.println("linke maustaste");
                    }
                }
            });

            ((JComponent) tmpPane.getContentPane()).setBorder(new EmptyBorder(1, 16, 1, 1));
            tmpPane.add(subTree);
            panes.add(tmpPane);

        }
    }


    /*
     * Hilfsmethode zum leeren des Containers der die SubTrees enthält
     */
    private void flushTreeContainer() {
        if (container != null) {
            for (int i = 0; i
                    < container.getComponentCount(); i++) {
                container.remove(container.getComponent(i));
            }
        }
    }

    /*
     * Hilfsmethode zum befüllen des Containers der die Subtrees entählt
     */
    private void addToTreeContainer(ArrayList<JXTaskPane> list) {
        for (JComponent c : list) {
            container.add(c);
        }
    }

    /*
     * Hilfsmethode die zu einem Knoten der durch einen TreePath representiert
     * wird, den subtree zurückliefert, der diesen Knoten enthält
     * liefert null zurrück, falls es keinen Subtree gibt
     */
    private SlideableSubTree getSubTreeForPath(TreePath path) {
        final Object pathRoot = path.getPathComponent(0);
        //Der Pfad entählt nur einen Knoten, also die Wurzel
        if (path.getPathCount() <= 1) {
            return null;
        } else {
            final Object pathSubRoot = path.getPathComponent(1);
            if (trees != null) {
                for (JTree t : trees) {
                    final Object subTreeRoot = t.getModel().getRoot();

                    if (pathSubRoot.equals(subTreeRoot)) {
                        return (SlideableSubTree) t;
                    }
                }
            }
        }
        return null;
    }

    /*
     * Hilfsmethode die den Pfad eines Subtrees zu einem Pfad des
     * originalen Baums transformiert
     */
    private TreePath getPathforOriginalTree(TreePath subTreePath) {
        final Object pathRoot = subTreePath.getPathComponent(0);
        final Object origRoot = this.getModel().getRoot();
        //falls der Methode bereits ein pfad des original Baum übergeben wird,
        //diesen einfach zurückgeben
        if (pathRoot.equals(origRoot)) {
            return subTreePath;
        }
        final Object[] oldPath = subTreePath.getPath();
        Object[] newPath = new Object[oldPath.length + 1];
        newPath[0] = origRoot;
        System.arraycopy(oldPath, 0, newPath, 1, oldPath.length);
        return new TreePath(newPath);
    }

    /*
     * Hilfsmethode die einen Pfad des OriginalTrees zu einem Pfad
     * des entsprechenden subtrees generiert
     * return null, wenn originPath kein Path des OriginalBaums ist
     */
    private TreePath getPathForSubTree(TreePath originPath) {
        final Object pathRoot = originPath.getPathComponent(0);
        final Object originRoot = this.getModel().getRoot();

        //wenn path nicht zum originalbaum gehört
        if (!pathRoot.equals(originRoot)) {
            return null;
        } //der pfad enthält nur den Root knoten des Originalbaums
        else if (originPath.getPathCount() <= 1) {
            return null;
        } else {
            final Object[] oldPath = originPath.getPath();
            final Object[] newPath = new Object[oldPath.length - 1];
            System.arraycopy(oldPath, 1, newPath, 0, newPath.length);

            return new TreePath(newPath);
        }
    }

    /*
     * Hilfsmethode
     * return null für falls row > rowCount ist bzw. keine SubTrees vorhanden sind
     */
    private SlideableSubTree getSubTreeForRow(int row) {
        int sum = 0;
        if (trees != null) {
            for (SlideableSubTree t : trees) {
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

    /*
     * Hilfsmethode
     */
    private int getRowForSubTree(int originRow) {
        final SlideableSubTree tmpTree = getSubTreeForRow(originRow);

        int offset = 1;

        if (tmpTree != null) {
            for (SlideableSubTree t : trees) {
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
    /*
     * Returns the last path component in the first node of the current selection.
     * ruft methode für path auf, der als ergebnis von getSelectedpath zurückkommt
     * daher nicht überschreibe
     * */

    @Override
    public Object getLastSelectedPathComponent() {
        final TreePath path = this.getSelectionPath();

        if (path != null) {
            return path.getLastPathComponent();
        }
        return null;
    }

    /*********************** nicht zu überschreibende Methoden ***************/
    /*************************************************************************/
// <editor-fold defaultstate="collapsed" desc="comment">
    /*
    @Override
    public void addTreeExpansionListener(TreeExpansionListener tel) {

    super.addTreeExpansionListener(tel);
    }

    @Override
    public void addTreeSelectionListener(TreeSelectionListener tsl) {

    super.addTreeSelectionListener(tsl);
    }

    @Override
    public void addTreeWillExpandListener(TreeWillExpandListener tel) {

    super.addTreeWillExpandListener(tel);
    }

    @Override
    public void removeTreeExpansionListener(TreeExpansionListener tel) {
    super.removeTreeExpansionListener(tel);
    }

    @Override
    public void removeTreeSelectionListener(TreeSelectionListener tsl) {
    super.removeTreeSelectionListener(tsl);
    }

    @Override
    public void removeTreeWillExpandListener(TreeWillExpandListener tel) {
    super.removeTreeWillExpandListener(tel);
    }


     * laut JavaDoc wird diese Methode lediglich von der UI aufgerufen
     * zb wenn Knoten expandiert oder hinzugefügt wurden, daher m.E.n.
     * keine neue impl notwendig
     *
     * repaint()
     * revalidate()
    @Override
    public void treeDidChange() {
    super.treeDidChange();
    }

    @Override
    public boolean isRootVisible() {
    return super.isRootVisible();
    }

     * laut javadoc für debug gedacht, muss also evtl nicht überschrieben werden
    @Override
    protected String paramString() {
    return super.paramString();
    }

    @Override
    public boolean isFixedRowHeight() {
    return super.isFixedRowHeight();
    }

    @Override
    public boolean isLargeModel() {
    return super.isLargeModel();
    }

     * returns isEditable
    @Override
    public boolean isPathEditable(TreePath path) {
    return super.isPathEditable(path);
    }

     * returns !isExpandend(path)
    @Override
    public boolean isCollapsed(TreePath path) {
    return super.isCollapsed(path);
    }

     * returns !isExpandend(row)
    @Override
    public boolean isCollapsed(int row) {
    return super.isCollapsed(row);
    }

    @Override
    public boolean isEditable() {
    return super.isEditable();
    }

    @Override
    public boolean getShowsRootHandles() {
    return super.getShowsRootHandles();
    }

    @Override
    public int getToggleClickCount() {
    return super.getToggleClickCount();
    }

    @Override
    public String getToolTipText(MouseEvent event) {
    return super.getToolTipText(event);
    }

    @Override
    public TreeExpansionListener[] getTreeExpansionListeners() {
    return super.getTreeExpansionListeners();
    }

    @Override
    public TreeSelectionListener[] getTreeSelectionListeners() {
    return super.getTreeSelectionListeners();
    }

    @Override
    public TreeWillExpandListener[] getTreeWillExpandListeners() {
    return super.getTreeWillExpandListeners();
    }

    @Override
    public TreeUI getUI() {
    return super.getUI();
    }

    @Override
    public String getUIClassID() {
    return super.getUIClassID();
    }

    @Override
    public int getVisibleRowCount() {
    return super.getVisibleRowCount();
    }

    @Override
    public TreeSelectionModel getSelectionModel() {
    return super.getSelectionModel();
    }

    @Override
    public boolean getScrollsOnExpand() {
    return super.getScrollsOnExpand();
    }

    @Override
    public int getRowHeight() {
    return super.getRowHeight();
    }

    @Override
    public TreeModel getModel() {
    return super.getModel();
    }

     * return getPathBounds(getPathForRow(row))
     * daher
     * nicht überschreiben
    @Override
    public Rectangle getRowBounds(int row) {
    return super.getRowBounds(row);
    }

    @Override
    public boolean getDragEnabled() {
    return super.getDragEnabled();
    }

    @Override
    public TreeCellEditor getCellEditor() {
    return super.getCellEditor();
    }

    @Override
    public TreeCellRenderer getCellRenderer() {
    return super.getCellRenderer();
    }


     * nicht überschreiben
     * setExpandedState(path, false)
    @Override
    public void collapsePath(TreePath path) {
    super.collapsePath(path);
    }

     * nicht überschreiben
     * collapsePath(getPathForRow(row))
    @Override
    public void collapseRow(int row) {
    super.collapseRow(row);
    }

     * nicht überschreiben
     * setExpandedState(path, true)
     * subtree für knoten herausfinden, path umbauen, mehtode weiterleiten
    @Override
    public void expandPath(TreePath path) {
    super.expandPath(path);
    }

     * return getRowForPath(getPathForLocation(x, y));
    @Override
    public int getRowForLocation(int x, int y) {
    return super.getRowForLocation(x, y);
    }

     * return getRowForPath(getClosestPathForLocation(x, y));
    @Override
    public int getClosestRowForLocation(int x, int y) {
    return super.getClosestRowForLocation(x, y);
    }

    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
    return super.getScrollableBlockIncrement(visibleRect, orientation, direction);
    }

    @Override
    public boolean getScrollableTracksViewportHeight() {
    return super.getScrollableTracksViewportHeight();
    }

    @Override
    public boolean getScrollableTracksViewportWidth() {
    return super.getScrollableTracksViewportWidth();
    }

    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
    return super.getScrollableUnitIncrement(visibleRect, orientation, direction);
    }

    @Override
    public Dimension getPreferredScrollableViewportSize() {
    return super.getPreferredScrollableViewportSize();
    }

    @Override
    public boolean getExpandsSelectedPaths() {
    return super.getExpandsSelectedPaths();
    }

    @Override
    public boolean getInvokesStopCellEditing() {
    return super.getInvokesStopCellEditing();
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    return super.convertValueToText(value, selected, expanded, leaf, row, hasFocus);
    }

    @Override
    protected TreeModelListener createTreeModelListener() {
    return super.createTreeModelListener();
    }

    @Override
    public TreePath getAnchorSelectionPath() {
    return super.getAnchorSelectionPath();
    }

     * Gets the AccessibleContext associated with this JTree. For JTrees,
     * the AccessibleContext takes the form of an AccessibleJTree.
     * A new AccessibleJTree instance is created if necessary.

    @Override
    public AccessibleContext getAccessibleContext() {
    return super.getAccessibleContext();
    }

    @Override
    public void fireTreeCollapsed(TreePath path) {
    super.fireTreeCollapsed(path);
    }

    @Override
    public void fireTreeExpanded(TreePath path) {
    super.fireTreeExpanded(path);
    }

    @Override
    public void fireTreeWillCollapse(TreePath path) throws ExpandVetoException {
    super.fireTreeWillCollapse(path);
    }

    @Override
    public void fireTreeWillExpand(TreePath path) throws ExpandVetoException {
    super.fireTreeWillExpand(path);
    }

    @Override
    protected void fireValueChanged(TreeSelectionEvent e) {
    super.fireValueChanged(e);
    }

    
     * nicht überschreiben

    @Override
    public TreePath getLeadSelectionPath() {
    return super.getLeadSelectionPath();
    }

    
     * returns row for getleadselectionPath
     * methode getRowforPath wird benutzt daher
     * nicht überschreiben

    @Override
    public int getLeadSelectionRow() {
    return super.getLeadSelectionRow();
    }
     */
    // </editor-fold>
    /**************************** Interface Methods***************************/
    /************************************************************************/
    public void treeExpanded(TreeExpansionEvent event) {
        //fireTreeExpanded(getPathforOriginalTree(event.getPath()));
    }

    public void treeCollapsed(TreeExpansionEvent event) {
        fireTreeCollapsed(getPathforOriginalTree(event.getPath()));
    }

    public void valueChanged(TreeSelectionEvent e) {
        if (trees != null) {
            final TreePath path = getPathforOriginalTree(e.getPath());

            for (JTree tmpTree : trees) {
                if (!(e.isAddedPath()) || tmpTree.equals((SlideableSubTree) e.getSource())) {
                    continue;
                } else {
                    tmpTree.clearSelection();
                }
            }
            fireValueChanged(e);
        }

    }

    public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
        fireTreeWillExpand(getPathforOriginalTree(event.getPath()));
    }

    public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
        fireTreeWillCollapse(getPathforOriginalTree(event.getPath()));

    }

    @Override
    public TreeModelListener createTreeModelListener() {
        return new MyTreeModelHandler(this);

    }

    public void print(TreeNode node, String indent) {
        System.out.println(indent + node.toString());
        indent += "\t";

        for (int i = 0; i
                < node.getChildCount(); i++) {
            if (node.getChildCount() > 0) {
                final TreeNode child = node.getChildAt(i);

                if (child.isLeaf()) {
                    System.out.println(indent + child.toString());

                } else {
                    indent += "\t";
                    this.print(child, indent);
                }
            }
        }
    }

    protected class MyTreeModelHandler implements TreeModelListener {

        private SlideableTree tree;

        public MyTreeModelHandler() {
        }

        public MyTreeModelHandler(SlideableTree t) {
            tree = t;
        }

        public void treeNodesChanged(TreeModelEvent e) {
            final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
            t.updateUI();
        }

        public void treeNodesInserted(TreeModelEvent e) {
            final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
            t.updateUI();
        }

        public void treeStructureChanged(TreeModelEvent e) {
            final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
            t.updateUI();
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            final SlideableSubTree t = getSubTreeForPath(e.getTreePath());
            t.updateUI();
        }
    }
}
