package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;

import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by axx42 on 2016-11-21.
 */

public class DetailActivity extends AppCompatActivity /*implements MapView.MapViewEventListener*/ {

    //public static String API_KEY = "563f51225e2f3bd14cd9287c3e7b067";

    TextView main_name;
    TextView level;
    ImageView imageView;
    TextView area;
    TextView info;
    Bitmap image;
    Button Tracking;
    Button Community;

    ImageButton Icon_walk, Icon_bicycle, Icon_pet, Icon_baby;
    TextView num_walk, num_bicycle, num_pet, num_baby;
    int[] checked;

    double mapX = 0;
    double mapY = 0;
    String walk_name;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_scrolling);

        checked = new int[4];
//        ///////////////////////////////////////맵///////////////////////////////////////
//        MapView mapView = new MapView(this);
//        mapView.setDaumMapApiKey(API_KEY);
//
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.detail_map);
//        mapViewContainer.addView(mapView);
//
//        mapView.setMapViewEventListener(this);
//        /////////////////////////////////////////////////////////////////////////////////

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
                i.putExtra("walk_name",walk_name);
                startActivity(i);
            }
        });


        main_name = (TextView) findViewById(R.id.detail_name);
        area = (TextView) findViewById(R.id.detail_area);
        level = (TextView) findViewById(R.id.detail_level);
        info = (TextView) findViewById(R.id.detail_Info);
        imageView = (ImageView) findViewById(R.id.detail_imageView);


        //----------------------------------------------------- 추천수 기능 --------------------------------
        Icon_walk = (ImageButton) findViewById(R.id.icon_walk);
        Icon_bicycle = (ImageButton) findViewById(R.id.icon_bicycle);
        Icon_pet = (ImageButton) findViewById(R.id.icon_dog);
        Icon_baby = (ImageButton) findViewById(R.id.icon_baby);
        num_walk = (TextView) findViewById(R.id.num_walk);
        num_bicycle = (TextView) findViewById(R.id.num_bicycle);
        num_pet = (TextView) findViewById(R.id.num_pet);
        num_baby = (TextView) findViewById(R.id.num_baby);


        // --------------------------------------------------- 추천수 기능 ---------------------------------

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
    }
    public void onClickIcon(View v){
        int i;
        //디비에서 일치하는 유저-산책로 컬럼잇는지 확인
        pref = getSharedPreferences("like",MODE_PRIVATE);
        if(pref.getString("initial","").equals("true")){
            Log.e("TT","처음");
//            editor = pref.edit();
//            editor.putString("initial","true");
//            editor.commit();
            //DB 유저-산책로 디비 생성
        }else {
            Log.e("TT","처음 아님");
        }

        int before=0;
        for(int j=0; j<4; j++){
            if(checked[j]==1){
                before=j;
                checked[before] = 0;
                switch (before){
                    case 0:
                        i = Integer.parseInt(num_walk.getText().toString());
                        i--;
                        num_walk.setText(i+"");
                        Log.e("sumin4",before+"");
                        break;
                    case 1:
                        i = Integer.parseInt(num_bicycle.getText().toString());
                        i--;
                        num_bicycle.setText(i+"");
                        Log.e("sumin4",before+"");
                        break;
                    case 2:
                        i = Integer.parseInt(num_pet.getText().toString());
                        i--;
                        num_pet.setText(i+"");
                        Log.e("sumin4",before+"");
                        break;
                    case 3:
                        i = Integer.parseInt(num_baby.getText().toString());
                        i--;
                        num_baby.setText(i+"");
                        Log.e("sumin4",before+"");
                        break;
                }
                break;
            }
        }

        switch (v.getId()) {
            case R.id.icon_walk:
                Icon_walk.setImageResource(R.drawable.icon_walk);
                Icon_bicycle.setImageResource(R.drawable.icon_before_bicycle);
                Icon_pet.setImageResource(R.drawable.icon_before_dog);
                Icon_baby.setImageResource(R.drawable.icon_before_baby);
                i = Integer.parseInt(num_walk.getText().toString());
                i++;
                num_walk.setText(i+"");
                checked[0] = 1;
                break;
            case R.id.icon_bicycle:
                Icon_walk.setImageResource(R.drawable.icon_before_walk);
                Icon_bicycle.setImageResource(R.drawable.icon_bicycle);
                Icon_pet.setImageResource(R.drawable.icon_before_dog);
                Icon_baby.setImageResource(R.drawable.icon_before_baby);
                i = Integer.parseInt(num_bicycle.getText().toString());
                i++;
                num_bicycle.setText(i+"");
                checked[1] = 1;
                break;
            case R.id.icon_dog:
                Icon_walk.setImageResource(R.drawable.icon_before_walk);
                Icon_bicycle.setImageResource(R.drawable.icon_before_bicycle);
                Icon_pet.setImageResource(R.drawable.icon_dog);
                Icon_baby.setImageResource(R.drawable.icon_before_baby);
                i = Integer.parseInt(num_pet.getText().toString());
                i++;
                num_pet.setText(i+"");
                checked[2] = 1;
                break;
            case R.id.icon_baby:
                Icon_walk.setImageResource(R.drawable.icon_before_walk);
                Icon_bicycle.setImageResource(R.drawable.icon_before_bicycle);
                Icon_pet.setImageResource(R.drawable.icon_before_dog);
                Icon_baby.setImageResource(R.drawable.icon_baby);
                i = Integer.parseInt(num_baby.getText().toString());
                i++;
                num_baby.setText(i+"");
                checked[3] = 1;
                break;
        }

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



//    @Override
//    public void onMapViewInitialized(MapView mapView) {
////        //지도 이동
////        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(mapX, mapY);
////        mapView.setMapCenterPoint(mapPoint, true);
////
////        //마커 생성
////        MapPOIItem marker = new MapPOIItem();
////        marker.setItemName("Default Marker");
////        marker.setTag(0);
////        marker.setMapPoint(mapPoint);
////        marker.setMarkerType(MapPOIItem.MarkerType.BluePin);  //기본으로 제공하는 BluePin 마커 모양
////        marker.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); //마커를 클릭했을때 기본으로 제공하는 RedPin 마커 모양
////
////        //마커 추가
////        mapView.addPOIItem(marker);
//    }
//
//    @Override
//    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewZoomLevelChanged(MapView mapView, int i) {
//
//    }
//
//    @Override
//    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {
//
//    }
//
//    @Override
//    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {
//
//    }
}
