package com.tv.herelo.MyProfile.SearchForPeople;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.herelo.R;
import com.tv.herelo.XListViewPullToRefresh.XListView;
import com.tv.herelo.adapter.FindFriendsAdapter;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.login.FindSocialFriends.DatumFindSocialFriends;
import com.tv.herelo.login.FindSocialFriends.FindSocialFriendModal;
import com.tv.herelo.tab.MainTabActivity;

import java.util.List;

/**
 * Created by shoeb on 15/7/16.
 */
public class SearchForPeopleResultFragment extends Fragment implements View.OnClickListener, XListView.IXListViewListener {
    private View view = null;
    private XListView search_list_result;


    /*Header*/
    private ImageView back_btn = null;
    private ImageView headerIV = null;
    private ImageView next_btn2 = null;
    private ImageView _next1IV = null;
    private TextView header_tv = null;


    /*Controller*/
    private SearchForPeopleController mController = new SearchForPeopleController(this);

    /*Request Params*/
    private int page_no = 1;
    private int user_id = 0;


    /*Shared Pref*/

    private SharedPreferences sPref = null;
    private SharedPreferences.Editor editor;


    private String around_me = "";
    private String lat = "";
    private String lng = "";
    private String dob = "";
    private String birth_state = "";
    private String birth_city = "";
    private String about_user = "";
    private String status = "";
    private String high_school = "";
    private String college = "";
    private String fav_movie = "";
    private String movie_gen = "";
    private String fav_tv_show = "";
    private String fav_band = "";
    private String music_gen = "";
    private String fav_team = "";
    private String fav_sport = "";
    private String first_name = "";
    private String last_name = "";
    private String username = "";
    private String email = "";

    Uri.Builder builder = null;
    List<DatumFindSocialFriends> mDatalist;
    FindFriendsAdapter friendsAdapter;

