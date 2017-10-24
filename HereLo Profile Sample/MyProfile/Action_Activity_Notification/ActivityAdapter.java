package com.tv.herelo.MyProfile.Action_Activity_Notification;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.HomeTab.post_details.PostDetailsScreen;
import com.tv.herelo.MyProfile.Action_Activity_Notification.ActivityModal.ActivityModalDatum;
import com.tv.herelo.MyProfile.MyProfileScreen;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.MySharedPreference;
import com.tv.herelo.tab.BaseContainerFragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shoeb on 08/5/17.
 */
public class ActivityAdapter extends BaseAdapter {

    private final Typeface tf;
    private Activity mActivity = null;
    private ActivityScreen mActivityScreen;
    private TaggedActivityFragment taggedActivityFragment;
    private ViewHolder view_holder;
    private List<ActivityModalDatum> mActivityModalDatumList = new ArrayList<>();

    /*Image Loader*/
    private DisplayImageOptions optionsProfileImg;
    private DisplayImageOptions optionsPostImg;

    com.nostra13.universalimageloader.core.ImageLoader imageLoaderNew;

    public ActivityAdapter(Activity mActivity, List<ActivityModalDatum> mActivityModalDatumList,
                           ActivityScreen mActivityScreen,
                           TaggedActivityFragment taggedActivityFragment) {
        this.mActivity = mActivity;
        this.mActivityModalDatumList = mActivityModalDatumList;
        this.mActivityScreen = mActivityScreen;
        this.taggedActivityFragment = taggedActivityFragment;


        /**
         * TypeFace
         *
         */

        tf = Typeface.createFromAsset(mActivity.getAssets(), "Helvetica-Normal.ttf");

        /*Image Loader*/

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(mActivity));

        imageLoaderNew = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
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


    }

    @Override
    public int getCount() {
        return mActivityModalDatumList.size();
    }

    @Override
    public Object getItem(int position) {
        return mActivityModalDatumList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addMoreData(List<ActivityModalDatum> data) {
        for (ActivityModalDatum bean :
                data) {
            mActivityModalDatumList.add(bean);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        private TextView textview_activity;
        private TextView time_tv;
        private TextView post_tv;
        private TextView more_images_count_tv;
        private CircleImageView profileIV;
        private ImageView post_img;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            view_holder = new ViewHolder();
            LayoutInflater _linflater = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
            convertView = _linflater.inflate(R.layout.activity_item, null);
            view_holder.textview_activity = (TextView) convertView.findViewById(R.id.textview_activity);
            view_holder.post_tv = (TextView) convertView.findViewById(R.id.post_tv);
            view_holder.more_images_count_tv = (TextView) convertView.findViewById(R.id.more_images_count_tv);
            view_holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
            view_holder.profileIV = (CircleImageView) convertView.findViewById(R.id.profileIV);
            view_holder.post_img = (ImageView) convertView.findViewById(R.id.post_img);


            convertView.setTag(view_holder);

        } else {
            view_holder = (ViewHolder) convertView.getTag();
        }


        /*Populating Data*/

        final ActivityModalDatum bean = mActivityModalDatumList.get(position);

        view_holder.post_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mActivity, PostDetailsScreen.class);
                DatumHome mDatumHome = new DatumHome();
                mDatumHome.setId(bean.getPostId());
                mDatumHome.setPostType(bean.getPType() + "");
                List<DatumHome> mDatumList = new ArrayList<>();
                mDatumList.add(mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS, mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDatumList);
                mActivity.startActivity(mIntent);
            }
        });
        view_holder.post_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mActivity, PostDetailsScreen.class);
                DatumHome mDatumHome = new DatumHome();
                mDatumHome.setId(bean.getPostId());
                mDatumHome.setPostType(bean.getPType() + "");
                List<DatumHome> mDatumList = new ArrayList<>();
                mDatumList.add(mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS, mDatumHome);
                mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDatumList);
                mActivity.startActivity(mIntent);
            }
        });
        view_holder.textview_activity.setTypeface(tf);
        view_holder.post_tv.setTypeface(tf);
        view_holder.time_tv.setTypeface(Constant.typeface(mActivity, "HelveticaNeueLTStd-Lt.otf"));


        view_holder.time_tv.setText(bean.getTime());
        view_holder.post_tv.setText(bean.getPostText());
        imageLoaderNew.displayImage(bean.getSimage(), view_holder.profileIV, optionsProfileImg);
        imageLoaderNew.displayImage(bean.getMediaUrl(), view_holder.post_img, optionsPostImg);
        String middle = "";
        String last = "";
        String sName = "";
        String tName = "";

        if (bean.getTotalMedia() > 1) {

            view_holder.more_images_count_tv.setVisibility(View.VISIBLE);
            int remainingCount = bean.getTotalMedia() - 1;
            view_holder.more_images_count_tv.setText("+" + String.valueOf(remainingCount));
        } else {
            view_holder.more_images_count_tv.setVisibility(View.GONE);
        }
        switch (bean.getNotificationType()) {
            case Constant.post_like:
                middle = " liked ";
                last = " HereLo";

                sName = bean.getSname();
                tName = bean.getTname();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);
                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.post_comment:
                middle = " commented on ";
                last = " HereLo";
                sName = bean.getSname();
                tName = bean.getTname();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.comment_like:
                middle = " liked ";
                last = " comment: " + bean.getCommentText();
                sName = bean.getSname();
                tName = bean.getOwnerName();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.checkin:
                middle = " checked in @ " + bean.getPostText();
