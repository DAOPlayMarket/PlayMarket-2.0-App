package com.blockchain.store.dao.database;

import android.arch.persistence.room.RoomDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Rules;
import android.arch.persistence.room.Database;

@Database(entities = {Proposal.class, Rules.class}, version = 1)
public abstract class DaoDatabase extends RoomDatabase {
    public abstract IProposalDao proposalDao();
    public abstract IRulesDao rulesDao();
}
