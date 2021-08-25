/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.cismet.security.handler;
import jcifs.ntlmssp.NtlmMessage;
import jcifs.ntlmssp.Type1Message;
import jcifs.ntlmssp.Type2Message;
import jcifs.ntlmssp.Type3Message;

import jcifs.util.Base64;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NTCredentials;
import org.apache.commons.httpclient.auth.AuthChallengeParser;
import org.apache.commons.httpclient.auth.AuthPolicy;
import org.apache.commons.httpclient.auth.AuthScheme;
import org.apache.commons.httpclient.auth.AuthenticationException;
import org.apache.commons.httpclient.auth.InvalidCredentialsException;
import org.apache.commons.httpclient.auth.MalformedChallengeException;

import java.io.IOException;

/**
 * DOCUMENT ME!
 *
 * @version  $Revision$, $Date$
 */
public class JcifsNtlmScheme implements AuthScheme {

    //~ Enums ------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @version  $Revision$, $Date$
     */
    private enum State {

        //~ Enum constants -----------------------------------------------------

        NOT_STARTED, STARTED, CHALLENGE_REQUESTED, CHALLENGE_RECEIVED, FINISHED, FAILED
    }

    //~ Instance fields --------------------------------------------------------

    private Type2Message ntlmChallenge = null;
    private State state;

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new JcifsNtlmScheme object.
     *
     * @throws  AuthenticationException  DOCUMENT ME!
     */
    public JcifsNtlmScheme() throws AuthenticationException {
        try {
            Class.forName("jcifs.ntlmssp.NtlmMessage", false, this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new AuthenticationException("JCIFS lib not found");
        }
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param   ntlmMessage  DOCUMENT ME!
     *
     * @return  DOCUMENT ME!
     */
    private static String toBase64String(final NtlmMessage ntlmMessage) {
        return Base64.encode(ntlmMessage.toByteArray());
    }

    @Override
    public String authenticate(final Credentials credentials, final HttpMethod method) throws AuthenticationException {
        if (this.state == State.NOT_STARTED) {
            throw new IllegalStateException("NTLM authentication not started");
        }

        final NTCredentials ntcredentials;
        try {
            ntcredentials = (NTCredentials)credentials;
        } catch (final ClassCastException ex) {
            throw new InvalidCredentialsException(String.format(
                    "Credentials cannot be used for NTLM authentication: %s",
                    credentials.getClass().getName()),
                ex);
        }

        final NtlmMessage response;
        if ((this.state == State.STARTED) || (this.state == State.FAILED)) {
            response = new Type1Message(Type1Message.getDefaultFlags(),
                    ntcredentials.getDomain(),
                    ntcredentials.getHost());
            this.state = State.CHALLENGE_REQUESTED;
        } else {
            response = new Type3Message(
                    this.ntlmChallenge,
                    ntcredentials.getPassword(),
                    ntcredentials.getDomain(),
                    ntcredentials.getUserName(),
                    ntcredentials.getHost(),
                    0);
            this.state = State.FINISHED;
        }
        return String.format("NTLM %s", toBase64String(response));
    }

    @Override
    public void processChallenge(final String challenge) throws MalformedChallengeException {
        final String scheme = AuthChallengeParser.extractScheme(challenge);
        if (!scheme.equalsIgnoreCase(getSchemeName())) {
            throw new MalformedChallengeException(String.format("Invalid NTLM challenge: %s", challenge));
        }
        final int i = challenge.indexOf(' ');
        if (i != -1) {
            try {
                this.ntlmChallenge = new Type2Message(Base64.decode(challenge.substring(i, challenge.length()).trim()));
            } catch (final IOException ex) {
                throw new MalformedChallengeException(String.format("Invalid NTLM challenge: %s", challenge), ex);
            }
            this.state = State.CHALLENGE_RECEIVED;
        } else {
            this.ntlmChallenge = null;
            if (this.state == State.NOT_STARTED) {
                this.state = State.STARTED;
            } else {
                this.state = State.FAILED;
            }
        }
    }

    @Override
    public String authenticate(final Credentials credentials, final String method, final String uri)
            throws AuthenticationException {
        throw new RuntimeException(
            "Not implemented as it is deprecated anyway in Httpclient 3.x");
    }

    @Override
    public String getID() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getParameter(final String name) {
        if (name == null) {
            throw new IllegalArgumentException("Parameter name may not be null");
        }
        return null;
    }

    @Override
    public String getRealm() {
        return null;
    }

    @Override
    public String getSchemeName() {
        return AuthPolicy.NTLM;
    }

    @Override
    public boolean isComplete() {
        return (this.state == State.FINISHED) || (this.state == State.FAILED);
    }

    @Override
    public boolean isConnectionBased() {
        return true;
    }
}
