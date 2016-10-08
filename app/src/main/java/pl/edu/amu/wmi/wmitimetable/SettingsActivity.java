package pl.edu.amu.wmi.wmitimetable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.audiofx.BassBoost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import java.util.ArrayList;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;
import pl.edu.amu.wmi.wmitimetable.task.SchedulesRestTask;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerYear, spinnerStudy, spinnerGroup;
    private ProgressDialog dialog;
    DataService dataService;
    SettingsService settingsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        View settingsView = findViewById(R.id.activity_settings);
        View settingRootView = settingsView.getRootView();
        settingRootView.setBackgroundColor(getResources().getColor(R.color.colorSettingsBackground));

        spinnerYear = (Spinner) findViewById(R.id.spinnerYear);
        spinnerStudy = (Spinner) findViewById(R.id.spinnerStudy);
        spinnerGroup = (Spinner) findViewById(R.id.spinnerGroup);

        dialog = new ProgressDialog(SettingsActivity.this);
        dataService = new DataService(getApplicationContext());
        settingsService = new SettingsService(this);

        loadData();
    }

    private void loadData() {
        if(dataService.getLoaded()){
            loadFilters();
        }else {
            if (dataService.isDataFile()) {
                dataService.loadMeetings();
                goMeetings();
            } else {
                new LoadDataTask().execute();
            }
        }
    }

    private class LoadDataTask extends SchedulesRestTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Wczytywanie zajęć...");
            dialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<Meeting> meetings) {
            super.onPostExecute(meetings);

            dataService.setMeetings(meetings);
            dataService.saveMeetings();

            loadFilters();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void goMeetings(){
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void showMeetings(View view){
        saveSettings();
        goMeetings();
    }

    private void saveSettings() {
        String study = (String) spinnerStudy.getSelectedItem();
        settingsService.saveSetting("study", study);
        String year = (String) spinnerYear.getSelectedItem();
        settingsService.saveSetting("year", year);
        String group = (String) spinnerGroup.getSelectedItem();
        settingsService.saveSetting("group", group);
    }

    private void loadFilters() {
        loadStudies();
        loadYears();
        loadGroups();
    }

    private void loadStudies() {
        ArrayAdapter<String> dataAdapterStudies = new ArrayAdapter<>(this, R.layout.settings_spinner_item, dataService.getFilter().getStudies());
        dataAdapterStudies.setDropDownViewResource(R.layout.settings_spinner_dropdown_item);
        spinnerStudy.setAdapter(dataAdapterStudies);
        String study = settingsService.loadSetting("study");
        if(study != null) {
            spinnerStudy.setSelection(dataAdapterStudies.getPosition(study));
        }
    }
    private void loadYears() {
        ArrayAdapter<String> dataAdapterYear = new ArrayAdapter<>(this, R.layout.settings_spinner_item, dataService.getFilter().getYears());
        dataAdapterYear.setDropDownViewResource(R.layout.settings_spinner_dropdown_item);
        spinnerYear.setAdapter(dataAdapterYear);
        String year = settingsService.loadSetting("year");
        if(year != null) {
            spinnerYear.setSelection(dataAdapterYear.getPosition(year));
        }
    }

    private void loadGroups() {
        ArrayAdapter<String> dataAdapterGroups = new ArrayAdapter<>(this, R.layout.settings_spinner_item, dataService.getFilter().getGroups());
        dataAdapterGroups.setDropDownViewResource(R.layout.settings_spinner_dropdown_item);
        spinnerGroup.setAdapter(dataAdapterGroups);
        String group = settingsService.loadSetting("group");
        if(group != null) {
            spinnerGroup.setSelection(dataAdapterGroups.getPosition(group));
        }
    }
}
