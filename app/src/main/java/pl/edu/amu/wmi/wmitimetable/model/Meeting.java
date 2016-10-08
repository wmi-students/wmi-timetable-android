package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class Meeting {
    private Date date;
    private ArrayList<MeetingDay> meetingDays = new ArrayList<>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<MeetingDay> getMeetingDays() {
        return meetingDays;
    }

    public void setMeetingDays(ArrayList<MeetingDay> meetingDays) {
        this.meetingDays = meetingDays;
    }
}
