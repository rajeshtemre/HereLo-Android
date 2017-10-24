package com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shoeb on 9/5/17.
 */
public class ActivityResponseModalMain {
    @SerializedName("msg")
    @Expose
    private String msg = "Seems like there is HereLo for your request!";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @SerializedName("status")
    @Expose
    private int status = 0;
    @SerializedName("data")
    @Expose
    private List<ActivityModalDatum> data = new ArrayList<>();

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ActivityModalDatum> getData() {
        return data;
    }

    public void setData(List<ActivityModalDatum> data) {
        this.data = data;
    }
}
