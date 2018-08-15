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

public class GuessLevel2 extends AppCompatActivity {

    String word;
    TextView guessbox;
    Button guessbutton;
    EditText inputword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guess);

        int level = bbgActivityLevel2.lavel2.getLevel();
        guessbox=(TextView) findViewById(R.id.guessbox);
        inputword = (EditText) findViewById(R.id.inputword);
        guessbutton=(Button) findViewById(R.id.guessbutton);

        final String tmpString = Arrays.toString(bbgActivityLevel2.lavel2.textarray[level]);
        final String  guessword = tmpString.replaceAll("[^a-zA-Z0-9]", "");


        guessbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word = inputword.getText().toString();
                if(word.equals(guessword)){
                    showToast();
                    bbgActivityLevel2.lavel2.levelUp();
                    finish();
                }
            }
        });

    }

    private void showToast() {
        Toast.makeText(GuessLevel2.this,"YOU WIN!",Toast.LENGTH_LONG).show();
    }



}
