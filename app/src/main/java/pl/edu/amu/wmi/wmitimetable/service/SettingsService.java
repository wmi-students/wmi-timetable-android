package pl.edu.amu.wmi.wmitimetable.service;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import pl.edu.amu.wmi.wmitimetable.model.Schedule;

public class SettingsService {
    private Activity activity;
    public SettingsService(Activity activity) {
        this.activity = activity;
    }

    public void saveSetting(String key, String value){
        SharedPreferences sharedPref = activity.getSharedPreferences("pl.edu.amu.pl.wmi.wmitimetable", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String loadSetting(String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences("pl.edu.amu.pl.wmi.wmitimetable", Context.MODE_PRIVATE);
        return sharedPref.getString(key, null);
    }

    public boolean scheduleInFilter(Schedule schedule){
        String group = loadSetting("group");
        String year = loadSetting("year");
        String study = loadSetting("study");
        return  schedule.getStudy().equals(study) && schedule.getYear().equals(year) && (schedule.getGroup().equals(group) || schedule.getGroup().contains("WA"));
    }

    public boolean settingsExists() {
        return loadSetting("study") != null;
    }
}
