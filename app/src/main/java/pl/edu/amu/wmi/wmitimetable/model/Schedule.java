package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class Schedule implements Serializable {
    private int id;
    private String when;
    private String group;
    private String subject;
    private String year;
    private String study;
    private String leader;
    private String room1;
    private String room2;

    private transient boolean special;
}
