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
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;


import java.io.Serializable;
import java.util.ArrayList;


public class Curriculum implements Serializable {
     private ArrayList<Subject> subjects; //The subjects in the curriculum
     private String name; //The name of the curriculum

     /*
     * Name: Curriculum
     * Creation Date: 2/02/18
     * Purpose: Constructor for Curriculum class
     * Arguments:
     *      aName - name of the curriculum
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public Curriculum(String aName) {
          this.subjects = new ArrayList<Subject>();
          this.name = aName;
     }

     /*
     * Name: addSubject
     * Creation Date: 2/02/18
     * Purpose: adds subject to the curriculum
     * Arguments:
     *      aSubject - the subject to be added
     * Other Requirements:
     *      subjects - the list of subjects the curriculum has
     * Return Value: void
     */
     public void addSubject(Subject aSubject) {
          this.subjects.add(aSubject);
     }

     /*
     * Name: isEmpty
     * Creation Date: 2/02/18
     * Purpose: checks if the Curriculum is empty
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: boolean
     */
     public boolean isEmpty() {
          if (subjects.isEmpty()) {
               return true;
          } else {
               return false;
          }
     }

     /*
     * Name: getName
     * Creation Date: 2/02/18
     * Purpose: getter for name
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: String name
     */
     public String getName() {
          return name;
     }

     /*
     * Name: printCurriculum
     * Creation Date: 2/02/18
     * Purpose: stores the content of the curriculum to a string
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: StringBuffer buffer
     */
     public StringBuffer printCurriculum() {
          StringBuffer buffer = new StringBuffer();
          for (Subject subject : subjects) {
               buffer.append(subject.getSubjectPrint());
               buffer.append("---------------\n");
          }
          return buffer;
     }

}
