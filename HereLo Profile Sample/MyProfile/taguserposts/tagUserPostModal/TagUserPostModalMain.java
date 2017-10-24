package com.tv.herelo.MyProfile.taguserposts.tagUserPostModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 18/11/16.
 */
public class TagUserPostModalMain {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Posts")
    @Expose
    private List<DatumHome> posts = new ArrayList<DatumHome>();

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
     * @return The posts
     */
    public List<DatumHome> getPosts() {
        return posts;
    }

    /**
     * @param posts The Posts
     */
    public void setPosts(List<DatumHome> posts) {
        this.posts = posts;
    }
}
