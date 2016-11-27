package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by axx42 on 2016-11-21.
 */

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    TextView main_name;
    TextView level;
    ImageView imageView;
    TextView area;
    TextView info;
    Bitmap image;
    Button Tracking;
    Button Community;

    double mapX = 0;
    double mapY = 0;
    String walk_name;

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_scrolling);

        ////////////////////////////////map///////////////////////////////////////////
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detail_map);
        mapFragment.getMapAsync(this);
        ////////////////////////////////map///////////////////////////////////////////

        Tracking = (Button) findViewById(R.id.detail_tracking_btn);
        Community = (Button) findViewById(R.id.detail_community_btn);

        Tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this, MapActivity.class);
                //좌표정보 받아와야 해!

                startActivity(i);
            }
        });

        Community.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailActivity.this,CommunityActivity.class);
                startActivity(i);
            }
        });


        main_name = (TextView) findViewById(R.id.detail_name);
        area = (TextView) findViewById(R.id.detail_area);
        level = (TextView) findViewById(R.id.detail_level);
        info = (TextView) findViewById(R.id.detail_Info);
        imageView = (ImageView) findViewById(R.id.detail_imageView);

        Intent intent = getIntent();
        walk_name = intent.getStringExtra("walk_name");

        String walk_level = intent.getStringExtra("walk_level");
        String walk_area = intent.getStringExtra("walk_area");

        byte[] arr = getIntent().getByteArrayExtra("walk_image");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);

        main_name.setText(walk_name);
        imageView.setImageBitmap(image);
        area.setText(walk_area);
        level.setText("난이도 " + walk_level);


        JSONObject jsonObject;
        String data = loadJSONFromAsset("json/data.json");
        int point = 0;
        String point_content ="";
        String course ="";
        String traffic_info="";
        String time ="";

        //json으로 상세정보 불러오기!
        try {
            jsonObject = new JSONObject(data);
            JSONArray Data = jsonObject.getJSONArray("DATA");
            int q=0;
            for(int i=0; i<Data.length(); i++){
                JSONObject c = Data.getJSONObject(i);
                if(c.getString("COURSE_NAME").equals(walk_name)){
                    q=1;
                    point = c.getInt("CPI_IDX");
                    course = "<코스>\n" + c.getString("DETAIL_COURSE");
                    traffic_info = "<진입로 정보>\n" + c.getString("TRAFFIC_INFO");
                    time = "<예상소요시간>\n" + c.getString("LEAD_TIME");
                    point_content += "[" + Integer.toString(point) + " 포인트]\n" + c.getString("CPI_CONTENT") + "\n\n";
                    mapX = c.getDouble("X");
                    mapY = c.getDouble("Y");
                }else if (q==1) {
                    Log.e("check","끝남");
                    break;
                }
            }

            info.setText(course + "\n\n" + traffic_info + "\n\n" + time + "\n\n<포인트별 상세정보>\n" + point_content + "\n");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mapFragment.getMapAsync(this);
    }
    //Json파일 불러오는 Method
    public String loadJSONFromAsset(String url) {
        String json = null;
        try {
            InputStream is = getAssets().open(url);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    public void onMapReady(final GoogleMap detail_map) {
        googleMap = detail_map;

//        JSONObject jsonObject;
//        String data = loadJSONFromAsset("json/data.json");
//
//        try {
//            jsonObject = new JSONObject(data);
//            JSONArray Data = jsonObject.getJSONArray("DATA");
//            for(int i=0; i<Data.length(); i++){
//                JSONObject c = Data.getJSONObject(i);
//                if(c.getString("COURSE_NAME").equals(walk_name)){
//                    mapX = c.getDouble("X");
//                    mapY = c.getDouble("Y");
//                    Log.e("Check","1");
//                    break;
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        ////////////////////////////////////////map//////////////////////////////////////////////////////
//        CameraUpdate update = CameraUpdateFactory.newLatLng(new LatLng(mapY,mapX));
//        googleMap.moveCamera(update);
//        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
//        googleMap.animateCamera(zoom);
//
//        googleMap.getUiSettings().setZoomControlsEnabled(false);
//        googleMap.getUiSettings().setZoomGesturesEnabled(false);
//        googleMap.getUiSettings().setScrollGesturesEnabled(false);
//        googleMap.getUiSettings().setAllGesturesEnabled(false);
//
//        MarkerOptions markerOptions = new MarkerOptions()
//                // 마커 위치
//                .position(new LatLng(mapY,mapX))
//                .title(walk_name);
//
//        googleMap.addMarker(markerOptions).showInfoWindow();
//        ///////////////////////////////////////map//////////////////////////////////////////////////////
    }
}
