package com.blockchain.store.playmarket.utilities.net;

import android.location.Location;
import android.util.Log;

import com.blockchain.store.playmarket.BuildConfig;
import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.ExchangeRate;
import com.blockchain.store.playmarket.data.entities.Node;
import com.blockchain.store.playmarket.utilities.Constants;
import com.orhanobut.hawk.Hawk;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;
import java.util.Set;

import de.measite.minidns.hla.ResolverApi;
import de.measite.minidns.hla.ResolverResult;
import de.measite.minidns.record.TXT;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by samsheff on 04/09/2017.
 */

public class NodeUtils {
    private static final String TAG = "NodeUtils";
    private static final String NODES_DNS_SERVER_MAINNET = "mainnet.playmarket.io";
    private static final String NODES_DNS_SERVER_DEBUG = "testnet.playmarket.io";
    private static final String NODES_DNS_SERVER_LEGACY = "nodes.playmarket.io";

    private static final String NODES_DNS_SERVER = (BuildConfig.BUILD_TYPE.contentEquals("mainnet") ? NODES_DNS_SERVER_MAINNET : NODES_DNS_SERVER_DEBUG);
    private static final String IP_LOOKUP_URL = "http://ip-api.com/line";


    private static ArrayList<Node> getNodesList(String domain) throws IOException {
        ResolverResult<TXT> result = ResolverApi.INSTANCE.resolve(domain, TXT.class);
        if (!result.wasSuccessful()) {
            throw new RuntimeException("Dns look up error. " + result.getResponseCode());
        }
        Set<TXT> answers = result.getAnswers();
        StringBuilder nodesList = new StringBuilder("");
        for (TXT a : answers) {
            nodesList.append(a.getText());
        }
        String[] nodes = nodesList.toString().split("\\|");
        return convertNodesToLocation(nodes);
    }

    private static ArrayList<Node> convertNodesToLocation(String[] nodes) {
        ArrayList<Node> nodesList = new ArrayList<>();
        for (String node : nodes) {
            Location location = new Location("");
            String[] locationSplit = node.split(":");
            location.setLatitude(Double.parseDouble(locationSplit[1]));
            location.setLongitude(Double.parseDouble(locationSplit[2]));
            nodesList.add(new Node(locationSplit[0], location));
        }
        return nodesList;
    }

    public static ArrayList getCoordinates() throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet(IP_LOOKUP_URL);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET", responseBody);

        String[] geoInfo = responseBody.split("\n");

        ArrayList coords = new ArrayList();
        coords.add(Double.valueOf(geoInfo[7]));
        coords.add(Double.valueOf(geoInfo[8]));

        return coords;
    }


    public static ArrayList<Node> sortNodesByNearest(ArrayList<Node> nodes, Location location) {
        Log.d(TAG, "getNearestNodes: nodes before sorting " + nodes.toString());
        Collections.sort(nodes, new NodeComparator(location));
        Log.d(TAG, "getNearestNodes: nodes after sorting " + nodes.toString());
        return nodes;
    }


    public Observable<Node> getNearestNode(Location location) {
        return Observable.create(subscriber -> {
            try {
                String currencyCode = Currency.getInstance(Locale.getDefault()).getCurrencyCode();
                ArrayList<Node> nodes = NodeUtils.getNodesList(NodeUtils.NODES_DNS_SERVER);
                ArrayList<Node> nearestNodeIP = NodeUtils.sortNodesByNearest(nodes, location);
                for (Node node : nearestNodeIP) {
                    Response<ExchangeRate> execute = null;
                    try {
                        execute = new RestApi().getCustomUrlApi(RestApi.getCheckUrlEndpointByNode(node.address)).getExchangeRate(currencyCode).execute();
                        if (!execute.body().currency.name.equalsIgnoreCase("PMC")) {
                            execute.body().currency.name = currencyCode;
                        }
                        Hawk.put(Constants.CURRENT_CURRENCY, execute.body());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (execute != null && execute.isSuccessful()) {
                        subscriber.onNext(node);
                        return;
                    }
                }
                subscriber.onError(new Exception("Failed to find node"));
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    private static class NodeComparator implements Comparator<Node> {
        Location origin;

        NodeComparator(Location origin) {
            this.origin = origin;
        }

        public int compare(Node left, Node right) {
            return Float.compare(origin.distanceTo(left.location), origin.distanceTo(right.location));
        }

    }
}
