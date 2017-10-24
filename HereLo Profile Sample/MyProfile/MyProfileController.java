package com.tv.herelo.MyProfile;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.myprofilemodal.MyProfileModalMain;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.ProfileFragment;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoeb on 8/6/16.
 */
public class MyProfileController {


    private MyProfileScreen mMyProfileScreen = null;
    private ProfileFragment mProfileFragment = null;
    private boolean isProgressBar = false;


    public MyProfileController(MyProfileScreen loginScreen) {
        this.mMyProfileScreen = loginScreen;
    }

    public MyProfileController(ProfileFragment mProfileFragment) {
        this.mProfileFragment = mProfileFragment;
    }

    public void callMyProfileWS(Uri.Builder builder, boolean isProgressBar) {
        this.isProgressBar = isProgressBar;

        /*Api Calling*/
        if (mMyProfileScreen != null) {
            if (NetworkAvailablity.checkNetworkStatus(mMyProfileScreen.getActivity())) {

                try {

                    ProfileTask task = new ProfileTask(WebserviceTask.POST,
                            null, null, builder);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        task.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
            /*No Internet screen appears*/

            }
        } else if (mProfileFragment != null) {
            if (NetworkAvailablity.checkNetworkStatus(mProfileFragment.getActivity())) {

                try {

                    ProfileTask task = new ProfileTask(WebserviceTask.POST,
                            null, null, builder);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        task.execute();
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                }

            } else {
            /*No Internet screen appears*/

            }
        }


    }

    class ProfileTask extends WebserviceTask {

        public ProfileTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            /*Progress Dialog here*/
            if (mMyProfileScreen != null) {
                if (isProgressBar)
                Constant.showProgressDialog(mMyProfileScreen.getActivity());

            } else if (mProfileFragment != null) {
                Constant.showProgressDialog(mProfileFragment.getActivity());
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getUserProfile;
        }

        @Override
        public void onLoadingFinished(String response) {
            Constant.cancelDialog();

            try {
                Gson gson = new Gson();
                MyProfileModalMain myProfileModalMain = gson.fromJson(response, MyProfileModalMain.class);

                if (mMyProfileScreen != null) {
                    mMyProfileScreen.successResponse(myProfileModalMain);
                } else if (mProfileFragment != null) {
                    mProfileFragment.successResponse(myProfileModalMain);
                }


            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
