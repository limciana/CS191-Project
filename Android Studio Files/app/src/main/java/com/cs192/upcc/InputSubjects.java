/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by James Gabriel Abaja.
 */

/* Code History
 * Programmer           Date     Description
 * James Gabriel Abaja  2/4/18   Set up the back end for the Input Subjects screen.
 * James Gabriel Abaja  2/7/18   Completed the file with appropriate comments.
 * Rayven Ely Cruz      2/07/18  Fixed padding for phones with different dpi
 * Ciana Lim            2/14/18  Added insertion of passed subjects and deletion of wrongly marked subjects to the student's database for non-volatility.
 */

/*
 * File Creation Date: 2/4/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class InputSubjects extends AppCompatActivity{
     Curriculum curriculum; //The curriculum that was passed from the previous screen.
     CheckBox checkbox; //The variable that will be used to create a new CheckBox programmatically.
     LinearLayout layout; //The parent layout of this module's screen.
     TextView text; //The variable that will be used to create a new TextView programmatically.
     boolean isInserted; //The variable that checks if the data was inserted or not.
     DatabaseHelper UPCCdb; //The database variable used for loading the curriculum in the db file
     String subject_name; //The name of the subject that was clicked
     String curriculum_name; //The curriculum name of the student
     Cursor res; // the resulting rows selected from the query found in DatabaseHelper.java
     AlertDialog.Builder builder; // instance to be used for the dialog
     StringBuffer buffer; // buffer string to show the data stored in the database
     int isDeleted; // the number of rows that were deleted from the student_table

     /*
      * Name: onCreate
      * Creation Date: 2/4/18
      * Purpose: Renders the layout and the database on the main activity
      * Arguments:
      *      savedInstanceState - Bundle, for passing data between Android activities
      * Other Requirements:
      *      curriculum - class containing the data from the db class.
      *
      * Return Value: void
      */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          UPCCdb = new DatabaseHelper(this);
          UPCCdb.createDB();
          setTheme(R.style.AppTheme);
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_input_subjects);
          curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");
          layout = findViewById(R.id.layout);
          for (int i = 0; i < curriculum.getSubjects().size(); i++) {
               RelativeLayout r_row = new RelativeLayout(this);

               text = createTextView(curriculum.getSubjects().get(i).getSubjectName());
               checkbox = createCheckBox(i + 1);

               r_row.setId((i + 1) + curriculum.getSubjects().size());

               RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
               lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
               text.setLayoutParams(lp);
               r_row.addView(text);

               RelativeLayout.LayoutParams lp_1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
               lp_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
               checkbox.setLayoutParams(lp_1);
               r_row.addView(checkbox);

               int paddingDp = convertDpToPx(10);
               r_row.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

               r_row.setBackgroundResource(setClickEffect().resourceId);

               layout.addView(r_row);

               layout.addView(createDivider());

               r_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         int id = view.getId() - curriculum.getSubjects().size();
                         CheckBox checkBox = (CheckBox) findViewById(id);
                         checkBox.toggle();
                         curriculum_name = curriculum.getName();
                         subject_name = curriculum.getSubjects().get(id-1).getSubjectName();
                         res = UPCCdb.searchStudentData(curriculum_name, subject_name);
                         if(res.getCount() == 0){
                              isInserted = UPCCdb.insertData(curriculum_name, subject_name);
                              if(isInserted == true) {
                                   res = UPCCdb.getStudentData();
                                   if (res.getCount() == 0) {
                                        showMessage("Error", "Nothing found");
                                        return;
                                   }
                                   buffer = new StringBuffer();
                                   while (res.moveToNext()) {
                                        buffer.append("Curriculum: " + res.getString(0) + "\n");
                                        buffer.append("Subject name: " + res.getString(1) + "\n\n");
                                   }

                                   // show all data
                                   showMessage("Data", buffer.toString());
                              }
                              else{
                                   Toast.makeText(InputSubjects.this, "Data not inserted", Toast.LENGTH_LONG).show();
                              }
                         }
                         else{
                              isDeleted = UPCCdb.deleteData(curriculum_name, subject_name);
                              res = UPCCdb.getStudentData();
                              if (res.getCount() == 0) {
                                   showMessage("Error", "Nothing found");
                                   return;
                              }
                              buffer = new StringBuffer();
                              while (res.moveToNext()) {
                                   buffer.append("Curriculum: " + res.getString(0) + "\n");
                                   buffer.append("Subject name: " + res.getString(1) + "\n\n");
                              }

                              // show all data
                              showMessage("Data", buffer.toString());
                         }

                    }
               });
          }
          /* CheckBox init = findViewById(1);
          init.toggle(); */
     }

     /* @Override
     public void onClick(View view){
          Log.d("Testing", "Was clicked!");
          curriculum_name = curriculum.getName();
          subject_name = curriculum.getSubjects().get(view.getId()-1).getSubjectName();
          isInserted = UPCCdb.insertData(curriculum_name, subject_name);
          if(isInserted == true){
               res = UPCCdb.getStudentData();
               if(res.getCount() == 0){
                    if(res.getCount() == 0){
                         showMessage("Error", "Nothing found");
                         return;
                    }
                    buffer = new StringBuffer();
                    while(res.moveToNext()){
                         buffer.append("Curriculum: " + res.getString(0)+"\n");
                         buffer.append("Subject name: " + res.getString(1)+"\n\n");
                    }

                    // show all data
                    showMessage("Data", buffer.toString());
               }
          }
     }*/
    /*
     * Name: createDivider
     * Creation Date: 2/4/18
     * Purpose: Creates a divider for the layout
     * Arguments:
     *      None
     * Other Requirements:
     *      View - connects to a layout that will be modified
     *
     * Return Value: modified UI components
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
     * Name: setClickEffect
     * Creation Date: 2/4/18
     * Purpose: Sets the view of the layout when it is clicked.
     * Arguments:
     *      None
     * Other Requirements:
     *      None
     *
     * Return Value: modified UI components
     */

     private TypedValue setClickEffect() {
          TypedValue outValue = new TypedValue();
          getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
          return outValue;
     }

    /*
     * Name: createTextView
     * Creation Date: 2/4/18
     * Purpose: Dynamically creates a new textview.
     * Arguments:
     *      aTextName - name of the subject that will be set to the textview UI.
     * Other Requirements:
     *      None
     *
     * Return Value: newly created TextView with the subject name.
     */

     private TextView createTextView(String aTextName) {
          TextView aTextView = new TextView(this);
          int paddingDp = convertDpToPx(10);
          aTextView.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
          aTextView.setText(aTextName);
          return aTextView;
     }

    /*
     * Name: createCheckBox
     * Creation Date: 2/4/18
     * Purpose: Dynamically creates a new checkbox.
     * Arguments:
     *      anID - id that will be set to the unique checkbox
     * Other Requirements:
     *      None
     *
     * Return Value: initialized checkbox with a unique id
     */

     private CheckBox createCheckBox(int anID) {
          CheckBox aCheckBox = new CheckBox(this);
          aCheckBox.setId(anID);
          aCheckBox.setTag(anID);
          aCheckBox.setClickable(false);
          return aCheckBox;
     }


     /*
    * Name: convertDpToPx
    * Creation Date: 2/07/18
    * Purpose: converts Dp to Px values
    * Arguments:
    *      dp - value in dp
    * Other Requirements:
    *      none
    * Return Value: int
    *
    * Vicky Chijwani. https://stackoverflow.com/questions/8295986/how-to-calculate-dp-from-pixels-in-android-programmatically. Last Accessed: 2/07/18
    */
     public int convertDpToPx(int dp) {
          return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
     }

     /*
      * Name: showMessage
      * Creation Date: 2/14/18
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

}
