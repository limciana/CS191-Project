package com.cs192.upcc;

import android.app.AlertDialog;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * Created by CruzAlon on 02/02/2018.
 */

public class Subject implements Serializable{
    public String curriculum;
    public String subject_name;
    public String subject_description;
    public int units;
    public boolean isJs;
    public boolean isSs;
    public int yearToBeTaken;
    public ArrayList<String> prereq;
    public ArrayList<String> coreq;

    public Subject(String aCurriculum, String aSubject_name, String aSubject_desc, int aUnits, boolean anIsJs, boolean anIsSs, int aYearToBeTaken, String aPrereq, String aCoreq) {
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

        String[] coreqData = getArrayStringSplit(aCoreq);

        if(coreqData != null) {
            for (String subject : coreqData) {
                coreq.add(subject.trim());
            }
        }

        String[] prereqData = getArrayStringSplit(aPrereq);

        if(prereqData != null) {
            for (String subject : prereqData) {
                prereq.add(subject.trim());
            }
        }
    }


    private String[] getArrayStringSplit(String aString){
        String[] data;
        if(aString != null) {
            data = aString.split(",");
            return data;
        } else {
            return null;
        }


    }
    public String getCurriculumName() {
        return this.curriculum;
    }

    public String getSubjectName() {
        return this.subject_name;
    }

    public String getSubjectDesc() {
        return this.subject_description;
    }

    public int getUnits(){
        return this.units;
    }

    public boolean isJs(){
        return this.isJs;
    }

    public boolean isSs(){
        return this.isSs;
    }

    public int getYearToBeTaken(){
        return this.yearToBeTaken;
    }

    public ArrayList<String> getPrereq(){
        return this.prereq;
    }

    public ArrayList<String> getCoreq(){
        return this.coreq;
    }


    public StringBuffer getSubjectPrint(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("Curriculum: " + this.curriculum + "\n");
        buffer.append("Name: " + this.subject_name + "\n");
        buffer.append("Desc: " + this.subject_description + "\n");
        buffer.append("Units: " + this.units + "\n");
        buffer.append("JS: " + booleanToString(isJs) + "\n");
        buffer.append("SS " + booleanToString(isSs) + "\n");
        buffer.append("Year: " + this.yearToBeTaken + "\n");
        buffer.append("Prereq: " + this.prereq + "\n");
        buffer.append("Coreq: " + this.coreq + "\n");
        return buffer;
    }
    private String booleanToString(Boolean aBool){
        if(aBool){
            return "true";
        } else {
            return "false";
        }
    }


}
