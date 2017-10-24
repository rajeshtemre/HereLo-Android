package com.tv.herelo.MyProfile.YourHereLo;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.tv.herelo.MyProfile.YourHereLo.MyShares.ModalsMySharesMain.MySharesModalMain;
import com.tv.herelo.MyProfile.YourHereLo.MyShares.MySharesController;
import com.tv.herelo.MyProfile.YourHereLo.MyShares.YourReferralsShareAdapter;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsDetailsActivity;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsMainController;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsMainModals.NewsMainDatum;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsMainModals.NewsModalMain;
import com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList.VotingListActivityScreen;
import com.tv.herelo.R;
import com.tv.herelo.adapter.NewsListAdapter;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shoeb on 26/9/15.
 */
public class YourHereLoScreen extends Fragment implements View.OnClickListener {

    private static final String TAG = YourHereLoScreen.class.getSimpleName();
    View view;
    ImageView back_btn;

    private ImageView headerIV;
    TextView header_tv;

    ImageView next_btn;
    ListView yerelo_list;

    ImageView share_iv;
    ImageView votes_iv;
    ImageView news_iv;
    ImageView others_iv;

    TextView text_top_tv;

    Typeface header_tf;
    Typeface mid_tf;

    @Bind(R.id.breaking_news_ll)
    LinearLayout breaking_news_ll;

    @Bind(R.id.news_ll)
    LinearLayout news_ll;

    @Bind(R.id.share_ll)
    LinearLayout share_ll;

    @Bind(R.id.total_shares_tv)
    TextView total_shares_tv;

    @Bind(R.id.monthly_share_tv)
    TextView monthly_share_tv;

    @Bind(R.id.monthly_share_info_tv)
    TextView monthly_share_info_tv;

    @Bind(R.id.referral_share_tv)
    TextView referral_share_tv;

    @Bind(R.id.referral_share_info_tv)
    TextView referral_share_info_tv;

    @Bind(R.id.total_referral_tv)
    TextView total_referral_tv;

    @Bind(R.id.total_referral_info_tv)
    TextView total_referral_info_tv;

    @Bind(R.id.refermoreFriends_tv)
    TextView refermoreFriends_tv;

    @Bind(R.id.msg_title_tv)
    TextView msg_title_tv;

    @Bind(R.id.msg_detail_tv)
    TextView msg_detail_tv;

    @Bind(R.id.yourReferralsInfo_tv)
    TextView yourReferralsInfo_tv;

    @Bind(R.id.referrals_listview)
    ListView referrals_listview;

    private MySharesController mMySharesController = new MySharesController(this);
    private NewsMainController mNewsMainController = new NewsMainController(this);

    /*Request Params*/
    private int page_no = 1;

