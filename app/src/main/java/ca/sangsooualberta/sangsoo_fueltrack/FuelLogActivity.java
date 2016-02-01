package ca.sangsooualberta.sangsoo_fueltrack;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
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
import java.util.ArrayList;

/**
 * Created by sangsoo on 27/01/16.
 *
 * This class is the view for the Fuel Log in a list view with 2 buttons.
 * Add Entry button to go to add Clear Log Entry (FuelTrackActivity) and Clear button to delete all log entries.
 * Clicking the log entries goes to add Log Entry view (FuelTrackActivity) with the data preloaded.
 *
 */

//used to resolve error mesages with "R.": http://stackoverflow.com/questions/17421104/android-studio-marks-r-in-red-with-error-message-cannot-resolve-symbol-r-but
public class FuelLogActivity extends Activity {

    private LogList loglist = new LogList();

    private ListView oldLogList;
    private ArrayAdapter<String> adapter;
    // logdatalist stores the String data of Log object in order to display on List View.
    private ArrayList<String> logdatalist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_log);

        // Sets up for List View.
        oldLogList = (ListView) findViewById(R.id.oldLogList);

        loadFromFile();
        adapter = new ArrayAdapter<String>(this,
                R.layout.list_item, logdatalist);
        oldLogList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // The Add Button impelmentation:
        Button addButton = (Button) findViewById(R.id.add);
        addButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                Intent intent = new Intent(FuelLogActivity.this, FuelTrackActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // The Clear Button implementation:
        Button clearButton = (Button) findViewById(R.id.clear);
        clearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                loglist.clearLog();
                saveInFile();
                adapter.notifyDataSetChanged();
                finish();
                Intent intent = new Intent(FuelLogActivity.this, FuelLogActivity.class);
                startActivity(intent);
            }
        });


        // Click on log entries function implementation; read the 2 links below:
        // http://androidexample.com/Create_A_Simple_Listview_-_Android_Example/index.php?view=article_discription&aid=65&aaid=90
        // http://stackoverflow.com/questions/5265913/how-to-use-putextra-and-getextra-for-string-data
        oldLogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int index = position;
                Log log = loglist.getLog(index);
                setResult(RESULT_OK);
                Intent intent = new Intent(FuelLogActivity.this, FuelTrackActivity.class);
                intent.putExtra("index", String.valueOf(index));
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // after loading file into loglist, parse it to get data for logdatalist.
        for (int i = 0; i < loglist.getCount(); i++){
            Log log = loglist.getLog(i);
            String data = log.getString();
            logdatalist.add(data);
        }

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

}
