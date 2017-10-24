package com.tv.herelo.MyProfile.myprofilemodal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.constants.Constant;

/**
 * Created by shoeb on 17/11/16.
 */
public class MyProfileModalProfile {

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    @SerializedName("is_online")
    @Expose
    private String is_online;


    @SerializedName("s_id")
    @Expose
    private int s_id;


    @SerializedName("is_checkin")
    @Expose
    private int is_checkin;

    public int getIs_checkin() {
        return is_checkin;
    }

    public void setIs_checkin(int is_checkin) {
        this.is_checkin = is_checkin;
    }

    public int getC_id() {
        return c_id;
    }

    public void setC_id(int c_id) {
        this.c_id = c_id;
    }

    public int getS_id() {
        return s_id;
    }

    public void setS_id(int s_id) {
        this.s_id = s_id;
    }

    @SerializedName("c_id")
    @Expose
    private int c_id;

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("fname")
    @Expose
    private String fname;
    @SerializedName("lname")
    @Expose
    private String lname;
    @SerializedName("dob")
    @Expose
    private String dob;
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
    @SerializedName("lat")
    @Expose
    private String lat;
    @SerializedName("lng")
    @Expose
    private String lng;
    @SerializedName("pstatus")
    @Expose
    private String pstatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("cover_pic")
    @Expose
    private String coverPic;
    @SerializedName("shares")
    @Expose
    private int shares = 0;
    @SerializedName("friends")
    @Expose
    private int friends;
    @SerializedName("following")
    @Expose
    private int following;
    @SerializedName("followers")
    @Expose
    private int followers;
    @SerializedName("herelos")
    @Expose
    private int herelos;
    @SerializedName("liked")
    @Expose
    private int liked;
    @SerializedName("liked_mine")
    @Expose
    private int likedMine;

    /**
     *
     * @return
     * The id
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(int id) {
        this.id = id;
    }

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
     * The fname
     */
    public String getFname() {
        return fname;
    }

    /**
     *
     * @param fname
     * The fname
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     *
     * @return
     * The lname
     */
    public String getLname() {
        return lname;
    }

    /**
     *
     * @param lname
     * The lname
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     *
     * @return
     * The dob
     */
    public String getDob() {
        return dob;
    }

    /**
     *
     * @param dob
     * The dob
     */
    public void setDob(String dob) {
        this.dob = dob;
    }

    /**
     *
     * @return
     * The birthCountry
     */
    public String getBirthCountry() {
        return birthCountry;
    }

    /**
     *
     * @param birthCountry
     * The birth_country
     */
    public void setBirthCountry(String birthCountry) {
        this.birthCountry = birthCountry;
    }

    /**
     *
     * @return
     * The birthState
     */
    public String getBirthState() {
        return birthState;
    }

    /**
     *
     * @param birthState
     * The birth_state
     */
    public void setBirthState(String birthState) {
        this.birthState = birthState;
    }

    /**
     *
     * @return
     * The birthCity
     */
    public String getBirthCity() {
        return birthCity;
    }

    /**
     *
     * @param birthCity
     * The birth_city
     */
    public void setBirthCity(String birthCity) {
        this.birthCity = birthCity;
    }

    /**
     *
     * @return
     * The highSchool
     */
    public String getHighSchool() {
        return highSchool;
    }

    /**
     *
     * @param highSchool
     * The high_school
     */
    public void setHighSchool(String highSchool) {
        this.highSchool = highSchool;
    }

    /**
     *
     * @return
     * The college
     */
    public String getCollege() {
        return college;
    }

    /**
     *
     * @param college
     * The college
     */
    public void setCollege(String college) {
        this.college = college;
    }

    /**
     *
     * @return
     * The favmovie
     */
    public String getFavmovie() {
        return favmovie;
    }

    /**
     *
     * @param favmovie
     * The favmovie
     */
    public void setFavmovie(String favmovie) {
        this.favmovie = favmovie;
    }

