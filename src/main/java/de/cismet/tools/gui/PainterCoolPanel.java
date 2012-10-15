/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.AbstractAreaPainter.Style;
import org.jdesktop.swingx.painter.CompoundPainter;
import org.jdesktop.swingx.painter.MattePainter;
import org.jdesktop.swingx.painter.RectanglePainter;
import org.jdesktop.swingx.painter.effects.InnerGlowPathEffect;
import org.jdesktop.swingx.painter.effects.ShadowPathEffect;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.geom.Point2D;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class PainterCoolPanel extends JXPanel {

    //~ Static fields/initializers ---------------------------------------------

    private static final Color colorDarkLine = Color.black;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new PainterCoolPanel object.
     */
    public PainterCoolPanel() {
        final float[] dist = { 0.0f, 0.7f, 0.85f };
        final Color[] colors = { new Color(66, 66, 66), new Color(102, 102, 102), new Color(66, 66, 66) };
        final LinearGradientPaint titleAndInterGradient = new LinearGradientPaint(new Point2D.Double(400, 0),
                new Point2D.Double(400, 600),
                dist,
                colors,
                CycleMethod.NO_CYCLE);
//        final GradientPaint titleAndInterGradient = new GradientPaint(
//                400,
//                100,
//                new Color(66, 66, 66),
//                400,
//                300,
//                new Color(102, 102, 102));

        /*
         * Outer round Rect
         */
        final RectanglePainter interAndTitle = new RectanglePainter(
                titleAndInterGradient,
                colorDarkLine,
                1,
                null);
        interAndTitle.setRoundHeight(30);
        interAndTitle.setRoundWidth(30);
        interAndTitle.setRounded(true);
        interAndTitle.setInsets(new Insets(15, 15, 15, 15));

        /*
         *  Shadow- Effect
         */

        final ShadowPathEffect outerShadow = new ShadowPathEffect();
        outerShadow.setOffset(new Point2D.Float(5, 5));
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

        /*
         * Center Rect
         */

        final RectanglePainter center = new RectanglePainter(new Color(226, 226, 226),
                new Color(226, 226, 226),
                1,
                Style.BOTH);
        center.setInsets(new Insets(75, 16, 63, 16));

        /*
         * lower and upper white line
         */

        final RectanglePainter upperWhiteLine = new RectanglePainter(
                new Color(220, 220, 220),
                new Color(46, 46, 46),
                1,
                Style.OUTLINE);
        upperWhiteLine.setInsets(new Insets(75, 15, 64, 15));

        final GradientPaint centerGradientPaint = new GradientPaint(
                0,
                0,
                new Color(150, 150, 150),
                0,
                100,
                new Color(231, 231, 231));
        final RectanglePainter centerGradient = new RectanglePainter(centerGradientPaint, null, 0, Style.FILLED);
        centerGradient.setPaintStretched(true);
        centerGradient.setInsets(new Insets(77, 16, 65, 16));

        final CompoundPainter centerFill = new CompoundPainter(upperWhiteLine, centerGradient);

        final CompoundPainter centerCompoundPainter = new CompoundPainter(center, centerFill);
        final CompoundPainter compound = new CompoundPainter(interAndTitle, centerCompoundPainter);

        this.setBackgroundPainter(compound);
    }
}
