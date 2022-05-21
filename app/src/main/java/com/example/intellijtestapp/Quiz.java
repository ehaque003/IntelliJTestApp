package com.example.intellijtestapp;

import android.content.Intent;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import java.util.*;
import android.widget.*;

public class Quiz extends AppCompatActivity {
    int questions = 0;//checks how many questions done
    int gottenCorrect = 0;//how many questions are correct
    int maxnum = 0;
    boolean isCorrect = false;
    boolean correct = false;
    Random rdn = new Random();
    String correctword;
    int rand = rdn.nextInt(10);
    int correctbutton;
    ArrayList<String> wrongword = new ArrayList<>();
    TextView question;
    RadioButton answer1;
    RadioButton answer2;
    RadioButton answer3;
    RadioButton answer4;
    ImageButton next;
    TextView results;
    TextView correctText;
    TextView messageText;

    //Key: Word, Value: Definition
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        question = findViewById(R.id.textView);
        answer1 = findViewById(R.id.radioButton);
        answer2 = findViewById(R.id.radioButton2);
        answer3 =findViewById(R.id.radioButton3);
        answer4 = findViewById(R.id.radioButton4);
        next = findViewById(R.id.nextQuestion);
        results = findViewById(R.id.Results);
        correctText = findViewById(R.id.correct);
        messageText = findViewById(R.id.message);
        ImageButton finished = findViewById(R.id.finished);
        ImageView imageView = findViewById(R.id.imageView2);
        ImageView imageView1 = findViewById(R.id.imageView3);
        ImageView imageView2 = findViewById(R.id.imageView4);
        ImageView imageView3 = findViewById(R.id.imageView5);
        TextView textView = findViewById(R.id.textView3);
        TextView textView1 = findViewById(R.id.textView9);
        Intent intent = getIntent();
        maxnum = Integer.parseInt(intent.getStringExtra("NumberOfQuestions"));
        backgroundtask();
        answer1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctbutton == 1){
                    isCorrect = true;
                }
                else{
                    isCorrect = false;
                }
                answer2.setChecked(false);
                answer3.setChecked(false);
                answer4.setChecked(false);
            }
        });

        answer2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctbutton == 2){
                    isCorrect = true;
                }
                else{
                    isCorrect = false;
                }
                answer1.setChecked(false);
                answer3.setChecked(false);
                answer4.setChecked(false);
            }
        });

        answer3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctbutton == 3){
                    isCorrect = true;
                }
                else{
                    isCorrect = false;
                }
                answer1.setChecked(false);
                answer2.setChecked(false);
                answer4.setChecked(false);
            }
        });

        answer4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(correctbutton == 4){
                    isCorrect = true;
                }
                else{
                    isCorrect = false;
                }
                answer1.setChecked(false);
                answer2.setChecked(false);
                answer3.setChecked(false);
            }
        });

        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                answer1.setChecked(false);
                answer2.setChecked(false);
                answer3.setChecked(false);
                answer4.setChecked(false);
                if(isCorrect){
                    ++gottenCorrect;
                }
                else{
                    wrongword.add(correctword);
                }
                ++questions;
                if(questions==maxnum-1){
                    results.setVisibility(View.VISIBLE);
                    correctText.setVisibility(View.VISIBLE);
                    messageText.setVisibility(View.VISIBLE);
                    finished.setVisibility(View.VISIBLE);
                    textView1.setVisibility(View.VISIBLE);
                    next.setVisibility(View.INVISIBLE);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    answer1.setVisibility(View.INVISIBLE);
                    answer2.setVisibility(View.INVISIBLE);
                    answer3.setVisibility(View.INVISIBLE);
                    answer4.setVisibility(View.INVISIBLE);
                    question.setVisibility(View.INVISIBLE);
                    textView.setVisibility(View.INVISIBLE);;
//                    if(gottenCorrect < 5){
//                        messageText.setText("Hey don't be sad, at least you tried!");
//                    }else if(gottenCorrect > 4 && gottenCorrect < 8){
//                        messageText.setText("Great job, but time to pick up the throttle!");
//                    }else if(gottenCorrect == 8 || gottenCorrect == 9){
//                        messageText.setText("Amazing! You are among the top!");
//                    }else if(gottenCorrect == 10){
//                        messageText.setText("Pin point accuracy! Click the next button with pride! After all you  have bragging rights, right?");
//                    }
                    if(gottenCorrect==maxnum){
                        messageText.setText("Good");
                    }
                    else{
                        messageText.setText("Bad");
                    }
                    correctText.setText(gottenCorrect+"/"+maxnum);
                }
                else{
                    backgroundtask();
                }
            }
        });

        finished.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getBaseContext(), Homepage.class);
                startActivity(intent1);
            }
        });

    }


    public void backgroundtask(){
        correctword = MainActivity.words[questions];
        String definition = MainActivity.definitions[questions];
        String word2 = "";
        String word3 = "";
        String word4 = "";
        while(questions<maxnum){
            word2 = MainActivity.words[rdn.nextInt(maxnum-1)];
            word3 = MainActivity.words[rdn.nextInt(maxnum-1)];
            word4 = MainActivity.words[rdn.nextInt(maxnum-1)];
            if(word2 != null && word3 != null && word4 != null){
                if( !(word2.equals(correctword)) && !(word3.equals(correctword)) && !(word4.equals(correctword))) {
                    break;
                }
            }
        }
        String[] words = {word2, word3, word4};
        question.setText("What is the word related to this definition: "+definition);
        switch (rdn.nextInt(3)+1){
            case 1:
                correctbutton = 1;
                answer1.setText(correctword);
                answer2.setText(words[2]);
                answer3.setText(words[0]);
                answer4.setText(words[1]);
            case 2:
                correctbutton = 2;
                answer2.setText(correctword);
                answer1.setText(words[1]);
                answer3.setText(words[2]);
                answer4.setText(words[0]);
            case 3:
                correctbutton = 3;
                answer3.setText(correctword);
                answer1.setText(words[0]);
                answer2.setText(words[1]);
                answer4.setText(words[2]);
            case 4:
                correctbutton = 4;
                answer4.setText(correctword);
                answer3.setText(words[0]);
                answer2.setText(words[1]);
                answer1.setText(words[2]);
        }
    }
}


