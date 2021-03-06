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
 * Ciana Lim            3/7/18   Added methods to keep track of coreqs
 * Rayven Ely Cruz      3/8/18   Added methods for checking standings
 * Ciana Lim            3/9/18   Remove coreq restriction
 * Ciana Lim            4/7/18   Added functions that will help in the "warning" function of InputSubjectFragment.java
 * Rayven Ely Cruz      4/11/18  Updated standing updates
 * Rayven Ely Cruz      4/13/18  Updated methods
 * Rayven Ely Cruz      4/24/18  Fixed Standing
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

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Student {
     private Curriculum curriculum; // the curriculum of the student
     private ArrayList<Subject> subjects_taken; // the list of subjects the student has taken
     private DatabaseHelper UPCCdb; // the database instance
     private int totalUnits; // the total units taken by the student
     private int standing; // the current standing of the student
     private int[] unitsPerYear; //the number of units per year as recommended
     private int[] takenUnitsPerYear; // the number of units taken per year that are not GEs
     private int[] percentageUnits; // percent of units for each year
     private int[] GEsPerYear; // GEs per year
     private int takenGEs; // the number of GE units taken
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
          this.standing = UPCC.STUDENT_FRESHMAN;
          /* get the student's data from the database (subjects that were passed) */
          Cursor res = this.UPCCdb.getStudentData();
          takenGEs = 0;
          unitsPerYear = new int[4];
          takenUnitsPerYear = new int[4];
          GEsPerYear = new int[4];
          percentageUnits = new int[4];
          for(int i = 0; i < 4; i++){
               unitsPerYear[i] = 0;
               takenUnitsPerYear[i] = 0;
               GEsPerYear[i] = 0;
               percentageUnits[i] = 0;
          }


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
          /* set standing after loading from db */
          setYearStandings();

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
      * Name: checkSubjectExists
      * Creation Date: 3/7/18
      * Purpose: Checks the existence of a taken subject
      * Arguments:
      *      subject - String, the subject being found
      * Other Requirements:
      *      none
      * Return Value: boolean
      */
     public boolean checkSubjectExists(String subject){
          for(Subject s : this.subjects_taken){
               if(s.getSubjectName().equals(subject)){
                    return true;
               }
          }
          return false;
     }


     /*
      * Name: toggle_subject
      * Creation Date: 3/6/18
      * Purpose: Checks to see if a subject will be added/deleted to/from the database, and will do appropriate actions
      * Arguments:
      *      subject - Subject, the subject that was clicked
      *      selection - int, variable that signifies the state of the subject that was clicked
      *                  (0 - first time the subject was clicked
      *                   1 - the user will mark a subject
      *                   2 - the user tries to unmark a subject (the first time)
      *                   3 - the user confirms that he/she will unmark the subject
      *                   4 - the user cancels the unmarking of the subject
      *                   5 - the job of the function is done)
      * Other Requirements:
      *      none
      * Return Value: int
      */
     public int toggle_subject(Subject subject, int selection){
          int isDeleted = 0;
          Iterator<Subject> iter = this.subjects_taken.iterator();
          Subject iterSubject;



          /* while there are subjects in the array */
          while(iter.hasNext()){
               iterSubject = iter.next();
               /* if the subject clicked already exists in the table */
               if(iterSubject.getSubjectName().equals(subject.getSubjectName())){
                    if(selection == 0){ // if it is the user's first time to unmark the subject
                        return 2; // return to InputSubjectFragment that it is the user's first time to remove
                    }
                    else if(selection == 3){
                        // if the user confirms to remove, delete from the database and from the array
                        this.totalUnits = this.totalUnits - subject.getUnits();
                        isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), subject.getSubjectName());
                        iter.remove();
                        Log.d("delete", subject.getSubjectName());

                        /* get year units for standing , delete subject */
                        int year = 0;
                        if(subject.getYearToBeTaken() != 0) {
                            year = subject.getYearToBeTaken() - 1;
                            if(year >= 0 && year <= 4) {
                                takenUnitsPerYear[year] -= subject.getUnits();
                            }
                        } else {
                            takenGEs -= subject.getUnits();
                        }
                        checkYearStandings();
                    }
                    else if(selection == 4){
                        // if the user cancels the unmarking, return to InputSubjectFragment that the function is done
                        return 5;
                    }
               }
          }

          /* if nothing was deleted from the database, it means that the entry was never there in the first place */
          /* insert the selected subject (means it was taken) */
          if(isDeleted == 0){
               this.totalUnits = this.totalUnits + subject.getUnits();
               boolean isInserted = this.UPCCdb.insertData(this.curriculum.getName(), subject.getSubjectName());
               this.subjects_taken.add(subject);
               Log.d("insert", subject.getSubjectName());

               /* get year units for standing , add subject*/
               int year = 0;
               if(subject.getYearToBeTaken() != 0) {
                    year = subject.getYearToBeTaken() - 1;
                    if(year >= 0 && year <= 4) {
                         takenUnitsPerYear[year] += subject.getUnits();
                    }
               } else {
                    takenGEs += subject.getUnits();
               }
               checkYearStandings();
          }
          Log.d("selected", subject.getSubjectName());

          /* checker for prereq violations */
          iter = this.subjects_taken.iterator();
          while(iter.hasNext()){
               iterSubject = iter.next();
               Log.d("prereq", iterSubject.getSubjectName());

               /* gets the prereq list of a specific subject in the subjects taken array */
               ArrayList<String> prereqs = iterSubject.getPrereq();
               //ArrayList<String> coreqs = iterSubject.getCoreq();
               int prereq_number = prereqs.size();
               //int coreq_number = coreqs.size();
               Iterator<Subject> iter_prereq;
               //ArrayList<Subject> iter_coreq;

               /* checks if prereqs are satisfied */
               for(String prereq_name : prereqs){
                    iter_prereq = this.subjects_taken.iterator();
                    while(iter_prereq.hasNext()){
                         Subject iterPrereq = iter_prereq.next();
                         Log.d("prereq_in_question", iterSubject.getSubjectName());
                         if(prereq_name.equals(iterPrereq.getSubjectName())){
                              Log.d("prereq_in_question", prereq_name);
                              Log.d("prereq_in_question", String.valueOf(prereq_number));

                              /* checks if the coreqs of the prereq are satisfied */
                              /*ArrayList<String> prereq_coreqs = iterPrereq.getCoreq();
                              int prereq_coreq_number = prereq_coreqs.size();
                              for(String coreq : prereq_coreqs){
                                   for(Subject satisfied_coreq : this.subjects_taken){
                                        if(satisfied_coreq.getSubjectName().equals(coreq)){
                                             prereq_coreq_number--;
                                             break;
                                        }
                                   }
                              }*/
                              /* if all coreqs are satisfied */
                              //if(prereq_coreq_number == 0){
                                   prereq_number--;
                              //}
                              break;
                         }
                    }
               }

               /* checks if the prereqs of the coreqs are satisfied */
               /*iter_coreq = this.curriculum.getSubjects(); // all the subjects in the curiculum
               for(String coreq_name : coreqs){
                    for(Subject c : iter_coreq){ // find the subject in question (the coreq)
                         int coreq_prereq_number = 0;
                         if(c.getSubjectName().equals(coreq_name)){
                              ArrayList<String> coreq_prereqs = c.getPrereq();
                              coreq_prereq_number = coreq_prereqs.size();
                              for(String p : coreq_prereqs){
                                   iter_prereq = this.subjects_taken.iterator();
                                   while(iter_prereq.hasNext()){
                                        Subject iterPrereq = iter_prereq.next();
                                        if(p.equals(iterPrereq.getSubjectName())){
                                             coreq_prereq_number--;
                                             break;
                                        }
                                   }
                              }*/
                              /* if all the prereqs of the coreq is satisfied */
                              /*if(coreq_prereq_number == 0){
                                   coreq_number--;
                              }
                              break;
                         }
                    }
               }*/

               /* if all coreqs are satisfied */
               /*if(coreq_number > 0){
                    this.totalUnits = this.totalUnits - iterSubject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());
                    iter.remove();
                    continue;
               }*/

               /* if there is a prereq unsatisfied, remove from table and list */
               if(prereq_number > 0){
                    // Log.d("prereq_mali", iterSubject.getSubjectName());
                    this.totalUnits = this.totalUnits - iterSubject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                    /* get year units for standing , delete subject */
                    int year = 0;
                    if(iterSubject.getYearToBeTaken() != 0) {
                         year = iterSubject.getYearToBeTaken() - 1;
                         if(year >= 0 && year <= 4) {
                              takenUnitsPerYear[year] -= iterSubject.getUnits();
                         }
                    } else {
                         takenGEs -= iterSubject.getUnits();
                    }
                    checkYearStandings();

                    iter.remove();
                    continue;

               }
               /* check if the subject satisfies JS */
               if(iterSubject.isJs()){
                    if(this.totalUnits < Math.ceil(this.curriculum.getUnits()*0.50) || standing < UPCC.STUDENT_JUNIOR){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                         /* get year units for standing , delete subject */
                         int year = 0;
                         if(iterSubject.getYearToBeTaken() != 0) {
                              year = iterSubject.getYearToBeTaken() - 1;
                              if(year >= 0 && year <= 4) {
                                   takenUnitsPerYear[year] -= iterSubject.getUnits();
                              }
                         } else {
                              takenGEs -= iterSubject.getUnits();
                         }
                         checkYearStandings();

                         iter.remove();
                         continue;
                    }
               }
               /* check if the subject satisfies SS */
               if(iterSubject.isSs()){
                    if(this.totalUnits < Math.ceil(this.curriculum.getUnits()*0.75) || standing < UPCC.STUDENT_SENIOR){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                         /* get year units for standing , delete subject */
                         int year = 0;
                         if(iterSubject.getYearToBeTaken() != 0) {
                              year = iterSubject.getYearToBeTaken() - 1;
                              if(year >= 0 && year <= 4) {
                                   takenUnitsPerYear[year] -= iterSubject.getUnits();
                              }
                         } else {
                              takenGEs -= iterSubject.getUnits();
                         }
                         checkYearStandings();

                         iter.remove();
                         continue;
                    }
               }
          }

          /* reverse the list, to check if the subjects have prereqs from subjects above them */
          Collections.reverse(this.subjects_taken);
          iter = this.subjects_taken.iterator();
          while(iter.hasNext()){
               iterSubject = iter.next();
               Log.d("prereq", iterSubject.getSubjectName());

               /* gets the prereq list of a specific subject in the subjects taken array */
               ArrayList<String> prereqs = iterSubject.getPrereq();
               //ArrayList<String> coreqs = iterSubject.getCoreq();
               int prereq_number = prereqs.size();
               //int coreq_number = coreqs.size();
               Iterator<Subject> iter_prereq;
               //ArrayList<Subject> iter_coreq;

               /* checks if prereqs are satisfied */
               for(String prereq_name : prereqs){
                    iter_prereq = this.subjects_taken.iterator();
                    while(iter_prereq.hasNext()){
                         Subject iterPrereq = iter_prereq.next();
                         Log.d("prereq_in_question", iterSubject.getSubjectName());
                         if(prereq_name.equals(iterPrereq.getSubjectName())){
                              Log.d("prereq_in_question", prereq_name);
                              Log.d("prereq_in_question", String.valueOf(prereq_number));

                              /* checks if the coreqs of the prereq are satisfied */
                              /*ArrayList<String> prereq_coreqs = iterPrereq.getCoreq();
                              int prereq_coreq_number = prereq_coreqs.size();
                              for(String coreq : prereq_coreqs){
                                   for(Subject satisfied_coreq : this.subjects_taken){
                                        if(satisfied_coreq.getSubjectName().equals(coreq)){
                                             prereq_coreq_number--;
                                             break;
                                        }
                                   }
                              }*/
                              /* if all the coreqs are satisfied */
                              /*if(prereq_coreq_number == 0){*/
                                   prereq_number--;
                              //}
                              break;
                         }
                    }
               }

               /* checks if the prereqs of the coreqs are satisfied */
               /*iter_coreq = this.curriculum.getSubjects(); // all the subjects in the curriculum
               for(String coreq_name : coreqs){
                    for(Subject c : iter_coreq){ // find the subject in question (the coreq)
                         int coreq_prereq_number = 0;
                         if(c.getSubjectName().equals(coreq_name)){
                              ArrayList<String> coreq_prereqs = c.getPrereq();
                              coreq_prereq_number = coreq_prereqs.size();
                              for(String p : coreq_prereqs){
                                   iter_prereq = this.subjects_taken.iterator();
                                   while(iter_prereq.hasNext()){
                                        Subject iterPrereq = iter_prereq.next();
                                        if(p.equals(iterPrereq.getSubjectName())){
                                             coreq_prereq_number--;
                                             break;
                                        }
                                   }
                              }*/
                              /* if all the prereqs of the coreqs are satisfied */
                              /*if(coreq_prereq_number == 0){
                                   coreq_number--;
                              }
                              break;
                         }
                    }
               }*/

               /* if there are unsatisfied coreqs, remove from table and list */
               /*if(coreq_number > 0){
                    this.totalUnits = this.totalUnits - iterSubject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());
                    iter.remove();
                    continue;
               }*/

               /* if there is a prereq unsatisfied, remove from table and list */
               if(prereq_number > 0){
                    // Log.d("prereq_mali", iterSubject.getSubjectName());
                    this.totalUnits = this.totalUnits - iterSubject.getUnits();
                    isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                    /* get year units for standing , delete subject */
                    int year = 0;
                    if(iterSubject.getYearToBeTaken() != 0) {
                         year = iterSubject.getYearToBeTaken() - 1;
                         if(year >= 0 && year <= 4) {
                              takenUnitsPerYear[year] -= iterSubject.getUnits();
                         }
                    } else {
                         takenGEs -= iterSubject.getUnits();
                    }
                    checkYearStandings();

                    iter.remove();
                    continue;
               }

               /* check if the subject satisfies JS */
               if(iterSubject.isJs()){
                    if(this.totalUnits < Math.ceil(this.curriculum.getUnits()*0.50) || standing < UPCC.STUDENT_JUNIOR){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                         /* get year units for standing , delete subject */
                         int year = 0;
                         if(iterSubject.getYearToBeTaken() != 0) {
                              year = iterSubject.getYearToBeTaken() - 1;
                              if(year >= 0 && year <= 4) {
                                   takenUnitsPerYear[year] -= iterSubject.getUnits();
                              }
                         } else {
                              takenGEs -= iterSubject.getUnits();
                         }
                         checkYearStandings();

                         iter.remove();
                         continue;
                    }
               }
               /* check if the subject satisfies SS */
               if(iterSubject.isSs()){
                    if(this.totalUnits < Math.ceil(this.curriculum.getUnits()*0.75) || standing < UPCC.STUDENT_SENIOR){
                         this.totalUnits = this.totalUnits - iterSubject.getUnits();
                         isDeleted = this.UPCCdb.deleteData(this.curriculum.getName(), iterSubject.getSubjectName());

                         /* get year units for standing , delete subject */
                         int year = 0;
                         if(iterSubject.getYearToBeTaken() != 0) {
                              year = iterSubject.getYearToBeTaken() - 1;
                              if(year >= 0 && year <= 4) {
                                   takenUnitsPerYear[year] -= iterSubject.getUnits();
                              }
                         } else {
                              takenGEs -= iterSubject.getUnits();
                         }
                         checkYearStandings();

                         iter.remove();
                         continue;
                    }
               }
          }
          /* return to original arranegment */
          Collections.reverse(this.subjects_taken);
          return 5;
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
     /*
      * Name: setStanding
      * Creation Date: 3/23/18
      * Purpose: updates student standing
      * Arguments:
      *      aStanding
      * Other Requirements:
      *      none
      * Return Value: void
      */
     public void setStanding(int aStanding){

          this.standing = aStanding;

     }
     /*
      * Name: updateStanding
      * Creation Date: 3/23/18
      * Purpose: updates student standing
      * Arguments:
      *      aStanding
      * Other Requirements:
      *      none
      * Return Value: void
      */
     public void updateStanding(){
          if( totalUnits >= unitsPerYear[1] ){
               setStanding(UPCC.STUDENT_SOPHOMORE);
          }
          if ( totalUnits >= unitsPerYear[2]) {
               setStanding(UPCC.STUDENT_JUNIOR);
          }
          if (totalUnits >= unitsPerYear[3]) {

               setStanding(UPCC.STUDENT_SENIOR);
          }
     }
     /*
      * Name: getStanding
      * Creation Date: 3/23/18
      * Purpose: gets student standing
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: int
      */
     public int getStanding(){
          return this.standing;
     }

     /*
      * Name: getUnitsPerYearString
      * Creation Date: 4/11/18
      * Purpose: gets units in a year as a string
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: string
      */
     public String getUnitsPerYearString(int year){
          int tempTaken = takenGEs;

          int remaining = percentageUnits[year] - totalUnits;


          return "Units needed: "  + Integer.toString(remaining);
          //return totalUnits + "/" + percentageUnits[year];

     }


     /*
     * Name: setYearStandings
     * Creation Date: 3/8/18
     * Purpose: Sets the number of units per year
     * Arguments:
     *   none
     * Other Requirements: units_junior, units_senior
     * Return Value: void
     */
     private void setYearStandings(){
          /* Query the curriculum selected */
          Cursor res = UPCCdb.getYearlyUnits(getCurriculum().getName());

          /* Initialize taken units from db */

          for( Subject subject : subjects_taken) {
               int year = 0;
               if(subject.getYearToBeTaken() != 0) {
                    year = subject.getYearToBeTaken() - 1;
                    if(year >= 0 && year <= 4) {
                         takenUnitsPerYear[year] += subject.getUnits();
                    }
               } else {
                    takenGEs += subject.getUnits();
               }
          }


          /* Get units per year */
          int i = 0;
          int total = 0;
          while(res.moveToNext() && i < 4){
               unitsPerYear[i] = Integer.parseInt(res.getString(UPCC.CURRICULUM_UNITS));
               total += unitsPerYear[i];
               i++;

          }

          /* Compute units */
          percentageUnits[1] = (int)(Math.ceil(.25f * total));
          percentageUnits[2] = (int)(Math.ceil(.5f * total));
          percentageUnits[3] = (int)(Math.ceil(.75f * total));

          checkYearStandings();


     }
     /*
      * Name: checkYearStandings
      * Creation Date: 3/8/18
      * Purpose: checks current standings based on recommended per year
      * Arguments:
      *   none
      * Other Requirements: student, unitsPerYear, takenUnitsPerYear
      * Return Value: boolean
      */
     private void checkYearStandings(){
          int tempTakenGEs = takenGEs;
          /* iterates over years and checks if the student comply */
          boolean changed = false;

          for(int studentYear = 0; studentYear < 4; studentYear++){
               GEsPerYear[studentYear] = 0;

               /* if student complies */
               Log.d("AYYt" , String.valueOf(takenUnitsPerYear[studentYear]));

               Log.d("units1" , "Year " + UPCC.yearToString(studentYear + 1) + ": " + Integer.toString(takenUnitsPerYear[studentYear] + tempTakenGEs) + " vs " + unitsPerYear[studentYear]);
               Log.d("units1" , "------ " + totalUnits + "------");

               if(takenUnitsPerYear[studentYear] + tempTakenGEs >= unitsPerYear[studentYear] ){
                    /* set standing */
                    if(studentYear <= 4 ) {
                         setStanding(studentYear + 2);
                    }

                    /* subtract GE count that is included in this year */
                    GEsPerYear[studentYear] = unitsPerYear[studentYear] - takenUnitsPerYear[studentYear];
                    tempTakenGEs -= unitsPerYear[studentYear] - takenUnitsPerYear[studentYear];

                    /* checks if the standing updated */
                    changed = true;
               }

               if(totalUnits >= percentageUnits[studentYear]){
                    Log.d("pctg" , "------ " + totalUnits + "vs" + percentageUnits[studentYear] + "------");
                    if(studentYear <= 4){
                         setStanding(studentYear + 1);
                         changed = true;
                    }
               }
          }

          GEsPerYear[standing - 1] = tempTakenGEs;
          if(!changed){
               setStanding(UPCC.STUDENT_FRESHMAN);
          }

     }
}
