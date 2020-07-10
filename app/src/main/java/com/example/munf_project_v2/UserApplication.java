package com.example.munf_project_v2;

import android.app.Application;

import androidx.room.Room;

public class UserApplication extends Application {

    private UserDatabase userDatabase;

    @Override
    public void onCreate() {
        super.onCreate();
        userDatabase = Room
                .databaseBuilder(this, UserDatabase.class, "Sensordaten")
                .build();
    }

    public UserDatabase getUserDatabase(){
        return userDatabase;
    }
}
