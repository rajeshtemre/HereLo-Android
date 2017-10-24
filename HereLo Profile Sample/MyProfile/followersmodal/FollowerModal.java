package com.tv.herelo.MyProfile.followersmodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 10/6/16.
 */
public class FollowerModal {


    @SerializedName("is_more")
    private String is_more = "";

    public String getIs_more() {
        return is_more;
    }

    public void setIs_more(String is_more) {
        this.is_more = is_more;
    }
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("followers")
    @Expose
    private List<Follower_Following_FriendsModal> followers = new ArrayList<Follower_Following_FriendsModal>();

    /**
     * @return The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return The followers
     */
    public List<Follower_Following_FriendsModal> getFollowers() {
        return followers;
    }

    /**
     * @param followers The followers
     */
    public void setFollowers(List<Follower_Following_FriendsModal> followers) {
        this.followers = followers;
    }
}
