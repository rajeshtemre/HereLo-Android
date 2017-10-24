package com.tv.herelo.MyProfile.map_cluster.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.constants.Constant;

/**
 * Created by shoeb on 22/11/16.
 */
public class ClusterMapPostMedium {

    @SerializedName("post_id")
    @Expose
    private Integer postId;
    @SerializedName("thumb_url")
    @Expose
    private String thumbUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("img_width")
    @Expose
    private String imgWidth;
    @SerializedName("img_height")
    @Expose
    private String imgHeight;

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
        return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME+thumbUrl;
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
        return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME+url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     * The imgWidth
     */
    public String getImgWidth() {
        return imgWidth;
    }

    /**
     *
     * @param imgWidth
     * The img_width
     */
    public void setImgWidth(String imgWidth) {
        this.imgWidth = imgWidth;
    }

    /**
     *
     * @return
     * The imgHeight
     */
    public String getImgHeight() {
        return imgHeight;
    }

    /**
     *
     * @param imgHeight
     * The img_height
     */
    public void setImgHeight(String imgHeight) {
        this.imgHeight = imgHeight;
    }

}
