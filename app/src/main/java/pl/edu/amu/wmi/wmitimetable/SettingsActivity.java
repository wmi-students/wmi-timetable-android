package pl.edu.amu.wmi.wmitimetable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.service.SettingsService;
import pl.edu.amu.wmi.wmitimetable.task.SchedulesRestTask;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerYear, spinnerStudy, spinnerGroup;
    private ProgressDialog dialog;
    private DataService dataService;
    private SettingsService settingsService;
    private Button buttonShowMeetings;
    private Button buttonReloadData;


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

        buttonShowMeetings = (Button) findViewById(R.id.buttonShowMeetings);
        buttonReloadData = (Button) findViewById(R.id.buttonReload);

        dialog = new ProgressDialog(SettingsActivity.this);
        dataService = new DataService(getApplicationContext());
        settingsService = new SettingsService(this);

        loadData();
    }

    private void loadData() {
        if(dataService.getLoaded() && settingsService.settingsExists()){
            loadFilters();
        }else {
            if (dataService.isDataFile() && settingsService.settingsExists()) {
                dataService.loadMeetings();
                goMeetings();
            } else {
                new LoadDataTask().execute();
            }
        }
    }

    public void reloadDataClick(View view){
        buttonReloadData.setVisibility(View.INVISIBLE);
        loadData();
    }

    private void showButtons(int visibility) {
        spinnerStudy.setVisibility(visibility);
        spinnerYear.setVisibility(visibility);
        spinnerGroup.setVisibility(visibility);
        buttonShowMeetings.setVisibility(visibility);
    }

    private class LoadDataTask extends SchedulesRestTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Wczytywanie zajęć... :)");
            dialog.show();
            showButtons(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(ArrayList<Meeting> meetings) {
            super.onPostExecute(meetings);

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            if(meetings == null){
                showNoDataInfo();
                buttonReloadData.setVisibility(View.VISIBLE);
            }else {
                dataService.setMeetings(meetings);
                dataService.saveMeetings();

                loadFilters();
                showButtons(View.VISIBLE);
            }
        }
    }

    private void showNoDataInfo() {
        Snackbar.make(findViewById(android.R.id.content), "Sprawdź połączenie internetowe :(", Snackbar.LENGTH_LONG).show();
    }

    private void goMeetings() {
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
