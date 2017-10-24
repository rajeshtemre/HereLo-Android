package com.tv.herelo.MyProfile.SearchForPeople;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shoeb on 15/7/16.
 */
public class DatumSearchForPeopleModal {


    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("is_following")
    @Expose
    private Integer isFollowing;

    /**
     *
     * @return
     * The username
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The pstatus
     */
    public String getPstatus() {
        return pstatus;
    }

    /**
     *
     * @param pstatus
     * The pstatus
     */
    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return image;
    }

    /**
     *
     * @param image
     * The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The isFollowing
     */
    public Integer getIsFollowing() {
        return isFollowing;
    }

    /**
     *
     * @param isFollowing
     * The is_following
     */
    public void setIsFollowing(Integer isFollowing) {
        this.isFollowing = isFollowing;
    }
}
