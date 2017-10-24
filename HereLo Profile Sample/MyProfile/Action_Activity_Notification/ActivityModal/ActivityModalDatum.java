package com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.constants.Constant;

/**
 * Created by Shoeb on 9/5/17.
 */
public class ActivityModalDatum {

    @SerializedName("tag_id")
    @Expose
    private int tag_id = 0;

    public int getTag_id() {
        return tag_id;
    }

    public void setTag_id(int tag_id) {
        this.tag_id = tag_id;
    }

    @SerializedName("id")
    @Expose
    private int id = 0;
    @SerializedName("sid")
    @Expose
    private int sid = 0;
    @SerializedName("tag_user")
    @Expose
    private String tag_user = "";

    public String getTag_user() {
        return tag_user;
    }

    public void setTag_user(String tag_user) {
        this.tag_user = tag_user;
    }

    @SerializedName("sname")
    @Expose
    private String sname = "";
    @SerializedName("tid")
    @Expose
    private int tid = 0;
    @SerializedName("tname")
    @Expose
    private String tname = "";
    @SerializedName("post_id")
    @Expose
    private int postId = 0;
    @SerializedName("notification_type")
    @Expose
    private String notificationType = "";
    @SerializedName("created_at")
    @Expose
    private String createdAt = "";
    @SerializedName("updated_at")
    @Expose
    private String updatedAt = "";
    @SerializedName("p_type")
    @Expose
    private int pType = 0;
    @SerializedName("media_url")
    @Expose
    private String mediaUrl = "";
    @SerializedName("total_media")
    @Expose
    private int totalMedia = 0;
    @SerializedName("post_text")
    @Expose
    private String postText = "";
    @SerializedName("comment_text")
    @Expose
    private String commentText = "";
    @SerializedName("time")
    @Expose
    private String time = "";
    @SerializedName("simage")
    @Expose
    private String simage = "";

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @SerializedName("owner_id")

    @Expose
    private Integer ownerId=0;
    @SerializedName("owner_name")
    @Expose
    private String ownerName="";
    public String getSimage() {
        return simage;
    }

    public void setSimage(String simage) {
        this.simage = simage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getTname() {
        if (notificationType.equalsIgnoreCase(Constant.following)){
            if (tname==null){
                return "";
            }
            return tname ;
        }
        return tname + "'s";


    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(String notificationType) {
        this.notificationType = notificationType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getPType() {
        return pType;
    }

    public void setPType(int pType) {
        this.pType = pType;
    }

    public String getMediaUrl() {
        return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME  + mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public int getTotalMedia() {
        return totalMedia;
    }

    public void setTotalMedia(int totalMedia) {
        this.totalMedia = totalMedia;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
