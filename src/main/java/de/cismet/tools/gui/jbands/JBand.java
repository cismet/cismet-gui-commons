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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.cismet.tools.gui.jbands.interfaces.Band;
import de.cismet.tools.gui.jbands.interfaces.BandMember;
import de.cismet.tools.gui.jbands.interfaces.BandMemberActionProvider;
import de.cismet.tools.gui.jbands.interfaces.BandMemberMouseListeningComponent;
import de.cismet.tools.gui.jbands.interfaces.BandMemberSelectable;
import de.cismet.tools.gui.jbands.interfaces.BandModel;
import de.cismet.tools.gui.jbands.interfaces.BandPrefixProvider;
import de.cismet.tools.gui.jbands.interfaces.BandWeightProvider;
import de.cismet.tools.gui.jbands.interfaces.Section;
import de.cismet.tools.gui.jbands.interfaces.Spot;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class JBand extends JPanel implements ActionListener, MouseListener, MouseMotionListener {

    //~ Static fields/initializers ---------------------------------------------

    public static final Dimension MINDIM = new Dimension(0, 0);

    //~ Instance fields --------------------------------------------------------

    HashMap<JComponent, BandMember> bandMembersViaComponents = new HashMap<JComponent, BandMember>();
    HashMap<BandMember, JComponent> componentsViaBandMembers = new HashMap<BandMember, JComponent>();
    private int maxPreferredPrefixWidth = 0;
    JPanel bandsPanel = new JPanel(null) {

            @Override
            public Dimension getPreferredSize() {
                final Dimension d = super.getPreferredSize();
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
                layoutBandMemberComponents();
            }
        };

    ArrayList<ActionListener> actionListeners = new ArrayList<ActionListener>();
    BandMember selectedBandMember = null;
    private final transient org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(this.getClass());
    private BandModel model;
    private double zoomFactor = 1.0;
    private JScrollPane scrollPane = new JScrollPane(bandsPanel);
    private float[] heightWeights;
    private double minValue = Double.MAX_VALUE;
    private double maxValue = Double.MIN_VALUE;
    private float heightsWeightSum = 0f;
    private double realWidth = 0;
    private int xoffset = 0;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JBand object.
     */
    public JBand() {
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        setOpaque(false);
        bandsPanel.setOpaque(false);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
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
        this.model = model;
        init();
    }

    /**
     * DOCUMENT ME!
     */
    private void init() {
        bandsPanel.removeAll();
        for (int row = 0; row < model.getNumberOfBands(); ++row) {
            final int cols = model.getBand(row).getNumberOfMembers();
            final Band rowBand = model.getBand(row);
            JComponent prefix = null;
            if (rowBand instanceof BandPrefixProvider) {
                prefix = ((BandPrefixProvider)rowBand).getPrefixComponent();
                bandsPanel.add(prefix);
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
                comp.addMouseListener(this);
                comp.addMouseMotionListener(this);
            }
        }

        heightWeights = new float[model.getNumberOfBands()];

        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band b = model.getBand(zeile);
            if (b instanceof BandWeightProvider) {
                heightWeights[zeile] = ((BandWeightProvider)b).getBandWeight();
            } else {
                heightWeights[zeile] = 1f;
            }
            if (b.getMax() > maxValue) {
                maxValue = b.getMax();
            }
            if (b.getMin() < minValue) {
                minValue = b.getMin();
            }
        }

        realWidth = maxValue - minValue;

        for (final float hw : heightWeights) {
            heightsWeightSum += hw;
        }

        layoutBandMemberComponents();
    }
    /**
     * DOCUMENT ME!
     */
    private void layoutBandMemberComponents() {
        if ((model == null) || (model.getNumberOfBands() == 0) || (bandsPanel.getWidth() <= 0)) {
            return;
        }

        int posy = 0;

        // prefixes
        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band rowBand = model.getBand(zeile);
            final int memberHeight = (int)(((double)bandsPanel.getHeight()) * heightWeights[zeile] / heightsWeightSum);

            if (rowBand instanceof BandPrefixProvider) {
                final JComponent prefix = ((BandPrefixProvider)rowBand).getPrefixComponent();
                prefix.setBounds(0, posy, maxPreferredPrefixWidth, memberHeight);
                xoffset = (xoffset > prefix.getPreferredSize().width) ? xoffset : prefix.getPreferredSize().width;
            }
            posy += memberHeight;
        }

        // BandMember
        posy = 0;
        for (int zeile = 0; zeile < model.getNumberOfBands(); ++zeile) {
            final Band b = model.getBand(zeile);
            int memberHeight = (int)(((double)bandsPanel.getHeight()) * heightWeights[zeile] / heightsWeightSum);

            final ArrayList<ArrayList<BandMember>> subBands = new ArrayList<ArrayList<BandMember>>();
            final ArrayList<BandMember> masterBand = new ArrayList<BandMember>();
            for (int i = 0; i < b.getNumberOfMembers(); ++i) {
                masterBand.add(b.getMember(i));
            }

            for (int i = 0; i < b.getNumberOfMembers(); ++i) {
                for (int j = i; j < b.getNumberOfMembers(); ++j) {
                    final BandMember bi = b.getMember(i);
                    final BandMember bj = b.getMember(j);
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
            if (subBands.size() > 0) {
                memberHeight = (int)(memberHeight / (1 + subBands.size()));
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
     * DOCUMENT ME!
     *
     * @param   a  DOCUMENT ME!
     * @param   b  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private boolean isColliding(final BandMember a, final BandMember b) {
        if ((a instanceof Spot) || (b instanceof Spot)) {
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
        } else if ((a instanceof Section) && (b instanceof Section)) {
            return (!((a.getMax() <= b.getMin()) || (a.getMin() >= b.getMax())));
        }
        assert (false);
        return false;
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
        final double widthFactor = ((double)bandsPanel.getWidth() - xoffset) / realWidth;
        double memberRealWidth = 0;
        if (member instanceof Section) {
            memberRealWidth = member.getMax() - member.getMin();
            final int memberWidth = Math.max((int)((memberRealWidth * widthFactor) + 0.5d), 1);
            final int posx = (int)((member.getMin() * widthFactor) + 0.5d) + xoffset;
            return new Rectangle(posx, posy, memberWidth, memberHeight);
        } else if (member instanceof Spot) {
            final int memberWidth = comp.getPreferredSize().width;
            final int posx = (int)((member.getMin() * widthFactor) + 0.5d - (memberWidth / 2d)) + xoffset;
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
        return zoomFactor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  zoomFactor  DOCUMENT ME!
     */
    public void setZoomFactor(final double zoomFactor) {
        this.zoomFactor = zoomFactor;
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
        if (e.getComponent() instanceof BandMemberSelectable) {
            final BandMemberSelectable selecteable = (BandMemberSelectable)e.getComponent();
            final BandMember oldBelectedBandMember = selectedBandMember;

            if (e.getClickCount() == 1) {
                if (!e.isPopupTrigger()) {
                    if (selecteable.isSelectable() && !selecteable.isSelected()
                                && !(selecteable == selectedBandMember)) {
                        selecteable.setSelected(true);
                        if (selectedBandMember != null) {
                            ((BandMemberSelectable)selectedBandMember).setSelected(false);
                        }
                        selectedBandMember = selecteable.getBandMember();
                    } else {
                        selecteable.setSelected(false);
                        selectedBandMember = null;
                    }
                    fireActionPerformed();
                } else {
                    // Popup
                }
            }
        }

        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseClicked(e);
        }
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
        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(final MouseEvent e) {
        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseReleased(e);
        }
    }

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (e instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseDragged(e);
        }
    }

    @Override
    public void mouseMoved(final MouseEvent e) {
        if (e.getComponent() instanceof BandMemberMouseListeningComponent) {
            ((BandMemberMouseListeningComponent)e.getComponent()).mouseMoved(e);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        // reagieren auf ActionEvents von BandMembern
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final JFrame jf = new JFrame("Test");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.getContentPane().setLayout(new BorderLayout());

        final JBand jbdTest = new JBand();

        final SimpleBand sb0 = new SimpleBand();

        sb0.addMember(new SimpleTextSection("OTOL-800", 0, 300, true, false));
        sb0.addMember(new SimpleTextSection("OTOL-8700", 300, 500, false, false));
        sb0.addMember(new SimpleTextSection("OTOL-900", 500, 1010, false, true));

        final SimpleBand sb1 = new SimpleBand();
        sb1.addMember(new SimpleSection(0, 10));
        int from = 10;
        int to = 20;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));
        from += 10;
        to += 10;
        sb1.addMember(new SimpleSection(from, to));

        sb1.addMember(new SimpleSection(310, 1000));
        sb1.addMember(new SimpleSection(1000, 1010));

        final SimpleBand sb2 = new SimpleBand();
        sb2.addMember(new SimpleSection(0, 10));
        sb2.addMember(new SimpleSection(10, 20));
        sb2.addMember(new SimpleSection(20, 80));
        sb2.addMember(new SimpleSection(80, 100));

        final SimpleBand sb3 = new SimpleBand();
        sb3.addMember(new SimpleSection(0, 50));
        sb3.addMember(new SimpleSection(50, 300));
        sb3.addMember(new SimpleSection(300, 1010));
        final SimpleBand sb4 = new SimpleBand();

        sb4.addMember(new SimpleSpot(100));

        sb4.addMember(new SimpleSpot(200));

        sb4.addMember(new SimpleSpot(300));

        sb4.addMember(new SimpleSpot(400));

        sb4.addMember(new SimpleSpot(500));

        final SimpleBandModel sbm = new SimpleBandModel();
        sbm.addBand(sb0);
        sbm.addBand(sb1);
        sbm.addBand(new SimpleBand());
        sbm.addBand(sb2);
        sbm.addBand(new SimpleBand());
        sbm.addBand(sb3);
        sbm.addBand(sb4);

        final SimpleBand sb5 = new SimpleBand();
        sb5.addMember(new SimpleSection(0, 1010));
        sb5.addMember(new SimpleSection(50, 500));
        sb5.addMember(new SimpleSection(400, 600));
        sbm.addBand(sb5);

        jbdTest.setModel(sbm);
        jbdTest.setZoomFactor(2);
//        jf.getContentPane().setBackground(Color.red);
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
        jsl.setValue(0);
        jf.getContentPane().add(jsl, BorderLayout.SOUTH);
        jf.setSize(300, 400);
        jf.setVisible(true);
        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        jf.setBounds((screenSize.width - 800) / 2, (screenSize.height - 222) / 2, 800, 222);
    }
}
