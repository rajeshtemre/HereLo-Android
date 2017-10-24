package com.tv.herelo.MyProfile.YourHereLo.News;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsDetailsModals.NewsDetailsModalMain;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shoeb on 18/5/17.
 */
public class NewsDetailsController {
    private boolean isProgressBarRequired = false;
    private NewsDetailsActivity mNewsDetailsActivity;

    public NewsDetailsController(NewsDetailsActivity mNewsDetailsActivity) {
        this.mNewsDetailsActivity = mNewsDetailsActivity;
    }


    public void callWS(Uri.Builder mBuilder, boolean isProgressBarRequired) {
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
                if (mNewsDetailsActivity != null) {
                    Constant.showProgressDialog(mNewsDetailsActivity);
                }
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getNewsDetail;

        }

        @Override
        public void onLoadingFinished(String response) {
            if (isProgressBarRequired) {
                Constant.cancelDialog();
            }


            try {

                if (response != null || !response.equalsIgnoreCase("")) {

                    Gson gson = new Gson();
                    NewsDetailsModalMain modal = gson.fromJson(response, NewsDetailsModalMain.class);
                    mNewsDetailsActivity.responseNewsDetails(modal);

                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
