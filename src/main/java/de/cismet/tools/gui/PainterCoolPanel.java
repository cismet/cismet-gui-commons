/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.graphics.ShadowRenderer;
import org.jdesktop.swingx.painter.AbstractAreaPainter.Style;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.HorizontalAlignment;
import org.jdesktop.swingx.painter.AbstractLayoutPainter.VerticalAlignment;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.ImagePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.InnerGlowPathEffect;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;

import org.openide.util.Exceptions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class PainterCoolPanel extends JXPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Color colorDarkLine = Color.black;
    private static final int IMAGE_TYPE = BufferedImage.TYPE_4BYTE_ABGR;

    //~ Instance fields --------------------------------------------------------

    private JComponent panTitle;
    private JComponent panInter;
    private boolean changeFlag = true;
    private int oldHeight;
    private int oldPanInterHeight;
    private int oldPanTitleHeight;
    private int offset = 6;
    private ImageIcon icons;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PainterCoolPanel object.
     */
    public PainterCoolPanel() {
        refreshPainter();
        oldHeight = getHeight();
        oldPanInterHeight = 0;
        oldPanTitleHeight = 0;
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);
        final int changeFactor = Math.abs(oldHeight - getHeight());
        if (oldHeight <= 0) {
            oldHeight = getHeight();
//            return;
        }
        final int percent = changeFactor * 100 / oldHeight;

        if ((panInter != null) && (panTitle != null)) {
            if ((oldPanInterHeight != panInter.getBounds().height)
                        || (oldPanTitleHeight != panTitle.getBounds().height)) {
                oldPanInterHeight = panInter.getBounds().height;
                oldPanTitleHeight = panTitle.getBounds().height;
                changeFlag = true;
            }
        }

        if (changeFlag || (percent > 10)) {
            changeFlag = false;
            oldHeight = getHeight();

            refreshPainter();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  panInter  DOCUMENT ME!
     */
    public void setPanInter(final JComponent panInter) {
        changeFlag = true;
        this.panInter = panInter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  panTitle  DOCUMENT ME!
     */
    public void setPanTitle(final JComponent panTitle) {
        changeFlag = true;
        this.panTitle = panTitle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getPanInter() {
        return panInter;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public JComponent getPanTitle() {
        return panTitle;
    }

    /**
     * Legt das Icon fest, die spaeter in die rechte obere Ecke des CoolPanels gezeichnet werden. Diese sollten das im
     * Navigator angewaehlte Objekt beschreiben. Icons werden nur gezeichnet, falls sie vorhanden sind. Um mehrere Icons
     * zu zeichnen, muessen diese mit der Methode Static2DTools.joinIcons() zusammengefuegt werden.
     *
     * @param  icon  das zu zeichnende Icon.
     */
    public void setImageRechtsOben(final ImageIcon icon) {
        if (icon != null) {
            try {
//            ReflectionRenderer renderer2 = new ReflectionRenderer(0.5f,0.4f,true);
                final ShadowRenderer renderer = new ShadowRenderer(3, 0.5f, Color.BLACK);

                final BufferedImage temp = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(),
                        IMAGE_TYPE);
                final Graphics tg = temp.createGraphics();
                tg.drawImage(icon.getImage(), 0, 0, null);
                tg.dispose();

                final BufferedImage shadow = renderer.createShadow(temp);

                final BufferedImage result = new BufferedImage(icon.getIconWidth() + (2 * 3),
                        icon.getIconHeight()
                                + (2 * 3),
                        IMAGE_TYPE);
                final Graphics rg = result.createGraphics();
                rg.drawImage(shadow, 0, 0, null);
                rg.drawImage(temp, 0, 0, null);
                rg.dispose();
                shadow.flush();

//            BufferedImage ref = renderer.appendReflection(tmp);
                final ImageIcon newIcon = new ImageIcon(result);
                this.icons = newIcon;
            } catch (Exception e) {
                this.icons = null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void refreshPainter() {
        if ((panInter == null) || (panTitle == null)) {
            return;
        }

        final float centerGradientOffset = (this.getHeight() - offset - (panInter.getBounds().height * 0.8f))
                    / (this.getHeight() - offset);

        final float[] dist = { 0.0f, centerGradientOffset, 1.0f };
        final Color[] colors = { new Color(66, 66, 66), new Color(102, 102, 102), new Color(66, 66, 66) };
        final LinearGradientPaint titleAndInterGradient = new LinearGradientPaint(new Point2D.Double(0, 0),
                new Point2D.Double(0, getHeight() - offset),
                dist,
                colors,
                CycleMethod.NO_CYCLE);

        /*
         * Outer round Rect
         */
        final RectanglePainter interAndTitle = new RectanglePainter(
                titleAndInterGradient,
                colorDarkLine,
                1,
                null);
        interAndTitle.setPaintStretched(true);
        interAndTitle.setRoundHeight(30);
        interAndTitle.setRoundWidth(30);
        interAndTitle.setRounded(true);
        interAndTitle.setInsets(new Insets(0, offset, offset, offset));
        /*
         * Icon
         */

        final ImagePainter ip = new ImagePainter();
        if (icons != null) {
            final BufferedImage img = (BufferedImage)icons.getImage();
            ip.setImage(img);
//            ip.setInsets(new Insets(offset, getWidth() - img.getWidth() - offset, 0, offset));
            ip.setInsets(new Insets(((panTitle.getBounds().height - img.getHeight()) / 2) + 3, 0, 0, 2 * offset));
            ip.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            ip.setVerticalAlignment(VerticalAlignment.TOP);
        }

        /*
         *  Shadow- Effect
         */

        final ShadowPathEffect outerShadow = new ShadowPathEffect();
        outerShadow.setOffset(new Point2D.Float(3, 3));
        outerShadow.setBrushColor(Color.LIGHT_GRAY);
        outerShadow.setEffectWidth(3);

        /*
         * Glossy Effect
         */
        final InnerGlowPathEffect titleGlow = new InnerGlowPathEffect();
        titleGlow.setBrushColor(Color.white);
        titleGlow.setBrushSteps(2);
        titleGlow.setOffset(new Point2D.Double(0, 1));
        titleGlow.setEffectWidth(3);

        interAndTitle.setAreaEffects(outerShadow, titleGlow);

        final CompoundPainter interAndTitleCompound = new CompoundPainter(interAndTitle, ip);

        /*
         * Center Rect
         */
        final int upperInset = panTitle.getBounds().height;
        final int lowerInset = panInter.getBounds().height;
        final RectanglePainter center = new RectanglePainter(new Color(226, 226, 226),
                new Color(226, 226, 226),
                1,
                Style.BOTH);
        center.setInsets(new Insets(upperInset, offset + 1, lowerInset, offset + 1));

        /*
         * lower and upper white line
         */

        final RectanglePainter upperWhiteLine = new RectanglePainter(
                new Color(220, 220, 220),
                new Color(46, 46, 46),
                1,
                Style.OUTLINE);
        upperWhiteLine.setInsets(new Insets(upperInset, offset, lowerInset + 1, offset));

        final GradientPaint centerGradientPaint = new GradientPaint(
                0,
                upperInset
                        + 2,
                new Color(150, 150, 150),
                0,
                getHeight()
                        - lowerInset,
                new Color(231, 231, 231));
        final RectanglePainter centerGradient = new RectanglePainter(centerGradientPaint, null, 0, Style.FILLED);
        centerGradient.setPaintStretched(true);
        centerGradient.setInsets(new Insets(upperInset + 2, offset + 1, lowerInset + 2, offset + 1));

        final CompoundPainter centerFill = new CompoundPainter(upperWhiteLine, centerGradient);

        final CompoundPainter centerCompoundPainter = new CompoundPainter(center, centerFill);
        final CompoundPainter compound = new CompoundPainter(interAndTitleCompound, centerCompoundPainter);

        this.setBackgroundPainter(compound);
    }
}