    /**
     *
     * @return
     * The favtvshow
     */
    public String getFavtvshow() {
        return favtvshow;
    }

    /**
     *
     * @param favtvshow
     * The favtvshow
     */
    public void setFavtvshow(String favtvshow) {
        this.favtvshow = favtvshow;
    }

    /**
     *
     * @return
     * The favband
     */
    public String getFavband() {
        return favband;
    }

    /**
     *
     * @param favband
     * The favband
     */
    public void setFavband(String favband) {
        this.favband = favband;
    }

    /**
     *
     * @return
     * The favsportsteam
     */
    public String getFavsportsteam() {
        return favsportsteam;
    }

    /**
     *
     * @param favsportsteam
     * The favsportsteam
     */
    public void setFavsportsteam(String favsportsteam) {
        this.favsportsteam = favsportsteam;
    }

    /**
     *
     * @return
     * The favsport
     */
    public String getFavsport() {
        return favsport;
    }

    /**
     *
     * @param favsport
     * The favsport
     */
    public void setFavsport(String favsport) {
        this.favsport = favsport;
    }

    /**
     *
     * @return
     * The favgenreofmovie
     */
    public String getFavgenreofmovie() {
        return favgenreofmovie;
    }

    /**
     *
     * @param favgenreofmovie
     * The favgenreofmovie
     */
    public void setFavgenreofmovie(String favgenreofmovie) {
        this.favgenreofmovie = favgenreofmovie;
    }

    /**
     *
     * @return
     * The favgenreofmusic
     */
    public String getFavgenreofmusic() {
        return favgenreofmusic;
    }

    /**
     *
     * @param favgenreofmusic
     * The favgenreofmusic
     */
    public void setFavgenreofmusic(String favgenreofmusic) {
        this.favgenreofmusic = favgenreofmusic;
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
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The image
     */
    public String getImage() {
        return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME + image;
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
     * The coverPic
     */
    public String getCoverPic() {
        return Constant.HTTP_CLOUDFRONT_DOMAIN_NAME + coverPic;
    }

    /**
     *
     * @param coverPic
     * The cover_pic
     */
    public void setCoverPic(String coverPic) {
        this.coverPic = coverPic;
    }

    /**
     *
     * @return
     * The shares
     */
    public int getShares() {
        return shares;
    }

    /**
     *
     * @param shares
     * The shares
     */
    public void setShares(int shares) {
        this.shares = shares;
    }

    /**
     *
     * @return
     * The friends
     */
    public int getFriends() {
        return friends;
    }

    /**
     *
     * @param friends
     * The friends
     */
    public void setFriends(int friends) {
        this.friends = friends;
    }

    /**
     *
     * @return
     * The following
     */
    public int getFollowing() {
        return following;
    }

    /**
     *
     * @param following
     * The following
     */
    public void setFollowing(int following) {
        this.following = following;
    }

    /**
     *
     * @return
     * The followers
     */
    public int getFollowers() {
        return followers;
    }

    /**
     *
     * @param followers
     * The followers
     */
    public void setFollowers(int followers) {
        this.followers = followers;
    }

    /**
     *
     * @return
     * The herelos
     */
    public int getHerelos() {
        return herelos;
    }

    /**
     *
     * @param herelos
     * The herelos
     */
    public void setHerelos(int herelos) {
        this.herelos = herelos;
    }

    /**
     *
     * @return
     * The liked
     */
    public int getLiked() {
        return liked;
    }

    /**
     *
     * @param liked
     * The liked
     */
    public void setLiked(int liked) {
        this.liked = liked;
    }

    /**
     *
     * @return
     * The likedMine
     */
    public int getLikedMine() {
        return likedMine;
    }

    /**
     *
     * @param likedMine
     * The liked_mine
     */
    public void setLikedMine(int likedMine) {
        this.likedMine = likedMine;
    }
}
