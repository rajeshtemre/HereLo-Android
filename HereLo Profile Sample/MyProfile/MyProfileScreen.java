package com.tv.herelo.MyProfile;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;
import com.squareup.picasso.Picasso;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.HomeTab.HomeModal.LikedUserHome;
import com.tv.herelo.HomeTab.HomeModal.TagedUserList;
import com.tv.herelo.HomeTab.LikesFragment;
import com.tv.herelo.HomeTab.post_details.PostDetailsScreen;
import com.tv.herelo.HomeTab.reherelo_details.RehereLoDetails;
import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityScreen;
import com.tv.herelo.MyProfile.ChatToggle.ChatToggleController;
import com.tv.herelo.MyProfile.YourHereLo.YourHereLoScreen;
import com.tv.herelo.MyProfile.map_cluster.ClusteringMapActivity;
import com.tv.herelo.MyProfile.myprofilemodal.MyProfileModalMain;
import com.tv.herelo.MyProfile.updateCover.UpdateCoverController;
import com.tv.herelo.MyProfile.updateCover.UpdateCoverModalMain;
import com.tv.herelo.Popular.HashTagDetails.HashTagScreenTwoGridView;
import com.tv.herelo.R;
import com.tv.herelo.XListViewPullToRefresh.XListView;
import com.tv.herelo.adapter.HomeMainAdapter;
import com.tv.herelo.adapter.MiniRecyclerAdapterSingle;
import com.tv.herelo.bean.FrameDetailsBean;
import com.tv.herelo.bean.FrameParentBean;
import com.tv.herelo.bean.MiniViewBean;
import com.tv.herelo.chat.ChatDetailsScreen;
import com.tv.herelo.checkIns.CheckInDetailsActivity;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.constants.ViewAnimationUtils;
import com.tv.herelo.database.frames.DataSourceFrames;
import com.tv.herelo.login.UpdateProfileController;
import com.tv.herelo.login.UpdateProfileModal;
import com.tv.herelo.tab.AddEditPost.EditReHereLo;
import com.tv.herelo.tab.BaseContainerFragment;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.tab.ProfileFragment;
import com.tv.herelo.tab.settings.CallBackReplaceStyleTheme;
import com.tv.herelo.tab.settings.SettingsScreen;
import com.tv.herelo.theme.ThemeManager;
import com.tv.herelo.utils.ErrorReporter;
import com.tv.herelo.utils.ImageFilterScreen;
import com.tv.herelo.utils.Logger;
import com.tv.herelo.utils.MyGridViewPost;
import com.tv.herelo.utils.MyImageView;
import com.tv.herelo.utils.OnLoadMoreListener;
import com.tv.herelo.utils.SimpleGestureFilter;
import com.tv.herelo.utils.TextureVideoView;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.lab.toro.Toro;
import im.ene.lab.toro.ToroAdapter;
import im.ene.lab.toro.ToroVideoViewHolder;
import im.ene.lab.toro.widget.ToroVideoView;

import static com.tv.herelo.R.id.bottomRelay;


public class MyProfileScreen extends Fragment implements View.OnClickListener,
        XListView.IXListViewListener, SimpleGestureFilter.SimpleGestureListener,
        CallBackReplaceStyleTheme {

    private final String TAG = MyProfileScreen.class.getSimpleName();
    private boolean isMainView = false;
    private boolean isMiniView = false;
    private boolean isGridView = false;

    View view;
    ImageView edit_iv;
    /*GridView*/
    private MyGridViewPost my_profile_gv;
    private GridViewAdapter adapter;

    /*Main View*/
    @Bind(R.id.main_view_list)
    RecyclerView main_view_list;

    @Bind(R.id.main_rl)
    RelativeLayout main_rl;

    //public MainViewAdapterRecyclerView adapterRecyclerView;
    public HomeMainAdapter adapterRecyclerView;


    /*Mini View*/
    @Bind(R.id.miniView_list)
    RecyclerView miniView_list;

    @Bind(R.id.check_in_iv)
    ImageView check_in_iv;

    @Bind(R.id.header_main_ll)
    LinearLayout header_main_ll;


    /*Swipe Event*/
    private float x1, x2;
    static final int MIN_DISTANCE = 150;


    @Bind(R.id.cover_photo_iv)
    ImageView cover_photo_iv;

    @Bind(R.id.second_postion_iv)
    ImageView second_postion_iv;

    @Bind(R.id.options_ll)
    LinearLayout options_ll;

    @Bind(R.id.chat_on_off_ll)
    LinearLayout chat_on_off_ll;

    @Bind(R.id.swipe_screen)
    SwipeRefreshLayout swipe_screen;

    // private MiniViewAdapter miniViewAdapter;
    private MiniRecyclerAdapterSingle miniViewAdapter;
    private MyProfileModalMain myProfileModalMain;

    /**
     * Swipe
     */
    private SimpleGestureFilter detector;

    private CallBackReplaceStyleTheme mCallBackReplaceStyleTheme;

    public static MyProfileScreen newInstance(Bundle mBundle, CallBackReplaceStyleTheme mCallBackReplaceStyleTheme) {
        MyProfileScreen mMyProfileScreen = new MyProfileScreen();
        mMyProfileScreen.setArguments(mBundle);
        mMyProfileScreen.mCallBackReplaceStyleTheme = mCallBackReplaceStyleTheme;
        return mMyProfileScreen;

    }


    /*Local Database*/
    DataSourceFrames mDataSourceFrames;
    private ArrayList<FrameDetailsBean> mFrameDetailsBeanArrayList = new ArrayList<>();
    private float frameHeight = 0;
    private float frameWidth = 0;
    private float aspectFactor = 0;
    private final int marginLayout = 5;

    int currentFirstVisibleItem = 0;
    int currentVisibleItemCount = 0;
    int mtotalItemCount = 0;
    int currentScrollState = 0;
    private String is_more = "Yes";


    public static List<List<DatumHome>> mDataListWhole = new ArrayList<>();
    public static List<DatumHome> mDataList = new ArrayList<>();
    public static List<DatumHome> mDataListBackup = new ArrayList<>();
    private int page_no = 1;
    private String post_content_type = "0";
    private boolean isImagesVisible = false;
    private boolean isVideosVisible = false;
    private boolean isGVDataLoaded = true;


    LinearLayout followers_ll;
    LinearLayout following_ll;
    LinearLayout friends_ll;
    LinearLayout change_view_ll;
    ImageView change_view_iv;


    /*Header*/
    private ImageView _left1IV;
    private ImageView _next1IV;
    private ImageView _next2IV;
    private ImageView _headerIV;


    private CircularImageView profile_img;
    private TextView _headerTV;
    public LinearLayout _shareLL;
    private LinearLayout header_layout;
    private TextView _cancelIV;
    private Typeface tf;

    private LinearLayout _likeMineLL;
    private LinearLayout _hereloLL;
    private LinearLayout _likedLL;
    private LinearLayout _photoLL;
    private LinearLayout map_ll;
    private LinearLayout activity_ll;
    private LinearLayout share_ll;

    private TextView friends_num_tv;
    private TextView friends_tv;

    private TextView following_num_tv;
    private TextView following_tv;

    private TextView followers_num_tv;
    private TextView followers_tv;

    private TextView HerelosTV;
    private TextView likesTV;
    private TextView likedmineTV;
    private TextView chat_status_tv;
    private ImageView change_chat_status_iv;

    private TextView info_status_tv;


    private TextView number_share_tv;
    private TextView share_tv;
    private TextView user_name_tv;
    private TextView fName_tv_info;
    private TextView fName_tv;
    private TextView movie_tv_info;
    private TextView movie_tv;
    private TextView LName_tv_info;
    private TextView LName_tv;
    private TextView tv_show_tv_info;
    private TextView tv_show_tv;
    private TextView description_tv_info;
    private TextView description_tv;
    private TextView band_tv_info;
    private TextView band_tv;
    private TextView status_tv_info;
    private TextView status_tv;
    private TextView sport_team_tv_info;
    private TextView sport_team_tv;
    private TextView dob_tv_info;
    private TextView dob_tv;
    private TextView sport_tv_info;
    private TextView sport_tv;
    private TextView placeOfBirth_tv_info;
    private TextView placeOfBirth_tv;
    private TextView movieGenre_tv_info;
    private TextView movieGenre_tv;
    private TextView HighSchool_tv_info;
    private TextView HighSchool_tv;
    private TextView musicGenreTv_info;
    private TextView musicGenreTv;
    private TextView clg_tv_info;
    private TextView clg_tv;

    private TextView moreTextView;
    private LinearLayout _infoDetailLinearLayout;
    private LinearLayout _infoLinearLayout;
    private View _view1;

    /*Select Profile Image*/
    private RelativeLayout _bottomRelativeLayout = null;
    private TextView _takeNewButton = null;
    private TextView _galleryButton = null;
    private TextView _defaultButton = null;
    private TextView _cancelButton = null;
    private TranslateAnimation _translateAnimation = null;
    private static final int RESULT_LOAD_IMG = 1;
    private static final int RESULT_LOAD_IMG_COVER = 11;
    private static final int TAKE_PHOTO_CODE = 0;
    private static final int TAKE_PHOTO_CODE_COVER = 10;
    private static final int FILTERIMGCODE = 2;
    private static final int FILTERIMGCODE_COVER = 12;
    File sdImageMainDirectory;
    Uri outputFileUri;
    private String fileToUploadPath = "";
    private String image = "";

    /*Controller*/
    private UpdateProfileController mUpdateProfileController = new UpdateProfileController(this);
    private UpdateCoverController mUpdateCoverController = new UpdateCoverController(this);
    private ChatToggleController mChatToggleController = new ChatToggleController(this);


    /*Request Params*/
    private int user_id = 0;
    boolean fromactivity = false;
    private String username = "";


    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;

    private DisplayImageOptions optionsProfileImg;
    private DisplayImageOptions optionsCoverImgImg;
    ImageLoader imageLoaderNew;


    /*Initializing MyProfile Controller*/

    private MyProfileController controller = new MyProfileController(this);
    private boolean this_is_other_user = false;


    private void setHeaderRightIconImg() {
        if (isImagesVisible) {
            isImagesVisible = false;
            _next1IV.setImageResource(R.drawable.suggetions_image);
        } else {
            isImagesVisible = true;
            _next1IV.setImageResource(R.drawable.suggetions_image_sel);
            if (isVideosVisible) {
                isVideosVisible = false;
                _next2IV.setImageResource(R.drawable.suggetions_video);
            }
        }

    }

    private void setHeaderRightIconVid() {


        if (isVideosVisible) {
            isVideosVisible = false;
            _next2IV.setImageResource(R.drawable.suggetions_video);
        } else {
            isVideosVisible = true;

            _next2IV.setImageResource(R.drawable.suggetions_video_sel);

            if (isImagesVisible) {
                isImagesVisible = false;
                _next1IV.setImageResource(R.drawable.suggetions_image);
            }
        }

    }

    public void successResponse(MyProfileModalMain myProfileModalMain) {
        swipe_screen.setRefreshing(false);

        Logger.logsInfo(TAG, "Status :" + myProfileModalMain.getStatus());
        int status = myProfileModalMain.getStatus();
        if (status == 1) {
            this.myProfileModalMain = myProfileModalMain;
            isGVDataLoaded = true;
            if (myProfileModalMain.getProfile().getIs_checkin() == 1) {
                check_in_iv.setImageResource(R.drawable.check_in_green_icon);
            } else {
                check_in_iv.setImageResource(R.drawable.checkin_icon_grey);
            }
            user_name_tv.setText(myProfileModalMain.getProfile().getUsername());
            if (this_is_other_user) {
                _headerTV.setText(myProfileModalMain.getProfile().getUsername());
                if (myProfileModalMain.getProfile().getIs_online().equalsIgnoreCase("1")) {
                    //todo
                    if (this_is_other_user) {

//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        options_ll.setWeightSum(4);
                        edit_iv.setVisibility(View.GONE);
                        check_in_iv.setVisibility(View.GONE);
                        activity_ll.setVisibility(View.VISIBLE);

                        change_chat_status_iv.setVisibility(View.INVISIBLE);
                        chat_on_off_ll.setVisibility(View.INVISIBLE);
                        _left1IV.setImageResource(ThemeManager.Current().headerLeftArrowIcon());
                        second_postion_iv.setImageResource(R.drawable.some_one_else_chat_icon);
                    } else {
                        options_ll.setWeightSum(4);
                        edit_iv.setVisibility(View.VISIBLE);
                        check_in_iv.setVisibility(View.VISIBLE);
                        activity_ll.setVisibility(View.VISIBLE);
                        change_chat_status_iv.setVisibility(View.VISIBLE);
                        chat_on_off_ll.setVisibility(View.VISIBLE);
                        _left1IV.setImageResource(ThemeManager.Current().headerLeftSettingIcon());
                        second_postion_iv.setImageResource(R.drawable.news_icon);
                    }
                } else {
                    if (this_is_other_user) {

//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                        options_ll.setWeightSum(3);
                        edit_iv.setVisibility(View.GONE);
                        check_in_iv.setVisibility(View.GONE);
                        activity_ll.setVisibility(View.GONE);

                        change_chat_status_iv.setVisibility(View.INVISIBLE);
                        chat_on_off_ll.setVisibility(View.INVISIBLE);
                        _left1IV.setImageResource(ThemeManager.Current().headerLeftArrowIcon());
                        second_postion_iv.setImageResource(R.drawable.some_one_else_chat_icon);

                    } else {
                        options_ll.setWeightSum(4);
                        edit_iv.setVisibility(View.VISIBLE);
                        check_in_iv.setVisibility(View.VISIBLE);
                        activity_ll.setVisibility(View.VISIBLE);
                        change_chat_status_iv.setVisibility(View.VISIBLE);
                        chat_on_off_ll.setVisibility(View.VISIBLE);
                        _left1IV.setImageResource(ThemeManager.Current().headerLeftSettingIcon());
                        second_postion_iv.setImageResource(R.drawable.news_icon);
                    }
                }
            }

            if (myProfileModalMain.getProfile().getIs_online().equalsIgnoreCase("1")) {
                isChatOn = true;
                change_chat_status_iv.setImageResource(R.drawable.android_new_chat_on);
                chat_status_tv.setText(getResources().getString(R.string.chatOnText));
            } else {
                isChatOn = false;
                change_chat_status_iv.setImageResource(R.drawable.android_new_chat_off);
                chat_status_tv.setText(getResources().getString(R.string.chatOffText));
            }
            info_status_tv.setText(myProfileModalMain.getProfile().getPstatus());
            followers_num_tv.setText(myProfileModalMain.getProfile().getFollowers() + "");
            following_num_tv.setText(myProfileModalMain.getProfile().getFollowing() + "");
            number_share_tv.setText(myProfileModalMain.getProfile().getShares() + "");
            friends_num_tv.setText(myProfileModalMain.getProfile().getFriends() + "");
            fName_tv.setText(myProfileModalMain.getProfile().getFname());
            LName_tv.setText(myProfileModalMain.getProfile().getLname());
            description_tv.setText(myProfileModalMain.getProfile().getPstatus());
            status_tv.setText(myProfileModalMain.getProfile().getPstatus());
            dob_tv.setText(myProfileModalMain.getProfile().getDob());
            HighSchool_tv.setText(myProfileModalMain.getProfile().getHighSchool());
            clg_tv.setText(myProfileModalMain.getProfile().getCollege());
            movie_tv.setText(myProfileModalMain.getProfile().getFavmovie());
            tv_show_tv.setText(myProfileModalMain.getProfile().getFavtvshow());
            band_tv.setText(myProfileModalMain.getProfile().getFavband());
            sport_team_tv.setText(myProfileModalMain.getProfile().getFavsportsteam());
            sport_tv.setText(myProfileModalMain.getProfile().getFavsport());
            status_tv.setText(myProfileModalMain.getProfile().getStatus());
            movieGenre_tv.setText(myProfileModalMain.getProfile().getFavgenreofmovie());
            musicGenreTv.setText(myProfileModalMain.getProfile().getFavgenreofmusic());
            placeOfBirth_tv.setText(myProfileModalMain.getProfile().getBirthCity());
            HerelosTV.setText(myProfileModalMain.getProfile().getHerelos() + " " + getActivity().getResources().getString(R.string.hereLosText));
            likesTV.setText(myProfileModalMain.getProfile().getLiked() + " " + getActivity().getResources().getString(R.string.likesText));
            likedmineTV.setText(myProfileModalMain.getProfile().getLikedMine() + " " + getActivity().getResources().getString(R.string.likedmine_tv));
            Glide
                    .with(getActivity())
                    .asGif()
                    .apply(RequestOptions.circleCropTransform().placeholder(R.drawable.profile_icon))
                    .load(myProfileModalMain.getProfile().getImage())

                    .into(profile_img);
            //  imageLoaderNew.displayImage(myProfileModalMain.getProfile().getImage(), profile_img, optionsProfileImg);
            Glide
                    .with(getActivity())
                    .load(myProfileModalMain.getProfile().getCoverPic())
                    .into(cover_photo_iv);
           /* imageLoaderNew.loadImage(myProfileModalMain.getProfile().getCoverPic(), optionsCoverImgImg, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    super.onLoadingComplete(imageUri, view, loadedImage);
                    cover_photo_iv.setImageBitmap(loadedImage);
                }
            });*/
//            imageLoaderNew.displayImage(myProfileModalMain.getProfile().getCoverPic(), cover_photo_iv, optionsCoverImgImg);


            if (myProfileModalMain.getPosts().size() > 0) {

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

                Logger.logsInfo(MyProfileScreen.class.getSimpleName(), "page_no : " + page_no);
                mDataListWhole.add(page_no - 1, myProfileModalMain.getPosts());

                for (int i = 0; i < mDataListWhole.get(page_no - 1).size(); i++) {

                    mDataList.add(mDataListWhole.get(page_no - 1).get(i));
                    mDataListBackup.add(mDataListWhole.get(page_no - 1).get(i));

                }

                if (mDataList.size() > 0) {

                    if (page_no == 1) {

                        adapter = new GridViewAdapter(getActivity(), mDataList, true);
                        miniViewAdapter = new MiniRecyclerAdapterSingle(miniView_list, getActivity(), mDataList, null, null, false);
                        my_profile_gv.setAdapter(adapter);
                        main_view_list.setAdapter(adapterRecyclerView);


                        miniView_list.setAdapter(miniViewAdapter);

                        SetListViewHeightMiniView mSetListViewHeightMiniView = new SetListViewHeightMiniView();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            mSetListViewHeightMiniView.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        } else {
                            mSetListViewHeightMiniView.execute();
                        }
//                        Constant.setListViewHeightBasedOnChildren(miniView_list);
                    } else {
                        adapter.notifyDataSetChanged();
                        adapterRecyclerView.notifyDataSetChanged();
                    }
                }
            }

        }


    }

    public static final int storage_per = 2;
    /*Permissions Marsh Mallow*/
    public static String[] permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permission) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            MainTabActivity.camera = result == 0;
            Logger.logsInfo("Main Activity ", "Value of Result : " + result);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);


            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.
                    toArray(new String[listPermissionsNeeded.size()]), storage_per);
            return false;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onDetach() {
        super.onDetach();


    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.profile_screen_header, container, false);
        ButterKnife.bind(this, view);
        header_main_ll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x1 = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = event.getX();
                        float deltaX = x2 - x1;
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            // Left to Right swipe action
                            if (x2 > x1) {
//                                Constant.showToast("left2right swipe", getActivity());
                                if (this_is_other_user) {
                                    MainTabActivity.backbutton();
                                }

                            }

                            // Right to left swipe action
                            else {
//                                Constant.showToast("Right 2 Left swipe", PostDetailsScreen.this);
                            }
                        } else {
                            // consider as something else - a screen tap for example
                        }
                        break;
                }
                return true;
            }
        });


        initView();
        onClickListener();
        initCache();
        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);
        ErrorReporter.getInstance().Init(getActivity());
        try {
            if (snappydb.exists(Constant.PROFILEVIEW_TYPE)) {
                Logger.logsInfo(TAG, "snappydb.get(Constant.VIEW_TYPE)" + snappydb.get(Constant.PROFILEVIEW_TYPE));
                if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MINIVIEW_TYPE)) {
                    main_view_list.setVisibility(View.GONE);
                    miniView_list.setVisibility(View.VISIBLE);
                    my_profile_gv.setVisibility(View.GONE);
                    change_view_iv.setImageResource(R.drawable.three_view_icon);
                    isMiniView = true;

                } else if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.GRIDVIEW_TYPE)) {
                    main_view_list.setVisibility(View.GONE);
                    miniView_list.setVisibility(View.GONE);
                    my_profile_gv.setVisibility(View.VISIBLE);
                    change_view_iv.setImageResource(R.drawable.menu_icon_);
                    isGridView = true;

                } else if (snappydb.get(Constant.PROFILEVIEW_TYPE).equalsIgnoreCase(Constant.MAINVIEW_TYPE)) {
                    main_view_list.setVisibility(View.VISIBLE);
                    miniView_list.setVisibility(View.GONE);
                    my_profile_gv.setVisibility(View.GONE);
                    change_view_iv.setImageResource(R.drawable.image_icon);
                    isMainView = true;
                }

            } else {
                isMainView = false;
                isMiniView = false;
                isGridView = true;
            }
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
        if (miniViewAdapter != null) {
            miniViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore() {
                   /* if (isGVDataLoaded) {
                          *//*Api Calling*//*
                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                            try {
                                page_no = page_no + 1;
                                int api_user_id = 0;
                                if (MainTabActivity.other_user_id > 0) {
                                    api_user_id = MainTabActivity.other_user_id;
                                } else {
                                    api_user_id = user_id;
                                }
                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("user_id", api_user_id + "")
                                        .appendQueryParameter("post_type", post_content_type + "")
                                        .appendQueryParameter("page_no", page_no + "");

                                controller.callMyProfileWS(builder, false);
                                isGVDataLoaded = false;
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } else {
                *//*No Internet screen appears*//*
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                        }
                    }*/


                }
            });
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        Toro.register(main_view_list);
        if (adapterRecyclerView != null) {
            adapterRecyclerView.notifyDataSetChanged();
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        Toro.unregister(main_view_list);
        if (this.isRemoving()) {
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().remove(SettingsScreen.newInstance(null, new CallBackReplaceStyleTheme() {
                @Override
                public void CallBackReplaceStyleTheme(int mThemeID, boolean b) {
                    Logger.logsError(TAG, "mThemeId : " + mThemeID);
                    mCallBackReplaceStyleTheme.CallBackReplaceStyleTheme(mThemeID, true);

                }
            })).commitAllowingStateLoss();
        }
    }

    /* public Fragment getVisibleFragment(){
         FragmentManager fragmentManager = MyProfileScreen.this.getFragmentManager();
         List<Fragment> fragments = fragmentManager.getFragments();
         if(fragments != null){
             for(Fragment fragment : fragments){
                 if(fragment != null && fragment.isVisible())
                     fragment.isRemoving();
                 return fragment;
             }
         }
         return null;
     }*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap;
                Uri selectedImage;
                Cursor cursor;
                File root;
                int columnIndex;
                Intent mIntentToFilter;
                switch (requestCode) {
                    case RESULT_LOAD_IMG:

                        // Get the Image from data

                        selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        // Get the cursor
                        cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        fileToUploadPath = cursor.getString(columnIndex);
                        Logger.logsInfo("Gallery : ", "File Path of Img From Gallery : " + fileToUploadPath);


                        String filenameArray[] = fileToUploadPath.split("\\.");
                        String extension = filenameArray[filenameArray.length - 1];
                        Logger.logsInfo("Gallery : ", "extension From Gallery : " + extension);
                        if (extension.equalsIgnoreCase("jpg")) {
                            root = new File(Environment
                                    .getExternalStorageDirectory()
                                    + File.separator + "HereLoImages" + File.separator);
                            root.mkdirs();
                            File outPutFile = new File(root, "HereLoProfilePicture_" + System.currentTimeMillis() + ".png");

                            File file = new File(fileToUploadPath);
                            try {
                                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
                                FileOutputStream fos = new FileOutputStream(outPutFile);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            if (outPutFile.exists()) {
                                fileToUploadPath = outPutFile.getAbsolutePath();
                            }

                            if (!fileToUploadPath.contains("file://")) {

                                // imageLoaderNew.displayImage("file://" + fileToUploadPath, profile_img, optionsProfileImg);
                            } else {
                                imageLoaderNew.displayImage(fileToUploadPath, profile_img, optionsProfileImg);
                            }

                            mIntentToFilter = new Intent(getActivity(), ImageFilterScreen.class);
                            mIntentToFilter.putExtra(Constant.IMGPATH, fileToUploadPath);
                            this.startActivityForResult(mIntentToFilter, FILTERIMGCODE);


                        } else if (extension.equalsIgnoreCase("gif")) {
                            Glide
                                    .with(getActivity())
                                    .asGif()
                                    .apply(RequestOptions.circleCropTransform())
                                    .load("file://" + fileToUploadPath)
                                    .into(profile_img);
                            uploadProfileImageToAWS();
                        }

//                fileToUploadPath = fileToUploadPath.replace("file://", "");

                        break;
                    case RESULT_LOAD_IMG_COVER:

                        // Get the Image from data

                        selectedImage = data.getData();
                        String[] filePathColumn_cover = {MediaStore.Images.Media.DATA};

                        // Get the cursor
                        cursor = getActivity().getContentResolver().query(selectedImage,
                                filePathColumn_cover, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        columnIndex = cursor.getColumnIndex(filePathColumn_cover[0]);
                        fileToUploadPath = cursor.getString(columnIndex);
                        Logger.logsInfo(TAG, "File Path of Img From Gallery : " + fileToUploadPath);

//                fileToUploadPath = fileToUploadPath.replace("file://", "");
                        root = new File(Environment
                                .getExternalStorageDirectory()
                                + File.separator + "HereLoImages" + File.separator);
                        root.mkdirs();

                        //  imageLoaderNew.displayImage("file://" + fileToUploadPath, cover_photo_iv, optionsCoverImgImg);
                        String filenamecover[] = fileToUploadPath.split("\\.");
                        String extensioncover = filenamecover[filenamecover.length - 1];

                        if (extensioncover.equalsIgnoreCase("jpg")) {
                            File outPutFile_cover = new File(root, "HereLoProfilePicture_" + System.currentTimeMillis() + ".png");
                            File file_cover = new File(fileToUploadPath);
                            try {
                                bitmap = BitmapFactory.decodeStream(new FileInputStream(file_cover));
                                FileOutputStream fos = new FileOutputStream(outPutFile_cover);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            if (outPutFile_cover.exists()) {
                                fileToUploadPath = outPutFile_cover.getAbsolutePath();
                            }

                            if (!fileToUploadPath.contains("file://")) {
                                imageLoaderNew.displayImage("file://" + fileToUploadPath, cover_photo_iv, optionsCoverImgImg);
                            } else {
                                imageLoaderNew.displayImage(fileToUploadPath, cover_photo_iv, optionsCoverImgImg);
                            }

                            mIntentToFilter = new Intent(getActivity(), ImageFilterScreen.class);
                            mIntentToFilter.putExtra(Constant.IMGPATH, fileToUploadPath);
                            this.startActivityForResult(mIntentToFilter, FILTERIMGCODE_COVER);

                        } else if (extensioncover.equalsIgnoreCase("gif")) {
                            Glide
                                    .with(getActivity())
                                    .load("file://" + fileToUploadPath)
                                    .into(cover_photo_iv);
                            uploadCoverImageToAWS();
                        }


                        break;
                    case TAKE_PHOTO_CODE:

                        fileToUploadPath = outputFileUri.toString();

                        fileToUploadPath = fileToUploadPath.replace("file://", "");
                        root = new File(fileToUploadPath);
                        try {
                            bitmap = BitmapFactory.decodeStream(new FileInputStream(root));
                            FileOutputStream fos = new FileOutputStream(fileToUploadPath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }


//                        "file://" + /storage/emulated/0/Rotate/11242015174423.jpg
                        Logger.logsInfo(TAG, "File Path of Img From Camera : " + fileToUploadPath);
                        imageLoaderNew.displayImage("file://" + fileToUploadPath, profile_img, optionsProfileImg);

                        mIntentToFilter = new Intent(getActivity(), ImageFilterScreen.class);
                        mIntentToFilter.putExtra(Constant.IMGPATH, fileToUploadPath);
                        this.startActivityForResult(mIntentToFilter, FILTERIMGCODE);
                        break;
                    case TAKE_PHOTO_CODE_COVER:

                        fileToUploadPath = outputFileUri.toString();

                        fileToUploadPath = fileToUploadPath.replace("file://", "");
                        root = new File(fileToUploadPath);
                        try {
                            bitmap = BitmapFactory.decodeStream(new FileInputStream(root));
                            FileOutputStream fos = new FileOutputStream(fileToUploadPath);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);

                            fos.flush();
                            fos.close();
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }


//                        "file://" + /storage/emulated/0/Rotate/11242015174423.jpg
                        Logger.logsInfo(TAG, "File Path of Img From Camera : " + fileToUploadPath);


                        imageLoaderNew.displayImage("file://" + fileToUploadPath, cover_photo_iv, optionsCoverImgImg);

                        mIntentToFilter = new Intent(getActivity(), ImageFilterScreen.class);
                        mIntentToFilter.putExtra(Constant.IMGPATH, fileToUploadPath);
                        this.startActivityForResult(mIntentToFilter, FILTERIMGCODE_COVER);
                        break;
                    case FILTERIMGCODE:

                        fileToUploadPath = data.getStringExtra("result");

                        if (!fileToUploadPath.contains("file://")) {
                            imageLoaderNew.displayImage("file://" + fileToUploadPath, profile_img, optionsProfileImg);
                        } else {
                            imageLoaderNew.displayImage(fileToUploadPath, profile_img, optionsProfileImg);
                        }

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                            if (!fileToUploadPath.equalsIgnoreCase("") && fileToUploadPath != null) {
                                uploadProfileImageToAWS();
                            }
                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }
                        break;
                    case FILTERIMGCODE_COVER:

                        fileToUploadPath = data.getStringExtra(Constant.result);

                        if (!fileToUploadPath.contains("file://")) {
                            imageLoaderNew.displayImage("file://" + fileToUploadPath, cover_photo_iv, optionsCoverImgImg);
                        } else {
                            imageLoaderNew.displayImage(fileToUploadPath, cover_photo_iv, optionsCoverImgImg);
                        }

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                            if (!fileToUploadPath.equalsIgnoreCase("") && fileToUploadPath != null) {
                                uploadCoverImageToAWS();
                            }
                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }
                        break;
                    default:
                        break;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();


            Constant.showToast("Something went wrong", getActivity());
        }
    }

    private void uploadCoverImageToAWS() {


        AsyncTask<String, String, String> _Task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {

                Constant.showProgressDialog(getActivity());
            }


            @SuppressWarnings("deprecation")
            @Override
            protected String doInBackground(String... arg0) {

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    try {
                        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(Constant.AWS_KEY, Constant.AWS_SECRET));

                        File mFile = new File(fileToUploadPath);
                        Logger.logsInfo(TAG, " AWS File fileToUploadPath : " + fileToUploadPath);
                        String mKey = "";
                        if (mFile.exists()) {

                            String filenamecover[] = fileToUploadPath.split("\\.");
                            String extensioncover = filenamecover[filenamecover.length - 1];

                            if (extensioncover.equalsIgnoreCase("png")) {
                                mKey = "CoverImg_" + UUID.randomUUID() + ".png";
                            } else if (extensioncover.equalsIgnoreCase("gif")) {
                                mKey = "CoverImg_" + UUID.randomUUID() + ".gif";
                            }

                            PutObjectRequest por = new PutObjectRequest(Constant.BUCKET,
                                    mKey, mFile);//key is  URL
                            //making the object Public
                            por.setCannedAcl(CannedAccessControlList.PublicRead);
                            s3.putObject(por);

                            image = /*"https://" + Constant.BUCKET + ".s3.amazonaws.com/" +*/ mKey;
                            Logger.logsInfo(TAG, " AWS File URL : " + image);

                        }

                    } catch (Exception e) {
                        // writing error to Log
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {


                            }
                        });

                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                        }
                    });

                }


                return null;

            }

            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(String result) {
                Constant.cancelDialog();


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("url", image);

                mUpdateCoverController.callWS(builder);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            _Task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String[]) null);
        } else {
            _Task.execute((String[]) null);
        }


    }

    private void uploadProfileImageToAWS() {


        AsyncTask<String, String, String> _Task = new AsyncTask<String, String, String>() {

            @Override
            protected void onPreExecute() {

                Constant.showProgressDialog(getActivity());
            }


            @SuppressWarnings("deprecation")
            @Override
            protected String doInBackground(String... arg0) {

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    try {
                        AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(Constant.AWS_KEY, Constant.AWS_SECRET));

                        File mFile = new File(fileToUploadPath);
                        if (mFile.exists()) {
                            String mKey = "";

                            String filenamecover[] = fileToUploadPath.split("\\.");
                            String extensioncover = filenamecover[filenamecover.length - 1];

                            if (extensioncover.equalsIgnoreCase("png")) {
                                mKey = "UserImg_" + UUID.randomUUID() + ".png";
                            } else if (extensioncover.equalsIgnoreCase("gif")) {
                                mKey = "UserImg_" + UUID.randomUUID() + ".gif";
                            }

                            PutObjectRequest por = new PutObjectRequest(Constant.BUCKET,
                                    mKey, mFile);//key is  URL
                            //making the object Public
                            por.setCannedAcl(CannedAccessControlList.PublicRead);
                            s3.putObject(por);

                            image = mKey;
                            Logger.logsInfo("Update Profile : ", " AWS File URL : " + image);

                        }

                    } catch (Exception e) {
                        // writing error to Log
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {


                            }
                        });

                    }


                } else {
                    getActivity().runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub

                        }
                    });

                }


                return null;

            }

            @Override
            protected void onProgressUpdate(String... values) {
                // TODO Auto-generated method stub
                super.onProgressUpdate(values);

            }

            @Override
            protected void onPostExecute(String result) {
                Constant.cancelDialog();

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("image", image);

                mUpdateProfileController.callUpdateProfileWS(builder);

            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            _Task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (String[]) null);
        } else {
            _Task.execute((String[]) null);
        }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Logger.logsInfo("MyProfileScreen : ", "=====onActivityCreated Called");
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();

        }
        detector = new SimpleGestureFilter(getActivity(), this);
        sPref = getActivity().getSharedPreferences(Constant.SPREF, Context.MODE_PRIVATE);
        editor = sPref.edit();

        user_id = sPref.getInt(Constant.USER_ID_SPREF, 0);
        username = sPref.getString(Constant.USER_NAME_SPREF, "");

        if (MainTabActivity.other_user_id > 0) {
            user_id = MainTabActivity.other_user_id;
            if (user_id == sPref.getInt(Constant.USER_ID_SPREF, 0)) {
                this_is_other_user = false;
            } else {
                this_is_other_user = true;
            }
        } /*else {

            this_is_other_user = false;
        }*/


        if (getArguments() != null) {

            if (getArguments().containsKey(Constant.fromActivity)) {
                fromactivity = getArguments().getBoolean(Constant.fromActivity, false);
            }
            if (getArguments().containsKey(Constant.other_user_id)) {
                user_id = getArguments().getInt(Constant.other_user_id, 0);
                if (user_id == sPref.getInt(Constant.USER_ID_SPREF, 0)) {
                    this_is_other_user = false;
                } else {
                    this_is_other_user = true;
                }

            } /*else {
                this_is_other_user = false;
            }*/
        } /*else {
            this_is_other_user = false;
        }*/

        if (this_is_other_user) {


//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

            options_ll.setWeightSum(4);
            edit_iv.setVisibility(View.GONE);
            check_in_iv.setVisibility(View.GONE);
            activity_ll.setVisibility(View.VISIBLE);

            change_chat_status_iv.setVisibility(View.INVISIBLE);
            chat_on_off_ll.setVisibility(View.INVISIBLE);
            _left1IV.setImageResource(ThemeManager.Current().headerLeftArrowIcon());
            second_postion_iv.setImageResource(R.drawable.some_one_else_chat_icon);


        } else {
            options_ll.setWeightSum(4);
            edit_iv.setVisibility(View.VISIBLE);
            check_in_iv.setVisibility(View.VISIBLE);
            activity_ll.setVisibility(View.VISIBLE);
            change_chat_status_iv.setVisibility(View.VISIBLE);
            chat_on_off_ll.setVisibility(View.VISIBLE);
            _left1IV.setImageResource(ThemeManager.Current().headerLeftSettingIcon());
            second_postion_iv.setImageResource(R.drawable.news_icon);
        }

        callGetUserProfile(true);


    }

    private void callGetUserProfile(boolean isProgressBar) {
          /*Api Calling*/
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

            try {

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("post_type", post_content_type + "")
                        .appendQueryParameter("page_no", page_no + "");

                controller.callMyProfileWS(builder, isProgressBar);

            } catch (Exception e) {

            }

        } else {
                /*No Internet screen appears*/
            Constant.showToast(getActivity().getString(R.string.internet), getActivity());

        }
    }

    private void initView() {
        swipe_screen.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                Logger.logsError(TAG,"onRefresh Called ");
                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    callGetUserProfile(false);


                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }
            }
        });

      /*  miniView_list.setXListViewListener(this);
        miniView_list.setPullRefreshEnable(true);*/


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
//        main_view_list.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        main_view_list.setLayoutManager(linearLayoutManager);
        main_view_list.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager linearLayoutManagerMini = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        miniView_list.setLayoutManager(linearLayoutManagerMini);
        miniView_list.setItemAnimator(new DefaultItemAnimator());

        _shareLL = (LinearLayout) view.findViewById(R.id.shareLL);
        if (adapterRecyclerView == null) {
            adapterRecyclerView = new HomeMainAdapter(main_view_list, getActivity(), null,
                    MyProfileScreen.this, null, null, null, null, false, mDataList);
//            adapterRecyclerView = new MainViewAdapterRecyclerView(main_view_list);
        }

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        imageLoaderNew = ImageLoader.getInstance();
        optionsProfileImg = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_icon) //
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)

                .build();
        optionsCoverImgImg = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.post_img_default) //
                .showImageForEmptyUri(R.drawable.post_img_default)
                .showImageOnFail(R.drawable.post_img_default)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)

                .build();

