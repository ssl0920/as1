package ca.sangsooualberta.sangsoo_fueltrack;

import android.test.ActivityInstrumentationTestCase2;

/**
 * Created by sangsoo on 01/02/16.
 */
public class LogListTest extends ActivityInstrumentationTestCase2 {
    public LogListTest(){
        super(LogList.class);
    }

    public void testHasAndAddLog(){
        LogList loglist = new LogList();
        Log log = new Log();
        loglist.addLog(log);
        assertTrue(loglist.hasLog(log));
    }

    public void testRemoveAndClearLog(){
        LogList loglist = new LogList();
        Log log = new Log();
        Log log2 = new Log();
        loglist.addLog(log);
        loglist.addLog(log2);
        loglist.removeLog(log);
        assertFalse(loglist.hasLog(log));
        loglist.addLog(log);
        loglist.clearLog();
        assertFalse(loglist.hasLog(log2));
        assertFalse(loglist.hasLog(log));
    }

    public void testGetCount(){
        LogList loglist = new LogList();
        Log log = new Log();
        Log log2 = new Log();
        loglist.addLog(log);
        loglist.addLog(log2);
        assertEquals(2, loglist.getCount());
    }

    public void testGetAndSetLog(){
        LogList loglist = new LogList();
        Log log = new Log();
        Log log2 = new Log();
        loglist.addLog(log);
        loglist.setLog(0,log2);
        assertEquals(log2, loglist.getLog(0));
    }

}

