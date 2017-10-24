package com.tv.herelo.MyProfile.map_cluster.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 22/11/16.
 */
public class ClusterMapModalMain {


    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("Posts")
    @Expose
    private List<ClusterMapPosts> posts = new ArrayList<ClusterMapPosts>();

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
     * The posts
     */
    public List<ClusterMapPosts> getPosts() {
        return posts;
    }

    /**
     *
     * @param posts
     * The Posts
     */
    public void setPosts(List<ClusterMapPosts> posts) {
        this.posts = posts;
    }
}
