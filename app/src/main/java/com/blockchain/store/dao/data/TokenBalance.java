package com.blockchain.store.dao.data;

public class TokenBalance {

    private String totalPmt;
    private String localBalance;
    private String repositoryBalance;
    private String notLockedBalance;
    private String minBalance;

    public String getTotalPmt() {
        return totalPmt;
    }

    public String getLocalBalance() {
        return localBalance;
    }

    public String getRepositoryBalance() {
        return repositoryBalance;
    }

    public String getNotLockedBalance() {
        return notLockedBalance;
    }

    public String getMinBalance() {
        return minBalance;
    }

    public void setTotalPmt(String totalPmt) {
        totalPmt = String.valueOf(getTokenWithDecimals(totalPmt));
        this.totalPmt = totalPmt;
    }

    public void setLocalBalance(String localBalance) {
        localBalance = String.valueOf(getTokenWithDecimals(localBalance));
        this.localBalance = localBalance;
    }

    public void setRepositoryBalance(String repositoryBalance) {
        repositoryBalance = String.valueOf(getTokenWithDecimals(repositoryBalance));
        this.repositoryBalance = repositoryBalance;
    }

    public void setNotLockedBalance(String notLockedBalance) {
        notLockedBalance = String.valueOf(getTokenWithDecimals(notLockedBalance));
        this.notLockedBalance = notLockedBalance;
    }

    public void setMinBalance(String minBalance) {
        minBalance = String.valueOf(getTokenWithDecimals(minBalance));
        this.minBalance = minBalance;
    }

    private double getTokenWithDecimals(String balance) {
        if (Long.valueOf(balance) == 0) {
            return 0;
        } else {
            return (Double.valueOf(balance) / Math.pow(10, 4));
        }
    }
}
