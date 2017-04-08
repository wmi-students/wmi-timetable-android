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
import pl.edu.amu.wmi.wmitimetable.model.SpecialFilter;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;

public class SpecialFiltersAdapter extends ArrayAdapter<SpecialFilter> {


//    private ListView meetingDayListView;
//    private MeetingDayListAdapter meetingDayArrayAdapter;
//    private SettingsService settingsService;

    public SpecialFiltersAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public SpecialFiltersAdapter(Context context, int resource, List<SpecialFilter> items) {
        super(context, resource, items);
        //settingsService = new SettingsService((Activity) context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.special_filter_item, parent, false);
        }

        SpecialFilter specialFilter = getItem(position);

        TextView textSubject = (TextView) view.findViewById(R.id.subject);
        textSubject.setText(specialFilter.getSubject());

        TextView textStudy = (TextView) view.findViewById(R.id.study);
        textStudy.setText(specialFilter.getStudy());

        TextView textYear = (TextView) view.findViewById(R.id.year);
        textYear.setText(specialFilter.getYear());

        TextView textGroup = (TextView) view.findViewById(R.id.group);
        textGroup.setText(specialFilter.getGroup());

        return view;
    }
}