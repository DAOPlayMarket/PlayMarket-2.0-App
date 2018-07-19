package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo(AppInfo app) {
//        String tokenSold = String.valueOf(Long.parseLong(appInfo.infoICO.tokenSold) / (long) Math.pow(10, Long.parseLong(appInfo.app.icoDecimals)));
        int currentPeriod = Integer.valueOf(app.currentInfo.currentPeriod);
        String tokenSold = app.currentInfo.currentPeriod;
        String totalTokens = String.valueOf(Long.parseLong(app.icoTotalSupply) / (long) Math.pow(10, 8));

        long endIcoUnixDate = Long.parseLong(app.icoStages.get(currentPeriod).time);
        long totalTimeFromUnix = (endIcoUnixDate - (System.currentTimeMillis() / 1000));
        objects.add(new InvestMainItem(
                app.nameApp,
                "",
                tokenSold,
                totalTokens,
                1,
                3,
                totalTimeFromUnix,
                "",
                app.adrICO,
                app.getIconUrl(),
                app.icoSymbol
        ));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube(app.infoICO.youtubeID));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);


        objects.add(new InvestTitle("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new InvestBody(app.infoICO.description));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Investors advantages"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);


        objects.add(new InvestBody(app.infoICO.advantages));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new ScreenShotBody(app.getImages()));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        if (app.infoICO.advisors != null && !app.infoICO.advisors.isEmpty()) {

            objects.add(new InvestTitle("Advisors"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam advisor : app.infoICO.advisors) {
                objects.add(new InvestMember(advisor.name, advisor.description, app.getImageByPath(advisor.photo), advisor.social));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }

        if (app.infoICO.team != null && !app.infoICO.team.isEmpty()) {

            objects.add(new InvestTitle("Our team"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam team : app.infoICO.team) {
                objects.add(new InvestMember(team.name, team.description, app.getImageByPath(team.photo), team.social));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }
//        objects.add(new InvestTitle("Contacts"));
//        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
    }
}


