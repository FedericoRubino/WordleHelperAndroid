package com.example.wordlehelper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        outputLV = findViewById(R.id.outputList);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void doWork() {
        List<String> words;

        try {
            words = loadWords("PossibleAnswers.txt");
        } catch (IOException e) {
            System.out.println("Error loading word files: " + e.getMessage());
            return;
        }

        List<String> possibleWords = getPossibleWords(words, yellowS, greyS, guessS);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                possibleWords
        );

        outputLV.setAdapter(arrayAdapter);

    }

    private List<String> loadWords(String filePath) throws IOException {
        List<String> words = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getAssets().open(filePath)));
            String line;
            while ((line = reader.readLine()) != null) {
                words.add(line.trim());
            }
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    private static boolean alreadyUsed(String word, List<String> pastWords) {
        for (String pastWord : pastWords) {
            if (word.equalsIgnoreCase(pastWord)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static List<String> getPossibleWords(List<String> words, String yellowLetters, String greyLetters,
                                                 String guess) {
        List<String> possibleWords = new ArrayList<>();
        List<String> pastAnswers = new ArrayList<>();
        Map<Character, Integer> yellowCount = new HashMap<>();

        for (char c : yellowLetters.toCharArray()) {
            yellowCount.put(c, yellowCount.getOrDefault(c, 0) + 1);
        }

        for (String word : words) {
            Map<Character, Integer> wordCount = new HashMap<>();
            int yellowMatchCount = 0;
            boolean greyInWord = false;

            for (char c : word.toCharArray()) {
                wordCount.put(c, wordCount.getOrDefault(c, 0) + 1);
            }

            for (char c : word.toCharArray()) {
                if (wordCount.getOrDefault(c, 0) <= yellowCount.getOrDefault(c, 0)) {
                    yellowMatchCount++;
                }
            }

            for (int i = 0; i < 5; i++) {
                if (greyLetters.contains(String.valueOf(word.charAt(i))) && word.charAt(i) != guess.charAt(i)) {
                    greyInWord = true;
                    break;
                }
            }

            if (yellowMatchCount >= yellowLetters.length() && !greyInWord) {
                for (int i = 0; i < 5; i++) {
                    if (word.charAt(i) != guess.charAt(i) && guess.charAt(i) != '0') {
                        break;
                    } else if (i == 4) {
                        if (alreadyUsed(word, pastAnswers)) {
                            pastAnswers.add(word);
                        } else {
                            possibleWords.add(word);
                        }
                    }
                }
            }
        }
        return possibleWords;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onButtonPressWordle(View view) {
        hideKeyboard(this);
        if(!yelowLettersET.getText().toString().equals("")){
            yellowS = yelowLettersET.getText().toString();
        } else {
            yellowS = "";
        }
        if(!greyLettersET.getText().toString().equals("")){
            greyS = greyLettersET.getText().toString();
        } else {
            greyS = "";
        }
        if(!guessWordET.getText().toString().equals("") && guessWordET.getText().toString().length() == 5){
            guessS = guessWordET.getText().toString();
        } else {
            guessS = "00000";
            Toast.makeText(this,"Guess needs to be 5 letters long", Toast.LENGTH_LONG).show();
            return;
        }
        doWork();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}