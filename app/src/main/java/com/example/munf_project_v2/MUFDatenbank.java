package com.example.munf_project_v2;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Depot.class},version=1)
public abstract class MUFDatenbank extends RoomDatabase {
    public abstract SensorDao getSensorDao();
}
