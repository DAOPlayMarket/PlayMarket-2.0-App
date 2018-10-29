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

    public ScreenShotBody fromIds(ArrayList<Integer> screenShotsIds) {
        ScreenShotBody screenShotBody = new ScreenShotBody();
        screenShotBody.screenShotsIds = screenShotsIds;
        return screenShotBody;
    }
}
