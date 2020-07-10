package com.example.munf_project_v2;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface SensorDao {

    @Insert
    public void insert(AccelerationInformation accelerationInformation);

    @Update
    public void update(AccelerationInformation accelerationInformation);

    @Delete
    public void delete(AccelerationInformation accelerationInformation);

    @Query("SELECT * FROM acclererationinformation WHERE id = :id")
    public AccelerationInformation findById(String id);

    @Query("SELECT * FROM acclererationinformation")
    public List<AccelerationInformation> getItems();

    @Query("SELECT * FROM acclererationinformation")
    public LiveData<List<AccelerationInformation>> getItemsAsLiveData();

    @Query("SELECT * FROM acclererationinformation WHERE timestamp = :timestamp")
    public AccelerationInformation findByTimestamp(long timestamp);

    @Query("SELECT * FROM acclererationinformation WHERE timestamp >= :start AND timestamp <= :stop")
    public List<AccelerationInformation> findByTimerange(long start, long stop);
}

