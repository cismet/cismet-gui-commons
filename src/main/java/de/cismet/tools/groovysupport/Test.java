/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * Test.java
 *
 * Created on 11. November 2006, 10:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package de.cismet.tools.groovysupport;

import java.util.*;

import javax.swing.JFrame;
import javax.swing.JTextPane;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class Test {

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        final JFrame frame = new JFrame();
        final JTextPane editor = new JTextPane();
        final CodeDocument doc = new CodeDocument();
        final Vector keywords = new Vector();
        keywords.addElement("abstract");     // NOI18N
        keywords.addElement("boolean");      // NOI18N
        keywords.addElement("break");        // NOI18N
        keywords.addElement("byte");         // NOI18N
        keywords.addElement("byvalue");      // NOI18N
        keywords.addElement("case");         // NOI18N
        keywords.addElement("cast");         // NOI18N
        keywords.addElement("catch");        // NOI18N
        keywords.addElement("char");         // NOI18N
        keywords.addElement("class");        // NOI18N
        keywords.addElement("const");        // NOI18N
        keywords.addElement("continue");     // NOI18N
        keywords.addElement("default");      // NOI18N
        keywords.addElement("do");           // NOI18N
        keywords.addElement("double");       // NOI18N
        keywords.addElement("extends");      // NOI18N
        keywords.addElement("else");         // NOI18N
        keywords.addElement("false");        // NOI18N
        keywords.addElement("final");        // NOI18N
        keywords.addElement("finally");      // NOI18N
        keywords.addElement("float");        // NOI18N
        keywords.addElement("for");          // NOI18N
        keywords.addElement("future");       // NOI18N
        keywords.addElement("generic");      // NOI18N
        keywords.addElement("if");           // NOI18N
        keywords.addElement("implements");   // NOI18N
        keywords.addElement("import");       // NOI18N
        keywords.addElement("inner");        // NOI18N
        keywords.addElement("instanceof");   // NOI18N
        keywords.addElement("int");          // NOI18N
        keywords.addElement("interface");    // NOI18N
        keywords.addElement("long");         // NOI18N
        keywords.addElement("native");       // NOI18N
        keywords.addElement("new");          // NOI18N
        keywords.addElement("null");         // NOI18N
        keywords.addElement("operator");     // NOI18N
        keywords.addElement("outer");        // NOI18N
        keywords.addElement("package");      // NOI18N
        keywords.addElement("private");      // NOI18N
        keywords.addElement("protected");    // NOI18N
        keywords.addElement("public");       // NOI18N
        keywords.addElement("rest");         // NOI18N
        keywords.addElement("return");       // NOI18N
        keywords.addElement("short");        // NOI18N
        keywords.addElement("static");       // NOI18N
        keywords.addElement("super");        // NOI18N
        keywords.addElement("switch");       // NOI18N
        keywords.addElement("synchronized"); // NOI18N
        keywords.addElement("this");         // NOI18N
        keywords.addElement("throw");        // NOI18N
        keywords.addElement("throws");       // NOI18N
        keywords.addElement("transient");    // NOI18N
        keywords.addElement("true");         // NOI18N
        keywords.addElement("try");          // NOI18N
        keywords.addElement("var");          // NOI18N
        keywords.addElement("void");         // NOI18N
        keywords.addElement("volatile");     // NOI18N
        keywords.addElement("while");        // NOI18N
        doc.setKeywords(keywords);
        editor.setDocument(doc);
        frame.setSize(400, 400);
        frame.getContentPane().add(editor);
        frame.show();
    }
}
