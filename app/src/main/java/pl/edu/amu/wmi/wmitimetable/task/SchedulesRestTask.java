package pl.edu.amu.wmi.wmitimetable.task;

import android.os.AsyncTask;

import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;

import pl.edu.amu.wmi.wmitimetable.http.NullHostNameVerifier;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.rest.ScheduleRestService;
import pl.edu.amu.wmi.wmitimetable.service.ScheduleService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class SchedulesRestTask extends AsyncTask<Void, Void, ArrayList<Meeting>> {

    public static final String REST_URL = "http://wmitimetable.herokuapp.com";

    @Override
    protected ArrayList<Meeting> doInBackground(Void... params) {
        try{
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(REST_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(new OkHttpClient().setHostnameVerifier(new NullHostNameVerifier())))
                    .build();

            ScheduleRestService scheduleRestService = restAdapter.create(ScheduleRestService.class);
            return new ScheduleService().convertSchedulesToMeetings( scheduleRestService.getAllSchedules());
        }catch (Exception e){
            return  null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Meeting> meetings) {
        super.onPostExecute(meetings);
    }
}
