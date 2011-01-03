/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.javakit;
/*
 * @(#)JavaDocument.java        1.2 99/05/27
 *
 * Copyright (c) 1998 Sun Microsystems, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Sun
 * Microsystems, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Sun.
 *
 * SUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. SUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

import java.io.*;

import javax.swing.event.*;
import javax.swing.text.*;

/**
 * A document to represent text in the form of the java programming language. This is quite primitive in that it simply
 * provides support for lexically analyzing the text.
 *
 * @author   Timothy Prinzing
 * @version  1.2 05/27/99
 */
public class JavaDocument extends PlainDocument {

    //~ Static fields/initializers ---------------------------------------------

    /** Key to be used in AttributeSet's holding a value of Token. */
    static final Object CommentAttribute = new AttributeKey();

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JavaDocument object.
     */
    public JavaDocument() {
        super(new GapContent(1024));
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * Create a lexical analyzer for this document.
     *
     * @return  DOCUMENT ME!
     */
    public Scanner createScanner() {
        Scanner s;
        try {
            s = new Scanner();
        } catch (IOException e) {
            s = null;
        }
        return s;
    }

    /**
     * Fetch a reasonable location to start scanning given the desired start location. This allows for adjustments
     * needed to accomodate multiline comments.
     *
     * @param   p  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getScannerStart(final int p) {
        final Element elem = getDefaultRootElement();
        int lineNum = elem.getElementIndex(p);
        Element line = elem.getElement(lineNum);
        AttributeSet a = line.getAttributes();
        while (a.isDefined(CommentAttribute) && (lineNum > 0)) {
            lineNum -= 1;
            line = elem.getElement(lineNum);
            a = line.getAttributes();
        }
        return line.getStartOffset();
    }

    // --- AbstractDocument methods ----------------------------

    /**
     * Updates document structure as a result of text insertion. This will happen within a write lock. The superclass
     * behavior of updating the line map is executed followed by marking any comment areas that should backtracked
     * before scanning.
     *
     * @param  chng  the change event
     * @param  attr  the set of attributes
     */
    @Override
    protected void insertUpdate(final DefaultDocumentEvent chng, final AttributeSet attr) {
        super.insertUpdate(chng, attr);

        // update comment marks
        final Element root = getDefaultRootElement();
        final DocumentEvent.ElementChange ec = chng.getChange(root);
        if (ec != null) {
            final Element[] added = ec.getChildrenAdded();
            boolean inComment = false;
            for (int i = 0; i < added.length; i++) {
                final Element elem = added[i];
                final int p0 = elem.getStartOffset();
                final int p1 = elem.getEndOffset();
                String s;
                try {
                    s = getText(p0, p1 - p0);
                } catch (BadLocationException bl) {
                    s = null;
                }
                if (inComment) {
                    final MutableAttributeSet a = (MutableAttributeSet)elem.getAttributes();
                    a.addAttribute(CommentAttribute, CommentAttribute);
                    final int index = s.indexOf("*/"); // NOI18N
                    if (index >= 0) {
                        // found an end of comment, turn off marks
                        inComment = false;
                    }
                } else {
                    // scan for multiline comment
                    int index = s.indexOf("/*"); // NOI18N
                    if (index >= 0) {
                        // found a start of comment, see if it spans lines
                        index = s.indexOf("*/", index); // NOI18N
                        if (index < 0) {
                            // it spans lines
                            inComment = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates any document structure as a result of text removal. This will happen within a write lock. The superclass
     * behavior of updating the line map is executed followed by placing a lexical update command on the analyzer queue.
     *
     * @param  chng  the change event
     */
    @Override
    protected void removeUpdate(final DefaultDocumentEvent chng) {
        super.removeUpdate(chng);

        // update comment marks
    }

    // --- variables ------------------------------------------------

    //~ Inner Classes ----------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static class AttributeKey {

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new AttributeKey object.
         */
        private AttributeKey() {
        }

        //~ Methods ------------------------------------------------------------

        @Override
        public String toString() {
            return "comment"; // NOI18N
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    public class Scanner extends sun.tools.java.Scanner {

        //~ Instance fields ----------------------------------------------------

        int p0;

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new Scanner object.
         *
         * @throws  IOException  DOCUMENT ME!
         */
        Scanner() throws IOException {
            super(new LocalEnvironment(), new DocumentInputStream(0, getLength()));
            scanComments = true;
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Sets the range of the scanner. This should be called to reinitialize the scanner to the desired range of
         * coverage.
         *
         * @param   p0  DOCUMENT ME!
         * @param   p1  DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        public void setRange(final int p0, final int p1) throws IOException {
            useInputStream(new DocumentInputStream(p0, p1));
            this.p0 = p0;
        }

        /**
         * This fetches the starting location of the current token in the document.
         *
         * @return  DOCUMENT ME!
         */
        public final int getStartOffset() {
            final int begOffs = (int)(pos & MAXFILESIZE);
            return p0 + begOffs;
        }

        /**
         * This fetches the ending location of the current token in the document.
         *
         * @return  DOCUMENT ME!
         */
        public final int getEndOffset() {
            final int endOffs = (int)(getEndPos() & MAXFILESIZE);
            return p0 + endOffs;
        }
    }

    /**
     * Class to provide InputStream functionality from a portion of a Document. This really should be a Reader, but not
     * enough things use it yet.
     *
     * @version  $Revision$, $Date$
     */
    class DocumentInputStream extends InputStream {

        //~ Instance fields ----------------------------------------------------

        Segment segment;
        int p0;    // start position
        int p1;    // end position
        int pos;   // pos in document
        int index; // index into array of the segment

        //~ Constructors -------------------------------------------------------

        /**
         * Creates a new DocumentInputStream object.
         *
         * @param   p0  DOCUMENT ME!
         * @param   p1  DOCUMENT ME!
         *
         * @throws  Error  DOCUMENT ME!
         */
        public DocumentInputStream(final int p0, final int p1) {
            this.segment = new Segment();
            this.p0 = p0;
            this.p1 = Math.min(getLength(), p1);
            pos = p0;
            try {
                loadSegment();
            } catch (IOException ioe) {
                throw new Error("unexpected: " + ioe); // NOI18N
            }
        }

        //~ Methods ------------------------------------------------------------

        /**
         * Reads the next byte of data from this input stream. The value byte is returned as an <code>int</code> in the
         * range <code>0</code> to <code>255</code>. If no byte is available because the end of the stream has been
         * reached, the value <code>-1</code> is returned. This method blocks until input data is available, the end of
         * the stream is detected, or an exception is thrown.
         *
         * <p>A subclass must provide an implementation of this method.</p>
         *
         * @return     the next byte of data, or <code>-1</code> if the end of the stream is reached.
         *
         * @exception  IOException  if an I/O error occurs.
         *
         * @since      JDK1.0
         */
        @Override
        public int read() throws IOException {
            if (index >= (segment.offset + segment.count)) {
                if (pos >= p1) {
                    // no more data
                    return -1;
                }
                loadSegment();
            }
            return segment.array[index++];
        }

        /**
         * DOCUMENT ME!
         *
         * @throws  IOException  DOCUMENT ME!
         */
        void loadSegment() throws IOException {
            try {
                final int n = Math.min(1024, p1 - pos);
                getText(pos, n, segment);
                pos += n;
                index = segment.offset;
            } catch (BadLocationException e) {
                throw new IOException("Bad location"); // NOI18N
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    static class LocalEnvironment extends sun.tools.java.Environment {

        //~ Methods ------------------------------------------------------------

        /**
         * DOCUMENT ME!
         *
         * @param  source  DOCUMENT ME!
         * @param  where   DOCUMENT ME!
         * @param  err     DOCUMENT ME!
         * @param  arg1    DOCUMENT ME!
         * @param  arg2    DOCUMENT ME!
         * @param  arg3    DOCUMENT ME!
         */
        public void error(final Object source,
                final int where,
                final String err,
                final Object arg1,
                final Object arg2,
                final Object arg3) {
            // should do something useful...
            System.err.println(err);
            System.err.println("location: " + where); // NOI18N
        }
    }
}
