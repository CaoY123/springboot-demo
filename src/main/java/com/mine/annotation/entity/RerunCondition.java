package com.mine.annotation.entity;

import java.util.Date;

/**
 * Created by xuhu on 2017/10/26.
 */
public class RerunCondition {
    private String accountId;
    private Date fromDate;
    private Date toDate;
    private String isForce;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getIsForce() {
        return isForce;
    }

    public void setIsForce(String isForce) {
        this.isForce = isForce;
    }

    @Override
    public String toString() {
        return "RerunCondition{" +
                "accountId='" + accountId + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", isForce='" + isForce + '\'' +
                '}';
    }
}
