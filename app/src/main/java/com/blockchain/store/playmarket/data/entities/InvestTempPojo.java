package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo(AppInfo appInfo) {
        String tokenSold = String.valueOf(Long.parseLong(appInfo.infoICO.tokenSold) / (long) Math.pow(10, Long.parseLong(appInfo.app.icoDecimals)));
        String totalTokens = String.valueOf(Long.parseLong(appInfo.app.icoTotalSupply) / (long) Math.pow(10, Long.parseLong(appInfo.app.icoDecimals)));

        long totalTimeInUnix = Long.parseLong(appInfo.app.icoStages.get(Integer.parseInt(appInfo.infoICO.currentStage) - 1).time);
        long totalTimeFromUnix = ((totalTimeInUnix*1000)-System.currentTimeMillis());
        objects.add(new InvestMainItem(
                appInfo.app.nameApp,
                "",
                tokenSold,
                totalTokens,
                Integer.parseInt(appInfo.infoICO.currentStage),
                3,
                totalTimeFromUnix,
                appInfo.app.adrDev,
                appInfo.getIcoIcon(),
                appInfo.app.icoSymbol
        ));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube(appInfo.infoICO.youtubeID));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);


        objects.add(new InvestTitle("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);


        objects.add(new InvestBody(appInfo.description));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new ScreenShotBody(appInfo.getIcoScreenShotsUrl()));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        if (appInfo.infoICO.advisors != null && !appInfo.infoICO.advisors.isEmpty()) {

            objects.add(new InvestTitle("Advisors"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam advisor : appInfo.infoICO.advisors) {
                objects.add(new InvestMember(advisor.name, advisor.description, appInfo.getIcoAdvisorsUrl(advisor.photo), advisor.socialLinks));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }

        if (appInfo.infoICO.team != null && !appInfo.infoICO.team.isEmpty()) {

            objects.add(new InvestTitle("Our team"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam team : appInfo.infoICO.team) {
                objects.add(new InvestMember(team.name, team.description, appInfo.getIcoTeamUrl(team.photo), team.socialLinks));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }
//        objects.add(new InvestTitle("Contacts"));
//        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
    }
}


