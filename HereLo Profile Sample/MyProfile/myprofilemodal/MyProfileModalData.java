package com.tv.herelo.MyProfile.myprofilemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 19/8/16.
 */
public class MyProfileModalData {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("cover_pic")
    @Expose
    private String coverPic;
    @SerializedName("social_type")
    @Expose
    private String socialType;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("birth_country")
    @Expose
    private String birthCountry;
    @SerializedName("birth_state")
    @Expose
    private String birthState;
    @SerializedName("birth_city")
    @Expose
    private String birthCity;
    @SerializedName("high_school")
    @Expose
    private String highSchool;
    @SerializedName("college")
    @Expose
    private String college;
    @SerializedName("favmovie")
    @Expose
    private String favmovie;
    @SerializedName("favtvshow")
    @Expose
    private String favtvshow;
    @SerializedName("favband")
    @Expose
    private String favband;
    @SerializedName("favsportsteam")
    @Expose
    private String favsportsteam;
    @SerializedName("favsport")
    @Expose
    private String favsport;
    @SerializedName("favgenreofmovie")
    @Expose
    private String favgenreofmovie;
    @SerializedName("favgenreofmusic")
    @Expose
    private String favgenreofmusic;
    @SerializedName("referUserId")
    @Expose
    private String referUserId;
    @SerializedName("user_status")
    @Expose
    private Integer userStatus;
    @SerializedName("forgotPasswordToken")
    @Expose
    private String forgotPasswordToken;
    @SerializedName("verifytoken")
    @Expose
    private String verifytoken;
    @SerializedName("verifytoeknexpire")
    @Expose
    private String verifytoeknexpire;
    @SerializedName("tokenexpiretime")
    @Expose
    private String tokenexpiretime;
    @SerializedName("is_view")
    @Expose
    private Integer isView;
    @SerializedName("is_user_debugmode_on")
    @Expose
    private Integer isUserDebugmodeOn;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("shares")
    @Expose
    private Integer shares;
    @SerializedName("friends")
    @Expose
    private Integer friends;
    @SerializedName("following")
    @Expose
    private Integer following;
    @SerializedName("followers")
    @Expose
    private Integer followers;
    @SerializedName("herelos")
    @Expose
    private Integer herelos;
    @SerializedName("liked")
    @Expose
    private Integer liked;
    @SerializedName("liked_mine")
    @Expose
    private Integer likedMine;
    @SerializedName("Posts")
    @Expose
    private List<DatumHome> posts = new ArrayList<DatumHome>();


    @SerializedName("s_id")
    @Expose
    private Integer s_id;

    public Integer getC_id() {
        return c_id;
    }

    public void setC_id(Integer c_id) {
        this.c_id = c_id;
    }

    public Integer getS_id() {
        return s_id;
    }

    public void setS_id(Integer s_id) {
        this.s_id = s_id;
    }

    @SerializedName("c_id")
    @Expose
    private Integer c_id;

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username The username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return The pstatus
     */
    public String getPstatus() {
        return pstatus;
    }

    /**
     * @param pstatus The pstatus
     */
    public void setPstatus(String pstatus) {
        this.pstatus = pstatus;
    }

    /**
     * @return The email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return The image
     */
    public String getImage() {
        return image;
    }

    /**
     * @param image The image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * @return The coverPic
     */
    public String getCoverPic() {
        return coverPic;
    }

    /**
     * @param coverPic The cover_pic
     */
    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    /**
     * @return The socialType
     */
    public String getSocialType() {
        return socialType;
    }

    /**
     * @param socialType The social_type
     */
    public void setSocialType(String socialType) {
        this.socialType = socialType;
    }

    /**
     * @return The socialId
     */
    public String getSocialId() {
        return socialId;
    }

    /**
     * @param socialId The social_id
     */
    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    /**
     * @return The deviceType
     */
    public String getDeviceType() {
        return deviceType;
    }

    /**
     * @param deviceType The device_type
     */
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    /**
     * @return The deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     * @param deviceToken The device_token
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     * @return The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     * @param lat The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     * @return The lng
     */
    public String getLng() {
        return lng;
    }

    /**
     * @param lng The lng
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     * @return The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     * @param dob The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     * @return The gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * @param gender The gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * @return The birthCountry
     */
    public String getBirthCountry() {
        return birthCountry;
    }

    /**
     * @param birthCountry The birth_country
     */
    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    /**
     * @return The birthState
     */
    public String getBirthState() {
        return birthState;
    }

    /**
     * @param birthState The birth_state
     */
    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    /**
     * @return The birthCity
     */
    public String getBirthCity() {
        return birthCity;
    }

    /**
     * @param birthCity The birth_city
     */
    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    /**
     * @return The highSchool
     */
    public String getHighSchool() {
        return highSchool;
    }

    /**
     * @param highSchool The high_school
     */
    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    /**
     * @return The college
     */
    public String getCollege() {
        return college;
    }

    /**
     * @param college The college
     */
    public void setCollege(String college) {
        this.college = college;
    }

    /**
     * @return The favmovie
     */
    public String getFavmovie() {
        return favmovie;
    }

    /**
     * @param favmovie The favmovie
     */
    public void setFavmovie(String favmovie) {
        this.favmovie = favmovie;
    }

    /**
     * @return The favtvshow
     */
    public String getFavtvshow() {
        return favtvshow;
    }

    /**
     * @param favtvshow The favtvshow
     */
    public void setFavtvshow(String favtvshow) {
        this.favtvshow = favtvshow;
    }