//        View headerView = ((LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.profile_screen_header, null, false);

        tf = Typeface.createFromAsset(getActivity().getAssets(), "Helvetica-Normal.ttf");

        _left1IV = (ImageView) view.findViewById(R.id.back_btn);


        _bottomRelativeLayout = (RelativeLayout) view.findViewById(bottomRelay);
        _bottomRelativeLayout.setVisibility(View.GONE);

        _cancelButton = (TextView) view.findViewById(R.id.cancelB);
        _cancelButton.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        _cancelButton.setOnClickListener(this);
        _takeNewButton = (TextView) view.findViewById(R.id.takeB);
        _takeNewButton.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        _galleryButton = (TextView) view.findViewById(R.id.galleryB);
        _galleryButton.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        _defaultButton = (TextView) view.findViewById(R.id.defaultB);
        _defaultButton.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        _left1IV.setVisibility(View.VISIBLE);
        _left1IV.setOnClickListener(this);


        _takeNewButton.setOnClickListener(this);
        _galleryButton.setOnClickListener(this);
        _defaultButton.setOnClickListener(this);
        check_in_iv.setOnClickListener(this);
        cover_photo_iv.setOnClickListener(this);


        header_layout = (LinearLayout) view.findViewById(R.id.header_layout);

        _cancelIV = (TextView) view.findViewById(R.id.cancelIV);
        _cancelIV.setOnClickListener(this);

        _next1IV = (ImageView) view.findViewById(R.id.next_btn);
        _next1IV.setVisibility(View.VISIBLE);
        _next1IV.setOnClickListener(this);

        _next2IV = (ImageView) view.findViewById(R.id.next_btn2);
        _headerIV = (ImageView) view.findViewById(R.id.headerIV);
        profile_img = (CircularImageView) view.findViewById(R.id.profile_img);
        _next2IV.setOnClickListener(this);
        profile_img.setOnClickListener(this);
        _next2IV.setVisibility(View.VISIBLE);
        _headerIV.setVisibility(View.GONE);
        setHeaderRightIcon();


        _headerTV = (TextView) view.findViewById(R.id.header_tv);
        _headerTV.setText(getActivity().getResources().getString(R.string.profileText));
        Logger.logsInfo("My Profile Screen", "Header color : " + ThemeManager.Current().headerTextColor());
        _headerTV.setTextColor(ThemeManager.Current().headerTextColor());
        _headerTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        _left1IV.setImageResource(ThemeManager.Current().headerLeftSettingIcon());

        friends_num_tv = (TextView) view.findViewById(R.id.friends_num_tv);

        fName_tv = (TextView) view.findViewById(R.id.fName_tv);
        LName_tv = (TextView) view.findViewById(R.id.LName_tv);
        description_tv = (TextView) view.findViewById(R.id.description_tv);
        status_tv = (TextView) view.findViewById(R.id.status_tv);
        dob_tv = (TextView) view.findViewById(R.id.dob_tv);
        HighSchool_tv = (TextView) view.findViewById(R.id.HighSchool_tv);
        movieGenre_tv = (TextView) view.findViewById(R.id.movieGenre_tv);
        musicGenreTv = (TextView) view.findViewById(R.id.musicGenreTv);
        placeOfBirth_tv = (TextView) view.findViewById(R.id.placeOfBirth_tv);
        clg_tv = (TextView) view.findViewById(R.id.clg_tv);
        movie_tv = (TextView) view.findViewById(R.id.movie_tv);
        tv_show_tv = (TextView) view.findViewById(R.id.tv_show_tv);
        band_tv = (TextView) view.findViewById(R.id.band_tv);
        sport_team_tv = (TextView) view.findViewById(R.id.sport_team_tv);
        sport_tv = (TextView) view.findViewById(R.id.sport_tv);

        friends_tv = (TextView) view.findViewById(R.id.friends_tv);

        following_num_tv = (TextView) view.findViewById(R.id.following_num_tv);
        following_tv = (TextView) view.findViewById(R.id.following_tv);

        followers_num_tv = (TextView) view.findViewById(R.id.followers_num_tv);
        followers_tv = (TextView) view.findViewById(R.id.followers_tv);


        HerelosTV = (TextView) view.findViewById(R.id.HerelosTV);
        likesTV = (TextView) view.findViewById(R.id.likesTV);
        likedmineTV = (TextView) view.findViewById(R.id.likedmineTV);
        chat_status_tv = (TextView) view.findViewById(R.id.chat_status_tv);
        change_chat_status_iv = (ImageView) view.findViewById(R.id.change_chat_status_iv);
        change_chat_status_iv.setOnClickListener(this);
        info_status_tv = (TextView) view.findViewById(R.id.info_status_tv);


        followers_num_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        friends_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        followers_num_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        followers_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        following_num_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        following_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        info_status_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        HerelosTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        likesTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        likedmineTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));


        edit_iv = (ImageView) view.findViewById(R.id.edit_iv);

        my_profile_gv = (MyGridViewPost) view.findViewById(R.id.profile_grid);
//        my_profile_gv.setExpanded(true);
   /*Scroll Events*/
        my_profile_gv.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        my_profile_gv.setOnScrollListener(new AbsListView.OnScrollListener() {
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

                /*if (_gridView.getChildAt(0) != null) {
                    swipe_ref_layout.setEnabled(_gridView.getFirstVisiblePosition() == 0 && _gridView.getChildAt(0).getTop() == 0);
                }*/
                currentFirstVisibleItem = firstVisibleItem;
                currentVisibleItemCount = visibleItemCount;
                mtotalItemCount = totalItemCount;


            }

            private void isScrollCompleted() {
                if (currentVisibleItemCount > 0 && currentScrollState == SCROLL_STATE_IDLE &&
                        mtotalItemCount == (currentFirstVisibleItem + currentVisibleItemCount)) {
                    /*** In this way I detect if there's been a scroll which has completed ***/
                    /*** do the work for load more date! ***/
                    Logger.logsInfo(MyProfileScreen.class.getSimpleName(), "Load More Called");
                    Logger.logsInfo(MyProfileScreen.class.getSimpleName(), "is_more : " + is_more);

                    if (isGVDataLoaded) {
                          /*Api Calling*/
                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                            try {
                                page_no = page_no + 1;
                                int api_user_id = 0;
                                if (MainTabActivity.other_user_id > 0) {
                                    api_user_id = MainTabActivity.other_user_id;
                                } else {
                                    api_user_id = user_id;
                                }
                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("user_id", api_user_id + "")
                                        .appendQueryParameter("post_type", post_content_type + "")
                                        .appendQueryParameter("page_no", page_no + "");

                                controller.callMyProfileWS(builder, false);
                                isGVDataLoaded = false;
                            } catch (Exception e) {
                                e.printStackTrace();

                            }

                        } else {
                /*No Internet screen appears*/
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                        }
                    }


                }
            }
        });
//        my_profile_gv.add(headerView);

        followers_ll = (LinearLayout) view.findViewById(R.id.followers_ll);
        following_ll = (LinearLayout) view.findViewById(R.id.following_ll);
        friends_ll = (LinearLayout) view.findViewById(R.id.friends_ll);
        change_view_ll = (LinearLayout) view.findViewById(R.id.change_view_ll);
        change_view_iv = (ImageView) view.findViewById(R.id.change_view_iv);
        change_view_ll.setOnClickListener(this);


        _hereloLL = (LinearLayout) view.findViewById(R.id.HereloSLL);
        _likedLL = (LinearLayout) view.findViewById(R.id.likedLL);
        _likeMineLL = (LinearLayout) view.findViewById(R.id.likeMineLL);
        _photoLL = (LinearLayout) view.findViewById(R.id.photoLL);
        map_ll = (LinearLayout) view.findViewById(R.id.map_ll);

        share_ll = (LinearLayout) view.findViewById(R.id.share_ll);

        activity_ll = (LinearLayout) view.findViewById(R.id.activity_ll);

        number_share_tv = (TextView) view.findViewById(R.id.number_share_tv);
        share_tv = (TextView) view.findViewById(R.id.share_tv);

        number_share_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
        share_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

        user_name_tv = (TextView) view.findViewById(R.id.user_name_tv);
        user_name_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Md.otf"));

        String tempString = "More Info";
        moreTextView = (TextView) view.findViewById(R.id.moreInfoTV);
   /*     SpannableString spanString = new SpannableString(tempString);
        spanString.setSpan(new UnderlineSpan(), 1, spanString.length() - 1, 0);
        moreTextView.setText(spanString);*/

        _infoDetailLinearLayout = (LinearLayout) view.findViewById(R.id.moreInfoDetailLL);
        _infoLinearLayout = (LinearLayout) view.findViewById(R.id.moreInfoLL);

        _view1 = view.findViewById(R.id.view1);


//        Helper.getListViewSize(my_profile_gv);

/*
        my_profile_gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ((BaseContainerFragment) getParentFragment()).replaceFragment(new DemoClass(), true);

            }
        });
*/


    }


    private void onClickListener() {

        followers_ll.setOnClickListener(this);
        following_ll.setOnClickListener(this);
        friends_ll.setOnClickListener(this);
        edit_iv.setOnClickListener(this);
        _hereloLL.setOnClickListener(this);
        _likedLL.setOnClickListener(this);
        _likeMineLL.setOnClickListener(this);
        _photoLL.setOnClickListener(this);
        map_ll.setOnClickListener(this);
        activity_ll.setOnClickListener(this);
        share_ll.setOnClickListener(this);
        _infoLinearLayout.setOnClickListener(this);

    }

    private DB snappydb;

    private void initCache() {
        try {
            snappydb = DBFactory.open(getActivity(), getActivity().getResources().getString(R.string.hereloText));
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    boolean isChatOn = false;
    boolean isCoverSelected = false;

    private void setHeaderRightIcon() {


        if (isImagesVisible) {
            _next1IV.setImageResource(ThemeManager.Current().headerImageSelectIcon());
        } else {
            _next1IV.setImageResource(ThemeManager.Current().headerImageunselectIcon());
        }
        if (isVideosVisible) {

            _next2IV.setImageResource(ThemeManager.Current().headerVideoSelectIcon());
        } else {
            _next2IV.setImageResource(ThemeManager.Current().headerVideoUnselectIcon2());
        }
    }

    @Override
    public void onClick(View view) {

        FriendScreen friendScreen = new FriendScreen();
        Bundle bundle = new Bundle();

        switch (view.getId()) {

            case R.id.second_postion_iv:


                break;
            case R.id.next_btn://Image
                page_no = 1;

                if (!isImagesVisible) {


                    //                Hide All Images from the Post list
                    /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {
                            post_content_type = "1";
                            isImagesVisible = true;
                            isVideosVisible = false;
                            setHeaderRightIcon();

                            callGetUserProfile(true);
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
                            post_content_type = "0";
                            isImagesVisible = false;
                            setHeaderRightIcon();

                            callGetUserProfile(true);
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

                if (!isVideosVisible) {

//                    *//*Hide All Videos from the Post list*//*
                       /*Api Calling*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                        try {
                            post_content_type = "2";
                            isVideosVisible = true;
                            isImagesVisible = false;

                            setHeaderRightIcon();

                            callGetUserProfile(true);
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
                            post_content_type = "0";
                            isVideosVisible = false;

                            setHeaderRightIcon();

                            callGetUserProfile(true);
                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    } else {
                /*No Internet screen appears*/
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());

                    }
                }

                break;
            case R.id.cover_photo_iv:
                if (!this_is_other_user) {

                    Constant.hideKeyBoard(getActivity());
                    if (_bottomRelativeLayout.getVisibility() != View.VISIBLE) {
                        animateUpProfileImg();
                        isCoverSelected = true;
                    } else {
                        isCoverSelected = false;
                        animateDown();
                        _bottomRelativeLayout.clearAnimation();
                    }
                }
                break;
            case R.id.check_in_iv:

                Bundle mBundle = new Bundle();
                mBundle.putBoolean(Constant.FROM_PROFILE_CHECKIN, true);
                EditReHereLo mEditRehereLo = new EditReHereLo();
                mEditRehereLo.setArguments(mBundle);
                ((BaseContainerFragment) getParentFragment()).replaceFragment(mEditRehereLo, true);

                break;
            case R.id.change_view_ll:

                /*ChangeView*/
                changeView();

                break;

            case R.id.change_chat_status_iv:
                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    updateChatStatus();
                } else {
                    Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                }


                break;

            case R.id.cancelIV:
                animateDown();
                break;

            case R.id.back_btn:
                if (this_is_other_user) {
                    MainTabActivity.backbutton();
                } else {
                    if (fromactivity) {
                        MainTabActivity.backbutton();
                    } else {
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(SettingsScreen.newInstance(null, new CallBackReplaceStyleTheme() {
                            @Override
                            public void CallBackReplaceStyleTheme(int mThemeID, boolean b) {
                                mCallBackReplaceStyleTheme.CallBackReplaceStyleTheme(mThemeID, true);
                            }
                        }), true);
                    }
                }


                break;

            case R.id.edit_iv:

                ((BaseContainerFragment) getParentFragment()).replaceFragment(new ProfileFragment(), true);

                break;

            case R.id.followers_ll:

                bundle.putString("Screen", "Followers");
                bundle.putInt(Constant.USER_ID, user_id);
                friendScreen.setArguments(bundle);
                ((BaseContainerFragment) getParentFragment()).replaceFragment(friendScreen, true);

                break;

            case R.id.following_ll:

                bundle.putString("Screen", "Following");
                bundle.putInt(Constant.USER_ID, user_id);
                friendScreen.setArguments(bundle);
                ((BaseContainerFragment) getParentFragment()).replaceFragment(friendScreen, true);

                break;

            case R.id.friends_ll:

                bundle.putString("Screen", "Friends");
                bundle.putInt(Constant.USER_ID, user_id);
                friendScreen.setArguments(bundle);
                ((BaseContainerFragment) getParentFragment()).replaceFragment(friendScreen, true);

                break;

            case R.id.HereloSLL:

                bundle.putInt("Screen", 1);
                bundle.putInt(Constant.USER_ID, user_id);
                bundle.putBoolean(Constant.FROM_MYPROFILE_REHERELO, true);
                if (isGridView) {
                    HereLoFragmentGridview hereLoFragmentGridview = new HereLoFragmentGridview();
                    hereLoFragmentGridview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentGridview, true);
                } else if (isMiniView) {
                    HereLoFragmentMiniview hereLoFragmentMiniview = new HereLoFragmentMiniview();
                    hereLoFragmentMiniview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMiniview, true);
                } else if (isMainView) {
                    HereLoFragmentMainview hereLoFragmentMainview = new HereLoFragmentMainview();
                    hereLoFragmentMainview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMainview, true);
                }


                break;

            case R.id.likedLL:

                bundle.putInt("Screen", 2);
                bundle.putInt(Constant.USER_ID, user_id);
                if (isGridView) {
                    HereLoFragmentGridview hereLoFragmentGridview = new HereLoFragmentGridview();
                    hereLoFragmentGridview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentGridview, true);
                } else if (isMiniView) {
                    HereLoFragmentMiniview hereLoFragmentMiniview = new HereLoFragmentMiniview();
                    hereLoFragmentMiniview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMiniview, true);
                } else if (isMainView) {
                    HereLoFragmentMainview hereLoFragmentMainview = new HereLoFragmentMainview();
                    hereLoFragmentMainview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMainview, true);
                }

                break;

            case R.id.likeMineLL:

                bundle.putInt("Screen", 3);
                bundle.putInt(Constant.USER_ID, user_id);
                if (isGridView) {
                    HereLoFragmentGridview hereLoFragmentGridview = new HereLoFragmentGridview();
                    hereLoFragmentGridview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentGridview, true);
                } else if (isMiniView) {
                    HereLoFragmentMiniview hereLoFragmentMiniview = new HereLoFragmentMiniview();
                    hereLoFragmentMiniview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMiniview, true);
                } else if (isMainView) {
                    HereLoFragmentMainview hereLoFragmentMainview = new HereLoFragmentMainview();
                    hereLoFragmentMainview.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentMainview, true);
                }

                break;

            case R.id.photoLL:

                bundle.putInt("Screen", 4);
                bundle.putInt(Constant.USER_ID, user_id);
                bundle.putBoolean(Constant.FROM_MYPROFILE, true);
                HereLoFragmentGridview hereLoFragmentGridview = new HereLoFragmentGridview();
                hereLoFragmentGridview.setArguments(bundle);
                ((BaseContainerFragment) getParentFragment()).replaceFragment(hereLoFragmentGridview, true);


                break;

            case R.id.map_ll:

                Intent mIntent = new Intent(getActivity(), ClusteringMapActivity.class);
                mIntent.putExtra(Constant.USER_ID, user_id);
                startActivity(mIntent);
