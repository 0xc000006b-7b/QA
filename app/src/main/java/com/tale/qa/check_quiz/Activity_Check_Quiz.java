package com.tale.qa.check_quiz;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tale.qa.R;

public class Activity_Check_Quiz extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_quiz);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setButtonListener();
    }

    public void setButtonListener(){
        Button undone = (Button) findViewById(R.id.undoneBtn);
        Button done = (Button) findViewById(R.id.doneBtn);
        Button getFile = (Button) findViewById(R.id.getfileBtn);

        undone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent undone;
                undone = new Intent(getApplicationContext(), Activity_CheckQuiz_Undone.class);
                startActivity(undone);
                //Toast.makeText(getApplicationContext(), "UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent Up;
                //Up = new Intent(getApplicationContext(), )
                Toast.makeText(getApplicationContext(), "UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();
            }
        });

        getFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent check;
                //check = new Intent(getApplicationContext(), Activity_Check_Quiz.class);
                //startActivity(check);
                Toast.makeText(getApplicationContext(), "UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
