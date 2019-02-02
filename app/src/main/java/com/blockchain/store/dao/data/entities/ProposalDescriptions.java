package com.blockchain.store.dao.data.entities;

public class ProposalDescriptions {

    public String description;
    public String transactionByteCode;

    public ProposalDescriptions(String description, String transactionByteCode) {
        this.description = description;
        this.transactionByteCode = transactionByteCode;
    }

    public ProposalDescriptions() {

    }
}
