/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 thorsten
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
package de.cismet.tools.gui;

import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;

/**
 * DOCUMENT ME!
 *
 * @author   thorsten
 * @version  $Revision$, $Date$
 */
public class TextAreaAppender extends WriterAppender {

    //~ Static fields/initializers ---------------------------------------------

    private static JTextArea jTextArea = null;
    private static JTextComponent regs = null;

    //~ Instance fields --------------------------------------------------------

    String prefix =
        "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">"
                + "<html><head><title>Log4J Log Messages</title><style type=\"text/css\">"
                + "<!--body, table {font-family: arial,sans-serif; font-size: x-small;}th {background: #336699; color: #FFFFFF; text-align: left;}-->"
                + "</style></head><body bgcolor=\"#FFFFFF\" topmargin=\"6\" leftmargin=\"6\"><hr size=\"1\" noshade>"
                + "Log session <br><br><table cellspacing=\"0\" cellpadding=\"4\" border=\"0\" bordercolor=\"#224466\" width=\"100%\">"
                + "<tr><th>Time</th><th>Thread</th><th>Level</th><th>Category</th><th>Message</th></tr>";

    //~ Methods ----------------------------------------------------------------

    /**
     * Set the target JTextArea for the logging information to appear.
     *
     * @param  jTextArea  DOCUMENT ME!
     * @param  regs       DOCUMENT ME!
     */
    public static void setTextArea(final JTextArea jTextArea, final JTextComponent regs) {
        TextAreaAppender.jTextArea = jTextArea;
        TextAreaAppender.regs = regs;
    }

    /**
     * Format and then append the loggingEvent to the stored JTextArea.
     *
     * @param  loggingEvent  DOCUMENT ME!
     */
    @Override
    public void append(final LoggingEvent loggingEvent) {
        final String message = this.layout.format(loggingEvent);

        // Append formatted message to textarea using the Swing Thread.
        SwingUtilities.invokeLater(new Runnable() {

                @Override
                public void run() {
                    if (message.contains(regs.getText())) {
                        jTextArea.append(message);
                    }
                }
            });
    }
}
