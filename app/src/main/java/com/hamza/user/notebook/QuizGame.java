//
// Quiz game  with one question and 4 button answers
// /

package com.hamza.user.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.hamza.user.notebook.R.id.buttonAnswer1;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class QuizGame extends AppCompatActivity {

    private String questionWord;
    private String correctAnswer;
    private Button buttonNext;
    private Button buttonAnsw1;
    private Button buttonAnsw2;
    private Button buttonAnsw3;
    private Button buttonAnsw4;
    private TextView textViewQuestion;
    private TextView textViewPoints;
    public DBHelper mydb;
    private int numRound = 1;
    private List<String> answers;
    private ArrayList<String> arrayWords;
    private  ArrayList<String> arrayAnswers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        mydb = new DBHelper(this);
        initialControl();
        randomSetting();


    }

    //Declaration and iniz. of the objects
    public void initialControl(){
        buttonAnsw1 = (Button) findViewById(buttonAnswer1);
        buttonAnsw2 = (Button) findViewById(R.id.buttonAnswer2);
        buttonAnsw3 = (Button) findViewById(R.id.buttonAnswer3);
        buttonAnsw4 = (Button) findViewById(R.id.buttonAnswer4);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        textViewPoints = (TextView) findViewById(R.id.textViewPoints);
        textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);
        textViewPoints.setText(numRound+"/10");
        arrayWords = new ArrayList<>();
        arrayAnswers = new ArrayList<>();

    }

    public void randomSetting() {
        answers = new ArrayList<>();
        buttonNext.setVisibility(View.INVISIBLE);
        //GET RANDOM VARIABLE FROM THE DATABASE
        questionWord = mydb.randomVariable("word");
        correctAnswer = mydb.getTraductionFromWord(questionWord);
        //set the correct answer
        answers.add(correctAnswer);
        boolean bo;
        while (answers.size() < 4) {
            bo = false;
            String t = mydb.randomVariable("traduction");
            int temp = 2;
            for(String b: answers){
                if(b.equalsIgnoreCase(t))               //Check if i already got that random traduction
                    bo = true;
            }
            if (!bo)
                answers.add(t);
        }
        setView();
    }

    private void setView(){
        buttonAnsw1.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInitial));
        buttonAnsw2.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInitial));
        buttonAnsw3.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInitial));
        buttonAnsw4.setBackgroundColor(ContextCompat.getColor(this, R.color.colorInitial));

        // Set a Random Answers in different buttons
        View[] colm = { buttonAnsw1, buttonAnsw2, buttonAnsw3, buttonAnsw4 };
        List l = new ArrayList();
        l.addAll(answers);

        Collections.shuffle(l);

        for (int i = 0; i < 4; i++) {
            ((Button) colm[i]).setText(l.get(i).toString());
        }


        textViewQuestion.setText("What is the traduction for "+questionWord+"?");
    }

        //Setting color and waiting the user
    private void setFinish(Boolean a){
        setClicable(FALSE);
        if(numRound>= 10)
            finishGame();
        else {
            buttonNext.setVisibility(View.VISIBLE);
        }

    }
    private void finishGame(){
        buttonNext.setVisibility(View.VISIBLE);
        buttonNext.setText("RESULT");


    }
    //NEXT Question or finish TEST
    public void nextButton(View view){
        if(numRound>=10){
            Intent myintent=new Intent(QuizGame.this, ShowResultQuiz.class);
            myintent.putExtra("arrayWords", arrayWords);
            myintent.putExtra("arrayAnswers", arrayAnswers);
            finish();
            startActivity(myintent);
        }
        else {
            numRound++;
            textViewPoints.setText(numRound + "/10");
            setClicable(TRUE);
            randomSetting();
        }
    }

        //Set All the  buttons Clickable
    private void setClicable(Boolean b){
        buttonAnsw1.setClickable(b);
        buttonAnsw2.setClickable(b);
        buttonAnsw3.setClickable(b);
        buttonAnsw4.setClickable(b);

    }


    //OnClickListener for all the ButtonsAnswers
    public void onClick(View v) {
        arrayAnswers.add(((Button) v).getText().toString());
        arrayWords.add(questionWord);
        v.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGrey));
        if(((Button) v).getText().toString().equalsIgnoreCase(correctAnswer))
            setFinish(TRUE);
        else
            setFinish(FALSE);

    }


}
