package com.tv.herelo.MyProfile.YourHereLo.News;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsDetailsModals.NewsDetailsModalMain;
import com.tv.herelo.MyProfile.YourHereLo.News.NewsMainModals.NewsMainDatum;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.theme.ThemeBlue;
import com.tv.herelo.theme.ThemeGreen;
import com.tv.herelo.theme.ThemeManager;
import com.tv.herelo.theme.ThemeMaroon;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shoeb on 18/5/17.
 */
public class NewsDetailsActivity extends Activity implements View.OnClickListener {

    private static final String TAG = NewsDetailsActivity.class.getSimpleName();

    @Bind(R.id.headerLLMain)
    LinearLayout headerLLMain;

    @Bind(R.id.back_btn)
    ImageView back_btn;

    @Bind(R.id.headerIV)
    ImageView headerIV;

    @Bind(R.id.news_main_iv)
    SelectableRoundedImageView news_main_iv;

    @Bind(R.id.header_tv)
    TextView header_tv;

    @Bind(R.id.news_title_tv)
    TextView news_title_tv;

    @Bind(R.id.news_short_text_tv)
    TextView news_short_text_tv;

    @Bind(R.id.news_webview)
    WebView news_webview;


    private NewsDetailsController mNewsDetailsController = new NewsDetailsController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Constant.getThemeIdFromPref(NewsDetailsActivity.this) == 1) {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        } else if (Constant.getThemeIdFromPref(NewsDetailsActivity.this) == 2) {
            ThemeManager.Set(new ThemeMaroon());
            setTheme(R.style.AppThemeMaroon);
        } else if (Constant.getThemeIdFromPref(NewsDetailsActivity.this) == 3) {
            ThemeManager.Set(new ThemeGreen());
            setTheme(R.style.AppThemeGreen);
        } else {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        }
        setContentView(R.layout.news_details_screen);

        ButterKnife.bind(this);

        init();
        onClicks();
        setFonts();

        if (getIntent() != null) {
            if (getIntent().hasExtra(Constant.NEWS_DETAILS)) {
                NewsMainDatum bean = (NewsMainDatum) getIntent().getSerializableExtra(Constant.NEWS_DETAILS);
                if (NetworkAvailablity.checkNetworkStatus(this)) {
                    callWS(bean, true);
                } else {
                    Constant.showToast(getResources().getString(R.string.internet), this);
                }
            }
        }

    }

    private void callWS(NewsMainDatum bean, boolean isProgressBarRequired) {
        try {

            Uri.Builder mBuilder = new Uri.Builder()
                    .appendQueryParameter("news_id", bean.getId() + "");
            mNewsDetailsController.callWS(mBuilder, isProgressBarRequired);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setFonts() {
        header_tv.setTypeface(Constant.typeface(this, "HelveticaNeueLTStd-LtEx.otf"));

    }

    private void onClicks() {
        back_btn.setOnClickListener(this);
    }

    private void init() {
        Constant.changeHeaderColor(headerLLMain);
        headerIV.setVisibility(View.GONE);
        header_tv.setText(getResources().getString(R.string.newsText));

    }

    public void responseNewsDetails(NewsDetailsModalMain modal) {
        if (modal.getStatus() == 1) {
            news_title_tv.setText(modal.getData().getTitle());
            news_short_text_tv.setText(modal.getData().getShortText());
            news_main_iv.setCornerRadiiDP(4, 4, 4, 4);
            news_main_iv.setBorderWidthDP(2);
            news_main_iv.setBorderColor(Color.DKGRAY);

            news_webview.getSettings().setJavaScriptEnabled(true);
            news_webview.loadDataWithBaseURL("", modal.getData().getLongText(), "text/html", "UTF-8", "");
            Constant.loadImageWithOutAnyListener(this, modal.getData().getImage(), news_main_iv);
        } else {
            Constant.showToast(getResources().getString(R.string.oopsSomethingWentWrong), this);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }
}
