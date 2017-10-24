package com.tv.herelo.MyProfile.map_cluster;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.HomeTab.HomeModal.HomeTabModal;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoeb on 22/11/16.
 */
public class ClusterMapLocationController {
    private ClusteringMapActivity clusteringMapActivity;
    private boolean isProgress = false;

    public ClusterMapLocationController(ClusteringMapActivity clusteringMapActivity) {
        this.clusteringMapActivity = clusteringMapActivity;
    }

    public void callWS(Uri.Builder builder, boolean isProgress) {
        this.isProgress = isProgress;

        try {

            WSTask task = new WSTask(WebserviceTask.POST,
                    null, null, builder);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }

        } catch (Exception e) {

        }
    }

    class WSTask extends WebserviceTask {

        public WSTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            if (isProgress){
            /*Progress Dialog here*/
                Constant.showProgressDialog(clusteringMapActivity);
            }


        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.locationPostList;
        }

        @Override
        public void onLoadingFinished(String response) {

            if (isProgress){
                Constant.cancelDialog();
            }

            try {

                if (response != null || !response.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    HomeTabModal modal = gson.fromJson(response, HomeTabModal.class);

                    clusteringMapActivity.response(modal);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
