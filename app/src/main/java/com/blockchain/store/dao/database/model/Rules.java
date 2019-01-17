package com.blockchain.store.dao.database.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Rules {
    @PrimaryKey
    public int id;
    public int newMinimumQuorum;
    public int newDebatingPeriodDuration;
    public int newRequisiteMajority;
}
