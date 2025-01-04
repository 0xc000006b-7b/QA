package com.tale.qa;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.tale.qa.student.Student1Activity;
import com.tale.qa.student.Student_Login;
import com.tale.qa.student.Student_Register;

public class MainActivity extends AppCompatActivity {
    public Button teacherBtn, studentBtn;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

    }

    public void init() {

        DatabaseHelper db = new DatabaseHelper(context);

        teacherBtn = (Button) findViewById(R.id.teacherBtn1);
        teacherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Teacher1Activity.class);
                startActivity(intent);
            }
        });

        studentBtn = (Button) findViewById(R.id.studentBtn1);
        studentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Student_Login.class);
                startActivity(intent);
            }
        });

    }


}
