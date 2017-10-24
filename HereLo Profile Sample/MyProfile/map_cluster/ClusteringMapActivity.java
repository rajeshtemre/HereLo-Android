package com.tv.herelo.MyProfile.map_cluster;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.squareup.picasso.Picasso;
import com.tv.herelo.HomeTab.HomeModal.DatumHome;
import com.tv.herelo.HomeTab.HomeModal.HomeTabModal;
import com.tv.herelo.HomeTab.post_details.PostDetailsScreen;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.theme.ThemeBlack;
import com.tv.herelo.theme.ThemeBlue;
import com.tv.herelo.theme.ThemeGray;
import com.tv.herelo.theme.ThemeGreen;
import com.tv.herelo.theme.ThemeManager;
import com.tv.herelo.theme.ThemeMaroon;
import com.tv.herelo.theme.ThemeWhite;
import com.tv.herelo.utils.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by shoeb on 22/11/16.
 */
public class ClusteringMapActivity extends FragmentActivity
        implements
        ClusterManager.OnClusterClickListener<DatumHome>,
        ClusterManager.OnClusterInfoWindowClickListener<DatumHome>,
        ClusterManager.OnClusterItemClickListener<DatumHome>,
        ClusterManager.OnClusterItemInfoWindowClickListener<DatumHome>,
        OnMapReadyCallback,
        View.OnClickListener {

    @Bind(R.id.headerLLMain)
    LinearLayout headerLLMain;

    @Bind(R.id.back_btn)
    ImageView _left1IV;

    @Bind(R.id.back_btn2)
    ImageView _left2IV;

    @Bind(R.id.next_btn)
    ImageView _next1IV;

    @Bind(R.id.next_btn2)
    ImageView _next2IV;

    @Bind(R.id.headerIV)
    ImageView _headerIV;

    @Bind(R.id.header_tv)
    TextView _headerTV;


    private static final String TAG = ClusteringMapActivity.class.getSimpleName();
    private ClusterManager<DatumHome> mClusterManager;
    private Random mRandom = new Random(1984);

    private GoogleMap mMap;
    private int user_id = 0;

    /*Demo URL*/
//    String posturl = "https://sixpillarstopersia.files.wordpress.com/2012/07/fb_logo1.png";

    /*Image Loader*/
    private DisplayImageOptions options;
    ImageLoader imageLoaderNew;
    List<DatumHome> mDatumHomeList = new ArrayList<DatumHome>();
    private ClusterMapLocationController mapLocationController = new ClusterMapLocationController(this);


    protected int getLayoutId() {
        return R.layout.cluster_map;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Logger.logsInfo("Current Theme", Constant.getThemeIdFromPref(ClusteringMapActivity.this) + "");
        if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 1) {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        } else if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 2) {
            ThemeManager.Set(new ThemeMaroon());
            setTheme(R.style.AppThemeMaroon);
        } else if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 3) {
            ThemeManager.Set(new ThemeGreen());
            setTheme(R.style.AppThemeGreen);
        } else if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 4) {
            ThemeManager.Set(new ThemeBlack());
            setTheme(R.style.AppThemeBlack);
        } else if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 5) {
            ThemeManager.Set(new ThemeGray());
            setTheme(R.style.AppThemeRed);
        } else if (Constant.getThemeIdFromPref(ClusteringMapActivity.this) == 6) {
            ThemeManager.Set(new ThemeWhite());
            setTheme(R.style.AppThemeWhite);
        } else {
            ThemeManager.Set(new ThemeBlue());
            setTheme(R.style.AppTheme);
        }
        setContentView(getLayoutId());


        ButterKnife.bind(this);

        Constant.changeHeaderColor(headerLLMain);
        _headerTV.setText(getResources().getString(R.string.mapText));
        _left1IV.setVisibility(View.VISIBLE);
        _headerIV.setVisibility(View.GONE);
        _next2IV.setVisibility(View.GONE);
        _next1IV.setVisibility(View.GONE);
        _left2IV.setVisibility(View.GONE);

        onClicks();


        if (NetworkAvailablity.checkNetworkStatus(this)) {


            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

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

            if (getIntent() != null) {
                if (getIntent().hasExtra(Constant.USER_ID)) {
                    user_id = getIntent().getIntExtra(Constant.USER_ID, 0);
                    try {
                        Uri.Builder builder = new Uri.Builder()
                                .appendQueryParameter("user_id", user_id + "");
                        mapLocationController.callWS(builder, true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        } else {
            Constant.showToast(getResources().getString(R.string.internet), this);
        }
        setUpMap();
    }

    private void onClicks() {
        _left1IV.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMap();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (mMap != null) {
            return;
        }
        mMap = map;

    }

    private void startDemo(List<DatumHome> posts) {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.valueOf(posts.get(0).getLat()),
                Double.valueOf(posts.get(0).getLng())), 9.5f));

        mClusterManager = new ClusterManager<DatumHome>(this, getMap());
        mClusterManager.setRenderer(new PersonRenderer());
        getMap().setOnCameraIdleListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        addItems(posts);
        mClusterManager.cluster();
    }

    private void addItems(List<DatumHome> posts) {

        for (DatumHome postBean :
                posts) {
            mClusterManager.addItem(postBean);
        }

    }


    @Override
    public boolean onClusterClick(Cluster<DatumHome> cluster) {
        // Show a toast with some info when the cluster is clicked.
        String firstName = cluster.getItems().iterator().next().getText();

        Constant.showToast(cluster.getSize() + " (including " + firstName + ")", this);

        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<DatumHome> cluster) {
        // Does nothing, but you could go to a list of the users.
    }

    @Override
    public boolean onClusterItemClick(DatumHome item) {
        // Does nothing, but you could go into the user's profile page, for example.
//        Constant.showToast(item.getText()+"" /*+ " " + item.getId()*/, this);

        Intent mIntent = new Intent(this, PostDetailsScreen.class);
//        mIntent.putExtra(Constant.FOR_NON_BEAN_SCREENS, true);
//        mIntent.putExtra(Constant.FOR_NON_BEAN_SCREENS_POST_ID, item.getId());
        mIntent.putExtra(Constant.POST_DETAILS, item);
        mIntent.putExtra(Constant.POST_DETAILS_LIST, (Serializable) mDatumHomeList);
        startActivity(mIntent);
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(DatumHome item) {
        // Does nothing, but you could go into the user's profile page, for example.
    }

    public void response(HomeTabModal modal) {

        int status = modal.getStatus();
        mDatumHomeList=modal.getData();
        Logger.logsInfo(TAG, "Status : " + status);
        if (status == 1) {
            if (modal.getData().size() > 0) {
                startDemo(modal.getData());
            } else {
                alertDialogShow(this, getResources().getString(R.string.app_name),
                        getResources().getString(R.string.no_photos_on_mapText));
            }

        } else {
            alertDialogShow(this, getResources().getString(R.string.app_name),
                    getResources().getString(R.string.no_photos_on_mapText));
        }
    }

    private void alertDialogShow(Context context, String _heading, String _subHeading) {

        /*Code by SHoeb*/
        _heading = context.getResources().getString(R.string.app_name);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(_heading);
        builder.setCancelable(false);
        builder.setMessage(_subHeading);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });

        builder.show();
        /*End Here*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            default:
                break;
        }

    }


    /**
     * Draws profile photos inside markers (using IconGenerator).
     * When there are multiple people in the cluster, draw multiple photos (using MultiDrawable).
     */
    private class PersonRenderer extends DefaultClusterRenderer<DatumHome> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;

        public PersonRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.map_marker_custom, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image_map);
            mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding_map);
            mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);
        }

        @Override
        protected void onBeforeClusterItemRendered(DatumHome p, MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
//            mImageView.setImageResource(R.drawable.profile);
            if (p.getPostType().equalsIgnoreCase("1")) {
                if (p.getPostMedia().size() > 0) {
                    Picasso.with(ClusteringMapActivity.this)
                            .load(p.getPostMedia().get(0).getUrl())
                            .placeholder(R.drawable.post_img_default)
                            .error(R.drawable.post_img_default)
                            .into(mImageView);
                } else {
                    mImageView.setImageResource(R.drawable.post_img_default);
                }
            } else if (p.getPostType().equalsIgnoreCase("2")) {
                if (p.getPostMedia().size() > 0) {
                    Picasso.with(ClusteringMapActivity.this)
                            .load(p.getPostMedia().get(0).getThumb_url())
                            .placeholder(R.drawable.post_img_default)
                            .error(R.drawable.post_img_default)
                            .into(mImageView);
                } else {


                    mImageView.setImageResource(R.drawable.post_img_default);
                }
            } else if (p.getPostType().equalsIgnoreCase("6")) {
                if (p.getPostMedia().size() > 0) {
                    Picasso.with(ClusteringMapActivity.this)
                            .load(p.getPostMedia().get(0).getUrl())
                            .placeholder(R.drawable.post_img_default)
                            .error(R.drawable.post_img_default)
                            .into(mImageView);
                } else {


                    mImageView.setImageResource(R.drawable.post_img_default);
                }
            } else if (p.getPostType().equalsIgnoreCase("7")) {
                if (p.getPostMedia().size() > 0) {
                    Picasso.with(ClusteringMapActivity.this)
                            .load(p.getPostMedia().get(0).getUrl())
                            .placeholder(R.drawable.post_img_default)
                            .error(R.drawable.post_img_default)
                            .into(mImageView);
                } else {


                    mImageView.setImageResource(R.drawable.post_img_default);
                }
            } else if (p.getPostType().equalsIgnoreCase("9")) {
                if (p.getPostMedia().size() > 0) {
                    Picasso.with(ClusteringMapActivity.this)
                            .load(p.getPostMedia().get(0).getThumb_url())
                            .placeholder(R.drawable.post_img_default)
                            .error(R.drawable.post_img_default)
                            .into(mImageView);
                } else {


                    mImageView.setImageResource(R.drawable.post_img_default);
                }
            } else {


                mImageView.setImageResource(R.drawable.post_img_default);
            }


            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(p.getText());
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<DatumHome> cluster, MarkerOptions markerOptions) {

// Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).
            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;
            for (DatumHome p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                if (p.getPostType().equalsIgnoreCase("1")) {
                    if (p.getPostMedia().size() > 0) {
                        Picasso.with(ClusteringMapActivity.this)
                                .load(p.getPostMedia().get(0).getUrl())
                                .placeholder(R.drawable.post_img_default)
                                .error(R.drawable.post_img_default)
                                .into(mImageView);
                    } else {
                        mImageView.setImageResource(R.drawable.post_img_default);
                    }
                } else if (p.getPostType().equalsIgnoreCase("2")) {
                    if (p.getPostMedia().size() > 0) {
                        Picasso.with(ClusteringMapActivity.this)
                                .load(p.getPostMedia().get(0).getThumb_url())
                                .placeholder(R.drawable.post_img_default)
                                .error(R.drawable.post_img_default)
                                .into(mImageView);
                    } else {


                        mImageView.setImageResource(R.drawable.post_img_default);
                    }
                } else if (p.getPostType().equalsIgnoreCase("6")) {
                    if (p.getPostMedia().size() > 0) {
                        Picasso.with(ClusteringMapActivity.this)
                                .load(p.getPostMedia().get(0).getUrl())
                                .placeholder(R.drawable.post_img_default)
                                .error(R.drawable.post_img_default)
                                .into(mImageView);
                    } else {


                        mImageView.setImageResource(R.drawable.post_img_default);
                    }
                } else if (p.getPostType().equalsIgnoreCase("7")) {
                    if (p.getPostMedia().size() > 0) {
                        Picasso.with(ClusteringMapActivity.this)
                                .load(p.getPostMedia().get(0).getUrl())
                                .placeholder(R.drawable.post_img_default)
                                .error(R.drawable.post_img_default)
                                .into(mImageView);
                    } else {


                        mImageView.setImageResource(R.drawable.post_img_default);
                    }
                } else if (p.getPostType().equalsIgnoreCase("9")) {
                    if (p.getPostMedia().size() > 0) {
                        Picasso.with(ClusteringMapActivity.this)
                                .load(p.getPostMedia().get(0).getThumb_url())
                                .placeholder(R.drawable.post_img_default)
                                .error(R.drawable.post_img_default)
                                .into(mImageView);
                    } else {


                        mImageView.setImageResource(R.drawable.post_img_default);
                    }
                } else {


                    mImageView.setImageResource(R.drawable.post_img_default);
                }


                Bitmap icon = mIconGenerator.makeIcon();
                //  Bitmap myBitmap = BitmapFactory.decodeStream(input);
                Drawable drawable = new BitmapDrawable(getResources(), icon);
                // Drawable drawable = getResources().getDrawable(R.drawable.profile);
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);
            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }

    }

    protected GoogleMap getMap() {
        return mMap;
    }

    private void setUpMap() {
        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
    }
}
