package pl.edu.amu.wmi.wmitimetable.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public class Filter {
    private ArrayList<String> years = new ArrayList<>();
    private ArrayList<String> groups = new ArrayList<>();
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

    public ArrayList<String> getYears() {
        return years;
    }

    public void setYears(ArrayList<String> years) {
        this.years = years;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public ArrayList<String> getStudies() {
        return studies;
    }

    public void setStudies(ArrayList<String> studies) {
        this.studies = studies;
    }
}
