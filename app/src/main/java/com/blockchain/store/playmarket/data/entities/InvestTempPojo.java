package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo(AppInfo appInfo) {
        objects.add(new InvestMainItem(
                appInfo.app.nameApp,
                "",
                String.valueOf(Long.parseLong(appInfo.icoInfo.tokenSold) / Math.pow(10, Long.parseLong(appInfo.app.icoDecimals))),
                String.valueOf(Long.parseLong(appInfo.app.icoTotalSupply) / Math.pow(10, Long.parseLong(appInfo.app.icoDecimals))),
                Integer.parseInt(appInfo.icoInfo.currentStage),
                3,
                appInfo.app.icoStages.get(Integer.parseInt(appInfo.icoInfo.currentStage) - 1).time,
                appInfo.app.adrDev
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

        objects.add(new ScreenShotBody(appInfo.pictures.imageNameList));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        if (appInfo.icoInfo.advisors != null && !appInfo.icoInfo.advisors.isEmpty()) {

            objects.add(new String("Advisors"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam advisor : appInfo.icoInfo.advisors) {
                objects.add(new InvestMember(advisor.name, advisor.description, advisor.photo));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }

        if (appInfo.icoInfo.team != null && !appInfo.icoInfo.team.isEmpty()) {

            objects.add(new String("Our team"));
            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

            for (IcoTeam team : appInfo.icoInfo.team) {
                objects.add(new InvestMember(team.name, team.description, team.photo));
                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
            }
        }
        objects.add(new String("Contacts"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
    }
}


