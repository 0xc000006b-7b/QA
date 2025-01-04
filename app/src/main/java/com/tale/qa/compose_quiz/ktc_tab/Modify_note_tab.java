package com.tale.qa.compose_quiz.ktc_tab;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
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

public class Modify_note_tab extends AppCompatActivity {
    public Button noteBtn3, noteBtn4;
    Context context = this;
    public String _title2, _content2, _date2;
    DatabaseHelper mydb = new DatabaseHelper(context);
    int pos, retrieveID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_note_tab);
        back();


        //---------- Whenever the user click a note then setText for EditText

        EditText kTitle = (EditText) findViewById(R.id.ktcT);
        EditText kContent = (EditText) findViewById(R.id.ktcCon);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        pos = Integer.parseInt(id);
        Toast.makeText(Modify_note_tab.this, id, Toast.LENGTH_LONG).show();

        kTitle.setText(title);
        kContent.setText(content);

    }

    public void back() {
        noteBtn3 = (Button) findViewById(R.id.noteCan);
        noteBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Modify_note_tab.this, Compose_Quiz.class);
                startActivity(intent);

            }
        });

        noteBtn4 = (Button) findViewById(R.id.noteSav);
        noteBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Cursor cursor = mydb.Retrieve_ID(pos);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();

                    do {

                        retrieveID = Integer.parseInt(cursor.getString(0));

                    } while (cursor.moveToNext());
                }
                if (pos == retrieveID) {
                    updateKtc();
                } else {
                }


            }
        });
    }

    public void Error_Dialog(final String msg)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(Modify_note_tab.this);
        builder.setTitle("Notepad Says");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (msg.equals("Saved")) {

                            Intent intent = new Intent(Modify_note_tab.this, Compose_Quiz.class);
                            startActivity(intent);
                        }
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void updateKtc(){

        EditText kTitle = (EditText) findViewById(R.id.ktcT);
        EditText kContent = (EditText) findViewById(R.id.ktcCon);
        _title2 = kTitle.getText().toString();
        _content2 = kContent.getText().toString();
        _date2 = DateFormat.format("MM-dd-yyyyy-h:mmaa", System.currentTimeMillis()).toString();
        if (!kTitle.getText().toString().equals("") && !kContent.getText().toString().equals("")) {

            mydb.Update_KTC(mydb, _date2, _title2, _content2, pos);
            //Toast.makeText(Notepad_tab.this, _title, Toast.LENGTH_SHORT).show();
            //Toast.makeText(Notepad_tab.this, _content, Toast.LENGTH_SHORT).show();
            //Toast.makeText(Notepad_tab.this, _date, Toast.LENGTH_SHORT).show();
            Error_Dialog("Saved");
            createFile();
        } else {
            Error_Dialog("Ooops!, Fill up those fields!");
        }

    }

    public void createFile(){
        try {
            // this will create a new name everytime and unique
            File root = new File(Environment.getExternalStorageDirectory(), "QuizApp/KTC");
            // if external memory exists and folder with name Notes
            if (!root.exists()) {
                root.mkdirs(); // this will create folder.
            }
            File filepath = new File(root, _title2);  // file path to save
            FileWriter writer = new FileWriter(filepath);
            writer.append(_content2);
            writer.flush();
            writer.close();
            String m = "File Generated "+_title2+".txt";
            Toast.makeText(getBaseContext(), m, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(),e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }




}
