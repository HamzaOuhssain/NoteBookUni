package com.hamza.user.notebook;

import android.os.Bundle;
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
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private String correctAnswer;
    private Button buttonNext;
    private Button buttonAnsw1;
    private Button buttonAnsw2;
    private Button buttonAnsw3;
    private Button buttonAnsw4;
    private TextView textViewQuestion;
    private TextView textViewPoints;
    public DBHelper mydb;
    private int points = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);
        mydb = new DBHelper(this);

        initialControl();

        randomSetting();
    }
    public void initialControl(){
        buttonAnsw1 = (Button) findViewById(buttonAnswer1);
        buttonAnsw2 = (Button) findViewById(R.id.buttonAnswer2);
        buttonAnsw3 = (Button) findViewById(R.id.buttonAnswer3);
        buttonAnsw4 = (Button) findViewById(R.id.buttonAnswer4);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        textViewPoints = (TextView) findViewById(R.id.textViewPoints);
        textViewQuestion = (TextView) findViewById(R.id.textViewQuestion);

    }

    public void randomSetting(){
        buttonNext.setVisibility(View.INVISIBLE);
        //GET RANDOM VARIABLE FROM THE DATABASE
        questionWord = mydb.randomVariable("word");
        answer1 = mydb.getTraductionFromWord(questionWord);
        correctAnswer = answer1;                                    //set answer1 the Correct Answer
        answer2 = mydb.randomVariable("traduction");
        answer3 = mydb.randomVariable("traduction");
        answer4 = mydb.randomVariable("traduction");
        setView();
    }

    private void setView(){
        buttonAnsw1.setBackgroundColor(getResources().getColor(R.color.colorInitial));
        buttonAnsw2.setBackgroundColor(getResources().getColor(R.color.colorInitial));
        buttonAnsw3.setBackgroundColor(getResources().getColor(R.color.colorInitial));
        buttonAnsw4.setBackgroundColor(getResources().getColor(R.color.colorInitial));

        // Set a Random Answers in different buttons
        View[] colm = { buttonAnsw1, buttonAnsw2, buttonAnsw3, buttonAnsw4 };
        String[] answers = {answer1, answer2, answer3, answer4};
        List l = new ArrayList();
        for(String i: answers)
            l.add(i);

        Collections.shuffle(l);

        for (int i = 0; i < 4; i++) {
            ((Button) colm[i]).setText(""+l.get(i));
        }

        textViewPoints.setText(""+ points);
        textViewQuestion.setText("What is the traduction for "+questionWord+"?");
    }

    private void setFinish(Boolean a){
        setClicable(FALSE);

        buttonNext.setVisibility(View.VISIBLE);
        if(a)
            points++;
        else
            points--;

    }

    public void nextButton(View view){
       setClicable(TRUE);
        randomSetting();
    }

    private void setClicable(Boolean b){
        buttonAnsw1.setClickable(b);
        buttonAnsw2.setClickable(b);
        buttonAnsw3.setClickable(b);
        buttonAnsw4.setClickable(b);

    }


    //OnClickListener for all the ButtonsAnswers
    public void onClick(View v) {
        String answer = v.toString();

        if(((Button) v).getText().toString().equalsIgnoreCase(correctAnswer)) {
            v.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            setFinish(TRUE);
        }else {
            v.setBackgroundColor(getResources().getColor(R.color.colorRed));
            setFinish(FALSE);
        }
    }


}
