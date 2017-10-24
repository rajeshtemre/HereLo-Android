package com.tv.herelo.MyProfile.YourHereLo.MyShares.ModalsMySharesMain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Shoeb on 11/5/17.
 */
public class ReferralsUsersDatum {


    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("pstatus")
    @Expose
    private String pstatus = "";

    public String getPstatus() {
        return pstatus;
    }

    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("is_following")
    @Expose
    private int is_following = 0;

    public int getIs_following() {
        return is_following;
    }

    public void setIs_following(int is_following) {
        this.is_following = is_following;
    }

    @SerializedName("user_id")
    @Expose
    private int user_id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
