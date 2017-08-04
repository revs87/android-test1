package com.tripas.test1.test1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.tripas.test1.test1.mvvm.view.TestActivity;

public class MainActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SafetyNetChecker safetyNetChecker = new SafetyNetChecker(activity);
                safetyNetChecker.check();
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToTestViewModelActivity = new Intent(activity, TestActivity.class);
                startActivity(goToTestViewModelActivity);
            }
        });

    }
}
