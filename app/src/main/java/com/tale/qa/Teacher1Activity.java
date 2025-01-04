package com.tale.qa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tale.qa.check_quiz.Activity_Check_Quiz;

public class Teacher1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher1);

        setButtonListener();

    }


    public void setButtonListener(){
        Button ComposeBtn = (Button) findViewById(R.id.Compose_Quiz);
        Button UpBtn = (Button) findViewById(R.id.Up_Quiz);
        Button CheckBtn = (Button) findViewById(R.id.Check_Quiz);

        ComposeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent compose;
                compose = new Intent(getApplicationContext(), Compose_Quiz.class);
                startActivity(compose);
            }
        });

        UpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent Up;
                //Up = new Intent(getApplicationContext(), )
                Toast.makeText(getApplicationContext(), "UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();
            }
        });

        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent check;
                check = new Intent(getApplicationContext(), Activity_Check_Quiz.class);
                startActivity(check);
            }
        });
    }

}
