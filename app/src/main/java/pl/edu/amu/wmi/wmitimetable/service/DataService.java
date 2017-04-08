package pl.edu.amu.wmi.wmitimetable.service;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import pl.edu.amu.wmi.wmitimetable.model.Filter;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.model.SpecialFilter;
import pl.edu.amu.wmi.wmitimetable.model.World;

public class DataService {

    private Gson gson;
    private Context context;

    public final String MEETINGS_FILE_NAME = "meetings.json";
    public final String SPECIAL_FILTERS_FILE_NAME = "special_filters.json";

    public DataService(Context context) {
        this.context = context;
        this.gson = new Gson();
    }

    public Filter getFilter(){
        return World.getInstance().getFilter();
    }

    public ArrayList<Meeting> getMeetings(){
        return  World.getInstance().getMeetings();
    }

    public void setMeetings(ArrayList<Meeting> meetings){
        World.getInstance().setMeetings(meetings);
    }

    public boolean loadMeetings(){
        try {
            World.getInstance().setMeetings(loadMeetingsFromFile());
            return true;
        }catch (IOException exc){
            World.getInstance().clear();
            return false;
        }
    }

    public boolean saveMeetings(){
        try {
            saveMeetingsToFile(World.getInstance().getMeetings());
            return true;
        }catch (IOException exc){
            return false;
        }
    }

    private void saveMeetingsToFile(ArrayList<Meeting> meetings) throws IOException {
        String content = serializeMeetings(meetings);
        writeToFile(MEETINGS_FILE_NAME, content);
    }

    private ArrayList<Meeting> loadMeetingsFromFile() throws IOException {
        String content = readFromFile(MEETINGS_FILE_NAME);
        return deserializeMeetings(content);
    }

    private ArrayList<Meeting> deserializeMeetings(String content) {
        return gson.fromJson(content,new TypeToken<ArrayList<Meeting>>() {}.getType());
    }

    private String readFromFile(String fileName) throws IOException {
        File file = new File(context.getFilesDir(), fileName);

        // read file
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append('\n');
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

    public boolean deleteLocalData(){
        if(deleteFile(MEETINGS_FILE_NAME)){
            World.getInstance().setLoaded(false);
            return true;
        }else {
            return false;
        }
    }

    private boolean deleteFile(String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return file.delete();
    }

    private String serializeMeetings(ArrayList<Meeting> meetings) {
        return gson.toJson(meetings);
    }

    private void writeToFile(String fileName, String content) throws IOException {
        FileOutputStream outputStream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    public boolean isMeetingsDataFile() {
        return isDataFile(MEETINGS_FILE_NAME);
    }

    public boolean isDataFile(String fileName){
        File file = new File(context.getFilesDir(), fileName);
        return  file.exists();
    }

    public boolean getLoaded() {
        return  World.getInstance().getLoaded();
    }

    public List<SpecialFilter> getSpecialFilters(){
        return  World.getInstance().getSpecialFilters();
    }

    public void setSpecialFilters(List<SpecialFilter> specialFilters){
        World.getInstance().setSpecialFilters(specialFilters);
    }
    public boolean loadSpecialFilters() {
        try {
            World.getInstance().setSpecialFilters(loadSpecialFiltersFromFile());
            return true;
        }catch (IOException exc){
            World.getInstance().clear();
            return false;
        }
    }

    public boolean saveSpecialFilters(){
        try {
            saveSpecialFiltersToFile(World.getInstance().getSpecialFilters());
            return true;
        }catch (IOException exc){
            return false;
        }
    }
    private List<SpecialFilter> loadSpecialFiltersFromFile() throws IOException {
        String content = readFromFile(SPECIAL_FILTERS_FILE_NAME);
        return deserializeSpecialFilters(content);
    }

    private List<SpecialFilter> deserializeSpecialFilters(String content) {
        return gson.fromJson(content,new TypeToken<ArrayList<SpecialFilter>>() {}.getType());
    }
    private void saveSpecialFiltersToFile(List<SpecialFilter> specialFilters) throws IOException {
        String content = serializeSpecialFilters(specialFilters);
        writeToFile(SPECIAL_FILTERS_FILE_NAME, content);
    }

    private String serializeSpecialFilters(List<SpecialFilter> specialFilters) {
        return gson.toJson(specialFilters);
    }

    public boolean isSpecialFilterDataFile() {
        return isDataFile(SPECIAL_FILTERS_FILE_NAME);
    }

    public boolean scheduleInSpecialFilters(Schedule schedule) {
        List<SpecialFilter> specialFilters = getSpecialFilters();

        for (SpecialFilter specialFilter : specialFilters) {
            if(specialFilter.getSubject().equals(schedule.getSubject()) &&
                    specialFilter.getStudy().equals(schedule.getStudy())  &&
                    specialFilter.getYear().equals(schedule.getYear())
                    ){
                // optional group
                if(specialFilter.getGroup()== null ) {
                    return  true;
                }

                if (schedule.getGroup().contains("WA") || schedule.getGroup().toLowerCase().equals(specialFilter.getGroup().toLowerCase())) {
                    return true;
                }
            }
        }

        return false;
    }
}