//                ((BaseContainerFragment) getParentFragment()).replaceFragment(new MapScreen(), true);

                break;

            case R.id.activity_ll:
                if (this_is_other_user) {
//                    Constant.showToast("Chat Message",getActivity());
                    Intent mChatDetailsIntent = new Intent(getActivity(), ChatDetailsScreen.class);
                    mChatDetailsIntent.putExtra(Constant.other_user_name, myProfileModalMain.getProfile().getUsername());
                    mChatDetailsIntent.putExtra(Constant.USER_IMG_OTHER, myProfileModalMain.getProfile().getImage());
                    startActivity(mChatDetailsIntent);
                } else {
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new ActivityScreen(), true);
                }


                break;

            case R.id.share_ll:

                ((BaseContainerFragment) getParentFragment()).replaceFragment(new YourHereLoScreen(), true);

                break;

            case R.id.moreInfoLL:

                if (_infoDetailLinearLayout.getVisibility() == View.VISIBLE) {
                    _view1.setVisibility(View.GONE);
                    moreTextView.setText(getActivity().getResources().getString(R.string.moreInfoText));

                    ViewAnimationUtils.collapse(_infoDetailLinearLayout);
//                    _infoDetailLinearLayout.setVisibility(View.GONE);
                } else {

                    _view1.setVisibility(View.VISIBLE);
                    moreTextView.setText(getActivity().getResources().getString(R.string.lessInfoText));

//                    _infoDetailLinearLayout.setVisibility(View.VISIBLE);
                    ViewAnimationUtils.expand(_infoDetailLinearLayout);
                }

                break;
            case R.id.profile_img:

                if (!this_is_other_user) {
                    isCoverSelected = false;
                    Constant.hideKeyBoard(getActivity());
                    if (_bottomRelativeLayout.getVisibility() != View.VISIBLE) {
                        animateUpProfileImg();
                    } else {

                        animateDown();
                        _bottomRelativeLayout.clearAnimation();
                    }
                }


                break;

            case R.id.cancelB:

                animateDown();
                _bottomRelativeLayout.clearAnimation();

                break;
            case R.id.takeB:

                animateDown();
                _bottomRelativeLayout.clearAnimation();
                File root = new File(Environment
                        .getExternalStorageDirectory()
                        + File.separator + "HereLoImages" + File.separator);
                root.mkdirs();
                sdImageMainDirectory = new File(root, "HereLoProfilePicture_" + System.currentTimeMillis() + ".png");
                outputFileUri = Uri.fromFile(sdImageMainDirectory);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                if (isCoverSelected) {
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE_COVER);
                } else {
                    startActivityForResult(cameraIntent, TAKE_PHOTO_CODE);
                }


                break;

            case R.id.galleryB:

                animateDown();
                _bottomRelativeLayout.clearAnimation();
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                if (isCoverSelected) {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG_COVER);
                } else {
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
                }


                break;

            case R.id.defaultB:

                animateDown();
                _bottomRelativeLayout.clearAnimation();

                break;

            default:

                break;

        }
    }


    private void changeView() {

        if (isMainView) {
            isMainView = false;
            isGridView = false;
            /*Mini View to show*/
            isMiniView = true;

            main_view_list.setVisibility(View.GONE);
            my_profile_gv.setVisibility(View.GONE);
            miniView_list.setVisibility(View.VISIBLE);

            SetListViewHeightMiniView mSetListViewHeightMiniView = new SetListViewHeightMiniView();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                mSetListViewHeightMiniView.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                mSetListViewHeightMiniView.execute();
            }
            try {
                snappydb.put(Constant.PROFILEVIEW_TYPE, Constant.MINIVIEW_TYPE);
            } catch (SnappydbException e) {
                e.printStackTrace();
            }

            change_view_iv.setImageResource(R.drawable.three_view_icon);
        } else if (isMiniView) {
            isMiniView = false;
            isMainView = false;
            /*Grid View to show*/
            main_view_list.setVisibility(View.GONE);
            miniView_list.setVisibility(View.GONE);
            my_profile_gv.setVisibility(View.VISIBLE);

            change_view_iv.setImageResource(R.drawable.menu_icon_);
            try {
                snappydb.put(Constant.PROFILEVIEW_TYPE, Constant.GRIDVIEW_TYPE);
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
            isGridView = true;
        } else if (isGridView) {

            isGridView = false;
            isMiniView = false;
            /*Main View to show*/
            change_view_iv.setImageResource(R.drawable.image_icon);
            isMainView = true;

            my_profile_gv.setVisibility(View.GONE);
            miniView_list.setVisibility(View.GONE);
            main_view_list.setVisibility(View.VISIBLE);
            try {
                snappydb.put(Constant.PROFILEVIEW_TYPE, Constant.MAINVIEW_TYPE);
            } catch (SnappydbException e) {
                e.printStackTrace();
            }
            if (mDataList.size() > 0) {

                if (page_no == 1) {
                    adapterRecyclerView = new HomeMainAdapter(main_view_list, getActivity(),
                            null, MyProfileScreen.this, null, null, null, null, false, mDataList);
//                    adapterRecyclerView = new MainViewAdapterRecyclerView(main_view_list);

                    main_view_list.setAdapter(adapterRecyclerView);
                } else {

                    adapterRecyclerView.notifyDataSetChanged();
                }


            }


        }

    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                str = "Swipe Up";
                break;

        }
        Constant.showToast(str, getActivity());
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public void CallBackReplaceStyleTheme(int mThemeID, boolean b) {

    }

    class SetListViewHeightMiniView extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constant.showProgressDialog(getActivity());
                }
            });


            Logger.logsError(TAG, "onPreExecute : CA;;;ed");

        }

        @Override
        protected Void doInBackground(Void... params) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Constant.setListViewHeightBasedOnChildren(miniView_list);
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Constant.cancelDialog();
                }
            });
            Logger.logsError(TAG, "onPostExecute : CA;;;ed");
        }
    }

    public void animateUpProfileImg() {
        _translateAnimation = new TranslateAnimation(0, 0, 500, 0);
        _translateAnimation.setDuration(500);
        _translateAnimation.setFillAfter(true);
        _bottomRelativeLayout.startAnimation(_translateAnimation);
        _bottomRelativeLayout.setVisibility(View.VISIBLE);
    }

    private int posToDelete = 0;

    public void animateUp(final DatumHome bean, final int position) {

        this.posToDelete = position;
        _translateAnimation = new TranslateAnimation(0, 0, 500, 0);
        _translateAnimation.setDuration(500);
        _translateAnimation.setFillAfter(true);
        _shareLL.startAnimation(_translateAnimation);
        _shareLL.setVisibility(View.VISIBLE);

      /*  if (bean.getUserId().equalsIgnoreCase(String.valueOf(""))) {
            delete_post_ll.setVisibility(View.VISIBLE);
            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateDown();
                    if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.appendQueryParameter("post_id", bean.getId() + "")
                                .appendQueryParameter("user_id", user_id + "");
                        mDeletePostController.callWS(builder, true);
                    } else {
                        Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                    }
                }
            });*/
        /*    editIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateDown();
                    if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {

                       *//* if (bean.getPostType().equalsIgnoreCase("3")) {
                            Bundle mBundle = new Bundle();
                            mBundle.putSerializable(Constant.POST_DETAILS, bean);
                            mBundle.putBoolean(Constant.FROM_EDIT_POST, true);
                            EditReHereLo editReHereLo = new EditReHereLo();

                            editReHereLo.setArguments(mBundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(editReHereLo, true);
                        } else {
                            // TODO: 4/2/17
                        }*//*
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable(Constant.POST_DETAILS, bean);
                        mBundle.putBoolean(Constant.FROM_EDIT_POST, true);
                        mBundle.putBoolean(Constant.FROM_HOME_SCREEN, true);
                        mBundle.putInt(Constant.INDEX, position);
                        EditReHereLo editReHereLo = new EditReHereLo();

                        editReHereLo.setArguments(mBundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(editReHereLo, true);


                    } else {
                        Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                    }
                }
            });

            edit_post_ll.setVisibility(View.VISIBLE);
        } else {
            delete_post_ll.setVisibility(View.GONE);
            edit_post_ll.setVisibility(View.GONE);
        }*/

        /*if (!bean.getUserId().equalsIgnoreCase(String.valueOf(user_id))) {
            report_post_ll.setVisibility(View.VISIBLE);
            reportIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    animateDown();
                    if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {
                        Uri.Builder builder = new Uri.Builder();
                        builder.appendQueryParameter("post_id", bean.getId() + "")
                                .appendQueryParameter("user_id", user_id + "");
                        mReportPostController.callWS(builder, true);
                    } else {
                        Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                    }
                }
            });
        } else {
            report_post_ll.setVisibility(View.GONE);
        }*/


      /*  fbIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {
//                    postAppToWall();
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        if (bean.getIs_reherelo() == 1) {
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                shareContent = bean.getOriginalPost().getPostMedia().get(0).getUrl();
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentTitle("HereLo")
                                        .setImageUrl(Uri.parse(shareContent))
                                        .setContentDescription(
                                                bean.getOriginalPost().getText())
                                        .setContentUrl(Uri.parse("https://www.facebook.com"))
                                        .build();
                                shareDialog.show(linkContent);
                            } else {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentTitle("HereLo")
                                        .setContentDescription(
                                                bean.getOriginalPost().getText())
                                        .setContentUrl(Uri.parse("https://www.facebook.com"))
                                        .build();
                                shareDialog.show(linkContent);
                            }
                        } else {
                            if (bean.getPostMedia().size() > 0) {
                                shareContent = bean.getPostMedia().get(0).getUrl();
                                if (bean.getPostType().equalsIgnoreCase("2")) {
                                    ShareVideo mShareVideo = new ShareVideo.Builder()
                                            .setLocalUrl(Uri.parse(shareContent))
                                            .build();
                                    ShareVideoContent ShareVideo = new ShareVideoContent.Builder()
                                            .setVideo(mShareVideo)
                                            .setContentUrl(Uri.parse("https://www.facebook.com"))
                                            .setContentDescription(bean.getText())
                                            .setContentTitle("HereLo")
                                            .build();
                                    shareDialog.show(ShareVideo);
                                } else {
                                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                            .setContentTitle("HereLo")
                                            .setImageUrl(Uri.parse(shareContent))
                                            .setContentDescription(
                                                    bean.getText())
                                            .setContentUrl(Uri.parse("https://www.facebook.com"))
                                            .build();
                                    shareDialog.show(linkContent);
                                }
                            } else {
                                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                        .setContentTitle("HereLo")
                                        .setContentDescription(
                                                bean.getText())
                                        .setContentUrl(Uri.parse("https://www.facebook.com"))
                                        .build();
                                shareDialog.show(linkContent);
                            }
                        }
                        animateDown();
                    }
                } else {
                    Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                }
            }
        });
        twitterIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateDown();
                mTwitterAuthClient.authorize(CheckInDetailsActivity.this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> twitterSessionResult) {
                        // Success
                        // The TwitterSession is also available through:
                        // Twitter.getInstance().core.getSessionManager().getActiveSession()
                        TwitterSession session = twitterSessionResult.data;
                        // TODO: Remove toast and use the TwitterSession's userID
                        // with your app's user model
                        String msg = "@" + session.getUserName() + " logged in! (#" + session.getUserId() + ")";
                        Logger.logsInfo(MainViewScreen.class.getSimpleName(), "twitterSessionResult : "
                                + msg);


                        if (bean.getIs_reherelo() == 1) {
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {

                                shareContent = bean.getOriginalPost().getPostMedia().get(0).getUrl();

                                Uri myImageUri = Uri.parse(shareContent);

                                TweetComposer.Builder builder = new TweetComposer.Builder(CheckInDetailsActivity.this)
                                        .text("")
                                        .image(myImageUri);

                                builder.show();
                            } else {


                                TweetComposer.Builder builder = new TweetComposer.Builder(CheckInDetailsActivity.this)
                                        .text(bean.getOriginalPost().getText());

                                builder.show();

                            }
                        } else {
                            if (bean.getPostMedia().size() > 0) {

                                shareContent = bean.getPostMedia().get(0).getUrl();


                                if (bean.getPostType().equalsIgnoreCase("2")) {
                                    Uri myImageUri = Uri.parse(shareContent);

                                    TweetComposer.Builder builder = new TweetComposer.Builder(CheckInDetailsActivity.this)
                                            .text("")
                                            .image(myImageUri);

                                    builder.show();
                                } else {


                                    TweetComposer.Builder builder = new TweetComposer.Builder(CheckInDetailsActivity.this)
                                            .text(bean.getText());

                                    builder.show();
                                }

                            } else {

                                TweetComposer.Builder builder = new TweetComposer.Builder(CheckInDetailsActivity.this)
                                        .text(bean.getText());

                                builder.show();
                            }
                        }

                    }

                    @Override
                    public void failure(TwitterException e) {
                        Logger.logsInfo(MainViewScreen.class.getSimpleName(), "Exception : " + e.getMessage());
                    }
                });
            }
        });

        instagramIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (bean.getIs_reherelo() == 1) {

                    if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {

                        animateDown();
                        intent = CheckInDetailsActivity.this.getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                        if (intent != null) {
                            shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.setPackage("com.instagram.android");
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                downLoadImageForInsta(bean);
                            }


                        } else {
                            // bring user to the market to download the app.
                            // or let them choose an app?
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckInDetailsActivity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle(getResources().getString(R.string.installInstaText));

                            // Setting Dialog Message
                            alertDialog.setMessage(getResources().getString(R.string.areyousurewanttoinstallInstaText));

                            // Setting Icon to Dialog
//                        alertDialog.setIcon(R.drawable.delete);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton(getResources().getString(R.string.yesText), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    // Write your code here to invoke YES event
                                    intent = new Intent(Intent.ACTION_VIEW);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
                                    startActivity(intent);
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton(getResources().getString(R.string.noText), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event

                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();

                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                    }

                } else {
                    if (bean.getPostType().equalsIgnoreCase("1") || bean.getPostType().equalsIgnoreCase("2")) {
                        if (NetworkAvailablity.checkNetworkStatus(CheckInDetailsActivity.this)) {

                            animateDown();
                            intent = getPackageManager().getLaunchIntentForPackage("com.instagram.android");
                            if (intent != null) {
                                shareIntent = new Intent();
                                shareIntent.setAction(Intent.ACTION_SEND);
                                shareIntent.setPackage("com.instagram.android");
                                if (bean.getPostMedia().size() > 0) {
                                    downLoadImageForInsta(bean);
                                }


                            } else {
                                // bring user to the market to download the app.
                                // or let them choose an app?
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(CheckInDetailsActivity.this);

                                // Setting Dialog Title
                                alertDialog.setTitle(getResources().getString(R.string.installInstaText));

                                // Setting Dialog Message
                                alertDialog.setMessage(getResources().getString(R.string.areyousurewanttoinstallInstaText));

                                // Setting Icon to Dialog
//                        alertDialog.setIcon(R.drawable.delete);

                                // Setting Positive "Yes" Button
                                alertDialog.setPositiveButton(getResources().getString(R.string.yesText), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        // Write your code here to invoke YES event
                                        intent = new Intent(Intent.ACTION_VIEW);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.setData(Uri.parse("market://details?id=" + "com.instagram.android"));
                                        startActivity(intent);
                                    }
                                });

                                // Setting Negative "NO" Button
                                alertDialog.setNegativeButton(getResources().getString(R.string.noText), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Write your code here to invoke NO event

                                        dialog.cancel();
                                    }
                                });

                                // Showing Alert Message
                                alertDialog.show();

                            }
                        } else {
                            Constant.showToast(getResources().getString(R.string.internet), CheckInDetailsActivity.this);
                        }
                    } else {
                        Constant.showToast(getResources().getString(R.string.youcantshareTextOnInsta), CheckInDetailsActivity.this);
                    }
                }


            }
        });
*/
       /* tumblerIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateDown();
                setupTumblrSharing(bean);
            }
        });*/

    }


    // ////////// animation down
    public void animateDown() {
        _translateAnimation = new TranslateAnimation(0, 0, 0, 500);
        _translateAnimation.setDuration(500);
        _translateAnimation.setFillAfter(true);
        _shareLL.startAnimation(_translateAnimation);
        _shareLL.clearAnimation();
        _shareLL.setVisibility(View.GONE);
        _bottomRelativeLayout.setVisibility(View.GONE);
    }

    private void updateChatStatus() {
        String is_online = "0";
        if (isChatOn) {
            isChatOn = false;
            change_chat_status_iv.setImageResource(R.drawable.new_chat_off);
            chat_status_tv.setText(getResources().getString(R.string.chatOffText));
            is_online = "0";
        } else {
            isChatOn = true;
            change_chat_status_iv.setImageResource(R.drawable.new_chat_on);
            chat_status_tv.setText(getResources().getString(R.string.chatOnText));
            is_online = "1";
        }
        if (!this_is_other_user) {
            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                try {
                    Uri.Builder mBuilder = new Uri.Builder()

                            .appendQueryParameter("user_id", MySharedPreference.getCurrentUserId(getActivity()) + "")
                            .appendQueryParameter("is_online", is_online);
                    mChatToggleController.callWS(mBuilder, false, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
            }

        }
    }

    public void successResponse(UpdateProfileModal modal) {
        Logger.logsInfo("Update Profile", "Status : " + modal.status);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void responseUpdateCover(UpdateCoverModalMain modal) {

    }


    public class MyProfileScreenAdapter extends BaseAdapter {

        private Activity context = null;
        public ArrayList<MiniViewBean> list_item = new ArrayList<MiniViewBean>();
        int widthDevice = 0;
        int widthOfSmallImg = 0;
        public int clickpos = 0;

        String applicant_id;
        ViewHolder view_holder = new ViewHolder();
        SharedPreferences.Editor editor = null;

        public MyProfileScreenAdapter(Activity context, ArrayList<MiniViewBean> list_item) {
            this.context = context;
            this.list_item = list_item;
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            widthDevice = displayMetrics.widthPixels;

            widthOfSmallImg = widthDevice / 3;
        }

        @Override
        public int getCount() {
            return 6;
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
            //            public TextView _repost_count;
            public TextView _like_count;

            LinearLayout comment_click_ll;
            //            LinearLayout repostLL;
            LinearLayout likeLL;
            LinearLayout dislike_click_new;
            RelativeLayout sub_layout;

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
                view_holder._like_count = (TextView) convertView.findViewById(R.id.likes_count);
//                view_holder._like_count = (TextView) convertView.findViewById(R.id.like_count);


                view_holder.comment_click_ll = (LinearLayout) convertView.findViewById(R.id.coomment_click);
//                view_holder.repostLL = (LinearLayout) convertView.findViewById(R.id.like_click_new);
                view_holder.likeLL = (LinearLayout) convertView.findViewById(R.id.like_click_new);
                view_holder.dislike_click_new = (LinearLayout) convertView.findViewById(R.id.dislike_click_new);
                view_holder.sub_layout = (RelativeLayout) convertView.findViewById(R.id.sub_layout);


// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = view_holder.sub_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = widthOfSmallImg;
                params.width = widthOfSmallImg;


                /**
                 * TypeFace
                 *
                 */

                view_holder._userName.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
                view_holder._comment_count.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
//                view_holder._repost_count.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
                view_holder._like_count.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));

                convertView.setTag(view_holder);

            } else {
                view_holder = (ViewHolder) convertView.getTag();
            }
            view_holder.likeLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LikesFragment likesFragment = new LikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Screen", "Likes");

                    likesFragment.setArguments(bundle);

                    ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                }
            });

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

            if (view_holder.dislike_click_new != null) {
                view_holder.dislike_click_new.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Dislikes");
                        likesFragment.setArguments(bundle);

                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                    }
                });

            }

           /* view_holder.repostLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    _shareLL.setVisibility(View.GONE);
                    popupWindow();
                }
            });*/

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.image1);
            Bitmap bitmap = getRefelection(icon);

            if (bitmap != null) {
                BitmapDrawable ob = new BitmapDrawable(context.getResources(), bitmap);
                view_holder._herelo_img.setBackgroundDrawable(ob);
            }

            return convertView;
        }


        public void popupWindow() {

            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.post_popup);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

            dialog.show();
        }


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
            Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Bitmap.Config.ARGB_8888);
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
    }


    /**
     * Adapter class
     */


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
        private boolean isHashClicked = false;
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

            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            widthDevice = displayMetrics.widthPixels;

            widthOfSmallImg = widthDevice / 3;
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

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

            private LinearLayout like_ll;
            private LinearLayout dislike_click_new;
            private LinearLayout bottom_ll;

            public LinearLayout _commentLL;
            public ProgressBar img_progress = null;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;

            private RelativeLayout sub_layout;

            /*CHeckIn Indicator*/
            private ImageView checkIn_indicator_iv;

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder view_holder;

            //final MiniViewBean bean = (MiniViewBean) list_item.get(position);

            if (convertView == null) {
                view_holder = new ViewHolder();

                LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = _linflater.inflate(R.layout.gridview_row, null);


                view_holder.checkIn_indicator_iv = (ImageView) convertView.findViewById(R.id.checkIn_indicator_iv);
                view_holder.heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                view_holder.circleBackground = convertView.findViewById(R.id.circleBg);

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
                view_holder.sub_layout = (RelativeLayout) convertView.findViewById(R.id.sub_layout);


// Gets the layout params that will allow you to resize the layout
                ViewGroup.LayoutParams params = view_holder.sub_layout.getLayoutParams();
// Changes the height and width to the specified *pixels*
                params.height = widthOfSmallImg;

                view_holder._userName.setTypeface(tf);
                view_holder._comment_count.setTypeface(tf);
//                view_holder._like_count.setTypeface(tf);
                view_holder.likes_count.setTypeface(tf);
                view_holder.dislike_count.setTypeface(tf);
                view_holder.post_tv.setTypeface(tf);
                view_holder.more_images_count_tv.setTypeface(tf);
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
                view_holder.bottom_ll.setBackgroundResource(R.drawable.overlap_image);
            }


            if (view_holder.coomment_img != null) {
                view_holder.coomment_img.setImageResource(R.drawable.chat_icon_01);
            }
            if (fromGridHome) {
                view_holder._repostLL.setVisibility(View.GONE);
            } else {
                view_holder._repostLL.setVisibility(View.VISIBLE);
            }
           /* if (fromGridHome) {
                view_holder._userName.setVisibility(View.VISIBLE);
            } else {
                view_holder._userName.setVisibility(View.GONE);
            }*/
            view_holder._userName.setVisibility(View.GONE);

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
                /*    LikesFragment likesFragment = new LikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Screen", "Comments");
                    bundle.putInt("POSTID", bean.getId());
                    bundle.putInt("INDEX", position);
                    likesFragment.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/
                    Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                    mIntent.putExtra(Constant.POST_DETAILS, bean);
                    mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                    startActivity(mIntent);
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


            view_holder._herelo_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      /*Code of click*/
                    findDoubleClick(view_holder.circleBackground, view_holder.heartImageView, position);


                    if (!mHasDoubleClicked) {
                        _lastPos = position;

                    }

                }
            });

            view_holder.post_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isHashClicked) {
                      /*Code of click*/
                        findDoubleClick(view_holder.circleBackground, view_holder.heartImageView, position);


                        if (!mHasDoubleClicked) {

                            _lastPos = position;
                        }

                    } else {
                        isHashClicked = false;
                    }
                }
            });


            /*Managing Post Type */
            if (bean.getIs_reherelo() == 1) {
                view_holder.checkIn_indicator_iv.setVisibility(View.GONE);

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
                                    isHashClicked = true;
                                    Bundle mBundle = new Bundle();
//                            mBundle.putString(Constant.HASH_TAG_ID, hashtag_id);
                                    mBundle.putString(Constant.HASH_TAG_TEXT, hashTag);
//                            mBundle.putInt(Constant.PAGE_NO, page_no);
                                    mBundle.putInt(Constant.POST_CONTENT_TYPE, Integer.parseInt(post_content_type));
                                    HashTagScreenTwoGridView mHashTagScreenTwoGridView = new HashTagScreenTwoGridView();
                                    mHashTagScreenTwoGridView.setArguments(mBundle);
                                    replaceHashDetailsFragment(mHashTagScreenTwoGridView);
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
                        //Img post + text
                        view_holder.checkIn_indicator_iv.setVisibility(View.GONE);
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
                        //Video Post
                        view_holder.checkIn_indicator_iv.setVisibility(View.GONE);
                        view_holder._herelo_img.setVisibility(View.VISIBLE);
                        view_holder.play_icon.setVisibility(View.VISIBLE);
                        if (bean.getPostMedia().size() > 0) {
                            imageLoaderNew.displayImage(bean.getPostMedia().get(0).getThumb_url(), view_holder._herelo_img, options);
                        }

                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                        view_holder.post_tv.setVisibility(View.INVISIBLE);
                    } else if (bean.getPostType().equalsIgnoreCase("3")) {
                        //Text Only Post
                        view_holder.checkIn_indicator_iv.setVisibility(View.GONE);
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
                                            isHashClicked = true;
                                            Bundle mBundle = new Bundle();
//                            mBundle.putString(Constant.HASH_TAG_ID, hashtag_id);
                                            mBundle.putString(Constant.HASH_TAG_TEXT, hashTag);
//                            mBundle.putInt(Constant.PAGE_NO, page_no);
                                            mBundle.putInt(Constant.POST_CONTENT_TYPE, Integer.parseInt(post_content_type));
                                            HashTagScreenTwoGridView mHashTagScreenTwoGridView = new HashTagScreenTwoGridView();
                                            mHashTagScreenTwoGridView.setArguments(mBundle);
                                            replaceHashDetailsFragment(mHashTagScreenTwoGridView);
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

                    } else if (bean.getPostType().equalsIgnoreCase("6")) {
                        //For single image + text - 6 - CheckIn
                        view_holder.checkIn_indicator_iv.setVisibility(View.VISIBLE);
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

                    } else if (bean.getPostType().equalsIgnoreCase("7")) {
                        //For multiple image - 7 - CheckIn
                        view_holder.checkIn_indicator_iv.setVisibility(View.VISIBLE);
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

                    } else if (bean.getPostType().equalsIgnoreCase("8")) {
                        //For Text only - 8 - CheckIn Text Only Post

                        view_holder.checkIn_indicator_iv.setVisibility(View.VISIBLE);
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
                                            isHashClicked = true;
                                            //Constant.showToast("You clicked on :" + hashTag, getActivity());
                                            Bundle mBundle = new Bundle();
//                            mBundle.putString(Constant.HASH_TAG_ID, hashtag_id);
                                            mBundle.putString(Constant.HASH_TAG_TEXT, hashTag);
//                            mBundle.putInt(Constant.PAGE_NO, page_no);
                                            mBundle.putInt(Constant.POST_CONTENT_TYPE, Integer.parseInt(post_content_type));
                                            HashTagScreenTwoGridView mHashTagScreenTwoGridView = new HashTagScreenTwoGridView();
                                            mHashTagScreenTwoGridView.setArguments(mBundle);
                                            replaceHashDetailsFragment(mHashTagScreenTwoGridView);
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


                    } else if (bean.getPostType().equalsIgnoreCase("9")) {
                        //For Video - 9- CheckIn Video Post
                        view_holder.checkIn_indicator_iv.setVisibility(View.VISIBLE);
                        view_holder._herelo_img.setVisibility(View.VISIBLE);
                        view_holder.play_icon.setVisibility(View.VISIBLE);
                        if (bean.getPostMedia().size() > 0) {
                            imageLoaderNew.displayImage(bean.getPostMedia().get(0).getThumb_url(), view_holder._herelo_img, options);
                        }

                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                        view_holder.post_tv.setVisibility(View.INVISIBLE);

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
                        // Constant.heart(view_holder.circleBackground, view_holder.heartImageView);
                        int is_like_status = bean.getIs_like_status();
                        Constant.growShrinkLike(view_holder.smile_img1);
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
                        //Constant.dislikeAnimation(view_holder.circleBackground, view_holder.heartImageView);
                        int is_dislike_status = bean.getIs_dislike_status();
                        Constant.growShrinkLike(view_holder.dislike_img);
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

//            view_holder._userName.setText(bean.getUser().getUsername());
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
        private boolean findDoubleClick(View circleBackground, ImageView heartImageView, final int position) {
            // Get current time in nano seconds.
            long pressTime = System.currentTimeMillis();

            final int mCurrentImgInd = 0;
            // If double click...
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {

                if (_lastPos == position) {

                    Constant.heart(circleBackground, heartImageView);

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
                        mIntent.putExtra(Constant.FROM_USER_PROFILE, true);
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

                       /* Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
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
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);

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
                                mIntent.putExtra(Constant.FROM_USER_PROFILE, true);
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
                                mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
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
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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

    private void replaceHashDetailsFragment(HashTagScreenTwoGridView mHashTagScreenTwoGridView) {
        ((BaseContainerFragment) getParentFragment()).replaceFragment(mHashTagScreenTwoGridView, true);

    }


    public class MainViewAdapterRecyclerView extends ToroAdapter<ToroAdapter.ViewHolder> {

        private String followUnfollowAPIName = "";
        /*Image Loader*/

        private DisplayImageOptions optionsPostImg;
        private DisplayImageOptions optionsProfileImg;
        ImageLoader imageLoaderNew;


        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        private int lastVisibleItem, totalItemCount;
        private boolean loading;
        private OnLoadMoreListener onLoadMoreListener;

        public void autoPlayVideo(int position) throws Exception {

            Logger.logsError(TAG, "autoPlayVideo called for position " + position);
        }

        public MainViewAdapterRecyclerView(RecyclerView recyclerView) {

//            Logger.logsInfo(MainViewScreen.class.getSimpleName(),"MainViewAdapterRecyclerView Cons Called");
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

            imageLoaderNew = ImageLoader.getInstance();
            optionsProfileImg = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.profile_icon) //
                    .showImageForEmptyUri(R.drawable.profile_icon)
                    .showImageOnFail(R.drawable.profile_icon)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)

                    .build();
            optionsPostImg = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.post_img_default) //
                    .showImageForEmptyUri(R.drawable.post_img_default)
                    .showImageOnFail(R.drawable.post_img_default)

                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)

                    .build();


            mDataSourceFrames = new DataSourceFrames(getActivity());
            mDataSourceFrames.createFrameDataBase();
            mDataSourceFrames.openFrameDataBase();

            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();


                recyclerView
                        .addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrolled(RecyclerView recyclerView,
                                                   int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);

                                totalItemCount = linearLayoutManager.getItemCount();
                                lastVisibleItem = linearLayoutManager
                                        .findLastVisibleItemPosition();
                                if (!loading
                                        && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                                    // End has been reached
                                    // Do something
                                    if (onLoadMoreListener != null) {
                                        onLoadMoreListener.onLoadMore();
                                    }
                                    loading = true;
                                }
                            }
                        });
            }
        }

        public void setLoaded() {
            loading = false;
        }

        public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
            this.onLoadMoreListener = onLoadMoreListener;
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            Logger.logsInfo(MainViewScreen.class.getSimpleName(),"MainViewAdapterRecyclerView onCreateViewHolder Called");
            View view;
            final ViewHolder viewHolder;
