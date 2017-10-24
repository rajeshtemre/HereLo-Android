package com.tv.herelo.MyProfile.myprofilemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by shoeb on 8/6/16.
 */
public class MyProfileModalMain {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Profile")
    @Expose
    private MyProfileModalProfile profile;
    @SerializedName("Posts")
    @Expose
    private List<DatumHome> posts = new ArrayList<DatumHome>();

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The profile
     */
    public MyProfileModalProfile getProfile() {
        return profile;
    }

    /**
     *
     * @param profile
     * The Profile
     */
    public void setProfile(MyProfileModalProfile profile) {
        this.profile = profile;
    }

    /**
     *
     * @return
     * The posts
     */
    public List<DatumHome> getPosts() {
        return posts;
    }

    /**
     *
     * @param posts
     * The Posts
     */
    public void setPosts(List<DatumHome> posts) {
        this.posts = posts;
    }


}
