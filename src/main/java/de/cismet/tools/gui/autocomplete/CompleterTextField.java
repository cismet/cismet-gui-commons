package de.cismet.tools.gui.autocomplete;

import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class CompleterTextField extends JTextField {

    /**
     * default constructor shows the completer window when offering matches.
     * @param completeMatches
     */
    public CompleterTextField(Object[] completeMatches) {
        super();
        initWindow(completeMatches);
    }

    /**
     * useWindow - true will popup the completer window to help with matches,
     * false will just complete in the textfield with no window.
     */
    public CompleterTextField(Object[] completeMatches, boolean useWindow) {
        super();
        if (useWindow) {
            initWindow(completeMatches);
        } else {
            initWindowless(completeMatches);
        }
    }

    private void initWindow(Object[] completeMatches) {
        PlainDocument pd = new PlainDocument();
        filter = new CompleterFilterWithWindow(completeMatches, this);
        pd.setDocumentFilter(filter);
        setDocument(pd);
    }

    private void initWindowless(Object[] completeMatches) {
        PlainDocument pd = new PlainDocument();
        filter = new CompleterFilter(completeMatches, this);
        pd.setDocumentFilter(filter);
        setDocument(pd);
    }

    @Override
    /**
     * Warning: Calling setDocument on a completerTextField will remove the completion
     * mecanhism for this text field if the document is not derived from AbstractDocument.
     *
     *  Only AbstractDocuments support the required DocumentFilter API for completion.
     */
    public void setDocument(Document doc) {
        super.setDocument(doc);

        if (doc instanceof AbstractDocument) {
            ((AbstractDocument) doc).setDocumentFilter(filter);
        }
    }

    public boolean isCaseSensitive() {
        return filter.isCaseSensitive();
    }

    public boolean isCorrectingCase() {
        return filter.isCorrectingCase();
    }

    public void setCaseSensitive(boolean caseSensitive) {
        filter.setCaseSensitive(caseSensitive);
    }

    /**
     * Will change the user entered part of the string to match the case of the matched item.
     *
     * e.g.
     * "europe/lONdon" would be corrected to "Europe/London"
     *
     * This option only makes sense if case sensitive is turned off
     */
    public void setCorrectCase(boolean correctCase) {
        filter.setCorrectCase(correctCase);
    }

    /**
     * Set the list of objects to match against.
     * @param completeMatches
     */
    public void setCompleterMatches(Object[] completeMatches) {
        filter.setCompleterMatches(completeMatches);
    }
    private CompleterFilter filter;
}
