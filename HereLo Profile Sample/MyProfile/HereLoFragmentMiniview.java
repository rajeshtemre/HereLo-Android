package com.tv.herelo.MyProfile;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.MyProfile.HereLos.HereLosController;
import com.tv.herelo.MyProfile.HereLosLikedMine.HereLoLikedMineController;
import com.tv.herelo.MyProfile.HereLosLikes.HereLoLikesController;
import com.tv.herelo.MyProfile.taguserposts.TagUserPostController;
import com.tv.herelo.MyProfile.taguserposts.tagUserPostModal.TagUserPostModalMain;
import com.tv.herelo.R;
import com.tv.herelo.adapter.MiniRecyclerAdapterSingle;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.theme.ThemeBlack;
import com.tv.herelo.theme.ThemeBlue;
import com.tv.herelo.theme.ThemeGray;
import com.tv.herelo.theme.ThemeGreen;
import com.tv.herelo.theme.ThemeManager;
import com.tv.herelo.theme.ThemeMaroon;
import com.tv.herelo.theme.ThemeWhite;
import com.tv.herelo.utils.ErrorReporter;
import com.tv.herelo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by user on 28/7/17.
 */

public class HereLoFragmentMiniview extends Fragment implements View.OnClickListener {

    private final String TAG = HereLoFragmentGridview.class.getSimpleName();

    @Bind(R.id.fragment_container)
    public
    FrameLayout fragment_container;
    private ImageView _left1IV;
    private ImageView _left2IV;
    private ImageView _next1IV;
    private ImageView _next2IV;

    private TextView _headerTV;
    private Typeface tf;

   // private RecyclerView miniViewList = null;
    private TextView noPost_tv;
    private ImageView _headerIV;
    private LinearLayout header_ll;
    private LinearLayout headerLLMain;
    //    private GridViewAdapter adapter;

    /*Swipe refresh*/
    private SwipeRefreshLayout swipe_ref_layout;


    /*Request Params*/
    private int user_id = 0;
    private DB snappydb;
    private String following = "1";
    private String friends = "0";
    private String username = "";
    private String is_more = "Yes";
    private int page_no = 1;
    private String post_content_type = "0";
    private boolean isImagesVisible = false;
    private boolean isVideosVisible = false;
    private boolean isFromMyProfile;

    /*Share Pref*/
    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;


    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int mtotalItemCount = 0;
    int currentScrollState = 0;
    MiniRecyclerAdapterSingle adapter;

    private List<List<DatumHome>> mDataListWhole = new ArrayList<>();
    private List<DatumHome> mDataList = new ArrayList<>();
    private List<DatumHome> mDataListBackup = new ArrayList<>();
    @Bind(R.id.type_miniView)
    RecyclerView miniViewList;

    @Bind(R.id.appBarLayout)
    AppBarLayout appBarLayout;

    /*Controller*/
    private TagUserPostController mTagUserPostController = new TagUserPostController(this);
    private HereLosController mHereLosController = new HereLosController(this);
    private HereLoLikedMineController mHereLoLikedMineController = new HereLoLikedMineController(this);
    private HereLoLikesController mHereLoLikesController = new HereLoLikesController(this);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ErrorReporter.getInstance().Init(getActivity());
        View mView = inflater.inflate(R.layout.mini_view_screen_herelo, null);

        ButterKnife.bind(this, mView);

