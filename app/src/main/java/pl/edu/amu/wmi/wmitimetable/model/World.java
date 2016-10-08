package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;
import java.util.Collections;

import lombok.Getter;
import lombok.Setter;

// Singleton to store and access local data
public class World {
    private static World mInstance = null;

    //@Getter @Setter
    //private ArrayList<Schedule> schedules = new ArrayList<>();

    Boolean loaded = false;
    Filter filter = new Filter();
    private ArrayList<Meeting> meetings = new ArrayList<>();

    private World(){

    }

    public static World getInstance(){
        if(mInstance == null)
        {
            mInstance = new World();
        }
        return mInstance;
    }

    private void createFiltersFromMeetings() {
        filter.clear();
        for (Meeting meeting : this.meetings) {
            for (MeetingDay meetingDay : meeting.getMeetingDays()) {
                for (Schedule schedule : meetingDay.getSchedules()) {
                    if(!filter.studyExists(schedule.getStudy())){
                        filter.getStudies().add(schedule.getStudy());
                    }
                    if(!filter.yearExists(schedule.getYear())){
                        filter.getYears().add(schedule.getYear());
                    }
                    if(!filter.groupExists(schedule.getGroup())){
                        if(!schedule.getGroup().contains("WA")) {
                            filter.getGroups().add(schedule.getGroup());
                        }
                    }
                }
            }
        }

        Collections.sort(filter.getStudies(), String.CASE_INSENSITIVE_ORDER);
        Collections.sort(filter.getYears(), String.CASE_INSENSITIVE_ORDER);
        Collections.sort(filter.getGroups(), String.CASE_INSENSITIVE_ORDER);
    }

    public void clear(){
        this.meetings = new ArrayList<>();
        this.loaded = false;
    }

    public ArrayList<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(ArrayList<Meeting> meetings) {
        this.meetings = meetings;
        createFiltersFromMeetings();
        loaded = true;
    }

    public Filter getFilter() {
        return filter;
    }

    public Boolean getLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}