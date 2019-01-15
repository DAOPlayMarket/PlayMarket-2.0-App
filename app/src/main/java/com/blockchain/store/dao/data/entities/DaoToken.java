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
        long balance = Long.valueOf(daoBalance);
        value = balance - getWithdraw();
        if (value > 0) {
            value = value * multiplier;
            value = value * 100 / TotalPMT;
            value = safePerc(total, value);
            value = value / (multiplier / 100);
        }

        return value;
    }

    private long safePerc(long x, long y) {
        if (x == 0) {
            return 0;
        }
        long z = x * y;
        z = z / 10_000;
        return z;
    }

}
