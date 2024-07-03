package com.example.wordlehelper;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText yelowLettersET;
    String yellowS;
    EditText greyLettersET;
    String greyS;
    EditText guessWordET;
    String guessS;
    ArrayList<String> possibleWords;
    ListView outputLV;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        yelowLettersET = findViewById(R.id.yellow);
        greyLettersET = findViewById(R.id.grey);
        guessWordET = findViewById(R.id.guess);


    }

    private void doWork() {
    }

    public void onButtonPressWordle(View view) {
        if(!yelowLettersET.getText().toString().equals("")){
            yellowS = yelowLettersET.getText().toString();
        }
        if(!greyLettersET.getText().toString().equals("")){
            greyS = greyLettersET.getText().toString();
        }
        if(!guessWordET.getText().toString().equals("") && guessWordET.getText().toString().length() == 5){
            guessS = guessWordET.getText().toString();
        } else {
            Toast.makeText(this,"Guess needs to be 5 letters long", Toast.LENGTH_LONG).show();
            return;
        }
        doWork();
    }
}