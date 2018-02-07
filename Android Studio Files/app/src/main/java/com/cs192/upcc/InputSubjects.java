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
 */

/*
 * File Creation Date: 2/4/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.AlertDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InputSubjects extends AppCompatActivity {
     Curriculum curriculum; //The curriculum that was passed from the previous screen.
     CheckBox checkbox; //The variable that will be used to create a new CheckBox programmatically.
     LinearLayout layout; //The parent layout of this module's screen.
     TextView text; //The variable that will be used to create a new TextView programmatically.

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
          setTheme(R.style.AppTheme);
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_input_subjects);
          curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");
          layout = findViewById(R.id.layout);
          for(int i = 0; i < curriculum.getSubjects().size(); i++) {
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

               r_row.setPadding(20, 20, 100, 20);

               r_row.setBackgroundResource(setClickEffect().resourceId);

               layout.addView(r_row);

               layout.addView(createDivider());
          }
          CheckBox init = findViewById(1);
          init.toggle();
     }

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

     private View createDivider(){
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

     private TypedValue setClickEffect(){
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

     private TextView createTextView(String aTextName){
          TextView aTextView = new TextView(this);
          aTextView.setPadding(10, 10, 10, 10);
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

     private CheckBox createCheckBox(int anID){
          CheckBox aCheckBox = new CheckBox(this);
          aCheckBox.setId(anID);
          aCheckBox.setTag(anID);
          aCheckBox.setClickable(true);
          return aCheckBox;
     }


    /*public void onClick(View view) {
        // Toast.makeText(getApplicationContext(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
        String ind = String.valueOf(view.getId());
        int id = view.getId() - curriculum.subjects.size();
        int indx = Integer.parseInt(ind);
        String temp = String.valueOf(id);
        CheckBox checkBox = (CheckBox) findViewById(id);
        CheckBox cbTemp;
        for (int i = 1; i <= curriculum.subjects.size(); i++) {
            cbTemp = (CheckBox) findViewById(i);
            cbTemp.setChecked(false);
        }
        checkBox.toggle();
    }

    public void printBuffer(StringBuffer buffer){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Subject");
        builder.setMessage(buffer);
        builder.show();

    }*/
}
