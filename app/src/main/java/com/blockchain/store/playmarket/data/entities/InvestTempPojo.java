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
                String.valueOf(Long.parseLong(appInfo.app.tokenSold) / Long.parseLong(appInfo.app.icoDecimals)),
                String.valueOf(Long.parseLong(appInfo.app.icoTotalSupply) / Long.parseLong(appInfo.app.icoDecimals)),
                Integer.parseInt(appInfo.app.currentStage),
                3,
                appInfo.app.icoStages.get(Integer.parseInt(appInfo.app.currentStage) - 1).time
        ));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube(appInfo.app.youtubeID));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);


        objects.add(new String("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new String(appInfo.description));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new String("Screenshots"));
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


class InvestMainItem {
    String name = "PlayMarket 2.0";
    String description = "A Decentralized App Store for Android made with the Ethereum Blockchain";
    String earnedMin = "1 242,38";
    String earnedMax = "25 500";
    int stageCurrent = 2;
    int stageMax = 5;
    String totalTime = "28:15:22:54";

    public InvestMainItem(String name, String description, String earnedMin, String earnedMax, int stageCurrent, int stageMax, String totalTime) {
        this.name = name;
        this.description = description;
        this.earnedMin = earnedMin;
        this.earnedMax = earnedMax;
        this.stageCurrent = stageCurrent;
        this.stageMax = stageMax;
        this.totalTime = totalTime;
    }
}

class InvestYoutube {
    String videoId = "";

    public InvestYoutube(String videoId) {
        this.videoId = videoId;
    }
}

class InvestTitle {
    String title;

    public InvestTitle(String title) {
        this.title = title;
    }
}

class InvestBody {
    String body;

    public InvestBody(String body) {
        this.body = body;
    }
}

class ScreenShotBody {
    ArrayList<String> screenShotsList;

    public ScreenShotBody(ArrayList<String> screenShotsList) {
        this.screenShotsList = screenShotsList;
    }
}

class InvestMember {
    String name = "Александр Рубин";
    String description = "Основатель Glenwood Capital, финансист, эксперт в сфере инвестирования и управления капиталом за плечами которого15-лет опыта в области и более 20 международных инвести-\u0003ционных проектов успешно завершенных при его непосред-\u0003ственном участии.\n";
    String fb;
    String vk;
    String imagePath;

    public InvestMember(String name) {
        this.name = name;
    }

    public InvestMember(String name, String description, String imagePath) {
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
    }
}
