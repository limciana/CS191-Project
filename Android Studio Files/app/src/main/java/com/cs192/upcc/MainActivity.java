/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Ciana Lim.
 */

/* Code History
 * Programmer     Date     Description
 * Ciana Lim      1/27/18  Set up the database for the application
 */

/*
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
     DatabaseHelper UPCCdb; // the database instance of the application

     /*
      * Name: onCreate
      * Creation Date: 1/27/18
      * Purpose: Renders the layout and the database on the main activity
      * Arguments:
      *      savedInstanceState - Bundle, for passing data between Android activities
      * Other Requirements:
      *      UPCCdb - DatabaseHelper, calls the constructor method of DatabaseHelper to create the database
      * Return Value: void
      */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          UPCCdb = new DatabaseHelper(this);
     }
}
