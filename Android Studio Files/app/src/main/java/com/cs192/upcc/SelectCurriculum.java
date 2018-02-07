package com.cs192.upcc;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
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


    LinearLayout parent;
    CheckBox cb;
    TextView tv;
    ArrayList<String> chck;
    ArrayList<String> curriculumNames;
    DatabaseHelper UPCCdb;
    Button fabNext;
    Curriculum selectedCurriculum;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_curriculum);

        UPCCdb = new DatabaseHelper(this);
        curriculumNames = new ArrayList<String>();
        UPCCdb.createDB();

        Cursor res = UPCCdb.getCurriculum();
        if(res.getCount() == 0){
            Toast.makeText(getApplicationContext(), "ERROR: NOTHING TO SHOW", Toast.LENGTH_SHORT).show();
        }

        while(res.moveToNext()){
            curriculumNames.add(res.getString(UPCC.SUBJECT_CURRICULUM));
        }

        chck = new ArrayList<String>();
        fabNext = findViewById(R.id.next_button);
        fabNext.setOnClickListener(this);
        NestedScrollView nsv = findViewById(R.id.sView);
        /*nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fabNext.hide();
                } else {
                    fabNext.show();
                }
            }
        });*/

        parent = (LinearLayout) findViewById(R.id.ll_parentLayout);

        for (int i = 0; i < curriculumNames.size(); i++) {
            RelativeLayout r_row = new RelativeLayout(this);

            tv = createTextView(curriculumNames.get(i));
            cb = createCheckBox(i + 1);

            r_row.setId((i + 1) + curriculumNames.size());

            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tv.setLayoutParams(lp);
            r_row.addView(tv);

            RelativeLayout.LayoutParams lp_1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb.setLayoutParams(lp_1);
            r_row.addView(cb);

            r_row.setPadding(20, 20, 20, 20);

            r_row.setBackgroundResource(setClickEffect().resourceId);

            parent.addView(r_row);

            parent.addView(createDivider());

            r_row.setOnClickListener(this);
        }
        CheckBox init = (CheckBox) findViewById(1);
        init.toggle();


    }

    @Override
    public void onClick(View view) {
       // Toast.makeText(getApplicationContext(), String.valueOf(view.getId()), Toast.LENGTH_SHORT).show();
        if(view.getId() == R.id.next_button){
            //Toast.makeText(getApplicationContext(), "asda", Toast.LENGTH_SHORT).show();
            CheckBox cbTemp;
            int selectedId = 0;
            for(int i = 1; i <= curriculumNames.size(); i++){
                cbTemp = (CheckBox) findViewById(i);
                if(cbTemp.isChecked()){
                    selectedId = i;
                }
            }
            selectedCurriculum = new Curriculum(curriculumNames.get(selectedId - 1));
            
            Cursor res = UPCCdb.getSubjects(curriculumNames.get(selectedId - 1));
            if(res.getCount() == 0){
                Toast.makeText(getApplicationContext(), "Error: No Subjects", Toast.LENGTH_SHORT).show();
            }
            while(res.moveToNext()){
                int tempUnits = 0;
                int tempYear = 0;
                if(res.getString(UPCC.SUBJECT_YEAR) != null){
                    tempYear = Integer.parseInt(res.getString(UPCC.SUBJECT_YEAR));
                }
                if(res.getString(UPCC.SUBJECT_UNITS) != null){
                    tempUnits = Integer.parseInt(res.getString(UPCC.SUBJECT_UNITS));
                }
                Subject tempSubject = new Subject(res.getString(UPCC.SUBJECT_CURRICULUM), res.getString(UPCC.SUBJECT_NAME),
                        res.getString(UPCC.SUBJECT_DESC), tempUnits, stringToBoolean(res.getString(UPCC.SUBJECT_JS)),
                        stringToBoolean(res.getString(UPCC.SUBJECT_SS)), tempYear, res.getString(UPCC.SUBJECT_PREREQ),
                        res.getString(UPCC.SUBJECT_COREQ));
                selectedCurriculum.addSubject(tempSubject);
            }

            Intent intent = new Intent(getBaseContext(), InputSubjects.class);
            intent.putExtra("curriculum", selectedCurriculum);
            startActivity(intent);

        } else {

            String ind = String.valueOf(view.getId());
            int id = view.getId() - curriculumNames.size();
            int indx = Integer.parseInt(ind);
            String temp = String.valueOf(id);
            CheckBox checkBox = (CheckBox) findViewById(id);
            CheckBox cbTemp;
            for (int i = 1; i <= curriculumNames.size(); i++) {
                cbTemp = (CheckBox) findViewById(i);
                cbTemp.setChecked(false);
            }
            checkBox.toggle();

        }
    }

    private boolean stringToBoolean(String aString){
        if(aString != null) {
            if (aString.equals("true")) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    private TextView createTextView(String aTextName){
        TextView aTextView = new TextView(this);
        aTextView.setPadding(10, 10, 10, 10);
        aTextView.setText(aTextName);
        return aTextView;
    }
    private CheckBox createCheckBox(int anID){
        CheckBox aCheckBox = new CheckBox(this);
        aCheckBox.setId(anID);
        aCheckBox.setTag(anID);
        aCheckBox.setClickable(false);
        return aCheckBox;
    }

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

    private TypedValue setClickEffect(){
        TypedValue outValue = new TypedValue();
        getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        return outValue;
    }


    public void printBuffer(StringBuffer buffer){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Subject");
        builder.setMessage(buffer);
        builder.show();

    }
}
