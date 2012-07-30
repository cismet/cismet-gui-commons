/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.security.exceptions;

/**
 * DOCUMENT ME!
 *
 * @author   Sebastian
 * @version  $Revision$, $Date$
 */
public class BadHttpStatusCodeException extends Exception {

    //~ Instance fields --------------------------------------------------------

    private final String requestedURI;
    private final int statuscode;
    private final String response;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new instance of BadHttpStatusCodeException.
     */
    public BadHttpStatusCodeException() {
        super();

        requestedURI = "";
        statuscode = Integer.MIN_VALUE;
        response = "";
    }

    /**
     * Creates a new BadHttpStatusCodeException object.
     *
     * @param  requestedURI  DOCUMENT ME!
     * @param  statuscode    DOCUMENT ME!
     * @param  message       DOCUMENT ME!
     * @param  response      DOCUMENT ME!
     */
    public BadHttpStatusCodeException(final String requestedURI,
            final int statuscode,
            final String message,
            final String response) {
        super(message + "(" + statuscode + ")");

        this.requestedURI = requestedURI;
        this.statuscode = statuscode;
        this.response = response;
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getRequestedURI() {
        return requestedURI;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public int getStatuscode() {
        return statuscode;
    }

    /**
     * DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    public String getResponse() {
        return response;
    }
}
