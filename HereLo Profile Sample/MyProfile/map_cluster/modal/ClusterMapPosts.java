package com.tv.herelo.MyProfile.map_cluster.modal;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 22/11/16.
 */
public class ClusterMapPosts implements ClusterItem {

    public  ClusterMapPosts(){

    }

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("post_type")
    @Expose
    private Integer postType;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("post_media")
    @Expose
    private List<ClusterMapPostMedium> postMedia = new ArrayList<ClusterMapPostMedium>();

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The postType
     */
    public Integer getPostType() {
        return postType;
    }

    /**
     *
     * @param postType
     * The post_type
     */
    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    /**
     *
     * @return
     * The text
     */
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The lng
     */
    public String getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     * The lng
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     * The postMedia
     */
    public List<ClusterMapPostMedium> getPostMedia() {
        return postMedia;
    }

    /**
     *
     * @param postMedia
     * The post_media
     */
    public void setPostMedia(List<ClusterMapPostMedium> postMedia) {
        this.postMedia = postMedia;
    }

    @Override
    public LatLng getPosition() {
        LatLng mPosition = new LatLng(Float.valueOf(lat),Float.valueOf(lng));
        return mPosition;
    }
}
