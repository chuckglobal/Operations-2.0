package com.scottquach.operations20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    public void startButtonClicked(View view) {
        Intent start = new Intent(StartActivity.this, GameActivity.class);
        startActivity(start);
    }
}
