package com.tv.herelo.MyProfile.myprofilemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 19/8/16.
 */
public class MyProfileModalPost {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("tag_people")
    @Expose
    private String tagPeople;
    @SerializedName("is_public")
    @Expose
    private Integer isPublic;
    @SerializedName("is_friends")
    @Expose
    private Integer isFriends;
    @SerializedName("hashtag_id")
    @Expose
    private String hashtagId;
    @SerializedName("post_type")
    @Expose
    private Integer postType;
    @SerializedName("post_view_count")
    @Expose
    private Integer postViewCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("location")
    @Expose
    private String location;
    @SerializedName("total_comments")
    @Expose
    private Integer totalComments;
    @SerializedName("reherelo")
    @Expose
    private Integer reherelo;
    @SerializedName("like")
    @Expose
    private Integer like;
    @SerializedName("dislike")
    @Expose
    private Integer dislike;
    @SerializedName("post_media")
    @Expose
    private List<MyProfileModalPostMedium> postMedia = new ArrayList<MyProfileModalPostMedium>();

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
     * The tagPeople
     */
    public String getTagPeople() {
        return tagPeople;
    }

    /**
     *
     * @param tagPeople
     * The tag_people
     */
    public void setTagPeople(String tagPeople) {
        this.tagPeople = tagPeople;
    }

    /**
     *
     * @return
     * The isPublic
     */
    public Integer getIsPublic() {
        return isPublic;
    }

    /**
     *
     * @param isPublic
     * The is_public
     */
    public void setIsPublic(Integer isPublic) {
        this.isPublic = isPublic;
    }

    /**
     *
     * @return
     * The isFriends
     */
    public Integer getIsFriends() {
        return isFriends;
    }

    /**
     *
     * @param isFriends
     * The is_friends
     */
    public void setIsFriends(Integer isFriends) {
        this.isFriends = isFriends;
    }

    /**
     *
     * @return
     * The hashtagId
     */
    public String getHashtagId() {
        return hashtagId;
    }

    /**
     *
     * @param hashtagId
     * The hashtag_id
     */
    public void setHashtagId(String hashtagId) {
        this.hashtagId = hashtagId;
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
     * The postViewCount
     */
    public Integer getPostViewCount() {
        return postViewCount;
    }

    /**
     *
     * @param postViewCount
     * The post_view_count
     */
    public void setPostViewCount(Integer postViewCount) {
        this.postViewCount = postViewCount;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The location
     */
    public String getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The totalComments
     */
    public Integer getTotalComments() {
        return totalComments;
    }

    /**
     *
     * @param totalComments
     * The total_comments
     */
    public void setTotalComments(Integer totalComments) {
        this.totalComments = totalComments;
    }

    /**
     *
     * @return
     * The reherelo
     */
    public Integer getReherelo() {
        return reherelo;
    }

    /**
     *
     * @param reherelo
     * The reherelo
     */
    public void setReherelo(Integer reherelo) {
        this.reherelo = reherelo;
    }

    /**
     *
     * @return
     * The like
     */
    public Integer getLike() {
        return like;
    }

    /**
     *
     * @param like
     * The like
     */
    public void setLike(Integer like) {
        this.like = like;
    }

    /**
     *
     * @return
     * The dislike
     */
    public Integer getDislike() {
        return dislike;
    }

    /**
     *
     * @param dislike
     * The dislike
     */
    public void setDislike(Integer dislike) {
        this.dislike = dislike;
    }

    /**
     *
     * @return
     * The postMedia
     */
    public List<MyProfileModalPostMedium> getPostMedia() {
        return postMedia;
    }

    /**
     *
     * @param postMedia
     * The post_media
     */
    public void setPostMedia(List<MyProfileModalPostMedium> postMedia) {
        this.postMedia = postMedia;
    }
}
