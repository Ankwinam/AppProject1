package com.example.ankwinam.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

/**
 * Created by An Kwi nam on 2016-09-08.
 */
public class CommunityWriteActivity extends AppCompatActivity {

    private Button write_finish;
    private EditText write_title;
    private EditText write_content;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_wirte);

        write_finish = (Button)findViewById(R.id.write_upload);
        write_title = (EditText) findViewById(R.id.write_title);
        write_content = (EditText) findViewById(R.id.write_content);
        imageButton = (ImageButton)findViewById(R.id.write_img);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityWriteActivity.this, "사진 업로드", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(CommunityWriteActivity.this, "작성 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });


    }
}