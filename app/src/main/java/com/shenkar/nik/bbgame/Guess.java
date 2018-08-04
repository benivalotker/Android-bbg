package com.shenkar.nik.bbgame;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

public class Guess extends AppCompatActivity {

    String word;
    TextView guessbox;
    Button guessbutton;
    EditText inputword;
    //BreakoutEngine breakoutEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        int level = bbgActivity.breakoutEngine.getLevel();
        guessbox=(TextView) findViewById(R.id.guessbox);
        inputword = (EditText) findViewById(R.id.inputword);
        guessbutton=(Button) findViewById(R.id.guessbutton);

        final String tmpString = Arrays.toString(bbgActivity.breakoutEngine.textarray[level]);
        //final String  guessword = tmpString.replaceAll(", ", "");
        final String  guessword = tmpString.replaceAll("[^a-zA-Z0-9]", "");


        guessbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //guessbox.setText(guessword);
                word = inputword.getText().toString();
                if(word.equals(guessword)){
                    showToast();
                    bbgActivity.breakoutEngine.levelUp();
                    finish();
                }
                //bbgActivity.breakoutEngine.resume();
                 // bbgActivity.breakoutEngine.textarray;
            }
        });

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/

    }

    private void showToast() {
        Toast.makeText(Guess.this,"YOU WIN!",Toast.LENGTH_LONG).show();
    }


}
