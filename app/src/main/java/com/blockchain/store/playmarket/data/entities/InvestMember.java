package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InvestMember {
    public String name;
    public String description;
    public String imagePath;
    public SocialLinks socialLinks;

    public InvestMember(String name, String description, String imagePath,SocialLinks socialLinks) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.socialLinks = socialLinks;
        ;
    }
}
