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
 * Rayven Ely Cruz      2/18/18  Created file
 * Rayven Ely Cruz      2/20/18  Added fragments
 * Rayven Ely Cruz      2/21/18  Modified structure
 * Rayven Ely Cruz      2/22/18  Modified structure
 * Ciana Lim            3/6/18   Included logic so that start screens are now dynamic
 * Rayven Ely Cruz      3/18/18  Created method for passing result to view subjects fragment
 * Rayven Ely Cruz      3/23/18  added required methods for implements
 * Ciana Lim            4/7/18   Add the variable to check if it is the first time the user uses the app or not
 * Rayven Ely Cruz      4/24/18  Disables menu items on start and enabled on the fly
 */

/*
 * File Creation Date: 2/18/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;

public class MainDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SelectCurriculumFragment.OnDataPass, InputSubjectFragment.OnDataPass, ViewSubjectFragment.OnDataPass, ViewSubjectFragment.OnListFragmentInteractionListener {
     Curriculum curriculum; //the curriculum selected by the fragment
     boolean doubleBackToExitPressedOnce; //handles the double back to exit
     DrawerLayout drawer; //layout for the nav drawer
     ActionBarDrawerToggle toggle; //Listener for the nav drawer
     NavigationView navigationView; //nav view variable
     DatabaseHelper UPCCdb;
     private ArrayList<Subject> resultSubjects;
     boolean first; // variable that states if it is the user's first time to select a curriculum or not
     /*
     * Name: onCreate
     * Creation Date: 2/18/18
     * Purpose: Handles the initialization of the activity
     * Arguments:
     *      savedInstanceState
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          UPCCdb = new DatabaseHelper(this);
          UPCCdb.createDB();
          setTheme(R.style.AppTheme_NoActionBar);
          doubleBackToExitPressedOnce = false;
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_main_drawer);

          /* Get Toolbar */
          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);


          /* Setup widgets */
          drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

          toggle = new ActionBarDrawerToggle(
                  this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
          drawer.addDrawerListener(toggle);
          toggle.syncState();

          resultSubjects = new ArrayList<Subject>();
          /* gets the student table, to check if the user has previous input data */
          /* for the app to know which screen to show */
          Cursor res = UPCCdb.getStudentData();

          /* if the table is empty, show the select curriculum screen */
          if(res.getCount() == 0){
               /* Attach SelectCurriculumFragment */
               FragmentManager fragmentManager = getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               SelectCurriculumFragment selectCurriculumFragment = new SelectCurriculumFragment();
               fragmentTransaction.add(R.id.fragContainer, selectCurriculumFragment);
               fragmentTransaction.commit();
               navigationView = (NavigationView) findViewById(R.id.nav_view);
               navigationView.setNavigationItemSelectedListener(this);
               navigationView.setCheckedItem(R.id.nav_select_curriculum);
               first = true;

               /* disable changing fragments */
               navigationView.getMenu().findItem(R.id.nav_view_subjects).setEnabled(false);
               navigationView.getMenu().findItem(R.id.nav_mark_subjects).setEnabled(false);
          } else {
               /* if the table is not empty, show the input subject screen */
               first = false;
               if(res.moveToFirst()){
                    /* just to get the first value in the student table, to know which curriculum to use */

                    /* create the curriculum */
                    this.curriculum = new Curriculum(res.getString(0));
                    res = UPCCdb.getSubjects(res.getString(0));

                    if (res.getCount() == 0) {
                         Toast.makeText(this, "Warning: No Subjects", Toast.LENGTH_SHORT).show();
                    }

                    /* Adds the subjects to the selectedCurriculum from the database */
                    while (res.moveToNext()) {
                         int tempUnits = 0;
                         int tempYear = 0;

                    /* Handles the cases where parsed fields are numm */
                         if (res.getString(UPCC.SUBJECT_YEAR) != null) {
                              tempYear = Integer.parseInt(res.getString(UPCC.SUBJECT_YEAR));
                         }
                         if (res.getString(UPCC.SUBJECT_UNITS) != null) {
                              tempUnits = Integer.parseInt(res.getString(UPCC.SUBJECT_UNITS));
                         }

                    /* Creates the subject from the loaded values */
                         Subject tempSubject = new Subject(res.getString(UPCC.SUBJECT_CURRICULUM), res.getString(UPCC.SUBJECT_NAME),
                                 res.getString(UPCC.SUBJECT_DESC), tempUnits, stringToBoolean(res.getString(UPCC.SUBJECT_JS)),
                                 stringToBoolean(res.getString(UPCC.SUBJECT_SS)), tempYear, res.getString(UPCC.SUBJECT_PREREQ),
                                 res.getString(UPCC.SUBJECT_COREQ));

                    /* Adds the created subject to the curriculum */
                         this.curriculum.addSubject(tempSubject);
                         //demo purpose
                         //this.resultSubjects.add(tempSubject);
                    }
               }

               /* Attach InputSubjectFragment */
               FragmentManager fragmentManager = getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               InputSubjectFragment inputsubjectsfragment = new InputSubjectFragment();
               fragmentTransaction.add(R.id.fragContainer, inputsubjectsfragment);

               fragmentTransaction.commit();
               navigationView = (NavigationView) findViewById(R.id.nav_view);
               navigationView.setNavigationItemSelectedListener(this);
               navigationView.setCheckedItem(R.id.nav_mark_subjects);

               /* enable changing fragments */
               navigationView.getMenu().findItem(R.id.nav_view_subjects).setEnabled(true);
               navigationView.getMenu().findItem(R.id.nav_mark_subjects).setEnabled(true);
          }




     }

     /*
     * Name: onTitlePass
     * Creation Date: 2/22/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - the title string
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onTitlePass(String data) {
          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          toolbar.setTitle(data);
          setSupportActionBar(toolbar);
     }
     /*
     * Name: onSubjectsPass
     * Creation Date: 3/23/18
     * Purpose: Handles the subjects being passed through the interfaces
     * Arguments:
     *      data - the arraylist
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onSubjectsPass(ArrayList<Subject> data) {
          this.resultSubjects = data;
     }

     /*
     * Name: onDataPass
     * Creation Date: 2/22/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - passed data
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onDataPass(String data) {

     }
     /*
     * Name: onUnitsPass
     * Creation Date: 3/23/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - units passed
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onUnitsPass(int data) {
          /* get nav layout */
          TextView header = (TextView) findViewById(R.id.textView);
          String display = String.valueOf(data) + "  units";
          header.setText(display);
     }
     /*
     * Name: onStaningPass
     * Creation Date: 3/23/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - passed data
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onStandingPass(String data) {
          /* get nav layout */
          TextView header = (TextView) findViewById(R.id.header);
          header.setText(data);
     }

     /*
     * Name: onCurriculumPass
     * Creation Date: 2/22/18
     * Purpose: Handles the curriculum being passed through the interfaces
     * Arguments:
     *      data - the curriculum
     *      pass - checks if the passed curriculum should directly be sent to the InputSubjectsFragment
     *      first - variable that states if it is the first time the user selects a curriculum or not
     * Other Requirements:
     *      navigationView
     * Return Value: void
     */
     @Override
     public void onCurriculumPass(Curriculum data, boolean pass, boolean first) {
          this.curriculum = data;
          this.first = first;

          /* enable changing fragments */
          navigationView.getMenu().findItem(R.id.nav_view_subjects).setEnabled(true);
          navigationView.getMenu().findItem(R.id.nav_mark_subjects).setEnabled(true);
           /* Attach InputSubjectFragment */
          if (pass) {
               FragmentManager fragmentManager = getSupportFragmentManager();
               FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
               InputSubjectFragment inputsubjectsfragment = new InputSubjectFragment();
               fragmentTransaction.replace(R.id.fragContainer, inputsubjectsfragment);
               navigationView.setCheckedItem(R.id.nav_mark_subjects);
               fragmentTransaction.commit();
          }
     }
     /*
     * Name: getCurriculum
     * Creation Date: 2/22/18
     * Purpose: Handles the curriculum being passed to fragments
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: Curriculum
     */
     public Curriculum getCurriculum() {
          return this.curriculum;
     }

     /*
      * Name: getCurriculum
      * Creation Date: 4/7/18
      * Purpose: handles the variable "first" that is being passed to fragments. "first" states if it is the first time the user selects a curriculum or not.
      * Arguments:
      *      none
      * Other Requirements:
      *      none
      * Return Value: boolean
      */
     public boolean getFirstTime(){
          return this.first;
     }
     /*
     * Name: onCreateOptionsMenu
     * Creation Date: 2/22/18
     * Purpose: implementation from fragment
     * Arguments:
     *      Menu
     * Other Requirements:
     *      none
     * Return Value: boolean
     */
     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu; this adds items to the action bar if it is present.
          getMenuInflater().inflate(R.menu.main_drawer, menu);


          return true;
     }

     /*
     * Name: onOptionsItemSelected
     * Creation Date: 2/22/18
     * Purpose: Handles the presses on items in the toolbar
     * Arguments:
     *      MenuItem item - the pressed item
     * Other Requirements:
     *      toggle
     * Return Value: boolean
     */
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.

          int id = item.getItemId();

          /* Makes the hamburger clickable */
          if (toggle.onOptionsItemSelected(item)) {
               return true;
          }


          //If help is clicked
          if (id == R.id.action_settings) {
               AlertDialog.Builder builder = new AlertDialog.Builder(this);
               builder.setTitle("Help");

               /* Checks in which page the help is clicked */
               if (getSupportActionBar().getTitle() == "Select Curriculum") {
                    builder.setMessage("Tap a curriculum to select it. \n\nClick the button on the lower right to input subjects from this curriculum.");
               } else if (getSupportActionBar().getTitle() == "Mark Subjects") {
                    builder.setMessage("Tap a subject to input it as passed. \n\nTap again to unselect it. \n\nLong press to learn more about the subject.");

               } else if (getSupportActionBar().getTitle() == "View Subjects") {
                    builder.setMessage("These are the subjects that you can take.");
               }
               builder.show();
               return true;
          }

          return super.onOptionsItemSelected(item);
     }

     /*
     * Name: onNavigationItemSelected
     * Creation Date: 2/22/18
     * Purpose: Handles the onclick event of items in the nav drawer
     * Arguments:
     *      MenuItem item - the pressed item
     * Other Requirements:
     *      none
     * Return Value: boolean
     */
     @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
          // Handle navigation view item clicks here.
          int id = item.getItemId();
          /* For select Curriculum, Input Subjects and About */

          if (id == R.id.nav_select_curriculum) {
               if(curriculum != null) {
          /* Check if currently on different fragment */
                    if (!(navigationView.getMenu().findItem(R.id.nav_select_curriculum).isChecked())) {
               /* Switch to Select Curriculum fragment */
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         SelectCurriculumFragment selectCurriculumFragment = new SelectCurriculumFragment();
                         fragmentTransaction.replace(R.id.fragContainer, selectCurriculumFragment);
                         navigationView.setCheckedItem(R.id.nav_select_curriculum);
                         fragmentTransaction.commit();
                    }
               }
          } else if (id == R.id.nav_mark_subjects) {
               if(curriculum != null) {
          /* Check if currently on different fragment */
                    if (!(navigationView.getMenu().findItem(R.id.nav_mark_subjects).isChecked())) {
               /* Switch to Select Curriculum fragment */
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         InputSubjectFragment inputsubjectsfragment = new InputSubjectFragment();
                         fragmentTransaction.replace(R.id.fragContainer, inputsubjectsfragment);
                         navigationView.setCheckedItem(R.id.nav_mark_subjects);
                         fragmentTransaction.commit();
                    }
               }
          } else if (id == R.id.nav_view_subjects) {
               if(curriculum != null) {
         /* Check if currently on different fragment */
                    if (!(navigationView.getMenu().findItem(R.id.nav_view_subjects).isChecked())) {
               /* Switch to Select Curriculum fragment */
                         FragmentManager fragmentManager = getSupportFragmentManager();
                         FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                         ViewSubjectFragment viewSubjectFragment = new ViewSubjectFragment();
                         fragmentTransaction.replace(R.id.fragContainer, viewSubjectFragment);
                         navigationView.setCheckedItem(R.id.nav_view_subjects);
                         fragmentTransaction.commit();
                    }
               }
          } else if (id == R.id.about) {
          /* Display image for about */
          /* https://stackoverflow.com/questions/6276501/how-to-put-an-image-in-an-alertdialog-android. Last Accessed 02/22/18. Miguel Rivero */
               ImageView image = new ImageView(this);
               image.setImageResource(R.drawable.upcc_logo);

               AlertDialog.Builder builder =
                       new AlertDialog.Builder(this).
                               setMessage("UP Curriculum Checker\nAll Rights Reserved 2018").
                               setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                         dialog.dismiss();
                                    }
                               }).
                               setView(image);
               builder.create().show();
          }

          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          drawer.closeDrawer(GravityCompat.START);
          return true;
     }

     /*
   * Name: onBackPressed
   * Creation Date: 2/19/18
   * Purpose: double tap to back
   * Arguments:
   *      none
   * Other Requirements:
   *      doubleBackToExitPressedOnce
   * Return Value: void
   */
     @Override
     public void onBackPressed() {
          if (doubleBackToExitPressedOnce) {
               super.onBackPressed();
               return;
          }

          this.doubleBackToExitPressedOnce = true;
          Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

          new Handler().postDelayed(new Runnable() {

               @Override
               public void run() {
                    doubleBackToExitPressedOnce = false;
               }
          }, 2000);
     }

     /*
     * Name: stringToBoolean
     * Creation Date: 3/6/18
     * Purpose: converts string to boolean
     * Arguments:
     *      aString - the string to boolean
     * Other Requirements:
     *      none
     * Return Value: boolean
     */
     private boolean stringToBoolean(String aString) {
          /* Handle null strings */
          Log.d("bool", aString);
          if (aString != null) {
               if (aString.equals("1")) {
                    return true;
               } else {
                    return false;
               }
          } else {
               return false;
          }
     }

     /*
     * Name: getResult
     * Creation Date: 3/18/18
     * Purpose: passes the resulting possible subjects to a fragment
     * Arguments:
     *      none
     * Other Requirements:
     *      resultSubjects
     * Return Value: ArrayList<Subject>
     */
     public ArrayList<Subject> getResult(){
          return resultSubjects;
     }
     /*
    * Name: onListFragmentInteraction
    * Creation Date: 3/18/18
    * Purpose: implement required function
    * Arguments:
    *      subject
    * Other Requirements:
    *      none
    * Return Value: none
    */
     @Override
     public void onListFragmentInteraction(Subject subject) {

     }
}
