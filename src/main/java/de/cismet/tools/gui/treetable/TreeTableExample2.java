package de.cismet.tools.gui.treetable;
/*
 * TreeTableExample2.java
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

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;

/**
 * Assembles the UI. The UI consists of a JTreeTable and a status label.
 * As nodes are loaded by the FileSystemModel2, in a background thread,
 * the status label updates as well as the renderer to draw the node that
 * is being loaded differently.
 *
 * @author Scott Violet
 * @author Philip Milne
 */
public class TreeTableExample2 {
    /** Number of instances of TreeTableExample2. */
    protected static int         ttCount;

    /** Model for the JTreeTable. */
    protected FileSystemModel2    model;
    /** Used to represent the model. */
    protected JTreeTable         treeTable;
    /** Row the is being reloaded. */
    protected int                reloadRow;
    /** TreePath being reloaded. */
    protected TreePath           reloadPath;
    /** A counter increment as the Timer fies and the same path is
     * being reloaded. */
    protected int                reloadCounter;
    /** Timer used to update reload state. */
    protected Timer              timer;
    /** Used to indicate status. */
    protected JLabel             statusLabel;
    /** Frame containing everything. */
    protected JFrame             frame;
    /** Path created with. */
    protected String             path;


    public TreeTableExample2(String path) {
	this.path = path;
	ttCount++;

	frame = createFrame();

	Container      cPane = frame.getContentPane();
	JMenuBar       mb = createMenuBar();

	model = createModel(path);
	treeTable = createTreeTable();
	statusLabel = createStatusLabel();
	cPane.add(new JScrollPane(treeTable));
	cPane.add(statusLabel, BorderLayout.SOUTH);

	reloadRow = -1;
	frame.setJMenuBar(mb);
	frame.pack();
	frame.show();
	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
		reload(model.getRoot());
	    }
	});
    }

    /**
     * Creates and return a JLabel that is used to indicate the status
     * of loading.
     */
    protected JLabel createStatusLabel() {
	JLabel         retLabel = new JLabel(" ");  //NOI18N

	retLabel.setHorizontalAlignment(JLabel.RIGHT);
	retLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
	return retLabel;
    }

    /**
     * Creates and returns the instanceof JTreeTable that will be used.
     * This also creates, but does not start, the Timer that is used to
     * update the display as files are loaded.
     */
    protected JTreeTable createTreeTable() {
	JTreeTable       treeTable = new JTreeTable(model);

	treeTable.getColumnModel().getColumn(1).setCellRenderer
	                           (new IndicatorRenderer());

	Reloader rl = new Reloader();

	timer = new Timer(700, rl);
	timer.setRepeats(true);
	treeTable.getTree().addTreeExpansionListener(rl);
	return treeTable;
    }

    /**
     * Creates the FileSystemModel2 that will be used.
     */
    protected FileSystemModel2 createModel(String path) {
	return new FileSystemModel2(path);
    }

    /**
     * Creates the JFrame that will contain everything.
     */
    protected JFrame createFrame() {
	JFrame       retFrame = new JFrame(org.openide.util.NbBundle.getMessage(TreeTableExample2.class, "TreeTableExample2.createFrame().retFrame.title"));  //NOI18N

	retFrame.addWindowListener(new WindowAdapter() {
	    public void windowClosing(WindowEvent we) {
		if (--ttCount == 0) {
		    System.exit(0);
		}
	    }
	});
	return retFrame;
    }

    /**
     * Creates a menu bar.
     */
    protected JMenuBar createMenuBar() {
        JMenu            fileMenu = new JMenu(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().fileMenu.title")); //NOI18N
	JMenuItem        menuItem;

	menuItem = new JMenuItem(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().menuItem.text.open"));  //NOI18N
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		JFileChooser      fc = new JFileChooser(path);

		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

		int               result = fc.showOpenDialog(frame);

		if (result == JFileChooser.APPROVE_OPTION) {
		    String      newPath = fc.getSelectedFile().getPath();

		    new TreeTableExample2(newPath);
		}
	    }
	});
	fileMenu.add(menuItem);
	fileMenu.addSeparator();
	
	menuItem = new JMenuItem(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().menuItem.text.reload"));  //NOI18N
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		TreePath         path = treeTable.getTree().getSelectionPath();

		if (path != null) {
		    model.stopLoading();
		    reload(path.getLastPathComponent());
		}
	    }
	});
	fileMenu.add(menuItem);

	menuItem = new JMenuItem(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().menuItem.text.stop"));  //NOI18N
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		model.stopLoading();
	    }
	});
	fileMenu.add(menuItem);

	fileMenu.addSeparator();

	menuItem = new JMenuItem(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().menuItem.text.exit"));   //NOI18N
	menuItem.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent ae) {
		System.exit(0);
	    }
	});
	fileMenu.add(menuItem); 


	// Create a menu bar
	JMenuBar        menuBar = new JMenuBar();

	menuBar.add(fileMenu);

	// Menu for the look and feels (lafs).
	UIManager.LookAndFeelInfo[]        lafs = UIManager.
	                                    getInstalledLookAndFeels();
	ButtonGroup                        lafGroup = new ButtonGroup();

	JMenu          optionsMenu = new JMenu(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.createMenuBar().optionsMenu.title"));  //NOI18N

	menuBar.add(optionsMenu);

	for(int i = 0; i < lafs.length; i++) {
	    JRadioButtonMenuItem rb = new JRadioButtonMenuItem(lafs[i].
							       getName()); 
	    optionsMenu.add(rb);
	    rb.setSelected(UIManager.getLookAndFeel().getName().equals
			   (lafs[i].getName()));
	    rb.putClientProperty("UIKey", lafs[i]);     //NOI18N
	    rb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent ae) {
	            JRadioButtonMenuItem rb2 = (JRadioButtonMenuItem)ae.
			                       getSource();
	            if(rb2.isSelected()) {
		        UIManager.LookAndFeelInfo       info =
                                      (UIManager.LookAndFeelInfo)
			               rb2.getClientProperty("UIKey");      //NOI18N
		        try {
		            UIManager.setLookAndFeel(info.getClassName());
		            SwingUtilities.updateComponentTreeUI(frame);
			}
			catch (Exception e) {
		             System.err.println("unable to set UI " +       //NOI18N
						e.getMessage());
			}
	            }
	        }
	    });
	    lafGroup.add(rb);
	}
	return menuBar;
    }

    /**
     * Invoked to reload the children of a particular node. This will
     * also restart the timer.
     */
    protected void reload(Object node) {
	model.reloadChildren(node);
	if (!timer.isRunning()) {
	    timer.start();
	}
    }

    /**
     * Updates the status label based on reloadRow.
     */
    protected void updateStatusLabel() {
	if (reloadPath != null) {
	    statusLabel.setText(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.statusLabel.text.reload", model.getPath
				(reloadPath.getLastPathComponent())));
	    if ((reloadCounter % 4) < 2) {
		statusLabel.setForeground(Color.red);
	    }
	    else {
		statusLabel.setForeground(Color.blue);
	    }
	}
	else if (!model.isReloading()) {
	    statusLabel.setText(org.openide.util.NbBundle.getMessage(TreeTableExample2.class,"TreeTableExample2.statusLabel.text.default", NumberFormat.getInstance().
				format(model.getTotalSize(model.getRoot()))));
	    statusLabel.setForeground(Color.black);
	}
    }


    /**
     * Reloader is the ActionListener used in the Timer. In response to
     * the timer updating it will reset the reloadRow/reloadPath and
     * generate the necessary event so that the display will update. It
     * also implements the TreeExpansionListener so that if the tree is
     * altered while loading the reloadRow is updated accordingly.
     */
    class Reloader implements ActionListener, TreeExpansionListener {
	public void actionPerformed(ActionEvent ae) {
	    if (!model.isReloading()) {
		// No longer loading.
		timer.stop();
		if (reloadRow != -1) {
		    generateChangeEvent(reloadRow);
		}
		reloadRow = -1;
		reloadPath = null;
	    }
	    else {
		// Still loading, see if paths changed.
		TreePath       newPath = model.getPathLoading();

		if (newPath == null) {
		    // Hmm... Will usually indicate the reload thread
		    // completed between time we asked if reloading.
		    if (reloadRow != -1) {
			generateChangeEvent(reloadRow);
		    }
		    reloadRow = -1;
		    reloadPath = null;
		}
		else {
		    // Ok, valid path, see if matches last path.
		    int        newRow = treeTable.getTree().getRowForPath
			                          (newPath);

		    if (newPath.equals(reloadPath)) {
			reloadCounter = (reloadCounter + 1) % 8;
			if (newRow != reloadRow) {
			    int             lastRow = reloadRow;

			    reloadRow = newRow;
			    generateChangeEvent(lastRow);
			}
			generateChangeEvent(reloadRow);
		    }
		    else {
			int          lastRow = reloadRow;

			reloadCounter = 0;
			reloadRow = newRow;
			reloadPath = newPath;
			if (lastRow != reloadRow) {
			    generateChangeEvent(lastRow);
			}
			generateChangeEvent(reloadRow);
		    }
		}
	    }
	    updateStatusLabel();
	}

	/**
	 * Generates and update event for the specified row. FileSystemModel2
	 * could do this, but it would not know when the row has changed
	 * as a result of expanding/collapsing nodes in the tree.
	 */
	protected void generateChangeEvent(int row) {
	    if (row != -1) {
		AbstractTableModel     tModel = (AbstractTableModel)treeTable.
		                                 getModel();

		tModel.fireTableChanged(new TableModelEvent
				       (tModel, row, row, 1));
	    }
	}

	//
	// TreeExpansionListener
	//

	/**
	 * Invoked when the tree has expanded.
	 */
	public void treeExpanded(TreeExpansionEvent te) {
	    updateRow();
	}

	/**
	 * Invoked when the tree has collapsed.
	 */
	public void treeCollapsed(TreeExpansionEvent te) {
	    updateRow();
	}

	/**
	 * Updates the reloadRow and path, this does not genernate a
	 * change event.
	 */
	protected void updateRow() {
	    reloadPath = model.getPathLoading();

	    if (reloadPath != null) {
		reloadRow = treeTable.getTree().getRowForPath(reloadPath);
	    }
	}
    }


    /**
     * A renderer that will give an indicator when a cell is being reloaded.
     */
    class IndicatorRenderer extends DefaultTableCellRenderer {
	/** Makes sure the number of displayed in an internationalized
	 * manner. */
	protected NumberFormat       formatter;
	/** Row that is currently being painted. */
	protected int                lastRow;
	

	IndicatorRenderer() {
	    setHorizontalAlignment(JLabel.RIGHT);
	    formatter = NumberFormat.getInstance();
	}

	/**
	 * Invoked as part of DefaultTableCellRenderers implemention. Sets
	 * the text of the label.
	 */
	public void setValue(Object value) { 
	    setText((value == null) ? "---" : formatter.format(value)); //NOI18N
	}

	/**
	 * Returns this.
	 */
	public Component getTableCellRendererComponent(JTable table,
			    Object value, boolean isSelected, boolean hasFocus,
			    int row, int column) {
	    super.getTableCellRendererComponent(table, value, isSelected,
						hasFocus, row, column);
	    lastRow = row;
	    return this;
	}

	/**
	 * If the row being painted is also being reloaded this will draw
	 * a little indicator.
	 */
	public void paint(Graphics g) {
	    if (lastRow == reloadRow) {
		int       width = getWidth();
		int       height = getHeight();

		g.setColor(getBackground());
		g.fillRect(0, 0, width, height);
		g.setColor(getForeground());

		int       diameter = Math.min(width, height);

		if (reloadCounter < 5) {
		    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
			      diameter, diameter, 90, -(reloadCounter * 90));
		}
		else {
		    g.fillArc((width - diameter) / 2, (height - diameter) / 2,
			      diameter, diameter, 90,
			      (4 - reloadCounter % 4) * 90);
		}
	    }
	    else {
		super.paint(g);
	    }
	}
    }


    public static void main(String[] args) {
	if (args.length > 0) {
	    for (int counter = args.length - 1; counter >= 0; counter--) {
		new TreeTableExample2(args[counter]);
	    }
	}
	else {
	    String            path;

	    try {
		path = System.getProperty("user.home");  //NOI18N
		if (path != null) {
		    new TreeTableExample2(path);
		}
	    }
	    catch (SecurityException se) {
		path = null;
	    }
	    if (path == null) {
		System.out.println("Could not determine home directory");  //NOI18N
	    }
	}
    }
}
