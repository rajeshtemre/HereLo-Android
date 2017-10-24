package com.tv.herelo.MyProfile;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tv.herelo.MyProfile.SearchForPeople.SearchForPeopleFragment;
import com.tv.herelo.MyProfile.followersmodal.FollowerModal;
import com.tv.herelo.MyProfile.followersmodal.Follower_Following_FriendsModal;
import com.tv.herelo.MyProfile.followingmodal.FollowingModal;
import com.tv.herelo.MyProfile.friendsModal.FriendsModalMain;
import com.tv.herelo.R;
import com.tv.herelo.XListViewPullToRefresh.XListView;
import com.tv.herelo.adapter.FriendsAdapter;
import com.tv.herelo.chat.ChatAdapterCallBack;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.BaseContainerFragment;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.utils.ErrorReporter;
import com.tv.herelo.utils.Logger;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Shoeb on 26/9/15.
 */
public class FriendScreen extends Fragment implements View.OnClickListener, XListView.IXListViewListener,ChatAdapterCallBack {

    private static final String TAG = FriendScreen.class.getSimpleName();
    View view;
    ImageView header_iv;
    XListView friends_list;

    ImageView back_btn;
    TextView header_tv;
    TextView no_data_tv;
    ImageView next_btn;
    ImageView crossIV;
    EditText searchET;
    private String screen = "";

    LinearLayout header_ll;
    RelativeLayout searchRL;

    private ImageButton _cancelButton;
    private ImageButton searchPeopleB;
    private RelativeLayout _bottomRelativeLayout;
    private TranslateAnimation _translateAnimation;
    private int x;
    private int y;

    private String apiName = "";
    private String is_search = "";
    private String keyword = "";
    private int user_id = 0;
    private int page_no = 1;


    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;

    /*Declarations and initialization*/
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms

    /*Controller*/
    private FollowerFollowingController controller = new FollowerFollowingController(this);

    public void responseHandleFollower(FollowerModal modal) {
        int status = modal.getStatus();
        if (status == 1) {
            /*Success*/

            List<Follower_Following_FriendsModal> mDataList = modal.getFollowers();
            if (mDataList.size() > 0) {
                if (modal.getIs_more().equalsIgnoreCase("Yes")) {
                    friends_list.setPullLoadEnable(true);
                } else {
                    friends_list.setPullLoadEnable(false);
                }

                friends_list.setVisibility(View.VISIBLE);
                no_data_tv.setVisibility(View.GONE);
                /*Set Adapter*/
                FriendsAdapter adapter = new FriendsAdapter(getActivity(), mDataList);
                friends_list.setAdapter(adapter);
                adapter.setCallBack(this);


            } else {
                friends_list.setVisibility(View.GONE);
                no_data_tv.setVisibility(View.VISIBLE);
            }
        } else {
            /*Failure*/

            friends_list.setVisibility(View.GONE);
            no_data_tv.setVisibility(View.VISIBLE);
        }
        friends_list.stopLoadMore();
        friends_list.stopRefresh();
    }

