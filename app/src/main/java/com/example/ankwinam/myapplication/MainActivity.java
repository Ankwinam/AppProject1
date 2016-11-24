package com.example.ankwinam.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText email_Input;
    private EditText password_Input;
//    private CheckBox login_auto;
//    private Boolean loginChecked;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    String URL= "https://today-walks-lee-s-h.c9users.io/index.php";
    JSONParser jsonParser=new JSONParser();
    int successResult = -1;
    String messageResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email_Input = (EditText)findViewById(R.id.main_emailinput);
        password_Input = (EditText)findViewById(R.id.main_pwinput);
//        login_auto = (CheckBox)findViewById(R.id.main_checkBox);
        pref = getSharedPreferences("auto_login",MODE_PRIVATE);

        //자동로그인
        if(pref.getString("auto","").equals("true")){
            AttemptLogin attemptLogin= new AttemptLogin();
            attemptLogin.execute(pref.getString("email",""),pref.getString("pw",""),"");
            Log.e("auto",pref.toString());
            while (successResult < 0) {
            }

            if(successResult == 1) {
                Intent login = new Intent(MainActivity.this, Choice_NaviActivity.class);
                startActivity(login);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
            }
        }

        //회원가입 버튼 Intent
        Button btn_signup = (Button)findViewById(R.id.write_finish);
        btn_signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent signup = new Intent(MainActivity.this, SignupActivity.class);
                startActivityForResult(signup,1000);
            }
        });
        //로그인 버튼 Intent
        Button btn_login = (Button)findViewById(R.id.login_btn);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AttemptLogin attemptLogin= new AttemptLogin();
                attemptLogin.execute(email_Input.getText().toString(),password_Input.getText().toString(),"");


                while (successResult < 0) {
                }

                if(successResult == 1) {
                    editor = pref.edit();
                    editor.putString("email",email_Input.getText().toString());
                    editor.putString("pw",password_Input.getText().toString());
                    editor.putString("auto","true");
                    editor.commit();
                    Intent login = new Intent(MainActivity.this, Choice_NaviActivity.class);
                    startActivity(login);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        // checkBoxListener 셋팅
//        login_auto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked)
//                    loginChecked = true;
//                else {
//                    // 체크되지 않았다면 다 지워라
//                    loginChecked = false;
//                    editor.clear();
//                    editor.commit();
//                }
//            }
//        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // setResult를 통해 받아온 요청번호, 상태, 데이터
        Log.d("RESULT", requestCode + "");
        Log.d("RESULT", resultCode + "");
        Log.d("RESULT", data + "");

        if(requestCode == 1000 && resultCode == RESULT_OK){
            Toast.makeText(MainActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();
            email_Input.setText(data.getStringExtra("EMAIL"));
        }

    }

    private class AttemptLogin extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] args) {
            String name = args[2].toString();
            String password = args[1].toString();
            String email = args[0].toString();

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("password", password));
            if (name.length() > 0)
                params.add(new BasicNameValuePair("name", name));
            Log.e("Check",params.toString());

            JSONObject result = jsonParser.makeHttpRequest(URL, "POST", params);

            try {
                if (result != null) {
                    successResult = result.getInt("success");
                    messageResult = result.getString("message");
                } else {
                    messageResult = "json 생성오류";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


}
