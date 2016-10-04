package pl.edu.amu.wmi.wmitimetable.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;

public class ScheduleService {
    public ArrayList<Meeting> convertSchedulesToMeetings(ArrayList<Schedule> schedules){
        ArrayList<MeetingDay> meetingDays = groupSchedulesToMeetingDays(schedules);
        ArrayList<Meeting> meetings = groupMeetingDaysToMeetings(meetingDays);
        return  meetings;
    }

    public ArrayList<MeetingDay> groupSchedulesToMeetingDays(ArrayList<Schedule> schedules) {
        // hash table do stworzenia dni zjazdu
        Map<Date,MeetingDay> meetingDaysMap = new HashMap<>();

        for (Schedule schedule : schedules) {
            // pobranie dnia bez godzin i minut będzie kluczem
            Date day = getDayFromSchedule(schedule);
            if(meetingDaysMap.containsKey(day)){
                // jeśli istnieje już meeting day o takim kluczu to dodaj do niego
                meetingDaysMap.get(day).getSchedules().add(schedule);
            }else{
                // jeśli nie istnieje to stwórz nowy
                MeetingDay meetingDay = new MeetingDay();
                meetingDay.setDate(day);
                meetingDay.getSchedules().add(schedule);
                meetingDaysMap.put(day, meetingDay);
            }
        }
        return new ArrayList<>(meetingDaysMap.values());
    }

    public ArrayList<Meeting> groupMeetingDaysToMeetings(ArrayList<MeetingDay> meetingDays) {

        ArrayList<Meeting> meetings = new ArrayList<>();

        for (MeetingDay meetingDay : meetingDays) {
            Date dayDate = meetingDay.getDate();
            Meeting meeting = findNearestMeeting(meetings, dayDate);
            if(meeting == null){
                meeting = new Meeting();
                meetings.add(meeting);
                meeting.setDate(dayDate);
            }else{
                if(dayDate.before(meeting.getDate())){
                    meeting.setDate(dayDate);
                }
            }
            meeting.getMeetingDays().add(meetingDay);
        }

        return  meetings;
    }

    public Meeting findNearestMeeting(ArrayList<Meeting> meetings, Date dayDate) {
        for (Meeting meeting : meetings) {
            long differenceInDays = getDifferenceDays(meeting.getDate(),dayDate);
            if(differenceInDays<2){
                return meeting;
            }
        }
        return null;
    }

    public Date getDayFromSchedule(Schedule schedule) {
        try {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            Date scheduleDate = format.parse(schedule.getWhen());
            return removeTime(scheduleDate);
        }catch (Exception exc){
            return null;
        }
    }

    public static Date removeTime(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    public long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
