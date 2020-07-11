package com.example.munf_project_v2;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class BaseViewModel extends AndroidViewModel {
    public BaseViewModel(@NonNull Application application){super(application);}
    public MUFDatenbank getDatabase(){
        return ((MUFApplication) getApplication()).getDatabase();
    }
}
