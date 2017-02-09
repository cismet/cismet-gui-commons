package de.cismet.commons.gui.protocol;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.cismet.commons.gui.protocol.impl.CommentProtocolStep;
import de.cismet.commons.gui.protocol.impl.CommentProtocolStepImpl;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pd
 */
public class ProtocolTest {

    public ProtocolTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void test010SingleProtocolStep() throws JsonProcessingException, IOException {
        final CommentProtocolStep step = new CommentProtocolStepImpl("dies ist ein Test-Kommentar");

        final String jsonString = step.toJsonString();

        final CommentProtocolStep newStep = (CommentProtocolStep) AbstractProtocolStep.fromJsonString(
                jsonString,
                CommentProtocolStepImpl.class);

        assertEquals(step.getDate().getTime(), newStep.getDate().getTime());
        assertEquals(step.getMessage(), newStep.getMessage());
    }

    @Test
    public void test020MultipleProtocolSteps() throws JsonProcessingException, IOException, ClassNotFoundException {
        
        ProtocolHandler.getInstance().setRecordEnabled(true);
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("Protollierungs-Test nummer eins..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...noch ein Test..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...es wird wie wild getestet..."));
        ProtocolHandler.getInstance().recordStep(new CommentProtocolStepImpl("...irgendwann reicht es aber auch !"));
                
        assertEquals(4, ProtocolHandler.getInstance().getAllSteps().size());

        final String allProtosJson = ProtocolHandler.getInstance().toJsonString();
        ProtocolHandler.getInstance().fromJsonString(allProtosJson);
        
        final List<ProtocolStep> list = ProtocolHandler.getInstance().getAllSteps();
        final List<ProtocolStep> newList = ProtocolHandler.getInstance().getAllSteps();
        
        assertEquals(list.size(), newList.size());
        
        final Iterator<ProtocolStep> stepIterator = list.iterator();
        final Iterator<ProtocolStep> newStepIterator = newList.iterator();
        
        while(stepIterator.hasNext() && newStepIterator.hasNext()) {
            final CommentProtocolStep step = (CommentProtocolStep)stepIterator.next();
            final CommentProtocolStep newStep = (CommentProtocolStep)newStepIterator.next();
            
            assertEquals(step.getDate().getTime(), newStep.getDate().getTime());
            assertEquals(step.getMessage(), newStep.getMessage());
        }
    }
}
