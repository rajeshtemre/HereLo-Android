package com.tv.herelo.MyProfile.followersmodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.constants.Constant;

import java.util.List;

/**
 * Created by shoeb on 10/6/16.
 */
public class Follower_Following_FriendsModal {


    @SerializedName("user_id")
    @Expose
    private int user_id = 0;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("is_following")
    @Expose
    private int isFollowing = 0;


    @SerializedName("group")
    @Expose
    private List<Follower_Following_Friends_Group_Datum> group = null;

    public List<Follower_Following_Friends_Group_Datum> getGroup() {
        return group;
    }

    public void setGroup(List<Follower_Following_Friends_Group_Datum> group) {
        this.group = group;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The image
     */
    public String getImage() {


        if (image.contains("d1ii8ekxby58dl.cloudfront.net") || image.contains("herelo.s3.amazonaws.com")) {
            return image;
        } else {
            return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME + image;
        }

    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The pstatus
     */
    public String getPstatus() {
        return pstatus;
    }

    /**
     * @param pstatus The pstatus
     */
    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    /**
     * @return The isFollowing
     */
    public int getIsFollowing() {
        return isFollowing;
    }

    /**
     * @param isFollowing The is_following
     */
    public void setIsFollowing(int isFollowing) {
        this.isFollowing = isFollowing;
    }
}
