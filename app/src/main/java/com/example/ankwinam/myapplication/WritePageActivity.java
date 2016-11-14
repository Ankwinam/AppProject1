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
public class WritePageActivity extends AppCompatActivity {

    private Button write_finish;
    private Button write_cancel;
    private EditText write_title;
    private EditText road_name;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.page_write);

        write_finish = (Button)findViewById(R.id.write_finish);
        write_cancel = (Button)findViewById(R.id.write_cancel);
        write_title = (EditText) findViewById(R.id.write_title);
        road_name = (EditText) findViewById(R.id.road_name);
        imageButton = (ImageButton)findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WritePageActivity.this, "사진 업로드", Toast.LENGTH_SHORT).show();
            }
        });

        write_finish.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(write_title.getText().toString().isEmpty()){
                    Toast.makeText(WritePageActivity.this, "제목을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else if(road_name.getText().toString().isEmpty()){
                    Toast.makeText(WritePageActivity.this, "산책로를 입력하세요", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(WritePageActivity.this, "작성 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        write_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}