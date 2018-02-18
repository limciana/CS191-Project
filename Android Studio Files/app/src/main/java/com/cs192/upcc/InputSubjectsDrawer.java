package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class InputSubjectsDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
     Curriculum curriculum; //The curriculum that was passed from the previous screen.
     LinearLayout layout; //The parent layout of this module's screen.
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_input_subjects_drawer);
          curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");
          layout = findViewById(R.id.layout);
          Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
          setSupportActionBar(toolbar);

          for (int i = 0; i < curriculum.getSubjects().size(); i++) {
               RelativeLayout r_row = new RelativeLayout(this);
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
               desc.setTextSize((float) convertDpToPx(7));
               units.setTextSize((float) convertDpToPx(6));
               cb = createCheckBox(i + 1);

               /* To change, depreciated */
               tv.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.primaryText));
               desc.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.secondaryText));

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

               r_row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                         int id = view.getId() - curriculum.getSubjects().size();

                         CheckBox checkBox = (CheckBox) findViewById(id);
                         CheckBox cbTemp;

                         checkBox.toggle();
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

          FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
          fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
               }
          });

          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                  this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
          drawer.addDrawerListener(toggle);
          toggle.syncState();

          NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
          navigationView.setNavigationItemSelectedListener(this);
     }

     @Override
     public void onBackPressed() {
          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          if (drawer.isDrawerOpen(GravityCompat.START)) {
               drawer.closeDrawer(GravityCompat.START);
          } else {
               super.onBackPressed();
          }
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
          // Inflate the menu; this adds items to the action bar if it is present.
          getMenuInflater().inflate(R.menu.input_subjects_drawer, menu);
          return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
          // Handle action bar item clicks here. The action bar will
          // automatically handle clicks on the Home/Up button, so long
          // as you specify a parent activity in AndroidManifest.xml.
          int id = item.getItemId();

          //noinspection SimplifiableIfStatement
          if (id == R.id.action_settings) {
               return true;
          }

          return super.onOptionsItemSelected(item);
     }

     @SuppressWarnings("StatementWithEmptyBody")
     @Override
     public boolean onNavigationItemSelected(MenuItem item) {
          // Handle navigation view item clicks here.
          int id = item.getItemId();

          if (id == R.id.nav_camera) {
               // Handle the camera action
          } else if (id == R.id.nav_gallery) {

          } else if (id == R.id.nav_slideshow) {

          } else if (id == R.id.nav_manage) {

          } else if (id == R.id.nav_share) {

          } else if (id == R.id.nav_send) {

          }

          DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
          drawer.closeDrawer(GravityCompat.START);
          return true;
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
          AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
          LayoutInflater inflater = getLayoutInflater();
          View convertView = (View) inflater.inflate(R.layout.linear_list, null);
          alertDialog.setView(convertView);
          alertDialog.setTitle(aName);
          ListView lv = (ListView) convertView.findViewById(R.id.listView1);
          ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
          lv.setAdapter(adapter);
          alertDialog.show();

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
}

