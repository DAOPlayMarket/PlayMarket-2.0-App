package com.blockchain.store.playmarket.utilities.net;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import de.measite.minidns.hla.ResolverApi;
import de.measite.minidns.hla.ResolverResult;
import de.measite.minidns.record.TXT;

/**
 * Created by samsheff on 04/09/2017.
 */

public class NodeUtils {

    public static final String NODES_DNS_SERVER = "nodes.playmarket.io";
    public static final String IP_LOOKUP_URL = "http://ip-api.com/line";

    public static String[] getNodesList(String domain) throws IOException {
        ResolverResult<TXT> result = ResolverApi.INSTANCE.resolve(domain, TXT.class);
        if (!result.wasSuccessful()) {
            return new String[0];
        }
        Set<TXT> answers = result.getAnswers();
        String nodesList = "";

        for (TXT a : answers) {
            nodesList = a.getText();
        }
        String[] nodes = nodesList.split("\\|");

        return nodes;
    }

    public static ArrayList getCoordinates() throws IOException {
        HttpClient client = new DefaultHttpClient();

        HttpGet request = new HttpGet(IP_LOOKUP_URL);

        HttpResponse response;
        response = client.execute(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        Log.d("NET",  responseBody);

        String[] geoInfo =  responseBody.split("\n");

        ArrayList coords = new ArrayList();
        coords.add(Double.valueOf(geoInfo[7]));
        coords.add(Double.valueOf(geoInfo[8]));

        return coords;
    }

    public static String getNearestNode(String[] nodes, Double lat, Double lon) {

        int nearestNodeIndex = 0;

        double minNodeLat = 300;
        double minNodeLon = 300;

        for (int i=0; i < nodes.length; i++) {
            String[] splitNode = nodes[i].split(":");

            if (i==0 || (Math.abs(lat - Double.valueOf(splitNode[1])) < minNodeLat && Math.abs(lon - Double.valueOf(splitNode[2])) < minNodeLon)) {
                minNodeLat = Math.abs(lon - Double.valueOf(splitNode[1]));
                minNodeLon = Math.abs(lon - Double.valueOf(splitNode[2]));

                nearestNodeIndex = i;
            }
        }

        return nodes[nearestNodeIndex].split(":")[0];
    }
}
