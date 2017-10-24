package com.tv.herelo.MyProfile.SearchForPeople;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.tv.herelo.R;
import com.tv.herelo.constants.Constant;
import com.tv.herelo.constants.NetworkAvailablity;
import com.tv.herelo.tab.BaseContainerFragment;
import com.tv.herelo.tab.MainTabActivity;
import com.tv.herelo.utils.Logger;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by shoeb on 14/7/16.
 */
public class SearchForPeopleFragment extends Fragment implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        DatePickerDialog.OnDateSetListener {
    private View view = null;

    /*Header*/
    private ImageView back_btn = null;
    private ImageView headerIV = null;
    private ImageView next_btn2 = null;
    private TextView header_tv = null;

    /*Views*/
    private TextView search_tv = null;
    private TextView tv_header_1 = null;
    private TextView fName_tv = null;
    private TextView LName_tv = null;
    private TextView UserName_tv = null;
    private TextView email_tv = null;
    private TextView tv_header_2 = null;
    private TextView AroundMe_tv = null;
    private TextView AbtUser_tv = null;
    private TextView Status_tv = null;
    private TextView DOB_tv = null;
    private TextView BirthState_tv = null;
    private TextView BirthCity_tv = null;
    private TextView HighSchool_tv = null;
    private TextView Clg_tv = null;
    private TextView FavMovie_tv = null;
    private TextView FavTvShow_tv = null;
    private TextView FavBand_tv = null;
    private TextView favSportsTeam_tv = null;
    private TextView favSports_tv = null;
    private TextView favGenreOfMovie_tv = null;
    private TextView favGenreOfMusic_tv = null;

    private CheckBox cbFName = null;
    private CheckBox cbLName = null;
    private CheckBox cbUserName = null;
    private CheckBox cbEmail = null;
    private CheckBox cbAroundMe = null;
    private CheckBox cbAbtUser = null;
    private CheckBox cbStatus = null;
    private CheckBox cbDOB = null;
    private CheckBox cbBirthState = null;
    private CheckBox cbBirthCity = null;
    private CheckBox cbHighSchool = null;
    private CheckBox cbClg = null;
    private CheckBox cbFavMovie = null;
    private CheckBox cbFavTvShow = null;
    private CheckBox cbFavBand = null;
    private CheckBox cbFavSportsTeam = null;
    private CheckBox cbFavSports = null;
    private CheckBox cbFavGenreOfMovie = null;
    private CheckBox cbFavGenreOfMusic = null;

    private EditText fName_et = null;
    private EditText LName_et = null;
    private EditText UserName_et = null;
    private EditText email_et = null;
    private EditText AbtUser_et = null;
    private EditText DOB_et = null;
    private EditText BirthState_et = null;
    private EditText BirthCity_et = null;
    private EditText HighSchool_et = null;
    private EditText Clg_et = null;
    private EditText FavMovie_et = null;
    private EditText FavTvShow_et = null;
    private EditText FavBand_et = null;
    private EditText favSportsTeam_et = null;

    private Spinner aroundme_et = null;
    private Spinner Status_et = null;
    private Spinner favSports_et = null;
    private Spinner favGenreOfMovie_et = null;
    private Spinner favGenreOfMusic_et = null;

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

    DatePickerDialog dpd;