        if (Constant.getThemeIdFromPref(getActivity()) == 1) {
            ThemeManager.Set(new ThemeBlue());
            getActivity().setTheme(R.style.AppTheme);
        } else if (Constant.getThemeIdFromPref(getActivity()) == 2) {
            ThemeManager.Set(new ThemeMaroon());
            getActivity().setTheme(R.style.AppThemeMaroon);
        } else if (Constant.getThemeIdFromPref(getActivity()) == 3) {
            ThemeManager.Set(new ThemeGreen());
            getActivity().setTheme(R.style.AppThemeGreen);
        } else if (Constant.getThemeIdFromPref(getActivity()) == 4) {
            ThemeManager.Set(new ThemeBlack());
            getActivity().setTheme(R.style.AppThemeBlack);
        } else if (Constant.getThemeIdFromPref(getActivity()) == 5) {
            ThemeManager.Set(new ThemeGray());
            getActivity().setTheme(R.style.AppThemeRed);
        } else if (Constant.getThemeIdFromPref(getActivity()) == 6) {
            ThemeManager.Set(new ThemeWhite());
            getActivity().setTheme(R.style.AppThemeWhite);
        } else {
            ThemeManager.Set(new ThemeBlue());
            getActivity().setTheme(R.style.AppTheme);
        }
      //  miniViewList = (RecyclerView) getView().findViewById(R.id.type_miniView);
        miniViewList.setVisibility(View.VISIBLE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//        main_view_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        miniViewList.setLayoutManager(linearLayoutManager);
        miniViewList.setItemAnimator(new DefaultItemAnimator());
       /* try {
            if (snappydb.exists(Constant.PROFILEVIEW_TYPE)) {
                Logger.logsInfo(TAG, "snappydb.get(Constant.VIEW_TYPE)" + snappydb.get(Constant.PROFILEVIEW_TYPE));
                if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MINIVIEW_TYPE)) {

                }
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }*/
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                header_ll.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));


            }
        });
        return mView;
    }

    private int screen = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        screen = getArguments().getInt("Screen");
        initView();
        initCache();


    }

    private void initView() {
        if (getArguments().containsKey(Constant.USER_ID)) {
            user_id = getArguments().getInt(Constant.USER_ID, 0);
            if (user_id > 0) {
                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    try {
                        page_no = 1;
                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("user_id", user_id + "")
                                .appendQueryParameter("post_type", post_content_type)
                                .appendQueryParameter("page_no", page_no + "");
                        switch (screen) {
                            case 1:
                                mHereLosController.callWS(builder, false);
                                break;
                            case 2:
                                mHereLoLikesController.callWS(builder, false);
                                break;
                            case 3:
                                mHereLoLikedMineController.callWS(builder, false);
                                break;
                            case 4:
                                mTagUserPostController.callWS(builder, false);
                                break;
                            default:
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }

            }
        }
        sPref = getActivity().getSharedPreferences(Constant.SPREF, Context.MODE_PRIVATE);
        editor = sPref.edit();
        user_id = sPref.getInt(Constant.USER_ID_SPREF, 0);
        username = sPref.getString(Constant.USER_NAME_SPREF, "");
        post_content_type = sPref.getString(Constant.post_content_type, "0");

        tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLTStd-LtEx.otf");

        _left1IV = (ImageView) getView().findViewById(R.id.back_btn);
//        _left1IV.setImageResource(R.drawable.grid_icon);
        _left1IV.setVisibility(View.GONE);
        _left1IV.setOnClickListener(this);
        LinearLayout headerLLMain2 = (LinearLayout) getView().findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain2);
        _left2IV = (ImageView) getView().findViewById(R.id.back_btn2);
        _left2IV.setOnClickListener(this);
        _left2IV.setImageResource(ThemeManager.Current().headerLeftArrowIcon());
        _left2IV.setVisibility(View.VISIBLE);

        _next1IV = (ImageView) getView().findViewById(R.id.next_btn);
        _next1IV.setOnClickListener(this);
        _next1IV.setVisibility(View.VISIBLE);

        _next2IV = (ImageView) getView().findViewById(R.id.next_btn2);
        _next2IV.setOnClickListener(this);
        _next2IV.setVisibility(View.VISIBLE);
        setHeaderRightIcon();
//        _headerIV = (ImageView)view.findViewById(R.id.headerIV);
//        _headerIV.setVisibility(View.VISIBLE);

        _headerIV = (ImageView) getView().findViewById(R.id.headerIV);
        _headerIV.setVisibility(View.GONE);
//        _headerIV.setOnClickListener(this);

        header_ll = (LinearLayout) getView().findViewById(R.id.header_ll);
        headerLLMain = (LinearLayout) getView().findViewById(R.id.headerRelay);
        header_ll.setOnClickListener(this);


        _headerTV = (TextView) getView().findViewById(R.id.header_tv);

        _headerTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
/*		_backBtn = (ImageView) getView().findViewById(R.id.back_btn);
        _nextBtn = (ImageView) getView().findViewById(R.id.next_btn*//*);*/


        noPost_tv = (TextView) getView().findViewById(R.id.noPost_tv);

        if (getArguments() != null) {

            if (getArguments().containsKey(Constant.FROM_MYPROFILE)) {
                isFromMyProfile = getArguments().getBoolean(Constant.FROM_MYPROFILE);
            }
        }

        swipe_ref_layout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_ref_layout_RV);
        swipe_ref_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Logger.logsInfo(TAG, "onRefresh Called");
                 /*Api Calling*/
                if (getArguments() != null) {

                    if (getArguments().containsKey(Constant.FROM_MYPROFILE)) {
                        isFromMyProfile = getArguments().getBoolean(Constant.FROM_MYPROFILE);

                        Logger.logsInfo(TAG, "isFromMyProfile======" + isFromMyProfile);
                    }

                    if (getArguments().containsKey(Constant.USER_ID)) {
                        user_id = getArguments().getInt(Constant.USER_ID, 0);
                        if (user_id > 0) {
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                try {
                                    page_no = 1;
                                    Uri.Builder builder = new Uri.Builder()
                                            .appendQueryParameter("user_id", user_id + "")
                                            .appendQueryParameter("post_type", post_content_type)
                                            .appendQueryParameter("page_no", page_no + "");
                                    switch (screen) {
                                        case 1:
                                            mHereLosController.callWS(builder, false);
                                            break;
                                        case 2:
                                            mHereLoLikesController.callWS(builder, false);
                                            break;
                                        case 3:
                                            mHereLoLikedMineController.callWS(builder, false);
                                            break;
                                        case 4:
                                            mTagUserPostController.callWS(builder, false);
                                            break;
                                        default:
                                            break;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                            }

                        }
                    }
                }


            }
        });


    /*
        _backBtn.setOnClickListener(this);
		_nextBtn.setOnClickListener(this);	*/


        if (mDataList != null) {
            if (mDataList.size() > 0) {


                adapter = new MiniRecyclerAdapterSingle(miniViewList, getActivity(),
                        mDataList,
                        null,
                        HereLoFragmentMiniview.this, isFromMyProfile);
                miniViewList.setAdapter(adapter);

            } else {
                noPost_tv.setVisibility(View.VISIBLE);
                miniViewList.setVisibility(View.GONE);
            }


        } else {
            noPost_tv.setVisibility(View.VISIBLE);
            miniViewList.setVisibility(View.GONE);
        }
   /*Scroll Events*/
