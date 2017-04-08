package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class SpecialFilter implements Serializable {
    private String subject;
    private String year;
    private String study;
    private String group;
}
