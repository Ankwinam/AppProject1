package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class Tema_NaviActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String[] Tema;
    static final String BASE_URL="https://today-walks-lee-s-h.c9users.io";

    String myJSON;
    private static final String TAG_RESULTS="result";
    ArrayList<Walk_Info> h_info_list;

    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;

    private ListView list;
    WalkListAdapter myadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tema_navigation);

        Tema = new String[]{"걷기 좋은 길","자전거 타기 좋은 길","유아동반 좋은 길","애완동물 좋은 길"};

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //테마별 스크롤 스피너 매핑 부분
        String[] temaspnnier = getResources().getStringArray(R.array.Temaspnnier);
        ArrayAdapter<String> tema_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, temaspnnier);
        Spinner spnnier = (Spinner)findViewById(R.id.tema_spinner);
        spnnier.setAdapter(tema_adapter);

        spnnier.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        //한글 parameter 인코딩
                        String gu_parmeter = "";
                        try {
                            gu_parmeter = java.net.URLEncoder.encode(new String(Tema[position].getBytes("UTF-8")));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        //get ListView
                        getData(BASE_URL+"/list_index2.php?Tema=" + gu_parmeter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                }
        );



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    //수정해야할 print 함수 부분
    public void print(View v, int position){
        Spinner sp = (Spinner)findViewById(R.id.tema_spinner);
        String res = "";
        if(sp.getSelectedItemPosition()>0){
            res=(String)sp.getAdapter().getItem(sp.getSelectedItemPosition());
        }
        if(res!=""){
            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Handle the camera action
        if (id == R.id.menu_home) {
            Toast.makeText(getApplicationContext(), "홈", Toast.LENGTH_SHORT).show();
            Intent go_home = new Intent(Tema_NaviActivity.this, Choice_NaviActivity.class);
            startActivity(go_home);
            finish();
        } else if (id == R.id.menu_local) {
            Toast.makeText(getApplicationContext(), "지역 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_local = new Intent(Tema_NaviActivity.this, Local_NaviActivity.class);
            startActivity(go_local);
            finish();
        } else if (id == R.id.menu_tema) {
            Toast.makeText(getApplicationContext(), "테마 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_tema = new Intent(Tema_NaviActivity.this, Tema_NaviActivity.class);
            startActivity(go_tema);
            finish();
        } else if (id == R.id.menu_history) {
            Toast.makeText(getApplicationContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show();
            Intent go_his = new Intent(Tema_NaviActivity.this, CommunityHistoryActivity.class);
            startActivity(go_his);
        } else if (id == R.id.menu_stamp) {
            Toast.makeText(getApplicationContext(), "스탬프", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_jjim) {
            Toast.makeText(getApplicationContext(),"찜 한 산책로",Toast.LENGTH_SHORT).show();
            Intent go_jjim = new Intent(Tema_NaviActivity.this, JJim_NaviActivity.class);
            startActivity(go_jjim);
            finish();
        } else if (id == R.id.menu_logout) {
            SharedPreferences pref = getSharedPreferences("auto_login",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putString("auto","false");
            editor.commit();

            Intent go_main = new Intent(Tema_NaviActivity.this, MainActivity.class);
            startActivity(go_main);
            finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //리스트 뷰
    protected void showList(){
        try {
            list = (ListView) findViewById(R.id.listView_tema);
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            h_info_list = new ArrayList<Walk_Info>();

            for(int i=0;i<15;i++){
                JSONObject c = peoples.getJSONObject(i);
                String id = c.getString("walk_name");
                String name = "자치구" + c.getString("area");
                String address = c.getString("level")+"";
                String walk = c.getString("walk");
                String bicycle = c.getString("bicycle");
                String pet = c.getString("pet");
                String baby = c.getString("baby");
                String image_url = URLEncoder.encode(id,"UTF-8");
                image_url = image_url.replace("+","%20");
                String imgUrl = BASE_URL+ "/walks/" + image_url + ".jpg";

                Walk_Info data = new Walk_Info(id, name, address, imgUrl, walk, bicycle, pet, baby);
                h_info_list.add(data);
            }
            myadapter = new WalkListAdapter(getApplicationContext(),R.layout.tema_info, h_info_list);
            list.setAdapter(myadapter);

            int i = 0;
            for(Walk_Info each : h_info_list){
                if(i>15) break; //상위 20개만 load
                i++;
                each.loadImage(myadapter);
            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class); // 다음넘어갈 화면
                    Walk_Info data = h_info_list.get(position);
                    if (data.getImage() != null) {
                        intent.putExtra("walk_name", data.walk_name);
                        intent.putExtra("walk_level", data.level);
                        intent.putExtra("walk_area", data.area);
                        intent.putExtra("walk", data.walk);
                        intent.putExtra("bicycle", data.bicycle);
                        intent.putExtra("baby", data.baby);
                        intent.putExtra("pet", data.pet);

                        Bitmap sendBitmap = data.image;
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                        byte[] byteArray = stream.toByteArray();

                        intent.putExtra("walk_image", byteArray);

                        startActivity(intent);
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void getData(String url){
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result){
                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}
