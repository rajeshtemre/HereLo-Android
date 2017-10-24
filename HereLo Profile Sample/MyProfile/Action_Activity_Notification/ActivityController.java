package com.tv.herelo.MyProfile.Action_Activity_Notification;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal.ActivityResponseModalMain;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoeb on 9/5/17.
 */
public class ActivityController {


    private boolean isProgressBar = false;

    private ActivityScreen mActivityScreen;
    private TaggedActivityFragment mTaggedActivityFragment;


    public ActivityController(ActivityScreen mActivityScreen) {
        this.mActivityScreen = mActivityScreen;
    }

    public ActivityController(TaggedActivityFragment mTaggedActivityFragment) {
        this.mTaggedActivityFragment = mTaggedActivityFragment;
    }


    public void callWS(Uri.Builder builder, boolean isProgressBar) {
        this.isProgressBar = isProgressBar;
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
                if (mActivityScreen != null) {
                    Constant.showProgressDialog(mActivityScreen.getActivity());
                } else if (mTaggedActivityFragment != null) {
                    Constant.showProgressDialog(mTaggedActivityFragment.getActivity());
                }
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getActivity;

        }

        @Override
        public void onLoadingFinished(String response) {
            if (isProgressBar) {
                Constant.cancelDialog();
            }


            try {

                if (response != null || !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();
                    ActivityResponseModalMain modal = gson.fromJson(response, ActivityResponseModalMain.class);
                    if (mActivityScreen != null) {
                        mActivityScreen.responseOfActivity(modal);
                    } else if (mTaggedActivityFragment != null) {
                        mTaggedActivityFragment.responseOfActivity(modal);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
                if (mActivityScreen != null) {
                    mActivityScreen.progress_bar.setVisibility(View.GONE);
                }

            }

        }
    }

}