//            Logger.logsInfo(MainViewScreen.class.getSimpleName(),"viewType : " + viewType);
            if (viewType == 1) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_img, null);
                viewHolder = new ImageTextType(view);
            } else if (viewType == 2) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_video_only, null);
                viewHolder = new SimpleToroVideoViewHolder(view);
            } else if (viewType == 3) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_text_only, null);
                viewHolder = new TextOnlyPost(view);
            } else if (viewType == 4) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_img_frames, null);
                viewHolder = new FramePostType(view);
            } else if (viewType == 5) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_reherelo, null);
                viewHolder = new RehereloPostType(view);
            } else if (viewType == 6) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_checkin_singleimg_text, null);
                viewHolder = new CheckInSingleImageWithText(view);
            } else if (viewType == 7) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_img_frames_checkins, null);
                viewHolder = new CheckInFrames(view);
            } else if (viewType == 8) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_checkin_text_only, null);
                viewHolder = new CheckInTextOnlyPostType(view);
            } else if (viewType == 9) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.main_view_posttype_video_checkin, null);
                viewHolder = new CheckInVideoPostType(view);
            } else {
                return null;
            }

            return viewHolder;
        }

        @Nullable
        @Override
        protected Object getItem(int position) {
            return mDataList.get(position);
        }


        public class ProgressViewHolder extends RecyclerView.ViewHolder {
            public ProgressBar progressBar;

            public ProgressViewHolder(View v) {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            }
        }

        class LikeUnLike extends WebserviceTask {

            public LikeUnLike(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {
            /*Progress Dialog here*/
//                Constant.showProgressDialog(activity);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.like_dislikepost;
            }

            @Override
            public void onLoadingFinished(String response) {

//                Constant.cancelDialog();
                try {

                    if (response != null || !response.equalsIgnoreCase("")) {
                        Gson gson = new Gson();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }

        class DislikeRemoveDislike extends WebserviceTask {

            public DislikeRemoveDislike(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {
            /*Progress Dialog here*/
//                Constant.showProgressDialog(activity);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.removeUnlikeDislike;
            }

            @Override
            public void onLoadingFinished(String response) {

//                Constant.cancelDialog();
                try {

                    if (response != null || !response.equalsIgnoreCase("")) {
                        Gson gson = new Gson();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }

        VideoPostOnly lastPlayVideoHolder = null;
        RehereloPostType rehereloPostTypeVideo = null;
        CheckInVideoPostType mCheckInVideoPostType = null;


        /////////////////////
        private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
        private long lastPressTime;
        private int _lastPos;
        private boolean mHasDoubleClicked = false;

        private boolean findDoubleClick(View circleBackground, ImageView heartImageView, final int position, final int mCurrentImgInd,
                                        final boolean b,
                                        final VideoPostOnly final_viewHolder,
                                        final RehereloPostType final_viewHolder1
                , final CheckInVideoPostType mCheckInVideoPostTypeHolder, final View v, final boolean isSingleClickRequired) {
            // Get current time in nano seconds.
            long pressTime = System.currentTimeMillis();


            // If double click...
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {

                if (_lastPos == position) {

                    mHasDoubleClicked = true;
                    Logger.logsInfo("============>double click", "double click");

                    Constant.heart(circleBackground, heartImageView);

                    final DatumHome bean = mDataList.get(position);
//                    Logger.logsInfo("============>double click", "ID : " + bean.getId());
                     /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

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
                        Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                    }
                } else {
                    if (isSingleClickRequired) {
                        mHasDoubleClicked = false;
                        Logger.logsInfo(TAG, "Single click");
                        final DatumHome bean = mDataList.get(position);
                        if (b) {
                            TextureVideoView textureView = (TextureVideoView) v;
                       /* Matrix txform = new Matrix();
                        textureView.getTransform(txform);
                        int xoff = (textureView.getWidth() - Constant.getDeviceWidth(getActivity())) / 2;
                        int yoff = (textureView.getHeight() - 320) / 2;
                        txform.setScale((float) Constant.getDeviceWidth(getActivity()) / textureView.getWidth(),
                                (float) 320 / textureView.getHeight());
                        txform.postTranslate(xoff, yoff);
                        textureView.setTransform(txform);*/
                            if (final_viewHolder != null) {
                                if (textureView.getState() == TextureVideoView.MediaState.INIT
                                        ||
                                        textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                    textureView.play(bean.getPostMedia().get(0).getUrl(), bean.getPostMedia().get(0).getSlow_motion());

                                    final_viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                                    final_viewHolder.imvPlay.setVisibility(View.GONE);
                                    final_viewHolder.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                    textureView.start();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.GONE);
                                    final_viewHolder.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                    textureView.pause();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
//                            final_viewHolder.thumb_iv.setVisibility(View.VISIBLE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                    textureView.stop();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                                    final_viewHolder.thumb_iv.setVisibility(View.VISIBLE);
                                }
                            } else if (final_viewHolder1 != null) {
                                if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {
                                    textureView.play(bean.getOriginalPost().getPostMedia().get(0).getUrl(), bean.getOriginalPost().getPostMedia().get(0).getSlow_motion());
                                    final_viewHolder1.pbWaiting.setVisibility(View.VISIBLE);
                                    final_viewHolder1.imvPlay.setVisibility(View.GONE);
                                    final_viewHolder1.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                    textureView.start();
                                    final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder1.imvPlay.setVisibility(View.GONE);
                                    final_viewHolder1.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                    textureView.pause();
                                    final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder1.imvPlay.setVisibility(View.VISIBLE);
//                            final_viewHolder1.thumb_iv.setVisibility(View.VISIBLE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                    textureView.stop();
                                    final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder1.imvPlay.setVisibility(View.VISIBLE);
                                    final_viewHolder1.thumb_iv.setVisibility(View.VISIBLE);
                                }
                            } else if (mCheckInVideoPostTypeHolder != null) {
                                if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                    textureView.play(bean.getPostMedia().get(0).getUrl(), bean.getPostMedia().get(0).getSlow_motion());
                                    mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.VISIBLE);
                                    mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.GONE);
                                    mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                    textureView.start();
                                    mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                    mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.GONE);
                                    mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                    textureView.pause();
                                    mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                    mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.VISIBLE);
//                            mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.VISIBLE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                    textureView.stop();
                                    mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                    mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.VISIBLE);
                                    mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.VISIBLE);
                                }
                            }

                        } else {
                          /*  Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
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
                                if (bean.getOriginalPost().getFrame_id() == 0) {
                                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                        mIntent.putExtra(Constant.MEDIA_ID, bean.getOriginalPost().getPostMedia().get(0).getId());
                                    }

                                }
                                mIntent.putExtra("POSTTEXT", bean.getOriginalPost().getText());
                                mIntent.putExtra("POSTTYPE", bean.getOriginalPost().getPost_type());
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
                            mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                            startActivity(mIntent);
                        }
                    }


                }


            } else {     // If not double click....
                mHasDoubleClicked = false;
                Handler myHandler = new Handler() {
                    public void handleMessage(Message m) {
                        if (isSingleClickRequired) {
                            if (!mHasDoubleClicked) {
                                final DatumHome bean = mDataList.get(position);
                                Logger.logsInfo("============>Single click", "Single click");
                                if (b) {
                                    TextureVideoView textureView = (TextureVideoView) v;
                                    if (final_viewHolder != null) {
                                        if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                            textureView.play(bean.getPostMedia().get(0).getUrl(), bean.getPostMedia().get(0).getSlow_motion());
                                            final_viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                                            final_viewHolder.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                            textureView.start();
                                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                                            final_viewHolder.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                            textureView.pause();
                                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
//                            final_viewHolder.thumb_iv.setVisibility(View.VISIBLE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                            textureView.stop();
                                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                                            final_viewHolder.thumb_iv.setVisibility(View.VISIBLE);
                                        }
                                    } else if (final_viewHolder1 != null) {
                                        if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                            textureView.play(bean.getOriginalPost().getPostMedia().get(0).getUrl(), bean.getOriginalPost().getPostMedia().get(0).getSlow_motion());
                                            final_viewHolder1.pbWaiting.setVisibility(View.VISIBLE);
                                            final_viewHolder1.imvPlay.setVisibility(View.GONE);
                                            final_viewHolder1.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                            textureView.start();
                                            final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder1.imvPlay.setVisibility(View.GONE);
                                            final_viewHolder1.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                            textureView.pause();
                                            final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder1.imvPlay.setVisibility(View.VISIBLE);
//                            final_viewHolder1.thumb_iv.setVisibility(View.VISIBLE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                            textureView.stop();
                                            final_viewHolder1.pbWaiting.setVisibility(View.GONE);
                                            final_viewHolder1.imvPlay.setVisibility(View.VISIBLE);
                                            final_viewHolder1.thumb_iv.setVisibility(View.VISIBLE);
                                        }
                                    } else if (mCheckInVideoPostTypeHolder != null) {
                                        if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                            textureView.play(bean.getPostMedia().get(0).getUrl(), bean.getPostMedia().get(0).getSlow_motion());
                                            mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.VISIBLE);
                                            mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.GONE);
                                            mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                            textureView.start();
                                            mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                            mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.GONE);
                                            mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.GONE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                            textureView.pause();
                                            mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                            mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.VISIBLE);
//                            mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.VISIBLE);
                                        } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                            textureView.stop();
                                            mCheckInVideoPostTypeHolder.pbWaiting.setVisibility(View.GONE);
                                            mCheckInVideoPostTypeHolder.imvPlay.setVisibility(View.VISIBLE);
                                            mCheckInVideoPostTypeHolder.thumb_iv.setVisibility(View.VISIBLE);
                                        }
                                    }
                                } else {

                          /*  Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
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
                                if (bean.getOriginalPost().getFrame_id() == 0) {
                                    if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                        mIntent.putExtra(Constant.MEDIA_ID, bean.getOriginalPost().getPostMedia().get(0).getId());
                                    }

                                }
                                mIntent.putExtra("POSTTEXT", bean.getOriginalPost().getText());
                                mIntent.putExtra("POSTTYPE", bean.getOriginalPost().getPost_type());
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
                                    Logger.logsInfo(TAG, "POst Id : " + bean.getId());
                                    mIntent.putExtra(Constant.POST_DETAILS, bean);
                                    mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                                    startActivity(mIntent);
                                }
                                //                            Toast.makeText(context.getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
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

        @Override
        public int getItemCount() {
//            Logger.logsInfo(MainViewScreen.class.getSimpleName(),"MainViewAdapterRecyclerView getItemCount Called");
            return mDataList.size();
        }

        public class CheckInSingleImageWithText extends ViewHolder {

            CircleImageView profileIV;

            TextView nameTV;
            TextView loc_name_tv;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            public ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;
            LinearLayout parent_container;

            ImageView map_iv;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;


            public CheckInSingleImageWithText(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                loc_name_tv = (TextView) convertView.findViewById(R.id.loc_name_tv);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                coverIV = (ImageView) convertView.findViewById(R.id.coverIV);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                map_iv = (ImageView) convertView.findViewById(R.id.map_iv);

                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);
                parent_container = (LinearLayout) convertView.findViewById(R.id.parent_container);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;
                map_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                        if (!mHasDoubleClicked) {


                            _lastPos = getAdapterPosition();
                        }
                    }
                });
                loc_name_tv.setText(bean.getLocation());


                imageLoaderNew.loadImage(Constant.getMapUrlCheckInStatic(bean.getLat(), bean.getLng(), 500
                        , 1500, bean.getZoom_level()), optionsPostImg, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
//                            BitmapDrawable ob = new BitmapDrawable(getResources(), loadedImage);
                        map_iv.setImageBitmap(loadedImage);
                    }
                });
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);

                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
/*
                                if (anim != null && !anim.isRunning()){
                                    anim.start();
                                }*/

                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/

                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                                Constant.heart(circleBackground, heartImageView);

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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }
                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);

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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(getResources().getString(R.string.checkedInAtText));
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }
                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
                cover_detailTV.setMaxLines(4);
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }


                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });

                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }

                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });
                }
            }
        }

        public class CheckInTextOnlyPostType extends ViewHolder {


            CircleImageView profileIV;

            TextView nameTV;
            TextView loc_name_tv;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;
            ImageView map_iv;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;

            ImageView check_in_iv;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;


            public CheckInTextOnlyPostType(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                loc_name_tv = (TextView) convertView.findViewById(R.id.loc_name_tv);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                check_in_iv = (ImageView) convertView.findViewById(R.id.check_in_iv);

                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                map_iv = (ImageView) convertView.findViewById(R.id.map_iv);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;

                check_in_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getActivity(), CheckInDetailsActivity.class));
                    }
                });

                imageLoaderNew.loadImage(Constant.getMapUrlCheckInStatic(bean.getLat(), bean.getLng(), 500
                        , 1500, bean.getZoom_level()), optionsPostImg, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
//                        BitmapDrawable ob = new BitmapDrawable(getResources(), loadedImage);
                        map_iv.setImageBitmap(loadedImage);
                    }
                });

                loc_name_tv.setText(bean.getLocation());
                    /*imageLoaderNew.displayImage(Constant.getMapUrlCheckInStatic(bean.getLat(), bean.getLng(), 500
                                    , Constant.getDeviceWidth(getActivity())),
                            map_iv, optionsPostImg);*/
                map_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                        if (!mHasDoubleClicked) {


                            _lastPos = getAdapterPosition();
                        }
                    }
                });
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }

                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });

            /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);

                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);

                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(getResources().getString(R.string.checkedInAtText));
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }


                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
                cover_detailTV.setMaxLines(14);
                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {


                                _lastPos = getAdapterPosition();
                            } else {


                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
            }
        }

        class WSTaskFollowUnfollow extends WebserviceTask {

            public WSTaskFollowUnfollow(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {
            /*Progress Dialog here*/
//            Constant.showProgressDialog(mActivity);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + followUnfollowAPIName;
            }

            @Override
            public void onLoadingFinished(String response) {

            }
        }

        public class RehereloPostType extends ToroVideoViewHolder {
            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;

            private FrameLayout frame_container;
            private FrameLayout frame_container_reherelo;
            private RelativeLayout frame_rl;
            private RelativeLayout frame_rl_reherelo;

            CircleImageView profileIV_OG;
            ImageView onlieIV_OG;
            ImageView post_iv_og;
            TextView nameTV_OG;
            TextView detailTV_OG;
            TextView timeTV_OG;
            TextView viewTV_OG;
            TextView cover_detailTV_og;
            TextView original_header_nameTV;

            RelativeLayout videoplayer_og_rl;
            RelativeLayout reherelo_main_rl;


            TextureVideoView mTextureVideoView;
            ImageView imvPreview;
            ImageView imvPlay;
            ProgressBar pbWaiting;
            ProgressBar pbProgressBar;
            RelativeLayout fullscreen_rl;
            ImageView thumb_iv;
            ImageView volume_iv;
            ImageView details_iv;


            Runnable mRunnable;
            Handler mHandler = new Handler();
            private static final int VIDEO_BOTTOM_VIEW_TIME = 4;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;


            public RehereloPostType(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV_OG = (CircleImageView) convertView.findViewById(R.id.profileIV_OG);
                onlieIV_OG = (ImageView) convertView.findViewById(R.id.onlieIV_OG);
                thumb_iv = (ImageView) convertView.findViewById(R.id.thumb_iv);
                volume_iv = (ImageView) convertView.findViewById(R.id.volume_iv);
                details_iv = (ImageView) convertView.findViewById(R.id.details_iv);
                fullscreen_rl = (RelativeLayout) convertView.findViewById(R.id.fullscreen_rl);
                nameTV_OG = (TextView) convertView.findViewById(R.id.nameTV_OG);
                detailTV_OG = (TextView) convertView.findViewById(R.id.detailTV_OG);
                timeTV_OG = (TextView) convertView.findViewById(R.id.timeTV_OG);
                viewTV_OG = (TextView) convertView.findViewById(R.id.viewTV_OG);
                cover_detailTV_og = (TextView) convertView.findViewById(R.id.cover_detailTV_og);
                post_iv_og = (ImageView) convertView.findViewById(R.id.post_iv_og);
                original_header_nameTV = (TextView) convertView.findViewById(R.id.original_header_nameTV);
                frame_container_reherelo = (FrameLayout) convertView.findViewById(R.id.frame_container_reherelo);
                frame_rl_reherelo = (RelativeLayout) convertView.findViewById(R.id.frame_rl_reherelo);
                videoplayer_og_rl = (RelativeLayout) convertView.findViewById(R.id.videoplayer_og_rl);
//                        videoplayer_og = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer_og);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);

                mTextureVideoView = (TextureVideoView) convertView.findViewById(R.id.textureview);
                imvPreview = (ImageView) convertView.findViewById(R.id.imv_preview);
                imvPlay = (ImageView) convertView.findViewById(R.id.imv_video_play);
                pbWaiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
                pbProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_progressbar);


                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);
                reherelo_main_rl = (RelativeLayout) convertView.findViewById(R.id.reherelo_main_rl);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV_OG.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV_OG.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV_og.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                original_header_nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));


            }

            @Override
            protected ToroVideoView findVideoView(View itemView) {
                return (ToroVideoView) itemView.findViewById(R.id.video);
            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;

                if (bean.isVolumeOn()) {
                    volume_iv.setImageResource(R.drawable.volume_on);
                } else {
                    volume_iv.setImageResource(R.drawable.volume_off);
                }
                volume_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
                    }
                });


                mRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        fullscreen_rl.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }
                };
                videoplayer_og_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, false);


                        if (!mHasDoubleClicked) {
                            _lastPos = getAdapterPosition();

                        }
                        if (fullscreen_rl.getVisibility() == View.VISIBLE) {
                            fullscreen_rl.setVisibility(View.GONE);

                        } else {
                            fullscreen_rl.setVisibility(View.VISIBLE);


                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, VIDEO_BOTTOM_VIEW_TIME * 1000); // VIDEO_BOTTOM_VIEW_TIME seconds
                        }
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
                        //Constant.showToast("Video Clicked called.", getActivity());
                    }
                });
                if (bean.getOriginalPost().getPostMedia().size() > 0) {
                    mVideoView.setVideoURI(Uri.parse(bean.getOriginalPost().getPostMedia().get(0).getUrl()), bean.getOriginalPost().getPostMedia().get(0).getSlow_motion());
                }
                profileIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                        mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                        mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                        startActivity(mIntent);
                    }
                });


                nameTV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                        mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                        mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                        startActivity(mIntent);
                    }
                });

//                    Logger.logsInfo(TAG, "Case 5 called");
                if (bean.getOriginalPost().getPost_location() != null && !bean.getOriginalPost().getPost_location().equalsIgnoreCase("")) {
                    detailTV_OG.setVisibility(View.VISIBLE);
                    nameTV_OG.setVisibility(View.VISIBLE);
                    detailTV_OG.setText(bean.getLocation());

                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    detailTV_OG.setVisibility(View.GONE);
                    nameTV_OG.setVisibility(View.VISIBLE);

                }
                onlieIV_OG.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getOriginalPost().getIs_following() == 1) {
                                bean.getOriginalPost().setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.getOriginalPost().setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getOriginalPost().getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });
                Logger.logsError(TAG, "post Id : " + bean.getId());
                if (username.equalsIgnoreCase(bean.getOriginalPost().getUser().getUsername())) {
                    onlieIV_OG.setVisibility(View.GONE);
                } else {
                    onlieIV_OG.setVisibility(View.VISIBLE);
                    if (bean.getOriginalPost().getIs_following() == 1) {
                        onlieIV_OG.setImageResource(R.drawable.main_right);
                    } else {
                        onlieIV_OG.setImageResource(R.drawable.main_plus_icon);
                    }

                }
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }


                }
                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });
                    /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                       comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                       comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);


                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(bean.getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }

                if (bean.getOriginalPost() != null) {
                    if (profileIV_OG != null) {
                        imageLoaderNew.displayImage(bean.getOriginalPost().getUser().getImage(), profileIV_OG, optionsProfileImg);
                    }

                }
                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
//                    original_header_nameTV.setText(bean.getOriginalPost().getUser().getUsername());
                original_header_nameTV.setText(getResources().getString(R.string.rehereloadText));
                nameTV_OG.setText(bean.getOriginalPost().getUser().getUsername());
                detailTV_OG.setText(bean.getOriginalPost().getPost_location());
                timeTV_OG.setText(bean.getOriginalPost().getPostTime());
                viewTV_OG.setText(bean.getOriginalPost().getTotalPostView() + "");

                if (bean.getOriginalPost().getFrame_id() > 0) {

                    mDataSourceFrames.openFrameDataBase();
                    mFrameDetailsBeanArrayList = mDataSourceFrames.getFrameDetails(bean.getOriginalPost().getFrame_id());
                    int deviceWidth2 = Constant.getDeviceWidth(getActivity()) - 50;
//                        int deviceWidth = _viewHolder.frame_rl_reherelo.getWidth();
                    FrameParentBean mFrameParentBean2 = mDataSourceFrames.getSingleFrameParent(
                            bean.getOriginalPost().getFrame_id());
                    frameWidth = mFrameParentBean2.getFrame_width();
                    frameHeight = mFrameParentBean2.getFrame_width();
//                        Logger.logsInfo(TAG, "Frame Height & Width before update : " + frameHeight + ":" + frameWidth);
                    aspectFactor = (deviceWidth2 / frameWidth);
                    frameWidth = frameWidth * aspectFactor;
                    frameHeight = frameHeight * aspectFactor;
//                        Logger.logsInfo(TAG, "Frame Height & Width after update : " + frameHeight + ":" + frameWidth);
//                        Logger.logsInfo(TAG, "aspectFactor : " + aspectFactor);
                    RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(deviceWidth2, (int) frameHeight);
                    frame_container_reherelo.setLayoutParams(lp2);

                    frame_container_reherelo.removeAllViews();

                    for (int i = 0; i < mFrameDetailsBeanArrayList.size(); i++) {
                        FrameDetailsBean mFrameDetailsBean = mFrameDetailsBeanArrayList.get(i);
//            int demoColor= mColorDemo.get(i);
                        final ImageView img = new ImageView(getActivity());

                        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        final int finalI = i;
                        img.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Logger.logsInfo(TAG, "IMg Clicked : " + finalI);
                                findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), finalI, false, null, null, null, null, true);
                                if (!mHasDoubleClicked) {
                                    _lastPos = getAdapterPosition();

                                }
                            }
                        });
                        try {
                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(i).getUrl(), img, optionsPostImg);
                            }
                        } catch (Exception e) {

                        }



                           /* // Load image, decode it to Bitmap and return Bitmap to callback
                            imageLoaderNew.loadImage( bean.getPostMedia().get(i).getUrl(),optionsPostImg, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    // Do whatever you want with Bitmap
                                    img.setImageBitmap(loadedImage);
                                }
                            });*/
//            img.setBackgroundColor(demoColor);
//..load something inside the ImageView, we just set the background color
                        float viewHeight = (mFrameDetailsBean.getmImgHeight() * aspectFactor) - marginLayout;
                        float viewWidth = (mFrameDetailsBean.getmImgWidth() * aspectFactor) - marginLayout;
//                            Logger.logsInfo(TAG, "viewWidth : " +" " +i + " " +(int)viewWidth);
//                            Logger.logsInfo(TAG, "viewHeight : "+" " +i+" " +(int)viewHeight);
//                            Logger.logsInfo(TAG, "Frame Id : "+bean.getFrame_id());
//                            Logger.logsInfo(TAG, "Id : "+bean.getId());
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                        float marginLeft = (mFrameDetailsBean.getmLeftCoordinate() * aspectFactor) + marginLayout;
                        float marginTop = (mFrameDetailsBean.getmTopCoordinate() * aspectFactor) + marginLayout;

//                            Logger.logsInfo(TAG, "marginLeft : " +" " +i+" " + marginLeft);
//                            Logger.logsInfo(TAG, "marginTop : " +" " +i+" " + marginTop);

                        params.leftMargin = (int) marginLeft;
                        params.topMargin = (int) marginTop;
//            params.rightMargin = (int)aspectFactor*5;
//            params.bottomMargin = (int)aspectFactor*5;

                        RelativeLayout mRelativeLayout = new RelativeLayout(getActivity());
                        mRelativeLayout.setPadding(0, 0, marginLayout, marginLayout);
                        mRelativeLayout.addView(img, params);
                        frame_container_reherelo.addView(mRelativeLayout);
                        if (bean.getOriginalPost().getPostMedia().size() > mFrameDetailsBeanArrayList.size()
                                && i == (mFrameDetailsBeanArrayList.size() - 1)) {
//                                Logger.logsInfo(MainViewScreen.class.getSimpleName(), "+ show Reherelo: " + i);
                            final ImageView imgOverLay = new ImageView(getActivity());
                            final TextView tvOverlay = new TextView(getActivity());
                            int remainingImgCount = bean.getOriginalPost().getPostMedia().size() - mFrameDetailsBeanArrayList.size();
                            tvOverlay.setText("+" + remainingImgCount);
                            tvOverlay.setTextColor(Color.WHITE);
                            tvOverlay.setTextSize(30);
                            tvOverlay.setGravity(Gravity.CENTER);
                            imgOverLay.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            imgOverLay.setBackgroundColor(getActivity().getResources().getColor(R.color.black_overlay));
                            RelativeLayout mRelativeLayoutOverLay = new RelativeLayout(getActivity());
//                                    mRelativeLayoutOverLay.setGravity(Gravity.CENTER);
                            mRelativeLayoutOverLay.setPadding(0, 0, marginLayout, marginLayout);
//                                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                            RelativeLayout.LayoutParams paramsSub = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                            RelativeLayout mSubRel = new RelativeLayout(getActivity());
                            paramsSub.addRule(RelativeLayout.CENTER_IN_PARENT);
                            mSubRel.setGravity(Gravity.CENTER);
                            mSubRel.addView(imgOverLay, paramsSub);
                            mSubRel.addView(tvOverlay, paramsSub);
                            mRelativeLayoutOverLay.addView(mSubRel, params);

                            frame_container_reherelo.addView(mRelativeLayoutOverLay);
                        }

                    }
                    if (bean.getOriginalPost().getPost_type() == 1) { //img , img + text
                        videoplayer_og_rl.setVisibility(View.GONE);
                        post_iv_og.setVisibility(View.GONE);
                        frame_rl_reherelo.setVisibility(View.VISIBLE);

                        if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {

                            if (bean.getHashTags().size() > 0) {
                                HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                    @Override
                                    public void onHashTagClicked(String hashTag) {
//                                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                                    }
                                });
                                cover_detailTV_og.setText(bean.getOriginalPost().getText());
                                // pass a TextView or any descendant of it (incliding EditText) here.
                                // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                mTextHashTagHelper.handle(cover_detailTV_og);
                            } else {
                                cover_detailTV_og.setText(bean.getOriginalPost().getText());
                            }

                        } else {
                            cover_detailTV_og.setVisibility(View.GONE);
                            cover_detailTV_og.setText("");
                        }

                    } else if (bean.getOriginalPost().getPost_type() == 2) {
                        videoplayer_og_rl.setVisibility(View.GONE);
                        post_iv_og.setVisibility(View.GONE);
                        frame_rl_reherelo.setVisibility(View.GONE);
                    } else if (bean.getOriginalPost().getPost_type() == 3) {
                        videoplayer_og_rl.setVisibility(View.GONE);
                        post_iv_og.setVisibility(View.GONE);
                        frame_rl_reherelo.setVisibility(View.GONE);
                    }
                } else {
/*
                        if (username.equalsIgnoreCase(bean.getOriginalPost().getUser().getUsername())) {
                            onlieIV_OG.setVisibility(View.GONE);
                        } else {
                            onlieIV_OG.setVisibility(View.VISIBLE);
                            onlieIV_OG.setImageResource(R.drawable.main_right);

                        }*/

                    nameTV_OG.setText(bean.getOriginalPost().getUser().getUsername());
                    detailTV_OG.setText(bean.getOriginalPost().getPost_location());
                    timeTV_OG.setText(bean.getOriginalPost().getPostTime());
                    viewTV_OG.setText(bean.getOriginalPost().getTotalPostView() + "");

                    frame_rl_reherelo.setVisibility(View.GONE);
                    if (bean.getOriginalPost().getPost_type() == 1) { //img , img + text
                        post_iv_og.setVisibility(View.VISIBLE);
                        cover_detailTV_og.setVisibility(View.VISIBLE);
                        videoplayer_og_rl.setVisibility(View.GONE);
                        if (bean.getOriginalPost().getPostMedia().size() > 0) {
                            imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(0).getUrl(), post_iv_og, optionsPostImg);
                            cover_detailTV_og.setMaxLines(2);
                        } else {
                            cover_detailTV_og.setMaxLines(8);
                        }
                        if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {
                            cover_detailTV_og.setVisibility(View.VISIBLE);
                            if (bean.getHashTags().size() > 0) {
                                HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                    @Override
                                    public void onHashTagClicked(String hashTag) {
//                                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                                    }
                                });
                                cover_detailTV_og.setText(bean.getOriginalPost().getText());
                                // pass a TextView or any descendant of it (incliding EditText) here.
                                // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                mTextHashTagHelper.handle(cover_detailTV_og);
                            } else {
                                cover_detailTV_og.setText(bean.getOriginalPost().getText());
                            }

                        } else {
                            cover_detailTV_og.setVisibility(View.GONE);
                            cover_detailTV_og.setText("");
                        }

                    } else if (bean.getOriginalPost().getPost_type() == 2) {//video

                        post_iv_og.setVisibility(View.GONE);
                        cover_detailTV_og.setVisibility(View.GONE);
                        videoplayer_og_rl.setVisibility(View.VISIBLE);
                        if (bean.getOriginalPost().getPostMedia().size() > 0) {
                            cover_detailTV_og.setMaxLines(2);
                            if (details_iv != null) {
                                details_iv.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                                    }
                                });
                            }

                        } else {
                            cover_detailTV_og.setMaxLines(8);
                        }

                    } else if (bean.getOriginalPost().getPost_type() == 3) {//text
                        post_iv_og.setVisibility(View.GONE);
                        cover_detailTV_og.setVisibility(View.VISIBLE);
                        videoplayer_og_rl.setVisibility(View.GONE);
                        cover_detailTV_og.setText(bean.getOriginalPost().getText());
                        cover_detailTV_og.setMaxLines(8);
                    }

                }

                if (reherelo_main_rl != null) {
                    reherelo_main_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });
                }
                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });
                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (profileIV_OG != null) {
                    profileIV_OG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }

                if (nameTV_OG != null) {
                    nameTV_OG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
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
                            mIntent.putExtra(Constant.INDEX, getAdapterPosition());
                            mIntent.putExtra(Constant.FROM_USER_PROFILE, true);
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
                        }
                    });
                }

            }

            @Nullable
            @Override
            public String getVideoId() {
                return "TEST: " + getAdapterPosition();
            }


            @Override
            public boolean wantsToPlay() {
                Rect childRect = new Rect();
                itemView.getGlobalVisibleRect(childRect, new Point());
                // wants to play if user could see at lease 0.75 of video
                return childRect.height() > mVideoView.getHeight() * 0.75
                        && childRect.width() > mVideoView.getWidth() * 0.75;
            }


            @Override
            public void onVideoPrepared(MediaPlayer mp) {
                super.onVideoPrepared(mp);
//                mInfo.setText("Prepared");
            }

            @Override
            public void onViewHolderBound() {
                super.onViewHolderBound();
                Picasso.with(itemView.getContext())
                        .load(R.drawable.post_img_default)
                        .fit()
                        .centerInside()
                        .into(thumb_iv);
//                mInfo.setText("Bound");
            }

            @Override
            public void onPlaybackStarted() {
                thumb_iv.animate().alpha(0.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onPlaybackStarted();
                    }
                }).start();
