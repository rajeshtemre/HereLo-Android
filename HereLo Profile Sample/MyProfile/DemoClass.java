package com.tv.herelo.MyProfile;

import android.graphics.drawable.ClipDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tv.herelo.R;

/**
 * Created by ravindra on 14/10/15.
 */
public class DemoClass extends Fragment {


    private ImageView demo_iv1;
    private ImageView demo_iv2;
    private ClipDrawable mImageDrawable;

    ImageLoader imageLoader ;
    private DisplayImageOptions options;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.demo, container, false);

        demo_iv1 = (ImageView)view.findViewById(R.id.demo_iv1);
        demo_iv2 = (ImageView)view.findViewById(R.id.demo_iv2);

//        ImageView img = (ImageView) findViewById(R.id.imageView1);
        mImageDrawable = (ClipDrawable) demo_iv2.getDrawable();
        mImageDrawable.setLevel(5000);

        imageLoader = ImageLoader.getInstance(); // Get singleton instance
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));
        imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_launcher)
                .showImageForEmptyUri(R.drawable.blur_image)
                .showImageOnFail(R.drawable.tab_black)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
// .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
                .build();

        demo_iv2.setImageResource(R.drawable.uncheck_mark);
        imageLoader.displayImage("https://trello-attachments.s3.amazonaws.com/560b7b87d760044ab69313ec/666x667/de0ea6f7fdbe2d7864b01428fe0c00b6/Take_the_quiz_home.PNG", demo_iv1,options, null);
//        imageLoader.displayImage("https://trello-attachments.s3.amazonaws.com/560b7b87d760044ab69313ec/666x667/de0ea6f7fdbe2d7864b01428fe0c00b6/Take_the_quiz_home.PNG", demo_iv2,options, null);

        return view;

    }

}
