package com.cs192.upcc;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by CruzAlon on 02/02/2018.
 */

public class Curriculum implements Serializable{
    private ArrayList<Subject> subjects;
    private String name;

    public Curriculum(String aName){
        this.subjects = new ArrayList<Subject>();
        this.name = aName;
    }


    public void addSubject(Subject aSubject){
        this.subjects.add(aSubject);
    }

    public boolean isEmpty(){
        if(subjects.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
    public String getName(){
        return name;
    }
    public StringBuffer printCurriculum(){
        StringBuffer buffer = new StringBuffer();
        for(Subject subject : subjects){
            buffer.append(subject.getSubjectPrint());
            buffer.append("---------------\n");
        }
        return  buffer;
    }

}
