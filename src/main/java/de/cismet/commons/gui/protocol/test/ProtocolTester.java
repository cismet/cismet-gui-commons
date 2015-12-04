/***************************************************
*
* cismet GmbH, Saarbruecken, Germany
*
*              ... and it just works.
*
****************************************************/
package de.cismet.commons.gui.protocol.test;

import org.apache.log4j.Logger;

import java.util.List;

import de.cismet.commons.gui.protocol.AbstractProtocolStep;
import de.cismet.commons.gui.protocol.ProtocolHandler;
import de.cismet.commons.gui.protocol.ProtocolStep;
import de.cismet.commons.gui.protocol.impl.CommentProtocolStep;
import de.cismet.commons.gui.protocol.impl.CommentProtocolStepImpl;

import de.cismet.tools.gui.log4jquickconfig.Log4JQuickConfig;

/**
 * DOCUMENT ME!
 *
 * @author   jruiz
 * @version  $Revision$, $Date$
 */
public class ProtocolTester {

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getLogger(ProtocolTester.class);

    //~ Constructors -----------------------------------------------------------

    /**
     * Creates a new ProtocolTester object.
     */
    private ProtocolTester() {
    }

    //~ Methods ----------------------------------------------------------------

    /**
     * DOCUMENT ME!
     *
     * @param  args  DOCUMENT ME!
     */
    public static void main(final String[] args) {
        Log4JQuickConfig.configure4LumbermillOnLocalhost();
        final ProtocolTester tester = new ProtocolTester();
        try {
            tester.test();
        } catch (final Exception ex) {
            LOG.fatal(ex, ex);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws  Exception  DOCUMENT ME!
     */
    private void test() throws Exception {
        log("========================");
        log("= single protocol test = ");
        log("========================");

        log("creating protocol...");
        final CommentProtocolStep proto = new CommentProtocolStepImpl("dies ist ein Test-Kommentar");
        log("protocol: " + proto);
        log("* date: " + proto.getDate());
        log("* message: " + proto.getMessage());

        log("storing protocol to json...");
        final String jsonString = proto.toJsonString();
        log("json:" + jsonString);

        log("restoring protocol from json...");
        final CommentProtocolStep newProto = (CommentProtocolStep)AbstractProtocolStep.fromJsonString(
                jsonString,
                CommentProtocolStepImpl.class);
        log("protocol: " + newProto);
        log("* date: " + newProto.getDate());
        log("* message: " + newProto.getMessage());

        log("=========================");
        log("= protocol handler test =");
        log("=========================");

        log(new CommentProtocolStepImpl("Protollierungs-Test nummer eins...").toJsonString());

        log("recording some protocols...");
        ProtocolHandler.getInstance().setRecordEnabled(true);
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("Protollierungs-Test nummer eins..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...noch ein Test..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...es wird wie wild getestet..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...irgendwann reicht es aber auch !"));
        log("number of recorded protocols: " + ProtocolHandler.getInstance().getAllSteps().size());

        log("storing all protocols to json...");
        final String allProtosJson = ProtocolHandler.getInstance().toJsonString();
        log("json: " + allProtosJson);

        log("restoring all protocols from json...");
        ProtocolHandler.getInstance().fromJsonString(allProtosJson);
        final List<ProtocolStep> newList = ProtocolHandler.getInstance().getAllSteps();
        log("size after jsonify: " + newList.size());
        log("protocolls after jsonify: ");
        for (final ProtocolStep newProtoFromList : newList) {
            log("protocol: " + newProtoFromList.getClass().getCanonicalName());

            final CommentProtocolStep newProtoFromListCasted = (CommentProtocolStep)newProtoFromList;
            log("* date: " + newProtoFromListCasted.getDate());
            log("* message: " + newProtoFromListCasted.getMessage());
            log(" ----- ");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param  message  DOCUMENT ME!
     */
    private static void log(final String message) {
        if (LOG.isDebugEnabled()) {
            LOG.debug(message);
        }
        System.out.println(message);
    }
}