//                mInfo.setText("Started");
            }

            @Override
            public void onPlaybackProgress(int position, int duration) {
                super.onPlaybackProgress(position, duration);
//                mInfo.setText(Util.timeStamp(position, duration));
            }

            @Override
            public void onPlaybackPaused() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onPlaybackPaused();
                    }
                }).start();
//                mInfo.setText("Paused");
            }

            @Override
            public void onPlaybackStopped() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Completed");
            }

            @Override
            public void onPlaybackError(MediaPlayer mp, int what, int extra) {
                super.onPlaybackError(mp, what, extra);
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Error: videoId = " + getVideoId());
            }

            @Override
            protected boolean allowLongPressSupport() {
                return itemView != null;
            }

            @Override
            public String toString() {
                return "Video: " + getVideoId();
            }
        }


        public class CheckInFrames extends ViewHolder {

            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;


            LinearLayout comment_ll;

            private FrameLayout frame_container;
            private FrameLayout frame_container_reherelo;
            private RelativeLayout frame_rl;
            private RelativeLayout frame_rl_reherelo;

            TextView loc_name_tv;
            ImageView map_iv;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;


            public CheckInFrames(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                loc_name_tv = (TextView) convertView.findViewById(R.id.loc_name_tv);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                frame_rl = (RelativeLayout) convertView.findViewById(R.id.frame_rl);
                frame_container = (FrameLayout) convertView.findViewById(R.id.frame_container);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);

                map_iv = (ImageView) convertView.findViewById(R.id.map_iv);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;


                imageLoaderNew.loadImage(Constant.getMapUrlCheckInStatic(bean.getLat(), bean.getLng(), 500
                        , 1500, bean.getZoom_level()), optionsPostImg, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                        BitmapDrawable ob = new BitmapDrawable(getResources(), loadedImage);
                        map_iv.setImageBitmap(loadedImage);
                    }
                });
                map_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                        if (!mHasDoubleClicked) {


                            _lastPos = getAdapterPosition();
                        }
                    }
                });
                loc_name_tv.setText(bean.getLocation());
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }


                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });

                    /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);
                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(bean.getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }


                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
                mDataSourceFrames.openFrameDataBase();
                mFrameDetailsBeanArrayList = mDataSourceFrames.getFrameDetails(bean.getFrame_id());
                int deviceWidth1 = Constant.getDeviceWidth(getActivity());
                FrameParentBean mFrameParentBean1 = mDataSourceFrames.getSingleFrameParent(bean.getFrame_id());
                frameWidth = mFrameParentBean1.getFrame_width();
                frameHeight = mFrameParentBean1.getFrame_width();
//                        Logger.logsInfo(TAG, "Frame Height & Width before update : " + frameHeight + ":" + frameWidth);
                aspectFactor = (deviceWidth1 / frameWidth);
                frameWidth = frameWidth * aspectFactor;
                frameHeight = frameHeight * aspectFactor;
//                        Logger.logsInfo(TAG, "Frame Height & Width after update : " + frameHeight + ":" + frameWidth);
//                        Logger.logsInfo(TAG, "aspectFactor : " + aspectFactor);
                RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(deviceWidth1, (int) frameHeight);
                frame_container.setLayoutParams(lp1);

                frame_container.removeAllViews();

                for (int i = 0; i < mFrameDetailsBeanArrayList.size(); i++) {
                    FrameDetailsBean mFrameDetailsBean = mFrameDetailsBeanArrayList.get(i);
//            int demoColor= mColorDemo.get(i);
                    final ImageView img = new ImageView(getActivity());
                    final int finalI = i;
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Logger.logsInfo(TAG, "IMg Clicked : " + finalI);
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), finalI, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);

                    // Load image, decode it to Bitmap and return Bitmap to callback

                    if (bean.getPostMedia().size() > 0) {
//                                    Logger.logsInfo(MainViewScreen.class.getSimpleName(), "POst Id :  " + bean.getId());
                        try {
                            imageLoaderNew.displayImage(bean.getPostMedia().get(i).getUrl(), img, optionsPostImg);
                        } catch (Exception e) {

                        }

                    }

//            img.setBackgroundColor(demoColor);
//..load something inside the ImageView, we just set the background color
                    float viewHeight = (mFrameDetailsBean.getmImgHeight() * aspectFactor) - marginLayout;
                    float viewWidth = (mFrameDetailsBean.getmImgWidth() * aspectFactor) - marginLayout;
//                            Logger.logsInfo(TAG, "viewWidth : " +" " +i + " " +(int)viewWidth);
//                            Logger.logsInfo(TAG, "viewHeight : "+" " +i+" " +(int)viewHeight);
//                            Logger.logsInfo(TAG, "Frame Id : "+bean.getFrame_id());
//                            Logger.logsInfo(TAG, "Id : "+bean.getId());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                    float marginLeft = (mFrameDetailsBean.getmLeftCoordinate() * aspectFactor) + marginLayout;
                    float marginTop = (mFrameDetailsBean.getmTopCoordinate() * aspectFactor) + marginLayout;

//                            Logger.logsInfo(TAG, "marginLeft : " +" " +i+" " + marginLeft);
//                            Logger.logsInfo(TAG, "marginTop : " +" " +i+" " + marginTop);

                    params.leftMargin = (int) marginLeft;
                    params.topMargin = (int) marginTop;
//            params.rightMargin = (int)aspectFactor*5;
//            params.bottomMargin = (int)aspectFactor*5;


                    RelativeLayout mRelativeLayout = new RelativeLayout(getActivity());
                    mRelativeLayout.setPadding(0, 0, marginLayout, marginLayout);
                    mRelativeLayout.addView(img, params);
                    frame_container.addView(mRelativeLayout);
                    if (bean.getPostMedia().size() > mFrameDetailsBeanArrayList.size()
                            && i == (mFrameDetailsBeanArrayList.size() - 1)) {
//                                    Logger.logsInfo(MainViewScreen.class.getSimpleName(), "+ show : "+i);
                        final ImageView imgOverLay = new ImageView(getActivity());
                        final TextView tvOverlay = new TextView(getActivity());
                        int remainingImgCount = bean.getPostMedia().size() - mFrameDetailsBeanArrayList.size();
                        tvOverlay.setText("+" + remainingImgCount);
                        tvOverlay.setTextColor(Color.WHITE);
                        tvOverlay.setTextSize(30);
                        tvOverlay.setGravity(Gravity.CENTER);
                        imgOverLay.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgOverLay.setBackgroundColor(getActivity().getResources().getColor(R.color.black_overlay));
                        RelativeLayout mRelativeLayoutOverLay = new RelativeLayout(getActivity());
//                                    mRelativeLayoutOverLay.setGravity(Gravity.CENTER);
                        mRelativeLayoutOverLay.setPadding(0, 0, marginLayout, marginLayout);
//                                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        RelativeLayout.LayoutParams paramsSub = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                        RelativeLayout mSubRel = new RelativeLayout(getActivity());
                        paramsSub.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mSubRel.setGravity(Gravity.CENTER);
                        mSubRel.addView(imgOverLay, paramsSub);
                        mSubRel.addView(tvOverlay, paramsSub);
                        mRelativeLayoutOverLay.addView(mSubRel, params);

                        frame_container.addView(mRelativeLayoutOverLay);
                    }
                }

                if (frame_rl != null) {
                    frame_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });
                }
                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });
                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }
            }
        }

        public class FramePostType extends ViewHolder {
            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;

            private FrameLayout frame_container;
            private FrameLayout frame_container_reherelo;
            private RelativeLayout frame_rl;
            private RelativeLayout frame_rl_reherelo;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;


            public FramePostType(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                frame_rl = (RelativeLayout) convertView.findViewById(R.id.frame_rl);
                frame_container = (FrameLayout) convertView.findViewById(R.id.frame_container);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);


                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }

                    if (bean.getIs_following() == 1) {
                        onlieIV.setImageResource(R.drawable.main_right);
                    } else {
                        onlieIV.setImageResource(R.drawable.main_plus_icon);
                    }

                    onlieIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                                if (bean.getIs_following() == 1) {
                                    bean.setIs_following(0);
                                    followUnfollowAPIName = "unfollow";
                                } else {
                                    bean.setIs_following(1);
                                    followUnfollowAPIName = "follow";
                                }
                                notifyDataSetChanged();

                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("user_id", user_id + "")
                                        .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                                WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                        null, null, builder);
                                task.execute();


                            } else {
                                Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                            }

                        }
                    });
                }

                    /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);
                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(bean.getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }


                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
                mDataSourceFrames.openFrameDataBase();
                mFrameDetailsBeanArrayList = mDataSourceFrames.getFrameDetails(bean.getFrame_id());
                int deviceWidth = Constant.getDeviceWidth(getActivity());
                FrameParentBean mFrameParentBean = mDataSourceFrames.getSingleFrameParent(bean.getFrame_id());
                frameWidth = mFrameParentBean.getFrame_width();
                frameHeight = mFrameParentBean.getFrame_width();
//                        Logger.logsInfo(TAG, "Frame Height & Width before update : " + frameHeight + ":" + frameWidth);
                aspectFactor = (deviceWidth / frameWidth);
                frameWidth = frameWidth * aspectFactor;
                frameHeight = frameHeight * aspectFactor;
//                        Logger.logsInfo(TAG, "Frame Height & Width after update : " + frameHeight + ":" + frameWidth);
//                        Logger.logsInfo(TAG, "aspectFactor : " + aspectFactor);
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(deviceWidth, (int) frameHeight);
                frame_container.setLayoutParams(lp);

                frame_container.removeAllViews();

                for (int i = 0; i < mFrameDetailsBeanArrayList.size(); i++) {
                    FrameDetailsBean mFrameDetailsBean = mFrameDetailsBeanArrayList.get(i);
//            int demoColor= mColorDemo.get(i);
                    final MyImageView img = new MyImageView(getActivity());

                    final int finalI = i;
                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Logger.logsInfo(TAG, "IMg Clicked : " + finalI);
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), finalI, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
//                    img.setScaleType(ImageView.ScaleType.CENTER_CROP);


                    final float viewHeight = (mFrameDetailsBean.getmImgHeight() * aspectFactor) - marginLayout;
                    final float viewWidth = (mFrameDetailsBean.getmImgWidth() * aspectFactor) - marginLayout;


//                    img.setAdjustViewBounds(true);


//                    float finalX = bean.getPostMedia().get(i).getZ_x() * newAspectFactor;
//                    float finalY = bean.getPostMedia().get(i).getZ_y() * newAspectFactor;


                    // Load image, decode it to Bitmap and return Bitmap to callback

                    if (bean.getPostMedia().size() > 0) {

//                                    Logger.logsInfo(MainViewScreen.class.getSimpleName(), "POst Id :  " + bean.getId());
                        try {
//                            img.setZoom(bean.getPostMedia().get(i).getZoom_lvl());


/*
                            final Matrix matrix = img.getImageMatrix();



                            float scale;


                            if (bean.getPostMedia().get(i).getImg_width() * viewHeight > bean.getPostMedia().get(i).getImg_width()  * viewWidth) {
                                scale = (float) viewHeight / (float) bean.getPostMedia().get(i).getImg_height() ;
                            } else {
                                scale = (float) viewWidth / (float) bean.getPostMedia().get(i).getImg_width() ;
                            }
                            matrix.setScale(scale, scale);
                            img.setImageMatrix(matrix);*/
//                            img.setScaleType(ImageView.ScaleType.CENTER_CROP);


//                            Matrix mMatrix = img.getImageMatrix();
                            int imageWidth = bean.getPostMedia().get(finalI).getImg_width();
                            int imageHeight = bean.getPostMedia().get(finalI).getImg_height();


                            //  get a rectangle that is the size of the ImageView
//                            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);

//                            final float x= newAspectFactor * bean.getPostMedia().get(finalI).getZ_x();
//                            final float y=  newAspectFactor * bean.getPostMedia().get(finalI).getZ_y();
                            //src value / destination value.

                            float newAspectFactor = 0;


                            if (viewHeight > viewWidth) {
                                //Aspect factor for Height
                                newAspectFactor = viewHeight / bean.getPostMedia().get(i).getImg_height();

                            } else {
                                //Aspect factor for Width
                                newAspectFactor = viewWidth / bean.getPostMedia().get(i).getImg_width();
                            }

                            float imgNewHeight = newAspectFactor * imageHeight;
                            float imgNewWidth = newAspectFactor * imageWidth;
                            Logger.logsError(TAG, " Image New Height : " + imgNewHeight);
                            Logger.logsError(TAG, " Image New Width : " + imgNewWidth);

                            if (imgNewHeight < viewHeight) {
                                newAspectFactor = viewHeight / bean.getPostMedia().get(i).getImg_height();
                            } else if (imgNewWidth < viewWidth) {
                                newAspectFactor = viewWidth / bean.getPostMedia().get(i).getImg_width();
                            }
//                            new aspect  = 1115.5/600 ;
//                            new aspect  = 1.8583;
                            float x = bean.getPostMedia().get(finalI).getZ_x();
                            float y = bean.getPostMedia().get(finalI).getZ_y();


                            Matrix m = img.getImageMatrix();
             /*       RectF drawableRect = new RectF(bean.getPostMedia().get(i).getZ_x(),
                            bean.getPostMedia().get(i).getZ_y(),
                            bean.getPostMedia().get(i).getImg_width(),
                            bean.getPostMedia().get(i).getImg_height());
                    RectF viewRect = new RectF(0,
                            0,
                            viewWidth,
                            viewHeight);*/


                            if (viewHeight > viewWidth) {

                                if (imageHeight > imageWidth) {
                                    RectF drawableRect = new RectF(x,
                                            y,
                                            ((x * newAspectFactor) + viewWidth) / newAspectFactor,
                                            imageHeight);
                                    RectF viewRect = new RectF(0,
                                            0,
                                            viewWidth,
                                            viewHeight);


                                    m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

                                    Logger.logsError(TAG, finalI + " newAspectFactor : " + newAspectFactor);
                                    Logger.logsError(TAG, finalI + " Left  : " + x);
                                    Logger.logsError(TAG, finalI + " Top  : " + y);
                                    Logger.logsError(TAG, finalI + " Right  : " + ((x * newAspectFactor) + viewWidth) / newAspectFactor);
                                    Logger.logsError(TAG, finalI + " Bottom  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " OG Img height  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " OG Img Width  : " + imageWidth);
                                    Logger.logsError(TAG, finalI + " viewHeight By Shoeb New  : " + viewHeight);
                                    Logger.logsError(TAG, finalI + " viewWidth By Shoeb New  : " + viewWidth);
                                } else {
                                    RectF drawableRect = new RectF(x,
                                            y,
                                            ((x * newAspectFactor) + viewWidth) / newAspectFactor,
                                            imageHeight);
                                    RectF viewRect = new RectF(0,
                                            0,
                                            viewWidth,
                                            viewHeight);


                                    m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);

                                    Logger.logsError(TAG, finalI + " newAspectFactor : " + newAspectFactor);
                                    Logger.logsError(TAG, finalI + " Left  : " + x);
                                    Logger.logsError(TAG, finalI + " Top  : " + y);
                                    Logger.logsError(TAG, finalI + " Right  : " + ((x * newAspectFactor) + viewWidth) / newAspectFactor);
                                    Logger.logsError(TAG, finalI + " Bottom  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " OG Img height  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " OG Img Width  : " + imageWidth);
                                    Logger.logsError(TAG, finalI + " viewHeight By Shoeb New  : " + viewHeight);
                                    Logger.logsError(TAG, finalI + " viewWidth By Shoeb New  : " + viewWidth);
                                }

                            } else {

                                if (imageHeight > imageWidth) {
                                    RectF drawableRect = new RectF(x,
                                            y,
                                            imageWidth,
                                            ((y * newAspectFactor) + viewHeight) / newAspectFactor);
                                    RectF viewRect = new RectF(0,
                                            0,
                                            viewWidth,
                                            viewHeight);


                                    m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);


                                    Logger.logsError(TAG, finalI + " newAspectFactor : " + newAspectFactor);
                                    Logger.logsError(TAG, finalI + " Left  : " + x);
                                    Logger.logsError(TAG, finalI + " Top  : " + y);
                                    Logger.logsError(TAG, finalI + " Right  : " + imageWidth);
                                    Logger.logsError(TAG, finalI + " Bottom  : " + ((y * newAspectFactor) + viewHeight) / newAspectFactor);
                                    Logger.logsError(TAG, finalI + " OG Img height  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " OG Img Width  : " + imageWidth);
                                    Logger.logsError(TAG, finalI + " viewHeight By Shoeb New  : " + viewHeight);
                                    Logger.logsError(TAG, finalI + " viewWidth By Shoeb New  : " + viewWidth);
                                } else {
                                    RectF drawableRect = new RectF(x,
                                            y,
                                            ((x * newAspectFactor) + viewWidth) / newAspectFactor,
                                            ((y * newAspectFactor) + viewHeight) / newAspectFactor);
                                    RectF viewRect = new RectF(0,
                                            0,
                                            viewWidth,
                                            viewHeight);


                                    m.setRectToRect(drawableRect, viewRect, Matrix.ScaleToFit.FILL);


                                    Logger.logsError(TAG, finalI + " 2nd else newAspectFactor : " + newAspectFactor);
                                    Logger.logsError(TAG, finalI + " 2nd else Left  : " + x);
                                    Logger.logsError(TAG, finalI + " 2nd else Top  : " + y);
                                    Logger.logsError(TAG, finalI + " 2nd else Right  : " + ((x * newAspectFactor) + viewWidth) / newAspectFactor);
                                    Logger.logsError(TAG, finalI + " 2nd else Bottom  : " + ((y * newAspectFactor) + viewHeight) / newAspectFactor);
                                    Logger.logsError(TAG, finalI + " 2nd else OG Img height  : " + imageHeight);
                                    Logger.logsError(TAG, finalI + " 2nd else OG Img Width  : " + imageWidth);
                                    Logger.logsError(TAG, finalI + " 2nd else viewHeight By Shoeb New  : " + viewHeight);
                                    Logger.logsError(TAG, finalI + " 2nd else viewWidth By Shoeb New  : " + viewWidth);
                                }

                            }


                            img.setImageMatrix(m);
                            img.setScaleType(ImageView.ScaleType.MATRIX);
/*
                            PointF mPointFNew = img.tranformCoordTouchToBitmapByShoeb(viewWidth / 2, viewHeight / 2, true, imageHeight, imageWidth,x,y,
                                    imageWidth*newAspectFactor, imageHeight*newAspectFactor);
                            mPointFNew.x /= imageWidth;
                            mPointFNew.y /= imageHeight;
                            Logger.logsError(TAG, " ##### newAspectFactor : " + newAspectFactor);
                            Logger.logsError(TAG, " ##### OG X : " + x);
                            Logger.logsError(TAG, " ##### OG Y : " + y);
                            Logger.logsError(TAG, " ##### mPointFNew.x: " + mPointFNew.x);
                            Logger.logsError(TAG, " ##### mPointFNew.y: " + mPointFNew.y);*/
//                            x = x*newAspectFactor;
//                            y = y*newAspectFactor;

                            final float reciprocal = 1 / newAspectFactor;
                            final float rightImgCoord = (reciprocal * viewWidth);
                            Logger.logsError(TAG, "rightImgCoord  : " + rightImgCoord);

                            // get a rectangle that is the size of the bitmap
//                            final float rightImgCoord = (reciprocal * viewWidth) * newAspectFactor;

                            final float bottomImgCoord = bean.getPostMedia().get(finalI).getImg_height();
//                            img.setScaleType(ImageView.ScaleType.CENTER_CROP);

/*


                            Matrix matrix = new Matrix();

//                            matrix.setScale(viewWidth/imageWidth, viewHeight/imageHeight);

                            //x = newAspectFactor*x;
                            //y = newAspectFactor*y;

                            if (x!=0){
                                x = -x;
                            }

                            if (y!=0){
                                y = -y;
                            }
                            Logger.logsError(TAG, " #### newAspectFactor : " + newAspectFactor);
                            Logger.logsError(TAG, " #### xOffset : " + x);
                            Logger.logsError(TAG, " #### yOffset : " + y);

                            matrix.setTranslate(x, y);
                            img.setImageMatrix(matrix);*/

                          /*  RectF mRectImg = new RectF(x, y, 100, 100);
                            RectF mRectView = new RectF(0, 0, viewWidth, viewHeight);
                            Matrix m = img.getImageMatrix();
                            m.setRectToRect(mRectImg, mRectView, Matrix.ScaleToFit.FILL);
                            img.setImageMatrix(m);*/

//                            Logger.logsError(TAG,"#####");
//                            PointF point = img.tranformCoordTouchToBitmapByShoeb(x, y, true, viewHeight, viewWidth);
//                            PointF mPointF = img.getScrollPositionByShoeb(imageHeight, imageWidth, x,y);
                            Logger.logsError(TAG, "##### point X New : " + x);
                            Logger.logsError(TAG, "##### point Y New : " + y);

                            imageLoaderNew.displayImage(bean.getPostMedia().get(i).getUrl(), img, optionsPostImg);
                            /*imageLoaderNew.loadImage(bean.getPostMedia().get(i).getUrl(), optionsPostImg, new SimpleImageLoadingListener() {
                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    super.onLoadingComplete(imageUri, view, loadedImage);
                                    BitmapDrawable ob = new BitmapDrawable(loadedImage);
                                    img.setBackground(ob);
                                }
                            });*/
//                            Logger.logsError(TAG, "##### mPointF X Scroll POs: " + mPointF.x);
//                            Logger.logsError(TAG, "##### mPointF Y Scroll POs: " + mPointF.y);
//                            Logger.logsError(TAG, "##### (float)0.38551402 : " + (float) 0.38551402);


                            /*Code at 25 morning thinking sharp.... hmmmmmm...*/
                          /*  float fcx = imageWidth / 2;
                            float fcy = imageHeight / 2;

                            Logger.logsError(TAG, "##### Frame  Width  : " + viewWidth);
                            Logger.logsError(TAG, "##### Frame  Height  : " + viewHeight);
                            Logger.logsError(TAG, "##### Frame Centre Width  : " + fcx);
                            Logger.logsError(TAG, "##### Frame Centre Height  : " + fcy);

                            float fcix = (fcx);
                            float fciy =(fcy);

                            Logger.logsError(TAG, "##### Frame Centre Width With Image : " + fcix);
                            Logger.logsError(TAG, "##### Frame Centre Height With Image : " + fciy);

                            float fcixPoint = ((fcix / imageWidth) * 100) / 100;
                            float fciyPoint = ((fciy / imageHeight) * 100) / 100 ;

                            Logger.logsError(TAG, "##### Frame Centre Width Image Scale : " + fcixPoint);
                            Logger.logsError(TAG, "##### Frame Centre Height Image Scale : " + fciyPoint);

                          *//*  img.setZoom(bean.getPostMedia().get(i).getZoom_lvl(),
                                    (float) 0.38551402,
                                    (float) 0.5,
                                    ImageView.ScaleType.CENTER_CROP);*//*

                            if (finalI==0){
                                img.setZoom(bean.getPostMedia().get(i).getZoom_lvl(),
                                        (float) 0.38187954,
                                        (float) 0.5,
                                        ImageView.ScaleType.CENTER_CROP);
                            }else{
                                img.setZoom(bean.getPostMedia().get(i).getZoom_lvl(),
                                        (float) 0.66095537,
                                        (float) 0.5,
                                        ImageView.ScaleType.CENTER_CROP);
                            }
*/


/*
                            ImageSize targetSize = new ImageSize(bean.getPostMedia().get(finalI).getImg_width(), bean.getPostMedia().get(finalI).getImg_height());
                            Bitmap bmp = imageLoaderNew.loadImageSync(bean.getPostMedia().get(i).getUrl(), targetSize);
                            Logger.logsError(TAG, "##### bmp Bitmap Height  : " + bmp.getHeight());
                            Logger.logsError(TAG, "##### bmp Bitmap Width  : " + bmp.getWidth());*/
                            /*imageLoaderNew.loadImage(bean.getPostMedia().get(i).getUrl(), optionsPostImg,
                                    new SimpleImageLoadingListener() {
                                        @Override
                                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                            // Do whatever you want with Bitmap
                                            *//*Logger.logsError(TAG, "##### New Aspect  : " + newAspectFactor);
                                            Logger.logsError(TAG, "##### reciprocal  : " + reciprocal);
                                            Logger.logsError(TAG, "##### OG Img Height  : " + bean.getPostMedia().get(finalI).getImg_height());
                                            Logger.logsError(TAG, "##### OG Img Width  : " + bean.getPostMedia().get(finalI).getImg_width());
                                            Logger.logsError(TAG, "##### Loaded Bitmap Height  : " + loadedImage.getHeight());
                                            Logger.logsError(TAG, "##### Loaded Bitmap Width  : " + loadedImage.getWidth());
                                            Logger.logsError(TAG, "##### rightImgCoord  : " + rightImgCoord);
                                            Logger.logsError(TAG, "##### bottomImgCoord  : " + bottomImgCoord);

                                            Logger.logsError(TAG, "##### Loaded To X  : " + x);
                                            Logger.logsError(TAG, "##### Loaded To Y  : " + y);
                                            Logger.logsError(TAG, "##### View Height : " + viewHeight);
                                            Logger.logsError(TAG, "##### View Width : " + viewWidth);

                                            float newAspectBitmapHeight =( float)(( float)loadedImage.getHeight()/( float)bean.getPostMedia().get(finalI).getImg_height());
                                            float newAspectBitmapWidth =( float)(( float)loadedImage.getWidth()/( float)bean.getPostMedia().get(finalI).getImg_width());
                                            Logger.logsError(TAG, "##### newAspectBitmapheight : " + newAspectBitmapHeight);
                                            Logger.logsError(TAG, "##### newAspectBitmapWidth : " + newAspectBitmapWidth);
                                            float newRightImgCoor = rightImgCoord * newAspectBitmapWidth;
                                            float newBottomImgCoor = bottomImgCoord * newAspectBitmapHeight;
                                            Logger.logsError(TAG, "##### newRightImgCoor : " + newRightImgCoor);
                                            Logger.logsError(TAG, "##### newBottomImgCoor : " + newBottomImgCoor);
                                            Bitmap drawingSurface = Bitmap.createBitmap(loadedImage, (int) x,
                                                    (int) y, (int) newRightImgCoor, (int) newBottomImgCoor);
*//*
                                            BitmapDrawable ob = new BitmapDrawable(loadedImage);
                                            img.setBackground(ob);

                                            img.setScrollX((int)x);
                                            img.setScrollY((int) y);

//                                    img.setImageBitmap(drawingSurface);
//                                            Logger.logsError(TAG, "##### drawingSurface Height  : " + drawingSurface.getHeight());
//                                            Logger.logsError(TAG, "##### drawingSurface Width  : " + drawingSurface.getWidth());
                                            Logger.logsError(TAG, "####################################");
//                                    Canvas canvas = new Canvas(drawingSurface);
                                    *//*Bitmap drawingSurface = Bitmap.createBitmap(loadedImage.getWidth(), loadedImage.getHeight(), Bitmap.Config.ARGB_4444);

                                    Canvas canvas = new Canvas(drawingSurface);
                                    canvas.drawBitmap(loadedImage, bean.getPostMedia().get(finalI).getZ_x(), bean.getPostMedia().get(finalI).getZ_y(), null);
                                    Logger.logsError(TAG, "topLeft bean.getPostMedia().get(finalI1).getZ_x() : finalI1 : " + finalI + " " + bean.getPostMedia().get(finalI).getZ_x());
                                    Logger.logsError(TAG, "topLeft bean.getPostMedia().get(finalI1).getZ_y() : finalI1 " + finalI + " " + bean.getPostMedia().get(finalI).getZ_y());
*//*
//                                    canvas.drawText("Hi!", 10, 10, new Paint());

//
                                            //     image.setImageBitmap(bmp);

//                                    img.setImageBitmap(loadedImage);


                                        }
                                    });*/


                        } catch (Exception e) {

                        }

                    }


