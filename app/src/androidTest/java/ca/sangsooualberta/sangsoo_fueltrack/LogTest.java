package ca.sangsooualberta.sangsoo_fueltrack;

import android.support.v4.util.LogWriter;
import android.test.ActivityInstrumentationTestCase2;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sangsoo on 30/01/16.
 */
public class LogTest extends ActivityInstrumentationTestCase2{

    public LogTest(){
        super(Log.class);
    }

    public void testodometer(){
        Log log = new Log();
        log.setOdometer("12.121212");
        BigDecimal value = new BigDecimal(12.11).setScale(3, RoundingMode.HALF_UP);
        assertEquals(value, log.getOdometer());
    }

    public void testcost(){
        Log log = new Log();
        log.setAmount("30.0");
        log.setUnit("100");
        log.setCost();
        //assertEquals(new BigDecimal(30.000), log.getAmount());
        //assertEquals(100.0, log.getUnit());
        assertEquals(30.001, log.getCost());

    }

    public void teststring() throws ParseException {
        Log log = new Log();
        //log.setDate("2016-13-13");
        log.setStation("Costco");
        log.setOdometer("142342414.242");
        log.setGrade("Premium");
        log.setAmount("43.1");
        log.setUnit("64.1");
        log.setCost();
        log.setString();
        assertEquals("expected", log.getString());
    }

}
