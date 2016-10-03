package pl.edu.amu.wmi.wmitimetable.rest;

import java.util.ArrayList;

import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import retrofit.http.GET;

public interface ScheduleRestService {
    @GET("/schedules.json")
    ArrayList<Schedule> getAllSchedules();
}
