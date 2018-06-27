package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo(AppInfo appInfo) {
        String tokenSold = String.valueOf(Long.parseLong(appInfo.icoInfo.tokenSold) / (long) Math.pow(10, Long.parseLong(appInfo.app.icoDecimals)));
        String totalTokens = String.valueOf(Long.parseLong(appInfo.app.icoTotalSupply) / (long) Math.pow(10, Long.parseLong(appInfo.app.icoDecimals)));
        objects.add(new InvestMainItem(
                appInfo.app.nameApp,
                "",
                tokenSold,
                totalTokens,
                Integer.parseInt(appInfo.icoInfo.currentStage),
                3,
                appInfo.app.icoStages.get(Integer.parseInt(appInfo.icoInfo.currentStage) - 1).time,
                appInfo.app.adrDev,
                appInfo.getIcoIcon(),
                appInfo.app.icoSymbol
        ));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube(appInfo.icoInfo.youtubeID));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);


        objects.add(new InvestTitle("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);


        objects.add(new InvestBody(appInfo.description));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new ScreenShotBody(appInfo.getIcoScreenShotsUrl()));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        if (appInfo.icoInfo.advisors != null && !appInfo.icoInfo.advisors.isEmpty()) {

            objects.add(new InvestTitle("Advisors"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam advisor : appInfo.icoInfo.advisors) {
                objects.add(new InvestMember(advisor.name, advisor.description, appInfo.getIcoAdvisorsUrl(advisor.photo)));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }

        if (appInfo.icoInfo.team != null && !appInfo.icoInfo.team.isEmpty()) {

            objects.add(new InvestTitle("Our team"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam team : appInfo.icoInfo.team) {
                objects.add(new InvestMember(team.name, team.description, appInfo.getIcoTeamUrl(team.photo)));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }
//        objects.add(new InvestTitle("Contacts"));
//        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
    }
}


