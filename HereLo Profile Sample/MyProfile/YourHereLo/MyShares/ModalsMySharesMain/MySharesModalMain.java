package com.tv.herelo.MyProfile.YourHereLo.MyShares.ModalsMySharesMain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shoeb on 11/5/17.
 */
public class MySharesModalMain {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("totalshare")
    @Expose
    private int totalshare;
    @SerializedName("mnthshare")
    @Expose
    private int mnthshare;
    @SerializedName("refralshare")
    @Expose
    private int refralshare;
    @SerializedName("totalrefral")
    @Expose
    private int totalrefral;
    @SerializedName("data")
    @Expose
    private List<ReferralsUsersDatum> data = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalshare() {
        return totalshare;
    }

    public void setTotalshare(int totalshare) {
        this.totalshare = totalshare;
    }

    public int getMnthshare() {
        return mnthshare;
    }

    public void setMnthshare(int mnthshare) {
        this.mnthshare = mnthshare;
    }

    public int getRefralshare() {
        return refralshare;
    }

    public void setRefralshare(int refralshare) {
        this.refralshare = refralshare;
    }

    public int getTotalrefral() {
        return totalrefral;
    }

    public void setTotalrefral(int totalrefral) {
        this.totalrefral = totalrefral;
    }

    public List<ReferralsUsersDatum> getData() {
        return data;
    }

    public void setData(List<ReferralsUsersDatum> data) {
        this.data = data;
    }
}
