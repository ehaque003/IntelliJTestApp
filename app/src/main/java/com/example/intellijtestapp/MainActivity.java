package com.example.intellijtestapp;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity {
    Button nextword;
    Button showorhide;
    Button prononuciation;
    TextView partsofspeech;
    TextView definition;
    TextView sentence;
    TextView word;
    String[] infoonword = new String[6];
    String response2 = "";
    MediaPlayer mediaPlayer;
    ImageButton imageButton;
    //Word, Phonetics, Pronunciation, Definition, Parts of Speech, Sentence

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nextword = findViewById(R.id.nextword);
        showorhide = findViewById(R.id.showhide);
        prononuciation = findViewById(R.id.phonetics);
        partsofspeech = findViewById(R.id.partsOfSpeech);
        definition = findViewById(R.id.definition);
        sentence = findViewById(R.id.sentence);
        word = findViewById(R.id.word2);
        imageButton = findViewById(R.id.imageButton);
        prononuciation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadein);
                imageButton.startAnimation(animation);
            }
        });
        try {
            dictionaryapicall();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String randomwordgetter() throws InterruptedException {
        String word = "";
        String queryURL = "https://random-words-api.vercel.app/word";
        String jsonString =  httphandler(queryURL);
        response2 = "";
        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            word = jsonObject.getString("word");
        } catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        return word;
    }
    public void dictionaryapicall() throws InterruptedException {
        String word = randomwordgetter();
        String queryURL = "https://api.dictionaryapi.dev/api/v2/entries/en_US/"+word;
        String jsonString = httphandler(queryURL);
        response2 = "";
        if(jsonString == null){
            dictionaryapicall();
        }
        try{
            System.out.println(jsonString);
            JSONObject jsonObject = new JSONObject(jsonString);
            //Word, Phonetics, Pronunciation, Definition, Parts of Speech, Sentence
            infoonword[0] = word;
            infoonword[1] = jsonObject.getJSONArray("phonetics").getJSONObject(0).getString("text");
            infoonword[2] = jsonObject.getJSONArray("phonetics").getJSONObject(0).getString("audio");
            infoonword[3] = jsonObject.getJSONArray("meaning").getJSONObject(0).getJSONArray("definitions").getJSONObject(0).getString("definition");
            infoonword[4] = jsonObject.getJSONArray("meaning").getJSONObject(0).getString("partOfSpeech");
            infoonword[5] = jsonObject.getJSONArray("meaning").getJSONObject(0).getJSONArray("definitions").getJSONObject(0).getString("example");
        } catch (JSONException jsonException){
            jsonException.printStackTrace();
        }
        screensetter();
    }

    public String httphandler(String queryURL) throws InterruptedException {
        new Thread() {
            public void run() {
                try{
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
                    if((line = reader.readLine()) != null){
                        boolean itIsNull = true;
                    }
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
                    System.out.println(response2);
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
            Thread.sleep(1000);
        } catch (Exception e){
            e.printStackTrace();
        }
        return response2;
    }



    public void screensetter(){
        //Word, Phonetics, Pronunciation, Definition, Parts of Speech, Sentence
//        System.out.println(infoonword);
        word.setText(infoonword[0]);
        prononuciation.setText(infoonword[1]);
        definition.setText(infoonword[3]);
        partsofspeech.setText(infoonword[4]);
        sentence.setText(infoonword[5]);
    }

    public void playAudio(){
        String audioURL = "https://lex-audio.useremarkable.com/mp3/hello_us_2_rr.mp3";
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