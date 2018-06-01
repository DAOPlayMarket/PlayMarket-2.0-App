package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

public class InvestTempPojo {
    public InvestMainItem mainItem = new InvestMainItem();

}

class InvestMainItem {
    String name = "PlayMarket 2.0";
    String description = "A Decentralized App Store for Android made with the Ethereum Blockchain";
    String earnedMin = "1 242,38";
    String earnedMax = "25 500";
    int stageCurrent = 2;
    int stageMax = 5;
    String totalTime = "28:15:22:54";
}

class InvestYoutube {
    String videoId = "";
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

class InvestMember {
    String name = "Александр Рубин";
    String description = "Основатель Glenwood Capital, финансист, эксперт в сфере инвестирования и управления капиталом за плечами которого15-лет опыта в области и более 20 международных инвести-\u0003ционных проектов успешно завершенных при его непосред-\u0003ственном участии.\n";
}