//            img.setBackgroundColor(demoColor);
//..load something inside the ImageView, we just set the background color
//                            Logger.logsInfo(TAG, "viewWidth : " +" " +i + " " +(int)viewWidth);
//                            Logger.logsInfo(TAG, "viewHeight : "+" " +i+" " +(int)viewHeight);
//                            Logger.logsInfo(TAG, "Frame Id : "+bean.getFrame_id());
//                            Logger.logsInfo(TAG, "Id : "+bean.getId());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                    float marginLeft = (mFrameDetailsBean.getmLeftCoordinate() * aspectFactor) + marginLayout;
                    float marginTop = (mFrameDetailsBean.getmTopCoordinate() * aspectFactor) + marginLayout;

//                            Logger.logsInfo(TAG, "marginLeft : " +" " +i+" " + marginLeft);
//                            Logger.logsInfo(TAG, "marginTop : " +" " +i+" " + marginTop);

                    params.leftMargin = (int) marginLeft;
                    params.topMargin = (int) marginTop;
//            params.rightMargin = (int)aspectFactor*5;
//            params.bottomMargin = (int)aspectFactor*5;


                    RelativeLayout mRelativeLayout = new RelativeLayout(getActivity());
                    mRelativeLayout.setPadding(0, 0, marginLayout, marginLayout);
                    mRelativeLayout.addView(img, params);
                    frame_container.addView(mRelativeLayout);
                    if (bean.getPostMedia().size() > mFrameDetailsBeanArrayList.size()
                            && i == (mFrameDetailsBeanArrayList.size() - 1)) {
//                                    Logger.logsInfo(MainViewScreen.class.getSimpleName(), "+ show : "+i);
                        final ImageView imgOverLay = new ImageView(getActivity());
                        final TextView tvOverlay = new TextView(getActivity());
                        int remainingImgCount = bean.getPostMedia().size() - mFrameDetailsBeanArrayList.size();
                        tvOverlay.setText("+" + remainingImgCount);
                        tvOverlay.setTextColor(Color.WHITE);
                        tvOverlay.setTextSize(30);
                        tvOverlay.setGravity(Gravity.CENTER);
                        imgOverLay.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        imgOverLay.setBackgroundColor(getActivity().getResources().getColor(R.color.black_overlay));
                        RelativeLayout mRelativeLayoutOverLay = new RelativeLayout(getActivity());
//                                    mRelativeLayoutOverLay.setGravity(Gravity.CENTER);
                        mRelativeLayoutOverLay.setPadding(0, 0, marginLayout, marginLayout);
//                                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                        RelativeLayout.LayoutParams paramsSub = new RelativeLayout.LayoutParams((int) viewWidth, (int) viewHeight);
                        RelativeLayout mSubRel = new RelativeLayout(getActivity());
                        paramsSub.addRule(RelativeLayout.CENTER_IN_PARENT);
                        mSubRel.setGravity(Gravity.CENTER);
                        mSubRel.addView(imgOverLay, paramsSub);
                        mSubRel.addView(tvOverlay, paramsSub);
                        mRelativeLayoutOverLay.addView(mSubRel, params);

                        frame_container.addView(mRelativeLayoutOverLay);
                    }
                }

                if (frame_rl != null) {
                    frame_rl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }

            }
        }

        public class SimpleToroVideoViewHolder extends ToroVideoViewHolder {


            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;


            TextureVideoView mTextureVideoView;
            ImageView imvPreview;
            ImageView thumb_iv;
            ImageView imvPlay;
            ProgressBar pbWaiting;
            ProgressBar pbProgressBar;
            RelativeLayout fullscreen_rl;
            ImageView details_iv;
            ImageView volume_iv;
            RelativeLayout vid_rl;
            Runnable mRunnable;
            Handler mHandler = new Handler();
            private static final int VIDEO_BOTTOM_VIEW_TIME = 4;

            private final String TAG = getClass().getSimpleName();


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;

            public SimpleToroVideoViewHolder(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                vid_rl = (RelativeLayout) convertView.findViewById(R.id.vid_rl);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                fullscreen_rl = (RelativeLayout) convertView.findViewById(R.id.fullscreen_rl);
                details_iv = (ImageView) convertView.findViewById(R.id.details_iv);
                volume_iv = (ImageView) convertView.findViewById(R.id.volume_iv);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
//                        video_view = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);


                mTextureVideoView = (TextureVideoView) convertView.findViewById(R.id.textureview);

                imvPreview = (ImageView) convertView.findViewById(R.id.imv_preview);
                thumb_iv = (ImageView) convertView.findViewById(R.id.thumb_iv);
                imvPlay = (ImageView) convertView.findViewById(R.id.imv_video_play);
                pbWaiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
                pbProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_progressbar);


                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
            }

            @Override
            protected ToroVideoView findVideoView(View itemView) {
                return (ToroVideoView) itemView.findViewById(R.id.video);
            }


            @Nullable
            @Override
            public String getVideoId() {
                return "TEST: " + getAdapterPosition();
            }

            private DatumHome bean;

            @Override
            public void bind(@Nullable Object item) {
                if (!(item instanceof DatumHome)) {
                    throw new IllegalStateException("Unexpected object: " + item.toString());
                }

                bean = (DatumHome) item;
                if (bean.getPostMedia().size() > 0) {
                    mVideoView.setVideoURI(Uri.parse(bean.getPostMedia().get(0).getUrl()), bean.getPostMedia().get(0).getSlow_motion());
                }

                if (bean.isVolumeOn()) {
                    volume_iv.setImageResource(R.drawable.volume_on);
                } else {
                    volume_iv.setImageResource(R.drawable.volume_off);
                }
                volume_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
                    }
                });

                mRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        fullscreen_rl.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }
                };
                vid_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, false);


                        if (!mHasDoubleClicked) {
                            _lastPos = getAdapterPosition();

                        }


                        if (fullscreen_rl.getVisibility() == View.VISIBLE) {
                            fullscreen_rl.setVisibility(View.GONE);

                        } else {
                            fullscreen_rl.setVisibility(View.VISIBLE);


                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, VIDEO_BOTTOM_VIEW_TIME * 1000); // VIDEO_BOTTOM_VIEW_TIME seconds
                        }
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
//                        //Constant.showToast("Video Clicked called.", getActivity());
                    }
                });
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }

                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });
               /* if (thumb_iv != null) {
                    if (bean.getPostMedia().size() > 0) {
                        final VideoPostOnly tempHolder = (VideoPostOnly) holder;
                        imageLoaderNew.loadImage(bean.getPostMedia().get(0).getThumb_url(), optionsPostImg, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                tempHolder.thumb_iv.setImageBitmap(loadedImage);
                            }
                        });
                    }

                }*/

                if (details_iv != null) {
                    details_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });
                }
                    /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);
                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(bean.getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }

                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);

                /*if (mTextureVideoView != null) {
                    final VideoPostOnly final_viewHolder = ((VideoPostOnly) holder);
                    mTextureVideoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                    *//*Code of click*//*
                            findDoubleClick(getAdapterPosition(), 0, true, final_viewHolder, null, null, v);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }

                            if (lastPlayVideoHolder == null) {
                                lastPlayVideoHolder = final_viewHolder;
                            } else {
                                if (!final_viewHolder.equals(lastPlayVideoHolder)) {
                                    lastPlayVideoHolder.mTextureVideoView.stop();
                                    lastPlayVideoHolder.pbWaiting.setVisibility(View.GONE);
                                    lastPlayVideoHolder.imvPlay.setVisibility(View.VISIBLE);
                                    lastPlayVideoHolder = final_viewHolder;
                                }
                            }
                                *//*TextureVideoView textureView = (TextureVideoView) v;
                                if (textureView.getState() == TextureVideoView.MediaState.INIT || textureView.getState() == TextureVideoView.MediaState.RELEASE) {

                                    textureView.play(bean.getPostMedia().get(0).getUrl());
                                    final_viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                                    final_viewHolder.imvPlay.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PAUSE) {
                                    textureView.start();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.GONE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PLAYING) {
                                    textureView.pause();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                                } else if (textureView.getState() == TextureVideoView.MediaState.PREPARING) {
                                    textureView.stop();
                                    final_viewHolder.pbWaiting.setVisibility(View.GONE);
                                    final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                                }*//*
                        }
                    });

                    mTextureVideoView.setOnStateChangeListener(new TextureVideoView.OnStateChangeListener() {
                        @Override
                        public void onSurfaceTextureDestroyed(SurfaceTexture surface) {
                            final_viewHolder.mTextureVideoView.stop();
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPlaying() {
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBuffering() {
                            final_viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSeek(int max, int progress) {
                            final_viewHolder.imvPreview.setVisibility(View.GONE);
                            final_viewHolder.pbProgressBar.setMax(max);
                            final_viewHolder.pbProgressBar.setProgress(progress);
                        }

                        @Override
                        public void onStop() {
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPause() {
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onTextureViewAvaliable() {

                        }

                        @Override
                        public void playFinish() {
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPrepare() {

                        }
                    });
                }*/
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }

            }


            @Override
            public boolean wantsToPlay() {
                Rect childRect = new Rect();
                itemView.getGlobalVisibleRect(childRect, new Point());
                // wants to play if user could see at lease 0.75 of video
                return childRect.height() > mVideoView.getHeight() * 0.75
                        && childRect.width() > mVideoView.getWidth() * 0.75;
            }


            @Override
            public void onVideoPrepared(MediaPlayer mp) {
                super.onVideoPrepared(mp);
//                mInfo.setText("Prepared");
            }

            @Override
            public void onViewHolderBound() {
                super.onViewHolderBound();
                Picasso.with(itemView.getContext())
                        .load(R.drawable.post_img_default)
                        .fit()
                        .centerInside()
                        .into(thumb_iv);
//                mInfo.setText("Bound");
            }

            @Override
            public void onPlaybackStarted() {
                thumb_iv.animate().alpha(0.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SimpleToroVideoViewHolder.super.onPlaybackStarted();
                    }
                }).start();
//                mInfo.setText("Started");
            }

            @Override
            public void onPlaybackProgress(int position, int duration) {
                super.onPlaybackProgress(position, duration);
//                mInfo.setText(Util.timeStamp(position, duration));
            }

            @Override
            public void onPlaybackPaused() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SimpleToroVideoViewHolder.super.onPlaybackPaused();
                    }
                }).start();
//                mInfo.setText("Paused");
            }

            @Override
            public void onPlaybackStopped() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SimpleToroVideoViewHolder.super.onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Completed");
            }

            @Override
            public void onPlaybackError(MediaPlayer mp, int what, int extra) {
                super.onPlaybackError(mp, what, extra);
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        SimpleToroVideoViewHolder.super.onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Error: videoId = " + getVideoId());
            }

            @Override
            protected boolean allowLongPressSupport() {
                return itemView != null;
            }

            @Override
            public String toString() {
                return "Video: " + getVideoId();
            }
        }

        private void updatePlaceView(float xCordinate, float yCordinate, final TagedUserList bean, RelativeLayout mLayout) {

            try {

                TextView textView = new TextView(getActivity());
                textView.setText(bean.getUsername());
                textView.setPadding(3, 10, 3, 3);
                textView.setGravity(Gravity.CENTER);
                textView.setBackgroundResource(R.drawable.tag);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                        mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                        mIntent.putExtra(Constant.other_user_id, bean.getUser_id());
                        startActivity(mIntent);
                        return false;
                    }
                });

                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                textView.measure(0, 0);       //must call measure!
                textView.getMeasuredHeight(); //get height
                textView.getMeasuredWidth();  //get width

                Logger.logsInfo(TAG, "textView Height : " + textView.getMeasuredHeight());

                int textViewWidth = textView.getMeasuredWidth();
                Logger.logsInfo(TAG, "textView Width : " + textViewWidth);

                params.leftMargin = (int) xCordinate - (textViewWidth / 2);
                params.topMargin = (int) yCordinate;


//            _tagLayout.removeView(textView);
//            _tagLayout.removeViewInLayout(textView);

                mLayout.addView(textView, params);


            } catch (Exception ex) {
                ex.printStackTrace();

            }

        }

        public class ImageTextType extends ViewHolder {
            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            public ImageView onlieIV;
            ImageView coverIV;
            ImageView tag_iv;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;
            LinearLayout parent_container;
            RelativeLayout img_rl;

            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;

            public ImageTextType(View convertView) {
                super(convertView);

                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
                coverIV = (ImageView) convertView.findViewById(R.id.coverIV);
                tag_iv = (ImageView) convertView.findViewById(R.id.tag_iv);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);


                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);
                parent_container = (LinearLayout) convertView.findViewById(R.id.parent_container);
                img_rl = (RelativeLayout) convertView.findViewById(R.id.img_rl);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;

                Logger.logsError(TAG, "Tag user size " + bean.getTaged_user_list().size());
                if (bean.getTaged_user_list().size() > 0) {

                    tag_iv.setVisibility(View.VISIBLE);
                    tag_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (bean.isTagShow()) {
                                bean.setIsTagShow(false);

                                for (int i = 0; i < bean.getTaged_user_list().get(0).size(); i++) {
                                    img_rl.removeViewAt(4);
                                }
                            } else {
                                bean.setIsTagShow(true);
                                for (int i = 0; i < bean.getTaged_user_list().get(0).size(); i++) {
                                    TagedUserList mBean = bean.getTaged_user_list().get(0).get(i);
                                    int heightOfView = 0;
                                    int widthOfView = 0;
                                    if (bean.getPostMedia().size() > 0) {
                                        heightOfView = bean.getPostMedia().get(0).getImg_height();
                                        widthOfView = bean.getPostMedia().get(0).getImg_width();
                                    }

                                    int heightOfCurrentView = img_rl.getHeight();
                                    int widthOfCurrentView = img_rl.getWidth();

                                    float aspectFactorWidth = ((float) widthOfCurrentView / (float) widthOfView);
                                    float aspectFactorHeight = ((float) heightOfCurrentView / (float) heightOfView);

                                    float x = mBean.getX() * aspectFactorWidth;
                                    float y = mBean.getY() * aspectFactorHeight;

                                    updatePlaceView(x, y, mBean, img_rl);
                                }
                            }

                            notifyDataSetChanged();

                        }
                    });
                } else {
                    tag_iv.setVisibility(View.GONE);
                }
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                      /*  Spannable s = (Spannable) comment_first.getText();
                        int start = first.length();
                        int end = start + next.length();

                        s.setSpan(new ForegroundColorSpan(Color.GREEN), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
*/
                   /*     HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });*/

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);

                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);


                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
/*
                                if (anim != null && !anim.isRunning()){
                                    anim.start();
                                }*/

                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
                                int is_like_status = bean.getIs_like_status();

                                Constant.heart(circleBackground, heartImageView);

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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
                                int is_dislike_status = bean.getIs_dislike_status();

                                Constant.dislikeAnimation(circleBackground, heartImageView);

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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(bean.getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }
                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);
                cover_detailTV.setMaxLines(4);
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }

                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }

                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });

                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }

                if (!bean.getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(bean.getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }

            }
        }

        public class CheckInVideoPostType extends ToroVideoViewHolder {


            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;


            TextureVideoView mTextureVideoView;
            ImageView imvPreview;
            ImageView thumb_iv;
            ImageView imvPlay;
            ProgressBar pbWaiting;
            ProgressBar pbProgressBar;
            RelativeLayout fullscreen_rl;
            RelativeLayout vid_rl;


            TextView loc_name_tv;
            ImageView map_iv;
            ImageView volume_iv;
            ImageView details_iv;


            Runnable mRunnable;
            Handler mHandler = new Handler();
            private static final int VIDEO_BOTTOM_VIEW_TIME = 4;

            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;

            public CheckInVideoPostType(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                loc_name_tv = (TextView) convertView.findViewById(R.id.loc_name_tv);
                map_iv = (ImageView) convertView.findViewById(R.id.map_iv);
                details_iv = (ImageView) convertView.findViewById(R.id.details_iv);
                volume_iv = (ImageView) convertView.findViewById(R.id.volume_iv);
                fullscreen_rl = (RelativeLayout) convertView.findViewById(R.id.fullscreen_rl);
                vid_rl = (RelativeLayout) convertView.findViewById(R.id.vid_rl);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
//                        video_view = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);


                mTextureVideoView = (TextureVideoView) convertView.findViewById(R.id.textureview);

                imvPreview = (ImageView) convertView.findViewById(R.id.imv_preview);
                thumb_iv = (ImageView) convertView.findViewById(R.id.thumb_iv);
                imvPlay = (ImageView) convertView.findViewById(R.id.imv_video_play);
                pbWaiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
                pbProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_progressbar);


                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
            }

            @Override
            protected ToroVideoView findVideoView(View itemView) {
                return (ToroVideoView) itemView.findViewById(R.id.video);
            }

            @Override
            public void bind(@Nullable Object object) {
                final DatumHome bean = (DatumHome) object;
                if (bean.isVolumeOn()) {
                    volume_iv.setImageResource(R.drawable.volume_on);
                } else {
                    volume_iv.setImageResource(R.drawable.volume_off);
                }
                volume_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
                    }
                });

                mRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        fullscreen_rl.setVisibility(View.GONE); //This will remove the View. and free s the space occupied by the View
                    }
                };
                vid_rl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, false);


                        if (!mHasDoubleClicked) {
                            _lastPos = getAdapterPosition();

                        }
                        if (fullscreen_rl.getVisibility() == View.VISIBLE) {
                            fullscreen_rl.setVisibility(View.GONE);

                        } else {
                            fullscreen_rl.setVisibility(View.VISIBLE);


                            mHandler.removeCallbacks(mRunnable);
                            mHandler.postDelayed(mRunnable, VIDEO_BOTTOM_VIEW_TIME * 1000); // VIDEO_BOTTOM_VIEW_TIME seconds
                        }
                        if (bean.isVolumeOn()) {
                            bean.setIsVolumeOn(false);
                            mVideoView.setVolumeOFFMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_off);
                        } else {
                            bean.setIsVolumeOn(true);
                            mVideoView.setVolumeONMediaPlayer();
                            volume_iv.setImageResource(R.drawable.volume_on);
                        }
                        //Constant.showToast("Video Clicked called.", getActivity());
                    }
                });
                if (bean.getPostMedia().size() > 0) {
                    mVideoView.setVideoURI(Uri.parse(bean.getPostMedia().get(0).getUrl()), bean.getPostMedia().get(0).getSlow_motion());
                }

                map_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                        findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                        if (!mHasDoubleClicked) {


                            _lastPos = getAdapterPosition();
                        }
                    }
                });
                loc_name_tv.setText(bean.getLocation());


                imageLoaderNew.loadImage(Constant.getMapUrlCheckInStatic(bean.getLat(), bean.getLng(), 500
                        , 1500, bean.getZoom_level()), optionsPostImg, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
//                            BitmapDrawable ob = new BitmapDrawable(getResources(), loadedImage);
                        map_iv.setImageBitmap(loadedImage);
                    }
                });
                if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);

                    }
                }

                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }

                if (bean.getIs_following() == 1) {
                    onlieIV.setImageResource(R.drawable.main_right);
                } else {
                    onlieIV.setImageResource(R.drawable.main_plus_icon);
                }
                onlieIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                            if (bean.getIs_following() == 1) {
                                bean.setIs_following(0);
                                followUnfollowAPIName = "unfollow";
                            } else {
                                bean.setIs_following(1);
                                followUnfollowAPIName = "follow";
                            }
                            notifyDataSetChanged();

                            Uri.Builder builder = new Uri.Builder()
                                    .appendQueryParameter("user_id", user_id + "")
                                    .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                            WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                    null, null, builder);
                            task.execute();


                        } else {
                            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                        }

                    }
                });

                /*if (thumb_iv != null) {
                    if (bean.getPostMedia().size() > 0) {
                        final CheckInVideoPostType tempHolder = (CheckInVideoPostType) holder;
                        imageLoaderNew.loadImage(bean.getPostMedia().get(0).getThumb_url(), optionsPostImg, new SimpleImageLoadingListener() {
                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                super.onLoadingComplete(imageUri, view, loadedImage);
                                tempHolder.thumb_iv.setImageBitmap(loadedImage);
                            }
                        });
                    }

                }*/

                if (details_iv != null) {
                    details_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                        }
                    });
                }
                    /*Populating Comments with username*/
                if (bean.getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (bean.getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (bean.getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (bean.getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = bean.getPostComments().get(0).getUsername() + ": ";
                    String next = bean.getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(((ImageTextType) holder).comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(((ImageTextType) holder).comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        ((ImageTextType) holder).comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = bean.getPostComments().get(1).getUsername() + ": ";
                    String next2 = bean.getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = bean.getPostComments().get(2).getUsername() + ": ";
                    String next3 = bean.getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = bean.getPostComments().get(3).getUsername() + ": ";
                    String next4 = bean.getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        ((ImageTextType) holder).comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);
                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(bean);
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (mHasDoubleClicked) {


                            } else {

                                _lastPos = getAdapterPosition();
//                        Logger.logsInfo("============>Single click", "Single click");
                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
/*
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", bean.getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (bean.getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", bean.getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (bean.getIs_like_status() == 1) {
                /*Liked*/
//                    _viewHolder.smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    _viewHolder.smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
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
                                        bean.setIs_dislike_status(0);
                                    }


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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(bean.getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(bean.getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (bean.getTotalComments() != null) {
                        if (bean.getTotalComments() > 0) {
                            commentTV.setText(bean.getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (bean.getReherelo() > 0) {
                        rehereLoCount.setText(bean.getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (bean.getLike() > 0) {
                        _smilyTV.setText(bean.getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (bean.getDislike() > 0) {
                        sad_tv.setText(bean.getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (bean.getLikedUser() != null) {
                        int likedUsersSize = bean.getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (bean.getLikedUser().get(i) != null) {
                                String name = bean.getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
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
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (bean.getLikedUser() != null) {
                    if (bean.getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(bean.getUser().getUsername());
                }

                if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(getResources().getString(R.string.checkedInAtText));
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }

                imageLoaderNew.displayImage(bean.getUser().getImage(), profileIV, optionsProfileImg);

                /*if (mTextureVideoView != null) {
                    final CheckInVideoPostType final_viewHolder = ((CheckInVideoPostType) holder);
                    mTextureVideoView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                    *//*Code of click*//*
                            findDoubleClick(getAdapterPosition(), 0, true, null, null, final_viewHolder, v);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();
                            }

                            if (lastPlayVideoHolder == null) {
                                mCheckInVideoPostType = final_viewHolder;
                            } else {
                                if (!final_viewHolder.equals(lastPlayVideoHolder)) {
                                    lastPlayVideoHolder.mTextureVideoView.stop();
                                    lastPlayVideoHolder.pbWaiting.setVisibility(View.GONE);
                                    lastPlayVideoHolder.imvPlay.setVisibility(View.VISIBLE);
                                    mCheckInVideoPostType = final_viewHolder;
                                }
                            }

                        }
                    });

                    mTextureVideoView.setOnStateChangeListener(new TextureVideoView.OnStateChangeListener() {
                        @Override
                        public void onSurfaceTextureDestroyed(SurfaceTexture surface) {
                            final_viewHolder.mTextureVideoView.stop();
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPlaying() {
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBuffering() {
                            final_viewHolder.pbWaiting.setVisibility(View.VISIBLE);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSeek(int max, int progress) {
                            final_viewHolder.imvPreview.setVisibility(View.GONE);
                            final_viewHolder.pbProgressBar.setMax(max);
                            final_viewHolder.pbProgressBar.setProgress(progress);
                        }

                        @Override
                        public void onStop() {
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPause() {
                            final_viewHolder.pbWaiting.setVisibility(View.GONE);
                            final_viewHolder.imvPlay.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onTextureViewAvaliable() {

                        }

                        @Override
                        public void playFinish() {
                            final_viewHolder.pbProgressBar.setMax(1);
                            final_viewHolder.pbProgressBar.setProgress(0);
                            final_viewHolder.imvPlay.setVisibility(View.GONE);
                            final_viewHolder.imvPreview.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onPrepare() {

                        }
                    });
                }*/
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }
            }

            @Nullable
            @Override
            public String getVideoId() {
                return "TEST: " + getAdapterPosition();
            }

            @Override
            public boolean wantsToPlay() {
                Rect childRect = new Rect();
                itemView.getGlobalVisibleRect(childRect, new Point());
                // wants to play if user could see at lease 0.75 of video
                return childRect.height() > mVideoView.getHeight() * 0.75
                        && childRect.width() > mVideoView.getWidth() * 0.75;
            }


            @Override
            public void onVideoPrepared(MediaPlayer mp) {
                super.onVideoPrepared(mp);
//                mInfo.setText("Prepared");
            }

            @Override
            public void onViewHolderBound() {
                super.onViewHolderBound();
                Picasso.with(itemView.getContext())
                        .load(R.drawable.post_img_default)
                        .fit()
                        .centerInside()
                        .into(thumb_iv);
//                mInfo.setText("Bound");
            }

            @Override
            public void onPlaybackStarted() {
                thumb_iv.animate().alpha(0.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CheckInVideoPostType.super.onPlaybackStarted();
                    }
                }).start();
//                mInfo.setText("Started");
            }

            @Override
            public void onPlaybackProgress(int position, int duration) {
                super.onPlaybackProgress(position, duration);
//                mInfo.setText(Util.timeStamp(position, duration));
            }

            @Override
            public void onPlaybackPaused() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CheckInVideoPostType.super.onPlaybackPaused();
                    }
                }).start();
//                mInfo.setText("Paused");
            }

            @Override
            public void onPlaybackStopped() {
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CheckInVideoPostType.super.onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Completed");
            }

            @Override
            public void onPlaybackError(MediaPlayer mp, int what, int extra) {
                super.onPlaybackError(mp, what, extra);
                thumb_iv.animate().alpha(1.f).setDuration(250).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        CheckInVideoPostType.super.onPlaybackStopped();
                    }
                }).start();
//                mInfo.setText("Error: videoId = " + getVideoId());
            }

            @Override
            protected boolean allowLongPressSupport() {
                return itemView != null;
            }

            @Override
            public String toString() {
                return "Video: " + getVideoId();
            }
        }

        public class VideoPostOnly extends RecyclerView.ViewHolder {
            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;


            TextureVideoView mTextureVideoView;
            ImageView imvPreview;
            ImageView thumb_iv;
            ImageView imvPlay;
            ProgressBar pbWaiting;
            ProgressBar pbProgressBar;
            RelativeLayout fullscreen_rl;

            public VideoPostOnly(View convertView) {
                super(convertView);
                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                fullscreen_rl = (RelativeLayout) convertView.findViewById(R.id.fullscreen_rl);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);
//                        video_view = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);


                mTextureVideoView = (TextureVideoView) convertView.findViewById(R.id.textureview);

                imvPreview = (ImageView) convertView.findViewById(R.id.imv_preview);
                thumb_iv = (ImageView) convertView.findViewById(R.id.thumb_iv);
                imvPlay = (ImageView) convertView.findViewById(R.id.imv_video_play);
                pbWaiting = (ProgressBar) convertView.findViewById(R.id.pb_waiting);
                pbProgressBar = (ProgressBar) convertView.findViewById(R.id.progress_progressbar);


                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
            }
        }

        public class TextOnlyPost extends ViewHolder {
            CircleImageView profileIV;

            TextView nameTV;
            TextView detailTV;
            TextView timeTV;
            TextView viewTV;
            TextView cover_detailTV;
            TextView likeUserName_tv;
            TextView c_nameTV1;
            TextView c_nameTV2;
            TextView c_nameTV3;
            TextView c_nameTV4;
            TextView c_nameTV5;
            TextView comment_first;
            TextView comment_second;
            TextView comment_third;
            TextView comment_forth;
            TextView comment_fifth;
            TextView commentTV;
            TextView rehereLoCount;
            TextView _smilyTV;
            TextView sad_tv;


            ImageView onlieIV;
            ImageView coverIV;
            ImageView smilyIV;
            ImageView like_icon_count;
            ImageView dislike_iv;
            ImageView indicatorIV;

            LinearLayout commentor1_ll;
            LinearLayout commentor2_ll;
            LinearLayout commentor3_ll;
            LinearLayout commentor4_ll;
            LinearLayout commentor5_ll;
            LinearLayout _repostLL;
            LinearLayout like_ll;
            LinearLayout sad_ll;
            LinearLayout comment_ll;


            /*Animation*/

            private ImageView heartImageView;
            private View circleBackground;

            public TextOnlyPost(View convertView) {
                super(convertView);


                heartImageView = (ImageView) convertView.findViewById(R.id.heart);
                circleBackground = convertView.findViewById(R.id.circleBg);

                profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
                nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                detailTV = (TextView) convertView.findViewById(R.id.detailTV);
                timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                viewTV = (TextView) convertView.findViewById(R.id.viewTV);

                cover_detailTV = (TextView) convertView.findViewById(R.id.cover_detailTV);
                smilyIV = (ImageView) convertView.findViewById(R.id.smilyIV);
                likeUserName_tv = (TextView) convertView.findViewById(R.id.likeUserName_tv);
                dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                like_icon_count = (ImageView) convertView.findViewById(R.id.like_icon_count);
                commentor1_ll = (LinearLayout) convertView.findViewById(R.id.commentor1_ll);
                commentor2_ll = (LinearLayout) convertView.findViewById(R.id.commentor2_ll);
                commentor3_ll = (LinearLayout) convertView.findViewById(R.id.commentor3_ll);
                commentor4_ll = (LinearLayout) convertView.findViewById(R.id.commentor4_ll);
                commentor5_ll = (LinearLayout) convertView.findViewById(R.id.commentor5_ll);


                c_nameTV1 = (TextView) convertView.findViewById(R.id.c_nameTV1);
                c_nameTV2 = (TextView) convertView.findViewById(R.id.c_nameTV2);
                c_nameTV3 = (TextView) convertView.findViewById(R.id.c_nameTV3);
                c_nameTV4 = (TextView) convertView.findViewById(R.id.c_nameTV4);
                c_nameTV5 = (TextView) convertView.findViewById(R.id.c_nameTV5);


                comment_first = (TextView) convertView.findViewById(R.id.comment_first);
                comment_second = (TextView) convertView.findViewById(R.id.comment_second);
                comment_third = (TextView) convertView.findViewById(R.id.comment_third);
                comment_forth = (TextView) convertView.findViewById(R.id.comment_forth);
                comment_fifth = (TextView) convertView.findViewById(R.id.comment_fifth);


                commentTV = (TextView) convertView.findViewById(R.id.commentTV);
                rehereLoCount = (TextView) convertView.findViewById(R.id.rehereLoCount);
                _smilyTV = (TextView) convertView.findViewById(R.id._smilyTV);
                sad_tv = (TextView) convertView.findViewById(R.id.sad_TV);

                _repostLL = (LinearLayout) convertView.findViewById(R.id.repostLL);

                indicatorIV = (ImageView) convertView.findViewById(R.id.indicatorIV);


                like_ll = (LinearLayout) convertView.findViewById(R.id.like_ll);
                sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                comment_ll = (LinearLayout) convertView.findViewById(R.id.comment_ll);

                _smilyTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                sad_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV1.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV2.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV3.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV4.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                c_nameTV5.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_fifth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_forth.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_third.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_second.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                comment_first.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                commentTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                rehereLoCount.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                likeUserName_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));

                nameTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
                cover_detailTV.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Roman.otf"));
            }

            @Override
            public void bind(@Nullable final Object object) {

                final DatumHome bean = (DatumHome) object;
                if (!(bean instanceof DatumHome)) {
                    throw new IllegalStateException("Unexpected object");
                }

                if (username.equalsIgnoreCase(((DatumHome) bean).getUser().getUsername())) {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.GONE);
                    }

                } else {
                    if (onlieIV != null) {
                        onlieIV.setVisibility(View.VISIBLE);
                        onlieIV.setImageResource(R.drawable.main_right);
                    }
                }

            /*Populating Comments with username*/
                if (((DatumHome) bean).getPostComments().size() == 1) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = ((DatumHome) bean).getPostComments().get(0).getUsername() + ": ";
                    String next = ((DatumHome) bean).getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                } else if (((DatumHome) bean).getPostComments().size() == 2) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                    c_nameTV1.setVisibility(View.GONE);


                    String first = ((DatumHome) bean).getPostComments().get(0).getUsername() + ": ";
                    String next = ((DatumHome) bean).getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = ((DatumHome) bean).getPostComments().get(1).getUsername() + ": ";
                    String next2 = ((DatumHome) bean).getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);
                } else if (((DatumHome) bean).getPostComments().size() == 3) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = ((DatumHome) bean).getPostComments().get(0).getUsername() + ": ";
                    String next = ((DatumHome) bean).getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = ((DatumHome) bean).getPostComments().get(1).getUsername() + ": ";
                    String next2 = ((DatumHome) bean).getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = ((DatumHome) bean).getPostComments().get(2).getUsername() + ": ";
                    String next3 = ((DatumHome) bean).getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);
                } else if (((DatumHome) bean).getPostComments().size() == 4) {
                    commentor1_ll.setVisibility(View.VISIBLE);
                    commentor2_ll.setVisibility(View.VISIBLE);
                    commentor3_ll.setVisibility(View.VISIBLE);
                    commentor4_ll.setVisibility(View.VISIBLE);
                    commentor5_ll.setVisibility(View.GONE);


                    c_nameTV1.setVisibility(View.GONE);


                    String first = ((DatumHome) bean).getPostComments().get(0).getUsername() + ": ";
                    String next = ((DatumHome) bean).getPostComments().get(0).getComment();

                    final SpannableStringBuilder sb = new SpannableStringBuilder(first + next);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb.setSpan(fcs, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb.setSpan(bss, 0, first.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_first.setText(sb);
                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelper.handle(comment_first);
                    HashTagHelper mTextHashTagHelpercomment_second = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                        @Override
                        public void onHashTagClicked(String hashTag) {
//                            ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                        }
                    });

                    // pass a TextView or any descendant of it (incliding EditText) here.
                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
//                        mTextHashTagHelpercomment_second.handle(comment_second);

                    c_nameTV2.setVisibility(View.GONE);
//                        comment_second.setText(bean.getPostComments().get(1).getComment());


                    String first2 = ((DatumHome) bean).getPostComments().get(1).getUsername() + ": ";
                    String next2 = ((DatumHome) bean).getPostComments().get(1).getComment();

                    final SpannableStringBuilder sb2 = new SpannableStringBuilder(first2 + next2);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs2 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss2 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb2.setSpan(fcs2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    comment_second.setText(sb2);


                    c_nameTV3.setVisibility(View.GONE);


                    String first3 = ((DatumHome) bean).getPostComments().get(2).getUsername() + ": ";
                    String next3 = ((DatumHome) bean).getPostComments().get(2).getComment();

                    final SpannableStringBuilder sb3 = new SpannableStringBuilder(first3 + next3);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs3 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss3 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb3.setSpan(fcs3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_third.setText(sb3);


                    c_nameTV4.setVisibility(View.GONE);


                    String first4 = ((DatumHome) bean).getPostComments().get(3).getUsername() + ": ";
                    String next4 = ((DatumHome) bean).getPostComments().get(3).getComment();

                    final SpannableStringBuilder sb4 = new SpannableStringBuilder(first4 + next4);

// Span to set text color to some RGB value
                    final ForegroundColorSpan fcs4 = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
                    final StyleSpan bss4 = new StyleSpan(Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
// Set the text color for first 4 characters
                    sb4.setSpan(fcs4, 0, first4.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                    //sb3.setSpan(bss3, 0, first3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    comment_forth.setText(sb4);


                } else {
                    commentor1_ll.setVisibility(View.GONE);
                    commentor2_ll.setVisibility(View.GONE);
                    commentor3_ll.setVisibility(View.GONE);
                    commentor4_ll.setVisibility(View.GONE);
                    commentor5_ll.setVisibility(View.GONE);
                }


                indicatorIV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(((DatumHome) bean), getAdapterPosition());
                        }
                    }
                });

                _repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        animateDown();
                        popupWindow(((DatumHome) bean));
                    }
                });

                if (coverIV != null) {
                    coverIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            animateDown();
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {

                                _lastPos = getAdapterPosition();

                            }
                        }
                    });

                }

                like_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((DatumHome) bean).getLike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Likes");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });
                sad_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((DatumHome) bean).getDislike() > 0) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "DisLikes");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }

                    }
                });


                comment_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    /*    LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "Comments");
                        bundle.putInt("POSTID", ((DatumHome) bean).getId());
                        bundle.putInt("INDEX", getAdapterPosition());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/


                        Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                        mIntent.putExtra(Constant.POST_DETAILS, bean);
                        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                        startActivity(mIntent);

                    }
                });


                if (((DatumHome) bean).getPostMedia().size() > 0 && coverIV != null) {

                    imageLoaderNew.displayImage(((DatumHome) bean).getPostMedia().get(0).getUrl(), coverIV, optionsPostImg);

                }

                if (comment_fifth != null) {
                    comment_fifth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_first != null) {
                    comment_first.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_second != null) {
                    comment_second.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });


                }
                if (comment_third != null) {
                    comment_third.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });

                }

                if (comment_forth != null) {
                    comment_forth.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LikesFragment likesFragment = new LikesFragment();
                            Bundle bundle = new Bundle();
                            bundle.putString("Screen", "Comments");
                            bundle.putInt("POSTID", ((DatumHome) bean).getId());
                            bundle.putInt("INDEX", getAdapterPosition());
                            likesFragment.setArguments(bundle);
                            ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                        }
                    });
                }


                if (((DatumHome) bean).getIs_like_status() == 1) {
                /*Liked*/
