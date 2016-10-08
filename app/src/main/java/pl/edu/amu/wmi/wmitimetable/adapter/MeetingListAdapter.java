package pl.edu.amu.wmi.wmitimetable.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pl.edu.amu.wmi.wmitimetable.R;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;

public class MeetingListAdapter extends ArrayAdapter<MeetingDay> {


    ListView meetingDayListView;
    MeetingDayListAdapter meetingDayArrayAdapter;
    SettingsService settingsService;

    public MeetingListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MeetingListAdapter(Context context, int resource, List<MeetingDay> items) {
        super(context, resource, items);
        settingsService = new SettingsService((Activity) context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.meeting_list_item, null);
        }

        MeetingDay meetingDay = getItem(position);

        meetingDayListView = (ListView) view.findViewById(R.id.list_schedules);
        ArrayList<Schedule> schedules = filterSchedules( meetingDay.getSchedules());
        meetingDayArrayAdapter = new MeetingDayListAdapter(view.getContext(),R.layout.meeting_day_list_item, schedules);
        meetingDayListView.setAdapter(meetingDayArrayAdapter);

        TextView day = (TextView) view.findViewById(R.id.meeting_day_header);
        Date dayDate = meetingDay.getDate();
        DateFormat format = new SimpleDateFormat("dd MMM", new Locale("pl", "PL"));
        day.setText(format.format(dayDate));

        return view;
    }

    public ArrayList<Schedule> filterSchedules(ArrayList<Schedule> schedules) {
        ArrayList<Schedule> filteredSchedules = new ArrayList<>();

        for (Schedule schedule : schedules) {
            if(settingsService.scheduleInFilter(schedule)){
                filteredSchedules.add(schedule);
            }
        }

        return filteredSchedules;
    }
}