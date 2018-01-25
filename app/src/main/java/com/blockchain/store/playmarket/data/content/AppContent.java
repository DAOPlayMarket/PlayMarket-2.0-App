package com.blockchain.store.playmarket.data.content;

import com.blockchain.store.playmarket.utilities.net.APIUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample name for user interfaces created by
 * Android template wizards.
 * <p>
 */
public class AppContent implements Serializable {

    /**
     * An array of sample (dummy) items.
     */
    public List<AppItem> ITEMS = new ArrayList<AppItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public Map<String, AppItem> ITEM_MAP = new HashMap<String, AppItem>();

    public boolean READY = false;
    public boolean IS_LOADING = false;
    public boolean NO_MORE_CONTENT = false;
    public String categoryId = "";
    public int FETCH_COUNT = 4;

    public AppContent(final String category) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (NO_MORE_CONTENT) {
                        return;
                    }

                    categoryId = category;
                    IS_LOADING = true;

                    JSONArray apps;
                    if (category.equals("")) {
                        apps = APIUtils.api.start();
                    } else {
                        apps = APIUtils.api.pageCategory(category, 1, FETCH_COUNT);
                    }

                    for (int i = 0; i < apps.length(); i++) {
                        addItem(createAppItem(apps.getJSONObject(i), i));
                    }

                    if (apps.length() == 0) {
                        NO_MORE_CONTENT = true;
                    }

                    READY = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                IS_LOADING = false;
            }
        });

        thread.start();
    }

    public void loadMoreItems() throws JSONException, IOException {
        if (NO_MORE_CONTENT) {
            return;
        }

        IS_LOADING = true;
        JSONArray apps = APIUtils.api.pageCategory(categoryId, ITEMS.size() + 1, FETCH_COUNT);

        if (apps.length() == 0) {
            NO_MORE_CONTENT = true;
        }

        for (int i = 0; i < apps.length(); i++) {
            addItem(createAppItem(apps.getJSONObject(i), ITEMS.size() + i));
        }
        IS_LOADING = false;
    }

    private void addItem(AppItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private AppItem createAppItem(JSONObject app, int position) throws JSONException {
        String price = app.getString("value");
        String idApp = app.getString("nameApp");
        boolean free = app.getBoolean("free");
        String hashIPFS = app.getString("hashIpfs");

        String icon = null;
        try {
            icon = app.getString("img");
        } catch (org.json.JSONException e) {
            e.printStackTrace();
        }

        return new AppItem(String.valueOf(position), app.getString("idApp"),
                    app.getString("idCTG"), idApp, app.getString("developer"),
                    price, price, free, makeDetails(1), icon, hashIPFS, app.getString("description"),
                    5);
    }

    private String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of name.
     */
    public static class AppItem implements Serializable{
        public final String id;
        public final String appId;
        public final String name;
        public final String developer;
        public final String details;
        public final String price;
        public final String priceWei;
        public final boolean free;
        public String category;
        public String icon;
        public String hashIPFS;
        public String description;
        public int imageCount;

        public AppItem(String id, String appId, String category, String content, String developer, String price, String priceWei, boolean free, String details, String icon, String hashIPFS, String description, int imageCount) {
            this.id = id;
            this.appId = appId;
            this.category = category;
            this.developer = developer;
            this.name = content;
            this.priceWei = priceWei;
            this.details = details;
            this.price = price;
            this.free = free;
            this.icon = icon;
            this.hashIPFS = hashIPFS;
            this.description = description;
            this.imageCount = imageCount;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}
