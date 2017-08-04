package com.tripas.test1.test1.mvvm.view;

import android.arch.lifecycle.LifecycleActivity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.tripas.test1.test1.R;

/**
 * VIEW
 */
public class TestActivity extends LifecycleActivity {

    TestContract testContract;
    private TextView test1Tv;
    private TextView test2Tv;
    private TextView test2NameTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Bla bla bla UI stuff
        test1Tv = (TextView) findViewById(R.id.test1_tv);
        test2Tv = (TextView) findViewById(R.id.test2_tv);
        test2NameTv = (TextView) findViewById(R.id.test2_name_tv);

        // Work around to allow DI for testing
//        try {
//            //Fragment stuff
//            String viewModelName = this.getArguments().getString(VIEW_MODEL_CLASS);
//            Class clazz = Class.forName(viewModelName);
//            testContract = (TestContract) ViewModelProviders.of(this).get(clazz);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            testContract = ViewModelProviders.of(this).get(TestViewModel.class);
//        }

        testContract = ViewModelProviders.of(this).get(TestViewModel.class);
        testContract.init("Initiating tests");
        testContract.getTest1().observe(this, new Observer<TestContract.Test1>() {
            @Override
            public void onChanged(TestContract.Test1 test1) {
                test1Tv.setText("" + test1.test);
            }
        });
        testContract.getTest2().observe(this, new Observer<TestContract.Test2>() {
            @Override
            public void onChanged(TestContract.Test2 test2) {
                test2Tv.setText("" + test2.test);
                test2NameTv.setText(test2.name);
            }
        });

    }
}
