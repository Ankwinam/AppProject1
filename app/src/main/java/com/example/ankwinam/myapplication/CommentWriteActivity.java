package com.example.ankwinam.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by axx42 on 2016-12-08.
 */

public class CommentWriteActivity  extends AppCompatActivity{

    private Button write_finish;
    private EditText write_title;
    private EditText write_content;
    private ImageButton imageButton;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private SharedPreferences pref;

    private String UPLOAD_URL ="https://today-walks-lee-s-h.c9users.io/comment_write.php";
    private String walk_name;
    private String id;
    private String date;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_write);

        write_finish = (Button) findViewById(R.id.comment_write_upload);
        write_content = (EditText) findViewById(R.id.comment_write_content);
        pref = getSharedPreferences("auto_login",MODE_PRIVATE);

        Intent i = getIntent();
        walk_name = i.getStringExtra("walk_name");
        id = i.getStringExtra("id");
        date = i.getStringExtra("date");
        content = i.getStringExtra("content");

        write_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(write_content.getText().toString().isEmpty()){
                    Toast.makeText(CommentWriteActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImage();
                }
            }
        });
    }

    private void uploadImage(){
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response

                        Toast.makeText(CommentWriteActivity.this, "업로드 완료" , Toast.LENGTH_LONG).show();
                        Log.e("Check", "success");
                        finish();
                        Intent i = new Intent(CommentWriteActivity.this,CommentActivity.class);
                        i.putExtra("walk_name",walk_name);
                        i.putExtra("date",date);
                        i.putExtra("content",content);
                        i.putExtra("id",id);
                        startActivity(i);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CommentWriteActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                //Getting Image Name
                String content = write_content.getText().toString().trim();
                String email = pref.getString("email","");
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                params.put("content",content);
                params.put("email",email);
                params.put("course",walk_name);

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
