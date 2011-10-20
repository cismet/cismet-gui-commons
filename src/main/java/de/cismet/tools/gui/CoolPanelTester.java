/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 thorsten
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * CoolPanelTester.java
 *
 * Created on 24.02.2011, 20:58:30
 */
package de.cismet.tools.gui;

import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.geom.Point2D;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class CoolPanelTester extends javax.swing.JFrame {

    //~ Static fields/initializers ---------------------------------------------

    public static final GradientPaint BLUE_EXPERIENCE = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(168, 204, 241),
            new Point2D.Double(0, 1),
            new Color(44, 61, 146));
    public static final GradientPaint MAC_OSX_SELECTED = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(81, 141, 236),
            new Point2D.Double(0, 1),
            new Color(36, 96, 192));
    public static final GradientPaint MAC_OSX = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(167, 210, 250),
            new Point2D.Double(0, 1),
            new Color(99, 147, 206));
    public static final GradientPaint AERITH = new GradientPaint(
            new Point2D.Double(0, 0),
            Color.WHITE,
            new Point2D.Double(
                0,
                1),
            new Color(64, 110, 161));
    public static final GradientPaint GRAY = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(226, 226, 226),
            new Point2D.Double(0, 1),
            new Color(250, 248, 248));
    public static final GradientPaint RED_XP = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(236, 81, 81),
            new Point2D.Double(0, 1),
            new Color(192, 36, 36));
    public static final GradientPaint NIGHT_GRAY = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(102, 111, 127),
            new Point2D.Double(0, 1),
            new Color(38, 45, 61));
    public static final GradientPaint NIGHT_GRAY_LIGHT = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(129, 138, 155),
            new Point2D.Double(0, 1),
            new Color(58, 66, 82));
    public static final GradientPaint HELL = new GradientPaint(
            new Point2D.Double(0, 0),
            new Color(129, 138, 155),
            new Point2D.Double(0, 100),
            new Color(222, 222, 222));
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JRadioButton jRadioButton1;
    private org.jdesktop.swingx.JXPanel p;
    // End of variables declaration//GEN-END:variables

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates new form CoolPanelTester.
     */
    public CoolPanelTester() {
        initComponents();
//        PureCoolPanel p=new PureCoolPanel();
//        p.setPanTitle(new JPanel());
//        p.getPanTitle().add(new JLabel("jhdskaflkasdjh"));
        // JXPanel p = new JXPanel();
        p.setPreferredSize(new Dimension(300, 400));

//        RectanglePainter rp=new RectanglePainter(10,10,10,10,20,20);
        final RectanglePainter rp = new RectanglePainter(HELL, Color.BLACK, 1, null);

        rp.setRounded(true);
        rp.setRoundHeight(20);
        rp.setRoundWidth(20);
        final ShadowPathEffect spe = new ShadowPathEffect();
        spe.setOffset(new Point2D.Float(5, 5));
        spe.setBrushColor(Color.gray);
        spe.setEffectWidth(5);

        rp.setAreaEffects(spe);

        rp.setInsets(new Insets(15, 15, 15, 15));
        final CompoundPainter cp = new CompoundPainter(rp);
        p.setBackgroundPainter(cp);
        getContentPane().add(p);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The
     * content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        p = new org.jdesktop.swingx.JXPanel();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jRadioButton1 = new javax.swing.JRadioButton();

        jButton1.setText(org.openide.util.NbBundle.getMessage(CoolPanelTester.class, "CoolPanelTester.jButton1.text")); // NOI18N

        jCheckBox1.setText(org.openide.util.NbBundle.getMessage(
                CoolPanelTester.class,
                "CoolPanelTester.jCheckBox1.text")); // NOI18N

        jRadioButton1.setText(org.openide.util.NbBundle.getMessage(
                CoolPanelTester.class,
                "CoolPanelTester.jRadioButton1.text")); // NOI18N

        final org.jdesktop.layout.GroupLayout pLayout = new org.jdesktop.layout.GroupLayout(p);
        p.setLayout(pLayout);
        pLayout.setHorizontalGroup(
            pLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                pLayout.createSequentialGroup().addContainerGap(286, Short.MAX_VALUE).add(jButton1).add(
                    116,
                    116,
                    116)).add(
                pLayout.createSequentialGroup().add(113, 113, 113).add(jCheckBox1).addContainerGap(
                    280,
                    Short.MAX_VALUE)).add(
                org.jdesktop.layout.GroupLayout.TRAILING,
                pLayout.createSequentialGroup().addContainerGap(220, Short.MAX_VALUE).add(jRadioButton1).add(
                    159,
                    159,
                    159)));
        pLayout.setVerticalGroup(
            pLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(
                pLayout.createSequentialGroup().add(98, 98, 98).add(jButton1).add(44, 44, 44).add(jCheckBox1).add(
                    46,
                    46,
                    46).add(jRadioButton1).addContainerGap(115, Short.MAX_VALUE)));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        final java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 400) / 2, (screenSize.height - 322) / 2, 400, 322);
    } // </editor-fold>//GEN-END:initComponents

    /**
     * DOCUMENT ME!
     *
     * @param  args  the command line arguments
     */
    public static void main(final String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

                @Override
                public void run() {
                    new CoolPanelTester().setVisible(true);
                }
            });
    }
}
