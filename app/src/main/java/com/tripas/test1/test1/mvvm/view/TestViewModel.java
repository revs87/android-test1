package com.tripas.test1.test1.mvvm.view;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

/**
 * VIEWMODEL / CORE STUFF
 * */
public class TestViewModel extends ViewModel implements TestContract {

    String str;
    MutableLiveData<Test1> test1 = new MutableLiveData<>();
    MutableLiveData<Test2> test2 = new MutableLiveData<>();

    @Override
    public void init(String str) {
        // Instantiate all objs to provide all info needed
        this.str = str;

        // Trigger API, db calls
        getModelData1();
        getModelData2();
    }

    @Override
    public LiveData<Test1> getTest1() {
        return test1;
    }

    @Override
    public LiveData<Test2> getTest2() {
        return test2;
    }

    public void getModelData1() {
        // Set the callback result.
        test1.postValue(new Test1(1));
    }

    public void getModelData2() {
        // Set the callback result.
        test2.postValue(new Test2(2, str + ":test2"));
    }
}