    // The minimum distance to change Updates in meters
    private static final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.searchforpeople_frag, container, false);
        LinearLayout headerLLMain = (LinearLayout) view.findViewById(R.id.headerLLMain);
        Constant.changeHeaderColor(headerLLMain);

        initView();
        initLocation();
        initTextChangeListeners();
        return view;
    }

    private void initTextChangeListeners() {

        cbFName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked){
                    fName_et.setText("");
                }
            }
        });
        fName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbFName.setChecked(true);
                } else {
                    cbFName.setChecked(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbLName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    LName_et.setText("");
                }
            }
        });
        LName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbLName.setChecked(true);
                } else {
                    cbLName.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbUserName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    UserName_et.setText("");
                }
            }
        });
        UserName_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbUserName.setChecked(true);
                } else {
                    cbUserName.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    email_et.setText("");
                }
            }
        });
        email_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbEmail.setChecked(true);
                } else {
                    cbEmail.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbAbtUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    AbtUser_et.setText("");
                }
            }
        });
        AbtUser_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbAbtUser.setChecked(true);
                } else {
                    cbAbtUser.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        cbDOB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    DOB_et.setText("");
                }
            }
        });
        DOB_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbDOB.setChecked(true);
                } else {
                    cbDOB.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbBirthState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    BirthState_et.setText("");
                }
            }
        });
        BirthState_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbBirthState.setChecked(true);
                } else {
                    cbBirthState.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbBirthCity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    BirthCity_et.setText("");
                }
            }
        });
        BirthCity_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbBirthCity.setChecked(true);
                } else {
                    cbBirthCity.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        cbHighSchool.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    HighSchool_et.setText("");
                }
            }
        });
        HighSchool_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbHighSchool.setChecked(true);
                } else {
                    cbHighSchool.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbClg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    Clg_et.setText("");
                }
            }
        });
        Clg_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbClg.setChecked(true);
                } else {
                    cbClg.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbFavMovie.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    FavMovie_et.setText("");
                }
            }
        });
        FavMovie_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbFavMovie.setChecked(true);
                } else {
                    cbFavMovie.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        cbFavTvShow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    FavTvShow_et.setText("");
                }
            }
        });
        FavTvShow_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbFavTvShow.setChecked(true);
                } else {
                    cbFavTvShow.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbFavBand.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    FavBand_et.setText("");
                }
            }
        });
        FavBand_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbFavBand.setChecked(true);
                } else {
                    cbFavBand.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cbFavSportsTeam.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    favSportsTeam_et.setText("");
                }
            }
        });
        favSportsTeam_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    cbFavSportsTeam.setChecked(true);
                } else {
                    cbFavSportsTeam.setChecked(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        aroundme_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cbAroundMe.setChecked(true);
                } else {
                    cbAroundMe.setChecked(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Status_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cbStatus.setChecked(true);
                } else {
                    cbStatus.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        favSports_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cbFavSports.setChecked(true);
                } else {
                    cbFavSports.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        favGenreOfMovie_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cbFavGenreOfMovie.setChecked(true);
                } else {
                    cbFavGenreOfMovie.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        favGenreOfMusic_et.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cbFavGenreOfMusic.setChecked(true);
                } else {
                    cbFavGenreOfMusic.setChecked(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }


    public static final int storage_per = 2;
    /*Permissions Marsh Mallow*/
    public static String[] permission = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    public boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permission) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            MainTabActivity.camera = result == 0;
            Logger.logsInfo("Main Activity ", "Value of Result : " + result);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);


            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.
                    toArray(new String[listPermissionsNeeded.size()]), storage_per);
            return false;
        }
        return true;
    }


    private void initLocation() {

        createLocationRequest();

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        }

        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if (gps_enabled) {

                if (locationManager != null) {
                    if (ActivityCompat.checkSelfPermission(getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                            , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, new android.location.LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {

                                }

                                @Override
                                public void onStatusChanged(String provider, int status, Bundle extras) {

                                }

                                @Override
                                public void onProviderEnabled(String provider) {

                                }

                                @Override
                                public void onProviderDisabled(String provider) {

                                }
                            });
                    Log.d("Network", "Network");
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Logger.logsInfo("Search For People", "latitude : " + latitude);
                        Logger.logsInfo("Search For People", "longitude : " + longitude);
                        lat = String.valueOf(latitude);
                        lng = String.valueOf(longitude);

                        Geocoder geocoder;
                        List<Address> addresses;
                        geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        try {
                            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    }
                }
            } else {
                showLocationAlertDialog(getActivity());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        Calendar now = Calendar.getInstance();
        dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.setTitle("Choose Date of Birth!");
        dpd.setOnDateSetListener(this);
        dpd.showYearPickerFirst(true);
        dpd.setMaxDate(now);
    }

    /**
     * Show dialog for location using Google API Client
     *
     * @param activity
     */

    @SuppressWarnings("unchecked")
    public void showLocationAlertDialog(final Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= 19) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API).build();
                mGoogleApiClient.connect();
                Object obj = LocationRequest.create();
                ((LocationRequest) (obj)).setPriority(100);
                ((LocationRequest) (obj)).setInterval(30000L);
                ((LocationRequest) (obj)).setFastestInterval(5000L);
                obj = new LocationSettingsRequest.Builder().addLocationRequest(
                        ((LocationRequest) (obj))).setAlwaysShow(true);

                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        ((LocationSettingsRequest.Builder) (obj)).build())
                        .setResultCallback(
                                new ResultCallback<LocationSettingsResult>() {

                                    @Override
                                    public void onResult(
                                            LocationSettingsResult locationsettingsresult) {
                                        // TODO Auto-generated method stub
                                        Status status;
                                        Log.e("result of yes no",
                                                (new StringBuilder())
                                                        .append("=")
                                                        .append(locationsettingsresult
                                                                .getStatus())
                                                        .toString());
                                        status = locationsettingsresult
                                                .getStatus();
                                        locationsettingsresult
                                                .getLocationSettingsStates();
                                        status.getStatusCode();

                                        try {
                                            status.startResolutionForResult(
                                                    getActivity(), 1000);

                                        } catch (IntentSender.SendIntentException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }

                                    }
                                });
            }
            return;
        } else {
            startActivityForResult(new Intent(
                    "android.settings.LOCATION_SOURCE_SETTINGS"), 1);
            return;

        }
    }

    private void initView() {
        back_btn = (ImageView) view.findViewById(R.id.back_btn);
        back_btn.setOnClickListener(this);
        headerIV = (ImageView) view.findViewById(R.id.headerIV);
        headerIV.setVisibility(View.GONE);
        next_btn2 = (ImageView) view.findViewById(R.id.next_btn2);
        next_btn2.setVisibility(View.GONE);
        header_tv = (TextView) view.findViewById(R.id.header_tv);
        header_tv.setText(getActivity().getResources().getString(R.string.search));

        fName_et = (EditText) view.findViewById(R.id.fName_et);
        LName_et = (EditText) view.findViewById(R.id.LName_et);
        UserName_et = (EditText) view.findViewById(R.id.UserName_et);
        email_et = (EditText) view.findViewById(R.id.email_et);
        AbtUser_et = (EditText) view.findViewById(R.id.AbtUser_et);
        DOB_et = (EditText) view.findViewById(R.id.DOB_et);
        DOB_et.setOnClickListener(this);
        BirthState_et = (EditText) view.findViewById(R.id.BirthState_et);
        BirthCity_et = (EditText) view.findViewById(R.id.BirthCity_et);
        HighSchool_et = (EditText) view.findViewById(R.id.HighSchool_et);
        Clg_et = (EditText) view.findViewById(R.id.Clg_et);
        FavMovie_et = (EditText) view.findViewById(R.id.FavMovie_et);
        FavTvShow_et = (EditText) view.findViewById(R.id.FavTvShow_et);
        FavBand_et = (EditText) view.findViewById(R.id.FavBand_et);
        favSportsTeam_et = (EditText) view.findViewById(R.id.favSportsTeam_et);


        aroundme_et = (Spinner) view.findViewById(R.id.aroundme_et);
        Status_et = (Spinner) view.findViewById(R.id.Status_et);
        favSports_et = (Spinner) view.findViewById(R.id.favSports_et);
        favGenreOfMovie_et = (Spinner) view.findViewById(R.id.favGenreOfMovie_et);
        favGenreOfMusic_et = (Spinner) view.findViewById(R.id.favGenreOfMusic_et);

        cbFName = (CheckBox) view.findViewById(R.id.cbFName);
        cbLName = (CheckBox) view.findViewById(R.id.cbLName);
        cbUserName = (CheckBox) view.findViewById(R.id.cbUserName);
        cbEmail = (CheckBox) view.findViewById(R.id.cbEmail);
        cbAroundMe = (CheckBox) view.findViewById(R.id.cbAroundMe);
        cbAbtUser = (CheckBox) view.findViewById(R.id.cbAbtUser);
        cbStatus = (CheckBox) view.findViewById(R.id.cbStatus);
        cbDOB = (CheckBox) view.findViewById(R.id.cbDOB);
        cbBirthState = (CheckBox) view.findViewById(R.id.cbBirthState);
        cbBirthCity = (CheckBox) view.findViewById(R.id.cbBirthCity);
        cbHighSchool = (CheckBox) view.findViewById(R.id.cbHighSchool);
        cbClg = (CheckBox) view.findViewById(R.id.cbClg);
        cbFavMovie = (CheckBox) view.findViewById(R.id.cbFavMovie);
        cbFavTvShow = (CheckBox) view.findViewById(R.id.cbFavTvShow);
        cbFavBand = (CheckBox) view.findViewById(R.id.cbFavBand);
        cbFavSportsTeam = (CheckBox) view.findViewById(R.id.cbFavSportsTeam);
        cbFavSports = (CheckBox) view.findViewById(R.id.cbFavSports);
        cbFavGenreOfMovie = (CheckBox) view.findViewById(R.id.cbFavGenreOfMovie);
        cbFavGenreOfMusic = (CheckBox) view.findViewById(R.id.cbFavGenreOfMusic);

        search_tv = (TextView) view.findViewById(R.id.search_tv);
        tv_header_1 = (TextView) view.findViewById(R.id.tv_header_1);
        fName_tv = (TextView) view.findViewById(R.id.fName_tv);
        LName_tv = (TextView) view.findViewById(R.id.LName_tv);
        UserName_tv = (TextView) view.findViewById(R.id.UserName_tv);
        email_tv = (TextView) view.findViewById(R.id.email_tv);
        tv_header_2 = (TextView) view.findViewById(R.id.tv_header_2);
        AroundMe_tv = (TextView) view.findViewById(R.id.AroundMe_tv);
        AbtUser_tv = (TextView) view.findViewById(R.id.AbtUser_tv);
        Status_tv = (TextView) view.findViewById(R.id.Status_tv);
        DOB_tv = (TextView) view.findViewById(R.id.DOB_tv);
        BirthState_tv = (TextView) view.findViewById(R.id.BirthState_tv);
        BirthCity_tv = (TextView) view.findViewById(R.id.BirthCity_tv);
        HighSchool_tv = (TextView) view.findViewById(R.id.HighSchool_tv);
        Clg_tv = (TextView) view.findViewById(R.id.Clg_tv);
        FavMovie_tv = (TextView) view.findViewById(R.id.FavMovie_tv);
        FavTvShow_tv = (TextView) view.findViewById(R.id.FavTvShow_tv);
        FavBand_tv = (TextView) view.findViewById(R.id.FavBand_tv);
        favSportsTeam_tv = (TextView) view.findViewById(R.id.favSportsTeam_tv);
        favSports_tv = (TextView) view.findViewById(R.id.favSports_tv);
        favGenreOfMovie_tv = (TextView) view.findViewById(R.id.favGenreOfMovie_tv);
        favGenreOfMusic_tv = (TextView) view.findViewById(R.id.favGenreOfMusic_tv);

        search_tv.setOnClickListener(this);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Constant.hideKeyBoard(getActivity());
    }

    private void showDatePicker() {


        if (!dpd.isVisible()){
            dpd.show(getActivity().getFragmentManager(), "Choose Date of Birth");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.DOB_et:
                showDatePicker();
                break;
            case R.id.back_btn:
                MainTabActivity.backbutton();
                break;
            case R.id.search_tv:
                if (NetworkAvailablity.checkNetworkStatus(getActivity())) {

                    if (validData()) {


                        try {
                            Bundle mBundle = new Bundle();

                            mBundle.putString("around_me", around_me);
                            mBundle.putString("lat", lat);
                            mBundle.putString("lng", lng);
                            mBundle.putString("dob", dob);
                            mBundle.putString("birth_state", birth_state);
                            mBundle.putString("birth_city", birth_city);
                            mBundle.putString("about_user", about_user);
                            mBundle.putString("status", status);
                            mBundle.putString("high_school", high_school);
                            mBundle.putString("college", college);
                            mBundle.putString("fav_movie", fav_movie);
                            mBundle.putString("movie_gen", movie_gen);
                            mBundle.putString("fav_tv_show", fav_tv_show);
                            mBundle.putString("fav_band", fav_band);
                            mBundle.putString("music_gen", music_gen);
                            mBundle.putString("fav_team", fav_team);
                            mBundle.putString("fav_sport", fav_sport);
                            mBundle.putString("first_name", first_name);
                            mBundle.putString("last_name", last_name);
                            mBundle.putString("username", username);
                            mBundle.putString("email", email);

                            SearchForPeopleResultFragment searchForPeopleResultFragment = new SearchForPeopleResultFragment();
                            searchForPeopleResultFragment.setArguments(mBundle);


                            ((BaseContainerFragment) getParentFragment()).replaceFragment(searchForPeopleResultFragment, true);

//                            mController.callSearchForPeopleWS(builder);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else{
                        Constant.alertDialogShow(getActivity(), getActivity().getResources().getString(R.string.app_name),
                                getActivity().getResources().getString(R.string.pleaseSelectAtLeastOneOptionText));
                    }
                } else {
                    Constant.showToast(getActivity().getResources().getString(R.string.internet), getActivity());
                }
                break;
            default:
                break;
        }

    }

    private boolean validData() {
        boolean isSuccess = false;

        isSuccess = cbAroundMe.isChecked() ||
                cbDOB.isChecked() ||
                cbBirthState.isChecked() ||
                cbBirthCity.isChecked() ||
                cbAbtUser.isChecked() ||
                cbStatus.isChecked() ||
                cbHighSchool.isChecked() ||
                cbClg.isChecked() ||
                cbFavMovie.isChecked() ||
                cbFavGenreOfMovie.isChecked() ||
                cbFavTvShow.isChecked() ||
                cbFavBand.isChecked() ||
                cbFavGenreOfMusic.isChecked() ||
                cbFavSportsTeam.isChecked() ||
                cbFavSports.isChecked() ||
                cbFName.isChecked() ||
                cbLName.isChecked() ||
                cbUserName.isChecked() ||
                cbEmail.isChecked();

        if (cbAroundMe.isChecked()) {
            int aroundMepos = aroundme_et.getSelectedItemPosition();
            switch (aroundMepos) {
                case 0:
                    around_me = "";
                    break;
                case 1:
                    around_me = "5";
                    break;
                case 2:
                    around_me = "20";
                    break;
                case 3:
                    around_me = "50";
                    break;
                case 4:
                    around_me = "100";
                    break;
                case 5:
                    around_me = "300";
                    break;
                default:
                    break;
            }
        }


        if (cbStatus.isChecked()) {
            if (Status_et.getSelectedItemPosition() == 0) {
                status = "";
            } else {
                status = Status_et.getSelectedItem().toString().trim();
            }
        }


        if (cbBirthState.isChecked()) {
            birth_state = BirthState_et.getText().toString().trim();
        }

        if (cbBirthCity.isChecked()) {
            birth_city = BirthCity_et.getText().toString().trim();
        }

        if (cbAbtUser.isChecked()) {
            about_user = AbtUser_et.getText().toString().trim();
        }


        if (cbHighSchool.isChecked()) {
            high_school = HighSchool_et.getText().toString().trim();
        }

        if (cbClg.isChecked()) {
            college = Clg_et.getText().toString().trim();
        }

        if (cbFavMovie.isChecked()) {
            fav_movie = FavMovie_et.getText().toString().trim();
        }


        if (cbFavGenreOfMovie.isChecked()) {
            if (favGenreOfMovie_et.getSelectedItemPosition() == 0) {
                movie_gen = "";
            } else {
                movie_gen = favGenreOfMovie_et.getSelectedItem().toString().trim();
            }

        }

        if (cbFavGenreOfMusic.isChecked()) {
            if (favGenreOfMusic_et.getSelectedItemPosition() == 0) {
                music_gen = "";
            } else {
                music_gen = favGenreOfMusic_et.getSelectedItem().toString().trim();
            }
        }


        if (cbFavSports.isChecked()) {
            if (favSports_et.getSelectedItemPosition() == 0) {
                fav_sport = "";
            } else {
                fav_sport = favSports_et.getSelectedItem().toString().trim();
            }
        }

        if (cbFavTvShow.isChecked()) {
            fav_tv_show = FavTvShow_et.getText().toString().trim();
        }
        if (cbFavSportsTeam.isChecked()) {
            fav_team = favSportsTeam_et.getText().toString().trim();
        }
        if (cbFavBand.isChecked()) {
            fav_band = FavBand_et.getText().toString().trim();
        }

        if (cbFName.isChecked()) {
            first_name = fName_et.getText().toString().trim();
        }

        if (cbLName.isChecked()) {
            last_name = LName_et.getText().toString().trim();
        }
        if (cbUserName.isChecked()) {
            username = UserName_et.getText().toString().trim();
        }

        if (cbEmail.isChecked()) {
            email = email_et.getText().toString().trim();
        }


        isSuccess = !(around_me.equalsIgnoreCase("") &&
                dob.equalsIgnoreCase("") &&
                birth_state.equalsIgnoreCase("") &&
                birth_city.equalsIgnoreCase("") &&
                about_user.equalsIgnoreCase("") &&
                status.equalsIgnoreCase("") &&
                high_school.equalsIgnoreCase("") &&
                college.equalsIgnoreCase("") &&
                fav_movie.equalsIgnoreCase("") &&
                movie_gen.equalsIgnoreCase("") &&
                fav_tv_show.equalsIgnoreCase("") &&
                fav_band.equalsIgnoreCase("") &&
                music_gen.equalsIgnoreCase("") &&
                fav_team.equalsIgnoreCase("") &&
                fav_sport.equalsIgnoreCase("") &&
                first_name.equalsIgnoreCase("") &&
                last_name.equalsIgnoreCase("") &&
                username.equalsIgnoreCase("") &&
                email.equalsIgnoreCase(""));

        return isSuccess;
    }

    private GoogleApiClient mGoogleApiClient;

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        // Disconnect the client.
        if (mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                mGoogleApiClient.disconnect();
            }

        }

        super.onStop();
    }

    /**
     * If locationChanges change lat and long
     *
     * @param location
     */
    boolean isFirstCall = false;
    String address = "";

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        lat = String.valueOf(latitude);
        double longitude = location.getLongitude();
        lng = String.valueOf(longitude);
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        } catch (IOException e) {
            e.printStackTrace();
        }


        if (!isFirstCall) {
            try {
                isFirstCall = true;
//                Toast.makeText(getActivity(), mlatitude + " WORKS onLocationChanged " + mlongitude + "", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }


    /**
     * If connected get lat and long
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10 * 1000);      // 10 seconds, in milliseconds
        mLocationRequest.setFastestInterval(1 * 1000); // 1 second, in milliseconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private LocationRequest mLocationRequest;

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity()
                , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            double currentLatitude = location.getLatitude();
            double currentLongitude = location.getLongitude();

//            Toast.makeText(getActivity(), currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        monthOfYear = monthOfYear + 1;
        StringBuilder stringBuilder = new StringBuilder().append(year).append("-")
                .append(String.format("%02d", monthOfYear)).append("-").append(String.format("%02d", dayOfMonth));
        dob = stringBuilder.toString();
        String monthName = "";
        switch (monthOfYear) {

            case 1:
                monthName = "Jan";
                break;
            case 2:
                monthName = "Feb";
                break;
            case 3:
                monthName = "Mar";
                break;
            case 4:
                monthName = "Apr";
                break;
            case 5:
                monthName = "May";
                break;
            case 6:
                monthName = "Jun";
                break;
            case 7:
                monthName = "Jul";
                break;
            case 8:
                monthName = "Aug";
                break;
            case 9:
                monthName = "Sep";
                break;
            case 10:
                monthName = "Oct";
                break;
            case 11:
                monthName = "Nov";
                break;
            case 12:
                monthName = "Dec";
                break;
            default:
                break;
        }
        DOB_et.setText(new StringBuilder().append(String.format("%02d", dayOfMonth)).append("-")
                .append(monthName).append("-").append(year));
    }
}
