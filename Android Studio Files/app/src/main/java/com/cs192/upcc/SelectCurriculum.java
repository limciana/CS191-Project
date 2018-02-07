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
 * Rayven Ely Cruz      1/30/18  Set up the buttons for the activity.
 * Rayven Ely Cruz      1/31/18  Fixed scrollable view and added splash screen.
 * Rayven Ely Cruz      2/02/18  Integrated Curriculum and Subject classes as well as methods for passing it to the next activity.
 */

/*
 * File Creation Date: 1/27/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectCurriculum extends AppCompatActivity implements View.OnClickListener {
     LinearLayout parent; //The layout specified in the corresponding xml file for this activity.
     ArrayList<String> curriculumNames; //List of the names of the curriculum loaded.
     DatabaseHelper UPCCdb; //The database cariable used for loading the curriculum in the db file
     FloatingActionButton fabNext; //The button for switching and passing to the next activity
     Curriculum selectedCurriculum; //The curriculum that is selected. Used for passing to the next activity.
     /*
     * Name: onCreate
     * Creation Date: 1/30/18
     * Purpose: Renders the layout and the database on the main activity
     * Arguments:
     *      savedInstanceState - Bundle, for passing data between Android activities
     * Other Requirements:
     *      UPCCdb - DatabaseHelper, calls the constructor method of DatabaseHelper to create the database
     *
     * Return Value: void
     */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          /* Switch from splash screen to activity screen layout */
          setTheme(R.style.AppTheme);

          /* Start activity screen and determine the layout*/
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_select_curriculum);
          parent = (LinearLayout) findViewById(R.id.ll_parentLayout);
          setTitle("Select Curriculum");

          /* Initialize db and list of curriculum */
          UPCCdb = new DatabaseHelper(this);
          curriculumNames = new ArrayList<String>();
          UPCCdb.createDB();

          /* Setup Floating action button */
          setUpFAB();

          /* Count the number of loaded curriculum */
          Cursor res = UPCCdb.getCurriculum();
          if (res.getCount() == 0) {
               Toast.makeText(getApplicationContext(), "ERROR: NOTHING TO SHOW", Toast.LENGTH_SHORT).show();
          }

          /* Add the names of the curriculum loaded from db to the list */
          while (res.moveToNext()) {
               curriculumNames.add(res.getString(UPCC.SUBJECT_CURRICULUM));
          }


          /* Add r_row entry for every curriculum in the list */
          for (int i = 0; i < curriculumNames.size(); i++) {
               /* Specified row for each curriculum, each row has a textview and checkbox */
               RelativeLayout r_row = new RelativeLayout(this);
               CheckBox cb;
               TextView tv;

               /* Setup the contents of the row */
               tv = createTextView(curriculumNames.get(i));
               cb = createCheckBox(i + 1);

               /* Give unique id for the row */
               r_row.setId((i + 1) + curriculumNames.size());

               /* Set alignment of row's components */
               alignRelative(tv, RelativeLayout.ALIGN_PARENT_LEFT);
               alignRelative(cb, RelativeLayout.ALIGN_PARENT_RIGHT);

               /* Add the components to the row */
               r_row.addView(tv);
               r_row.addView(cb);

               /* Add margins and effect when the row is clicked*/
               r_row.setPadding(20, 20, 20, 20);
               r_row.setBackgroundResource(setClickEffect().resourceId);

               /* Add the row to the parent layout */
               parent.addView(r_row);

               /* Add a divider to the parent layout */
               parent.addView(createDivider());

               /* Set the onclick event to the row */
               r_row.setOnClickListener(this);
          }

          /* Setup the first curriculum to be the default on start of the activity */
          CheckBox init = findViewById(1);
          init.toggle();
     }

     /*
     * Name: onClick
     * Creation Date: 1/30/18
     * Purpose: Sets the events when certain components(views) are clicked
     * Arguments:
     *      view - the specific component in which the onclick event was triggered
     * Other Requirements:
     *      selectedCurriculum - the Curriculum selected by the user
     * Return Value: void
     */
     @Override
     public void onClick(View view) {
          // Toast.makeText(getApplicationContext(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();

          /* Event when the floating action button is clicked
             It loads the selectedCurriculum and then passes it to the next activity
           */
          if (view.getId() == R.id.next_button) {

               /* Search for the checked curriculum in the listed curriculum */
               CheckBox cbTemp;
               int selectedId = 0;
               for (int i = 1; i <= curriculumNames.size(); i++) {
                    cbTemp = (CheckBox) findViewById(i);
                    if (cbTemp.isChecked()) {
                         selectedId = i;
                    }
               }

               /* Creates the selected curriculum */
               selectedCurriculum = new Curriculum(curriculumNames.get(selectedId - 1));

               /* Count the number of subjects in the curriculum selected */
               Cursor res = UPCCdb.getSubjects(curriculumNames.get(selectedId - 1));
               if (res.getCount() == 0) {
                    Toast.makeText(getApplicationContext(), "Warning: No Subjects", Toast.LENGTH_SHORT).show();
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
                    selectedCurriculum.addSubject(tempSubject);
               }

               /* Setup for the next activity */
               Intent intent = new Intent(getBaseContext(), InputSubjects.class);
               intent.putExtra("curriculum", selectedCurriculum);
               startActivity(intent);

          /* Event when a row is clicked */
          } else {
               int id = view.getId() - curriculumNames.size();
               CheckBox checkBox = (CheckBox) findViewById(id);
               CheckBox cbTemp;

               /* To make sure that only one checkbox is checked at any moment */
               for (int i = 1; i <= curriculumNames.size(); i++) {
                    cbTemp = (CheckBox) findViewById(i);
                    cbTemp.setChecked(false);
               }
               checkBox.toggle();
          }
     }

     /*
     * Name: stringToBoolean
     * Creation Date: 2/02/18
     * Purpose: Handles the conversion of string loaded from the database
     * Arguments:
     *      aString - the string to be converted
     * Other Requirements:
     *      nonde
     * Return Value: boolean - the result of the conversion
     */
     private boolean stringToBoolean(String aString) {
          /* Handle null strings */
          if (aString != null) {
               if (aString.equals("true")) {
                    return true;
               } else {
                    return false;
               }
          } else {
               return false;
          }
     }
     /*
     * Name: createTextView
     * Creation Date: 1/30/18
     * Purpose: setups the margins and name of a TextView
     * Arguments:
     *      aTextName - the name to be put on the TextView
     * Other Requirements:
     *      none
     * Return Value: TextView - the resulting aTextView
     */
     private TextView createTextView(String aTextName) {
          TextView aTextView = new TextView(this);
          aTextView.setPadding(10, 10, 10, 10);
          aTextView.setText(aTextName);
          return aTextView;
     }

     /*
     * Name: createCheckBox
     * Creation Date: 1/30/18
     * Purpose: setups the margins and name of a checkbox
     * Arguments:
     *      anID - the unique ID to be put on the checkbox
     * Other Requirements:
     *      none
     * Return Value: CheckBox - the resulting aCheckBox
     */
     private CheckBox createCheckBox(int anID) {
          CheckBox aCheckBox = new CheckBox(this);
          aCheckBox.setId(anID);
          aCheckBox.setTag(anID);
          aCheckBox.setClickable(false);
          return aCheckBox;
     }
     /*
     * Name: createDivider
     * Creation Date: 1/30/18
     * Purpose: creates a divider
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: View - the divider v
     *
     * vipul mittal. https://stackoverflow.com/questions/21098618/how-to-make-horizontal-line-in-android-programmatically. Last Accessed: 1/28/18
     */
     private View createDivider() {
          View v = new View(this);
          v.setLayoutParams(new LinearLayout.LayoutParams(
                  (LinearLayout.LayoutParams.MATCH_PARENT),
                  1
          ));
          v.setBackgroundColor(Color.parseColor("#B3B3B3"));
          v.getBackground().setAlpha(100);
          return v;
     }
     /*
    * Name: setClickedEffect
    * Creation Date: 1/30/18
    * Purpose: sets the visual response when a View is clicked
    * Arguments:
    *      none
    * Other Requirements:
    *      none
    * Return Value: TypedValue - the resulting effect object
    * Wooseng Kim. https://stackoverflow.com/questions/8732662/how-to-set-ripple-effect-on-a-linearlayout-programmatically. Last Accessed 1/28/18
    */
     private TypedValue setClickEffect() {
          TypedValue outValue = new TypedValue();
          getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
          return outValue;
     }


     /*
    * Name: printBuffer
    * Creation Date: 2/02/18
    * Purpose: for testing and debugging, displays a string to the screen
    * Arguments:
    *      buffer - the value to be displayed
    * Other Requirements:
    *      none
    * Return Value: void
    */
     public void printBuffer(StringBuffer buffer) {

          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setCancelable(true);
          builder.setTitle("Subject");
          builder.setMessage(buffer);
          builder.show();

     }

     /*
    * Name: alignRelative
    * Creation Date: 1/30/18
    * Purpose: setups the positioning in a relative layout
    * Arguments:
    *      aView - the component to be changed
    *      anAlignment - the corresponind value specified by the layout
    * Other Requirements:
    *      none
    * Return Value: TextView - void
    */
     public void alignRelative(View aView, int anAlignment){
          RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
          lp.addRule(anAlignment);
          aView.setLayoutParams(lp);
     }

     /*
    * Name: setUpFAB
    * Creation Date: 2/02/18
    * Purpose: setups the floating action button and its events
    * Arguments:
    *      none
    * Other Requirements:
    *      fabNext - the floating action button as specified in the layout of the activity
    * Return Value: void
    *
    * hcmonte. https://stackoverflow.com/questions/34560770/hide-fab-in-nestedscrollview-when-scrolling/35427564. Last Accessed: 2/02/18
    */
     public void setUpFAB(){
          fabNext = findViewById(R.id.next_button);
          fabNext.setOnClickListener(this);

          /* Hide or show FAB depending on user's scroll */
          NestedScrollView nsv = (NestedScrollView) findViewById(R.id.sView);
          nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
               @Override
               public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                         fabNext.hide();
                    } else {
                         fabNext.show();
                    }
               }
          });

     }
}
