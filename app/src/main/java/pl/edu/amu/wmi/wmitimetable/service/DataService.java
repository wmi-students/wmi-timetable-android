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

import pl.edu.amu.wmi.wmitimetable.model.Filter;
import pl.edu.amu.wmi.wmitimetable.model.Meeting;
import pl.edu.amu.wmi.wmitimetable.model.MeetingDay;
import pl.edu.amu.wmi.wmitimetable.model.Schedule;
import pl.edu.amu.wmi.wmitimetable.model.World;

public class DataService {

    Gson gson;
    Context context;

    private final String MEETINGS_FILE_NAME = "meetings.json";

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

    public void saveMeetingsToFile(ArrayList<Meeting> meetings) throws IOException{
        String content = serializeMeetings(meetings);
        writeToFile(MEETINGS_FILE_NAME, content);
    }

    public ArrayList<Meeting> loadMeetingsFromFile() throws IOException{
        String content = readFromFile(MEETINGS_FILE_NAME);
        ArrayList<Meeting> meetings = deserializeMeetings(content);
        return meetings;
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

    public void writeToFile(String fileName, String content) throws IOException {
        FileOutputStream outputStream = this.context.openFileOutput(fileName, Context.MODE_PRIVATE);
        outputStream.write(content.getBytes());
        outputStream.close();
    }

    public boolean isDataFile() {
        File file = new File(context.getFilesDir(), MEETINGS_FILE_NAME);
        return  file.exists();
    }

    public boolean getLoaded() {
        return  World.getInstance().getLoaded();
    }
}