/*

        miniViewList.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int mLastFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                currentScrollState = scrollState;
                isScrollCompleted();
            }


            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (mLastFirstVisibleItem < firstVisibleItem) {

//                    Logger.logsInfo("Main screen : ", "SCROLLING DOWN");
//                    headerLL.animate().alpha(0.0f);
//                    Constant.collapse(headerLLMain, 100, 0);
//                    headerLL.setVisibility(View.GONE);
                }
                if (mLastFirstVisibleItem > firstVisibleItem) {
//                    Logger.logsInfo("Main screen : ", "SCROLLING UP");
//                    Constant.expand(headerLLMain, 100, 110);
//                    headerLL.setVisibility(View.VISIBLE);
//                    headerLL.animate().alpha(1.0f);
                }
                mLastFirstVisibleItem = firstVisibleItem;

                if (miniViewList.getChildAt(0) != null) {
                    swipe_ref_layout.setEnabled(miniViewList.isF == 0 && miniViewList.getChildAt(0).getTop() == 0);
                }
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                mtotalItemCount = totalItemCount;


            }

            private void isScrollCompleted() {
                if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE &&
                        mtotalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
                    */
/*** In this way I detect if there's been a scroll which has completed ***//*

                    */
/*** do the work for load more date! ***//*

                    Logger.logsInfo(HereLoFragmentGridview.class.getSimpleName(), "Load More Called");
                    Logger.logsInfo(HereLoFragmentGridview.class.getSimpleName(), "is_more : " + is_more);

                    if (is_more.equalsIgnoreCase("Yes")) {
                          */
