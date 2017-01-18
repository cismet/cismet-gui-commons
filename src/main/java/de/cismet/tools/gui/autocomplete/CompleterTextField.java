/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.tools.gui.autocomplete;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class CompleterTextField extends JTextField {

    //~ Instance fields --------------------------------------------------------

    private CompleterFilter filter;

    //~ Constructors -----------------------------------------------------------

    /**
     * default constructor shows the completer window when offering matches.
     *
     * @param  completeMatches  DOCUMENT ME!
     */
    public CompleterTextField(final Object[] completeMatches) {
        super();
        initWindow(completeMatches);
    }

    /**
     * useWindow - true will popup the completer window to help with matches, false will just complete in the textfield
     * with no window.
     *
     * @param  completeMatches  DOCUMENT ME!
     * @param  useWindow        DOCUMENT ME!
     */
    public CompleterTextField(final Object[] completeMatches, final boolean useWindow) {
        super();
        if (useWindow) {
            initWindow(completeMatches);
        } else {
            initWindowless(completeMatches);
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  completeMatches  DOCUMENT ME!
     */
    private void initWindow(final Object[] completeMatches) {
        final PlainDocument pd = new PlainDocument();
        filter = new CompleterFilterWithWindow(completeMatches, this);
        pd.setDocumentFilter(filter);
        setDocument(pd);
    }

    /**
     * DOCUMENT ME!
     *
     * @param  completeMatches  DOCUMENT ME!
     */
    private void initWindowless(final Object[] completeMatches) {
        final PlainDocument pd = new PlainDocument();
        filter = new CompleterFilter(completeMatches, this);
        pd.setDocumentFilter(filter);
        setDocument(pd);
    }

    /**
     * Warning: Calling setDocument on a completerTextField will remove the completion mecanhism for this text field if
     * the document is not derived from AbstractDocument.
     *
     * <p>Only AbstractDocuments support the required DocumentFilter API for completion.</p>
     *
     * @param  doc  DOCUMENT ME!
     */
    @Override
    public void setDocument(final Document doc) {
        super.setDocument(doc);

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument)doc).setDocumentFilter(filter);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param  caseSensitive  DOCUMENT ME!
     */
    public void setCaseSensitive(final boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    /**
     * Will change the user entered part of the string to match the case of the matched item.
     *
     * <p>e.g. "europe/lONdon" would be corrected to "Europe/London"</p>
     *
     * <p>This option only makes sense if case sensitive is turned off</p>
     *
     * @param  correctCase  DOCUMENT ME!
     */
    public void setCorrectCase(final boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }

    /**
     * Set the list of objects to match against.
     *
     * @param  completeMatches  DOCUMENT ME!
     */
    public void setCompleterMatches(final Object[] completeMatches) {
        filter.setCompleterMatches(completeMatches);
    }
}
