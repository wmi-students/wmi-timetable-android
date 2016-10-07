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
import java.util.Date;

import pl.edu.amu.wmi.wmitimetable.adapter.MeetingListAdapter;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.World;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;

public class MainActivity extends AppCompatActivity {

    DataService dataService;
    SettingsService settingsService;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        //FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    public void onButtonClick(View view) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            int pageNr = getArguments().getInt(ARG_SECTION_NUMBER);

            meetingListView = (ListView)  rootView.findViewById(R.id.list_meeting_days);
            ArrayList<MeetingDay> meetingDays = World.getInstance().getMeetings().get(pageNr).getMeetingDays();
            meetingArrayAdapter = new MeetingListAdapter(getActivity(),R.layout.meeting_list_item,meetingDays);
            meetingListView.setAdapter(meetingArrayAdapter);


//            if( World.getInstance().getLoaded()) {
//                // TODO
//                //new MeetingService().getMeeting()
//                Meeting meeting = World.getInstance().getMeetings().get(pageNr);
//                textView.setText(meeting.getDate().toString());
//                textSubject.setText(meeting.getMeetingDays().get(0).getSchedules().get(0).getSubject());
//            }else {
//                textView.setText("...");
//            }
            return rootView;
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
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Date today = new Date();
            int offset = 0;
            for (Meeting meeting : dataService.getMeetings()) {
                if(meeting.getDate().before(DateTime.now().plusDays(-1).toDate())){
                    offset++;
                }else{
                    break;
                }
            }
            return PlaceholderFragment.newInstance(position + offset);
        }

        @Override
        public int getCount() {
            return  5;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            if(World.getInstance().getLoaded()) {
                SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM");
                Meeting meeting = World.getInstance().getMeetings().get(position);
                return simpleDate.format(meeting.getDate());
            }else{
                return "...";
            }
        }
    }
}
