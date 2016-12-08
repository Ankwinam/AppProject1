package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Created by axx42 on 2016-12-07.
 */

public class CommentActivity extends AppCompatActivity {
    TextView Id;
    TextView Date;
    TextView Contents;
    ListView list;

    JSONArray comments = null;
    String myJSON;
    static final String BASE_URL="https://today-walks-lee-s-h.c9users.io";
    private static final String TAG_RESULTS="result";
    ArrayList<Comment_item> h_info_list;
    String walk_name;
    private SharedPreferences pref;
    private String id;
    private String date;
    private String content;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_main);

        pref = getSharedPreferences("auto_login",MODE_PRIVATE);

        Id = (TextView) findViewById(R.id.comment_id);
        Date = (TextView) findViewById(R.id.comment_time);
        Contents = (TextView) findViewById(R.id.comment_content);
        list = (ListView) findViewById(R.id.comment_Listview);

        Intent i = getIntent();
        id = i.getStringExtra("id");
        date = i.getStringExtra("date");
        content = i.getStringExtra("content");
        walk_name = i.getStringExtra("walk_name");
        Id.setText("ID : "+ id);
        Date.setText("날짜 : " + date);
        Contents.setText(content);

        //글쓰기 버튼 이벤트
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.comment_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent i = new Intent(CommentActivity.this, CommentWriteActivity.class);
                i.putExtra("walk_name",walk_name);
                i.putExtra("id",id);
                i.putExtra("date",date);
                i.putExtra("content",content);
                startActivity(i);
            }
        });

        String walk_parm = "";
        try {
            walk_parm = java.net.URLEncoder.encode(new String(walk_name.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        getData(BASE_URL+"/comment.php?walk="+walk_parm);
    }

    //리스트 뷰
    protected void showList(){
        try {
            Log.e("Check",myJSON);
            JSONObject jsonObj = new JSONObject(myJSON);
            comments = jsonObj.getJSONArray(TAG_RESULTS);
            h_info_list = new ArrayList<Comment_item>();

            for(int i=0;i<comments.length();i++){
                JSONObject c = comments.getJSONObject(i);
                String id = c.getString("email");
                String date = c.getString("date");
                String contents = c.getString("content");
                Comment_item data = new Comment_item(date, id, contents);
                h_info_list.add(data);
            }

            list.setAdapter(new CommentAdapter(getApplicationContext(),R.layout.comment_info,h_info_list));

//            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class); // 다음넘어갈 화면
//                    Comment_item data = h_info_list.get(position);
//                    intent.putExtra("walk_name",data.walk_name);
//                    intent.putExtra("walk_level",data.level);
//                    intent.putExtra("walk_area",data.area);
//
//                    Bitmap sendBitmap = data.image;
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//
//                    intent.putExtra("walk_image",byteArray);
//
//                    startActivity(intent);
//                }
//            });
        } catch (JSONException e) {
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
