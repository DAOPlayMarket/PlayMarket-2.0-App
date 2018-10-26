package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.Application;
import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo(AppInfo app) {
        IcoInfoResponse icoInfoResponse = app.icoInfoResponse;
        String tokenSold = String.valueOf(Double.parseDouble(icoInfoResponse.tokensSold) / (double) Math.pow(10, Double.parseDouble(icoInfoResponse.decimals)));
        tokenSold = String.valueOf((double) Math.round(Double.parseDouble(tokenSold) * 1000d) / 1000d);
        String totalTokens = String.valueOf(Long.parseLong(app.icoTotalForSale) / ((long) Math.pow(10, Long.parseLong(icoInfoResponse.decimals))));

        objects.add(new InvestMainItem(
                app.nameApp,
                app.description,
                tokenSold,
                totalTokens,
                Integer.parseInt(icoInfoResponse.stage) + 1,
                icoInfoResponse.stages.size(),
                app.getUnixTimeToStageEnding(),
                "",
                app.icoCrowdSaleAddress,
                app.getIconUrl(),
                app.icoSymbol,
                app.isIcoAlreadyStarts()
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
    public InvestTempPojo createTest() {
        String tokenSold = "10000";
        String totalTokens = "1000000000";

        objects.add(new InvestMainItem(
                "PlayMarket 2.0",
                "PlayMarket description",
                tokenSold,
                totalTokens,
                1,
                3,
                System.currentTimeMillis()+1000000L,
                "",
                "",
                "icon url",
                "PMT",
                false
        ));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube("QYjyfCt6gWc"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);


        objects.add(new InvestTitle("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new InvestBody("DAO PlayMarket 2.0 is a decentralized Android app store that accepts cryptocurrency payments. It is combined with a crowdinvesting (ICO) developer platform. The DAO PlayMarket 2.0 platform has a lot of advantages over alternative platforms and large mobile app stores, such as:\",\n" +
                "\"• the platform is resistant to censorship: the basic information is stored in a blockchain, which ensures security and provides access to apps from anywhere in the world;\",\n" +
                "\"• developers are given the opportunity to raise additional investments for their projects through a built-in crowdinvesting (ICO) platform. Any developer can issue tokens of their app in a few clicks;\",\n" +
                "\"• the DAO PlayMarket 2.0 platform has an integrated decentralized cryptocurrency exchange, for which an open programming interface (API) will be developed. The API will be available to third-party developers for integration into various systems;\",\n" +
                "\"• the platform accepts payments with cryptocurrencies to significantly expand a range of uses of the store and to make cryptocurrency closer to end users;\",\n" +
                "\"• developers can promote their apps by setting the conditions for users to receive tokens for installing an app;\",\n" +
                "\"• at the first stage, all apps are scanned for viruses and exploits in automatic mode, and then manual moderation is performed. Later, with the development of the platform, moderation algorithms will be determined by the community (DAO) using the latest technologies. "));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Investors advantages"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);


        objects.add(new InvestBody("The DAO PlayMarket 2.0 platform implies that holders of PMT tokens automatically become co-owners of the platform-based DAO PlayMarket Foundation (PMF). One of the primary functions of the foundation is open management of its resources in conjunction with other members of DAO PlayMarket 2.0. "));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new InvestTitle("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
//
//
//        objects.add(new ScreenShotBody(app.getImages()));
//        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);
//        if (app.infoICO.advisors != null && !app.infoICO.advisors.isEmpty()) {
//
//            objects.add(new InvestTitle("Advisors"));
//            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
//
//            for (IcoTeam advisor : app.infoICO.advisors) {
//                objects.add(new InvestMember(advisor.name, advisor.description, app.getImageByPath(advisor.photo), advisor.social));
//                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
//            }
//        }
//
//        if (app.infoICO.team != null && !app.infoICO.team.isEmpty()) {
//
//            objects.add(new InvestTitle("Our team"));
//            objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
//
//            for (IcoTeam team : app.infoICO.team) {
//                objects.add(new InvestMember(team.name, team.description, app.getImageByPath(team.photo), team.social));
//                objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
//            }
//        }
//        objects.add(new InvestTitle("Contacts"));
//        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
        return null;
    }
}


