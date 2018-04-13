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
 * Rayven Ely Cruz      2/18/18  Created the fragment.
 * Rayven Ely Cruz      2/19/18  Fixed Errors
 * Rayven Ely Cruz      2/22/18  Created interface
 * Ciana Lim            4/7/18   Added warning for switching from one curriculum to the other
 */

/*
 * File Creation Date: 2/18/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */
package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class SelectCurriculumFragment extends Fragment {

     LinearLayout parent; //The layout specified in the corresponding xml file for this activity.
     ArrayList<String> curriculumNames; //List of the names of the curriculum loaded.
     DatabaseHelper UPCCdb; //The database cariable used for loading the curriculum in the db file
     FloatingActionButton fabNext; //The button for switching and passing to the next activity
     Curriculum selectedCurriculum; //The curriculum that is selected. Used for passing to the next activity.
     View v; // To get the view
     OnDataPass dataPasser; //To send message to the activity
     AlertDialog.Builder builder; // the builder for the warning dialog

     public SelectCurriculumFragment() {
          // Required empty public constructor
     }

     /*
     * Name: onDataPass
     * Creation Date: 2/22/18
     * Purpose: interface to pass data to activity
     * Arguments:
     *      nonde
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public interface OnDataPass {
          public void onCurriculumPass(Curriculum data, boolean pass, boolean first);

          public void onTitlePass(String data);
     }

     /*
     * Name: passCurriculum
     * Creation Date: 2/22/18
     * Purpose: passes the Curriculum
     * Arguments:
     *      data - the curriculum
     *      pass - if it should go straight to the next fragment
     *      first - if it is the first time the user will select a curriculum or not
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passCurriculum(Curriculum data, boolean pass, boolean first) {
          dataPasser.onCurriculumPass(data, pass, first);
     }

     /*
     * Name: passTitle
     * Creation Date: 2/22/18
     * Purpose: Handles the strings being passed through the interfaces
     * Arguments:
     *      data - the title string
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passTitle(String data) {

          dataPasser.onTitlePass(data);
     }

     /*
     * Name: onAttach
     * Creation Date: 2/18/18
     * Purpose: implementation from fragment
     * Arguments:
     *      context
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onAttach(Context context) {
          super.onAttach(context);
          dataPasser = (OnDataPass) context;
     }

     /*
     * Name: onDetach
     * Creation Date: 2/22/18
     * Purpose: handles the sending of message when leaving the fragment
     * Arguments:
     *      none
     * Other Requirements:
     *      none
     * Return Value: void
     */
     @Override
     public void onDetach() {
          super.onDetach();
     }

     /*
     * Name: onCreateView
     * Creation Date: 2/18/18
     * Purpose: Setups the fragment
     * Arguments:
     *      inflate
     *      container
     *      savedInstanceState
     * Other Requirements:
     *      none
     * Return Value: view
     */
     @Override
     public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
          v = inflater.inflate(R.layout.fragment_select_curriculum, container, false);
          passTitle("Select Curriculum");
          // Inflate the layout for this fragment
          parent = (LinearLayout) v.findViewById(R.id.f_ll_parentLayout);

          /* Initialize db and list of curriculum */
          UPCCdb = new DatabaseHelper(getActivity());
          curriculumNames = new ArrayList<String>();
          UPCCdb.createDB();

          /* Setup Floating action button */
          setUpFAB();

          /* Count the number of loaded curriculum */
          Cursor res = UPCCdb.getCurriculum();

          /* Add the names of the curriculum loaded from db to the list */
          while (res.moveToNext()) {
               curriculumNames.add(res.getString(UPCC.SUBJECT_CURRICULUM));
          }


          /* Add r_row entry for every curriculum in the list */
          for (int i = 0; i < curriculumNames.size(); i++) {
               /* Specified row for each curriculum, each row has a textview and checkbox */
               RelativeLayout r_row = new RelativeLayout(v.getContext());
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
               int paddingDp = convertDpToPx(10);
               r_row.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
               r_row.setBackgroundResource(setClickEffect().resourceId);

               /* Add the row to the parent layout */
               parent.addView(r_row);

               /* Add a divider to the parent layout */
               parent.addView(createDivider());

               /* Set the onclick event to the row */
               r_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         int id = view.getId() - curriculumNames.size();
                         CheckBox checkBox = (CheckBox) v.findViewById(id);
                         CheckBox cbTemp;

               /* To make sure that only one checkbox is checked at any moment */
                         for (int i = 1; i <= curriculumNames.size(); i++) {
                              cbTemp = (CheckBox) v.findViewById(i);
                              cbTemp.setChecked(false);
                         }
                         checkBox.toggle();

                    }
               });
          }

          /* Setup the first curriculum to be the default on start of the activity */
          CheckBox init = v.findViewById(1);
          init.toggle();
          return v;
     }
     /*
     * Name: stringToBoolean
     * Creation Date: 1/30/18
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
          TextView aTextView = new TextView(getActivity());
          int paddingDp = convertDpToPx(10);
          aTextView.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);
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
          CheckBox aCheckBox = new CheckBox(getActivity());
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
          View v = new View(getActivity());
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
          getActivity().getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
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

          AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
     public void alignRelative(View aView, int anAlignment) {
          RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
          lp.addRule(anAlignment);
          aView.setLayoutParams(lp);
     }


     /*
    * Name: convertDptoPx
    * Creation Date: 2/02/18
    * Purpose: converts Dp to Px
    * Arguments:
    *      none
    * Other Requirements:
    *     dp
    * Return Value: int px
    *
    * Vicky Chijwani. https://stackoverflow.com/questions/8295986/how-to-calculate-dp-from-pixels-in-android-programmatically. Last Accessed: 2/07/18
    */
     public int convertDpToPx(int dp) {
          return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
     }

     /*
     * Name: setUpFAB
     * Creation Date: 2/02/18
     * Purpose: setups the floating action button and its events
     * Arguments:
     *      none
     * Other Requirements:
     *      fabNext - the floating action button as specified in the layout of the activity
     *      builder - the warning for the alert dialog
     * Return Value: void
     *
     * hcmonte. https://stackoverflow.com/questions/34560770/hide-fab-in-nestedscrollview-when-scrolling/35427564. Last Accessed: 2/02/18
     */
     public void setUpFAB() {
          builder = new AlertDialog.Builder(v.getContext());
          fabNext = (FloatingActionButton) v.findViewById(R.id.f_next_button);
          fabNext.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    /* check if it is the first time the user will be selecting a curriculum */
                    boolean first = ((MainDrawer) getActivity()).getFirstTime();

                    /* if it is the first time, no need to show warning */
                    if(first == true){
                         getCurriculumFromList(true);
                    }
                    else{
                         CheckBox cbTemp;

                         int selectedId = 0;
                         for (int i = 1; i <= curriculumNames.size(); i++) {
                              cbTemp = (CheckBox) v.findViewById(i);
                              if (cbTemp.isChecked()) {
                                   selectedId = i;
                              }
                         }

                         /* get the selected curriculum's name */
                         String curriculumName = curriculumNames.get(selectedId - 1);
                         Cursor res = UPCCdb.getStudentData();

                         /* check if the selected curriculum's name is actually the user's current curriculum */
                         if(res.moveToFirst()){
                              /* if the selected/current curriculum is equal to the curriculum previously being used by the student */
                              if(res.getString(0).equals(curriculumName)){
                                   /* just create the curriculum as is */
                                   getCurriculumFromList(true);
                              }
                              else{
                                   /* if it is a new curriculum, show a warning */
                                   builder.setMessage("You are selecting a new curriculum. You will be given a new curriculum, and your old curriculum will be reset. Continue?").setTitle("Warning");
                                   builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        /* if the user really wants to switch curriculums, create the new curriculum */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                             getCurriculumFromList(true);
                                        }
                                   });
                                   builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        /* if the user changes his/her mind, ignore the click */
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                   });
                                   AlertDialog dialog = builder.create();
                                   dialog.show();
                              }
                         }
                         else{
                              getCurriculumFromList(true);
                         }
                    }
               }
          });

          /* Hide or show FAB depending on user's scroll */
          NestedScrollView nsv = (NestedScrollView) v.findViewById(R.id.f_sView);
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

     /*
     * Name: getCurriculumFromList
     * Creation Date: 2/22/18
     * Purpose: gets curriculum to the list
     * Arguments:
     *      pass - if the curriculum should be passed to InputSubjectsFragment
     * Other Requirements:
     *      selectCurriculum
     * Return Value: void
     *
     */
     public void getCurriculumFromList(boolean pass) {
          /* Search for the checked curriculum in the listed curriculum */

          CheckBox cbTemp;

          int selectedId = 0;
          for (int i = 1; i <= curriculumNames.size(); i++) {
               cbTemp = (CheckBox) v.findViewById(i);
               if (cbTemp.isChecked()) {
                    selectedId = i;
               }
          }

          /* Creates the selected curriculum */
          selectedCurriculum = new Curriculum(curriculumNames.get(selectedId - 1));

          /* Count the number of subjects in the curriculum selected */
          Cursor res = UPCCdb.getSubjects(curriculumNames.get(selectedId - 1));
          if (res.getCount() == 0) {
               Toast.makeText(v.getContext(), "Warning: No Subjects", Toast.LENGTH_SHORT).show();
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
          passCurriculum(selectedCurriculum, pass, false);

     }
}
