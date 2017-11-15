//
// Main Activity, Lunch Acivity
// Showing buttons for import, export, search and add new word
// Showing listview with the the 4 word more searched
// /

package com.hamza.user.notebook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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


    public void buttonMain(View v) throws IOException {
        Intent myintent;
        switch (v.getId()){
            case R.id.buttonNote:
                myintent= new Intent(MainActivity.this, ShowNoteBook.class);
                startActivity(myintent);
                break;

            case R.id.buttonQuiz:
                if(mydb.numberOfRows()> 10){
                    myintent = new Intent(MainActivity.this, QuizGame.class);
                    startActivity(myintent);
                }
                else
                    Toast.makeText(getApplicationContext(), "At Least 10 words", Toast.LENGTH_SHORT).show();
                break;

            case R.id.buttonExport :
                buttonExp();
                break;

            case R.id.buttonImport:
                importFile();
                break;

            case R.id.buttonsEARCH:
                EditText editSearch = (EditText) findViewById(R.id.editTextSearch);
                myintent = new Intent(MainActivity.this, SearchResult.class);
                myintent.putExtra("search", editSearch.getText().toString().replace("'","''"));
                startActivity(myintent);
                break;

            case R.id.buttonAddWord:
                addWord();
                break;
        }
    }

    //Button Move to Import CSV
    public void importFile() throws FileNotFoundException {
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

    public void buttonExp() throws IOException {
        File directory = Environment.getExternalStorageDirectory();
        File file = new File(directory + "/download/notebook.csv");
        CSVFile csvFile = new CSVFile(file);
        csvFile.export(this);
        Toast.makeText(getApplicationContext(), "EXPORTED", Toast.LENGTH_SHORT).show();
    }

    public void addWord(){
        mydb = new DBHelper(this);

        String contentWord = editTextWord.getText().toString();
        String contentTraduction = editTextTraduction.getText().toString();
        if(!contentWord.matches("") && !contentTraduction.matches("")) {
            if (mydb.insertWord(contentWord, contentTraduction, 0)) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }
            editTextTraduction.getText().clear();
            editTextWord.getText().clear();
        }
        else
        Toast.makeText(getApplicationContext(), "Empty text", Toast.LENGTH_SHORT).show();
    }

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