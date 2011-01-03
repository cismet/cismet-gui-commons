/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * CodeDocument.java
 *
 * Created on 11. November 2006, 10:57
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.tools.groovysupport;

import java.awt.Color;

import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CodeDocument extends DefaultStyledDocument {

    //~ Static fields/initializers ---------------------------------------------

    public static int STRING_MODE = 10;
    public static int TEXT_MODE = 11;
    public static int NUMBER_MODE = 12;
    public static int COMMENT_MODE = 13;

    //~ Instance fields --------------------------------------------------------

    private String word = ""; // NOI18N
    private SimpleAttributeSet bold = new SimpleAttributeSet();
    private SimpleAttributeSet string = new SimpleAttributeSet();
    private SimpleAttributeSet normal = new SimpleAttributeSet();
    private SimpleAttributeSet number = new SimpleAttributeSet();
    private SimpleAttributeSet comments = new SimpleAttributeSet();
    private int currentPos = 0;
    private Vector keywords = new Vector();
    private int mode = TEXT_MODE;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new CodeDocument object.
     */
    public CodeDocument() {
        // set the bold attribute
        StyleConstants.setBold(bold, true);
        StyleConstants.setForeground(string, Color.red);
        StyleConstants.setForeground(number, Color.magenta);
        StyleConstants.setForeground(comments, Color.blue);
        StyleConstants.setItalic(comments, true);
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     */
    private void insertKeyword(final String str, final int pos) {
        try {
            // remove the old word and formatting
            this.remove(pos - str.length(), str.length());
            /*replace it with the same word, but new formatting
             *we MUST call the super class insertString method here, otherwise we
             *would end up in an infinite loop !!!!!*/
            super.insertString(pos - str.length(), str, bold);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     */
    private void insertTextString(final String str, final int pos) {
        try {
            // remove the old word and formatting
            this.remove(pos, str.length());
            super.insertString(pos, str, string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     */
    private void insertNumberString(final String str, final int pos) {
        try {
            // remove the old word and formatting
            this.remove(pos, str.length());
            super.insertString(pos, str, number);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     * @param  pos  DOCUMENT ME!
     */
    private void insertCommentString(final String str, final int pos) {
        try {
            // remove the old word and formatting
            this.remove(pos, str.length());
            super.insertString(pos, str, comments);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void checkForString() {
        int offs = this.currentPos;
        final Element element = this.getParagraphElement(offs);
        String elementText = ""; // NOI18N
        try {
            // this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(),
                    element.getEndOffset()
                            - element.getStartOffset());
        } catch (Exception ex) {
            // whoops!
            System.out.println("no text"); // NOI18N
        }
        final int strLen = elementText.length();
        if (strLen == 0) {
            return;
        }
        int i = 0;

        if (element.getStartOffset() > 0) {
            // translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        int quoteCount = 0;
        if ((offs >= 0) && (offs <= (strLen - 1))) {
            i = offs;
            while (i > 0) {
                // the while loop walks back until we hit a delimiter

                final char charAt = elementText.charAt(i);
                if ((charAt == '"')) {
                    quoteCount++;
                }
                i--;
            }
            final int rem = quoteCount % 2;
            // System.out.println(rem);
            mode = (rem == 0) ? TEXT_MODE : STRING_MODE;
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void checkForKeyword() {
        if (mode != TEXT_MODE) {
            return;
        }
        int offs = this.currentPos;
        final Element element = this.getParagraphElement(offs);
        String elementText = ""; // NOI18N
        try {
            // this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(),
                    element.getEndOffset()
                            - element.getStartOffset());
        } catch (Exception ex) {
            // whoops!
            System.out.println("no text"); // NOI18N
        }
        final int strLen = elementText.length();
        if (strLen == 0) {
            return;
        }
        int i = 0;

        if (element.getStartOffset() > 0) {
            // translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        if ((offs >= 0) && (offs <= (strLen - 1))) {
            i = offs;
            while (i > 0) {
                // the while loop walks back until we hit a delimiter
                i--;
                final char charAt = elementText.charAt(i);
                if ((charAt == ' ') | (i == 0) | (charAt == '(') | (charAt == ')')
                            | (charAt == '{')
                            | (charAt == '}')) {           // if i == 0 then we're at the begininng
                    if (i != 0) {
                        i++;
                    }
                    word = elementText.substring(i, offs); // skip the period

                    final String s = word.trim().toLowerCase();
                    // this is what actually checks for a matching keyword
                    if (keywords.contains(s)) {
                        insertKeyword(word, currentPos);
                    }
                    break;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void checkForNumber() {
        int offs = this.currentPos;
        final Element element = this.getParagraphElement(offs);
        String elementText = ""; // NOI18N
        try {
            // this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(),
                    element.getEndOffset()
                            - element.getStartOffset());
        } catch (Exception ex) {
            // whoops!
            System.out.println("no text"); // NOI18N
        }
        final int strLen = elementText.length();
        if (strLen == 0) {
            return;
        }
        int i = 0;

        if (element.getStartOffset() > 0) {
            // translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        mode = TEXT_MODE;
        if ((offs >= 0) && (offs <= (strLen - 1))) {
            i = offs;
            while (i > 0) {
                // the while loop walks back until we hit a delimiter
                final char charAt = elementText.charAt(i);
                if ((charAt == ' ') | (i == 0) | (charAt == '(') | (charAt == ')')
                            | (charAt == '{')
                            | (charAt == '}')          /*|*/) { // if i == 0 then we're at the begininng
                    if (i != 0) {
                        i++;
                    }
                    mode = NUMBER_MODE;
                    break;
                } else if (!(((charAt >= '0') & (charAt <= '9')) | (charAt == '.')
                                | (charAt == '+')
                                | (charAt == '-')
                                | (charAt == '/')
                                | (charAt == '*')
                                | (charAt == '%')
                                | (charAt == '='))) {
                    mode = TEXT_MODE;
                    break;
                }
                i--;
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    private void checkForComment() {
        int offs = this.currentPos;
        final Element element = this.getParagraphElement(offs);
        String elementText = ""; // NOI18N
        try {
            // this gets our chuck of current text for the element we're on
            elementText = this.getText(element.getStartOffset(),
                    element.getEndOffset()
                            - element.getStartOffset());
        } catch (Exception ex) {
            // whoops!
            System.out.println("no text"); // NOI18N
        }
        final int strLen = elementText.length();
        if (strLen == 0) {
            return;
        }
        int i = 0;

        if (element.getStartOffset() > 0) {
            // translates backward if neccessary
            offs = offs - element.getStartOffset();
        }
        if ((offs >= 1) && (offs <= (strLen - 1))) {
            i = offs;
            final char commentStartChar1 = elementText.charAt(i - 1);
            final char commentStartChar2 = elementText.charAt(i);
            if ((commentStartChar1 == '/') && (commentStartChar2 == '*')) {
                mode = COMMENT_MODE;
                this.insertCommentString("/*", currentPos - 1); // NOI18N
            } else if ((commentStartChar1 == '*') && (commentStartChar2 == '/')) {
                mode = TEXT_MODE;
                this.insertCommentString("*/", currentPos - 1); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  str  DOCUMENT ME!
     */
    private void processChar(final String str) {
        final char strChar = str.charAt(0);
        if (mode != this.COMMENT_MODE) {
            mode = TEXT_MODE;
        }
        switch (strChar) {
            case ('{'):
            case ('}'):
            case (' '):
            case ('\n'):
            case ('('):
            case (')'):
            case (';'):
            case ('.'): {
                checkForKeyword();
                if ((mode == STRING_MODE) && (strChar == '\n')) {
                    mode = TEXT_MODE;
                }
            }
            break;
            case ('"'): {
                insertTextString(str, currentPos);
                this.checkForString();
            }
            break;
            case ('0'):
            case ('1'):
            case ('2'):
            case ('3'):
            case ('4'):
            case ('5'):
            case ('6'):
            case ('7'):
            case ('8'):
            case ('9'): {
                checkForNumber();
            }
            break;
            case ('*'):
            case ('/'): {
                checkForComment();
            }
            break;
        }
        if (mode == this.TEXT_MODE) {
            this.checkForString();
        }
        if (mode == this.STRING_MODE) {
            insertTextString(str, this.currentPos);
        } else if (mode == this.NUMBER_MODE) {
            insertNumberString(str, this.currentPos);
        } else if (mode == this.COMMENT_MODE) {
            insertCommentString(str, this.currentPos);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  strChar  DOCUMENT ME!
     */
    private void processChar(final char strChar) {
        final char[] chrstr = new char[1];
        chrstr[0] = strChar;
        final String str = new String(chrstr);
        processChar(str);
    }

    @Override
    public void insertString(final int offs, final String str, final AttributeSet a) throws BadLocationException {
        super.insertString(offs, str, normal);

        final int strLen = str.length();
        final int endpos = offs + strLen;
        int strpos;
        for (int i = offs; i < endpos; i++) {
            currentPos = i;
            strpos = i - offs;
            processChar(str.charAt(strpos));
        }
        currentPos = offs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public Vector getKeywords() {
        return this.keywords;
    }

    /**
     * DOCUMENT ME!
     *
     * @param  aKeywordList  DOCUMENT ME!
     */
    public void setKeywords(final Vector aKeywordList) {
        if (aKeywordList != null) {
            this.keywords = aKeywordList;
        }
    }
}
