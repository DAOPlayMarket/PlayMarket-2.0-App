package com.blockchain.store.playmarket.data.entities;

import com.blockchain.store.playmarket.R;
import com.blockchain.store.playmarket.adapters.InvestScreenAdapter;

import java.util.ArrayList;

public class InvestTempPojo {
    public ArrayList<Object> objects = new ArrayList<>();
    public ArrayList<Integer> objectViewType = new ArrayList<>();

    public InvestTempPojo() {
    }

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
        InvestTempPojo investTempPojo = new InvestTempPojo();

        ArrayList<Integer> objectViewType = new ArrayList<>();
        ArrayList<Object> objects = new ArrayList<>();

        String tokenSold = "10000";
        String totalTokens = "1000000000";
        InvestMainItem investMainItem = new InvestMainItem(
                "PlayMarket 2.0",
                "PlayMarket description",
                tokenSold,
                totalTokens,
                1,
                3,
                System.currentTimeMillis() + 1000000L,
                "",
                "",
                "icon url",
                "PMT",
                false
        );
        investMainItem.iconResourceId = R.mipmap.ic_logo;
        objects.add(investMainItem);
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

        ArrayList<Integer> screenshotsIds = new ArrayList<>();
        screenshotsIds.add(R.drawable.ico_screenshot1);
        screenshotsIds.add(R.drawable.ico_screenshot2);
        screenshotsIds.add(R.drawable.ico_screenshot3);
        screenshotsIds.add(R.drawable.ico_screenshot4);
        screenshotsIds.add(R.drawable.ico_screenshot5);
        screenshotsIds.add(R.drawable.ico_screenshot6);
        screenshotsIds.add(R.drawable.ico_screenshot7);
        screenshotsIds.add(R.drawable.ico_screenshot8);

