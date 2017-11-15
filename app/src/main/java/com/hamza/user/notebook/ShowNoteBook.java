//
// Show all the words from the Database and possibility to select only words start with a letter
// Possibility to lissen the prununciation of the word
// /


package com.hamza.user.notebook;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;


public class ShowNoteBook extends AppCompatActivity {
    TextToSpeech ttobj;
    public DBHelper mydb;
    private ListView obj;
    public int pos = 0;
    String [] arrayMenu = {"delete", "modify"};
    ArrayList array_list;
    ArrayList array_listTrad;
    String[] arrayLetters = new String[]{"%!","A", "B", "C", "D", "E", "F", "G", "H","I",
            "J","K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V","W","X" ,"Y","Z"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_note_book);
        obj = (ListView) findViewById(R.id.listView1);

        ListView lettersListView = (ListView) findViewById(R.id.listViewLetters);
        mydb = new DBHelper(this);
        ArrayAdapter arrayAdapterLettera = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayLetters);
        lettersListView.setAdapter(arrayAdapterLettera);
        array_list = mydb.getAllWords("word");
        array_listTrad = mydb.getAllWords("traduction");
        registerForContextMenu(obj);

        setView();

        lettersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;
                setView();
            }
        });

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView1) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(array_list.get(info.position).toString());
            for (int i = 0; i<arrayMenu.length; i++) {
                menu.add(Menu.NONE, i, i, arrayMenu[i]);
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String menuItemName = arrayMenu[menuItemIndex];
        if(menuItemName.equalsIgnoreCase("delete")){
            mydb.deleteWord(array_list.get(info.position).toString());
            if (pos == 0) {
                array_list = mydb.getAllWords("word");
                array_listTrad = mydb.getAllWords("traduction");
            } else {
                array_list = mydb.getWordsbyLetter(arrayLetters[pos], "word");
                array_listTrad = mydb.getWordsbyLetter(arrayLetters[pos], "traduction");
            }
            setView();
        }
        else{
            Intent myintent=new Intent(ShowNoteBook.this, ShowWord.class);
            myintent.putExtra("word", array_list.get(info.position).toString());
            myintent.putExtra("traduction", array_listTrad.get(info.position).toString());
            startActivity(myintent);
        }


        return true;
    }

    //Class adapter for the listview that show all the words with traduction and button pron
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
            Button buttonSp = (Button) viewListWords.findViewById(R.id.buttonSp);
            textViewWord.setText(array_list.get(position).toString().replace("''","'"));
            textViewTraduction.setText(array_listTrad.get(position).toString().replace("''","'"));
            if (position % 2 == 1) {
                viewListWords.setBackgroundColor(getColor(R.color.colorOneRow));
            } else {
                viewListWords.setBackgroundColor(getColor(R.color.colorSecondRow));
            }
            buttonSp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ttobj=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            ttobj.setLanguage(Locale.FRENCH);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                ttobj.speak(array_listTrad.get(position).toString(),TextToSpeech.QUEUE_FLUSH,null,null);
                            } else {
                                ttobj.speak(array_listTrad.get(position).toString(), TextToSpeech.QUEUE_FLUSH, null);
                            }

                        }
                    });

                }
            });


            return viewListWords;

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
        ArrayAdapter arrayAdapter=new ShowNoteBook.MyListAdapterWord(array_list);
        obj.setAdapter(arrayAdapter);
    }

    @Override
    protected void onPostResume() {
        setView();
        super.onPostResume();
    }
}

