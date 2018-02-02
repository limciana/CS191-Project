package com.cs192.upcc;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.file.StandardWatchEventKinds;
import java.util.ArrayList;

public class SelectCurriculum extends AppCompatActivity implements View.OnClickListener {


    LinearLayout parent;
    CheckBox cb;
    TextView tv;
    ArrayList<String> chck;
    ArrayList<String> curriculum_names;
    DatabaseHelper UPCCdb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_curriculum);

        UPCCdb = new DatabaseHelper(this);
        curriculum_names = new ArrayList<String>();
        UPCCdb.createDB();

        Cursor res = UPCCdb.getCurriculum();
        if(res.getCount() == 0){
            Toast.makeText(getApplicationContext(), "ERROR: NOTHING TO SHOW", Toast.LENGTH_SHORT).show();
        }

        while(res.moveToNext()){
            curriculum_names.add(res.getString(UPCC.SUBJECT_CURRICULUM));
        }

        chck = new ArrayList<String>();
        chck.add("Ako");
        

        parent = (LinearLayout) findViewById(R.id.ll_parentLayout);

        for (int i = 0; i < curriculum_names.size(); i++) {
            RelativeLayout r_row = new RelativeLayout(this);

            tv = createTextView(curriculum_names.get(i));
            cb = createCheckBox(i + 1);

            r_row.setId((i + 1) + curriculum_names.size());

            //tv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            tv.setLayoutParams(lp);
            r_row.addView(tv);

            RelativeLayout.LayoutParams lp_1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp_1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            cb.setLayoutParams(lp_1);
            r_row.addView(cb);
            //cb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            //row.addView(tv);
            //row.addView(cb);
            r_row.setPadding(20, 20, 20, 20);
            //parent.addView(row);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // If we're running on Honeycomb or newer, then we can use the Theme's
                // selectableItemBackground to ensure that the View has a pressed state
                TypedValue outValue = new TypedValue();
                getApplicationContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                r_row.setBackgroundResource(outValue.resourceId);
            }
            parent.addView(r_row);


            View v = new View(this);
            v.setLayoutParams(new LinearLayout.LayoutParams(
                    (LinearLayout.LayoutParams.MATCH_PARENT),
                    1
            ));
            v.setBackgroundColor(Color.parseColor("#B3B3B3"));
            v.getBackground().setAlpha(100);
            parent.addView(v);


            r_row.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        String ind = String.valueOf(view.getId());
        int id = view.getId() - curriculum_names.size();
        int indx = Integer.parseInt(ind);
        String temp = String.valueOf(id);
        CheckBox checkBox = (CheckBox) findViewById(id);
        CheckBox cbTemp;
        for (int i = 1; i <= curriculum_names.size(); i++) {
            cbTemp = (CheckBox) findViewById(i);

            cbTemp.setChecked(false);

        }
        checkBox.toggle();
        //Toast.makeText(getApplicationContext(), temp, Toast.LENGTH_SHORT).show();
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
}
