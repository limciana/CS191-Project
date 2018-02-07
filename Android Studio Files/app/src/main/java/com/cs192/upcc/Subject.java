/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Rayven Ely Cruz.
 */

/* Code History
 * Programmer       Date        Description
 * Rayven Ely Cruz  2/02/2018   Created the class.
 */

/*
 * File Creation Date: 2/02/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable {
     private String curriculum;  //The name of the curriculum the subject belongs to
     private String subjectName; //The name of the subject
     private String subjectDescription; //The subject description
     private int units; //The number of units the subject is worth
     private boolean isJs; //Does the subject require junior standing?
     private boolean isSs; //Does the subject require senior standing?
     private int yearToBeTaken; //What year is the subject recommended to be taken
     private ArrayList<String> prereq; //List of the subject's prerequisites
     private ArrayList<String> coreq; //List of hte subject's corequisites

     /*
    * Name: Subject
    * Creation Date: 1/30/18
    * Purpose: Constructor for the object
    * Arguments:
    *      aCurriculum - the name of the curriculum the subject belongs to
    *      aSubjectName - the name of the subject
    *      aSubjectDesc - the description of the subject
    *      aUnits - the number of units of the subject
    *      anIsJs - Does the subject require Junior standing
    *      anIsSs - Does the subject require Senior standing
    *      aYearToBeTaken - year the subject is recommended
    *      aPrereq - list of prereqs of the subject (comma separated)
    *      aCoreq - list of coreqs of the subject (comma separated)
    * Other Requirements:
    *      Serializable
    * Return Value: none
    */
     public Subject(String aCurriculum, String aSubjectName, String aSubjectDesc, int aUnits, boolean anIsJs, boolean anIsSs, int aYearToBeTaken, String aPrereq, String aCoreq) {
          /* Load the arguments' values */
          this.curriculum = aCurriculum;
          this.subjectName = aSubjectName;
          this.subjectDescription = aSubjectDesc;
          this.units = aUnits;
          this.isJs = anIsJs;
          this.isSs = anIsSs;
          this.yearToBeTaken = aYearToBeTaken;

          /* Setup and handle comma separated string list of prereqs and coreqs */
          this.prereq = new ArrayList<String>();
          this.coreq = new ArrayList<String>();

          String[] coreqData = getArrayStringSplit(aCoreq);
          String[] prereqData = getArrayStringSplit(aPrereq);

          if (coreqData != null) {
               for (String subject : coreqData) {
                    coreq.add(subject.trim());
               }
          }

          if (prereqData != null) {
               for (String subject : prereqData) {
                    prereq.add(subject.trim());
               }
          }
     }

     /*
     * Name: getArraySplitString
     * Creation Date: 2/02/18
     * Purpose: splits the comma separated string and stores it in an array
     * Arguments:
     *      aString - the csv
     * Other Requirements:
     *      none
     * Return Value: String[] - an array of the strings of the contents of aString
     */
     private String[] getArrayStringSplit(String aString) {
          String[] data;
          if (aString != null) {
               data = aString.split(",");
               return data;
          } else {
               return null;
          }


     }

     /*
     * Name: getCurriculumName
     * Creation Date: 2/02/18
     * Purpose: getter for curriculumName
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: String - curriculum
     */
     public String getCurriculumName() {
          return this.curriculum;
     }

     /*
     * Name: getSubjectName
     * Creation Date: 2/02/18
     * Purpose: getter for subjectName
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: String - subjectName
     */
     public String getSubjectName() {
          return this.subjectName;
     }

     /*
     * Name: getSubjectDesc
     * Creation Date: 2/02/18
     * Purpose: getter for subjectDesc
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: String - subectDescription
     */
     public String getSubjectDesc() {
          return this.subjectDescription;
     }

     /*
     * Name: getUnits
     * Creation Date: 2/02/18
     * Purpose: getter for units
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: int - units
     */
     public int getUnits() {
          return this.units;
     }

     /*
     * Name: isJs
     * Creation Date: 2/02/18
     * Purpose: getter for isJs
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: boolean - isJs
     */
     public boolean isJs() {
          return this.isJs;
     }

     /*
     * Name: isSs
     * Creation Date: 2/02/18
     * Purpose: getter for isSs
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: boolean - isSs
     */
     public boolean isSs() {
          return this.isSs;
     }

     /*
     * Name: getYearToBeTaken
     * Creation Date: 2/02/18
     * Purpose: getter for yearToBeTaken
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: int yearToBeTaken
     */
     public int getYearToBeTaken() {
          return this.yearToBeTaken;
     }

     /*
     * Name: getPrereq
     * Creation Date: 2/02/18
     * Purpose: getter for prereq
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: ArrayList<String> orereq
     */
     public ArrayList<String> getPrereq() {
          return this.prereq;
     }

     /*
     * Name: getCoreq
     * Creation Date: 2/02/18
     * Purpose: getter for coreq
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: ArrayList<String> coreq
     */
     public ArrayList<String> getCoreq() {
          return this.coreq;
     }

     /*
     * Name: getSubjectPrint
     * Creation Date: 2/02/18
     * Purpose: for printing purposes
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: StringBuffer buffer - returns the contents of the subject as a StringBuffer
     */
     public StringBuffer getSubjectPrint() {
          StringBuffer buffer = new StringBuffer();
          buffer.append("Curriculum: " + this.curriculum + "\n");
          buffer.append("Name: " + this.subjectName + "\n");
          buffer.append("Desc: " + this.subjectDescription + "\n");
          buffer.append("Units: " + this.units + "\n");
          buffer.append("JS: " + booleanToString(isJs) + "\n");
          buffer.append("SS " + booleanToString(isSs) + "\n");
          buffer.append("Year: " + this.yearToBeTaken + "\n");
          buffer.append("Prereq: " + this.prereq + "\n");
          buffer.append("Coreq: " + this.coreq + "\n");
          return buffer;
     }

     /*
     * Name: booleanToString
     * Creation Date: 2/02/18
     * Purpose: converts boolean to string
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: String
     */
     private String booleanToString(Boolean aBool) {
          if (aBool) {
               return "true";
          } else {
               return "false";
          }
     }
}
