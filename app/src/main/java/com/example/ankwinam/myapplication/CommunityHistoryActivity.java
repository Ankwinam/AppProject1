package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by axx42 on 2016-12-12.
 */

public class CommunityHistoryActivity extends AppCompatActivity {
    String myJSON;
    static final String BASE_URL="https://today-walks-lee-s-h.c9users.io";
    private static final String TAG_RESULTS="result";
    JSONArray boards = null;
    ArrayList<Community_item> info_list;
    RecyclerView recyclerView;
    String walk_name;
    private SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_history);

        recyclerView=(RecyclerView)findViewById(R.id.history_recyclerview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        pref = getSharedPreferences("auto_login",MODE_PRIVATE);
        String id = pref.getString("email","");

        String id_parm = "";
        try {
            id_parm = java.net.URLEncoder.encode(new String(id.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        getData(BASE_URL+"/board_history.php?id=" + id_parm);
    }




    protected void showList(){
        try {
            Log.e("Check",myJSON);
            JSONObject jsonObj = new JSONObject(myJSON);
            boards = jsonObj.getJSONArray(TAG_RESULTS);
            info_list = new ArrayList<Community_item>();

            for(int i = 0; i< boards.length(); i++){
                JSONObject c = boards.getJSONObject(i);
                String id = c.getString("title");
                String url = c.getString("img");
                String image_url = URLEncoder.encode(url,"UTF-8");
                image_url = image_url.replace("+","%20").replace("%3A",":").replace("%2F","/");
                String content = c.getString("contents");
                String date = c.getString("created_at");
                String email = c.getString("id");
                int comment_num = c.getInt("comment_num");
                walk_name = c.getString("course_name");

                Community_item data = new Community_item(image_url,id,walk_name,content,date,email,comment_num);

                info_list.add(data);
            }
            recyclerView.setAdapter(new CommunityAdapter(getApplicationContext(),info_list,R.layout.community_item));

            for(Community_item each : info_list){
                each.loadImage(recyclerView);
            }

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