    public void responseHandleFollowing(FollowingModal modal) {
        int status = modal.getStatus();
        if (status == 1) {
            /*Success*/


            List<Follower_Following_FriendsModal> mDataList = modal.getFollowing();
            if (mDataList.size() > 0) {

                if (modal.getIs_more().equalsIgnoreCase("Yes")) {
                    friends_list.setPullLoadEnable(true);
                } else {
                    friends_list.setPullLoadEnable(false);
                }

                /*Set Adapter*/
                friends_list.setVisibility(View.VISIBLE);
                no_data_tv.setVisibility(View.GONE);
                FriendsAdapter adapter = new FriendsAdapter(getActivity(), mDataList);
                friends_list.setAdapter(adapter);
                adapter.setCallBack(this);

            } else {
                friends_list.setVisibility(View.GONE);
                no_data_tv.setVisibility(View.VISIBLE);
            }
        } else {
            /*Failure*/
            friends_list.setVisibility(View.GONE);
            no_data_tv.setVisibility(View.VISIBLE);
        }
        friends_list.stopLoadMore();
        friends_list.stopRefresh();

    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.friends_screen, container, false);
        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);
        ErrorReporter.getInstance().Init(getActivity());
        sPref = getActivity().getSharedPreferences(Constant.SPREF, Context.MODE_PRIVATE);
        editor = sPref.edit();

        user_id = sPref.getInt(Constant.USER_ID_SPREF, 0);
        if (getArguments() != null) {
            if (getArguments().containsKey("Screen")) {
                screen = getArguments().get("Screen").toString();
            }
            if (getArguments().containsKey(Constant.USER_ID)) {
                user_id = getArguments().getInt(Constant.USER_ID, user_id);
            }

        }


        initView();
        onCLikListener();

        return view;
    }

    private void initView() {
//        View headerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_screen_header, null, false);


        back_btn = (ImageView) view.findViewById(R.id.back_btn);

        back_btn = (ImageView) view.findViewById(R.id.back_btn);

        next_btn = (ImageView) view.findViewById(R.id.next_btn);
        crossIV = (ImageView) view.findViewById(R.id.crossIV);
        searchET = (EditText) view.findViewById(R.id.searchET);
        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(Editable s) {
                keyword = s.toString().trim();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                         /*Api Calling*/
                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                            try {
                                is_search = "1";
                                final Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("user_id", user_id + "")
                                        .appendQueryParameter("is_search", is_search)
                                        .appendQueryParameter("other_user_id", MySharedPreference.getCurrentUserId(getActivity())+"")
                                        .appendQueryParameter("keyword", keyword)
                                        .appendQueryParameter("page_no", page_no + "");

                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        controller.callFollowerFollowingWS(builder, apiName, true);
                                    }
                                });


                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                /*No Internet screen appears*/

                        }
                    }
                }, DELAY);
            }
        });

        header_tv = (TextView) view.findViewById(R.id.header_tv);
        no_data_tv = (TextView) view.findViewById(R.id.no_data_tv);
        header_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        no_data_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        searchET.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));

        friends_list = (XListView) view.findViewById(R.id.friends_list);
        friends_list.setXListViewListener(this);
        friends_list.setPullRefreshEnable(true);

        next_btn.setImageResource(R.drawable.add_icon);

        header_iv = (ImageView) view.findViewById(R.id.headerIV);
        header_iv.setVisibility(View.VISIBLE);


        header_ll = (LinearLayout) view.findViewById(R.id.header_ll);
        searchRL = (RelativeLayout) view.findViewById(R.id.SearchRL);


        _cancelButton = (ImageButton) view.findViewById(R.id.cancelB);
        searchPeopleB = (ImageButton) view.findViewById(R.id.searchPeopleB);
        _cancelButton.setOnClickListener(this);
        searchPeopleB.setOnClickListener(this);

        _bottomRelativeLayout = (RelativeLayout) view.findViewById(R.id.bottomRelay);
        _bottomRelativeLayout.setVisibility(View.GONE);


        if (screen.equalsIgnoreCase("Friends")) {
            header_tv.setText("Friends");
            apiName = "getFriendsList";
        } else if (screen.equalsIgnoreCase("Followers")) {
            header_tv.setText("Followers");
            apiName = "followerList";
            next_btn.setVisibility(View.GONE);
        } else if (screen.equalsIgnoreCase("Following")) {
            header_tv.setText("Following");
            apiName = "followingList";
            next_btn.setVisibility(View.VISIBLE);
        }

//        my_profile_gv = (GridView)view.findViewById(R.id.my_profile_gv);

//        my_profile_gv.add(headerView);

        friends_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                popupWindow();
            }
        });

        friends_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                x = (int) event.getX();
                y = (int) event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_UP:
                }
                return false;
            }

        });
        /*Api Calling*/
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

            try {

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("is_search", is_search)
                        .appendQueryParameter("other_user_id", MySharedPreference.getCurrentUserId(getActivity())+"")
                        .appendQueryParameter("keyword", keyword)
                        .appendQueryParameter("page_no", page_no + "");

                controller.callFollowerFollowingWS(builder, apiName, true);
                /*FollowTask task = new FollowTask(WebserviceTask.POST,
                        null, null, builder);
                task.execute();*/
            } catch (Exception e) {
                e.printStackTrace();

            }

        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                /*No Internet screen appears*/

        }

    }


    private void onCLikListener() {

        back_btn.setOnClickListener(this);
        header_ll.setOnClickListener(this);
        next_btn.setOnClickListener(this);
        crossIV.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.crossIV:
                searchET.setText("");
                break;
            case R.id.back_btn:

                MainTabActivity.backbutton();

                break;
            case R.id.header_ll:
                if (searchRL.getVisibility() == View.VISIBLE) {
//                    searchET.setText("");
                    searchRL.setVisibility(View.GONE);
                } else {
                    searchRL.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.next_btn:
                if (_bottomRelativeLayout.getVisibility() != View.VISIBLE) {
                    animateUp();
                }

                break;
            case R.id.cancelB:
                animateDown();

                _bottomRelativeLayout.clearAnimation();
                break;
            case R.id.searchPeopleB:
                animateDown();

                _bottomRelativeLayout.clearAnimation();
                /*Redirect to Search For People screen*/
                ((BaseContainerFragment) getParentFragment()).replaceFragment(new SearchForPeopleFragment(), true);
                break;
            default:
                /*animateDown();

                _bottomRelativeLayout.clearAnimation();

                *//*Redirect to Search For People screen*//*
                ((BaseContainerFragment) getParentFragment()).replaceFragment(new SearchForPeopleFragment(), true);*/
                break;
        }
    }


    public void animateUp() {
        _translateAnimation = new TranslateAnimation(0, 0, 500, 0);
        _translateAnimation.setDuration(500);
        _translateAnimation.setFillAfter(true);
        _bottomRelativeLayout.startAnimation(_translateAnimation);
        _bottomRelativeLayout.setVisibility(View.VISIBLE);
    }

    // ////////// animation down
    public void animateDown() {
        _translateAnimation = new TranslateAnimation(0, 0, 0, 500);
        _translateAnimation.setDuration(500);
        _translateAnimation.setFillAfter(true);
        _bottomRelativeLayout.startAnimation(_translateAnimation);
        _bottomRelativeLayout.clearAnimation();
        _bottomRelativeLayout.setVisibility(View.GONE);
    }

    /**
     * My Group List
     */

    public void popupWindow() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.my_group_popup);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setTitle("");


        TextView doneTextView = (TextView) dialog.findViewById(R.id.doneTV);
        doneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });


