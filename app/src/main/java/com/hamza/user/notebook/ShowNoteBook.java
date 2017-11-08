package com.hamza.user.notebook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ShowNoteBook extends AppCompatActivity {
    public DBHelper mydb;
    private ListView obj;
    private ListView lettersListView;
    private ArrayAdapter arrayAdapter;
    private ArrayAdapter arrayAdapterLettera;
    public int pos = 0;
    int i = 0;
    ArrayList array_list;
    ArrayList array_listTrad;
    String[] arrayLetters = new String[]{"%!","A", "B", "C", "D", "E", "F", "G", "E", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note_book);
        obj = (ListView) findViewById(R.id.listView1);
        lettersListView = (ListView) findViewById(R.id.listViewLetters);
        mydb = new DBHelper(this);
        arrayAdapterLettera = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayLetters);
        lettersListView.setAdapter(arrayAdapterLettera);
        array_list = mydb.getAllWords("word");
        array_listTrad = mydb.getAllWords("traduction");
        setView();


        obj.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mydb.deleteWord(array_list.get(position).toString());
                if (pos == 0) {
                    array_list = mydb.getAllWords("word");
                    array_listTrad = mydb.getAllWords("traduction");
                } else {
                    array_list = mydb.getWordsbyLetter(arrayLetters[position], "word");
                    array_listTrad = mydb.getWordsbyLetter(arrayLetters[position], "traduction");
                }
                setView();
                return false;
            }
        });
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myintent=new Intent(ShowNoteBook.this, ShowWord.class);
                myintent.putExtra("word", array_list.get(position).toString());
                myintent.putExtra("traduction", array_listTrad.get(position).toString());
                startActivity(myintent);
            }
        });

        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                setView();
            }
        });

    }

    public class MyListAdapterWord extends ArrayAdapter<String> {
        public MyListAdapterWord(ArrayList<String> arrayAdapter) {
            super(ShowNoteBook.this, R.layout.word_list, arrayAdapter);

        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View viewListWords = convertView;
            if (viewListWords == null) {
                viewListWords =getLayoutInflater().inflate(R.layout.word_list, parent, false);
            }
            TextView textViewWord = (TextView) viewListWords.findViewById(R.id.textViewWord);
            TextView textViewTraduction = (TextView) viewListWords.findViewById(R.id.textViewTraduzione);
            textViewWord.setText(array_list.get(position).toString());
            textViewTraduction.setText(array_listTrad.get(position).toString());
            if (position % 2 == 1) {
                viewListWords.setBackgroundColor(getColor(R.color.colorOneRow));
            } else {
                viewListWords.setBackgroundColor(getColor(R.color.colorSecondRow));
            }

            return viewListWords;

        }
    }


    public void addWord(View view){
        EditText editTextWord = (EditText) findViewById(R.id.plain_text_inputWord);
        EditText editTextTraduction = (EditText) findViewById(R.id.plain_text_inputTraduction);

        String contentWord = editTextWord.getText().toString();
        String contentTraduction = editTextTraduction.getText().toString();
        if(!contentWord.matches("") && !contentTraduction.matches("")) {
            if (mydb.insertWord(contentWord, contentTraduction)) {
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
            }
            setView();
            editTextTraduction.getText().clear();
            editTextWord.getText().clear();
        }
    }
    public void setView(){
        if(pos >0){
            array_list = mydb.getWordsbyLetter(arrayLetters[pos],"word");
            array_listTrad = mydb.getWordsbyLetter(arrayLetters[pos],"traduction" );}
        else{
            array_list = mydb.getAllWords("word");
            array_listTrad = mydb.getAllWords("traduction" );
        }
        arrayAdapter=new ShowNoteBook.MyListAdapterWord(array_list);
        obj.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPostResume() {
        setView();
        super.onPostResume();
    }
}

