package com.blockchain.store.playmarket.repositories;

import android.util.Log;

import com.blockchain.store.playmarket.api.RestApi;
import com.blockchain.store.playmarket.data.entities.IpfsConfigResponse;
import com.blockchain.store.playmarket.data.entities.IpfsPeersResponse;
import com.blockchain.store.playmarket.data.entities.IpfsPingResponse;
import com.blockchain.store.playmarket.data.entities.IpfsVersionResponse;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class IpfsRepository {
    private IpfsRepositoryData data;

    public Observable<IpfsRepositoryData> getIpfsData() {
        data = new IpfsRepositoryData();
        return getIpfsConfig().map(result -> result)
                .flatMap(result -> {
                    data.config = result;
                    return getIpfsPeers();
                })
                .flatMap(result -> {
                    data.peers = result;
                    return getIpfsVersion();
                })
                .map(result -> {
                    data.version = result;
                    return data;
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<IpfsPingResponse> pingIpfs(String peerId) {
        return RestApi.getServerApi().pingToIpfs(peerId);
    }

    public Observable<IpfsPeersResponse> getIpfsPeers() {
        return RestApi.getServerApi().ipfsGetPeers();
    }

    public Observable<IpfsConfigResponse> getIpfsConfig() {
        return RestApi.getServerApi().getPeersIdFromConfig();
    }

    public Observable<IpfsVersionResponse> getIpfsVersion() {
        return RestApi.getServerApi().ipfsGetVersion();
    }

    public void shuwDownIpfs(){
        RestApi.getServerApi().shutdownIpfs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::onShutdown, this::onShutDownError);
    }

    private void onShutdown(ResponseBody responseBody) {
    }

    private void onShutDownError(Throwable throwable) {

    }

    public class IpfsRepositoryData {
        public IpfsPeersResponse peers;
        public IpfsConfigResponse config;
        public IpfsVersionResponse version;
    }
}
