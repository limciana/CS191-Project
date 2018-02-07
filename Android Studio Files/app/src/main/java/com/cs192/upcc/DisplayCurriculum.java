/*
 * This is a course requirement for CS 192 Software Engineering II
 * under the supervision of Asst. Prof. Ma. Rowena C. Solamo
 * of the Department of Computer Science, College of Engineering,
 * University of the Philippines, Diliman
 * for the AY 2017-2018.
 * This code is written by Rayven Ely Cruz.
 */

/* Code History
 * Programmer       Date        Description
 * Rayven Ely Cruz  2/02/2018   Created the class for testing.
 */

/*
 * File Creation Date: 2/02/18
 * Development Group: James Abaja, Rayven Cruz, Ciana Lim
 * Client Group: CS 192 Class
 * Purpose of the Software: To aid the DCS students in tracking their taken subjects, and the subjects they can take afterwards.
 */

package com.cs192.upcc;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class DisplayCurriculum extends AppCompatActivity {
     Curriculum curriculum; //The curriculum received

     /*
     * Name: onCreate
     * Creation Date: 1/30/18
     * Purpose: Renders the layout, adds button and receives the Curriculum object
     * Arguments:
     *      savedInstanceState - Bundle, for passing data between Android activities
     * Other Requirements:
     *      curriculum - the curriculum to be received
     * Return Value: void
     */
     @Override
     protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.activity_display_curriculum);

          /* Receive curriculum */
          curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");

          /* Find button defined in layout */
          Button button = (Button) findViewById(R.id.button);

          /* Set on click listener for button */
          button.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                    printBuffer(curriculum.printCurriculum());
               }
          });
     }

     /*
     * Name: printBuffer
     * Creation Date: 1/30/18
     * Purpose: prints to screen
     * Arguments:
     *      buffer - stringbuffer to be printed
     * Other Requirements:
     *      curriculum - the received curriculum
     * Return Value: void
     */
     public void printBuffer(StringBuffer buffer) {

          AlertDialog.Builder builder = new AlertDialog.Builder(this);
          builder.setCancelable(true);
          builder.setTitle(curriculum.getName());
          builder.setMessage(buffer);
          builder.show();

     }
}
