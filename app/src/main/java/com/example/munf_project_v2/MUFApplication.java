package com.example.munf_project_v2;

import android.app.Application;

import androidx.room.Room;

public class MUFApplication extends Application {
    private AppDatenbank appDatenbank;

    @Override
    public void onCreate() {
        super.onCreate();
        appDatenbank = Room
                .databaseBuilder(this, AppDatenbank.class,"Sensorwerte")
                .build();
    }

    public AppDatenbank getDatabase(){

        return appDatenbank;
    }
}
