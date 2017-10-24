package com.tv.herelo.MyProfile.updateCover;

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
 * Created by Shoeb on 27/1/17.
 */
public class UpdateCoverController {

    private MyProfileScreen mMyProfileScreen = null;

    public UpdateCoverController(MyProfileScreen mMyProfileScreen) {
        this.mMyProfileScreen = mMyProfileScreen;
    }

    public void callWS(Uri.Builder builder) {
        WSTask task = new WSTask(WebserviceTask.POST,
                null, null, builder);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    class WSTask extends WebserviceTask {

        public WSTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            /*Progress Dialog here*/
            if (mMyProfileScreen != null) {
                Constant.showProgressDialog(mMyProfileScreen.getActivity());
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.updateCoverPic;
        }

        @Override
        public void onLoadingFinished(String response) {
            Constant.cancelDialog();

            try {

                if (response != null || !response.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    UpdateCoverModalMain modal = gson.fromJson(response, UpdateCoverModalMain.class);
                    if (mMyProfileScreen != null) {
                        mMyProfileScreen.responseUpdateCover(modal);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

}
