package com.blockchain.store.playmarket.utilities;

import com.blockchain.store.playmarket.data.entities.ReferralData;
import com.orhanobut.hawk.Hawk;

import java.util.ArrayList;

public class ReferralManager {
    private static final String REFERRAL_KEY = "referral_key";


    public void addReferralData(ReferralData referralData) {
        ArrayList<ReferralData> listOfReferral = Hawk.get(REFERRAL_KEY, new ArrayList<>());
        for (ReferralData data : listOfReferral) {
            if(data.packageName.equalsIgnoreCase(referralData.packageName)){
                listOfReferral.remove(data);
            }
        }
        listOfReferral.add(referralData);
        Hawk.put(REFERRAL_KEY, listOfReferral);

    }

    public ReferralData getReferralData(String packageName){
        ArrayList<ReferralData> listOfReferral = Hawk.get(REFERRAL_KEY, new ArrayList<>());
        for (ReferralData data : listOfReferral) {
            if(data.packageName.equalsIgnoreCase(packageName)){
                return data;
            }
        }
        return new ReferralData("","");
    }

}
