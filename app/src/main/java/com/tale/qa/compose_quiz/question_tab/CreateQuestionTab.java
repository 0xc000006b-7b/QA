package com.tale.qa.compose_quiz.question_tab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tale.qa.Compose_Quiz;
import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateQuestionTab extends AppCompatActivity {
    Button noteBtn1, noteBtn2;
    Context context = this;
    public String _title, _content, _date;
    DatabaseHelper mydb = new DatabaseHelper(context);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question_tab);
        back();
    }

    public void back() {
        noteBtn1 = (Button) findViewById(R.id.QuestionTabCancel);
        noteBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateQuestionTab.this, Compose_Quiz.class);
                startActivity(intent);

            }
        });

        noteBtn2 = (Button) findViewById(R.id.QuestionTabSave);
        noteBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertQuestions();

            }
        });
    }

    public void Error_Dialog(final String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateQuestionTab.this);
        builder.setTitle("Notepad Says");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (msg.equals("Saved")) {

                            Intent intent = new Intent(CreateQuestionTab.this, Compose_Quiz.class);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void insertQuestions(){

        EditText kTitle = (EditText) findViewById(R.id.QuestionTitle);
        EditText kContent = (EditText) findViewById(R.id.QuestionContent);
        _title = kTitle.getText().toString();
        _content = kContent.getText().toString();
        _date = DateFormat.format("MM-dd-yyyyy-h:mmaa", System.currentTimeMillis()).toString();
        if (!kTitle.getText().toString().equals("") && !kContent.getText().toString().equals("")) {

            mydb.Insert_Questions(mydb, _title, _content, _date);
            //Toast.makeText(Notepad_tab.this, _title, Toast.LENGTH_SHORT).show();
            //Toast.makeText(Notepad_tab.this, _content, Toast.LENGTH_SHORT).show();
            //Toast.makeText(Notepad_tab.this, _date, Toast.LENGTH_SHORT).show();
            Error_Dialog("Saved");
            createFile();
        } else {
            Error_Dialog("Ooops, Fill up those fields!");
        }

    }


    public void createFile(){
        try {
            // this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "QuizApp/Questions");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, _title);  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(_content);
            writer.flush();
            writer.close();
            String m = "File Generated "+_title+".txt";
            Toast.makeText(getBaseContext(), m, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
