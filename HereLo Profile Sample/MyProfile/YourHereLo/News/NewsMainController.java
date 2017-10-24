package com.tv.herelo.MyProfile.YourHereLo.News;

import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsMainModals.NewsModalMain;
import com.tv.herelo.MyProfile.YourHereLo.YourHereLoScreen;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoeb on 12/5/17.
 */
public class NewsMainController {

    private YourHereLoScreen mYourHereLoScreen;
    private boolean isProgressBarRequired = false;

    public NewsMainController(YourHereLoScreen mYourHereLoScreen){
        this.mYourHereLoScreen = mYourHereLoScreen;
    }

    public void callWS(boolean isProgressBarRequired){
        this.isProgressBarRequired = isProgressBarRequired;
        WSTASK task = new WSTASK(WebserviceTask.GET,
                null, null);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            task.execute();
        }
    }

    class WSTASK extends WebserviceTask {
        public WSTASK(int method, Map<String, String> params, HashMap<String, String> headerParams) {
            super(method, (HashMap<String, String>) params, headerParams);
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

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getNews;

        }

        @Override
        public void onLoadingFinished(String response) {
            if (isProgressBarRequired) {
                Constant.cancelDialog();
            }


            try {

                if (response != null || !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();
                    NewsModalMain modal = gson.fromJson(response, NewsModalMain.class);
                    mYourHereLoScreen.responseNewsMain(modal);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
