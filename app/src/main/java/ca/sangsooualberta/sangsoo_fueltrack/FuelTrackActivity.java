package ca.sangsooualberta.sangsoo_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by sangsoo on 27/01/16.
 *
 * This class is the view for the Fuel Entry with 2 buttons.
 * Save Entry button to save the Log Entry (either add new or change exisiting) in LogList.
 * View Log button goes to Fuel Log view (FuelLogActivity).
 *
 */

public class FuelTrackActivity extends Activity {

    private LogList loglist = new LogList();

    private EditText date;
    private EditText station;
    private EditText odometer;
    private EditText grade;
    private EditText amount;
    private EditText unit;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_track);

        date = (EditText) findViewById(R.id.edit_date);
        station = (EditText) findViewById(R.id.edit_station);
        odometer = (EditText) findViewById(R.id.edit_odometer);
        grade = (EditText) findViewById(R.id.edit_grade);
        amount = (EditText) findViewById(R.id.edit_amount);
        unit = (EditText) findViewById(R.id.edit_unit);

        loadFromFile();

        // index = -1 : New Log Entry
        // index >= 0 : Existing Entry
        // learned how to use getStringExtra from the below address.
        // http://alvinalexander.com/java/edu/qanda/pjqa00010.shtml
        try {
            index = Integer.parseInt(getIntent().getStringExtra("index"));
        } catch (NumberFormatException e){
         index = -1; }

        // if index exists (clicked on the List View Log Entries), fill in the text spaces with
        // stored values for the fields in the Log obj of LogList.
        if (index >= 0) {
            Log log = loglist.getLog(index);
            date.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(log.getDate()));
            station.setText(log.getStation());
            odometer.setText(log.getOdometer());
            grade.setText(log.getGrade());
            amount.setText(log.getAmount());
            unit.setText(log.getUnit());
        }


        // Save Log button either saves new entry (index = -1) or edits existing entry (index >= 0)
        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);

                // check for empty text fields; if any one of the text field is empty, do not allow save.
                if (!date.getText().toString().equals("") && !station.getText().toString().equals("")
                        && !odometer.getText().toString().equals("") && !grade.getText().toString().equals("")
                        && !amount.getText().toString().equals("") && !unit.getText().toString().equals("")) {

                    Log log = new Log();
                    Date temp_date = new Date();

                    try {
                        temp_date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(date.getText().toString());
                        log.setDate(temp_date);
                        log.setStation(station.getText().toString());
                        log.setOdometer(odometer.getText().toString());
                        log.setGrade(grade.getText().toString());
                        log.setAmount(amount.getText().toString());
                        log.setUnit(unit.getText().toString());
                        log.setCost();
                        log.setString();

                        // modify existing entry in LogList Model.
                        if (index >=0 ){
                            loglist.setLog(index, log);
                        }
                        // save new entry to LogList Model.
                        else {
                            loglist.addLog(log);
                        }

                        //Save in gson and display success message.
                        saveInFile();
                        Snackbar.make(v, "Successfully Saved.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    // catches invalid Date field.
                    catch (ParseException e) {
                        Snackbar.make(v, "Please enter a valid date.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    // catches invalid (non-numeric) Odometer, Fuel Amount, and Fuel Unit Cost fields.
                    catch (NumberFormatException e) {
                        if (isNumeric(odometer.getText().toString()) == false) {
                            Snackbar.make(v, "Please enter only numeric values for Odometer.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        else if (isNumeric(amount.getText().toString()) == false) {
                            Snackbar.make(v, "Please enter only numeric values for Fuel Amount.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        else if (isNumeric(unit.getText().toString()) == false) {
                            Snackbar.make(v, "Please enter only numeric values for Fuel Unit Cost.", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    }
                }
                else {
                    Snackbar.make(v, "Please do not leave any field empty.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        // View Log button that switches view to Log List view (FuelLogActivity).
        Button logButton = (Button) findViewById(R.id.log);
        logButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(FuelTrackActivity.this, FuelLogActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //load the data from Gson.
    public void loadFromFile() {
        try {
            FileInputStream fis = openFileInput(loglist.getFILENAME());
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            // Took from https://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/Gson.html Jan-21-2016
            Type listType = new TypeToken<ArrayList<Log>>() {
            }.getType();
            ArrayList<Log> temp_loglist = loglist.getLoglist();
            temp_loglist = gson.fromJson(in, listType);
            loglist.setLoglist(temp_loglist);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            ArrayList<Log> loglist = new ArrayList<Log>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // Save the data to Gson and update the LogList Model.
    public void saveInFile() {
        try {
            FileOutputStream fos = openFileOutput(loglist.getFILENAME(),
                    0);

            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            ArrayList<Log> temp_loglist = loglist.getLoglist();
            gson.toJson(temp_loglist, out);
            loglist.setLoglist(temp_loglist);
            out.flush();

            fos.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            throw new RuntimeException();
        }
    }

    // from http://stackoverflow.com/questions/1102891/how-to-check-if-a-string-is-numeric-in-java
    protected static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

}
