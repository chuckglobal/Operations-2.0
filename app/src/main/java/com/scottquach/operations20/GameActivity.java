package com.scottquach.operations20;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.Random;
import java.util.StringTokenizer;

public class GameActivity extends Activity {

    //TextViews
    TextView topView;
    TextView midView;
    TextView bottomView;
    TextView highScoreView;
    TextView roundCountView;


    int answerKey = -1;
    int roundCount = 1;
    int timeProgressed = 0;

    CountDownTimer gameTimer;

    MediaPlayer mp;

    CircularProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //initialize TextViews
        topView = (TextView) findViewById(R.id.topView);
        midView = (TextView) findViewById(R.id.middleView);
        bottomView = (TextView) findViewById(R.id.bottomView);
        highScoreView = (TextView) findViewById(R.id.highScoreView);
        roundCountView = (TextView) findViewById(R.id.roundCounterView);
        highScoreView.setText(String.valueOf(getHighScore()));
        //initialize media player
        mp = MediaPlayer.create(this, R.raw.music_success);
        //initialize circular progress bar
        progressBar = (CircularProgressBar) findViewById(R.id.circleProgressBar);
        progressBar.setProgressWithAnimation(0,50);
        generateEquation();

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.recreate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }
    

    //start per round timer
    private void startTimer(){
        if (gameTimer != null){
            gameTimer.cancel();
            gameTimer = null;
        }

        gameTimer = new CountDownTimer(5000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {
                    timeProgressed += 2;
                    progressBar.setProgressWithAnimation(timeProgressed,200);
                Log.d("randomNumber", String.valueOf(timeProgressed));

            }

            @Override
            public void onFinish() {
                timeProgressed += 2;
                Log.d("randomNumber", String.valueOf(timeProgressed) + "on finish");
                progressBar.setProgressWithAnimation(timeProgressed, 200);
                roundLost();
            }
        }.start();
    }

    //stop timer and reset progressBar
    private void stopTimer(){
        if (gameTimer != null){
            gameTimer.cancel();
            gameTimer = null;
        }
        timeProgressed = 0;
        progressBar.setProgressWithAnimation(timeProgressed,10);

    }



    //Choose what operation to use
    private int chooseEquationNumber(){
        Random rand = new Random();
        int randomNumber = rand.nextInt(4);
        Log.d("randomNumber", String.valueOf(randomNumber));
        return randomNumber;
    }

    //return a random number from -49 to 199
    private int getRandomNumber(int max, int min){
        Random rand = new Random();
        int random = rand.nextInt(max)-min;
        if (random == 0 || random == 1 || random == -1){
            return rand.nextInt(30)+1;
        }else{
            return random;
        }
    }

    //generate numbers and which operation to use
    private void generateEquation(){

        //set values of top and mid TextView
        int topValue = getRandomNumber(150,50);
        int midValue = getRandomNumber(150,50);
        updateTop(topValue);
        updateMiddle(midValue);

        int chosenEquation = chooseEquationNumber();
        int bottomValue = 0;

        switch (chosenEquation){

            //addition
            case 0:
                bottomValue = (topValue + midValue);
                setAnswerKey(0);
                updateBottom(bottomValue);
                break;
            //subtraction
            case 1:
                bottomValue = (topValue - midValue);
                setAnswerKey(1);
                updateBottom(bottomValue);
                break;
            //multiplication
            case 2:
                topValue = getRandomNumber(31, 30);
                midValue = getRandomNumber(31, 30);
                bottomValue = (topValue * midValue);
                setAnswerKey(2);
                updateTop(topValue);
                updateMiddle(midValue);
                updateBottom(bottomValue);
                break;
            //division
            case 3:
                int remainder = 1;
                bottomValue = 0;
                do {
                    topValue = getRandomNumber(130, 30);
                    midValue = getRandomNumber(130, 30);
                    if ((midValue != 0) && (topValue > midValue)){
                        bottomValue  = (topValue / midValue);
                        remainder = (topValue % midValue);
                    }

                }while ((remainder != 0) || (midValue == 0) || (topValue < midValue));
                setAnswerKey(3);
                updateTop(topValue);
                updateMiddle(midValue);
                updateBottom(bottomValue);
                break;

            default:
                updateBottom(0);
                break;


        }
        startTimer();
    }





    //set the correct answer
    private void setAnswerKey(int i){
        answerKey = i;
    }

    //get the String of the right answer
    private String getAnswer(int i){
        switch(i){
            case 0:
                return "addition";

            case 1:
                return "subtraction";

            case 2:
                return "multiplication";

            case 3:
                return "division";

            default:
                return "error";

        }

    }

    //determines if the selected answer is correct
    private void determineResult(int i){
        switch (i){
            case 0:
                if (i == answerKey){
                    nextRound();
                }else{
                    roundLost();
                }
                break;
            case 1:
                if (i == answerKey){
                    nextRound();
                }else{
                    roundLost();
                }
                break;
            case 2:
                if (i == answerKey){
                    nextRound();
                }else{
                    roundLost();
                }
                break;
            case 3:
                if (i == answerKey){
                    nextRound();
                }else{
                    roundLost();
                }
                break;
            default:
                roundLost();
                break;
        }

    }

    //update round view
    private void updateRoundCount(){
            roundCount++;
            roundCountView.setText(String.valueOf(roundCount));
    }

    //start next round
    private void nextRound(){
        playSuccessAudio();
        answerKey = -1;
        updateRoundCount();
        generateEquation();
    }

    //initialize round lost dialog
    private void roundLost(){

        progressBar.setProgressWithAnimation(100,100);
        playFailureAudio();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String correctAnswer = getAnswer(answerKey);


        builder.setTitle("You Lost");
        if (roundCount > getHighScore()){
            setHighScore(roundCount);
            builder.setMessage("New high score: "+ String.valueOf(roundCount) +", The correct answer was " + correctAnswer);
        }else{
            builder.setMessage("Bummer, the answer was "+ correctAnswer);
        }


        builder.setPositiveButton(R.string.restart, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent restart = getIntent();
                finish();
                startActivity(restart);
            }
        });
        builder.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener()   {
            @Override
            public void onClick(DialogInterface dialog, int which)  {
                Intent goHome = new Intent(GameActivity.this, StartActivity.class);
                startActivity(goHome);
            }
        });
        builder.setOnDismissListener(new DialogInterface.OnDismissListener(){
            @Override
                public void onDismiss(DialogInterface dialog){
//                Intent quit = new Intent(GameActivity.this, StartActivity.class);
//                startActivity(quit);
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Intent quit = new Intent(GameActivity.this, StartActivity.class);
                startActivity(quit);
            }
        });
        builder.show();
    }

    //retrieve highscore
    private int getHighScore(){
        SharedPreferences sharedPref = getSharedPreferences("saveFile",MODE_PRIVATE);
        int highScore = sharedPref.getInt("highScore", 1);
        return highScore;
    }

    //set new highscore
    private void setHighScore(int score){
        SharedPreferences sharedPref = getSharedPreferences("saveFile",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("highScore", score);
        editor.commit();
    }



    //update top view
    private void updateTop(int value){
        topView.setText(String.valueOf(value));
    }
    //updtae bottom view
    private void updateMiddle(int value){
        midView.setText(String.valueOf(value));
    }
    //update bottom view
    private void updateBottom(int value){
        bottomView.setText(String.valueOf(value));
    }
    //play ding audio
    private void playSuccessAudio(){
        mp.start();
    }
    //play failure audio
    private void playFailureAudio() {
        MediaPlayer failMP = MediaPlayer.create(this, R.raw.music_fail);
        failMP.start();
    }



/*
    Button
    clicks
 */

    public void additionButtonClicked(View view) {
        stopTimer();
        determineResult(0);

    }

    public void subtractionButtonClicked(View view) {
        stopTimer();
        determineResult(1);

    }

    public void multiplicationButtonClicked(View view) {
        stopTimer();
        determineResult(2);

    }

    public void divisionButtonClicked(View view) {
        stopTimer();
        determineResult(3);

    }
}
