package com.shenkar.nik.bbgame;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Dialog extends AppCompatActivity {

    Button startOver, exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);

        startOver = findViewById(R.id.btn_startOver);
        startOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dialog.this, bbgActivity.class);
                startActivity(intent);
            }
        });

        exit = findViewById(R.id.btn_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dialog.this, MainActivity.class);
                startActivity(intent);
                bbgActivity.breakoutEngine.finish();
                bbgActivityLevel2.lavel2.finish();
                finish();

            }
        });
    }

}
