package pl.edu.amu.wmi.wmitimetable;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import lombok.Setter;
import pl.edu.amu.wmi.wmitimetable.adapter.MeetingListAdapter;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.model.World;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;

public class MainActivity extends AppCompatActivity {

    private DataService dataService;
    private SettingsService settingsService;
    private ArrayList<Meeting> meetings = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.dataService = new DataService(getApplicationContext());
        this.settingsService = new SettingsService(this);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        /*
      The {@link android.support.v4.view.PagerAdapter} that will provide
      fragments for each of the sections. We use a
      {@link FragmentPagerAdapter} derivative, which will keep every
      loaded fragment in memory. If this becomes too memory intensive, it
      may be best to switch to a
      {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        loadData();

        // Set up the ViewPager with the sections adapter.
        /*
      The {@link ViewPager} that will host the section contents.
     */
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void loadData() {
        meetings = filterMeetings(dataService.getMeetings());
    }

    private ArrayList<Meeting> filterMeetings(ArrayList<Meeting> meetings) {
        ArrayList<Meeting> filteredMeetings = new ArrayList<>();

        for (Meeting meeting : meetings) {
            if(meetingHasFiteredSchedules(meeting)){
                filteredMeetings.add(meeting);
            }
        }
        return filteredMeetings;
    }

    private boolean meetingHasFiteredSchedules(Meeting meeting) {
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

    private void goSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_MEETING = "meeting_object";

        MeetingListAdapter meetingArrayAdapter;
        ListView meetingListView;

        Meeting meeting;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, Meeting meeting) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.setMeeting(meeting);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int pageNr = getArguments().getInt(ARG_SECTION_NUMBER);

            meetingListView = (ListView) rootView.findViewById(R.id.list_meeting_days);
            ArrayList<MeetingDay> meetingDays = meeting.getMeetingDays();
            meetingArrayAdapter = new MeetingListAdapter(getActivity(), R.layout.meeting_list_item, meetingDays);
            meetingListView.setAdapter(meetingArrayAdapter);

            return rootView;
        }

        public void setMeeting(Meeting meeting) {
            this.meeting = meeting;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            int offset = 0;
            for (Meeting meeting : meetings) {
                if(meeting.getDate().before(DateTime.now().plusDays(-1).toDate())){
                    offset++;
                }else{
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
        public int getCount() {
            return  5;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            if(position < meetings.size()-1) {
                Meeting meeting = meetings.get(position);
                SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM", new Locale("pl", "PL"));
                return simpleDate.format(meeting.getDate());
            }else{
                return "...";
            }
        }
    }
}
