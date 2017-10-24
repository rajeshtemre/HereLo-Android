package com.tv.herelo.MyProfile.Action_Activity_Notification;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal.ActivityResponseModalMain;
import com.tv.herelo.R;
import com.tv.herelo.XListViewPullToRefresh.XListView;
import com.tv.herelo.bean.SearchChatFriendsHeaderBean;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.utils.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shoeb on 12/5/17.
 */
public class TaggedActivityFragment extends Fragment {

    private static final String TAG = TaggedActivityFragment.class.getSimpleName();
    View mView;
    @Bind(R.id.listview)
    XListView listview;

    private String page_name = "";


    /*Controller*/
    private ActivityController mActivityController = new ActivityController(this);

    /*Adapter*/
    private ActivityAdapter mActivityAdapter;

    /*REquest Params*/

    private int page_no = 1;
    private int sub_list = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tagged_activity_fragment, container, false);
        ButterKnife.bind(this, mView);


        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();

        /** Getting integer data of the key current_page from the bundle */
        if (data.containsKey("whole_data")) {
            SearchChatFriendsHeaderBean bean = (SearchChatFriendsHeaderBean) data.getSerializable("whole_data");
            page_name = bean.getName();
            sub_list = bean.getId();

        }

        Logger.logsError(TAG, "onCreateView Called");

        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                        callWS(false);
                    } else {
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                    }
                }
            },100);
        }
    }

    private void callWS(boolean isProgressBar) {
        try {
            String sub_list_str = "";
            sub_list_str = String.valueOf(sub_list);


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("list_type", 3 + "")
                    .appendQueryParameter("user_id", MySharedPreference.getCurrentUserId(getActivity()) + "")
                    .appendQueryParameter("page_no", page_no + "")
                    .appendQueryParameter("sub_list", sub_list_str + "");
            mActivityController.callWS(builder, isProgressBar);
        } catch (Exception e) {
            Logger.logsError(TAG, e.getMessage());
        }
    }

    public void responseOfActivity(ActivityResponseModalMain modal) {
        listview.stopLoadMore();
        listview.stopRefresh();


        if (modal.getStatus() == 1) {
            if (modal.getData().size() >= 10) {
                listview.setPullLoadEnable(true);
            } else {
                listview.setPullLoadEnable(false);
            }
            mActivityAdapter = new ActivityAdapter(getActivity(), modal.getData(), null, TaggedActivityFragment.this);
            listview.setAdapter(mActivityAdapter);
        } else {
            mActivityAdapter = new ActivityAdapter(getActivity(), modal.getData(), null,TaggedActivityFragment.this);
            listview.setAdapter(mActivityAdapter);
        }
    }
}
