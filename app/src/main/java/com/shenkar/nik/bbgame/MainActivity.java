package com.shenkar.nik.bbgame;
///////////////////beni valotker//////////////////////
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button soundButton;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        soundButton = (Button) findViewById(R.id.sound);
        playButton = (Button) findViewById(R.id.play);

        playButton.setOnClickListener(this);
        soundButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.play:
                Intent intent = new Intent(this,bbgActivity.class);
                startActivity(intent);
                break;
            case R.id.sound:
                Intent intent2 = new Intent(this,soundActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