//                    smilyIV.setVisibility(View.VISIBLE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smile_icon);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smile_icon);
                    }

                } else {
//                    smilyIV.setVisibility(View.GONE);
                    if (smilyIV != null) {
                        smilyIV.setImageResource(R.drawable.smiley_icon_01);
                    }

                    if (like_icon_count != null) {
                        like_icon_count.setImageResource(R.drawable.smiley_icon_01);
                    }

                }
                if (like_icon_count != null) {
                    like_icon_count.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.heart(circleBackground, heartImageView);
                                int is_like_status = ((DatumHome) bean).getIs_like_status();
                                Constant.growShrinkLike(like_icon_count);
                                if (is_like_status == 1) {

                                    Uri.Builder builder = new Uri.Builder()
                                            .appendQueryParameter("user_id", user_id + "")
                                            .appendQueryParameter("post_id", ((DatumHome) bean).getId() + "")
                                            .appendQueryParameter("unlike_Removedislike", 1 + "");
                                    DislikeRemoveDislike task = new DislikeRemoveDislike(WebserviceTask.POST,
                                            null, null, builder);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        task.execute();
                                    }

                                    if (((DatumHome) bean).getIs_like_status() == 1) {
                                        ((DatumHome) bean).setLike(((DatumHome) bean).getLike() - 1);
                                    }
                                    ((DatumHome) bean).setIs_like_status(0);


                                    for (int i = 0; i < ((DatumHome) bean).getLikedUser().size(); i++) {
                                        if (((DatumHome) bean).getLikedUser().get(i) != null) {
                                            String names = ((DatumHome) bean).getLikedUser().get(i).getUsername();
                                            if (names.equalsIgnoreCase(username)) {
                            /*Remove user name from liked names*/
                                                ((DatumHome) bean).getLikedUser().remove(((DatumHome) bean).getLikedUser().get(i));
                                                notifyDataSetChanged();
                                                break;
                                            }
                                        }

                                    }

                                    notifyDataSetChanged();
                                } else {


                                    Uri.Builder builder = new Uri.Builder()
                                            .appendQueryParameter("user_id", user_id + "")
                                            .appendQueryParameter("post_id", ((DatumHome) bean).getId() + "")
                                            .appendQueryParameter("like_dislike", 1 + "");
                                    LikeUnLike task = new LikeUnLike(WebserviceTask.POST,
                                            null, null, builder);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        task.execute();
                                    }
                                    ((DatumHome) bean).setIs_like_status(1);
                                    if (((DatumHome) bean).getIs_dislike_status() == 1) {
                                        ((DatumHome) bean).setDislike(((DatumHome) bean).getDislike() - 1);
                                        ((DatumHome) bean).setIs_dislike_status(0);
                                    }


                                    if (((DatumHome) bean).getLikedUser().size() > 0) {
                                        for (int i = 0; i < ((DatumHome) bean).getLikedUser().size(); i++) {
                                            if (((DatumHome) bean).getLikedUser().get(i) != null) {
                                                String names = ((DatumHome) bean).getLikedUser().get(i).getUsername();
                                                if (!names.equalsIgnoreCase(username)) {

                                            /*Add user name from liked names*/
                                                    LikedUserHome mLikedUserHome = new LikedUserHome();
                                                    mLikedUserHome.setUsername(username);
                                                    mLikedUserHome.setUser_id(user_id + "");

                                                    ((DatumHome) bean).getLikedUser().add(mLikedUserHome);

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
                                        ((DatumHome) bean).setLikedUser(mLikedUserHomeList);
                                    }

                                    ((DatumHome) bean).setLike(((DatumHome) bean).getLike() + 1);
                                    notifyDataSetChanged();
                                }


                            } else {
                        /*No Internet*/
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });
                }


                if (timeTV != null) {
                    timeTV.setText(((DatumHome) bean).getPostTime());
                }

                if (viewTV != null) {
                    viewTV.setText(((DatumHome) bean).getTotalPostView() + "");
                }


                if (commentTV != null) {
                    if (((DatumHome) bean).getTotalComments() != null) {
                        if (((DatumHome) bean).getTotalComments() > 0) {
                            commentTV.setText(((DatumHome) bean).getTotalComments() + "");
                        } else {
                            commentTV.setText("");
                        }
                    } else {
                        commentTV.setText("");
                    }
                }


                if (rehereLoCount != null) {
                    if (((DatumHome) bean).getReherelo() > 0) {
                        rehereLoCount.setText(((DatumHome) bean).getReherelo() + "");
                    } else {
                        rehereLoCount.setText("");
                    }
                }


                if (_smilyTV != null) {
                    if (((DatumHome) bean).getLike() > 0) {
                        _smilyTV.setText(((DatumHome) bean).getLike() + "");
                    } else {
                        _smilyTV.setText("");
                    }
                }


                if (sad_tv != null) {
                    if (((DatumHome) bean).getDislike() > 0) {
                        sad_tv.setText(((DatumHome) bean).getDislike() + "");
                    } else {
                        sad_tv.setText("");
                    }


                }

                if (likeUserName_tv != null) {
                    if (((DatumHome) bean).getLikedUser() != null) {
                        int likedUsersSize = ((DatumHome) bean).getLikedUser().size();

                        StringBuilder result = new StringBuilder();
                        for (int i = 0; i < likedUsersSize; i++) {
                            if (((DatumHome) bean).getLikedUser().get(i) != null) {
                                String name = ((DatumHome) bean).getLikedUser().get(i).getUsername();
                                result.append(name);
                                if (i < likedUsersSize - 1) {
                                    result.append(",");
                                }
                            }


                        }

                        String likeduserName = "";
                        if (result.length() > 0) {
                            likeduserName = result.toString();
                        }
                        likeUserName_tv.setText(likeduserName);
                    }

                }


                if (dislike_iv != null) {
                    if (((DatumHome) bean).getIs_dislike_status() == 1) {
               /*Disliked*/
                        dislike_iv.setImageResource(R.drawable.orange_dislike);
                    } else {
                        dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
                    }
                    dislike_iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                Constant.dislikeAnimation(circleBackground, heartImageView);
                                int is_dislike_status = ((DatumHome) bean).getIs_dislike_status();
                                Constant.growShrinkLike(dislike_iv);
                                if (is_dislike_status == 1) {

                                    Uri.Builder builder = new Uri.Builder()
                                            .appendQueryParameter("user_id", user_id + "")
                                            .appendQueryParameter("post_id", ((DatumHome) bean).getId() + "")
                                            .appendQueryParameter("unlike_Removedislike", 2 + "");
                                    DislikeRemoveDislike task = new DislikeRemoveDislike(WebserviceTask.POST,
                                            null, null, builder);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        task.execute();
                                    }

                                    if (((DatumHome) bean).getIs_dislike_status() == 1) {
                                        ((DatumHome) bean).setDislike(((DatumHome) bean).getDislike() - 1);
                                    }

                                    ((DatumHome) bean).setIs_dislike_status(0);


                                    notifyDataSetChanged();
                                } else {


                                    Uri.Builder builder = new Uri.Builder()
                                            .appendQueryParameter("user_id", user_id + "")
                                            .appendQueryParameter("post_id", ((DatumHome) bean).getId() + "")
                                            .appendQueryParameter("like_dislike", 2 + "");
                                    LikeUnLike task = new LikeUnLike(WebserviceTask.POST,
                                            null, null, builder);

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else {
                                        task.execute();
                                    }
                                    ((DatumHome) bean).setIs_dislike_status(1);
                                    ((DatumHome) bean).setDislike(((DatumHome) bean).getDislike() + 1);
                                    if (((DatumHome) bean).getIs_like_status() == 1) {
                                        ((DatumHome) bean).setLike(((DatumHome) bean).getLike() - 1);
                                    }

                                    ((DatumHome) bean).setIs_like_status(0);
                                    for (int i = 0; i < ((DatumHome) bean).getLikedUser().size(); i++) {
                                        if (((DatumHome) bean).getLikedUser().get(i) != null) {
                                            String names = ((DatumHome) bean).getLikedUser().get(i).getUsername();
                                            if (names.equalsIgnoreCase(username)) {
                            /*Remove user name from liked names*/
                                                ((DatumHome) bean).getLikedUser().remove(((DatumHome) bean).getLikedUser().get(i));
                                                notifyDataSetChanged();
                                                break;
                                            }
                                        }

                                    }
                                    notifyDataSetChanged();
                                }


                            } else {
                        /*No Internet*/
                                Constant.showToast(getActivity().getString(R.string.internet), getActivity());
                            }
                        }
                    });

                }


                if (((DatumHome) bean).getLikedUser() != null) {
                    if (((DatumHome) bean).getLikedUser().size() > 0) {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.VISIBLE);
                        }


                    } else {
                        if (smilyIV != null) {
                            smilyIV.setVisibility(View.GONE);
                        }

                    }
                } else {
                    if (smilyIV != null) {
                        smilyIV.setVisibility(View.GONE);
                    }

                }


                if (nameTV != null) {
                    nameTV.setText(((DatumHome) bean).getUser().getUsername());
                }

                if (((DatumHome) bean).getLocation() != null && !((DatumHome) bean).getLocation().equalsIgnoreCase("")) {

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }

                    if (detailTV != null) {
                        detailTV.setVisibility(View.VISIBLE);
                        detailTV.setText(((DatumHome) bean).getLocation());
                    }


                /*RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _viewHolder.nameTV.getLayoutParams();
                params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                _viewHolder.nameTV.setLayoutParams(params); //causes layout update*/
                } else {
                    if (detailTV != null) {
                        detailTV.setVisibility(View.GONE);
                    }

                    if (nameTV != null) {
                        nameTV.setVisibility(View.VISIBLE);
                    }


                }


                imageLoaderNew.displayImage(((DatumHome) bean).getUser().getImage(), profileIV, optionsProfileImg);
                cover_detailTV.setMaxLines(14);
                if (!((DatumHome) bean).getText().equalsIgnoreCase("")) {

                    cover_detailTV.setVisibility(View.VISIBLE);
                    if (((DatumHome) bean).getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
                                ////Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        cover_detailTV.setText(((DatumHome) bean).getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(cover_detailTV);
                    } else {
                        cover_detailTV.setText(((DatumHome) bean).getText());
                    }


                } else {
                    cover_detailTV.setVisibility(View.GONE);
                    cover_detailTV.setText("");
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            _shareLL.setVisibility(View.GONE);

                    /*Code of click*/
                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);


                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();

                            }
                        }
                    });
                }
                if (profileIV != null) {
                    profileIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Logger.logsError(TAG, "Other User Id : " + ((DatumHome) bean).getUserId());
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(((DatumHome) bean).getUserId()));
                            startActivity(mIntent);
                        }
                    });
                }
                if (nameTV != null) {
                    nameTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Logger.logsError(TAG, "Other User Id : " + ((DatumHome) bean).getUserId());
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, Integer.parseInt(((DatumHome) bean).getUserId()));
                            startActivity(mIntent);
                        }
                    });

                }
                if (cover_detailTV != null) {
                    cover_detailTV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            findDoubleClick(circleBackground, heartImageView, getAdapterPosition(), 0, false, null, null, null, null, true);
                            if (!mHasDoubleClicked) {
                                _lastPos = getAdapterPosition();
                            }
                        }
                    });
                }

            }
        }


        @Override
        public int getItemViewType(int position) {
//            Logger.logsInfo(MainViewScreen.class.getSimpleName(),"MainViewAdapterRecyclerView getItemViewType Called");

            Object item = getItem(position);
            DatumHome bean = (DatumHome) item;

            if (bean.getFrame_id() > 0 && !bean.getPostType().equalsIgnoreCase("7")) {
//                Logger.logsInfo(MainViewScreen.class.getSimpleName(), "Return type = 4" + "with Post : " + position);
                return 4;

            } else if (bean.getIs_reherelo() == 1) {
//                Logger.logsInfo(MainViewScreen.class.getSimpleName(), "Return type = 5" + "with Post : " + position);
                return 5;
            } else {
//                Logger.logsInfo(MainViewScreen.class.getSimpleName(), "Return type = ELSE CASE : " + Integer.parseInt(bean.getPostType()) + "with Post : " + position);
                return (Integer.parseInt(bean.getPostType()));
            }


        }

    }

    public void popupWindow(DatumHome bean) {

    }


    /**
     * MiniView Adapter
     */

    public class MiniViewAdapter extends BaseAdapter {

        private Activity context = null;


        public int clickpos = 0;

        /////////////////////
        private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
        private long lastPressTime;
        private int _lastPos;
        private boolean mHasDoubleClicked = false;


        SharedPreferences.Editor editor = null;
        List<DatumHome> mDataList;


        private String followUnfollowAPIName = "";


        /*Image Loader*/
        private DisplayImageOptions options;
        private DisplayImageOptions optionsPost;
        ImageLoader imageLoaderNew;

        public MiniViewAdapter(Activity context, List<DatumHome> dataList) {
            this.context = context;
            this.mDataList = dataList;

            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

            imageLoaderNew = ImageLoader.getInstance();
            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.profile_icon)
                    .showImageOnLoading(R.drawable.profile_icon)
                    .showImageOnFail(R.drawable.profile_icon)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)

                    .build();
            optionsPost = new DisplayImageOptions.Builder()
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

            public CircleImageView _userPic;
            public CircleImageView profileIV_OG;
            public ImageView _Pic;
            public ImageView picforcomment_reherelo;

            public TextView _userNmae;
            public TextView nameTV_OG;
            public TextView original_header_nameTV;
            public TextView more_images_count_tv_og;
            public TextView more_images_count_tv;
            public TextView _gilNmae;
            public TextView detailTV_OG;
            public TextView _timeCount;
            public TextView timeTV_OG;
            public TextView _viewCount;
            public TextView viewTV_OG;
            public TextView _comment;
            public TextView orignal_comment_reherelo;
            public TextView _commentCount;
            public TextView _repostCount;
            public TextView _likeCount;
            public TextView sad_TV;

            public LinearLayout _repostLL;
            public LinearLayout img_ll;

            private LinearLayout like_ll;
            private LinearLayout sad_ll;
            private LinearLayout comment_ll;
            private LinearLayout main_ll;
            private RelativeLayout main_ll_og;

            public ImageView _moreLV;
            public ImageView play_icon;
            public ImageView play_icon_og;
            public TextView _addressTV;
            ProgressBar img_progress;

            ImageView smilyIV;
            ImageView dislike_iv;

            ImageView onlieIV;
            ImageView onlieIV_OG;

            LinearLayout main_txt_imgll;
            private View circleBg;
            private ImageView heart;


            private View circleBg_animation;
            private ImageView heart_animation;

            private View circleBg_animation_dis;
            private ImageView heart_animation_dis;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public int getItemViewType(int position) {
            DatumHome bean = mDataList.get(position);
            if (bean.getIs_reherelo() == 1) {
//                Logger.logsInfo(MiniViewAdapterSearch.class.getSimpleName(), "Return type = 5" + "with Post : " + position);
                return 2;
            } else {
                return 1;
            }

        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder view_holder;

            //final MiniViewBean bean = (MiniViewBean) list_item.get(position);
            int type = getItemViewType(position);
            if (type == 1) {
                if (convertView == null) {
                    view_holder = new ViewHolder();

                    LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = _linflater.inflate(R.layout.miniview_row, null);

                    view_holder._userPic = (CircleImageView) convertView.findViewById(R.id.profileIV);
                    view_holder.circleBg = (View) convertView.findViewById(R.id.circleBg);
                    view_holder.heart = (ImageView) convertView.findViewById(R.id.heart);


                    view_holder.circleBg_animation = (View) convertView.findViewById(R.id.circleBg_animation);
                    view_holder.heart_animation = (ImageView) convertView.findViewById(R.id.heart_animation);

                    view_holder.circleBg_animation_dis = (View) convertView.findViewById(R.id.circleBg_animation_dis);
                    view_holder.heart_animation_dis = (ImageView) convertView.findViewById(R.id.heart_animation_dis);

                    view_holder._Pic = (ImageView) convertView.findViewById(R.id.picforcomment);
                    view_holder.onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                    view_holder._userNmae = (TextView) convertView.findViewById(R.id.nameTV);
                    view_holder.more_images_count_tv = (TextView) convertView.findViewById(R.id.more_images_count_tv);
                    view_holder._gilNmae = (TextView) convertView.findViewById(R.id.detailTV);
                    view_holder._timeCount = (TextView) convertView.findViewById(R.id.timeTV);
                    view_holder.sad_TV = (TextView) convertView.findViewById(R.id.sad_TV);
                    view_holder._viewCount = (TextView) convertView.findViewById(R.id.viewTV);
                    view_holder._comment = (TextView) convertView.findViewById(R.id.orignal_comment);
                    view_holder._commentCount = (TextView) convertView.findViewById(R.id.commentcount_tv);
                    view_holder._repostCount = (TextView) convertView.findViewById(R.id.repostlocount_tv);
                    view_holder._likeCount = (TextView) convertView.findViewById(R.id.likecount_tv);
                    view_holder._addressTV = (TextView) convertView.findViewById(R.id.addressTV);
                    view_holder.smilyIV = (ImageView) convertView.findViewById(R.id.like_icon);
                    view_holder.dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                    view_holder.play_icon = (ImageView) convertView.findViewById(R.id.play_icon);
                    view_holder._repostLL = (LinearLayout) convertView.findViewById(R.id.reHereloLL);
                    view_holder.img_ll = (LinearLayout) convertView.findViewById(R.id.img_ll);


                    view_holder._moreLV = (ImageView) convertView.findViewById(R.id.moreinfo_click);

                    view_holder.like_ll = (LinearLayout) convertView.findViewById(R.id.likeLL);
                    view_holder.sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                    view_holder.comment_ll = (LinearLayout) convertView.findViewById(R.id.commentLL);
                    view_holder.main_ll = (LinearLayout) convertView.findViewById(R.id.main_ll);

                    view_holder.img_progress = (ProgressBar) convertView.findViewById(R.id.img_progress);


                    /**
                     * TypeFace
                     *
                     */

                    view_holder._userNmae.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._addressTV.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._viewCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._timeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._repostCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._commentCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._likeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    convertView.setTag(view_holder);

                } else {
                    view_holder = (ViewHolder) convertView.getTag();
                }
            } else if (type == 2) {
                if (convertView == null) {
                    view_holder = new ViewHolder();

                    LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = _linflater.inflate(R.layout.miniview_row_reherelo, null);

                    view_holder._userPic = (CircleImageView) convertView.findViewById(R.id.profileIV);
                    view_holder.profileIV_OG = (CircleImageView) convertView.findViewById(R.id.profileIV_OG);

                    view_holder.circleBg = (View) convertView.findViewById(R.id.circleBg);
                    view_holder.heart = (ImageView) convertView.findViewById(R.id.heart);
                    view_holder.picforcomment_reherelo = (ImageView) convertView.findViewById(R.id.picforcomment_reherelo);
                    view_holder.onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                    view_holder.onlieIV_OG = (ImageView) convertView.findViewById(R.id.onlieIV_OG);
                    view_holder._userNmae = (TextView) convertView.findViewById(R.id.nameTV);
                    view_holder.nameTV_OG = (TextView) convertView.findViewById(R.id.nameTV_OG);
                    view_holder.original_header_nameTV = (TextView) convertView.findViewById(R.id.original_header_nameTV);
                    view_holder.more_images_count_tv_og = (TextView) convertView.findViewById(R.id.more_images_count_tv_og);
                    view_holder._gilNmae = (TextView) convertView.findViewById(R.id.detailTV);
                    view_holder.detailTV_OG = (TextView) convertView.findViewById(R.id.detailTV_OG);
                    view_holder._timeCount = (TextView) convertView.findViewById(R.id.timeTV);
                    view_holder.timeTV_OG = (TextView) convertView.findViewById(R.id.timeTV_OG);
                    view_holder.sad_TV = (TextView) convertView.findViewById(R.id.sad_TV);
                    view_holder._viewCount = (TextView) convertView.findViewById(R.id.viewTV);
                    view_holder.viewTV_OG = (TextView) convertView.findViewById(R.id.viewTV_OG);
                    view_holder._comment = (TextView) convertView.findViewById(R.id.orignal_comment);
                    view_holder.orignal_comment_reherelo = (TextView) convertView.findViewById(R.id.orignal_comment_reherelo);
                    view_holder._commentCount = (TextView) convertView.findViewById(R.id.commentcount_tv);
                    view_holder._repostCount = (TextView) convertView.findViewById(R.id.repostlocount_tv);
                    view_holder._likeCount = (TextView) convertView.findViewById(R.id.likecount_tv);
                    view_holder._addressTV = (TextView) convertView.findViewById(R.id.addressTV);
                    view_holder.smilyIV = (ImageView) convertView.findViewById(R.id.like_icon);
                    view_holder.dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                    view_holder._repostLL = (LinearLayout) convertView.findViewById(R.id.reHereloLL);
                    view_holder.img_ll = (LinearLayout) convertView.findViewById(R.id.img_ll);

                    view_holder.play_icon_og = (ImageView) convertView.findViewById(R.id.play_icon_og);

                    view_holder.main_txt_imgll = (LinearLayout) convertView.findViewById(R.id.main_txt_imgll);


                    view_holder._moreLV = (ImageView) convertView.findViewById(R.id.moreinfo_click);

                    view_holder.like_ll = (LinearLayout) convertView.findViewById(R.id.likeLL);
                    view_holder.sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                    view_holder.comment_ll = (LinearLayout) convertView.findViewById(R.id.commentLL);
                    view_holder.main_ll = (LinearLayout) convertView.findViewById(R.id.main_ll);
                    view_holder.main_ll_og = (RelativeLayout) convertView.findViewById(R.id.main_ll_og);

                    view_holder.img_progress = (ProgressBar) convertView.findViewById(R.id.img_progress);


                    /**
                     * TypeFace
                     *
                     */

                    view_holder.original_header_nameTV.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));

                    view_holder.more_images_count_tv_og.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder.detailTV_OG.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder.nameTV_OG.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._userNmae.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._addressTV.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._viewCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._timeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._repostCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._commentCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._likeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder.orignal_comment_reherelo.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    convertView.setTag(view_holder);

                } else {
                    view_holder = (ViewHolder) convertView.getTag();
                }
            } else {
                if (convertView == null) {
                    view_holder = new ViewHolder();

                    LayoutInflater _linflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = _linflater.inflate(R.layout.miniview_row, null);
                    view_holder.circleBg = (View) convertView.findViewById(R.id.circleBg);
                    view_holder.heart = (ImageView) convertView.findViewById(R.id.heart);


                    view_holder.circleBg_animation = (View) convertView.findViewById(R.id.circleBg_animation);
                    view_holder.heart_animation = (ImageView) convertView.findViewById(R.id.heart_animation);

                    view_holder.circleBg_animation_dis = (View) convertView.findViewById(R.id.circleBg_animation_dis);
                    view_holder.heart_animation_dis = (ImageView) convertView.findViewById(R.id.heart_animation_dis);
                    view_holder._userPic = (CircleImageView) convertView.findViewById(R.id.profileIV);
                    view_holder._Pic = (ImageView) convertView.findViewById(R.id.picforcomment);
                    view_holder.onlieIV = (ImageView) convertView.findViewById(R.id.onlieIV);
                    view_holder._userNmae = (TextView) convertView.findViewById(R.id.nameTV);
                    view_holder.more_images_count_tv = (TextView) convertView.findViewById(R.id.more_images_count_tv);

                    view_holder._gilNmae = (TextView) convertView.findViewById(R.id.detailTV);
                    view_holder._timeCount = (TextView) convertView.findViewById(R.id.timeTV);
                    view_holder.sad_TV = (TextView) convertView.findViewById(R.id.sad_TV);
                    view_holder._viewCount = (TextView) convertView.findViewById(R.id.viewTV);
                    view_holder._comment = (TextView) convertView.findViewById(R.id.orignal_comment);
                    view_holder._commentCount = (TextView) convertView.findViewById(R.id.commentcount_tv);
                    view_holder._repostCount = (TextView) convertView.findViewById(R.id.repostlocount_tv);
                    view_holder._likeCount = (TextView) convertView.findViewById(R.id.likecount_tv);
                    view_holder._addressTV = (TextView) convertView.findViewById(R.id.addressTV);
                    view_holder.smilyIV = (ImageView) convertView.findViewById(R.id.like_icon);
                    view_holder.dislike_iv = (ImageView) convertView.findViewById(R.id.dislike_iv);
                    view_holder._repostLL = (LinearLayout) convertView.findViewById(R.id.reHereloLL);
                    view_holder.img_ll = (LinearLayout) convertView.findViewById(R.id.img_ll);

                    view_holder.play_icon = (ImageView) convertView.findViewById(R.id.play_icon);
                    view_holder._moreLV = (ImageView) convertView.findViewById(R.id.moreinfo_click);

                    view_holder.like_ll = (LinearLayout) convertView.findViewById(R.id.likeLL);
                    view_holder.sad_ll = (LinearLayout) convertView.findViewById(R.id.sad_ll);
                    view_holder.comment_ll = (LinearLayout) convertView.findViewById(R.id.commentLL);
                    view_holder.main_ll = (LinearLayout) convertView.findViewById(R.id.main_ll);

                    view_holder.img_progress = (ProgressBar) convertView.findViewById(R.id.img_progress);


                    /**
                     * TypeFace
                     *
                     */

                    view_holder._userNmae.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._addressTV.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Md.otf"));
                    view_holder._viewCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._timeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._repostCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._commentCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    view_holder._likeCount.setTypeface(Constant.typeface(context, "HelveticaNeueLTStd-Roman.otf"));
                    convertView.setTag(view_holder);

                } else {
                    view_holder = (ViewHolder) convertView.getTag();
                }
            }

            final DatumHome bean = mDataList.get(position);
            switch (bean.getPostType()) {
                case "6":
                    view_holder._repostLL.setVisibility(View.INVISIBLE);
                    break;
                case "7":
                    view_holder._repostLL.setVisibility(View.INVISIBLE);
                    break;
                case "8":
                    view_holder._repostLL.setVisibility(View.INVISIBLE);
                    break;
                case "9":
                    view_holder._repostLL.setVisibility(View.INVISIBLE);
                    break;
                default:
                    view_holder._repostLL.setVisibility(View.VISIBLE);
                    break;
            }
            if (view_holder._repostLL != null) {
                view_holder._repostLL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        animateDown();
                        popupWindow(bean);
                    }
                });
            }
            if (view_holder._moreLV != null) {
                view_holder._moreLV.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (_shareLL.getVisibility() != View.VISIBLE) {
                            animateUp(bean, position);
                        }

                    }
                });
            }
            if (view_holder._comment != null) {
                view_holder._comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                            mIntent.putExtra(Constant.INDEX, position);
                            mIntent.putExtra(Constant.FROM_USER_PROFILE, true);
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

                    /*Code of click*/
                            findDoubleClick(view_holder.circleBg, view_holder.heart, position);


                            if (!mHasDoubleClicked) {

                                _lastPos = position;
                            }
                        }

                    }
                });
            }
            if (type == 2) {

                if (bean.getOriginalPost() != null) {
                    if (username.equalsIgnoreCase(bean.getOriginalPost().getUser().getUsername())) {
                        view_holder.onlieIV_OG.setVisibility(View.GONE);
                    } else {
                        view_holder.onlieIV_OG.setVisibility(View.VISIBLE);
                        if (bean.getOriginalPost().getIs_following() == 1) {
                            view_holder.onlieIV_OG.setImageResource(R.drawable.main_right);
                        } else {
                            view_holder.onlieIV_OG.setImageResource(R.drawable.main_plus_icon);
                        }

                    }


                    view_holder.onlieIV_OG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                                if (bean.getOriginalPost().getIs_following() == 1) {
                                    bean.getOriginalPost().setIs_following(0);
                                    followUnfollowAPIName = "unfollow";
                                } else {
                                    bean.getOriginalPost().setIs_following(1);
                                    followUnfollowAPIName = "follow";
                                }
                                notifyDataSetChanged();

                                Uri.Builder builder = new Uri.Builder()
                                        .appendQueryParameter("user_id", user_id + "")
                                        .appendQueryParameter("other_user_id", bean.getOriginalPost().getUser().getUsr_id() + "");

                                WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                        null, null, builder);
                                task.execute();


                            } else {
                                Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                            }

                        }
                    });
                    view_holder.profileIV_OG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                            mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                            mIntent.putExtra(Constant.other_user_id, bean.getOriginalPost().getUser().getUsr_id());
                            startActivity(mIntent);
                        }
                    });

                    imageLoaderNew.displayImage(bean.getOriginalPost().getUser().getImage(), view_holder.profileIV_OG, options);
