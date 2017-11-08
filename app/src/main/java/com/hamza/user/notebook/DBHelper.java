package com.hamza.user.notebook;


import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String NOTEBOOK_TABLE_NAME = "notebook";
    public static final String NOTEBOOK_COLUMN_WORD = "word";
    public static final String NOTEBOOK_COLUMN_TRADUCTION = "traduction";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    //Create Table Notebook if doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table notebook "+
                        "(word text primary key,traduction text)"
        );
    }
    //DROP table if already exists
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS notebook");
        onCreate(db);
    }


    // Inserti a new Word with its traduction in the Database if doesn't exist
    public boolean insertWord (String name, String traduction) {
        if(!Exists(name)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOTEBOOK_COLUMN_WORD, name);
            contentValues.put(NOTEBOOK_COLUMN_TRADUCTION, traduction);
            db.insert("notebook", null, contentValues);
            return true;
        }
        return false;
    }

    //Get Back a Random variable Word or Traduction
    public String randomVariable(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor s = db.rawQuery("SELECT * FROM notebook ORDER BY RANDOM() LIMIT 1", null);
        s.moveToFirst();
        return s.getString(s.getColumnIndex(a));
    }

    //Number of element in the DataBase
    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, NOTEBOOK_TABLE_NAME);
        return numRows;
    }

    //Update a word and its traduction
    public boolean updateContat (String word, String traduction, String lastWord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEBOOK_COLUMN_WORD, word);
        contentValues.put(NOTEBOOK_COLUMN_TRADUCTION, traduction);
        deleteWord(lastWord);
        return insertWord(word, traduction);


    }

    // Delete Determinate Word
    public Integer deleteWord (String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notebook",
                "word = ? ",
                new String[] { word });
    }

    // Get all the Words there are in the DataBase
    public ArrayList<String> getAllWords(String b) {
        ArrayList<String> array_list = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook ORDER BY word", null );
        res.moveToFirst();
        return getArray(res,array_list, b);
    }

    //Get list of word that start with a determinate letter
    public ArrayList<String> getWordsbyLetter(String letter, String b) {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+letter+"%' ORDER BY word", null);
        res.moveToFirst();
        return getArray(res,array_list, b);

    }

    // get from Cursor the array of element i want
    private ArrayList<String> getArray(Cursor res,ArrayList<String> array_list, String variable){
        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(variable)));
            res.moveToNext();
        }
        return array_list;
    }

    /*public String getWord(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+a+" ORDER BY word", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex("word"));
    }*/

    //Get the traduction associated with the words
    public String getTraductionFromWord(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+a+"' ORDER BY word", null);
        res.moveToFirst();
        return res.getString(res.getColumnIndex("traduction"));
    }
    //Check is the Word is already in the DataBase
    public boolean Exists(String _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return DatabaseUtils.longForQuery(db, "select count(*) from " + NOTEBOOK_TABLE_NAME + " where word=?"+" limit 1", new String[] {"1"}) > 0;
    }
}


