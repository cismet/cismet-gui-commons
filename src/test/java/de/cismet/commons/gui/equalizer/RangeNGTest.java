package de.cismet.commons.gui.equalizer;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 *
 * @author mscholl
 */
public class RangeNGTest
{
    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testEnsureMinLessThanMax1()
    {
        final Range range = new Range(0,0);
        fail("no exception thrown");
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testEnsureMinLessThanMax2()
    {
        final Range range = new Range(1,0);
        fail("no exception thrown");
    }
}