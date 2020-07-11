package com.example.munf_project_v2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.munf_project_v2.Depot;

@Dao
public abstract class SensorDao {

    @Query("SELECT * FROM aufnahme")
    public abstract LiveData<Depot> getMessung();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert (Depot depot);
}
