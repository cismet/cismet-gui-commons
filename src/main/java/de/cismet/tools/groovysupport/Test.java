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

public class Test{

  public static void main(String[] args) {
    JFrame frame = new JFrame();
    JTextPane editor = new JTextPane();
    CodeDocument doc = new CodeDocument();
    Vector keywords = new Vector();
    keywords.addElement("abstract");
    keywords.addElement("boolean");
    keywords.addElement("break");
    keywords.addElement("byte");
    keywords.addElement("byvalue");
    keywords.addElement("case");
    keywords.addElement("cast");
    keywords.addElement("catch");
    keywords.addElement("char");
    keywords.addElement("class");
    keywords.addElement("const");
    keywords.addElement("continue");
    keywords.addElement("default");
    keywords.addElement("do");
    keywords.addElement("double");
    keywords.addElement("extends");
    keywords.addElement("else");
    keywords.addElement("false");
    keywords.addElement("final");
    keywords.addElement("finally");
    keywords.addElement("float");
    keywords.addElement("for");
    keywords.addElement("future");
    keywords.addElement("generic");
    keywords.addElement("if");
    keywords.addElement("implements");
    keywords.addElement("import");
    keywords.addElement("inner");
    keywords.addElement("instanceof");
    keywords.addElement("int");
    keywords.addElement("interface");
    keywords.addElement("long");
    keywords.addElement("native");
    keywords.addElement("new");
    keywords.addElement("null");
    keywords.addElement("operator");
    keywords.addElement("outer");
    keywords.addElement("package");
    keywords.addElement("private");
    keywords.addElement("protected");
    keywords.addElement("public");
    keywords.addElement("rest");
    keywords.addElement("return");
    keywords.addElement("short");
    keywords.addElement("static");
    keywords.addElement("super");
    keywords.addElement("switch");
    keywords.addElement("synchronized");
    keywords.addElement("this");
    keywords.addElement("throw");
    keywords.addElement("throws");
    keywords.addElement("transient");
    keywords.addElement("true");
    keywords.addElement("try");
    keywords.addElement("var");
    keywords.addElement("void");
    keywords.addElement("volatile");
    keywords.addElement("while");
    doc.setKeywords(keywords);
    editor.setDocument(doc);
    frame.setSize(400,400);
    frame.getContentPane().add(editor);
    frame.show();
  }
}
