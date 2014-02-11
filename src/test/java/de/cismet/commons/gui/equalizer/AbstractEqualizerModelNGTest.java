package de.cismet.commons.gui.equalizer;

import java.lang.reflect.Field;
import java.util.Set;
import static org.testng.Assert.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author mscholl
 */
public class AbstractEqualizerModelNGTest
{
    private EqualizerModelListenerImpl eqL1;
    private EqualizerModelListenerImpl eqL2;
    private EqualizerModelListenerImpl eqL3;
    
    @BeforeMethod
    public void setUpMethod() throws Exception
    {
        eqL1 = new EqualizerModelListenerImpl();
        eqL2 = new EqualizerModelListenerImpl();
        eqL3 = new EqualizerModelListenerImpl();
    }

    @AfterMethod
    public void tearDonwMethod() throws Exception
    {
        eqL1 = null;
        eqL2 = null;
        eqL3 = null;
    }

    /**
     * Test of fireEqualizerModelEvent method, of class AbstractEqualizerModel.
     */
    @Test
    public void testFireEqualizerModelEvent()
    {
        final AbstractEqualizerModelImpl model = new AbstractEqualizerModelImpl();
        final EqualizerModelEvent event = new EqualizerModelEvent(model);
        
        model.addEqualizerModelListener(eqL1);
        model.fireEqualizerModelEvent(event);
        assertEquals(eqL1.event, event);
        eqL1.event = null;
        model.addEqualizerModelListener(eqL2);
        model.addEqualizerModelListener(eqL3);
        model.fireEqualizerModelEvent(event);
        assertEquals(eqL1.event, event);
        assertEquals(eqL2.event, event);
        assertEquals(eqL3.event, event);
    }
    
    @Test(expectedExceptions = {IllegalArgumentException.class})
    public void testFireEqualizerModelEvent_nullEvent()
    {
        final AbstractEqualizerModelImpl model = new AbstractEqualizerModelImpl();
        model.fireEqualizerModelEvent(null);
    }

    /**
     * Test of addEqualizerModelListener method, of class AbstractEqualizerModel.
     */
    @Test
    public void testAddEqualizerModelListener() throws Exception
    {
        final AbstractEqualizerModelImpl model = new AbstractEqualizerModelImpl();
        assertEquals(model.getListenerCount(), 0);
        
        model.addEqualizerModelListener(eqL1);
        assertEquals(model.getListenerCount(), 1);
        model.addEqualizerModelListener(eqL2);
        model.addEqualizerModelListener(eqL3);
        assertEquals(model.getListenerCount(), 3);
        model.addEqualizerModelListener(eqL1);
        assertEquals(model.getListenerCount(), 3);
        model.addEqualizerModelListener(eqL2);
        model.addEqualizerModelListener(eqL3);
        assertEquals(model.getListenerCount(), 3);
        model.addEqualizerModelListener(eqL1);
        assertEquals(model.getListenerCount(), 3);
        model.addEqualizerModelListener(eqL2);
        model.addEqualizerModelListener(eqL3);
        assertEquals(model.getListenerCount(), 3);
        model.addEqualizerModelListener(null);
        assertEquals(model.getListenerCount(), 3);
    }

    /**
     * Test of removeEqualizerModelListener method, of class AbstractEqualizerModel.
     */
    @Test(dependsOnMethods = {"testAddEqualizerModelListener"})
    public void testRemoveEqualizerModelListener() throws Exception
    {
        final AbstractEqualizerModelImpl model = new AbstractEqualizerModelImpl();
        
        model.addEqualizerModelListener(eqL1);
        model.addEqualizerModelListener(eqL2);
        model.addEqualizerModelListener(eqL3);
        
        model.removeEqualizerModelListener(null);
        assertEquals(model.getListenerCount(), 3);
        model.removeEqualizerModelListener(eqL1);
        assertEquals(model.getListenerCount(), 2);
        model.removeEqualizerModelListener(eqL1);
        assertEquals(model.getListenerCount(), 2);
        model.removeEqualizerModelListener(eqL2);
        model.removeEqualizerModelListener(eqL3);
        assertEquals(model.getListenerCount(), 0);
        model.removeEqualizerModelListener(eqL1);
        model.removeEqualizerModelListener(eqL2);
        model.removeEqualizerModelListener(eqL3);
        assertEquals(model.getListenerCount(), 0);
    }

    private static final class EqualizerModelListenerImpl implements EqualizerModelListener {
        
        EqualizerModelEvent event = null;
        
            @Override
            public void equalizerChanged(EqualizerModelEvent event)
            {
                this.event = event;
            }
    }
    
    private static final class AbstractEqualizerModelImpl extends AbstractEqualizerModel {
        
        int getListenerCount() throws Exception {
            final Field field = AbstractEqualizerModel.class.getDeclaredField("listeners");
            field.setAccessible(true);
            
            return ((Set)field.get(this)).size();
        }
        
        @Override
        public Range getRange()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public String getEqualizerCategory(int index)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getEqualizerCategoryCount()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public int getValueAt(int index)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void setValueAt(int index, int value)
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
    }
}