//                    view_holder.nameTV_OG.setText(bean.getUser().getUsername());
//                    view_holder.original_header_nameTV.setText(bean.getOriginalPost().getUser().getUsername());
                    view_holder.original_header_nameTV.setText(getResources().getString(R.string.rehereloadText));
                    view_holder.nameTV_OG.setText(bean.getOriginalPost().getUser().getUsername());
                    if (bean.getOriginalPost().getPost_location() != null && !bean.getOriginalPost().getPost_location().equalsIgnoreCase("")) {
                        view_holder.detailTV_OG.setVisibility(View.VISIBLE);
                        view_holder.detailTV_OG.setText(bean.getOriginalPost().getPost_location());
                    } else {
                        view_holder.detailTV_OG.setVisibility(View.GONE);
                    }

                    view_holder.timeTV_OG.setText(bean.getOriginalPost().getPostTime());
                    view_holder.viewTV_OG.setText(bean.getOriginalPost().getTotalPostView() + "");
                    switch (bean.getOriginalPost().getPost_type()) {
                        case 1://img and img+text
                            view_holder.main_txt_imgll.setVisibility(View.VISIBLE);

                            if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {
                                view_holder.orignal_comment_reherelo.setText(bean.getOriginalPost().getText());
                            } else {
                                view_holder.orignal_comment_reherelo.setText("");
                            }
                               /*Clickable TextView*/
                            if (!bean.getText().equalsIgnoreCase("")) {
                                if (bean.getHashTags().size() > 0) {
                                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                        @Override
                                        public void onHashTagClicked(String hashTag) {
//                                            Constant.showToast("You clicked on :" + hashTag, getActivity());
                                        }
                                    });
                                    view_holder._comment.setText(bean.getText());
                                    // pass a TextView or any descendant of it (incliding EditText) here.
                                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                    mTextHashTagHelper.handle(view_holder._comment);
                                } else {
                                    view_holder._comment.setText(bean.getText());
                                }

                            } else {
                                view_holder._comment.setText("");
                            }


                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                view_holder.picforcomment_reherelo.setVisibility(View.VISIBLE);
//                                imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(0).getUrl(), view_holder.picforcomment_reherelo, optionsPost);
                                imageLoaderNew.loadImage(bean.getOriginalPost().getPostMedia().get(0).getUrl(), optionsPost, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
//                                view_holder.img_progress.setVisibility(View.VISIBLE);
//                        Toast.makeText(context, "Loading started..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Failed..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Complete..", Toast.LENGTH_SHORT).show();
                                        /*BitmapDrawable ob = new BitmapDrawable(context.getResources(), loadedImage);
                                        view_holder.picforcomment_reherelo.setBackgroundDrawable(ob);
*/

                                        view_holder.picforcomment_reherelo.setImageBitmap(loadedImage);

                                    }
                                });
                                if (bean.getOriginalPost().getPostMedia().size() > 1) {
                                    view_holder.more_images_count_tv_og.setVisibility(View.VISIBLE);
                                    int count = bean.getOriginalPost().getPostMedia().size() - 1;
                                    view_holder.more_images_count_tv_og.setText("+" + count);
                                } else {
                                    view_holder.more_images_count_tv_og.setVisibility(View.GONE);
                                }
                            } else {
                                view_holder.picforcomment_reherelo.setVisibility(View.GONE);
                                view_holder.more_images_count_tv_og.setVisibility(View.GONE);
                            }
                            view_holder.play_icon_og.setVisibility(View.GONE);
                            view_holder.picforcomment_reherelo.setVisibility(View.VISIBLE);
                            view_holder.img_ll.setVisibility(View.VISIBLE);

                            break;
                        case 2://video
                                /*Clickable TextView*/
                            if (!bean.getText().equalsIgnoreCase("")) {
                                if (bean.getHashTags().size() > 0) {
                                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                        @Override
                                        public void onHashTagClicked(String hashTag) {
//                                            Constant.showToast("You clicked on :" + hashTag, getActivity());
                                        }
                                    });
                                    view_holder._comment.setText(bean.getText());
                                    // pass a TextView or any descendant of it (incliding EditText) here.
                                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                    mTextHashTagHelper.handle(view_holder._comment);
                                } else {
                                    view_holder._comment.setText(bean.getText());
                                }

                            } else {
                                view_holder._comment.setText("");
                            }
                            view_holder.main_txt_imgll.setVisibility(View.VISIBLE);
                            view_holder.img_ll.setVisibility(View.VISIBLE);
                            view_holder.play_icon_og.setVisibility(View.VISIBLE);
                            view_holder.picforcomment_reherelo.setVisibility(View.VISIBLE);

                            if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                view_holder.picforcomment_reherelo.setVisibility(View.VISIBLE);
//                                imageLoaderNew.displayImage(bean.getOriginalPost().getPostMedia().get(0).getUrl(), view_holder.picforcomment_reherelo, optionsPost);
                                imageLoaderNew.loadImage(bean.getOriginalPost().getPostMedia().get(0).getThumbUrl(), optionsPost, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
//                                view_holder.img_progress.setVisibility(View.VISIBLE);
//                        Toast.makeText(context, "Loading started..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Failed..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Complete..", Toast.LENGTH_SHORT).show();

                                        view_holder.picforcomment_reherelo.setImageBitmap(loadedImage);

                                    }
                                });
                            }
                            if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {
                                view_holder.orignal_comment_reherelo.setText(bean.getOriginalPost().getText());
                            } else {
                                view_holder.orignal_comment_reherelo.setText("");
                            }
                            break;
                        case 3://text
                            view_holder.main_txt_imgll.setVisibility(View.VISIBLE);

                            view_holder.img_ll.setVisibility(View.GONE);
                            view_holder.play_icon_og.setVisibility(View.GONE);
                            if (!bean.getOriginalPost().getText().equalsIgnoreCase("")) {
                                view_holder.orignal_comment_reherelo.setText(bean.getOriginalPost().getText());
                            } else {
                                view_holder.orignal_comment_reherelo.setText("");
                            }
                               /*Clickable TextView*/
                            if (!bean.getText().equalsIgnoreCase("")) {
                                if (bean.getHashTags().size() > 0) {
                                    HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                                        @Override
                                        public void onHashTagClicked(String hashTag) {
//                                            Constant.showToast("You clicked on :" + hashTag, getActivity());
                                        }
                                    });
                                    view_holder._comment.setText(bean.getText());
                                    // pass a TextView or any descendant of it (incliding EditText) here.
                                    // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                                    mTextHashTagHelper.handle(view_holder._comment);
                                } else {
                                    view_holder._comment.setText(bean.getText());
                                }

                            } else {
                                view_holder._comment.setText("");
                            }

                            break;
                        default:
                            break;
                    }
                }

            }
            if (username.equalsIgnoreCase(bean.getUser().getUsername())) {
                view_holder.onlieIV.setVisibility(View.GONE);
            } else {
                view_holder.onlieIV.setVisibility(View.VISIBLE);
                view_holder.onlieIV.setImageResource(R.drawable.main_right);


            }

            if (bean.getIs_following() == 1) {
                view_holder.onlieIV.setImageResource(R.drawable.main_right);
            } else {
                view_holder.onlieIV.setImageResource(R.drawable.main_plus_icon);
            }
            view_holder.onlieIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {


                        if (bean.getIs_following() == 1) {
                            bean.setIs_following(0);
                            followUnfollowAPIName = "unfollow";
                        } else {
                            bean.setIs_following(1);
                            followUnfollowAPIName = "follow";
                        }
                        notifyDataSetChanged();

                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("user_id", user_id + "")
                                .appendQueryParameter("other_user_id", bean.getUser().getUsr_id() + "");

                        WSTaskFollowUnfollow task = new WSTaskFollowUnfollow(WebserviceTask.POST,
                                null, null, builder);
                        task.execute();


                    } else {
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                    }

                }
            });


            view_holder._userPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                    mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                    mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                    startActivity(mIntent);
                }
            });
            view_holder._userNmae.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getActivity(), MainTabActivity.class);
                    mIntent.putExtra(Constant.FROMSUGGESTIONS, true);
                    mIntent.putExtra(Constant.other_user_id, Integer.parseInt(bean.getUserId()));
                    startActivity(mIntent);
                }
            });


            if (type != 2) {
                if (bean.getPostType() != null) {
                    if (bean.getPostType().equalsIgnoreCase("1")) {
                        view_holder.img_ll.setVisibility(View.VISIBLE);
                        view_holder.play_icon.setVisibility(View.GONE);
                    } else if (bean.getPostType().equalsIgnoreCase("2")) {
                        view_holder.img_ll.setVisibility(View.VISIBLE);
                        view_holder.play_icon.setVisibility(View.VISIBLE);
                    } else if (bean.getPostType().equalsIgnoreCase("3")) {
                        view_holder.img_ll.setVisibility(View.GONE);
                        view_holder.play_icon.setVisibility(View.GONE);
                    }

                }

            }

            if (bean.getIs_like_status() == 1) {
                /*Liked*/
                view_holder.smilyIV.setImageResource(R.drawable.smile_icon);
            } else {
                view_holder.smilyIV.setImageResource(R.drawable.smiley_icon_01);
            }

            if (bean.getIs_dislike_status() == 1) {
               /*Disliked*/
                view_holder.dislike_iv.setImageResource(R.drawable.orange_dislike);
            } else {
                view_holder.dislike_iv.setImageResource(R.drawable.sad_icon_home_screen);
            }

            if (view_holder.main_ll != null) {
                view_holder.main_ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    /*Code of click*/
                        findDoubleClick(view_holder.circleBg, view_holder.heart, position);


                        if (!mHasDoubleClicked) {

                            _lastPos = position;
                        }
                    }
                });
            }
            if (view_holder.main_ll_og != null) {
                view_holder.main_ll_og.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    /*Code of click*/
                        findDoubleClick(view_holder.circleBg, view_holder.heart, position);


                        if (!mHasDoubleClicked) {
                            _lastPos = position;

                        }
                    }
                });
            }
            view_holder.smilyIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                     /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(context)) {
                       /* if (bean.getPostType().equalsIgnoreCase("3")) {
                            Constant.heart(view_holder.circleBg_animation, view_holder.heart_animation);
                        } else {
                            Constant.heart(view_holder.circleBg, view_holder.heart);
                        }*/
                        Constant.growShrinkLike(view_holder.smilyIV);
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
/*

                            for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                if (bean.getLikedUser().get(i) != null) {
                                    String names = bean.getLikedUser().get(i).getUsername();
                                    if (names.equalsIgnoreCase(username)) {
                            *//*Remove user name from liked names*//*
                                        bean.getLikedUser().remove(bean.getLikedUser().get(i));
                                        notifyDataSetChanged();
                                        break;
                                    }
                                }

                            }*/

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

                            /*if (bean.getLikedUser().size() > 0) {
                                for (int i = 0; i < bean.getLikedUser().size(); i++) {
                                    if (bean.getLikedUser().get(i) != null) {
                                        String names = bean.getLikedUser().get(i).getUsername();
                                        if (!names.equalsIgnoreCase(username)) {
                            *//*Remove user name from liked names*//*
                                            bean.getLikedUser().add(bean.getLikedUser().get(i));
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
                            }*/

                            bean.setLike(bean.getLike() + 1);
                            notifyDataSetChanged();
                        }


                    } else {
                        /*No Internet*/
                        Constant.showToast(context.getString(R.string.internet), context);
                    }
                }
            });
            view_holder.dislike_iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Calling Like/Unlike Service Here*/
                    if (NetworkAvailablity.checkNetworkStatus(context)) {


                       /* if (bean.getPostType().equalsIgnoreCase("3")) {
                            Constant.dislikeAnimation(view_holder.circleBg_animation_dis, view_holder.heart_animation_dis);
                        } else {
                            Constant.dislikeAnimation(view_holder.circleBg, view_holder.heart);
                        }*/
                        Constant.growShrinkLike(view_holder.dislike_iv);
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
            view_holder.sad_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getDislike() > 0) {
                        LikesFragment likesFragment = new LikesFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("Screen", "DisLikes");
                        bundle.putInt("INDEX", position);
                        bundle.putInt("POSTID", bean.getId());
                        likesFragment.setArguments(bundle);
                        ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);
                    }

                }
            });

            view_holder.comment_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
              /*      LikesFragment likesFragment = new LikesFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("Screen", "Comments");
                    bundle.putInt("POSTID", bean.getId());
                    bundle.putInt("INDEX", position);
                    likesFragment.setArguments(bundle);
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(likesFragment, true);*/

                    Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                    mIntent.putExtra(Constant.POST_DETAILS, bean);
                    mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                    startActivity(mIntent);
                }
            });

            /*Populating Data*/
            view_holder._userNmae.setText(bean.getUser().getUsername());

            if (bean.getLocation() != null && !bean.getLocation().equalsIgnoreCase("")) {
                view_holder._addressTV.setVisibility(View.VISIBLE);
                view_holder._userNmae.setVisibility(View.VISIBLE);
                view_holder._addressTV.setText(bean.getLocation());

            } else {
                view_holder._addressTV.setVisibility(View.GONE);
                view_holder._userNmae.setVisibility(View.VISIBLE);

            }
            view_holder._timeCount.setText(bean.getPostTime());
            if (bean.getTotalPostView() > 0) {
                view_holder._viewCount.setText(bean.getTotalPostView() + "");
            } else {
                view_holder._viewCount.setText("");
            }

            if (type != 2) {
                   /*Clickable TextView*/
                if (!bean.getText().equalsIgnoreCase("")) {
                    if (bean.getHashTags().size() > 0) {
                        HashTagHelper mTextHashTagHelper = HashTagHelper.Creator.create(getResources().getColor(R.color.colorPrimary), new HashTagHelper.OnHashTagClickListener() {
                            @Override
                            public void onHashTagClicked(String hashTag) {
//                                Constant.showToast("You clicked on :" + hashTag, getActivity());
                            }
                        });
                        view_holder._comment.setText(bean.getText());
                        // pass a TextView or any descendant of it (incliding EditText) here.
                        // Hash tags that are in the text will be hightlighed with a color passed to HasTagHelper
                        mTextHashTagHelper.handle(view_holder._comment);
                    } else {
                        view_holder._comment.setText(bean.getText());
                    }

                } else {
                    view_holder._comment.setText("");
                }
            }

            view_holder._comment.setMaxLines(4);

            if (bean.getTotalComments() > 0) {
                view_holder._commentCount.setText(bean.getTotalComments() + "");
            } else {
                view_holder._commentCount.setText("");
            }

            if (bean.getLike() > 0) {
                view_holder._likeCount.setText(bean.getLike() + "");
            } else {
                view_holder._likeCount.setText("");
            }

            if (bean.getReherelo() > 0) {
                view_holder._repostCount.setText(bean.getReherelo() + "");
            } else {
                view_holder._repostCount.setText("");
            }

            if (bean.getDislike() > 0) {
                view_holder.sad_TV.setText(bean.getDislike() + "");
            } else {
                view_holder.sad_TV.setText("");
            }


            if (view_holder._Pic != null) {
                if (bean.getPostMedia().size() > 0) {
                    if (bean.getPostMedia().size() > 1) {
                        view_holder.more_images_count_tv.setVisibility(View.VISIBLE);
                        int count = bean.getPostMedia().size() - 1;
                        view_holder.more_images_count_tv.setText("+" + count);
                    } else {
                        view_holder.more_images_count_tv.setVisibility(View.GONE);
                    }

                    view_holder._Pic.setVisibility(View.VISIBLE);
//                    imageLoaderNew.displayImage(bean.getPostMedia().get(0).getUrl(), view_holder._Pic, optionsPost);
                    if (bean.getPostType().equalsIgnoreCase("1")) {
                        view_holder.play_icon.setVisibility(View.GONE);
                        imageLoaderNew.loadImage(bean.getPostMedia().get(0).getUrl(),
                                options, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
//                                view_holder.img_progress.setVisibility(View.VISIBLE);
//                        Toast.makeText(context, "Loading started..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Failed..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Complete..", Toast.LENGTH_SHORT).show();

                                        view_holder._Pic.setImageBitmap(loadedImage);

                                    }
                                });


                    } else if (bean.getPostType().equalsIgnoreCase("2")) {
                        view_holder.play_icon.setVisibility(View.VISIBLE);

                        Logger.logsError(TAG, "ThumbUrl : " + bean.getPostMedia().get(0).getThumb_url());
                        imageLoaderNew.loadImage(bean.getPostMedia().get(0).getThumb_url(),
                                options, new SimpleImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String imageUri, View view) {
//                                view_holder.img_progress.setVisibility(View.VISIBLE);
//                        Toast.makeText(context, "Loading started..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Failed..", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                                view_holder.img_progress.setVisibility(View.GONE);
//                        Toast.makeText(context, "Loading Complete..", Toast.LENGTH_SHORT).show();

                                        view_holder._Pic.setImageBitmap(loadedImage);

                                    }
                                });


                    } else {
                        view_holder.play_icon.setVisibility(View.GONE);
                    }

                } else {
                    view_holder._Pic.setVisibility(View.GONE);
                    view_holder.more_images_count_tv.setVisibility(View.GONE);
                }
            }
            imageLoaderNew.displayImage(bean.getUser().getImage(), view_holder._userPic, options);

/*
            view_holder._Pic.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(getActivity(), com.tv.herelo.HomeTab.MainViewDetails.class);
                    mIntent.putExtra("VIEWCOUNT", bean.getTotalPostView());
                    mIntent.putExtra("COMMENTCOUNT", bean.getTotalComments());
                    mIntent.putExtra("REHERELOCOUNT", bean.getReherelo());
                    mIntent.putExtra("LIKESCOUNT", bean.getLike());
                    mIntent.putExtra("DISLIKESCOUNT", bean.getDislike());
                    mIntent.putExtra("POSTID", bean.getId());
                    mIntent.putExtra("POSTTYPE", bean.getPostType());
                    if (bean.getPostMedia() != null) {
                        if (bean.getPostMedia().size() > 0) {
                            mIntent.putExtra("POSTIMG", bean.getPostMedia().get(0).getUrl());
                        }
                    }

                    startActivity(mIntent);
                }
            });*/


            return convertView;
        }

        class WSTaskFollowUnfollow extends WebserviceTask {

            public WSTaskFollowUnfollow(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {
            /*Progress Dialog here*/
//            Constant.showProgressDialog(mActivity);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + followUnfollowAPIName;
            }

            @Override
            public void onLoadingFinished(String response) {

            }
        }

        //////////////////////////
        private boolean findDoubleClick(View circleBg, ImageView heart, final int position) {
            // Get current time in nano seconds.
            long pressTime = System.currentTimeMillis();


            // If double click...
            if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {

                if (_lastPos == position) {

                    Constant.heart(circleBg, heart);


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

                   /* Intent mIntent = new Intent(getActivity(), MainViewDetails.class);
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
                        mIntent.putExtra("POSITION", 0);
                    } else if (bean.getOriginalPost() != null) {
                        if (bean.getOriginalPost().getPostMedia().size() > 0) {
                            mIntent.putExtra("POSTIMG", bean.getOriginalPost().getPostMedia().get(0).getUrl());
                            mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getOriginalPost().getPostMedia());
                            mIntent.putExtra("POSITION", 0);
                        }
                    }

                    startActivity(mIntent);*/

                    Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);

                    mIntent.putExtra(Constant.POST_DETAILS, bean);
                    mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                    startActivity(mIntent);
                }


            } else {     // If not double click....
                mHasDoubleClicked = false;
                Handler myHandler = new Handler() {
                    public void handleMessage(Message m) {
                        if (!mHasDoubleClicked) {
                            final DatumHome bean = mDataList.get(position);
                            Logger.logsInfo("============>Single click", "Single click");

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
                                mIntent.putExtra("POSITION", 0);
                            } else if (bean.getOriginalPost() != null) {
                                if (bean.getOriginalPost().getPostMedia().size() > 0) {
                                    mIntent.putExtra("POSTIMG", bean.getOriginalPost().getPostMedia().get(0).getUrl());
                                    mIntent.putExtra("POSTIMGLIST", (Serializable) bean.getOriginalPost().getPostMedia());
                                    mIntent.putExtra("POSITION", 0);
                                }
                            }

                            startActivity(mIntent);*/

                            Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                            mIntent.putExtra(Constant.POST_DETAILS, bean);
                            mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDataList);
                            startActivity(mIntent);
                            //                            Toast.makeText(context.getApplicationContext(), "Single Click Event", Toast.LENGTH_SHORT).show();
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
            /*Progress Dialog here*/
//                Constant.showProgressDialog(context);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.like_dislikepost;
            }

            @Override
            public void onLoadingFinished(String response) {

//                Constant.cancelDialog();
                try {

                    if (response != null || !response.equalsIgnoreCase("")) {
                        Gson gson = new Gson();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }

        class DislikeRemoveDislike extends WebserviceTask {

            public DislikeRemoveDislike(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
                super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
            }


            @Override
            public void onLoadingStarted() {
            /*Progress Dialog here*/
//                Constant.showProgressDialog(context);

            }

            @Override
            public String getWebserviceURL() {

                return WebserviceConstant.MAIN_BASE_URL + WebserviceConstant.removeUnlikeDislike;
            }

            @Override
            public void onLoadingFinished(String response) {

//                Constant.cancelDialog();
                try {

                    if (response != null || !response.equalsIgnoreCase("")) {
                        Gson gson = new Gson();

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

            }
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page_no = 1;
        MainTabActivity.other_user_id = 0;


        this_is_other_user = false;
        Logger.logsInfo(TAG, "onDestroyView Called : " + MainTabActivity.other_user_id);
    }
}
