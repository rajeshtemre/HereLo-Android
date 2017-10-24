package com.tv.herelo.MyProfile.map_cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by shoeb on 21/11/16.
 */
public class AppClusterItem implements ClusterItem {

    public final String name;
    public final int profilePhoto;
    private final LatLng mPosition;

    public AppClusterItem(LatLng position, String name, int pictureResource) {
        this.name = name;
        this.profilePhoto = pictureResource;
        this.mPosition = position;
    }



    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}
