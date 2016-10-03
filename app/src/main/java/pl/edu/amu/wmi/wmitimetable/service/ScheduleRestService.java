package pl.edu.amu.wmi.wmitimetable.service;

import java.util.ArrayList;

import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import retrofit.http.GET;

public interface ScheduleRestService {
    @GET("/schedules.json")
    ArrayList<Schedule> getAllSchedules();
}
