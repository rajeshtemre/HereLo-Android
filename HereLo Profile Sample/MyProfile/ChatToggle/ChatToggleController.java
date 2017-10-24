package com.tv.herelo.MyProfile.ChatToggle;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.MyProfileScreen;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoeb on 28/7/17.
 */

public class ChatToggleController {


    private boolean isProgressBar = false;
    private boolean isCacheRequired = false;
    private MyProfileScreen mMyProfileScreen;

    public ChatToggleController(MyProfileScreen mMyProfileScreen) {
        this. mMyProfileScreen =  mMyProfileScreen;
    }

    public void callWS(Uri.Builder builder, boolean isProgressBar, boolean isCacheRequired) {
        this.isProgressBar = isProgressBar;
        this.isCacheRequired = isCacheRequired;
        WSTASK task = new WSTASK(WebserviceTask.POST,
                null, null, builder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    class WSTASK extends WebserviceTask {

        public WSTASK(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            /*Progress Dialog here*/
            if (isProgressBar) {
                if ( mMyProfileScreen != null) {
                    Constant.showProgressDialog( mMyProfileScreen.getActivity());
                }
            }
        }

        @Override
        public String getWebserviceURL() {
            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.toggleChat;
        }

        @Override
        public void onLoadingFinished(String response) {
            if (isProgressBar) {
                Constant.cancelDialog();
            }



            try {

                if (response != null || !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();
                   /* CheckInAllPostsModalMain modal = gson.fromJson(response, CheckInAllPostsModalMain.class);
                    if ( mMyProfileScreen != null) {
                        mMyProfileScreen.responseOfCheckAllPostsList(modal);
                    }*/


                }
            } catch (Exception e) {
                e.printStackTrace();


            }
        }
    }



}
