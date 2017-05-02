package com.scottquach.operations20;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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
/* Button
    Clicks
 */

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

    public void highscoreClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure");
        builder.setMessage("This will reset your highscore to 0");
        builder.setPositiveButton("Reset", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences sharedPref = getSharedPreferences("saveFile", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putInt("highScore", 0);
                editor.commit();
                updateScoreView();
            }
        });
        builder.setNegativeButton("Nope", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();

    }

    public void tutorialButtonClicked(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How-To");
        builder.setMessage("The goal of this game is to find the missing operation to the equation. You will be presented with 3 numbers, the first two interact with each other with the missing" +
                " operator in order to equal the third bottom number. Click one of the four operations available that satisfies the equation before the timer runs out.");
        builder.setPositiveButton("Okay!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

}
