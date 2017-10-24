package com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList.VotingListModal.VotingListDatumModal;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.theme.ThemeBlue;
import com.tv.herelo.theme.ThemeGreen;
import com.tv.herelo.theme.ThemeManager;
import com.tv.herelo.theme.ThemeMaroon;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

public class VotingListActivityScreen extends Activity implements View.OnClickListener {
    private final static String TAG = VotingListActivityScreen.class.getSimpleName();

    @Bind(R.id.headerLLMain)
    LinearLayout headerLLMain;

    @Bind(R.id.back_btn)
    ImageView back_btn;

    @Bind(R.id.headerIV)
    ImageView headerIV;

    @Bind(R.id.header_tv)
    TextView header_tv;

    @Bind(R.id.hereLo_prop_tv)
    TextView hereLo_prop_tv;

    @Bind(R.id.hereLo_prop_ll)
    LinearLayout hereLo_prop_ll;

    @Bind(R.id.user_submitted_tv)
    TextView user_submitted_tv;

    @Bind(R.id.user_submitted_ll)
    LinearLayout user_submitted_ll;

    @Bind(R.id.list)
    StickyListHeadersListView mVotingListView;

    /*ADapter*/
    private VotingListAdapter mVotingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.getThemeIdFromPref(VotingListActivityScreen.this) == 1) {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        } else if (Constant.getThemeIdFromPref(VotingListActivityScreen.this) == 2) {
            ThemeManager.Set(new ThemeMaroon());
            setTheme(R.style.AppThemeMaroon);
        } else if (Constant.getThemeIdFromPref(VotingListActivityScreen.this) == 3) {
            ThemeManager.Set(new ThemeGreen());
            setTheme(R.style.AppThemeGreen);
        } else {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.activity_voting_list_activity_screen);
        ButterKnife.bind(this);

        init();
        onClicks();
        setFonts();


    }


    private void setFonts() {
        header_tv.setTypeface(Constant.typeface(this, "HelveticaNeueLTStd-LtEx.otf"));
        user_submitted_tv.setTypeface(Constant.typeface(this, "Helvetica-Normal.ttf"));
        hereLo_prop_tv.setTypeface(Constant.typeface(this, "Helvetica-Normal.ttf"));


    }

    private void onClicks() {
        back_btn.setOnClickListener(this);
        hereLo_prop_ll.setOnClickListener(this);
        user_submitted_ll.setOnClickListener(this);
    }

    private void init() {
        Constant.changeHeaderColor(headerLLMain);
        headerIV.setVisibility(View.VISIBLE);
        header_tv.setText(getResources().getString(R.string.votingText));
        hereLoPropSelected();

        dumpDummyDataMandatory();

    }

    private void dumpDummyDataMandatory() {

        List<VotingListDatumModal> list = new ArrayList<>();

        list.add(new VotingListDatumModal("Add A Dislike Button", "Vote Yes or No", "mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No", "mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
/*

        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));

*/

        list.add(new VotingListDatumModal("Allow Animation gif's for profile pictures", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Celebrity verification  tags", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow business account", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
/*

        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
*/

        mVotingListAdapter = new VotingListAdapter(this, list);
        mVotingListView.setAdapter(mVotingListAdapter);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.hereLo_prop_ll:
                hereLoPropSelected();
                dumpDummyDataMandatory();
                break;
            case R.id.user_submitted_ll:
                userSubmittedSelected();
                dumpDummyDataFeatures();
                break;
            default:
                break;
        }

    }

    private void dumpDummyDataFeatures() {

       /* VotingListDatumModal bean1 = new VotingListDatumModal("Add Blog Section",
                "Vote Yes or No"
                , "mandatory",
                "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg");

        VotingListDatumModal bean2 = new VotingListDatumModal("Add Blog Section",
                "Vote Yes or No"
                , "mandatory",
                "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg");


        VotingListDemo bean3 = new VotingListDemo("Add Blog Section",
                "Vote Yes or No"
                , "mandatory",
                "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg");
        if (bean1.hashCode()==bean2.hashCode()){
            Logger.logsError(TAG, "Hash Code is same");
        }else{
            Logger.logsError(TAG, "Hash Code is Diff");
        }
        if (bean1.equals(bean2)) {
            Logger.logsError(TAG, "same");
        } else {
            Logger.logsError(TAG, "diff");
        }*/
/*
        if (bean1.equals(bean3)) {
            Logger.logsError(TAG, "same");
        } else {
            Logger.logsError(TAG, "diff");
        }*/

        List<VotingListDatumModal> list = new ArrayList<>();


/*

        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Add Blog Section", "Vote Yes or No","mandatory", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));

*/

        list.add(new VotingListDatumModal("Allow Animation gif's for profile pictures", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Celebrity verification  tags", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow business account", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features", "features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
/*

        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
        list.add(new VotingListDatumModal("Allow political account", "Vote for up to 2 new features","features", "https://daveweasel.files.wordpress.com/2015/09/ct-facebook-dislike-bsi-20150915.jpg"));
*/

        mVotingListAdapter = new VotingListAdapter(this, list);
        mVotingListView.setAdapter(mVotingListAdapter);


    }

    private void userSubmittedSelected() {
        user_submitted_ll.setBackgroundResource(R.drawable.white_right_box_);
        hereLo_prop_ll.setBackgroundColor(Color.TRANSPARENT);


        user_submitted_tv.setTextColor(getResources().getColor(R.color.text_gray));
        hereLo_prop_tv.setTextColor(getResources().getColor(R.color.white));

    }

    private void hereLoPropSelected() {
        hereLo_prop_ll.setBackgroundResource(R.drawable.white_left_box_);
        user_submitted_ll.setBackgroundColor(Color.TRANSPARENT);


        hereLo_prop_tv.setTextColor(getResources().getColor(R.color.text_gray));
        user_submitted_tv.setTextColor(getResources().getColor(R.color.white));

    }
}
