/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.jbands;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.KeyStroke;
import javax.swing.RepaintManager;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.tools.gui.jbands.interfaces.Band;
import de.cismet.tools.gui.jbands.interfaces.BandAbsoluteHeightProvider;
import de.cismet.tools.gui.jbands.interfaces.BandMember;
import de.cismet.tools.gui.jbands.interfaces.BandMemberActionProvider;
import de.cismet.tools.gui.jbands.interfaces.BandMemberMouseListeningComponent;
import de.cismet.tools.gui.jbands.interfaces.BandMemberSelectable;
import de.cismet.tools.gui.jbands.interfaces.BandModel;
import de.cismet.tools.gui.jbands.interfaces.BandModelListener;
import de.cismet.tools.gui.jbands.interfaces.BandModificationProvider;
import de.cismet.tools.gui.jbands.interfaces.BandPostfixProvider;
import de.cismet.tools.gui.jbands.interfaces.BandPrefixProvider;
import de.cismet.tools.gui.jbands.interfaces.BandSnappingPointProvider;
import de.cismet.tools.gui.jbands.interfaces.BandWeightProvider;
import de.cismet.tools.gui.jbands.interfaces.Section;
import de.cismet.tools.gui.jbands.interfaces.Spot;
import de.cismet.tools.gui.jbands.interfaces.StationaryBandMemberMouseListeningComponent;
import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class JBand extends JPanel implements ActionListener,
    MouseListener,
    MouseMotionListener,
    BandModelListener,
    KeyListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final Dimension MINDIM = new Dimension(0, 0);

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public static enum SelectionMode {

        //~ Enum constants -----------------------------------------------------

        SINGLE_SELECTION, MULTIPLE_INTERVAL_SELECTION
    }

    //~ Instance fields --------------------------------------------------------

    HashMap<JComponent, BandMember> bandMembersViaComponents = new HashMap<JComponent, BandMember>();
    HashMap<BandMember, JComponent> componentsViaBandMembers = new HashMap<BandMember, JComponent>();
    JBandsPanel bandsPanel = new JBandsPanel();
    JLegendPanel legendPanel = new JLegendPanel();
    JLegendPanel postfixPanel = new JLegendPanel();

    ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    List<BandMemberSelectable> selectedBandMember = new ArrayList<BandMemberSelectable>();
    int count = 0;
    private int maxPreferredPrefixWidth = 0;
    private int maxPreferredPostfixWidth = 0;
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private BandModel model;
    private double zoomFactor = 1.0;
    private JScrollPane scrollPane = new JScrollPane(bandsPanel);
    private float[] heightWeights;
    private double minValue = Double.MAX_VALUE;
    private double maxValue = Double.MIN_VALUE;
    private double additionalZoomFactor = 1;
    private float heightsWeightSum = 0f;
    private double realWidth = 0;
    private List<JBandYDimension> bandPosY = new ArrayList<JBandYDimension>();
    private Map<Band, ArrayList<ArrayList<BandMember>>> subBandMap =
        new HashMap<Band, ArrayList<ArrayList<BandMember>>>();
    private List<SnappingPoint> snappingPoints = new ArrayList<SnappingPoint>();
    private boolean readOnly = false;
    private boolean refreshAvoided = false;
    private Component lastPressedComponent = null;
    private boolean dragged = false;
    private SelectionMode selectionMode = SelectionMode.SINGLE_SELECTION;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JBand object.
     */
    public JBand() {
        this(false);
    }

    /**
     * Creates a new JBand object.
     *
     * @param  readOnly  DOCUMENT ME!
     */
    public JBand(final boolean readOnly) {
        this.readOnly = readOnly;
        setLayout(new BorderLayout());
        add(legendPanel, BorderLayout.LINE_START);
        add(scrollPane, BorderLayout.CENTER);
        add(postfixPanel, BorderLayout.LINE_END);
        setOpaque(false);
        bandsPanel.setOpaque(false);
        bandsPanel.addMouseMotionListener(this);
        bandsPanel.addMouseListener(this);
        bandsPanel.addKeyListener(this);
        legendPanel.addKeyListener(this);
        postfixPanel.addKeyListener(this);
        bandsPanel.getActionMap().put(KeyStroke.getKeyStroke('a'), new AbstractAction() {

                @Override
                public Object getValue(final String key) {
                    return null;
                }

                @Override
                public void putValue(final String key, final Object value) {
                }

                @Override
                public void setEnabled(final boolean b) {
                }

                @Override
                public boolean isEnabled() {
                    return true;
                }

                @Override
                public void addPropertyChangeListener(final PropertyChangeListener listener) {
                }

                @Override
                public void removePropertyChangeListener(final PropertyChangeListener listener) {
                }

                @Override
                public void actionPerformed(final ActionEvent e) {
                    System.out.println("drin");
                }
            });
        setFocusable(true);
        bandsPanel.setFocusable(true);
        legendPanel.setFocusable(true);
        postfixPanel.setFocusable(true);
        this.addKeyListener(this);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    /**
     * Creates a new JBand object.
     *
     * @param  model  DOCUMENT ME!
     */
    public JBand(final BandModel model) {
        this();
        setModel(model);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  the selectionMode
     */
    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  selectionMode  the selectionMode to set
     */
    public void setSelectionMode(final SelectionMode selectionMode) {
        this.selectionMode = selectionMode;
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        if (((e.getModifiers() & KeyEvent.CTRL_DOWN_MASK) != 0) && (e.getKeyChar() == 'a')) {
            for (int bandIndex = 0; bandIndex < model.getNumberOfBands(); ++bandIndex) {
                final Band b = model.getBand(bandIndex);

                for (int memberIndex = 0; memberIndex < b.getNumberOfMembers(); ++memberIndex) {
                    final BandMember member = b.getMember(memberIndex);

                    if (member instanceof BandMemberSelectable) {
                        final BandMemberSelectable bms = (BandMemberSelectable)member;

                        if (!selectedBandMember.contains(bms)) {
                            bms.setSelected(true);
                            selectedBandMember.add(bms);
                        }
                    }
                }
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(final KeyEvent e) {
    }

    @Override
    public void keyReleased(final KeyEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BandMember getSelectedBandMember() {
        if (selectedBandMember.size() > 0) {
            return selectedBandMember.get(0).getBandMember();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public List<BandMemberSelectable> getSelectedBandMemberList() {
        return selectedBandMember;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  horizontalScrollBarPolicy  DOCUMENT ME!
     */
    public void setHorizontalScrollBarPolicy(final int horizontalScrollBarPolicy) {
        scrollPane.setHorizontalScrollBarPolicy(horizontalScrollBarPolicy);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  model  DOCUMENT ME!
     */
    public void setModel(final BandModel model) {
        if (this.model != null) {
            this.model.removeBandModelListener(this);
        }
        this.model = model;
        model.addBandModelListener(this);
        init();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  readOnly  DOCUMENT ME!
     */
    public void setReadOnly(final boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        final double prefWidth = legendPanel.getPreferredSize().getWidth();
        minValue = Double.MAX_VALUE;
        maxValue = Double.MIN_VALUE;
        bandsPanel.removeAll();
        legendPanel.removeAll();

        for (int row = 0; row < model.getNumberOfBands(); ++row) {
            final int cols = model.getBand(row).getNumberOfMembers();
            final Band rowBand = model.getBand(row);
            JComponent prefix = null;
            if (rowBand instanceof BandPrefixProvider) {
                prefix = ((BandPrefixProvider)rowBand).getPrefixComponent();
                legendPanel.add(prefix);
                maxPreferredPrefixWidth = (maxPreferredPrefixWidth < prefix.getPreferredSize().width)
                    ? prefix.getPreferredSize().width : maxPreferredPrefixWidth;
            }

            for (int col = 0; col < cols; ++col) {
                final BandMember member = rowBand.getMember(col);
                final JComponent comp = member.getBandMemberComponent();
                if (member instanceof BandMemberActionProvider) {
                    ((BandMemberActionProvider)member).addActionListener(this);
                }
                bandsPanel.add(comp);
                bandMembersViaComponents.put(comp, member);
                componentsViaBandMembers.put(member, comp);

                if (!Arrays.asList(comp.getMouseListeners()).contains(this)) {
                    comp.addMouseListener(this);
                }

                if (!Arrays.asList(comp.getMouseMotionListeners()).contains(this)) {
                    comp.addMouseMotionListener(this);
                }
            }

            if (rowBand instanceof BandPostfixProvider) {
                final JComponent postfix = ((BandPostfixProvider)rowBand).getPostfixComponent();
                postfixPanel.add(postfix);
                final int pWidth = postfix.getPreferredSize().width;
                if (maxPreferredPostfixWidth < pWidth) {
                    maxPreferredPostfixWidth = pWidth;
                }
            }
        }

        heightWeights = new float[model.getNumberOfBands()];

        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band b = model.getBand(zeile);
            if (b instanceof BandWeightProvider) {
                heightWeights[zeile] = ((BandWeightProvider)b).getBandWeight();
            } else if (b instanceof BandAbsoluteHeightProvider) {
                heightWeights[zeile] = 0f;
            } else {
                heightWeights[zeile] = 1f;
            }
            if (b.getMax() > getMaxValue()) {
                setMaxValue(b.getMax());
            }
            if (b.getMin() < getMinValue()) {
                setMinValue(b.getMin());
            }
        }

        realWidth = getMaxValue() - getMinValue();

        layoutBandMemberComponents();

        if (legendPanel.getPreferredSize().getWidth() != prefWidth) {
            updateUI();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void layoutBandMemberComponents() {
        heightsWeightSum = 0;
        bandPosY.clear();
        snappingPoints.clear();

        if ((model == null) || (model.getNumberOfBands() == 0) || (bandsPanel.getWidth() <= 0)) {
            return;
        }

        int remainingBandsPanelHeight = bandsPanel.getHeight();

        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band rowBand = model.getBand(zeile);
            if (rowBand.isEnabled()) {
                heightsWeightSum += heightWeights[zeile];
                if (rowBand instanceof BandAbsoluteHeightProvider) {
                    remainingBandsPanelHeight -= ((BandAbsoluteHeightProvider)rowBand).getAbsoluteHeight();
                }

                // save snapping points
                if (rowBand instanceof BandSnappingPointProvider) {
                    for (int i = 0; i < rowBand.getNumberOfMembers(); ++i) {
                        final BandMember m = rowBand.getMember(i);
                        snappingPoints.add(new SnappingPoint(m.getMin(), m.getBandMemberComponent()));
                        snappingPoints.add(new SnappingPoint(m.getMax(), m.getBandMemberComponent()));
                    }
                }

                if (remainingBandsPanelHeight < 0) {
                    // TODO
                }
            }
        }

        // prefixes
        int posy = 0;
        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band rowBand = model.getBand(zeile);

            int memberHeight;
            if (!rowBand.isEnabled()) {
                memberHeight = 0;
            } else if ((heightWeights[zeile] == 0) && (rowBand instanceof BandAbsoluteHeightProvider)) {
                memberHeight = ((BandAbsoluteHeightProvider)rowBand).getAbsoluteHeight();
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            } else {
                memberHeight = (int)(((double)remainingBandsPanelHeight) * heightWeights[zeile] / heightsWeightSum);
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            }
            if (rowBand instanceof BandPrefixProvider) {
                final JComponent prefix = ((BandPrefixProvider)rowBand).getPrefixComponent();
                prefix.setBounds(0, posy, maxPreferredPrefixWidth, memberHeight);
            }
            if (memberHeight > 0) {
                bandPosY.add(new JBandYDimension(rowBand, posy, posy + memberHeight - 1));
            }
            posy += memberHeight;
        }

        // BandMember
        posy = 0;
        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band rowBand = model.getBand(zeile);
            int memberHeight;
            if (!rowBand.isEnabled()) {
                memberHeight = 0;
                for (int i = 0; i < rowBand.getNumberOfMembers(); ++i) {
                    rowBand.getMember(i).getBandMemberComponent().setBounds(new Rectangle(0, 0, 0, 0));
                }
                continue;
            } else if ((heightWeights[zeile] == 0) && (rowBand instanceof BandAbsoluteHeightProvider)) {
                memberHeight = ((BandAbsoluteHeightProvider)rowBand).getAbsoluteHeight();
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            } else {
                memberHeight = (int)(((double)remainingBandsPanelHeight) * heightWeights[zeile] / heightsWeightSum);
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            }
            final ArrayList<ArrayList<BandMember>> subBands = new ArrayList<ArrayList<BandMember>>();
            final ArrayList<BandMember> masterBand = new ArrayList<BandMember>();
            for (int i = 0; i < rowBand.getNumberOfMembers(); ++i) {
                masterBand.add(rowBand.getMember(i));
            }

            for (int i = 0; i < rowBand.getNumberOfMembers(); ++i) {
                for (int j = i; j < rowBand.getNumberOfMembers(); ++j) {
                    final BandMember bi = rowBand.getMember(i);
                    final BandMember bj = rowBand.getMember(j);
                    if ((i != j) && masterBand.contains(bi) && masterBand.contains(bj)) {
                        if (isColliding(bi, bj)) {
                            masterBand.remove(bj);
                            boolean added = false;
                            for (final ArrayList<BandMember> subBand : subBands) {
                                if (!hasCollisions(subBand, bj)) {
                                    subBand.add(bj);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                final ArrayList<BandMember> newSubBand = new ArrayList<BandMember>();
                                newSubBand.add(bj);
                                subBands.add(newSubBand);
                            }
                        }
                    }
                }
            }
            double roundingDifference = 0.0;
            if (subBands.size() > 0) {
                roundingDifference = ((double)memberHeight / (1.0 + (double)subBands.size()));
                memberHeight = (int)(memberHeight / (1 + subBands.size()));
                roundingDifference = roundingDifference - (double)memberHeight;
            }
            for (final BandMember bm : masterBand) {
                bm.getBandMemberComponent().setBounds(getBoundsOfComponent(bm, memberHeight, posy));
            }
            posy += memberHeight;
            for (final ArrayList<BandMember> subBand : subBands) {
                for (final BandMember bm : subBand) {
                    bm.getBandMemberComponent().setBounds(getBoundsOfComponent(bm, memberHeight, posy));
                }
                posy += memberHeight;
            }
            posy += roundingDifference * (1 + subBands.size());
            subBands.add(masterBand);
            subBandMap.put(rowBand, subBands);
        }

        // postfix
        posy = 0;
        for (int row = 0; row < model.getNumberOfBands(); ++row) {
            final Band rowBand = model.getBand(row);

            int memberHeight;
            if (!rowBand.isEnabled()) {
                memberHeight = 0;
            } else if ((heightWeights[row] == 0) && (rowBand instanceof BandAbsoluteHeightProvider)) {
                memberHeight = ((BandAbsoluteHeightProvider)rowBand).getAbsoluteHeight();
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            } else {
                memberHeight = (int)(((double)remainingBandsPanelHeight) * heightWeights[row] / heightsWeightSum);
                final int subCount = getSubbandCount(rowBand);
                if (memberHeight < subCount) {
                    memberHeight = subCount;
                }
            }
            if (rowBand instanceof BandPostfixProvider) {
                final JComponent prefix = ((BandPostfixProvider)rowBand).getPostfixComponent();
                prefix.setBounds(0, posy, maxPreferredPostfixWidth, memberHeight);
            }
            if (memberHeight > 0) {
                bandPosY.add(new JBandYDimension(rowBand, posy, posy + memberHeight - 1));
            }
            posy += memberHeight;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   members    DOCUMENT ME!
     * @param   candidate  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean hasCollisions(final ArrayList<BandMember> members, final BandMember candidate) {
        for (final BandMember bm : members) {
            if (isColliding(bm, candidate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the count of subbands for the given band.
     *
     * @param   band  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private int getSubbandCount(final Band band) {
        final ArrayList<ArrayList<BandMember>> subBands = new ArrayList<ArrayList<BandMember>>();
        final ArrayList<BandMember> masterBand = new ArrayList<BandMember>();
        for (int i = 0; i < band.getNumberOfMembers(); ++i) {
            masterBand.add(band.getMember(i));
        }

        for (int i = 0; i < band.getNumberOfMembers(); ++i) {
            for (int j = i; j < band.getNumberOfMembers(); ++j) {
                final BandMember bi = band.getMember(i);
                final BandMember bj = band.getMember(j);
                if ((i != j) && masterBand.contains(bi) && masterBand.contains(bj)) {
                    if (isColliding(bi, bj)) {
                        masterBand.remove(bj);
                        boolean added = false;
                        for (final ArrayList<BandMember> subBand : subBands) {
                            if (!hasCollisions(subBand, bj)) {
                                subBand.add(bj);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            final ArrayList<BandMember> newSubBand = new ArrayList<BandMember>();
                            newSubBand.add(bj);
                            subBands.add(newSubBand);
                        }
                    }
                }
            }
        }

        return subBands.size() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     *
     * @throws  IllegalArgumentException  DOCUMENT ME!
     */
    private boolean isColliding(final BandMember a, final BandMember b) {
        if ((a instanceof Section) && (b instanceof Section)) {
            return (!((a.getMax() <= b.getMin()) || (a.getMin() >= b.getMax())));
        } else if ((a instanceof Spot) || (b instanceof Spot)) {
            // dummy werte
            final int memberHeight = 10;
            final int posy = 10;
            final Rectangle ri = getBoundsOfComponent(a, memberHeight, posy);
            final Rectangle rj = getBoundsOfComponent(b, memberHeight, posy);
            final int rimax = ri.x + ri.width;
            final int rimin = ri.x;
            final int rjmax = rj.x + rj.width;
            final int rjmin = rj.x;
            return (!((rimax <= rjmin) || (rimin >= rjmax)));
        }
        throw new IllegalArgumentException("Possible Combinations are Section,Section and Spot,Spot");
    }

    /**
     * DOCUMENT ME!
     *
     * @param   member        DOCUMENT ME!
     * @param   memberHeight  DOCUMENT ME!
     * @param   posy          DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private Rectangle getBoundsOfComponent(final BandMember member,
            final int memberHeight,
            final int posy) {
        final JComponent comp = member.getBandMemberComponent();
        final double widthFactor = ((double)bandsPanel.getWidth()) / realWidth;

        if (member instanceof Section) {
            final int posx = (int)(((member.getMin() - minValue) * widthFactor) + 0.5d);
            final int lastPosX = (int)(((member.getMax() - minValue) * widthFactor) + 0.5d);
            final int memberWidth = Math.max(lastPosX - posx, 1);
            return new Rectangle(posx, posy, memberWidth, memberHeight);
        } else if (member instanceof Spot) {
            final int memberWidth = comp.getPreferredSize().width;
            final int posx = (int)(((member.getMin() - minValue) * widthFactor) + 0.5d - (memberWidth / 2d));
            return new Rectangle(posx, posy, memberWidth, memberHeight);
        }
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public double getZoomFactor() {
        return zoomFactor / additionalZoomFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  zoomFactor  DOCUMENT ME!
     */
    public void setZoomFactor(double zoomFactor) {
        zoomFactor = zoomFactor * additionalZoomFactor;
        final double myZoomFactor = zoomFactor / this.zoomFactor;
        this.zoomFactor = zoomFactor;
        setRefreshAvoided(true);
        final RepaintManager rm = RepaintManager.currentManager(bandsPanel);
        rm.markCompletelyClean(bandsPanel);

        scrollPane.getViewport().revalidate();

        if (!selectedBandMember.isEmpty()) {
            final double relTargetPosition = ((selectedBandMember.get(0).getBandMember().getMin() - minValue)
                            / (maxValue - minValue));
            final double newJBandWidth = scrollPane.getWidth() * 0.9 * zoomFactor; //
            final double absTargetPosition = newJBandWidth / myZoomFactor * relTargetPosition;
            final double currentXOffset = scrollPane.getViewport().getViewPosition().getX();
            final double currentAbsTargetViewPosition = absTargetPosition - currentXOffset;
            final double newOffset = ((currentAbsTargetViewPosition + currentXOffset) * myZoomFactor)
                        - currentAbsTargetViewPosition;

            final Rectangle r = scrollPane.getViewportBorderBounds();
            final Point newPosition = new Point((int)(newOffset), (int)r.getY());
            scrollPane.getViewport().setViewPosition(newPosition);
        }
        setRefreshAvoided(false);
        scrollPane.getViewport().revalidate();
    }

    /**
     * DOCUMENT ME!
     */
    public void fireActionPerformed() {
        final ActionEvent e = new ActionEvent(this, 1, "selectionChanged");
        log.info("BandMemberAction Performed");
        for (final ActionListener al : actionListeners) {
            al.actionPerformed(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void addActionListener(final ActionListener listener) {
        actionListeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  listener  DOCUMENT ME!
     */
    public void removeActionListener(final ActionListener listener) {
        actionListeners.remove(listener);
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        if (!readOnly && (e.getClickCount() == 2)) {
            if (e.getComponent() == bandsPanel) {
                final int y = e.getY();
                int startY = 0;
                int bandHeight = 0;
                Band targetBand = null;

                for (final JBandYDimension tmp : bandPosY) {
                    if ((y >= tmp.getyMin()) && (y <= tmp.getyMax())) {
                        targetBand = tmp.getBand();
                        startY = tmp.getyMin();
                        bandHeight = tmp.getyMax() - tmp.getyMin();
                        break;
                    }
                }

                if (targetBand != null) {
                    if (targetBand instanceof BandModificationProvider) {
                        final double station = getSationForXValue(e.getX());
                        final ArrayList<ArrayList<BandMember>> subBands = subBandMap.get(targetBand);
                        int n = (y - startY) / (bandHeight / subBands.size());

                        --n;
                        // the first band is a the end of the list
                        if (n == -1) {
                            n = subBands.size() - 1;
                        }
                        ((BandModificationProvider)targetBand).addMember(
                            station,
                            null,
                            getMinValue(),
                            getMaxValue(),
                            subBands.get(n));
                    }
                }
            }
        } else {
            if (e.getComponent() instanceof BandMemberSelectable) {
                final BandMemberSelectable selecteable = (BandMemberSelectable)e.getComponent();
                JBandCursorManager.getInstance().setCursor(this);

                if (e.getClickCount() == 1) {
                    if (!e.isPopupTrigger()) {
                        if ((selectionMode == SelectionMode.MULTIPLE_INTERVAL_SELECTION)
                                    && !(e.isShiftDown() || e.isControlDown())
                                    && (selectedBandMember.size() > 1)) {
                            deselectAllBandMember();
                        }

                        if (selecteable.isSelectable() && !selecteable.isSelected()
                                    && !selectedBandMember.contains(selecteable)) {
                            // select band member
                            selecteable.setSelected(true);
                            if (!selectedBandMember.isEmpty()
                                        && !((selectionMode == SelectionMode.MULTIPLE_INTERVAL_SELECTION)
                                            && (e.isShiftDown() || e.isControlDown()))) {
                                deselectAllBandMember();
                            }

                            if ((selectionMode == SelectionMode.MULTIPLE_INTERVAL_SELECTION) && e.isShiftDown()
                                        && !selectedBandMember.isEmpty()) {
                                final Band targetBand = getBandForYCoordinate(e.getY());
                                final BandMember bm = selectedBandMember.get(selectedBandMember.size() - 1)
                                            .getBandMember();
                                final double min = Math.min(bm.getMin(), selecteable.getBandMember().getMin());
                                final double max = Math.max(bm.getMax(), selecteable.getBandMember().getMax());
                                final List<BandMember> members = getAllBandMembersBetween(targetBand, min, max);
                                selectedBandMember.add(selecteable);
                                Collections.sort(members, new BandMemberComparator());

                                for (final BandMember member : members) {
                                    if (member instanceof BandMemberSelectable) {
                                        final BandMemberSelectable bms = (BandMemberSelectable)member;

                                        if (!selectedBandMember.contains(bms)) {
                                            bms.setSelected(true);
                                            selectedBandMember.add(bms);
                                        }
                                    }
                                }

                                repaint();
                            } else {
                                selectedBandMember.add(selecteable);
                            }
                        } else {
                            // deselect band member
                            if (!selectedBandMember.isEmpty()
                                        && !((selectionMode == SelectionMode.MULTIPLE_INTERVAL_SELECTION)
                                            && (e.isShiftDown() || e.isControlDown()))) {
                                deselectAllBandMember();
                            }

                            if (selecteable.isSelected()) {
                                selecteable.setSelected(false);
                            }
                            selectedBandMember.remove(selecteable);

                            repaint();
                        }

                        if (model instanceof SimpleBandModel) {
                            final SimpleBandModel sbm = ((SimpleBandModel)model);
                            final BandModelEvent event = new BandModelEvent();
                            event.setSelectionLost(!(e.isShiftDown() || e.isControlDown()));
                            sbm.fireBandModelSelectionChanged(event);
                        }
                    } else {
                        // Popup
                    }
                }
            }
        }
        if ((e.getComponent() instanceof BandMemberMouseListeningComponent)) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseClicked(e);
        }
    }

    /**
     * Deselects all selected band member.
     */
    private void deselectAllBandMember() {
        for (final BandMemberSelectable tmp : selectedBandMember) {
            tmp.setSelected(false);
        }

        selectedBandMember.clear();
        repaint();
    }

    /**
     * Determies the band that conains the given y coordinate.
     *
     * @param   y  DOCUMENT ME!
     *
     * @return  the band at the position of the given y coordinate
     */
    private Band getBandForYCoordinate(final int y) {
        Band targetBand = null;

        for (final JBandYDimension tmp : bandPosY) {
            if ((y >= tmp.getyMin()) && (y <= tmp.getyMax())) {
                targetBand = tmp.getBand();
                break;
            }
        }

        return targetBand;
    }

    /**
     * Determines all band members, which intersects the given x value range.
     *
     * @param   band    the band that should check its members
     * @param   startX  the start of the x range
     * @param   endX    the end of the x range
     *
     * @return  all band members, which intersects the given x value range.
     */
    private List<BandMember> getAllBandMembersBetween(final Band band, final double startX, final double endX) {
        final List<BandMember> bandMembers = new ArrayList<BandMember>();

        for (int i = 0; i < band.getNumberOfMembers(); ++i) {
            final BandMember member = band.getMember(i);

            if (((startX < member.getMin()) && (member.getMin() < endX))
                        || ((startX < member.getMax()) && (member.getMax() < endX))) {
                bandMembers.add(member);
            }
        }

        return bandMembers;
    }

    /**
     * Select the given band member.
     *
     * @param  selecteable  The band member to select
     */
    public void setSelectedMember(final BandMemberSelectable selecteable) {
        final List<BandMemberSelectable> l = new ArrayList<BandMemberSelectable>();

        if (selecteable != null) {
            l.add(selecteable);
        }

        setSelectedMember(l);
    }

    /**
     * Select the given band member. Even if the selection mode is SINGLE_SELECTION, it is possible to select more than
     * one band member with this method.
     *
     * @param  selecteable  The band member to select
     */
    public void setSelectedMember(final List<BandMemberSelectable> selecteable) {
        List<BandMemberSelectable> newSelection = selecteable;

        if (selecteable == null) {
            newSelection = new ArrayList<BandMemberSelectable>();
        }

        for (final BandMemberSelectable tmp : new ArrayList<BandMemberSelectable>(selectedBandMember)) {
            if (!newSelection.contains(tmp)) {
                tmp.setSelected(false);
                selectedBandMember.remove(tmp);
            }
        }

        for (final BandMemberSelectable tmp : newSelection) {
            if (tmp.isSelectable() && !tmp.isSelected() && !selectedBandMember.contains(tmp)) {
                tmp.setSelected(true);
                selectedBandMember.add(tmp);
            }
        }

        repaint();

        if (model instanceof SimpleBandModel) {
            final SimpleBandModel sbm = ((SimpleBandModel)model);
            final BandModelEvent e = new BandModelEvent();
            e.setSelectionLost(true);
            sbm.fireBandModelSelectionChanged(e);
        }
    }

    @Override
    public void setSize(final int width, final int height) {
        super.setSize(width, height);
        scrollPane.setSize(width, height);
        bandsPanel.setSize(width, height);
    }

    @Override
    public void bandModelSelectionChanged(final BandModelEvent e) {
    }

    @Override
    public void mouseEntered(final MouseEvent e) {
        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseEntered(e);
        }
    }

    @Override
    public void mouseExited(final MouseEvent e) {
        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseExited(e);
        }
    }

    @Override
    public void mousePressed(final MouseEvent e) {
        if (!readOnly && (e.getComponent() instanceof BandMemberMouseListeningComponent)) {
            lastPressedComponent = e.getComponent();
            ((BandMemberMouseListeningComponent)e.getComponent()).mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        dragged = false;
        if (!readOnly && (e.getComponent() instanceof BandMemberMouseListeningComponent)) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (JBandCursorManager.getInstance().isLocked()) {
            JBandCursorManager.getInstance().setCursor(this);
        }
        dragged = true;
        if (!readOnly && (e.getComponent() instanceof BandMemberMouseListeningComponent)) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseDragged(e);
        }

        if (!readOnly && (e.getComponent() instanceof StationaryBandMemberMouseListeningComponent)) {
            int x = 0;
            if (e.getSource() == bandsPanel) {
                x = e.getX();
            } else {
                x = (int)((Component)e.getSource()).getBounds().getX() + e.getX();
            }
            double station = getSationForXValue(x);

            if (station < getMinValue()) {
                station = getMinValue();
            } else if (station > getMaxValue()) {
                station = getMaxValue();
            }

            station = considerSnapping(station, e.getComponent());

            ((StationaryBandMemberMouseListeningComponent)e.getComponent()).mouseDragged(e, station);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   station  DOCUMENT ME!
     * @param   c        DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private double considerSnapping(final double station, final Component c) {
        int dist = (int)((maxValue - minValue) / (100 * ((zoomFactor == 0) ? 1 : zoomFactor)));
        if (dist < 1) {
            dist = 1;
        }
        for (final SnappingPoint tmp : snappingPoints) {
            if ((Math.abs(tmp.value - station) < (dist)) && (tmp.c != c)) {
                return tmp.value;
            }
        }

        return station;
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (e.isAltDown()) {
            final Component source = e.getComponent();
            int x = 0;
            if (source == bandsPanel) {
                x = e.getX();
            } else {
                x = (int)source.getBounds().getX()
                            + e.getX();
            }
            if (!bandsPanel.isMeasurementEnabled()) {
                bandsPanel.setMeasurementEnabled(true);
            }
            bandsPanel.setMeasurementx(x);

//            System.out.println("x"+first.getBounds());
        } else {
            if (bandsPanel.isMeasurementEnabled()) {
                bandsPanel.setMeasurementEnabled(false);
            }
        }
        if (JBandCursorManager.getInstance().isLocked()) {
            JBandCursorManager.getInstance().setCursor(this);
        } else {
            JBandCursorManager.getInstance().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            JBandCursorManager.getInstance().setCursor(this);
        }
        if ((e.getComponent() instanceof BandMemberMouseListeningComponent)) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseMoved(e);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // reagieren auf ActionEvents von BandMembern
    }

    @Override
    public void bandModelChanged(final BandModelEvent e) {
        if (!refreshAvoided) {
            init();
            if ((e != null) && e.isSelectionLost()) {
                selectedBandMember = null;
            }
            EventQueue.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        bandsPanel.repaint();
                    }
                });
        }
    }

    @Override
    public void bandModelValuesChanged(final BandModelEvent e) {
        if (!refreshAvoided) {
            if ((e != null) && e.isSelectionLost()) {
                selectedBandMember = null;
            }
            layoutBandMemberComponents();
            repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public BandModel getModel() {
        return model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final JFrame jf = new JFrame("Test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().setLayout(new BorderLayout());

        final JBand jbdTest = new JBand();

        final MinimumHeightBand sb0 = new MinimumHeightBand();

        sb0.addMember(new SimpleTextSection("OTOL-800", 0, 300, true, false));
        sb0.addMember(new SimpleTextSection("OTOL-8700", 300, 500, false, false));
        sb0.addMember(new SimpleTextSection("OTOL-900", 500, 1000, false, true));

        final DefaultBand sb1 = new DefaultBand("Selektierbar");

        sb1.addMember(new SelectableSectionPanel(250, 1000));

        final SimpleBand sb2 = new SimpleBand();
        sb2.addMember(new SimpleSection(0, 10));
        sb2.addMember(new SimpleSection(10, 20));
        sb2.addMember(new SimpleSection(20, 80));
        sb2.addMember(new SimpleSection(80, 100));

        final SimpleBand sb3 = new SimpleBand();
        sb3.addMember(new SimpleSection(0, 50));
        sb3.addMember(new SimpleSection(50, 300));
        sb3.addMember(new SimpleSection(300, 1000));
        final SimpleBand sb4 = new SimpleBand("Punkte .......");

        sb4.addMember(new SimpleSpot(100));

        sb4.addMember(new SimpleSpot(200));

        sb4.addMember(new SimpleSpot(300));

        sb4.addMember(new SimpleSpot(400));

        sb4.addMember(new SimpleSpot(500));

        final SimpleBand sb4a = new SimpleBand("auch");

        sb4a.addMember(new SimpleSection(100, 100));

        sb4a.addMember(new SimpleSection(110, 110));

        sb4a.addMember(new SimpleSection(300, 300));

        sb4a.addMember(new SimpleSection(400, 400));

        sb4a.addMember(new SimpleSection(500, 500));

        final SimpleBandModel sbm = new SimpleBandModel();
        sbm.addBand(sb0);
        sbm.addBand(sb1);
        // sbm.addBand(new EmptyWeightedBand(2f));
        sbm.addBand(new EmptyAbsoluteHeightedBand(1));
        sbm.addBand(sb2);
        sbm.addBand(new EmptyAbsoluteHeightedBand(1));

        final SimpleModifiableBand sb6 = new SimpleModifiableBand("Band");

        sb6.addMember(new SimpleModifiableBandMember(sb6, false, 0, 100));
        sb6.addMember(new SimpleModifiableBandMember(sb6, false, 200, 500));
        sb6.addMember(new SimpleModifiableBandMember(sb6, false, 600, 1000));

        sbm.addBand(sb3);
        sbm.addBand(sb4);
        sbm.addBand(sb4a);
        sbm.addBand(sb6);

        final SimpleBand sb5 = new SimpleBand();
        sb5.addMember(new SimpleSection(0, 1100));
        sb5.addMember(new SimpleSection(50, 500));
        sb5.addMember(new SimpleSection(400, 600));
        sbm.addBand(sb5);

        jbdTest.setModel(sbm);
        // jbdTest.setZoomFactor(2);
// jf.getContentPane().setBackground(Color.red);
        jbdTest.setBorder(new EmptyBorder(10, 10, 10, 10));
        jf.getContentPane().add(jbdTest, BorderLayout.CENTER);

        final JSlider jsl = new JSlider(0, 100);
        jsl.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(final ChangeEvent ce) {
                    final double zfAdd = jsl.getValue() / 10.0;
                    jbdTest.setZoomFactor(1 + zfAdd);
                }
            });

        final JCheckBox checker = new JCheckBox("test");
        checker.setSelected(true);
        checker.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    sb1.setEnabled(!sb1.isEnabled());
                    ((SimpleBandModel)(jbdTest.getModel())).fireBandModelValuesChanged();
                }
            });

        final JButton cmd2 = new JButton("2.0");
        cmd2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(final ActionEvent e) {
                    jbdTest.setZoomFactor(2d);
                }
            });
        // jf.getContentPane().add(checker, BorderLayout.NORTH);
        jf.getContentPane().add(cmd2, BorderLayout.NORTH);

        jsl.setValue(0);
        jf.getContentPane().add(jsl, BorderLayout.SOUTH);
        jf.setSize(300, 400);
        jf.setVisible(true);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        jf.setBounds((screenSize.width - 800) / 2, (screenSize.height - 222) / 2, 1000, 222);
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the refreshAvoided
     */
    public boolean isRefreshAvoided() {
        return refreshAvoided;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  refreshAvoided  the refreshAvoided to set
     */
    public void setRefreshAvoided(final boolean refreshAvoided) {
        this.refreshAvoided = refreshAvoided;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the minValue
     */
    public double getMinValue() {
        return minValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  minValue  the minValue to set
     */
    public void setMinValue(final double minValue) {
        this.minValue = minValue;
        setAdditionalZoomFactor();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  the maxValue
     */
    public double getMaxValue() {
        return maxValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  maxValue  the maxValue to set
     */
    public void setMaxValue(final double maxValue) {
        this.maxValue = maxValue;
        setAdditionalZoomFactor();
    }

    /**
     * DOCUMENT ME!
     */
    private void setAdditionalZoomFactor() {
        additionalZoomFactor = (maxValue - minValue) / 1000;
        if (additionalZoomFactor < 1) {
            additionalZoomFactor = 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param   x  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private double getSationForXValue(final int x) {
        return ((double)Math.round(((realWidth * (x) / (bandsPanel.getWidth())) + minValue)
                            * 10.0)) / 10.0;
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private static class SelectableSectionPanel extends JPanel implements Section, BandMember, BandMemberSelectable {

        //~ Instance fields ----------------------------------------------------

        private boolean selected;
        private double from = 0;
        private double to = 0;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates new form SimpleSectionPanel.
         *
         * @param  from  DOCUMENT ME!
         * @param  to    DOCUMENT ME!
         */
        public SelectableSectionPanel(final double from, final double to) {
            this(Color.getHSBColor((float)Math.random(), 0.85f, 1.0f), from, to);
        }

        /**
         * Creates a new SimpleSectionPanel object.
         *
         * @param  c     DOCUMENT ME!
         * @param  from  DOCUMENT ME!
         * @param  to    DOCUMENT ME!
         */
        public SelectableSectionPanel(final Color c, final double from, final double to) {
            initComponents();
            this.from = from;
            this.to = to;
            setBackground(c);
        }

        //~ Methods ------------------------------------------------------------

        /**
         * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code.
         * The content of this method is always regenerated by the Form Editor.
         */
        private void initComponents() {
            setBackground(new java.awt.Color(255, 51, 0));
            setOpaque(true);
            setPreferredSize(new java.awt.Dimension(1, 1));

            final org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
            this.setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 1, Short.MAX_VALUE));
            layout.setVerticalGroup(
                layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(0, 1, Short.MAX_VALUE));
        }

        @Override
        public boolean isSelected() {
            return selected;
        }

        @Override
        public void setSelected(final boolean selection) {
            this.selected = selection;
            if (selection) {
                setBackground(getBackground().darker());
            } else {
                setBackground(getBackground().brighter());
            }
        }

        @Override
        public boolean isSelectable() {
            return true;
        }

        @Override
        public BandMember getBandMember() {
            return this;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  from  DOCUMENT ME!
         */
        public void setFrom(final double from) {
            this.from = from;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  to  DOCUMENT ME!
         */
        public void setTo(final double to) {
            this.to = to;
        }

        @Override
        public double getMax() {
            return (from < to) ? to : from;
        }

        @Override
        public double getMin() {
            return (from < to) ? from : to;
        }

        @Override
        public JComponent getBandMemberComponent() {
            return this;
        }

        @Override
        public double getFrom() {
            return from;
        }

        @Override
        public double getTo() {
            return to;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class SnappingPoint implements Comparable<SnappingPoint> {

        //~ Instance fields ----------------------------------------------------

        private Double value;
        private Component c;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new SnappingPoint object.
         *
         * @param  value  DOCUMENT ME!
         * @param  c      DOCUMENT ME!
         */
        public SnappingPoint(final double value, final Component c) {
            this.value = value;
            this.c = c;
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public int compareTo(final SnappingPoint other) {
            final double o = other.value;

            if (Math.abs(value - o) < ((maxValue - minValue) / 100)) {
                return 0;
            } else {
                return value.compareTo(o);
            }
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the c
         */
        public Component getC() {
            return c;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  c  the c to set
         */
        public void setC(final Component c) {
            this.c = c;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class JBandYDimension {

        //~ Instance fields ----------------------------------------------------

        private Band band;
        private int yMin;
        private int yMax;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new JBandYDimension object.
         *
         * @param  band  DOCUMENT ME!
         * @param  yMin  DOCUMENT ME!
         * @param  yMax  DOCUMENT ME!
         */
        public JBandYDimension(final Band band, final int yMin, final int yMax) {
            this.band = band;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  the band
         */
        public Band getBand() {
            return band;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  band  the band to set
         */
        public void setBand(final Band band) {
            this.band = band;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the yMin
         */
        public int getyMin() {
            return yMin;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  yMin  the yMin to set
         */
        public void setyMin(final int yMin) {
            this.yMin = yMin;
        }

        /**
         * DOCUMENT ME!
         *
         * @return  the yMax
         */
        public int getyMax() {
            return yMax;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  yMax  the yMax to set
         */
        public void setyMax(final int yMax) {
            this.yMax = yMax;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class JLegendPanel extends JPanel {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new JBandsPanel object.
         */
        public JLegendPanel() {
            super(null);
            setOpaque(false);
            setForeground(new Color(50, 50, 50, 150));
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public Dimension getPreferredSize() {
            final Dimension d = super.getPreferredSize();
            double maxWidth = 100;
            for (int i = 0; i < getComponentCount(); ++i) {
                final int tmp = (int)getComponent(i).getPreferredSize().getWidth();
                if (tmp > maxWidth) {
                    maxWidth = tmp;
                }
            }
            maxWidth += 2;
            return new Dimension((int)maxWidth, d.height);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class JBandsPanel extends JPanel {

        //~ Instance fields ----------------------------------------------------

        private boolean measurementEnabled = false;
        private int measurementx = 0;
        private Color MIDDLE = new Color(50, 50, 50, 150);
        private Color SIDE = new Color(250, 250, 250, 150);
        private Color SIDER = new Color(220, 220, 220, 50);

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new JBandsPanel object.
         */
        public JBandsPanel() {
            super(null);
            setForeground(new Color(50, 50, 50, 150));
        }

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public boolean isMeasurementEnabled() {
            return measurementEnabled;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  measurementEnabled  DOCUMENT ME!
         */
        public void setMeasurementEnabled(final boolean measurementEnabled) {
            this.measurementEnabled = measurementEnabled;
            repaint();
        }

        /**
         * DOCUMENT ME!
         *
         * @return  DOCUMENT ME!
         */
        public int getMeasurementx() {
            return measurementx;
        }

        /**
         * DOCUMENT ME!
         *
         * @param  measurementx  DOCUMENT ME!
         */
        public void setMeasurementx(final int measurementx) {
            if (measurementx < 0) {
                this.measurementx = 0;
            } else {
                this.measurementx = measurementx;
            }
            if (measurementEnabled) {
                repaint();
            }
        }

        @Override
        public Dimension getPreferredSize() {
            final Dimension d = super.getPreferredSize();
//            return new Dimension((int)(scrollPane.getWidth() * 0.9 * zoomFactor)
//                            - (int)legendPanel.getPreferredSize().getWidth(),
//                    d.height);
            return new Dimension((int)(scrollPane.getWidth() * 0.9 * zoomFactor), d.height);
        }

        @Override
        public void setSize(final int width, final int height) {
            super.setSize(width, height);
            layoutBandMemberComponents();
        }

        @Override
        public void setSize(final Dimension d) {
            setSize(d.width, d.height);
        }

//        @Override
//        protected void paintComponent(Graphics g) {
//
//            super.paintComponent(g);
//
//
//        }
        @Override
        protected void paintChildren(final Graphics g) {
            super.paintChildren(g);
            if (measurementEnabled) {
                final Graphics2D g2d = (Graphics2D)g;
                final double station = getSationForXValue(measurementx);

                g.setColor(SIDER);
                g.drawLine(measurementx - 2, 0, measurementx - 2, getHeight());
                g.drawLine(measurementx + 2, 0, measurementx + 2, getHeight());
                g.setColor(SIDE);
                g.drawLine(measurementx - 1, 0, measurementx - 1, getHeight());
                g.drawLine(measurementx + 1, 0, measurementx + 1, getHeight());
                g.setColor(MIDDLE);
                g.drawLine(measurementx, 0, measurementx, getHeight());

                final String s = String.valueOf(station);
                final int sWidth = SwingUtilities.computeStringWidth(g.getFontMetrics(), s);
                final int sPos = measurementx
                            + 5;
                if ((sPos + sWidth) <= (getWidth() - 10)) {
                    g.drawString(s, measurementx + 5, getHeight() - 3);
                } else {
                    g.drawString(s, measurementx - sWidth - 5, getHeight() - 3);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private class BandMemberComparator implements Comparator<BandMember> {

        //~ Methods ------------------------------------------------------------

        @Override
        public int compare(final BandMember o1, final BandMember o2) {
            return (int)Math.signum(o1.getMin() - o2.getMin());
        }
    }
}
