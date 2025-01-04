package com.tale.qa;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.sql.SQLException;

/**
 * Created by 0xc000007b on 11/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static String DB_NAME = "ClassQuiz.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;


    public DatabaseHelper(Context context){
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    public void openDataBase() throws SQLException {
        myDataBase = SQLiteDatabase.openDatabase(DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();
    }
        @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS Quiz_Questions(_id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Content TEXT, date_Created TEXT);");
        db.execSQL("CREATE TABLE IF NOT EXISTS Key_to_Correction(_id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Content TEXT, date_Created TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Student_Info(_id INTEGER PRIMARY KEY AUTOINCREMENT, Fname TEXT, class_Id INTEGER, Gender TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Student_Quiz(_id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Content TEXT, date_Created TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Student_Ans(_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Answer TEXT, Date TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Section_List(_id INTEGER PRIMARY KEY AUTOINCREMENT, Course TEXT, Section TEXT, exam_Period TEXT, Year TEXT);");
            db.execSQL("CREATE TABLE IF NOT EXISTS Student_QuizList(_id INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Answer TEXT, date_Created TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS Quiz_Questions");
        db.execSQL("DROP TABLE IF EXISTS Key_to_Correction");
        db.execSQL("DROP TABLE IF EXISTS Student_Info");
        db.execSQL("DROP TABLE IF EXISTS Student_Quiz");
        db.execSQL("DROP TABLE IF EXISTS Student_Ans");
        db.execSQL("DROP TABLE IF EXISTS Student_QuizList");
        onCreate(db);
    }

    //------------------------------- HANDLE'S STUDENT QUIZLIST ----------------------------------//

    public void Insert_QuizList(DatabaseHelper dataH, String name, String answer, String date)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Answer", answer);
        cv.put("date_Created", date);
        //cv.put("SectionID", sectionID);
        SQ.insert("Student_QuizList", null, cv);
    }

    public Cursor Retrieve_Answer(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select Answer, Name from Student_QuizList where _id =" + id, null);
        return res;
    }

    public Cursor Retrieve_QuizList()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Name, Answer from Student_QuizList", null);
        return res;
    }

    public void Delete_QuizList(DatabaseHelper db, int id)
    {
        SQLiteDatabase SQ = db.getWritableDatabase();
        SQ.delete("Student_QuizList"," _id = '"+id+"'",null);
    }




    //------------------------------- HANDLE's SECTION LIST --------------------------------------//

    public void Insert_Section_List(DatabaseHelper dataH, String course, String section, String period, String year)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Course", course);
        cv.put("Section", section);
        cv.put("exam_Period", period);
        cv.put("Year", year);
        SQ.insert("Section_List", null, cv);
    }

    public Cursor Retrieve_Section()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Section_List", null);
        return res;
    }



    //------------------------------- HANDLE'S STUDENT ANSWER ------------------------------------//

    public void Insert_Student_Answ(DatabaseHelper dataH, String Name, String Answer, String Date)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", Name);
        cv.put("Answer", Answer);
        cv.put("Date", Date);
        SQ.insert("Student_Ans", null, cv);
    }


    //------------------------------- STUDENT QUIZ QUESTION'S ------------------------------------//

    public void Insert_Student_Quiz(DatabaseHelper dataH,String title,String content,String date)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", title);
        cv.put("Content", content);
        cv.put("date_Created", date);
        SQ.insert("Student_Quiz", null, cv);
    }

    public Cursor Retrieve_Student_Quiz()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, date_Created from Student_Quiz", null);
        return res;
    }

    public Cursor Retrieve_Student_Quiz_ID(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, Content from Student_Quiz where _id = " + id, null);
        return res;
    }

    public void Delete_Student_Quiz(DatabaseHelper db, int id)
    {
        SQLiteDatabase SQ = db.getWritableDatabase();
        SQ.delete("Student_Quiz", " _id = '" + id + "'", null);
    }

    //------------------------------- STUDENT INFO's -------------------------------------------//

    public void Insert_Student(DatabaseHelper dataH, String fname, int class_id, String gender)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Fname", fname);
        cv.put("class_Id", class_id);
        cv.put("Gender", gender);
        SQ.insert("Student_Info", null, cv);
    }

    public Cursor Retrieve_Student(int class_id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, class_Id, Fname from Student_Info where class_Id = " + class_id, null);
        return res;
    }

    public Cursor Retrieve_Name(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Fname from Student_Info where _id = " + id, null);
        return res;
    }


    //------------------------------------KEY TO CORRECTION------------------------------------//

    public void Insert_KTC(DatabaseHelper dataH,String title,String content,String date)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title",title);
        cv.put("Content", content);
        cv.put("date_Created", date);
        SQ.insert("Key_to_Correction", null, cv);
    }

    public void Update_KTC(DatabaseHelper dataH,String date, String title, String content, int id)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", title);
        cv.put("Content", content);
        cv.put("date_Created", date);
        SQ.update("Key_to_Correction", cv, "_id = '" + id + "'", null);
    }

    public Cursor Retrieve_KTC()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, date_Created from Key_to_Correction", null);
        return res;
    }

    public Cursor Edit_KTC(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, Content from Key_to_Correction where _id = " + id, null);
        return res;
    }

    public Cursor Retrieve_ID(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id from Key_to_Correction where _id = " + id, null);
        return res;
    }

    public void Delete_KTC(DatabaseHelper db, int id)
    {
        SQLiteDatabase SQ = db.getWritableDatabase();
        SQ.delete("Key_to_Correction","_id = '"+id+"'",null);
    }

    public Cursor Retrieve_InSpinner()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title from Key_to_Correction", null);
        return res;
    }

    //-------------------------HANDLES QUESTION TAB -----------------------------------------//

    public void Insert_Questions(DatabaseHelper dataH,String title,String content,String date)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title",title);
        cv.put("Content", content);
        cv.put("date_Created", date);
        SQ.insert("Quiz_Questions", null, cv);
    }

    public void Update_Questions(DatabaseHelper dataH,String date, String title, String content, int id)
    {
        SQLiteDatabase SQ = dataH.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Title", title);
        cv.put("Content", content);
        cv.put("date_Created", date);
        SQ.update("Quiz_Questions", cv, "_id = '" + id + "'", null);
    }

    public Cursor Retrieve_Questions()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, date_Created from Quiz_Questions", null);
        return res;
    }

    public Cursor Edit_Questions(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id, Title, Content from Quiz_Questions where _id = " + id, null);
        return res;
    }

    public void Delete_Questions(DatabaseHelper db, int id)
    {
        SQLiteDatabase SQ = db.getWritableDatabase();
        SQ.delete("Quiz_Questions","_id = '"+id+"'",null);
    }

    public Cursor Retrieve_QID(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select _id from Quiz_Questions where _id = " + id, null);
        return res;
    }


}
