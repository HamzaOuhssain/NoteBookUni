package com.hamza.user.notebook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class SearchResult extends AppCompatActivity {
    private ListView listSearch;
    private ArrayAdapter arrayAdapter;
    public DBHelper mydb;
    ArrayList array_list;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        mydb = new DBHelper(this);
        Bundle extras = getIntent().getExtras();
        String search = extras.getString("search");
        listSearch = (ListView) findViewById(R.id.listViewSearch);
        array_list = mydb.getWordsbyLetter(search, "word");
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,array_list );
        listSearch.setAdapter(arrayAdapter);


        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mydb.plusS(array_list.get(position).toString());
                Intent myintent=new Intent(SearchResult.this, ShowWord.class);
                myintent.putExtra("word", array_list.get(position).toString());
                myintent.putExtra("traduction", mydb.getTraductionFromWord(array_list.get(position).toString()).toString());
                startActivity(myintent);
            }
        });
    }
}
