package com.blockchain.store.dao.data;

public class IpfsData {

    public String Name;
    public String Hash;
    public String Size;

    public IpfsData(String name, String hash, String size){
        this.Name = name;
        this.Hash = hash;
        this.Size = size;
    }

}
