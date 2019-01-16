package com.blockchain.store.dao.data.entities;

public class DaoToken {
    public String address;
    public long decimals;
    public long total;  // Total number of tokens transferred to PMFund

    public String name;
    public String symbol;

    public String fund;
    public String withdraw;
    public String daoBalance;
    public String balance;

    public int totalTokensLength = 0;
    public int tokenPositionInArray = 0;

    private long multiplier = 100_000L;
    private long TotalPMT = 30000000000L;

    public long getDaoBalance() {
        return Long.valueOf(daoBalance);
    }

    public long getWithdraw() {
        return Long.valueOf(withdraw);
    }

    public long getFund() {
        return Long.valueOf(fund);
    }

    public long countToken() {
        long value = 0;
        long balance = getDaoBalance();
        value = balance - getWithdraw();
        if (value > 0) {
            value = value * multiplier;
            value = value * 100 / TotalPMT;
            value = safePerc(total, value);
            value = value / (multiplier / 100);
        }

        return value;
    }

    public long totalCountToken() {
        return getFund() + countToken();
    }

    private long safePerc(long x, long y) {
        if (x == 0) {
            return 0;
        }
        long z = x * y;
        z = z / 10_000;
        return z;
    }

    public DaoToken generatePmToken() {
        DaoToken daoToken = new DaoToken();
        daoToken.name = "DAO PlayMarket 2.0";
        daoToken.symbol = "PMT";
        daoToken.address = "0xc1322d8ae3b0e2e437e0ae36388d0cfd2c02f1c9";
        daoToken.decimals = 4;
        return daoToken;
    }

    public long getBalanceWithDecimals() {
        if (Long.valueOf(balance) == 0) {
            return 0;
        } else {
            return (long) (Long.valueOf(balance) / Math.pow(10, decimals));
        }
    }
    public long getDaoBalanceWithDecimals() {
        if (Long.valueOf(daoBalance) == 0) {
            return 0;
        } else {
            return (long) (Long.valueOf(daoBalance) / Math.pow(10, decimals));
        }
    }
}
