package com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tv.herelo.MyProfile.YourHereLo.Voting.VotingList.VotingListModal.VotingListDatumModal;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

/**
 * Created by Shoeb on 19/5/17.
 */
public class VotingListAdapter extends BaseAdapter implements StickyListHeadersAdapter {
    private static final String TAG = VotingListAdapter.class.getSimpleName();
    private List<VotingListDatumModal> mVotingListBeanList = new ArrayList<>();
    private Activity mActivity;
    private ImageLoader imageLoaderNew;
    private DisplayImageOptions options;

    public VotingListAdapter(Activity mActivity, List<VotingListDatumModal> mVotingListBeanList) {
        this.mActivity = mActivity;
        this.mVotingListBeanList = mVotingListBeanList;


        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));

        imageLoaderNew = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.profile_icon) //
                .showImageForEmptyUri(R.drawable.profile_icon)
                .showImageOnFail(R.drawable.profile_icon)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .imageScaleType(ImageScaleType.EXACTLY)

                .build();


    }

    @Override
    public int getCount() {
        return mVotingListBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mVotingListBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        int type;
//        Logger.logsError(TAG,"Type in Get ITem Type : " + mVotingListBeanList.get(position).getHeaderType());
        if (mVotingListBeanList.get(position).getHeaderType().equalsIgnoreCase("mandatory")) {
            type = 1;
        } else {
            type = 2;

        }
//        Logger.logsError(TAG,"RETURN Type in Get ITem Type : " +type );
        return type;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderMandatory mViewHolderMandatory = null;


        int type = getItemViewType(position);
        if (convertView == null) {
            LayoutInflater _linflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            if (type == 1) {
                convertView = _linflater.inflate(R.layout.voting_list_row_mandatory, null);
            } else {
                convertView = _linflater.inflate(R.layout.voting_list_row_features, null);

            }
            mViewHolderMandatory = new ViewHolderMandatory();
            mViewHolderMandatory.voting_name_tv = (TextView) convertView.findViewById(R.id.voting_name_tv);
            mViewHolderMandatory.mandatory_info_tv = (TextView) convertView.findViewById(R.id.mandatory_info_tv);
            mViewHolderMandatory.vote_title_iv = (SelectableRoundedImageView) convertView.findViewById(R.id.vote_title_iv);
            mViewHolderMandatory.vote_tv = (TextView) convertView.findViewById(R.id.vote_tv);
            mViewHolderMandatory.vote_icon_green_check_iv = (ImageView) convertView.findViewById(R.id.vote_icon_green_check_iv);
            convertView.setTag(mViewHolderMandatory);


        } else {
            mViewHolderMandatory = (ViewHolderMandatory) convertView.getTag();
        }



        /*Populating Data*/

        /*Set Fonts*/
        mViewHolderMandatory.voting_name_tv.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Roman.otf"));


        VotingListDatumModal bean = mVotingListBeanList.get(position);
        if (type == 1)


        {


            mViewHolderMandatory.mandatory_info_tv.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Roman.otf"));
            mViewHolderMandatory.voting_name_tv.setText(bean.getmVoteName());
            mViewHolderMandatory.vote_title_iv.setCornerRadiiDP(4, 4, 4, 4);
            mViewHolderMandatory.vote_title_iv.setBorderWidthDP(2);
            mViewHolderMandatory.vote_title_iv.setBorderColor(Color.DKGRAY);
            imageLoaderNew.displayImage(bean.getVote_main_img(), mViewHolderMandatory.vote_title_iv, options);
        } else

        {
            mViewHolderMandatory.vote_tv.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Roman.otf"));


            mViewHolderMandatory.voting_name_tv.setText(bean.getmVoteName());
            mViewHolderMandatory.vote_title_iv.setCornerRadiiDP(4, 4, 4, 4);
            mViewHolderMandatory.vote_title_iv.setBorderWidthDP(2);
            mViewHolderMandatory.vote_title_iv.setBorderColor(Color.DKGRAY);
            imageLoaderNew.displayImage(bean.getVote_main_img(), mViewHolderMandatory.vote_title_iv, options);
        }

        return convertView;
    }

    public class HeaderViewHolder {
        @Bind(R.id.header_tv)
        TextView header_tv;

        public HeaderViewHolder(View mView) {
            ButterKnife.bind(this, mView);
        }

    }


    public class ViewHolderMandatory {

        TextView voting_name_tv;

        TextView mandatory_info_tv;
        TextView vote_tv;

        TextView yes_vote_counts_tv;

        TextView no_vote_counts_tv;

        TextView yes_info_tv;

        TextView no_info_tv;

        SelectableRoundedImageView vote_title_iv;

        ImageView yes_select_iv;

        ImageView no_select_iv;
        ImageView vote_icon_green_check_iv;


    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder mHeaderViewHolder;
        if (convertView == null) {

            LayoutInflater _linflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            convertView = _linflater.inflate(R.layout.voting_list_header_view, null);

            mHeaderViewHolder = new HeaderViewHolder(convertView);

            convertView.setTag(mHeaderViewHolder);

        } else {
            mHeaderViewHolder = (HeaderViewHolder) convertView.getTag();
        }

        /*Populating Data*/
        VotingListDatumModal bean = mVotingListBeanList.get(position);
        mHeaderViewHolder.header_tv.setText(bean.getmHeaderName());
//        mHeaderViewHolder.header_tv.setTypeface(Constant.typeface(mActivity, "Helvetica-Normal.ttf"));

        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mVotingListBeanList.get(position).getHeaderType().subSequence(0, 1).charAt(0);

    }
}
