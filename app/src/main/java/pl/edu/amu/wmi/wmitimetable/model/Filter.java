package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Filter {

    @Getter @Setter
    private ArrayList<String> years = new ArrayList<>();

    @Getter @Setter
    private ArrayList<String> groups = new ArrayList<>();

    @Getter @Setter
    private ArrayList<String> studies = new ArrayList<>();

    public boolean yearExists(String yearName){
        for (String year : years) {
            if(year.equals(yearName)){
                return true;
            }
        }
        return  false;
    }

    public boolean groupExists(String groupName){
        for (String group : groups) {
            if(group.equals(groupName)){
                return true;
            }
        }
        return  false;
    }

    public boolean studyExists(String studyName){
        for (String study : studies) {
            if(study.equals(studyName)){
                return true;
            }
        }
        return false;
    }

    public void clear(){
        years = new ArrayList<>();
        groups = new ArrayList<>();
        studies = new ArrayList<>();
    }
}
