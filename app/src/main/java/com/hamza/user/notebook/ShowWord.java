package com.hamza.user.notebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShowWord extends AppCompatActivity {
    EditText editTextEnglish;
    EditText editTextFrench;
    String newStringWord;
    String newStringTraduction;
    public DBHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        mydb = new DBHelper(this);




        if (savedInstanceState == null) {                           //Getting info from activity
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newStringWord= "";
                newStringTraduction = "";
            } else {
                newStringWord= extras.getString("word");
                newStringTraduction = extras.getString("traduction");
            }
        } else {
            newStringWord = (String) savedInstanceState.getSerializable("word");
            newStringTraduction = (String) savedInstanceState.getSerializable("traduction");
        }

        editTextEnglish = (EditText) findViewById(R.id.textViewEnglish);
        editTextFrench = (EditText) findViewById(R.id.textViewFrench);
        editTextEnglish.setText(newStringWord);
        editTextFrench.setText(newStringTraduction);
    }

    public void uploadButton(View view){
        String contentWord = editTextEnglish.getText().toString();
        String contentTraduction = editTextFrench.getText().toString();
        if(!contentWord.matches("") && !contentTraduction.matches("")) {
            if (mydb.updateContat(contentWord, contentTraduction, newStringWord)) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
