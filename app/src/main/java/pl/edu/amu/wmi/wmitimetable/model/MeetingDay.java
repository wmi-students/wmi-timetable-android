package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class MeetingDay {
    Date date;
    ArrayList<Schedule> schedules = new ArrayList<>();
}
