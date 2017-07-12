package com.tripas.test1.test1;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tripas.test1.test1.svg.SVGImage;

public class MainActivity extends AppCompatActivity {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        /**
         * SafetyNet
         * */
        final SafetyNetChecker safetyNetChecker = new SafetyNetChecker(activity);
        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SAFETYNET", "checking triggered");
                safetyNetChecker.check();
            }
        });

        /**
         * SVG-android tool
         * */
        try {
            SVGImage svgImage = new SVGImage(MainActivity.this, R.id.svgTest, R.raw.android);

//        ImageView imageView = (ImageView) findViewById(R.id.svgTest);
//        SVGImage svgImage = new SVGImage(MainActivity.this, imageView, R.raw.menu_journeys_on);
        } catch (Exception e) {
        }




    }
}
