package com.example.intellijtestapp;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    ImageButton nextword;
    ImageButton prononuciation;
    TextView partsofspeech;
    TextView definition;
    TextView word;
    TextView phonetics;
    String[] infoonword = new String[6];
    String response2 = "";
    MediaPlayer mediaPlayer;
    ImageButton flashCard;
    boolean onWordScreen = true;
    int howmanywordslearned = 1;
    static String[] words = new String[15];
    static String[] definitions = new String[15];
    //Word, Phonetics, Pronunciation, Definition, Parts of Speech, Sentence

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextword = findViewById(R.id.nextWord);
        prononuciation = findViewById(R.id.pronunciation);
        partsofspeech = findViewById(R.id.partsOfSpeech);
        definition = findViewById(R.id.definition);
        word = findViewById(R.id.word2);
        flashCard = findViewById(R.id.imageButton);
        phonetics = findViewById(R.id.phonectics);
        Intent intent = getIntent();
        int maxnum = Integer.parseInt(intent.getStringExtra("NumberOfWord"));
        prononuciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
        flashCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
                Animation animation1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
                flashCard.startAnimation(animation);
                flashCard.startAnimation(animation1);
                if(onWordScreen){
                    try {
                        screensetter(true, false);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    try {
                        screensetter(false, true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        try {
            dictionaryapicall();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        nextword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(howmanywordslearned == maxnum){
//                    Intent intent2 = new Intent(getBaseContext(), Quiz.class);
//                    intent2.putExtra("NumberOfQuestions", maxnum+"");
//                    startActivity(intent2);

                    Intent intent1 = new Intent(getBaseContext(), Homepage.class);
                    startActivity(intent1);
                }
                else {
                    try {
                        dictionaryapicall();
                        ++howmanywordslearned;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public String randomwordgetter() throws InterruptedException {
//        String queryURL = "https://random-word-api.herokuapp.com/word?number=1";
//        String jsonString = httphandler(queryURL);
//        response2 = "";
//        return jsonString;
        Random random = new Random();
        String word = WordData.words.get(random.nextInt(words.length-1));
        return word;
    }
    public void dictionaryapicall() throws InterruptedException {
        String word = randomwordgetter();
//        try{
//            word = word.substring(1);
//            word = word.substring(0, word.length()-1);
//        } catch (StringIndexOutOfBoundsException e){
//
//        }
        String queryURL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/"+word+"?key=09e41908-6945-4172-b4e5-0cb33b4ecb0d";
        String jsonString = httphandler(queryURL);
        response2 = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            infoonword[0] = word;
            infoonword[1] = jsonObject.getJSONObject("hwi").getJSONArray("prs").getJSONObject(0).getString("mw");
            infoonword[2] = jsonObject.getJSONObject("hwi").getJSONArray("prs").getJSONObject(0).getJSONObject("sound").getString("audio");
            infoonword[3] = jsonObject.getJSONArray("shortdef").getString(0);
            infoonword[4] = jsonObject.getString("fl");
        } catch (JSONException jsonException){
            redo();
            System.out.println("Call Again");
        }
        screensetter(false, true);
    }
    public void redo() throws InterruptedException {
        dictionaryapicall();
    }
    public String httphandler(String queryURL) throws InterruptedException {
        new Thread() {
            public void run() {
                try{
                    Animation animation1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.lefttoright);
                    flashCard.startAnimation(animation1);
                    URL url = new URL(queryURL);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setAllowUserInteraction(false);
                    conn.setInstanceFollowRedirects(true);
                    conn.setRequestMethod("GET");
                    conn.connect();
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        sb.append(line).append("\n");
                    }
                    in.close();
                    response2 = sb.toString();
                    try {
                        response2 = response2.substring(1);
                        response2 = response2.substring(0, response2.length()-2);
                    } catch (StringIndexOutOfBoundsException e){

                    }

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try{
            Thread.sleep(2000);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response2;
    }



    public void screensetter(boolean wantToFlipInfo, boolean wantToFlipWord) throws InterruptedException {
        word.setText(infoonword[0]);
        phonetics.setText(infoonword[1]);
        definition.setText(infoonword[3]);
        partsofspeech.setText(infoonword[4]);
        if(infoonword[1] == null&&infoonword[3]==null&&infoonword[4]==null){
//            prononuciation.setText("No Pronunciations");
//            definition.setText("No Definition");
//            partsofspeech.setText("No Parts Of Speech");
            System.out.println("Called Again 2");
            dictionaryapicall();
        }
        else{
            words[howmanywordslearned-1] = infoonword[0];
            definitions[howmanywordslearned-1] = infoonword[3];
        }
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeout);
        Animation animation1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
        if(onWordScreen&&wantToFlipInfo){
            word.startAnimation(animation); word.setVisibility(View.INVISIBLE);
            prononuciation.startAnimation(animation1); prononuciation.setVisibility(View.VISIBLE);
            definition.startAnimation(animation1); definition.setVisibility(View.VISIBLE);
            partsofspeech.startAnimation(animation1); partsofspeech.setVisibility(View.VISIBLE);
            phonetics.startAnimation(animation1); phonetics.setVisibility(View.VISIBLE);
            onWordScreen = false;
        }
        else if(!onWordScreen&&wantToFlipWord){
            word.startAnimation(animation1); word.setVisibility(View.VISIBLE);
            prononuciation.startAnimation(animation); prononuciation.setVisibility(View.INVISIBLE);
            definition.startAnimation(animation); definition.setVisibility(View.INVISIBLE);
            partsofspeech.startAnimation(animation); partsofspeech.setVisibility(View.INVISIBLE);
            phonetics.startAnimation(animation); phonetics.setVisibility(View.INVISIBLE);
            onWordScreen = true;
        }
    }

    public void playAudio(){
        String catagorayize = Character.toString(infoonword[2].charAt(0));
        String audioURL = "https://media.merriam-webster.com/audio/prons/en/us/mp3/"+catagorayize+"/"+infoonword[2]+".mp3";
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try{
            mediaPlayer.setDataSource(audioURL);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}