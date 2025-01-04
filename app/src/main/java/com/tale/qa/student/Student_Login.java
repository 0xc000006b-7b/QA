package com.tale.qa.student;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;

public class Student_Login extends AppCompatActivity {
    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    Boolean login_status = false;
    public TextView reg;
    String toStringFrst = "", Fname = "", dbID = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_login_activity);
        init();
    }

    public void init() {
        Button login = (Button) findViewById(R.id.student_login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText idnum = (EditText) findViewById(R.id.retrieve_id);

                if (!idnum.getText().toString().equals("")) {
                    String idtoString = idnum.getText().toString();
                    int convId = Integer.parseInt(idtoString);
                    Cursor rs = mydb.Retrieve_Student(convId);
                    if (rs.getCount() != 0) {
                        rs.moveToFirst();
                        do {
                            dbID = rs.getString(0);
                            toStringFrst = rs.getString(1);
                            Fname = rs.getString(2);

                            if (idtoString.equals(toStringFrst)) {
                                Toast.makeText(Student_Login.this, toStringFrst, Toast.LENGTH_SHORT).show();
                                Toast.makeText(Student_Login.this, Fname, Toast.LENGTH_SHORT).show();
                                login_status = true;
                                break;
                            }

                        } while (rs.moveToNext());
                    }


                    if (login_status) {
                        Intent intent = new Intent(getApplicationContext(), Student1Activity.class);
                        intent.putExtra("ID", dbID);
                        //intent.putExtra("Fname", Fname);
                        startActivity(intent);
                    } else {
                        Toast.makeText(Student_Login.this, "Account not found", Toast.LENGTH_SHORT).show();
                        idnum.setText("");
                    }
                } else {
                    Toast.makeText(Student_Login.this, "Fill those field", Toast.LENGTH_SHORT).show();
                }

            }

        });

        reg = (TextView)findViewById(R.id.reg_textView);
        reg.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Student_Register.class);
                startActivity(intent);
             }
    });

    }
}