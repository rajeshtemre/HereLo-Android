package com.tv.herelo.MyProfile.SearchForPeople;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tv.herelo.login.FindSocialFriends.DatumFindSocialFriends;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shoeb on 15/7/16.
 */
public class SearchForPeopleModal {


    @SerializedName("message")
    @Expose
    private String message = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("is_more")
    @Expose
    private String isMore;
    @SerializedName("data")
    @Expose
    private List<DatumFindSocialFriends> data = new ArrayList<DatumFindSocialFriends>();

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
     * The isMore
     */
    public String getIsMore() {
        return isMore;
    }

    /**
     *
     * @param isMore
     * The is_more
     */
    public void setIsMore(String isMore) {
        this.isMore = isMore;
    }

    /**
     *
     * @return
     * The data
     */
    public List<DatumFindSocialFriends> getData() {
        return data;
    }

    /**
     *
     * @param data
     * The data
     */
    public void setData(List<DatumFindSocialFriends> data) {
        this.data = data;
    }

}
