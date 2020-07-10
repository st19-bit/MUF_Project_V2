package com.example.munf_project_v2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Userdata.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {

    public abstract UserDao getUserDao(); // von Datenbankobject auf Dao


}
