package com.tv.herelo.MyProfile.Action_Activity_Notification;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lantouzi.wheelview.WheelView;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.HomeTab.post_details.PostDetailsScreen;
import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal.ActivityModalDatum;
import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal.ActivityResponseModalMain;
import com.tv.herelo.R;
import com.tv.herelo.XListViewPullToRefresh.XListView;
import com.tv.herelo.bean.SearchChatFriendsHeaderBean;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.settings.addGroups.groupListModal.GroupDataModal;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.utils.HorizontalPicker;
import com.tv.herelo.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shoeb on 08/5/17.
 */
public class ActivityScreen extends Fragment implements View.OnClickListener, XListView.IXListViewListener, HorizontalPicker.OnItemSelected, HorizontalPicker.OnItemClicked {

    private static final String TAG = ActivityScreen.class.getSimpleName();
    private View view;
    private ImageView back_btn;
    private ImageView next_btn;
    private ImageView headerIV;
    private TextView header_tv;

    @Bind(R.id.header_ll)
    LinearLayout header_ll;

    @Bind(R.id.search_ll_new)
    LinearLayout search_ll_new;

    @Bind(R.id.search_et_new)
    EditText search_et_new;

    @Bind(R.id.empty_tv)
    TextView empty_tv;

    @Bind(R.id.progress_bar)
    ProgressBar progress_bar;


    private XListView active_list;

    private LinearLayout activity_ll;
    private LinearLayout friends_ll;
    private LinearLayout tags_ll;
    private RelativeLayout tag_rl;
    private LinearLayout notif_ll;

    private TextView activity_tv;
    private TextView friends_tv;
    private TextView tags_tv;
    private TextView notif_tv;

    private ViewPager viewpager;

    @Bind(R.id.wheeler_view)
    WheelView wheeler_view;

    @Bind(R.id.activity_coverflow)
    HorizontalPicker activity_coverflow;

    @Bind(R.id.tagged_coverflow)
    HorizontalPicker tagged_coverflow;

    @Bind(R.id.activity_rl)
    RelativeLayout activity_rl;

    /*Request Params*/
    private int list_type = 1;
    private int user_id = 0;
    private int page_no = 1;
    private int mCurrentPosOfActivityItem = 0;
    private int mCurrentPosOfTaggedItem = 0;
    private String sub_list = "";
    private String search = "";


    List<SearchChatFriendsHeaderBean> tagSubMenuOptionsList = new ArrayList<>();
    List<ActivityModalDatum> mActivityModalDatumList ;

    /*Share Pref*/
    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;

    /*Controller*/
    private ActivityController mActivityController = new ActivityController(this);

    /*Adapter*/
    private ActivityAdapter mActivityAdapter;


    /*Search */
    /*Declarations and initialization*/
    private Timer timer = new Timer();
    private final long DELAY = 1000; // in ms
    private String keywords = "";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.activity_screen, container, false);

        ButterKnife.bind(this, view);

