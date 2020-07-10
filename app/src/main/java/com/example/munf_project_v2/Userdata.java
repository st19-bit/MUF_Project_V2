package com.example.munf_project_v2;

import com.github.mikephil.charting.data.Entry;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")


public class Userdata {

    @PrimaryKey
    private Long id;


    private String name;



    // Constructor:
    public Userdata(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