        objects.add(new ScreenShotBody().fromIds(screenshotsIds));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWETYPE_IMAGE_GALLERY);

        objects.add(new InvestTitle("Advisors"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
// for
        objects.add(new InvestMember("Chandler Guo", "Crypto investor. I have been engaged in the field of cryptocurrency for a long time, so I can confidently state about the great possibilities of the PlayMarket 2.0 platform. Everyone knows the advantages of the world of digital currencies, the main one of which is the absence of any external control both in use and in production. More and more people are attracted to the delicious possibilities of the crypto sphere, in particular crowdfunding with cryptocurrencies. The project is interesting not only in terms of investments, it will also be especially attractive for developers. Developers can not only place their applications for users, but at the same time create and develop new apps using financial help from investors. And it is all on the same PlayMarket2.0 platform! In my opinion, the PlayMarket2.0 project is an interesting, innovative and very attractive solution for everyone.", R.drawable.ico_advisors_chandler, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Larry Bates", "Chief Security Officer/President of Bitland Global. Chris Bates is a telecommunications and cyber-security specialist. He received a B.S. in Psychology from Indiana University in 2007 and a M.S. in Telecommunications in 2009 with a focus in immersive mediated environments. During the first M.S. he worked on creating digital algorithms that generated music with human interaction. He received his M.S. in Cyber-Security for enterprise scale organizations in July, 2016 from Colorado Technical University. Currently he is Chief Security Officer/President for Bitland Global. The project focuses on utilizing blockchain technology to register land titles and record new plots of unregistered land with the larger goal of settling land disputes and unlocking land capital in developing countries. With the aim to help bring transparency to governments and bring development to disenfranchised areas around the world, Chris wants to bridge the gap between technology and the people, in order to realize the economy of the future. In addition to technological pursuits, Chris composes music and performs in his free time and his name can be found on the roster of the music label Strong Roots Records. ", R.drawable.ico_advisors_bates, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Artem Zverev", "Founder of BitBaza, co-owner of AK47pool, a mining pool. I have been engaged in cryptocurrency mining since 2013, and the PlayMarket 2.0 project interested me first of all with its unusual mining mechanism. In fact, this is not a traditional way, it is more like the cooperation of users. At the moment, Google Play earns almost 30% of the total income from the sale of applications and then spends it at its discretion. PlayMarket 2.0 offers to divide the income received among the cooperating members: the commission for the developer is significantly reduced; the owners of the nodes that help the network work receive a reward in the form of a percentage of the turnover. All these advantages show that working with the PlayMarket 2.0 project can be more profitable than cryptocurrency mining.", R.drawable.ico_advisors_zverev, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Manie Eagar", "Chairman of the Blockchain Association of Canada, Director of BC Blockchain Ecosystem Consortium. Users are increasingly relying on mobile technology to navigate everyday activities. PlayMarket 2.0, a decentralized Android app store, combines an ICO developer platform and crypto exchange to support a sustainable economic model for developers, investors, and users by tokenizing the mobile application market.", R.drawable.ico_advisors_eagar, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Roman Povolotski", "Leader of the Cyber Russia project. We live in a world of huge information flows, with which it is necessary to do something: to process, store, receive and share. IT-technologies have penetrated almost all spheres of human life. And there is nothing wrong with this — modern information technologies greatly simplify our life. Today, the largest IT companies in the world are Google, Apple, Microsoft. I got interested in this area a long time ago, starting with e-sports, then switched over to game development. During the whole time, I encountered various difficulties, for example, the periodic change in the rules for publishing applications, account lockout and the practical impossibility of appealing against it. In my opinion, the PlayMarket 2.0 project is an ideal innovative solution to such problems. This is a convenient product for both users and developers.»", R.drawable.ico_advisors_povolotckii, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Aaron Koenig", "Entrepreneur, consultant, writer and film producer, specialised in Bitcoin and Blockchain technology. He has been actively promoting Bitcoin and Blockchain technology since June 2011 by writing about it and by organising events and meetups. He has also been a speaker and panelist at numerous conferences. Aaron is the author of the books A Beginner's Guide to Bitcoin and Austrian Economics and Cryptocoins - Investing in Digital Currencies. Holding a master in communication and marketing from the Berlin Unversity of the Arts, Aaron has been working in the creative internet industry since 1994. He has created websites for clients such as MTV, ARD German Television, Die Zeit, Der Stern, Milka Chocolate and many others. His company Bitfilm has been working in production, distribution and event management for clients such as Deutsche Telekom, Vodafone, Kabel NRW, Swisscom, Nokia, Microsoft, Adobe, Intel, Apple, Axel Springer and many more.", R.drawable.ico_advisors_koenig, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objects.add(new InvestMember("Ilya Churakov", "General Director of Expert System. Now it is impossible to imagine our life without the Internet, smartphones and other digital gadgets. They are so tightly integrated into our lives that we react very painfully to their absence. Virtual reality has penetrated into all spheres of our life. As an old crypto enthusiast, I got carried away by the PlayMarket 2.0 project. Its key idea is freedom and independence, what we are striving for. The unique blockchain system is a good alternative to the existing centralized services, which can be replaced with decentralized systems. Penetrating all areas, blocking and smart contracts have found their place in the mobile app market. Mobile app developers are very smart guys. They should not lose excellent opportunities of the PlayMarket 2.0 project. Imagine: a built-in crowdfunding / crowdinvesting site, a quick and easy way of doing your own ICO, low commission — why should you pay extra money to third parties? I am sure, PlayMarket is a delicious piece of pie that everyone will enjoy! ", R.drawable.ico_advisors_churakov, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        //
        objects.add(new InvestTitle("Our team"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);

        objects.add(new InvestMember("Oleg Linok", "He has been engaged in mobile app development for over eight years. In 2015, he began his activities in the blockchain industry. In early 2017, he gathered a team of leading professionals in their fields. At present, he is the CEO of Crypton.Studio", R.drawable.ico_team_olinok, null));
        objects.add(new InvestMember("Aldar Sandanov", "From 2008 to 2016, he headed the IT Department at Promregionbank, was a member of the Board, the Credit and Finance Committees. He was responsible for the current operations of all Bank’s information systems, involved in creating the IT development and security strategy.", R.drawable.ico_team_asandanov, null));
        objects.add(new InvestMember("Ilya Solovyov", "Over six years of legal practice. Being an expert in legal support of IT companies, consumer protection, and construction defect litigation support, he protected the interests of over 100 clients. His core competencies include civil law and information technology law.", R.drawable.ico_team_isoloviev, null));
        objects.add(new InvestMember("Mikhail Vinertsev", "From 2011 to 2016, he worked for Promregionbank. He started as an IT department specialist and later was promoted to the Head of the IT Department. In the bank, Mikhail was engaged in developing his own gateways to work with Electronic Interagency Interaction System (SMEW), the State Information Systems for State and Municipal Payments (GIS GMOs), and Retail Banking System (RBS). He successfully integrated a remote banking service system with various banking software. Since 2015, he has been working as the technical director of VLSystem LLC, a company engaged in developing and implementing various accounting and data analysis systems. Two years of experience in blockchain development.", R.drawable.ico_team_mvinertsev, null));
        objects.add(new InvestMember("Alexey Pronin", "He graduated from the Department of Information Systems and Technology (Tomsk State University of Control Systems and Radioelectronics). In 2014, he joined Synthesis of Intelligent Systems as a front-end developer. He is fluent in a number of languages such as HTML, C#, PHP, JavaScript, and NodeJS. One year of experience in blockchain development.", R.drawable.ico_team_apronin, null));
        objects.add(new InvestMember("Alex Maynov", "Over three years of experience in smart contracts and blockchain development. Previously, he was engaged in android app development for four years.", R.drawable.ico_team_amaynov, null));
        objects.add(new InvestMember("Anton Khryachkov", "From 2014 to 2016, he worked for MapInfo as an android developer. He developed and launched a mobile app — Rental of Special Equipment.", R.drawable.ico_team_akhryachkov, null));
        objects.add(new InvestMember("Ilya Molchanov", "Over six years in web design, about ten years in design, printing and illustration.", R.drawable.ico_team_imolchanov, null));
        objects.add(new InvestMember("Aleksandr Bragin", "He graduated from the Institute of Power Engineering (Tomsk Polytechnic University) with a degree in electric power and electrical engineering. Since 2014, he worked as a back-end developer for R70 Web Studio.", R.drawable.ico_team_abragin, null));
        objects.add(new InvestMember("Ilya Mikheev", "In 2010, he graduated from the TUSUR Radio Engineering Department. In 2017, he graduated from TUSUR with a degree in Information Security. Ilya headed the Information Security Department of a bank. He participated in the development of a combined GPS / Glonass / Galileo receiver.", R.drawable.ico_team_imiheev, null));
        objects.add(new InvestMember("Rosa Gallyamova", "She has been writing texts for more than three years. Rosa has implemented over fifty projects as a copywriter, rewriter, content manager, and proofreader. In parallel, Rosa is involved in the management of a news portal, as well as preparing a project about one famous poet of the 17st century.", R.drawable.ico_team_rgallyamova, null));
        objects.add(new InvestMember("Marina Korsikova", "She graduated from the Tomsk University of Control Systems and Radioelectronics with a bachelor’s degree in Applied Informatics in Economics. She also holds a bachelor’s degree in Technical Translation from the Tomsk Polytechnic University. She is fluent in three foreign languages: English, Spanish and Turkish. Over two years of experience in translation of technical documents.", R.drawable.ico_team_mkorsikova, null));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_MEMBER);

//for
        objects.add(new InvestTitle("Contacts"));
        objectViewType.add(InvestScreenAdapter.INVEST_VIEWTYPE_TITLE);
        investTempPojo.objects = objects;
        investTempPojo.objectViewType = objectViewType;
        return investTempPojo;
    }
}


