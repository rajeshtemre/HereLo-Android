package com.tv.herelo.MyProfile.followersmodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shoeb on 18/5/17.
 */
public class Follower_Following_Friends_Group_Datum {

    @SerializedName("group_id")
    @Expose
    private int groupId;
    @SerializedName("group_name")
    @Expose
    private String groupName;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
