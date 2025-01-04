package com.tale.qa.student;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;

public class Student_Register extends AppCompatActivity {
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_register_activity);
        init();
    }


    public void init(){
        Button reg = (Button)findViewById(R.id.student_Register);
        Button canc = (Button)findViewById(R.id.student_cancel);
        final EditText fname = (EditText) findViewById(R.id.register_Fname);
        final EditText idnum = (EditText) findViewById(R.id.register_Id);
        final Spinner chooseGender = (Spinner)findViewById(R.id.spinner_gender);
        String[] items = new String[]{"Gender","Male","Female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Student_Register.this, android.R.layout.simple_spinner_dropdown_item, items);
        chooseGender.setAdapter(adapter);

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String strname = fname.getText().toString();
                String strnumber = idnum.getText().toString();

                String gen = chooseGender.getSelectedItem().toString();

                if (strname.equals("") && strnumber.equals("") && gen.equals("Gender")) {
                    Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
                } else {
                    int idn = Integer.parseInt(strnumber);
                    DatabaseHelper mydb = new DatabaseHelper(context);
                    mydb.Insert_Student(mydb, strname, idn, gen);
                    Toast.makeText(getApplicationContext(), "You are now registered", Toast.LENGTH_SHORT).show();
                    fname.setText("");
                    idnum.setText("");
                    Intent intent = new Intent(getApplicationContext(), Student_Login.class);
                    startActivity(intent);
                }
            }
        });

        canc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Student_Login.class);
                startActivity(intent);
            }
        });




    }

}
