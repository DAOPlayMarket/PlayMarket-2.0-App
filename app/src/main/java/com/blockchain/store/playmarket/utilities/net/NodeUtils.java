package com.blockchain.store.playmarket.utilities.net;

import android.location.Location;
import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.Node;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import de.measite.minidns.hla.ResolverApi;
import de.measite.minidns.hla.ResolverResult;
import de.measite.minidns.record.TXT;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

/**
 * Created by samsheff on 04/09/2017.
 */

public class NodeUtils {
    private static final String TAG = "NodeUtils";
    private static final String NODES_DNS_SERVER = "nodes.playmarket.io";
    private static final String IP_LOOKUP_URL = "http://ip-api.com/line";


    private static ArrayList<Node> getNodesList(String domain) throws IOException {
        ResolverResult<TXT> result = ResolverApi.INSTANCE.resolve(domain, TXT.class);
        if (!result.wasSuccessful()) {
            throw new RuntimeException("Dns look up error. " + result.getResponseCode());
        }

        Set<TXT> answers = result.getAnswers();
        String nodesList = "";

        for (TXT a : answers) {
            nodesList = a.getText();
        }
        String[] nodes = nodesList.split("\\|");

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

    public static String getNearestNode(String[] nodes, Double lat, Double lon) {

        int nearestNodeIndex = 0;

        double minNodeLat = 300;
        double minNodeLon = 300;
        for (int i = 0; i < nodes.length; i++) {
            String[] splitNode = nodes[i].split(":");

            if (i == 0 || (Math.abs(lat - Double.valueOf(splitNode[1])) < minNodeLat && Math.abs(lon - Double.valueOf(splitNode[2])) < minNodeLon)) {
//                minNodeLat = Math.abs(lon - Double.valueOf(splitNode[1]));
                minNodeLat = Math.abs(lat - Double.valueOf(splitNode[1]));
                minNodeLon = Math.abs(lon - Double.valueOf(splitNode[2]));

                nearestNodeIndex = i;
            }
        }

        return nodes[nearestNodeIndex].split(":")[0];
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
                ArrayList<Node> nodes = NodeUtils.getNodesList(NodeUtils.NODES_DNS_SERVER);
                ArrayList<Node> nearestNodeIP = NodeUtils.sortNodesByNearest(nodes, location);
                for (Node node : nearestNodeIP) {
                    Response<ResponseBody> execute = null;
                    try {
                        execute = RestApi.getCustomUrlApi(RestApi.getCheckUrlEndpointByNode(node.address)).checkAvailability().execute();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (execute != null && execute.isSuccessful()) {
                        subscriber.onNext(node);
                        return;
                    }
                }
                subscriber.onCompleted();
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
