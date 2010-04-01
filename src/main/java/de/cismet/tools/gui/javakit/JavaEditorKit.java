package de.cismet.tools.gui.javakit;
/*
 * @(#)JavaEditorKit.java	1.2 99/05/27
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

import javax.swing.text.*;

/**
 * This kit supports a fairly minimal handling of
 * editing java text content.  It supports syntax 
 * highlighting and produces the lexical structure
 * of the document as best it can.
 *
 * @author  Timothy Prinzing
 * @version 1.2 05/27/99
 */
public class JavaEditorKit extends DefaultEditorKit {

    public JavaEditorKit() {
	super();
    }

    public JavaContext getStylePreferences() {
	if (preferences == null) {
	    preferences = new JavaContext();
	}
	return preferences;
    }

    public void setStylePreferences(JavaContext prefs) {
	preferences = prefs;
    }

    // --- EditorKit methods -------------------------

    /**
     * Get the MIME type of the data that this
     * kit represents support for.  This kit supports
     * the type <code>text/java</code>.
     */
    public String getContentType() {
	return "text/java";  //NOI18N
    }

    /**
     * Create a copy of the editor kit.  This
     * allows an implementation to serve as a prototype
     * for others, so that they can be quickly created.
     */
    public Object clone() {
	JavaEditorKit kit = new JavaEditorKit();
	kit.preferences = preferences;
	return kit;
    }

    /**
     * Creates an uninitialized text storage model
     * that is appropriate for this type of editor.
     *
     * @return the model
     */
    public Document createDefaultDocument() {
	return new JavaDocument();
    }

    /**
     * Fetches a factory that is suitable for producing 
     * views of any models that are produced by this
     * kit.  The default is to have the UI produce the
     * factory, so this method has no implementation.
     *
     * @return the view factory
     */
    public final ViewFactory getViewFactory() {
	return getStylePreferences();
    }

    JavaContext preferences;
}







