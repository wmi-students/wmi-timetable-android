package pl.edu.amu.wmi.wmitimetable.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pl.edu.amu.wmi.wmitimetable.R;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.ScheduleService;

public class MeetingDayListAdapter extends ArrayAdapter<Schedule> {

    ScheduleService scheduleService = new ScheduleService();

    public MeetingDayListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MeetingDayListAdapter(Context context, int resource, List<Schedule> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.meeting_day_list_item, null);
        }

        Schedule schedule = getItem(position);

        TextView textTime = (TextView) view.findViewById(R.id.schedule_time);
        TextView textGroup = (TextView) view.findViewById(R.id.schedule_group);
        TextView textRoom = (TextView) view.findViewById(R.id.schedule_room);
        TextView textSubject = (TextView) view.findViewById(R.id.schedule_subject);

        DateFormat format = new SimpleDateFormat("HH:mm");
        Date date = scheduleService.getDateFromSchedule(schedule);
        textTime.setText(format.format(date));

        textRoom.setText(schedule.getRoom1());
        textGroup.setText(schedule.getGroup());
        textSubject.setText(schedule.getSubject());

        return view;
    }

}