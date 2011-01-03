/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * RichJLabel.java
 *
 * Created on 7. August 2007, 09:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.tools.gui;
import java.awt.*;
import java.awt.font.*;

import javax.swing.*;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class RichJLabel extends JLabel {

    //~ Instance fields --------------------------------------------------------

    private int tracking;

    private int left_x;
    private int left_y;
    private int right_x;
    private int right_y;
    private Color left_color;
    private Color right_color;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new RichJLabel object.
     *
     * @param  text      DOCUMENT ME!
     * @param  tracking  DOCUMENT ME!
     */
    public RichJLabel(final String text, final int tracking) {
        super(text);
        this.tracking = tracking;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  x      DOCUMENT ME!
     * @param  y      DOCUMENT ME!
     * @param  color  DOCUMENT ME!
     */
    public void setLeftShadow(final int x, final int y, final Color color) {
        left_x = x;
        left_y = y;
        left_color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  x      DOCUMENT ME!
     * @param  y      DOCUMENT ME!
     * @param  color  DOCUMENT ME!
     */
    public void setRightShadow(final int x, final int y, final Color color) {
        right_x = x;
        right_y = y;
        right_color = color;
    }

    @Override
    public Dimension getPreferredSize() {
        final String text = getText();
        final FontMetrics fm = this.getFontMetrics(getFont());

        int w = fm.stringWidth(text);
        w += (text.length() - 1) * tracking;
        w += left_x + right_x;

        int h = fm.getHeight();
        h += left_y + right_y;

        return new Dimension(w, h);
    }

    @Override
    public void paintComponent(final Graphics g) {
        ((Graphics2D)g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        final char[] chars = getText().toCharArray();

        final FontMetrics fm = this.getFontMetrics(getFont());
        final int h = fm.getAscent();
        final LineMetrics lm = fm.getLineMetrics(getText(), g);
        g.setFont(getFont());

        int x = 0;

        for (int i = 0; i < chars.length; i++) {
            final char ch = chars[i];
            final int w = fm.charWidth(ch) + tracking;

            g.setColor(left_color);
            g.drawString("" + chars[i], x - left_x, h - left_y); // NOI18N

            g.setColor(right_color);
            g.drawString("" + chars[i], x + right_x, h + right_y); // NOI18N

            g.setColor(getForeground());
            g.drawString("" + chars[i], x, h); // NOI18N

            x += w;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final RichJLabel label = new RichJLabel("76", 0); // NOI18N

        /*
         * // drop shadow w/ highlight label.setLeftShadow(1,1,Color.white); label.setRightShadow(2,3,Color.black);
         * label.setForeground(Color.gray); label.setFont(label.getFont().deriveFont(140f));
         */

        // subtle outline
        label.setLeftShadow(1, 1, Color.white);
        label.setRightShadow(1, 1, Color.white);
        label.setForeground(Color.BLACK);
        label.setFont(label.getFont().deriveFont(26f));

        /*
         * // 3d letters label.setLeftShadow(5,5,Color.white); label.setRightShadow(-3,-3, new Color(0xccccff));
         * label.setForeground(new Color(0x8888ff)); label.setFont(label.getFont().deriveFont(140f));
         */

        final JFrame frame = new JFrame("RichJLabel hack"); // NOI18N
        frame.getContentPane().add(label);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     */
    public static void p(final String str) {
        System.out.println(str);
    }
}
