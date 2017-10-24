package com.tv.herelo.MyProfile.SearchForPeople;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoeb on 15/7/16.
 */
public class SearchForPeopleController {

    private SearchForPeopleResultFragment mSearchForPeopleResultFragment;
    private boolean isProgress = false;

    public SearchForPeopleController(SearchForPeopleResultFragment mSearchForPeopleResultFragment) {
        this.mSearchForPeopleResultFragment = mSearchForPeopleResultFragment;

    }

    public void callSearchForPeopleWS(Uri.Builder builder, boolean isProgress) {
        this.isProgress = isProgress;
        try {

            SearchForPeopleTask task = new SearchForPeopleTask(WebserviceTask.POST,
                    null, null, builder);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                task.execute();
            }

        } catch (Exception e) {

        }
    }

    class SearchForPeopleTask extends WebserviceTask {

        public SearchForPeopleTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {
            if (isProgress){
            /*Progress Dialog here*/
                Constant.showProgressDialog(mSearchForPeopleResultFragment.getActivity());

            }

        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.searchUser;
        }

        @Override
        public void onLoadingFinished(String response) {

            Constant.cancelDialog();
            try {

                if (response != null || !response.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    SearchForPeopleModal modal = gson.fromJson(response, SearchForPeopleModal.class);
                    mSearchForPeopleResultFragment.responseHandle(modal);
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }
}
