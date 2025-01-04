package com.tale.qa.check_quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;
import com.tale.qa.student.Activity_Student_onQuiz;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_CheckQuiz_Undone extends AppCompatActivity {

    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    String getCourse, getSection, getYear, getPeriod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkquiz_undone);
        showSection();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        fab_listener();

    }


    String retrieve_ID, retrieve_Course, retrieve_Section, retrieve_Year, retrieve_Period;

    public void showSection() {

        Cursor rs = mydb.Retrieve_Section();
        if (rs.getCount() != 0) {
            rs.moveToFirst();

            ArrayList<HashMap<String, String>> data;
            final ListView listview = (ListView) findViewById(R.id.section_ListView);
            data = new ArrayList<HashMap<String, String>>();
            do {

                retrieve_ID = rs.getString(0);
                retrieve_Course = rs.getString(1);
                retrieve_Section = rs.getString(2);
                retrieve_Period = rs.getString(3);
                retrieve_Year = rs.getString(4);

                HashMap<String, String> datum = new HashMap<String, String>();
                datum.put("Course", retrieve_Course.toUpperCase() + retrieve_Year + retrieve_Section.toUpperCase() + retrieve_Period.toUpperCase());
                datum.put("ID", retrieve_ID);
                data.add(datum);
            } while (rs.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_view_record, new String[]{"ID", "Course"}, new int[]{
                    R.id.viewId, R.id.viewTitle //We use activity_view_record to show ID, Title and Content
            });
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    Toast.makeText(Activity_CheckQuiz_Undone.this, "LONG CLICK - UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();

                    return true;
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Toast.makeText(Activity_CheckQuiz_Undone.this, "UNDER CONSTRUCTION", Toast.LENGTH_SHORT).show();
                    //TextView getId = (TextView) view.findViewById(R.id.viewId);
                   // String toStringID = getId.getText().toString();
                    Intent intent = new Intent(getApplicationContext(), Activity_Undone_child.class);
                   // intent.putExtra("ID", toStringID);
                    startActivity(intent);
                }
            });
        }
    }

    public void fab_listener(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog dialog = new Dialog(Activity_CheckQuiz_Undone.this);
                dialog.setContentView(R.layout.check_quiz_section_list_dialog);
                dialog.setTitle("ADD SECTION");

                final EditText course = (EditText) dialog.findViewById(R.id.undone_course);
                final EditText section = (EditText) dialog.findViewById(R.id.undone_section);
                Button add = (Button) dialog.findViewById(R.id.undone_dialog_add);
                Button canc = (Button) dialog.findViewById(R.id.undone_dialog_cancel);

                final Spinner dropdown_year = (Spinner) dialog.findViewById(R.id.spinner_year);
                String[] items = new String[]{"Year", "1", "2","3","4","5","6","7","8","9","10","11","12"};
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Activity_CheckQuiz_Undone.this, android.R.layout.simple_spinner_dropdown_item, items);
                dropdown_year.setAdapter(adapter);

                final Spinner dropdown_exam_period = (Spinner) dialog.findViewById(R.id.spinner_exam_period);
                String[] exam_items = new String[]{"Exam Period", "PRELIM", "MIDTERM", "FINALS"};
                ArrayAdapter<String> dropdown_adapter = new ArrayAdapter<String>(Activity_CheckQuiz_Undone.this, android.R.layout.simple_spinner_dropdown_item, exam_items);
                dropdown_exam_period.setAdapter(dropdown_adapter);

                canc.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        getCourse = course.getText().toString();
                        getSection = section.getText().toString();
                        getYear = dropdown_year.getSelectedItem().toString();
                        getPeriod = dropdown_exam_period.getSelectedItem().toString();

                        if (getCourse.equals("") && getSection.equals("") && getYear.equals("Year") && getPeriod.equals("Exam Period")) {
                            Toast.makeText(Activity_CheckQuiz_Undone.this, "Please fill up all fields", Toast.LENGTH_LONG).show();
                        } else {
                            mydb.Insert_Section_List(mydb, getCourse, getSection, getPeriod, getYear);
                            Toast.makeText(Activity_CheckQuiz_Undone.this, "Successfully added", Toast.LENGTH_SHORT).show();
                            showSection();
                            //Intent intent = new Intent(getApplicationContext(), Activity_CheckQuiz_Undone.class);
                            //startActivity(intent);
                            dialog.dismiss();
                        }
                    }
                });dialog.show();
                //Snackbar.make(view,"Replace with your own action",Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
                //We make a listfolder for section containing all the studen quiz
            }
        });
    }

}
