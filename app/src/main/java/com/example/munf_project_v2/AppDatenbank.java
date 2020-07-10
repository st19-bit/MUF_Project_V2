package com.example.munf_project_v2;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {AccelerationInformation.class}, version = 1)
public abstract class AppDatenbank extends RoomDatabase {
    public abstract SensorDao getSensorDao();
}
