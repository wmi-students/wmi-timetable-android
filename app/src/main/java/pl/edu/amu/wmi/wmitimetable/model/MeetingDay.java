package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;
import java.util.Date;
import lombok.Data;

@Data
public class MeetingDay {
    Date date;
    ArrayList<Schedule> schedules = new ArrayList<>();

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(ArrayList<Schedule> schedules) {
        this.schedules = schedules;
    }
}
