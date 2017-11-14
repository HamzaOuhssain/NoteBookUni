package com.hamza.user.notebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList array_list;
    ListView listStory;
    EditText editTextTraduction;
    EditText editTextWord;
    public DBHelper mydb;
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.VIBRATE,
            Manifest.permission.RECORD_AUDIO,
    };
    PrintWriter printWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        editTextWord = (EditText) findViewById(R.id.plain_text_inputWord);
        editTextTraduction = (EditText) findViewById(R.id.plain_text_inputTraduction);
        populateListView();


        listStory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mydb.plusS(array_list.get(position).toString());
                Intent myintent = new Intent(MainActivity.this, ShowWord.class);
                myintent.putExtra("word", array_list.get(position).toString());
                myintent.putExtra("traduction", mydb.getTraductionFromWord(array_list.get(position).toString()).toString());
                startActivity(myintent);
            }
        });
    }

    private void populateListView() {
        listStory = (ListView) findViewById((R.id.listViewStory));
        mydb = new DBHelper(this);
        array_list = mydb.getStory();
        ArrayList array_listShow = new ArrayList();
        for (Object i : array_list) {
            array_listShow.add(i.toString() + "--->" + mydb.getTraductionFromWord(i.toString()));
        }
        if (!(array_list.size() == 0)) {
            if (array_list.size() > 4) {
                array_listShow.subList(4, array_listShow.size()).clear();

            }
            ArrayAdapter arrayAdapterLettera;
            arrayAdapterLettera = new ArrayAdapter(this, android.R.layout.simple_list_item_1, array_listShow);
            listStory.setAdapter(arrayAdapterLettera);
        }

    }

    //Button Move from MainActivity to Notebook!
    public void moveNoteBook(View view) {
        Intent myintent = new Intent(MainActivity.this, ShowNoteBook.class);
        startActivity(myintent);
    }

    //Button Move from MainActivity to QUIZ!
    public void buttonQuiz(View view) {
        Intent myintent = new Intent(MainActivity.this, QuizGame.class);
        startActivity(myintent);
    }

    public void buttonSearch(View view) {
        EditText editSearch = (EditText) findViewById(R.id.editTextSearch);
        Intent myintent = new Intent(MainActivity.this, SearchResult.class);
        myintent.putExtra("search", editSearch.getText().toString());
        startActivity(myintent);
    }


    //Button Move to Import CSV
    public void buttonImport(View view) throws FileNotFoundException {
        File directory = Environment.getExternalStorageDirectory();
        File file = new File(directory + "/download/notebook.csv");
        if (!file.exists()) {
            throw new RuntimeException("File not found");
        }
        Log.e("Testing", "Startiing to Read");
        CSVFile csvFile = new CSVFile(file);
        csvFile.read(this);
        Toast.makeText(getApplicationContext(), "IMPORTED", Toast.LENGTH_SHORT).show();

    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    public boolean buttonExport(View v) throws IOException {
        mydb = new DBHelper(this);
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        } else {
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try {
                file = new File(exportDir, "notebookdatabase.csv");
                printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println("ORIGINAL WORD,TRANSLATED WORD");
                ArrayList<String> words = mydb.getAllWords("word");
                ArrayList<String> traductions = mydb.getAllWords("traduction");
                int i = 0;
                for (String b : words) {
                    String data[] = {b.toString(), traductions.get(i).toString()};
                    printWriter.println(data[0]+","+data[1]);
                    i++;
                }

            } catch (Exception exc) {
                debug(exc.toString());
                return false;
            } finally {
                if (printWriter != null)
                    printWriter.close();
                MediaScannerConnection.scanFile(MainActivity.this, new String[] { "notebookdatabase.csv" }, null, null);
            }
            return true;
        }
    }


    public void debug(Object obj){
        Log.d("DEBUG", obj.toString());
    }

/*
*       String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return false;
        } else {
            File exportDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }
            File file;
            PrintWriter printWriter = null;
            try {
                file = new File(exportDir, "NotebookDatabase.csv");
                printWriter = new PrintWriter(new FileWriter(file));
                printWriter.println("ORIGINAL WORD,TRANSLATED WORD");
                if (cursor.getCount() < 1)
                        return false;
                ArrayList<String> words =  mydb.getAllWords("word");
                ArrayList<String> traductions =  mydb.getAllWords("traduction");
                int i=0;
                for(String b: words){
                String data[] = {words.get(i).toString(), traductions.get(i).toString()};
                printWriter.println(record);
                i++;
            }

                }
            } catch (Exception exc) {
                debug(exc.toString());
                return false;
            } finally {
                if (printWriter != null) printWriter.close();
            }
            return true;
        }

* */
        //CSVFile csvFile = new CSVFile(inputStream);
        //csvFile.export();
    public void addWord(View view){
        mydb = new DBHelper(this);

        String contentWord = editTextWord.getText().toString();
        String contentTraduction = editTextTraduction.getText().toString();
        if(!contentWord.matches("") && !contentTraduction.matches("")) {
            if (mydb.insertWord(contentWord, contentTraduction)) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }
            editTextTraduction.getText().clear();
            editTextWord.getText().clear();
        }
    }
  /*  public void translateButton(View v){
        // TODO Auto-generated method stub
        String InputString;
        String OutputString = null;
        InputString = editTextWord.getText().toString();

        try {
            Transl.setHttpReferrer("http://android-er.blogspot.com/");
            OutputString = Translate.execute(InputString,
                    Language.ENGLISH, Language.FRENCH);
        } catch (Exception ex) {
            ex.printStackTrace();
            OutputString = "Error";
        }

        MyOutputText.setText(OutputString);

    }

}*/


    @Override
    protected void onPostResume() {
        super.onPostResume();
        populateListView();
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