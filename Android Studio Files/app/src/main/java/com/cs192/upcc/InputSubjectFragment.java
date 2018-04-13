package com.cs192.upcc;

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
 * Rayven Ely Cruz      2/19/18  Created the fragment.
 * James Gabriel Abaja  2/21/18  Added logic of curriculum without JS and SS
 * Rayven Ely Cruz      2/22/18  Modified structure
 * James Gabriel Abaja  2/23/18  Added logic of curriculum with JS and SS
 * Ciana Lim            3/6/18   Included the non-volatility aspect of the app
 * Ciana Lim            3/7/18   Added methods and logic to include the calculation of coreqs
 * Rayven Ely Cruz      3/8/18   Added methods for junior/senior standing based on recommended subjects
 * Ciana Lim            3/9/18   Remove coreqs restriction
 * Rayven Ely Cruz      3/23/18  Added methods for passing visible subjects
 * Ciana Lim            4/7/18   Added the warning function, and relocated the logic of updating the input subject screen to another function
 * Rayven Ely Cruz      4/11/18  Updated FAB
 * Rayven Ely Cruz      4/13/18  Updated FAB
 * Ciana Lim            4/13/18  Added drawer information
 */

/*
 * File Creation Date: 2/19/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.TooltipCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.github.clans.fab.FloatingActionMenu;


import java.util.ArrayList;


public class InputSubjectFragment extends Fragment {
     Curriculum curriculum; //The curriculum that was passed from the previous screen.
     LinearLayout layout; //The parent layout of this module's screen.
     View v; // The general view of the fragment
     OnDataPass dataPasser; //Data being passed to activity
     TextView text; //The variable that will be used to create a new TextView programmatically
     DatabaseHelper UPCCdb; //The database variable used for loading the curriculum in the db file
     AlertDialog.Builder builder; // instance to be used for the dialog
     int units_taken = 0; // total number of units taken by the student
     ArrayList<Subject> resultArray; // the subjects that can be taken
     FloatingActionMenu fab;
     int toRemove; // signifies the state of the user's click
     Subject subject; // the subject that was clicked
     CheckBox checkBox; // the checkbox that was clicked

     Student student; // the student object
     public InputSubjectFragment() {
          // Required empty public constructor
     }
     /*
     * Name: onAttach
     * Creation Date: 2/19/18
     * Purpose: setups the fragment
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
     * Name: passData
     * Creation Date: 2/22/18
     * Purpose: passes string
     * Arguments:
     *      data - string to be passed
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passData(String data) {
          dataPasser.onDataPass(data);
     }
     /*
     * Name: passTitle
     * Creation Date: 2/22/18
     * Purpose: passing the title
     * Arguments:
     *      data - title
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passTitle(String data) {
          dataPasser.onTitlePass(data);
     }
     /*
     * Name: passStanding
     * Creation Date: 3/23/18
     * Purpose: pass standing
     * Arguments:
     *      data - standing
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passStanding(String data) {
          dataPasser.onStandingPass(data);
     }
     /*
     * Name: passSubjects
     * Creation Date: 3/23/18
     * Purpose: pass subjects
     * Arguments:
     *      data - subjects
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passSubjects(ArrayList<Subject> data) {
          dataPasser.onSubjectsPass(data);
     }
     /*
     * Name: passUnits
     * Creation Date: 3/23/18
     * Purpose: pass units
     * Arguments:
     *      data - units
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void passUnits(int data) {
          dataPasser.onUnitsPass(data);
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
          public void onDataPass(String data);
          public void onStandingPass(String data);
          public void onUnitsPass(int data);
          public void onTitlePass(String data);
          public void onSubjectsPass(ArrayList<Subject> data);
     }
     /*
     * Name: onCreateView
     * Creation Date: 2/19/18
     * Purpose: setups the fragment
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

          /* Pass title to the activity, receive curriculum from the activity */
          passTitle("Mark Subjects");
          curriculum = ((MainDrawer) getActivity()).getCurriculum();
          //Log.d("curriculum", curriculum.getName());
          // Inflate the layout for this fragment
          UPCCdb = new DatabaseHelper(getActivity());
          UPCCdb.createDB();

          resultArray = new ArrayList<Subject>();

          /* List the subjects of the curriculum */
          if (curriculum != null) {

               /* create the student object */
               student = new Student(UPCCdb, curriculum);


               v = inflater.inflate(R.layout.fragment_input_subject, container, false);

               layout = v.findViewById(R.id.f_layout);
               Toast.makeText(v.getContext(), curriculum.getName(), Toast.LENGTH_LONG).show();
               builder = new AlertDialog.Builder(v.getContext());

               for (int i = 0; i < curriculum.getSubjects().size(); i++) {

                    Log.d("curriculum", curriculum.getSubjects().get(i).getSubjectName());
                    final RelativeLayout r_row = new RelativeLayout(v.getContext());
                    r_row.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                    CheckBox cb;
                    TextView tv;
                    TextView desc;
                    TextView units;
               /* Setup the contents of the row */
                    tv = createTextView(curriculum.getSubjects().get(i).getSubjectName());
                    units = createTextView(curriculum.getSubjects().get(i).getUnits() + "u");
                    tv.setId(curriculum.getSubjects().size() * 2 + (i + 1));
                    desc = createTextView(curriculum.getSubjects().get(i).getSubjectDesc());

                    desc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                    units.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    cb = createCheckBox(i + 1);

               /* To change, depreciated */
                    tv.setTextColor(ContextCompat.getColor(v.getContext(), R.color.primaryText));
                    desc.setTextColor(ContextCompat.getColor(v.getContext(), R.color.secondaryText));

               /* Set alignment of row's components */
                    alignRelative(tv, RelativeLayout.ALIGN_PARENT_LEFT);
                    alignRelative(desc, RelativeLayout.BELOW, tv.getId());
                    alignRelative(cb, RelativeLayout.ALIGN_PARENT_RIGHT);
                    alignRelative(units, RelativeLayout.RIGHT_OF, tv.getId());

               /* Add the components to the row */
                    r_row.addView(tv);
                    r_row.addView(desc);
                    r_row.addView(cb);
                    r_row.addView(units);

                    r_row.setId((i + 1) + curriculum.getSubjects().size());

                    int paddingDp = convertDpToPx(10);
                    r_row.setPadding(paddingDp, paddingDp, paddingDp, paddingDp);

                    r_row.setBackgroundResource(setClickEffect().resourceId);

                    layout.addView(r_row);

                    layout.addView(createDivider());

                    /* each row is set to gone */
                    r_row.setVisibility(View.GONE);

                    /* checks if the subject was marked previously */
                    boolean set_mark = student.mark_subject(curriculum.getSubjects().get(i).getSubjectName());
                    Log.d("curriculum", String.valueOf(set_mark));
                    CheckBox init = v.findViewById(i+1);
                    if(set_mark){
                         init.toggle();
                    }

                    /* the onClick method. Checks if a subject is to be shown or not. Handles the click of the user */
                    r_row.setOnClickListener(new View.OnClickListener() {
                         /*
                         * Name: onClick
                         * Creation Date: 2/23/18
                         * Purpose: function that is executed when a subject row is clicked.
                         * Arguments:
                         *      View
                         * Other Requirements:
                         *      none
                         * Return Value: void
                         */
                         @Override
                         public void onClick(View view) {
                              int id = view.getId() - curriculum.getSubjects().size();

                              checkBox = v.findViewById(id);
                              //CheckBox cbTemp;

                              /* manage the student table
                              * get the subject name based on the clicked instance
                              */

                              /* checks the clicked subject, if it will be inserted or not, and if some other subjects should be removed from subjects taken */
                              subject = curriculum.getSubjects().get(id-1);

                              toRemove = student.toggle_subject(subject, 0);
                              Log.d("selection", String.valueOf(toRemove));
                              if(toRemove == 2){
                                  /* if it was detected that the user is trying to unmark a subject, show warning */
                                  Log.d("unmark", String.valueOf(toRemove));
                                  builder.setMessage("Unmarking "+subject.getSubjectName()+" may unmark other subjects as well.\nAre you sure you want to unmark?").setTitle("Warning");
                                  builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                            /* if the user confirms to remove the subject, remove the subject from the database, toggle the checkbox, and update the screen */
                                            toRemove = student.toggle_subject(subject, 3);
                                            checkBox.toggle();
                                            updateScreen(student, curriculum);
                                            updateFabStanding(student.getStanding());
                                       }
                                  });
                                  builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                       @Override
                                       public void onClick(DialogInterface dialog, int which) {
                                            /* if the user cancels, leave the screen as is */
                                       }
                                  });
                                  AlertDialog dialog = builder.create();
                                  dialog.show();
                              }
                              else{
                                   /* for marking subjects */
                                   checkBox.toggle();
                                   updateScreen(student, curriculum);
                                   updateFabStanding(student.getStanding());
                              }
                              passUnits(student.getTotalUnits());
                             String name = curriculum.getName();
                             if(student.getStanding() == UPCC.STUDENT_FRESHMAN){
                                 passStanding(name+"\nFreshman Standing");
                             }
                             else if(student.getStanding() == UPCC.STUDENT_SOPHOMORE){
                                 passStanding(name+"\nSophomore Standing");
                             }
                             else if(student.getStanding() == UPCC.STUDENT_JUNIOR){
                                 passStanding(name+"\nJunior Standing");
                             }
                             else if(student.getStanding() == UPCC.STUDENT_SENIOR){
                                 passStanding(name+"\nSenior Standing");
                             }

                         }

                    });
                     /* Display details on long press */
                    r_row.setOnLongClickListener(new View.OnLongClickListener() {
                         @Override
                         public boolean onLongClick(View view) {
                         /* Vibration feedback */
                              view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                         /* Print subject descriptions */
                              int id = view.getId() - curriculum.getSubjects().size() - 1;
                              ArrayList<String> buffer = curriculum.getSubjects().get(id).getSubjectPrintArrayList();
                              printBuffer(curriculum.getSubjects().get(id).getSubjectName(), buffer);

                              return false;
                         }
                    });


               }

               updateScreen(student, curriculum);
          }
          /*passUnits(student.getTotalUnits());
          String name = curriculum.getName();
          if(student.getStanding() == UPCC.STUDENT_FRESHMAN){
               passStanding(name+"\nFreshman Standing");
          }
          else if(student.getStanding() == UPCC.STUDENT_SOPHOMORE){
               passStanding(name+"\nSophomore Standing");
          }
          else if(student.getStanding() == UPCC.STUDENT_JUNIOR){
               passStanding(name+"\nJunior Standing");
          }
          else if(student.getStanding() == UPCC.STUDENT_SENIOR){
               passStanding(name+"\nSenior Standing");
          }*/
          setUpFAB();
          return v;
     }

    /*
     * Name: updateScreen
     * Creation Date: 4/7/18
     * Purpose: updates the input subjects screen
     * Arguments:
     *      student - the student instance (has passed subjects, etc.)
     *      curriculum - the curriculum being used by the student
     * Other Requirements:
     *      none
     * Return Value: void
     */
     public void updateScreen(Student student, Curriculum curriculum){
          units_taken = student.getTotalUnits();

          /* iterate over all the subjects in the curriculum */
          for(int i = 0; i < curriculum.getSubjects().size(); i++) {
               TextView tv_s = v.findViewById(curriculum.getSubjects().size() * 2 + (i + 1));
               String cbtext = tv_s.getText().toString();
               ArrayList<Subject> subjectList = curriculum.getSubjects();
               ArrayList<String> prereqList = new ArrayList<>();
               //ArrayList<String> coreqListPrereq = new ArrayList<>();
               boolean isSS = false;
               boolean isJS = false;
               for (Subject a : subjectList) {
                    prereqList = a.getPrereq();
                    isJS = a.isJs();
                    isSS = a.isSs();
                    if (a.getSubjectName().equals(cbtext)) { // the subject in question
                         Log.d("subject", a.getSubjectName());
                         break;
                    }
               }

               boolean set_vis = true;

               /* for each prereq of the subject */
               for (int x = 0; x < prereqList.size(); x++) {
                    String b = prereqList.get(x); // for each prereq in the list
                                        /*for(Subject s : subjectList){
                                             if(s.getSubjectName().equals(b)){ // get the object of it
                                                  coreqListPrereq = s.getCoreq(); // each coreq must have been checked (exists)
                                                  for(String c : coreqListPrereq){
                                                       boolean exists = student.checkSubjectExists(c);
                                                       if (!exists) {
                                                            set_vis = false;
                                                            break;
                                                       }
                                                  }
                                                  break;
                                             }
                                        }*/
                    boolean exists = student.checkSubjectExists(b);
                    if (!exists) {
                         set_vis = false;
                         break;
                    }
               }

               /* if the row is to be set visible */
               if (set_vis) {
                    if (!isJS && !isSS) { // set visible if it doesn't have JS or SS restriction
                         RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                         r_row_check.setVisibility(View.VISIBLE);
                    } else {
                         if (isJS) { // for JS restriction
                              if (units_taken >= Math.ceil(curriculum.getUnits()*0.50) || student.getStanding() >= UPCC.STUDENT_JUNIOR) { // satisfies JS
                                   RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                                   r_row_check.setVisibility(View.VISIBLE);
                              } else {
                                   RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                                   CheckBox cb_checked = v.findViewById(i + 1);
                                   cb_checked.setChecked(false);
                                   r_row_check.setVisibility(View.GONE);
                              }
                         } else if (isSS) { // for SS restriction
                              if (units_taken >= Math.ceil(curriculum.getUnits()*0.75) || student.getStanding() == UPCC.STUDENT_SENIOR) { // satisfies SS
                                   RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                                   r_row_check.setVisibility(View.VISIBLE);
                              } else {
                                   RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                                   CheckBox cb_checked = v.findViewById(i + 1);
                                   cb_checked.setChecked(false);
                                   r_row_check.setVisibility(View.GONE);
                              }
                         }
                    }

               } else { // not set visible
                    RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                    CheckBox cb_checked = v.findViewById(i + 1);
                    cb_checked.setChecked(false);
                    r_row_check.setVisibility(View.GONE);
               }
               Log.d("units", String.valueOf(units_taken));

          }

          /* to check if the coreq is already visible. Loop through entire subject list */
                              /*for(int i = 0; i < curriculum.getSubjects().size(); i++) {
                                   TextView tv_s = v.findViewById(curriculum.getSubjects().size() * 2 + (i + 1));
                                   String cbtext = tv_s.getText().toString();
                                   ArrayList<Subject> subjectList = curriculum.getSubjects();
                                   ArrayList<String> coreqList = new ArrayList<>();
                                   for (Subject a : subjectList) {
                                        coreqList = a.getCoreq();
                                        if (a.getSubjectName().equals(cbtext)) { // the subject in question
                                             Log.d("subject", a.getSubjectName());
                                             break;
                                        }
                                   }*/
          /* for each coreq of the subject, check if it is visible */
                                   /*for (int x = 0; x < coreqList.size(); x++) {
                                        String b = coreqList.get(x);
                                        Log.d("subject_coreq", b);
                                        TextView cb_p;
                                        int some_i = 0;
                                        for (int j = 0; j < curriculum.getSubjects().size(); j++) {
                                             cb_p = v.findViewById(curriculum.getSubjects().size() * 2 + (j + 1));
                                             String cbtext1 = cb_p.getText().toString();
                                             if (cbtext1.equals(b)) {
                                                  Log.d("subject", cbtext1);
                                                  some_i = j;
                                                  break;
                                             }
                                        }
                                        RelativeLayout coreq_r = v.findViewById(curriculum.getSubjects().size() + (some_i + 1));
                                        Log.d("subject_not", b);
                                        if (coreq_r.getVisibility() != View.VISIBLE) { // if the coreq is not visible, do not show the subject
                                             RelativeLayout r_row_check = v.findViewById(curriculum.getSubjects().size() + (i + 1));
                                             CheckBox cb_checked = v.findViewById(i + 1);
                                             cb_checked.setChecked(false);
                                             r_row_check.setVisibility(View.GONE);
                                             break;
                                        }
                                   }
                              }*/
          //passStanding(UPCC.yearToString(student.getStanding()));
          //passUnits(student.getTotalUnits());
          resultArray.clear();
          for( int x = 0; x < curriculum.getSubjects().size(); x++){
               RelativeLayout r_row_visib = v.findViewById(curriculum.getSubjects().size() + (x + 1));
               CheckBox cb_check = v.findViewById(x + 1);
               if(!cb_check.isChecked() && r_row_visib.getVisibility() == View.VISIBLE){
                    resultArray.add(curriculum.getSubjects().get(x));
               }
          }
          passSubjects(resultArray);
          Log.d("units", Integer.toString(student.getTotalUnits()));
          Log.d("standing", UPCC.yearToString(student.getStanding()));
     }
     /*
     * Name: updateFabStanding
     * Creation Date: 4/9/18
     * Purpose: updates the image of the FAB
     * Arguments:
     *      int year
     * Other Requirements:
     *      Fab
     * Return Value: void
     *
     */
     private void updateFabStanding(int year){
          fab.setMenuButtonLabelText("Current units : " + student.getTotalUnits());
          com.github.clans.fab.FloatingActionButton so = (com.github.clans.fab.FloatingActionButton) v.findViewById(R.id.so);
          com.github.clans.fab.FloatingActionButton jr = (com.github.clans.fab.FloatingActionButton) v.findViewById(R.id.jr);
          com.github.clans.fab.FloatingActionButton sr = (com.github.clans.fab.FloatingActionButton) v.findViewById(R.id.sr);


          if(year == 2){
               so.setLabelText("Cleared!");
               jr.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_JUNIOR - 1));
               sr.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_SENIOR - 1));
          } else if ( year == 3){
               so.setLabelText("Cleared!");
               jr.setLabelText("Cleared!");
               sr.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_SENIOR - 1));
          } else if (year >= 4){
               so.setLabelText("Cleared!");
               jr.setLabelText("Cleared!");
               sr.setLabelText("Cleared!");
          } else {

               so.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_SOPHOMORE - 1));
               jr.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_JUNIOR - 1));
               sr.setLabelText(student.getUnitsPerYearString(UPCC.STUDENT_SENIOR - 1));
          }


          if(year == 1) {
               fab.getMenuIconView().setImageResource(R.drawable.fr);
          } else if (year == 2){
               fab.getMenuIconView().setImageResource(R.drawable.so);
          } else if (year == 3){
               fab.getMenuIconView().setImageResource(R.drawable.jr);
          } else {
               fab.getMenuIconView().setImageResource(R.drawable.sr);
          }
     }
     /*
     * Name: createDivider
     * Creation Date: 2/19/18
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
          View v_d = new View(v.getContext());
          v_d.setLayoutParams(new LinearLayout.LayoutParams(
                  (LinearLayout.LayoutParams.MATCH_PARENT),
                  1
          ));
          v_d.setBackgroundColor(Color.parseColor("#B3B3B3"));
          v_d.getBackground().setAlpha(100);
          return v_d;
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
          v.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
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
          TextView aTextView = new TextView(v.getContext());
          int paddingDp = convertDpToPx(5);
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
          CheckBox aCheckBox = new CheckBox(v.getContext());
          aCheckBox.setId(anID);
          aCheckBox.setTag(anID);
          aCheckBox.setButtonDrawable(R.drawable.custom_checkbox);
          aCheckBox.setClickable(false);

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
    */

     /*
     * Name: printBuffer
     * Creation Date: 2/16/18
     * Purpose: Displays subject desc
     * Arguments:
     *      Name
     *      buffer
     * Other Requirements:
     *      none
     * Return Value: void
     * https://stackoverflow.com/questions/15762905/how-can-i-display-a-list-view-in-an-android-alert-dialog. Raghunandan
     */
     public void printBuffer(String aName, ArrayList<String> details) {

          String names[] = details.toArray(new String[details.size()]);
          AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
          LayoutInflater inflater = getLayoutInflater();
          View convertView = (View) inflater.inflate(R.layout.linear_list, null);
          alertDialog.setView(convertView);
          alertDialog.setTitle(aName);
          ListView lv = (ListView) convertView.findViewById(R.id.listView1);
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(v.getContext(), android.R.layout.simple_list_item_1, names);
          lv.setAdapter(adapter);
          alertDialog.show();

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
    * Name: alignRelative
    * Creation Date: 2/14/18
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
    * Name: alignRelative
    * Creation Date: 2/14/18
    * Purpose: setups the positioning in a relative layout
    * Arguments:
    *      aView - the component to be changed
    *      anAlignment - the corresponind value specified by the layout
    * Other Requirements:
    *      none
    * Return Value: TextView - void
    */
     public void alignRelative(View aView, int anAlignment, int id) {
          RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
          lp.addRule(anAlignment, id);
          aView.setLayoutParams(lp);
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
          builder = new AlertDialog.Builder(getActivity());
          builder.setCancelable(true);
          builder.setTitle(title);
          builder.setMessage(Message);
          builder.show();
     }
     /*
     * Name: setUpFAB
     * Creation Date: 4/09/18
     * Purpose: setups the floating action button and its events
     * Arguments:
     *      none
     * Other Requirements:
     *      fabNext - the floating action button as specified in the layout of the activity
     * Return Value: void
     *
     * hcmonte. https://stackoverflow.com/questions/34560770/hide-fab-in-nestedscrollview-when-scrolling/35427564. Last Accessed: 2/02/18
     */
     public void setUpFAB() {
          fab = (FloatingActionMenu) v.findViewById(R.id.f_standing_detail);
          updateFabStanding(student.getStanding());
          fab.setIconAnimated(false);
          /* Hide or show FAB depending on user's scroll */
          NestedScrollView nsv = (NestedScrollView) v.findViewById(R.id.f_sView_input);
          nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
               @Override
               public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY) {
                         fab.hideMenu(true);
                    } else {
                         fab.showMenu(true);
                    }
               }
          });

     }

}
