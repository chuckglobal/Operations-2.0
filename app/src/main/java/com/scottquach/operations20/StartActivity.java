package com.scottquach.operations20;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        updateScoreView();
    }

    public void startButtonClicked(View view) {
        Intent start = new Intent(StartActivity.this, GameActivity.class);
        startActivity(start);
    }

    public void aboutButtonClicked(View view) {
        new LibsBuilder()
                //provide a style (optional) (LIGHT, DARK, LIGHT_DARK_TOOLBAR)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                //start the activity
                .start(this);
    }

    //get highscore
    private int getHighScore(){
        SharedPreferences sharedPreferences = getSharedPreferences("saveFile", MODE_PRIVATE);
        int highScore = sharedPreferences.getInt("highScore", 0);
        return highScore;
    }

    //update the highscore view
    private void updateScoreView(){
        int score = getHighScore();

        TextView scoreView = (TextView) findViewById(R.id.highScoreView);
        scoreView.setText(String.valueOf(score));
    }
}
