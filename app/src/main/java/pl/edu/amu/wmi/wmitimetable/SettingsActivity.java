package pl.edu.amu.wmi.wmitimetable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.World;
import pl.edu.amu.wmi.wmitimetable.service.DataService;
import pl.edu.amu.wmi.wmitimetable.task.SchedulesRestTask;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinnerYear, spinnerStudy, spinnerGroup;
    private ProgressDialog dialog;
    DataService dataService;

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

        if(!dataService.isLoaded()) {
            loadData();
        }
    }

    private void loadData() {
        if(dataService.loadMeetings()){
            showMeetings(null);
        }else{
            new LoadDataTask().execute();
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

            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    public void showMeetings(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void loadFilters() {
        // YEARS
        String[] listYear = new String[]{"Rok 1", "Rok 2","Rok 3","Rok 4","Rok 5"};
        ArrayAdapter<String> dataAdapterYear = new ArrayAdapter<>(this, R.layout.settings_spinner_item, listYear);
        dataAdapterYear.setDropDownViewResource(R.layout.settings_spinner_dropdown_item);
        spinnerYear.setAdapter(dataAdapterYear);

        // STUDIES
        String[] listStudies = new String[]{
                "Matematyka - Licencjackie",
                "Matematyka - Uzupełaniające",
                "Nauczanie Matematyki i Informatyki - Licencjat",
                "Nauczanie Matematyki i Informatyki - Uzupełniające",
                "Informatyka - Inżynier",
                "Informatyka - Uzupełniające (magister)",
                "Informatyka - Uzupełniające (magister po inżynier)",
                "Informatyka - Uzupełniające (magister inżynier)",
                "Podyplomowe - Informatyka i Technologie Informacyjne",
                "Podyplomowe - Matematyka"
        };
        ArrayAdapter<String> dataAdapterStudies = new ArrayAdapter<>(this, R.layout.settings_spinner_item, listStudies);
        dataAdapterStudies.setDropDownViewResource(R.layout.settings_spinner_dropdown_item);
        spinnerStudy.setAdapter(dataAdapterStudies);


    }

}