//                last = " HereLo";
                last = "";
                sName = bean.getSname();
                tName = "";

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.following:
                middle = " started following ";
                last = "";
                sName = bean.getSname();
                tName = bean.getTname();
                view_holder.post_img.setVisibility(View.INVISIBLE);
                view_holder.post_tv.setVisibility(View.INVISIBLE);
                break;

            case Constant.tag_post_like:
                middle = " liked ";
                last = " HereLo.";
//                last = " HereLo in which you are tagged.";
                sName = bean.getSname();
                tName = bean.getTname();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.tag_post_comment:
                middle = " commented on ";
//                last = " HereLo in which you are tagged.";
                last = " HereLo.";
                sName = bean.getSname();
                tName = bean.getTname();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            case Constant.tag_post_comment_like:
                middle = " liked ";
                last = " comment: " + bean.getCommentText();
                sName = bean.getSname();
                tName = bean.getOwnerName();

                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }

                break;

            case Constant.tagged:
                if (bean.getTag_id() == MySharedPreference.getCurrentUserId(mActivity)) {
                    middle = " tagged you in a HereLo.";
                    last = "";
                    sName = bean.getSname();
                    tName = "";
                } else {
                    middle = " tagged ";
                    last = " in a HereLo.";
                    sName = bean.getSname();
                    tName = bean.getTag_user();
                }


                if (bean.getPType() == 3 || bean.getPType() == 8) {
                    view_holder.post_img.setVisibility(View.GONE);
                    view_holder.post_tv.setVisibility(View.VISIBLE);

                } else {
                    view_holder.post_img.setVisibility(View.VISIBLE);
                    view_holder.post_tv.setVisibility(View.GONE);
                }
                break;

            default:
                break;
        }


        ClickableSpan mClickableSpanSUser = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                replaceWithProfileFragment(bean.getSid());
//                Constant.showToast(bean.getSname(),mActivity);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan mClickableSpanTUser = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (bean.getNotificationType().equalsIgnoreCase(Constant.tagged)) {
                    replaceWithProfileFragment(bean.getTag_id());
                } else if (bean.getNotificationType().equalsIgnoreCase(Constant.comment_like)) {
                    replaceWithProfileFragment(bean.getOwnerId());
                } else {
                    replaceWithProfileFragment(bean.getTid());
                }


//                Constant.showToast(bean.getTname(),mActivity);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };


        final SpannableStringBuilder sb2 = new SpannableStringBuilder(sName + middle + tName + last);

// Span to set text color to some RGB value
        final ForegroundColorSpan mForegroundColorSpanFirst = new ForegroundColorSpan(Color.rgb(1, 8, 122));
        final ForegroundColorSpan mForegroundColorSpanLast = new ForegroundColorSpan(Color.rgb(1, 8, 122));

// Span to make text bold
//        final StyleSpan bss2 = new StyleSpan(android.graphics.Typeface.BOLD);

//                        comment_first.setText(Html.fromHtml(first + next));
        if (sb2 != null) {
            if (sb2.length() > 0) {
                sb2.setSpan(mClickableSpanSUser, 0,
                        sName.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                sb2.setSpan(mClickableSpanTUser, sb2.length() - (tName.length() + last.length()),
                        sb2.length() - last.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

// Set the text color for first 4 characters
                sb2.setSpan(mForegroundColorSpanFirst, 0, sName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb2.setSpan(mForegroundColorSpanLast, sb2.length() - (tName.length() + last.length()), sb2.length() - last.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

// make them also bold
                //sb2.setSpan(bss2, 0, first2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                view_holder.textview_activity.setText(sb2);
                view_holder.textview_activity.setMovementMethod(LinkMovementMethod.getInstance());
                view_holder.textview_activity.setHighlightColor(Color.TRANSPARENT);
            }
        }
        return convertView;
    }

    private void replaceWithProfileFragment(int mUserId) {

        Bundle mBundle = new Bundle();
        mBundle.putInt(Constant.other_user_id, mUserId);
        mBundle.putBoolean(Constant.fromActivity, true);
        MyProfileScreen mMyProfileScreen = new MyProfileScreen();
        mMyProfileScreen.setArguments(mBundle);

        if (mActivityScreen != null) {
            ((BaseContainerFragment) mActivityScreen.getParentFragment()).replaceFragment(mMyProfileScreen, true);
        } else if (taggedActivityFragment != null) {
            ((BaseContainerFragment) taggedActivityFragment.getParentFragment()).replaceFragment(mMyProfileScreen, true);
        }

    }
}