//        screen = getArguments().get("Screen").toString();

        initView();
        onClickListener();

        return view;
    }


    private void initView() {

        search_et_new.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search_et_new.getRight() - search_et_new.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        search_et_new.setText("");

                        Constant.hideKeyBoard(getActivity());
//                        search_ll_new.setVisibility(View.GONE);
                        return true;
                    }
                }
                return false;
            }
        });
        search_et_new.addTextChangedListener(new TextWatcher() {
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
                keywords = s.toString().trim();

                if (keywords.length() > 0) {

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            // TODO: do what you need here (refresh list)
                            // you will probably need to use
                            // runOnUiThread(Runnable action) for some specific
                            // actions

                            Logger.logsInfo(TAG, "Keywords for search : " + keywords);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
//                                        putBeanIntoFragment(true);
                                        progress_bar.setVisibility(View.VISIBLE);
                                        callWS(false);

                                    } else {
                /*No Internet screen appears*/
                                        Constant.showToast(getResources().getString(R.string.internet), getActivity());

                                    }
                                }
                            });
                        }

                    }, DELAY);
                } else {
                    keywords = "";


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
//                                putBeanIntoFragment(true);
                                progress_bar.setVisibility(View.VISIBLE);
                                callWS(false);

                            } else {
                /*No Internet screen appears*/
                                Constant.showToast(getResources().getString(R.string.internet), getActivity());

                            }
                        }
                    });

                }
            }


        });

        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);


        viewpager = (ViewPager) view.findViewById(R.id.viewpager);

        PagerTitleStrip indicator = (PagerTitleStrip) view.findViewById(R.id.pager_title_strip);
        indicator.setTextSpacing(0);

        /** Getting fragment manager */
        FragmentManager fm = getActivity().getSupportFragmentManager();


        final SearchChatFriendsHeaderBean bean = new SearchChatFriendsHeaderBean();
        bean.setId(1);
        bean.setName(getActivity().getResources().getString(R.string.followingText));
        tagSubMenuOptionsList.add(bean);


        SearchChatFriendsHeaderBean bean1 = new SearchChatFriendsHeaderBean();
        bean1.setId(2);
        bean1.setName(getActivity().getResources().getString(R.string.friendsText));
        tagSubMenuOptionsList.add(bean1);


        SearchChatFriendsHeaderBean bean2 = new SearchChatFriendsHeaderBean();
        bean2.setId(3);
        bean2.setName(getActivity().getResources().getString(R.string.mineText));
        tagSubMenuOptionsList.add(bean2);


        ArrayList<GroupDataModal> mGroupDataModals = Constant.getUserGroups(getActivity());
        for (GroupDataModal mGroupDataModal :
                mGroupDataModals) {
            SearchChatFriendsHeaderBean mSearchChatFriendsHeaderBean = new SearchChatFriendsHeaderBean();
            mSearchChatFriendsHeaderBean.setName(mGroupDataModal.getGroupName());
            mSearchChatFriendsHeaderBean.setId(mGroupDataModal.getId());
            tagSubMenuOptionsList.add(mSearchChatFriendsHeaderBean);
        }

        ArrayList<String> mArrayList = new ArrayList<>();

        for (SearchChatFriendsHeaderBean searchChatFriendsHeaderBean :
                tagSubMenuOptionsList) {

            mArrayList.add(searchChatFriendsHeaderBean.getName());

        }
        //SearchChatFriendsHeaderBean bean = mSearchChatFriendsHeaderBeanList.get(position);
        SearchChatFriendsHeaderBean activity_coverflow_Bean = new SearchChatFriendsHeaderBean();
        activity_coverflow_Bean = tagSubMenuOptionsList.get(0);
        ActivitySubMenuAdapter activitySubMenuAdapter = new ActivitySubMenuAdapter(getActivity(),
                tagSubMenuOptionsList);
        SearchChatFriendsHeaderBean beanbean = tagSubMenuOptionsList.get(mCurrentPosOfActivityItem);


        activity_coverflow.setValues(mArrayList);
        activity_coverflow.setOnItemClickedListener(this);
        activity_coverflow.setOnItemSelectedListener(this);


        tagged_coverflow.setValues(mArrayList);
        tagged_coverflow.setOnItemClickedListener(this);
        tagged_coverflow.setOnItemSelectedListener(this);
        /*activity_coverflow.scrollToPosition(2);
        activity_coverflow.setAdjustPositionMultiplier(2);*/

       /* tagged_coverflow.setAdapter(activitySubMenuAdapter);
        tagged_coverflow.scrollToPosition(2);
        tagged_coverflow.setAdjustPositionMultiplier(2);
        tagged_coverflow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mCurrentPosOfTaggedItem = position;
                Logger.logsError(TAG, "onScrolledToPosition CAlled with  position  : " + position);


                if (list_type == 3) {
                    SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(mCurrentPosOfTaggedItem);
                    switch (bean.getName()) {
                        case Constant.mine:
                            sub_list = "M";
                            break;
                        case Constant.friends:
                            sub_list = "FR";
                            break;
                        case Constant.following_sub_menu:
                            sub_list = "F";
                            break;
                        default:
                            sub_list = bean.getId() + "";
                            break;
                    }
                    if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
//                                 list_type = 2;
                        progress_bar.setVisibility(View.VISIBLE);
//                        putBeanIntoFragment(true);
                        page_no = 1;
                        putBeanIntoFragment(false);
                    } else {
                        Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                    }
                }

            }

            @Override
            public void onScrolling() {

            }
        });*/
     /*   activity_coverflow.setOnScrollPositionListener
                (new FeatureCoverFlow.OnScrollPositionListener() {


                     @Override
                     public void onScrolledToPosition(int position) {
                         mCurrentPosOfActivityItem = position;
                         Logger.logsError(TAG, "onScrolledToPosition CAlled with  position  : " + position);


                         if (list_type == 2) {
                             SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(mCurrentPosOfActivityItem);
                             switch (bean.getName()) {
                                 case Constant.mine:
                                     sub_list = "M";
                                     break;
                                 case Constant.friends:
                                     sub_list = "FR";
                                     break;
                                 case Constant.following_sub_menu:
                                     sub_list = "F";
                                     break;
                                 default:
                                     sub_list = bean.getId() + "";
                                     break;
                             }
                             if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
//                                 list_type = 2;
//                                 putBeanIntoFragment(true);
                                 progress_bar.setVisibility(View.VISIBLE);
                                 page_no = 1;
                                 putBeanIntoFragment(false);
                             } else {
                                 Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                             }
                         }
                     }


                     @Override
                     public void onScrolling() {
                         Logger.logsError(TAG, "onScrolling CAlled");
                     }
                 }*/

        //  );

        List<String> mFInalListNames = new ArrayList<>();
        for (
                SearchChatFriendsHeaderBean beanNEw
                :
                tagSubMenuOptionsList)

        {
            mFInalListNames.add(beanNEw.getName());

        }

        wheeler_view.setItems(mFInalListNames);
