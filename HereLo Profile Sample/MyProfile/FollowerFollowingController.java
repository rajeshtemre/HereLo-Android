package com.tv.herelo.MyProfile;

import android.net.Uri;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.followersmodal.FollowerModal;
import com.tv.herelo.MyProfile.followingmodal.FollowingModal;
import com.tv.herelo.MyProfile.friendsModal.FriendsModalMain;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoeb on 10/6/16.
 */
public class FollowerFollowingController {

    final private FriendScreen mFriendScreen;
    private String mApiname = "";
    private boolean isProgress = false;

    public FollowerFollowingController(FriendScreen mFriendScreen) {
        this.mFriendScreen = mFriendScreen;
    }

    public void callFollowerFollowingWS(Uri.Builder builder, String mApiname,boolean isProgress) {
        this.mApiname = mApiname;
        this.isProgress = isProgress;
        FollowTask task = new FollowTask(WebserviceTask.POST,
                null, null, builder);
        task.execute();
    }

    class FollowTask extends WebserviceTask {

        public FollowTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            /*Progress Dialog here*/
            if (isProgress){
                Constant.showProgressDialog(mFriendScreen.getActivity());
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + mApiname;
        }

        @Override
        public void onLoadingFinished(String response) {

            Constant.cancelDialog();
            try {

                if (response != null && !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();


                    if (mApiname.equalsIgnoreCase("followerList")) {
                        FollowerModal modal = gson.fromJson(response,FollowerModal.class);
                        mFriendScreen.responseHandleFollower(modal);

                    } else if (mApiname.equalsIgnoreCase("followingList")) {
                        FollowingModal modalFollowingModal = gson.fromJson(response,FollowingModal.class);
                        mFriendScreen.responseHandleFollowing(modalFollowingModal);


                    } else if (mApiname.equalsIgnoreCase("getFriendsList")) {
                        FriendsModalMain friendsModalMain = gson.fromJson(response,FriendsModalMain.class);
                        mFriendScreen.responseHandleFriends(friendsModalMain);


                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
