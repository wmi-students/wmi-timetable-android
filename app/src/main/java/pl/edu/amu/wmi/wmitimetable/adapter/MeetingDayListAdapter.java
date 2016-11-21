package pl.edu.amu.wmi.wmitimetable.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
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
import java.util.Locale;

import pl.edu.amu.wmi.wmitimetable.R;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.ScheduleService;

class MeetingDayListAdapter extends ArrayAdapter<Schedule> {

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
            view = vi.inflate(R.layout.meeting_day_list_item, parent, false);
        }

        Schedule schedule = getItem(position);

        TextView textTime = (TextView) view.findViewById(R.id.schedule_time);
        TextView textGroup = (TextView) view.findViewById(R.id.schedule_group);
        TextView textRoom = (TextView) view.findViewById(R.id.schedule_room);
        TextView textSubject = (TextView) view.findViewById(R.id.schedule_subject);

        DateFormat format = new SimpleDateFormat("HH:mm", new Locale("pl", "PL"));
        Date date = scheduleService.getDateFromSchedule(schedule);
        textTime.setText(format.format(date));

        if (scheduleIsNow(date)) {
            //view.setBackgroundResource(R.color.colorActive);
            textTime.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            textGroup.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            textRoom.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            textSubject.setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
        }

        String roomInfo = schedule.getRoom1();
        String room2 = normalizeString(schedule.getRoom2());
        if(!room2.isEmpty()){
            roomInfo += " " + room2;
        }


        textRoom.setText(roomInfo);
        textGroup.setText(schedule.getGroup());
        textSubject.setText(schedule.getSubject());

        return view;
    }

    private String normalizeString(String text) {
        text = text.replace("\u00A0", "");
        return text;
    }

    private boolean scheduleIsNow(Date scheduleDate) {
        Date start = scheduleDate;
        Date end = scheduleService.getScheduleEndDate(scheduleDate);
        Date now = DateTime.now().toDate();
        return  now.after(start) && now.before(end);
    }

}