//        wheeler_view.selectIndex(mFInalListNames.size()/2);
        wheeler_view.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectedListener()

                                                    {
                                                        @Override
                                                        public void onWheelItemChanged(WheelView wheelView, int position) {
                                                            Logger.logsError(TAG, "Selected Name : " + wheelView.getItems().get(position));
                                                            if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                                                                for (SearchChatFriendsHeaderBean beanNEw :
                                                                        tagSubMenuOptionsList) {
                                                                    if (beanNEw.getName().equalsIgnoreCase(wheelView.getItems().get(position))) {
//                            sub_list = beanNEw.getId();
                                                                        break;
                                                                    }
                                                                }

                                                                list_type = 3;
                                                                callWS(false);
                                                            } else {
                                                                Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                                                            }
                                                        }

                                                        @Override
                                                        public void onWheelItemSelected(WheelView wheelView, int position) {

                                                        }
                                                    }

        );

        /** Instantiating FragmentPagerAdapter */
//        ListPagerAdapter pagerAdapter = new ListPagerAdapter(fm, tagSubMenuOptionsList);

        /** Setting the pagerAdapter to the pager object */
//        viewpager.setAdapter(pagerAdapter);


        activity_ll = (LinearLayout) view.findViewById(R.id.activity_ll);
        friends_ll = (LinearLayout) view.findViewById(R.id.friends_ll);
        tags_ll = (LinearLayout) view.findViewById(R.id.tags_ll);
        tag_rl = (RelativeLayout) view.findViewById(R.id.tag_rl);
        notif_ll = (LinearLayout) view.findViewById(R.id.notif_ll);

        activity_tv = (TextView) view.findViewById(R.id.activity_tv);
        friends_tv = (TextView) view.findViewById(R.id.friends_tv);
        tags_tv = (TextView) view.findViewById(R.id.tags_tv);
        notif_tv = (TextView) view.findViewById(R.id.notif_tv);

        back_btn = (ImageView) view.findViewById(R.id.back_btn);
        next_btn = (ImageView) view.findViewById(R.id.next_btn2);
        headerIV = (ImageView) view.findViewById(R.id.headerIV);
        header_tv = (TextView) view.findViewById(R.id.header_tv);
        next_btn.setVisibility(View.GONE);
        headerIV.setVisibility(View.VISIBLE);
        header_tv.setText(getActivity().getResources().getString(R.string.activityText));
        active_list = (XListView) view.findViewById(R.id.active_list);
        active_list.setXListViewListener(this);
        active_list.setPullRefreshEnable(true);
        active_list.setPullLoadEnable(true);
        active_list.setEmptyView(empty_tv);

        notificationSelected();
        setFonts();
        sPref = getActivity().getSharedPreferences(Constant.SPREF, Context.MODE_PRIVATE);

        editor = sPref.edit();
        user_id = sPref.getInt(Constant.USER_ID_SPREF, 0);

        if (NetworkAvailablity.checkNetworkStatus(getActivity()))

        {
//            putBeanIntoFragment(true);
            progress_bar.setVisibility(View.VISIBLE);
            callWS(false);
        } else

        {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
        }

        active_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivityModalDatum bean  = mActivityModalDatumList.get(position-1);


                Intent mIntent = new Intent(getActivity(), PostDetailsScreen.class);
                DatumHome mDatumHome = new DatumHome();
                mDatumHome.setId(bean.getPostId());
                mDatumHome.setPostType(bean.getPType() + "");
                List<DatumHome> mDatumList = new ArrayList<>();
                mDatumList.add(mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS, mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDatumList);
                startActivity(mIntent);
            }
        });

    }

    private void callWS(boolean isProgressBar) {
        try {
            String sub_list_str = "";
            if (list_type == 1) {
                sub_list_str = "";
            } else {
                sub_list_str = sub_list;

            }
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("list_type", list_type + "")
                    .appendQueryParameter("user_id", user_id + "")
                    .appendQueryParameter("page_no", page_no + "")
                    .appendQueryParameter("sub_list", sub_list_str + "")
                    .appendQueryParameter("search", keywords);
            mActivityController.callWS(builder, isProgressBar);
        } catch (Exception e) {
            Logger.logsError(TAG, e.getMessage());
        }
    }

    private void setFonts() {
        header_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        activity_tv.setTypeface(Constant.typeface(getActivity(), "Helvetica-Normal.ttf"));
        friends_tv.setTypeface(Constant.typeface(getActivity(), "Helvetica-Normal.ttf"));
        tags_tv.setTypeface(Constant.typeface(getActivity(), "Helvetica-Normal.ttf"));
        notif_tv.setTypeface(Constant.typeface(getActivity(), "Helvetica-Normal.ttf"));
        search_et_new.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Lt.otf"));
        empty_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-Lt.otf"));
    }

    private void onClickListener() {

        back_btn.setOnClickListener(this);
        activity_ll.setOnClickListener(this);
        friends_ll.setOnClickListener(this);
        tags_ll.setOnClickListener(this);
        notif_ll.setOnClickListener(this);
        header_ll.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.header_ll:
                progress_bar.setVisibility(View.GONE);
                if (search_ll_new.getVisibility() == View.VISIBLE) {
                    search_ll_new.setVisibility(View.GONE);
                } else {
                    search_ll_new.setVisibility(View.VISIBLE);
                }


                break;
            case R.id.activity_ll:
                progress_bar.setVisibility(View.GONE);
                activitySelected();

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(mCurrentPosOfActivityItem);
                    switch (bean.getName()) {
                        case Constant.mine:
                            sub_list = "M";
                            break;
                        case Constant.friends:
                            sub_list = "FR";
                            break;
                        case Constant.following_sub_menu:
                            sub_list = "F";
                            break;
                        default:
                            sub_list = bean.getId() + "";
                            break;
                    }

                    list_type = 2;
                    page_no = 1;
//                    putBeanIntoFragment(true);
                    progress_bar.setVisibility(View.VISIBLE);
                    callWS(false);
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }
                break;
            case R.id.friends_ll:
                progress_bar.setVisibility(View.GONE);

                mineSelected();

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    list_type = 2;
                    page_no = 1;
