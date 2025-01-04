package com.tale.qa.check_quiz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.R;
import com.tale.qa.file_manager.Activity_CheckQuiz_ListFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Activity_Undone_child extends AppCompatActivity {
    Context context = this;
    DatabaseHelper mydb = new DatabaseHelper(context);
    Button add_QuizFile;
    String[] arr = new String[]{""};
    int array_ID[];
    int getID;
    int array_getItemID[];
    String change;
    String IntentID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_undone_child);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       populateListView();
        getFile();
        populateSpinner();

        //Intent intent = getIntent();
        //IntentID = intent.getStringExtra("ID");

        final Spinner spinner_KTC = (Spinner)findViewById(R.id.display_KTC);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Activity_Undone_child.this, android.R.layout.simple_spinner_dropdown_item, arr);
        spinner_KTC.setAdapter(adapter2);
        spinner_KTC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, android.view.View v, int position, long id) {

                if (position == 0) {
                    change = "-- Select KTC --";
                } else {
                    change = "spinnerchange";
                    Toast.makeText(getApplicationContext(), arr[position], Toast.LENGTH_LONG).show();
                    getID = array_ID[position];
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    public void populateSpinner(){

        Cursor rs = mydb.Retrieve_InSpinner();
        if (rs.getCount() != 0) {
            rs.moveToFirst();
            arr = new String[rs.getCount() + 1];                     //ARRAY INSIDE SPINNER
            array_ID = new int[rs.getCount() + 1];
            arr[0] = "-- Select KTC --";
            array_ID[0] = 0;
            int num = 1;
            do {
                //Toast.makeText(Login.this,rs.getString(0),Toast.LENGTH_LONG).show();
                String Title = rs.getString(1);
                arr[num] = (Title.toUpperCase());
                array_ID[num] = Integer.parseInt(rs.getString(0));
                num++;
            } while (rs.moveToNext());
        }
    }

    public void getFile(){
        add_QuizFile = (Button) findViewById(R.id.add_StudentQuiz);
        add_QuizFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), IntentID, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), Activity_CheckQuiz_ListFile.class);
                //intent.putExtra("SectionID", IntentID);
                startActivity(intent);
            }
        });
    }

    String disp_id;
    int listPos;
    int countItemID = 0;
    int arrayItemID = 1;

    public void populateListView() {

       // int convId = Integer.parseInt(IntentID);

        Cursor rs = mydb.Retrieve_QuizList(); //------------ SELECT FROM DATABASEHELPER
        if (rs.getCount() != 0) {
            rs.moveToFirst();
            array_getItemID = new int[rs.getCount() + 1];
            array_getItemID[0] = 0;

            ArrayList<HashMap<String, String>> data;
            final ListView listview = (ListView) findViewById(R.id.listView3);
            data = new ArrayList<HashMap<String, String>>();
            do {
                String title = rs.getString(1);
                String date = rs.getString(2);
                disp_id = rs.getString(0);
                array_getItemID[arrayItemID] = Integer.parseInt(rs.getString(0)); // We insert all the id
                HashMap<String, String> datum = new HashMap<String, String>();
                datum.put("ID", disp_id);
                datum.put("Title", title.toUpperCase());
                datum.put("date_Created", date);
                data.add(datum);
                countItemID++;
                arrayItemID++;
            } while (rs.moveToNext());

            SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.activity_view_record, new String[]{"ID", "Title"}, new int[]{
                    R.id.viewId, R.id.viewTitle //We use activity_view_record to show ID, Title and Content
            });
            adapter.notifyDataSetChanged();
            listview.setAdapter(adapter);

            listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                    TextView intoID = (TextView) view.findViewById(R.id.viewId);
                    String temp = intoID.getText().toString();
                    listPos = Integer.parseInt(temp);

                    final Dialog dialog = new Dialog(Activity_Undone_child.this);
                    dialog.setContentView(R.layout.dialog_showaction_quizlist);
                    dialog.setTitle("ACTION");

                    Button view_Quiz = (Button) dialog.findViewById(R.id.quiz_list_view);
                    Button delete_Quiz = (Button) dialog.findViewById(R.id.quiz_list_delete);

                    view_Quiz.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();

                            Dialog dialog_View = new Dialog(Activity_Undone_child.this);
                            dialog_View.setContentView(R.layout.dialog_view_quiz);
                            dialog_View.setTitle("View");

                            TextView view_Content = (TextView) dialog_View.findViewById(R.id.show_Content);
                            String Answer = "";
                            Cursor rs = mydb.Retrieve_Answer(listPos);
                            if (rs.getCount() != 0) {
                                rs.moveToFirst();

                                do {
                                    Answer = rs.getString(0);
                                } while (rs.moveToNext());
                            }
                                view_Content.setText(Answer);
                            dialog_View.show();
                            }

                        });dialog.show();

                        delete_Quiz.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View view){
                            dialog.dismiss();
                            mydb.Delete_QuizList(mydb,listPos);
                            populateListView();
                        }
                        });dialog.show();

                    return true;
                }
            });

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        TextView intoID = (TextView) view.findViewById(R.id.viewId);
                        String temp = intoID.getText().toString();
                        listPos = Integer.parseInt(temp);
                        Dialog dialog_View = new Dialog(Activity_Undone_child.this);
                        dialog_View.setContentView(R.layout.dialog_view_quiz);
                        dialog_View.setTitle("View");

                        TextView view_Content = (TextView) dialog_View.findViewById(R.id.show_Content);
                        String Answer = "";
                        Cursor rs = mydb.Retrieve_Answer(listPos);
                        if (rs.getCount() != 0) {
                            rs.moveToFirst();

                            do {
                                Answer = rs.getString(0);
                            } while (rs.moveToNext());
                        }
                        view_Content.setText(Answer);
                        dialog_View.show();
                    }
                });

            }
        }

    String getContent;
    String[] getAnswer;
    String[] getNames;
    public void startChecking(){
        if(arr[0].equals(change)){
            //check if user choose nothing
            Toast.makeText(getApplicationContext(), "Oops, choose some KtC", Toast.LENGTH_LONG).show();
        }
        else
        {
            //check chooses something then we will its spinner ID
            getAnswer = new String[0];
            getNames = new String[0];
            Cursor rs = mydb.Edit_KTC(getID);
            if(rs.getCount() != 0){
                rs.moveToFirst();
                do {
                        getContent = rs.getString(2);
                }while(rs.moveToNext());
            }
            //String convert = String.valueOf(getID);
            //Toast.makeText(getApplicationContext(), convert, Toast.LENGTH_LONG).show();
            int count2 = 0;
            for(int i = 1; i<=countItemID; i++) {
                int count=0;
                String convert = String.valueOf(array_getItemID[i]);
                Toast.makeText(getApplicationContext(), convert, Toast.LENGTH_SHORT).show();
                //iteration, using this, we can get all the answer and put into the array
                rs = mydb.Retrieve_Answer(array_getItemID[i]);
                if(rs.getCount() != 0){
                    rs.moveToFirst();
                    getAnswer = new String[rs.getCount()];
                    getNames = new String[rs.getCount()];

                    do{
                        getNames[count] = rs.getString(1);
                        getAnswer[count] = rs.getString(0);
                        count++;
                    }while (rs.moveToNext());
                }
                //start comparing *REMINDER: StringTokenizer*
                //int countCheck = 0;
                String getAns = getAnswer[count2];

                Toast.makeText(getApplicationContext(), getAns, Toast.LENGTH_LONG).show();
                int countCorrect = 0;
                /*for(int j = 0, k = 0; j<getContent.length() && k<getAns.length();j++, k++)
                {

                    if(Character.toLowerCase(getContent.charAt(j)) == Character.toLowerCase(getAns.charAt(k))){
                       if(getContent.charAt(j) != ' '){
                           countCorrect+=1;
                        }
                       else if(getAns.charAt(k) == ' '){countCorrect+=0;}
                        else{
                           countCorrect+=0;
                       }

                    }
                    else{countCorrect+=0;}
                } */
                // We Use StringTokenizer to split the string into word
                StringTokenizer theContent = new StringTokenizer(getContent);
                StringTokenizer theAnswer = new StringTokenizer(getAns);

                while(theContent.hasMoreTokens() && theAnswer.hasMoreTokens())
                {
                    if(theContent.nextToken().equals(theAnswer.nextToken()))
                    {
                        countCorrect++;

                    }
                    else
                    {
                        countCorrect+=0;
                    }

                }
                    Toast.makeText(getApplicationContext(), getNames[count2]+" = "+countCorrect, Toast.LENGTH_LONG).show();


            }

        }
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.check_quiz, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_check) {
            startChecking();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
