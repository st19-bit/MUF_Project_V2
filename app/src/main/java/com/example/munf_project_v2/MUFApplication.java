package com.example.munf_project_v2;

import android.app.Application;

import androidx.room.Room;

public class MUFApplication extends Application {
    private MUFDatenbank mufDatenbank;

    @Override
    public void onCreate() {
        super.onCreate();
        mufDatenbank = Room.databaseBuilder(this,MUFDatenbank.class,"Liste")
                .build();
    }
    public MUFDatenbank getDatabase() {return mufDatenbank;}
}
