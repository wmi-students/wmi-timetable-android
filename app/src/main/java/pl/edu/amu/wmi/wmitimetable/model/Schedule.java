package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;

import lombok.Data;
import pl.edu.amu.wmi.wmitimetable.service.ScheduleService;

@Data
public class Schedule implements Serializable, Comparable<Schedule> {
    private int id;
    private String when;
    private String group;
    private String subject;
    private String year;
    private String study;
    private String leader;
    private String room1;
    private String room2;

    private transient boolean special;

    @Override
    public int compareTo(Schedule schedule) {
        ScheduleService scheduleService = new ScheduleService();
        return scheduleService.getDateFromSchedule(this).compareTo(scheduleService.getDateFromSchedule(schedule));
    }
}
