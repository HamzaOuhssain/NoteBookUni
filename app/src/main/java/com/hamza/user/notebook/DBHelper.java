//
// DATABASE HELPER
// /

package com.hamza.user.notebook;


import java.util.ArrayList;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String NOTEBOOK_TABLE_NAME = "notebook";
    private static final String NOTEBOOK_COLUMN_WORD = "word";
    private static final String NOTEBOOK_COLUMN_TRADUCTION = "traduction";
    private static final String NOTEBOOK_COLUMN_TOTR = "totr";

    DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }


    //Create Table Notebook if doesn't exist
    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table notebook "+
                        "(word text primary key,traduction text, totr number)"
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
    boolean insertWord (String name, String traduction, int n) {
        name = name.replace("'","''");
        traduction = traduction.replace("'","''");
        if(!checkIsDataAlreadyInDBorNot(name)) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(NOTEBOOK_COLUMN_WORD, name);
            contentValues.put(NOTEBOOK_COLUMN_TRADUCTION, traduction);
            contentValues.put(NOTEBOOK_COLUMN_TOTR, n);
            db.insert("notebook", null, contentValues);
            return true;
        }
        else
            return false;
    }
        //Checking if already exist a word
    boolean checkIsDataAlreadyInDBorNot(String dbfield) {
            dbfield = dbfield.replace("'","''");
            SQLiteDatabase db = this.getReadableDatabase();
            String Query = "select * from notebook where word like '"+dbfield+"' ORDER BY word";
            Cursor cursor = db.rawQuery(Query, null);
            if (cursor.getCount() <= 0) {
                cursor.close();
                return false;
            }
            cursor.close();
            return true;
        }

    //Get Back a Random variable Word or Traduction
     String randomVariable(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor s = db.rawQuery("SELECT * FROM notebook ORDER BY RANDOM() LIMIT 1", null);
        s.moveToFirst();
        String ret = s.getString(s.getColumnIndex(a)).replace("'","''");
        s.close();
        return ret;
    }

    //Number of element in the DataBase
    int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, NOTEBOOK_TABLE_NAME);
    }

    //Plus 1 for searching
    void plusS(String word){
        SQLiteDatabase db = this.getReadableDatabase();
        int a = getNumRWord(word) +1;
        db.execSQL( "UPDATE " + NOTEBOOK_TABLE_NAME + " SET " + NOTEBOOK_COLUMN_TOTR + "="+a+" WHERE " +  NOTEBOOK_COLUMN_WORD + " like '"+word+"'");
    }

    //Get the words order by number of search
    ArrayList getStory(){
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook WHERE totr > 0 ORDER BY totr DESC", null );
        res.moveToFirst();
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex("word")));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    //Update a word and its traduction
    boolean updateContat (String word, String traduction, String lastWord) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(NOTEBOOK_COLUMN_WORD, word);
        contentValues.put(NOTEBOOK_COLUMN_TRADUCTION, traduction);
        int a = getNumRWord(lastWord);
        contentValues.put(NOTEBOOK_COLUMN_TOTR, a);
        deleteWord(lastWord);
        return insertWord(word, traduction, a);


    }

    // Delete Determinate Word
    Integer deleteWord (String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("notebook",
                "word = ? ",
                new String[] { word });
    }

    // Get all the Words there are in the DataBase
    ArrayList<String> getAllWords(String b) {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook ORDER BY word", null );
        res.moveToFirst();
        return getArray(res,array_list, b);
    }

    //Get list of word that start with a determinate letter
    ArrayList<String> getWordsbyLetter(String letter, String b) {
        letter.replace("'","''");
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+letter+"%' ORDER BY word", null);
        res.moveToFirst();
        return getArray(res,array_list, b);

    }

    // get from Cursor the array of element i want
    private ArrayList<String> getArray(Cursor res,ArrayList<String> array_list, String variable){
        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(variable)));
            res.moveToNext();
        }
        return array_list;
    }

    //Get the traduction associated with the words
    String getTraductionFromWord(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+a+"' ORDER BY word", null);
        res.moveToFirst();
        String ret = res.getString(res.getColumnIndex("traduction")).replace("'","''");
        res.close();
        return ret;
    }
    //Get number how many times a word was searched
    int getNumRWord(String a){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from notebook where word like '"+a+"' ORDER BY word", null);
        res.moveToFirst();
        int ret = res.getInt(res.getColumnIndex(NOTEBOOK_COLUMN_TOTR));
        res.close();
        return ret;
    }

    void deleteAllElement(){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from "+ NOTEBOOK_TABLE_NAME);
    }
}