    /**
     * @return The favband
     */
    public String getFavband() {
        return favband;
    }

    /**
     * @param favband The favband
     */
    public void setFavband(String favband) {
        this.favband = favband;
    }

    /**
     * @return The favsportsteam
     */
    public String getFavsportsteam() {
        return favsportsteam;
    }

    /**
     * @param favsportsteam The favsportsteam
     */
    public void setFavsportsteam(String favsportsteam) {
        this.favsportsteam = favsportsteam;
    }

    /**
     * @return The favsport
     */
    public String getFavsport() {
        return favsport;
    }

    /**
     * @param favsport The favsport
     */
    public void setFavsport(String favsport) {
        this.favsport = favsport;
    }

    /**
     * @return The favgenreofmovie
     */
    public String getFavgenreofmovie() {
        return favgenreofmovie;
    }

    /**
     * @param favgenreofmovie The favgenreofmovie
     */
    public void setFavgenreofmovie(String favgenreofmovie) {
        this.favgenreofmovie = favgenreofmovie;
    }

    /**
     * @return The favgenreofmusic
     */
    public String getFavgenreofmusic() {
        return favgenreofmusic;
    }

    /**
     * @param favgenreofmusic The favgenreofmusic
     */
    public void setFavgenreofmusic(String favgenreofmusic) {
        this.favgenreofmusic = favgenreofmusic;
    }

    /**
     * @return The referUserId
     */
    public String getReferUserId() {
        return referUserId;
    }

    /**
     * @param referUserId The referUserId
     */
    public void setReferUserId(String referUserId) {
        this.referUserId = referUserId;
    }

    /**
     * @return The userStatus
     */
    public Integer getUserStatus() {
        return userStatus;
    }

    /**
     * @param userStatus The user_status
     */
    public void setUserStatus(Integer userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * @return The forgotPasswordToken
     */
    public String getForgotPasswordToken() {
        return forgotPasswordToken;
    }

    /**
     * @param forgotPasswordToken The forgotPasswordToken
     */
    public void setForgotPasswordToken(String forgotPasswordToken) {
        this.forgotPasswordToken = forgotPasswordToken;
    }

    /**
     * @return The verifytoken
     */
    public String getVerifytoken() {
        return verifytoken;
    }

    /**
     * @param verifytoken The verifytoken
     */
    public void setVerifytoken(String verifytoken) {
        this.verifytoken = verifytoken;
    }

    /**
     * @return The verifytoeknexpire
     */
    public String getVerifytoeknexpire() {
        return verifytoeknexpire;
    }

    /**
     * @param verifytoeknexpire The verifytoeknexpire
     */
    public void setVerifytoeknexpire(String verifytoeknexpire) {
        this.verifytoeknexpire = verifytoeknexpire;
    }

    /**
     * @return The tokenexpiretime
     */
    public String getTokenexpiretime() {
        return tokenexpiretime;
    }

    /**
     * @param tokenexpiretime The tokenexpiretime
     */
    public void setTokenexpiretime(String tokenexpiretime) {
        this.tokenexpiretime = tokenexpiretime;
    }

    /**
     * @return The isView
     */
    public Integer getIsView() {
        return isView;
    }

    /**
     * @param isView The is_view
     */
    public void setIsView(Integer isView) {
        this.isView = isView;
    }

    /**
     * @return The isUserDebugmodeOn
     */
    public Integer getIsUserDebugmodeOn() {
        return isUserDebugmodeOn;
    }

    /**
     * @param isUserDebugmodeOn The is_user_debugmode_on
     */
    public void setIsUserDebugmodeOn(Integer isUserDebugmodeOn) {
        this.isUserDebugmodeOn = isUserDebugmodeOn;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     * @param updatedAt The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * @return The shares
     */
    public Integer getShares() {
        return shares;
    }

    /**
     * @param shares The shares
     */
    public void setShares(Integer shares) {
        this.shares = shares;
    }

    /**
     * @return The friends
     */
    public Integer getFriends() {
        return friends;
    }

    /**
     * @param friends The friends
     */
    public void setFriends(Integer friends) {
        this.friends = friends;
    }

    /**
     * @return The following
     */
    public Integer getFollowing() {
        return following;
    }

    /**
     * @param following The following
     */
    public void setFollowing(Integer following) {
        this.following = following;
    }

    /**
     * @return The followers
     */
    public Integer getFollowers() {
        return followers;
    }

    /**
     * @param followers The followers
     */
    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    /**
     * @return The herelos
     */
    public Integer getHerelos() {
        return herelos;
    }

    /**
     * @param herelos The herelos
     */
    public void setHerelos(Integer herelos) {
        this.herelos = herelos;
    }

    /**
     * @return The liked
     */
    public Integer getLiked() {
        return liked;
    }

    /**
     * @param liked The liked
     */
    public void setLiked(Integer liked) {
        this.liked = liked;
    }

    /**
     * @return The likedMine
     */
    public Integer getLikedMine() {
        return likedMine;
    }

    /**
     * @param likedMine The liked_mine
     */
    public void setLikedMine(Integer likedMine) {
        this.likedMine = likedMine;
    }

    /**
     * @return The posts
     */
    public List<DatumHome> getPosts() {
        return posts;
    }

    /**
     * @param posts The posts
     */
    public void setPosts(List<DatumHome> posts) {
        this.posts = posts;
    }
}
