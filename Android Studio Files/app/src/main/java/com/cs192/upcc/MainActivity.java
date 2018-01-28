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
 * Ciana Lim      1/28/18  Modified database set-up to accept pre-seeded data.
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
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
     DatabaseHelper UPCCdb; // the database instance of the application
     EditText editCurriculum, editName, editDescription, editUnits, editJS, editSS, editYear, editPrereq, editCoreq; // the fields used for inserting to the database
     Button btnAddData; // button driver to add the data
     boolean isInserted; // checks if the data is inserted in the database

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

          editCurriculum = (EditText)findViewById(R.id.editText_curriculum);
          editName = (EditText)findViewById(R.id.editText_name);
          editDescription = (EditText)findViewById(R.id.editText_description);
          editUnits = (EditText)findViewById(R.id.editText_units);
          editJS = (EditText)findViewById(R.id.editText_js);
          editSS = (EditText)findViewById(R.id.editText_ss);
          editYear = (EditText)findViewById(R.id.editText_year);
          editPrereq = (EditText)findViewById(R.id.editText_prereq);
          editCoreq = (EditText)findViewById(R.id.editText_coreq);
          btnAddData = (Button)findViewById(R.id.button_add);
          UPCCdb.createDB();
     }

     /* public void AddData(){
          btnAddData.setOnClickListener(
                  new View.OnClickListener(){
                       @Override
                       public void onClick(View v){
                            isInserted = UPCCdb.insertData(editCurriculum.getText().toString(),
                                    editName.getText().toString(),
                                    editDescription.getText().toString(),
                                    editUnits.getText().toString(),
                                    editJS.getText().toString(),
                                    editSS.getText().toString(),
                                    editYear.getText().toString(),
                                    editPrereq.getText().toString(),
                                    editCoreq.getText().toString() );
                            if(isInserted == true){
                                 Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            }
                            else{
                                 Toast.makeText(MainActivity.this, "Data not inserted", Toast.LENGTH_LONG).show();
                            }
                       }
                  }
          );
     } */
}
