package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class JJim_NaviActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    static final String BASE_URL="https://today-walks-lee-s-h.c9users.io";
    String myJSON;
    private static final String TAG_RESULTS="result";
    ArrayList<Walk_Info> h_info_list;

    JSONArray peoples = null;

    private SharedPreferences pref;
    Set<String> values ;
    private ListView list;
    WalkListAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jjim_navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pref = getSharedPreferences("Jjim",MODE_PRIVATE);
        values = pref.getStringSet("jjimset", new HashSet<String>());


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getData(BASE_URL+"/jjim_list.php");
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
            Intent go_home = new Intent(JJim_NaviActivity.this, Choice_NaviActivity.class);
            startActivity(go_home);
            finish();
        } else if (id == R.id.menu_local) {
            Toast.makeText(getApplicationContext(), "지역 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_local = new Intent(JJim_NaviActivity.this, Local_NaviActivity.class);
            startActivity(go_local);
            finish();
        } else if (id == R.id.menu_tema) {
            Toast.makeText(getApplicationContext(), "테마 별 이동", Toast.LENGTH_SHORT).show();
            Intent go_tema = new Intent(JJim_NaviActivity.this, Tema_NaviActivity.class);
            startActivity(go_tema);
            finish();
        } else if (id == R.id.menu_history) {
            Toast.makeText(getApplicationContext(), "내가 쓴 글", Toast.LENGTH_SHORT).show();
            Intent go_his = new Intent(JJim_NaviActivity.this, CommunityHistoryActivity.class);
            startActivity(go_his);
        } else if (id == R.id.menu_stamp) {
            Toast.makeText(getApplicationContext(), "스탬프", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.menu_jjim) {
            Toast.makeText(getApplicationContext(),"찜 한 산책로",Toast.LENGTH_SHORT).show();
            Intent go_jjim = new Intent(JJim_NaviActivity.this, JJim_NaviActivity.class);
            startActivity(go_jjim);
            finish();
        } else if (id == R.id.menu_logout) {
            SharedPreferences pref = getSharedPreferences("auto_login",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.putString("auto","false");
            editor.commit();

            Intent go_main = new Intent(JJim_NaviActivity.this, MainActivity.class);
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
            list = (ListView) findViewById(R.id.listView_jjim);
            JSONObject jsonObj = new JSONObject(myJSON);
            peoples = jsonObj.getJSONArray(TAG_RESULTS);
            h_info_list = new ArrayList<Walk_Info>();

            for(int i=0;i<peoples.length();i++){
                JSONObject c = peoples.getJSONObject(i);
                for(String walks : values){
                    String id = c.getString("walk_name");
                    Log.e("id",id);
                    Log.e("walks",walks);
                    if(id.equals(walks)) {
                        Log.e("Check123","들어옴");
                        String name = "자치구" + c.getString("area");
                        String address = c.getString("level") + "";
                        String walk = c.getString("walk");
                        String bicycle = c.getString("bicycle");
                        String pet = c.getString("pet");
                        String baby = c.getString("baby");
                        String image_url = URLEncoder.encode(id, "UTF-8");
                        image_url = image_url.replace("+", "%20");
                        String imgUrl = BASE_URL + "/walks/" + image_url + ".jpg";

                        Walk_Info data = new Walk_Info(id, name, address, imgUrl, walk, bicycle, pet, baby);
                        h_info_list.add(data);
                        break;
                    }
                }
            }
            myadapter = new WalkListAdapter(getApplicationContext(),R.layout.tema_info, h_info_list);
            list.setAdapter(myadapter);


            for(Walk_Info each : h_info_list){
                each.loadImage(myadapter);
            }

            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class); // 다음넘어갈 화면
                    Walk_Info data = h_info_list.get(position);
                    intent.putExtra("walk_name",data.walk_name);
                    intent.putExtra("walk_level",data.level);
                    intent.putExtra("walk_area",data.area);
                    intent.putExtra("walk",data.walk);
                    intent.putExtra("bicycle",data.bicycle);
                    intent.putExtra("baby",data.baby);
                    intent.putExtra("pet",data.pet);

                    Bitmap sendBitmap = data.image;
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();

                    intent.putExtra("walk_image",byteArray);

                    startActivity(intent);
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
