package pl.edu.amu.wmi.wmitimetable.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.edu.amu.wmi.wmitimetable.R;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.service.ScheduleService;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;

public class MeetingListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MeetingDay> meetingDays;
    private SettingsService settingsService;
    private DataService dataService;
    private ScheduleService scheduleService;

    public MeetingListAdapter(Context context, List<MeetingDay> meetingDays) {
        this.context = context;
        this.meetingDays = meetingDays;
        settingsService = new SettingsService((Activity) context);
        scheduleService = new ScheduleService();
        dataService = new DataService(context);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filterSchedules(this.meetingDays.get(groupPosition).getSchedules()).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return filterSchedules(this.meetingDays.get(groupPosition).getSchedules()).get(childPosition).getId();
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meeting_item, null);
        }

        List<Schedule> schedules = filterSchedules(meetingDays.get(groupPosition).getSchedules());
        Schedule schedule = schedules.get(childPosition);

        LinearLayout item = (LinearLayout) convertView.findViewById(R.id.schedule_item);
        TextView textSubject = (TextView) convertView.findViewById(R.id.schedule_subject);
        TextView textTime = (TextView) convertView.findViewById(R.id.schedule_time);
        TextView textDetails = (TextView) convertView.findViewById(R.id.schedule_details);

        DateFormat format = new SimpleDateFormat("HH:mm", new Locale("pl", "PL"));
        Date date = scheduleService.getDateFromSchedule(schedule);
        textTime.setText(format.format(date));

        if (schedule.isSpecial()) {
            item.setBackgroundResource(R.color.colorSpecial);
        }else{
            item.setBackgroundResource(R.color.colorSettingsBackground);
        }

        if (scheduleIsNow(date)) {
            textTime.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            textSubject.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            textDetails.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }

        String roomInfo = schedule.getRoom1();
        String room2 = normalizeString(schedule.getRoom2());
        if (!room2.isEmpty()) {
            roomInfo += " " + room2;
        }

        textDetails.setText(schedule.getGroup() + "  " + roomInfo + "  Rok: " +  schedule.getYear() + "  " + schedule.getLeader());
        textSubject.setText(schedule.getSubject());

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filterSchedules(this.meetingDays.get(groupPosition).getSchedules()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.meetingDays.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.meetingDays.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        MeetingDay meetingDay = meetingDays.get(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meeting_header, null);
        }

        TextView day = (TextView) convertView.findViewById(R.id.meeting_day_header);
        Date dayDate = meetingDay.getDate();
        DateFormat format = new SimpleDateFormat("dd MMMM yyyy", new Locale("pl", "PL"));
        day.setText(format.format(dayDate));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private ArrayList<Schedule> filterSchedules(ArrayList<Schedule> schedules) {
        ArrayList<Schedule> filteredSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            if (settingsService.scheduleInFilter(schedule)) {
                filteredSchedules.add(schedule);
            } else if (dataService.scheduleInSpecialFilters(schedule)) {
                schedule.setSpecial(true);
                filteredSchedules.add(schedule);
            }
        }

        Collections.sort(filteredSchedules);

        return filteredSchedules;
    }

    private String normalizeString(String text) {
        text = text.replace("\u00A0", "");
        return text;
    }

    private boolean scheduleIsNow(Date scheduleDate) {
        Date start = scheduleDate;
        Date end = scheduleService.getScheduleEndDate(scheduleDate);
        Date now = DateTime.now().toDate();
        return now.after(start) && now.before(end);
    }
}