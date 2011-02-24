/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2011 dmeiers
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
package de.cismet.tools.gui.slideabletree;

import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.plaf.basic.BasicTaskPaneUI;

import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;

import javax.swing.border.Border;

/**
 * DOCUMENT ME!
 *
 * @author   dmeiers
 * @version  $Revision$, $Date$
 */
public class MyTaskPaneUI extends BasicTaskPaneUI {

    //~ Methods ----------------------------------------------------------------

    @Override
    protected Border createPaneBorder() {
        return new MyPaneBorder();
    }

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    class MyPaneBorder extends PaneBorder {

        //~ Methods ------------------------------------------------------------

// @Override
// protected void paintTitleBackground(final JXTaskPane group, final Graphics g) {
// super.paintTitleBackground(group, g);
// final Color backgroundGradientStart = UIManager.getColor("TaskPane.titleBackgroundGradientStart");
// final Color backgroundGradientEnd = UIManager.getColor("TaskPane.titleBackgroundGradientEnd");
// final Graphics2D g2 = (Graphics2D)g;
////            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
////                RenderingHints.VALUE_ANTIALIAS_ON);
//            final GradientPaint gradient = new GradientPaint(
//                    0,
//                    0,
//                    backgroundGradientStart,
//                    0,
//                    getTitleHeight(group)
//                            - getRoundHeight(),
//                    backgroundGradientEnd);
//            g2.setPaint(gradient);
//
//            g2.fillRoundRect(
//                0,
//                0,
//                group.getWidth(),
//                getRoundHeight()
//                        * 2,
//                getRoundHeight(),
//                getRoundHeight());
//
//            g2.fillRect(
//                0,
//                getRoundHeight(),
//                group.getWidth(),
//                getTitleHeight(group)
//                        - getRoundHeight());
//        }
        @Override
        protected void paintTitleBackground(final JXTaskPane group, final Graphics g) {
            if (group.isSpecial()) {
                g.setColor(specialTitleBackground);

                g.fillRoundRect(
                    0,
                    0,
                    group.getWidth(),
                    getRoundHeight()
                            * 2,
                    getRoundHeight(),
                    getRoundHeight());

                g.fillRect(
                    0,
                    getRoundHeight(),
                    group.getWidth(),
                    getTitleHeight(group)
                            - getRoundHeight());
            } else {
                final Paint oldPaint = ((Graphics2D)g).getPaint();

                final GradientPaint gradientRect = new GradientPaint((group.getWidth() / 4),
                        0f,
                        titleBackgroundGradientStart,
                        (group.getWidth() / 4)
                                + 1,
                        getTitleHeight(group)
                                - (getTitleHeight(group) / 4),
                        titleBackgroundGradientEnd);

                ((Graphics2D)g).setRenderingHint(
                    RenderingHints.KEY_COLOR_RENDERING,
                    RenderingHints.VALUE_COLOR_RENDER_QUALITY);

                ((Graphics2D)g).setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                ((Graphics2D)g).setRenderingHint(
                    RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
                ((Graphics2D)g).setPaint(gradientRect);

                g.fillRoundRect(
                    0,
                    0,
                    group.getWidth(),
                    getRoundHeight()
                            * 2,
                    getRoundHeight(),
                    getRoundHeight());

                g.fillRect(
                    0,
                    getRoundHeight(),
                    group.getWidth(),
                    getTitleHeight(group)
                            - getRoundHeight());
                ((Graphics2D)g).setPaint(oldPaint);
            }
        }

        @Override
        protected void paintExpandedControls(final JXTaskPane group,
                final Graphics g,
                final int x,
                final int y,
                final int width,
                final int height) {
            ((Graphics2D)g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

            paintOvalAroundControls(group, g, x, y, width, height);

            g.setColor(getPaintColor(group));

            paintChevronControls(group, g, x, y, width, height);

            ((Graphics2D)g).setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        }

        @Override
        protected boolean isMouseOverBorder() {
            return true;
        }
    }
}
