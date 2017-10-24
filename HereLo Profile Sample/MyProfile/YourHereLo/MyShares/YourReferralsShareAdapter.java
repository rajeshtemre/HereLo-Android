package com.tv.herelo.MyProfile.YourHereLo.MyShares;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tv.herelo.MyProfile.YourHereLo.MyShares.ModalsMySharesMain.ReferralsUsersDatum;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.webservices.WebserviceConstant;
import com.tv.herelo.webservices.WebserviceTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shoeb on 9/5/17.
 */
public class YourReferralsShareAdapter extends BaseAdapter {

    private List<ReferralsUsersDatum> mReferralsUsersDatumList = new ArrayList<>();
    private Activity mActivity;
    private String apiName = "";

    public YourReferralsShareAdapter(Activity mActivity,
                                     List<ReferralsUsersDatum> mReferralsUsersDatumList) {
        this.mReferralsUsersDatumList = mReferralsUsersDatumList;
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return mReferralsUsersDatumList.size();
    }

    @Override
    public Object getItem(int position) {
        return mReferralsUsersDatumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder {


        public TextView _nameTextView;
        public CircleImageView _profileIV;
        public ImageView _followImageView;
        public ImageView refrred_btn;
        public TextView detailTV;
        public LinearLayout follow_ll;
        public LinearLayout chat_ll;
        public LinearLayout user_det_ll;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder view_holder;


        if (convertView == null) {
            view_holder = new ViewHolder();
            LayoutInflater _linflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            convertView = _linflater.inflate(R.layout.friends_row, null);

            view_holder._nameTextView = (TextView) convertView.findViewById(R.id.nameTV);
            view_holder._profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
            view_holder._followImageView = (ImageView) convertView.findViewById(R.id.followedIV);
            view_holder.refrred_btn = (ImageView) convertView.findViewById(R.id.refrred_btn);

            view_holder.detailTV = (TextView) convertView.findViewById(R.id.detailTV);

            view_holder.follow_ll = (LinearLayout) convertView.findViewById(R.id.follow_ll);
            view_holder.chat_ll = (LinearLayout) convertView.findViewById(R.id.chat_ll);
            view_holder.user_det_ll = (LinearLayout) convertView.findViewById(R.id.user_det_ll);


            convertView.setTag(view_holder);

        } else {
            view_holder = (ViewHolder) convertView.getTag();
        }

        /*Set Fonts*/

        view_holder._nameTextView.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Md.otf"));
        view_holder.detailTV.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Md.otf"));

        final ReferralsUsersDatum bean = mReferralsUsersDatumList.get(position);

        if (bean.getIs_following() == 0) {
                /*False*/
            view_holder.refrred_btn.setImageResource(R.drawable.follow_button);
        } else {
                /*True*/
            view_holder.refrred_btn.setImageResource(R.drawable.unfollow_btn);
        }
        view_holder._nameTextView.setText(bean.getUsername());
        if (!bean.getPstatus().equalsIgnoreCase("") && bean.getPstatus() != null) {
            view_holder.detailTV.setVisibility(View.VISIBLE);
            view_holder.detailTV.setText(bean.getPstatus());
        } else {
            view_holder.detailTV.setVisibility(View.GONE);
        }

        view_holder.refrred_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 /*Api Calling*/
                if (NetworkAvailablity.checkNetworkStatus(mActivity)) {
                    if (bean.getIs_following() == 1) {
                        apiName = "unfollow";
                    } else {
                        apiName = "follow";
                    }


                    if (bean.getIs_following() == 1) {
                        bean.setIs_following(0);

                    } else {
                        bean.setIs_following(1);

                    }
                    notifyDataSetChanged();


                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("user_id", MySharedPreference.getCurrentUserId(mActivity) + "")
                            .appendQueryParameter("other_user_id", bean.getUser_id() + "");

                    FollowUnfollowUserTask task = new FollowUnfollowUserTask(WebserviceTask.POST,
                            null, null, builder);
                    task.execute();
                } else {
                    /*No Internet screen appears*/
                    Constant.showToast(mActivity.getResources().getString(R.string.internet), mActivity);
                }
            }
        });

        Constant.loadProfileImageWithOutAnyListener(mActivity, bean.getImage(), view_holder._profileIV);
        return convertView;
    }

    class FollowUnfollowUserTask extends WebserviceTask {

        public FollowUnfollowUserTask(int method, Map<String, String> params, HashMap<String, String> headerParams, Uri.Builder mUrlParameters) {
            super(method, (HashMap<String, String>) params, headerParams, mUrlParameters);
        }


        @Override
        public void onLoadingStarted() {

        }

        @Override
        public String getWebserviceURL() {

            return WebserviceConstant.MAIN_BASE_URL + apiName;
        }

        @Override
        public void onLoadingFinished(String response) {

        }
    }
}
