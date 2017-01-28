package pl.edu.amu.wmi.wmitimetable.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class SpecialFilter implements Serializable {

    public SpecialFilter() {
    }

    public SpecialFilter(String subject, String year, String study){
        setSubject(subject);
        setYear(year);
        setStudy(study);
    }

    private String subject;
    private String year;
    private String study;
}
