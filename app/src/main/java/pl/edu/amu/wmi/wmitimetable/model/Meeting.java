package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class Meeting {
    Date date;
    ArrayList<MeetingDay> meetingDays = new ArrayList<>();
}
