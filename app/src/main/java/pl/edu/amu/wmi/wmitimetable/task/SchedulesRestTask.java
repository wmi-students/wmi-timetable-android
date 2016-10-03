package pl.edu.amu.wmi.wmitimetable.task;

import android.os.AsyncTask;
import com.squareup.okhttp.OkHttpClient;
import java.util.ArrayList;

import lombok.Getter;
import pl.edu.amu.wmi.wmitimetable.http.NullHostNameVerifier;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.rest.ScheduleRestService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

public class SchedulesRestTask extends AsyncTask<Void, Void, ArrayList<Schedule>> {

    private final String REST_URL = "http://wmitimetable.herokuapp.com";

    @Getter
    private Exception exception;

    @Override
    protected ArrayList<Schedule> doInBackground(Void... params) {
        try{
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(REST_URL)
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setClient(new OkClient(new OkHttpClient().setHostnameVerifier(new NullHostNameVerifier())))
                    .build();

            ScheduleRestService scheduleRestService = restAdapter.create(ScheduleRestService.class);
            ArrayList<Schedule> schedules = scheduleRestService.getAllSchedules();
            return schedules;
        }catch (Exception e){
            this.exception = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<Schedule> schedules) {
        super.onPostExecute(schedules);
    }
}
