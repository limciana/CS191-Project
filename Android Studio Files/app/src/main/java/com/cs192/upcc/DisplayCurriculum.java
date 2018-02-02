package com.cs192.upcc;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class DisplayCurriculum extends AppCompatActivity {
    Curriculum curriculum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_curriculum);
        curriculum = (Curriculum) getIntent().getSerializableExtra("curriculum");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                printBuffer(curriculum.printCurriculum());
            }
        });
    }

    public void printBuffer(StringBuffer buffer){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Subject");
        builder.setMessage(buffer);
        builder.show();

    }
}
