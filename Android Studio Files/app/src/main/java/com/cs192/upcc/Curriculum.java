package com.cs192.upcc;

import java.util.ArrayList;

/**
 * Created by CruzAlon on 02/02/2018.
 */

public class Curriculum {
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
}
