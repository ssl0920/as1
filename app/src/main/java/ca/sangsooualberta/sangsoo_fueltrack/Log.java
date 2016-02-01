package ca.sangsooualberta.sangsoo_fueltrack;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by sangsoo on 27/01/16.
 */
public class Log {
    private Date date;
    private String station;
    private String odometer;
    private String grade;
    private String amount;
    private String unit;
    private String cost;
    private String string;

    public Log(){
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;

    }

    public String getCost() {
        return cost;
    }

    // http://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
    // http://stackoverflow.com/questions/153724/how-to-round-a-number-to-n-decimal-places-in-java
    public void setCost() {
        BigDecimal amo = new BigDecimal(amount);
        BigDecimal uni = new BigDecimal(unit);
        this.cost = amo.multiply(uni.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_UP).toString();
    }

    public String getString() {
        return string;
    }

    // http://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html
    // http://stackoverflow.com/questions/4216745/java-string-to-date-conversion
    public void setString() {
        BigDecimal odo = new BigDecimal(odometer);
        String rounded_odometer = odo.setScale(1, RoundingMode.HALF_UP).toString();
        BigDecimal amo = new BigDecimal(amount);
        String rounded_amount = amo.setScale(3, RoundingMode.HALF_UP).toString();
        BigDecimal uni = new BigDecimal(unit);
        String rounded_unit = uni.setScale(1, RoundingMode.HALF_UP).toString();
        this.string = "Date: " + new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date) +
                "   Station: " + station + "   Odometer: " + rounded_odometer + " km   Grade: " + grade +
                "   Fuel Amount: " + rounded_amount + " L   Fuel Unit Cost: " + rounded_unit + " cents/L   Total Cost: $" + cost;
    }

}
