package com.tripas.test1.test1.mvvm.view;

import android.arch.lifecycle.LiveData;

/**
 * MODEL / CONTRACT
 * */
public interface TestContract {

    String str = "test";

    class Test1 {
        int test;

        public Test1(int test) {
            this.test = test;
        }
    }

    class Test2 {
        int test;
        String name;

        public Test2(int test, String name) {
            this.test = test;
            this.name = name;
        }
    }

    void init(String str);

    LiveData<Test1> getTest1();

    LiveData<Test2> getTest2();
}
