package com.example.intellijtestapp;

import android.content.Intent;
import android.os.TransactionTooLargeException;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Arrays;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Spinner spinner = findViewById(R.id.spinner);
        Button button = findViewById(R.id.button);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Homepage.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.numbers));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(myAdapter);
        WordData.makeWords();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numofwords = spinner.getSelectedItem().toString();
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("NumberOfWord", numofwords);
                startActivity(intent);
            }
        });
    }
}