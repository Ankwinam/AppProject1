package com.example.ankwinam.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;


/**
 * Created by axx42 on 2016-11-21.
 */

public class DetailActivity extends AppCompatActivity /*implements MapView.MapViewEventListener*/ {

    //public static String API_KEY = "563f51225e2f3bd14cd9287c3e7b067";

    TextView main_name;
    ImageView level;
    ImageView imageView;
    TextView area;
    TextView info;
    Bitmap image;
    Button Tracking;
    Button Community;

    ImageButton Icon_walk, Icon_bicycle, Icon_pet, Icon_baby;
    TextView num_walk, num_bicycle, num_pet, num_baby;
    int[] checked;
    final static String kind[] = new String[]{"WALK_LIKE", "BICYCLE_LIKE", "PET_LIKE", "BABY_LIKE"};;

    double mapX = 0;
    double mapY = 0;
    String walk_name;
    String rewalk_name;

    private String UPLOAD_URL ="https://today-walks-lee-s-h.c9users.io/update_like.php";

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    private int jjimOk = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_scrolling);

        Intent intent = getIntent();
        checked = new int[4];

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.detail_fab);

        pref = getSharedPreferences("Jjim",MODE_PRIVATE);

        Set<String> values = new HashSet<String>();
        Set<String> temp = pref.getStringSet("jjimset", new HashSet<String>());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pref = getSharedPreferences("Jjim", MODE_PRIVATE);
                Set<String> values = new HashSet<String>();
                Set<String> temp = pref.getStringSet("jjimset", new HashSet<String>());
                editor = pref.edit();
                if(jjimOk == 0) {
                    Toast.makeText(getApplicationContext(), "찜하기", Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.mipmap.favorite_btn_after);
                    for (String r : temp) {
                        values.add(r);
                    }
                    values.add(walk_name);
                    editor.putStringSet("jjimset", values);
                    editor.commit();
                    jjimOk = 1;
                    values.clear();
                }else {
                    for (String r : temp) {
                        if(r.equals(walk_name)){
                        }else {
                            values.add(r);
                        }
                    }
                    editor.putStringSet("jjimset", values);
                    editor.commit();
                    jjimOk = 0;
                    Toast.makeText(getApplicationContext(), "찜 취소", Toast.LENGTH_SHORT).show();
                    fab.setImageResource(R.mipmap.favorite_btn_before);
                    values.clear();
                }
            }
        });
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
        level = (ImageView) findViewById(R.id.detail_level);
        info = (TextView) findViewById(R.id.detail_Info);
        imageView = (ImageView) findViewById(R.id.detail_imageView);

        walk_name = intent.getStringExtra("walk_name");
        String walk_level = intent.getStringExtra("walk_level");
        String walk_area = intent.getStringExtra("walk_area");

        for(String r : temp){
            if(r.equals(walk_name)){
                fab.setImageResource(R.mipmap.favorite_btn_after);
                jjimOk = 1;
            }
        }

        //----------------------------------------------------- 추천수 기능 --------------------------------
        Icon_walk = (ImageButton) findViewById(R.id.icon_walk);
        Icon_bicycle = (ImageButton) findViewById(R.id.icon_bicycle);
        Icon_pet = (ImageButton) findViewById(R.id.icon_dog);
        Icon_baby = (ImageButton) findViewById(R.id.icon_baby);
        num_walk = (TextView) findViewById(R.id.num_walk);
        num_bicycle = (TextView) findViewById(R.id.num_bicycle);
        num_pet = (TextView) findViewById(R.id.num_pet);
        num_baby = (TextView) findViewById(R.id.num_baby);

        num_walk.setText(intent.getStringExtra("walk"));
        num_bicycle.setText(intent.getStringExtra("bicycle"));
        num_pet.setText(intent.getStringExtra("pet"));
        num_baby.setText(intent.getStringExtra("baby"));

        //해당 산책로에 대한 앱 데이터 get
        rewalk_name = walk_name.replace(" ","");
        Log.e("sumin1",walk_name);
        pref = getSharedPreferences("Like",MODE_PRIVATE);
        String current = pref.getString(rewalk_name,"");
        Log.e("sumin2",current);
        switch (current){
            case "0":
                Icon_walk.setImageResource(R.drawable.icon_walk);
                break;
            case "1":
                Icon_bicycle.setImageResource(R.drawable.icon_bicycle);
                break;
            case "2":
                Icon_pet.setImageResource(R.drawable.icon_dog);
                break;
            case "3":
                Icon_baby.setImageResource(R.drawable.icon_baby);
                break;
        }

        // --------------------------------------------------- 추천수 기능 ---------------------------------




        byte[] arr = getIntent().getByteArrayExtra("walk_image");
        image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        Bitmap resized = Bitmap.createScaledBitmap(image,1200,700,true);

        main_name.setText(walk_name);
        imageView.setImageBitmap(resized);
        area.setText(walk_area);
        switch (walk_level){
            case "1" :
                level.setImageResource();
                break;
            case "2" :
                level.setImageResource();
                break;
            case "3" :
                level.setImageResource();
                break;
        }



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
                    point_content += "[" + Integer.toString(point) + " 포인트]\n";

                    if (c.getString("CPI_CONTENT").equals("null")){
                        point_content += "정보가 없습니다." + "\n\n";
                    }else {
                        point_content += c.getString("CPI_CONTENT") + "\n\n";
                    }

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
        int i, before_i = 0;
        editor = pref.edit();
        String before = pref.getString(rewalk_name,"");

        switch (before) {
            case "0":
                before_i = Integer.parseInt(num_walk.getText().toString());
                before_i--;
                num_walk.setText(before_i + "");
                break;
            case "1":
                before_i = Integer.parseInt(num_bicycle.getText().toString());
                before_i--;
                num_bicycle.setText(before_i + "");
                break;
            case "2":
                before_i = Integer.parseInt(num_pet.getText().toString());
                before_i--;
                num_pet.setText(before_i + "");
                break;
            case "3":
                before_i = Integer.parseInt(num_baby.getText().toString());
                before_i--;
                num_baby.setText(before_i + "");
                break;
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
                editor.putString(rewalk_name,"0");
                editor.commit();
                if(before == "0" || before == "1" || before == "2" || before == "3"){
                    if(before == "0") break;
                    uploadlike("both",walk_name,kind[0],Integer.toString(i),kind[Integer.parseInt(before)],Integer.toString(before_i));
                }else {
                    uploadlike("single",walk_name,kind[0],Integer.toString(i));
                }
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
                editor.putString(rewalk_name,"1");
                editor.commit();
                if(before == "0" || before == "1" || before == "2" || before == "3"){
                    if(before == "1") break;
                    uploadlike("both",walk_name,kind[1],Integer.toString(i),kind[Integer.parseInt(before)],Integer.toString(before_i));
                }else {
                    uploadlike("single",walk_name,kind[1],Integer.toString(i));
                }
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
                editor.putString(rewalk_name,"2");
                editor.commit();
                if(before == "0" || before == "1" || before == "2" || before == "3"){
                    if(before == "2") break;
                    uploadlike("both",walk_name,kind[2],Integer.toString(i),kind[Integer.parseInt(before)],Integer.toString(before_i));
                }else {
                    uploadlike("single",walk_name,kind[2],Integer.toString(i));
                }
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
                editor.putString(rewalk_name,"3");
                editor.commit();
                if(before == "0" || before == "1" || before == "2" || before == "3"){
                    if(before == "3") break;
                    uploadlike("both",walk_name,kind[3],Integer.toString(i),kind[Integer.parseInt(before)],Integer.toString(before_i));
                }else {
                    uploadlike("single",walk_name,kind[3],Integer.toString(i));
                }
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



    private void uploadlike(final String type,final  String course,final  String select,final  String select_num,final  String before_select,final  String before_select_num){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response

                        Toast.makeText(DetailActivity.this, "추천하기 완료" + s , Toast.LENGTH_LONG).show();
                        Log.e("Check", "success");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(DetailActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put("type",type);
                params.put("course",walk_name);
                params.put("select", select);
                params.put("select_num", select_num);
                params.put("before_select",before_select);
                params.put("before_select_num",before_select_num);

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    private void uploadlike(final String type,final  String course,final  String select,final  String select_num){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response

                        Toast.makeText(DetailActivity.this, "추천하기 완료" + s , Toast.LENGTH_LONG).show();
                        Log.e("Check", "success");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(DetailActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();
                params.put("type",type);
                params.put("course",walk_name);
                params.put("select", select);
                params.put("select_num", select_num);

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}