//                    putBeanIntoFragment(true);
                    progress_bar.setVisibility(View.VISIBLE);
                    callWS(false);
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }
                break;
            case R.id.tags_ll:
                progress_bar.setVisibility(View.GONE);

                tagsSelected();


                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(mCurrentPosOfActivityItem);
                    switch (bean.getName()) {
                        case Constant.mine:
                            sub_list = "M";
                            break;
                        case Constant.friends:
                            sub_list = "FR";
                            break;
                        case Constant.following_sub_menu:
                            sub_list = "F";
                            break;
                        default:
                            sub_list = bean.getId() + "";
                            break;
                    }

                    list_type = 3;
                    page_no = 1;
//                    putBeanIntoFragment(true);
                    progress_bar.setVisibility(View.VISIBLE);
                    callWS(false);
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }
                break;
            case R.id.notif_ll:
                progress_bar.setVisibility(View.GONE);
                notificationSelected();

                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    list_type = 1;
                    page_no = 1;
//                    putBeanIntoFragment(true);
                    progress_bar.setVisibility(View.VISIBLE);
                    callWS(false);
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }

                break;
            case R.id.back_btn:
                MainTabActivity.backbutton();
                break;
            default:
                progress_bar.setVisibility(View.GONE);
                break;
        }

    }


    private void activitySelected() {
        activity_ll.setBackgroundResource(R.drawable.white_middle_box_);
        friends_ll.setBackgroundColor(Color.TRANSPARENT);
        tags_ll.setBackgroundColor(Color.TRANSPARENT);
        notif_ll.setBackgroundColor(Color.TRANSPARENT);

        activity_tv.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
        friends_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        tags_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        notif_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        tag_rl.setVisibility(View.GONE);
        active_list.setVisibility(View.VISIBLE);
        activity_rl.setVisibility(View.VISIBLE);
    }

    private void notificationSelected() {
        notif_ll.setBackgroundResource(R.drawable.white_left_box_);
        activity_ll.setBackgroundColor(Color.TRANSPARENT);
        friends_ll.setBackgroundColor(Color.TRANSPARENT);
        tags_ll.setBackgroundColor(Color.TRANSPARENT);

        notif_tv.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
        activity_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        friends_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        tags_tv.setTextColor(getActivity().getResources().getColor(R.color.white));

        tag_rl.setVisibility(View.GONE);
        activity_rl.setVisibility(View.GONE);
        active_list.setVisibility(View.VISIBLE);

    }

    private void tagsSelected() {
        tags_ll.setBackgroundResource(R.drawable.white_middle_box_);
        activity_ll.setBackgroundColor(Color.TRANSPARENT);
        friends_ll.setBackgroundColor(Color.TRANSPARENT);
        notif_ll.setBackgroundColor(Color.TRANSPARENT);

        tags_tv.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
        activity_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        friends_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        notif_tv.setTextColor(getActivity().getResources().getColor(R.color.white));

        tag_rl.setVisibility(View.VISIBLE);
        activity_rl.setVisibility(View.GONE);
        active_list.setVisibility(View.VISIBLE);
    }

    private void mineSelected() {

        friends_ll.setBackgroundResource(R.drawable.white_middle_box_);
        activity_ll.setBackgroundColor(Color.TRANSPARENT);
        tags_ll.setBackgroundColor(Color.TRANSPARENT);
        notif_ll.setBackgroundColor(Color.TRANSPARENT);

        friends_tv.setTextColor(getActivity().getResources().getColor(R.color.text_gray));
        activity_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        tags_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        notif_tv.setTextColor(getActivity().getResources().getColor(R.color.white));
        tag_rl.setVisibility(View.GONE);
        activity_rl.setVisibility(View.GONE);
        active_list.setVisibility(View.VISIBLE);
    }

    public void responseOfActivity(ActivityResponseModalMain modal) {



        active_list.stopLoadMore();
        active_list.stopRefresh();
        progress_bar.setVisibility(View.GONE);

        if (modal.getStatus() == 1) {
            if (modal.getData().size() >= 10) {
                active_list.setPullLoadEnable(true);
            } else {
                active_list.setPullLoadEnable(false);
            }
            if (page_no==1){
                mActivityModalDatumList= new ArrayList<>();
                mActivityModalDatumList.clear();
            }

            mActivityModalDatumList.addAll(modal.getData());


            if (page_no == 1) {
                mActivityAdapter = new ActivityAdapter(getActivity(), mActivityModalDatumList, ActivityScreen.this, null);
                active_list.setAdapter(mActivityAdapter);
            } else {

                mActivityAdapter.addMoreData(mActivityModalDatumList);
            }

        } else {

            if (page_no == 1) {
                mActivityModalDatumList= new ArrayList<>();

                empty_tv.setText(modal.getMsg());
                mActivityAdapter = new ActivityAdapter(getActivity(), mActivityModalDatumList, ActivityScreen.this, null);
                active_list.setAdapter(mActivityAdapter);
            }
//            mActivityAdapter = new ActivityAdapter(getActivity(), modal.getData(), ActivityScreen.this, null);
//            active_list.setAdapter(mActivityAdapter);
        }


    }

    @Override
    public void onRefresh() {
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            page_no = 1;
            callWS(false);
        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
        }
    }

    @Override
    public void onLoadMore() {
        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            page_no = page_no + 1;
            callWS(false);
        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
        }
    }

    @Override
    public void onItemSelected(int index) {
        SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(index);
        if (list_type == 2) {
            activitySelected();
        } else if (list_type == 3) {
            tagsSelected();
        }
        switch (bean.getName()) {
            case Constant.mine:
                sub_list = "M";
                break;
            case Constant.friends:
                sub_list = "FR";
                break;
            case Constant.following_sub_menu:
                sub_list = "F";
                break;
            default:
                sub_list = bean.getId() + "";
                break;
        }

        page_no = 1;
        progress_bar.setVisibility(View.VISIBLE);
        callWS(false);
    }

    @Override
    public void onItemClicked(int index) {
        SearchChatFriendsHeaderBean bean = tagSubMenuOptionsList.get(index);
        switch (bean.getName()) {
            case Constant.mine:
                sub_list = "M";
                break;
            case Constant.friends:
                sub_list = "FR";
                break;
            case Constant.following_sub_menu:
                sub_list = "F";
                break;
            default:
                sub_list = bean.getId() + "";
                break;
        }
        page_no = 1;
        progress_bar.setVisibility(View.VISIBLE);
        callWS(false);
    }


    /***
     * Class for Activity View Wheeler
     */
    class ActivitySubMenuAdapter extends BaseAdapter {
        private Activity mActivity;

        private List<SearchChatFriendsHeaderBean> mSearchChatFriendsHeaderBeanList = new ArrayList<>();

        public ActivitySubMenuAdapter(Activity mActivity, List<SearchChatFriendsHeaderBean> mSearchChatFriendsHeaderBeanList) {
            this.mActivity = mActivity;
            this.mSearchChatFriendsHeaderBeanList = mSearchChatFriendsHeaderBeanList;
        }

        @Override
        public int getCount() {
            return mSearchChatFriendsHeaderBeanList.size();
        }

        @Override
        public Object getItem(int position) {
            return mSearchChatFriendsHeaderBeanList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        public class ViewHolder {
            public TextView mOptionValue;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder view_holder;

            if (convertView == null) {
                view_holder = new ViewHolder();
                LayoutInflater _linflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
                convertView = _linflater.inflate(R.layout.text_activity_sub_menu_options_row, null);
                view_holder.mOptionValue = (TextView) convertView.findViewById(R.id.value_tv);
                convertView.setTag(view_holder);

            } else {
                view_holder = (ViewHolder) convertView.getTag();
            }


            SearchChatFriendsHeaderBean bean = mSearchChatFriendsHeaderBeanList.get(position);
            view_holder.mOptionValue.setText(bean.getName());
            view_holder.mOptionValue.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-LtEx.otf"));


            return convertView;

        }
    }


}
