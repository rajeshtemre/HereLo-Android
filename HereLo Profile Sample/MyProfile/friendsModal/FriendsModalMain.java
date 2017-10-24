package com.tv.herelo.MyProfile.friendsModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.MyProfile.followersmodal.Follower_Following_FriendsModal;

import java.util.List;

/**
 * Created by Shoeb on 25/1/17.
 */
public class FriendsModalMain {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("data")
    @Expose
    private List<Follower_Following_FriendsModal> data = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Follower_Following_FriendsModal> getData() {
        return data;
    }

    public void setData(List<Follower_Following_FriendsModal> data) {
        this.data = data;
    }

}
