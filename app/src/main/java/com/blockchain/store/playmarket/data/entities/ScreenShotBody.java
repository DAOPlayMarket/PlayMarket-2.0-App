package com.blockchain.store.playmarket.data.entities;

import java.util.ArrayList;

public class ScreenShotBody {
    public ArrayList<String> screenShotsList;
    public ArrayList<Integer> screenShotsIds;

    public ScreenShotBody(ArrayList<String> screenShotsList) {
        this.screenShotsList = screenShotsList;
    }

    public ScreenShotBody() {
    }

    public void setIds(ArrayList<Integer> screenShotsIds) {
        this.screenShotsIds = screenShotsIds;
    }
}
