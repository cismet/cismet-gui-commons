/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui;

import lombok.Getter;
import lombok.Setter;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.Icon;
import javax.swing.JTextField;
import javax.swing.UIManager;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class JSearchTextField extends JTextField {

    //~ Static fields/initializers ---------------------------------------------

    private static final int MARGIN_INNER_LEFT = 2;
    private static final int MARGIN_INNER_RIGHT = 2;

    //~ Instance fields --------------------------------------------------------

    @Getter
    @Setter
    private String emptyText;
    @Getter
    @Setter
    private Icon searchIcon;
    @Getter
    @Setter
    private Icon abortIcon;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JSearchTextField object.
     */
    public JSearchTextField() {
        super();
        this.searchIcon = null;

        this.emptyText = "";

        this.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(final MouseEvent e) {
                    if (searchIcon != null) {
                        final int mouseX = e.getX();
                        if (mouseX < (getMargin().left)) {
                            fireActionPerformed();
                        }
                    }
                    if (abortIcon != null) {
                        final int mouseX = e.getX();
                        if (mouseX > (getWidth() - getMargin().right)) {
                            setText("");
                        }
                    }
                }
            });
        this.addMouseMotionListener(new MouseMotionAdapter() {

                @Override
                public void mouseMoved(final MouseEvent e) {
                    if (searchIcon != null) {
                        final int mouseX = e.getX();
                        if (mouseX < (getMargin().left)) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            return;
                        }
                    }
                    if ((abortIcon != null) && !getText().isEmpty()) {
                        final int mouseX = e.getX();
                        if (mouseX > (getWidth() - getMargin().right)) {
                            setCursor(new Cursor(Cursor.HAND_CURSOR));
                            return;
                        }
                    }
                    setCursor(new Cursor(Cursor.TEXT_CURSOR));
                }
            });
        this.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(final KeyEvent e) {
                    if (KeyEvent.VK_ESCAPE == e.getExtendedKeyCode()) {
                        setText("");
                    }
                }
            });
    }

    //~ Methods ----------------------------------------------------------------

    @Override
    protected void paintComponent(final Graphics g) {
        super.paintComponent(g);

        final Insets margin = new Insets(2, 2, 2, 2);

        if (this.searchIcon != null) {
            final int iconWidth = searchIcon.getIconWidth();
            final int iconHeight = searchIcon.getIconHeight();
            final int iconX = margin.left;
            final int iconY = (getHeight() - iconHeight) / 2;

            searchIcon.paintIcon(this, g, iconX, iconY);

            margin.left += iconWidth + MARGIN_INNER_LEFT;
        }

        if ((this.abortIcon != null) && !getText().isEmpty()) {
            final int iconWidth = abortIcon.getIconWidth();
            final int iconHeight = abortIcon.getIconHeight();
            final int iconX = getWidth() - iconWidth - margin.right - MARGIN_INNER_RIGHT;
            final int iconY = (getHeight() - iconHeight) / 2;

            abortIcon.paintIcon(this, g, iconX, iconY);

            margin.right += iconWidth + MARGIN_INNER_RIGHT;
        }

        setMargin(margin);

        if ((getText() == null) || getText().isEmpty()) {
            final Graphics2D g2d = (Graphics2D)g;

            final Font font = g2d.getFont();
            final Color color = g2d.getColor();

            final int textHeight = g2d.getFontMetrics().getHeight();
            final int textBottomX = ((getHeight() - textHeight) / 2) + textHeight - margin.bottom;

            g2d.setFont(font.deriveFont(Font.ITALIC));
            g2d.setColor(UIManager.getColor("textInactiveText"));
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g2d.drawString(emptyText, getInsets().left, textBottomX);
            g2d.setRenderingHints(g2d.getRenderingHints());
            g2d.setFont(font);
            g2d.setColor(color);
        }
    }
}
