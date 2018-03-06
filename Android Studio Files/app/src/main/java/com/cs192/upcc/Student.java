/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Ciana Lim.
 */

/* Code History
 * Programmer           Date     Description
 * Ciana Lim            3/6/18   Created file
 */

/*
 * File Creation Date: 2/18/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;

import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class Student {
     private Curriculum curriculum;
     private ArrayList<Subject> subjects_taken;
     private DatabaseHelper UPCCdb;
     private int totalUnits;

     /*
      * Name: Student
      * Creation Date: 3/6/18
      * Purpose: Constructor for the object
      * Arguments:
      *      UPCCdb - DatabaseHelper, an instance of the database
      *      curriculum - Curriculum, the curriculum selected
      * Other Requirements:
      *      none
      * Return Value: none
      */
     public Student(DatabaseHelper UPCCdb, Curriculum curriculum){
          this.UPCCdb = UPCCdb;
          this.curriculum = curriculum;
          this.subjects_taken = new ArrayList<Subject>();

          /* get the student's data from the database (subjects that were passed) */
          Cursor res = this.UPCCdb.getStudentData();

          /* if there is data inside the table */
          if(res.moveToFirst()){
               /* if the selected/current curriculum is equal to the curriculum previously being used by the student */
               if(res.getString(0).equals(curriculum.getName())){
                    /* add the subjects that the student has taken from the database to the object */
                    for(Subject subject : this.curriculum.getSubjects()){
                         if(subject.getSubjectName().equals(res.getString(1))){
                              this.subjects_taken.add(subject);
                              this.totalUnits = this.totalUnits + subject.getUnits();
                              break;
                         }
                    }
                    while (res.moveToNext()) {
                         for(Subject subject : this.curriculum.getSubjects()){
                              if(subject.getSubjectName().equals(res.getString(1))){
                                   this.subjects_taken.add(subject);
                                   this.totalUnits = this.totalUnits + subject.getUnits();
                                   break;
                              }
                         }
                    }
               }
               else{ // if the selected curriculum is different from what was previously used
                    /* reset the table */
                    int isDeleted = this.UPCCdb.deleteAllStudentData();
                    this.totalUnits = 0;
               }
          }


     }

     /*
      * Name: getSubjectsTaken
      * Creation Date: 3/6/18
      * Purpose: Returns list of taken subjects
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: ArrayList<Subject>
      */
     public ArrayList<Subject> getSubjectsTaken(){
          return this.subjects_taken;
     }

     /*
      * Name: getCurriculum
      * Creation Date: 3/6/18
      * Purpose: Returns the curriculum being used
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: Curriculum
      */
     public Curriculum getCurriculum(){
          return this.curriculum;
     }

     /*
      * Name: getTotalUnits
      * Creation Date: 3/6/18
      * Purpose: Returns the total number of units taken
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: int
      */
     public int getTotalUnits(){
          return this.totalUnits;
     }

     /*
      * Name: toggle_subject
      * Creation Date: 3/6/18
      * Purpose: Checks to see if a subject will be added/deleted to/from the database, and will do appropriate actions
      * Arguments:
      *      subject - Subject, the subject that was clicked
      * Other Requirements:
      *      none
      * Return Value: none
      */
     public void toggle_subject(Subject subject){
          int isDeleted = 0;
          Iterator<Subject> iter = this.subjects_taken.iterator();
          Subject iterSubject;

          /* while there are subjects in the array */
          while(iter.hasNext()){
               iterSubject = iter.next();
               /* if the subject clicked already exists in the table, delete it from the database and from the array */
               if(iterSubject.getSubjectName().equals(subject.getSubjectName())){
                    this.totalUnits = this.totalUnits - subject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), subject.getSubjectName());
                    iter.remove();
                    Log.d("delete", subject.getSubjectName());
               }
          }

          /* if nothing was deleted from the database, it means that the entry was never there in the first place */
          /* insert the selected subject (means it was taken) */
          if(isDeleted == 0){
               this.totalUnits = this.totalUnits + subject.getUnits();
               boolean isInserted = this.UPCCdb.insertData(this.curriculum.getName(), subject.getSubjectName());
               this.subjects_taken.add(subject);
               Log.d("insert", subject.getSubjectName());
          }
          Log.d("selected", subject.getSubjectName());

          /* checker for prereq violations */
          iter = this.subjects_taken.iterator();
          while(iter.hasNext()){
               iterSubject = iter.next();
               Log.d("prereq", iterSubject.getSubjectName());

               /* gets the prereq list of a specific subject in the subjects taken array */
               ArrayList<String> prereqs = iterSubject.getPrereq();
               int prereq_number = prereqs.size();
               Iterator<Subject> iter_prereq;

               /* checks if prereqs are satisfied */
               for(String prereq_name : prereqs){
                    iter_prereq = this.subjects_taken.iterator();
                    while(iter_prereq.hasNext()){
                         Subject iterPrereq = iter_prereq.next();
                         Log.d("prereq_in_question", iterSubject.getSubjectName());
                         if(prereq_name.equals(iterPrereq.getSubjectName())){
                              Log.d("prereq_in_question", prereq_name);
                              Log.d("prereq_in_question", String.valueOf(prereq_number));
                              prereq_number--;
                              break;
                         }
                    }
               }

               /* if there is a prereq unsatisfied, remove from table and list */
               if(prereq_number > 0){
                    // Log.d("prereq_mali", iterSubject.getSubjectName());
                    this.totalUnits = this.totalUnits - iterSubject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());
                    iter.remove();
                    continue;
               }
               if(iterSubject.isJs()){
                    if(this.totalUnits < 73){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());
                         iter.remove();
                         continue;
                    }
               }
               if(iterSubject.isSs()){
                    if(this.totalUnits < 110){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());
                         iter.remove();
                         continue;
                    }
               }
          }
     }

     /*
      * Name: mark_subject
      * Creation Date: 3/6/18
      * Purpose: For the non-volatility of the app. Checks if a certain subject was marked previously or not
      * Arguments:
      *      subject_name - String, the name of the subject being checked
      * Other Requirements:
      *      none
      * Return Value: boolean
      */
     public boolean mark_subject(String subject_name){
          for(Subject subject : this.subjects_taken){
               Log.d("curriculum", subject.getSubjectName());
               if(subject.getSubjectName().equals(subject_name)){
                    return true;
               }
          }
          return false;
     }

     /*
      * Name: viewSubject
      * Creation Date: 3/6/18
      * Purpose: Debugging purpose. Checks if the stored values in the student table are correct
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: StringBuffer
      */
     public StringBuffer viewSubject(){
          StringBuffer buffer = new StringBuffer();
          for(Subject subject : this.subjects_taken){
               buffer.append(subject.getSubjectName()+"\n");
          }
          return buffer;
     }


}
