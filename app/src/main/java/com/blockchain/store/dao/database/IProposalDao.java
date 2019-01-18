package com.blockchain.store.dao.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.blockchain.store.dao.database.model.Proposal;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface IProposalDao {

    @Insert
    void insert(Proposal proposal);

    @Update
    void update(Proposal proposal);

    @Delete
    void delete(Proposal proposal);

    @Insert
    void insert(ArrayList<Proposal> proposals);

    @Query("SELECT * FROM Proposal WHERE proposalID = :proposalId")
    Proposal getById(long proposalId);

    @Query("SELECT * FROM Proposal")
    List<Proposal> getAll();

}
