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
 * Ciana Lim      1/31/18  Included methods to view the curriculums available, and the subjects under a selected curriculum.
 * Ciana Lim      2/4/18   Added citations.
 */

/*
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

/* 
 * Citations
 * ProgrammingKnowledge. Android SQLite Database Tutorial 4 # Show SQLite Database table Values using Android. Last accessed: February 4, 2018
 */

package com.cs192.upcc;

import android.app.AlertDialog;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
     DatabaseHelper UPCCdb; // the database instance of the application
     EditText editCurriculum, editName, editDescription, editUnits, editJS, editSS, editYear, editPrereq, editCoreq; // the fields used for inserting to the database
     Button btnAddData; // button driver to add the data
     boolean isInserted; // checks if the data is inserted in the database
     Button btnviewAll; // button driver to view all the data stored in the database
     Button btnviewCurriculum; // button driver to view the curriculums in the database
     AlertDialog.Builder builder; // instance to be used for the dialog
     StringBuffer buffer; // buffer string to show the data stored in the database
     Cursor res; // the resulting rows selected from the query found in DatabaseHelper.java
     CheckBox checkbox; // the checkboxes for the curriculums
     LinearLayout linearMain; // instance to add components to the screen
     String selectedCurriculum; // the user's selected curriculum

     @Override
     public <T extends View> T findViewById(int id) {
          return super.findViewById(id);
     }

     /*
           * Name: onCreate
           * Creation Date: 1/27/18
           * Purpose: Renders the layout and the database on the main activity
           * Arguments:
           *      savedInstanceState - Bundle, for passing data between Android activities
           * Other Requirements:
           *      UPCCdb - DatabaseHelper, calls the constructor method of DatabaseHelper to create the database
           *      checkbox - CheckBox, creates a new checkbox for the curriculum name
           * Return Value: void
           */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main);
          linearMain = (LinearLayout)findViewById(R.id.layout);
          UPCCdb = new DatabaseHelper(this);

          btnviewAll = (Button)findViewById(R.id.button_viewAll);

          UPCCdb.createDB();
          viewAll();

          /* to get the curriculums available */
          res = UPCCdb.getCurriculum();
          if(res.getCount() == 0){
               showMessage("Error", "Nothing found");
               return;
          }
          while(res.moveToNext()) {
               checkbox = new CheckBox(this);
               checkbox.setId(res.getPosition());
               checkbox.setText(res.getString(0));
               checkbox.setOnClickListener(
                       new View.OnClickListener(){
                            @Override
                            public void onClick(View v){
                                 selectedCurriculum = (String)checkbox.getText();

                                 /* to get the subjects under the curriculum selected */
                                 res = UPCCdb.getSubjects(selectedCurriculum);
                                 if(res.getCount() == 0){
                                      showMessage("Error", "Nothing found");
                                      return;
                                 }
                                 buffer = new StringBuffer();
                                 while(res.moveToNext()){
                                      buffer.append("Curriculum: " + res.getString(0)+"\n");
                                      buffer.append("Subject name: " + res.getString(1)+"\n");
                                      buffer.append("Description: " + res.getString(2)+"\n");
                                      buffer.append("Units: " + res.getString(3)+"\n");
                                      buffer.append("JS: " + res.getString(4)+"\n");
                                      buffer.append("SS: " + res.getString(5)+"\n");
                                      buffer.append("Year: " + res.getString(6)+"\n");
                                      buffer.append("Prerequisites: " + res.getString(7)+"\n");
                                      buffer.append("Corequisites: " + res.getString(8)+"\n\n");
                                 }
                                 showMessage("Subjects under the curriculum", buffer.toString());
                            }
                       }
               );
               linearMain.addView(checkbox);
               // buffer.append(res.getString(0)+"\n");
          }
          // showMessage("Curriculums Available", buffer.toString());
     }



     /*
      * Name: viewCurriculum
      * Creation Date: 1/31/18
      * Purpose: Listens to when the View Curriculums button is clicked, and when it is clicked, it shows the data stored in the variable res
      * Arguments: none
      * Other Requirements:
      *   UPCCdb - DatabaseHelper, stores the methods and the database instance used
      *   showMessage() - method, used to create an Alert Dialog to show the data stored in the variable res
      * Return Value: void
      */
     public void viewCurriculum(){
          btnviewCurriculum.setOnClickListener(
                  new View.OnClickListener(){
                       @Override
                       public void onClick(View v){
                            res = UPCCdb.getCurriculum();
                            if(res.getCount() == 0){
                                 showMessage("Error", "Nothing found");
                                 return;
                            }
                            buffer = new StringBuffer();
                            while(res.moveToNext()){
                                 buffer.append(res.getString(0)+"\n");
                            }
                            showMessage("Curriculums Available", buffer.toString());
                       }
                  }
          );
     }

     /*
      * Name: viewAll
      * Creation Date: 1/31/18
      * Purpose: Listens to when the View All button is clicked, and when it is clicked, it shows the data stored in the variable res
      * Arguments: none
      * Other Requirements:
      *   UPCCdb - DatabaseHelper, stores the methods and the database instance used
      *   showMessage() - method, used to create an Alert Dialog to show the data stored in the variable res
      * Return Value: void
      */
     public void viewAll(){
          btnviewAll.setOnClickListener(
                  new View.OnClickListener(){
                       @Override
                       public void onClick(View v) {
                            res = UPCCdb.getAllData();
                            if(res.getCount() == 0){
                                 showMessage("Error", "Nothing found");
                                 return;
                            }
                            buffer = new StringBuffer();
                            while(res.moveToNext()){
                                 buffer.append("Curriculum: " + res.getString(0)+"\n");
                                 buffer.append("Subject name: " + res.getString(1)+"\n");
                                 buffer.append("Description: " + res.getString(2)+"\n");
                                 buffer.append("Units: " + res.getString(3)+"\n");
                                 buffer.append("JS: " + res.getString(4)+"\n");
                                 buffer.append("SS: " + res.getString(5)+"\n");
                                 buffer.append("Year: " + res.getString(6)+"\n");
                                 buffer.append("Prerequisites: " + res.getString(7)+"\n");
                                 buffer.append("Corequisites: " + res.getString(8)+"\n\n");
                            }

                            // show all data
                            showMessage("Data", buffer.toString());
                       }
                  }
          );
     }

     /*
      * Name: showMessage
      * Creation Date: 1/31/18
      * Purpose: Creates the Alert Dialog box
      * Arguments:
      *   title - String, the title of the Alert Dialog box
      *   Message - String, the message to be shown in the Alert Dialog box
      * Other Requirements: none
      * Return Value: void
      */
     public void showMessage(String title, String Message){
          builder = new AlertDialog.Builder(this);
          builder.setCancelable(true);
          builder.setTitle(title);
          builder.setMessage(Message);
          builder.show();
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
