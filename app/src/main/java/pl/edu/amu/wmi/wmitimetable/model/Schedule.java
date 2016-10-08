package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import lombok.Data;

@Data
public class Schedule implements Serializable {
    int id;
    String when;
    String group;
    String subject;
    String year;
    String study;
    String leader;
    String room1;
    String room2;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getWhen() {
        return when;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getRoom1() {
        return room1;
    }

    public void setRoom1(String room1) {
        this.room1 = room1;
    }

    public String getRoom2() {
        return room2;
    }

    public void setRoom2(String room2) {
        this.room2 = room2;
    }
}
