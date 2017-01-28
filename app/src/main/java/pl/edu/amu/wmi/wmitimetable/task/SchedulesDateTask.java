package pl.edu.amu.wmi.wmitimetable.task;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class SchedulesDateTask extends AsyncTask<Void, Void, DateTime> {
    private static final String SCHEDULES_URL = "http://retardo.pl/wmi-timetable-date/schedules.date";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    protected DateTime doInBackground(Void... params) {
        DateTimeFormatter format = DateTimeFormat.forPattern(DATE_TIME_FORMAT);
        String date = getContentFromUrl(SCHEDULES_URL);
        if(date != null) {
            return format.parseDateTime(date);
        }
        return null;
    }

    private String getContentFromUrl(String url) {
        try {
            return new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        } catch (IOException e) {
            Log.e("DateService", e.getMessage());
        }
        return null;
    }
}
