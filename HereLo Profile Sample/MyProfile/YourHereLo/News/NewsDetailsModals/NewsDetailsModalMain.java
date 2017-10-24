package com.tv.herelo.MyProfile.YourHereLo.News.NewsDetailsModals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shoeb on 18/5/17.
 */
public class NewsDetailsModalMain {

    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("data")
    @Expose
    private NewsDetailsModalDatum data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public NewsDetailsModalDatum getData() {
        return data;
    }

    public void setData(NewsDetailsModalDatum data) {
        this.data = data;
    }

}
