package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;
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
}
