package com.tv.herelo.MyProfile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.HomeTab.HomeModal.LikedUserHome;
import com.tv.herelo.HomeTab.LikesFragment;
import com.tv.herelo.HomeTab.post_details.PostDetailsScreen;
import com.tv.herelo.HomeTab.reherelo_details.RehereLoDetails;
import com.tv.herelo.MyProfile.HereLos.HereLosController;
import com.tv.herelo.MyProfile.HereLosLikedMine.HereLoLikedMineController;
import com.tv.herelo.MyProfile.HereLosLikes.HereLoLikesController;
import com.tv.herelo.MyProfile.taguserposts.TagUserPostController;
import com.tv.herelo.MyProfile.taguserposts.tagUserPostModal.TagUserPostModalMain;
import com.tv.herelo.R;
import com.tv.herelo.adapter.GridViewAdapterRecycler;
import com.tv.herelo.bean.MiniViewBean;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.AddEditPost.EditReHereLo;
import com.tv.herelo.tab.BaseContainerFragment;
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
import com.tv.herelo.utils.OnLoadMoreListener;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HereLoFragmentGridview extends Fragment implements View.OnClickListener {

    private final String TAG = HereLoFragmentGridview.class.getSimpleName();

    @Bind(R.id.fragment_container)
    FrameLayout fragment_container;
    private ImageView _left1IV;
    private ImageView _left2IV;
    private ImageView _next1IV;
    private ImageView _next2IV;

    private TextView _headerTV;
    private Typeface tf;

    private RecyclerView _gridView = null;
    private TextView noPost_tv;
    private ImageView _headerIV;
    private LinearLayout header_ll;
    private LinearLayout headerLLMain;
    //    private GridViewAdapter adapter;
    // private HereLoGridViewAdapterSingle adapter;
    private GridViewAdapterRecycler adapter;
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
    private boolean isFromMyProfileRehereLo;
    AppBarLayout appBarLayout;
    /*Share Pref*/
    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;


    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int mtotalItemCount = 0;
    int currentScrollState = 0;


    private List<List<DatumHome>> mDataListWhole = new ArrayList<>();
    private List<DatumHome> mDataList = new ArrayList<>();
    private List<DatumHome> mDataListBackup = new ArrayList<>();


    /*Controller*/
    private TagUserPostController mTagUserPostController = new TagUserPostController(this);
    private HereLosController mHereLosController = new HereLosController(this);
    private HereLoLikedMineController mHereLoLikedMineController = new HereLoLikedMineController(this);
    private HereLoLikesController mHereLoLikesController = new HereLoLikesController(this);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ErrorReporter.getInstance().Init(getActivity());
        View mView = inflater.inflate(R.layout.gird_view_screen, null);
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

        initCache();
       /* try {
            if (snappydb.exists(Constant.PROFILEVIEW_TYPE)) {
                Logger.logsInfo(TAG, "snappydb.get(Constant.VIEW_TYPE)" + snappydb.get(Constant.PROFILEVIEW_TYPE));
                if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MINIVIEW_TYPE)) {

                }
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }*/

      /*  try {
            if (snappydb.exists(Constant.PROFILEVIEW_TYPE)) {
                Logger.logsInfo(TAG, "snappydb.get(Constant.VIEW_TYPE)" + snappydb.get(Constant.PROFILEVIEW_TYPE));
                if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MINIVIEW_TYPE)) {
                    Handler uiHandler = new Handler();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            HereLoFragmentMiniview hereLoFragment = new HereLoFragmentMiniview();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Screen", 1);
                            bundle.putInt(Constant.USER_ID, user_id);
                            hereLoFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragment, true);
                            _left1IV.setImageResource(R.drawable.grid_icon);
                        }
                    });

                } else if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MAINVIEW_TYPE)) {
                    Handler uiHandler = new Handler();
                    uiHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            HereLoFragmentMainview hereLoFragment = new HereLoFragmentMainview();
                            Bundle bundle = new Bundle();
                            bundle.putInt("Screen", 1);
                            bundle.putInt(Constant.USER_ID, user_id);
                            hereLoFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragment, true);
                            _left1IV.setImageResource(R.drawable.grid_icon);
                        }
                    });

                }
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }*/


        return mView;
    }

    private int screen = 0;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        screen = getArguments().getInt("Screen");
        initView();


    }

    private void initView() {
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

        _gridView = (RecyclerView) getView().findViewById(R.id.type_gridView_RV);
        appBarLayout = (AppBarLayout) getView().findViewById(R.id.appBarLayout);
        _gridView.setVisibility(View.VISIBLE);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 3);
        _gridView.setLayoutManager(linearLayoutManager);
        //   _gridView.setItemAnimator(new DefaultItemAnimator());
        noPost_tv = (TextView) getView().findViewById(R.id.noPost_tv);

        if (getArguments() != null) {

            if (getArguments().containsKey(Constant.FROM_MYPROFILE)) {
                isFromMyProfile = getArguments().getBoolean(Constant.FROM_MYPROFILE);
            }
            if (getArguments().containsKey(Constant.FROM_MYPROFILE_REHERELO)) {
                isFromMyProfileRehereLo = getArguments().getBoolean(Constant.FROM_MYPROFILE_REHERELO);
            }
        }

        swipe_ref_layout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_ref_layout);
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


                adapter = new GridViewAdapterRecycler(_gridView, getActivity(),
                        mDataList,
                        true,
                        fragment_container,
                        null,
                        HereLoFragmentGridview.this, null, isFromMyProfile, isFromMyProfileRehereLo);
                _gridView.setAdapter(adapter);

            } else {
                noPost_tv.setVisibility(View.VISIBLE);
                _gridView.setVisibility(View.GONE);
            }


        } else {
            noPost_tv.setVisibility(View.VISIBLE);
            _gridView.setVisibility(View.GONE);
        }


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
        if (adapter != null) {
            adapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                    if (is_more.equalsIgnoreCase("Yes")) {

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
            });
        }

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                header_ll.setAlpha(1.0f - Math.abs(verticalOffset / (float)
                        appBarLayout.getTotalScrollRange()));


            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        //mDataList.clear();
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
                _gridView.setVisibility(View.VISIBLE);
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

                        adapter = new GridViewAdapterRecycler(_gridView, getActivity(),
                                mDataList,
                                true,
                                fragment_container,
                                null,
                                HereLoFragmentGridview.this, null, isFromMyProfile, isFromMyProfileRehereLo);
                        _gridView.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                }


            } else {
                if (page_no == 1) {
                    noPost_tv.setVisibility(View.VISIBLE);
                    _gridView.setVisibility(View.GONE);
                } else {
                    noPost_tv.setVisibility(View.GONE);
                    _gridView.setVisibility(View.VISIBLE);
                }

            }

        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.oopsSomethingWentWrong), getActivity());
            noPost_tv.setVisibility(View.VISIBLE);
            _gridView.setVisibility(View.GONE);

        }
    }

    /**
     * Adapetr class
     */


    public class HereLosAdapter extends BaseAdapter {

        private Activity context = null;
        public ArrayList<MiniViewBean> list_item = new ArrayList<MiniViewBean>();

        public int clickpos = 0;

        String applicant_id;
        ViewHolder view_holder = new ViewHolder();
        SharedPreferences.Editor editor = null;

        public HereLosAdapter(Activity context, ArrayList<MiniViewBean> list_item) {
            this.context = context;
            this.list_item = list_item;
        }

        @Override
        public int getCount() {
            return 16;
        }

        @Override
        public Object getItem(int position) {
            return list_item.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {

            public ImageView _herelo_img;

            public TextView _userName;
            public TextView _comment_count;
            public TextView _repost_count;
            public TextView _like_count;
            public LinearLayout _repostLL;

            private LinearLayout like_ll;
            private LinearLayout like_click_new;
            private LinearLayout comment_click_ll;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder view_holder;

            //final MiniViewBean bean = (MiniViewBean) list_item.get(position);

            if (convertView == null) {
                view_holder = new ViewHolder();

                LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = _linflater.inflate(R.layout.gridview_row, null);

                view_holder._herelo_img = (ImageView) convertView.findViewById(R.id.herelo_img);

                view_holder._userName = (TextView) convertView.findViewById(R.id.username_tv);
                view_holder._comment_count = (TextView) convertView.findViewById(R.id.comment_count);
                view_holder._repost_count = (TextView) convertView.findViewById(R.id.likes_count);
                view_holder._like_count = (TextView) convertView.findViewById(R.id.likes_count);

                view_holder._repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                view_holder.like_ll = (LinearLayout) convertView.findViewById(R.id.likeLL);
                view_holder.like_click_new = (LinearLayout) convertView.findViewById(R.id.like_click_new);

                view_holder.comment_click_ll = (LinearLayout) convertView.findViewById(R.id.coomment_click);

                convertView.setTag(view_holder);

            } else {
                view_holder = (ViewHolder) convertView.getTag();
            }

            view_holder._userName.setVisibility(View.GONE);


            if (view_holder._repostLL != null) {
                view_holder._repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //	_shareLL.setVisibility(View.GONE);
                        popupWindow();
                    }
                });
            }

            if (view_holder.like_click_new != null) {
                view_holder.like_click_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Likes");
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);

                    }
                });
            }

            if (view_holder.like_ll != null) {
                view_holder.like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Likes");
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                    }
                });
            }


            view_holder.comment_click_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LikesFragment likesFragment = new LikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Screen", "Comments");
                    likesFragment.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                }
            });


            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.image1);

            Bitmap bitmap = getRefelection(icon);

            if (bitmap != null) {
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                view_holder._herelo_img.setBackgroundDrawable(ob);
            }


            return convertView;
        }


        public void popupWindow() {

            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.post_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setTitle("");

            TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
            titleTV.setTypeface(tf);


            EditText postET = (EditText) dialog.findViewById(R.id.postET);
            postET.setTypeface(tf);

            ImageView done = (ImageView) dialog.findViewById(R.id.doneIV);
            TextView editIV = (TextView) dialog.findViewById(R.id.editRehereLoIV);
            TextView cancelIV = (TextView) dialog.findViewById(R.id.cancelIV);


            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditReHereLo editReHereLo = new EditReHereLo();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(editReHereLo, true);
                    dialog.dismiss();
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            cancelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.show();
            } else
                dialog.show();
        }
    }


    @SuppressLint("NewApi")
    public Bitmap getRefelection(Bitmap image) {
        // The gap we want between the reflection and the original image
        final int reflectionGap = 0;

        // Get your bitmap from drawable folder
        Bitmap originalImage = image;

        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // This will not scale but will flip on the Y axis
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

		  /*Create a Bitmap with the flip matix applied to it.
           We only want the bottom half of the image*/

        Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
                height / 2, width, height / 2, matrix, false);

		/*Bitmap outputBitmap = Bitmap.createBitmap(reflectionImage);
        RenderScript rs = RenderScript.create(getActivity());
		ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
		Allocation tmpIn = Allocation.createFromBitmap(rs, reflectionImage);
		Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
		theIntrinsic.setRadius(7.5f);
		theIntrinsic.setInput(tmpIn);
		theIntrinsic.forEach(tmpOut);
		tmpOut.copyTo(outputBitmap);*/


        // Create a new bitmap with same width but taller to fit reflection
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 3 + 8), Bitmap.Config.ARGB_8888);
        // Create a new Canvas with the bitmap that's big enough for
        // the image plus gap plus reflection
        Canvas canvas = new Canvas(bitmapWithReflection);
        // Draw in the original image
        canvas.drawBitmap(originalImage, 0, 0, null);
        //Draw the reflection Image
        canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

        // Create a shader that is a linear gradient that covers the reflection
          /*Paint paint = new Paint();
          LinearGradient shader = new LinearGradient(0,
		    originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
		    + reflectionGap, 0x99ffffff, 0x00ffffff, TileMode.CLAMP);
		  // Set the paint to use this shader (linear gradient)
		  paint.setShader(shader);
		  // Set the Transfer mode to be porter duff and destination in
		  paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));*/
        // Draw a rectangle using the paint with our linear gradient
          /*canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
            + reflectionGap, paint);*/
        if (originalImage != null && originalImage.isRecycled()) {
            originalImage.recycle();
            originalImage = null;
        }
        if (reflectionImage != null && reflectionImage.isRecycled()) {
            reflectionImage.recycle();
            reflectionImage = null;
        }
        /*if(outputBitmap!=null && outputBitmap.isRecycled()){
            outputBitmap.recycle();
			outputBitmap=null;
		}*/
        return bitmapWithReflection;
    }

    public class GridViewAdapter extends BaseAdapter {

        private Activity context = null;
        List<DatumHome> mDataList;

        public int clickpos = 0;

        String applicant_id;
        ViewHolder view_holder = new ViewHolder();
        SharedPreferences.Editor editor = null;
        boolean fromGridHome = false;

        /////////////////////
        private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
        private long lastPressTime;
        private int _lastPos;
        private boolean mHasDoubleClicked = false;
        int heightDevice = 0;
        int widthDevice = 0;
        int widthOfSmallImg = 0;

        /*Image Loader*/
        private DisplayImageOptions options;
        ImageLoader imageLoaderNew;

        public GridViewAdapter(Activity context, List<DatumHome> dataList, boolean mfromGridHome) {
            this.context = context;
            this.mDataList = dataList;
            this.fromGridHome = mfromGridHome;

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            widthDevice = displayMetrics.widthPixels;

            widthOfSmallImg = widthDevice / 3;
            imageLoaderNew = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.post_img_default) //
                    .showImageForEmptyUri(R.drawable.post_img_default)
                    .showImageOnFail(R.drawable.post_img_default)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
        }

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int position) {
            return mDataList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder {

            public ImageView _herelo_img;
            public ImageView coomment_img;
            public ImageView smile_img1;
            public ImageView dislike_img;
            public ImageView play_icon;

            public TextView _userName;
            public TextView _comment_count;
            public TextView likes_count;
            public TextView post_tv;
            public TextView more_images_count_tv;
            //            public TextView _like_count;
            public TextView dislike_count;
            public RelativeLayout _repostLL;
            public RelativeLayout reherelo_tag_rel;
            public RelativeLayout sub_layout;

            private LinearLayout like_ll;
            private LinearLayout dislike_click_new;
            private LinearLayout bottom_ll;

            public LinearLayout _commentLL;
            public ProgressBar img_progress = null;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder view_holder;

            //final MiniViewBean bean = (MiniViewBean) list_item.get(position);

            if (convertView == null) {
                view_holder = new ViewHolder();

                LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = _linflater.inflate(R.layout.gridview_row, null);

                view_holder._herelo_img = (ImageView) convertView.findViewById(R.id.herelo_img);
                view_holder.coomment_img = (ImageView) convertView.findViewById(R.id.coomment_img);
                view_holder.dislike_img = (ImageView) convertView.findViewById(R.id.dislike_img);
                view_holder.play_icon = (ImageView) convertView.findViewById(R.id.play_icon);
                view_holder.smile_img1 = (ImageView) convertView.findViewById(R.id.smile_img1);

                view_holder._userName = (TextView) convertView.findViewById(R.id.username_tv);
                view_holder._comment_count = (TextView) convertView.findViewById(R.id.comment_count);
                view_holder.likes_count = (TextView) convertView.findViewById(R.id.likes_count);
                view_holder.post_tv = (TextView) convertView.findViewById(R.id.post_tv);
                view_holder.more_images_count_tv = (TextView) convertView.findViewById(R.id.more_images_count_tv);
//                view_holder._like_count = (TextView) convertView.findViewById(R.id.like_count);
                view_holder.dislike_count = (TextView) convertView.findViewById(R.id.dislike_count);

                view_holder._repostLL = (RelativeLayout) convertView.findViewById(R.id.repost_ll);
                view_holder.reherelo_tag_rel = (RelativeLayout) convertView.findViewById(R.id.reherelo_tag_rel);

                view_holder.like_ll = (LinearLayout) convertView.findViewById(R.id.like_click_new);
                view_holder.dislike_click_new = (LinearLayout) convertView.findViewById(R.id.dislike_click_new);
                view_holder._commentLL = (LinearLayout) convertView.findViewById(R.id.coomment_click);
                view_holder.bottom_ll = (LinearLayout) convertView.findViewById(R.id.bottom_ll);
                view_holder.img_progress = (ProgressBar) convertView.findViewById(R.id.img_progress);

                view_holder._userName.setTypeface(tf);
                view_holder._comment_count.setTypeface(tf);
//                view_holder._like_count.setTypeface(tf);
                view_holder.likes_count.setTypeface(tf);
                view_holder.dislike_count.setTypeface(tf);
                view_holder.post_tv.setTypeface(tf);
                view_holder.more_images_count_tv.setTypeface(tf);

                view_holder.sub_layout = (RelativeLayout) convertView.findViewById(R.id.sub_layout);


// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = view_holder.sub_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = widthOfSmallImg;
                Constant.setVerticalTextViewScrollable(view_holder.post_tv);
                convertView.setTag(view_holder);

            } else {
                view_holder = (ViewHolder) convertView.getTag();
            }


            final DatumHome bean = mDataList.get(position);
            if (bean.getIs_reherelo() == 1) {
                view_holder.reherelo_tag_rel.setVisibility(View.VISIBLE);
            } else {
                view_holder.reherelo_tag_rel.setVisibility(View.GONE);
            }
            if (view_holder.bottom_ll != null) {
//                view_holder.bottom_ll.setBackgroundResource(R.drawable.gry_slide_bg);
                view_holder.bottom_ll.setBackgroundResource(R.drawable.overlap_image);
            }


            if (view_holder.coomment_img != null) {
                view_holder.coomment_img.setImageResource(R.drawable.chat_icon_01);
            }
           /* if (fromGridHome) {
                view_holder._repostLL.setVisibility(View.GONE);
            } else {
                view_holder._repostLL.setVisibility(View.VISIBLE);
            }*/
            view_holder._repostLL.setVisibility(View.GONE);
            if (fromGridHome) {
                view_holder._userName.setVisibility(View.VISIBLE);
            } else {
                view_holder._userName.setVisibility(View.GONE);
            }


            view_holder._repostLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //	_shareLL.setVisibility(View.GONE);
                    popupWindow();
                }
            });

            view_holder.like_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getLike() > 0) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Likes");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", position);
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                    }

                }
            });

            view_holder._commentLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LikesFragment likesFragment = new LikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Screen", "Comments");
                    bundle.putInt("POSTID", bean.getId());
                    bundle.putInt("INDEX", position);
                    likesFragment.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                }
            });
            view_holder.dislike_click_new.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (bean.getDislike() > 0) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "DisLikes");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", position);
                        likesFragment.setArguments(bundle);

                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                    }

                }
            });

            /*Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.image1);

            Bitmap bitmap = getRefelection(icon);

            if (bitmap != null) {
                BitmapDrawable ob = new BitmapDrawable(getResources(), bitmap);
                view_holder._herelo_img.setBackgroundDrawable(ob);
            }*/

            view_holder._herelo_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      /*Code of click*/
                    findDoubleClick(position);


                    if (!mHasDoubleClicked) {
                        _lastPos = position;

                    }

                }
            });

            view_holder.post_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      /*Code of click*/
                    findDoubleClick(position);


                    if (!mHasDoubleClicked) {

                        _lastPos = position;
                    }
                }
            });


            /*Managing Post Type */
            if (bean.getIs_reherelo() == 1) {

                if (bean.getOriginalPost().getPost_type() == 1) {
                    view_holder.play_icon.setVisibility(View.GONE);
                    view_holder._herelo_img.setVisibility(View.VISIBLE);
                    if (view_holder.post_tv != null) {
                        view_holder.post_tv.setVisibility(View.GONE);

                    }
                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                        view_holder._herelo_img.setVisibility(View.VISIBLE);
                        if (view_holder.more_images_count_tv != null) {
                            if (bean.getOriginalPost().getPostMedia().size() > 1) {
                                view_holder.more_images_count_tv.setVisibility(View.VISIBLE);
                                int count = bean.getOriginalPost().getPostMedia().size() - 1;
                                view_holder.more_images_count_tv.setText("+" + count);
                            } else {
                                view_holder.more_images_count_tv.setVisibility(View.GONE);
                            }
                        }

                        imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(0).getUrl(), view_holder._herelo_img, options);

                    } else {
                        view_holder._herelo_img.setVisibility(View.INVISIBLE);
                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                    }


                } else if (bean.getOriginalPost().getPost_type() == 2) {
                    view_holder._herelo_img.setVisibility(View.VISIBLE);
                    view_holder.play_icon.setVisibility(View.VISIBLE);
                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                        imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(0).getThumbUrl(), view_holder._herelo_img, options);
                    }

                    view_holder.more_images_count_tv.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.INVISIBLE);
                } else if (bean.getOriginalPost().getPost_type() == 3) {
                    view_holder.play_icon.setVisibility(View.GONE);
                    view_holder.more_images_count_tv.setVisibility(View.GONE);
                    view_holder._herelo_img.setVisibility(View.INVISIBLE);
                    if (view_holder.post_tv != null) {
                        view_holder.post_tv.setVisibility(View.VISIBLE);
                            /*Clickable TextView*/
                        if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {

                            HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                @Override
                                public void onHashTagClicked(String hashTag) {
//                                            //Constant.showToast("You clicked on :" + hashTag, getActivity());
                                }
                            });
                            view_holder.post_tv.setText(bean.getOriginalPost().getText());
                            // pass a TextView or any descendant of it (incliding EditText) here.
                            // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                            mTextHashTagHelper.handle(view_holder.post_tv);


                        } else {
                            view_holder.post_tv.setText("");
                        }

                    }

                }


            } else {
                if (bean.getPostType() != null) {
                    if (bean.getPostType().equalsIgnoreCase("1")) {
                        view_holder.play_icon.setVisibility(View.GONE);
                        view_holder._herelo_img.setVisibility(View.VISIBLE);
                        if (view_holder.post_tv != null) {
                            view_holder.post_tv.setVisibility(View.GONE);

                        }
                        if (bean.getPostMedia().size() > 0) {
                            view_holder._herelo_img.setVisibility(View.VISIBLE);
                            if (view_holder.more_images_count_tv != null) {
                                if (bean.getPostMedia().size() > 1) {
                                    view_holder.more_images_count_tv.setVisibility(View.VISIBLE);
                                    int count = bean.getPostMedia().size() - 1;
                                    view_holder.more_images_count_tv.setText("+" + count);
                                } else {
                                    view_holder.more_images_count_tv.setVisibility(View.GONE);
                                }
                            }

                            imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), view_holder._herelo_img, options);

                        } else {
                            view_holder._herelo_img.setVisibility(View.INVISIBLE);
                            view_holder.more_images_count_tv.setVisibility(View.GONE);
                        }


                    } else if (bean.getPostType().equalsIgnoreCase("2")) {
                        view_holder._herelo_img.setVisibility(View.VISIBLE);
                        view_holder.play_icon.setVisibility(View.VISIBLE);
                        if (bean.getPostMedia().size() > 0) {
                            imageLoaderNew.displayImage(bean.getPostMedia().get(0).getThumb_url(), view_holder._herelo_img, options);
                        }

                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                        view_holder.post_tv.setVisibility(View.INVISIBLE);
                    } else if (bean.getPostType().equalsIgnoreCase("3")) {
                        view_holder.play_icon.setVisibility(View.GONE);
                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                        view_holder._herelo_img.setVisibility(View.INVISIBLE);
                        if (view_holder.post_tv != null) {
                            view_holder.post_tv.setVisibility(View.VISIBLE);
                            /*Clickable TextView*/
                            if (!bean.getText().equalsIgnoreCase("")) {
                                if (bean.getHashTags().size() > 0) {
                                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                        @Override
                                        public void onHashTagClicked(String hashTag) {
                                            //Constant.showToast("You clicked on :" + hashTag, getActivity());
                                        }
                                    });
                                    view_holder.post_tv.setText(bean.getText());
                                    // pass a TextView or any descendant of it (incliding EditText) here.
                                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                    mTextHashTagHelper.handle(view_holder.post_tv);
                                } else {
                                    view_holder.post_tv.setText(bean.getText());
                                }

                            } else {
                                view_holder.post_tv.setText("");
                            }

                        }

                    }

                }
            }


            /*Populating data*/

            if (bean.getIs_like_status() == 1) {
                /*Liked*/
                view_holder.smile_img1.setImageResource(R.drawable.smile_icon);
            } else {
                view_holder.smile_img1.setImageResource(R.drawable.smiley_icon_01);
            }

            view_holder.smile_img1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                     /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(context)) {

                        int is_like_status = bean.getIs_like_status();

                        if (is_like_status == 1) {

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("unlike_Removedislike", 1 + "");
                            DislikeRemoveDislike task = new DislikeRemoveDislike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }

                            if (bean.getIs_like_status() == 1) {
                                bean.setLike(bean.getLike() - 1);
                            }
                            bean.setIs_like_status(0);


                            for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                if (bean.getLikedUser().get(i) != null) {
                                    String names = bean.getLikedUser().get(i).getUsername();
                                    if (names.equalsIgnoreCase(username)) {
                            /*Remove user name from liked names*/
                                        bean.getLikedUser().remove(bean.getLikedUser().get(i));
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }

                            notifyDataSetChanged();
                        } else {


                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("like_dislike", 1 + "");
                            LikeUnLike task = new LikeUnLike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }
                            bean.setIs_like_status(1);
                            if (bean.getIs_dislike_status() == 1) {
                                bean.setDislike(bean.getDislike() - 1);
                            }

                            bean.setIs_dislike_status(0);

                            if (bean.getLikedUser().size() > 0) {
                                for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                    if (bean.getLikedUser().get(i) != null) {
                                        String names = bean.getLikedUser().get(i).getUsername();
                                        if (!names.equalsIgnoreCase(username)) {

                                            /*Add user name from liked names*/
                                            LikedUserHome mLikedUserHome = new LikedUserHome();
                                            mLikedUserHome.setUsername(username);
                                            mLikedUserHome.setUser_id(user_id + "");

                                            bean.getLikedUser().add(mLikedUserHome);

                                            notifyDataSetChanged();
                                            break;
                                        }

                                    }

                                }
                            } else {
                                LikedUserHome mLikedUserHome = new LikedUserHome();
                                mLikedUserHome.setUsername(username);
                                mLikedUserHome.setUser_id(user_id + "");
                                List<LikedUserHome> mLikedUserHomeList = new ArrayList<LikedUserHome>();
                                mLikedUserHomeList.add(mLikedUserHome);
                                bean.setLikedUser(mLikedUserHomeList);
                            }

                            bean.setLike(bean.getLike() + 1);
                            notifyDataSetChanged();
                        }


                    } else {
                        /*No Internet*/
                        Constant.showToast(context.getString(R.string.internet), context);
                    }
                }
            });

            if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                view_holder.dislike_img.setImageResource(R.drawable.orange_dislike);
            } else {
                view_holder.dislike_img.setImageResource(R.drawable.sad_icon_home_screen);
            }


            view_holder.dislike_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(context)) {

                        int is_dislike_status = bean.getIs_dislike_status();

                        if (is_dislike_status == 1) {

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("unlike_Removedislike", 2 + "");
                            DislikeRemoveDislike task = new DislikeRemoveDislike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }

                            if (bean.getIs_dislike_status() == 1) {
                                bean.setDislike(bean.getDislike() - 1);
                            }

                            bean.setIs_dislike_status(0);


                            notifyDataSetChanged();
                        } else {


                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("like_dislike", 2 + "");
                            LikeUnLike task = new LikeUnLike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }
                            bean.setIs_dislike_status(1);
                            bean.setDislike(bean.getDislike() + 1);
                            if (bean.getIs_like_status() == 1) {
                                bean.setLike(bean.getLike() - 1);
                            }
                            bean.setIs_like_status(0);
                            for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                if (bean.getLikedUser().get(i) != null) {
                                    String names = bean.getLikedUser().get(i).getUsername();
                                    if (names.equalsIgnoreCase(username)) {
                            /*Remove user name from liked names*/
                                        bean.getLikedUser().remove(bean.getLikedUser().get(i));
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }
                            notifyDataSetChanged();
                        }


                    } else {
                        /*No Internet*/
                        Constant.showToast(context.getString(R.string.internet), context);
                    }
                }
            });

            view_holder._userName.setText(bean.getUser().getUsername());
            if (bean.getTotalComments() > 0) {
                view_holder._comment_count.setText(bean.getTotalComments() + "");
            } else {
                view_holder._comment_count.setText("");
            }


            if (bean.getDislike() > 0) {
                view_holder.dislike_count.setText(bean.getDislike() + "");
            } else {
                view_holder.dislike_count.setText("");
            }
            if (bean.getLike() > 0) {
                view_holder.likes_count.setText(bean.getLike() + "");
            } else {
                view_holder.likes_count.setText("");
            }


            return convertView;
        }


        //////////////////////////
        private boolean findDoubleClick(final int position) {
            // Get current time in nano seconds.
            long pressTime = System.currentTimeMillis();

            final int mCurrentImgInd = 0;
            // If double click...
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {

                if (_lastPos == position) {


                    mHasDoubleClicked = true;
                    Logger.logsInfo("============>double click", "double click");

                    final DatumHome bean = mDataList.get(position);
                     /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(context)) {

                        int is_like_status = bean.getIs_like_status();

                        if (is_like_status == 1) {

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("unlike_Removedislike", 1 + "");
                            DislikeRemoveDislike task = new DislikeRemoveDislike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }

                            if (bean.getIs_like_status() == 1) {
                                bean.setLike(bean.getLike() - 1);
                            }
                            bean.setIs_like_status(0);

                            for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                if (bean.getLikedUser().get(i) != null) {
                                    String names = bean.getLikedUser().get(i).getUsername();
                                    if (names.equalsIgnoreCase(username)) {
                            /*Remove user name from liked names*/
                                        bean.getLikedUser().remove(bean.getLikedUser().get(i));
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }

                            notifyDataSetChanged();
                        } else {


                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("post_id", bean.getId() + "")
                                    .appendQueryParameter("like_dislike", 1 + "");
                            LikeUnLike task = new LikeUnLike(WebserviceTask.POST,
                                    null, null, builder);

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            } else {
                                task.execute();
                            }
                            bean.setIs_like_status(1);
                            if (bean.getIs_dislike_status() == 1) {
                                bean.setDislike(bean.getDislike() - 1);
                            }

                            bean.setIs_dislike_status(0);

                            if (bean.getLikedUser().size() > 0) {
                                for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                    if (bean.getLikedUser().get(i) != null) {
                                        String names = bean.getLikedUser().get(i).getUsername();
                                        if (!names.equalsIgnoreCase(username)) {

                                            /*Add user name from liked names*/
                                            LikedUserHome mLikedUserHome = new LikedUserHome();
                                            mLikedUserHome.setUsername(username);
                                            mLikedUserHome.setUser_id(user_id + "");

                                            bean.getLikedUser().add(mLikedUserHome);

                                            notifyDataSetChanged();
                                            break;
                                        }

                                    }

                                }
                            } else {
                                LikedUserHome mLikedUserHome = new LikedUserHome();
                                mLikedUserHome.setUsername(username);
                                mLikedUserHome.setUser_id(user_id + "");
                                List<LikedUserHome> mLikedUserHomeList = new ArrayList<LikedUserHome>();
                                mLikedUserHomeList.add(mLikedUserHome);
                                bean.setLikedUser(mLikedUserHomeList);
                            }

                            bean.setLike(bean.getLike() + 1);
                            notifyDataSetChanged();
                        }


                    } else {
                        /*No Internet*/
                        Constant.showToast(context.getString(R.string.internet), context);
                    }
                } else {
                    mHasDoubleClicked = false;
                    Logger.logsInfo("============>Single click", "Single click");


                    final DatumHome bean = mDataList.get(position);
                    if (bean.getIs_reherelo() == 1) {
                        Intent mIntent = new Intent(getActivity(), RehereLoDetails.class);
                        mIntent.putExtra(Constant.USER_ID, bean.getUserId());
                        mIntent.putExtra(Constant.POST_ID, bean.getOriginal_post_id());
                        mIntent.putExtra(Constant.USERNAME, bean.getUser().getUsername());
                        mIntent.putExtra(Constant.USERNAME_OG, bean.getOriginalPost().getUser().getUsername());
                        mIntent.putExtra(Constant.USER_LOC, bean.getLocation());
                        mIntent.putExtra(Constant.USER_LOC_OG, bean.getOriginalPost().getPost_location());
                        mIntent.putExtra(Constant.TOTAL_POST_VIEW, bean.getTotalPostView());
                        mIntent.putExtra(Constant.TOTAL_POST_VIEW_OG, bean.getOriginalPost().getTotalPostView());
                        mIntent.putExtra(Constant.POST_TIME, bean.getPostTime());
                        mIntent.putExtra(Constant.POST_TIME_OG, bean.getOriginalPost().getPostTime());
                        mIntent.putExtra(Constant.POST_TEXT_OG, bean.getOriginalPost().getText());
                        mIntent.putExtra(Constant.POST_TEXT, bean.getText());
                        mIntent.putExtra(Constant.USER_IMG, bean.getUser().getImage());
                        mIntent.putExtra(Constant.USER_IMG_OG, bean.getOriginalPost().getUser().getImage());
                        mIntent.putExtra(Constant.POST_TYPE_REHERELO, bean.getOriginalPost().getPost_type());
                        mIntent.putExtra(Constant.POST_TYPE_REHERELO, bean.getOriginalPost().getPost_type());
                        mIntent.putExtra(Constant.FRAME_ID_REHERELO, bean.getOriginalPost().getFrame_id());
                        mIntent.putExtra(Constant.OG_COMMENT_COUNT, bean.getOriginalPost().getTotalComments());
                        mIntent.putExtra(Constant.OG_LIKES_COUNT, bean.getOriginalPost().getLike());
                        mIntent.putExtra(Constant.OG_DISLIKES_COUNT, bean.getOriginalPost().getDislike());
                        mIntent.putExtra(Constant.OG_REHERELO_COUNT, bean.getOriginalPost().getReherelo());
                        mIntent.putExtra(Constant.POST_LIKES_COUNT, bean.getLike());
                        mIntent.putExtra(Constant.POST_DISLIKES_COUNT, bean.getDislike());
                        mIntent.putExtra(Constant.POST_COMMENTS_COUNT, bean.getTotalComments());
                        mIntent.putExtra(Constant.POST_REHERELO_COUNT, bean.getReherelo());
                        mIntent.putExtra(Constant.IS_LIKED, bean.getIs_like_status());
                        mIntent.putExtra(Constant.IS_FOLLOWING, bean.getIs_following());
                        mIntent.putExtra(Constant.INDEX, position);
                        if (bean.getOriginalPost().getPostMedia() != null) {
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                mIntent.putExtra(Constant.POST_IMG_LIST,
                                        (Serializable) bean.getOriginalPost().getPostMedia());
                            }
                        }
                        if (bean.getPostComments() != null) {
                            if (bean.getPostComments().size() > 0) {
                                mIntent.putExtra(Constant.POST_COMMENT_LIST,
                                        (Serializable) bean.getPostComments());
                            }
                        }
                        if (bean.getLikedUser().size() > 0) {
                            mIntent.putExtra(Constant.LIKE_USER_LIST,
                                    (Serializable) bean.getLikedUser());
                        }
                        getActivity().startActivity(mIntent);
                    } else {

                        /*Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
                        mIntent.putExtra("VIEWCOUNT", bean.getTotalPostView());
                        mIntent.putExtra("COMMENTCOUNT", bean.getTotalComments());
                        mIntent.putExtra("REHERELOCOUNT", bean.getReherelo());
                        mIntent.putExtra("LIKESCOUNT", bean.getLike());
                        mIntent.putExtra("DISLIKESCOUNT", bean.getDislike());
                        mIntent.putExtra(Constant.IS_LIKED, bean.getIs_like_status());
                        mIntent.putExtra(Constant.IS_DISLIKED, bean.getIs_dislike_status());
                        mIntent.putExtra(Constant.USER_ID_FROM_POST, bean.getUserId());
                        mIntent.putExtra(Constant.POST_IND, position);

                        if (bean.getIs_reherelo() == 1) {
                            mIntent.putExtra("POSTID", Integer.valueOf(bean.getOriginal_post_id()));
                            mIntent.putExtra("ISREHERELO", true);
                            mIntent.putExtra("POSTTEXT", bean.getOriginalPost().getText());
                            mIntent.putExtra("POSTTYPE", bean.getOriginalPost().getPost_type());
                            if (bean.getOriginalPost().getFrame_id() == 0) {
                                if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                    mIntent.putExtra(Constant.MEDIA_ID, bean.getOriginalPost().getPostMedia().get(0).getId());
                                }

                            }
                        } else {
                            mIntent.putExtra("POSTID", bean.getId());
                            mIntent.putExtra("ISREHERELO", false);
                            mIntent.putExtra("POSTTEXT", bean.getText());
                            mIntent.putExtra("POSTTYPE", Integer.parseInt(bean.getPostType()));
                        }

                        if (bean.getPostMedia().size() > 0) {
                            mIntent.putExtra("POSTIMG", bean.getPostMedia().get(0).getUrl());

                            mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getPostMedia());
                            mIntent.putExtra("POSITION", mCurrentImgInd);
                        } else if (bean.getOriginalPost() != null) {
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                mIntent.putExtra("POSTIMG", bean.getOriginalPost().getPostMedia().get(0).getUrl());
                                mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getOriginalPost().getPostMedia());
                                mIntent.putExtra("POSITION", mCurrentImgInd);
                            }
                        }

                        startActivity(mIntent);*/
                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        startActivity(mIntent);
                    }

                }


            } else {     // If not double click....
                mHasDoubleClicked = false;
                Handler myHandler = new Handler() {
                    public void handleMessage(Message m) {
                        if (!mHasDoubleClicked) {
                            final DatumHome bean = mDataList.get(position);
                            Logger.logsInfo("============>Single click", "Single click");
                            if (bean.getIs_reherelo() == 1) {
                                Intent mIntent = new Intent(getActivity(), RehereLoDetails.class);
                                mIntent.putExtra(Constant.USER_ID, bean.getUserId());
                                mIntent.putExtra(Constant.POST_ID, bean.getOriginal_post_id());
                                mIntent.putExtra(Constant.USERNAME, bean.getUser().getUsername());
                                mIntent.putExtra(Constant.USERNAME_OG, bean.getOriginalPost().getUser().getUsername());
                                mIntent.putExtra(Constant.USER_LOC, bean.getLocation());
                                mIntent.putExtra(Constant.USER_LOC_OG, bean.getOriginalPost().getPost_location());
                                mIntent.putExtra(Constant.TOTAL_POST_VIEW, bean.getTotalPostView());
                                mIntent.putExtra(Constant.TOTAL_POST_VIEW_OG, bean.getOriginalPost().getTotalPostView());
                                mIntent.putExtra(Constant.POST_TIME, bean.getPostTime());
                                mIntent.putExtra(Constant.POST_TIME_OG, bean.getOriginalPost().getPostTime());
                                mIntent.putExtra(Constant.POST_TEXT_OG, bean.getOriginalPost().getText());
                                mIntent.putExtra(Constant.POST_TEXT, bean.getText());
                                mIntent.putExtra(Constant.USER_IMG, bean.getUser().getImage());
                                mIntent.putExtra(Constant.USER_IMG_OG, bean.getOriginalPost().getUser().getImage());
                                mIntent.putExtra(Constant.POST_TYPE_REHERELO, bean.getOriginalPost().getPost_type());
                                mIntent.putExtra(Constant.POST_TYPE_REHERELO, bean.getOriginalPost().getPost_type());
                                mIntent.putExtra(Constant.FRAME_ID_REHERELO, bean.getOriginalPost().getFrame_id());
                                mIntent.putExtra(Constant.OG_COMMENT_COUNT, bean.getOriginalPost().getTotalComments());
                                mIntent.putExtra(Constant.OG_LIKES_COUNT, bean.getOriginalPost().getLike());
                                mIntent.putExtra(Constant.OG_DISLIKES_COUNT, bean.getOriginalPost().getDislike());
                                mIntent.putExtra(Constant.OG_REHERELO_COUNT, bean.getOriginalPost().getReherelo());
                                mIntent.putExtra(Constant.POST_LIKES_COUNT, bean.getLike());
                                mIntent.putExtra(Constant.POST_DISLIKES_COUNT, bean.getDislike());
                                mIntent.putExtra(Constant.POST_COMMENTS_COUNT, bean.getTotalComments());
                                mIntent.putExtra(Constant.POST_REHERELO_COUNT, bean.getReherelo());
                                mIntent.putExtra(Constant.IS_LIKED, bean.getIs_like_status());
                                mIntent.putExtra(Constant.IS_FOLLOWING, bean.getIs_following());
                                mIntent.putExtra(Constant.INDEX, position);
                                if (bean.getOriginalPost().getPostMedia() != null) {
                                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                        mIntent.putExtra(Constant.POST_IMG_LIST,
                                                (Serializable) bean.getOriginalPost().getPostMedia());
                                    }
                                }
                                if (bean.getPostComments() != null) {
                                    if (bean.getPostComments().size() > 0) {
                                        mIntent.putExtra(Constant.POST_COMMENT_LIST,
                                                (Serializable) bean.getPostComments());
                                    }
                                }
                                if (bean.getLikedUser().size() > 0) {
                                    mIntent.putExtra(Constant.LIKE_USER_LIST,
                                            (Serializable) bean.getLikedUser());
                                }
                                getActivity().startActivity(mIntent);
                            } else {
                                /*Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
                                mIntent.putExtra("VIEWCOUNT", bean.getTotalPostView());
                                mIntent.putExtra("COMMENTCOUNT", bean.getTotalComments());
                                mIntent.putExtra("REHERELOCOUNT", bean.getReherelo());
                                mIntent.putExtra("LIKESCOUNT", bean.getLike());
                                mIntent.putExtra("DISLIKESCOUNT", bean.getDislike());
                                mIntent.putExtra(Constant.IS_LIKED, bean.getIs_like_status());
                                mIntent.putExtra(Constant.IS_DISLIKED, bean.getIs_dislike_status());
                                mIntent.putExtra(Constant.USER_ID_FROM_POST, bean.getUserId());
                                mIntent.putExtra(Constant.POST_IND, position);

                                if (bean.getIs_reherelo() == 1) {
                                    mIntent.putExtra("POSTID", Integer.valueOf(bean.getOriginal_post_id()));
                                    mIntent.putExtra("ISREHERELO", true);
                                    mIntent.putExtra("POSTTEXT", bean.getOriginalPost().getText());
                                    mIntent.putExtra("POSTTYPE", bean.getOriginalPost().getPost_type());
                                    if (bean.getOriginalPost().getFrame_id() == 0) {
                                        if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                            mIntent.putExtra(Constant.MEDIA_ID, bean.getOriginalPost().getPostMedia().get(0).getId());
                                        }

                                    }
                                } else {
                                    mIntent.putExtra("POSTID", bean.getId());
                                    mIntent.putExtra("ISREHERELO", false);
                                    mIntent.putExtra("POSTTEXT", bean.getText());
                                    mIntent.putExtra("POSTTYPE", Integer.parseInt(bean.getPostType()));
                                }

                                if (bean.getPostMedia().size() > 0) {
                                    mIntent.putExtra("POSTIMG", bean.getPostMedia().get(0).getUrl());

                                    mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getPostMedia());
                                    mIntent.putExtra("POSITION", mCurrentImgInd);
                                } else if (bean.getOriginalPost() != null) {
                                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                        mIntent.putExtra("POSTIMG", bean.getOriginalPost().getPostMedia().get(0).getUrl());
                                        mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getOriginalPost().getPostMedia());
                                        mIntent.putExtra("POSITION", mCurrentImgInd);
                                    }
                                }

                                startActivity(mIntent);*/
                                Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                                mIntent.putExtra(Constant.POST_DETAILS, bean);
                                startActivity(mIntent);
                            }

                        }
                    }
                };
                Message m = new Message();
                myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
            }
            // record the last time the Image was tapped.
            lastPressTime = pressTime;
            return true;
        }

        class LikeUnLike extends WebserviceTask {

            public LikeUnLike(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.like_dislikepost;
            }

            @Override
            public void onLoadingFinished(String response) {


            }
        }

        class DislikeRemoveDislike extends WebserviceTask {

            public DislikeRemoveDislike(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.removeUnlikeDislike;
            }

            @Override
            public void onLoadingFinished(String response) {


            }
        }

        /**
         * reherelo Popup
         */
        public void popupWindow() {

            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.post_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setTitle("");

            TextView titleTV = (TextView) dialog.findViewById(R.id.titleTV);
            titleTV.setTypeface(tf);


            EditText postET = (EditText) dialog.findViewById(R.id.postET);
            postET.setTypeface(tf);

            TextView done = (TextView) dialog.findViewById(R.id.doneIV);
            TextView editIV = (TextView) dialog.findViewById(R.id.editRehereLoIV);
            TextView cancelIV = (TextView) dialog.findViewById(R.id.cancelIV);

            done.setTypeface(tf);
            editIV.setTypeface(tf);
            cancelIV.setTypeface(tf);
            editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditReHereLo editReHereLo = new EditReHereLo();
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(editReHereLo, true);
                    dialog.dismiss();
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            cancelIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.show();
            } else
                dialog.show();
        }
    }


}
