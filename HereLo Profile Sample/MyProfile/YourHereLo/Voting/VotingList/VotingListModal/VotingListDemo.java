package com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList.VotingListModal;

/**
 * Created by Shoeb on 22/5/17.
 */
public class VotingListDemo {

    public VotingListDemo(String mVoteName, String mHeaderName, String headerType, String vote_main_img) {
        this.mVoteName = mVoteName;
        this.mHeaderName = mHeaderName;
        this.headerType = headerType;
        this.vote_main_img = vote_main_img;
    }

    public String getmVoteName() {
        return mVoteName;
    }

    public void setmVoteName(String mVoteName) {
        this.mVoteName = mVoteName;
    }

    String mVoteName = "";
    String mHeaderName= "";
    String headerType= "";
    String vote_main_img= "";

    public String getVote_main_img() {
        return vote_main_img;
    }

    public void setVote_main_img(String vote_main_img) {
        this.vote_main_img = vote_main_img;
    }

    public String getHeaderType() {
        return headerType;
    }

    public void setHeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String getmHeaderName() {
        return mHeaderName;
    }

    public void setmHeaderName(String mHeaderName) {
        this.mHeaderName = mHeaderName;
    }

}