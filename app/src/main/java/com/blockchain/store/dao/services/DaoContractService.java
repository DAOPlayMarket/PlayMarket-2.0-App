package com.blockchain.store.dao.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.blockchain.store.dao.database.DaoDatabase;
import com.blockchain.store.dao.database.model.Proposal;
import com.blockchain.store.dao.database.model.Vote;
import com.blockchain.store.dao.repository.DaoTransactionRepository;
import com.blockchain.store.dao.ui.DaoConstants;
import com.blockchain.store.dao.ui.votes_screen.main_votes_screen.MainVotesFragment;
import com.blockchain.store.playmarket.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

public class DaoContractService extends Service {

    private static final String TAG = "DaoContractService";
    private DaoDatabase daoDatabase = Application.getDaoDatabase();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        getContractHistory();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    private void getContractHistory() {
        WebSocket webSocket = createWebSocketConnection(new WebSocketListener());
        webSocket.send("{\"id\":1,\"jsonrpc\":\"2.0\",\"params\":[{\"fromBlock\":\"" + DaoConstants.CONTRACT_CREATION_BLOCK_RINKEBY + "\",\"toBlock\":\"latest\",\"address\":\"" + DaoConstants.CONTRACT_ADDRESS_RINKEBY + "\"}],\"method\":\"eth_getLogs\"}");
    }

    private WebSocket createWebSocketConnection(WebSocketListener listener) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url(DaoConstants.INFURA_WEBSOCKET_RINKEBYT).build();
        return okHttpClient.newWebSocket(request, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class WebSocketListener extends okhttp3.WebSocketListener {

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            Log.d(TAG, "onMessage() called with: webSocket = [" + webSocket + "], text = [" + text + "]");
            try {
                JSONArray contractLogs = new JSONObject(text).getJSONArray("result");
                for (int i = 0; i < contractLogs.length(); i ++) {
                    JSONObject log = contractLogs.getJSONObject(i);
                    String topics = log.getJSONArray("topics").get(0).toString();
                    String data = log.getString("data").replace("0x", "");
                    switch (topics) {
                        case DaoConstants.CHANGE_OF_RULES:
                            break;
                        case DaoConstants.PAYMENT:
                            break;
                        case DaoConstants.PROPOSAL_TALLIED:
                            break;
                        case DaoConstants.VOTED:
                            voteHanding(data);
                            break;
                        case DaoConstants.PROPOSAL_ADDED:
                            proposalHanding(data);
                            break;
                    }
                }
                sendBroadcast();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onMessage(webSocket, text);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            Log.d(TAG, "onClosed() called with: webSocket = [" + webSocket + "], code = [" + code + "], reason = [" + reason + "]");
            super.onClosed(webSocket, code, reason);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            Log.d(TAG, "onFailure() called with: webSocket = [" + webSocket + "], t = [" + t + "], response = [" + response + "]");
            super.onFailure(webSocket, t, response);
        }
    }

    private void proposalHanding(String data) {
        Proposal proposal = DaoTransactionRepository.decodeProposal(data);
        if (proposal != null){
            Proposal proposalFromDb = daoDatabase.proposalDao().getById(proposal.proposalID);
            if (proposalFromDb == null) daoDatabase.proposalDao().insert(proposal);
        }
    }

    private void voteHanding(String data) {
        Vote vote = DaoTransactionRepository.decodeVote(data);
        if (vote != null) {
            Proposal proposalFromDb = daoDatabase.proposalDao().getById(vote.proposalID);

            boolean isAlreadyVoted = false;
            for (Vote voteFromDb: proposalFromDb.votes) {
                if (voteFromDb.voter.equals(vote.voter)){
                    isAlreadyVoted = true;
                    break;
                }
            }

            if (!isAlreadyVoted) {
                proposalFromDb.votes.add(vote);
                if (vote.position) proposalFromDb.votesSupport++;
                else proposalFromDb.votesAgainst++;
                daoDatabase.proposalDao().update(proposalFromDb);
            }
        }
    }

    private void sendBroadcast(){
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainVotesFragment.BROADCAST_ACTION);
        broadcastIntent.putExtra("IsSync", true);
        sendBroadcast(broadcastIntent);

    }

}
