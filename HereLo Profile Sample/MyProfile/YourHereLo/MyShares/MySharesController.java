package com.tv.herelo.MyProfile.YourHereLo.MyShares;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.YourHereLo.MyShares.ModalsMySharesMain.MySharesModalMain;
import com.tv.herelo.MyProfile.YourHereLo.YourHereLoScreen;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoeb on 11/5/17.
 */
public class MySharesController {

    private YourHereLoScreen mYourHereLoScreen;
    private boolean isProgressBarRequired = false;

    public MySharesController(YourHereLoScreen mYourHereLoScreen){
        this.mYourHereLoScreen = mYourHereLoScreen;
    }

    public void callWS(Uri.Builder mBuilder,boolean isProgressBarRequired){
        this.isProgressBarRequired = isProgressBarRequired;
        WSTASK task = new WSTASK(WebserviceTask.POST,
                null, null, mBuilder);

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
            if (isProgressBarRequired) {
                if (mYourHereLoScreen != null) {
                    Constant.showProgressDialog(mYourHereLoScreen.getActivity());
                }
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getMyshare;

        }

        @Override
        public void onLoadingFinished(String response) {
            if (isProgressBarRequired) {
                Constant.cancelDialog();
            }


            try {

                if (response != null || !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();
                    MySharesModalMain modal = gson.fromJson(response, MySharesModalMain.class);
                    mYourHereLoScreen.responseMyShares(modal);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
