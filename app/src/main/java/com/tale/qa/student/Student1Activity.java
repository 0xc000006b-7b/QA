package com.tale.qa.student;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;
import com.tale.qa.file_manager.ListFileActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Student1Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    String Fname;
    String setUserID, setUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student1);

        display_student_Quiz();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        setUser();



    }

    public void setUser(){
        //------------------------ Set User ----------------------//

            TextView kTitle = (TextView) findViewById(R.id.student_TextView);
            Intent intent = getIntent();
            Fname = intent.getStringExtra("ID");
            int parseID = Integer.parseInt(Fname);
            Cursor rs = mydb.Retrieve_Name(parseID);
            if (rs.getCount() != 0) {
                rs.moveToFirst();
                do {
                    setUserID = rs.getString(0);
                    setUserName = rs.getString(1);
                } while (rs.moveToNext());
            }
            kTitle.setText(setUserName);
        //-------------- WHEN RESUMED WILL GIVE AN ERROR SINCE Fname dont have a value
    }

    String editTitle = "", editContent = "";
    String disp_id;
    String editID;
    int listPosition;

    public void display_student_Quiz() {

        Cursor rs = mydb.Retrieve_Student_Quiz(); //------------ SELECT FROM DATABASEHELPER
        if (rs.getCount() != 0) {
            rs.moveToFirst();

            ArrayList<HashMap<String, String>> data;
            final ListView listview = (ListView) findViewById(R.id.listView2);
            data = new ArrayList<HashMap<String, String>>();
            do {
                String title = rs.getString(1);
                String date = rs.getString(2);
                disp_id = rs.getString(0);
                HashMap<String, String> datum = new HashMap<String, String>();
                datum.put("ID", disp_id);
                datum.put("Title", title.toUpperCase());
                datum.put("date_Created", date);
                data.add(datum);
            } while (rs.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_view_record, new String[] {"ID", "Title", "date_Created"}, new int[] {
                    R.id.viewId, R.id.viewTitle, R.id.viewContent //We use activity_view_record to show ID, Title and Content
            });
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView idTextView = (TextView) view.findViewById(R.id.viewId);
                    //get the text of the TextView
                    String parseID = idTextView.getText().toString();
                    listPosition = Integer.parseInt(parseID);

                    final Dialog dialog = new Dialog(Student1Activity.this);
                    dialog.setContentView(R.layout.student_quiz_list_dialog);
                    dialog.setTitle("ACTION");

                    //Button quiz_View = (Button) dialog.findViewById(R.id.student_quiz_view);
                    Button quiz_TakeQuiz = (Button) dialog.findViewById(R.id.student_quiz_take);
                    Button quiz_Share = (Button) dialog.findViewById(R.id.student_quiz_share);
                    Button quiz_Delete = (Button) dialog.findViewById(R.id.student_quiz_delete);


                    Cursor cursor = mydb.Retrieve_Student_Quiz_ID(listPosition);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        do {
                            editID = cursor.getString(0);
                            editTitle = cursor.getString(1);
                            editContent = cursor.getString(2);

                        } while (cursor.moveToNext());
                    }

                    quiz_Share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            //---------------------------- USE FOR SHARING OR USE THIRD PARTY APP TO SHARE ---------------//
                            Intent share = new Intent(android.content.Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                            //Add Data to the Intent

                            share.putExtra(Intent.EXTRA_SUBJECT, editTitle);
                            share.putExtra(Intent.EXTRA_TEXT, editContent);
                            startActivity(Intent.createChooser(share, "Send Quiz!"));

                        }
                    });
                    dialog.show();
                    //--------------------------------END ---------------------------------------//
                    //---------------------------LISTENER FOR VIEW ----------------------------- //

                    quiz_TakeQuiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Student_Take_Quiz();


                        }

                    });
                    dialog.show();
                    //------------------------------- END ----------------------------------------//
                    //-------------------------LISTENER FOR DELETE -------------------------------//
                    quiz_Delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            mydb.Delete_Student_Quiz(mydb, listPosition);
                            display_student_Quiz();

                        }

                    });
                    dialog.show();
                    //------------------------------- END ----------------------------------------//
                    return true;
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, final long viewId) {
                    TextView idTextView = (TextView) view.findViewById(R.id.viewId);
                    //get the text of the TextView
                    String parseID = idTextView.getText().toString();
                    listPosition = Integer.parseInt(parseID);

                    Cursor cursor = mydb.Retrieve_Student_Quiz_ID(listPosition);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        do {
                            editID = cursor.getString(0);
                            editTitle = cursor.getString(1);
                            editContent = cursor.getString(2);

                        } while (cursor.moveToNext());
                    }
                    Student_Take_Quiz();
;

                }
            });
                }
        }





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) {
            Toast.makeText(Student1Activity.this, Fname, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Student1Activity.this, ListFileActivity.class);
            //intent.putExtra("pass", setUserID);
            startActivity(intent);
            // Handle the camera action

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void Student_Take_Quiz()
    {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Student1Activity.this);
        builder.setTitle("Take the Quiz?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setMessage("Do you really wanna take a quiz right now?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(Student1Activity.this, "TEST: GOOD!", Toast.LENGTH_LONG).show();
                        Intent sendId = new Intent(getApplicationContext(), Activity_Student_onQuiz.class);
                        sendId.putExtra("id", editID);
                        sendId.putExtra("Fname", Fname);
                        startActivity(sendId);
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        android.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