    /*News*/
    private List<NewsMainDatum> mNewsMainDatumList = new ArrayList<>();


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.your_herelo, container, false);
        ButterKnife.bind(this, view);


        header_tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLTStd-LtEx.otf");
        mid_tf = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaNeueLTStd-Md.otf");

        initView();
        onClickListener();

        return view;
    }

    private void initView() {

        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);
        back_btn = (ImageView) view.findViewById(R.id.back_btn);
        headerIV = (ImageView) view.findViewById(R.id.headerIV);

        header_tv = (TextView) view.findViewById(R.id.header_tv);
        header_tv.setText("Your HereLo");

        next_btn = (ImageView) view.findViewById(R.id.next_btn2);

        next_btn.setVisibility(View.GONE);
        headerIV.setVisibility(View.GONE);


        share_iv = (ImageView) view.findViewById(R.id.share_iv);
        votes_iv = (ImageView) view.findViewById(R.id.votes_iv);
        news_iv = (ImageView) view.findViewById(R.id.news_iv);
        others_iv = (ImageView) view.findViewById(R.id.others_iv);

        yerelo_list = (ListView) view.findViewById(R.id.yerelo_list);
        yerelo_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsMainDatum bean = mNewsMainDatumList.get(position);
                Intent mIntent = new Intent(getActivity(), NewsDetailsActivity.class);
                mIntent.putExtra(Constant.NEWS_DETAILS, bean);
                startActivity(mIntent);
            }
        });

        text_top_tv = (TextView) view.findViewById(R.id.text_top_tv);
        text_top_tv.setSelected(true);

        setFonts();


        if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
            callMyShareWS(true);
        } else {
            Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
        }
    }

    private void callMyShareWS(boolean isProgressBarRequired) {
        try {
            Uri.Builder mBuilder = new Uri.Builder()
                    .appendQueryParameter("user_id", MySharedPreference.getCurrentUserId(getActivity()) + "")
                    .appendQueryParameter("page_no", page_no + "");
            mMySharesController.callWS(mBuilder, isProgressBarRequired);

        } catch (Exception e) {
            Logger.logsError(TAG, e.getMessage());
        }
    }

    private void setFonts() {
        header_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        total_shares_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        monthly_share_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        monthly_share_info_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        referral_share_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        referral_share_info_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        total_referral_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));
        total_referral_info_tv.setTypeface(Constant.typeface(getActivity(), "HelveticaNeueLTStd-LtEx.otf"));

        text_top_tv.setTypeface(mid_tf);
        msg_title_tv.setTypeface(mid_tf);
        msg_detail_tv.setTypeface(mid_tf);
        header_tv.setTypeface(header_tf);
        refermoreFriends_tv.setTypeface(header_tf);
        yourReferralsInfo_tv.setTypeface(header_tf);
    }


    private void onClickListener() {
        back_btn.setOnClickListener(this);

        share_iv.setOnClickListener(this);
        votes_iv.setOnClickListener(this);
        news_iv.setOnClickListener(this);
        others_iv.setOnClickListener(this);

        share_iv.setImageResource(R.drawable.shares);
        votes_iv.setImageResource(R.drawable.vote1);
        news_iv.setImageResource(R.drawable.news1);
        others_iv.setImageResource(R.drawable.other);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:

                MainTabActivity.backbutton();

                break;

            case R.id.share_iv:
                share_iv.setImageResource(R.drawable.shares);
                votes_iv.setImageResource(R.drawable.vote1);
                news_iv.setImageResource(R.drawable.news1);
                others_iv.setImageResource(R.drawable.other);
                breaking_news_ll.setVisibility(View.GONE);
                news_ll.setVisibility(View.GONE);
                share_ll.setVisibility(View.VISIBLE);

                break;

            case R.id.votes_iv:

               /* share_iv.setImageResource(R.drawable.shares1);
                votes_iv.setImageResource(R.drawable.vote);
                news_iv.setImageResource(R.drawable.news1);
                others_iv.setImageResource(R.drawable.other);
                breaking_news_ll.setVisibility(View.GONE);
                news_ll.setVisibility(View.GONE);
                share_ll.setVisibility(View.GONE);*/

                share_iv.setImageResource(R.drawable.shares);
                votes_iv.setImageResource(R.drawable.vote1);
                news_iv.setImageResource(R.drawable.news1);
                others_iv.setImageResource(R.drawable.other);
                breaking_news_ll.setVisibility(View.GONE);
                news_ll.setVisibility(View.GONE);
                share_ll.setVisibility(View.VISIBLE);

                Intent mIntent = new Intent(getActivity(), VotingListActivityScreen.class);
                startActivity(mIntent);

                break;
            case R.id.news_iv:
                share_iv.setImageResource(R.drawable.shares1);
                votes_iv.setImageResource(R.drawable.vote1);
                news_iv.setImageResource(R.drawable.news);
                others_iv.setImageResource(R.drawable.other);
                breaking_news_ll.setVisibility(View.VISIBLE);
                news_ll.setVisibility(View.VISIBLE);
                share_ll.setVisibility(View.GONE);


                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {
                    callNewsMain(true);
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }

                break;

            case R.id.others_iv:

                share_iv.setImageResource(R.drawable.shares1);
                votes_iv.setImageResource(R.drawable.vote1);
                news_iv.setImageResource(R.drawable.news1);
                others_iv.setImageResource(R.drawable.other1);
                breaking_news_ll.setVisibility(View.GONE);
                news_ll.setVisibility(View.GONE);
                share_ll.setVisibility(View.GONE);

                break;
            default:
                break;
        }
    }

    private void callNewsMain(boolean isProgress) {
        try {

            mNewsMainController.callWS(isProgress);

        } catch (Exception e) {
            Logger.logsError(TAG, e.getMessage());
        }
    }

    public void responseMyShares(MySharesModalMain modal) {

        if (modal.getStatus() == 1) {
            populateShareData(modal);
        }

    }

    private void populateShareData(MySharesModalMain modal) {

        total_shares_tv.setText(modal.getTotalshare() + "\n" + getActivity().getResources().getString(R.string.totalSharesText));
        monthly_share_tv.setText(modal.getMnthshare() + "");
        referral_share_tv.setText(modal.getRefralshare() + "");
        total_referral_tv.setText(modal.getTotalrefral() + "");

        if (modal.getData().size() > 0) {
            YourReferralsShareAdapter mYourReferralsShareAdapter = new YourReferralsShareAdapter(getActivity(),
                    modal.getData());
            referrals_listview.setAdapter(mYourReferralsShareAdapter);
        }
    }

    public void responseNewsMain(NewsModalMain modal) {
        if (modal.getStatus() == 1) {
            populateMainNewsData(modal);
        }


    }

    private void populateMainNewsData(NewsModalMain modal) {
        text_top_tv.setText(modal.getHeading());
        text_top_tv.setSelected(true);

        msg_title_tv.setText(modal.getTitle());

        msg_detail_tv.setText(modal.getText());

        if (modal.getData().size() > 0) {
            mNewsMainDatumList = modal.getData();

            NewsListAdapter adapter = new NewsListAdapter(getActivity(), modal.getData());
            yerelo_list.setAdapter(adapter);
        }
    }
}
