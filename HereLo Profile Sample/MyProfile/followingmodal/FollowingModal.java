package com.tv.herelo.MyProfile.followingmodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.MyProfile.followersmodal.Follower_Following_FriendsModal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 10/6/16.
 */
public class FollowingModal {

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
    @SerializedName("following")
    @Expose
    private List<Follower_Following_FriendsModal> following = new ArrayList<Follower_Following_FriendsModal>();

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
     * @return The following
     */
    public List<Follower_Following_FriendsModal> getFollowing() {
        return following;
    }

    /**
     * @param following The following
     */
    public void setFollowing(List<Follower_Following_FriendsModal> following) {
        this.following = following;
    }

}
