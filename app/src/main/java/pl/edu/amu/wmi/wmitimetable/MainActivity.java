package pl.edu.amu.wmi.wmitimetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import pl.edu.amu.wmi.wmitimetable.adapter.MeetingListAdapter;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.task.SchedulesDateTask;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;
import pl.edu.amu.wmi.wmitimetable.task.SchedulesRestTask;

public class MainActivity extends AppCompatActivity {

    private static final String ARG_MEETINGS = "meetings_object";
    private DataService dataService;
    private SettingsService settingsService;
    private ArrayList<Meeting> meetings = new ArrayList<>();
    private SectionsPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dataService = new DataService(getApplicationContext());
        this.settingsService = new SettingsService(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        if (dataService.isDataFile() && settingsService.settingsExists()) {
            dataService.loadMeetings();
        } else {
            goSettings();
            return;
        }

        if (savedInstanceState == null) {
            loadData();
        } else {
            meetings = (ArrayList<Meeting>) savedInstanceState.getSerializable(ARG_MEETINGS);
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        TextView textStudy = (TextView)findViewById(R.id.filter_study);
        TextView textYear = (TextView)findViewById(R.id.filter_year);
        TextView textGroup = (TextView)findViewById(R.id.filter_group);

        textStudy.setText("Kierunek: "+settingsService.loadSetting("study"));
        textYear.setText("Rok: "+settingsService.loadSetting("year"));
        textGroup.setText("Grupa: "+settingsService.loadSetting("group"));

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        refreshDataIfNewer();
    }

    private void refreshDataIfNewer() {
        if (dataOutdated()) {
            new BackgroundLoadData(pagerAdapter).execute();
        }
    }

    public boolean dataOutdated() {
        Date dataDate = settingsService.getDataDate();
        Date schedulesDate = null;
        try {
            schedulesDate = new SchedulesDateTask().execute().get().toDate();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return dataDate == null || dataDate.before(schedulesDate);
    }

    private class BackgroundLoadData extends SchedulesRestTask {
        SectionsPagerAdapter pagerAdapter;

        public BackgroundLoadData(SectionsPagerAdapter pagerAdapter) {
            super();
            this.pagerAdapter = pagerAdapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            settingsService.setDataDate(new Date());
        }

        @Override
        protected void onPostExecute(ArrayList<Meeting> meetings) {
            super.onPostExecute(meetings);
            dataService.setMeetings(meetings);
            dataService.saveMeetings();
            loadData();
            this.pagerAdapter.notifyDataSetChanged();
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.data_updated, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable(ARG_MEETINGS, meetings);
    }

    private void loadData() {
        meetings = filterMeetings(dataService.getMeetings());
    }

    private ArrayList<Meeting> filterMeetings(ArrayList<Meeting> meetings) {
        ArrayList<Meeting> filteredMeetings = new ArrayList<>();

        for (Meeting meeting : meetings) {
            if(meetingHasFilteredSchedules(meeting)){
                filteredMeetings.add(meeting);
            }
        }
        return filteredMeetings;
    }

    private boolean meetingHasFilteredSchedules(Meeting meeting) {
        for (MeetingDay meetingDay : meeting.getMeetingDays()) {
            for (Schedule schedule : meetingDay.getSchedules()) {
                if(settingsService.scheduleInFilter(schedule)){
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            showSettings();
            return true;
        }

        if (id == R.id.action_reset) {
            settingsReset();
            return true;
        }

        if(id == R.id.action_about) {
            showAbout();
        }

        return super.onOptionsItemSelected(item);
    }

    private void settingsReset() {
        deleteData();
        resetSettings();
        goSettings();
    }

    private void resetSettings(){
        settingsService.saveSetting("study", null);
        settingsService.saveSetting("year", null);
        settingsService.saveSetting("group", null);
    }

    private void deleteData() {
        dataService.deleteLocalData();
    }

    private void showSettings() {
        goSettings();
    }

    private void showAbout() {
        goAbout();
    }

    private void goSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void goAbout() {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_MEETING = "meeting_object";

        MeetingListAdapter meetingArrayAdapter;
        ListView meetingListView;

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, Meeting meeting) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable(ARG_MEETING, meeting);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            //int position = getArguments().getInt(ARG_SECTION_NUMBER);
            Meeting meeting = (Meeting) getArguments().getSerializable(ARG_MEETING);

            meetingListView = (ListView) rootView.findViewById(R.id.list_meeting_days);
            ArrayList<MeetingDay> meetingDays = meeting.getMeetingDays();
            meetingArrayAdapter = new MeetingListAdapter(getActivity(), R.layout.meeting_list_item, meetingDays);
            meetingListView.setAdapter(meetingArrayAdapter);

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            update();
        }

        private void update() {
            meetingArrayAdapter.notifyDataSetChanged();
        }
    }

    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int offset = 0;
            for (Meeting meeting : meetings) {
                if (meeting.getDate().before(DateTime.now().plusDays(-2).toDate())) {
                    offset++;
                } else {
                    break;
                }
            }

            Meeting meeting;
            int meetingIndex = position + offset;
            if(meetingIndex>meetings.size()-1){
                meeting = new Meeting();
            }else{
                meeting = meetings.get(meetingIndex);
            }
            return PlaceholderFragment.newInstance(meetingIndex, meeting);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE; // when you call notifyDataSetChanged() the view pager will remove all views and reload them all. @see BackgroundLoadData
        }

        @Override
        public int getCount() {
            return  5;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            int offset = 0;
            for (Meeting meeting : meetings) {
                if (meeting.getDate().before(DateTime.now().plusDays(-2).toDate())) {
                    offset++;
                } else {
                    break;
                }
            }
            int meetingIndex = position + offset;
            if (meetingIndex > meetings.size() - 1) {
                return "...";
            } else {
                Meeting meeting = meetings.get(meetingIndex);
                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM", new Locale("pl", "PL"));
                return simpleDate.format(meeting.getDate());

            }
        }
    }
}
