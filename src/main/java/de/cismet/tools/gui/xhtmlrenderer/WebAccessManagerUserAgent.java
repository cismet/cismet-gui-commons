/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
/*
 *  Copyright (C) 2010 jweintraut
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
package de.cismet.tools.gui.xhtmlrenderer;

import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.swing.NaiveUserAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.cismet.security.WebAccessManager;

import de.cismet.security.exceptions.AccessMethodIsNotSupportedException;
import de.cismet.security.exceptions.MissingArgumentException;
import de.cismet.security.exceptions.NoHandlerForURLException;
import de.cismet.security.exceptions.RequestFailedException;

/**
 * A subclass of Flying Saucer's NaiveUserAgent. It's intended to fetch all requested resources via WebAccessManager.
 *
 * @version  $Revision$, $Date$
 */
public class WebAccessManagerUserAgent extends NaiveUserAgent {

    //~ Static fields/initializers ---------------------------------------------

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(
            WebAccessManagerUserAgent.class);

    //~ Instance fields --------------------------------------------------------

    private Pattern encodingPattern = Pattern.compile("(encoding|charset)=\"?(.*?)[;\" ]");
    private Pattern windowsEncodingPattern = Pattern.compile("windows-(\\d{4})");

    //~ Methods ----------------------------------------------------------------

    @Override
    protected InputStream resolveAndOpenStream(final String uri) {
        InputStream result = null;

        if ((uri != null) && (uri.trim().length() > 0)) {
            if (uri.startsWith("jar") || uri.startsWith("file")) {
                try {
                    result = new URL(uri).openStream();
                } catch (MalformedURLException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since the resulting URL is malformed.", ex); // NOI18N
                } catch (IOException ex) {
                    LOG.error("Can't load from URI '" + uri + "'.", ex);                                      // NOI18N
                }
            } else {
                try {
                    result = WebAccessManager.getInstance().doRequest(new URL(uri));
                } catch (URISyntaxException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since its syntax is broken.", ex);           // NOI18N
                } catch (MissingArgumentException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since it couldn't be converted to a URL.", ex); // NOI18N
                } catch (AccessMethodIsNotSupportedException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since the access method isn't supported.", ex); // NOI18N
                } catch (RequestFailedException ex) {
                    LOG.error("The request to load URI '" + uri + "' failed.", ex);                           // NOI18N
                } catch (NoHandlerForURLException ex) {
                    LOG.error("Can't load from URI '" + uri + "' since there is no matching handler.", ex);   // NOI18N
                } catch (Exception ex) {
                    LOG.error("Can't load from URI '" + uri + "' since an unexcpected exception occurred.", ex); // NOI18N
                }
            }
        }

        return result;
    }

    @Override
    public XMLResource getXMLResource(final String uri) {
        final Reader reader = resolveAndOpenEncodedStream(uri);
        final XMLResource xmlResource = XMLResource.load(reader);

        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOG.warn("Couldn't close reader.", e);
            }
        }

        return xmlResource;
    }

    /**
     * This method is intended to avoid broken umlauts when reading from text files. To do this it returns an
     * InputStream for the given URI which uses the correct encoding. Since this method should be used to open streams
     * on HTML/XHTML documents the correct encoding is determined by parsing the file referenced by the given URI. So
     * one should be sure to call this method on text files.
     *
     * @param   uri  The URI referencing the resource which is to be opened
     *
     * @return  A reader which allows reading from the resource using the correct encoding.
     */
    private Reader resolveAndOpenEncodedStream(final String uri) {
        Reader result = null;
        String encoding = null;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(resolveAndOpenStream(uri)));
        Matcher matcher = null;
        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                matcher = encodingPattern.matcher(line);
                if (matcher.find()) {
                    encoding = matcher.group(2);
                    break;
                }
            }
        } catch (IOException ex) {
            LOG.warn("Couldn't determine encoding of resource: '" + uri + "'.", ex); // NOI18N
        }

        matcher = windowsEncodingPattern.matcher(encoding);
        if (matcher.find()) {
            encoding = "Cp" + matcher.group(1); // NOI18N
        }

        if (LOG.isDebugEnabled()) {
            LOG.debug("Encoding resource '" + uri + "' in '" + encoding + "'."); // NOI18N
        }

        try {
            result = new InputStreamReader(resolveAndOpenStream(uri), encoding);
        } catch (UnsupportedEncodingException ex) {
            LOG.error("Error opening a reader on URI '" + uri + "' with unsupported encoding '" + encoding + "'.", ex);
        }

        return result;
    }
}