//        ListView listView = (ListView) dialog.findViewById(R.id.listview);
//        listView.setAdapter(new MyGroupPopupAdapter(getActivity()));
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();

        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        //wmlp.x = ;   //x position
        wmlp.y = y;
        dialog.show();
    }


    @Override
    public void onRefresh() {
         /*Api Calling*/
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

            try {
                page_no = 1;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("is_search", is_search)
                        .appendQueryParameter("other_user_id", MySharedPreference.getCurrentUserId(getActivity())+"")
                        .appendQueryParameter("keyword", keyword)
                        .appendQueryParameter("page_no", page_no + "");

                controller.callFollowerFollowingWS(builder, apiName, false);
                /*FollowTask task = new FollowTask(WebserviceTask.POST,
                        null, null, builder);
                task.execute();*/
            } catch (Exception e) {
                e.printStackTrace();
                friends_list.stopRefresh();

            }

        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                /*No Internet screen appears*/
            friends_list.stopRefresh();
        }
    }

    @Override
    public void onLoadMore() {
   /*Api Calling*/
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

            try {
                page_no = page_no + 1;
                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("is_search", is_search)
                        .appendQueryParameter("other_user_id", MySharedPreference.getCurrentUserId(getActivity())+"")
                        .appendQueryParameter("keyword", keyword)
                        .appendQueryParameter("page_no", page_no + "");

                controller.callFollowerFollowingWS(builder, apiName, false);
                /*FollowTask task = new FollowTask(WebserviceTask.POST,
                        null, null, builder);
                task.execute();*/
            } catch (Exception e) {
                e.printStackTrace();
                friends_list.stopLoadMore();

            }

        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                /*No Internet screen appears*/
            friends_list.stopLoadMore();

        }
    }

    public void responseHandleFriends(FriendsModalMain modal) {
        int status = modal.getStatus();
        if (status == 1) {
            /*Success*/


            List<Follower_Following_FriendsModal> mDataList = modal.getData();
            if (mDataList.size() > 0) {

                friends_list.setPullLoadEnable(true);

                /*Set Adapter*/
                friends_list.setVisibility(View.VISIBLE);
                no_data_tv.setVisibility(View.GONE);
                FriendsAdapter adapter = new FriendsAdapter(getActivity(), mDataList);
                friends_list.setAdapter(adapter);
                adapter.setCallBack(this);

            } else {
                friends_list.setVisibility(View.GONE);
                no_data_tv.setVisibility(View.VISIBLE);
            }
        } else {
            /*Failure*/
            friends_list.setVisibility(View.GONE);
            no_data_tv.setVisibility(View.VISIBLE);
        }
        friends_list.stopLoadMore();
        friends_list.stopRefresh();

    }

    @Override
    public void replaceToProfile(int userID) {

        Logger.logsInfo(TAG,"userID============"+userID);
        Bundle mBundle = new Bundle();
        mBundle.putInt(Constant.other_user_id, userID);
        MyProfileScreen mMyProfileScreen = new MyProfileScreen();
        mMyProfileScreen.setArguments(mBundle);
        ((BaseContainerFragment) getParentFragment()).replaceFragment(mMyProfileScreen, true);

    }
}
