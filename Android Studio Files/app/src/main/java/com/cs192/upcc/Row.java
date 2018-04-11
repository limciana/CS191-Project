/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Rayven Ely Cruz.
 */

/* Code History
 * Programmer           Date     Description
 * Rayven Ely Cruz           4/4/18   Created file
 */

/*
 * File Creation Date: 4/4/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

public class Row {
     public static int getCheckBoxIdOnClick(int viewId, int size){
          return viewId - size;
     }
     public static int getCheckBoxId(int viewId){
          return viewId + 1;
     }
     public static int getTextViewId(int viewId, int size){
          return size * 2 + (viewId + 1);
     }
     public static int getRowId(int viewId, int size){
          return size + (viewId + 1);
     }

}
