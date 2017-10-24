package com.tv.herelo.MyProfile.myprofilemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shoeb on 19/8/16.
 */
public class MyProfileModalPostMedium {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     *
     * @return
     * The postId
     */
    public Integer getPostId() {
        return postId;
    }

    /**
     *
     * @param postId
     * The post_id
     */
    public void setPostId(Integer postId) {
        this.postId = postId;
    }

    /**
     *
     * @return
     * The thumbUrl
     */
    public String getThumbUrl() {
        return thumbUrl;
    }

    /**
     *
     * @param thumbUrl
     * The thumb_url
     */
    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
