package com.example.munf_project_v2;

import android.hardware.Sensor;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "acclererationinformation")
public class AccelerationInformation {

    @PrimaryKey
    @NonNull

    private String id;
    private float timestamp;

    private Sensor sensor;
    private float x;
    private float y;
    private float z;

    public Sensor getSensor() { return sensor; }

    public void setSensor(Sensor sensor) { this.sensor = sensor; }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public void setXYZ (float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }
}