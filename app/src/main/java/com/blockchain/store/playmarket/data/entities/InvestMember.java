package com.blockchain.store.playmarket.data.entities;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvestMember {
    public String name;
    public String description;
    public String imagePath;
    public ArrayList<Map<String, String>> socialLinks;
    public Map<String, String> socialMapLinks;

    public InvestMember(String name, String description, String imagePath, ArrayList<Map<String, String>> socialLinks) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.socialLinks = socialLinks;

        HashMap<String, String> socialMapLinks = new HashMap<>();
        for (Map<String, String> socialLink : socialLinks) {
            for (String key : socialLink.keySet()) {
                String value = socialLink.get(key);
                socialMapLinks.put(key, value);
            }
        }
    }
}
