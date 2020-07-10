package com.example.munf_project_v2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public abstract class UserDao {

    @Query("SELECT * FROM user")
    // user = Tabellenname
    public abstract LiveData<Userdata> getUser();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert (Userdata userdata);
}
