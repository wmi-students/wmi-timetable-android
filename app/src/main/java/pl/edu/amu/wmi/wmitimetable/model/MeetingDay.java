package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class MeetingDay implements Serializable {
    private Date date;
    private ArrayList<Schedule> schedules = new ArrayList<>();
}
