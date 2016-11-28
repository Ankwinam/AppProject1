package com.example.ankwinam.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by An Kwi nam on 2016-09-08.
 */
public class CommunityWriteActivity extends AppCompatActivity {

    private Button write_finish;
    private EditText write_title;
    private EditText write_content;
    private ImageButton imageButton;

    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private SharedPreferences pref;

    private String UPLOAD_URL ="https://today-walks-lee-s-h.c9users.io/board_write.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_wirte);

        write_finish = (Button)findViewById(R.id.write_upload);
        write_title = (EditText) findViewById(R.id.write_title);
        write_content = (EditText) findViewById(R.id.write_content);
        imageButton = (ImageButton)findViewById(R.id.write_img);
        pref = getSharedPreferences("auto_login",MODE_PRIVATE);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        write_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(write_title.getText().toString().isEmpty()){
                    Toast.makeText(CommunityWriteActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(write_content.getText().toString().isEmpty()){
                    Toast.makeText(CommunityWriteActivity.this, "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    uploadImage();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                Bitmap resized = Bitmap.createScaledBitmap(bitmap, 1024, 2048, true);
                bitmap = resized;
                //Setting the Bitmap to ImageView
                imageButton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

                        Toast.makeText(CommunityWriteActivity.this, "업로드 완료" , Toast.LENGTH_LONG).show();
                        Log.e("Check", "success");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(CommunityWriteActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String title = write_title.getText().toString().trim();
                String content = write_content.getText().toString().trim();
                String email = pref.getString("email","");
                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put("image", image);
                params.put("title", title);
                params.put("content",content);
                params.put("email",email);
                params.put("course","test");

                //returning parameters
                return params;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
}