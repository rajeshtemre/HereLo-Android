package com.tv.herelo.MyProfile.taguserposts;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.google.gson.Gson;
import com.tv.herelo.MyProfile.HereLoFragmentGridview;
import com.tv.herelo.MyProfile.HereLoFragmentMainview;
import com.tv.herelo.MyProfile.HereLoFragmentMiniview;
import com.tv.herelo.MyProfile.taguserposts.tagUserPostModal.TagUserPostModalMain;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shoeb on 18/11/16.
 */
public class TagUserPostController {

    private HereLoFragmentGridview mHereLoFragment;
    HereLoFragmentMiniview hereLoFragmentMiniview;
    HereLoFragmentMainview hereLoFragmentMainview;
    private boolean b = false;

    public TagUserPostController(HereLoFragmentGridview mHereLoFragment) {
        this.mHereLoFragment = mHereLoFragment;
    }

    public TagUserPostController(HereLoFragmentMiniview hereLoFragmentMiniview) {
        this.hereLoFragmentMiniview=hereLoFragmentMiniview;
    }

    public TagUserPostController(HereLoFragmentMainview hereLoFragmentMainview) {
        this.hereLoFragmentMainview=hereLoFragmentMainview;
    }

    public void callWS(Uri.Builder builder, boolean b) {
        try {
            this.b = b;

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
            if (b){
            /*Progress Dialog here*/
                Constant.showProgressDialog(mHereLoFragment.getActivity());

            }

        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.getTaggedUserPost;
        }

        @Override
        public void onLoadingFinished(String response) {

            if (b){
                Constant.cancelDialog();
            }

            try {

                if (response != null || !response.equalsIgnoreCase("")) {
                    Gson gson = new Gson();
                    TagUserPostModalMain modal = gson.fromJson(response, TagUserPostModalMain.class);
                    if (hereLoFragmentMiniview != null) {
                        hereLoFragmentMiniview.response(modal);
                    } else if (mHereLoFragment != null) {
                        mHereLoFragment.response(modal);
                    }else if (hereLoFragmentMainview != null) {
                        hereLoFragmentMainview.response(modal);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
    }

}
