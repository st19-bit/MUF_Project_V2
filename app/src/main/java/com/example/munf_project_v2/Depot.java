package com.example.munf_project_v2;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "aufnahme")
public class Depot {

    @PrimaryKey
    @NonNull
    private int zeile;
    private float x;
    private float y;
    private float z;
    private long time;

    public Depot(int zeile ,float x, float y, float z, long time) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.time = time;
        this.zeile = zeile;
    }


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

    public long getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getZeile() {
        return zeile;
    }

    public void setZeile(int zeile) {
        this.zeile = zeile;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
