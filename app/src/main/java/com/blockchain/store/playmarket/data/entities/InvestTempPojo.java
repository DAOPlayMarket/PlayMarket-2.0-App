package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo() {
        objects.add(new InvestMainItem());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MAIN);

        objects.add(new InvestYoutube("QYjyfCt6gWc"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_YOUTUBE);

        objects.add(new String("ICO description"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new String("Успешно проведено preTGE собрано $165 000, всем участникам уже обменяли предварительные preTGE токены на основные токены TGE \n" +
                "Разработана demo-версия продукта\n" +
                "Интервью советнику Президента - Герману Клименко http://radio.mediametrics.ru/investment_in_ICO/54907/\n" +
                "Достигнуты договоренности с биржей CoinPlace на размещение токена после ICO\n" +
                "Растущий рынок P2P кредитования: $300 000 000 000 к 2020 году по прогнозам Morgan Stanley\n" +
                "Отработанные на практике механизмы скоринга и верификации заемщиков и привлечения капитала для выдачи кредитов\n" +
                "Пристальное внимание к юридическим аспектам, проработанная легальная основа всех бизнес-процессов\n" +
                "Гарантийный фонд: поручительства по займам, до 100% гарантии возврата благодаря токену SOFIN\n" +
                "Защищенная от подделки кредитная история на базе блокчейн. Прозрачность системы для стороннего аудита\n" +
                "Опытная команда, закрывающая все необходимые компетенции: более 40 специалистов для успешного старта проекта"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new String("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new String("Screenshots"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        objects.add(new String("Investors advantages"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
        objects.add(new String("Успешно проведено preTGE собрано $165 000, всем участникам уже обменяли предварительные preTGE токены на основные токены TGE \n" +
                "Разработана demo-версия продукта\n" +
                "Интервью советнику Президента - Герману Клименко http://radio.mediametrics.ru/investment_in_ICO/54907/\n" +
                "Достигнуты договоренности с биржей CoinPlace на размещение токена после ICO\n" +
                "Растущий рынок P2P кредитования: $300 000 000 000 к 2020 году по прогнозам Morgan Stanley\n" +
                "Отработанные на практике механизмы скоринга и верификации заемщиков и привлечения капитала для выдачи кредитов\n" +
                "Пристальное внимание к юридическим аспектам, проработанная легальная основа всех бизнес-процессов\n" +
                "Гарантийный фонд: поручительства по займам, до 100% гарантии возврата благодаря токену SOFIN\n" +
                "Защищенная от подделки кредитная история на базе блокчейн. Прозрачность системы для стороннего аудита\n" +
                "Опытная команда, закрывающая все необходимые компетенции: более 40 специалистов для успешного старта проекта"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_BODY);

        objects.add(new String("Advisors"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
        objects.add(new InvestMember());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new String("Our team"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
        objects.add(new InvestMember());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember());
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
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

class InvestMember {
    String name = "Александр Рубин";
    String description = "Основатель Glenwood Capital, финансист, эксперт в сфере инвестирования и управления капиталом за плечами которого15-лет опыта в области и более 20 международных инвести-\u0003ционных проектов успешно завершенных при его непосред-\u0003ственном участии.\n";
    String fb;
    String vk;
    String imagePath;
}
