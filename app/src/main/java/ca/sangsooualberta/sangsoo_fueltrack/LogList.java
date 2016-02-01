package ca.sangsooualberta.sangsoo_fueltrack;

import android.app.Activity;
import java.util.ArrayList;


/**
 * Created by sangsoo on 27/01/16.
 */
public class LogList extends Activity {

    private ArrayList<Log> loglist = new ArrayList<Log>();

    private static final String FILENAME = "file.sav";

    public LogList() {
    }

    public ArrayList<Log> getLoglist() {
        return loglist;
    }

    public void setLoglist(ArrayList<Log> loglist) {
        this.loglist = loglist;
    }

    public static String getFILENAME() {
        return FILENAME;
    }

    public boolean hasLog(Log log) {
        return loglist.contains(log);
    }

    public void addLog(Log log)
            throws IllegalArgumentException {
        if (hasLog(log)) {
            throw new IllegalArgumentException();
        }
        loglist.add(log);
    }

    public void clearLog() {loglist.clear();}

    public void removeLog(Log log){
        loglist.remove(log);
    }

    public Log getLog(int index){
        return loglist.get(index);
    }

    public int getCount(){
        return loglist.size();
    }

    public void setLog(int index, Log log){
        loglist.set(index, log);
    }


}
