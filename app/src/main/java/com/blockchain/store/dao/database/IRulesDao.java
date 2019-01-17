package com.blockchain.store.dao.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;
import com.blockchain.store.dao.database.model.Rules;

@Dao
public interface IRulesDao {

    @Insert
    void insert(Rules rules);

    @Update
    void update(Rules rules);

    @Delete
    void delete(Rules rules);

}
