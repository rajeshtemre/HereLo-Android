package com.tv.herelo.MyProfile.YourHereLo.News.NewsMainModals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shoeb on 12/5/17.
 */
public class NewsModalMain {


    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("heading")
    @Expose
    private String heading;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("text ")
    @Expose
    private String text;
    @SerializedName("data")
    @Expose
    private List<NewsMainDatum> data = null;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<NewsMainDatum> getData() {
        return data;
    }

    public void setData(List<NewsMainDatum> data) {
        this.data = data;
    }
}
