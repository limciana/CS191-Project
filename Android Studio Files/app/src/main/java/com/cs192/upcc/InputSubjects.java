/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by James Abaja.
 */

/* Code History
 * Programmer     Date     Description
 * James Abaja    2/2/18   Set up initial file of the back end for the Input Subjects Screen.
 */

/*
 * File Creation Date: 2/2/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class InputSubjects extends AppCompatActivity {

    LinearLayout layout;
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_subjects);

        ArrayList<String> subjects = new ArrayList<>();

        layout = findViewById(R.id.layout);

        subjects.add("CS 11");
        subjects.add("CS 12");
        subjects.add("CS 21");
        subjects.add("CS 30");
        subjects.add("CS 32");

        for(int i=0; i < subjects.size(); i++) {
            checkBox = new CheckBox(this);
            checkBox.setText(subjects[i]);
            checkBox.setId(i);
            layout.addView(checkBox);
        }
    }
}