    public void responseHandle(SearchForPeopleModal modal) {

        int status = modal.getStatus();
        if (status == 1) {
            if (modal.getIsMore().equalsIgnoreCase("Yes")) {
                search_list_result.setPullLoadEnable(true);

            } else {
                search_list_result.setPullLoadEnable(false);
            }
            mDatalist = modal.getData();
            if (mDatalist != null) {
                if (mDatalist.size() > 0) {
                /*Set Adapter*/
                    friendsAdapter = new FindFriendsAdapter(getActivity(), mDatalist);
                    search_list_result.setAdapter(friendsAdapter);
                    search_list_result.stopRefresh();
                    search_list_result.stopLoadMore();
                }
            }
        } else {
            Constant.showToast(modal.getMessage(), getActivity());
            MainTabActivity.backbutton();
        }


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchforpeople_result_frag, container, false);
        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);
        sPref = getActivity().getSharedPreferences(Constant.SPREF, Context.MODE_PRIVATE);
        user_id = sPref.getInt(Constant.USER_ID_SPREF, 0);
        editor = sPref.edit();
        initView();
        return view;
    }

    private void initView() {
        search_list_result = (XListView) view.findViewById(R.id.search_list_result);
        search_list_result.setXListViewListener(this);
        search_list_result.setPullRefreshEnable(true);
        back_btn = (ImageView) view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        headerIV = (ImageView) view.findViewById(R.id.headerIV);
        headerIV.setVisibility(View.GONE);
        next_btn2 = (ImageView) view.findViewById(R.id.next_btn2);
        next_btn2.setVisibility(View.GONE);
        header_tv = (TextView) view.findViewById(R.id.header_tv);
        header_tv.setText(getActivity().getResources().getString(R.string.profileSearchResText));
        _next1IV = (ImageView) view.findViewById(R.id.next_btn);
        _next1IV.setVisibility(View.VISIBLE);
        _next1IV.setOnClickListener(this);

        _next1IV.setImageResource(R.drawable.reherelo_done);
        if (getArguments() != null) {
            if (getArguments().containsKey("around_me")) {
                builder = new Uri.Builder();
                around_me = getArguments().getString("around_me");
                lat = getArguments().getString("lat");
                lng = getArguments().getString("lng");
                dob = getArguments().getString("dob");
                birth_state = getArguments().getString("birth_state");
                birth_city = getArguments().getString("birth_city");
                about_user = getArguments().getString("about_user");
                status = getArguments().getString("status");
                high_school = getArguments().getString("high_school");
                college = getArguments().getString("college");
                fav_movie = getArguments().getString("fav_movie");
                movie_gen = getArguments().getString("movie_gen");
                fav_tv_show = getArguments().getString("fav_tv_show");
                fav_band = getArguments().getString("fav_band");
                music_gen = getArguments().getString("music_gen");
                fav_team = getArguments().getString("fav_team");
                fav_sport = getArguments().getString("fav_sport");
                first_name = getArguments().getString("first_name");
                last_name = getArguments().getString("last_name");
                username = getArguments().getString("username");
                email = getArguments().getString("email");
                builder.appendQueryParameter("page_no", page_no + "")
                        .appendQueryParameter("user_id", user_id + "")
                        .appendQueryParameter("around_me", around_me)
                        .appendQueryParameter("lat", lat)
                        .appendQueryParameter("lng", lng)
                        .appendQueryParameter("dob", dob)
                        .appendQueryParameter("birth_state", birth_state)
                        .appendQueryParameter("birth_city", birth_city)
                        .appendQueryParameter("about_user", about_user)
                        .appendQueryParameter("status", status)
                        .appendQueryParameter("high_school", high_school)
                        .appendQueryParameter("college", college)
                        .appendQueryParameter("fav_movie", fav_movie)
                        .appendQueryParameter("movie_gen", movie_gen)
                        .appendQueryParameter("fav_tv_show", fav_tv_show)
                        .appendQueryParameter("fav_band", fav_band)
                        .appendQueryParameter("music_gen", music_gen)
                        .appendQueryParameter("fav_team", fav_team)
                        .appendQueryParameter("fav_sport", fav_sport)
                        .appendQueryParameter("first_name", first_name)
                        .appendQueryParameter("last_name", last_name)
                        .appendQueryParameter("username", username)
                        .appendQueryParameter("email", email)
                ;
                mController.callSearchForPeopleWS(builder,true);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                MainTabActivity.backbutton();
                break;
            case R.id.next_btn:
                /*Redirect to Following Screen*/
                MainTabActivity.backbutton();
                MainTabActivity.backbutton();
                break;
            default:
                break;
        }

    }

    @Override
    public void onRefresh() {
        if (builder != null) {
            page_no = 1;
            builder.appendQueryParameter("page_no", page_no + "")
                    .appendQueryParameter("user_id", user_id + "")
                    .appendQueryParameter("around_me", around_me)
                    .appendQueryParameter("lat", lat)
                    .appendQueryParameter("lng", lng)
                    .appendQueryParameter("dob", dob)
                    .appendQueryParameter("birth_state", birth_state)
                    .appendQueryParameter("birth_city", birth_city)
                    .appendQueryParameter("about_user", about_user)
                    .appendQueryParameter("status", status)
                    .appendQueryParameter("high_school", high_school)
                    .appendQueryParameter("college", college)
                    .appendQueryParameter("fav_movie", fav_movie)
                    .appendQueryParameter("movie_gen", movie_gen)
                    .appendQueryParameter("fav_tv_show", fav_tv_show)
                    .appendQueryParameter("fav_band", fav_band)
                    .appendQueryParameter("music_gen", music_gen)
                    .appendQueryParameter("fav_team", fav_team)
                    .appendQueryParameter("fav_sport", fav_sport)
                    .appendQueryParameter("first_name", first_name)
                    .appendQueryParameter("last_name", last_name)
                    .appendQueryParameter("username", username)
                    .appendQueryParameter("email", email)
            ;
            mController.callSearchForPeopleWS(builder,false);
        }

    }

    @Override
    public void onLoadMore() {
        if (builder != null) {
            page_no = page_no+ 1;
            builder.appendQueryParameter("page_no", page_no + "")
                    .appendQueryParameter("user_id", user_id + "")
                    .appendQueryParameter("around_me", around_me)
                    .appendQueryParameter("lat", lat)
                    .appendQueryParameter("lng", lng)
                    .appendQueryParameter("dob", dob)
                    .appendQueryParameter("birth_state", birth_state)
                    .appendQueryParameter("birth_city", birth_city)
                    .appendQueryParameter("about_user", about_user)
                    .appendQueryParameter("status", status)
                    .appendQueryParameter("high_school", high_school)
                    .appendQueryParameter("college", college)
                    .appendQueryParameter("fav_movie", fav_movie)
                    .appendQueryParameter("movie_gen", movie_gen)
                    .appendQueryParameter("fav_tv_show", fav_tv_show)
                    .appendQueryParameter("fav_band", fav_band)
                    .appendQueryParameter("music_gen", music_gen)
                    .appendQueryParameter("fav_team", fav_team)
                    .appendQueryParameter("fav_sport", fav_sport)
                    .appendQueryParameter("first_name", first_name)
                    .appendQueryParameter("last_name", last_name)
                    .appendQueryParameter("username", username)
                    .appendQueryParameter("email", email)
            ;
            mController.callSearchForPeopleWS(builder,false);
        }

    }
}
