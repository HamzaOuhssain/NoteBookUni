package com.hamza.user.notebook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.opencsv.CSVWriter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    InputStream inputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //Button Move from MainActivity to Notebook!
    public void moveNoteBook(View view){
        Intent myintent=new Intent(MainActivity.this, ShowNoteBook.class);
        startActivity(myintent);
    }

    //Button Move from MainActivity to QUIZ!
    public void buttonQuiz(View view){
        Intent myintent=new Intent(MainActivity.this, QuizGame.class);
        startActivity(myintent);
    }
    public void buttonSearch(View view){
        EditText editSearch = (EditText) findViewById(R.id.editTextSearch);
        Intent myintent=new Intent(MainActivity.this, SearchResult.class);
        myintent.putExtra("search", editSearch.getText().toString());
        startActivity(myintent);
    }


    //Button Move to Import CSV
    public void buttonImport(View view) throws FileNotFoundException {

        String fileName = "notebook.csv";
        File directori = Environment.getExternalStorageDirectory();
        File file = new File(directori, "notebook.csv");
        //String path = yourAndroidURI.uri.getPath() // "file:///mnt/sdcard/FileName.mp3"
        //String path = Environment.getExternalStorageDirectory()+"/"+fileName;
       /* File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);
        CSVFile csvFile = new CSVFile(inputStream);
        List scoreList = csvFile.read(this);*/

       //The File CSV is saved in the folder Raw inside the app
         //inputStream = getResources().openRawResource(R.raw.notebook);
        CSVFile csvFile = new CSVFile(file);
        csvFile.read(this);
        Toast.makeText(getApplicationContext(), "IMPORTED", Toast.LENGTH_SHORT).show();
    }

    public void buttonExport(View v) throws IOException {
        //CSVFile csvFile = new CSVFile(inputStream);
        //csvFile.export();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        return true;
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }
}