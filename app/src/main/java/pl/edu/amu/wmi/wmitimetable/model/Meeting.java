package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class Meeting implements Serializable {
    private Date date;
    private ArrayList<MeetingDay> meetingDays = new ArrayList<>();
}
