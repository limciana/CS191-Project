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
    Curriculum curriculum;
    CheckBox checkbox;
    LinearLayout layout;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_subjects);
        curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");
        layout = findViewById(R.id.layout);
        for(int i = 0; i < curriculum.subjects.size(); i++) {
            RelativeLayout r_row = new RelativeLayout(this);

            text = createTextView(curriculum.subjects.get(i).getSubjectName());
            checkbox = createCheckBox(i + 1);

            r_row.setId((i + 1) + curriculum.subjects.size());

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
        CheckBox init = (CheckBox) findViewById(1);
        init.toggle();
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
    }*/

    public void printBuffer(StringBuffer buffer){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Subject");
        builder.setMessage(buffer);
        builder.show();

    }
}
