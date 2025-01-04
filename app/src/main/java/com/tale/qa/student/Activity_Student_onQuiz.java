package com.tale.qa.student;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Activity_Student_onQuiz extends AppCompatActivity {

    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    String getAns, getName, getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_onquiz);

        fill_TextView();
        getAns();
    }

    String dbId, dbTitle, dbContent;

    public void fill_TextView(){
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        int convId = Integer.parseInt(id);
        Cursor rs = mydb.Retrieve_Student_Quiz_ID(convId); //------------ SELECT FROM DATABASEHELPER
        if (rs.getCount() != 0) {
            rs.moveToFirst();

            do {
                dbId = rs.getString(0);
                dbTitle = rs.getString(1);
                dbContent = rs.getString(2);
            } while (rs.moveToNext());
        }

            TextView student_showQuiz = (TextView) findViewById(R.id.student_showQuiz);

            student_showQuiz.setText(dbContent);

    }

    public void getAns(){
        Button submit_Ans = (Button) findViewById(R.id.student_submit_ans);
        submit_Ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student_Submit_Quiz();
            }
        });
    }

    public void Error_Dialog(final String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Student_onQuiz.this);
        builder.setTitle("Notepad Says");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (msg.equals("Saved")) {

                            Intent intent = new Intent(getApplicationContext(), Student1Activity.class);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void Student_Submit_Quiz()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Student_onQuiz.this);
        builder.setTitle("Submit");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage("Are you sure?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = getIntent();
                        String Fname = intent.getStringExtra("Fname");
                        EditText ans = (EditText) findViewById(R.id.student_fill_ans);
                        getAns = ans.getText().toString();
                        getName = Fname;
                        getDate = DateFormat.format("MM-dd-yyyyy-h:mmaa", System.currentTimeMillis()).toString();
                        if (!ans.getText().toString().equals("")) {

                            mydb.Insert_Student_Answ(mydb, getName, getAns, getDate);
                            Error_Dialog("Saved");
                            createFile();
                        } else {
                            Error_Dialog("Ooops, Fill up those fields!");
                        }

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "Take your time", Toast.LENGTH_LONG).show();
                    }
                });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    /*
          --- WHEN USING SHAREIT TO PASS ---

           NEED TO ADD ENCRYPT AND DECRYPT FOR SECURITY REASON
           ONE STUDENT MIGHT SEND A QUIZ TO HIS/HER FELLOW CLASSMATE SINCE ITS ONLY A TEXT
           ITS IS READABLE

          ----------------------------------

          --- NEED TO ADD SOME FOREIGN KEY ---
            * STUDENT_****
          ------------------------------------

          --- NEED TO ADD *TAKEN* IN STUDENT_QUIZ ---
            * YES/NO VALUE SO WE CAN DETERMINE WHETHER THAT QUIZ IS ALREADY TAKEN OR NOT


     */

    public void createFile(){
        try {
            // this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "QuizApp/MyAnswer");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, getName);  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(getAns);
            writer.flush();
            writer.close();
            String m = "File Generated "+getName+".txt";
            Toast.makeText(getBaseContext(), m, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_Student_onQuiz.this);
            builder.setTitle("WARNING");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setMessage("If you choose yes, your ANSWER will be submitted and this QUIZ will be DISABLED so you cant go back")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = getIntent();
                            String Fname = intent.getStringExtra("Fname");
                            EditText ans = (EditText) findViewById(R.id.student_fill_ans);
                            getAns = ans.getText().toString();
                            getName = Fname;
                            getDate = DateFormat.format("MM-dd-yyyyy-h:mmaa", System.currentTimeMillis()).toString();
                            if (!ans.getText().toString().equals("")) {

                                mydb.Insert_Student_Answ(mydb, getName, getAns, getDate);
                                Error_Dialog("Saved");
                                createFile();
                            } else {
                                Error_Dialog("Ooops, Fill up those fields!");
                            }

                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getApplicationContext(), "Take your time", Toast.LENGTH_LONG).show();
                        }
                    });

            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        return super.onKeyDown(keyCode, event);
    }

}
