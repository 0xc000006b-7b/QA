package com.tale.qa.compose_quiz.question_tab;

import android.app.Dialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TextView;

import com.tale.qa.DatabaseHelper;
import com.tale.qa.File_support;
import com.tale.qa.R;
import com.tale.qa.compose_quiz.ktc_tab.Notepad_tab;
import com.tale.qa.web_support.Web_support;

import java.util.ArrayList;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class Question_tab_Activity extends AppCompatActivity {
    Button btn1, btn2, btn3;
    Context context = this;
    Intent modify_intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_tab_);
        display_notes();
        init();
    }

    public void init(){

        btn1 = (Button)findViewById(R.id.upQuestTab);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), Web_support.class);
                startActivity(intent);}
        });

        btn2 = (Button)findViewById(R.id.addfQuestTab);
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), File_support.class);
                startActivity(intent);
            }

        });

        btn3 = (Button)findViewById(R.id.createQuestTab);
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreateQuestionTab.class);
                startActivity(intent);
            }

        });

    }

    //------------- DISPLAY NOTES USING ListView ----------------//
    DatabaseHelper mydb = new DatabaseHelper(context);
    String editTitle, editContent, editID;
    int listPosition = 0;
    String disp_id;

    public void display_notes() {

        Cursor rs = mydb.Retrieve_Questions(); //------------ SELECT FROM DATABASEHELPER
        if (rs.getCount() != 0) {
            rs.moveToFirst();

            ArrayList<HashMap<String, String>> data;
            final ListView listview = (ListView) findViewById(R.id.listViewQuestions);
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
                    String parseID = idTextView.getText().toString();
                    listPosition = Integer.parseInt(parseID);

                    final Dialog dialog = new Dialog(Question_tab_Activity.this);
                    dialog.setContentView(R.layout.ktc_list_dialog);
                    dialog.setTitle("NOTES");

                    Button ktc_edit_btn = (Button) dialog.findViewById(R.id.ktc_Edit);
                    Button ktc_delete_btn = (Button) dialog.findViewById(R.id.ktc_delete);

                    ktc_edit_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            Cursor cursor = mydb.Edit_Questions(listPosition);

                            if (cursor.getCount() != 0) {
                                cursor.moveToFirst();

                                //store_class_data = new String[cursor.getCount()][1];
                                do {
                                    editID = cursor.getString(0);
                                    editTitle = cursor.getString(1);
                                    editContent = cursor.getString(2);

                                } while (cursor.moveToNext());

                                //Toast.makeText(Activity_Ktc_tab.this, viewId, Toast.LENGTH_LONG).show();
                                //Toast.makeText(Activity_Ktc_tab.this, editContent, Toast.LENGTH_LONG).show();

                                modify_intent = new Intent(getApplicationContext(), ModifyQuestionTab.class);
                                modify_intent.putExtra("id", editID);
                                modify_intent.putExtra("title", editTitle);
                               modify_intent.putExtra("content", editContent);
                                startActivity(modify_intent);
                            }


                        }
                    });
                    dialog.show();
                    ktc_delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            mydb.Delete_Questions(mydb, listPosition);

                            //Toast.makeText(Activity_Ktc_tab.this, listPosition, Toast.LENGTH_LONG).show();
                            display_notes();
                        }
                    });
                    dialog.show();

                    return true;
                }
            });

            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, final long viewId) {
                    //listPosition = position;
                    Cursor cursor = mydb.Edit_Questions(listPosition);

                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();

                        //store_class_data = new String[cursor.getCount()][1];
                        do {
                            editID = cursor.getString(0);
                            editTitle = cursor.getString(1);
                            editContent = cursor.getString(2);
                            ;
                        } while (cursor.moveToNext());

                        //Toast.makeText(Activity_Ktc_tab.this, viewId, Toast.LENGTH_LONG).show();
                        //Toast.makeText(Activity_Ktc_tab.this, editContent, Toast.LENGTH_LONG).show();

                        modify_intent = new Intent(getApplicationContext(), ModifyQuestionTab.class);
                        modify_intent.putExtra("id", editID);
                        modify_intent.putExtra("title", editTitle);
                        modify_intent.putExtra("content", editContent);
                        startActivity(modify_intent);
                    }


                }
            });

        }
    }

}