/*Api Calling*//*


                        if (getArguments() != null) {
                            if (getArguments().containsKey(Constant.USER_ID)) {
                                user_id = getArguments().getInt(Constant.USER_ID, 0);
                                if (user_id > 0) {
                                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                        try {
                                            page_no = page_no + 1;
                                            Uri.Builder builder = new Uri.Builder()
                                                    .appendQueryParameter("user_id", user_id + "")
                                                    .appendQueryParameter("post_type", post_content_type)
                                                    .appendQueryParameter("page_no", page_no + "");
                                            switch (screen) {
                                                case 1:
                                                    mHereLosController.callWS(builder, false);
                                                    break;
                                                case 2:
                                                    mHereLoLikesController.callWS(builder, false);
                                                    break;
                                                case 3:
                                                    mHereLoLikedMineController.callWS(builder, false);
                                                    break;
                                                case 4:
                                                    mTagUserPostController.callWS(builder, false);
                                                    break;
                                                default:
                                                    break;
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                                    }

                                }
                            }
                        }
                    }


                }
            }
        });
*/

        if (getArguments() != null) {

            screen = getArguments().getInt("Screen");
            if (screen == 1) {
                _headerTV.setText("HereLos");
            } else if (screen == 2) {
                _headerTV.setText("Liked");
            } else if (screen == 3) {
                _headerTV.setText("Liked Mine");
            } else if (screen == 4) {
                _headerTV.setText("Photos");
            }

            if (getArguments().containsKey(Constant.USER_ID)) {
                user_id = getArguments().getInt(Constant.USER_ID, 0);
                if (user_id > 0) {
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                        try {
                            page_no = 1;
                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_type", post_content_type)
                                    .appendQueryParameter("page_no", page_no + "");

                            switch (screen) {
                                case 1:
                                    mHereLosController.callWS(builder, true);
                                    break;
                                case 2:
                                    mHereLoLikesController.callWS(builder, true);
                                    break;
                                case 3:
                                    mHereLoLikedMineController.callWS(builder, true);
                                    break;
                                case 4:
                                    mTagUserPostController.callWS(builder, true);
                                    break;
                                default:
                                    break;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                    }

                }
            }
        }

    }

    private void setHeaderRightIcon() {
        if (sPref.getBoolean(Constant.isImageVisible, false)) {
            _next1IV.setImageResource(ThemeManager.Current().headerImageSelectIcon());
        } else {
            _next1IV.setImageResource(ThemeManager.Current().headerImageunselectIcon());
        }
        if (sPref.getBoolean(Constant.isVideosVisible, false)) {
            _next2IV.setImageResource(ThemeManager.Current().headerVideoSelectIcon());
        } else {
            _next2IV.setImageResource(ThemeManager.Current().headerVideoUnselectIcon2());
        }

    }


    private void initCache() {
        try {
            snappydb = DBFactory.open(getActivity(), getActivity().getResources().getString(R.string.hereloText));
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_btn2:
//                new MiniViewFragment(),
                MainTabActivity.backbutton();
                break;
            case R.id.next_btn://Image
                page_no = 1;
                isImagesVisible = sPref.getBoolean(Constant.isImageVisible, false);
                if (!isImagesVisible) {

                    //                Hide All Images from the Post list
                    /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {

                            editor.putBoolean(Constant.isImageVisible, true);
                            editor.putBoolean(Constant.isVideosVisible, false);
                            post_content_type = "1";
                            editor.putString(Constant.post_content_type, post_content_type);
                            editor.commit();

                            setHeaderRightIcon();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_type", post_content_type)
                                    .appendQueryParameter("page_no", page_no + "");
                            switch (screen) {
                                case 1:
                                    mHereLosController.callWS(builder, true);
                                    break;
                                case 2:
                                    mHereLoLikesController.callWS(builder, true);
                                    break;
                                case 3:
                                    mHereLoLikedMineController.callWS(builder, true);
                                    break;
                                case 4:
                                    mTagUserPostController.callWS(builder, true);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                /*No Internet screen appears*/
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                    }
                } else {

//                    *//*Show All Images from the Post list*//*
                    /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {

                            editor.putBoolean(Constant.isImageVisible, false);
                            post_content_type = "0";
                            editor.putString(Constant.post_content_type, post_content_type);
                            editor.commit();

                            setHeaderRightIcon();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_type", post_content_type)
                                    .appendQueryParameter("page_no", page_no + "");
                            switch (screen) {
                                case 1:
                                    mHereLosController.callWS(builder, true);
                                    break;
                                case 2:
                                    mHereLoLikesController.callWS(builder, true);
                                    break;
                                case 3:
                                    mHereLoLikedMineController.callWS(builder, true);
                                    break;
                                case 4:
                                    mTagUserPostController.callWS(builder, true);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                /*No Internet screen appears*/
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                    }
                }


                break;

            case R.id.next_btn2: // Video
                page_no = 1;
                isVideosVisible = sPref.getBoolean(Constant.isVideosVisible, false);
                if (!isVideosVisible) {

//                    *//*Hide All Videos from the Post list*//*
                       /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {

                            editor.putBoolean(Constant.isVideosVisible, true);
                            editor.putBoolean(Constant.isImageVisible, false);
                            post_content_type = "2";
                            editor.putString(Constant.post_content_type, post_content_type);
                            editor.commit();

                            setHeaderRightIcon();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_type", post_content_type)
                                    .appendQueryParameter("page_no", page_no + "");
                            switch (screen) {
                                case 1:
                                    mHereLosController.callWS(builder, true);
                                    break;
                                case 2:
                                    mHereLoLikesController.callWS(builder, true);
                                    break;
                                case 3:
                                    mHereLoLikedMineController.callWS(builder, true);
                                    break;
                                case 4:
                                    mTagUserPostController.callWS(builder, true);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                /*No Internet screen appears*/
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                    }
                } else {

//                    *//*Show All Images from the Post list*//*
                       /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {

                            editor.putBoolean(Constant.isVideosVisible, false);
                            post_content_type = "0";
                            editor.putString(Constant.post_content_type, post_content_type);
                            editor.commit();

                            setHeaderRightIcon();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_type", post_content_type)
                                    .appendQueryParameter("page_no", page_no + "");
                            switch (screen) {
                                case 1:
                                    mHereLosController.callWS(builder, true);
                                    break;
                                case 2:
                                    mHereLoLikesController.callWS(builder, true);
                                    break;
                                case 3:
                                    mHereLoLikedMineController.callWS(builder, true);
                                    break;
                                case 4:
                                    mTagUserPostController.callWS(builder, true);
                                    break;
                                default:
                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                /*No Internet screen appears*/
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                    }
                }

                break;
            default:
                break;
        }
    }

    public void response(TagUserPostModalMain modal) {

        swipe_ref_layout.setRefreshing(false);
        int status = modal.getStatus();

        Logger.logsInfo(TAG, "Response Status : " + status);

        if (status == 1) {

            int dataLength = modal.getPosts().size();
            if (dataLength > 0) {
                noPost_tv.setVisibility(View.GONE);
                miniViewList.setVisibility(View.VISIBLE);
            /*Set The Adapter Here*/
                swipe_ref_layout.setRefreshing(false);
                if (page_no == 1) {

                    if (mDataListWhole != null) {
                        if (mDataListWhole.size() > 0) {
                            mDataListWhole.clear();
                        }
                    }
                    if (mDataList != null) {
                        if (mDataList.size() > 0) {
                            mDataList.clear();
                        }
                    }
                    if (mDataListBackup != null) {
                        if (mDataListBackup.size() > 0) {
                            mDataListBackup.clear();
                        }
                    }


                }
                Logger.logsInfo(TAG, "page_no : " + page_no);
                mDataListWhole.add(page_no - 1, modal.getPosts());

                for (int i = 0; i < mDataListWhole.get(page_no - 1).size(); i++) {

                    mDataList.add(mDataListWhole.get(page_no - 1).get(i));
                    mDataListBackup.add(mDataListWhole.get(page_no - 1).get(i));

                }

                if (mDataList.size() > 0) {

                    if (page_no == 1) {

                        adapter = new MiniRecyclerAdapterSingle(miniViewList, getActivity(),
                                mDataList,
                                null,
                                HereLoFragmentMiniview.this,
                                isFromMyProfile);

                        miniViewList.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                }


            } else {
                if (page_no == 1) {
                    noPost_tv.setVisibility(View.VISIBLE);
                    miniViewList.setVisibility(View.GONE);
                } else {
                    noPost_tv.setVisibility(View.GONE);
                    miniViewList.setVisibility(View.VISIBLE);
                }

            }

        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.oopsSomethingWentWrong), getActivity());
            noPost_tv.setVisibility(View.VISIBLE);
            miniViewList.setVisibility(View.GONE);

        }
    }

    /**
     * Adapetr class
     */


}
