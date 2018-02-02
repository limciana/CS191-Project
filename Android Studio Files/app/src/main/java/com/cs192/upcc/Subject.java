package com.cs192.upcc;

import java.util.ArrayList;
/**
 * Created by CruzAlon on 02/02/2018.
 */

public class Subject {
    private String curriculum;
    private String subject_name;
    private String subject_description;
    private int units;
    private boolean isJs;
    private boolean isSs;
    private int yearToBeTaken;
    private ArrayList<String> prereq;
    private ArrayList<String> coreq;
    public Subject(String aCurriculum, String aSubject_name, String aSubject_desc, int aUnits, boolean anIsJs, boolean anIsSs, int aYearToBeTaken, String aPrereq, String aCoreq){
        this.curriculum = aCurriculum;
        this.subject_name = aSubject_name;

        this.subject_description = aSubject_desc;
        this.units = aUnits;
        this.isJs = anIsJs;
        this.isSs = anIsSs;
        this.yearToBeTaken = aYearToBeTaken;

        /*Split using delimiters; To be changed when format is known*/
        this.prereq = new ArrayList<String>();
        this.coreq = new ArrayList<String>();

        if(!aCoreq.isEmpty()){
            this.coreq.add(aCoreq);
        }

        if(!aPrereq.isEmpty()) {
            this.prereq.add(aPrereq);
        }
    }
}
