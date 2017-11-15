package com.hamza.user.notebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ShowResultQuiz extends AppCompatActivity {
    private ListView obj;
    private ArrayList<String> words;
    private ArrayList<String> answers;
    public DBHelper mydb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_result_quiz);
        Bundle extras = getIntent().getExtras();
        words = extras.getStringArrayList("arrayWords");
        answers = extras.getStringArrayList("arrayAnswers");
        mydb = new DBHelper(this);
        ArrayAdapter arrayAdapter = new ShowResultQuiz.MyListAdapterWordResult(words);
        obj = (ListView) findViewById(R.id.ListViewResQuiz);
        obj.setAdapter(arrayAdapter);
    }

    private class MyListAdapterWordResult extends ArrayAdapter<String> {
        public MyListAdapterWordResult(ArrayList<String> arrayAdapter) {
            super(ShowResultQuiz.this, R.layout.view_list_res_quiz, arrayAdapter);

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            String word = words.get(position).toString().replace("''", "'");
            String traduction = mydb.getTraductionFromWord(word);
            String myAnswer = answers.get(position).toString().replace("''", "'");
            View viewListWords = convertView;
            if (viewListWords == null) {
                viewListWords = getLayoutInflater().inflate(R.layout.view_list_res_quiz, parent, false);
            }
            TextView textViewWord = (TextView) viewListWords.findViewById(R.id.textViewQuestion);
            TextView textViewTraduction = (TextView) viewListWords.findViewById(R.id.textViewTradQue);
            TextView textViewAnswer = (TextView) viewListWords.findViewById(R.id.textViewAnswer);
            textViewWord.setText(word);
            textViewTraduction.setText(traduction);
            textViewAnswer.setText(myAnswer);
            if (traduction.equalsIgnoreCase(myAnswer)) {
                viewListWords.setBackgroundColor(getColor(R.color.colorLightGreen));
            } else {
                viewListWords.setBackgroundColor(getColor(R.color.colorLightRed));
            }

            return viewListWords;

        }
    }
}