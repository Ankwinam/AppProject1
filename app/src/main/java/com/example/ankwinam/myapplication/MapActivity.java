package com.example.ankwinam.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    static final LatLng SEOUL = new LatLng(37.56, 126.97);
    private static final String TAG = "@@@";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CODE_LOCATION = 2000;//임의의 정수로 정의
    private GoogleMap googleMap;

//    //////////////////////////////////////////
//    Marker mMarkerStart;
//    Marker mMarkerMan;
//    boolean mFirstLoc = true;
//    LocationManager mLocMgr;
//    //////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        //권한검사
        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Marshmallow이상이면 코드에서 권한요청이 필요
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_LOCATION);
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

////////////////////////////////////////////////////
//        initMap();
//        mLocMgr = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        //////////////////////////////////////////
    }

//////////////////////////////////////////////////////////////////////////////////////////
//    LocationListener mLocListener = new LocationListener() {
//        public void onLocationChanged(Location location) {
//            LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
//            if( mFirstLoc ) {
//                mFirstLoc = false;
//                mMarkerStart.setPosition(position);
//            }
//
//            mMarkerMan.setPosition(position);
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position) );
//        }
//
//        public void onProviderDisabled(String provider) {}
//        public void onProviderEnabled(String provider) {}
//        public void onStatusChanged(String provider, int status, Bundle extras) {}
//    };
//
//    public void onResume() {
//        super.onResume();
//        String locProv = LocationManager.GPS_PROVIDER;
//        mLocMgr.requestLocationUpdates(locProv, (long)3000, 3.f, mLocListener);
//    }
//
//    public void onPause() {
//        super.onPause();
//        mLocMgr.removeUpdates(mLocListener);
//    }
//
//    private void initMap() {
//        GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
//        googleMap = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
//        LatLng pos = new LatLng(37.4980, 127.027);
//        // 맵 중심 위치 이동
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
//
//        // 시작 위치에 마커 추가
//        MarkerOptions moStart = new MarkerOptions();
//        moStart.position(pos);
//        moStart.title("출발");
//        mMarkerStart = googleMap.addMarker(moStart);
//        mMarkerStart.showInfoWindow();
//        //mMarkerStart = mGoogleMap.addMarker(new MarkerOptions().position(pos).title("출발"));
//
//        //마커 클릭 리스너
//        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                Toast.makeText(getApplication(), "출발 위치", Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
//
//        // 보행자 마커 추가
//        MarkerOptions moMan = new MarkerOptions();
//        moMan.position(pos);
//        moMan.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_menu_camera));
//        mMarkerMan = googleMap.addMarker( moMan );
//    }
///////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {

                    LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
                    if (locationAvailability.isLocationAvailable()) {
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    } else {

                        Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
                        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                        Toast.makeText(this,"Location Unavialable",Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        googleMap = map;

        Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL).title("Seoul"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(3000);
        mLocationRequest.setFastestInterval(1500);


        if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            // Gets the best and most recent location currently available, which may be null
            // in rare cases when a location is not available.
            LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(mGoogleApiClient);
            if (locationAvailability.isLocationAvailable()) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                Marker seoul = googleMap.addMarker(new MarkerOptions().position(SEOUL)
                        .title("Seoul"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 15));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

                Toast.makeText(this, "Location Unavialable", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }


    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        LatLng CURRENT_LOCATION = new LatLng(location.getLatitude(), location.getLongitude());
        googleMap.clear();
        Marker seoul = googleMap.addMarker(new MarkerOptions().position(CURRENT_LOCATION).title("Here"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CURRENT_LOCATION, 15));
    }


}



