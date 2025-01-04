package com.tale.qa.file_manager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;
import com.tale.qa.check_quiz.Activity_Undone_child;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Activity_CheckQuiz_ListFile extends ListFileActivity {

    private String path;
    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    StringBuilder text = new StringBuilder();
    String date = DateFormat.format("MM-dd-yyyyy-h:mmaa", System.currentTimeMillis()).toString();
    String SectionID;
    int parseID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkquiz_listfile);



        path = Environment.getExternalStorageDirectory().getPath();

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".") && !file.endsWith(".pdf") && !file.endsWith(".png") && !file.endsWith(".jpeg")
                        && !file.endsWith(".jpg") && !file.endsWith(".gif") && !file.endsWith(".rar") && !file.endsWith(".zip")
                        && !file.endsWith(".apk") && !file.endsWith(".obb") && !file.endsWith(".mp3") && !file.endsWith(".mp4")) {
                    //FILE FILTER HARD CODED -- CHANGE TO ARRAY
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }
    String title;
    String content_toString;
    String filename;
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        filename = (String) getListAdapter().getItem(position);
        if (path.endsWith(File.separator)) {
            //Toast.makeText(this, filename, Toast.LENGTH_LONG).show();
            filename = path + filename;
        } else {
            //Toast.makeText(this, filename, Toast.LENGTH_LONG).show();
            title = filename;
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, Activity_CheckQuiz_ListFile.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {

            //Toast.makeText(this, filename + " is not a directory", Toast.LENGTH_LONG).show();
            //-------------------------- GET CONTENT OF THE FILE --------------------------------//

            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Activity_CheckQuiz_ListFile.this);
            builder.setTitle("GET FILE?");
            builder.setIcon(android.R.drawable.ic_dialog_alert);

            builder.setMessage("Are you sure this is the File?")
                    .setCancelable(false)
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            BufferedReader br = null;
                            String line = "";
                            try {
                                br = new BufferedReader(new FileReader(filename));


                                while ((line = br.readLine()) != null) {
                                    text.append(line);
                                    text.append('\n');
                                }
                                br.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    br.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            /* */
                            //if (!line.equals("")) {
                            //--------------------------- INSERT CONTENT -------------------------------//
                            //Intent intent = getIntent();
                            //SectionID = intent.getStringExtra("SectionID");
                           // parseID = Integer.parseInt(SectionID);
                            content_toString = text.toString();
                            mydb.Insert_QuizList(mydb, title, content_toString, date);
                            Intent dotHome = new Intent(getApplicationContext(), Activity_Undone_child.class);
                            startActivity(dotHome);
                        }
                    })
                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

            android.app.AlertDialog alertDialog = builder.create();
            alertDialog.show();

            // }else{Toast.makeText(this, "Oops, Sometimes something really happen.", Toast.LENGTH_LONG).show();}

        }
    }

}
